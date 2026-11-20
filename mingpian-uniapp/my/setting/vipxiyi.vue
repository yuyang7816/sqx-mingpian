<template>
	<view>
		<view style="line-height: 26px;padding: 32rpx;" class="home1">
			<view style="font-size: 28rpx;" v-html="content"></view>
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

	const content = ref('');

	// 会员服务协议	315
	const getText = () => {
		proxy.$http.getT('/app/common/type/315').then(res => {
			if (res.code === 0) {
				content.value = res.data.value;
			}
		});
	}

	onLoad(option => {
		getText()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}
</style>