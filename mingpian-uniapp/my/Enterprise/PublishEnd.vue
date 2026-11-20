<template>
	<view>
		<view class="headTop">

			<view class="zzbox">
				<view class="zzbox_title">
					<text>*</text>
					公司环境
				</view>

				<view class="zzbox_boxImg">
					<view class="zzbox_Imgs" v-for="(item,index) in companyAmbient" :key="index">
						<image :src="item" mode="aspectFill"></image>
						<view class="zzbox_ImgClose" @click="removeImage(index)">
							<up-icon name="close-circle-fill" color="#2979ff" size="22"></up-icon>
						</view>
					</view>
					<view class="zzbox_Img" @click="addImages" v-if="companyAmbient.length<8">
						<image src="/my/static/photo.png" mode="widthFix"></image>
						<text>添加图片</text>
					</view>
					<view class="zzbox_Imgs" style="height: 0;"></view>
					<view class="zzbox_Imgs" style="height: 0;"></view>
					<view class="zzbox_Imgs" style="height: 0;"></view>
				</view>


			</view>
		</view>
		<view class="zzrz">
			<view class="zzrz_Top">
				<view class="zzrz_title">
					<text>*</text>
					资质认证
				</view>
				<view class="" @click="AddQualification()">
					<up-icon name="plus-circle" color="#507AFE" size="25"></up-icon>
				</view>
			</view>

			<view class="zzrzbox" v-for="(item,index) in Qualificationlist" :key="index">
				<view class="zzboxClose" @click="removebox(index)" v-if="Qualificationlist.length>1">
					<up-icon name="close-circle-fill" color="#2979ff" size="22"></up-icon>
				</view>

				<view class="zzrzbox_head">
					<view class="zzrzbox_title" @click="openName(index)">{{item.name?item.name:'选择资质'}}</view>
					<view class="zzrzbox_cot" @click="UploadImage(item,index)">
						<up-icon name="photo-fill" color="#333333" size="24"></up-icon>
						<text>添加图片</text>
					</view>
				</view>

				<view class="zzrzbox_image" v-if="item.image&&item.image.length!=0">
					<view class="images" v-for="(ite,ind) in item.image" :key="ind" >
						<image :src="ite"></image>
						<view class="zzbox_ImgClose" @click="remove(index,ind)">
							<up-icon name="close-circle-fill" color="#2979ff" size="22"></up-icon>
						</view>
					</view>
					<view class="images" style="height: 0;"></view>
					<view class="images" style="height: 0;"></view>
				</view>
			</view>
		</view>

		<view class="zzbox">
			<view class="zzbox_title">
				<text>*</text>
				公司LOGO
			</view>

			<view class="zzbox_boxImg">
				<view class="zzbox_Imgs" v-if="companyLogo" @click="addImage">
					<image :src="companyLogo" mode="aspectFill"></image>
				</view>
				<view class="zzbox_Img" @click="addImage" v-else>
					<image src="/my/static/photo.png" mode="widthFix"></image>
					<text>添加图片</text>
				</view>
			</view>
		</view>

		<view class="btn" @click="submit()">确认发布</view>


		<up-picker :show="showZ" :columns="columns" keyName="value" @cancel="showZ = false"
			@confirm="confirm"></up-picker>
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
	const showZ = ref(false);
	const columns = reactive([]); //资质列表

	const companyAmbient = reactive([]); //公司环境
	const companyLogo = ref(''); //公司logo
	const Qualificationlist = reactive([{
		id: 1,
		name: '',
		image: []
	}]); //资质列表
	const QualificationIndex = ref('');
	const dataItem = ref({}); //上级页面传参数据
	const cardId = ref('');

	onLoad(option => {
		if (option.dataItem) {
			dataItem.value = JSON.parse(option.dataItem)
			console.log(dataItem.value)
			if (dataItem.value.cardId) {
				cardId.value = dataItem.value.cardId
				getCardInfo()
			}
		}
	});
	onShow(() => {
		getzizhi()
	});
	/* 获取资质认证 */
	const getzizhi = () => {
		columns.length =0
		proxy.$http.get("/app/dict/list", {
			type: '资质证书'
		}).then(res => {
			if (res.code == 0) {
				columns.push(res.data)

			}
		});
	};
	// 获取名片详情
	const getCardInfo = () => {
		let userId = uni.getStorageSync('userId');
		proxy.$http.get("/app/businessCard/info?cardId=" + cardId.value).then(res => {
			if (res.code == 0) {
				
				if(res.data.companyAmbient){
					let imageArr = res.data.companyAmbient.split(',')
					companyAmbient.splice(0, companyAmbient.length, ...imageArr);
				}
				
				if (res.data.companyFlair) {
					let companyFlair = JSON.parse(res.data.companyFlair)

					let arr = []
					companyFlair.forEach((d, index) => {
						let data = {
							id: index + 1,
							name: d.name, //资质认证名称
							image: d.pic.split(',') //资质认证图片
						}
						arr.push(data)

					})
					Qualificationlist.splice(0, Qualificationlist.length, ...arr);
				}
				companyLogo.value = res.data.companyLogo //公司logo
			}
		});
	};
	// 移出资质框
	const removebox = (index) => {
		Qualificationlist.splice(index, 1)
	};
	// 添加新资质
	const AddQualification = () => {
		for (var i = 0; i < Qualificationlist.length; i++) {
			if (Qualificationlist[i].name === '' || Qualificationlist[i].image.length === 0) {
				uni.showToast({
					title: '请完成资质之后新增资质',
					icon: 'none'
				})
				return
			}
		}
		let data = {
			id: Qualificationlist.length + 1,
			name: '',
			image: []
		}
		Qualificationlist.push(data)
	};
	// 删除资质图片
	const remove = (index, ind) => {
		Qualificationlist[index].image.splice(ind, 1)
	};
	// 选择资质
	const confirm = (e) => {
		console.log(e)
		Qualificationlist[QualificationIndex.value].name = e.value[0].value
		showZ.value = false
	};
	// 打开选择资质组件
	const openName = (index) => {
		QualificationIndex.value = index
		showZ.value = true
	};
	// 名片发布
	const submit = () => {
		if (companyAmbient.length == 0) {
			uni.showToast({
				title: '请上传公司环境图',
				icon: 'none'
			})
			return
		}
		let companyFlairs = []
		if (Qualificationlist[0].image.length==0) {
			uni.showToast({
				title: '请添加资质认证',
				icon: 'none'
			})
			return
		} else {
			Qualificationlist.forEach(d => {
				let data = {
					name: d.name, //资质认证名称
					pic: d.image.toString() //资质认证图片
				}
				companyFlairs.push(data)
			})
		}
		if (!companyLogo.value) {
			uni.showToast({
				title: '请上传公司logo',
				icon: 'none'
			})
			return
		}
		let data = {
			companyArea: dataItem.value.companyArea, //面积(单位:平方米)
			companyOutputValue: dataItem.value.companyOutputValue, //年产值(单位:万元)
			companyAddress: dataItem.value.companyAddress, //公司详细地址
			companyCity: dataItem.value.companyCity, //市
			companyDistrict: dataItem.value.companyDistrict, //区
			companyProvince: dataItem.value.companyProvince, //省
			industryFirstId: dataItem.value.industryFirstId, //行业一级id
			industryFirstName: dataItem.value.industryFirstName, //行业一级名称
			industrySecondId: dataItem.value.industrySecondId, //行业二级id 
			industrySecondName: dataItem.value.industrySecondName, //行业二级名称
			companyNum: dataItem.value.companyNum, //公司人数
			companyName: dataItem.value.companyName,
			realName: dataItem.value.realName,
			companyPosition: dataItem.value.companyPosition,
			cardPhone: dataItem.value.cardPhone,
			companyLicense: dataItem.value.businesslicense, //营业执照
			firstProductImg: dataItem.value.firstProductImg, //行业图片
			firstProduct: dataItem.value.firstProduct, //行业信息
			companyAmbient: companyAmbient.toString(),
			companyLogo: companyLogo.value,
			companyFlairs: companyFlairs,
			cardType: 2,
			companyBlurb:dataItem.value.companyBlurb,
			wxCode:dataItem.value.wxCode
		}
		if (cardId.value) {
			data.cardId = cardId.value
			proxy.$http.postJson('/app/businessCard/edit', data).then(res => {
				if (res.code === 0) {
					uni.showToast({
						title: '修改成功',
						icon: "none"
					})
					uni.switchTab({
						url: '/pages/Enterprise/index'
					})
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});

		} else {
			proxy.$http.postJson('/app/businessCard/insert', data).then(res => {
				if (res.code === 0) {
					uni.showToast({
						title: '发布成功',
						icon: "none"
					})
					uni.switchTab({
						url: '/pages/Enterprise/index'
					})
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		}
	};
	// 删除公司环境
	const removeImage = (index) => {
		companyAmbient.splice(index, 1)
	};
	//上传公司环境图片
	const addImages = () => {
		uni.chooseImage({
			count: 8 - companyAmbient.length,
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
							companyAmbient.push(JSON.parse(uploadFileRes.data).data)
							uni.hideLoading();
						}
					});
				}
			}
		})
	};
	//上传公司Logo图片
	const addImage = () => {
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
							companyLogo.value = JSON.parse(uploadFileRes.data)
								.data
							uni.hideLoading();
						}
					});
				}
			}
		})
	};
	//上传资质图片
	const UploadImage = (item, index) => {
		if (!item.name) {
			uni.showToast({
				title: '请先选择资质',
				icon: 'none'
			})
			return
		}
		uni.chooseImage({
			count: 8 - item.image.length,
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
							// item.image.push(JSON.parse(uploadFileRes.data).data)
							Qualificationlist[index].image.push(JSON.parse(uploadFileRes.data).data)
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

	.headTop {
		width: 100%;
		position: relative;
		background: linear-gradient(to bottom, #98B1FF 0%, #F3F2F8 100%);
	}

	.boxBor {
		background: #FFFFFF;
		border-radius: 13rpx;
		margin: 20rpx 30rpx;
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
		margin: 0 30rpx;
		padding: 30rpx 30rpx 0;

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

	.zzrz {
		background: #FFFFFF;
		border-radius: 13rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.zzrz_Top {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.zzrz_title {

				font-weight: bold;
				font-size: 28rpx;
				color: #333333;

				text {

					font-weight: bold;
					font-size: 30rpx;
					color: #FF0B1C;
				}
			}

		}

		.zzrzbox {
			background: #F5F5F5;

			padding: 20rpx 30rpx;
			border-radius: 18rpx;
			margin: 20rpx 0;
			position: relative;

			.zzboxClose {
				position: absolute;
				right: -10rpx;
				top: -10rpx
			}

			.zzrzbox_head {
				display: flex;
				align-items: center;
				justify-content: space-between;

				.zzrzbox_title {
					font-size: 26rpx;
				}

				.zzrzbox_cot {
					display: flex;
					align-items: center;

					text {
						font-size: 24rpx;
						margin-left: 10rpx;
					}
				}
			}

			.zzrzbox_image {
				display: flex;
				flex-wrap: wrap;
				justify-content: space-between;



				.images {
					width: 180rpx;
					height: 180rpx;
					border-radius: 10rpx;
					position: relative;
					margin-top: 20rpx;

					image {
						width: 180rpx;
						height: 180rpx;
						border-radius: 10rpx;
					}

					.zzbox_ImgClose {
						position: absolute;
						right: -10rpx;
						top: -10rpx
					}
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
</style>