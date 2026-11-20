<template>
	<router-view></router-view>
</template>

<script>
	export default {
		// watch: {
		//   $route(to, from) {
		//     // 根据条件判断是否强制刷新组件
		//  console.log(from)
		//     if (!from.meta.keepAlive) {
		//       this.$nextTick(() => {
		//         this.$forceUpdate();
		//       });
		//     }
		//   }
		// }
	}
</script>