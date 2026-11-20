<template>
	<view>
		<!-- 自定义导航栏 -->
		<up-tabbar :value="value" :fixed="true" :placeholder="true" :safeAreaInsetBottom="false" :height="110+'rpx'"
			activeColor="#287AFE" inactiveColor="#666666">
			<up-tabbar-item text="首页" @click="goSwitch">
				<template #active-icon>
					<image class="item_icon" src="/static/image/tabbar/fiest_.png">
					</image>
				</template>
				<template #inactive-icon>
					<image class="item_icon" src="/static/image/tabbar/fiest.png"></image>
				</template>
			</up-tabbar-item>

			<up-tabbar-item text="名片夹" @click="goSwitch">
				<template #active-icon>
					<image class="item_icon" src="/static/image/tabbar/card_.png">
					</image>
				</template>
				<template #inactive-icon>
					<image class="item_icon" src="/static/image/tabbar/card.png"></image>
				</template>
			</up-tabbar-item>

			<up-tabbar-item @click="goSwitch">
				<template #active-icon>
					<image class="item_icons" src="/static/image/tabbar/photo.png">
					</image>
				</template>
				<template #inactive-icon>
					<image class="item_icons" src="/static/image/tabbar/photo.png"></image>
				</template>
			</up-tabbar-item>

			<up-tabbar-item text="企业" @click="goSwitch">
				<template #active-icon>
					<image class="item_icon" src="/static/image/tabbar/qiye_.png">
					</image>
				</template>
				<template #inactive-icon>
					<image class="item_icon" src="/static/image/tabbar/qiye.png"></image>
				</template>
			</up-tabbar-item>

			<up-tabbar-item text="我" @click="goSwitch">
				<template #active-icon>
					<image class="item_icon" src="/static/image/tabbar/wo_.png">
					</image>
				</template>
				<template #inactive-icon>
					<image class="item_icon" src="/static/image/tabbar/wo.png"></image>
				</template>
			</up-tabbar-item>
		</up-tabbar>

	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	// 接收一下父组件的传值
	defineProps({
		//下标
		value: {
			type: [String, Number],
			default: ''
		},
	})



	const goSwitch = (e) => {
		console.log(e)
		if (e !== 2) {
			switch (e) {
				case 0:
					uni.switchTab({
						url: '/pages/index/index'
					})
					break;
				case 1:
					uni.switchTab({
						url: '/pages/Business/index'
					})
					break;
				case 3:
					uni.switchTab({
						url: '/pages/Enterprise/index'
					})
					break;
				case 4:
					uni.switchTab({
						url: '/pages/my/index'
					})
					break;
				default:
					break;
			}
		} else {
			console.log('扫码')
			// uni.navigateTo({
			// 	url: '/pages/Shoot/index'
			// })

			if (uni.getStorageSync('token')) {

				// #ifndef H5
				// 允许从相机和相册扫码
				uni.scanCode({
					onlyFromCamera: true,
					success: function(res) {
						console.log(res, '扫码')
						console.log(res.path)
						let router = decodeURIComponent(res.path);

						// 提取 scene 参数值
						const match = router.match(/scene=([^&]*)/);
						if (match && match[1]) {
							const sceneValue = match[1];
							const [id, type] = sceneValue.split(',');

							console.log('提取的ID:', id); // 250781
							console.log('提取的Type:', type); // 73
							uni.setStorageSync('inviterCode', id);
							if(type){
								uni.navigateTo({
									url: '/my/Enterprise/businessCard?id=' + type
									// 这里可以使用 id 和 type
								})
							}else{
								uni.showToast({
									title:'名片id识别失败',
									icon:'none'
								})
							}
							
						}
					}
				});
				// #endif
			} else {
				uni.navigateTo({
					url: '/pages/public/login'
				})
			}
		}
	};
</script>

<style lang="scss">
	.item_icon {
		width: 46rpx;
		height: 46rpx;
	}

	.item_icons {
		width: 65rpx;
		height: 65rpx;
	}
</style>