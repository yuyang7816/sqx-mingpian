<template>
	<view style="padding-bottom: 100rpx;">
		<up-sticky bgColor="#FFFFFF">
			<view class="">

				<up-tabs :list="listTab" :scrollable="false" @click="clickTab" lineColor="#287AFE"
					:current="current"></up-tabs>
			</view>
		</up-sticky>
		<view class="TopRemk" v-if="RefuseRemk&&CertificationStatus==2">拒绝理由：{{RefuseRemk}}</view>
		<view class="box">
			<view class=" ">
				<view class="boxItem_tit">{{current==0?'姓名':'法人'}}</view>
				<view class="boxItem_Input">
					<up-input placeholder="请填写(必填)" border="none" v-model="realName"></up-input>
				</view>
			</view>
			<block v-if="current==0">
				<view class=" padding-top">
					<view class="boxItem_tit">证件号码</view>
					<view class="boxItem_Input" style="border-bottom: none;">
						<up-input placeholder="请填写(必填)" border="none" v-model="identityCardNumber"></up-input>
					</view>
				</view>
			</block>

			<block v-if="current==1">
				<view class="padding-top">
					<view class="boxItem_tit">企业名称</view>
					<view class="boxItem_Input">
						<up-input placeholder="请填写企业名称" border="none" v-model="enterpriseName"></up-input>
					</view>
				</view>
				<view class="padding-top">
					<view class="boxItem_tit">企业联系方式</view>
					<view class="boxItem_Input">
						<up-input placeholder="请填写企业联系方式" border="none" v-model="enterprisePhone"></up-input>
					</view>
				</view>

				<view class=" padding-top">
					<view class="boxItem_tit">公司所在地</view>
					<view class="boxItem_Input" @click="openMap()">
						<view v-if="enterpriseAddress">{{enterpriseAddress}}</view>
						<view v-else class="input-placeholder">请选择公司所在地</view>
						<!-- <up-input placeholder="请填写(必填)" border="none" v-model="enterpriseAddress"></up-input> -->
					</view>
				</view>

				<view class=" padding-top">
					<view class="boxItem_tit">注册资金</view>
					<view class="boxItem_Input">
						<up-input placeholder="请填写(必填)" border="none" v-model="capital"></up-input>
					</view>
				</view>

				<view class=" padding-top">
					<view class="boxItem_tit">注册时间</view>
					<view class="boxItem_Input" @click="showCapital = true">
						<view v-if="registrationDate">{{registrationDate}}</view>
						<view v-else class="input-placeholder">请选择注册时间</view>
						<!-- <up-input placeholder="请填写（必填）" border="none" v-model="registrationDate"></up-input> -->
					</view>
				</view>

				<view class=" padding-top">
					<view class="boxItem_tit">营业时间</view>
					<view class="boxItem_Input" style="border-bottom: none;" @click="showHours = true">
						<view v-if="businessHours">{{businessHours}}</view>
						<view v-else class="input-placeholder">请选择注册时间</view>
						<!-- <up-input placeholder="请填写（必填）" border="none" v-model="businessHours" :readonly="true"
							@click="show = true"></up-input> -->
					</view>
				</view>
			</block>

		</view>

		<view class="box" v-if="current==0">
			<view class="boxItem_tit">身份证照片</view>
			<view class="boxImage" @click="addImages(3)">
				<image :src="identityCardFront" v-if="identityCardFront" mode="aspectFill"></image>
				<view class="boxImage_Icon" v-else>
					<image src="/my/static/photo.png"></image>
					<text>身份证正面</text>
				</view>
			</view>
			<view class="boxImage" @click="addImages(4)">
				<image :src="identityCardVerso" v-if="identityCardVerso" mode="aspectFill"></image>
				<view class="boxImage_Icon" v-else>
					<image src="/my/static/photo.png"></image>
					<text>身份证反面</text>
				</view>
			</view>
		</view>

		<view class="box" v-if="current==1">
			<view class="boxItem_tit">营业执照照片</view>
			<view class="boxImage" @click="addImages(1)">
				<image :src="businessLicense" v-if="businessLicense" mode="aspectFill"></image>
				<view class="boxImage_Icon" v-else>
					<image src="/my/static/photo.png"></image>
					<text>营业执照正面</text>
				</view>
			</view>
			<view class="boxImage" @click="addImages(2)">
				<image :src="businessLicenseImg" v-if="businessLicenseImg" mode="aspectFill"></image>
				<view class="boxImage_Icon" v-else>
					<image src="/my/static/photo.png"></image>
					<text>营业执照反面</text>
				</view>
			</view>
		</view>
		<!-- <view class="btn" @click="submit()">提交审核</view> -->

		<view class="" v-if="current==0">
			<view class="btn" @click="submit()" v-if="CertificationStatus!=0&&CertificationStatus!=1">提交审核</view>
		</view>
		<view class="" v-if="current==1">
			<view class="btn" @click="submit()" v-if="CertificationStatus!=0&&CertificationStatus!=1">提交审核</view>
		</view>
		<!-- 注册时间 -->
		<up-datetime-picker :show="showCapital" v-model="dataTime" mode="date" @cancel="showCapital = false"
			@confirm="confirmCapital"></up-datetime-picker>

		<!-- 营业时间 -->
		<up-datetime-picker :show="showHours" mode="time" @cancel="showHours = false"
			@confirm="confirmHours"></up-datetime-picker>


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
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	const userType = ref(1); //认证类型 1普通用户 2企业用户
	const realName = ref(''); //姓名
	const identityCardNumber = ref(''); //证件号码
	const enterpriseName = ref(''); //企业名称
	const enterprisePhone = ref(''); //企业联系电话
	const businessLicense = ref(''); //营业执照 正面
	const businessLicenseImg = ref(''); //营业执照 反面
	const identityCardFront = ref(''); //身份证 正面
	const identityCardVerso = ref(''); //身份证 反面
	const enterpriseAddress = ref(''); //企业地址
	const capital = ref(''); //注册资金
	const registrationDate = ref(''); //注册日期
	const businessHours = ref(''); //注册企业营业时间
	const showCapital = ref(false);
	const dataTime = Number(new Date());
	const showHours = ref(false);


	const identityCardBack = ref('');
	const show = ref(false);
	const valueData = ref(Date.now());
	const CertificationStatus = ref(-1); //认证状态  0-待审核 1-已通过 2-未通过
	const RefuseRemk = ref('');
	const isUserCertification = ref(false); //个人
	const isCompanyCertification = ref(false); //企业

	// 创建响应式数据  
	const listTab = reactive([{
		name: '个人认证',
		id: 1
	}, {
		name: '企业认证',
		id: 2
	}]);
	const current = ref(0);

	onLoad(option => {
		if (option.userType) {
			current.value = listTab[current.value].id
			userType.value = listTab[current.value].id
		} else {
			current.value = 0
			userType.value = listTab[current.value].id
		}
		getUserInfo()

	});
	onShow(() => {});
	// 选择时间
	const confirmHours = (e) => {
		console.log(e)
		businessHours.value = e.value
		showHours.value = false
	};
	// 选择日期
	const confirmCapital = (e) => {
		console.log(e, timeFormat(e.value, 'yyyy-mm-dd'))
		registrationDate.value = timeFormat(e.value, 'yyyy-mm-dd')
		showCapital.value = false
	};
	// 选择位置
	const openMap = () => {
		uni.chooseLocation({
			success: function(res) {
				console.log('位置名称：' + res.name);
				console.log('详细地址：' + res.address);
				console.log('纬度：' + res.latitude);
				console.log('经度：' + res.longitude);
				enterpriseAddress.value = res.address
			}
		});

	};
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				isUserCertification.value = res.data.isUserCertification //个人
				isCompanyCertification.value = res.data.isCompanyCertification //企业
				getUserCertification()
			}
		});
	};
	// 切换个人 企业认证
	const clickTab = (e) => {
		console.log(e)
		current.value = e.index
		userType.value = listTab[current.value].id
		getUserCertification()
	};
	// 选择时间
	const confirm = (e) => {
		console.log(e)
		businessHours.value = timeFormat(e.value, 'yyyy-mm-dd hh:MM');
		show.value = false
	};
	// 查询用户认证状态
	const getUserCertification = () => {
		let userType = listTab[current.value].id
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/userCertification/queryInsert?userId=" + userId + '&cardType=' + userType).then(res => {
			if (res.code == 0 && res.data) {
				CertificationStatus.value = res.data.status
				RefuseRemk.value = res.data.remark
				console.log(isUserCertification.value)
				if (current.value == 0) {
					realName.value = res.data.realName //姓名
					identityCardNumber.value = res.data.identityCardNumber //证件号码
					identityCardFront.value = res.data.identityCardFront //身份证 正面
					identityCardVerso.value = res.data.identityCardVerso //身份证 反面
				}
				if (current.value == 1) {
					realName.value = res.data.realName //姓名
					enterpriseName.value = res.data.enterpriseName //企业名称
					enterprisePhone.value = res.data.enterprisePhone //企业联系电话
					businessLicense.value = res.data.businessLicense //营业执照 正面
					businessLicenseImg.value = res.data.businessLicenseImg //营业执照 反面
					enterpriseAddress.value = res.data.enterpriseAddress //企业地址
					capital.value = res.data.capital //注册资金
					registrationDate.value = res.data.registrationDate //注册日期
					businessHours.value = res.data.businessHours //注册营业时间
				}
			}else{
				CertificationStatus.value = -1
			}
		});
	};
	// 认证提交
	const submit = () => {
		console.log(form.value)
		if (current.value == 0) {
			if (!realName.value) {
				uni.showToast({
					title: '请填写姓名',
					icon: 'none'
				})
				return
			}
			if (!identityCardNumber.value) {
				uni.showToast({
					title: '请填写证件号码',
					icon: 'none'
				})
				return
			}

			if (!identityCardFront.value) {
				uni.showToast({
					title: '请上传身份证正面',
					icon: 'none'
				})
				return
			}
			if (!identityCardVerso.value) {
				uni.showToast({
					title: '请上传身份证反面',
					icon: 'none'
				})
				return
			}
			let data = {
				realName: realName.value,
				identityCardNumber: identityCardNumber.value,
				identityCardFront: identityCardFront.value,
				identityCardVerso: identityCardVerso.value,
			}
		}
		if (current.value == 1) {
			if (!realName.value) {
				uni.showToast({
					title: '请填写法人姓名',
					icon: 'none'
				})
				return
			}

			if (!enterpriseName.value) {
				uni.showToast({
					title: '请填写企业名称',
					icon: 'none'
				})
				return
			}
			if (!enterprisePhone.value) {
				uni.showToast({
					title: '请填写企业联系电话',
					icon: 'none'
				})
				return
			}
			if (!enterpriseAddress.value) {
				uni.showToast({
					title: '请填写企业地址',
					icon: 'none'
				})
				return
			}
			if (!capital.value) {
				uni.showToast({
					title: '请填写注册资金',
					icon: 'none'
				})
				return
			}

			if (!registrationDate.value) {
				uni.showToast({
					title: '请填写注册日期',
					icon: 'none'
				})
				return
			}
			if (!businessHours.value) {
				uni.showToast({
					title: '请填写营业时间',
					icon: 'none'
				})
				return
			}

			if (!businessLicense.value) {
				uni.showToast({
					title: '请上传营业执照正面',
					icon: 'none'
				})
				return
			}
			if (!businessLicenseImg.value) {
				uni.showToast({
					title: '请上传营业执照反面',
					icon: 'none'
				})
				return
			}

			let data = {
				capital: capital.value,
				registrationDate: registrationDate.value,
				businessHours: businessHours.value,
				realName: realName.value,
				enterpriseName: enterpriseName.value,
				enterprisePhone: enterprisePhone.value,
				businessLicense: businessLicense.value,
				businessLicenseImg: businessLicenseImg.value,
				enterpriseAddress: enterpriseAddress.value,
			}
		}

		proxy.$http.getT("/app/userCertification/insert", data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '提交成功'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 500)
			}
		});
	};
	//上传图片
	const addImages = (e) => {
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

							if (e == 1) { //营业执照 正面
								form.value.businessLicense = JSON.parse(uploadFileRes.data)
									.data
							} else if (e == 2) { //营业执照 反面
								form.value.businessLicenseImg = JSON.parse(uploadFileRes.data)
									.data
							} else if (e == 3) { //身份证 正面
								form.value.identityCardFront = JSON.parse(uploadFileRes.data)
									.data
							} else if (e == 4) { //身份证 反面
								form.value.identityCardVerso = JSON.parse(uploadFileRes.data)
									.data
							}

							uni.hideLoading();
						}
					});
				}
			}
		})
	};

	onPullDownRefresh(() => {});
	onReachBottom(() => {});
