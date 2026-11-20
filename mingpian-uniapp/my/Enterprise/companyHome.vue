<template>
	<view class="page-wrap">
		<view class="company-hero">
			<image class="company-hero__cover" :src="companyCover" mode="aspectFill"></image>
			<view class="company-hero__mask"></view>
		</view>

		<view class="company-summary">
			<view class="company-summary__head">
				<view class="company-summary__logo-wrap">
					<view class="company-summary__logo">
						<image class="company-summary__logo-img" :src="companyLogoUrl" mode="aspectFit"></image>
					</view>
				</view>
				<view class="company-summary__main">
					<view class="company-summary__name">{{ CardInfo.companyName || '企业主页' }}</view>
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
				<view class="company-summary__strip-item company-summary__strip-item--tag" v-for="(item, index) in companyTopTags" :key="index">
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

		<view v-if="CardInfo.companyBlurb" class="section-card intro-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">企业简介</view>
					<view class="section-subtitle">品牌定位与主营方向</view>
				</view>
			</view>
			<view class="section-text preserve-line">{{ CardInfo.companyBlurb }}</view>
		</view>

		<view v-if="companyTopTags.length" class="section-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">企业信息</view>
					<view class="section-subtitle">核心经营数据与企业画像</view>
				</view>
			</view>
			<view class="company-summary__strip company-summary__strip--tags inside">
				<view class="company-summary__strip-item company-summary__strip-item--tag" v-for="(item, index) in companyTopTags" :key="'inside-' + index">
					<view class="company-summary__strip-label">{{ item.label }}</view>
					<view class="company-summary__strip-value">{{ item.value }}</view>
				</view>
			</view>
		</view>

		<view v-if="productImageList.length" class="section-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">公司产品</view>
					<view class="section-subtitle">展示产品与服务形象</view>
				</view>
				<view class="section-head__badge">{{ productImageList.length }}张</view>
			</view>
			<view class="product-grid">
				<view v-for="(item, index) in productImageList" :key="index" class="product-grid__card" @tap="prewImg(item)">
					<image class="product-grid__item" :src="item" mode="aspectFill"></image>
					<view class="product-grid__mask"></view>
					<view class="product-grid__caption">产品 {{ (index + 1).toString().padStart(2, '0') }}</view>
				</view>
			</view>
		</view>

		<view v-if="companyAmbientList.length" class="section-card">
			<view class="section-head simple">
				<view>
					<view class="section-title">企业环境</view>
					<view class="section-subtitle">办公空间与企业氛围</view>
				</view>
				<view class="section-head__badge">{{ companyAmbientList.length }}张</view>
			</view>
			<scroll-view scroll-x class="gallery-scroll" show-scrollbar="false">
				<view class="gallery-row">
					<view v-for="(item, index) in companyAmbientList" :key="index" class="gallery-row__card" @tap="prewImg(item)">
						<image class="gallery-row__item" :src="item" mode="aspectFill"></image>
						<view class="gallery-row__caption">环境 {{ (index + 1).toString().padStart(2, '0') }}</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<view v-if="companyFlairList.length" class="section-card">
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
						<image v-for="(img, imgIndex) in item.images" :key="imgIndex" class="qualification-item__image" :src="img" mode="aspectFill" @tap="prewImg(img)"></image>
					</view>
				</view>
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
	import { computed, getCurrentInstance, reactive, ref } from 'vue'
	import { onLoad, onShow } from '@dcloudio/uni-app'

	const { proxy } = getCurrentInstance()
	const id = ref('')
	const CardInfo = reactive({})
	const isVip = ref(false)
	const showVip = ref(false)

	const splitImageList = (value) => {
		if (!value) return []
		if (Array.isArray(value)) return value.filter(Boolean)
		return value.split(',').map(item => item.trim()).filter(Boolean)
	}

	const companyLogoUrl = computed(() => CardInfo.companyLogo || CardInfo.cardAvatar || '/static/logo.png')
	const companyAmbientList = computed(() => splitImageList(CardInfo.companyAmbient))
	const productImageList = computed(() => splitImageList(CardInfo.firstProductImg))
	const companyCover = computed(() => companyAmbientList.value[0] || CardInfo.cardImg || companyLogoUrl.value)
	const industryText = computed(() => CardInfo.industrySecondName || CardInfo.industryFirstName || '')
	const companyTopTags = computed(() => {
		const list = []
		if (CardInfo.companyAddress) list.push({ label: '地址', value: CardInfo.companyAddress })
		if (CardInfo.companyArea) list.push({ label: '面积', value: CardInfo.companyArea + 'm²' })
		if (CardInfo.companyOutputValue) list.push({ label: '产值', value: CardInfo.companyOutputValue + '万元' })
		if (industryText.value) list.push({ label: '行业', value: industryText.value })
		return list
	})
	const companySummaryStrip = computed(() => {
		const list = []
		if (productImageList.value.length) list.push({ label: '产品图集', value: productImageList.value.length + '张' })
		if (companyAmbientList.value.length) list.push({ label: '环境实拍', value: companyAmbientList.value.length + '张' })
		if (companyFlairList.value.length) list.push({ label: '资质证明', value: companyFlairList.value.length + '项' })
		return list.slice(0, 3)
	})
	const companyFlairList = computed(() => {
		const rawValue = CardInfo.companyFlair || CardInfo.companyFlairs
		if (!rawValue) return []
		try {
			const list = JSON.parse(rawValue)
			if (!Array.isArray(list)) return []
			return list.map(item => ({
				name: item.name,
				images: splitImageList(item.pic)
			})).filter(item => item.images.length)
		} catch (error) {
			return []
		}
	})

	const prewImg = (img) => {
		uni.previewImage({
			urls: [img],
			current: 0
		})
	}
	const getIsVip = () => {
		const userId = uni.getStorageSync('userId')
		if (!userId) return
		proxy.$http.getT('/app/user/isVip?userId=' + userId).then(res => {
			if (res.code === 0) {
				isVip.value = res.data
			}
		})
	}
	const goVip = () => {
		showVip.value = false
		uni.navigateTo({
			url: '/my/vip/index'
		})
	}
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
	}
	const getCompanyDetail = () => {
		const userId = uni.getStorageSync('userId') || ''
		const url = userId ? '/app/businessCard/info?cardId=' + id.value + '&userId=' + userId : '/app/businessCard/info?cardId=' + id.value
		proxy.$http.get(url).then(res => {
			uni.hideLoading()
			if (res.code === 0) {
				Object.assign(CardInfo, res.data)
				uni.setNavigationBarTitle({ title: '企业主页' })
			} else {
				uni.showToast({ title: res.msg, icon: 'none' })
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			}
		})
	}

	onLoad((option) => {
		if (option.id) {
			id.value = option.id
		}
	})

	onShow(() => {
		if (uni.getStorageSync('token')) {
			getIsVip()
		}
		if (id.value) {
			uni.showLoading({ title: '加载中...' })
			getCompanyDetail()
		}
	})
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
			font-size: 36rpx;
			font-weight: 700;
			line-height: 1.35;
			color: #23262f;
			word-break: break-word;
		}

		&__tags {
			margin-top: 18rpx;
			display: flex;
			flex-wrap: wrap;
			gap: 14rpx 12rpx;
		}

		&__strip {
			margin-top: 24rpx;
			padding: 20rpx 18rpx;
			border-radius: 22rpx;
			background: linear-gradient(135deg, #f4f8ff 0%, #eef8ff 52%, #f3fbf7 100%);
			border: 1rpx solid #e5edf9;
			display: grid;
			grid-template-columns: repeat(3, minmax(0, 1fr));
			gap: 12rpx;

			&.inside {
				margin-top: 20rpx;
			}
		}

		&__strip--tags {
			grid-template-columns: repeat(2, minmax(0, 1fr));
			background: transparent;
			border: 0;
			padding: 0;
			gap: 14rpx;
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
			gap: 10rpx;
			box-shadow: 0 10rpx 24rpx rgba(47, 82, 140, 0.06);
		}
	}

	.section-card {
		margin: 24rpx 24rpx 0;
		padding: 30rpx 28rpx;
		background: linear-gradient(180deg, #ffffff 0%, #fcfdff 100%);
		border-radius: 28rpx;
		box-shadow: 0 16rpx 48rpx rgba(27, 52, 91, 0.08);
	}

	.intro-card .section-text {
		padding: 24rpx 26rpx;
		border-radius: 22rpx;
		background: rgba(242, 247, 255, 0.85);
		color: #314056;
	}

	.section-head {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;

		&.simple {
			padding-bottom: 0;
		}

		&__badge {
			padding: 10rpx 18rpx;
			border-radius: 999rpx;
			background: linear-gradient(135deg, #edf4ff 0%, #e6f8f2 100%);
			font-size: 22rpx;
			font-weight: 600;
			color: #4065a8;
		}
	}

	.section-title {
		font-size: 34rpx;
		font-weight: 700;
		color: #111827;
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
	}

	.preserve-line {
		white-space: pre-wrap;
	}

	.pill {
		padding: 10rpx 18rpx 10rpx 14rpx;
		border-radius: 22rpx;
		font-size: 22rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
		gap: 8rpx;
		border: 1rpx solid #e2eaf5;

		&::before {
			content: '';
			width: 12rpx;
			height: 12rpx;
			border-radius: 999rpx;
			background: currentColor;
			opacity: 0.35;
		}

		&.soft {
			background: linear-gradient(135deg, #f5f8fd 0%, #eef4fb 100%);
			color: #5f6f86;
		}

		&.cert {
			background: linear-gradient(135deg, #edf8f1 0%, #e5f8ee 100%);
			color: #227a52;
		}

		&.warn {
			background: linear-gradient(135deg, #fff7e8 0%, #fff2d7 100%);
			color: #b06b12;
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
		box-shadow: 0 12rpx 24rpx rgba(40, 122, 254, 0.24);
		margin-top: 16rpx;

		&__icon {
			width: 28rpx;
			height: 28rpx;
			margin-right: 10rpx;
			filter: brightness(0) invert(1);
		}

		&__vip {
			position: absolute;
			right: -6rpx;
			top: -8rpx;
			width: 28rpx;
			height: 28rpx;
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
		}

		&__item {
			width: 100%;
			height: 100%;
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
		}
	}

	.gallery-scroll {
		margin-top: 22rpx;
		white-space: nowrap;
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
		}
	}

	.popupVip {
		width: 540rpx;
		padding: 32rpx 32rpx 40rpx;
		text-align: center;

		image {
			width: 100%;
		}
	}

	.popupVip_title {
		margin-top: 18rpx;
		font-size: 34rpx;
		font-weight: 700;
	}

	.popupVip_tit {
		margin-top: 12rpx;
		font-size: 26rpx;
		color: #6b7280;
	}

	.popupVip_btn {
		margin-top: 28rpx;
		height: 84rpx;
		border-radius: 18rpx;
		background: linear-gradient(135deg, #287afe 0%, #4a96ff 100%);
		color: #ffffff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 30rpx;
		font-weight: 600;
	}
</style>
