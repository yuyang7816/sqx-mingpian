<template>
	<nav class="site-navbar site-navbar--nf" :class="'site-navbar--' + navbarLayoutType">
		<div class="site-navbar__header">
			<h1 class="site-navbar__brand">
				<a class="site-navbar__brand-lg" href="javascript:;">
					<img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
					<span class="site-navbar__brand-text">省钱兄名片分享系统</span>
				</a>
				<a class="site-navbar__brand-mini" href="javascript:;">
					<img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
				</a>
			</h1>
		</div>
		<div class="site-navbar__body clearfix">
			<el-menu class="site-navbar__menu" mode="horizontal">
				<el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
					<icon-svg name="zhedie"></icon-svg>
				</el-menu-item>
			</el-menu>
			<div class="site-navbar__context">
				<span class="nf-context-badge">当前页面</span>
				<span class="site-navbar__context-title">{{ pageTitle }}</span>
			</div>
			<el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
				<el-menu-item class="site-navbar__avatar" index="3">
					<el-dropdown :show-timeout="0" placement="bottom">
						<span class="el-dropdown-link user-pill">
							<img class="user-pill__avatar" src="~@/assets/img/avatar.png" :alt="userName">
							<span class="user-pill__info">
								<span class="user-pill__name">{{ username || userName || '管理员' }}</span>
								<span class="user-pill__sub">欢迎您登录</span>
							</span>
							<i class="el-icon-arrow-down user-pill__arrow"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</el-menu-item>
			</el-menu>
		</div>

		<update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
	</nav>
</template>

