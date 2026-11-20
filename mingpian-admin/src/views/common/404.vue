<template>
  <div class="site-wrapper site-page--not-found nf-not-found-page">
    <div class="site-content__wrapper">
      <div class="site-content">
        <div class="nf-not-found-wrap">
          <div class="nf-page-hero">
            <span class="nf-page-hero__badge">系统提示</span>
            <h2 class="nf-page-hero__title">页面未找到</h2>
            <p class="nf-page-hero__desc">您访问的地址不存在或已被移除，请检查链接或返回首页继续操作</p>
          </div>

          <div class="nf-table-card nf-not-found-card">
            <div class="nf-not-found-card__code">404</div>
            <p class="nf-not-found-card__desc">抱歉！您访问的页面<em>失联</em>啦 ...</p>
            <div class="nf-not-found-card__actions">
              <el-button @click="$router.go(-1)">返回上一页</el-button>
              <el-button type="primary" @click="$router.push({ name: 'home' })">进入首页</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
  }
</script>

<style lang="scss" scoped>
  .nf-not-found-page {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    background: var(--nf-page-bg, #f4f6fa);
  }

  .nf-not-found-page .site-content__wrapper {
    padding: 0;
    margin: 0;
    min-height: 100%;
    background: var(--nf-page-bg, #f4f6fa);
  }

  .nf-not-found-page .site-content {
    position: relative;
    top: auto;
    left: auto;
    transform: none;
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 16px;
    box-sizing: border-box;
  }

  .nf-not-found-wrap {
    width: 100%;
    max-width: 560px;
  }

  .nf-not-found-page .nf-page-hero {
    margin-bottom: 12px;
  }

  .nf-not-found-card {
    margin-bottom: 0;
    padding: 32px 24px 28px;
    text-align: center;
  }

  .nf-not-found-card__code {
    margin: 0 0 12px;
    font-size: 72px;
    font-weight: 600;
    line-height: 1;
    color: var(--nf-brand, #2f3968);
    letter-spacing: 2px;
  }

  .nf-not-found-card__desc {
    margin: 0 0 24px;
    font-size: 16px;
    line-height: 1.6;
    color: var(--nf-text-secondary, #909399);
  }

  .nf-not-found-card__desc > em {
    font-style: normal;
    color: var(--nf-brand, #2f3968);
    font-weight: 500;
  }

  .nf-not-found-card__actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: center;
    gap: 12px;
  }

  .nf-not-found-card__actions .el-button + .el-button {
    margin-left: 0;
  }
</style>
