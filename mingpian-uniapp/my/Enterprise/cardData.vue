<template>
	<view>
		<view class="bgColor">
			<view class="CardboxNum">
				<view class="CardboxNum_Item">
					<view class="CardboxNum_ItemNum">
						{{totalVisitCount?totalVisitCount:0}}<text>次</text>
					</view>
					<view class="CardboxNum_ItemTit">累计访问量</view>
				</view>
				<view class="CardboxNum_Item">
					<view class="CardboxNum_ItemNum">
						{{todayVisitCoune?todayVisitCoune:0}}<text>次</text>
					</view>
					<view class="CardboxNum_ItemTit">今日访问量</view>
				</view>
				<view class="CardboxNum_Item">
					<view class="CardboxNum_ItemNum">
						{{totalShareCount?totalShareCount:0}}<text>次</text>
					</view>
					<view class="CardboxNum_ItemTit">分享名片</view>
				</view>
			</view>
		</view>

		<view class="tabbox margin-top-sm">
			<up-tabs :list="listTab" @click="clickTab" :current="current" :activeStyle="activeStyle"
				:inactiveStyle="inactiveStyle"></up-tabs>
		</view>

		<view class="" v-if="current==1">
			<view v-for="(item,index) in Visitlist" :key="index" class="Infobox" @click="goDetail(item)">
				<image :src="item.avatar?item.avatar:'/static/logo.png'"></image>
				<view class="Infobox_conte">
					<view class="Infobox_conteTitle">{{item.visitUserName}}</view>
					<view class="" style="color: #666666;font-size: 26rpx;">{{item.visitTime}}</view>
					<!-- <view class="Infobox_conteTit">来源：我分享到{{item.platform}}好友</view> -->
				</view>
			</view>

		</view>
		<view class="" v-else>
			<view class="listbox_Item" v-for="(item,index) in Visitlist" :key="index" @click.stop="goDetail(item)">
				<image :src="item.card_type==1?item.card_avatar:item.company_logo" mode="aspectFill"
					style="border-radius: 55%;"></image>
				<view class="listbox_Item_cot">
					<view class="listbox_Item_cotent">
						<view class="listbox_Item_cotentTitle">{{item.card_type==2?item.company_name:item.real_name}}
						</view>
						<view class="listbox_Item_cotentTit">
							<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
							{{item.company_position}}
						</view>
						<view class="listbox_Item_cotentTit">
							<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
							<view class="tit">{{item.address}}</view>
						</view>
					</view>
				</view>

			</view>
		</view>

		<empty v-if="Visitlist.length==0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="Visitlist.length>0" />


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
	const current = ref(0);
	const listTab = reactive([{
		id: 1,
		name: '我看过的'
	}, {
		id: 2,
		name: '我的访客'
	}]);
	const activeStyle = ref({
		'fontFamily': 'PingFang SC',
		'fontWeight': 'bold',
		'fontSize': '32rpx',
		'color': '#333333'
	});
	const inactiveStyle = ref({
		'fontFamily': 'PingFang SC',
		'fontWeight': '500',
		'fontSize': '28rpx',
		'color': '#333333'
	});
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const Visitlist = reactive([]);
	const status = ref('loadmore');
	const userId = ref('');

	const todayVisitCoune = ref(''); //今日访问次数
	const totalShareCount = ref(''); //分享次数
	const totalVisitCount = ref(''); //总访问次数

	const isVip = ref(false)

	const CardInfo = reactive({})

	onLoad(option => {
		uni.showLoading({
			title: '加载中...'
		})
		Visitlist.length = 0
		getMyCard()
		getIsVip()
		getCardData()
		getMyVisit()

	});
	onShow(() => {

	});
	// 查询是否开通会员
	const getIsVip = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.getT('/app/user/isVip?userId=' + userId).then(res => {
			if (res.code === 0) {
				isVip.value = res.data
			}
		});
	};
	// 跳转名片详情
	const goDetail = (ite) => {
		if (uni.getStorageSync('token')) {
			if (current.value == 0) {
				if (ite.card_id) {
					uni.navigateTo({
						url: '/my/Enterprise/businessCard?userId=' + ite.user_id + '&id=' + ite.card_id
					})
				} else {
					uni.showToast({
						title: '对方暂无名片',
						icon: 'none'
					})
				}
			} else {
				let messageStatus = 0
				if (ite.messageStatus) {
					messageStatus = ite.messageStatus
				}
				if (ite.card_id) {
					uni.navigateTo({
						url: '/my/Enterprise/businessCard?userId=' + ite.visitUserId + '&id=' + ite.card_id +
							'&messageStatus=' + messageStatus
					})
				} else {
					uni.showToast({
						title: '对方暂无名片',
						icon: 'none'
					})
				}
			}
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	// 查询展示的名片
	const getMyCard = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/businessCard/showCard?userId=" + userId).then(res => {
			if (res.code == 0 && res.data) {
				Object.assign(CardInfo, res.data)

			}
		});
	};
	// 获取名片数据
	const getCardData = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/visit/userData", {
			userId: userId,
		}).then(res => {
			if (res.code == 0) {
				// recentlyList.splice(0, recentlyList.length, ...res.data.recentlyList); //最近访客数据

				todayVisitCoune.value = res.data.todayVisitCoune //今日访问次数
				totalShareCount.value = res.data.totalShareCount //分享次数
				totalVisitCount.value = res.data.totalVisitCount //总访问次数



			}
		});
	};
	// tab切换
	const clickTab = (e) => {
		console.log(e)
		if (e.index == 0) {
			current.value = e.index
			Visitlist.length = 0
			page.value = 1
			getMyVisit()
		} else {
			if (isVip.value) {
				current.value = e.index
				Visitlist.length = 0
				page.value = 1
				getMyVisit()
			} else {
				current.value = 0
				uni.showToast({
					title: '仅限会员查看',
					icon: 'none'
				})
			}
		}


	};
	// 获取最近访客
	const getMyVisit = () => {
		if (current.value == 1) {
			let userId = uni.getStorageSync('userId')
			proxy.$http.postJson("/app/visit/visitMe", {
				userId: userId,
				pageNum: page.value,
				pageSize: limit.value,
				cardId: CardInfo.cardId
			}).then(res => {
				if (res.code == 0) {
					pages.value = res.data.pages
					if (page.value < pages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}
					if (page.value == 1) {
						Visitlist.splice(0, Visitlist.length, ...res.data.list);
					} else {
						Visitlist.push(...res.data.list);
					}
				}
				status.value = 'nomore';
				uni.stopPullDownRefresh()
				uni.hideLoading()
			});
		} else {
			let userId = uni.getStorageSync('userId')
			proxy.$http.postJson("/app/visit/myVisit", {
				// userId: userId,
				pageNum: page.value,
				pageSize: limit.value
			}).then(res => {
				if (res.code == 0) {
					pages.value = res.data.pages
					if (page.value < pages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}
					if (page.value == 1) {
						Visitlist.splice(0, Visitlist.length, ...res.data.list);
					} else {
						Visitlist.push(...res.data.list);
					}
				}
				status.value = 'nomore';
				uni.stopPullDownRefresh()
				uni.hideLoading()
			});
		}
	};
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getMyVisit()
		}
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getMyVisit()
	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.bgColor {
		background: linear-gradient(to bottom, #287AFE 30%, #FFFFFF 90%);
		padding-top: 30rpx;
	}

	.CardboxNum {
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: #FFFFFF;
		box-shadow: 0rpx 7rpx 18rpx 0rpx rgba(0, 0, 0, 0.05);
		border-radius: 24rpx;
		padding: 60rpx 40rpx;
		margin: 0 30rpx;

		.CardboxNum_Item {
			text-align: center;

			.CardboxNum_ItemNum {
				font-family: Source Han Sans CN;
				font-weight: bold;
				font-size: 32rpx;
				color: #333333;

				text {
					font-size: 24rpx;
					font-size: 400;
				}
			}

			.CardboxNum_ItemTit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}
	}

	.Infobox {
		display: flex;
		align-items: center;
		margin: 30rpx 30rpx;
		border-bottom: 1px solid #F5F5F5;
		padding-bottom: 30rpx;

		image {
			width: 88rpx;
			height: 88rpx;
			border-radius: 55%;
		}

		.Infobox_conte {
			margin-left: 20rpx;

			.Infobox_conteTitle {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 30rpx;
				color: #333333;
			}

			.Infobox_conteTit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
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

					.tit {
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 1;
						overflow: hidden;
						text-overflow: ellipsis;
					}

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