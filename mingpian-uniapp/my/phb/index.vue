<template>
	<view style="padding-bottom: 150rpx;">
		<!--自定义导航栏 2.0.19支持autoBack，默认为false -->
		<up-navbar :bgColor="bgColor" placeholder :border="false" :autoBack="true" title="名片榜单" leftIconColor="#FFFFFF"
			:titleStyle="titleStyle" safeAreaInsetTop>
		</up-navbar>


		<up-sticky offsetTop="0" bgColor="#FFFFFF" :customNavHeight="customNavHeight">
			<view class="Head">
				<view class="Headsx">
					<view class="HeadsxIte" @click="showTree = true">
						<text>{{titleHy}}</text>
						<up-icon name="arrow-down-fill" color="#FFFFFF" size="16"></up-icon>
					</view>
					<view class="HeadsxIte" @click="changeCity()">
						<text>{{titleQy}}</text>
						<up-icon name="arrow-down-fill" color="#FFFFFF" size="16"></up-icon>
					</view>

				</view>
				<view class="phbHead">
					<view>排名</view>
					<view>身份信息</view>
					<view>人气值</view>
				</view>

			</view>
		</up-sticky>


		<view class="">

			<view class="phbBox" v-for="(item,index) in list" :key="index" @click="goCardDetail(item)"
				:class="index==0?'huangBg':index==1?'lanBg':index==2?'hongBg':''">
				<view class="phbBoxIndex">
					<image src="/my/static/phb/one.png" v-if="index==0"></image>
					<image src="/my/static/phb/two.png" v-else-if="index==1"></image>
					<image src="/my/static/phb/three.png" v-else-if="index==2"></image>
					<text v-else>{{index+1}}</text>
				</view>
				<view class="phbBoxInfo">
					<image :src="item.card_avatar?item.card_avatar:'/static/logo.png'" style="border-radius: 55%;">
					</image>
					<view class="phbBoxInfo_cont">
						<view class="phbBoxInfo_contTitle">
							<text>{{item.real_name}}</text>
							<!-- 是否是会员(0不是 1是) -->
							<image src="/static/image/my/vipphb.png" v-if="item.isVip!=0"
								style="width: 60rpx;height: 30rpx;margin: 0 15rpx;">
							</image>
							<image src="/static/image/mp/qi.png" style="width: 27rpx;height: 30rpx;"
								v-if="item.card_type==2"></image>
						</view>
						<view class="phbBoxInfo_contTit padding-tb-xs">{{item.company_position}}</view>
						<view class="phbBoxInfo_contTit">{{item.company_name}}</view>
					</view>
				</view>
				<view class="phbBoxNum">{{item.popularity_value}}</view>
			</view>

			<empty v-if="list.length==0"></empty>
			<!-- 加载数据 -->
			<up-loadmore :status="status" v-if="list.length>0" />


			<DaCitySelector :visible="citySelectorVisible" themeColor="#287AFE" :multipleLimit="5"
				:show-unlimited-city="true" :cityValue="citySelectedValue" @confirm="handleConfirm" @close="handleClose"
				:headtop="customNavHeight">
			</DaCitySelector>
		</view>

		<!-- 行业 -->
		<up-picker :show="showTree" ref="uPickerRef" :loading="loading" :columns="columns" keyName="industryName"
			@change="changeHandler" @cancel="showTree = false" @confirm="confirmTree"></up-picker>


	</view>
</template>

