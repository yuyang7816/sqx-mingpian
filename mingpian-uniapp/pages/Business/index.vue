<template>
	<view>
		<up-sticky bgColor="#FFFFFF">
			<view class="Head">
				<view class="HeadTop">
					<up-search placeholder="搜索用户名称 " v-model="keyword" :showAction="false" bgColor="#F5F5F5"
						@change="changeInput"></up-search>
					<view class="HeadTop_msg" @click="goNav('/my/message/index')">
						<image src="/static/image/mp/msg.png"></image>
					</view>
				</view>
				<view class="margin-top-xs">
					<up-tabs :list="tablist" @click="clickTab" :current="current" lineColor="#287AFE"
						:activeStyle="activeStyle" :inactiveStyle="inactiveStyle" :scrollable="false"></up-tabs>
				</view>

			</view>
		</up-sticky>
		<view class="" v-if="current!=3">

			<view class="listbox">
				<view class="listbox_Item" v-for="(item,index) in list" :key="index" @click.stop="goDetail(item)">
					<image :src="item.cardAvatar?item.cardAvatar:'/static/logo.png'" mode="aspectFill"
						style="border-radius: 55%;"></image>
					<view class="listbox_Item_cot">
						<view class="listbox_Item_cotent flex-sub">
							<view class="flex align-center justify-between">
								<view class="listbox_Item_cotentTitle">{{item.cardRemark?item.cardRemark:item.realName}}
								</view>
								<view class="" v-if="item.contactCount&&current==2"
									style="color: #999999;font-size: 26rpx;">{{item.contactCount}}次
								</view>
							</view>
							<view class="listbox_Item_cotentTit">
								<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
								{{item.cardPosition}}
							</view>
							<view class="listbox_Item_cotentTit">
								<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
								{{item.cardCompany}}
							</view>
						</view>
						<view class="" @click.stop="openPopup(item,index)">
							<image src="/static/image/mp/more.png" style="width: 28rpx;height: 6rpx;"></image>
						</view>
					</view>

					<view class="popup" v-if="item.check">
						<view class="popupNote">
							<view class="triangle"></view>
							<view class="filter"></view>
							<!-- <view class="popupNoteItem">分享</view> -->
							<view class="popupNoteItem" @click.stop="clickHc(item)" v-if="item.cardType==2">AI核查</view>
							<view class="popupNoteItem" @click.stop="deteCard(item)">删除</view>
							<view class="popupNoteItem" @click.stop="show= true">分组</view>
							<view class="popupNoteItem" style="border-bottom: none;" @click.stop="bindRemk(item)">重命名
							</view>
						</view>
					</view>
				</view>
			</view>

			<empty v-if="list.length==0"></empty>
			<!-- 加载数据 -->
			<up-loadmore :status="status" v-if="list.length>0" />
		</view>
		<view class="" v-else>

			<view v-for="(ite,ind) in groupList" :key="ind">
				<view class="boxItem" @click="bindTap(ite,ind)">
					<view>{{ite.groupsName}}</view>

					<view>
						<up-icon name="arrow-up" color="#999999" size="20" v-if="ite.isTrue"></up-icon>
						<up-icon name="arrow-down" color="#999999" size="20" v-else></up-icon>
					</view>
				</view>
				<view class="listbox_Item" v-if="ite.isTrue" v-for="(item,index) in list" :key="index"
					@click.stop="goDetail(item)">
					<image :src="item.cardAvatar?item.cardAvatar:'/static/logo.png'" mode="aspectFill"
						style="border-radius: 55%;"></image>
					<view class="listbox_Item_cot">
						<view class="listbox_Item_cotent">
							<view class="listbox_Item_cotentTitle">{{item.cardRemark?item.cardRemark:item.realName}}
							</view>
							<view class="listbox_Item_cotentTit">
								<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
								{{item.cardPosition}}
							</view>
							<view class="listbox_Item_cotentTit">
								<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
								{{item.cardCompany}}
							</view>
						</view>
						<view class="" @click.stop="openPopup(item,index)">
							<image src="/static/image/mp/more.png" style="width: 28rpx;height: 6rpx;"></image>
						</view>
					</view>

					<view class="popup" v-if="item.check">
						<view class="popupNote">
							<view class="triangle"></view>
							<view class="filter"></view>
							<!-- <view class="popupNoteItem">分享</view> -->
							<view class="popupNoteItem" @click.stop="clickHc(item)" v-if="item.cardType==2">AI核查</view>
							<view class="popupNoteItem" @click.stop="deteCard(item)">删除</view>
							<view class="popupNoteItem" @click.stop="show= true">分组</view>
							<view class="popupNoteItem" style="border-bottom: none;" @click.stop="bindRemk(item)">重命名
							</view>
						</view>
					</view>
				</view>
			</view>
			<empty contont="暂无分组" v-if="groupList.length==0"></empty>
			<view class="btnfz" @click="show= true">添加分组</view>
		</view>
		<up-popup :show="show" mode="center" @close="show = false" :closeable="true" round="12">
			<view class="popupbox">
				<view class="popup_title">名片分组</view>
				<scroll-view scroll-y="true" class="scroll-Y" v-if="fenzu.length!=0">
					<view class="popup_box" v-for="(item,index) in fenzu" :key="index" @click="bindGroup(item,index)"
						:class="fenzuIndex==index?'popup_boxAct':''">
						{{item.groupsName}}
						<view class="close">
							<up-icon name="close" :color="fenzuIndex==index?'#FFFFFF':'#999999'" size="20"
								@click="deteGroup(item,index)"></up-icon>

							<up-icon name="edit-pen" :color="fenzuIndex==index?'#FFFFFF':'#999999'" size="24"
								class="margin-left-xs" @click="editGroup(item,index)"></up-icon>
						</view>
					</view>
				</scroll-view>

				<view class="popup_add" @click="openAdd()">
					+ 新建分组
				</view>
				<view class="popup_btn" @click="moveGropu()">确定</view>
			</view>
		</up-popup>

		<up-popup :show="showAdd" mode="center" @close="showAdd = false" :closeable="true" round="12">
			<view class="popupbox">
				<view class="popup_title">新建分组</view>
				<view class="popupbox-Input">
					<up-input placeholder="请输入分组名称" border="none" v-model="groupsName"></up-input>
				</view>
				<view class="popup_btn" @click="insterGroup()">确定</view>
			</view>
		</up-popup>


		<up-popup :show="showRemark" mode="center" @close="showRemark = false" :closeable="true" round="12">
			<view class="popupbox">
				<view class="popup_title">添加备注</view>
				<view class="popupbox-Input">
					<up-input placeholder="请输入备注名称" border="none" v-model="cardRemark"></up-input>
				</view>
				<view class="popup_btn" @click="insterRemark()">确定</view>
			</view>
		</up-popup>



		<!-- 自定义底部导航栏 -->
		<myTabbar :value="1" />
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
	// 引入底部 Tabbar 组件
	import myTabbar from '@/components/myTabbar/myTabbar.vue';
	const {
		proxy
	} = getCurrentInstance();
	const keyword = ref('');
	const show = ref(false);
	const fenzu = reactive([{
		name: '待跟进客户'
	}, {
		name: '重点客户'
	}, {
		name: '按职位分组'
	}, {
		name: '自定义分组'
	}]);
	const fenzuIndex = ref(-1);

	const showAdd = ref(false);
	const groupsName = ref('');


	// 创建响应式数据  
	const tablist = reactive([{
		name: '最近添加'
	}, {
		name: '姓名排序'
	}, {
		name: '联系次数'
	}, {
		name: '分组管理'
	}]);
	const current = ref(0);
	const activeStyle = ref({
		'color': '#287AFE',
		'font-size': '28rpx',
		'font-weight': '800'
	});
	const inactiveStyle = ref({
		'color': '#666666',
		'font-size': '28rpx'
	});
	const pages = ref(1);
	const page = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	const status = ref('loadmore');
	const userId = ref('');

	const groupList = reactive([]);
	const groupId = ref(''); //查询分组id
	const groupsId = ref(''); //修改分组id
	const cardListId = ref(''); //列表用户id

	const showRemark = ref(false); //备注弹框
	const cardRemark = ref(''); //备注名称
	const isVip = ref(false); //是否开通会员
	
	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框

	// 跳转
	const goNav = (url) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			goLogin();
		}
	};
	// 查询是否开通会员
	const getIsVip = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.getT('/app/user/isVip?userId=' + userId).then(res => {
			if (res.code === 0) {
				isVip.value = res.data
			}
		});
	};
	/* 名片核查 */
	const clickHc = (item) => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			if (isVip.value) {
				console.log(item)
				uni.navigateTo({
					url: '/my/hecha/index?companyName=' + item.cardCompany
				})
			} else {
				uni.showModal({
					title: '提示',
					content: '开通会员才可查看访客,去开通会员',
					confirmText: '去开通',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/my/vip/index'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	// 删除名片
	const deteCard = (item) => {
		proxy.$http.get("/app/userCollect/delCardList/" + item.id).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '删除成功',
					icon: 'none'
				})
				getBusinsslist();
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 添加修改备注
	const bindRemk = (item) => {
		console.log(item, '========')
		cardListId.value = item.id
		showRemark.value = true
	};
	// 修改备注
	const insterRemark = () => {
		if (!cardRemark.value) {
			uni.showToast({
				title: '请输入备注名称',
				icon: 'none'
			})
			return
		}
		let data = {
			cardRemark: cardRemark.value,
			id: cardListId.value
		}
		proxy.$http.postJson("/app/userCollect/editCardRemark", data).then(res => {
			if (res.code == 0) {
				cardListId.value = ''
				cardRemark.value = ''
				uni.showToast({
					title: '添加成功',
					icon: 'none'
				})
				getBusinsslist();
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			showRemark.value = false
		});
	};
	// 选择分组
	const bindGroup = (item, index) => {
		fenzuIndex.value = index
	};
	// 移动分组
	const moveGropu = () => {
		if (fenzu.length == 0) {
			uni.showToast({
				title: '请先添加分组',
				icon: 'none'
			})
			return
		}
		if (fenzuIndex.value == -1) {
			uni.showToast({
				title: '请选择分组',
				icon: 'none'
			})
			return
		}
		let groupId = fenzu[fenzuIndex.value].id

		let data = {
			groupId: groupId,
			cardListId: cardListId.value
		}
		proxy.$http.postJson("/app/userCollect/moveToGroup", data).then(res => {
			if (res.code == 0) {
				fenzuIndex.value = -1
				uni.showToast({
					title: '添加成功',
					icon: 'none'
				})
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			show.value = false
		});
	};
	// 新建分组
	const insterGroup = () => {
		if (!groupsName.value) {
			uni.showToast({
				title: '请输入分组名称',
				icon: 'none'
			})
			return
		}
		if (groupsId.value) {
			let data = {
				groupsName: groupsName.value,
				id: groupsId.value
			}
			proxy.$http.postJson("/app/cardGroups/edit", data).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '修改成功',
						icon: 'none'
					})
					groupsId.value = ''
					groupsName.value = ''
					showAdd.value = false
					getGroups()
				}

			});
		} else {
			let data = {
				groupsName: groupsName.value
			}
			proxy.$http.postJson("/app/cardGroups/insert", data).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '添加成功',
						icon: 'none'
					})
					groupsName.value = ''
					showAdd.value = false
					getGroups()
				}

			});
		}

	};
	// 打开新建分组弹框
	const openAdd = () => {
		// show.value = false
		showAdd.value = true
	};
	// 修改分支
	const editGroup = (item, index) => {
		groupsName.value = item.groupsName
		groupsId.value = item.id
		showAdd.value = true

	};
	// 删除分组
	const deteGroup = (item, index) => {
		let userId = uni.getStorageSync('userId');
		proxy.$http.post("/app/cardGroups/del/" + item.id).then(res => {
			if (res.code == 0) {
				showAdd.value = false
				getGroups()
			}

		});
	};
	// 分支切换展示
	const bindTap = (ite, ind) => {
		// ite.isTrue = !ite.isTrue
		console.log(ite.id)
		list.length = 0
		groupId.value = ite.id
		groupList.forEach((d, index) => {
			if (ind == index) {
				d.isTrue = !d.isTrue
			} else {
				d.isTrue = false
			}
		})
		getBusinsslist()
	};
	//列表打开弹框事件
	const openPopup = (item, index) => {
		cardListId.value = item.id
		list.map((d, ind) => {
			if (ind == index) {
				d.check = !d.check
			} else {
				d.check = false
			}
		})
	};
	// 跳转名片详情
	const goDetail = (ite) => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.navigateTo({
				url: '/my/Enterprise/businessCard?userId=' + ite.userId + '&id=' + ite.cardId
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	// 顶部tab切换
	const clickTab = (e) => {
		// #ifdef MP-WEIXIN
		if (uni.getStorageSync('sendMsg')) {
			uni.requestSubscribeMessage({
				tmplIds: arr,
				success(re) {
					var datas = JSON.stringify(re);
					if (datas.indexOf("accept") != -1) {
						console.log(re)
					}
				},
				fail: (res) => {
					console.log(res)
				}
			})
		}
		// #endif
		console.log(e)
		current.value = e.index
		getBusinsslist();
	};
	const changeInput = (e) => {
		console.log(e)
		keyword.value = e
		list.length = 0
		page.value = 1
		getBusinsslist()
	};
	// 获取名片分享
	const getBusinsslist = () => {
		let data = {
			sortType: current.value == 0 ? '' : current.value == 1 ? 1 : current.value == 2 ? 2 : "",
			pageNum: page.value,
			pageSize: limit.value,
			groupId: current.value == 3 ? groupId.value : '',
			sortType: current.value == 1 ? 1 : current.value == 2 ? 2 : '', //sortType=1 拼音升序  sortType=2 练习次数倒序
			searchKey: keyword.value
		}
		proxy.$http.postJson("/app/userCollect/list", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pageNum
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				res.data.list.forEach(item => {
					item.check = false
				})
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.list);
				} else {
					list.push(...res.data.list);
				}
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh()
		});
	};
	// 查询分组信息
	const getGroups = () => {
		let userId = uni.getStorageSync('userId');
		groupList.length = 0
		fenzu.length = 0
		proxy.$http.getT("/app/cardGroups/query?userId=" + userId).then(res => {
			if (res.code == 0) {
				if (res.data.length != 0) {
					res.data.forEach(item => {
						item.isTrue = false
					})
					groupList.splice(0, groupList.length, ...res.data);
					groupList[0].isTrue = true
					groupId.value = res.data[0].id
					getBusinsslist()
					fenzu.splice(0, fenzu.length, ...res.data);

				}
			}

		});
	};
	onLoad(option => {
		uni.hideTabBar();
		userId.value = uni.getStorageSync('userId');
		proxy.$http.getT('/app/common/type/240').then(res => { //微信消息通知模版	240
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		})
		
		proxy.$http.getT('/app/common/type/292').then(res => { //审核状态通知	292	
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		})
	});
	onShow(() => {
		if (uni.getStorageSync('token')) {
			getBusinsslist();
			getGroups()
			getIsVip()
			
			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		}
	});
	
	onReachBottom(() => {
		if (userId.value) {
			if (page.value < pages.value) {
				page.value += 1
				status.value = 'loading';
				getBusinsslist();
			}
		}
	});
	onPullDownRefresh(() => {
		if (userId.value) {
			page.value = 1;
			getBusinsslist();
		}
	});
	const openMsg = () => {
		uni.getSetting({
			withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
			success(ret) {
				if (ret.subscriptionsSetting.itemSettings) {
					uni.setStorageSync('sendMsg', true)
					uni.openSetting({ // 打开设置页 
						success(rea) {
							console.log(rea.authSetting)
						}
					});
				} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.setStorageSync('sendMsg', false)
					uni.showModal({
						title: '提示',
						content: '为了更好的体验,请绑定消息推送',
						confirmText: '确定',
						cancelText: '取消',
						success: function(res) {
							if (res.confirm) {
								wx.requestSubscribeMessage({
									tmplIds: arr,
									success(re) {
										var datas = JSON.stringify(re);
										if (datas.indexOf("accept") != -1) {
											console.log(re)
										}
									},
									fail: (res) => {
										console.log(res)
									}
								})
								showModalMsg.value = false
							} else if (res.cancel) {
								showModalMsg.value = true
							}
						}
					})
				}
			}
		})
	};
