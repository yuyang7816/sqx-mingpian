<template>
	<view>
		<view class="listItem" v-for="(item,index) in list" :key="index" >
			<view class="listItem_data">{{item.createTime}}</view>
			<view class="listItem_box">
				<!-- <image src="/static/logo.png"></image> -->
				<image :src="item.cardAvatar?item.cardAvatar:'/static/logo.png'"></image>
				<view class="listItem_boxCot">
					<view class="listItem_boxCotText">
						{{item.realName}} <text>加入了企业</text>
					</view>
					<view class="listItem_boxCotAll" @click="goDetail(item)" v-if="item.staffCardId">
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
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	const status = ref('loadmore');
	const userId = ref('');

	const cardId = ref('');



	onLoad(option => {
		uni.showLoading({
			title: '加载中...'
		})
		if (option.id) {
			cardId.value = option.id
			getDynamic()
		}
	});
	onShow(() => {

	});
	// 跳转名片详情
	const goDetail = (itre) => {
		uni.navigateTo({
			url: '/my/Enterprise/businessCard?id='+itre.staffCardId
		})
	};
	// 查询企业员工动态信息
	const getDynamic = () => {
		let userId = uni.getStorageSync('userId')
		let data = {
			pageNum: page.value,
			pageSize: limit.value,
			userId: userId,
			cardId:cardId.value
		}
		proxy.$http.get("/app/comRecord/page", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records);
				}
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh()
			uni.hideLoading()
		});
	};
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getDynamic();
		}
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getDynamic();
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