<script setup>
	import DaCitySelector from '@/components/da-city-selector/index.vue'
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
	const bgColor = ref('linear-gradient(to right, #287AFE 0%, #62AEFF 100%)');
	const titleStyle = ref({
		'color': '#FFFFFF'
	});
	const titleHy = ref('不限行业');
	const titleQy = ref('不限区域')
	const valueHy = ref('1');
	const valueQy = ref('1');

	const list = reactive([]);
	const page = ref('1');
	const pages = ref('1');
	const limit = ref('10');
	const status = ref('loadmore');
	const customNavHeight = ref(0)

	const showTree = ref(false); //行业分类组件
	const columns = reactive([]); //行业列表
	const uPickerRef = ref(null)
	const columnData = reactive([]);
	const loading = ref(false);
	const industry = ref(''); //行业名称
	const industryFirstId = ref(''); //行业一级id 
	const industryFirstName = ref(''); //行业一级名称
	const industrySecondId = ref(''); //行业二级id 
	const industrySecondName = ref(''); //行业二级名称

	const citySelectorVisible = ref(false)
	const citySelectedValue = ref('');
	const cityId = ref('');
	const cityName = ref('');

	/* 跳转名片详情 */
	const goCardDetail = (item) => {
		if (uni.getStorageSync('token')) {
			let messageStatus = 0
			if (item.messageStatus) {
				messageStatus = item.messageStatus
			}
			if (item.card_id) {
				uni.navigateTo({
					url: '/my/Enterprise/businessCard?id=' + item.card_id + '&messageStatus=' + messageStatus
				})
			} else {
				uni.showToast({
					title: '暂无名片',
					icon: 'none'
				})
			}

		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	// 选择城市
	const handleConfirm = (val) => {
		console.log('handleConfirm ==>', val)
		cityId.value = val.id || ''
		cityName.value = val.name || ''
		titleQy.value = val.name || ''
		citySelectorVisible.value = false
		list.length = 0
		page.value = 1
		getlist()
	};
	// 关闭城市选择
	const handleClose = () => {
		citySelectorVisible.value = false
	};
	// 选择城市
	const changeCity = () => {
		citySelectedValue.value = cityId.value
		citySelectorVisible.value = true
	};


	// 选择行业分类
	const confirmTree = (e) => {
		console.log(e)
		industryFirstId.value = e.value[0].industryId //行业一级id 
		industryFirstName.value = e.value[0].industryName //行业一级名称
		industrySecondId.value = e.value[1].industryId //行业二级id 
		industrySecondName.value = e.value[1].industryName //行业二级名称
		if (industryFirstName.value == '不限行业') {
			titleHy.value = industryFirstName.value //行业名称
		} else {
			titleHy.value = industryFirstName.value + '-' + industrySecondName.value //行业名称
		}

		showTree.value = false
		list.length = 0
		page.value = 1
		getlist()
	};
	// 行业滑动切换二级分类
	const changeHandler = (e) => {
		console.log(e)
		const {
			columnIndex,
			index,
			picker
		} = e;
		if (columnIndex === 0) {
			loading.value = true;
			console.log(columnData)
			// 模拟网络请求
			setTimeout(() => {
				uPickerRef.value.setColumnValues(1, columnData[index]);
				loading.value = false;
			}, 500);
		}
	};
	// 获取行业分类
	const getTreeList = () => {
		proxy.$http.getT('/app/industry/getIndustryTreeList').then(res => {
			if (res.code === 0) {
				let arr = []
				let LevelOne = []
				let LeveTwo = []
				// 在开头添加"不限行业"默认选项
				LevelOne.push({
					industryId: '',
					industryName: '不限行业',
					childrenList: [{
						industryId: '',
						industryName: '不限行业'
					}]
				});


				// 获取所有一级分类
				res.data.forEach(d => {
					LevelOne.push(d)
				})
				if (LevelOne.length != 0) {
					// 获取默认展示的第一个二级分类
					if (LevelOne[0].childrenList.length != 0) {
						LevelOne[0].childrenList.forEach(d => {
							LeveTwo.push(d)
						})
					}
					// 获取所有的二级分类  用于滑动切换使用
					LevelOne.forEach(d => {
						columnData.push(d.childrenList)
					})
				}
				// console.log(columnData)
				arr.push(LevelOne, LeveTwo)
				columns.splice(0, columns.length, ...arr);
			}
		});
	};
	const change = (e) => {
		console.log(e)
		// 更多的细节，如有需要请自行根据业务逻辑进行处理
		// this.$refs.uDropdownRef.highlight(xxx);
	};
	// 获取排行榜列表
	const getlist = () => {
		let data = {
			pageNum: page.value,
			pageSize: limit.value,
			industrySecondName: industrySecondName.value == '不限行业' ? '' : industrySecondName.value,
			companyCity: titleQy.value == '不限区域' ? '' : titleQy.value,
			userId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
		}
		proxy.$http.postJson("/app/businessCard/hotList", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.list);
				} else {
					list.push(...res.data.list);
				}
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh();
		});
	};
	onLoad(option => {
		// #ifdef H5
		customNavHeight.value = '44'
		// #endif
		// #ifndef H5

		customNavHeight.value = uni.getSystemInfoSync().statusBarHeight + 44
		console.log(customNavHeight.value)
		// #endif
		getlist()
		getTreeList()
	});
	onShow(() => {

	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getlist();
		}
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getlist();
	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	::v-deep .u-dropdown__menu__item__text {
		color: #FFFFFF !important;
	}

	::v-deep .u-dropdown__menu {
		margin-right: 200rpx !important;
	}

	.Head {

		// background: linear-gradient(33deg, #287AFE 0%, #62AEFF 100%);
		background-image: url('https://mydz.xianmxkj.com/file/uploadPath/2026/01/21/7747772778ce25d28f5763abbb35d4fc.png');
		background-size: 100%;
		background-repeat: no-repeat;
		padding: 15rpx 0;

		.Headsx {

			display: flex;
			align-items: center;
			height: 126rpx;
			padding-left: 50rpx;

			.HeadsxIte {
				display: flex;
				margin-right: 100rpx;

				text {
					font-weight: 500;
					font-size: 26rpx;
					color: #FFFFFF;
					margin-right: 6rpx;
				}
			}
		}
	}

	.phbHead {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx 30rpx;
		background: #FFFFFF;

		font-weight: 500;
		font-size: 26rpx;
		color: #333333;

	}

	.huangBg {
		background: linear-gradient(94deg, #FFEED0 0%, #FFFBF6 100%) !important;
	}

	.lanBg {
		background: linear-gradient(94deg, #D7E4FA 0%, #EDF4FF 100%) !important;
	}

	.hongBg {
		background: linear-gradient(94deg, #FBD7D9 0%, #FAF1F3 100%) !important;
	}

	.phbBox {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;
		background: #FAFAFA;
		margin: 0 30rpx 20rpx;
		border-radius: 20rpx;

		.phbBoxIndex {
			width: 51rpx;
			text-align: center;

			font-weight: bold;
			font-size: 36rpx;
			color: #333333;

			image {
				width: 51rpx;
				height: 58rpx;
			}
		}

		.phbBoxInfo {
			display: flex;
			align-items: center;
			flex: 1;
			margin-left: 50rpx;

			image {
				width: 93rpx;
				height: 93rpx;
			}

			.phbBoxInfo_cont {
				margin-left: 20rpx;


				.phbBoxInfo_contTitle {
					display: flex;
					align-items: center;

					text {

						font-weight: bold;
						font-size: 32rpx;
						color: #333333;
					}
				}

				.phbBoxInfo_contTit {

					font-weight: 500;
					font-size: 24rpx;
					color: #666666;
				}

			}
		}

		.phbBoxNum {

			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}
	}
</style>