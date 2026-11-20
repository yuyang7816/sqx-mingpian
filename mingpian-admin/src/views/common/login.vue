<template>
	<div class="site-wrapper site-page--login login-page">
		<div class="site-content__wrapper">
			<div class="site-content">
				<div class="nf-login-stage">
					<div class="nf-login-brand">
						<span class="nf-login-brand__badge">管理端</span>
						<h2 class="nf-login-brand__title">省钱兄名片分享系统</h2>
						<p class="nf-login-brand__desc">高效管理名片分享业务，数据一目了然，助力运营决策。</p>
						<ul class="nf-login-brand__checks">
							<li>多维度数据统计与可视化</li>
							<li>用户、财务、消息一体化管理</li>
							<li>安全可靠的权限与审核机制</li>
						</ul>
						<span class="nf-login-brand__line"></span>
					</div>
					<div class="nf-login-panel">
						<h3 class="nf-login-panel__welcome">欢迎登录</h3>
						<p class="nf-login-panel__subtitle">请输入您的账号信息</p>
						<el-form
							class="nf-login-form"
							:model="dataForm"
							:rules="dataRule"
							ref="dataForm"
							@keyup.enter.native="dataFormSubmit()"
							status-icon
							label-position="top">
							<el-form-item label="账号" prop="userName">
								<el-input v-model="dataForm.userName" placeholder="请输入账号"></el-input>
							</el-form-item>
							<el-form-item label="密码" prop="password">
								<el-input v-model="dataForm.password" type="password" placeholder="请输入密码"></el-input>
							</el-form-item>
							<el-form-item label="验证码" prop="captcha">
								<el-row :gutter="12">
									<el-col :span="14">
										<el-input v-model="dataForm.captcha" placeholder="请输入验证码"></el-input>
									</el-col>
									<el-col :span="10" class="login-captcha">
										<img :src="captchaPath" @click="getCaptcha()" alt="验证码">
									</el-col>
								</el-row>
							</el-form-item>
							<el-form-item>
								<el-button class="login-btn-submit nf-login-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
							</el-form-item>
						</el-form>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {
		getUUID
	} from '@/utils'
	export default {
		data() {
			return {
				dataForm: {
					userName: '',
					password: '',
					uuid: '',
					captcha: ''
				},
				dataRule: {
					userName: [{
						required: true,
						message: '帐号不能为空',
						trigger: 'blur'
					}],
					password: [{
						required: true,
						message: '密码不能为空',
						trigger: 'blur'
					}],
					captcha: [{
						required: true,
						message: '验证码不能为空',
						trigger: 'blur'
					}]
				},
				captchaPath: '',
				show: false,
			}
		},
		created() {
			this.getCaptcha()
		},
		methods: {
			dataFormSubmit() {
				this.$refs['dataForm'].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl('sys/login'),
							method: 'post',
							data: this.$http.adornData({
								'username': this.dataForm.userName,
								'password': this.dataForm.password,
								'uuid': this.dataForm.uuid,
								'captcha': this.dataForm.captcha
							})
						}).then(({
							data
						}) => {
							if (data && data.code === 0) {
								this.$cookie.set('token', data.token)
								this.getRouter()
							} else {
								this.getCaptcha()
								this.$message.error(data.msg)
							}
						})
					}
				})
			},
			getCaptcha() {
				this.dataForm.uuid = getUUID()
				this.captchaPath = this.$http.adornUrl(`captcha.jpg?uuid=${this.dataForm.uuid}`)
			},
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('app/common/type/261'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value == '是') {
							this.show = true
						}
					}
				})
			},
			getRouter(userId) {
				this.$http({
					url: this.$http.adornUrl(`sys/menu/nav`),
					method: 'get',
					params: this.$http.adornData()
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
						sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
						this.$router.replace({
							name: 'home'
						})
					} else {
						sessionStorage.setItem('menuList', '[]')
						sessionStorage.setItem('permissions', '[]')
					}
				})
			},
		}
	}
</script>

<style scoped>
.login-page.site-page--login {
	position: absolute;
	inset: 0;
	background: #f5f6f8;
	overflow: hidden;
	min-width: auto;
}

.login-page.site-page--login::before,
.login-page.site-page--login::after {
	content: "";
	position: fixed;
	border-radius: 50%;
	pointer-events: none;
	z-index: 0;
	filter: blur(2px);
}

.login-page.site-page--login::before {
	top: -18%;
	right: -8%;
	width: 52%;
	height: 52%;
	background: radial-gradient(circle, rgba(47, 57, 104, 0.14) 0%, rgba(47, 57, 104, 0.04) 45%, transparent 70%);
}

.login-page.site-page--login::after {
	bottom: -22%;
	left: -12%;
	width: 48%;
	height: 48%;
	background: radial-gradient(circle, rgba(47, 57, 104, 0.1) 0%, rgba(47, 57, 104, 0.03) 50%, transparent 72%);
}

