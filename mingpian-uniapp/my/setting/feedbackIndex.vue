<!-- 帮助反馈页面 -->
<template>
	<view>
		<view class="bg-list">
			<view v-for="(item,index) in helpClassifyList" :key="index" :title="item.helpClassifyName"
				class="list-title padding-bottom">
				<view class="flex align-center justify-between" @click.stop="openList(item)">
					<view class="text-title">{{item.helpClassifyName}}</view>
					<view @click.stop="openList(item)">
						<image src="../static/up.png" style="width: 21rpx;height: 15rpx;" v-if="item.parentId==0">
						</image>
						<image src="../static/dowm.png" style="width: 21rpx;height: 15rpx;" v-else></image>
					</view>
				</view>
				<view v-for="(problemItem,problemIndex) in item.helpWordList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)" v-if="item.parentId==0">
					<view class="text-item">{{problemItem.helpWordTitle}}</view>
					<view class="line" v-if="problemIndex!=item.helpWordList.length-1"></view>
				</view>
			</view>

			<!-- <view v-for="(item,index) in helpClassifyList" :key="index" :title="item.helpClassifyName"
				class="list-title padding-bottom">
				<view class="flex align-center justify-between" @click.stop="onClick(item)">
					<view class="text-title">{{item.helpClassifyName}}</view>
					<view>
						<image src="/static/image/my/right.png" style="width: 14rpx;height: 24rpx;" mode="widthFix"></image>
					</view>
				</view>
			</view> -->
		</view>


		<view class="bg-box">
			<view class="bg-white-box">
				<view class="flex align-center">
					<image src="../static/icon-letter.png" class="image"></image>
					<view class="text-feedback" hover-class="hover" @click="goChat()">联系客服</view>
				</view>
				<view class="vertical-line"></view>

				<image src="../static/icon-edit.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedback()">我要反馈</view>
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

	// 列表
	const helpClassifyList = reactive([]);
	// 客服方式 1 企业客服 2打电话 3小程序客服 4 二维码客服
	const SelKeFu = ref('')
	
	// 打开分类数据
	const openList = (item) => {
		var oldhelpClassifyList = item
		if (oldhelpClassifyList.parentId == 1) {
			item.parentId = 0
		} else {
			item.parentId = 1
		}
	};
	// 获取列表
	const getlist = () => {
		let data = {
			page: 1,
			limit: 100,
			type: 1
		}
		proxy.$http.get("/app/help/selectHelpList", data).then(res => {
			if (res.code == 0) {
				res.data.forEach(d => {
					d.parentId = 1
				})
				helpClassifyList.splice(0, helpClassifyList.length, ...res.data)




			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 跳转详情
	const onClick = (item) => {
		uni.navigateTo({
			url: '/my/setting/helpDetail?title=' + item.helpWordTitle + '&helpWordId=' + item.helpWordId,
		})
	};

	// 联系客服
	const goChat = () => {
		let kefu = uni.getStorageSync('SelKeFu'); // 用户端联系方式 1 手机号 2企业微信
		let kefuPhone = uni.getStorageSync('kefuPhone');
		if (kefu == 1) {
			uni.makePhoneCall({
				phoneNumber: kefuPhone //仅为示例
			});
		} else if (kefu == 2) {
			// #ifdef MP-WEIXIN
			let that = this
			try {
				wx.openCustomerServiceChat({
					extInfo: {
						url: uni.getStorageSync('kefuUrl')
					},
					corpId: uni.getStorageSync('kefuAppId'),
					success(res) {},
					fail(res) {
						console.error(res)
					}
				})
			} catch (error) {
				console.error("catchcatch" + error)
				uni.showToast({
					title: '请更新至微信最新版本'
				});
			}
			// #endif
			// #ifndef MP-WEIXIN
			let url = uni.getStorageSync('kefuUrl');
			if (url.indexOf('/pages/') !== -1 || url.indexOf('/pageA/') !== -1 || url.indexOf('/my/') !== -1) {
				uni.navigateTo({
					url
				});
			} else {
				//#ifndef H5
				uni.navigateTo({
					url: '/pages/index/webView?url=' + url
				});
				//#endif
				//#ifdef H5
				window.location.href = url;
				//#endif
			}
			// #endif
		} else if (kefu == 3) {
			uni.navigateTo({
				url: '/my/setting/customer'
			});
		}
	};

	// 跳转登录
	const goLoginInfo = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 跳转意见反馈
	const toFeedback = () => {
		uni.navigateTo({
			url: '/my/setting/feedback'
		});
	};

	onLoad(option => {
		SelKeFu.value = uni.getStorageSync('kefu');
		getlist()
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

	button::after {
		border: none;
		background-color: none;
	}

	button {
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		line-height: 1.35;
		overflow: hidden;
		color: #FFFFFF;
		/* background-color: #fff; */
		background-color: rgba(255, 255, 255, 0) !important;
		font-size: 28rpx;
		width: 100%;
		height: 100%;
	}

	.bg-box {

		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.bg-list {
		margin-bottom: 100rpx;

		padding: 30rpx
	}

	.bg-white-box {
		background: #f5f5f5;
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;

		font-size: 32rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #999999;
	}



	.text-title {

		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {

		font-size: 28rpx;
		padding: 24rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
		border-bottom: 1px solid #eeeeee;
	}

	.list-question {

		font-size: 28rpx;
	}

	.hover {
		background-color: #f5f5f5;
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
		color: #000000;
	}

	.text-top {
		margin: 30rpx;

		font-size: 34rpx;
	}
</style>