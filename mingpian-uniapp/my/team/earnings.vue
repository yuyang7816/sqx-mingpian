<template>
	<view class="">
		<view class="view1">
			<view class="view1_box">
				<view class="itembox" v-if="erjiyongjin>0">
					<view class="itembox_num">{{ teamMoney }}</view>
					<view class="itembox_tit">总收益</view>
				</view>
				<view class="itembox" v-else>
					<view class="itembox_num">{{ teamMoney }}</view>
					<view class="itembox_tit">总收益</view>
				</view>
				<view class="itembox" v-if="erjiyongjin>0">
					<view class="itembox_num">{{ oneTeamMoney }}</view>
					<view class="itembox_tit">一级收益</view>
				</view>
				<view class="itembox" v-if="erjiyongjin>0">
					<view class="itembox_num">{{ twoTeamMoney }}</view>
					<view class="itembox_tit">二级收益</view>
				</view>
			</view>

			<view class="view1_btb">
				<view class="view1_btban" @click="goTixian">提现</view>
				<view class="view1_btban" @click="goMingxi">收益明细</view>
			</view>
		</view>

		<view class="view2">
			<!-- <view class="navbar" v-if="erjiyongjin>0">
				<view class="nav-item" :class="{current: type == 1}" @click="changeList(1)">
					<view class="text-center">
						<view>一级</view>
						<view class="line"></view>
					</view>
				</view>
				<view class="nav-item" :class="{current: type == 2}" @click="changeList(2)">
					<view class="text-center">
						<view>二级</view>
						<view class="line"></view>
					</view>
				</view>
			</view> -->
			<view class="tabbox">
				<view style="width: 15%;">编号</view>
				<view style="width: 20%;">头像</view>
				<view style="width: 45%;">昵称</view>
				<view style="width: 30%;text-align: center;">奖励</view>
			</view>
			<view class="box" v-for="(item, index) in list" :key="index">
				<view style="width: 15%;">
					<image src="../static/paihangbang/one.png" style="width: 52rpx;height:42rpx" v-if="index==0">
					</image>
					<image src="../static/paihangbang/two.png" style="width: 52rpx;height:42rpx" v-else-if="index==1">
					</image>
					<image src="../static/paihangbang/three.png" style="width: 52rpx;height:42rpx" v-else-if="index==2">
					</image>
					<view style="font-size: 28rpx;margin-top: 6rpx;margin-left: 15rpx;" v-else>{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;">
					<image :src="item.avatar?item.avatar:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;border-radius: 55%;"></image>
				</view>
				<view style="width: 45%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28rpx;width: 90%;overflow: hidden;">{{ item.userName }}</view>
				</view>
				<view style="width: 30%;text-align: center;display: flex;justify-content: center;align-items: center;">
					<view style="font-size: 32rpx;color: #333333;">￥{{ item.amount ? item.amount : 0 }}</view>
				</view>
			</view>
			<!-- 加载更多 -->
			<up-loadmore :status="status" v-if="list.length>0" />
			<!-- 暂无数据 -->
			<empty v-if="list.length==0" content="暂无数据"></empty>
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
		onReachBottom,
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	// 占位图
	import empty from '@/components/empty/empty.vue';

	const teamMoney = ref(0); //我的收益
	const oneTeamMoney = ref(0); //一级邀请好友收益
	const twoTeamMoney = ref(0); //二级邀请好友收益
	const yijiyongjin = ref(0); //我的收益
	const erjiyongjin = ref(0); //我的收益

	const list = reactive([]); //列表
	const pages = ref(1); //分页
	const page = ref(1); //分页
	const limit = ref(20);
	const type = ref(1);
	//加载状态
	const status = ref('loadmore');

	onLoad(option => {
		getUserInfo(); // 获取用户佣金
		getTeamMoney(); // 邀请收益
		getList(); // 获取邀请列表
	});
	onShow(() => {

	});


	// 去提现
	const goTixian = () => {
		uni.navigateTo({
			url: '/my/wallet/tixian'
		})
	};
	// 跳转收益明细
	const goMingxi = () => {
		uni.navigateTo({
			url: '/my/wallet/moneydetail'
		})
	};
	// 获取用户佣金
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				yijiyongjin.value = res.data.divideScale ? res.data.divideScale : 0;
				erjiyongjin.value = res.data.divideTwoScale ? res.data.divideTwoScale : 0;
			}
		});
	};
	// 邀请收益
	const getTeamMoney = () => {
		proxy.$http.getT('/app/invite/selectInviteMoney').then(res => {
			if (res.code == 0) {
				if (res.data.inviteMoney) {
					teamMoney.value = res.data.inviteMoney.toFixed(2)
				}
				// oneTeamMoney.value = res.data.oneMoney.toFixed(2)
				// twoTeamMoney.value = res.data.twoMoney.toFixed(2)

			}
		});
	};
	// 切换一级二级
	const changeList = (index) => {
		page.value = 1
		type.value = index
		getList()
	};
	// 获取邀请列表
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			// type: type.value,
		}
		proxy.$http.getT("/app/invite/invitePage", data).then(res => {
			if (res.code == 0) {
				pages.value = res.page.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					list.splice(0, list.length, ...res.page.records);
				} else {
					list.push(...res.page.records);
				}
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh();
		});
	};
	onPullDownRefresh(() => {
		page.value = 1;
		getList();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getList();
		}
	});
</script>

<style lang="scss">
	page {
		background: #F2F2F2;
	}

	.view1 {
		background-color: #FFFFFF;
		margin: 20rpx 30rpx;
		border-radius: 20rpx;
		padding: 60rpx 0 50rpx;

		.view1_box {
			padding: 0 50rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}

		.itembox {

			text-align: center;

			.itembox_num {
				color: #3578FE;
				font-size: 40rpx;
			}

			.itembox_tit {
				font-size: 28rpx;
				margin-top: 10rpx;
			}
		}

		.view1_btb {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 20rpx;

			.view1_btban {
				background: linear-gradient(-90deg, #3578FE 0%, #5A91FF 100%);
				color: #FFFFFF;
				flex: 1;
				height: 70rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				border-radius: 55rpx;
				margin: 30rpx 15rpx 0;
			}


		}

	}

	.view2 {
		background-color: #FFFFFF;
		margin: 20rpx 30rpx;
		border-radius: 20rpx;
		padding: 20rpx 30rpx;

		.navbar {
			display: flex;
			height: 40px;
			border-radius: 20rpx;
			// box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
			position: relative;
			z-index: 10;
			overflow: hidden;

			.nav-item {
				flex: 1;
				display: flex;
				justify-content: center;
				align-items: center;
				height: 100%;
				font-size: 15px;
				// color: $font-color-dark;
				position: relative;

				.line {
					width: 56rpx;
					height: 8rpx;
					background-color: #FFFFFF;
					border-radius: 4rpx;
					margin: 10rpx auto 0;
				}

				&.current {
					background-color: #FFFFFF;

					.line {
						width: 56rpx;
						height: 8rpx;
						background: linear-gradient(-90deg, #3578FE 0%, #5A91FF 100%);
						border-radius: 4rpx;
						margin: 10rpx auto 0;
					}

					&:after {
						content: '';
						position: absolute;
						left: 50%;
						bottom: 0;
						transform: translateX(-50%);
						width: 44px;
						height: 0;
						// border-bottom: 2px solid $base-color;
					}
				}
			}
		}
	}

	.tabbox {
		display: flex;
		flex-direction: row;
		padding: 20rpx;
		color: #999999;
		font-size: 28rpx;
	}

	.box {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		font-size: 28rpx;
		margin-bottom: 16rpx;
	}
</style>