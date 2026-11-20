<template>
	<view>
		<view class="bgColor">
			<view class="head">
				<view class="searchbox" @click="goSearch()">
					<u-icon name="search" color="#999999" size="28"></u-icon>
					人脉搜索
				</view>
				<view class="margin-left flex align-center justify-center" style="position: relative;"
					@click="goNav('/my/message/index')">
					<image src="/static/image/index/msg.png" style="width: 50rpx;height: 50rpx;"></image>
					<view class="bade" v-if="messageCount">
						<u-badge type="error" max="99" :value="messageCount"></u-badge>
					</view>
				</view>
			</view>

			<view class="boxCard">
				<view class="box" v-if="CardInfo.realName" :style="{'background-image':'url('+CardInfo.cardImg+')'}">
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
							<image :src="CardInfo.cardType==1?CardInfo.cardAvatar: CardInfo.companyLogo"
								mode="aspectFill" style="border-radius: 55%;"></image>
						</view>
						<view class="boxhead_Item margin-top-sm">
							<image src="/static/image/index/add.png" mode="widthFix"></image>
							<text>{{CardInfo.cardType==1?CardInfo.address:CardInfo.companyAddress}}</text>
						</view>
						<view class="boxhead_Item margin-top-sm">
							<image src="/static/image/index/phone.png" mode="widthFix"></image>
							<text>{{CardInfo.cardPhone}}</text>
						</view>
					</view>
				</view>

				<view class="boxCard_Set" v-if="CardInfo.realName">
					<view class="boxCard_SetItem" @click="EditCard()">
						<image src="/static/image/index/bianji.png"></image>
						<text>编辑名片</text>
					</view>
					<view class="boxCard_SetLine"></view>
					<view class="boxCard_SetItem" @click="goNav('/my/Enterprise/style')">
						<image src="/static/image/index/bianji.png"></image>
						<text>更换样式</text>
					</view>
					<view class="boxCard_SetLine"></view>
					<view class="boxCard_SetItem" @click="goNav('/my/Enterprise/Switch')">
						<image src="/static/image/index/gneghuan.png"></image>
						<text>切换名片</text>
					</view>
				</view>

				<view class="padding" v-if="CardInfo.realName">
					<!-- #ifndef MP-WEIXIN -->
					<view class="boxCard_btn" @click="shareCard()">
						<image src="/static/image/index/share.png"></image>
						<text>分享名片</text>
					</view>
					<!-- #endif -->
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="share" v-if="canvasImgSrc">
						<view class="boxCard_btn">
							<image src="/static/image/index/share.png"></image>
							<text>分享名片</text>
						</view>
					</button>
					<!-- #endif -->
				</view>
				<view class="padding" v-else>
					<view class="boxCard_btn" @click="goNav('/my/Enterprise/Switch')">
						<text>添加名片</text>
					</view>
				</view>
			</view>
		</view>

		<view class="boxType">
			<view class="boxTypeItem" @click="goNav('/my/setting/szmp')">
				<image src="/static/image/index/nummp.png"></image>
				<view class="boxTypeItem_tit">数字名片</view>
			</view>
			<view class="boxTypeItem" @click="clickIdentification()">
				<image src="/static/image/index/shib.png" mode="widthFix"></image>
				<view class="boxTypeItem_tit">精准识别</view>
			</view>
			<view class="boxTypeItem" @click="goNav('/my/phb/index')" v-if="XCXIsSelect!='否'">
				<image src="/static/image/index/bangdan.png" mode="widthFix"></image>
				<view class="boxTypeItem_tit">名片榜单</view>
			</view>
			<view class="boxTypeItem" @click="goNav('/my/vip/index')" v-if="XCXIsSelect!='否'">
				<image src="/static/image/index/vipfw.png" mode="widthFix"></image>
				<view class="boxTypeItem_tit">VIP服务</view>
			</view>
			<view class="boxTypeItem" @click="clickCardCode()" v-if="XCXIsSelect!='否'">
				<image src="/static/image/index/mpm.png" mode="widthFix"></image>
				<view class="boxTypeItem_tit">名片码</view>
			</view>

		</view>

		<view class="Cardbox" v-if="XCXIsSelect!='否'">
			<view class="Cardbox_head">
				<view class="Cardbox_headTitle">我的名片数据</view>
				<view class="Cardbox_headTit" @click="goNav('/my/Enterprise/cardData')">更多
					<image src="/static/image/my/huiright.png"></image>
				</view>
			</view>


			<view class="CardboxNum" :style="recentlyList.length!=0?'border-bottom: 1px solid #F5F5F5;':''">
				<view class="CardboxNum_Item">
					<view class="CardboxNum_ItemNum">
						{{totalVisitCount?totalVisitCount:0}}<text>次</text>
					</view>
					<view class="CardboxNum_ItemTit">累计访问量</view>
				</view>
				<view class="CardboxNum_Item">
					<view class="CardboxNum_ItemNum">
						{{todayVisitCoune?todayVisitCoune:0}}<text>次</text>
					</view>
					<view class="CardboxNum_ItemTit">今日访问量</view>
				</view>
				<view class="CardboxNum_Item">
					<view class="CardboxNum_ItemNum">
						{{totalShareCount?totalShareCount:0}}<text>次</text>
					</view>
					<view class="CardboxNum_ItemTit">分享名片</view>
				</view>
			</view>
			<view class="CardboxTit" v-if="recentlyList.length!=0">最近访客</view>

			<view class="CardboxItem" v-for="(item,index) in recentlyList" :key="index" @click="goCardDetail(item)">
				<image :src="item.avatar?item.avatar:'/static/logo.png'"></image>
				<view class="CardboxItem_cot">
					<view class="CardboxItem_cotent">
						<view class="CardboxItem_cotent_Title">{{item.userName}}</view>
						<!-- <view class="CardboxItem_cotent_Title">来源：我分享到微信好友</view> -->
					</view>
					<view class="CardboxItem_cotdata">{{item.visitTime}} </view>
				</view>
			</view>
		</view>

		<canvas canvas-id="myShareImg" id="myShareImg" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}"
			class="myShareImg"></canvas>
		<!-- 自定义导航栏 -->
		<myTabbar :value="0" />
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
		onHide,
		onPullDownRefresh,
		onReachBottom,
		onShareAppMessage,
		onShareTimeline,
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	// 引入底部 Tabbar 组件
	import myTabbar from '@/components/myTabbar/myTabbar.vue';
	const keyword = ref('');
	const CardInfo = reactive({}); //我的名片信息
	const invitationCode = ref('');
	//推广文案
	const tuiguang1 = ref('');
	const url = ref('');
	const shareH5Url = ref('');
	const tuiguang = ref('');
	const backgroundImage = ref('');

	const canvasImgSrc = ref('');
	//海报宽度
	const shareImgWidth = ref(uni.upx2px(686));
	//海报高度
	const shareImgHeight = ref(uni.upx2px(360));

	const Visitlist = reactive([]); //访客列表
	const recentlyList = reactive([]); //最近访客数据

	const todayVisitCoune = ref(''); //今日访问次数
	const totalShareCount = ref(''); //分享次数
	const totalVisitCount = ref(''); //总访问次数
	const time = ref(null);
	const messageCount = ref('')

	const userName = ref('');
	const avatar = ref('');
	const XCXIsSelect = ref('否'); //小程序上架开关

	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框




	onShareAppMessage((res) => {
		return {
			title: tuiguang.value,
			path: '/pages/index/index?inviterCode=' + invitationCode.value + '&cardId=' + CardInfo.cardId,
			imageUrl: canvasImgSrc.value,
		}
	});
	onShareTimeline(() => {
		return {
			title: tuiguang.value,
			path: '/pages/index/index?inviterCode=' + invitationCode.value + '&cardId=' + CardInfo.cardId,
			imageUrl: canvasImgSrc.value,
		}
	});
	onLoad(option => {

		uni.hideTabBar();
		// #ifdef MP-WEIXIN
		if (option.scene) {
			const scene = decodeURIComponent(option.scene);
			uni.setStorageSync('inviterCode', scene.split(',')[0]);
		}
		// #endif
		console.log(option.inviterCode, 'option.inviterCodeoption.inviterCode')
		// 获取邀请码保存到本地
		if (option.inviterCode) {
			uni.setStorageSync('inviterCode', option.inviterCode);
		}
		console.log(option.cardId, '=========')
		if (option.cardId && option.cardId != 'undefined') {
			uni.setStorageSync('inviterCardId', option.cardId);
			uni.navigateTo({
				url: '/my/Enterprise/businessCard?id=' + option.cardId + '&isYao=1'
			})
		}
		
		proxy.$http.getT('/app/common/type/240').then(res => { //微信消息通知模版	240
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		})
		
		proxy.$http.getT('/app/common/type/292').then(res => { //审核状态通知	292	
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		})
	});
	onHide(() => {
		clearInterval(time.value)
	});
	onShow(() => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
		invitationCode.value = uni.getStorageSync('invitationCode')
		if (uni.getStorageSync('userId')) {
			getUserInfo() //获取用户信息
			getMyCard() // 查询展示的名片
			getMyVisit() // 获取最近访客
			getCardData() // 获取名片数据
			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		}
		getTuiguang1()

		time.value = setInterval(function() {
			messageCount.value = uni.getStorageSync('messageCount') //系统消息未读
		}, 5000)

	});
	/* 跳转名片详情 */
	const goCardDetail = (item) => {
		if (uni.getStorageSync('token')) {
			let messageStatus = 0
			if (item.messageStatus) {
				messageStatus = item.messageStatus
			}

			if (item.inviteeCardId) {
				uni.navigateTo({
					url: '/my/Enterprise/businessCard?id=' + item.inviteeCardId + '&messageStatus=' +
						messageStatus
				})
			} else {
				uni.showToast({
					title: '对方暂无名片',
					icon: 'none'
				})
			}

		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 查看名片码 */
	const clickCardCode = () => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			if (CardInfo.realName) {
				uni.navigateTo({
					url: '/my/Enterprise/cardCode'
				})
			} else {
				uni.showToast({
					title: '请先去添加名片',
					icon: 'none'
				})
			}
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 拍照识别名片 */
	const clickIdentification = () => {
		if (uni.getStorageSync('token')) {
			if (userName.value && avatar.value) {
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album', 'camera'], //从相册选择
					success: function(res) {
						uni.showLoading({
							title: '上传识别中...'
						});
						let token = uni.getStorageSync('token');
						let userId = uni.getStorageSync('userId');
						uni.uploadFile({
							url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
							filePath: res.tempFilePaths[0],
							header: {
								token: uni.getStorageSync('token')
							},
							name: 'file',
							success: uploadFileRes => {
								let cardUrl = JSON.parse(uploadFileRes.data).data;
								console.log(cardUrl)
								proxy.$http.get("/app/ai/extractCardInfo", {
									image: cardUrl
								}).then(res => {
									if (res.code == 0) {
										console.log(res)
										let infoData = res.data
										uni.navigateTo({
											url: '/pages/my/userInfo?infoData=' +
												JSON.stringify(infoData)
										})

									}
									uni.hideLoading();
								});
							}
						});
					}
				});
			} else {
				uni.showModal({
					title: '提示',
					content: '请先去完善个人信息',
					showCancel: false,
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/pages/my/info'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
		} else {
			goLogin();
		}
	};
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				avatar.value = res.data.avatar

			}
		});
	};
	// 跳转搜索
	const goSearch = () => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.navigateTo({
				url: '/my/search/index'
			})
		} else {
			goLogin();
		}
	};
	// 编辑名片
	const EditCard = () => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			if (CardInfo.cardType == 1) {
				uni.navigateTo({
					url: '/pages/my/userInfo?cardId=' + CardInfo.cardId
				})
			} else {
				uni.navigateTo({
					url: '/my/Enterprise/PublishOne?cardId=' + CardInfo.cardId
				})
			}
		} else {
			goLogin();
		}
	};
	// 跳转
	const goNav = (url) => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.navigateTo({
				url
			})
		} else {
			goLogin();
		}
	};
	// 跳转登录
	const goLogin = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 获取名片数据
	const getCardData = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/visit/userData", {
			userId: userId,
		}).then(res => {
			if (res.code == 0) {
				recentlyList.splice(0, recentlyList.length, ...res.data.recentlyList); //最近访客数据

				todayVisitCoune.value = res.data.todayVisitCoune //今日访问次数
				totalShareCount.value = res.data.totalShareCount //分享次数
				totalVisitCount.value = res.data.totalVisitCount //总访问次数



			}
		});
	};
	// 获取最近访客
	const getMyVisit = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.postJson("/app/visit/visitMe", {
			userId: userId,
			pageNum: 1,
			pageSize: 1
		}).then(res => {
			if (res.code == 0) {
				Visitlist.splice(0, Visitlist.length, ...res.data.list);
			}
		});
	};
	// 查询展示的名片
	const getMyCard = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.get("/app/businessCard/showCard?userId=" + userId).then(res => {
			if (res.code == 0 && res.data) {
				Object.assign(CardInfo, res.data)
				if (CardInfo.cardImg) {
					backgroundImage.value = CardInfo.cardImg
					// #ifdef MP-WEIXIN
					getShareImg()
					// #endif
				} else {
					getBackImageList()
				}

			}
		});
	};
	const getShareImg = async () => {
		//如果已经生成了海报，则直接显示 没有则去用canvas生成
		if (!canvasImgSrc.value) {
			await shareImg();
		}
	};

	//获取分享海报的图片
	const getBackImageList = () => {
		proxy.$http.getT('/app/common/type/317').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					backgroundImage.value = res.data.value;
					// #ifdef MP-WEIXIN
					getShareImg()
					// #endif
				}
			}
		});
	};
	const getTuiguang1 = () => {

		proxy.$http.getT('/app/common/type/276').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					tuiguang1.value = res.data.value;
				}
			}
		});
		// #ifndef H5
		proxy.$http.getT('/app/common/type/25').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					url.value = res.data.value;
				}
			}
		});
		//#endif
		// #ifdef H5
		proxy.$http.getT('/app/common/type/19').then(ress => {
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
		//#endif
	};
	// 分享名片
	const shareCard = () => {
		let relationId = invitationCode.value;
		uni.showModal({
			title: '文案推广',
			content: tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' + relationId +
				'&cardId=' + CardInfo.cardId,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#287AFE',
			success: res => {
				if (res.confirm) {
					uni.setClipboardData({
						data: tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' +
							relationId + '&cardId=' + CardInfo.cardId,
						success: function() {
							console.log('success');
							uni.showToast({
								title: '复制成功',
								icon: 'none'
							})
						}
					});
				}
			}
		});
	};
	//生成海报
	const shareImg = async () => {

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
		// 第321行附近
		let aveatr = ''
		if (CardInfo.cardType == 1) {
			aveatr = CardInfo.cardAvatar
		} else {
			aveatr = CardInfo.companyLogo
		}
		if (aveatr) {
			_avatarInfo.value = await getImageInfo(aveatr)
		} else {
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

			if (companyAddress.length > 28) {
				ctx.setFontSize(12);
				ctx.setFillStyle('#FFFFFF');

				// 计算文本可显示的最大宽度（从图标右侧到右边距）
				const maxWidth = shareImgWidth.value - (paddingNum.value + iconSize + iconTextSpacing) - paddingNum
					.value;

				// 获取文本宽度（辅助函数）
				const getTextWidth = (text) => {
					// 注意：ctx.measureText 在部分平台可能不支持，这里做了兼容处理
					if (ctx.measureText) {
						return ctx.measureText(text).width;
					} else {
						// 降级方案：粗略估算，每个字符按7px计算（可根据实际调整）
						return text.length * 7;
					}
				};

				// 智能换行函数：按字符分割，每行不超过10个字符，同时考虑实际宽度
				const splitAddressLines = (text, maxCharsPerLine = 28) => {
					const lines = [];
					let currentLine = '';

					for (let i = 0; i < text.length; i++) {
						const char = text[i];
						const testLine = currentLine + char;

						// 优先按字符数判断（超过10字强制换行）
						if (testLine.length > maxCharsPerLine) {
							lines.push(currentLine);
							currentLine = char;
						} else {
							// 同时检测实际宽度是否超限（但以字符数为主要依据）
							const testWidth = getTextWidth(testLine);
							if (testWidth > maxWidth && currentLine.length > 0) {
								lines.push(currentLine);
								currentLine = char;
							} else {
								currentLine = testLine;
							}
						}
					}

					if (currentLine.length > 0) {
						lines.push(currentLine);
					}

					return lines;
				};

				// 获取地址行数组（每行不超过10字）
				const addressLines = splitAddressLines(companyAddress, 28);

				// 计算第一行的Y坐标（与图标垂直居中对齐）
				const firstLineY = bottomSectionY + iconSize / 2 + 30; // 6为微调值

				// 行高设置（可根据需要调整）
				const lineHeight = 14;

				// 逐行绘制地址
				addressLines.forEach((line, index) => {
					ctx.fillText(
						line,
						paddingNum.value + iconSize + iconTextSpacing,
						firstLineY + index * lineHeight
					);
				});

				// 计算地址块占用的总高度（用于后续元素定位）
				const addressBlockHeight = addressLines.length * lineHeight;
				// 计算地址块底部位置（图标顶部 + 地址块总高度 + 一些间距）
				const addressBlockBottomY = bottomSectionY + addressBlockHeight + 10; // 加10px间距
			} else {
				// 绘制企业地址文字
				ctx.setFontSize(12);
				ctx.setFillStyle('#FFFFFF');
				ctx.fillText(companyAddress,
					paddingNum.value + iconSize + iconTextSpacing,
					bottomSectionY + iconSize * 2 + 7); // 文字与图标居中对齐
			}
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
		ctx.draw(false, () => {
			console.log('绘制完成')
			uni.hideLoading()
			getCanvasNewImage();
		});
	};
	//异步获取图片信息
	// 异步获取图片信息
	const getImageInfo = async (imgSrc) => {
		return new Promise((resolve, errs) => {
			// 检查图片源是否有效
			if (!imgSrc) {
				console.error('图片源为空');
				// 返回一个默认图片
				uni.getImageInfo({
					src: '/static/logo.png',
					success: function(image) {
						resolve(image);
					},
					fail: function(err) {
						console.error('默认图片也加载失败:', err);
						errs(err);
					}
				});
				return;
			}

			uni.getImageInfo({
				src: imgSrc,
				success: function(image) {
					resolve(image);
				},
				fail(err) {
					console.error('图片加载失败:', imgSrc, err);
					// 尝试使用本地默认图片
					uni.getImageInfo({
						src: '/static/logo.png',
						success: function(image) {
							resolve(image);
						},
						fail: function(defaultErr) {
							errs(defaultErr);
						}
					});
				}
			});
		});
	};
	//把canvas画布转为图片
	const getCanvasNewImage = () => {
		uni.canvasToTempFilePath({
			canvasId: 'myShareImg',
			quality: 1,
			complete: res => {
				console.log(res.tempFilePath)
				// canvasImgSrc.value = res.tempFilePath;
				uploadImg(res.tempFilePath)
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
				console.log(url, '图片图片')
			}
		});
	};
	// 开启订阅消息
	const openMsg = () => {
		uni.getSetting({
			withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
			success(ret) {
				console.log(ret, '=======')
				if (ret.subscriptionsSetting.itemSettings) {
					uni.setStorageSync('sendMsg', true)
					uni.openSetting({ // 打开设置页 
						success(rea) {
							console.log(rea.authSetting)
						}
					});
				} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.setStorageSync('sendMsg', false)
					uni.showModal({
						title: '提示',
						content: '为了更好的体验,请绑定消息推送',
						confirmText: '确定',
						cancelText: '取消',
						success: function(res) {
							if (res.confirm) {
								wx.requestSubscribeMessage({
									tmplIds: arr,
									success(re) {
										var datas = JSON.stringify(re);
										if (datas.indexOf("accept") != -1) {
											console.log(re)
										}
									},
									fail: (res) => {
										console.log(res)
									}
								})
								showModalMsg.value = false
							} else if (res.cancel) {
								showModalMsg.value = true
							}
						}
					})
				}
			}
		})
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	button {
		padding: 0;
		border: none !important;
		background-color: #FFFFFF !important;
	}

	button::after {
		border: none !important;
	}

	page {
		background: #F5F5FB;
	}

	.bgColor {
		background: linear-gradient(to bottom, #287AFE 10%, #F5F5FB 70%);
		padding: 30rpx 0 0;
	}

	.head {
		padding: 0 30rpx;
		display: flex;
		align-items: center;
	}

	.searchbox {
		flex: 1;
		height: 66rpx;
		background: #F0F5FF;
		border-radius: 33rpx;

		font-weight: 500;
		font-size: 26rpx;
		color: #999999;
		display: flex;
		align-items: center;
		padding: 0 30rpx;
	}

	.bade {
		position: absolute;
		top: -10rpx;
		right: -10rpx;
	}

	.boxCard {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 30rpx 30rpx 20rpx;


		.box {
			background-size: 100% 100%;
			background-repeat: no-repeat;
			// background: linear-gradient(to bottom, #F0F5FF, #FFFFFF);
			box-shadow: 0rpx 8rpx 16rpx 0rpx rgba(91, 109, 137, 0.11);
			// padding: 30rpx;
			border-radius: 24rpx;

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
								width: 40rpx;
								// height: 44rpx;
								padding: 4rpx 0;
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
								padding: 4rpx 20rpx 4rpx 35rpx;
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
	}

	.boxCard_Set {
		height: 101rpx;
		background: #FFFFFF;
		box-shadow: 0rpx 5rpx 9rpx 0rpx rgba(106, 135, 182, 0.21);
		border-radius: 10rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 30rpx;
		margin-top: 8rpx;

		.boxCard_SetLine {
			width: 1rpx;
			height: 26rpx;
			background: #B0B0B0;
		}

		.boxCard_SetItem {
			display: flex;
			align-items: center;

			image {
				width: 31rpx;
				height: 35rpx;
			}

			text {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #999999;
				margin-left: 10rpx;
			}
		}

	}

	.boxCard_btn {
		width: 626rpx;
		height: 80rpx;
		background: linear-gradient(-51deg, #287AFE 0%, #4A8FFF 100%);
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 36rpx;
			height: 40rpx;
		}

		text {
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 28rpx;
			color: #FFFFFF;
			margin-left: 10rpx;
		}
	}

	.boxType {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 0rpx 30rpx;
		padding: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.boxTypeItem {
			text-align: center;

			.boxTypeItem_tit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #333333;
			}

			image {
				width: 56rpx;
				height: 57rpx;
			}
		}
	}

	.Cardbox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.Cardbox_head {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.Cardbox_headTitle {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.Cardbox_headTit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #B0B0B0;
				display: flex;
				align-items: center;

				image {
					width: 25rpx;
					height: 25rpx;
				}
			}
		}

		.CardboxNum {
			display: flex;
			align-items: center;
			justify-content: space-between;

			padding: 30rpx 0 20rpx;

			.CardboxNum_Item {
				text-align: center;

				.CardboxNum_ItemNum {
					font-family: Source Han Sans CN;
					font-weight: bold;
					font-size: 32rpx;
					color: #333333;

					text {
						font-size: 24rpx;
						font-size: 400;
					}
				}

				.CardboxNum_ItemTit {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}

		.CardboxTit {
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #999999;
			margin-top: 20rpx;
		}

		.CardboxItem {
			display: flex;
			align-items: center;
			border-bottom: 1px solid #F5F5F5;
			padding: 30rpx 0;

			image {
				width: 88rpx;
				height: 88rpx;
				border-radius: 55%;
			}

			.CardboxItem_cot {
				flex: 1;
				display: flex;
				justify-content: space-between;
				margin-left: 20rpx;

				.CardboxItem_cotent {

					.CardboxItem_cotent_Title {
						font-family: Source Han Sans CN;
						font-weight: 500;
						font-size: 30rpx;
						color: #333333;
					}

					.CardboxItem_cotent_Title {
						font-family: Source Han Sans CN;
						font-weight: 400;
						font-size: 24rpx;
						color: #999999;
					}
				}

				.CardboxItem_cotdata {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}

	.myShareImg {
		position: fixed;
		left: -1000px;
	}
</style>