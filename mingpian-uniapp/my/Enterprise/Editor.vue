<template>
	<view style="padding-bottom: 140rpx;">
		<view class="box" :style="{'background-image':'url('+bgIamgeUrl+')'}">
			<view class="boxbg">

				<view class="box_title">{{companyItem.realName}}</view>
				<view class="box_zw">{{companyItem.companyPosition}}</view>

				<view class="" style="margin-top:50rpx">{{companyItem.companyName}}</view>
				<view class="bxoItm">
					<view class="bxoItm_Image">
						<image src="/my/static/mb_phone.png"></image>
					</view>
					<text>{{companyItem.cardPhone}}</text>
				</view>
				<view class="bxoItm" v-if="companyItem.email">
					<view class="bxoItm_Image">
						<image src="/my/static/mb_email.png"></image>
					</view>
					<text>{{companyItem.email}}</text>
				</view>
				<view class="bxoItm" v-if="companyItem.companyAddress">
					<view class="bxoItm_Image">
						<image src="/my/static/mb_add.png"></image>
					</view>
					<text style="width: 600rpx;">{{companyItem.companyAddress}}</text>
				</view>
			</view>
		</view>



		<view class="bgBox">
			<view class="bgBox_title">选择背景</view>
			<view class="bgBox_scrollView">
				<scroll-view class="scroll-view_H" scroll-x="true" scroll-left="20">
					<view class=" align-center padding-top-sm" style="display: inline-flex;">
						<view class="scroll-view_HImage" v-if="backImg&&bglistIndex===-1">
							<image :src="backImg" mode="aspectFill"></image>
							<view class="closeBgimg" @click="removeBgimg()">
								<up-icon name="close-circle-fill" color="#2979ff" size="20"></up-icon>
							</view>
						</view>
						<view class="scroll-view_HAdd" v-else @click="addImages(1)">
							<up-icon name="plus" color="#666666" size="18"></up-icon>
						</view>

						<view v-for="(item,index) in bglist" :key="index" class="scroll-view-item_H"
							@click.stop="changeBgimg(item,index)">
							<image :src="item.pic" mode="aspectFill"></image>
							<view class="Allin" @click.stop="removeBgimg()" v-if="bglistIndex==index">
								<up-icon name="checkmark" color="#FFFFFF" size="15"></up-icon>
							</view>
							<view class="close" @click.stop="removeIcon(index,item)">
								<up-icon name="close-circle-fill" color="#2979ff" size="15"></up-icon>
							</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</view>

		<view class="qybox">
			<view class="qybox_title">上传企业logo</view>
			<view class="qybox_Itm">
				<view class="qybox_ItmImage" v-if="companyLogo" @click="addImages(2)">
					<image :src="companyLogo" mode="aspectFill"></image>
				</view>
				<view class="qybox_Itmabeg" v-else @click="addImages(2)">
					<up-icon name="plus" color="#666666" size="18"></up-icon>
				</view>
				<view class="qybox_Itmatit">支持jpg.png 不得超过5M</view>
			</view>
		</view>

		<view class="infobox">
			<view class="infobox_title">展示信息</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">
					姓名
					<text>*</text>
				</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写联系人" border="none" v-model="userName"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">
					公司名称
					<text>*</text>
				</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写公司名称" border="none" v-model="companyName"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">
					职位
					<text>*</text>
				</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写职位" border="none" v-model="companyuPisition"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">
					手机
					<text>*</text>
				</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写手机" border="none" v-model="phone"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">邮箱</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写邮箱" border="none" v-model="email"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">地址</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写地址" border="none" v-model="companyAddress"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">公司简介</view>
				<view class="infoboxItem_cont" style="height: 400rpx;border: 1rpx solid #E6E6E6;">
					<view class="">
						<up-textarea v-model="companyDesc" placeholder="请输入企业介绍" maxlength="-1" height="300rpx"
							border="none"></up-textarea>

					</view>
					<view class=" aiscIcon" @click="clickAi()">
						<image src="/my/static/vipIcon.png" mode="widthFix" class="aiscIcon_vip"></image>
						<image src="/my/static/aishengcheng.png" class="aiscIcon_sc" mode="widthFix"></image>
					</view>
				</view>


			</view>
		</view>


		<view class="taber">
			<!-- <view class="btnline">删除模板</view> -->
			<view class="btnshi" @click="saveTemplate()">保存模板</view>
		</view>


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
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const bgIamgeUrl = ref('');
	const bglist = reactive([]);
	const bglistIndex = ref(-1);
	const backImg = ref(''); //图片背景
	const companyLogo = ref(''); //企业logo
	const userName = ref(''); //用户名称
	const companyName = ref(''); //公司名称
	const companyuPisition = ref(''); //职务名称
	const phone = ref(''); //手机号码
	const email = ref(''); //邮箱
	const companyAddress = ref(''); //公司地址
	const companyDesc = ref(''); //公司简介
	const companyCardId = ref('');
	const id = ref('')
	const companyItem = ref('');

	const showVip = ref(false);
	const isVip = ref(false);



	onLoad(option => {

		if (option.companyCardId) {
			companyCardId.value = option.companyCardId

			getcompanyTemplate()
		}
		if (option.id) {
			id.value = option.id
		}

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
			if (!companyName.value) {
				uni.showToast({
					title: '请输入公司名称',
					icon:'none'
				})
				return
			}
			uni.showLoading({
				title: '生成中...'
			})
			let userId = uni.getStorageSync('userId');
			proxy.$http.get("/app/ai/createInfo?companyName=" + companyName.value).then(res => {
				if (res.code == 0) {
					companyDesc.value = res.data
				}
				uni.hideLoading()
			});
		} else {
			showVip.value = true
		}
	};

	const removeIcon = (index, ite) => {

		proxy.$http.get('/app/companyTemplatePic/delete/' + ite.id).then(res => {
			if (res.code == 0) {
				bglist.splice(index, 1)
			}
		})
	};
	// 查询企业模板
	const getcompanyTemplate = () => {
		let data = {
			companyCardId: companyCardId.value
		}
		proxy.$http.post("/app/template/info", data).then(red => {
			if (red.code == 0 && red.data) {
				// Object.assign(CardInfo, red.data)
				companyItem.value = {
					realName: red.data.userName,
					companyPosition: red.data.companyuPisition,
					companyName: red.data.companyName,
					cardPhone: red.data.phone,
					email: red.data.email,
					companyAddress: red.data.companyAddress,
					companyLogo: red.data.companyLogo
				}

				companyName.value = red.data.companyName
				companyLogo.value = red.data.companyLogo
				userName.value = red.data.userName
				companyuPisition.value = red.data.companyuPisition
				phone.value = red.data.phone
				bgIamgeUrl.value = red.data.backImg
				email.value = red.data.email
				companyAddress.value = red.data.companyAddress
				companyDesc.value = red.data.companyDesc

			} else {
				companyItem.value = uni.getStorageSync('companyInfo')
				console.log(companyItem.value)
				let companyInfo = uni.getStorageSync('companyInfo')
				companyName.value = companyInfo.companyName
				companyLogo.value = companyInfo.companyLogo
				userName.value = companyInfo.realName
				companyuPisition.value = companyInfo.companyPosition
				phone.value = companyInfo.cardPhone

			}
			getbglist()
		});
	};
	// 获取背景列表
	const getbglist = () => {
		let data = {
			// companyCardId: companyCardId.value,
			pageNum: 1,
			pageSize: 100,
		}
		proxy.$http.postJson("/app/companyTemplatePic/page", data).then(red => {
			if (red.code == 0) {
				bglist.splice(0, bglist.length, ...red.data.records);
				if (!bgIamgeUrl.value) {
					bgIamgeUrl.value = bglist[0].pic
				}
			}
		});
	};
	// 保存背景图
	const saveTemplate = () => {
		// if (!backImg.value) {
		// 	uni.showToast({
		// 		title: '请选择背景图',
		// 		icon: 'none'
		// 	})
		// 	return
		// }
		if (!companyLogo.value) {
			uni.showToast({
				title: '请上传企业logo',
				icon: 'none'
			})
			return
		}
		if (!userName.value) {
			uni.showToast({
				title: '请填写联系人',
				icon: 'none'
			})
			return
		}
		if (!companyName.value) {
			uni.showToast({
				title: '请填写公司名称',
				icon: 'none'
			})
			return
		}
		if (!companyuPisition.value) {
			uni.showToast({
				title: '请填写职务名称',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请填写手机号码',
				icon: 'none'
			})
			return
		}

		let userId = uni.getStorageSync('userId')
		let data = {
			userId: userId,
			backImg: backImg.value, //图片背景
			companyLogo: companyLogo.value, //企业logo
			userName: userName.value, //用户名称
			companyName: companyName.value, //公司名称
			companyuPisition: companyuPisition.value, //职务名称
			phone: phone.value, //手机号码
			companyCardId: companyCardId.value,
			email: email.value,
			companyAddress: companyAddress.value,
			companyDesc: companyDesc.value,
		}
		console.log(data)
		if (id.value) {
			data.id = id.value
			proxy.$http.postJson('/app/template/update', data).then(res => {
				if (res.code === 0) {
					uni.showToast({
						title: '保存成功',
						icon: 'none'
					})
					setTimeout(res => {
						uni.navigateBack()
					}, 500)
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		} else {

			proxy.$http.postJson('/app/template/save', data).then(res => {
				if (res.code === 0) {
					uni.showToast({
						title: '保存成功',
						icon: 'none'
					})
					setTimeout(res => {
						uni.navigateBack()
					}, 500)
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		}
	};
	// 选择系统背景图
	const changeBgimg = (item, index) => {
		bgIamgeUrl.value = item.pic
		bglistIndex.value = index
		backImg.value = item.pic
		return
		if (backImg.value && bglistIndex.value === -1) {
			uni.showToast({
				title: '已上传背景图',
				icon: 'none'
			})
			return
		} else {
			bgIamgeUrl.value = item.pic
			bglistIndex.value = index
			backImg.value = item.pic
		}
	};
	//移除背景图
	const removeBgimg = () => {
		backImg.value = ''
	};
	//上传图片
	const addImages = (index) => {
		uni.chooseImage({
			count: index == 1 ? 6 : 1,
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
							if (index == 1) {
								bglistIndex.value = -1
								// backImg.value = JSON.parse(uploadFileRes.data).data
								let imageUrl = JSON.parse(uploadFileRes.data).data
								saveBgImage(imageUrl)
							} else {
								companyLogo.value = JSON.parse(uploadFileRes.data).data
							}
							uni.hideLoading();
						}
					});
				}
			}
		})
	};
	/* 报错企业模板背景图 */
	const saveBgImage = (imageUrl) => {
		proxy.$http.postJson('/app/companyTemplatePic/save', {
			companyCardId: companyCardId.value,
			pic: imageUrl
		}).then(res => {
			if (res.code === 0) {
				getbglist()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background: #FFFFFF;
	}

	.box {
		margin: 20rpx;

		border-radius: 12rpx;
		background-size: 100% 100%;
		background-repeat: no-repeat;
		// padding: 0 40rpx;

		color: #FFFFFF;

		.boxbg {
			width: 98%;
			height: 405rpx;
			padding: 0 40rpx;
			background: rgba(255, 255, 255, 0.3);
			border-radius: 12rpx;
		}

		.box_title {
			padding-top: 30rpx;
			font-size: 42rpx;
			font-weight: bold;
		}

		.box_zw {
			font-size: 26rpx;
			margin-top: 20rpx;
		}

		.bxoItm {
			display: flex;
			align-items: center;
			margin-top: 10rpx;

			.bxoItm_Image {
				width: 25rpx;
				height: 25rpx;
				display: flex;
				align-items: center;
				justify-content: center;

				image {
					width: 25rpx;
					height: 25rpx;
				}
			}

			text {
				font-size: 24rpx;
				margin-left: 10rpx;
			}
		}
	}

	.bgBox {
		margin: 30rpx;

		.bgBox_title {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 30rpx;
			color: #333333;
		}

		.bgBox_scrollView {
			.scroll-view_H {
				white-space: nowrap;
				width: 100%;


				.scroll-view-item_H {
					display: inline-flex;
					width: 152rpx;
					height: 88rpx;
					margin-right: 20rpx;
					position: relative;

					image {
						width: 152rpx;
						height: 88rpx;
						border-radius: 6rpx;
					}

					.Allin {
						position: absolute;
						bottom: 0;
						right: 0;
						border-radius: 24rpx 0 6rpx 0;
						background: #287AFE;
						padding: 4rpx 8rpx 4rpx 15rpx;
					}

					.close {
						position: absolute;
						top: -6rpx;
						right: -10rpx;
					}
				}

				.scroll-view_HAdd {
					display: inline-flex;
					width: 152rpx;
					height: 88rpx;
					background: #F5F5FB;
					border-radius: 6rpx;
					border: 1px solid #CCCCCC;
					align-items: center;
					justify-content: center;
					margin-right: 20rpx;
				}

				.scroll-view_HImage {
					display: inline-flex;
					width: 152rpx;
					height: 88rpx;
					margin-right: 20rpx;
					position: relative;

					image {
						width: 152rpx;
						height: 88rpx;
						border-radius: 6rpx;
					}

					.closeBgimg {
						position: absolute;
						top: -10rpx;
						right: -10rpx;
					}
				}
			}
		}
	}

	.qybox {
		margin: 0 30rpx;

		.qybox_title {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 30rpx;
			color: #333333;
		}

		.qybox_Itm {
			display: flex;
			align-items: flex-end;
			margin-top: 20rpx;

			.qybox_ItmImage {
				width: 150rpx;
				height: 150rpx;
				border-radius: 6rpx;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.qybox_Itmabeg {
				width: 150rpx;
				height: 150rpx;
				background: #F5F5FB;
				border-radius: 6rpx;
				border: 1px solid #CCCCCC;
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.qybox_Itmatit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
				margin-left: 10rpx;
			}
		}
	}

	.input-placeholder {

		font-weight: 500;
		font-size: 26rpx;
		color: #666666;
	}

	.infobox {
		margin: 30rpx 30rpx;

		.infobox_title {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 30rpx;
			color: #333333;
		}

		.infoboxItem {
			margin-top: 15rpx;

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
				border-bottom: 1px solid #E6E6E6;
				margin-top: 10rpx;
				padding-bottom: 20rpx;
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
			}
		}
	}

	.taber {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 50rpx 30rpx;

		.btnline {
			width: 330rpx;
			height: 88rpx;
			background: #FFFFFF;
			border-radius: 44rpx;
			border: 1px solid #CCCCCC;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 30rpx;
			color: #999999;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.btnshi {
			flex: 1;
			// width: 330rpx;
			height: 88rpx;
			background: #287AFE;
			border-radius: 44rpx;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 30rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
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