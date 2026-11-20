<template>
	<view>
		<up-sticky bgColor="#FFFFFF">
			<view class="Head">
				<view class="HeadTop">
					<up-search placeholder="人脉搜索" v-model="keyword" :showAction="false" height="70rpx" bgColor="#F5F5F5"
						@search="searchInput"></up-search>
				</view>
			</view>
		</up-sticky>

		<view class="seabox" v-if="isHittoy">
			<view class="flex align-center justify-between">
				<view class="seaboxTitle">搜索历史</view>
				<view @click="deteHistory()" v-if="historylist.length!=0">
					<up-icon name="trash" color="#666666" size="28"></up-icon>

				</view>
			</view>

			<view class="seaboxInfo">
				<view class="seaboxInfo_Ite" v-for="(item,index) in historylist" :key="index"
					@click="searchInput(item.keyWord)">
					{{item.keyWord}}
				</view>
			</view>
			<empty v-if="historylist.length==0"></empty>
		</view>

		<view class="" v-else>
			<view class="listbox">
				<view class="listbox_Item" v-for="(item,index) in list" :key="index" @click.stop="goDetail(item)">
					<image :src="item.cardAvatar?item.cardAvatar:'/static/logo.png'" mode="aspectFill"
						style="border-radius: 55%;"></image>
					<view class="listbox_Item_cot">
						<view class="listbox_Item_cotent">
							<view class="listbox_Item_cotentTitle">{{item.cardRemark?item.cardRemark:item.realName}}
							</view>
							<view class="listbox_Item_cotentTit">
								<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
								{{item.companyPosition}}
							</view>
							<view class="listbox_Item_cotentTit">
								<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
								{{item.companyName}}
							</view>
						</view>
					</view>

				</view>
			</view>
			<empty v-if="list.length==0"></empty>
			<!-- 加载数据 -->
			<up-loadmore :status="status" v-if="list.length>0" />
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
	const keyword = ref('');
	const isHittoy = ref(true);

	const historylist = reactive([]);
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	const status = ref('loadmore');



	onLoad(option => {
		gethistory()
	});
	onShow(() => {

	});
	// 跳转名片详情
	const goDetail = (ite) => {
		if (uni.getStorageSync('token')) {
			let messageStatus = 0
			if (ite.messageStatus) {
				messageStatus = ite.messageStatus
			}
			uni.navigateTo({
				url: '/my/Enterprise/businessCard?userId=' + ite.userId + '&id=' + ite.cardId +
					'&messageStatus=' + messageStatus
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 删除历史搜索记录 */
	const deteHistory = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.get("/app/search/deleteAppSearch").then(res => {
			if (res.code == 0) {
				// historylist.splice(0, historylist.length, ...res.data.records);
				uni.showToast({
					title: '删除成功',
					icon: 'none'
				})
				historylist.length = 0
				gethistory()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}

		});
	};
	//获取搜索历史标识
	const gethistory = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.get("/app/search/selectUserSearchList", data).then(res => {
			if (res.code == 0) {
				historylist.splice(0, historylist.length, ...res.data.records);
			}

		});
	};
	// 搜索
	const searchInput = (e) => {
		console.log(e)
		keyword.value = e
		if (keyword.value) {
			getlist()
			isHittoy.value = false
		} else {
			isHittoy.value = true
			gethistory()
		}

	};
	/* 获取搜索列表 */
	const getlist = () => {
		if (!keyword.value) {
			uni.showToast({
				title: "请输入搜索内容",
				icon: 'none'
			})
			return
		}

		uni.showLoading({
			title: '加载中...'
		})
		let data = {
			keyword: keyword.value,
			userId: uni.getStorageSync('userId'),
			page: page.value,
			limit: 10
		}
		proxy.$http.get("/app/businessCard/searchBusinessCard", data).then(res => {
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
		if (userId.value) {
			if (page.value < pages.value) {
				page.value += 1
				status.value = 'loading';
				getlist();
			}
		}
	});
	onPullDownRefresh(() => {
		if (userId.value) {
			page.value = 1;
			getlist();
		}
	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.Head {
		padding: 20rpx 30rpx;
	}

	.seabox {
		margin: 30rpx;

		.seaboxTitle {
			font-size: 30rpx;
			font-weight: bold;
		}

		.seaboxInfo {
			display: flex;
			flex-wrap: wrap;
			margin-top: 20rpx;

			.seaboxInfo_Ite {
				background: #F2F2F2;
				color: #666666;
				font-size: 26rpx;
				padding: 6rpx 15rpx;
				margin: 0 20rpx 20rpx 0;
				border-radius: 6rpx;
			}
		}
	}


	.listbox_Item {
		display: flex;
		align-items: center;
		background: #FFFFFF;
		border-radius: 18rpx;
		margin: 20rpx;
		padding: 25rpx;
		position: relative;

		image {
			width: 130rpx;
			height: 130rpx;

		}

		.listbox_Item_cot {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-left: 10rpx;

			.listbox_Item_cotent {
				height: 130rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.listbox_Item_cotentTitle {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 32rpx;
					color: #333333;
				}

				.listbox_Item_cotentTit {
					display: flex;
					align-items: center;
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;

					image {
						width: 26rpx;
						height: 26rpx;
						margin-right: 10rpx;
					}
				}
			}
		}
	}
</style>