<script>
	import UpdatePassword from './main-navbar-update-password'
	import {
		clearLoginInfo
	} from '@/utils'
	export default {
		data() {
			return {
				updatePassowrdVisible: false,
				username: '',
				intervalId: 0,
				storeId: 0,
				isshow: false,
				chatNum: 0,
				errCount: 0,
				code: 0,
				chatNumRz: 0,
				chatNumDp: 0,
				chatNumFw: 0,
				chatNumDls: 0,
				chatNumHyzx: 0,
				chatNumTx: 0,
				chatNumXx: 0,
				chatNumMp:0,
				chatNumQz:0,
				chatNumDh:0,
			}
		},
		components: {
			UpdatePassword
		},
		computed: {
			navbarLayoutType: {
				get() {
					return this.$store.state.common.navbarLayoutType
				}
			},
			sidebarFold: {
				get() {
					return this.$store.state.common.sidebarFold
				},
				set(val) {
					this.$store.commit('common/updateSidebarFold', val)
				}
			},
			mainTabs: {
				get() {
					return this.$store.state.common.mainTabs
				},
				set(val) {
					this.$store.commit('common/updateMainTabs', val)
				}
			},
			userName: {
				get() {
					return this.$store.state.user.name
				}
			},
			pageTitle() {
				if (this.$route.meta && this.$route.meta.title) {
					return this.$route.meta.title
				}
				const activeTab = this.mainTabs.find(item => item.name === this.$route.name)
				return activeTab ? activeTab.title : '管理系统'
			}
		},
		methods: {
			updatePasswordHandle() {
				this.updatePassowrdVisible = true
				this.$nextTick(() => {
					this.$refs.updatePassowrd.init()
				})
			},
			logoutHandle() {
				this.$confirm(`确定进行[退出]操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$cookie.set('token', '')
					this.$cookie.set('userId', '')

					clearInterval(this.chatNum2)
					clearInterval(this.chatNum)
					this.delAllCookie()
					this.$router.push({
						name: 'login'
					})

				}).catch(() => {})
			},
			delAllCookie() {
				var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
				if (keys) {
					for (var i = keys.length; i--;) {
						document.cookie =
							keys[i] + "=0;path=/;expires=" + new Date(0).toUTCString();
						document.cookie =
							keys[i] +
							"=0;path=/;domain=" +
							document.domain +
							";expires=" +
							new Date(0).toUTCString();
						document.cookie =
							keys[i] +
							"=0;path=/;expires=" +
							new Date(0).toUTCString(); 
					}
				}
			},

			userSelect() {
				let userId = this.$cookie.get('userId')
				this.$http({
					url: this.$http.adornUrl(`sys/user/info`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.user;
					this.username = returnData.username
				})
			},
			chatSelect() {
				this.$http({
					url: this.$http.adornUrl('chats/count'),
					method: 'get',
					params: this.$http.adornParams({
						'storeId': this.storeId
					})
				}).then(({
					data
				}) => {
					this.code = data.code
					
					console.log('this.chatNum', data.data)
					if (data.data > 0) {
						if (this.errCount != data.data) {
							this.$notify.info({
								title: '消息',
								dangerouslyUseHTMLString: true,
								message: '您有' + data.data + '条未读消息。',
								offset: 80,
								duration: 2500,
								type: "warning"
							});
							this.chatNum = data.data
							this.errCount = data.data
						}
					} else {
						this.chatNum = 0
						this.errCount = 0
					}
					if (this.chatNum == 0) {
						this.isshow = false
					} else {
						this.isshow = true
					}

				})
			},
			notification() {
				let that = this;
				if (!("Notification" in window)) {
					console.log("浏览器不支持消息通知");
					return;
				}
				Notification.requestPermission(function(permission) {});
			},
			chatSelect2() {
				this.$http({
					url: this.$http.adornUrl('chats/count'),
					method: 'get',
					params: this.$http.adornParams({
						'storeId': this.storeId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					if (data.data > 0) {
						this.$notify.info({
							title: '消息',
							dangerouslyUseHTMLString: true,
							message: '您有' + data.data + '条未读消息。',
							offset: 80,
							duration: 2500,
							type: "warning"
						});
					}
				})
			},
			aplayAudio() {
				console.log('语音提示')
				document.getElementById('audio').play()

			},
			dataSelectRz() {
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'status': 0,
						'userType': 1,
						'realName': '',
						'enterprisePhone': '',
					})
				}).then(({
					data
				}) => {
					console.log('认证待审核数据', data.data.total)
					if (data.data) {
						this.$cookie.set('numberRz', data.data.total)
					}

				})
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'status': 1,
						'state': 6,
						'type': 1,
					})
				}).then(({
					data
				}) => {
					console.log('申请待审核数据', data.data.total)
					if (data.data) {
						this.$cookie.set('numberSq', data.data.total)
					}
				
				})
			},
			dataSelectDp() {
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'status': 0,
						'userType': 2,
						'realName': '',
						'enterprisePhone': '',
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberDp', data.data.total)
					}
				})
			},
			dataSelectFw() {
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/getBusinessCardList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'userType': '',
						'cardStatus': 0,
						'companyName': '',
						'userName': '',
						'realName': '',
						'industryFirstName': '',
						'industrySecondName': '',
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberFw', data.data.total)
					}
				})
			},
			dataSelectDls() {
				this.$http({
					url: this.$http.adornUrl('admin/agentAuth/getAgentAuthList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'userName': '',
						'phone': '',
						'realName': '',
						'idNumber': '',
						'inviterCode': '',
						'status': 0,
						'sex': '',

					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberDls', data.data.total)
					}
				})
			},
			dataSelectHyzx() {
				this.$http({
					url: this.$http.adornUrl('admin/industryNews/getIndustryNewsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'industryFirstName': '',
						'industrySecondName': '',
						'title': '',
						'industryType': '',
						'status': 0,

					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberHyzx', data.data.total)
					}
				})

				this.$http({
					url: this.$http.adornUrl('admin/industryNewsComment/getCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'status': 0,
						'newsId': '',

					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberPl', data.data.total)
					}
				})


			},
			dataSelectTx() {
				this.$http({
					url: this.$http.adornUrl('cash/selectAdminHelpProfit'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'zhifubaoName': '',
						'zhifubao': '',
						'startTime': '',
						'endTime': '',
						'phone': '',
						'classify': '',
						'state': 0,

					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberTx', data.data.total)
					}
				})
			},
			dataSelectXx() {
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 4,
						'platform': '',
						'status': 0,

					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberXx1', data.data.total)
					}
				})
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 7,
						'platform': '',
						'status': 0,

					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberXx3', data.data.total)
					}
				})
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 11,
						'platform': '',
						'status': 0,
				
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberXx4', data.data.total)
					}
				})
			},
			dataSelectMp() {
				this.$http({
					url: this.$http.adornUrl('admin/purchaseCard/selectPurchaseCardList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'postName': '',
						'purchaseName': '',
						'status': 1,
						'purchasePhone': '',
						'userName': '',
						'phone': '',
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberMp', data.data.totalCount)
					}
				})
			},
			dataSelectQz() {
				this.$http({
					url: this.$http.adornUrl('admin/sourceGroup/getSourceGroupList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'sysUserName': '',
						'groupType': '',
						'isEnable': '',
						'groupName': '',
						'userName': '',
						'status':1,
			
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberQz', data.data.total)
					}
				})
			},
			dataSelectDh() {
				this.$http({
					url: this.$http.adornUrl('shopOrders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 0,
						'size': 10,
						'orderNum': '',
						'title': '',
						'mobile': '',
						'status': 2,
						'startTime': '',
						'endTime': ''
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberDh', data.data.totalElements)
					}
				})
				this.$http({
					url: this.$http.adornUrl('shopOrders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 0,
						'size': 10,
						'orderNum': '',
						'title': '',
						'mobile': '',
						'status': 6,
						'startTime': '',
						'endTime': ''
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.$cookie.set('numberDhTk', data.data.totalElements)
					}
				})
			},
		},
		mounted() {

			this.userSelect()
			this.dataSelectRz()
			this.dataSelectDp()
			this.dataSelectTx()
			var menuList = sessionStorage.getItem('menuList')
			menuList = JSON.parse(menuList)
			if (menuList.length > 0) {
				console.log('-=============', menuList)
				if (menuList[0].menuId != 33) {

					this.$router.replace({
						name: 'welcome'
					})
				}
			}

		},
		created() {
			this.chatNum = setInterval(() => {
				var token = this.$cookie.get('token')
				if (token !== '') {
				} else {
					clearInterval(this.chatNum)
				}
			}, 6000)

			this.chatNumRz = setInterval(() => {
				this.dataSelectRz()
			}, 6000)
			this.chatNumDp = setInterval(() => {
				this.dataSelectDp()
			}, 6000)
			this.chatNumTx = setInterval(() => {
				this.dataSelectTx()
			}, 6000)

		},
		beforeDestroy() {
			clearInterval(this.chatNum)
			clearInterval(this.chatNumRz)
			clearInterval(this.chatNumDp)
			clearInterval(this.chatNumFw)
		},
		watch: {
			'chatNum': function() {
				if (this.chatNum > 0) {
				}
			},
		}
	}
</script>
<style scoped="scoped">
	.site-navbar__body {
		display: flex;
		align-items: center;
	}

	.site-navbar__menu--right {
		margin-left: auto;
	}

	.left-number {
		position: absolute;
		top: 0;
		right: 20px;
		width: 18px;
		height: 18px;
		line-height: 18px;
		border-radius: 50%;
		background: #ff4949;
		color: #ffffff;
		text-align: center;
		font-size: 10px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		margin-top: 5px;
	}
</style>
