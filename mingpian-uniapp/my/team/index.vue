<template>
	<view class="">
		<view class="view1">
			<view class="view1_top">
				<view class="view1_top_itm">
					<view style="font-size: 40rpx;">{{ teamCount?teamCount:0 }}</view>
					<view>邀请好友（人）</view>
				</view>
				<view class="view1_top_itm" @click="goDet">
					<view style="font-size: 40rpx;">{{ teamMoney?teamMoney:0 }}</view>
					<view>我的收益</view>
				</view>
			</view>
			<view class="view1_tit" v-if="yijiyongjin">*可得一级好友消费奖励的{{yijiyongjin*100}}%</view>
			<view class="view1_tit" v-if="erjiyongjin">*可得二级好友消费奖励的{{erjiyongjin*100}}%</view>

			<button class="yaoqing_btn" @click="goYao">邀请好友</button>
		</view>


		<view class="view2" style="">
			<!-- <view class="navbar" v-if="erjiyongjin>0">
				<view class="nav-item" :class="{current: type == 1}" @click="changeList(1)">
					<view class="text-center">
						<view>一级( {{oneTeamCount}} )</view>
						<view class="line" v-if="type == 1"></view>
					</view>
				</view>
				<view class="nav-item" :class="{current: type == 2}" @click="changeList(2)">
					<view class="text-center">
						<view>二级( {{twoTeamCount}} )</view>
						<view class="line" v-if="type == 2"></view>
					</view>
				</view>
			</view> -->

			<view class="tabbox">
				<view style="width: 15%;">编号</view>
				<view style="width: 20%;text-align: center;">头像</view>
				<view style="width: 40%;">昵称</view>
				<view style="width: 35%;text-align: center;">时间</view>
			</view>
			<view class="box" v-for="(item, index) in list" :key="index" s>
				<view style="width: 15%;" :style="index<=3?'text-align: left;':'text-align: center;'">
					<image src="../static/paihangbang/one.png" style="width: 52rpx;height:42rpx" v-if="index==0">
					</image>
					<image src="../static/paihangbang/two.png" style="width: 52rpx;height:42rpx" v-else-if="index==1">
					</image>
					<image src="../static/paihangbang/three.png" style="width: 52rpx;height:42rpx" v-else-if="index==2">
					</image>
					<view style="margin-top: 6rpx;" v-else>{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;">
					<image :src="item.inviteePortrait?item.inviteePortrait:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;margin-left: 15rpx;border-radius: 55%"></image>
				</view>
				<view style="width: 40%;display: flex;flex-direction: row;align-items: center;">
					<view style="width: 90%;overflow: hidden;">{{ item.inviteeName }}</view>
				</view>
				<view style="width: 35%;text-align: center;">{{ item.inviteTime }}</view>
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

	const teamCount = ref(0); //邀请好友人数
	const teamMoney = ref(0); //我的收益
	const oneTeamCount = ref(0); //一级邀请好友人数
	const twoTeamCount = ref(0); //二级邀请好友人数

	const yijiyongjin = ref(0); //我的收益
	const erjiyongjin = ref(0); //我的收益
	const list = reactive([]); //列表
	const pages = ref(1); //分页
	const page = ref(1); //分页
	const limit = ref(20);
	const type = ref(1);
	//加载状态
	const status = ref('loadmore');



	// 跳转我的收益
	const goDet = () => {
		uni.navigateTo({
			url: '/my/team/earnings'
		})
	};
	// 跳转邀请好友
	const goYao = () => {
		uni.navigateTo({
			url: '/pages/my/invitationUser'
		})
	};
	// 获取用户佣金
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				yijiyongjin.value = res.data.divideScale ? res.data.divideScale : 0;
				erjiyongjin.value = res.data.divideTwoScale ? res.data.divideTwoScale : 0;
				uni.setStorageSync('avatar', res.data.avatar)
				uni.setStorageSync('invitationCode', res.data.staffInvitationCode)
				uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
				uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
			}
		});
	};
	// 邀请人数
	const getTeamMoney = () => {
		proxy.$http.getT('/app/invite/selectInviteMoney').then(res => {
			if (res.code == 0) {
				teamCount.value = res.data.inviteCount
				teamMoney.value = res.data.inviteMoney
				// teamCount.value = parseInt(Number(res.data.countChildren) + Number(res.data
				// 	.countChildrenChildren))
				// teamMoney.value = res.data.teamMoney.toFixed(2)
				// oneTeamCount.value = res.data.countChildren
				// twoTeamCount.value = res.data.countChildrenChildren
		
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
		proxy.$http.getT("/app/businessCard/myInvite", data).then(res => {
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
			status.value = 'nomore';
			uni.stopPullDownRefresh();
		});
	};
	onLoad(option => {
		getUserInfo(); //用户信息
		getTeamMoney(); //邀请信息
		getList();
	});
	onShow(() => {

	});
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
		background: #f2f2f2;
	}

	.view1 {
		background-color: #FFFFFF;
		border-radius: 20rpx;
		margin: 20rpx 30rpx;
		padding: 40rpx 30rpx;

		.view1_top {
			display: flex;

			.view1_top_itm {
				font-size: 28rpx;
				flex: 1;
				text-align: center;
			}
		}

		.view1_tit {
			margin: 30rpx 0;
			font-size: 28rpx;
			color: #000000;
		}

		.yaoqing_btn {
			width: 100%;
			height: 80rpx;
			background: linear-gradient(-90deg, #3578FE 0%, #5A91FF 100%);
			border-radius: 40rpx;
			 
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 40rpx;
		}
	}

	.view2 {
		background-color: #FFFFFF;
		margin: 20rpx 30rpx;
		border-radius: 20rpx;
		padding: 20rpx;

		.navbar {
			display: flex;
			height: 40px;
			border-radius: 20rpx;
			position: relative;
			z-index: 10;
			margin: 0 24rpx;
			border-radius: 20rpx;
			overflow: hidden;

			.nav-item {
				flex: 1;
				display: flex;
				justify-content: center;
				align-items: center;
				height: 100%;
				font-size: 15px;
				 
				font-weight: 500;
				font-size: 28rpx;
				color: #999999;
				position: relative;

				.line {
					width: 56rpx;
					height: 8rpx;
					background: #FF9002;
					border-radius: 4rpx;
					margin: 10rpx auto 0;
				}

				&.current {
					 
					font-weight: bold;
					font-size: 28rpx;
					color: #1F1F1F;
					background-color: #FFFFFF;

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