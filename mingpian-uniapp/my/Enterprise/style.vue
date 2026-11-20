<template>
	<view>
		<view class="bgColor">
			<view class="box" :style="'background-image:url('+CardInfo.cardImg+')'">
				<view class="boxbg">
					<view class="boxhead">
						<view class="boxheadTop">
							<view class="boxheadTop_Title">
								<text>{{CardInfo.realName}}</text>
								<view class="boxheadTop_Title_label">
									<view class="boxheadTop_Title_labelStart">
										<image src="/static/image/index/xingxing.png"></image>
									</view>
									<view class="boxheadTop_Title_labelZW">{{CardInfo.companyPosition}}</view>
								</view>
							</view>
							<view class="boxhead_Item margin-top">
								<image src="/static/image/index/qiye.png" mode="widthFix"></image>
								<text>{{CardInfo.companyName}}</text>
							</view>
						</view>

						<image :src="CardInfo.cardType==1?CardInfo.cardAvatar: CardInfo.companyLogo" mode="aspectFill"
							style="border-radius: 55%;"></image>
					</view>
					<view class="boxhead_Item margin-top-sm" v-if="CardInfo.cardType==1&&CardInfo.address">
						<image src="/static/image/index/add.png" mode="widthFix"></image>
						<text >{{CardInfo.address}}</text>
					</view>
					<view class="boxhead_Item margin-top-sm" v-if="CardInfo.cardType==2&&CardInfo.companyAddress">
						<image src="/static/image/index/add.png" mode="widthFix"></image>
						<text >{{CardInfo.companyAddress}}</text>
					</view>
					<view class="boxhead_Item margin-top-sm">
						<image src="/static/image/index/phone.png" mode="widthFix"></image>
						<text>{{CardInfo.cardPhone}}</text>
					</view>
				</view>
			</view>
		</view>



		<view class="card">
			<view class="card_title">名片样式</view>
			<view class="cardbox">

				<up-radio-group v-model="styleName">
					<view class="cardboxItem" v-for="(item,index) in bglist" :key="index">
						<image :src="item.styleImg" mode="aspectFill"></image>
						<view class="cardboxItem_Tit">{{item.styleName}}</view>
						<view class="cardboxItem_radio">
							<up-radio shape="circle" activeColor="#287AFE" :name="item.styleName"
								@change="radioChange(item)"></up-radio>
						</view>
					</view>
				</up-radio-group>
			</view>
			<empty v-if="bglist.length==0"></empty>
			<!-- 加载数据 -->
			<up-loadmore :status="status" v-if="bglist.length>0" />
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
	const bglist = reactive([]);
	const styleName = ref()
	const styleImg = ref('');
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const CardInfo = reactive({}); //我的名片信息



	const radioChange = (e) => {
		console.log(e)
		// styleName.value = e.styleName
		// styleImg.value = e.styleImg
		uni.showLoading({
			title: '修改中...'
		})
		let data = {
			cardId: CardInfo.cardId,
			styleName: e.styleName,
			styleImg: e.styleImg,
			cardStyleCode: e.styleCode
		}

		proxy.$http.get('/app/businessCard/editStyle', data).then(res => {
			if (res.code === 0) {
				uni.showToast({
					title: '修改成功',
					icon: "none"
				})
				getMyCard()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});

	};

	onLoad(option => {

	});
	onShow(() => {
		getMyCard()
		
	});
	// 查询展示的名片
	const getMyCard = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/businessCard/showCard?userId=" + userId).then(res => {
			if (res.code == 0 && res.data) {
				Object.assign(CardInfo, res.data)
				getStylelist()
			}
		});
	};
	// 查询名片样式列表
	const getStylelist = () => {

		let data = {
			pageNum: page.value,
			pageSize: limit.value
		}
		proxy.$http.postJson("/app/cardStyle/list", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pageNum
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				if (page.value == 1) {
					bglist.splice(0, bglist.length, ...res.data.list);
				} else {
					bglist.push(...res.data.list);
				}
				bglist.forEach(d => {
					if (d.styleCode == CardInfo.cardStyleCode) {
						styleName.value = d.styleName
						styleImg.value = d.styleImg
					}
				})
				// styleName.value = bglist[0].styleName
				// styleImg.value = bglist[0].styleImg
				// console.log(StyleName.value)
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh()
		});
	};
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getStylelist();
		}
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getStylelist();
	});
</script>

<style lang="scss">
	page {
		background: #F6F7FB;
	}

	.bgColor {
		background: linear-gradient(to bottom, #287AFE 10%, #F5F5FB 70%);
	}

	.box {
		background: linear-gradient(to bottom, #F0F5FF, #FFFFFF);
		box-shadow: 0rpx 8rpx 16rpx 0rpx rgba(91, 109, 137, 0.11);

		border-radius: 24rpx;
		position: relative;
		z-index: 9;
		margin: 0 30rpx;
		background-size: 100% 100%;

		.boxbg {
			padding: 30rpx;
			background: rgba(255, 255, 255, 0.3);
			border-radius: 24rpx;
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

	.card {
		margin: 20rpx 30rpx;

		.card_title {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
		}

		.cardbox {
			margin-top: 20rpx;
			display: flex;
			align-items: center;
			flex-wrap: wrap;
			justify-content: space-between;


			.cardboxItem {
				width: 320rpx;
				background: #F0F5FF;
				border-radius: 10rpx;
				margin-bottom: 20rpx;
				position: relative;

				image {
					width: 320rpx;
					height: 198rpx;
					background: #FDDCAD;
					box-shadow: 0rpx 8rpx 16rpx 0rpx rgba(91, 109, 137, 0.11);
					border-radius: 10rpx;
				}

				.cardboxItem_Tit {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 26rpx;
					color: #333333;
					margin-top: 8rpx;
				}

				.cardboxItem_radio {
					position: absolute;
					bottom: 60rpx;
					right: 0rpx;
				}
			}
		}
	}
</style>