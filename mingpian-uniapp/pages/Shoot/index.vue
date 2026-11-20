<template>
	<view class="">
		<camera class="camera" :style="{height:screenHeight+'px'}" id="myCamera" :flash="flash"
			:device-position="devicePosition" @error="onCameraError">

			<cover-view class="boxBot">
				<cover-view @click="handleCut" class="qiehuan">
					<cover-image src="/static/image/qiehuan.png"></cover-image>
				</cover-view>
				<cover-view @click="handleClick" class="photo">
					<cover-image src="/static/image/click.png"></cover-image>
				</cover-view>
				<cover-view @click="handleClick" class="xcImg">
					<cover-image src="/static/image/xiagnce.png"></cover-image>
				</cover-view>
			</cover-view>
		</camera>
		<!-- <image :src="tempImagePath"></image> -->
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
	const screenHeight = ref('');

	onLoad(option => {
		//因为手机状态栏高度的偏差，需要对条件粘性布局进行调整
		//获取手机系统信息
		let systemInfo = uni.getSystemInfoSync();
		console.log(systemInfo)
		screenHeight.value = systemInfo.screenHeight
	});
	onShow(() => {

	});
	const flash = ref('')
	const devicePosition = ref('back')
	const tempImagePath = ref('');

	// 打开摄像头 拍照
	const handleClick = () => {
		let cameraContext = uni.createCameraContext(); // 创建camera上下文实例
		console.log(cameraContext)
		cameraContext.takePhoto({
			quality: 'high', // 指定拍照质量
			success: (res) => {
				console.log('拍照成功', res);
				tempImagePath.value = res.tempImagePath
			},
			fail: (err) => {
				// 拍照失败的回调
				console.error('拍照失败', err);
			}
		})
	};
	// 切换摄像头
	const handleCut = () => {
		console.log('切换');
		devicePosition.value = devicePosition.value == 'front' ? 'back' : 'front'
	}
</script>

<style lang="scss">
	.camera {
		width: 100%;
		height: 600rpx;
		background: #ffbcab;

	}

	image {
		width: 400rpx;
		height: 400rpx;

	}

	.boxBot {
		position: fixed;
		bottom: 50px;
		left: 0;
		right: 0;
		z-index: 99;
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
		padding: 0 60rpx;
	}

	.photo {
		width: 110rpx;
		height: 110rpx;

		image {
			width: 110rpx;
			height: 110rpx;
		}
	}

	.qiehuan {
		width: 60rpx;
		height: 60rpx;

		image {
			width: 60rpx;
			height: 60rpx;
		}
	}

	.xcImg {
		width: 60rpx;
		height: 60rpx;

		image {
			width: 60rpx;
			height: 60rpx;
		}
	}
</style>