<template>
	<view>
		<view class="listItem" v-for="(item,index) in list" :key="index">
			<view class="listItem_data">{{item.createTime}}</view>
			<view class="listItem_box">
				<image :src="item.avatar?item.avatar:'/static/logo.png'"></image>
				<view class="listItem_boxCot">
					<view class="listItem_boxCotText">
						{{item.userName}} <text>通过你分享注册</text>
					</view>
					<view class="listItem_boxCotAll" @click="goDetail(item)" v-if="item.cardId">
						查看名片
						<image src="/static/image/my/huiright.png" mode="widthFix"></image>
					</view>
				</view>
			</view>
		</view>

		<empty v-if="list.length==0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="list.length>0" />
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
	const page = ref(1);
	const pages = ref(1);
	const status = ref('loadmore');
	const list = reactive([]);
	const userId = ref('');
	

	onLoad(option => {
		userId.value = uni.getStorageSync('userId')
	});
	onShow(() => {
		getInfo()
	});
	// 跳转名片详情
	const goDetail = (ite) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/my/Enterprise/businessCard?userId=' + ite.inviteeId + '&id=' + ite.cardId

			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	
	const getInfo = () => {
		let data = {
			pageSize: 10,
			pageNum: page.value,
		}
		proxy.$http.get('/app/inviteRecord/myInviteRecord', data).then(res => {
			if (res.code == 0) {
				pages.value = res.page.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				res.page.records.forEach(item => {
					item.check = false
				})
				if (page.value == 1) {
					list.splice(0, list.length, ...res.page.records);
				} else {
					list.push(...res.page.records);
				}
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh()
		});
	};
	onReachBottom(() => {
		if (userId.value) {
			if (page.value < pages.value) {
				page.value += 1
				status.value = 'loading';
				getInfo();
			}
		}
	});
	onPullDownRefresh(() => {
		if (userId.value) {
			page.value = 1;
			getInfo();
		}
	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.listItem {
		padding: 30rpx 40rpx;
		border-bottom: 1px solid #F0F0F0;

		.listItem_data {
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #333333;
		}

		.listItem_box {
			display: flex;
			align-items: center;
			margin-top: 15rpx;

			image {
				width: 60rpx;
				height: 60rpx;
				border-radius: 55%;
			}

			.listItem_boxCot {
				display: flex;
				align-items: center;
				flex: 1;
				justify-content: space-between;
				margin-left: 10rpx;

				.listItem_boxCotText {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 30rpx;
					color: #287AFE;


					text {
						font-family: Source Han Sans CN;
						font-weight: 500;
						font-size: 26rpx;
						color: #666666;
						margin-left: 10rpx;
					}
				}

				.listItem_boxCotAll {
					display: flex;
					align-items: center;
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 26rpx;
					color: #999999;

					image {
						width: 25rpx;
						height: 25rpx;
					}
				}
			}
		}
	}
</style>