</script>

<style lang="scss" scoped>
	page {
		background: #F5F5FB;
	}

	.Head {
		padding: 20rpx 0rpx;

		.HeadTop {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 30rpx;

			.HeadTop_msg {
				width: 45rpx;
				height: 43rpx;
				margin-left: 30rpx;

				image {
					width: 45rpx;
					height: 43rpx;
				}
			}
		}
	}


	.listbox_Item {
		display: flex;
		align-items: center;
		background: #FFFFFF;
		border-radius: 18rpx;
		margin: 20rpx;
		padding: 25rpx;
		position: relative;

		.popup {
			position: absolute;
			top: 110rpx;
			right: 30rpx;
			z-index: 9;

			.popupNote {
				width: 314rpx;
				padding-top: 10rpx;
				background: #FFFFFF;
				position: relative;
				border-radius: 20rpx;
				box-shadow: 0rpx 0rpx 10rpx 0rpx rgba(48, 86, 147, 0.16);
				display: flex;
				align-items: center;
				justify-content: space-between;
				flex-direction: column;

				/*水平偏移---垂直偏移---模糊度---扩张半径---颜色*/
				.triangle {
					border-left: 15px solid transparent;
					border-right: 15px solid transparent;
					border-bottom: 15px solid #FFFFFF;
					/*我们一般根据方向来写三角形，这样容易记忆;*/
					position: absolute;
					top: -10px;
					left: 75%;
					margin-left: -10px;


				}

				.filter {
					border-left: 15px solid transparent;
					border-right: 15px solid transparent;
					border-bottom: 15px solid rgba(48, 86, 147, 0.16);
					position: absolute;
					top: -10px;
					left: 75%;
					margin-left: -10px;
					z-index: -1;
					filter: blur(2px);
					/*这又设计到滤镜的知识*/
				}

				.popupNoteItem {
					width: 314rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 28rpx;
					color: #666666;
					border-bottom: 1px solid #F5F5F5;
					height: 99rpx;
					display: flex;
					align-items: center;
					justify-content: center;

				}
			}
		}

		image {
			width: 130rpx;
			height: 130rpx;

		}

		.listbox_Item_cot {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-left: 10rpx;

			.listbox_Item_cotent {
				height: 130rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.listbox_Item_cotentTitle {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 32rpx;
					color: #333333;
				}

				.listbox_Item_cotentTit {
					display: flex;
					align-items: center;
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;

					image {
						width: 26rpx;
						height: 26rpx;
						margin-right: 10rpx;
					}
				}
			}
		}
	}

	.popupbox {
		width: 600rpx;
		background: #FFFFFF;
		border-radius: 14rpx;
		padding: 30rpx;

		.popup_title {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
			text-align: center;
		}

		.scroll-Y {
			height: 500rpx;
		}

		.popup_boxAct {
			background: #287AFE !important;
			color: #FFFFFF !important;
		}

		.popup_box {
			width: 580rpx;
			height: 80rpx;
			background: #FFFFFF;
			border-radius: 40rpx;
			border: 1px solid #E6E6E6;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #333333;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 20rpx 0;
			position: relative;

			.close {
				display: flex;
				align-items: center;
				position: absolute;
				right: 30rpx;
			}
		}

		.popupbox-Input {
			height: 80rpx;
			background: #f4f4f4;
			border-radius: 12rpx;

			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #333333;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 30rpx 0;
			padding: 0 30rpx;
		}

		.popup_add {
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 20rpx 0;
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #287AFE;
			width: 580rpx;
			height: 98rpx;
			background: #FFFFFF;
			border-radius: 49rpx;
			border: 1px solid #E6E6E6;
		}

		.popup_btn {
			height: 86rpx;
			background: linear-gradient(-51deg, #287AFE 0%, #4A8FFF 100%);
			border-radius: 43rpx;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	.boxItem {
		background: #FFFFFF;
		font-family: Source Han Sans CN;
		font-weight: 500;
		font-size: 32rpx;
		color: #333333;
		padding: 20rpx 30rpx;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.btnfz {
		background: #287AFE;
		font-family: Source Han Sans CN;
		font-weight: 500;
		font-size: 32rpx;
		color: #FFFFFF;
		padding: 20rpx 30rpx;
		margin: 120rpx 30rpx;
		border-radius: 12rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>