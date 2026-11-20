<template>
	<view class="city-selector-wrap" v-if="visibleRef" :style="{'--theme-color': themeColor}">
		
		<view class="city-selector-box" :class="visibleAni ? 'is-show' : ''" :style="{ top: headtop + 'px' }">
			<view id="city-selector-filter-box" class="city-selector-filter-box" @touchmove.stop.prevent="handleMove">
				<view class="city-selector-search-box">
					<view class="city-selector-search-input"><input type="text" class="city-selector-search-input-inner"
							:placeholder="placeholder" @input="bindFilterKeyword" :value="filterKeyword"
							placeholder-class="-p" maxlength="20" /></view>
				</view>
				<!-- #ifndef H5 -->
				<view class="city-selector-local-box" v-if="showLocate">
					<view class="city-selector-local-info">
						<text class="city-selector-local-info-label">当前:</text>
						<text class="city-selector-local-info-cont">{{ localCity.cityName || '定位失败,重新定位' }}</text>
					</view>
					<view class="city-selector-local-button" @click="handleRepositioning()">
						<text class="city-selector-local-button-icon"></text>
						<text class="city-selector-local-button-text">重新定位</text>
					</view>
				</view>
				<!-- #endif -->
			</view>
			<view class="city-selector-list-box" v-if="cityPageData.length > 0">
				<scroll-view class="city-selector-list-scroll" scroll-y :scroll-top="cityPageNextScrollTop"
					scroll-with-animation @scroll="handlePageDataScroll">
					<view class="city-selector-list-inner">
						<!-- 添加不限城市选项 -->
						<view class="city-selector-list-part">
							<view class="city-selector-list-part-title" :class="{'is-show': filterKeyword == ''}">常用
							</view>
							<view class="city-selector-list-part-content">
								<view class="city-selector-list-part-item"
									:class="{'is-actived': unlimitedCity.isActived}" @click="handleUnlimitedCity">
									不限区域
								</view>
							</view>
						</view>

						<view class="city-selector-list-part" v-for="(item,pindex) in cityPageData"
							:key="item.first_char">
							<view class="city-selector-list-part-title" :class="{'is-show': filterKeyword == ''}"
								:id="'city-selector-index-'+item.first_char_key">{{ item.first_char }}</view>
							<view class="city-selector-list-part-content">
								<view class="city-selector-list-part-item"
									:class="{'is-actived': city.isActived,'is-hidden': city.show === false}"
									v-for="(city,cindex) in item.children" :key="city.id"
									@click="handleItem(city,cindex,pindex)">{{ city.name }}</view>
							</view>
						</view>
					</view>
				</scroll-view>
				<!-- 修改ABC索引 -->
				<view class="city-selector-abc-box" :class="{'is-show': filterKeyword == ''}"
					@touchmove.stop.prevent="handleMove">
					<view class="city-selector-abc-list">
						<!-- 添加不限城市索引 -->
						<view class="city-selector-abc-item" @click="tapUnlimitedCity">★</view>
						<view class="city-selector-abc-item" v-for="(item,index) in indexABC" :key="index"
							@click="tapABC(item,index)">{{ item.key }}</view>
					</view>
				</view>
				<view class="city-selector-abc-indicator" v-if="indexTips">{{ indexTips }}</view>
			</view>
			<view class="city-selector-list-box" v-else @touchmove.stop.prevent="handleMove">
				<view class="city-selector-list-box--none"> -- 暂无数据 -- </view>
			</view>
			<view class="city-selector-selected" v-if="selectedData.length > 0 && multiple">
				<view class="city-selector-selected-inner">
					<view class="city-selector-selected-title">已选</view>
					<scroll-view class="city-selector-selected-scroll" scroll-x scroll-with-animation
						:scroll-left="citySelectedScrollLeft">
						<view class="city-selector-selected-tag"
							:id="index === selectedData.length-1 ? 'city-selector-selected-last-tag' : ''"
							v-for="(item,index) in selectedData" :key="index" @click="handleDeleteSelect(item,index)">
							<view class="city-selector-selected-tag-inner">{{ item.name }}</view>
						</view>
					</scroll-view>
				</view>
				<view class="city-selector-selected-button" @click="handleSave()"><text>保存</text><text
						v-if="multipleLimit > 0"
						@touchmove.stop.prevent="handleMove">({{ selectedData.length || 0 }}/{{ multipleLimit }})</text>
				</view>
			</view>
			<view class="city-selector-close" @click="handleClose()" @touchmove.stop.prevent="handleMove"></view>
		</view>
	</view>
