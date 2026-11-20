<template>
	<view>
		<up-sticky bgColor="#FFFFFF">
			<view class="Head">
				<up-search placeholder="请输入企业名称" shape="square" v-model="keyword" :showAction="false" height="70rpx"
					bgColor="#F5F5F5" @search="searchInput"></up-search>
			</view>
		</up-sticky>

		<view class="">

			<view class="qyInfo" v-for="(item,index) in list" :key="index" @click="item.result.company_name?goDetail(item.name):''">
				<view class="qyInfo_Top">
					<view class="qyInfo_Topcot">
						<view class="qyInfo_Topcot_tit" v-if="item.result.company_name">{{item.result.company_name}} </view>
						<view class="qyInfo_Topcot_tit" v-else>{{item.name}} </view>
						<view class="qyInfo_Topcot_label" v-if="item.result.registration_status||item.result.business_term">
							<view class="qyInfo_Topcot_label_zy">{{item.result.registration_status}}</view>
							<view class="qyInfo_Topcot_label_jyqx">{{item.result.business_term}} </view>
						</view>
					</view>
				</view>
				<view class="qyInfo_tit">统一社会信用代码：{{item.result.unified_social_credit_code?item.result.unified_social_credit_code:'无'}}</view>
				<view class="qyInfo_tit">经营范围：{{item.result.industry?item.result.industry:'无'}}</view>
				<view class="qyInfo_user">
					<view class="qyInfo_userIte">
						<view class="qyInfo_userIte_Tit">法定代表人</view>
						<view class="qyInfo_userIte_Title" style="color: #277DFF;">
							{{item.result.legal_representative?item.result.legal_representative:'无'}}
						</view>
					</view>
					<view class="qyInfo_userIte">
						<view class="qyInfo_userIte_Tit">注册资金</view>
						<view class="qyInfo_userIte_Title">{{item.result.registered_capital?item.result.registered_capital:'无'}} </view>
					</view>
					<view class="qyInfo_userIte">
						<view class="qyInfo_userIte_Tit">成立日期</view>
						<view class="qyInfo_userIte_Title">{{item.result.establishment_date?item.result.establishment_date:'无'}} </view>
					</view>
				</view>
			</view>
			<!-- 加载数据 -->
			<up-loadmore :status="status" v-if="list.length>0" />

		</view>
		<view class="padding-lr">
			<empty v-if="list.length==0" :contont="contont"></empty>
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
	const keyword = ref('')
	const isHittoy = ref(true);
	const contont = ref('暂无核查记录');
	const EnterpriseInfo = reactive({})
	const verifyFlag = ref('');
	const companyName = ref('');

	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	const status = ref('loadmore');

	onLoad(option => {
		getEnterpriseInfo()
	});
	onShow(() => {

	});
	const goDetail = (name) => {
		uni.navigateTo({
			url: '/my/hecha/listDetail?name=' + name
		})
	};
	// 搜索
	const searchInput = (e) => {
		console.log(e)
		keyword.value = e
		if (keyword.value) {
			uni.showLoading({
				title: '查询中...'
			})
			list.length = 0
			getEnterpriseInfo()
		} else {
			uni.showToast({
				title: '请输入查询企业名称',
				icon: 'none'
			})
		}

	};
	/* 查询企业信息 -AI获取公司信息*/
	const getEnterpriseInfo = () => {
		let userId = uni.getStorageSync('userId')
		let data = {
			page: page.value,
			limit: limit.value,
			userId: userId,
			name: keyword.value,
		}
		proxy.$http.postJson("/app/verifyRecord/page", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pageNum
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				res.data.records.forEach(d => {
					if (d.result) {
						d.result = JSON.parse(d.result)
					}
				})
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
				getEnterpriseInfo();
			}
		}
	});
	onPullDownRefresh(() => {
		if (userId.value) {
			page.value = 1;
			getEnterpriseInfo();
		}
	});
</script>

<style lang="scss">
	.Head {
		padding: 20rpx 30rpx;
	}


	.qyInfo {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 30rpx;
		padding: 30rpx;

		.qyInfo_Top {
			display: flex;

			image {
				width: 100rpx;
				height: 100rpx;
				border-radius: 16rpx;
			}

			.qyInfo_Topcot {
				flex: 1;
				align-items: center;

				.qyInfo_Topcot_tit {
					font-weight: 800;
					font-size: 36rpx;
					color: #1D1D1D;
				}

				.qyInfo_Topcot_label {
					display: flex;
					align-items: center;
					margin-top: 8rpx;

					.qyInfo_Topcot_label_zy {
						background: #E6FFEC;
						border-radius: 6rpx;
						font-weight: 500;
						font-size: 24rpx;
						color: #37A743;
						padding: 4rpx 10rpx;
					}

					.qyInfo_Topcot_label_jyqx {
						background: #E1ECFF;
						border-radius: 6rpx;
						font-weight: 500;
						font-size: 24rpx;
						color: #468CFE;
						padding: 4rpx 10rpx;
						margin-left: 20rpx;
					}
				}
			}
		}

		.qyInfo_tit {
			font-weight: 500;
			font-size: 26rpx;
			color: #999999;
			margin-top: 10rpx;
		}

		.qyInfo_user {
			height: 125rpx;
			background: #F0F4FA;
			border-radius: 16rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 30rpx;
			margin-top: 20rpx;

			.qyInfo_userIte {
				text-align: center;

				.qyInfo_userIte_Tit {
					font-weight: 500;
					font-size: 26rpx;
					color: #999999;
				}

				.qyInfo_userIte_Title {
					font-weight: bold;
					font-size: 26rpx;
					color: #212121;
					margin-top: 10rpx;
				}
			}
		}
	}
</style>