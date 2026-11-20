<template>
	<view>
		<view class="headTop">

			<view class="boxBor">
				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						公司面积(m²)
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入公司面积" border="none" v-model="companyArea"
							inputAlign="right"></up-input>
						<!-- <up-icon name="arrow-right" color="#C5C5C5" size="14"></up-icon> -->
					</view>
				</view>

				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						年产值(万元)
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入年产值" border="none" v-model="companyOutputValue"
							inputAlign="right"></up-input>
						<!-- <up-icon name="arrow-right" color="#C5C5C5" size="14"></up-icon> -->
					</view>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						公司地址
					</view>
					<view class="infoboxItem_cont" @click="openMap()">
						<view v-if="companyAddress">{{companyAddress}}</view>
						<view v-else class="input-placeholder">请选择公司地址</view>
						<!-- <up-input placeholder="请选择公司地址" border="none" v-model="companyAddress"
							inputAlign="right"></up-input> -->
						<up-icon name="arrow-right" color="#C5C5C5" size="14"></up-icon>
					</view>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">
						<text>*</text>
						行业(类型)
					</view>
					<view class="infoboxItem_cont" @click="showTree = true">
						<view v-if="industry">{{industry}}</view>
						<view v-else class="input-placeholder">请选择行业类型</view>
						<!-- <up-input placeholder="请选择行业类型" border="none" v-model="industry" inputAlign="right"></up-input> -->
						<up-icon name="arrow-right" color="#C5C5C5" size="14"></up-icon>
					</view>
				</view>
				<view class="infoboxItem" style="border-bottom: none;">
					<view class="infoboxItem_title">
						<text>*</text>
						公司人数
					</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请输入公司人数" border="none" v-model="companyNum" inputAlign="right"
							type="number"></up-input>
						<!-- <up-icon name="arrow-right" color="#C5C5C5" size="14"></up-icon> -->
					</view>
				</view>
			</view>
		</view>
		<view class="zzbox">
			<view class="zzbox_title">
				<text>*</text>
				公司产品
			</view>
			<view class="zzbox_tit">
				<up-textarea v-model="firstProduct" placeholder="请输入主营产品" border="none"></up-textarea>
			</view>

			<view class="zzbox_boxImg">
				<view class="zzbox_Imgs" v-for="(item,index) in firstProductImg" :key="index">
					<image :src="item" mode="aspectFill"></image>
					<view class="zzbox_ImgClose" @click="removeImage(index)">
						<up-icon name="close-circle-fill" color="#2979ff" size="22"></up-icon>
					</view>
				</view>
				<view class="zzbox_Img" @click="addImages" v-if="firstProductImg.length<8">
					<image src="/my/static/photo.png" mode="widthFix"></image>
					<text>添加图片</text>
				</view>
				<view class="zzbox_Imgs" style="height: 0;"></view>
				<view class="zzbox_Imgs" style="height: 0;"></view>
				<view class="zzbox_Imgs" style="height: 0;"></view>
			</view>

		</view>

		<view class="zzbox" style="padding-bottom: 60rpx;">
			<view class="zzbox_title">
				<text>*</text>
				公司简介
			</view>
			<view class="zzbox_tit">
				<up-textarea v-model="companyBlurb" placeholder="请输入公司简介" border="none" :height="160"></up-textarea>
			</view>
			<view class=" aiscIcon" @click="clickAi()">
				<image src="/my/static/vipIcon.png" mode="widthFix" class="aiscIcon_vip"></image>
				<image src="/my/static/aishengcheng.png" class="aiscIcon_sc" mode="widthFix"></image>
			</view>
		</view>


		<view class="btn" @click="NextStep">下一步</view>

		<!-- 行业 -->
		<up-picker :show="showTree" ref="uPickerRef" :loading="loading" :columns="columns" keyName="industryName"
			@change="changeHandler" @cancel="showTree = false" @confirm="confirmTree"></up-picker>


		<up-popup :show="showVip" mode="center" round="24" closeable @close="showVip = false">
			<view class="popupVip">
				<image src="/my/static/vipbg.png" mode="widthFix"></image>
				<view class="popupVip_title">开通VIP会员</view>
				<view class="popupVip_tit">需开通VIP会员可使用AI生成</view>
				<view class="popupVip_btn" @click="goVip()">去开通</view>
			</view>
		</up-popup>
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
	const companyArea = ref(''); //面积(单位:平方米)
	const companyOutputValue = ref(''); //年产值(单位:万元)
	const companyAddress = ref(''); //公司详细地址
	const companyProvince = ref(''); //公司省
	const companyCity = ref(''); //公司市
	const companyDistrict = ref(''); //公司区

	const industry = ref(''); //行业名称
	const industryFirstId = ref(''); //行业一级id 
	const industryFirstName = ref(''); //行业一级名称
	const industrySecondId = ref(''); //行业二级id 
	const industrySecondName = ref(''); //行业二级名称
	const companyNum = ref(''); //公司人数
	const firstProductImg = reactive([]); //主营产品图
	const firstProduct = ref(''); //主营产品
	const dataItem = ref({}); //上级页面传参数据
	const showTree = ref(false); //行业分类组件
	const columns = reactive([]); //行业列表
	const uPickerRef = ref(null)
	const columnData = reactive([]);
	const loading = ref(false);
	const cardId = ref('');

	const companyBlurb = ref('');
	const showVip = ref(false);
	const isVip = ref(false);

	onLoad(option => {
		if (option.dataItem) {
			dataItem.value = JSON.parse(option.dataItem)

			if (dataItem.value.cardId) {
				cardId.value = dataItem.value.cardId
				getCardInfo()
			}
		}

		getTreeList()
	});
	onShow(() => {
		getIsVip()
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
	/* 跳转会员 */
	const goVip = () => {
		showVip.value = false
		uni.navigateTo({
			url: '/my/vip/index'
		})
	};
	/* AI 生成公司简介 */
	const clickAi = () => {
		if (isVip.value) {

			uni.showLoading({
				title: '生成中...'
			})
			let userId = uni.getStorageSync('userId');
			proxy.$http.get("//app/ai/createInfo?companyName=" + dataItem.value.companyName).then(res => {
				if (res.code == 0) {
					companyBlurb.value = res.data
				}
				uni.hideLoading()
			});
		} else {
			showVip.value = true
		}
	};
	// 获取名片详情
	const getCardInfo = () => {
		let userId = uni.getStorageSync('userId');
		proxy.$http.get("/app/businessCard/info?cardId=" + cardId.value).then(res => {
			if (res.code == 0) {
				companyArea.value = res.data.companyArea //面积(单位:平方米)
				companyOutputValue.value = res.data.companyOutputValue //年产值(单位:万元)
				companyAddress.value = res.data.companyAddress //公司详细地址
				companyCity.value = res.data.companyCity //市
				companyDistrict.value = res.data.companyDistrict //区
				companyProvince.value = res.data.companyProvince //省
				industryFirstId.value = res.data.industryFirstId //行业一级id
				industryFirstName.value = res.data.industryFirstName //行业一级名称
				industrySecondId.value = res.data.industrySecondId //行业二级id 
				industrySecondName.value = res.data.industrySecondName //行业二级名称
				companyBlurb.value = res.data.companyBlurb //公司简介
				industry.value = industryFirstName.value + '-' + industrySecondName.value //行业名称

				companyNum.value = res.data.companyNum //公司人数

				firstProduct.value = res.data.firstProduct
				if (res.data.firstProductImg) {
					let imageArr = res.data.firstProductImg.split(',')
					firstProductImg.splice(0, firstProductImg.length, ...imageArr);
				}

			}
		});
	};
	// 选择行业分类
	const confirmTree = (e) => {
		console.log(e)
		industryFirstId.value = e.value[0].industryId //行业一级id 
		industryFirstName.value = e.value[0].industryName //行业一级名称
		industrySecondId.value = e.value[1].industryId //行业二级id 
		industrySecondName.value = e.value[1].industryName //行业二级名称
		industry.value = industryFirstName.value + '-' + industrySecondName.value //行业名称
		showTree.value = false
	};
	// 行业滑动切换二级分类
	const changeHandler = (e) => {
		const {
			columnIndex,
			index,
			picker
		} = e;
		if (columnIndex === 0) {
			loading.value = true;
			console.log(columnData)
			// 模拟网络请求
			setTimeout(() => {
				uPickerRef.value.setColumnValues(1, columnData[index]);
				loading.value = false;
			}, 500);
		}
	};
	// 获取行业分类
	const getTreeList = () => {
		proxy.$http.getT('/app/industry/getIndustryTreeList').then(res => {
			if (res.code === 0) {
				let arr = []
				let LevelOne = []
				let LeveTwo = []
				// 获取所有一级分类
				res.data.forEach(d => {
					LevelOne.push(d)
				})
				if (LevelOne.length != 0) {
					// 获取默认展示的第一个二级分类
					if (LevelOne[0].childrenList.length != 0) {
						LevelOne[0].childrenList.forEach(d => {
							LeveTwo.push(d)
						})
					}
					// 获取所有的二级分类  用于滑动切换使用
					LevelOne.forEach(d => {
						columnData.push(d.childrenList)
					})
				}
				// console.log(columnData)
				arr.push(LevelOne, LeveTwo)
				columns.splice(0, columns.length, ...arr);
			}
		});
	};
	// 选择位置
	const openMap = () => {
		uni.chooseLocation({
			success: function(res) {
				console.log('位置名称：' + res.name);
				console.log('详细地址：' + res.address);
				console.log('纬度：' + res.latitude);
				console.log('经度：' + res.longitude);
				companyAddress.value = res.address
				selectCity(res.longitude, res.latitude)
			}
		});

	};
	// 经纬度换省市区
	const selectCity = (longitude, latitude) => {
		proxy.$http.getT('/app/Login/selectCity?lat=' + latitude + '&lng=' + longitude).then(res => {
			if (res.code === 0) {
				companyCity.value = res.data.city ? res.data.city : res.data.province
				companyDistrict.value = res.data.district
				companyProvince.value = res.data.province
			}
		});
	};
	const NextStep = () => {

		if (!companyArea.value) {
			uni.showToast({
				title: '请输入公司面积',
				icon: 'none'
			})
			return
		}
		if (!companyOutputValue.value) {
			uni.showToast({
				title: '请输入年产值',
				icon: 'none'
			})
			return
		}
		if (!companyAddress.value) {
			uni.showToast({
				title: '请输入公司地址',
				icon: 'none'
			})
			return
		}
		if (!industry.value) {
			uni.showToast({
				title: '请输入行业名称',
				icon: 'none'
			})
			return
		}
		if (!companyNum.value) {
			uni.showToast({
				title: '请输入公司人数',
				icon: 'none'
			})
			return
		}
		if (!firstProduct.value) {
			uni.showToast({
				title: '请输入公司主营产品',
				icon: 'none'
			})
			return
		}
		if (firstProductImg.length == 0) {
			uni.showToast({
				title: '请上传主营产品图',
				icon: 'none'
			})
			return
		}

		if (!companyBlurb.value) {
			uni.showToast({
				title: '请输入公司简介',
				icon: 'none'
			})
			return
		}
		let data = {
			companyArea: companyArea.value, //面积(单位:平方米)
			companyOutputValue: companyOutputValue.value, //年产值(单位:万元)
			companyAddress: companyAddress.value, //公司详细地址
			companyCity: companyCity.value, //市
			companyDistrict: companyDistrict.value, //区
			companyProvince: companyProvince.value, //省
			industryFirstId: industryFirstId.value, //行业一级id
			industryFirstName: industryFirstName.value, //行业一级名称
			industrySecondId: industrySecondId.value, //行业二级id 
			industrySecondName: industrySecondName.value, //行业二级名称
			companyNum: companyNum.value, //公司人数
			companyName: dataItem.value.companyName,
			realName: dataItem.value.realName,
			companyPosition: dataItem.value.companyPosition,
			cardPhone: dataItem.value.cardPhone,
			businesslicense: dataItem.value.businesslicense,
			firstProductImg: firstProductImg.toString(), //行业图片
			firstProduct: firstProduct.value, //行业信息
			cardId: cardId.value,
			companyBlurb: companyBlurb.value ,//公司简介
			wxCode:dataItem.value.wxCode,//微信号
		}
		console.log(data)
		uni.navigateTo({
			url: '/my/Enterprise/PublishEnd?dataItem=' + JSON.stringify(data)
		})
	};
	// 删除产品图
	const removeImage = (index) => {
		firstProductImg.splice(index, 1)
	};
	//上传图片
	const addImages = () => {
		uni.chooseImage({
			count: 8 - firstProductImg.length,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.showLoading({
						title: '上传中...'
					})
					uni.uploadFile({ // 上传接口
						// url: config.APIHOST + '/alioss/upload',
						url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							firstProductImg.push(JSON.parse(uploadFileRes.data).data)
							uni.hideLoading();
						}
					});
				}
			}
		})
	};
