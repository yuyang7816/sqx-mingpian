<template>
	<view>
		<view class="box">
			<view class="boxbg">
				<view class="boxbg_tit">总资产(元)</view>
				<view class="boxbg_price">{{money}} <text>元</text></view>
				<!-- <view class="boxbg_ljtx">累计提现:325.26元</view> -->
			</view>

			<view class="boxbot">
				<view class="boxbot_left">
					<view class="boxbot_lefttit">总资产(元)</view>
					<view class="boxbot_leftnum">{{money}}</view>
				</view>
				<view class="boxbot_tx" @click="Chongzhi">充值</view>
			</view>
		</view>

		<view class="box margin-top-sm">
			<view class="head">
				<view class="head_line"></view>
				<view class="head_tit">账单明细</view>
			</view>
			<view style="width: 100%;height: 1rpx;background: #E6E6E6;margin: 20rpx 0;"></view>

			<view class="itembox" v-for="(item,index) in list" :key="index">
				<view class="itemboxleft">
					<view class="itemboxleft_tit">{{item.title}}</view>
					<view class="itemboxleft_data">{{item.createTime}}</view>
				</view>
				<view class="itembox_price">
					<text v-if="item.type == 1">+</text>
					<text v-if="item.type == 2">-</text>
					{{item.money}}
				</view>
			</view>

			<up-loadmore :status="status" v-if="list.length>0" />
			<empty v-if="list.length==0" contont="暂无明细"></empty>

		</view>

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
	const money = ref(0);
	const page = ref(1);
	const limit = ref(10);
	const pages = ref(1);
	const list = reactive([]);
	//加载状态
	const status = ref('loadmore');

	// 跳转充值
	const Chongzhi = () => {
		uni.navigateTo({
			url: '/my/wallet/chongzhi'
		})
	};
	// 获取余额
	const getMoney = () => {
		proxy.$http.getT('/app/userMoney/selectMyMoney').then(res => {
			if (res.code == 0 && res.data) {
				money.value = res.data.money
			}
		});
	};
	const getlist = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userType: 1
		}
		proxy.$http.getT('/app/userMoney/queryUserMoneyDetails', data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records);
				}
			}
			uni.stopPullDownRefresh();
		});
	};
	onLoad(option => {
		getMoney();
		getlist();
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {
		page.value = 1;
		getlist();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getlist();
		}
	});
</script>

<style lang="scss">
	page {
		background: #F5F5F5;
	}

	.box {
		background: #FFFFFF;
		padding: 30rpx;
	}

	.boxbg {
		height: 312rpx;
		background: linear-gradient(90deg, #FFBB29 0%, #FF9002 100%);
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;

		.boxbg_tit {
			 
			font-weight: 500;
			font-size: 32rpx;
			color: #FFFFFF;
		}

		.boxbg_price {
			font-family: DINPro;
			font-weight: 500;
			font-size: 68rpx;
			color: #FFFFFF;

			text {
				font-size: 30rpx;
			}
		}

		.boxbg_ljtx {
			 
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;

		}
	}

	.boxbot {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 30rpx;

		.boxbot_left {

			.boxbot_lefttit {
				 
				font-weight: 500;
				font-size: 28rpx;
				color: #333333;
			}

			.boxbot_leftnum {
				font-family: DINPro;
				font-weight: 500;
				font-size: 48rpx;
				color: #333333;
			}
		}

		.boxbot_tx {
			width: 160rpx;
			height: 70rpx;
			background: #FF9002;
			border-radius: 8rpx;
			 
			font-weight: 500;
			font-size: 32rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	.head {
		display: flex;
		align-items: center;

		.head_line {
			width: 6rpx;
			height: 32rpx;
			background: #FF9002;
			margin-right: 15rpx;
		}

		.head_tit {
			 
			font-weight: bold;
			font-size: 32rpx;
			color: #333333;
		}
	}

	.itembox {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 30rpx;

		.itemboxleft {

			.itemboxleft_tit {
				 
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
			}

			.itemboxleft_data {
				 
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.itembox_price {
			font-family: DINPro;
			font-weight: 500;
			font-size: 38rpx;
			color: #333333;
		}
	}
</style>