.login-page >>> .site-content__wrapper {
	position: relative;
	z-index: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	min-height: 100vh;
	padding: 40px 24px;
	margin-left: 0 !important;
	padding-top: 40px !important;
	background: transparent !important;
}

.login-page >>> .site-content {
	padding: 0;
	width: 100%;
	max-width: 980px;
}

.nf-login-stage {
	display: flex;
	align-items: center;
	gap: 48px;
}

.nf-login-brand {
	flex: 1.05;
	padding: 12px 8px 12px 16px;
	color: var(--nf-text-primary, #1a2433);
}

.nf-login-brand__badge {
	display: inline-flex;
	align-items: center;
	padding: 5px 14px;
	font-size: 12px;
	font-weight: 600;
	letter-spacing: 0.02em;
	color: #fff;
	background: var(--nf-brand, #2f3968);
	border-radius: 999px;
	margin-bottom: 22px;
}

.nf-login-brand__title {
	margin: 0 0 14px;
	font-size: 34px;
	font-weight: 700;
	line-height: 1.25;
	color: var(--nf-text-primary, #1a2433);
	letter-spacing: -0.02em;
}

.nf-login-brand__desc {
	margin: 0 0 36px;
	font-size: 15px;
	line-height: 1.7;
	color: var(--nf-text-secondary, #909399);
	max-width: 420px;
}

.nf-login-brand__checks {
	list-style: none;
	margin: 0 0 36px;
	padding: 0;
}

.nf-login-brand__checks li {
	display: flex;
	align-items: center;
	gap: 12px;
	margin-bottom: 16px;
	font-size: 14px;
	color: var(--nf-text-regular, #303133);
}

.nf-login-brand__checks li::before {
	content: "✓";
	display: flex;
	align-items: center;
	justify-content: center;
	width: 22px;
	height: 22px;
	border-radius: 50%;
	background: var(--nf-brand, #2f3968);
	color: #fff;
	font-size: 11px;
	font-weight: 700;
	flex-shrink: 0;
}

.nf-login-brand__line {
	display: block;
	width: 48px;
	height: 3px;
	border-radius: 2px;
	background: var(--nf-brand, #2f3968);
}

.nf-login-panel {
	flex: 0 0 420px;
	width: 420px;
	padding: 40px 36px 36px;
	background: #fff;
	border-radius: 14px;
	border: 1px solid rgba(47, 57, 104, 0.08);
	border-top: 4px solid var(--nf-brand, #2f3968);
	box-shadow: 0 12px 40px rgba(31, 38, 69, 0.08), 0 2px 8px rgba(31, 38, 69, 0.04);
}

.nf-login-panel__welcome {
	margin: 0 0 8px;
	font-size: 24px;
	font-weight: 700;
	color: var(--nf-text-primary, #1a2433);
}

.nf-login-panel__subtitle {
	margin: 0 0 28px;
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
}

.nf-login-form >>> .el-form-item {
	margin-bottom: 20px;
}

.nf-login-form >>> .el-form-item__label {
	padding-bottom: 6px;
	line-height: 1.4;
	color: var(--nf-text-regular, #303133);
	font-weight: 500;
}

.nf-login-form >>> .el-input__inner {
	height: 42px;
	line-height: 42px;
	border-radius: 8px;
	border-color: #e4e7ed;
}

.nf-login-form >>> .el-input__inner:focus {
	border-color: var(--nf-brand, #2f3968);
}

.nf-login-form >>> .login-btn-submit {
	width: 100%;
	margin-top: 8px;
	height: 44px;
	font-size: 15px;
	font-weight: 600;
	border-radius: 8px;
	letter-spacing: 0.04em;
}

.login-page >>> .el-button--primary.nf-login-submit,
.login-page >>> .login-btn-submit.el-button--primary {
	background-color: var(--nf-brand, #2f3968) !important;
	border-color: var(--nf-brand, #2f3968) !important;
	color: #fff !important;
}

.login-page >>> .el-button--primary.nf-login-submit:hover,
.login-page >>> .login-btn-submit.el-button--primary:hover {
	background-color: var(--nf-brand-deep, #1f2645) !important;
	border-color: var(--nf-brand-deep, #1f2645) !important;
	color: #fff !important;
}

.login-captcha >>> img {
	width: 100%;
	height: 42px;
	object-fit: cover;
	border-radius: 8px;
	cursor: pointer;
	border: 1px solid #e4e7ed;
	background: #fff;
}

@media (max-width: 900px) {
	.nf-login-stage {
		flex-direction: column;
		gap: 28px;
		align-items: stretch;
	}

	.nf-login-brand {
		padding: 0;
		text-align: left;
	}

	.nf-login-brand__title {
		font-size: 28px;
	}

	.nf-login-panel {
		flex: none;
		width: 100%;
		max-width: 420px;
		margin: 0 auto;
	}
}

@media (max-width: 480px) {
	.login-page >>> .site-content__wrapper {
		padding: 24px 16px !important;
	}

	.nf-login-panel {
		padding: 28px 20px 24px;
	}

	.nf-login-brand__title {
		font-size: 24px;
	}
}
</style>
