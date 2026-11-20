<template>
	<view class="container">
		<!-- #ifndef MP-WEIXIN -->

		<view class="wrapper">
			<view class="title">手机号快捷登录</view>
			<view class="tit">未注册的手机号将自动创建账号</view>

			<view class="InputTtem" style="margin-top:60rpx;">
				<up-input placeholder="请输入手机号" border="none" v-model="phone" type="number"
					placeholderClass="input-placeholder"></up-input>
			</view>
			<view class="InputTtem">
				<up-input placeholder="请输入验证码" border="none" v-model="verCode"
					placeholderClass="input-placeholder"></up-input>
				<view @click="getCode()" :class="timer?'activeCode':''" class="yzm">
					{{codeText}}
				</view>
			</view>
			<view class="InputTtem" v-if="show">
				<up-input placeholder="请填写邀请码" border="none" v-model="invitation"
					placeholderClass="input-placeholder"></up-input>
			</view>


			<!-- 协议 -->
			<view class="loginXy flex align-center ">
				<view class="loginXy-box flex align-center ">
					<up-checkbox activeColor="#287AFE" shape="circle" label="" name="agree" usedAlone
						v-model:checked="check">
					</up-checkbox>
					<view @click="check = !check" class="flex align-center">
						登录即代表同意
						<text @click.stop.prevent="goNav('/my/setting/xieyi')">《用户服务协议》</text>
						及
						<text @click.stop.prevent="goNav('/my/setting/mimi')">《隐私政策》</text>
					</view>
				</view>
			</view>


			<view class="login_submit" @click.native="startReg()">登 录</view>
		</view>
		<!-- #endif -->
		<!-- #ifdef MP-WEIXIN -->
		<view class="wxbox">
			<view class='headers'>
				<image src='../../static/logo.png'></image>
			</view>
			<view class='content'>
				<view>申请获取以下权限</view>
				<text>获得你的公开信息(昵称，头像、地区等)</text>
			</view>

			<button v-show="weixinPhone" style="background: #287AFE;color: #FFFFFF;" class="bottom"
				open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
				授权手机号
			</button>
			<button v-show="!weixinPhone" style="background: #3578FE;color: #FFFFFF;" class='bottom'
				bindtap="getUserProfile" @tap="wxGetUserInfo">
				授权登录
			</button>

			<!-- 协议 -->
			<view class="loginXy flex align-center ">
				<view class="loginXy-box flex align-center ">
					<up-checkbox activeColor="#287AFE" shape="circle" label="" name="agree" usedAlone
						v-model:checked="check">
					</up-checkbox>
					<view @click="check = !check" class="flex align-center">
						登录即代表同意
						<text @click.stop.prevent="goNav('/my/setting/xieyi')">《用户服务协议》</text>
						及
						<text @click.stop.prevent="goNav('/my/setting/mimi')">《隐私政策》</text>
					</view>
				</view>
			</view>
		</view>
		<!-- #endif -->
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	// 用户 userId
	const userId = ref('');

	const phone = ref('');
	const verCode = ref('');
	const sysphone = ref('');
	const isRotate = ref(false);
	const platform = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const codeText = ref('获取验证码')
	const invitation = ref('');
	const show = ref(false);
	//是否选择协议
	const check = ref(false);

	const weixinPhone = ref(false);
	const userName = ref('');
	const userAvatarUrl = ref('');
	const sessionkey = ref('');
	const flag = ref('');



	//小程序微信登录后获取手机号
	const getPhoneNumber = (e) => {
		if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
			console.log('用户拒绝提供手机号');
		} else {
			console.log('用户同意提供手机号');
			if (e.detail.iv) {
				setPhoneByInsert(e.detail.encryptedData, e.detail.iv);
			}

		}
	};
	//小程序微信登录后获取手机号
	const setPhoneByInsert = (decryptData, iv) => {
		// console.log(decryptData, iv, '565656565656')
		let data = {
			decryptData: decryptData,
			key: uni.getStorageSync('sessionkey'),
			iv: iv
		};
		proxy.$http.postJson('/app/Login/selectPhone', data).then(res => {
			if (res.code == 0) {
				phone.value = res.data.phoneNumber;
				getWeixinInfo();
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				});
			}
		})
	};
	//小程序第一授权获取用户信息===》按钮触发
	const wxGetUserInfo = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			});
			return
		};
		wx.getUserProfile({
			desc: '业务需要',
			success: infoRes => {
				// console.log("infoRes.encryptedData__________:" + infoRes.encryptedData)
				userName.value = infoRes.userInfo.nickName; //昵称
				userAvatarUrl.value = infoRes.userInfo.avatarUrl; //头像
				console.log("infoRes.encryptedData__________:" + JSON.stringify(infoRes.userInfo))

				try {
					login();
				} catch (e) {}
			},
			fail(res) {}
		})
	};
	const login = () => {
		uni.showLoading({
			title: '登录中...'
		});
		// 1.wx获取登录用户code
		uni.login({
			provider: 'weixin',
			success: function(loginRes) {
				let code = loginRes.code;
				let data = {
					code: code
				};
				proxy.$http.getT("/app/Login/wxLogin", data).then(res => {
					if (res.code === 0) {

						uni.setStorageSync('unionid', res.data.unionid);
						uni.setStorageSync('openid', res.data.open_id);
						sessionkey.value = res.data.session_key;
						flag.value = res.data.isPhone;

						uni.hideLoading();
						//非第一次授权获取用户信息
						//获取用户信息后向调用信息更新方法
						uni.setStorageSync('sessionkey', res.data.session_key);
						if (uni.getStorageSync('inviterCode')) {
							invitation.value = uni.getStorageSync('inviterCode');
						} else {
							invitation.value == proxy.$getInvitation();
						}
						if (res.data.isPhone == 2) {
							weixinPhone.value = true
						} else {
							getWeixinInfo();
						}

					}
				});
			},
		});
	};
	const getWeixinInfo = () => {
		uni.showLoading({
			title: '登录中...'
		});

		let token = uni.getStorageSync('token');
		if (!token) {
			proxy.$http.postJson("/app/Login/insertWxUser", {
				nickName: userName.value,
				avatar: userAvatarUrl.value,
				openId: uni.getStorageSync('openid'),
				phone: phone.value,
				staffInvitationCode: invitation.value,
				cardId: uni.getStorageSync('inviterCardId')
			}).then(res => {
				if (res.code === 0) {
					uni.hideLoading();
					uni.setStorageSync('token', res.token);
					uni.setStorageSync('userId', res.user.userId);
					uni.setStorageSync('mobile', res.user.phone);
					uni.setStorageSync('nickName', res.user.nickName);
					uni.setStorageSync('invitationCode', res.user.staffInvitationCode);
					uni.setStorageSync('avatar', res.user.avatar);
					uni.showToast({
						title: '登录成功'
					})
					proxy.$http.postJson("/app/businessCard/queryAllList", {
						userId: res.data.userId,
						pageNum: 1,
						pageSize: 10
					}).then(ret => {
						if (ret.code == 0) {
							if (ret.data.list.length === 0) {
								setTimeout(() => {
									let inviterCardType = uni.getStorageSync('inviterCardType')
									if (uni.getStorageSync('inviterCardId') && Number(
											inviterCardType) === 2) {
										uni.removeStorageSync('inviterCardType')
										uni.navigateTo({
											url: '/pages/my/userInfo?inviterCardId=' +
												uni.getStorageSync(
													'inviterCardId')
										})
					
									} else {
										uni.switchTab({
											url: '/pages/index/index'
										});
									}
								}, 1000)
							} else {
								setTimeout(() => {
									if(uni.getStorageSync('inviterCardId')){
										uni.navigateTo({
											url: '/my/Enterprise/businessCard?id=' + uni.getStorageSync('inviterCardId')
										})
									}else{
										uni.navigateBack()
									}
									
								}, 1000)
							}
						}
						uni.stopPullDownRefresh()
					
					});
				} else {
					uni.hideLoading();
					uni.showModal({
						showCancel: false,
						title: '登录失败',
						content: res.msg,
					});
				}
			});
		} else {
			proxy.$http.getT('/app/user/info').then(res => {
				if (res.code == 0) {
					uni.hideLoading();
					uni.setStorageSync('userId', res.data.userId);
					uni.setStorageSync('mobile', res.data.phone);
					uni.setStorageSync('nickName', res.data.nickName);
					uni.setStorageSync('invitationCode', res.data.staffInvitationCode);
					uni.setStorageSync('avatar', res.data.avatar);
					uni.showToast({
						title: '登录成功'
					})
					proxy.$http.postJson("/app/businessCard/queryAllList", {
						userId: res.data.userId,
						pageNum: 1,
						pageSize: 10
					}).then(ret => {
						if (ret.code == 0) {
							if (ret.data.list.length === 0) {
								setTimeout(() => {
									let inviterCardType = uni.getStorageSync('inviterCardType')
									if (uni.getStorageSync('inviterCardId') && Number(
											inviterCardType) === 2) {
										uni.removeStorageSync('inviterCardType')
										uni.navigateTo({
											url: '/pages/my/userInfo?inviterCardId=' +
												uni.getStorageSync(
													'inviterCardId')
										})

									} else {
										uni.switchTab({
											url: '/pages/index/index'
										});
									}
								}, 1000)
							} else {
								setTimeout(() => {
									if(uni.getStorageSync('inviterCardId')){
										uni.navigateTo({
											url: '/my/Enterprise/businessCard?id=' + uni.getStorageSync('inviterCardId')
										})
									}else{
										uni.navigateBack()
									}
									
								}, 1000)
							}
						}
						uni.stopPullDownRefresh()

					});

				}
			});
		}
	};
	// 账号登录
	const register = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			});
			return
		};
		uni.navigateTo({
			url: '/pages/public/loginphone'
		});
	};

	//手机号效验
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	//获取验证码
	const getCode = () => {
		if (timer.value) {
			return
		}
		if (phone.value == '') {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		proxy.$http.get('/app/Login/sendMsg/' + phone.value + '/register').then(res => {
			if (res.code == 0) {
				timer.value = setInterval(() => {
					countDown.value--;
					if (countDown.value == 0) {
						clearInterval(timer.value)
						timer.value = null;
						countDown.value = 60;
						codeText.value = '获取验证码'
					} else {
						codeText.value = countDown.value + 's后重新获取'
					}
				}, 1000)
				uni.showToast({
					title: '验证码发送成功请注意查收',
					icon: 'none'
				})
				if (res.msg === 'register') {
					show.value = true;
				}

			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})

	};
	const startReg = () => {
		uni.getSystemInfo({
			success: function(res) {
				console.log(res.model);
				if (res.model == 'iPhone') {
					sysphone.value = 2
				} else {
					sysphone.value = 1
				}
			}
		});
		//注册
		if (isRotate.value) {
			//判断是否加载中，避免重复点击请求
			return false;
		}
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			});
			return
		};
		if (phone.value == '') {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		if (!verCode.value) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			});
			return
		}
		isRotate.value = true;

		var openid = uni.getStorageSync('openid') ? uni.getStorageSync("openid") : '';

		proxy.$http.postT("/app/Login/registerCode", {
			openId: openid,
			phone: phone.value,
			msg: verCode.value,
			platform: platform.value,
			sysphone: sysphone.value,
			staffInviterCode: invitation.value,
			cardId: uni.getStorageSync('inviterCardId')
		}).then(res => {
			if (res.code == 0) {
				uni.setStorageSync('token', res.token);
				uni.setStorageSync('userId', res.user.userId);
				uni.setStorageSync('mobile', res.user.phone);
				uni.setStorageSync('nickName', res.user.nickName);
				uni.setStorageSync('invitationCode', res.user.staffInvitationCode);
				uni.setStorageSync('avatar', res.user.avatar);
				uni.showToast({
					title: '登录成功'
				})
				// getUserCard()
				setTimeout(() => {
					let inviterCardType = uni.getStorageSync('inviterCardType')
					if (uni.getStorageSync('inviterCardId') && Number(inviterCardType) === 2) {
						// uni.navigateTo({
						// 	url: '/my/Enterprise/businessCard?id=' + uni.getStorageSync('inviterCardId')
						// })
						uni.removeStorageSync('inviterCardType')
						uni.navigateTo({
							url: '/pages/my/userInfo?inviterCardId=' + uni.getStorageSync(
								'inviterCardId')
						})

					} else {
						uni.switchTab({
							url: '/pages/index/index'
						});
					}
				}, 1000)
				return false;
			} else {
				isRotate.value = false;
				uni.hideLoading();
				uni.showModal({
					showCancel: false,
					title: '登录失败',
					content: res.msg,
				});
			}
		});
	};
	/* 查询用户是否有名片信息 */
	const getUserCard = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.getT('/app/businessCard/queryUserCard/' + userId).then(ress => {
			if (ress.code === 0 && ress.data && ress.data.cardId) {
				if (uni.getStorageSync('inviterCardId')) {
					uni.navigateTo({
						url: '/my/Enterprise/businessCard?id=' + uni.getStorageSync('inviterCardId')
					})
				} else {
					uni.switchTab({
						url: '/pages/index/index'
					});
				}
			} else {
				if (uni.getStorageSync('inviterCardId')) {
					uni.navigateTo({
						url: '/pages/my/userInfo'
					})
				} else {
					uni.switchTab({
						url: '/pages/index/index'
					});
				}

			}
		});
	};
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	};
	onLoad(option => {
		invitation.value = uni.getStorageSync('inviterCode');
		userId.value = uni.getStorageSync('userId')
		// #ifdef H5
		platform.value = 'H5'
		// #endif
		// #ifdef APP
		platform.value = 'APP'
		// #endif
		// #ifdef MP-WEIXIN
		platform.value = '微信小程序'
		// #endif
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.wrapper {
		padding: 0 50rpx;

		.title {
			font-size: 40rpx;
			font-weight: bold;
			margin-top: 150rpx;
		}

		.tit {
			font-size: 26rpx;
			color: #999999;
			margin-top: 20rpx;
		}

		.InputTtem {
			padding: 50rpx 0 15rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 1px solid #F2F2F2;

			.yzm {
				color: #666666;
				font-size: 26rpx;
			}

			.activeCode {
				color: #287AFE;
			}


		}
	}

	::v-deep .input-placeholder {
		color: #8a8a8a !important;
	}


	.loginXy {
		width: 100%;
		margin-top: 36rpx;

		.loginXy-box {
			width: 686rpx;
			font-size: 24rpx;
			color: #333333;

			text {
				color: #287AFE;
			}
		}
	}

	.login_submit {
		background: linear-gradient(-51deg, #287AFE 0%, #4A8FFF 100%);
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 28rpx;
		color: #FFFFFF;
		padding: 20rpx 0;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 55rpx;
		margin-top: 80rpx;
	}

	.wxbox {
		padding: 40rpx;

		.headers {
			border-bottom: 1px solid #ccc;
			padding-bottom: 50rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				border-radius: 55%;
				width: 200rpx;
				height: 200rpx;
			}
		}

		.content {
			margin: 90rpx 0;

			text {
				display: block;
				color: #9d9d9d;
				margin-top: 40rpx;
			}
		}

		.bottom {
			line-height: 80rpx;
			border-radius: 80rpx;
			margin: 70rpx 0rpx;
			height: 80rpx;
			font-size: 35rpx;
		}
	}
</style>