<template>
	<view class="page-wrap">
		<view v-if="isCompanyCard" class="company-hero">
			<image class="company-hero__cover" :src="companyCover" mode="aspectFill"></image>
			<view class="company-hero__mask"></view>
		</view>

		<view v-if="isCompanyCard" class="company-summary">
			<view class="company-summary__head">
				<view class="company-summary__logo-wrap">
					<view class="company-summary__logo">
						<image class="company-summary__logo-img" :src="companyLogoUrl" mode="aspectFit"></image>
					</view>
				</view>
				<view class="company-summary__main">
					<view class="company-summary__name">{{ CardInfo.companyName || '企业名片' }}</view>
					<view class="hecha-badge" :class="{ checked: CardInfo.verifyFlag == 1 }" @click="clickHc()">
						<image src="/my/static/hc.png" mode="widthFix" class="hecha-badge__icon"></image>
						<text>{{ CardInfo.verifyFlag == 1 ? '已核查' : 'AI核查' }}</text>
						<image src="/my/static/vipIcon.png" mode="widthFix" class="hecha-badge__vip"></image>
					</view>
				</view>
			</view>
			<view class="company-summary__tags tag-row">
				<view class="pill soft cert" v-if="CardInfo.verifyFlag == 1">已认证</view>
				<view class="pill soft warn" v-else>未认证</view>
				<view class="pill soft" v-if="industryText">{{ industryText }}</view>
				<view class="pill soft count" v-if="CardInfo.companyNum">{{ CardInfo.companyNum }}人单位</view>
			</view>
			<view class="company-summary__strip company-summary__strip--tags" v-if="companyTopTags.length">
				<view class="company-summary__strip-item company-summary__strip-item--tag"
					v-for="(item, index) in companyTopTags" :key="'tag-' + index">
					<view class="company-summary__strip-label">{{ item.label }}</view>
					<view class="company-summary__strip-value">{{ item.value }}</view>
				</view>
			</view>
			<view class="company-summary__strip company-summary__strip--stats" v-if="companySummaryStrip.length">
				<view class="company-summary__strip-item" v-for="(item, index) in companySummaryStrip" :key="index">
					<view class="company-summary__strip-label">{{ item.label }}</view>
					<view class="company-summary__strip-value">{{ item.value }}</view>
				</view>
			</view>
		</view>

		<!-- <view v-if="isPersonalCard && CardInfo.verifyFlag != 1" class="notice-card">
			<up-icon name="warning-fill" color="#F59E0B" size="18"></up-icon>
			<text>该企业暂未通过宽脉官方认证，请注意甄别</text>
		</view> -->

		<view v-if="isPersonalCard" class="card-panel personal-card"
			:class="{ 'personal-card--plain': !personalCardBackground }" :style="personalCardStyle">
			<view class="personal-card__mask"></view>
			<view class="personal-card__body">
				<view class="personal-card__top">
					<view class="personal-card__brand">{{ CardInfo.companyName || '个人数字名片' }}</view>
				</view>
				<view class="personal-card__main">
					<view class="personal-card__info">
						<view class="personal-card__name">{{ displayName }}</view>
						<view class="personal-card__job">{{ CardInfo.companyPosition || '职务待完善' }}</view>
						<view class="personal-card__line" v-if="CardInfo.cardPhone">
							<up-icon name="phone-fill" color="#FFFFFF" size="15"></up-icon>
							<text>{{ maskPhoneNumber(CardInfo.cardPhone) }}</text>
						</view>
						<view class="personal-card__line" v-if="CardInfo.email">
							<up-icon name="email-fill" color="#FFFFFF" size="15"></up-icon>
							<text>{{ CardInfo.email }}</text>
						</view>
						<view class="personal-card__line" v-if="currentAddress">
							<up-icon name="map-fill" color="#FFFFFF" size="15"></up-icon>
							<text>{{ currentAddress }}</text>
						</view>
					</view>
					<image class="personal-card__avatar" :src="avatarUrl" mode="aspectFill"></image>
				</view>
				<view class="hecha-badge personal-card__hecha" :class="{ checked: CardInfo.verifyFlag == 1 }"
					@click="clickHc()">
					<image src="/my/static/hc.png" mode="widthFix" class="hecha-badge__icon"></image>
					<text>{{ CardInfo.verifyFlag == 1 ? '已核查' : 'AI核查' }}</text>
					<image src="/my/static/vipIcon.png" mode="widthFix" class="hecha-badge__vip"></image>
				</view>
			</view>
		</view>

		<view v-if="showContactActions" class="card-panel action-panel">
			<view class="action-item" @click="callPhone()">
				<view class="action-item__icon">
					<up-icon name="phone-fill" color="#2F6BFF" size="22"></up-icon>
				</view>
				<view class="action-item__text">电话</view>
			</view>
			<view class="action-item" @click="copyWxCode()">
				<view class="action-item__icon">
					<up-icon name="weixin-fill" color="#2F6BFF" size="22"></up-icon>
				</view>
				<view class="action-item__text">微信</view>
			</view>
		</view>

		<view v-if="showCompanyEntry" class="card-panel company-entry" @click="goCompanyCard()">
			<image class="company-entry__logo" :src="companyLogoUrl" mode="aspectFill"></image>
			<view class="company-entry__content">
				<view class="company-entry__name-row">
					<view class="company-entry__name">{{ CardInfo.companyName }}</view>
					<!-- <view class="company-entry__badge">企业主页</view> -->
				</view>
				<view class="company-entry__desc">{{ companyMetaText || '点击进入企业主页' }}</view>
			</view>
			<up-icon name="arrow-right" color="#9CA3AF" size="16"></up-icon>
		</view>

		<view v-if="isCompanyCard && CardInfo.companyBlurb" class="section-card intro-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">企业简介</view>
					<view class="section-subtitle">品牌定位与主营方向</view>
				</view>
			</view>
			<view class="section-text preserve-line">{{ CardInfo.companyBlurb }}</view>
		</view>

		<view v-if="isCompanyCard && companyInfoList.length" class="section-card">
			<view class="section-head">
				<view>
					<view class="section-title">企业信息</view>
					<view class="section-subtitle">核心经营数据与企业画像</view>
				</view>
				<view class="section-head__badge">{{ companyInfoList.length }}项</view>
			</view>
			<view class="info-highlight" v-if="companyHighlightList.length">
				<view class="info-highlight__item" v-for="(item, index) in companyHighlightList" :key="index">
					<view class="info-highlight__label">{{ item.label }}</view>
					<view class="info-highlight__value">{{ item.value }}</view>
				</view>
			</view>
			<view class="info-list" v-if="companyDetailList.length">
				<view class="info-item" v-for="(item, index) in companyDetailList" :key="index">
					<view class="info-item__index">{{ (index + 1).toString().padStart(2, '0') }}</view>
					<view class="info-item__body">
						<view class="info-item__label">{{ item.label }}</view>
						<view class="info-item__value preserve-line">{{ item.value }}</view>
					</view>
				</view>
			</view>
		</view>

		<view v-if="isCompanyCard && productImageList.length" class="section-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">公司产品</view>
					<view class="section-subtitle">展示产品与服务形象</view>
				</view>
				<view class="section-head__badge">{{ productImageList.length }}张</view>
			</view>
			<view class="product-grid">
				<view v-for="(item, index) in productImageList" :key="index" class="product-grid__card"
					@tap="prewImg(item)">
					<image class="product-grid__item" :src="item" mode="aspectFill"></image>
					<view class="product-grid__mask"></view>
					<view class="product-grid__caption">产品 {{ (index + 1).toString().padStart(2, '0') }}</view>
				</view>
			</view>
		</view>

		<view v-if="isCompanyCard && companyAmbientList.length" class="section-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">企业环境</view>
					<view class="section-subtitle">办公空间与企业氛围</view>
				</view>
				<view class="section-head__badge">{{ companyAmbientList.length }}张</view>
			</view>
			<scroll-view scroll-x class="gallery-scroll" show-scrollbar="false">
				<view class="gallery-row">
					<view v-for="(item, index) in companyAmbientList" :key="index" class="gallery-row__card"
						@tap="prewImg(item)">
						<image class="gallery-row__item" :src="item" mode="aspectFill"></image>
						<view class="gallery-row__caption">环境 {{ (index + 1).toString().padStart(2, '0') }}</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<view v-if="isCompanyCard && companyFlairList.length" class="section-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">企业资质</view>
					<view class="section-subtitle">官方证照与能力证明</view>
				</view>
				<view class="section-head__badge">{{ companyFlairList.length }}项</view>
			</view>
			<view class="qualification-list">
				<view class="qualification-item" v-for="(item, index) in companyFlairList" :key="index">
					<view class="qualification-item__name">{{ item.name || '企业资质' }}</view>
					<view class="qualification-item__images">
						<image v-for="(img, imgIndex) in item.images" :key="imgIndex" class="qualification-item__image"
							:src="img" mode="aspectFill" @tap="prewImg(img)"></image>
					</view>
				</view>
			</view>
		</view>

		<view v-if="isPersonalCard && showPersonalIntro" class="section-card personal-intro">
			<view class="section-title">个人简介</view>
			<view class="tag-row intro-tags" v-if="introTagList.length">
				<view class="pill gray" v-for="(item, index) in introTagList" :key="index">{{ item }}</view>
			</view>
			<view v-if="CardInfo.detailsText" class="section-text preserve-line">{{ CardInfo.detailsText }}</view>
			<view v-if="CardInfo.detailsVideo" class="video-box">
				<video :src="CardInfo.detailsVideo" class="video-box__player"></video>
			</view>
			<view v-if="detailImageList.length" class="intro-image-list">
				<image v-for="(item, index) in detailImageList" :key="index" class="intro-image-list__item" :src="item"
					mode="widthFix" @tap="prewImg(item)"></image>
			</view>
		</view>

		<view class="bottom-space" v-if="showBottomAction"></view>
		<view class="bottom-bar" v-if="showBottomAction">
			<view class="bottom-bar__btn disabled"
				v-if="CardInfo.userCardListCount==0 && isYao !=1 && messageStatus==1">
				交换申请中...
			</view>
			<view class="bottom-bar__btn"
				v-if="CardInfo.userCardListCount==0 && isYao !=1 && (messageStatus==0 || messageStatus==3)"
				@click="clickExchange()">
				{{ isCompanyCard ? '发起联系' : '递名片' }}
			</view>
			<view class="bottom-bar__btn" v-if="CardInfo.cardType==1 && idGroup!=1 && messageStatus==2"
				@click="AddCard()">
				添加名片
			</view>
			<view class="bottom-bar__btn"
				v-if="(CardInfo.cardType==2 && CardInfo.companyStaffCount!=1 && messageStatus==2) || isYao ==1"
				@click="clickCardInfo()">
				同意加入
			</view>
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
		computed,
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
	const byUserId = ref(''); //名片userid
	const id = ref('');
	const CardInfo = reactive({}); //我的名片信息
	const idGroup = ref(''); //默认分组id

	const show = ref(false); //申请弹框
	const content = ref(''); //申请理由
	const isVip = ref(false); //是否开通会员

	const showVip = ref(false);
	const isTrue = ref(false)
	const messageStatus = ref(-1); //1 待审核 2已通过  3已拒绝
	const isYao = ref(2); //是否邀请  1 是 2 否
	const vipCardNum = ref(0); //交换次数
	const pageType = ref('card');
	const cardTypeValue = computed(() => Number(CardInfo.cardType));
	const isCompanyCard = computed(() => cardTypeValue.value === 2);
	const isPersonalCard = computed(() => cardTypeValue.value === 1);
	const displayName = computed(() => CardInfo.cardRemark ? CardInfo.cardRemark : (CardInfo.realName || '未设置姓名'));
	const currentAddress = computed(() => {
		if (Number(CardInfo.cardType) === 1 && CardInfo.address) {
			return CardInfo.address;
		}
		return CardInfo.companyAddress || '';
	});
	const avatarUrl = computed(() => CardInfo.cardAvatar || '/static/logo.png');
	const companyLogoUrl = computed(() => CardInfo.companyLogo || CardInfo.cardAvatar || '/static/logo.png');
	const companyAmbientList = computed(() => splitImageList(CardInfo.companyAmbient));
	const productImageList = computed(() => splitImageList(CardInfo.firstProductImg));
	const detailImageList = computed(() => splitImageList(CardInfo.detailsImage));
	const companyCover = computed(() => companyAmbientList.value[0] || CardInfo.cardImg || companyLogoUrl.value);
	const personalCardBackground = computed(() => CardInfo.cardImg || CardInfo.backgroundImg || CardInfo.bgImg || '');
	const industryText = computed(() => CardInfo.industrySecondName || CardInfo.industryFirstName || '');
	const currentLatitude = computed(() => Number(
		CardInfo.latitude || CardInfo.lat || CardInfo.companyLatitude || CardInfo.addressLatitude || 0
	));
	const currentLongitude = computed(() => Number(
		CardInfo.longitude || CardInfo.lng || CardInfo.companyLongitude || CardInfo.addressLongitude || 0
	));
	const showContactActions = computed(() => CardInfo.userCardListCount == 1 || CardInfo.companyStaffCount == 1);
	const showCompanyEntry = computed(() => isPersonalCard.value && !!CardInfo.companyName && !!(CardInfo.companyCardId ||
		CardInfo.companyLogo));
	const showPersonalIntro = computed(() => !!(CardInfo.detailsText || CardInfo.detailsVideo || detailImageList.value
		.length));
	const companyMetaText = computed(() => {
		const arr = [];
		if (CardInfo.companyNum) {
			arr.push(CardInfo.companyNum + '人');
		}
		if (industryText.value) {
			arr.push(industryText.value);
		}
		return arr.join(' · ');
	});
	const companyInfoList = computed(() => {
		const list = [];
		return list;
	});
	const companySummaryStrip = computed(() => {
		const list = [];
		if (productImageList.value.length) {
			list.push({
				label: '产品图集',
				value: productImageList.value.length + '张'
			});
		}
		if (companyAmbientList.value.length) {
			list.push({
				label: '环境实拍',
				value: companyAmbientList.value.length + '张'
			});
		}
		if (companyFlairList.value.length) {
			list.push({
				label: '资质证明',
				value: companyFlairList.value.length + '项'
			});
		}
		return list.slice(0, 3);
	});
	const companyTopTags = computed(() => {
		const list = [];
		if (CardInfo.companyAddress) {
			list.push({
				label: '地址',
				value: CardInfo.companyAddress
			});
		}
		if (CardInfo.companyArea) {
			list.push({
				label: '面积',
				value: CardInfo.companyArea + 'm²'
			});
		}
		if (CardInfo.companyOutputValue) {
			list.push({
				label: '产值',
				value: CardInfo.companyOutputValue + '万元'
			});
		}
		if (industryText.value) {
			list.push({
				label: '行业',
				value: industryText.value
			});
		}
		return list;
	});
	const companyHighlightList = computed(() => companyInfoList.value.filter(item =>
		item.label === '公司面积(m²)' || item.label === '年产值(万元)' || item.label === '公司人数'
	).slice(0, 3));
	const companyDetailList = computed(() => companyInfoList.value.filter(item =>
		!companyHighlightList.value.some(highlight => highlight.label === item.label)
	));
	const introTagList = computed(() => {
		const arr = [];
		if (CardInfo.industryFirstName) {
			arr.push(CardInfo.industryFirstName);
		}
		if (CardInfo.industrySecondName && CardInfo.industrySecondName !== CardInfo.industryFirstName) {
			arr.push(CardInfo.industrySecondName);
		}
		if (CardInfo.companyPosition) {
			arr.push(CardInfo.companyPosition);
		}
		return arr.slice(0, 3);
	});
	const personalCardStyle = computed(() => ({
		'background-image': personalCardBackground.value ? 'url(' + personalCardBackground.value + ')' : 'none'
	}));
	const companyFlairList = computed(() => {
		const rawValue = CardInfo.companyFlair || CardInfo.companyFlairs;
		if (!rawValue) {
			return [];
		}
		try {
			const list = JSON.parse(rawValue);
			if (!Array.isArray(list)) {
				return [];
			}
			return list.map(item => ({
				name: item.name,
				images: splitImageList(item.pic)
			})).filter(item => item.images.length);
		} catch (error) {
			return [];
		}
	});
	const showBottomAction = computed(() => {
		if (!isTrue.value) {
			return false;
		}
		if (CardInfo.userCardListCount == 0 && isYao.value != 1 && (messageStatus.value == 0 || messageStatus
				.value == 1 || messageStatus.value == 3)) {
			return true;
		}
		if (CardInfo.cardType == 1 && idGroup.value != 1 && messageStatus.value == 2) {
			return true;
		}
		if ((CardInfo.cardType == 2 && CardInfo.companyStaffCount != 1 && messageStatus.value == 2) || isYao
			.value == 1) {
			return true;
		}
		return false;
	});
	const splitImageList = (value) => {
		if (!value) {
			return [];
		}
		if (Array.isArray(value)) {
			return value.filter(Boolean);
		}
		return value.split(',').map(item => item.trim()).filter(Boolean);
	};
	const copyText = (value, emptyText) => {
		if (!value) {
			uni.showToast({
				title: emptyText,
				icon: 'none'
			});
			return;
		}
		uni.setClipboardData({
			data: value,
			success: function() {
				uni.showToast({
					title: '复制成功',
					icon: 'none'
				});
			}
		});
	};
	const prewImg = (img) => {
		uni.previewImage({
			urls: [img],
			current: 0
		})
	};
	const copyEmail = () => {
		copyText(CardInfo.email, '暂无邮箱');
	};

	const goCompanyCard = () => {
		const companyId = CardInfo.cardId || (isCompanyCard.value ? id.value : '');
		if (companyId) {
			uni.navigateTo({
				url: '/my/Enterprise/companyHome?id=' + companyId
			});
			return;
		}
		uni.showToast({
			title: '暂无企业主页',
			icon: 'none'
		});
	};
	const updatePageTitle = () => {
		const title = pageType.value === 'companyHome' || Number(CardInfo.cardType) === 2 ? '企业主页' : 'TA的名片'
		uni.setNavigationBarTitle({
			title
		})
	};
	/* 点击申请交换名片 */
	const clickExchange = () => {
		if (uni.getStorageSync('token')) {
			if (vipCardNum.value > 0) {


				uni.showModal({
					title: '提示',
					content: '交换次数剩余' + vipCardNum.value + '次，确认发起交换名片申请吗？',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let userId = uni.getStorageSync('userId')
							proxy.$http.postJson('/app/message/insertMessageBody', {
								platformId: id.value
							}).then(res => {
								if (res.code === 0) {
									getCardDetail()
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			} else {
				if (isVip.value) {
					uni.showToast({
						title: '当月会员次数已用完',
						icon: 'none',
						duration: 2000
					})
				} else {
					uni.showModal({
						title: '提示',
						content: '交换次数暂无，请先去开通会员获取次数',
						success: function(res) {
							if (res.confirm) {
								uni.navigateTo({
									url: '/my/vip/index'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				}

			}

		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
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
	/* 名片核查 */
	const clickHc = () => {
		if (uni.getStorageSync('token')) {
			if (isVip.value) {
				uni.navigateTo({
					url: '/my/hecha/index?companyName=' + CardInfo.companyName
				})
			} else {
				showVip.value = true

			}
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 复制微信 */
	const copyWxCode = () => {
		console.log(CardInfo.wxCode, 'CardInfo.wxCode')
		if (CardInfo.wxCode) {
			uni.setClipboardData({
				data: CardInfo.wxCode,
				success: function() {
					uni.showToast({
						title: '复制成功',
						icon: 'none'
					})
				}
			})
		} else {
			uni.showToast({
				title: '微信未设置',
				icon: 'none',
				duration: 2000
			})
		}

	};
	// 联系Ta
	const callPhone = (phone) => {
		if (uni.getStorageSync('token')) {
			let userId = uni.getStorageSync('userId')
			proxy.$http.get("/app/userCollect/incrContactCount?cardId=" + id.value).then(res => {
				if (res.code == 0) {
					uni.makePhoneCall({
						phoneNumber: CardInfo.cardPhone //仅为示例
					});
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}

	};
	//加密中间四位
	const maskPhoneNumber = (phoneNumber) => {
		// 将手机号码转换为字符串类型，并根据其长度确定要替换的位数
		if (phoneNumber) {
			phoneNumber = phoneNumber.toString() //先强制转换成字符串类型
			return phoneNumber.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2')
		}

	};
	// 获取名片详情
	const getCardDetail = () => {

		let userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''

		if (userId) {
			proxy.$http.get("/app/businessCard/info?cardId=" + id.value + '&userId=' + userId).then(res => {
				uni.hideLoading()
				if (res.code == 0) {
					Object.assign(CardInfo, res.data)
					updatePageTitle()
					if (res.data.messageStatus) {
						messageStatus.value = res.data.messageStatus
					}
					uni.setStorageSync('inviterCardType', CardInfo.cardType)
					if (uni.getStorageSync('token')) {
						getGroups()
					}

				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none',
						duration: 1000
					})
					setTimeout(function() {
						uni.navigateBack()
					}, 1000)
				}

			});
		} else {
			proxy.$http.get("/app/businessCard/info?cardId=" + id.value).then(res => {
				uni.hideLoading()
				if (res.code == 0) {
					Object.assign(CardInfo, res.data)
					updatePageTitle()
					uni.setStorageSync('inviterCardType', CardInfo.cardType)
					if (uni.getStorageSync('token')) {
						getGroups()
					}
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none',
						duration: 1000
					})
					setTimeout(function() {
						uni.navigateBack()
					}, 1000)
				}

			});
		}

	};
	// 查询是否添加到分组
	const getGroups = () => {

		let userId = uni.getStorageSync('userId');
		proxy.$http.getT("/app/userCollect/isCollectCard?userId=" + userId + '&cardId=' + CardInfo.cardId).then(
			res => {
				if (res.code == 0) {
					if (res.data) {
						idGroup.value = 1
					}
					isTrue.value = true
				}

			});
	};
	/* 添加名片 */
	const AddCard = () => {
		if (uni.getStorageSync('token')) {
			let userId = uni.getStorageSync('userId')
			proxy.$http.postJson("/app/userCollect/insert", {
				cardId: CardInfo.cardId
			}).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '添加成功',
						icon: 'none'
					})
					getGroups()
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 查询用户是否有名片 */
	const clickCardInfo = () => {
		if (uni.getStorageSync('token')) {

			let userId = uni.getStorageSync('userId')
			proxy.$http.get("/app/businessCard/userCardExists", {
				userId: userId,
			}).then(res => {
				if (res.code == 0) {
					if (res.data) {
						AddCardQy()
					} else {
						uni.navigateTo({
							url: '/pages/my/userInfo?inviterCardId=' + uni.getStorageSync(
								'inviterCardId')
						})
					}
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 加入企业 */
	const AddCardQy = () => {
		if (uni.getStorageSync('token')) {

			let userId = uni.getStorageSync('userId')
			proxy.$http.postJson("/app/comRecord/applyJoin", {
				comCardId: CardInfo.cardId,
				applyContent: content.value
			}).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '申请加入成功',
						icon: 'none'
					})
					show.value = false
					content.value = ''
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/index/index'
						})
					}, 1000)
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				vipCardNum.value = res.data.vipCardNum
			}
		});
	};
	onLoad(option => {
		if (option.pageType) {
			pageType.value = option.pageType
		}
		if (option.userId) {
			byUserId.value = option.userId
		}
		if (option.id) {
			id.value = option.id
		}
		if (option.messageStatus) {
			messageStatus.value = option.messageStatus
		}
		if (option.isYao) {
			isYao.value = option.isYao
		}



	});
	onShow(() => {
		if (uni.getStorageSync('token')) {
			getIsVip()
			getUserInfo()
		}
		if (id.value) {
			uni.showLoading({
				title: '加载中...'
			})
			getCardDetail()
		}

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: linear-gradient(180deg, #eef4ff 0%, #f6f8fc 220rpx, #f4f6fb 100%);
	}

	.page-wrap {
		padding-bottom: 40rpx;
	}

	.company-hero {
		position: relative;
		height: 420rpx;
		overflow: hidden;

		&__cover {
			width: 100%;
			height: 100%;
		}

		&__mask {
			position: absolute;
			inset: 0;
			background: linear-gradient(180deg, rgba(13, 33, 71, 0.08) 0%, rgba(10, 18, 35, 0.18) 100%);
		}
	}

	.company-summary {
		position: relative;
		margin: -58rpx 24rpx 0;
		padding: 24rpx 28rpx 30rpx;
		background: linear-gradient(180deg, #ffffff 0%, #fbfdff 100%);
		border-radius: 28rpx;
		box-shadow: 0 16rpx 48rpx rgba(27, 52, 91, 0.08);
		z-index: 2;
		overflow: hidden;

		&::after {
			content: '';
			position: absolute;
			right: -60rpx;
			top: -80rpx;
			width: 220rpx;
			height: 220rpx;
			border-radius: 50%;
			background: radial-gradient(circle, rgba(74, 144, 226, 0.14) 0%, rgba(74, 144, 226, 0) 72%);
		}

		&__head {
			position: relative;
			z-index: 1;
			display: flex;
			align-items: center;
			gap: 22rpx;
		}

		&__logo-wrap {
			flex-shrink: 0;
			padding: 10rpx;
			border-radius: 34rpx;
			background: linear-gradient(135deg, rgba(104, 160, 255, 0.12) 0%, rgba(138, 215, 193, 0.12) 100%);
		}

		&__logo {
			width: 164rpx;
			height: 164rpx;
			margin-top: 0;
			border-radius: 28rpx;
			background: #ffffff;
			border: 8rpx solid #ffffff;
			box-shadow: 0 10rpx 26rpx rgba(30, 64, 175, 0.12);
			padding: 14rpx;
			box-sizing: border-box;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		&__logo-img {
			width: 100%;
			height: 100%;
		}

		&__main {
			flex: 1;
			min-width: 0;
			display: flex;
			flex-direction: column;
			justify-content: center;
		}

		&__name {
			position: relative;
			z-index: 1;
			margin-top: 0;
			font-size: 36rpx;
			font-weight: 700;
			line-height: 1.35;
			color: #23262f;
			word-break: break-word;
		}

		&__tags {
			position: relative;
			z-index: 1;
			margin-top: 18rpx;
			display: flex;
			flex-wrap: wrap;
			gap: 14rpx 12rpx;

			.pill {
				padding: 10rpx 18rpx 10rpx 14rpx;
				border-radius: 22rpx;
				font-size: 22rpx;
				font-weight: 600;
				letter-spacing: 0.5rpx;
				box-shadow: 0 10rpx 20rpx rgba(72, 108, 163, 0.07);
				display: inline-flex;
				align-items: center;
				gap: 8rpx;
				backdrop-filter: blur(4rpx);
				line-height: 1;

				&::before {
					content: '';
					width: 12rpx;
					height: 12rpx;
					border-radius: 999rpx;
					background: currentColor;
					opacity: 0.35;
					box-shadow: 0 0 0 6rpx rgba(255, 255, 255, 0.55);
				}

				&.cert {
					background: linear-gradient(135deg, #edf8f1 0%, #e5f8ee 100%);
					color: #227a52;
					border-color: #cdebdc;
				}

				&.warn {
					background: linear-gradient(135deg, #fff7e8 0%, #fff2d7 100%);
					color: #b06b12;
					border-color: #f5dfb1;
				}

				&.count::before {
					width: 12rpx;
					height: 12rpx;
				}
			}
		}

		&__strip {
			position: relative;
			z-index: 1;
			margin-top: 24rpx;
			padding: 20rpx 18rpx;
			border-radius: 22rpx;
			background: linear-gradient(135deg, #f4f8ff 0%, #eef8ff 52%, #f3fbf7 100%);
			border: 1rpx solid #e5edf9;
			display: grid;
			grid-template-columns: repeat(3, minmax(0, 1fr));
			gap: 12rpx;
		}

		&__strip--tags {
			grid-template-columns: repeat(2, minmax(0, 1fr));
			background: transparent;
			border: 0;
			padding: 0;
			gap: 14rpx;
		}

		&__strip--stats {
			margin-top: 18rpx;
		}

		&__strip-item {
			min-width: 0;
			padding: 8rpx 10rpx;
			border-radius: 16rpx;
			background: rgba(255, 255, 255, 0.55);
		}

		&__strip-label {
			font-size: 22rpx;
			color: #7c8798;
		}

		&__strip-value {
			margin-top: 10rpx;
			font-size: 24rpx;
			font-weight: 600;
			line-height: 1.5;
			color: #23324d;
			word-break: break-word;
		}

		&__strip-item--tag {
			padding: 18rpx 20rpx;
			background: linear-gradient(135deg, #f8fbff 0%, #eef5ff 100%);
			border: 1rpx solid #dfe9f7;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			gap: 10rpx;
			box-shadow: 0 10rpx 24rpx rgba(47, 82, 140, 0.06);

			.company-summary__strip-label {
				display: inline-flex;
				align-items: center;
				align-self: flex-start;
				padding: 6rpx 14rpx;
				border-radius: 999rpx;
				background: rgba(255, 255, 255, 0.82);
				font-size: 22rpx;
				font-weight: 600;
				color: #607089;
			}

			.company-summary__strip-value {
				margin-top: 0;
				font-size: 24rpx;
				line-height: 1.55;
				text-align: left;
				color: #22324c;
				font-weight: 600;
			}
		}
	}

	.notice-card,
	.card-panel,
	.section-card {
		margin: 24rpx 24rpx 0;
		background: linear-gradient(180deg, #ffffff 0%, #fcfdff 100%);
		border-radius: 28rpx;
		box-shadow: 0 16rpx 48rpx rgba(27, 52, 91, 0.08);
	}

	.notice-card {
		display: flex;
		align-items: center;
		gap: 12rpx;
		padding: 20rpx 24rpx;
		font-size: 24rpx;
		color: #a16207;
		background: #fff5cc;
	}

	.personal-card {
		position: relative;
		overflow: hidden;
		background-size: cover;
		background-repeat: no-repeat;
		background-position: center;
		background-color: #2b3f68;

		&--plain {
			background-image: linear-gradient(135deg, #5d749c 0%, #2b3f68 100%) !important;
		}

		&__mask {
			position: absolute;
			inset: 0;
			background: linear-gradient(180deg, rgba(17, 24, 39, 0.18) 0%, rgba(8, 15, 30, 0.78) 100%);
			backdrop-filter: blur(4rpx);
		}

		&__body {
			position: relative;
			padding: 28rpx;
			padding-top: 28rpx;
			padding-bottom: 104rpx;
		}

		&__top {
			display: flex;
			align-items: flex-start;
			justify-content: flex-start;
			gap: 18rpx;
		}

		&__brand {
			font-size: 24rpx;
			color: rgba(255, 255, 255, 0.82);
			line-height: 1.5;
			flex: 1;
			min-width: 0;
		}

		&__hecha {
			position: absolute;
			right: 28rpx;
			bottom: 28rpx;
			flex-shrink: 0;
		}

		&__main {
			margin-top: 18rpx;
			display: flex;
			justify-content: space-between;
			align-items: flex-start;
			gap: 24rpx;
		}

		&__info {
			flex: 1;
		}

		&__name {
			font-size: 46rpx;
			font-weight: 700;
			color: #ffffff;
		}

		&__job {
			margin-top: 10rpx;
			font-size: 28rpx;
			color: rgba(255, 255, 255, 0.86);
		}

		&__line {
			display: flex;
			align-items: flex-start;
			gap: 12rpx;
			margin-top: 16rpx;
			padding: 10rpx 14rpx;
			border-radius: 16rpx;
			background: rgba(255, 255, 255, 0.08);
			font-size: 25rpx;
			color: #ffffff;
			line-height: 1.5;
		}

		&__avatar {
			width: 132rpx;
			height: 132rpx;
			border-radius: 18rpx;
			border: 4rpx solid rgba(255, 255, 255, 0.75);
			background: rgba(255, 255, 255, 0.24);
			flex-shrink: 0;
		}
	}

	.hecha-badge {
		display: inline-flex;
		align-items: center;
		justify-content: center;
		position: relative;
		width: fit-content;
		padding: 12rpx 26rpx 12rpx 16rpx;
		border-radius: 22rpx;
		background: linear-gradient(135deg, #287afe 0%, #4a96ff 100%);
		border: 1rpx solid rgba(40, 122, 254, 0.22);
		color: #ffffff;
		font-size: 22rpx;
		font-weight: 600;
		letter-spacing: 0.5rpx;
		box-shadow: 0 12rpx 24rpx rgba(40, 122, 254, 0.24);
		backdrop-filter: blur(6rpx);
		margin-top: 16rpx;

		&.checked {
			background: linear-gradient(135deg, #287afe 0%, #4a96ff 100%);
			border-color: rgba(40, 122, 254, 0.22);
			color: #ffffff;
		}

		&__icon {
			width: 28rpx;
			height: 28rpx;
			margin-right: 10rpx;
			flex-shrink: 0;
			opacity: 1;
			filter: brightness(0) invert(1);
		}

		&__vip {
			position: absolute;
			right: -6rpx;
			top: -8rpx;
			width: 28rpx;
			height: 28rpx;
			filter: drop-shadow(0 4rpx 8rpx rgba(79, 58, 19, 0.18));
		}

	}

	.action-panel {
		padding: 28rpx 8rpx;
		display: flex;
		justify-content: space-around;
	}

	.action-item {
		width: 25%;
		text-align: center;

		&__icon {
			width: 88rpx;
			height: 88rpx;
			margin: 0 auto;
			border-radius: 50%;
			background: #edf3ff;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		&__text {
			margin-top: 14rpx;
			font-size: 26rpx;
			color: #4b5563;
		}
	}

	.personal-card .personal-card__hecha {
		position: absolute;
		right: 28rpx;
		left: auto;
		bottom: 28rpx;
		margin: 0;
		z-index: 2;
	}

	.company-entry {
		padding: 24rpx;
		display: flex;
		align-items: center;
		gap: 20rpx;

		&__logo {
			width: 88rpx;
			height: 88rpx;
			border-radius: 20rpx;
			background: #f3f4f6;
		}

		&__content {
			flex: 1;
		}

		&__name-row {
			display: flex;
			align-items: center;
			gap: 12rpx;
		}

		&__name {
			font-size: 32rpx;
			font-weight: 700;
			color: #111827;
			flex: 1;
			min-width: 0;
		}

		&__badge {
			padding: 6rpx 14rpx;
			border-radius: 999rpx;
			font-size: 22rpx;
			font-weight: 600;
			color: #287AFE;
			background: rgba(40, 122, 254, 0.10);
			border: 1rpx solid rgba(40, 122, 254, 0.16);
			flex-shrink: 0;
		}

		&__desc {
			margin-top: 8rpx;
			font-size: 26rpx;
			color: #6b7280;
		}
	}

	.section-card {
		padding: 30rpx 28rpx;
		position: relative;
		overflow: hidden;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 0;
			width: 100%;
			height: 8rpx;
			background: linear-gradient(90deg, #68a0ff 0%, #8ad7c1 100%);
			opacity: 0.9;
		}
	}

	.intro-card {
		background: linear-gradient(135deg, #ffffff 0%, #f7fbff 100%);

		.section-text {
			position: relative;
			padding: 24rpx 26rpx;
			border-radius: 22rpx;
			background: rgba(242, 247, 255, 0.85);
			color: #314056;
		}
	}

	.section-title {
		font-size: 34rpx;
		font-weight: 700;
		color: #111827;
	}

	.section-head {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		padding-bottom: 18rpx;
		border-bottom: 1rpx solid #eef2f7;

		&.simple {
			padding-bottom: 0;
			border-bottom: 0;
		}

		&__badge {
			padding: 10rpx 18rpx;
			border-radius: 999rpx;
			background: linear-gradient(135deg, #edf4ff 0%, #e6f8f2 100%);
			font-size: 22rpx;
			font-weight: 600;
			color: #4065a8;
			white-space: nowrap;
		}
	}

	.section-subtitle {
		margin-top: 8rpx;
		font-size: 24rpx;
		color: #8a94a6;
	}

	.section-text {
		margin-top: 20rpx;
		font-size: 28rpx;
		line-height: 1.8;
		color: #374151;
	}

	.info-highlight {
		display: grid;
		grid-template-columns: repeat(3, minmax(0, 1fr));
		gap: 16rpx;
		margin-top: 24rpx;

		&__item {
			padding: 24rpx 22rpx;
			border-radius: 22rpx;
			background: linear-gradient(135deg, #f8fbff 0%, #eef6ff 45%, #f4fbf8 100%);
			border: 1rpx solid #e2ecfb;
			min-height: 150rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
		}

		&__label {
			font-size: 24rpx;
			color: #7b8797;
			line-height: 1.5;
		}

		&__value {
			margin-top: 18rpx;
			font-size: 34rpx;
			font-weight: 700;
			line-height: 1.35;
			color: #1d3557;
			word-break: break-word;
		}
	}

	.info-list {
		margin-top: 22rpx;
		padding: 10rpx 0 0;
		background: rgba(247, 250, 255, 0.72);
		border-radius: 22rpx;
		padding: 10rpx 22rpx 4rpx;
	}

	.info-item {
		display: flex;
		align-items: flex-start;
		gap: 18rpx;
		padding: 22rpx 0;
		border-bottom: 1rpx solid #edf1f6;

		&:last-child {
			border-bottom: 0;
			padding-bottom: 0;
		}

		&__index {
			width: 56rpx;
			height: 56rpx;
			border-radius: 18rpx;
			background: #f2f6fb;
			font-size: 22rpx;
			font-weight: 700;
			color: #6780a4;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-shrink: 0;
		}

		&__body {
			flex: 1;
			min-width: 0;
		}

		&__label {
			font-size: 26rpx;
			font-weight: 600;
			color: #556070;
		}

		&__value {
			margin-top: 12rpx;
			font-size: 28rpx;
			line-height: 1.7;
			color: #1f2937;
			word-break: break-word;
		}
	}

	.product-grid {
		display: grid;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 16rpx;
		margin-top: 22rpx;

		&__card {
			position: relative;
			height: 240rpx;
			border-radius: 20rpx;
			overflow: hidden;
			box-shadow: 0 16rpx 32rpx rgba(18, 38, 63, 0.1);
			transform: translateZ(0);
		}

		&__item {
			width: 100%;
			height: 100%;
			background: #e5e7eb;
		}

		&__mask {
			position: absolute;
			left: 0;
			right: 0;
			bottom: 0;
			height: 96rpx;
			background: linear-gradient(180deg, rgba(15, 23, 42, 0) 0%, rgba(15, 23, 42, 0.62) 100%);
		}

		&__caption {
			position: absolute;
			left: 18rpx;
			bottom: 16rpx;
			font-size: 24rpx;
			font-weight: 600;
			color: #ffffff;
			letter-spacing: 1rpx;
		}
	}

	.preserve-line {
		white-space: pre-wrap;
	}

	.tag-row {
		display: flex;
		flex-wrap: wrap;
		gap: 12rpx;
		margin-top: 18rpx;
	}

	.intro-tags {
		margin-top: 18rpx;
		margin-bottom: 6rpx;
	}

	.pill {
		padding: 8rpx 18rpx;
		border-radius: 999rpx;
		font-size: 22rpx;

		&.light {
			background: rgba(255, 255, 255, 0.18);
			color: #ffffff;
			border: 1rpx solid rgba(255, 255, 255, 0.24);
		}

		&.gray {
			background: #f3f4f6;
			color: #6b7280;
		}

		&.soft {
			background: linear-gradient(135deg, #f5f8fd 0%, #eef4fb 100%);
			color: #5f6f86;
			border: 1rpx solid #e2eaf5;
		}
	}

	.gallery-scroll {
		margin-top: 22rpx;
		white-space: nowrap;
		padding-bottom: 6rpx;
	}

	.gallery-row {
		display: inline-flex;
		gap: 18rpx;

		&__card {
			position: relative;
			width: 320rpx;
			height: 220rpx;
			border-radius: 20rpx;
			overflow: hidden;
			box-shadow: 0 16rpx 32rpx rgba(18, 38, 63, 0.1);
		}

		&__item {
			width: 100%;
			height: 100%;
			background: #e5e7eb;
		}

		&__caption {
			position: absolute;
			left: 18rpx;
			bottom: 16rpx;
			padding: 8rpx 14rpx;
			border-radius: 999rpx;
			font-size: 22rpx;
			color: #ffffff;
			background: rgba(15, 23, 42, 0.42);
			backdrop-filter: blur(6rpx);
		}
	}

	.qualification-list {
		margin-top: 22rpx;
	}

	.qualification-item {
		padding: 24rpx;
		border-radius: 24rpx;
		background: linear-gradient(180deg, #fcfdff 0%, #f6faff 100%);
		border: 1rpx solid #edf2f8;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.8);

		&:not(:first-child) {
			margin-top: 28rpx;
		}

		&__name {
			font-size: 30rpx;
			font-weight: 600;
			color: #1f2937;
		}

		&__images {
			display: flex;
			flex-wrap: wrap;
			gap: 16rpx;
			margin-top: 18rpx;
		}

		&__image {
			width: calc((100% - 16rpx) / 2);
			height: 220rpx;
			border-radius: 18rpx;
			background: #e5e7eb;
		}
	}

	.video-box {
		margin-top: 22rpx;

		&__player {
			width: 100%;
			height: 360rpx;
			border-radius: 20rpx;
		}
	}

	.intro-image-list {
		margin-top: 22rpx;

		&__item {
			width: 100%;
			border-radius: 20rpx;
			overflow: hidden;
			background: #e5e7eb;

			&:not(:first-child) {
				margin-top: 16rpx;
			}
		}
	}

	.bottom-space {
		height: 160rpx;
	}

	.bottom-bar {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		padding: 20rpx 24rpx calc(20rpx + env(safe-area-inset-bottom));
		background: rgba(255, 255, 255, 0.96);
		box-shadow: 0 -8rpx 36rpx rgba(15, 23, 42, 0.08);
		backdrop-filter: blur(8rpx);

		&__btn {
			height: 88rpx;
			border-radius: 22rpx;
			background: linear-gradient(135deg, #2f6bff 0%, #3d8bff 100%);
			color: #ffffff;
			font-size: 30rpx;
			font-weight: 600;
			display: flex;
			align-items: center;
			justify-content: center;

			&.disabled {
				background: #cbd5e1;
				color: #ffffff;
			}
		}
	}

	.popup {
		width: 500rpx;
		padding: 30rpx;

		.popupTitle {
			font-size: 30rpx;
			font-weight: bold;
			text-align: center;
		}

		.popupInput {
			margin-top: 30rpx;
			background: #f2f2f2;
			border-radius: 12rpx;
			padding-bottom: 30rpx;
			// height: 200rpx;
		}

		.u-textarea {
			background: #f2f2f2 !important;
		}

		.popupBtn {
			background: #FFFFFF;
			border-radius: 34rpx;
			background: #287AFE;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 28rpx;
			color: #FFFFFF;
			padding: 0 30rpx;
			height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 30rpx;
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
