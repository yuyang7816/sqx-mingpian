<template>
	<view class="">
		
	<view v-if="isTrue==1">
		<!--自定义导航栏 2.0.19支持autoBack，默认为false -->
		<up-navbar leftIcon="" :bgColor="bgColor" :placeholder="true">
		</up-navbar>
		<view class="headTop">
			<!-- 公司信息 -->
			<view class="infobox">
				<image :src="companyInfo.companyLogo?companyInfo.companyLogo:companyInfo.cardAvatar"></image>
				<view class="infobox_cot" v-if="isLogin">
					<view class="infobox_cotInfo">
						<view class="infobox_cotTite">{{companyInfo.companyName}}</view>
						<view class="infobox_cotbs">
							<image src="/static/image/mp/qi.png" mode="widthFix"></image>
							<!-- 认证状态  审核状态 0待审核 1审核成功 2审核未通过 -->
							<view v-if="companyInfo.auditStatus===''">未认证</view>
							<view v-if="companyInfo.auditStatus===0">待审核</view>
							<view v-if="companyInfo.auditStatus===1">已通过</view>
							<view v-if="companyInfo.auditStatus===2">未通过</view>
							<view v-if="companyInfo.auditStatus===2" style="margin-left: 20rpx;color: red;">
								{{companyInfo.examineRemark}}
							</view>
						</view>
						<view class="infobox_home" v-if="companyInfo.cardId" @click="goCompanyHome()">
							企业主页
							<up-icon name="arrow-right" color="#287AFE" size="12"></up-icon>
						</view>
					</view>
					<image src="/static/image/mp/qh.png" @click="openCompany"></image>
				</view>
				<view class="infobox_cotlogin" v-else @click="goLogin">登 录</view>
			</view>
			<!-- 成员管理 -->
			<view class="Memberbox" v-if="companyInfo.auditStatus===1">
				<view class="Memberbox_tit">成员管理</view>
				<view class="Memberbox_Info">
					<view class="Memberbox_InfoItem" @click="goNav('/my/Enterprise/member?current=0')">
						<view class="Memberbox_InfoItem_num">{{totalNum?totalNum:0}}</view>
						<view class="Memberbox_InfoItem_txt">
							全部成员
							<up-icon name="arrow-right" color="#999999" size="10"></up-icon>
						</view>
					</view>
					<!-- <view class="Memberbox_InfoItem" @click="goNav('/my/Enterprise/Record')">
						<view class="Memberbox_InfoItem_num">{{joinNum?joinNum:0}}</view>
						<view class="Memberbox_InfoItem_txt">
							申请加入
							<up-icon name="arrow-right" color="#999999" size="10"></up-icon>
						</view>
					</view> -->
				</view>
				<view class="Memberbox_btn">
					<view class="Memberbox_btn_ZX" @click="showPeople=true;userlist.length = 0">
						<image src="/static/image/qiye/bj.png" mode="widthFix"></image>
						<text class="margin-lr-xs"></text>
						在线录入
					</view>
					<!-- 	<view class="Memberbox_btn_WX">
						<image src="/static/image/qiye/wx.png"></image>
						<text class="margin-lr-xs"></text>
						微信邀请
					</view> -->

					<!-- #ifdef MP-WEIXIN -->
					<button open-type="share" v-if="canvasImgSrc">
						<view class="Memberbox_btn_WX">
							<image src="/static/image/qiye/wx.png"></image>
							<text class="margin-lr-xs"></text>
							微信邀请
						</view>
					</button>
					<!-- #endif -->

					<!-- #ifndef MP-WEIXIN -->
					<view class="Memberbox_btn_WX" @click="sharurl()">
						<image src="/static/image/qiye/wx.png"></image>
						<text class="margin-lr-xs"></text>
						微信邀请
					</view>
					<!-- #endif -->

				</view>
			</view>
		</view>

		<view class="rzbox" v-if="companyInfo.auditStatus!=1">
			<view class="rzbox_cont">
				<view class="rzbox_cont_title">
					<image src="/static/image/mp/qi.png"></image>
					<text>企业认证中心</text>
				</view>
				<view class="rzbox_cont_tit">完成企业认证，获取对应专属权益</view>
			</view>
			<view class="rzbox_btn" @click="bindCertion()">立即认证</view>
		</view>

		<view class="Template" v-if="companyInfo.auditStatus===1&&CardInfo.companyName">
			<view class="Template_tit">模板管理</view>
			<view class="Templatebox">
				<image :src="CardInfo.backImg?CardInfo.backImg:bgIamgeUrl" mode="aspectFill"></image>
				<view class="Templatebox_cot">
					<view class="Templatebox_cotTit">{{CardInfo.companyName}}</view>
					<view class="Templatebox_cotbox">
						<!-- <view class="Templatebox_cot_tit">1 成员在使用</view> -->
						<view class="" @click="EditTemplate()">
							<up-icon name="more-dot-fill" color="#999999" size="20"></up-icon>
						</view>
					</view>
					<view class="Templatebox_cotcj" v-if="CardInfo.userName">创建者：{{CardInfo.userName}}</view>
				</view>
			</view>
		</view>
		<!-- 成员动态 -->
		<view class="Dtmk" v-if="dtlist.length!=0">
			<view class="Dtmk_head">
				<view class="Dtmk_headTitle">成员动态</view>
				<view class="Dtmk_headtit" @click="goNav('/my/Enterprise/dt?id='+companyInfo.cardId)">
					全部动态
					<image src="/static/image/my/right.png" mode="widthFix"></image>
				</view>
			</view>

			<view class="DtmkItem" v-for="(item,index) in dtlist" :key="index">
				<view class="DtmkItem_cont">
					<image :src="item.cardAvatar?item.cardAvatar:'/static/logo.png'"></image>
					<view class="DtmkItem_contTit">
						<view class="DtmkItem_contTitlan ">{{item.realName}} </view>
						<view class="DtmkItem_contTithui">加入了企业</view>
					</view>
				</view>
				<view class="DtmkItem_data">{{item.createTime}}</view>
			</view>
		</view>
		<!-- 额度使用情况 -->
		<view class="Edbox" v-if="!isVip">
			<view class="Edbox_head">
				<view class="Edbox_headTitle">额度使用情况</view>
				<view class="Edbox_headTit">有效期：2024-09-14</view>
			</view>
			<view class="Edbox_cot">
				<view class="Edbox_cotItem">
					<view class="Edbox_cotItem_num">{{companyInfo.usedQuota?companyInfo.usedQuota:0}}</view>
					<view class="Edbox_cotItem_tit">已使用</view>
				</view>
				<view class="Edbox_cotItem">
					<view class="Edbox_cotItem_num">{{companyInfo.quota?companyInfo.quota:0}}</view>
					<view class="Edbox_cotItem_tit">剩余额度</view>
				</view>
				<view class="Edbox_cotBtn" @click="goVip()">额度升级</view>
			</view>
		</view>
		<up-action-sheet :actions="list" :show="show" cancelText="取消" @close="show = false"
			@select="select"></up-action-sheet>

		<!-- 自定义底部导航栏 -->
		<myTabbar :value="3" />


		<up-popup :show="showPeople" @close="showPeople= false;phone = '';" mode="center" :round="10" closeable>
			<view class="popup">
				<view class="flex align-center" style="margin-top: 60rpx;">
					<view class="popupInput">
						<up-input placeholder="请输入手机号" border="none" v-model="phone"></up-input>

					</view>
					<view @click="change" class="btncx">查询</view>
				</view>
				<view class="padding-tb-sm" v-if="userlist.length!=0">
					<scroll-view scroll-y="true" class="scroll-Y">
						<up-radio-group v-model="radiovalue1" placement="column">
							<view class="listbox_Item" v-for="(item,index) in userlist" :key="index">
								<image :src="item.cardType==1?item.cardAvatar: item.companyLogo" mode="aspectFill"
									style="border-radius: 55%;"></image>

								<view class="listbox_Item_cotent">
									<view class="listbox_Item_cot">
										<view class="listbox_Item_cotentTitle">
											{{item.realName}}
										</view>
										<view class="listbox_Item_cotentTit">
											<image src="/static/image/mp/zhiwei.png" mode="widthFix"></image>
											{{item.companyPosition}}
										</view>
										<view class="listbox_Item_cotentTit">
											<image src="/static/image/mp/qiy.png" mode="widthFix"></image>
											{{item.companyName}}
										</view>
									</view>
								</view>
								<up-radio :label="item.name" :name="item.cardId" @change="radioChange">
								</up-radio>
							</view>

						</up-radio-group>


					</scroll-view>
				</view>
				<view class="popupbtn" @click="clickLr()">确认</view>
			</view>
		</up-popup>
		
		<canvas canvas-id="myShareImg" id="myShareImg" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}"
			class="myShareImg">
		</canvas>
		
		
	</view>
	<view class="" v-if="isTrue==2">
		<view class="bgImage">
			<view class="content">

				<view class="box">
					<view class="boxHead">
						<view class="boxHead_num">01</view>
						<view class="boxHead_title">设计专属自己企业的数字名片</view>
					</view>
					<view class="flex flex-wrap margin-top-sm justify-between">
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
				</view>
				<view class="box" style="padding-bottom: 20rpx;">
					<view class="boxHead">
						<view class="boxHead_num">02</view>
						<view class="boxHead_title">高效智能管理企业成员名片</view>
					</view>
					<view class="flex flex-wrap margin-top-sm justify-between">
						<view class="flex flex-wrap margin-top-sm justify-between">
							<view class="boxLabel margin-bottom-sm" >在线录入</view>
							<view class="boxLabel margin-bottom-sm" >邀请加入</view>
							<view class="boxLabel margin-bottom-sm" >成员动态</view>
							<view class="boxLabel margin-bottom-sm" >额度进度</view>
							
						</view>
					</view>
				</view>
				<view class="box" style="padding-bottom: 20rpx;">
					<view class="boxHead">
						<view class="boxHead_num">03</view>
						<view class="boxHead_title">可视化数据面板</view>
					</view>
					<view class="flex flex-wrap margin-top-sm justify-between">
						<view class="flex flex-wrap margin-top-sm justify-between">
							<view class="boxLabel margin-bottom-sm">累计访问数量</view>
							<view class="boxLabel margin-bottom-sm">今日访问数量</view>
							<view class="boxLabel margin-bottom-sm">分享名片数量</view>
							<view class="boxLabel margin-bottom-sm">名片排行榜单</view>
						</view>
					</view>
				</view>

				<view class="btn" @click="goCampay()">立即使用</view>
			</view>
		</view>

		
		<!-- 自定义底部导航栏 -->
		<myTabbar :value="3" />

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
		onReachBottom,
		onShareAppMessage,
		onShareTimeline,
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	// 引入底部 Tabbar 组件
	import myTabbar from '@/components/myTabbar/myTabbar.vue';
	import index from '../../uni_modules/uview-plus';
	const bgColor = ref('linear-gradient(81deg, #C0E4FB 0%, #D3F9EC 100%)')
	const isLogin = ref(false); //是否登录
	const userId = ref(''); // 用户 userId
	const token = ref(''); // 用户 token
	// 用户信息
	const userName = ref('');
	const avatar = ref(''); //用户头像
	const CertificationStatus = ref(''); //认证状态  0-待审核 1-已通过 2-未通过

	// 使用 ref 创建响应式数据  
	const title = ref('标题');
	const list = reactive([]);
	const show = ref(false);
	const showPeople = ref(false);

	const companyInfo = reactive({});
	const totalNum = ref(0); //全部成员
	const phone = ref('');
	const userlist = reactive([]);
	const cardId = ref('');
	const CardInfo = reactive({});

	const isTrue = ref(-1); //是否显示企业
	const dtlist = reactive([]);
	//推广文案
	const tuiguang1 = ref('');
	const url = ref('');
	const shareH5Url = ref('');
	const backgroundImage = ref('');
	const tuiguang = ref('');

	const invitationCode = ref('');
	const joinNum = ref(0); //申请加人人数

	const bgIamgeUrl = ref(''); //模板默认背景
	const isVip = ref(false);

	const canvasImgSrc = ref('');
	//海报宽度
	const shareImgWidth = ref(uni.upx2px(686));
	//海报高度
	const shareImgHeight = ref(uni.upx2px(360));


	onShareAppMessage(() => {
		return {
			title: tuiguang.value,
			path: '/pages/index/index?inviterCode=' + invitationCode.value + '&cardId=' + companyInfo.cardId,
			imageUrl: canvasImgSrc.value,
		}
	});
	onShareTimeline(() => {
		return {
			title: tuiguang.value,
			path: '/pages/index/index?inviterCode=' + invitationCode.value + '&cardId=' + companyInfo.cardId,
			imageUrl: canvasImgSrc.value,
		}
	});
	onLoad(option => {
		uni.hideTabBar();
		// 获取邀请码保存到本地
		if (option.inviterCode) {
			uni.setStorageSync('inviterCode', option.inviterCode);
		}
		if (option.cardId) {
			uni.setStorageSync('inviterCardId', option.cardId);
		}
	});
	onShow(() => {
		if (uni.getStorageSync('token')) {
			invitationCode.value = uni.getStorageSync('invitationCode')
			isLogin.value = true
			getUserInfo() // 获取用户信息
			// getUserCertification() // 查询用户认证状态
			getlist()
			
			getTuiguang1()

			getIsVip()

		} else {
			isLogin.value = false
			userId.value = '';
			token.value = '';
			userName.value = '';
			CertificationStatus.value = ''
			totalNum.value = 0
			isTrue.value = 2

		}
	});
	// 查询是否开通会员
	const getIsVip = () => {
		let userId = uni.getStorageSync('userId')
		proxy.$http.getT('/app/user/isVip?userId=' + userId).then(res => {
			if (res.code === 0) {
				isVip.value = res.data
			}
		});
	};
	// 获取背景列表
	const getbglist = () => {
		let data = {
			// companyCardId: companyCardId.value,
			pageNum: 1,
			pageSize: 100,
		}
		proxy.$http.postJson("/app/companyTemplatePic/page", data).then(red => {
			if (red.code == 0) {
				if (red.data.records.length != 0) {
					bgIamgeUrl.value = red.data.records[0].pic
				}
			}
		});
	};
	const goVip = () => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/my/vip/index'
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	// 跳转入驻企业
	const goCampay = () => {
		if (uni.getStorageSync('token')) {
			if (userName.value && avatar.value) {
				uni.navigateTo({
					url: '/my/Enterprise/PublishOne'
				})
				return
			} else {
				uni.showModal({
					title: '提示',
					content: '请先去完善个人信息',
					showCancel: false,
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/pages/my/info'
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

	//文案推广
	const getTuiguang1 = () => {
		proxy.$http.getT('/app/common/type/276').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					tuiguang1.value = res.data.value;
				}
			}
		});
		// #ifndef H5
		proxy.$http.getT('/app/common/type/25').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					url.value = res.data.value;
				}
			}
		});
		//#endif
		// #ifdef H5
		proxy.$http.getT('/app/common/type/19').then(ress => {
			if (ress.code === 0) {
				if (ress.data && ress.data.value) {
					url.value = ress.data.value;
					shareH5Url.value = url.value
				} else {
					url.value = proxy.$publicYuMing();
					shareH5Url.value = url.value
				}
			}
		});
		//#endif
	};
	// 切换选择企业
	const openCompany = () => {
		
		list.forEach((ite, index) => {
			if (ite.cardId === companyInfo.cardId) {
				list[index].color = '#287AFE'
			} else {
				list[index].color = '#333333'
			}
		})
		show.value = true
	};
	// 查询企业名片信息
	const getCardInfo = () => {
		proxy.$http.get("/app/company/staffData?cardId=" + companyInfo.cardId).then(res => {
			if (res.code == 0) {
				totalNum.value = res.data.totalNum
				getcompanyTemplate() // 查询企业模板

			}

		});
	};
	/* 获取申请加入人数 */
	const getCardnum = () => {
		proxy.$http.get("/app/comRecord/applyCount?cardId=" + companyInfo.cardId).then(res => {
			if (res.code == 0) {
				joinNum.value = res.data

			}

		});
	};
	// 获取名片列表
	const getlist = () => {
		let userId = uni.getStorageSync('userId')
		let data = {
			userId: userId,
			pageNum: 1,
			pageSize: 100,
			cardType: 2
		}

		proxy.$http.postJson("/app/businessCard/queryAllList", data).then(res => {
			if (res.code == 0 && res.data && res.data.list.length!=0) {
				if (res.data.list.length != 0) {
					let arr = []
					res.data.list.forEach((d, index) => {
						d.name = d.companyName
						arr.push(d)
					})
					let ite = {
						name: '新建企业',
						cardId: -1
					}
					arr.push(ite)
					list.splice(0, list.length, ...arr);

					if (!uni.getStorageSync('companyInfo')) {
						uni.setStorageSync('companyInfo', res.data.list[0])
					} else {
						let iteInfo = uni.getStorageSync('companyInfo')
						let indexs = list.findIndex(item => item.cardId === iteInfo.cardId)
						if (indexs == -1) {
							uni.setStorageSync('companyInfo', res.data.list[0])
						} else {
							uni.setStorageSync('companyInfo', list[indexs])
						}

					}
					Object.assign(companyInfo, uni.getStorageSync('companyInfo'))
					if (companyInfo.cardImg) {
						backgroundImage.value = companyInfo.cardImg
						// #ifdef MP-WEIXIN
						getShareImg()
						// #endif
					} else {
						getBackImageList()
					}

					getCardInfo() // 查询企业名片信息
					getCardnum()
					getDynamic() // 查询企业员工动态信息
					getbglist()
					isTrue.value = 1
				}

			} else {
				isTrue.value = 2
				uni.removeStorageSync('companyInfo')
			}

		});
	};
	//获取分享海报的图片
	const getBackImageList = () => {
		proxy.$http.getT('/app/common/type/317').then(res => {
			if (res.code === 0) {
				if (res.data && res.data.value) {
					backgroundImage.value = res.data.value;
					console.log('backgroundImage.value', backgroundImage.value)
					// #ifdef MP-WEIXIN
					getShareImg()
					// #endif
				}
			}
		});
	};
	// 查询成员
	const change = () => {
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		proxy.$http.get("/app/businessCard/queryByPhone?phone=" + phone.value + '&cardId=' + companyInfo.cardId).then(
			res => {
				if (res.code == 0) {
					if (res.data && res.data.length != 0) {
						res.data.forEach(d => {
							d.ischeck = false
						})
						userlist.splice(0, userlist.length, ...res.data);
					} else {
						uni.showToast({
							title: '未查询到此用户',
							icon: 'none',
							duration: 2000
						})
					}
				}

			});
	};
	// 选择成员
	const radioChange = (n) => {
		cardId.value = n
	};
	// 录入成员
	const clickLr = () => {
		if (userlist.length == 0) {
			uni.showToast({
				title: '请输入查询手机号',
				icon: 'none'
			})
			return
		}
		if (!cardId.value) {
			uni.showToast({
				title: '请选择添加名片',
				icon: 'none'
			})
			return
		}
		proxy.$http.get("/app/company/saveStaffCard?cardId=" + cardId.value + '&companyCardId=' + companyInfo.cardId)
			.then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '添加成功',
						icon: 'none'
					})
					phone.value = ''
					userlist.length = 0
					showPeople.value = false
					getCardInfo()
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}

			});
	};
	const select = (e) => {
		if (e.name == '新建企业') {
			uni.navigateTo({
				url: '/my/Enterprise/PublishOne'
			})
		} else {
			uni.setStorageSync('companyInfo', e)
			Object.assign(companyInfo, uni.getStorageSync('companyInfo'))
			getCardInfo() // 查询企业名片信息
			getCardnum()
			getDynamic() // 查询企业员工动态信息
			getbglist()
		}

	};
	// 认证
	const bindCertion = () => {
		uni.navigateTo({
			url: '/my/Enterprise/PublishOne?cardId=' + companyInfo.cardId
		})
		// if (uni.getStorageSync('token')) {
		// 	uni.navigateTo({
		// 		url: '/my/renzheng/index?userType=1'
		// 	})
		// } else {
		// 	goLogin()
		// }
	};
	// 编辑企业模板
	const EditTemplate = () => {
		if (uni.getStorageSync('token')) {
			let id = CardInfo ? CardInfo.id : ''
			uni.navigateTo({
				url: '/my/Enterprise/Editor?companyCardId=' + companyInfo.cardId + '&id=' + id
			})
		} else {
			goLogin()
		}
	};
	const goNav = (url) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			goLogin()
		}
	};
	const goCompanyHome = () => {
		if (!companyInfo.cardId) {
			uni.showToast({
				title: '暂无企业主页',
				icon: 'none'
			})
			return
		}
		goNav('/my/Enterprise/companyHome?id=' + companyInfo.cardId)
	};
	// 跳转登录
	const goLogin = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 查询企业模板
	const getcompanyTemplate = () => {
		let data = {
			companyCardId: companyInfo.cardId
		}
		proxy.$http.post("/app/template/info", data).then(red => {
			if (red.code == 0) {
				Object.assign(CardInfo, red.data)
			}
		});
	};
	// 查询企业员工动态信息
	const getDynamic = () => {
		let userId = uni.getStorageSync('userId')
		let data = {
			pageNum: 1,
			pageSize: 3,
			userId: userId,
			cardId: companyInfo.cardId

		}
		proxy.$http.get("/app/comRecord/page", data).then(red => {
			if (red.code == 0) {
				dtlist.splice(0, dtlist.length, ...red.data.records);
			}
		});
	};
	// // 查询用户认证状态
	// const getUserCertification = () => {
	// 	let userId = uni.getStorageSync('userId')
	// 	proxy.$http.get("/app/userCertification/queryInsert?userId=" + userId).then(red => {
	// 		if (red.code == 0 && red.data) {
	// 			CertificationStatus.value = red.data.status
	// 		} else {
	// 			CertificationStatus.value = ''
	// 		}
	// 	});
	// };
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				avatar.value = res.data.avatar
			}
		});
	};
	const sharurl = () => {
		let relationId = invitationCode.value;
		uni.showModal({
			title: '文案推广',
			content: tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' +
				relationId + '&cardId=' + companyInfo.cardId,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#287AFE',
			success: res => {
				if (res.confirm) {
					uni.setClipboardData({
						data: tuiguang1.value + relationId + '\n' + url.value +
							'?inviterCode=' +
							relationId + '&cardId=' + companyInfo.cardId,
						success: function() {
							uni.showToast({
								title: '复制成功',
								icon: 'none'
							})
						}
					});
				}
			}
		});
	};
	const getShareImg = async () => {
		//如果已经生成了海报，则直接显示 没有则去用canvas生成
		if (!canvasImgSrc.value) {
			await shareImg();
		}
	};
	//生成海报
	const shareImg = async () => {
		let _imgInfo = await getImageInfo(backgroundImage.value);
		//获取canvas的上下文对象
		const ctx = uni.createCanvasContext('myShareImg');
		ctx.setFillStyle('#f2f2f2'); //canvas背景颜色
		ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value); //canvas画布大小
		ctx.save();
		// 修改：创建圆角矩形路径（24rpx圆角）
		ctx.beginPath();
		const radius = uni.upx2px(24); // 将24rpx转换为px
		ctx.moveTo(radius, 0);
		ctx.lineTo(shareImgWidth.value - radius, 0);
		ctx.quadraticCurveTo(shareImgWidth.value, 0, shareImgWidth.value, radius);
		ctx.lineTo(shareImgWidth.value, shareImgHeight.value - radius);
		ctx.quadraticCurveTo(shareImgWidth.value, shareImgHeight.value, shareImgWidth.value - radius,
			shareImgHeight.value);
		ctx.lineTo(radius, shareImgHeight.value);
		ctx.quadraticCurveTo(0, shareImgHeight.value, 0, shareImgHeight.value - radius);
		ctx.lineTo(0, radius);
		ctx.quadraticCurveTo(0, 0, radius, 0);
		ctx.closePath();
		ctx.clip(); // 裁剪为圆角矩形

		//绘制背景图片（现在会在圆角矩形内绘制）
		ctx.drawImage(_imgInfo.path, 0, 0, shareImgWidth.value, shareImgHeight.value);

		// 绘制半透明背景框（在背景图上方）
		ctx.setFillStyle('rgba(0, 0, 0, 0.3)'); // 白色半透明，透明度0.5
		ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value);

		ctx.restore(); // 恢复状态，这样后续绘制不会受裁剪影响

		// 在底部绘制个人信息与二维码
		//绘制头像
		const _avatarInfo = ref('');
		//头像宽高
		const avatarWidthOrHeight = ref(50);
		//边距 - 修改为距离顶部20像素
		const topDistance = 20; // 距离顶部20像素
		const paddingNum = ref(15); // 左边距保持不变

		//如果有头像、
		let aveatr = companyInfo.companyLogo
		if (aveatr) {
			_avatarInfo.value = await getImageInfo(aveatr)
		} else { //没有头像使用本地logo绘制
			_avatarInfo.value = await getImageInfo('/static/logo.png')
		}
		ctx.save();
		ctx.beginPath();
		//计算头像的中心坐标 - 修改Y坐标为距离顶部topDistance
		const avatarCenterX = paddingNum.value + avatarWidthOrHeight.value / 2;
		const avatarCenterY = topDistance + avatarWidthOrHeight.value / 2;
		//绘制圆形头像
		ctx.arc(avatarCenterX, avatarCenterY, avatarWidthOrHeight.value / 2, 0, 2 * Math.PI);
		ctx.clip();
		//绘制头像 - 修改Y坐标为距离顶部topDistance
		ctx.drawImage(_avatarInfo.value.path,
			paddingNum.value,
			topDistance,
			avatarWidthOrHeight.value,
			avatarWidthOrHeight.value);
		ctx.restore();

		//绘制用户昵称 - 调整Y坐标与头像对齐
		ctx.setFontSize(18);
		ctx.setFillStyle('#FFFFFF');
		ctx.fillText(companyInfo.realName,
			paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight.value,
			topDistance + avatarWidthOrHeight.value / 2 - 5); // 向上微调5px

		//绘制用户 岗位- 调整Y坐标在昵称下方
		ctx.setFontSize(14);
		ctx.setFillStyle('#FFFFFF');
		ctx.fillText(companyInfo.companyPosition,
			paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight.value,
			topDistance + avatarWidthOrHeight.value / 2 + 18); // 在昵称下方18px

		/* 绘制图标和文字 - 放在头像和昵称的底部 */
		// 图标尺寸
		const iconSize = 20;
		// 图标和文字的间距
		const iconTextSpacing = 5;

		// 计算底部区域的位置（在头像下方）
		const bottomSectionY = topDistance + avatarWidthOrHeight.value + 15; // 头像底部向下25px

		// 如果需要绘制公司名称，可以继续添加
		if (companyInfo.companyName) {
			// 绘制公司名称图标（如果有的话）
			let companyIcon = await getImageInfo('https://mpfx.xianmaxiong.com/image/qiye.png')
			ctx.drawImage(companyIcon.path,
				paddingNum.value,
				bottomSectionY + iconSize + 10,
				iconSize,
				iconSize);

			ctx.setFontSize(12);
			ctx.setFillStyle('#FFFFFF');
			ctx.fillText(companyInfo.companyName,
				paddingNum.value + iconSize + iconTextSpacing,
				bottomSectionY + iconSize - 3); // 在地址下方
		}

		// 企业地址文本
		if (companyInfo.companyAddress) {
			let AddIcon = await getImageInfo('https://mpfx.xianmaxiong.com/image/add.png')
			// 绘制企业地址图标
			ctx.drawImage(AddIcon.path,
				paddingNum.value,
				bottomSectionY,
				iconSize,
				iconSize);

			// 绘制企业地址文字
			ctx.setFontSize(12);
			ctx.setFillStyle('#FFFFFF');
			ctx.fillText(companyInfo.companyAddress,
				paddingNum.value + iconSize + iconTextSpacing,
				bottomSectionY + iconSize * 2 + 7); // 文字与图标居中对齐
		}

		// 绘制联系方式
		if (companyInfo.cardPhone) {
			// 手机号中间四位加密
			const phone = companyInfo.cardPhone;
			let encryptedPhone = phone;

			// 判断是否是手机号格式（11位数字）
			if (/^\d{11}$/.test(phone)) {
				encryptedPhone = phone.substring(0, 3) + '****' + phone.substring(7);
			} else if (phone.length >= 8) {
				// 如果不是标准手机号，根据长度处理
				const startLen = Math.floor(phone.length / 3);
				const endLen = Math.floor(phone.length / 3);
				encryptedPhone = phone.substring(0, startLen) + '****' + phone.substring(phone.length - endLen);
			}

			// 绘制手机号图标
			let phoneIcon = await getImageInfo('https://mpfx.xianmaxiong.com/image/phone.png')
			ctx.drawImage(phoneIcon.path,
				paddingNum.value,
				bottomSectionY + iconSize + 45,
				iconSize,
				iconSize);

			ctx.setFontSize(12);
			ctx.setFillStyle('#FFFFFF');
			ctx.fillText(encryptedPhone,
				paddingNum.value + iconSize + iconTextSpacing,
				bottomSectionY + iconSize * 2 + 38); // 在地址下方
		}
		
		ctx.draw(false, () => {
			console.log('绘制完成')
			uni.hideLoading()
			getCanvasNewImage();
		});
	};
	//异步获取图片信息
	const getImageInfo = async (imgSrc) => {
		return new Promise((resolve, errs) => {
			uni.getImageInfo({
				src: imgSrc,
				success: function(image) {
					resolve(image);
				},
				fail(err) {
					errs(err);
					uni.showToast({
						title: '海报生成失败'
					})
					uni.hideLoading()
				}
			});
		});
	};
	//把canvas画布转为图片
	const getCanvasNewImage = () => {
		uni.canvasToTempFilePath({
			canvasId: 'myShareImg',
			quality: 1,
			complete: res => {
				// console.log(res.tempFilePath, 'res.tempFilePath', res)
				// canvasImgSrc.value = res.tempFilePath;
				uploadImg(res.tempFilePath)
			}
		});
	};
	/* 上传图片 */
	const uploadImg = (e) => {
		uni.showLoading({
			title: '上传中...'
		});
		uni.uploadFile({
			url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
			// url: config.APIHOST + '/alioss/upload', //仅为示例，非真实的接口地址
			filePath: e,
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			success: uploadFileRes => {
				uni.hideLoading()
				let url = JSON.parse(uploadFileRes.data).data;
				canvasImgSrc.value = url;

				console.log(canvasImgSrc.value, '图片图片212121212121')
			}
		});
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background: #F5F5FB;
	}

	button {
		padding: 0;
		border: none !important;
		background-color: #FFFFFF !important;
	}

	button::after {
		border: none !important;
	}

	.headTop {
		width: 100%;
		position: relative;
		background: #FFFFFF;
	}

	.headTop::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(81deg, #C0E4FB 0%, #D3F9EC 100%);
		z-index: 1;
		/* 确保在垂直渐变之上 */
	}

	.headTop::after {
		content: "";
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to bottom, transparent, #F5F5FB);
		/* 垂直渐变，结束颜色为透明 */
		z-index: 2;
		/* 确保在水平渐变之上 */
	}

	.infobox {
		position: relative;
		z-index: 9;
		padding: 30rpx;
		display: flex;
		align-items: center;

		image {
			width: 110rpx;
			height: 110rpx;
			border-radius: 8rpx;
		}

		.infobox_cotlogin {
			margin-left: 15rpx;
			display: flex;
			justify-content: space-between;
			flex: 1;
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.infobox_cot {
			margin-left: 15rpx;
			display: flex;
			justify-content: space-between;
			flex: 1;

			.infobox_cotInfo {
				flex: 1;
				min-width: 0;
			}

			.infobox_cotTite {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.infobox_cotbs {
				display: flex;
				font-size: 24rpx;
				color: #287AFE;

				image {
					width: 32rpx;
					height: 36rpx;
					margin-right: 6rpx;
				}

			}

			.infobox_home {
				margin-top: 16rpx;
				display: inline-flex;
				align-items: center;
				gap: 8rpx;
				padding: 10rpx 18rpx;
				border-radius: 999rpx;
				font-size: 24rpx;
				font-weight: 500;
				color: #287AFE;
				background: rgba(40, 122, 254, 0.08);
				border: 1rpx solid rgba(40, 122, 254, 0.12);
			}

			image {
				width: 40rpx;
				height: 40rpx;
			}
		}
	}

	.Memberbox {
		position: relative;
		z-index: 9;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.Memberbox_tit {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.Memberbox_Info {
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 30rpx 120rpx;

			.Memberbox_InfoItem {
				text-align: center;


				.Memberbox_InfoItem_num {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 36rpx;
					color: #333333;
				}

				.Memberbox_InfoItem_txt {
					display: flex;
					align-items: center;
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}

		.Memberbox_btn {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 15rpx;

			.Memberbox_btn_ZX {
				width: 277rpx;
				height: 67rpx;
				background: #FFFFFF;
				border-radius: 34rpx;
				border: 1px solid #287AFE;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #287AFE;
				display: flex;
				align-items: center;
				justify-content: center;

				image {
					width: 31rpx;
					height: 33rpx;
				}
			}

			.Memberbox_btn_WX {
				width: 277rpx;
				height: 67rpx;
				background: #287AFE;
				border-radius: 34rpx;
				border: 1px solid #287AFE;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 28rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;

				image {
					width: 44rpx;
					height: 36rpx;
				}
			}
		}
	}

	.rzbox {
		margin: 20rpx 30rpx;
		height: 160rpx;
		background: linear-gradient(77deg, #EAF2FF 0%, #FFFFFF 100%);
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 34rpx;

		.rzbox_cont {

			.rzbox_cont_title {
				display: flex;
				align-items: center;

				image {
					width: 32rpx;
					height: 36rpx;
				}

				text {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 32rpx;
					color: #333333;
					margin-left: 10rpx;
				}
			}

			.rzbox_cont_tit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.rzbox_btn {
			width: 180rpx;
			height: 66rpx;
			border-radius: 33rpx;
			border: 1rpx solid #287AFE;
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 26rpx;
			color: #287AFE;
			display: flex;
			align-items: center;
			justify-content: center;

		}
	}

	.Template {
		background: #FFFFFF;
		border-radius: 18rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.Template_tit {
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;

		}

		.Templatebox {
			margin-top: 20rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			image {
				width: 253rpx;
				height: 147rpx;
				border-radius: 10rpx;
			}

			.Templatebox_cot {
				margin-left: 20rpx;
				flex: 1;
				height: 147rpx;
				// display: flex;
				// justify-content: space-between;
				// flex-direction: column;

				.Templatebox_cotTit {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 32rpx;
					color: #333333;
				}

				.Templatebox_cotbox {
					display: flex;
					justify-content: flex-end;

					.Templatebox_cot_tit {
						font-family: Source Han Sans CN;
						font-weight: 400;
						font-size: 24rpx;
						color: #666666;
					}
				}

				.Templatebox_cotcj {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}

	// 成员动态模块
	.Dtmk {
		background: #FFFFFF;
		border-radius: 18rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx 30rpx 10rpx;

		.Dtmk_head {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.Dtmk_headTitle {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.Dtmk_headtit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #666666;

				image {
					width: 12rpx;
					height: 18rpx;
					margin-left: 10rpx;
				}
			}
		}

		.DtmkItem {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin: 20rpx 0;

			.DtmkItem_cont {
				display: flex;
				align-items: center;

				image {
					width: 60rpx;
					height: 60rpx;
					border-radius: 55rpx;
				}

				.DtmkItem_contTit {
					display: flex;
					align-items: flex-end;
					margin-left: 10rpx;

					.DtmkItem_contTitlan {
						font-weight: 500;
						font-size: 30rpx;
						color: #287AFE;

						width: 120rpx;
						/* 设置容器的宽度 */
						white-space: nowrap;
						/* 防止文本换行 */
						overflow: hidden;
						/* 隐藏溢出的内容 */
						text-overflow: ellipsis;
						/* 显示省略号来代表被修剪的文本 */

					}

					.DtmkItem_contTithui {
						font-weight: 500;
						font-size: 26rpx;
						color: #666666;
						margin-left: 15rpx;
					}
				}
			}

			.DtmkItem_data {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}
	}

	// 额度使用情况
	.Edbox {
		background: #FFFFFF;
		border-radius: 18rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.Edbox_head {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.Edbox_headTitle {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.Edbox_headTit {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #666666;
			}
		}

		.Edbox_cot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 25rpx;

			.Edbox_cotItem {
				text-align: center;

				.Edbox_cotItem_num {
					font-family: Source Han Sans CN;
					font-weight: 500;
					font-size: 36rpx;
					color: #333333;
				}

				.Edbox_cotItem_tit {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
					margin-top: 10rpx;
				}
			}

			.Edbox_cotBtn {
				width: 183rpx;
				height: 66rpx;
				background: #FFFFFF;
				border-radius: 33rpx;
				border: 1px solid #287AFE;
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 26rpx;
				color: #287AFE;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}

	.popup {
		padding: 30rpx;
		width: 600rpx;

		.popupInput {
			flex: 1;
			border: 1rpx solid #dddddd;
			border-radius: 12rpx;
			padding: 20rpx 30rpx;
		}

		.btncx {
			background: #287AFE;
			color: #FFFFFF;
			padding: 15rpx 30rpx;
			border-radius: 12rpx;
			margin-left: 20rpx;
		}

		.popupbtn {
			background: #287AFE;
			color: #FFFFFF;
			padding: 25rpx 0;
			border-radius: 24rpx;
			text-align: center;
			margin-top: 50rpx;
		}
	}

	.scroll-Y {
		height: 500rpx;
	}

	.listbox_Item {
		display: flex;
		align-items: center;
		border-radius: 18rpx;
		margin: 20rpx;
		padding-bottom: 30rpx;


		image {
			width: 130rpx;
			height: 130rpx;

		}

		.listbox_Item_cotent {
			flex: 1;
			height: 130rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			margin-left: 10rpx;

			.listbox_Item_cotentTitle {
				font-family: Source Han Sans CN;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;

				text {
					background: linear-gradient(40deg, #FEF2DC 0%, #FEE4BF 100%);
					border-radius: 6rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 20rpx;
					color: #84663A;
					padding: 4rpx 10rpx;
				}
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

	.bgImage {
		background-image: url('https://mpfx.xianmaxiong.com/image/qiymp.png');
		background-size: 100% 100%;
		width: 100%;
		height: 1948rpx;
		padding-bottom: 120rpx;
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
			margin-right: 20rpx;

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
	
	.myShareImg {
		position: fixed;
		left: -1000px;
	}
</style>
