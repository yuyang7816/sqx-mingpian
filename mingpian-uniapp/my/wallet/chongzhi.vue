<template>
	<view>
		<view class="box">
			<image :src="avatar?avatar:'/static/logo.png'"></image>
			<view class="margin-left-sm">
				<view class="price">{{money}}<text>元</text></view>
				<view class="tit">可用于平台消费</view>
			</view>
		</view>


		<view class="juanItem">
			<view class="juanItem_box" :class="current==index?'juanItem_Act':''" @click="bindJuan(index)"
				v-for="(item,index) in wallet" :key="index">
				<view class="juanItem_box_pirce">{{item.money}}元</view>
				<view class="juanItem_box_tit">	赠送{{item.couponNum}}张{{item.tbCoupon.money}}元优惠劵</view>
			</view>
		</view>

		<view class="margin " style="font-size: 26rpx;">充值后仅可用于消费，不支持退款.</view>

		<view class="payType">
			<view class="popup_payType" v-for="(item,index) in openList" :key='index'>
				<view class="popup_payType_left">
					<image :src="item.image" mode="aspectFill"></image>
					<text>{{item.text}}</text>
				</view>
				<radio-group name="openWay">
					<label class="tui-radio">
						<radio color="#FF9002" :checked="openWay == item.id ? true : false" @click='selectWay(item)' />
					</label>
				</radio-group>
			</view>
		</view>

		<view class="btn" @click="pay">确认支付</view>
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
	const money = ref(0); //余额
	const avatar = ref(''); //头像
	const wallet = reactive([]); //充值类型列表
	const price = ref(0); //充值价格
	const rechargeId = ref(''); //充值类型id
	const current = ref(0);
	const openList = reactive([]); //支付类型
	const openWay = ref('');

	// 支付方式切换
	const selectWay = (item) => {
		openWay.value = item.id;
	};
	// 充值类型切换
	const bindJuan = (index) => {
		current.value = index
	};
	// 我的余额
	const getMoney = () => {
		proxy.$http.getT("/app/userMoney/selectMyMoney").then(res => {
			if (res.code == 0 && res.data) {
				money.value = res.data.money ? res.data.money : '0'
			}
		});
	};
	// 获取充值类型
	const getMoneyList = () => {
		proxy.$http.getT("/app/rechargeConfig/getConfigInfoList").then(res => {
			if (res.code == 0) {
				let money = res.data.records
				let arr = []
				money.forEach((item, index) => {
					let data = {}
					data = item
					data.isSelect = false
					arr.push(data)
				})
				wallet.splice(0, wallet.length, ...arr);
				price.value = wallet[0].money
				rechargeId.value = wallet[0].rechargeId


			}
		});
	};
	// 充值支付
	const pay = () => {
		let data = price.value
		uni.showLoading({
			title: '支付中...'
		});
		// H5
		// #ifdef H5
		payH5(data)
		// #endif
		//APP
		// #ifdef APP
		payApp(data)
		// #endif
		//小程序
		// #ifdef MP-WEIXIN
		payMP(data)
		// #endif
	};
	//h5支付
	const payH5 = (money) => {
		let data = {
			money: money,
			rechargeId: rechargeId.value
		}
		if (openWay.value == 1) { //h5支付宝
			proxy.$http.postT("/app/aliPay/payH5Money", data).then(res => {
				if (res.code == 0) {
					const div = document.createElement('div')
					div.innerHTML = res.data //此处form就是后台返回接收到的数据
					document.body.appendChild(div)
					document.forms[0].submit()
					uni.hideLoading()
				} else {
					uni.showToast({
						icon: 'none',
						title: '支付失败!'
					});
				}
			});
		} else { //h5微信支付
			proxy.$http.postT("/app/wxPay/wxPayMpMoney", data).then(res => {
				if (res.code == 0) {
					uni.hideLoading()
					callPay(res.data);
				} else {
					uni.showToast({
						icon: 'none',
						title: '支付失败!'
					});
				}
			});
		}
	};
	//app支付
	const payApp = (money) => {
		let data = {
			money: money,
			rechargeId: rechargeId.value
		}
		if (openWay.value == 1) { //app支付宝
			proxy.$http.postT("/app/aliPay/payAppMoney", data).then(ret => {
				isCheckPay(ret.code, 'alipay', ret.data);
			});
		} else { //app微信支付
			proxy.$http.postT("/app/wxPay/payAppMoney", data).then(ret => {
				isCheckPay(ret.code, 'wxpay', JSON.stringify(ret.data));
			});
		}
	};
	//微信支付
	const payMP = (money) => {
		let data = {
			money: money,
			rechargeId: rechargeId.value
		}
		proxy.$http.postT('/app/wxPay/wxPayJsApiMoney', data).then(ret => {
			uni.hideLoading()
			uni.requestPayment({
				provider: 'wxpay',
				timeStamp: ret.data.timestamp,
				nonceStr: ret.data.noncestr,
				package: ret.data.package,
				// signType: ret.data.signType,
				signType: 'MD5',
				paySign: ret.data.sign,
				success: function(suc) {
					uni.showToast({
						title: '支付成功',
						icon: 'success'
					})
					setTimeout(d => {
						uni.navigateBack(1)
					}, 1000);
				},
				fail: function(err) {
					uni.showToast({
						title: '支付失败',
						icon: 'none'
					})
				}
			});
		});
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
		if (!response.package) {
			return;
		}
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.appid, //公众号名称，由商户传入
				"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
				"nonceStr": response.noncestr, //随机串
				"package": response.package,
				"signType": 'MD5', //微信签名方式：
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
				console.log(res)
				uni.hideLoading();
				uni.showLoading({
					title: '支付成功'
				});
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
	onLoad(option => {
		avatar.value = uni.getStorageSync('avatar');
		getMoney();
		getMoneyList();

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
		background: #F5F5F5;
	}

	.box {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		display: flex;
		align-items: center;
		margin: 20rpx 30rpx;

		image {
			width: 80rpx;
			height: 80rpx;
			border-radius: 50%;
		}

		.price {
			font-size: 34rpx;
			font-weight: bold;

			text {
				font-size: 26rpx;
				font-weight: 500;
			}
		}

		.tit {
			font-size: 26rpx;
			font-weight: 500;
		}


	}

	.juanItem {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx 30rpx 0;
		margin: 20rpx 30rpx;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;


		.juanItem_box {
			color: #1e1f31;
			background-color: #f7f7f7;
			width: 41%;
			border: 1px solid #f7f7f7;
			border-radius: 18rpx;
			padding: 20rpx;
			margin-bottom: 30rpx;
		}

		.juanItem_Act {
			background: #FFFFFF;
			border: 1px solid #FF9002;
			color: #FF9002;
		}

		.juanItem_box_pirce {
			font-size: 28rpx;
		}

		.juanItem_box_tit {
			font-size: 26rpx;
		}

	}

	.payType {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx 30rpx 0;

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

	}

	.btn {
		height: 90rpx;
		background: #FF9002;
		border-radius: 55rpx;
		 
		font-weight: 500;
		font-size: 30rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 60rpx 30rpx;
	}
</style>