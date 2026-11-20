<template>
	<view>
		<!-- <up-sticky bgColor="#FFFFFF">
			<view class="Head">
				<up-search placeholder="请输入企业名称" shape="square" v-model="keyword" :showAction="false" height="70rpx"
					bgColor="#F5F5F5" @search="searchInput"></up-search>
			</view>
		</up-sticky> -->
		<view class="" v-if="!isHittoy">
			<view class="qyInfo">
				<view class="qyInfo_Top">
					<view class="qyInfo_Topcot">
						<view class="qyInfo_Topcot_tit">{{EnterpriseInfo.company_name}} </view>
						<view class="qyInfo_Topcot_label">
							<view class="qyInfo_Topcot_label_zy">{{EnterpriseInfo.registration_status}}</view>
							<view class="qyInfo_Topcot_label_jyqx">{{EnterpriseInfo.business_term}} </view>
						</view>
					</view>
				</view>
				<view class="qyInfo_tit">统一社会信用代码：{{EnterpriseInfo.unified_social_credit_code}}</view>
				<view class="qyInfo_tit">经营范围：{{EnterpriseInfo.industry}}</view>
				<view class="qyInfo_user">
					<view class="qyInfo_userIte">
						<view class="qyInfo_userIte_Tit">法定代表人</view>
						<view class="qyInfo_userIte_Title" style="color: #277DFF;">
							{{EnterpriseInfo.legal_representative}}
						</view>
					</view>
					<view class="qyInfo_userIte">
						<view class="qyInfo_userIte_Tit">注册资金</view>
						<view class="qyInfo_userIte_Title">{{EnterpriseInfo.registered_capital}} </view>
					</view>
					<view class="qyInfo_userIte">
						<view class="qyInfo_userIte_Tit">成立日期</view>
						<view class="qyInfo_userIte_Title">{{EnterpriseInfo.establishment_date}} </view>
					</view>
				</view>
			</view>

			<view class="boxInfo">
				<view class="boxInfo_ite">
					<view class="boxInfo_itetit">公司类型</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.company_type}}</view>
				</view>
				<view class="boxInfo_ite">
					<view class="boxInfo_itetit">登记机关</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.registration_authority}}</view>
				</view>
				<view class="boxInfo_ite" v-if="EnterpriseInfo.paid_in_capital">
					<view class="boxInfo_itetit">实缴资本</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.paid_in_capital}}</view>
				</view>
				<view class="boxInfo_ite">
					<view class="boxInfo_itetit">人员规模</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.staff_size}}</view>
				</view>
				<view class="boxInfo_ite">
					<view class="boxInfo_itetit">所属行业</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.industry}}</view>
				</view>

				<view class="boxInfo_ite" v-if="EnterpriseInfo.phone">
					<view class="boxInfo_itetit">联系电话</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.phone}}</view>
				</view>
				<view class="boxInfo_ite" v-if="EnterpriseInfo.email&&EnterpriseInfo.email!=null">
					<view class="boxInfo_itetit">联系邮箱</view>
					<view class="boxInfo_iteCot">{{EnterpriseInfo.email}}</view>
				</view>

			</view>


			<view class="boxqy">

				<view class="boxqy_ite" v-if="EnterpriseInfo.risk_info">
					<view class="boxqy_iteType">经营异常</view>
					<view class="boxqy_iteTit" v-if="EnterpriseInfo.risk_info.administrative_penalties">该公司有异常</view>
					<view class="boxqy_iteTit" v-else>该公司无异常</view>
				</view>

				<view class="boxqy_ite" v-if="EnterpriseInfo.risk_info">
					<view class="boxqy_iteType">严重违法</view>
					<view class="boxqy_iteTit" v-if="EnterpriseInfo.risk_info.serious_violations">该公司有异常</view>
					<view class="boxqy_iteTit" v-else>该公司无异常</view>
				</view>
				<view class="boxqy_ite" v-if="EnterpriseInfo.risk_info">
					<view class="boxqy_iteType">法院公告</view>
					<view class="boxqy_iteTit" v-if="EnterpriseInfo.risk_info.court_announcements">该公司有异常</view>
					<view class="boxqy_iteTit" v-else>该公司无异常</view>
				</view>

				<view class="boxqy_ite" v-if="EnterpriseInfo.risk_info">
					<view class="boxqy_iteType">被执行人</view>
					<view class="boxqy_iteTit" v-if="EnterpriseInfo.risk_info.executed_person">该公司有异常</view>
					<view class="boxqy_iteTit" v-else>该公司无异常</view>
				</view>

				<view class="boxqy_ite" v-if="EnterpriseInfo.risk_info">
					<view class="boxqy_iteType">限制高消费</view>
					<view class="boxqy_iteTit" v-if="EnterpriseInfo.risk_info.restricted_consumer">该公司有异常</view>
					<view class="boxqy_iteTit" v-else>该公司无异常</view>
				</view>
				<view class="boxqy_ite" style="height: 0;"> </view>
			</view>
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

	

	onLoad(option => {
		if(option.name){
			keyword.value = option.name
			getEnterpriseInfo()
		}
		
	});
	onShow(() => {

	});
	// 搜索
	const searchInput = (e) => {
		console.log(e)
		keyword.value = e
		if (keyword.value) {
			uni.showLoading({
				title: '查询中...'
			})
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

		proxy.$http.postJson("/app/verifyRecord/save", {
			name: keyword.value,
			userId: uni.getStorageSync('userId')
		}).then(res => {
			if (res.code == 0) {
				
				let info = JSON.parse(res.data.result)
				if (info.legal_representative) {
					isHittoy.value = false
					Object.assign(EnterpriseInfo, info)
				} else {
					isHittoy.value = true
					contont.value = info.verification_result
				}
				
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			uni.hideLoading()
		});
	};
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

	.boxInfo {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 30rpx;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		padding: 30rpx 30rpx 0 30rpx;

		.boxInfo_ite {
			padding-bottom: 30rpx;
			width: 50%;

			.boxInfo_itetit {
				font-weight: 500;
				font-size: 26rpx;
				color: #999999;
			}

			.boxInfo_iteCot {
				width: 95%;
				font-weight: bold;
				font-size: 28rpx;
				color: #1D1D1D;
				margin-top: 10rpx;

				word-break: break-word;
				/* 非标准但广泛支持 */
				overflow-wrap: break-word;
			}
		}
	}

	.boxqy {
		margin: 30rpx;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;

		.boxqy_ite {
			width: 215rpx;
			height: 140rpx;
			background: #FFFFFF;
			border-radius: 20rpx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			margin-bottom: 25rpx;


			.boxqy_iteType {
				font-weight: bold;
				font-size: 28rpx;
				color: #1D1D1D;
				padding-left: 30rpx;

			}

			.boxqy_iteTit {
				font-weight: 500;
				font-size: 26rpx;
				color: #999999;
				margin-top: 10rpx;
				padding-left: 30rpx;
			}
		}
	}
</style>