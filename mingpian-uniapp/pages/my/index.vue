<template>
	<view>
		<view class="bgColor">
			<view class="Head">
				<!-- 用户信息 -->
				<view class="Head_info">
					<image :src="avatar?avatar:'/static/logo.png'" class="Head_infoAvatr"
						@click="goNav('/pages/my/info')"></image>
					<view class="Head_infoCot" v-if="isLogin">
						<view class="Head_infoCot_user">
							<view class="Head_infoCot_userTie">
								<text>{{userName}}</text>
								<image src="/static/image/my/vipphb.png" style="width: 60rpx;height: 30rpx;"
									mode="widthFix" v-if="isVip">
								</image>
							</view>
							<view class="Head_infoCot_userTit" v-if="CardInfo.companyPosition">
								{{CardInfo.companyPosition}}
							</view>
							<view class="Head_infoCot_userTit" v-if="CardInfo.companyName">{{CardInfo.companyName}}
							</view>
						</view>

						<!-- #ifdef MP-WEIXIN -->
						<view class="Head_infoCot_mA" @click="openCard()" v-if="XCXIsSelect!='否'">
							<image src="/static/image/my/ma.png"></image>
						</view>
						<!-- #endif -->
					</view>
					<view class="Head_Login" v-else @click="goLogin">登录</view>
				</view>
				<!-- 用户资料 -->
				<view class="userInfo" v-if="XCXIsSelect!='否'">
					<view class="userInfoItem" @click="goVipFk()">
						<view class="userInfoItem_num">{{InfoData.visitor?InfoData.visitor:0}}</view>
						<view class="userInfoItem_tit">最近访客</view>
					</view>
					<view class="userInfoItem" @click="goNav('/my/grid/look?index=2')">
						<view class="userInfoItem_num">{{InfoData.views?InfoData.views:0}}</view>
						<view class="userInfoItem_tit">最近查看</view>
					</view>
					<view class="userInfoItem" @click="goNav('/my/grid/invitation')">
						<view class="userInfoItem_num">{{InfoData.invites?InfoData.invites:0}}</view>
						<view class="userInfoItem_tit">最近邀请</view>
					</view>
					<view class="userInfoItem" >
						<view class="userInfoItem_num">{{vipCardNum?vipCardNum:0}}</view>
						<view class="userInfoItem_tit">互换次数</view>
					</view>
					
				</view>
			</view>
		</view>
		<view class="contentColor">
			<view class="Vipbox" v-if="XCXIsSelect!='否'">
				<view class="Vipbox_left">
					<view class="Vipbox_leftTitle">开通会员</view>
					<view class="Vipbox_leftTit">成为会员畅享多种权益</view>
				</view>
				<view class="Vipbox_btn" @click="goNav('/my/vip/index')">{{isVip?'已开通':'立即开通'}}</view>
			</view>
			<!-- <view class="boxSet">
				<view class="boxSet_tit">常用工具</view>
				<view class="boxSet_cont">
					<view class="boxSet_contItem">
						<image src="/static/image/my/fapiao.png" mode="widthFix"></image>
						<view class="boxSet_contItem_tit">我的发票</view>
					</view>
					<view class="boxSet_contItem" @click="goToRz()">
						<image src="/static/image/my/renzheng.png" mode="widthFix"></image>
						<view class="boxSet_contItem_tit">认证中心</view>
					</view>
					<view class="boxSet_contItem">
						<image src="/static/image/my/qianbao.png" mode="widthFix"></image>
						<view class="boxSet_contItem_tit">我的钱包</view>
					</view>
					<view class="boxSet_contItem" @click="goChat">
						<image src="/static/image/my/kefu.png" mode="widthFix"></image>
						<view class="boxSet_contItem_tit">客服中心</view>
					</view>
				</view>
			</view> -->
		</view>

		<view class="setbox">
			<view class="setbox_tit">其他服务</view>
			<view class="setboxItem" @click="goHc()" v-if="XCXIsSelect!='否'">
				<view class="setboxItem_left">
					<image src="/static/image/my/hecha.png" mode="widthFix"></image>
					<text>AI核查</text>
				</view>
				<image src="/static/image/my/right.png"></image>
			</view>
			<view class="setboxItem" @click="goNav('/pages/my/invitationUser')">
				<view class="setboxItem_left">
					<image src="/static/image/my/yao.png" mode="widthFix"></image>
					<text>邀请好友</text>
				</view>
				<image src="/static/image/my/right.png"></image>
			</view>
			<view class="setboxItem" @click="goChat()">
				<view class="setboxItem_left">
					<image src="/static/image/my/chat.png" mode="widthFix"></image>
					<text>客服中心</text>
				</view>
				<image src="/static/image/my/right.png"></image>
			</view>
			<view class="setboxItem" @click="goNav('/my/team/index')" v-if="isPromoter==1">
				<view class="setboxItem_left">
					<image src="/static/image/my/team.png" mode="widthFix"></image>
					<text>团队分销</text>
				</view>
				<image src="/static/image/my/right.png"></image>
			</view>
			<view class="setboxItem" @click="goNav('/my/setting/feedback')">
				<view class="setboxItem_left">
					<image src="/static/image/my/fk.png" mode="widthFix"></image>
					<text>意见反馈</text>
				</view>
				<image src="/static/image/my/right.png"></image>
			</view>

			<view class="setboxItem" @click="goNav('/my/setting/index')">
				<view class="setboxItem_left">
					<image src="/static/image/my/set.png" mode="widthFix"></image>
					<text>设置中心</text>
				</view>
				<image src="/static/image/my/right.png"></image>
			</view>
		</view>


		<up-popup :show="showCode" @close="showCode = false" mode="center" round="24" :closeable="true">
			<view class="padding ">
				<view class="titleYq ">邀请码</view>
				<view class="margin-top">
					<!-- <up-qrcode cid="ex1" :size="200" :val="valueUrl"></up-qrcode> -->
					<!-- <image src="/static/logo.png" style="width: 500rpx;height: 500rpx;"></image> -->
					<!-- <image mode="widthFix"
						:src="'https://mpfx.xianmaxiong.com/sqx_fast/app/userCollect/shareCard/'+CardInfo.cardId">
					</image> -->
					<image mode="widthFix" :src="codeImage">
					</image>

				</view>
			</view>
		</up-popup>

		<up-popup :show="showVip" mode="center" round="24" closeable @close="showVip = false">
			<view class="popupVip">
				<image src="/my/static/vipbg.png" mode="widthFix"></image>
				<view class="popupVip_title">开通VIP会员</view>
				<view class="popupVip_tit">需开通VIP会员可使用AI核查</view>
				<view class="popupVip_btn" @click="goNav('/my/vip/index')">去开通</view>
			</view>
		</up-popup>

		<!-- 自定义导航栏 -->
		<myTabbar :value="4" />
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
	// 引入底部 Tabbar 组件
	import myTabbar from '@/components/myTabbar/myTabbar.vue';
	const isLogin = ref(false); //是否登录
	const userId = ref(''); // 用户 userId
	const token = ref(''); // 用户 token
	// 用户信息
	const invitationCode = ref(''); //邀请码
	const userName = ref(''); //用户昵称
	const avatar = ref(''); //用户头像
	const CertificationStatus = ref(''); //认证状态  0-待审核 1-已通过 2-未通过
	const showCode = ref(false); //分享码弹框
	const CardInfo = reactive({});
	const InfoData = reactive({});

	const valueUrl = ref('');
	const isVip = ref(false)

	const isPromoter = ref('');
	const codeImage = ref('')
	const showVip = ref(false);
	const XCXIsSelect = ref('否');
	
	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框
	const vipCardNum = ref(0);//名片次数


	onLoad(option => {
		uni.hideTabBar();
		proxy.$http.getT('/app/common/type/240').then(res => { //微信消息通知模版	240
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		})
		
		proxy.$http.getT('/app/common/type/292').then(res => { //审核状态通知	292	
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		})
	});
	onShow(() => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
		token.value = uni.getStorageSync('token');
		userId.value = uni.getStorageSync('userId');
		if (uni.getStorageSync('token')) {
			isLogin.value = true
			invitationCode.value = uni.getStorageSync('invitationCode');
			userName.value = uni.getStorageSync('userName');
			getUserInfo();
			getMyCard();
			getTuiguang1();

			getIsVip()
			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		} else {
			isPromoter.value = -1
			isLogin.value = false
			userId.value = '';
			token.value = '';
			userName.value = '';
			avatar.value = '';
			isVip.value = false
			InfoData.visitor = 0
			InfoData.views = 0
			InfoData.invites = 0
			vipCardNum.value = 0

		}
	});

	/* 会员核查企业 */
	const goHc = () => {
		if (uni.getStorageSync('token')) {
			if (isVip.value) {
				uni.navigateTo({
					url: '/my/hecha/index'
				})
			} else {
				// uni.showToast({
				// 	title: '开通会员才可核查企业',
				// 	icon: 'none'
				// })
				showVip.value = true
			}
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 会员查看访客 */
	const goVipFk = () => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			if (isVip.value) {
				uni.navigateTo({
					url: '/my/grid/look?index=1'
				})
			} else {
				uni.showToast({
					title: '开通会员才可查看访客',
					icon: 'none'
				})
			}
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}

	};
	const openCard = () => {
		if (CardInfo.cardId) {
			/* 获取小程序二维码 */
			codeImage.value = 'https://mpfx.xianmaxiong.com/sqx_fast/app/userCollect/shareCard/' + CardInfo.cardId

			showCode.value = true
		} else {
			uni.showToast({
				title: '请先去添加名片',
				icon: 'none'
			})
		}
	};
	// 查询是否开通会员
	const getIsVip = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.getT('/app/user/isVip?userId=' + userId).then(res => {
			if (res.code === 0) {
				isVip.value = res.data
			}
		});
	};
	// 小程序推广二维码
	const getmpCreateQr = () => {
		proxy.$http.getT('/app/userCollect/shareCard/' + CardInfo.cardId).then(res => {
			if (res.code === 0) {

			}
		});
	};
	//文案推广
	const getTuiguang1 = () => {
		// #ifndef H5
		proxy.$http.getT('/app/common/type/25').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					valueUrl.value = res.data.value;
				}
			}
		});
		//#endif
		// #ifdef H5
		proxy.$http.getT('/app/common/type/19').then(ress => {
			if (ress.code === 0) {
				if (ress.data && ress.data.value) {
					valueUrl.value = ress.data.value;
				} else {
					valueUrl.value = proxy.$publicYuMing();
				}
			}
		});
		//#endif
	};
	// 查询展示的名片
	const getMyCard = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/businessCard/showCard?userId=" + userId).then(res => {
			if (res.code == 0 && res.data) {
				Object.assign(CardInfo, res.data)
				// getmpCreateQr()
			}
			getData()
		});
	};
	// 获取顶部访客 查看 邀请
	const getData = () => {
		let cardId = CardInfo.cardId ? CardInfo.cardId : ''
		if(cardId){
			proxy.$http.get("/app/dataCenter/countCard?cardId=" + cardId).then(res => {
				if (res.code == 0 && res.data) {
					Object.assign(InfoData, res.data)
				}
			});
		}else{
			proxy.$http.get("/app/dataCenter/countCard").then(res => {
				if (res.code == 0 && res.data) {
					Object.assign(InfoData, res.data)
				}
			});
		}
		
	};
	// 跳转
	const goNav = (url) => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			if (url === '/my/vip/index') {
				showVip.value = false
			}
			uni.navigateTo({
				url
			})
		} else {
			goLogin();
		}
	};
	// 跳转登录
	const goLogin = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 认证
	const goToRz = () => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/my/renzheng/index'
			})
		} else {
			goLogin();
		}
	};
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				vipCardNum.value = res.data.vipCardNum
				invitationCode.value = res.data.staffInvitationCode
				userName.value = res.data.userName
				avatar.value = res.data.avatar
				isPromoter.value = res.data.isPromoter
				uni.setStorageSync('userId', res.data.userId);
				uni.setStorageSync('mobile', res.data.phone);
				uni.setStorageSync('nickName', res.data.nickName);
				uni.setStorageSync('invitationCode', res.data.staffInvitationCode);
				uni.setStorageSync('avatar', res.data.avatar);

				// getUserCertification()
			}
		});
	};
	// // 查询用户认证状态
	// const getUserCertification = () => {
	// 	let userId = uni.getStorageSync('userId')
	// 	proxy.$http.get("/app/userCertification/queryInsert?userId=" + userId).then(red => {
	// 		if (red.code == 0 && red.data) {
	// 			CertificationStatus.value = red.data.status
	// 		} else {
	// 			CertificationStatus.value = ''
	// 		}
	// 	});
	// };
	// 联系客服
	const goChat = () => {
		if (!uni.getStorageSync('token')) {
			goLogin();
			return
		}

		let kefu = uni.getStorageSync('SelKeFu'); // 用户端联系方式 1 手机号 2企业微信
		let kefuPhone = uni.getStorageSync('kefuPhone');
		if (kefu == 1) {
			uni.makePhoneCall({
				phoneNumber: kefuPhone //仅为示例
			});
		} else if (kefu == 2) {
			// #ifdef MP-WEIXIN
			// let urlKf = 'https://mpfx.xianmaxiong.com/pages/index/webView?url=' + uni.getStorageSync('kefuUrl')
			// uni.navigateTo({
			// 	url: '/pages/index/webView?url=' + urlKf
			// });
			try {
				wx.openCustomerServiceChat({
					extInfo: {
						url: uni.getStorageSync('kefuUrl')
					},
					corpId: uni.getStorageSync('kefuAppId'),
					success(res) {},
					fail(res) {
						console.error(res)
					}
				})
			} catch (error) {
				console.error("catchcatch" + error)
				uni.showToast({
					title: '请更新至微信最新版本'
				});
			}
			// #endif
			// #ifndef MP-WEIXIN
			let url = uni.getStorageSync('kefuUrl');
			if (url.indexOf('/pages/') !== -1 || url.indexOf('/pageA/') !== -1 || url.indexOf('/my/') !== -1) {
				uni.navigateTo({
					url
				});
			} else {
				//#ifndef H5
				uni.navigateTo({
					url: '/pages/index/webView?url=' + url
				});
				//#endif
				//#ifdef H5
				window.location.href = url;
				//#endif
			}
			// #endif
		} else if (kefu == 3) {
			uni.navigateTo({
				url: '/my/setting/customer'
			});
		}
	};
	// 开启订阅消息
	const openMsg = () => {
		uni.getSetting({
			withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
			success(ret) {
				console.log(ret,'=======')
				if (ret.subscriptionsSetting.itemSettings) {
					uni.setStorageSync('sendMsg', true)
					uni.openSetting({ // 打开设置页 
						success(rea) {
							console.log(rea.authSetting)
						}
					});
				} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.setStorageSync('sendMsg', false)
					uni.showModal({
						title: '提示',
						content: '为了更好的体验,请绑定消息推送',
						confirmText: '确定',
						cancelText: '取消',
						success: function(res) {
							if (res.confirm) {
								wx.requestSubscribeMessage({
									tmplIds: arr,
									success(re) {
										var datas = JSON.stringify(re);
										if (datas.indexOf("accept") != -1) {
											console.log(re)
										}
									},
									fail: (res) => {
										console.log(res)
									}
								})
								showModalMsg.value = false
							} else if (res.cancel) {
								showModalMsg.value = true
							}
						}
					})
				}
			}
		})
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F5F5F7;
	}

	.bgColor {
		background: linear-gradient(to bottom, #287AFE 10%, #FFFFFF 70%);
		padding: 30rpx 0;
	}

	// 用户信息
	.Head {
		margin: 0 32rpx 0;
		background: #FFFFFF;
		box-shadow: 0rpx 8rpx 16rpx 0rpx #E9E9E9;
		border-radius: 24rpx;
		padding: 30rpx;

		.Head_info {
			display: flex;
			align-items: center;

			.Head_infoAvatr {
				width: 88rpx;
				height: 88rpx;
				border-radius: 55%;
			}

			.Head_Login {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
				margin-left: 20rpx;
			}

			.Head_infoCot {
				flex: 1;
				display: flex;
				justify-content: space-between;
				margin-left: 20rpx;

				.Head_infoCot_user {
					.Head_infoCot_userTie {
						display: flex;
						align-items: center;

						text {
							font-family: Source Han Sans CN;
							font-weight: 500;
							font-size: 32rpx;
							color: #333333;
							margin-right: 10rpx;
						}
					}

					.Head_infoCot_userTit {
						font-family: Source Han Sans CN;
						font-weight: 400;
						font-size: 24rpx;
						color: #999999;
					}
				}

				.Head_infoCot_mA {
					width: 50rpx;
					height: 50rpx;
					border-radius: 12rpx;

					image {
						width: 100%;
						height: 100%;
						border-radius: 12rpx;
					}
				}
			}
		}
	}

	// 访客 
	.userInfo {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 30rpx;

		.userInfoItem {
			text-align: center;

			.userInfoItem_num {
				font-family: DINPro;
				font-weight: 500;
				font-size: 36rpx;
				color: #333333;
			}

			.userInfoItem_tit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #333333;
			}
		}
	}

	.contentColor {
		background: linear-gradient(to bottom, #FFFFFF 40%, #F5F5F7 80%);
	}

	// 会员模块
	.Vipbox {
		margin: 0 30rpx;
		height: 120rpx;
		// background-image: url('/static/image/my/vipbg.png');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		background: linear-gradient(86deg, #FEF2DC 0%, #FEE4BF 100%);
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 35rpx;

		.Vipbox_left {
			display: flex;
			align-items: flex-end;

			.Vipbox_leftTitle {
				font-weight: 800;
				font-size: 34rpx;
				color: #785831;
			}

			.Vipbox_leftTit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #785831;
				margin-left: 15rpx;
			}
		}

		.Vipbox_btn {
			width: 150rpx;
			height: 52rpx;
			background: #785831;
			border-radius: 26rpx;

			font-weight: 500;
			font-size: 26rpx;
			color: #FFF4E2;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	// 常用工具
	.boxSet {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		box-shadow: 0rpx 0rpx 6rpx 0rpx #E9E9E9;
		border-radius: 24rpx;
		padding: 30rpx 0;

		.boxSet_tit {

			font-weight: bold;
			font-size: 32rpx;
			color: #333333;
			margin-left: 15rpx;

		}

		.boxSet_cont {
			margin-top: 20rpx;
			padding: 0 30rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.boxSet_contItem {
				text-align: center;

				image {
					width: 65rpx;
					height: 53rpx;
				}

				.boxSet_contItem_tit {

					font-weight: 500;
					font-size: 26rpx;
					color: #333333;
				}
			}
		}
	}

	// 其他服务
	.setbox {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx 30rpx 20rpx;

		.setbox_tit {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
			padding-bottom: 10rpx;
		}

		.setboxItem {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 30rpx 0;

			.setboxItem_left {
				display: flex;
				align-items: center;

				image {
					width: 38rpx;
					height: 37rpx;
				}

				text {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 28rpx;
					color: #333333;
					margin-left: 15rpx;
				}
			}

			image {
				width: 11rpx;
				height: 21rpx;
			}
		}
	}

	.titleYq {
		text-align: center;
		font-size: 32rpx;
	}

	.popupVip {
		width: calc(500rpx - 60rpx);

		background: linear-gradient(0deg, #FFFFFF 0%, #FDF8EE 100%);
		border-radius: 32rpx;
		padding: 40rpx 30rpx 10rpx;
		text-align: center;

		image {
			width: 355rpx;
			height: 251rpx;
		}

		.popupVip_title {
			font-weight: bold;
			font-size: 38rpx;
			color: #D49E53;
		}

		.popupVip_tit {
			font-weight: 400;
			font-size: 26rpx;
			color: #333333;
			margin-top: 10rpx;
		}

		.popupVip_btn {
			width: 376rpx;
			height: 82rpx;
			background: #3E3E3E;
			border-radius: 18rpx;
			font-weight: 400;
			font-size: 26rpx;
			color: #FBE8C6;
			display: flex;
			align-items: center;
			justify-content: center;
			/* #ifdef H5 */
			margin: 40rpx auto;
			/* #endif */
			/* #ifndef H5 */
			margin: 40rpx auto 0;
			/* #endif */
		}
	}
</style>