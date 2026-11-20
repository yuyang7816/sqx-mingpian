'use strict'

const fs = require('fs')
const path = require('path')
const rimraf = require('rimraf')
const { spawnSync } = require('child_process')

const config = require('../config')

const distPath = path.resolve(__dirname, '../dist')
const env = process.env.npm_config_qa ? 'qa' : process.env.npm_config_uat ? 'uat' : 'prod'

function createVersion () {
  const d = new Date()
  const yy = d.getFullYear().toString().slice(2)
  const MM = String(d.getMonth() + 1).padStart(2, '0')
  const DD = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${yy}${MM}${DD}${hh}${mm}`
}

function replaceAll (filePath, replacer) {
  const content = fs.readFileSync(filePath, 'utf8')
  fs.writeFileSync(filePath, replacer(content), 'utf8')
}

function concatFiles (filePaths, targetPath) {
  const content = filePaths.map(filePath => fs.readFileSync(filePath, 'utf8')).join('\n')
  fs.mkdirSync(path.dirname(targetPath), { recursive: true })
  fs.writeFileSync(targetPath, content, 'utf8')
}

function getEntrypointJsAssets () {
  const statsPath = path.join(distPath, 'build-stats.json')
  const buildLogPath = path.resolve(__dirname, '../build.log')

  if (fs.existsSync(statsPath)) {
    const stats = JSON.parse(fs.readFileSync(statsPath, 'utf8'))
    const assets = (((stats.entrypoints || {}).app || {}).assets || [])
      .map(asset => typeof asset === 'string' ? asset : asset.name)
      .filter(asset => asset && /\.js($|\?)/.test(asset))

    if (assets.length) {
      return assets
    }
  }

  if (fs.existsSync(buildLogPath)) {
    const buildLog = fs.readFileSync(buildLogPath, 'utf8')
    const entrypointLine = buildLog.split(/\r?\n/).find(line => line.includes('Entrypoint') && line.includes('static/js/manifest.js'))

    if (entrypointLine) {
      const matches = entrypointLine.match(/static\/js\/[^\s]+\.js/g)
      if (matches && matches.length) {
        return matches
      }
    }
  }

  return [
    'static/js/manifest.js',
    'static/js/vendor.js',
    'static/js/app.js'
  ]
}

function getEntrypointCssAssets () {
  const statsPath = path.join(distPath, 'build-stats.json')

  if (fs.existsSync(statsPath)) {
    const stats = JSON.parse(fs.readFileSync(statsPath, 'utf8'))
    const assets = (((stats.entrypoints || {}).app || {}).assets || [])
      .map(asset => typeof asset === 'string' ? asset : asset.name)
      .filter(asset => asset && /\.css($|\?)/.test(asset) && !/manifest\.css/.test(asset))

    if (assets.length) {
      return assets
    }
  }

  return [
    'static/css/vendor.css',
    'static/css/app.css'
  ]
}

function replaceRuntimeJsList (filePath, entryJsAssets) {
  const jsLines = entryJsAssets
    .map(asset => `      window.SITE_CONFIG.cdnUrl + '/${asset}',`)
    .join('\n')

  replaceAll(filePath, content => content.replace(
    /      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/js\/manifest\.js',[\s\S]*?      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/js\/app\.js'/,
    jsLines.replace(/,$/, '')
  ))
}

function replaceRuntimeCssList (filePath, entryCssAssets) {
  const cssLines = entryCssAssets
    .map(asset => `      window.SITE_CONFIG.cdnUrl + '/${asset}',`)
    .join('\n')

  replaceAll(filePath, content => content.replace(
    /      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/css\/[^']+',[\s\S]*?      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/css\/app\.css'/,
    cssLines.replace(/,$/, '')
  ))
}

function removePath (targetPath) {
  rimraf.sync(targetPath)
}

function copyDirectory (sourcePath, targetPath) {
  const stat = fs.statSync(sourcePath)
  if (stat.isDirectory()) {
    fs.mkdirSync(targetPath, { recursive: true })
    for (const entry of fs.readdirSync(sourcePath)) {
      copyDirectory(path.join(sourcePath, entry), path.join(targetPath, entry))
    }
    return
  }
  fs.mkdirSync(path.dirname(targetPath), { recursive: true })
  fs.copyFileSync(sourcePath, targetPath)
}

function runWebpackBuild () {
  const result = spawnSync(process.execPath, [path.resolve(__dirname, 'build.js')], {
    stdio: 'inherit',
    env: process.env
  })

  if (result.status !== 0) {
    process.exit(result.status || 1)
  }
}

function runPostBuild () {
  const version = createVersion()
  const versionPath = path.join(distPath, version)
  const staticPath = path.join(distPath, 'static')
  const sourceStaticPath = path.resolve(__dirname, '../static')
  const versionStaticPath = path.join(versionPath, 'static')
  const manifestPath = path.join(versionStaticPath, 'js', 'manifest.js')
  const versionConfigPath = path.join(versionStaticPath, 'config', `index-${env}.js`)
  const initConfigPath = path.join(versionStaticPath, 'config', 'init.js')
  const outputConfigPath = path.join(distPath, 'config', 'index.js')
  const entryJsAssets = getEntrypointJsAssets()
  const entryCssAssets = getEntrypointCssAssets()

  removePath(versionPath)
  copyDirectory(sourceStaticPath, staticPath)
  copyDirectory(staticPath, versionStaticPath)

  replaceAll(manifestPath, content => content.replace(new RegExp(`"${config.build.assetsPublicPath}"`, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"'))
  replaceAll(versionConfigPath, content => content.replace(/window\.SITE_CONFIG\['version'\] = '.*'/g, `window.SITE_CONFIG['version'] = '${version}'`))

  concatFiles([versionConfigPath, initConfigPath], outputConfigPath)
  replaceRuntimeJsList(outputConfigPath, entryJsAssets)
  replaceRuntimeCssList(outputConfigPath, entryCssAssets)

  removePath(staticPath)
  removePath(path.join(versionStaticPath, 'config'))

  process.stdout.write(`\nRelease output: dist/${version}\n`)
  process.stdout.write(`Runtime config: dist/config/index.js\n`)
}

if (process.argv.includes('--post-only')) {
  runPostBuild()
} else if (process.argv.includes('--build-only')) {
  runWebpackBuild()
} else {
  runWebpackBuild()
  runPostBuild()
}
