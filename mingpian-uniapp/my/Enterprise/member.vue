<template>
	<view>

		<view class="listbox">
			<view class="listbox_Item" v-for="(item,index) in list" :key="index" @click.stop="goDetail(item)" >
				<image v-if="item.cardType==1" :src="item.cardAvatar?item.cardAvatar:'/static/logo.png'"
					mode="aspectFill" style="border-radius: 55%;">
				</image>
				<image v-if="item.cardType==2" :src="item.companyLogo?item.companyLogo:'/static/logo.png'"
					mode="aspectFill" style="border-radius: 55%;"></image>

				<view class="listbox_Item_cot">
					<view class="listbox_Item_cotent">
						<view class="listbox_Item_cotentTitle">
							{{item.realName}}
							<text v-if="item.isAdmin">创建者</text>
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
	// 创建响应式数据  
	const tablist = reactive([{
		name: '已加入成员'
	}, {
		name: '申请加入'
	}]);
	const current = ref(0);
	const activeStyle = ref({
		'color': '#287AFE',
		'font-size': '28rpx',
		'font-weight': '800'
	});
	const inactiveStyle = ref({
		'color': '#666666',
		'font-size': '28rpx'
	})
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const list = reactive([]);
	const companyInfo = ref({});

	const userId = ref('');


	onLoad(option => {
		userId.value = uni.getStorageSync('userId')
		companyInfo.value = uni.getStorageSync('companyInfo')
		if (companyInfo.value) {
			getlist()
		}

	});
	onShow(() => {

	});
	// 跳转名片详情
	const goDetail = (ite) => {
		uni.navigateTo({
			url: '/my/Enterprise/businessCard?id=' + ite.cardId
		})
	};
	//查询企业全部成员
	const getlist = () => {
		let data = {
			pageNum: page.value,
			pageSize: limit.value,
			cardId: companyInfo.value.cardId
		}
		proxy.$http.postJson("/app/company/queryStaff", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pageNum
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				res.data.list.forEach(item => {
					if (item) {
						item.check = false
					}
				})
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.list);
				} else {
					list.push(...res.data.list);
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
				getlist();
			}
		}
	});
	onPullDownRefresh(() => {
		if (userId.value) {
			page.value = 1;
			getlist();
		} else {
			uni.stopPullDownRefresh()
		}
	});
</script>

<style lang="scss">
	page {
		background: #F5F5FB;
	}

	.Head {
		padding: 20rpx 30rpx;
	}

	.listbox {

		.listbox_Item {
			display: flex;
			align-items: center;
			background: #FFFFFF;
			border-radius: 18rpx;
			margin: 20rpx;
			padding: 25rpx;


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

						text {
							background: linear-gradient(40deg, #FEF2DC 0%, #FEE4BF 100%);
							border-radius: 6rpx;
							font-family: Source Han Sans CN;
							font-weight: 400;
							font-size: 20rpx;
							color: #84663A;
							padding: 4rpx 10rpx;
						}
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
	}
</style>