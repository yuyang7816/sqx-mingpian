<template>
	<div class="nf-welcome-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">系统首页</span>
			<h2 class="nf-page-hero__title">欢迎回家</h2>
			<p class="nf-page-hero__desc">您已成功登录管理端，可通过左侧菜单进入各业务模块开始工作</p>
		</div>

		<div class="nf-table-card nf-welcome-card">
			<div class="nf-welcome-card__visual">
				<img class="nf-welcome-card__img" src="~@/assets/img/welvome.png" alt="欢迎" />
			</div>
			<p class="nf-welcome-card__slogan">欢迎回家···</p>
		</div>
	</div>
</template>

<script>
	export default {
		computed: {

		}
	}
</script>

<style lang="scss" scoped>
	.nf-welcome-page {
		padding: 0;
	}

	.nf-welcome-card {
		margin-bottom: 0;
		padding: 32px 24px 36px;
		text-align: center;
	}

	.nf-welcome-card__visual {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 20px;
	}

	.nf-welcome-card__img {
		display: block;
		max-width: 100%;
		height: auto;
	}

	.nf-welcome-card__slogan {
		margin: 0;
		font-size: 28px;
		font-weight: 500;
		line-height: 1.4;
		color: var(--nf-text-secondary, #909399);
		letter-spacing: 1px;
	}
</style>
