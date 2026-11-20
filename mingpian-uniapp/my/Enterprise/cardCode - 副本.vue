<template>
	<view>
		<view class="boxCont" v-if="CardInfo">
			<view class="lbox">
				<view class="lboxItem" style="padding: 70rpx 120rpx 0;" >
					<view class="lboxItem_label">{{CardInfo.companyPosition}}</view>
					<view class="lboxItem_label">{{CardInfo.industryFirstName}}</view>
				</view>
				<view class="lboxItem" style="padding: 60rpx 60rpx;">
					<view class="lboxItem_label">{{CardInfo.companyProvince}}{{CardInfo.companyCity}}</view>
					<view class="lboxItem_label">{{CardInfo.industrySecondName}}</view>
				</view>
			</view>
			<view class="lbox_Img">
				<image :src="CardInfo.cardType==1?CardInfo.cardAvatar: CardInfo.companyLogo" mode="aspectFill"></image>
			</view>

			<view class="boxhead">
				<view class="boxhead_Item_title">{{CardInfo.realName}}</view>
				<view class="boxhead_Item margin-top">
					<image src="/static/image/index/qiye.png" mode="widthFix"></image>
					<text>{{CardInfo.companyName}}</text>
				</view>
				<view class="boxhead_Item margin-top-sm">
					<image src="/static/image/index/add.png" mode="widthFix"></image>
					<text>{{CardInfo.cardType==1?CardInfo.address:CardInfo.companyAddress}}</text>
				</view>
				<view class="boxhead_Item margin-top-sm">
					<image src="/static/image/index/phone.png" mode="widthFix"></image>
					<text>{{CardInfo.cardPhone}}</text>
				</view>

				<view class="boxhead_wxm">
					<!-- <img :src="CardImg" alt="二维码" /> -->
					<!-- #ifdef H5 -->
					<image :src="'https://mpfx.xianmaxiong.com/sqx_fast/app/businessCard/cardQrCode/'+CardInfo.cardId">
					</image>
					<!-- #endif -->
					<!-- #ifndef H5 -->
					<image :src="'https://mpfx.xianmaxiong.com/sqx_fast/app/userCollect/shareCard/'+CardInfo.cardId">
					</image>
					<!-- #endif -->
				</view>
				<view class="boxhead_wxm_tit">微信扫一扫即可查看保存</view>
			</view>
		</view>

		<empty contont="暂无名片" v-if="!CardInfo"></empty>

		<view class="btn" @click="getShareImg()" v-if="CardInfo">保存名片码</view>


		<!-- 二维码 -->
		<!-- #ifdef H5 -->
		<view class="qrcodeBox" v-if="shareH5Url">
			<up-qrcode :size="200" @result="resultQr" :val="shareH5Url"></up-qrcode>
		</view>

		<!-- #endif -->
		<canvas canvas-id="myShareImg" id="myShareImg" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}"
			class="myShareImg"></canvas>

		<up-popup :show="showShareImg" @close="closeShareImg" mode="center" :safeAreaInsetBottom="false">
			<view class="flex">
				<image :src="canvasImgSrc" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}" mode="">
				</image>
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
	const settingWritePhotosAlbum = ref(false);

	const CardInfo = reactive({}); //我的名片信息
	const invitationCode = ref('');
	const CardImg = ref(''); //名片二维码
	//canvas生成的图片
	const canvasImgSrc = ref('');
	//是否显示海报弹窗
	const showShareImg = ref(false);
	const backgroundImage = ref('');
	//海报宽度
	const shareImgWidth = ref(uni.upx2px(686));
	//海报高度
	const shareImgHeight = ref(uni.upx2px(360));
	//推广文案
	const tuiguang1 = ref('');
	const url = ref('');
	const qrImg = ref('');
	const shareH5Url = ref('');
	onLoad(option => {
		getMyCard()
		getBackImageList()
		getUserInfo()
	});
	onShow(() => {

	});
	//获取用户信息
	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				invitationCode.value = res.data.staffInvitationCode
				getTuiguang1();
			}
		})
	};
	// 查询展示的名片
	const getMyCard = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/businessCard/showCard?userId=" + userId).then(async res => {
			if (res.code == 0 && res.data) {
				Object.assign(CardInfo, res.data)
				if (CardInfo.cardImg) {
					backgroundImage.value = CardInfo.cardImg
				}else{
					proxy.$http.getT('/app/common/type/317').then(ret => {
						if (ret.code === 0) {
							if (ret.data && ret.data.value) {
								backgroundImage.value = ret.data.value;
							}
						}
					});
				}
				// #ifdef MP-WEIXIN
				qrImg.value = await createPoster();
				console.log(qrImg.value)
				// #endif

			}
		});
	};


	//获取分享海报的图片
	const getBackImageList = () => {
		let data = {
			state: -1,
			classify: 5
		}
		proxy.$http.getT('/app/banner/selectBannerList', data).then(async res => {
			if (res.code === 0) {
				if (res.data && res.data.length != 0) {
					// backgroundImage.value = res.data[0].imageUrl;
					// let _imgInfo = await getImageInfo(backgroundImage.value);
					// shareImgHeight.value = (shareImgWidth.value * _imgInfo.height) / _imgInfo.width;
				}
			}
		});
	};

	//关闭海报弹窗
	const closeShareImg = () => {
		showShareImg.value = false
	};
	const getShareImg = async () => {
		//如果已经生成了海报，则直接显示
		if (canvasImgSrc.value) {
			// #ifndef MP-WEIXIN
			showShareImg.value = true
			// #endif
			// #ifdef MP-WEIXIN
			onSaveImg()
			// #endif
		} else { //没有则去用canvas生成
			await shareImg();
		}
	};

	//生成海报
	const shareImg = async () => {
		uni.showLoading({
			title: '生成中...'
		})
		let _imgInfo = await getImageInfo(backgroundImage.value);
		console.log(_imgInfo)
		//获取canvas的上下文对象
		const ctx = uni.createCanvasContext('myShareImg');
		ctx.setFillStyle('#f2f2f2'); //canvas背景颜色
		ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value); //canvas画布大小
		ctx.save();
		// 修改：创建圆角矩形路径（24rpx圆角）
		ctx.beginPath();
		const radius = uni.upx2px(24); // 将24rpx转换为px
		ctx.moveTo(radius, 0);
		ctx.lineTo(shareImgWidth.value - radius, 0);
		ctx.quadraticCurveTo(shareImgWidth.value, 0, shareImgWidth.value, radius);
		ctx.lineTo(shareImgWidth.value, shareImgHeight.value - radius);
		ctx.quadraticCurveTo(shareImgWidth.value, shareImgHeight.value, shareImgWidth.value - radius,
			shareImgHeight.value);
		ctx.lineTo(radius, shareImgHeight.value);
		ctx.quadraticCurveTo(0, shareImgHeight.value, 0, shareImgHeight.value - radius);
		ctx.lineTo(0, radius);
		ctx.quadraticCurveTo(0, 0, radius, 0);
		ctx.closePath();
		ctx.clip(); // 裁剪为圆角矩形

		//绘制背景图片（现在会在圆角矩形内绘制）
		ctx.drawImage(_imgInfo.path, 0, 0, shareImgWidth.value, shareImgHeight.value);

		// 绘制半透明背景框（在背景图上方）
		ctx.setFillStyle('rgba(0, 0, 0, 0.3)'); // 白色半透明，透明度0.5
		ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value);

		ctx.restore(); // 恢复状态，这样后续绘制不会受裁剪影响

		// 在底部绘制个人信息与二维码
		//绘制头像
		const _avatarInfo = ref('');
		//头像宽高
		const avatarWidthOrHeight = ref(50);
		//边距 - 修改为距离顶部20像素
		const topDistance = 20; // 距离顶部20像素
		const paddingNum = ref(15); // 左边距保持不变

		//如果有头像、
		let aveatr = ''
		if (CardInfo.cardType == 1) {
			aveatr = CardInfo.cardAvatar
		} else {
			aveatr = CardInfo.companyLogo
		}
		if (aveatr) {
			_avatarInfo.value = await getImageInfo(aveatr)
		} else { //没有头像使用本地logo绘制
			_avatarInfo.value = await getImageInfo('/static/logo.png')
		}
		ctx.save();
		ctx.beginPath();
		//计算头像的中心坐标 - 修改Y坐标为距离顶部topDistance
		const avatarCenterX = paddingNum.value + avatarWidthOrHeight.value / 2;
		const avatarCenterY = topDistance + avatarWidthOrHeight.value / 2;
		//绘制圆形头像
		ctx.arc(avatarCenterX, avatarCenterY, avatarWidthOrHeight.value / 2, 0, 2 * Math.PI);
		ctx.clip();
		//绘制头像 - 修改Y坐标为距离顶部topDistance
		ctx.drawImage(_avatarInfo.value.path,
			paddingNum.value,
			topDistance,
			avatarWidthOrHeight.value,
			avatarWidthOrHeight.value);
		ctx.restore();

		//绘制用户昵称 - 调整Y坐标与头像对齐
		ctx.setFontSize(18);
		ctx.setFillStyle('#FFFFFF');
		ctx.fillText(CardInfo.realName,
			paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight.value,
			topDistance + avatarWidthOrHeight.value / 2 - 5); // 向上微调5px

		//绘制用户 岗位- 调整Y坐标在昵称下方
		ctx.setFontSize(14);
		ctx.setFillStyle('#FFFFFF');
		ctx.fillText(CardInfo.companyPosition,
			paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight.value,
			topDistance + avatarWidthOrHeight.value / 2 + 18); // 在昵称下方18px




		/* 绘制图标和文字 - 放在头像和昵称的底部 */
		// 图标尺寸
		const iconSize = 20;
		// 图标和文字的间距
		const iconTextSpacing = 5;

		// 计算底部区域的位置（在头像下方）
		const bottomSectionY = topDistance + avatarWidthOrHeight.value + 15; // 头像底部向下25px

		// 如果需要绘制公司名称，可以继续添加
		if (CardInfo.companyName) {
			// 绘制公司名称图标（如果有的话）
			let companyIcon = await getImageInfo('https://mpfx.xianmaxiong.com/image/qiye.png')
			ctx.drawImage(companyIcon.path,
				paddingNum.value,
				bottomSectionY + iconSize + 10,
				iconSize,
				iconSize);

			ctx.setFontSize(12);
			ctx.setFillStyle('#FFFFFF');
			ctx.fillText(CardInfo.companyName,
				paddingNum.value + iconSize + iconTextSpacing,
				bottomSectionY + iconSize - 3); // 在地址下方
		}
		// 企业地址文本
		const companyAddress = CardInfo.cardType == 1 ? CardInfo.address : CardInfo.companyAddress;
		if (companyAddress) {
			let AddIcon = await getImageInfo('https://mpfx.xianmaxiong.com/image/add.png')
			// 绘制企业地址图标
			ctx.drawImage(AddIcon.path,
				paddingNum.value,
				bottomSectionY,
				iconSize,
				iconSize);

			// 绘制企业地址文字
			ctx.setFontSize(12);
			ctx.setFillStyle('#FFFFFF');
			ctx.fillText(companyAddress,
				paddingNum.value + iconSize + iconTextSpacing,
				bottomSectionY + iconSize * 2 + 7); // 文字与图标居中对齐
		}

		// 绘制联系方式
		if (CardInfo.cardPhone) {
			// 手机号中间四位加密
			const phone = CardInfo.cardPhone;
			let encryptedPhone = phone;

			// 判断是否是手机号格式（11位数字）
			if (/^\d{11}$/.test(phone)) {
				encryptedPhone = phone.substring(0, 3) + '****' + phone.substring(7);
			} else if (phone.length >= 8) {
				// 如果不是标准手机号，根据长度处理
				const startLen = Math.floor(phone.length / 3);
				const endLen = Math.floor(phone.length / 3);
				encryptedPhone = phone.substring(0, startLen) + '****' + phone.substring(phone.length - endLen);
			}

			// 绘制手机号图标
			let phoneIcon = await getImageInfo('https://mpfx.xianmaxiong.com/image/phone.png')
			ctx.drawImage(phoneIcon.path,
				paddingNum.value,
				bottomSectionY + iconSize + 45,
				iconSize,
				iconSize);

			ctx.setFontSize(12);
			ctx.setFillStyle('#FFFFFF');
			ctx.fillText(encryptedPhone,
				paddingNum.value + iconSize + iconTextSpacing,
				bottomSectionY + iconSize * 2 + 38); // 在地址下方
		}

		// 绘制二维码 - 修改为距离底部20像素
		const qrCodeSize = avatarWidthOrHeight.value + 40; // 二维码尺寸（90px）
		const qrCodeBottomDistance = 20; // 二维码距离底部20像素

		ctx.drawImage(qrImg.value,
			shareImgWidth.value - paddingNum.value - qrCodeSize, // X坐标：右边距
			shareImgHeight.value - qrCodeBottomDistance - qrCodeSize, // Y坐标：距离底部20像素
			qrCodeSize,
			qrCodeSize);

		ctx.draw(false, () => {
			console.log('绘制完成')
			uni.hideLoading()
			getCanvasNewImage();
		});
	};
	//把canvas画布转为图片
	const getCanvasNewImage = () => {
		uni.canvasToTempFilePath({
			canvasId: 'myShareImg',
			quality: 1,
			complete: res => {
				console.log(res.tempFilePath)
				canvasImgSrc.value = res.tempFilePath;

				// uploadImg(res.tempFilePath)

				// #ifndef MP-WEIXIN
				showShareImg.value = true
				// #endif
				// #ifdef MP-WEIXIN
				onSaveImg()
				// #endif
			}
		});
	};
	/* 上传图片 */
	const uploadImg = (e) => {
		uni.showLoading({
			title: '上传中...'
		});
		uni.uploadFile({
			url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
			// url: config.APIHOST + '/alioss/upload', //仅为示例，非真实的接口地址
			filePath: e,
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			success: uploadFileRes => {
				uni.hideLoading()
				let url = JSON.parse(uploadFileRes.data).data;
				canvasImgSrc.value = url;
				showShareImg.value = true;
				console.log(url, '图片图片')
			}
		});
	};
	//异步获取图片信息
	const getImageInfo = async (imgSrc) => {
		return new Promise((resolve, errs) => {
			uni.getImageInfo({
				src: imgSrc,
				success: function(image) {
					resolve(image);
				},
				fail(err) {
					errs(err);
					uni.showToast({
						title: '海报生成失败'
					})
					uni.hideLoading()
				}
			});
		});
	};
	//异步下载图片信息
	const createPoster = async () => {
		console.log('1111111')
		return new Promise((resolve, errs) => {
			uni.downloadFile({
				url: 'https://mpfx.xianmaxiong.com/sqx_fast/app/userCollect/shareCard/' + CardInfo
					.cardId,
				success: (res2) => {
					console.log(res2, res2.tempFilePath)
					resolve(res2.tempFilePath);
				},
				fail: err => {
					console.log(err)
					uni.hideLoading();
					uni.showToast({
						title: '海报制作失败，图片下载失败',
						icon: 'none'
					});
				},
				complete: com => {
					console.log(com, '=========')
				},
			});

		});
	};

	//生成二维码回掉的图片（base64）
	const resultQr = (e) => {
		qrImg.value = e;
	};
	//文案推广
	const getTuiguang1 = () => {
		proxy.$http.getT('/app/common/type/276').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					tuiguang1.value = res.data.value;
				}
			}
		});

		proxy.$http.getT('/app/common/type/25').then(ress => {
			if (ress.code === 0) {
				if (ress.data && ress.data.value) {
					url.value = ress.data.value;
					shareH5Url.value = url.value
				} else {
					url.value = proxy.$publicYuMing();
					shareH5Url.value = url.value
				}
			}
		});

	};
	// 微信小程序保存图片
	const onSaveImg = () => {

		uni.showLoading({
			title: '海报下载中'
		});
		if (settingWritePhotosAlbum.value) {
			uni.getSetting({
				success: res => {
					if (res.authSetting['scope.writePhotosAlbum']) {
						uni.saveImageToPhotosAlbum({
							filePath: canvasImgSrc.value,
							success: () => {
								uni.hideLoading();
								uni.showToast({
									title: '保存成功'
								});
							}
						});
					} else {
						uni.showModal({
							title: '提示',
							content: '请先在设置页面打开“保存相册”使用权限',
							confirmText: '去设置',
							cancelText: '算了',
							success: data => {
								if (data.confirm) {
									uni.hideLoading();
									uni.openSetting();
								}
							}
						});
					}
				}
			});
		} else {
			uni.hideLoading();
			settingWritePhotosAlbum.value = true;
			uni.authorize({
				scope: 'scope.writePhotosAlbum',
				success: () => {
					uni.saveImageToPhotosAlbum({
						filePath: canvasImgSrc.value,
						success: () => {
							uni.hideLoading();
							uni.showToast({
								title: '保存成功'
							});
						}
					});
				}
			});
		}

	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F5F5FB;
	}

	.boxCont {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		position: relative;

		.lbox {
			height: 322rpx;
			background: linear-gradient(53deg, #1F75FF 0%, #55A2FF 100%);
			border-radius: 24rpx;

			.lboxItem {
				display: flex;
				align-items: center;
				justify-content: space-between;


				.lboxItem_label {
					padding: 0 20rpx;
					// width: 140rpx;
					height: 48rpx;
					background: rgba(255, 255, 255, 0.2);
					border-radius: 24rpx;
					border: 1px solid rgba(255, 255, 255, 0.2);
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #FFFFFF;
					display: flex;
					align-items: center;
					justify-content: center;
				}
			}

		}

		.lbox_Img {
			width: 154rpx;
			height: 154rpx;
			background: #FFFFFF;
			border-radius: 55%;
			position: absolute;
			top: 210rpx;
			left: 40%;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				width: 146rpx;
				height: 146rpx;
				border-radius: 55%;
			}
		}

		.boxhead {
			padding-top: 60rpx;

			.boxhead_Item_title {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 44rpx;
				color: #333333;
				text-align: center;
			}

			.boxhead_Item {
				display: flex;
				align-items: center;
				padding: 0 40rpx 0 90rpx;

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

			.boxhead_wxm {
				width: 220rpx;
				height: 220rpx;
				margin: 40rpx auto 0;

				image {
					width: 100%;
					height: 100%;
					// border-radius: 55%;
				}
			}

			.boxhead_wxm_tit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #999999;
				text-align: center;
				padding: 30rpx 0;
			}

		}
	}

	.btn {
		margin: 60rpx 30rpx;
		height: 88rpx;
		background: linear-gradient(-17deg, #287AFE 0%, #62AEFF 100%);
		border-radius: 41rpx;
		font-family: Source Han Sans CN;
		font-weight: 400;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.qrcodeBox {
		position: fixed;
		left: -1000px;
	}

	.myShareImg {
		position: fixed;
		left: -1000px;
	}
</style>