</script>

<style lang="scss">
	page {
		background: #F6F7FB;
	}

	.input-placeholder {
		 
		font-weight: 500;
		font-size: 24rpx;
		color: #CCCCCC;
	}

	.TopRemk {
		background: #FFFFFF;
		padding: 30rpx;
	}

	.box {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin: 20rpx 30rpx;

		.boxItem_tit {
			 
			font-weight: 800;
			font-size: 28rpx;
			color: #000000;
		}

		.boxItem_Input {
			padding: 15rpx 0 10rpx;
			border-bottom: 1px solid #F2F2F2;
		}

		.boxImage {
			width: 100%;
			height: 320rpx;
			margin: 20rpx 0;
			border: 1px dashed #E0E0E0;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				width: 100%;
				height: 100%;
			}

			.boxImage_Icon {
				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;

				image {
					width: 73rpx;
					height: 65rpx;
				}

				text {
					 
					font-weight: 500;
					font-size: 20rpx;
					color: #353535;
					margin-top: 10rpx;
				}
			}
		}

	}

	.btn {
		margin: 30rpx;
		height: 90rpx;
		background: linear-gradient(-51deg, #287AFE 0%, #4A8FFF 100%);
		border-radius: 50rpx;
		 
		font-weight: 500;
		font-size: 30rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>