</template>
<script >
	import {
		defineComponent,
		ref,
		reactive, // 添加 reactive
		watch,
		unref,
		nextTick,
	} from 'vue'

	import {
		dt_city
	} from './city_data'
	import {
		createCityData,
		createCityABC,
		showModal,
		getLocalionInfo
	} from './utils'
	import {
		deepClone
	} from './utils'

	export default defineComponent({
		name: 'DaCitySelector',
		props: {
			/* 顶部距离 */
			headtop:{
				type: Number,
				default: 0,
			},
			/**
			 * 是否显示不限城市选项
			 */
			showUnlimitedCity: {
				type: Boolean,
				default: false,
			},
			/**
			 * 是否显示
			 */
			visible: {
				type: Boolean,
				default: false,
			},
			/**
			 * 城市数据，如果为空则使用组件自带的城市数据
			 * 数据格式为：[{first_char: '字母',[{id: '城市Code',name: '城市名称',sname: '城市缩写',pinyin: '城市名称拼音'}]}]
			 */
			cityData: {
				type: Array,
			},
			/**
			 * 默认已选数据
			 */
			cityValue: {
				type: [String, Number, Array],
			},
			/**
			 * 是否多选
			 */
			multiple: {
				type: Boolean,
				default: false,
			},
			/**
			 * 多选个数限制，0为不限制
			 */
			multipleLimit: {
				type: Number,
				default: 0,
			},
			/**
			 * 主题色
			 */
			themeColor: {
				type: String,
				default: '#17B2B4',
			},
			/**
			 * 搜索提示文本
			 */
			placeholder: {
				type: String,
				default: '请输入城市名称/拼音',
			},
			/**
			 * 是否显示定位城市，不支持H5
			 */
			showLocate: {
				type: Boolean,
				default: true,
			},
			/**
			 * 地图key
			 * key申请地址：https://lbs.qq.com/dev/console/key/add
			 * 如有不懂可看地图指南：https://lbs.qq.com/service/webService/webServiceGuide/webServiceOverview
			 * 通常 showLocate 为 true 时用的到，主要用来转换经纬度为城市信息
			 */
			mapKey: {
				type: String,
				default: 'TFTBZ-J5IRQ-OME5C-2RQW6-A3UQF-TTFSS',
			},
		},
		emits: ['close', 'confirm'],
		setup(props, {
			emit
		}) {
			const visibleRef = ref(false)
			const visibleAni = ref(false)

			const cityPageData = ref([])
			const cityPageHegith = ref(0)
			const cityPageDeltaY = ref(0)
			const cityPageNextScrollTop = ref(0)
			const cityPageItemHegith = ref(0)
			const indexTips = ref('')
			const indexABC = ref([])
			const indexABCSize = ref(0)
			const indexActive = ref(0)
			const filterBoxHeight = ref(0)
			const citySelectedScrollLen = ref(0)
			const citySelectedScrollLeft = ref(0)

			const filterKeyword = ref('') // 搜索值
			const selectedData = ref([]) // 已选项
			// const localCity = ref('') // 当前城市
			const localCity = reactive({ // 添加这行
				cityName: '',
				cityCode: '',
				province: '',
				district: ''
			})
			// 添加不限城市对象
			const unlimitedCity = ref({
				id: '',
				name: '不限区域',
				isActived: false,
			})

			/**
			 * 点击不限城市
			 */
			function handleUnlimitedCity() {
				if (!props.multiple) {
					// 单选模式下，直接选中不限城市
					unlimitedCity.value.isActived = true
					emit('confirm', unlimitedCity.value)
					return
				}

				const hasIndex = selectedData.value.findIndex(k => k.id === '')
				if (hasIndex > -1) {
					unlimitedCity.value.isActived = false
					selectedData.value.splice(hasIndex, 1)
				} else {
					// 多选模式下，选中不限城市时清空其他选择
					unlimitedCity.value.isActived = true
					selectedData.value = [unlimitedCity.value]

					// 清空其他城市的选择状态
					cityPageData.value.forEach(k => {
						if (k.children?.length) {
							k.children.forEach(city => {
								city.isActived = false
							})
						}
					})
				}

				checkSelectedTags(this)
			}

			/**
			 * 点击字母索引中的不限城市
			 */
			function tapUnlimitedCity() {
				// 滚动到顶部
				cityPageNextScrollTop.value = 0
				activeABC('★', 0)
			}

			/**
			 * 初始化已选数据
			 */
			function initData() {
				if (props.cityData?.length) {
					cityPageData.value = deepClone(props.cityData)
				} else {
					cityPageData.value = createCityData(dt_city)
				}

				cityPageData.value = checkSelectedData(cityPageData.value)

				indexABC.value = createCityABC(cityPageData.value)
				indexABCSize.value = indexABC.value.reduce((total, item) => {
					return total + item.size
				}, 0)

				if (props.showLocate) {
					if (props.mapKey) {
						checkGPSService()
					} else {
						// #ifndef H5
						showModal('地图Key不存在，请填写 mapKey 字段')
						// #endif
					}
				}
			}

			/**
			 * 检查已选项（修改部分）
			 */
			function checkSelectedData(data) {
				if (!props.cityValue && props.cityValue !== 0) {
					// 检查是否有不限城市的选中状态
					if (selectedData.value.some(item => item.id === '')) {
						unlimitedCity.value.isActived = true
					}
					return data
				}

				if (!Array.isArray(data) || data.length === 0) {
					return []
				}

				let cv
				if (props.multiple) {
					cv = Array.isArray(props.cityValue) ? props.cityValue : []

					if (cv.length === 0) {
						return data
					}

					selectedData.value = []
					if (cv.length > props.multipleLimit) {
						cv = cv.slice(0, props.multipleLimit)
					}
				} else {
					cv = Array.isArray(props.cityValue) ? props.cityValue[0] : props.cityValue
				}

				// 检查是否包含不限城市
				if (cv === '' || (Array.isArray(cv) && cv.includes(''))) {
					unlimitedCity.value.isActived = true
					if (props.multiple) {
						selectedData.value.push(unlimitedCity.value)
					}
				}
				// 检查其他城市选择
				for (let i = 0; i < data.length; i++) {
					const item = data[i]
					if (!item.children || item.children.length === 0) {
						continue
					}

					for (let i = 0; i < item.children.length; i++) {
						const city = item.children[i]
						if (props.multiple) {
							if (cv.includes(city.id)) {
								selectedData.value.push(city)
								city.isActived = true
							}
						} else {
							if (cv === city.id) {
								city.isActived = true
							}
						}
					}
				}

				return data
			}

			function getCSQI(self) {
				if (self) {
					return uni.createSelectorQuery().in(self)
				}
				return null
			}

			/**
			 * 点击关闭
			 */
			function handleClose() {
				visibleRef.value = false
				selectedData.value = []
				filterKeyword.value = ''
				// localCity.value = ''

				if (cityPageData.value?.length) {
					cityPageData.value.forEach(k => {
						if (k.children?.length) {
							k.children.forEach(ck => {
								ck.isActived = false
								ck.show = true
							})
						}
					})
				}

				emit('close', null)
			}

			/**
			 * 清理滚动
			 */
			function handleMove() {
				return false
			}

			/**
			 * 输入框筛选
			 * @param e
			 */
			function bindFilterKeyword(e) {
				const {
					value
				} = e.detail
				filterKeyword.value = value

				cityPageData.value.forEach(k => {
					filterListData(value, k)
				})
			}

			/**
			 * 过滤数据列表
			 * @param key  输入内容
			 * @param data 当前列表项
			 */
			function filterListData(key, item) {
				if (item.children?.length) {
					item.children.forEach(k => {
						if (!k.py) {
							k.py = ''
							k.pinyinArr = k.pinyin?.split(' ')
							if (k.pinyinArr?.length) {
								k.pinyinArr.forEach(pa => {
									k.py += pa[0]
								})
							}
						}
						if (k.name.indexOf(key) > -1 || k.pinyin.indexOf(key) === 0 || k.py.indexOf(
								key) === 0) {
							k.show = true
						} else {
							k.show = false
						}
					})
					if (item.children.some(k => k.show)) {
						item.show = true
					} else {
						item.show = false
					}
				}
			}

			/**
			 * 获取头部盒子元素
			 */
			function getFilterHeight(self = undefined) {
				try {
					getCSQI(self).select('#city-selector-filter-box').boundingClientRect(rect => {
						filterBoxHeight.value = rect?.height || 0
					}).exec()
				} catch (e) {
					console.warn('DaCitySelector: get filter height fail.', e)
				}
			}

			/**
			 * 列表滚动回调
			 * @param e
			 */
			function handlePageDataScroll(e) {
				if (e.detail.scrollHeight > cityPageHegith.value) {
					cityPageHegith.value = e.detail.scrollHeight || 0
				}
				if (cityPageItemHegith.value <= 0) {
					cityPageItemHegith.value = Math.floor(cityPageHegith.value / indexABCSize.value)
				}
				const eds = e.detail.scrollTop || 0
				cityPageDeltaY.value = eds
			}

			/**
			 * 点击字母
			 */
			function tapABC(item, index) {
				if (filterBoxHeight.value <= 0) {
					getFilterHeight(this)
				}

				const indexId = `#city-selector-index-${item.key?.toLocaleLowerCase()}`
				try {
					getCSQI(this).select(indexId).boundingClientRect(rect => {
						if (rect === null) return false

						cityPageNextScrollTop.value = cityPageDeltaY.value + rect.top - filterBoxHeight
							.value

						activeABC(item.key, index)
					}).exec()
				} catch (e) {
					console.warn('DaCitySelector: check abc list fail.', e)
				}
			}

			/**
			 * 激活当前字母
			 * @param key
			 * @param index
			 */
			function activeABC(key, index) {
				indexActive.value = index
				indexTips.value = key === 'hot' ? '★' : key
				setTimeout(() => {
					indexTips.value = ''
				}, 12000)
			}

			/**
			 * 点击当前列表项
			 */
			function handleItem(item, _currentIndex, _parentIndex) {
				console.log('点击城市:', item.name, item); // 添加调试信息

				// 如果是单选模式
				if (!props.multiple) {
					// 先重置所有选择状态
					unlimitedCity.value.isActived = false;

					// 重置所有城市的选择状态
					cityPageData.value.forEach(k => {
						if (k.children?.length) {
							k.children.forEach(city => {
								city.isActived = false;
							});
						}
					});

					// 设置当前选中项
					item.isActived = true;

					// 触发确认事件
					emit('confirm', item);
					return;
				}

				// 多选模式
				const hasIndex = selectedData.value.findIndex(k => k.id === item.id);

				if (hasIndex > -1) {
					// 如果已选中，则取消选择
					item.isActived = false;
					selectedData.value.splice(hasIndex, 1);
				} else {
					// 如果未选中，则选择
					if (props.multipleLimit > 0 && selectedData.value.length >= props.multipleLimit) {
						uni.showToast({
							title: `限制选择${props.multipleLimit}项`,
							duration: 1200,
							icon: 'none'
						});
						return;
					}

					item.isActived = true;
					selectedData.value.push(item);
				}

				// 更新已选项显示
				checkSelectedTags(this);

				// 调试输出
				console.log('已选项:', selectedData.value);
			}
			/**
			 * 删除已选项
			 */
			function handleDeleteSelect(item, index) {
				for (let i = 0; i < cityPageData.value.length; i++) {
					const k = cityPageData.value[i]
					if (k.children.length) {
						for (let j = 0; j < k.children.length; j++) {
							const c = k.children[j]
							if (item.id === c.id) {
								c.isActived = false
								break
							}
						}
					}
				}
				selectedData.value.splice(index, 1)

				checkSelectedTags(this)
			}

			/**
			 * 删除已选项（修改部分）
			 */
			function handleDeleteSelect(item, index) {
				if (item.id === '') {
					// 删除不限城市
					unlimitedCity.value.isActived = false
				} else {
					for (let i = 0; i < cityPageData.value.length; i++) {
						const k = cityPageData.value[i]
						if (k.children.length) {
							for (let j = 0; j < k.children.length; j++) {
								const c = k.children[j]
								if (item.id === c.id) {
									c.isActived = false
									break
								}
							}
						}
					}
				}
				selectedData.value.splice(index, 1)

				checkSelectedTags(this)
			}

			/**
			 * 保存已选
			 */
			function handleSave() {
				const res = selectedData.value.map(k => {
					return {
						id: k.id,
						name: k.name,
					}
				})
				emit('confirm', res)
			}
			/**
			 * 点击关闭（修改部分）
			 */
			function handleClose() {
				visibleRef.value = false
				selectedData.value = []
				filterKeyword.value = ''
				unlimitedCity.value.isActived = false // 重置不限城市状态
				// localCity.value = ''

				if (cityPageData.value?.length) {
					cityPageData.value.forEach(k => {
						if (k.children?.length) {
							k.children.forEach(ck => {
								ck.isActived = false
								ck.show = true
							})
						}
					})
				}

				emit('close', null)
			}
			/**
			 * 判断GPS是否开启，目前只有微信小程序支持
			 */
			function checkGPSService() {
				// #ifdef MP-WEIXIN
				uni.getLocation({
					type: 'wgs84',
					success: function(res) {
						console.log('当前位置的经度：' + res.longitude);
						console.log('当前位置的纬度：' + res.latitude);
						uni.request({
							url: 'https://mpfx.xianmaxiong.com/sqx_fast/app/Login/selectCity?lat=' +
								res.latitude + '&lng=' + res.longitude, //仅为示例，并非真实接口地址。
							header: {
								// 'custom-header': 'hello' ,//自定义请求头信息
								'header': "application/x-www-form-urlencoded"
							},
							success: (res) => {
								console.log(res.data, '//////');
								localCity.cityName = res.data.data.city; // 直接赋值，不需要 .value
								console.log(localCity)
							}
						});

					},
					complete(ret) {
						console.log(ret)
					}
				});

				// uni.getSystemInfo({
				// 	success(res) {
				// 		const locationEnabled = res.locationEnabled
				// 		const locationAuthorized = res.locationAuthorized
				// 		// 判断手机是否开启定位服务、是否授权微信定位服务
				// 		if (locationEnabled === false || locationAuthorized === false) {
				// 			showModal('请打开定位服务功能')
				// 		} else {
				// 			checkMPLocation()
				// 		}
				// 	},
				// 	fail(err) {
				// 		console.error('DaCitySelector: getSystemInfo fail', err)
				// 		showModal('获取位置信息失败')
				// 	},
				// })
				// #endif

				// #ifndef MP-WEIXIN | H5
				checkMPLocation()
				// #endif
			}

			/**
			 * 判断小程序是否授权位置信息
			 */
			function checkMPLocation() {
				uni.getSetting({
					success(res) {
						const scopeUserLocation = res.authSetting['scope.userLocation']
						if (scopeUserLocation) {
							// 已授权位置信息
							getCityInfo()
						} else {
							showModal('未授权位置信息')
						}
					},
					fail(err) {
						console.error('DaCitySelector: getSetting fail', err)
						showModal('获取位置信息失败')
					},
				})
			}

			/**
			 * 获取当前位置
			 */
			function getCityInfo() {
				if (!props.mapKey) {
					console.error('DaCitySelector: mapKey cannot be empty')
					return
				}

				getLocalionInfo(props.mapKey).then(res => {
						// localCity.value = res
						Object.assign(localCity, res)
					})
					.catch(() => {
						showModal('获取位置信息失败')
					})
			}

			/**
			 * 重新定位
			 */
			function handleRepositioning() {
				if (!props.showLocate) return

				// #ifdef H5
				showModal('不支持网页获取位置信息')
				// #endif

				checkGPSService()
			}

			watch(() => props.visible,
				(v) => {
					visibleRef.value = v
				})

			watch(() => unref(visibleRef),
				(v) => {
					setTimeout(() => {
						visibleAni.value = v

						if (v) {
							nextTick(() => {
								initData()
							})
						}
					}, 150)
				})

			return {
				visibleRef,
				visibleAni,

				localCity,
				cityPageData,
				cityPageNextScrollTop,
				indexTips,
				indexABC,
				indexActive,
				handleRepositioning,
				tapABC,
				filterKeyword,
				bindFilterKeyword,
				citySelectedScrollLeft,
				selectedData,
				handlePageDataScroll,
				handleItem,
				handleDeleteSelect,
				handleSave,
				handleClose,
				handleMove,

				unlimitedCity,
				handleUnlimitedCity,
				tapUnlimitedCity,
			}
		},
	})
