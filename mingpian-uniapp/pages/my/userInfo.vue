<template>
	<view style="padding-bottom: 120rpx;">
		<view class="headTop">
			<view class="boxIteo">
				<view class="boxIteo_Img">
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar"
						style="width: 146rpx;height: 146rpx;border-radius: 50%;">
						<image :src="cardAvatar?cardAvatar:'../../static/logo.png'" mode="aspectFill"
							style="width: 146rpx;height: 146rpx;border-radius: 50%;">
						</image>
					</button>
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<image @click="uploadImg()" :src="cardAvatar?cardAvatar:'../../static/logo.png'" mode="aspectFill">
					</image>
					<!-- #endif -->
					<!-- <image src="/static/logo.png"></image> -->
				</view>

				<!-- 拍照识别 -->
				<view class="pbsbIcon" @click="clickIdentification()">
					<image src="/static/image/my/shibie.png"></image>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">姓名</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请填写姓名" border="none" v-model="realName" inputAlign="right"></up-input>
					</view>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">公司</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请填写公司" border="none" v-model="companyName" inputAlign="right"></up-input>
					</view>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">职位</view>
					<view class="infoboxItem_cont">
						<up-input placeholder="请填写职位" border="none" v-model="companyPosition"
							inputAlign="right"></up-input>
					</view>
				</view>
				<view class="infoboxItem">
					<view class="infoboxItem_title">行业(类型)</view>
					<view class="infoboxItem_cont" @click="showTree = true">
						<view v-if="industry">{{industry}}</view>
						<view v-else class="input-placeholder">请选择行业类型</view>
						<image src="/static/image/my/huiright.png" mode="widthFix"></image>
					</view>
				</view>

				<view class="infoboxItem" style="border-bottom: none;">
					<view class="infoboxItem_title">城市</view>
					<view class="infoboxItem_cont" @click="changeCity()">
						<view v-if="cityName">{{cityName}}</view>
						<view v-else class="input-placeholder">请选择所在地</view>
						<image src="/static/image/my/huiright.png" mode="widthFix"></image>
					</view>
				</view>
			</view>
		</view>

		<view class="boxItm">
			<view class="boxItm_title">联系信息</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">手机</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写联系方式" border="none" v-model="cardPhone" inputAlign="right"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">微信号</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写微信号" border="none" v-model="wxCode" inputAlign="right"></up-input>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">邮箱</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写邮箱" border="none" v-model="email" inputAlign="right"></up-input>
				</view>
			</view>
			<view class="infoboxItem" style="border-bottom: none;">
				<view class="infoboxItem_title">地址</view>
				<view class="infoboxItem_cont" @click="openMap()">
					<view v-if="address">{{address}}</view>
					<view v-else class="input-placeholder">请填写地址</view>
					<image src="/static/image/my/huiright.png" mode="widthFix"></image>
				</view>
			</view>
		</view>
		<view class="boxItmWork">
			<view class="boxItmWork_head">
				<view class="boxItmWork_title">工作经历</view>
				<view class="" @click="openWork()">
					<up-icon name="plus-circle" color="#2979ff" size="28"></up-icon>
				</view>
			</view>

			<view v-for="(item,index) in Worklist" :key="index" v-if="Worklist.length!=0">
				<view class="boxItmWork_Cot">
					<view class="boxItmWork_CotTitle">{{item.Enterprise}}</view>
					<view class="flex align-center">
						<view class="boxItmWork_CotTit">{{item.Position}}</view>
						<view class="margin-left-sm" @click="removeWork(index)">
							<up-icon name="minus-circle-fill" color="#ff1a1d" size="28"></up-icon>
						</view>
					</view>
				</view>
			</view>
			<view v-else class="boxItmWork_empty">暂无工作经历</view>

		</view>
		<view class="boxItmWork">
			<view class="boxItmWork_head">
				<view class="boxItmWork_title">教育经历</view>
				<view class="" @click="openEducat()">
					<up-icon name="plus-circle" color="#2979ff" size="28"></up-icon>
				</view>
			</view>
			<view v-for="(item,index) in Educationlist" :key="index" v-if="Educationlist.length!=0">
				<view class="boxItmWork_Cot">
					<view class="boxItmWork_CotTitle">{{item.School}}</view>
					<view class="flex align-center">
						<view class="boxItmWork_CotTit">{{item.Educational}}</view>
						<view class="margin-left-sm" @click="removeEducat(index)">
							<up-icon name="minus-circle-fill" color="#ff1a1d" size="28"></up-icon>
						</view>
					</view>
				</view>
			</view>


			<view v-else class="boxItmWork_empty">暂无教育经历</view>
		</view>
		
		<view class="boxItm">
			<view class="boxItm_title">个人简介</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">简介</view>
				<view class="infoboxItem_cont">
					<up-input placeholder="请填写个人简介" border="none" v-model="detailsText" inputAlign="right"></up-input>
				</view>
			</view>
			<view class="boxItm_title margin-top">个人照片</view>
			<view class="Image_box">
				<view class="boxs" v-for="(item,index) in detailsImage" :key='index'>
					<image :src="item" style="width: 180rpx; height: 180rpx;border-radius: 12rpx;"></image>
					<image @click="deletePic(index,1)" src="../../static/image/detee.png" class="deleteImg">
					</image>
				</view>
				<view class="boxs" id='fileUploadBtn' @click="addImages(3)" v-show="detailsImage.length < 9">
					<image src="/static/image/tupian.png"
						style="width: 180rpx; height: 180rpx;"></image>
				</view>
			</view>
			<view class="boxItm_title margin-top">个人视频</view>
			<view class="Image_box">
				<view class="boxss" v-if="detailsVideo">
					<video :src="detailsVideo" style="width: 630rpx; height: 300rpx;"></video>
					<image @click="deletePic(index,2)" src="../../static/image/detee.png" class="deleteImg">
					</image>
				</view>
				<view class="boxs" id='fileUploadBtn' @click="addImagess()" v-else>
					<image src="/static/image/tupian.png"
						style="width: 180rpx; height: 180rpx;"></image>
				</view>
			</view>
		</view>
		<!-- <view class="boxItmTmage ">
			<view class="boxItmTmage_title">添加名片样式</view>
			<view class="boxItmTmage_cot">
				<view class="boxItmTmage_cotItem" @click="addImages(1)">
					<view class="boxItmTmage_cotItem_img" v-if="cardPositive">
						<image :src="cardPositive" mode="aspectFill"></image>
					</view>
					<view class="boxItmTmage_cotItem_tit" v-else>
						<up-icon name="plus" color="#666666" size="16"></up-icon>
						<text>名片正面</text>
					</view>
				</view>
			</view>
		</view> -->
		

		<view class="boxItmTmage ">
			<view class="boxItmTmage_title">添加名片样式</view>
			<view class="boxItmTmage_cot">
				<view class="boxItmTmage_cotItem" @click="addImages(1)">
					<view class="boxItmTmage_cotItem_img" v-if="cardPositive">
						<image :src="cardPositive" mode="aspectFill"></image>
					</view>
					<view class="boxItmTmage_cotItem_tit" v-else>
						<up-icon name="plus" color="#666666" size="16"></up-icon>
						<text>名片正面</text>
					</view>
				</view>
			</view>
		</view>

		<view class="btn" @click="submit()">确认</view>




		<!-- 添加工作经历 -->
		<up-popup :show="WorkShow" @close="WorkShow = false" mode="bottom" :round="12" :closeable="true">
			<view class="popupWork">
				<view class="popupItem">
					<view class="popupItem_title">岗位</view>
					<view class="popupItem_tit">
						<up-input placeholder="请填写岗位" border="none" v-model="Position" inputAlign="left"></up-input>
					</view>
				</view>
				<view class="popupItem    ">
					<view class="popupItem_title">企业</view>
					<view class="popupItem_tit">
						<up-input placeholder="请填写企业" border="none" v-model="Enterprise" inputAlign="left"></up-input>
					</view>
				</view>
				<view class="popupBtn" @click="submitWork()">确认</view>
			</view>
		</up-popup>

		<!-- 添加教育经历 -->
		<up-popup :show="EducationShow" @close="EducationShow = false" mode="bottom" :round="12" :closeable="true">
			<view class="popupWork">
				<view class="popupItem">
					<view class="popupItem_title">学校</view>
					<view class="popupItem_tit">
						<up-input placeholder="请填写学校" border="none" v-model="School" inputAlign="left"></up-input>
					</view>
				</view>
				<view class="popupItem    ">
					<view class="popupItem_title">学历</view>
					<view class="popupItem_tit">
						<up-input placeholder="请填写学历" border="none" v-model="Educational" inputAlign="left"></up-input>
					</view>
				</view>
				<view class="popupBtn" @click="submitEducation()">确认</view>
			</view>
		</up-popup>

		<!-- 行业 -->
		<up-picker :show="showTree" ref="uPickerRef" :loading="loading" :columns="columns" keyName="industryName"
			@change="changeHandler" @cancel="showTree = false" @confirm="confirmTree"></up-picker>

		<DaCitySelector :visible="citySelectorVisible" themeColor="#287AFE" :multipleLimit="5"
			:cityValue="citySelectedValue" @confirm="handleConfirm" @close="handleClose"></DaCitySelector>
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
	const cardAvatar = ref('');
	const realName = ref(''); //姓名
	const companyName = ref(''); //公司
	const companyPosition = ref(''); //职位

	const address = ref(''); //公司详细地址
	const cardPhone = ref(''); //手机号
	
	const detailsText = ref(''); //个人简介（文字）
	const detailsImage = ref([]); //个人简介（图片）
	const detailsVideo = ref(''); //个人简介（视频）
	
	const email = ref(''); //邮箱
	const wxCode = ref('');//微信号

	const WorkShow = ref(false); //经历弹框
	const Position = ref(''); //岗位
	const Enterprise = ref(''); //企业
	const Worklist = reactive([]);

	const EducationShow = ref(false); //经历弹框
	const School = ref(''); //学校
	const Educational = ref(''); //学历
	const Educationlist = reactive([]);
	const cardPositive = ref(''); //名片正面
	const cardBack = ref(''); //名片背面

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
	const cardId = ref('');

	const inviterCardId = ref('');
	const userName = ref('')
	const avatar = ref('')


	onLoad(option => {
		getUserInfo() //获取用户信息
		if (option.cardId) {
			cardId.value = option.cardId
			getCardInfo()
		}

		if (option.infoData) {
			let infoData = JSON.parse(option.infoData)
			console.log(infoData.address)
			realName.value = infoData.name
			companyPosition.value = infoData.title
			address.value = infoData.address
			cardPhone.value = infoData.mobile
			
			detailsText.value = infoData.detailsText ? infoData.detailsText : ''
			detailsImage.value = infoData.detailsImage ? infoData.detailsImage.split(',') : []
			detailsVideo.value = infoData.detailsVideo ? infoData.detailsVideo : ''
			
			email.value = infoData.email
			companyName.value = infoData.company
		}
		getTreeList()
		uni.removeStorageSync('inviterCardId')
		if (option.inviterCardId) {
			inviterCardId.value = option.inviterCardId
			getCardDetail()
		}

	});
	onShow(() => {

	});
	//删除上传
	const deletePic = (type, index) => {
		if (index == 1) {
			detailsImage.value.splice(type, 1)
		} else {
			detailsVideo.value = ''
		}
	};
	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				avatar.value = res.data.avatar
	
			}
		});
	};
	/* 拍照识别名片 */
	const clickIdentification = () => {
		if (uni.getStorageSync('token')) {
			if (userName.value && avatar.value) {
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album', 'camera'], //从相册选择
					success: function(res) {
						uni.showLoading({
							title: '上传识别中...'
						});
						let token = uni.getStorageSync('token');
						let userId = uni.getStorageSync('userId');
						uni.uploadFile({
							url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
							filePath: res.tempFilePaths[0],
							header: {
								token: uni.getStorageSync('token')
							},
							name: 'file',
							success: uploadFileRes => {
								let cardUrl = JSON.parse(uploadFileRes.data).data;
								console.log(cardUrl)
								proxy.$http.get("/app/ai/extractCardInfo", {
									image: cardUrl
								}).then(res => {
									if (res.code == 0) {
										console.log(res)
										let infoData = res.data
										realName.value = infoData.name
										companyPosition.value = infoData.title
										address.value = infoData.address
										cardPhone.value = infoData.mobile
										email.value = infoData.email
										companyName.value = infoData.company
										
										detailsText.value = infoData.detailsText ? infoData.detailsText : ''
										detailsImage.value = infoData.detailsImage ? infoData.detailsImage.split(',') : []
										detailsVideo.value = infoData.detailsVideo ? infoData.detailsVideo : ''
									}
									uni.hideLoading();
								});
							}
						});
					}
				});
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
			goLogin();
		}
	};
	/* 获取名片信息 */
	const getCardDetail = () => {
		proxy.$http.get("/app/businessCard/info?cardId=" + inviterCardId.value).then(res => {
			if (res.code == 0) {
				// Object.assign(CardInfo, res.data)
				companyName.value = res.data.companyName
				// companyPosition.value = res.data.companyPosition

				industryFirstId.value = res.data.industryFirstId //行业一级id
				industryFirstName.value = res.data.industryFirstName //行业一级名称
				industrySecondId.value = res.data.industrySecondId //行业二级id 
				industrySecondName.value = res.data.industrySecondName //行业二级名称
				industry.value = industryFirstName.value + '-' + industrySecondName.value //行业名称


			}
		});
	};

	// 选择城市
	const handleConfirm = (val) => {
		console.log('handleConfirm ==>', val)
		cityId.value = val.id || ''
		cityName.value = val.name || ''
		citySelectorVisible.value = false
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
		industry.value = industryFirstName.value + '-' + industrySecondName.value //行业名称
		showTree.value = false
	};
	// 行业滑动切换二级分类
	const changeHandler = (e) => {
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
	// 获取名片详情
	const getCardInfo = () => {
		let userId = uni.getStorageSync('userId');
		proxy.$http.get("/app/businessCard/info?cardId=" + cardId.value).then(res => {
			if (res.code == 0) {
				// console.log(JSON.stringify(res.data.eduExperience))
				cardId.value = res.data.cardId
				industryFirstId.value = res.data.industryFirstId; //行业一级id
				industryFirstName.value = res.data.industryFirstName; //行业一级名称
				industrySecondId.value = res.data.industrySecondId; //行业二级id 
				industrySecondName.value = res.data.industrySecondName; //行业二级名称
				industry.value = industryFirstName.value + '-' + industrySecondName.value //行业名称
				cityName.value = res.data.companyCity; //所在地
				cardAvatar.value = res.data.cardAvatar; //头像
				realName.value = res.data.realName; //姓名
				companyName.value = res.data.companyName; //公司
				companyPosition.value = res.data.companyPosition; //职位
				address.value = res.data.address; //公司详细地址
				cardPhone.value = res.data.cardPhone; //手机号
				
				detailsText.value = res.data.detailsText ? res.data.detailsText : ''
				if(res.data.detailsImage){
					detailsImage.value = res.data.detailsImage.split(',')
				}else{
					detailsImage.value = []
				}
				// detailsImage = res.data.detailsImage ? res.data.detailsImage.split(',') : []
				detailsVideo.value = res.data.detailsVideo ? res.data.detailsVideo : ''
				
				email.value = res.data.email; //邮箱
				cardPositive.value = res.data.cardImg; //名片正面
				cardBack.value = res.data.cardImgBack; //名片背面
				wxCode.value = res.data.wxCode //微信号

				let arr = []
				res.data.workExperience.split(';').forEach(d => {
					console.log(d)
					if (d) {
						let ite = d.split(',')
						let data = {
							Position: ite[1],
							Enterprise: ite[0],
						}
						Worklist.push(data)
					}

				})
				let arrEud = []
				res.data.eduExperience.split(';').forEach(d => {
					console.log(d)
					if (d) {
						let ite = d.split(',')
						let data = {
							School: ite[0],
							Educational: ite[1],
						}
						Educationlist.push(data)
					}

				})

			}
		});
	};

	// 选择位置
	const openMap = () => {
		uni.chooseLocation({
			success: function(res) {
				console.log('位置名称：' + res.name);
				console.log('详细地址：' + res.address);
				console.log('纬度：' + res.latitude);
				console.log('经度：' + res.longitude);
				address.value = res.address
				// selectCity(res.longitude, res.latitude)
			}
		});

	};
	// 经纬度换省市区
	const selectCity = (longitude, latitude) => {
		proxy.$http.getT('/app/Login/selectCity?lat=' + latitude + '&lng=' + longitude).then(res => {
			if (res.code === 0) {
				companyCity.value = res.data.city ? res.data.city : res.data.province
				companyDistrict.value = res.data.district
				companyProvince.value = res.data.province
			}
		});
	};
	// 移除教育经历
	const removeEducat = (index) => {
		Educationlist.splice(index, 1)
	};
	// 打开教育经历
	const openEducat = () => {
		School.value = '';
		Educational.value = '';
		EducationShow.value = true
	};
	// 添加教育经历
	const submitEducation = () => {
		if (!School.value) {
			uni.showToast({
				title: '请填写学校',
				icon: 'none'
			})
			return
		}

		if (!Educational.value) {
			uni.showToast({
				title: '请填写学历',
				icon: 'none'
			})
			return
		}
		let data = {
			School: School.value,
			Educational: Educational.value,
		}
		Educationlist.push(data)
		EducationShow.value = false
	};
	// 移除经历弹框
	const removeWork = (index) => {
		Worklist.splice(index, 1)
	};
	// 打开工作经历弹框
	const openWork = () => {
		Position.value = '';
		Enterprise.value = '';
		WorkShow.value = true
	};
	// 添加岗位
	const submitWork = () => {
		if (!Position.value) {
			uni.showToast({
				title: '请填写岗位',
				icon: 'none'
			})
			return
		}

		if (!Enterprise.value) {
			uni.showToast({
				title: '请填写企业',
				icon: 'none'
			})
			return
		}
		let data = {
			Position: Position.value,
			Enterprise: Enterprise.value,
		}
		Worklist.push(data)
		WorkShow.value = false
	};
	// 提交名片
	const submit = () => {
		let arr = []
		Worklist.forEach(d => {
			let item = d.Enterprise + ',' + d.Position + ';'
			arr.push(item)
		})

		let arrEud = []
		Educationlist.forEach(d => {
			let item = d.School + ',' + d.Educational
			arrEud.push(item + ';')
		})


		if (!cardAvatar.value) {
			uni.showToast({
				title: '请上传名片头像',
				icon: 'none'
			})
			return
		}
		if (!realName.value) {
			uni.showToast({
				title: '请填写姓名',
				icon: 'none'
			})
			return
		}
		if (!companyName.value) {
			uni.showToast({
				title: '请填写公司名称',
				icon: 'none'
			})
			return
		}
		if (!companyPosition.value) {
			uni.showToast({
				title: '请填写职位',
				icon: 'none'
			})
			return
		}
		if (!industry.value) {
			uni.showToast({
				title: '请选择行业类型',
				icon: 'none'
			})
			return
		}
		if(!cityName.value){
			uni.showToast({
				title: '请选择所在地',
				icon: 'none'
			})
			return
		}
		if (!cardPhone.value) {
			uni.showToast({
				title: '请填写手机号',
				icon: 'none'
			})
			return
		}
		if (!wxCode.value) {
			uni.showToast({
				title: '请填写微信号',
				icon: 'none'
			})
			return
		}
		
		if (!email.value) {
			uni.showToast({
				title: '请填写邮箱',
				icon: 'none'
			})
			return
		}
		if (!address.value) {
			uni.showToast({
				title: '请填写地址',
				icon: 'none'
			})
			return
		}
		if (!detailsText.value) {
			uni.showToast({
				title: '请填写个人简介',
				icon: 'none'
			})
			return
		}
		// if (!cardBack.value) {
		// 	uni.showToast({
		// 		title: '请填写名片背面',
		// 		icon: 'none'
		// 	})
		// 	return
		// }


		let data = {
			industryFirstId: industryFirstId.value, //行业一级id
			industryFirstName: industryFirstName.value, //行业一级名称
			industrySecondId: industrySecondId.value, //行业二级id 
			industrySecondName: industrySecondName.value, //行业二级名称
			companyCity: cityName.value, //所在地
			cardAvatar: cardAvatar.value, //头像
			realName: realName.value, //姓名
			companyName: companyName.value, //公司
			companyPosition: companyPosition.value, //职位
			address: address.value, //公司详细地址
			cardPhone: cardPhone.value, //手机号
			
			detailsText: detailsText.value,
			detailsImage: detailsImage.value.toString(''),
			detailsVideo: detailsVideo.value,
			
			email: email.value, //邮箱
			cardImg: cardPositive.value, //名片正面
			cardImgBack: cardBack.value, //名片背面
			workExperience: arr.join(''), //工作经历
			eduExperience: arrEud.join(''), //教育经验
			cardType: 1, //1 个人 2 企业
			cardId: cardId.value,
			wxCode:wxCode.value
		}

		proxy.$http.postJson("/app/businessCard/insert", data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '提交成功',
					icon: 'none'
				})
				if (inviterCardId.value) {
					uni.navigateTo({
						url: '/my/Enterprise/businessCard?id=' + inviterCardId.value+'&isYao=1'
					})
				} else {
					uni.navigateBack()
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 视频上传
	const addImagess = (e) => {
		uni.chooseVideo({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				console.log(res)
				// for (let i = 0; i < res.tempFilePath.length; i++) {
				uni.showLoading({
					title: '上传中...'
				})
				uni.uploadFile({ // 上传接口
					url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
					filePath: res.tempFilePath,
					name: 'file',
					success: (uploadFileRes) => {
						console.log(JSON.parse(uploadFileRes.data).data)
						detailsVideo.value = JSON.parse(uploadFileRes.data).data
						// that.form.videoUrl = JSON.parse(uploadFileRes.data).data
						// console.log(that.videoUrl)
						uni.hideLoading();
					}
				});
				// }
			}
		})
	};
	//上传图片
	const addImages = (e) => {
		uni.chooseImage({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < 1; i++) {
					uni.showLoading({
						title: '上传中...'
					})
					uni.uploadFile({ // 上传接口
						// url: config.APIHOST + '/alioss/upload',
						url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							if (e == 1) { //名片 正面
								cardPositive.value = JSON.parse(uploadFileRes.data)
									.data
							} else if (e == 2) { //名片 反面
								cardBack.value = JSON.parse(uploadFileRes.data)
									.data
							} else if(e == 3){
								detailsImage.value.push(JSON.parse(uploadFileRes.data)
									.data)
							}

							uni.hideLoading();
						}
					});
				}
			}
		})
	};
	//上传头像
	const uploadImg = () => {
		var url = null;
		uni.showActionSheet({
			// itemList按钮的文字接受的是数组
			itemList: ["查看头像", "从相册选择图片"],
			success(e) {
				var index = e.tapIndex
				if (index === 0) {
					// 用户点击了预览当前图片
					// 可以自己实现当前头像链接的读取
					let url = cardAvatar.value;
					let arr = []
					arr.push(url)
					uni.previewImage({
						// 预览功能图片也必须是数组的
						urls: arr
					})
				} else if (index === 1) {
					uni.chooseImage({
						count: 1, //默认9
						sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
						sourceType: ['album'], //从相册选择
						success: function(res) {
							uni.showLoading({
								title: '上传中...'
							});
							let token = uni.getStorageSync('token');
							let userId = uni.getStorageSync('userId');
							uni.uploadFile({
								url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
								// url: config.APIHOST + '/alioss/upload',
								filePath: res.tempFilePaths[0],
								header: {
									token: uni.getStorageSync('token')
								},
								name: 'file',
								success: uploadFileRes => {
									url = JSON.parse(uploadFileRes.data);
									cardAvatar.value = url.data
									uni.hideLoading();
								}
							});
						}
					});
				}
			}
		})
	};
	// 微信头像能力
	const onChooseAvatar = (e) => {
		uni.showLoading({
			title: '上传中...'
		});
		uni.uploadFile({
			url: 'https://mpfx.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
			// url: config.APIHOST + '/alioss/upload', //仅为示例，非真实的接口地址
			filePath: e.detail.avatarUrl,
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			success: uploadFileRes => {
				uni.hideLoading()
				let url = JSON.parse(uploadFileRes.data).data;
				cardAvatar.value = url
			}
		});
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F6F7FB;
	}

	button {
		padding: 0 !important;
	}

	button::after {
		border: none;
	}



	.headTop {
		width: 100%;
		background: linear-gradient(to bottom, #287AFE 20%, #F6F7FB 60%);
		padding: 80rpx 0 0;

		.boxIteo {
			background: #FFFFFF;
			border-radius: 24rpx;
			padding: 90rpx 30rpx 30rpx;
			margin: 0rpx 30rpx;
			position: relative;

			.pbsbIcon {
				width: 60rpx;
				height: 60rpx;
				position: absolute;
				top: 20rpx;
				right: 30rpx;

				image {
					width: 60rpx;
					height: 60rpx;
				}
			}

			.boxIteo_Img {
				width: 154rpx;
				height: 154rpx;
				background: #FFFFFF;
				border-radius: 55%;
				position: absolute;
				top: -60rpx;
				left: 40%;
				display: flex;
				align-items: center;
				justify-content: center;

				image {
					width: 146rpx;
					height: 146rpx;
					border-radius: 55%;
				}
			}


		}
	}

	.infoboxItem {
		margin-top: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		border-bottom: 1px solid #F2F2F2;
		padding-bottom: 30rpx;

		.infoboxItem_title {

			font-weight: 800;
			font-size: 28rpx;
			color: #333333;

		}

		.input-placeholder {

			font-weight: 500;
			font-size: 26rpx;
			color: #999999 !important;
		}

		.infoboxItem_cont {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: flex-end;
			font-size: 28rpx;
			margin-left: 20rpx;

			view {
				flex: 1;
				text-align: right;
			}

			image {
				width: 26rpx;
				height: 26rpx;
			}
		}
	}

	.boxItm {
		padding: 30rpx 30rpx 10rpx;
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;

		.boxItm_title {

			font-weight: bold;
			font-size: 32rpx;
			color: #000000;
		}
	}

	.boxItmWork {
		padding: 30rpx;
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;

		.boxItmWork_head {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.boxItmWork_title {

				font-weight: bold;
				font-size: 32rpx;
				color: #000000;
			}
		}

		.boxItmWork_Cot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 25rpx;

			.boxItmWork_CotTitle {

				font-weight: 800;
				font-size: 28rpx;
				color: #333333;
			}

			.boxItmWork_CotTit {

				font-weight: 400;
				font-size: 24rpx;
				color: #333333;
			}
		}

		.boxItmWork_empty {
			font-size: 30rpx;
			color: #999999;
			padding: 30rpx;
			text-align: center;
		}
	}

	.boxItmTmage {
		padding: 30rpx;
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;

		.boxItmTmage_title {
			font-weight: bold;
			font-size: 32rpx;
			color: #000000;
		}

		.boxItmTmage_cot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;

			.boxItmTmage_cotItem {
				width: 286rpx;
				height: 162rpx;
				background: #F5F5F5;
				border-radius: 16rpx;
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.boxItmTmage_cotItem_img {
				width: 286rpx;
				height: 162rpx;
				border-radius: 16rpx;

				image {
					width: 100%;
					height: 100%;
					border-radius: 16rpx;
				}
			}

			.boxItmTmage_cotItem_tit {

				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;

				text {
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
					margin-top: 6rpx;
				}
			}
		}
	}

	.btn {
		background: #287AFE;
		color: #FFFFFF;
		margin: 30rpx;
		padding: 30rpx;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.popupWork {
		padding: 30rpx;

		.popupItem {
			margin-top: 30rpx;

			.popupItem_title {

				font-weight: 800;
				font-size: 28rpx;
				color: #333333;
			}

			.popupItem_tit {
				font-size: 28rpx;

				font-weight: 500;
				border-bottom: 1rpx solid #dadada;
				padding: 10rpx 0 6rpx;
			}

			.input-placeholder {

				font-weight: 500;
				font-size: 26rpx;
				color: #999999 !important;
			}
		}

		.popupBtn {
			background: #287AFE;
			color: #FFFFFF;
			font-size: 28rpx;
			border-radius: 24rpx;
			padding: 20rpx 0;
			text-align: center;
			margin-top: 50rpx;
		}
	}
	
	.Image_box {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		margin-top: 20rpx;
	}
	
	.boxss{
		width: 630rpx;
		height: 300rpx;
		margin-bottom: 20rpx;
		position: relative;
	}
	
	.boxs {
		width: 180rpx;
		height: 180rpx;
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		position: relative;
	}
	
	.deleteImg {
		width: 40rpx;
		height: 40rpx;
		position: absolute;
		right: -12rpx;
		top: -10rpx;
	}
</style>