<template>
	<view>
		<!-- <up-sticky bgColor="#FFFFFF">
			<view class="Head">
				<up-search placeholder="输入姓名、职位" v-model="keyword" :showAction="false" bgColor="#F5F5F5"></up-search>
				<view class="margin-top-xs">
					<up-tabs :list="tablist" @click="click" :current="current" lineColor="#287AFE"
						:activeStyle="activeStyle" :inactiveStyle="inactiveStyle"></up-tabs>
				</view>

			</view>
		</up-sticky> -->


		<view class="listbox">
			<view class="listbox_Item" v-for="(item,index) in list" :key="index">
				<view class="flex align-center">
					<view class="flex align-center flex-sub">
						<image :src="item.cardType==1?item.cardAvatar: item.companyLogo?item.companyLogo:'/static/logo.png'" mode="aspectFill"
							style="border-radius: 55%;"></image>
						<view class="listbox_Item_cot">
							<view class="listbox_Item_cotent">
								<view class="listbox_Item_cotentTitle">
									{{item.realName}}
								</view>
								<view class="listbox_Item_cotentTit" v-if="item.companyPosition">
									<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
									{{item.companyPosition}}
								</view>
								<view class="listbox_Item_cotentTit" v-if="item.companyName">
									<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
									{{item.companyName}}
								</view>
							</view>
					
						</view>
					</view>
					
					<!-- 成员状态 0申请中 1审核通过 2拒绝 3退出 -->
					<view class="listbox_Item_btn" v-if="item.staffSattus==0">
						<view class="btn" @click.stop="clickJoin(item)">同意</view>
						<view class="btns" @click.stop="clickNo(item)">拒绝</view>
					</view>
					
				</view>
				<view v-if="item.applyContent" class="margin-top" style="color: #666666;">申请理由：{{item.applyContent}}</view>
			</view>
		</view>

		<empty v-if="list.length==0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="list.length>0" />


		<u-popup :show="show" :round="12" mode="center" @close="show = false" closeable>
			<view class="popup">
				<view class="popupTitle">拒绝原因</view>
				<view class="popupInput">
					<u--textarea v-model="content" placeholder="请输入拒绝原因" border="none" maxlength="300"
						autoHeight></u--textarea>
				</view>
				<view class="popupBtn" @click="clickJoinNo()">确认</view>
			</view>
		</u-popup>
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

	const show = ref(false);
	const content = ref('');
	const recordId = ref('');



	onLoad(option => {
		companyInfo.value = uni.getStorageSync('companyInfo')
		getlist()
	});
	onShow(() => {

	});
	// 跳转名片详情
	const goDetail = (ite) => {
		uni.navigateTo({
			url: '/my/Enterprise/businessCard?id='+ite.cardId
		})
	};

	// 同意加入企业
	const clickJoin = (ite) => {
		uni.showLoading({
			title: '加载中...'
		})
		let data = {
			recordId: ite.id,
			status: 1,
			content: content.value
		}
		proxy.$http.postJson("/app/comRecord/auditApply", data).then(res => {
			if (res.code == 0) {
				page.value = 1
				getlist()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			uni.hideLoading()
		});
	};
	// 拒绝加入企业
	const clickNo = (ite) => {
		recordId.value = ite.id
		show.value = true
	};

	// 拒绝加入企业
	const clickJoinNo = (ite) => {
		if (!content.value) {
			uni.showToast({
				title: '请输入拒绝理由',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '加载中...'
		})
		show.value = false
		let data = {
			recordId: recordId.value,
			status: 2,
			content: content.value
		}
		proxy.$http.postJson("/app/comRecord/auditApply", data).then(res => {
			if (res.code == 0) {
				page.value = 1
				getlist()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			uni.hideLoading()

		});
	};
	//查询企业全部成员
	const getlist = () => {
		let data = {
			pageNum: page.value,
			pageSize: limit.value,
			cardId: companyInfo.value.cardId,
			status: 0
		}
		proxy.$http.get("/app/comRecord/page", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				res.data.records.forEach(item => {
					item.check = false
				})
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records);
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

		.listbox_Item_btn {
			display: flex;
			align-items: center;

			.btn {
				background: #287AFE;
				border-radius: 34rpx;
				border: 1px solid #287AFE;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 28rpx;
				color: #FFFFFF;
				padding: 8rpx 30rpx;
			}

			.btns {
				background: #FFFFFF;
				border-radius: 34rpx;
				border: 1px solid #287AFE;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 28rpx;
				color: #287AFE;
				padding: 8rpx 30rpx;
				margin-left: 20rpx;
			}
		}
	}

	.popup {
		width: 500rpx;
		padding: 30rpx;

		.popupTitle {
			font-size: 30rpx;
			font-weight: bold;
			text-align: center;
		}

		.popupInput {
			margin-top: 30rpx;
			background: #f2f2f2;
			border-radius: 12rpx;
			padding-bottom: 30rpx;
			// height: 200rpx;
		}

		.u-textarea {
			background: #f2f2f2 !important;
		}

		.popupBtn {
			background: #FFFFFF;
			border-radius: 34rpx;
			background: #287AFE;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 28rpx;
			color: #FFFFFF;
			padding: 0 30rpx;
			height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 30rpx;
		}
	}
</style>