</script>

<style lang="scss" scoped>
	@font-face {
		font-family: 'da-city-selector-iconfont';
		/* Project id  */
		src: url('data:application/octet-stream;base64,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') format('truetype');
	}

	.icon-guanbi::before {
		content: '\e607';
	}

	.icon-fanhui1::before {
		content: '\e6ff';
	}

	.icon-dingwei::before {
		content: '\e627';
	}

	.icon-sousuo::before {
		content: '\e7d2';
	}

	.city-selector {
		&-wrap {
			position: relative;
		}

		&-box {
			position: fixed;
			top: var(--window-top, 0px);
			left: 0;
			z-index: 999;
			display: flex;
			flex-direction: column;
			width: 100vw;
			height: calc(100vh - var(--window-top, 0px));
			padding-bottom: var(--window-bottom, 0px);
			overflow: hidden;
			line-height: 1;
			visibility: hidden;
			background: #f4f4f4;
			opacity: 0;
			transition: all 0.15s ease-in-out;
			transform: translateY(30%);

			&.is-show {
				visibility: visible;
				opacity: 1;
				transform: translateY(0);
			}
		}

		&-filter {
			&-box {
				flex-shrink: 0;
				width: 100%;
				overflow: hidden;
			}
		}

		&-search {
			&-box {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: center;
				height: 100rpx;
				padding: 0 24rpx;
				margin: auto;
				margin-left: 64rpx;
				white-space: nowrap;
				background: #f5f5f5;
			}

			&-input {
				display: flex;
				flex-grow: 1;
				align-items: center;
				height: 72rpx;
				padding: 0 20rpx;
				overflow: hidden;
				background: #fff;
				border-radius: 16rpx;

				&::before {
					margin-right: 10rpx;
					/* stylelint-disable-next-line font-family-no-missing-generic-family-keyword */
					font-family: 'da-city-selector-iconfont' !important;
					font-size: 30rpx;
					font-style: normal;
					color: #999;
					content: '\e7d2';
					-webkit-font-smoothing: antialiased;
					-moz-osx-font-smoothing: grayscale;
				}

				&-inner {
					width: 100%;
					height: 100%;
					padding: 0;
					margin: 0;
					font-size: 28rpx;
					letter-spacing: 2rpx;
					border: none;

					.-p,
					&.-p {
						color: #999;
					}

					&::after {
						display: none;
					}
				}
			}
		}

		&-local {
			&-box {
				display: flex;
				align-items: center;
				padding: 30rpx;
				margin-bottom: 16rpx;
				background: #fff;
			}

			&-info {
				display: flex;
				flex-grow: 1;
				align-items: center;
				font-size: 30rpx;

				&-label {
					flex-shrink: 0;
					margin-right: 6rpx;
				}

				&-content {
					flex-grow: 1;
				}
			}

			&-button {
				display: flex;
				flex-shrink: 0;
				align-items: center;
				white-space: nowrap;

				&-icon {
					&::after {
						position: relative;
						z-index: 1;
						margin-left: 10rpx;
						/* stylelint-disable-next-line font-family-no-missing-generic-family-keyword */
						font-family: 'da-city-selector-iconfont' !important;
						font-size: 30rpx;
						font-style: normal;
						color: var(--theme-color);
						content: '\e627';
						-webkit-font-smoothing: antialiased;
						-moz-osx-font-smoothing: grayscale;
					}
				}

				&-text {
					margin-left: 6rpx;
					font-size: 30rpx;
				}
			}
		}

		&-list {
			&-box {
				position: relative;
				width: 100%;
				height: 100%;
				overflow: hidden;
				background: #fff;

				&--none {
					display: flex;
					align-items: center;
					justify-content: center;
					width: 100%;
					height: 100%;
					font-size: 12px;
					color: #999;
				}
			}

			&-scroll {
				position: relative;
				z-index: 1;
				width: 100%;
				height: 100%;
			}

			&-inner {
				position: relative;
			}

			&-part {
				--part-item-height: 80rpx;

				&-title {
					display: none;
					align-items: center;
					height: var(--part-item-height);
					padding: 0 30rpx;
					margin: auto;
					font-size: 36rpx;
					font-weight: bold;
					color: #232323;
					text-transform: uppercase;

					&.is-show {
						display: flex;
					}
				}

				&-content {
					position: relative;
					padding: 0 80rpx 0 30rpx;
				}

				&-item {
					position: relative;
					box-sizing: border-box;
					display: flex;
					align-items: center;
					width: 100%;
					height: var(--part-item-height);
					overflow: hidden;
					font-size: 28rpx;
					color: #232323;
					background: #fff;
					border-bottom: 1rpx solid #eee;

					&.is-actived {
						color: var(--theme-color);
					}

					&.is-hidden {
						display: none;
					}
				}
			}
		}

		&-abc {
			&-box {
				display: none;

				&.is-show {
					display: block;
				}
			}

			&-list {
				position: absolute;
				top: 50%;
				right: 2rpx;
				z-index: 3;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				width: 60rpx;
				padding: 0;
				margin: 0;
				overflow: hidden;
				color: #232323;
				transform: translateY(-50%);
			}

			&-item {
				display: flex;
				align-items: center;
				justify-content: center;
				padding: 4rpx 0;
				font-size: 24rpx;
				color: var(--theme-color);
				user-select: none;
			}

			&-indicator {
				position: fixed;
				top: calc(50% - 50rpx);
				left: calc(50% - 50rpx);
				z-index: 1003;
				width: 100rpx;
				height: 100rpx;
				font-size: 48rpx;
				line-height: 100rpx;
				color: #fff;
				text-align: center;
				background-color: var(--theme-color);
				border-radius: 10rpx;
				transition: all 0.15s;
			}
		}

		&-selected {
			position: absolute;
			right: 0;
			bottom: 0;
			left: 0;
			z-index: 3;
			padding: 30rpx 40rpx;
			background: #fff;

			&-inner {
				display: flex;
				align-items: center;
				width: 100%;
				overflow: hidden;
			}

			&-title {
				flex-shrink: 0;
				padding-right: 30rpx;
				margin-right: 30rpx;
				white-space: nowrap;
				border-right: 1px solid #eee;
			}

			&-scroll {
				flex-grow: 1;
				width: calc(100% - 126rpx);
				overflow: hidden;
				white-space: nowrap;
			}

			&-tag {
				position: relative;
				display: inline-flex;
				align-items: center;
				justify-content: center;
				padding: 16rpx 32rpx;
				margin-right: 12rpx;
				overflow: hidden;
				font-size: 24rpx;
				white-space: nowrap;
				border-radius: 64rpx;

				&-inner {
					position: relative;
					z-index: 1;
					display: flex;
					align-items: center;
					color: var(--theme-color);
				}

				&::before {
					position: absolute;
					top: 0;
					right: 0;
					bottom: 0;
					left: 0;
					z-index: 0;
					content: '';
					background: var(--theme-color);
					opacity: 0.1;
				}

				&::after {
					position: relative;
					z-index: 1;
					margin-left: 10rpx;
					/* stylelint-disable-next-line font-family-no-missing-generic-family-keyword */
					font-family: 'da-city-selector-iconfont' !important;
					font-size: 24rpx;
					font-style: normal;
					color: var(--theme-color);
					content: '\e607';
					-webkit-font-smoothing: antialiased;
					-moz-osx-font-smoothing: grayscale;
					transform: scale(0.8);
				}
			}

			&-button {
				display: flex;
				flex-shrink: 0;
				align-items: center;
				justify-content: center;
				width: 100%;
				height: 80rpx;
				margin-top: 30rpx;
				overflow: hidden;
				font-size: 28rpx;
				color: #fff;
				text-align: center;
				white-space: nowrap;
				background: var(--theme-color);
				border-radius: 16rpx;

				&:hover {
					opacity: 0.8;
				}
			}
		}

		&-close {
			position: absolute;
			top: 30rpx;
			left: 20rpx;
			z-index: 3;
			width: 40rpx;
			height: 40rpx;

			&::after {
				/* stylelint-disable-next-line font-family-no-missing-generic-family-keyword */
				font-family: 'da-city-selector-iconfont' !important;
				font-size: 40rpx;
				font-style: normal;
				color: #999;
				content: '\e6ff';
				-webkit-font-smoothing: antialiased;
				-moz-osx-font-smoothing: grayscale;
			}
		}

		&-abc {
			&-list {
				// 调整位置以容纳不限城市
				right: 2rpx;
				width: 60rpx;
			}

			&-item {

				// 不限城市的样式
				&:first-child {
					font-weight: bold;
					color: #FF9900; // 可以使用不同的颜色标识
				}
			}
		}

	}
</style>