<template>
	<view>
		<view class="headTop">
			<view class="vipbox">
				<view class="vipboxTitle">
					<image src="/my/static/vip/vip.png"></image>
					VIP会员
				</view>
				<view class="vipboxTitl">数字名片，高效沟通管理</view>
				<view class="vipboxTit">开通会员尊享多种特权福利</view>
				<view class="Vipbox_btn" v-if="!isVip">
					<view class="Vipbox_btnkt" @click="openPay">立即开通</view>
					<view class="Vipbox_btnPrice">￥<text>{{price}}</text>起</view>
				</view>
				<view class="Vipbox_btn" v-if="isVip&&vipEndTime ">
					<view class="Vipbox_btnPrice">会员到期时间：{{vipEndTime}}</view>
				</view>

			</view>
		</view>

		<view class="vipboxTc">
			<view class="vipboxTc_Tit">选择会员套餐</view>
			<view class="vipboxTc_Type">
				<scroll-view class="scroll-view_H" scroll-x="true" scroll-left="0">
					<view v-for="(item,index) in vipType" :key="index" class="scroll-view-item_H "
						@click="bindViptype(index)" :class="vipTypeIndex==index?'scroll-view-item_HAct':''">
						<view class="vipboxTc_Type_name">{{item.vipName}}</view>
						<view class="vipboxTc_Type_price">￥<text>{{item.money}}</text></view>
					</view>
				</scroll-view>
			</view>
		</view>

		<view class="vipboxQy">
			<view class="vipboxQy_Tit">会员专属权益</view>
			<view class="vipboxQy_box">
				<view v-for="(item,index) in vipQylist" :key="index" class="vipboxQy_Item">
					<image :src="item.memberImg?item.memberImg:'/static/logo.png'"></image>
					<view class="vipboxQy_ItemCot">
						<view class="vipboxQy_ItemCotTitle">{{item.memberName}}</view>
						<view class="vipboxQy_ItemCotTit">{{item.memberDescribe}}</view>
					</view>
				</view>
			</view>
		</view>

		<view class="btn" @click="openPay">确认开通并支付¥{{price}}</view>
		<!-- 协议 -->
		<view class="loginXy flex align-center justify-center">
			<view class="loginXy-box flex align-center ">
				<up-checkbox activeColor="#287AFE" shape="circle" label="" name="agree" usedAlone
					v-model:checked="check">
				</up-checkbox>
				<view @click="check = !check" class="flex align-center">
					开通即代表同意
					<text @click.stop.prevent="goNav('/my/setting/vipxiyi')">《会员服务协议》</text>
				</view>
			</view>
		</view>

		<!-- 支付弹框 -->
		<up-popup :show="show" @close="show = false" :round="12" :closeable="true">
			<view class="popup_pay">
				<view class="popup_payTit">支付方式</view>
				<view class="popup_payType" v-for="(item,index) in openList" :key='index'>
					<view class="popup_payType_left">
						<image :src="item.image" mode="aspectFill"></image>
						<text>{{item.text}}</text>
					</view>
					<radio-group name="openWay">
						<label class="tui-radio">
							<radio color="#3578FE" :checked="openWay == item.id ? true : false"
								@click='selectWay(item)' />
						</label>
					</radio-group>
				</view>
				<view class="pay_btn" @click="pay()">确认支付</view>
			</view>
		</up-popup>
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
	const vipType = reactive([]);
	const price = ref(0);
	const vipTypeIndex = ref(0);
	const vipQylist = reactive([{
		name: '查看访客详情',
		tit: '随时查看谁看过你'
	}, {
		name: '名片数量不上限',
		tit: '创建名片数量不上限'
	}, {
		name: '查看认证员工',
		tit: '随时可看员工信息'
	}, {
		name: '增长名片人气值',
		tit: '让更多用户看到你'
	}]);
	const check = ref(false);
	const show = ref(false);
	const openList = reactive([]);
	const openWay = ref('');
	const isVip = ref(false); //是否开通会员
	const vipEndTime = ref(''); //会员到期时间

	onLoad(option => {

		let payType = []

		// #ifdef APP-PLUS
		payType = [{
			image: '../../static/image/zhifubao.png',
			text: '支付宝支付',
			id: 1
		}, {
			image: '../../static/image/icon_weixin.png',
			text: '微信支付',
			id: 2
		}];
		openWay.value = 1;
		// #endif

		// #ifdef MP-WEIXIN
		payType = [{
			image: '../../static/image/icon_weixin.png',
			text: '微信支付',
			id: 2
		}];
		openWay.value = 2;
		// #endif

		// #ifdef H5
		let target = navigator.userAgent.toLowerCase();
		let isWeixin = (/micromessenger/.test(target)) ? true : false;
		if (isWeixin) {
			payType = [{
				image: '../../static/image/icon_weixin.png',
				text: '微信支付',
				id: 2
			}];
			openWay.value = 2;
		} else {
			payType = [{
				image: '../../static/image/zhifubao.png',
				text: '支付宝支付',
				id: 1
			}];
			openWay.value = 1;
		}
		// #endif
		openList.splice(0, openList.length, ...payType)
		getVIPlist()
		getVIPType()
	});
	onShow(() => {
		getIsVip()
		getUserInfo()
	});
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				vipEndTime.value = res.data.vipEndTime
			}
		});
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
	// 跳转
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	};
	// 选择会员套餐
	const bindViptype = (index) => {
		vipTypeIndex.value = index
		price.value = vipType[vipTypeIndex.value].money

	};
	// 支付方式切换
	const selectWay = (item) => {
		console.log(item, '========')
		openWay.value = item.id;
	};

	// 获取会员权益
	const getVIPType = () => {
		let data = {
			pageNum: 1,
			pageSize: 99,
		}
		proxy.$http.postJson("/app/vipDetails/page", data).then(red => {
			if (red.code == 0) {
				vipType.splice(0, vipType.length, ...red.data.list);
				if (vipType.length != 0) {
					price.value = vipType[0].money
				}
			}
		});
	};
	// 获取会员权益
	const getVIPlist = () => {
		proxy.$http.get("/app/member/getMemberList").then(red => {
			if (red.code == 0) {
				vipQylist.splice(0, vipQylist.length, ...red.data.records);
			}
		});
	};
	// 打开支付
	const openPay = () => {
		if (!check.value) {
			uni.showToast({
				title: '请先阅读并同意协议',
				icon: "none"
			})
			return
		}
		show.value = true
	};
	const pay = () => {

		let vipId = vipType[vipTypeIndex.value].id
		if (openWay.value == 1) { //支付宝
			// #ifdef H5
			let data = {
				userId: uni.getStorageSync('userId'),
				id: vipId,
				classify: 5
			}
			proxy.$http.postT("/app/aliPay/payVip", data).then(res => {
				if (res.code == 0) {
					const div = document.createElement('div')
					div.innerHTML = res.data //此处form就是后台返回接收到的数据
					document.body.appendChild(div)
					document.forms[0].submit()

				}
				// isCheckPay(res.code, 'wxpay', JSON.stringify(res.data));
			});
			// #endif

			// #ifdef APP
			let data = {
				userId: uni.getStorageSync('userId'),
				id: vipId,
				classify: 4
			}
			proxy.$http.postT("/app/aliPay/payVip", data).then(res => {
				if (res.code == 0) {
					isCheckPay(res.code, 'alipay', res.data)
				}
			});
			// #endif
		} else if (openWay.value == 2) { //微信


			// #ifdef APP-PLUS
			let data = {
				userId: uni.getStorageSync('userId'),
				id: vipId,
				classify: 1
			}
			proxy.$http.postT("/app/wxPay/payVip", data).then(res => {
				if (res.code == 0) {
					isCheckPay(res.code, 'wxpay', JSON.stringify(res.data));
				}
			});
			// #endif
			// #ifdef MP-WEIXIN
			let data = {
				userId: uni.getStorageSync('userId'),
				id: vipId,
				classify: 3
			}
			proxy.$http.postT("/app/wxPay/payVip", data).then(res => {
				if (res.code == 0) {
					uni.requestPayment({
						provider: 'wxpay',
						timeStamp: res.data.timestamp,
						nonceStr: res.data.noncestr,
						package: res.data.package,
						signType: res.data.signType,
						paySign: res.data.sign,
						success: function(res) {
							console.log(res)
							uni.hideLoading();
							uni.showToast({
								title: '支付成功！',
								icon: 'none'
							});
							setTimeout(function() {
								uni.navigateBack(1)
							}, 1000)
						},
						fail: function(err) {
							uni.hideLoading();
							uni.showToast({
								title: '支付失败',
								icon: 'none'
							})
						}
					});
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
			// #endif

			// #ifdef H5
			// 微信h5支付 根据订单id获取支付信息
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) { //微信里打开
				let data = {
					userId: uni.getStorageSync('userId'),
					id: vipId,
					classify: 3
				}
				proxy.$http.postT("/app/wxPay/payVip", data).then(res => {
					if (res.code == 0) {
						callPay(res.data);
					}
				});
			}
			// #endif
		}
	};
	const callPay = (response) => {
		if (typeof WeixinJSBridge === "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response));
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response));
			}
		} else {
			onBridgeReady(response);
		}
	};
	const onBridgeReady = (response) => {
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.appid, //公众号名称，由商户传入
				"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
				"nonceStr": response.noncestr, //随机串
				"package": response.package,
				"signType": response.signType, //微信签名方式：
				"paySign": response.sign //微信签名
			},
			function(res) {
				if (res.err_msg === "get_brand_wcpay_request:ok") {
					// 使用以上方式判断前端返回,微信团队郑重提示：
					//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
					uni.showLoading({
						title: '支付成功'
					});
					setTimeout(function() {
						uni.hideLoading();
						uni.navigateBack()
					}, 1000);
				} else {
					uni.hideLoading();
				}
				WeixinJSBridge.log(response.err_msg);
			}
		);
	};
	const isCheckPay = (status, name, order) => {
		if (status == 0) {
			setPayment(name, order);
		} else {
			uni.hideLoading();
			uni.showToast({
				title: '支付信息有误',
				icon: 'none'
			});
		}
	};
	const setPayment = (name, order) => {
		uni.requestPayment({
			provider: name,
			orderInfo: order, //微信、支付宝订单数据
			success: function(res) {
				uni.hideLoading();
				uni.showLoading({
					title: '支付成功'
				});
				setTimeout(function() {
					uni.navigateBack()
				}, 1000);
			},
			fail: function(err) {
				console.log(err)
				uni.hideLoading();
			},
			complete() {
				uni.hideLoading();
			}
		});
	};
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.headTop {
		width: 100%;
		position: relative;
		background: #FFFFFF;
	}

	.headTop::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(0deg, #F5F7FF 20%, #C8DAFF 40%);
		z-index: 1;
		/* 确保在垂直渐变之上 */
	}

	.headTop::after {
		content: "";
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to bottom, transparent, #F5F7FF);
		/* 垂直渐变，结束颜色为透明 */
		z-index: 2;
		/* 确保在水平渐变之上 */
	}

	.vipbox {
		position: relative;
		z-index: 9;
		background: linear-gradient(81deg, #3578FE 0%, #5C92FF 100%);
		border-radius: 24rpx;
		margin: 0 30rpx;
		padding: 35rpx 40rpx;

		.vipboxTitle {
			 
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;

			image {
				width: 39rpx;
				height: 33rpx;
				margin-right: 10rpx;
			}
		}

		.vipboxTitl {
			font-family: Source Han Sans CN;
			font-weight: 600;
			font-size: 34rpx;
			color: #FFFFFF;
			margin-top: 10rpx;
		}

		.vipboxTit {
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #FFFFFF;
		}

		.Vipbox_btn {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;

			.Vipbox_btnkt {
				width: 171rpx;
				height: 60rpx;
				background: #FEFDFD;
				border-radius: 30rpx;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 28rpx;
				color: #3679FE;
				display: flex;
				align-items: center;
				justify-content: center;

			}

			.Vipbox_btnPrice {
				 
				font-weight: 500;
				font-size: 24rpx;
				color: #FFFFFF;

				text {
					font-size: 44rpx;
				}
			}
		}
	}

	.vipboxTc {
		margin: 20rpx 30rpx;

		.vipboxTc_Tit {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.vipboxTc_Type {


			.scroll-view_H {
				white-space: nowrap;
				width: 100%;
			}

			.scroll-view-item_HAct {
				border: 1rpx solid #3578FE !important;
				background: #E3ECFF !important;

				.vipboxTc_Type_price {
					color: #3578FE !important;
				}
			}

			.vipboxTc_Type_Tip {
				width: 142rpx;
				height: 51rpx;
				background: linear-gradient(25deg, #FFE6C0 0%, #FFCC8F 100%);
				border-radius: 0rpx 30rpx 0rpx 30rpx;
				 
				font-weight: 500;
				font-size: 26rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
				position: absolute;
				top: -20rpx;
				right: -4rpx;
			}

			.scroll-view-item_H {
				margin-top: 30rpx;
				position: relative;
				display: inline-block;
				width: 208rpx;
				background: rgba(233, 233, 233, 0.2);
				border-radius: 21rpx;
				border: 1rpx solid #EBEBEB;
				padding: 30rpx;
				margin-right: 20rpx;

				.vipboxTc_Type_name {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 30rpx;
				}

				.vipboxTc_Type_oldprice {
					 
					font-weight: 500;
					font-size: 24rpx;
					color: #999999;
					text-decoration: line-through;
				}

				.vipboxTc_Type_price {
					 
					font-weight: 500;
					font-size: 24rpx;
					color: #333333;

					text {
						font-size: 54rpx;
					}
				}
			}
		}
	}

	.vipboxQy {
		margin: 20rpx 30rpx;

		.vipboxQy_Tit {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.vipboxQy_box {
			display: flex;
			justify-content: space-between;
			flex-wrap: wrap;
			margin-top: 20rpx;

			.vipboxQy_Item {
				width: 333rpx;
				height: 118rpx;
				background: #EBF2FF;
				border-radius: 24rpx;
				display: flex;
				align-items: center;
				margin-bottom: 20rpx;


				image {
					width: 60rpx;
					height: 60rpx;
					margin-left: 20rpx;
				}

				.vipboxQy_ItemCot {
					flex: 1;
					margin-left: 10rpx;
					padding-right: 20rpx;

					.vipboxQy_ItemCotTitle {
						font-family: Source Han Sans CN;
						font-weight: 500;
						font-size: 28rpx;
						color: #2E3034;
					}

					.vipboxQy_ItemCotTit {
						font-size: 24rpx;
						color: #C5C5C7;
					}
				}
			}
		}

	}

	.btn {
		margin: 30rpx 30rpx 0;
		height: 100rpx;
		background: linear-gradient(-90deg, #3578FE 0%, #5A91FF 100%);
		border-radius: 50rpx;
		 
		font-weight: 500;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.loginXy {
		width: 100%;
		margin-top: 10rpx;

		.loginXy-box {
			width: 646rpx;
			font-size: 24rpx;
			color: #999999;

			text {
				color: #287AFE;
			}
		}
	}

	/* 支付弹框 */
	.popup_pay {
		padding: 30rpx;

		.popup_payTit {
			 
			font-weight: bold;
			font-size: 32rpx;
			color: #030303;
			text-align: center;
			margin-bottom: 30rpx;
		}

		.popup_payType {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.popup_payType_left {
				display: flex;
				align-items: center;
				margin-bottom: 40rpx;

				image {
					width: 55rpx;
					height: 55rpx;
					border-radius: 50rpx;
				}

				text {
					margin-left: 15rpx;
					 
					font-weight: bold;
					font-size: 28rpx;
					color: #030303;

				}
			}
		}


		.pay_btn {
			height: 90rpx;
			background: linear-gradient(-90deg, #3578FE 0%, #5A91FF 100%);
			border-radius: 45rpx;
			 
			font-weight: 800;
			font-size: 32rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 20rpx;
		}
	}
</style>