var gulp = require('gulp');
var concat = require('gulp-concat');
var replace = require('gulp-replace');
var path = require('path');
var fs = require('fs');
var exec = require('child_process').exec;

var distPath = path.resolve('./dist');
var version = ''; // 版本号
var versionPath = ''; // 版本号路径
var env = ''; // 运行环境

// 创建版本号(年月日时分)
// (function () {
//   var d = new Date();
//   var yy = d.getFullYear().toString().slice(2);
//   var MM = d.getMonth() + 1 >= 10 ? (d.getMonth() + 1) : '0' + (d.getMonth() + 1);
//   var DD = d.getDate() >= 10 ? d.getDate() : '0' + d.getDate();
//   var h  = d.getHours() >= 10 ? d.getHours() : '0' + d.getHours();
//   var mm = d.getMinutes() >= 10 ? d.getMinutes() : '0' + d.getMinutes();
//   version = yy + MM + DD + h + mm;
//   versionPath = distPath + '/' + version;
// })();
(function () {
  version = 'vue';
  versionPath = distPath + '/' + version;
})();

function removePath(targetPath) {
  return fs.promises.rm(targetPath, { recursive: true, force: true });
}

// 编译
function buildTask(done) {
  var child = exec('node build/build.js', function (error, stdout, stderr) {
    if (stdout) {
      process.stdout.write(stdout);
    }
    if (stderr) {
      process.stderr.write(stderr);
    }
    done(error || null);
  });

  child.on('error', done);
}

// 创建版本号目录
function createVersionCatalog() {
  return gulp.src(`${distPath}/static/**/*`)
    .pipe(gulp.dest(`${versionPath}/static/`))
}

// 替换${versionPath}/static/js/manifest.js window.SITE_CONFIG.cdnUrl占位变量
function replaceCdnUrl() {
  return gulp.src(`${versionPath}/static/js/manifest.js`)
    .pipe(replace(new RegExp(`"${require('./config').build.assetsPublicPath}"`, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"'))
    .pipe(gulp.dest(`${versionPath}/static/js/`))
}

// 替换${versionPath}/static/config/index-${env}.js window.SITE_CONFIG['version']配置变量
function replaceVersion() {
  return gulp.src(`${versionPath}/static/config/index-${env}.js`)
    .pipe(replace(/window.SITE_CONFIG\['version'\] = '.*'/g, `window.SITE_CONFIG['version'] = '${version}'`))
    .pipe(gulp.dest(`${versionPath}/static/config/`))
}

// 合并${versionPath}/static/config/[index-${env}, init].js 至 ${distPath}/config/index.js
function concatConfig() {
  return gulp.src([`${versionPath}/static/config/index-${env}.js`, `${versionPath}/static/config/init.js`])
    .pipe(concat('index.js'))
    .pipe(gulp.dest(`${distPath}/config/`))
}

// 清空
function clean() {
  return removePath(versionPath)
}

function setEnv(done) {
  // 获取环境配置
  env = process.env.npm_config_qa ? 'qa' : process.env.npm_config_uat ? 'uat' : 'prod'
  done()
}

function cleanupGenerated() {
  return Promise.all([
    removePath(`${distPath}/static`),
    removePath(`${versionPath}/static/config`)
  ])
}

var defaultTask = gulp.series(
  clean,
  setEnv,
  buildTask,
  createVersionCatalog,
  gulp.parallel(replaceCdnUrl, replaceVersion),
  concatConfig,
  cleanupGenerated
);

exports.build = buildTask;
exports.clean = clean;
exports.default = defaultTask;
