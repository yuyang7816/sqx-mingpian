<template>
	<view class="content">
		<!-- <up-sticky bgColor="#ffffff">
			<view class="navbar">
				<up-tabs :list="tabList" @click="tabClicks" :current="current" :scrollable="false"></up-tabs>
			</view>
		</up-sticky> -->
		<view v-if="current==0">
			<view v-for="(item, index) in list" :key="index" class="item" @click="goDet(item.content)">
				<view class="" v-if="item.type==1">
					<view class="flex justify-between" style="font-size: 30rpx;">
						<view>
							<view style="color: #333333;" v-if="item.status==1">{{ item.byUserName }}申请交换名片，是否同意</view>
							<view style="color: #333333;" v-if="item.status==2">{{ item.byUserName }}发起申请交换名片，已同意</view>
							<view style="color: #333333;" v-if="item.status==3">{{ item.byUserName }}发起申请交换名片，已拒绝</view>
							<view style="margin-top: 10rpx;color: #999999;font-size: 28rpx;">
								{{ item.createAt }}
							</view>
						</view>
						<view v-if="item.isSee == 0"
							style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
						</view>


					</view>
					<view class="flex align-center justify-end" v-if="item.status==1">
						<view class="tybtn" @click.stop="clickEx(item,1)">同意</view>
						<view class="jjbtn" @click.stop="clickEx(item,2)">拒绝</view>
					</view>
					<view v-if="list.length-1!=index"
						style="width: 100%;height: 1rpx;background: #f5f5f5;margin-top: 30rpx;"></view>
				</view>
				<view v-else>
					<view class="flex justify-between"
						style="font-size: 30rpx;width: 100%;overflow: hidden;text-overflow: ellipsis;white-space:nowrap">
						<view style="color: #333333;">{{ item.title }}</view>
						<view v-if="item.isSee == 0"
							style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
						</view>
					</view>
					<view style="color: #999999;font-size: 28rpx;margin-top: 10rpx;">{{ item.content }}</view>

					<view style="margin-top: 10rpx;color: #999999;font-size: 28rpx;text-align: right;">
						{{ item.createAt }}
					</view>
					<view v-if="list.length-1!=index"
						style="width: 100%;height: 1rpx;background: #f5f5f5;margin-top: 30rpx;"></view>
				</view>
			</view>
		</view>
		<view v-if="current==1">
			<view v-for="(item, index) in list" :key="index" class="item" @click="goDet(item.content)">
				<view class="flex justify-between"
					style="font-size: 30rpx;width: 100%;overflow: hidden;text-overflow: ellipsis;white-space:nowrap">
					<view style="color: #333333;">投诉消息</view>
					<view v-if="item.isSee == 0"
						style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
					</view>
				</view>
				<view style="color: #999999;font-size: 28rpx;margin-top: 10rpx;">{{ item.title }}</view>

				<view style="margin-top: 10rpx;color: #999999;font-size: 28rpx;text-align: right;">
					{{ item.createAt }}
				</view>
				<view v-if="list.length-1!=index"
					style="width: 100%;height: 1rpx;background: #f5f5f5;margin-top: 30rpx;"></view>

			</view>
		</view>

		<empty v-if="list.length === 0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="list.length>0" />
	</view>
</template>

<script setup>
	import empty from '@/components/empty/empty.vue'
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
	const tabList = reactive([{
			state: 6,
			name: '用户消息',
			totalElements: 0
		},
		{
			state: 2,
			name: '意见反馈',
			totalElements: 0
		}
	]);
	const current = ref(0);
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');

	onLoad(option => {
		loadData();
	});
	onShow(() => {

	});
	/* 同意 拒绝名片申请 */
	const clickEx = (ite, index) => { //1 同意  2 拒绝
		if (index == 1) {

			uni.showModal({
				title: '提示',
				content: '确认同意交换申请？',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						let userId = uni.getStorageSync('userId')
						proxy.$http.postJson('/app/message/updateMessage', {
							status: 2,
							id: ite.id
						}).then(res => {
							if (res.code === 0) {
								loadData();
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
			uni.showModal({
				title: '提示',
				content: '确认拒绝交换申请？',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						let userId = uni.getStorageSync('userId')
						proxy.$http.postJson('/app/message/updateMessage', {
							status: 3,
							id: ite.id
						}).then(res => {
							if (res.code === 0) {
								loadData();
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
		}
	};
	/* tab切换 */
	const tabClicks = (e) => {
		console.log(e)
		current.value = e.index
		page.value = 1
		loadData();

	};
	/* 消息列表 */
	const loadData = () => {
		let state = tabList[current.value].state
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId'),
			state: state
		}

		proxy.$http.getT('/app/message/getUserMessageInfoList', data).then(res => {
			if (res.code === 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records);
				}
				status.value = 'nomore';
			}
			status.value = 'nomore';
			uni.hideLoading();
			uni.stopPullDownRefresh();
		});

	};
	onPullDownRefresh(() => {
		page.value = page.value + 1
		loadData();
	});
	onReachBottom(() => {
		page.value = 1
		loadData();
	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.item {

		background: #FFFFFF;
		padding: 30rpx;
		border-radius: 24rpx;
	}

	.tybtn {
		background: #287AFE;
		color: #FFFFFF;
		padding: 6rpx 30rpx;
		border-radius: 55rpx;
		font-size: 26rpx;

	}

	.jjbtn {
		background: #c6c6c6;
		color: #FFFFFF;
		padding: 6rpx 30rpx;
		border-radius: 55rpx;
		font-size: 26rpx;
		margin-left: 20rpx;
	}
</style>