</script>

<style lang="scss" scoped>
	page {
		background: #F3F2F8;
	}

	.headTop {
		width: 100%;
		position: relative;
		background: linear-gradient(to bottom, #98B1FF 0%, #F3F2F8 100%);
	}


	.boxBor {
		background: #FFFFFF;
		border-radius: 13rpx;
		margin: 0 30rpx;
		padding: 0 30rpx;

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
				flex: 1;
				margin-left: 30rpx;
				justify-content: flex-end;
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
		padding: 30rpx 30rpx 0;
		position: relative;

		.aiscIcon {
			display: flex;
			align-items: center;
			position: absolute;
			bottom: 20rpx;
			right: 30rpx;

			.aiscIcon_vip {
				width: 40rpx;
				height: 40rpx;
			}

			.aiscIcon_sc {
				width: 100rpx;
				height: 40rpx;
			}

		}



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

		.zzbox_boxImg {
			display: flex;
			flex-wrap: wrap;
			margin-top: 30rpx;
			justify-content: space-between;

			.zzbox_Imgs {
				width: 148rpx;
				height: 148rpx;
				margin-bottom: 15px;
				position: relative;

				image {
					width: 100%;
					height: 100%;
					border-radius: 10rpx;
				}

				.zzbox_ImgClose {
					position: absolute;
					right: -10rpx;
					top: -10rpx
				}
			}

			.zzbox_Img {
				width: 148rpx;
				height: 148rpx;
				background: #F5F6FA;
				border-radius: 10rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;
				margin-bottom: 15px;

				text {
					color: #505050;
					font-size: 24rpx;
					margin-top: 10rpx;
				}

				image {
					width: 40rpx;
					height: 30rpx;
				}
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

	.popupVip {
		width: calc(500rpx - 60rpx);

		background: linear-gradient(0deg, #FFFFFF 0%, #FDF8EE 100%);
		border-radius: 32rpx;
		padding: 40rpx 30rpx 10rpx;
		text-align: center;

		image {
			width: 355rpx;
			height: 251rpx;
		}

		.popupVip_title {
			font-weight: bold;
			font-size: 38rpx;
			color: #D49E53;
		}

		.popupVip_tit {
			font-weight: 400;
			font-size: 26rpx;
			color: #333333;
			margin-top: 10rpx;
		}

		.popupVip_btn {
			width: 376rpx;
			height: 82rpx;
			background: #3E3E3E;
			border-radius: 18rpx;
			font-weight: 400;
			font-size: 26rpx;
			color: #FBE8C6;
			display: flex;
			align-items: center;
			justify-content: center;
			/* #ifdef H5 */
			margin: 40rpx auto;
			/* #endif */
			/* #ifndef H5 */
			margin: 40rpx auto 0;
			/* #endif */
		}
	}
</style>