<template>
	<view>
		<view class="imagebg">
			<image src="/my/static/cardbg.png" mode="aspectFill"></image>
		</view>
		<view class="headTop">

			<view class="boxBor">
				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						公司名称
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入公司名称" border="none" v-model="companyName"
							inputAlign="right"></up-input>
					</view>
				</view>

				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						姓名
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入姓名" border="none" v-model="realName" inputAlign="right"></up-input>
					</view>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						公司职务
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入公司职务" border="none" v-model="companyPosition"
							inputAlign="right"></up-input>
						<!-- <up-icon name="arrow-right" color="#C5C5C5" size="14"></up-icon> -->
					</view>
				</view>
				<view class="infoboxItem" >
					<view class="infoboxItem_title">
						<text>*</text>
						手机号
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入手机号" border="none" v-model="cardPhone" inputAlign="right"></up-input>
					</view>
				</view>
				<view class="infoboxItem" style="border-bottom: none;">
					<view class="infoboxItem_title">
						<text>*</text>
						微信号
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入微信号" border="none" v-model="wxCode" inputAlign="right"></up-input>
					</view>
				</view>

			</view>
		</view>
		<view class="zzbox">
			<view class="zzbox_title">
				<text>*</text>
				营业执照
			</view>
			<view class="zzbox_tit">请保持图片显示完整字体清晰可见，大小不超过10M</view>

			<view class="zzbox_Img" @click="addImages">
				<image :src="businesslicense" v-if="businesslicense" mode="aspectFill"></image>
				<image src="/my/static/yyzzAdd.png" mode="aspectFill" v-else></image>
			</view>
		</view>

		<view class="btn" @click="NextStep">下一步</view>

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
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const companyName = ref(''); //公司名称
	const realName = ref(''); //姓名
	const companyPosition = ref(''); //职位
	const cardPhone = ref(''); //联系电话
	const wxCode = ref('');//微信号
	const businesslicense = ref(''); //营业执照

	const cardId = ref('');

	onLoad(option => {
		if (option.cardId) {
			cardId.value = option.cardId
			getCardInfo()
		}
	});
	onShow(() => {

	});
	// 获取名片详情
	const getCardInfo=()=>{
		let userId = uni.getStorageSync('userId');
		proxy.$http.get("/app/businessCard/info?cardId=" + cardId.value).then(res => {
			if (res.code == 0) {
				companyName.value = res.data.companyName
				realName.value = res.data.realName
				companyPosition.value = res.data.companyPosition
				cardPhone.value = res.data.cardPhone
				businesslicense.value = res.data.companyLicense
				wxCode.value = res.data.wxCode
			}
		});
	};
	// 下一步
	const NextStep = () => {
		if (!companyName.value) {
			uni.showToast({
				title: '请输入公司名称',
				icon: 'none'
			})
			return
		}
		if (!realName.value) {
			uni.showToast({
				title: '请输入姓名',
				icon: 'none'
			})
			return
		}
		if (!companyPosition.value) {
			uni.showToast({
				title: '请输入公司职务',
				icon: 'none'
			})
			return
		}
		if (!cardPhone.value) {
			uni.showToast({
				title: '请输入联系电话',
				icon: 'none'
			})
			return
		}
		if (!wxCode.value) {
			uni.showToast({
				title: '请输入微信号',
				icon: 'none'
			})
			return
		}
		
		if (!businesslicense.value) {
			uni.showToast({
				title: '请上传营业执照',
				icon: 'none'
			})
			return
		}
		
		let data = {
			companyName: companyName.value,
			realName: realName.value,
			companyPosition: companyPosition.value,
			cardPhone: cardPhone.value,
			businesslicense: businesslicense.value,
			cardId:cardId.value,
			wxCode:wxCode.value
		}
		uni.navigateTo({
			url: '/my/Enterprise/PublishTwo?dataItem=' + JSON.stringify(data)
		})
		
	};
	//上传图片
	const addImages = () => {
		uni.chooseImage({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < 1; i++) {
					uni.showLoading({
						title: '上传中...'
					})
					uni.uploadFile({ // 上传接口
						// url: config.APIHOST + '/alioss/upload',
						url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							businesslicense.value = JSON.parse(uploadFileRes.data)
								.data
							uni.hideLoading();
						}
					});
				}
			}
		})
	};
</script>

<style lang="scss">
	page {
		background: #F3F2F8;
	}

	.imagebg {
		width: 100%;
		height: 312rpx;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.headTop {
		width: 100%;
		position: relative;
		background: #FFFFFF;
	}

	.headTop::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(81deg, #D0DBFF 0%, #F0F4FF 100%);
		z-index: 1;
		/* 确保在垂直渐变之上 */
	}

	.headTop::after {
		content: "";
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to bottom, transparent, #F3F2F8);
		/* 垂直渐变，结束颜色为透明 */
		z-index: 2;
		/* 确保在水平渐变之上 */
	}


	.boxBor {
		background: #FFFFFF;
		border-radius: 13rpx;
		margin: 0 30rpx;
		padding: 0 30rpx;
		position: relative;
		z-index: 9;

		.infoboxItem {
			padding: 30rpx 0;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 1rpx solid #E6E6E6;

			.infoboxItem_title {
				 
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;

				text {
					 
					font-weight: bold;
					font-size: 30rpx;
					color: #FF0B1C;
				}
			}

			.infoboxItem_cont {
				display: flex;
				align-items: center;
			}

		}
	}

	.input-placeholder {
		font-weight: 400;
		font-size: 27rpx;
		color: #C5C5C5 !important;
	}

	.zzbox {
		background: #FFFFFF;
		border-radius: 13rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.zzbox_title {
			 
			font-weight: bold;
			font-size: 28rpx;
			color: #333333;

			text {
				 
				font-weight: bold;
				font-size: 30rpx;
				color: #FF0B1C;
			}
		}

		.zzbox_tit {
			font-family: SourceHanSansCNVF;
			font-weight: 400;
			font-size: 23rpx;
			color: #4B76FE;
		}

		.zzbox_Img {
			width: 625rpx;
			height: 314rpx;
			border-radius: 12rpx;
			margin-top: 20rpx;

			image {
				width: 100%;
				height: 100%;
				border-radius: 12rpx;
			}
		}
	}

	.btn {
		background: #507AFE;
		border-radius: 15rpx;
		margin: 60rpx 30rpx;
		height: 85rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		font-weight: 400;
		font-size: 31rpx;
		color: #FFFFFF;
	}
</style>