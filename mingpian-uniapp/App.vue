<script setup>
	import {
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	import {
		onLaunch,
		onShow,
		onHide
	} from '@dcloudio/uni-app';

	onLaunch(() => {
		
		// #ifdef MP-WEIXIN
		if (!uni.getStorageSync('token')) {
			uni.login({
				provider: 'weixin',
				success: function(loginRes) {
					console.log(loginRes, 'code')
					let data = {
						code: loginRes.code
					}
					proxy.$http.get('/app/Login/wxLogin', data).then(res => {
						if (res.code == 0 && res.data) {
							uni.setStorageSync('openId', res.data.open_id)
							uni.setStorageSync('unionId', res.data.unionId)
							uni.setStorageSync('flag', res.data.isPhone)
							uni.setStorageSync('sessionkey', res.data.session_key)
							//是否开启小程序自动登录	320
							proxy.$http.get('/app/common/type/320').then(rep => {
								if (rep.code == 0 && rep.data) {
									if (rep.data.value == '是' && Number(res.data.isPhone) != 2) {
										//没有登录的时候开始自动登录
										let invitationCode = '';
										if (uni.getStorageSync('invitation')) {
											invitationCode = uni.getStorageSync(
												'invitation')
										}
		
										let sendData = {
											openId: uni.getStorageSync(
												'openId'),
											unionId: uni.getStorageSync(
												'unionId'),
											userName: '微信用户',
											avatar: '/static/logo.png',
											sex: '', //性别
											inviterCode: invitationCode, //别人登录进来携带你的邀请码
										};
										let postData = {
											openId: sendData.openId, //小程序openId
											unionId: sendData.unionId, //unionId
											userName: sendData.userName, //微信名称
											avatar: sendData.avatar, //头像
											sex: sendData.sex, //性别
											phone: '',
											staffInvitationCode: sendData.inviterCode,
											cardId: uni.getStorageSync('inviterCardId')
										};
									
										proxy.$http.postJson(
												'/app/Login/insertWxUser', postData
											)
											.then(ret => {
												
												if (ret.code == 0) {
													uni.setStorageSync('token',
														ret
														.token)
													uni.setStorageSync(
														'userName',
														ret.user.userName)
													uni.setStorageSync(
														'avatar',
														ret.user.avatar)
													uni.setStorageSync('phone',
														ret
														.user.phone)
													uni.setStorageSync(
														'invitationCode',
														ret
														.user
														.invitationCode)
													uni.setStorageSync('sex',
														ret
														.user.sex)
													uni.setStorageSync(
														'userId',
														ret.user.userId)
													uni.setStorageSync(
														'openId',
														ret.user.openId)
		
												}
											})
									}
								}
							});
		
		
						}
					})
		
				}
			});
		}
		
		// #endif
		
		let userId = uni.getStorageSync('userId');
		let setin = setInterval(d => { //定时器，定时去调取聊天未读消息
			let token = uni.getStorageSync('token');
			if (token) {
				proxy.$http.get('/app/message/getMessageSign?userId=' + userId).then(res => {
					if (res.code === 0) {
						let messageCount = res.data.messageCount
						uni.setStorageSync('messageCount', messageCount)
					}
				});
			} else {
				uni.removeStorageSync('messageCount');
				clearInterval(setin)
			}
		}, 5000);
		//企业微信客服链接
		proxy.$http.get('/app/common/type/300').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuUrl', res.data.value)
			}
		})
		//企业微信客服appid
		proxy.$http.get('/app/common/type/297').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuAppId', res.data.value)
			}
		})
		// 客服类型(1:电话，2:企业微信，3:客服二维码)
		proxy.$http.get('/app/common/type/296').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('SelKeFu', res.data.value)
			}
		})
		//客服电话  298
		proxy.$http.get('/app/common/type/298').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuPhone', res.data.value)
			}
		})

		//小程序是否上下架  301
		proxy.$http.get('/app/common/type/301').then(res => {
			if (res.code == 0 && res.data.value) {
				// #ifdef MP-WEIXIN
				uni.setStorageSync('XCXIsSelect', res.data.value)
				// #endif
				// #ifndef MP-WEIXIN
				uni.setStorageSync('XCXIsSelect', '是')
				// #endif
			}
		})
		//小程序是否上下架  302
		// proxy.$http.get('/app/common/type/302').then(res => {
		// 	if (res.code == 0 && res.data.value) {
		// 		// #ifdef MP-WEIXIN
		// 		uni.setStorageSync('XCXIsSelect', res.data.value)
		// 		// #endif
		// 		// #ifndef MP-WEIXIN
		// 		uni.setStorageSync('XCXIsSelect', '是')
		// 		// #endif
		// 	}
		// })
	});
	onShow(() => {
		console.log('onShow')
	});
	onHide(() => {
		console.log('onHide')
	});
</script>

<style lang="scss">
	/*每个页面公共css */
	//uview-plus公共样式
	@import "@/uni_modules/uview-plus/index.scss";
	//自定义公共样式
	@import "@/static/css/main.css";

	page {
		background-color: #F5F7FF;
	}
</style>