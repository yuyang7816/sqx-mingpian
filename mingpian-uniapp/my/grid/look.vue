<template>
	<view>
		<view class="listbox" v-if="indexBot==1">
			<view class="listbox_Item" v-for="(item,index) in list" :key="index" @click="goDetail(item)">
				<image :src="item.avatar?item.avatar:'/static/logo.png'" mode="aspectFill"></image>
				<view class="listbox_Item_cot">
					<view class="listbox_Item_cotentTitle">
						{{item.visitUserName}}
					</view>
					<view style="color: #666666;font-size: 26rpx;">{{item.visitTime}}</view>
				</view>
			</view>
		</view>
		<view class="listbox" v-if="indexBot==2">


			<view class="listbox_Item" v-for="(item,index) in list" :key="index" @click="goDetail(item)">
				<image v-if="item.card_type==1" :src="item.card_avatar?item.card_avatar:'/static/logo.png'"
					mode="aspectFill"></image>
				<image v-if="item.card_type==2" :src="item.company_logo?item.company_logo:'/static/logo.png'"
					mode="aspectFill"></image>

				<view class="listbox_Item_cot">
					<view class="listbox_Item_cotent">
						<view class="listbox_Item_cotentTitle">
							{{item.real_name}}
						</view>
						<view class="listbox_Item_cotentTit">
							<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
							{{item.company_position}}
						</view>
						<view class="listbox_Item_cotentTit">
							<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
							{{item.company_name}}
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
	const indexBot = ref(''); //1 访客  2查看
	const page = ref(1);
	const pages = ref(1);
	const status = ref('loadmore');
	const list = reactive([]);
	const userId = ref('')
	const CardInfo = reactive({})


	onLoad(option => {
		console.log(option)
		if (option.index) {
			indexBot.value = option.index
			if (indexBot.value == 1) {
				uni.setNavigationBarTitle({
					title: '最近访客'
				})
				getMyCard()
			} else {
				getInfo()
			}


		}
	});
	onShow(() => {
		userId.value = uni.getStorageSync('userId')
	});
	// 跳转名片详情
	const goDetail = (ite) => {
		if (uni.getStorageSync('token')) {
			if (ite.card_id) {
				if (indexBot.value == 1) {
					let messageStatus = 0
					if (ite.messageStatus) {
						messageStatus = ite.messageStatus
					}

					uni.navigateTo({
						url: '/my/Enterprise/businessCard?userId=' + ite.visitUserId + '&id=' + ite.card_id +
							'&messageStatus=' + messageStatus
					})
				} else {
					uni.navigateTo({
						url: '/my/Enterprise/businessCard?userId=' + ite.user_id + '&id=' + ite.card_id
					})
				}

			} else {
				uni.showToast({
					title: '对方暂无名片',
					icon: 'none'
				})
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
				getInfo()
			}
		});
	};
	const getInfo = () => {
		let data = {
			pageSize: 10,
			pageNum: page.value
		}
		if (indexBot.value == 1) {
			data.cardId = CardInfo.cardId
			proxy.$http.postJson('/app/visit/visitMe', data).then(res => {
				if (res.code == 0) {
					pages.value = res.data.pages
					if (page.value < pages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}

					res.data.list.forEach(item => {
						item.check = false
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
		} else {
			proxy.$http.postJson('/app/visit/myVisit', data).then(res => {
				if (res.code == 0) {
					pages.value = res.data.pages
					if (page.value < pages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}

					res.data.list.forEach(item => {
						item.check = false
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
		}
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
		background: #F5F5FB;
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
				border-radius: 55%;
			}

			.listbox_Item_cot {
				flex: 1;
				// display: flex;
				// align-items: center;
				// justify-content: space-between;
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
							border-radius: 0;
						}
					}
				}
			}
		}
	}
</style>