<template>
	<view>
		<view class="bgImage">
			<view class="content">

				<view class="box">
					<view class="boxHead">
						<view class="boxHead_num">01</view>
						<view class="boxHead_title">设计专属自己企业的数字名片</view>
					</view>
					<view class="flex flex-wrap margin-top-sm justify-between">
						<view class="boxLabel margin-bottom-sm" >名片职务</view>
						<view class="boxLabel margin-bottom-sm" >行业类型</view>
						<view class="boxLabel margin-bottom-sm" >公司产品</view>
						<view class="boxLabel margin-bottom-sm" >公司简介</view>
						<view class="boxLabel margin-bottom-sm" >公司地址</view>
						<view class="boxLabel margin-bottom-sm" >年产值</view>
						<view class="boxLabel margin-bottom-sm" >公司面积</view>
						<view class="boxLabel margin-bottom-sm" >企业logo</view>
					</view>
				</view>
				<view class="box" style="padding-bottom: 20rpx;">
					<view class="boxHead">
						<view class="boxHead_num">02</view>
						<view class="boxHead_title">高效智能管理企业成员名片</view>
					</view>
					<view class="flex flex-wrap margin-top-sm justify-between">
						<view class="boxLabel margin-bottom-sm" >在线录入</view>
						<view class="boxLabel margin-bottom-sm" >邀请加入</view>
						<view class="boxLabel margin-bottom-sm" >成员动态</view>
						<view class="boxLabel margin-bottom-sm" >额度进度</view>
						
					</view>
				</view>
				<view class="box" style="padding-bottom: 20rpx;">
					<view class="boxHead">
						<view class="boxHead_num">03</view>
						<view class="boxHead_title">可视化数据面板</view>
					</view>
					<view class="flex flex-wrap margin-top-sm justify-between">
						<view class="boxLabel margin-bottom-sm">累计访问数量</view>
						<view class="boxLabel margin-bottom-sm">今日访问数量</view>
						<view class="boxLabel margin-bottom-sm">分享名片数量</view>
						<view class="boxLabel margin-bottom-sm">名片排行榜单</view>
					</view>
				</view>

				<view class="btn" @click="goCampay()">立即使用</view>
			</view>
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

	onLoad(option => {

	});
	onShow(() => {

	});
	// 跳转入驻企业
	const goCampay = () => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/my/Enterprise/PublishOne'
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	.bgImage {
		background-image: url('https://mpfx.xianmaxiong.com/image/qiymp.png');
		background-size: 100% 100%;
		width: 100%;
		height: 1948rpx;

	}

	.content {
		position: relative;
		top: 815rpx;
	}

	.box {
		// width: 686rpx;
		// height: 225rpx;
		background: linear-gradient(74deg, #AED3FE 0%, #EEF6FF 100%);
		border-radius: 14rpx;
		margin: 0 24rpx 30rpx 40rpx;
		padding: 40rpx 20rpx 40rpx 40rpx;

		.boxHead {
			display: flex;
			align-items: end;

			.boxHead_num {
				width: 74rpx;
				height: 74rpx;
				background: #3A64FF;
				border-radius: 88rpx 88rpx 88rpx 0;
				display: flex;
				align-items: center;
				justify-content: center;
				padding-right: 10rpx;
				font-family: DINPro;
				font-weight: bold;
				font-size: 50rpx;
				color: #FFFFFF;
				line-height: 43rpx;
				font-style: italic;


			}

			.boxHead_title {
				font-family: 045-SSYiFeiTi;
				font-weight: bold;
				font-size: 34rpx;
				color: #000000;
				font-style: italic;
				margin-left: 20rpx;
			}
		}

		.boxLabel {
			padding: 0 18rpx;
			height: 46rpx;
			background: #E4F0FF;
			border-radius: 23rpx;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 24rpx;
			color: #325DFF;
			display: flex;
			align-items: center;
			justify-content: center;

		}
	}

	.btn {
		margin: 80rpx 30rpx;
		height: 88rpx;
		background: #FFFFFF;
		border-radius: 44rpx;
		opacity: 0.8;
		font-family: Source Han Sans CN;
		font-weight: 400;
		font-size: 30rpx;
		color: #2E5CF3;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>