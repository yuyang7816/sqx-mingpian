<template>
	<view>
		<view class="bgColor"> </view>
		<view v-for="(item,index) in list" :key="index" @click="bindSwitch(item)">
			<view class="box" :style="{'background-image':'url('+item.cardImg+')'}">
				<view class="boxbg" :style="item.cardImg?'':'background:#FFFFFF'">
					<view class="boxhead">
						<view class="boxheadTop">
							<view class="boxheadTop_Title">
								<text>{{item.realName}}</text>
								<view class="boxheadTop_Title_label">
									<view class="boxheadTop_Title_labelStart">
										<image src="/static/image/index/xingxing.png"></image>
									</view>
									<view class="boxheadTop_Title_labelZW">{{item.companyPosition}}</view>
								</view>
							</view>
							<view class="boxhead_Item margin-top">
								<image src="/static/image/index/qiye.png" mode="widthFix"></image>
								<text>{{item.companyName}}</text>
							</view>
						</view>
						<image :src="item.cardType==1?item.cardAvatar: item.companyLogo" mode="aspectFill"
							style="border-radius: 55%;"></image>
					</view>
					<view class="boxhead_Item margin-top-sm">
						<image src="/static/image/index/add.png" mode="widthFix"></image>
						<text>{{item.cardType==1?item.address:item.companyAddress}}</text>
					</view>

					<view class="boxhead_Item margin-top-sm">
						<image src="/static/image/index/phone.png" mode="widthFix"></image>
						<text>{{item.cardPhone}}</text>
					</view>
					<view class="statusCard" v-if="item.auditStatus==0">待审核</view>
					<view class="statusCard" v-if="item.cardStatus==1">
						<up-icon name="checkmark" color="#FFFFFF" size="28"></up-icon>
					</view>
					<view class="statusCard" v-if="item.auditStatus==2">已拒绝</view>
				</view>
			</view>
		</view>
		<view class="addCard" @click="goNewAdd()" :style="list.length==0?'border: 1px solid #FFFFFF;':''">
			+添加新的名片
		</view>

		<up-action-sheet :actions="listSheet" :show="show" cancelText="取消" @close="show = false"
			@select="select"></up-action-sheet>
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
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	const listSheet = ref([{
		name: '个人名片',
		id: 1
	}, {
		name: '企业名片',
		id: 2
	}]);
	const show = ref(false);
	const userName = ref('');
	const avatar = ref('');




	onLoad(option => {

	});
	onShow(() => {
		getlist()
		getUserInfo()
	});
	const select = (e) => {
		console.log(e)
		if (e.id == 1) {
			uni.navigateTo({
				url: '/pages/my/userInfo'
			})
		} else {
			uni.navigateTo({
				url: '/my/Enterprise/PublishOne'
			})
		}
	};
	// 切换使用名片
	const bindSwitch = (ite) => {
		if (ite.auditStatus == 1) {
			let userId = uni.getStorageSync('userId')
			proxy.$http.getT("/app/businessCard/switchCard/" + ite.cardId + "?userId=" + userId).then(res => {
				if (res.code == 0) {
					page.value = 1
					getlist()
				} else {
					if (ite.cardType == 1) {
						uni.navigateTo({
							url: '/pages/my/userInfo?cardId=' + ite.cardId
						})
					} else {
						uni.navigateTo({
							url: '/my/Enterprise/PublishOne?cardId=' + ite.cardId
						})
					}
				}
			});
		} else if (ite.auditStatus == 2) {
			uni.navigateTo({
				url: '/my/Enterprise/PublishOne?cardId=' + ite.cardId
			})
		}

	};
	// 跳转添加名片
	const goNewAdd = () => {

		if (userName.value&&avatar.value) {
			show.value = true
			return
		} else {
			uni.showModal({
				title: '提示',
				content: '请先去完善个人信息',
				showCancel: false,
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/my/info'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			});
		}
		// uni.navigateTo({
		// 	url: '/my/Enterprise/PublishOne'
		// })
	};
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				avatar.value = res.data.avatar

			}
		});
	};
	// 获取名片列表
	const getlist = () => {
		let userId = uni.getStorageSync('userId')
		let data = {
			userId: userId,
			pageNum: page.value,
			pageSize: limit.value
		}

		proxy.$http.postJson("/app/businessCard/queryAllList", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pageNum
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.list);
				} else {
					list.push(...res.data.list);
				}
			}
			uni.stopPullDownRefresh()

		});
	};
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			getlist();
		}
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getlist();
	});
</script>

<style lang="scss" scoped>
	page {
		background: #F6F7FB;
	}

	.bgColor {
		background: linear-gradient(to bottom, #287AFE 20%, #F6F7FB 60%);
		height: 500rpx;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		width: 100%;
		z-index: 1;
	}

	.box {
		background-size: 100% 100%;
		background-repeat: no-repeat;
		// background: linear-gradient(to bottom, #F0F5FF, #FFFFFF);
		box-shadow: 0rpx 8rpx 16rpx 0rpx rgba(91, 109, 137, 0.11);

		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		position: relative;
		z-index: 9;

		.boxbg {
			padding: 30rpx;
			background: rgba(255, 255, 255, 0.3);
			border-radius: 24rpx;
		}

		.statusCard {
			width: 88rpx;
			height: 55rpx;
			background: #287AFE;
			border-radius: 24rpx 0 24rpx 0;
			font-size: 26rpx;
			color: #FFFFFF;
			display: flex;
			justify-content: center;
			align-items: center;
			padding: 4rpx 10rpx;
			position: absolute;
			bottom: 0;
			right: 0;
		}

		.boxhead {
			display: flex;
			align-items: center;
			justify-content: space-between;

			image {
				width: 154rpx;
				height: 154rpx;

			}

			.boxheadTop {
				.boxheadTop_Title {
					display: flex;
					align-items: center;

					text {
						font-family: Source Han Sans CN;
						font-weight: 500;
						font-size: 46rpx;
						color: #333333;
					}

					.boxheadTop_Title_label {
						margin-left: 10rpx;

						.boxheadTop_Title_labelStart {
							display: flex;
							align-items: center;
							justify-content: center;
							// box-shadow: 0rpx 0rpx 6rpx 0rpx #6A87B6;
							width: 44rpx;
							// height: 44rpx;
							padding: 6rpx 0;
							background: #287AFE;
							border-radius: 50%;
							float: left;
							border: 1px solid #FFFFFF;

							image {
								width: 30rpx;
								height: 30rpx;
							}
						}

						.boxheadTop_Title_labelZW {
							background: #287AFE;
							border-radius: 42rpx;

							font-weight: 500;
							font-size: 24rpx;
							color: #FFFFFF;
							padding: 6rpx 20rpx 6rpx 40rpx;
							margin-left: 10px;

						}
					}
				}
			}
		}

		.boxhead_Item {
			display: flex;
			align-items: center;

			image {
				width: 24rpx;
				height: 24rpx;
			}

			text {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #333333;
				margin-left: 8rpx;
			}
		}

	}

	.addCard {
		margin: 20rpx 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 200rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		border: 1px solid #4392FD;

		font-weight: bold;
		font-size: 32rpx;
		color: #4392FD;
		position: relative;
		z-index: 9;
	}
</style>