<template>
	<view class="">
		<view class="padding">
			<!-- 我的余额 -->
			<view class="boxtx">
				<view class="text-lg">可用余额</view>
				<view class="price">{{money}}<text>元</text></view>
			</view>
			<!-- 提现 -->
			<view class="boxtx">
				<view class="text-lg margin-bottom">提现金额</view>
				<view class="padding-bottom-sm" style="border-bottom: 1px solid #cccccc;">
					<input placeholder="请输入提现金额" v-model="moneyNum">
				</view>
				<view class="tit">{{placeholder}}</view>
			</view>

			<!-- 提现类型 -->
			<view class="boxType">
				<view class="zhifuSlect_btn" @tap="zhifuSelectTap(1)" v-if="zhifubaoTiXian"
					:style="zhifuSelect == 1 ? 'border: 1px solid #3578FE;' : 'border: 1px solid #ffffff;'">
					<image src="../static/zhifubao.png"></image>
					<view style="margin-left:10rpx">支付宝</view>
				</view>
				<view class="zhifuSlect_btn" @tap="zhifuSelectTap(2)" v-if="weixinTiXian"
					:style="zhifuSelect ==2 ? 'border: 1px solid #3578FE;' : 'border: 1px solid #ffffff;'">
					<image src="../static/icon_weixin.png"></image>
					<view style="margin-left:10rpx">微信</view>
				</view>
			</view>

			<view class="btn" @click="cashMoney">立即提现</view>

			<up-popup :show="show" :round="20" mode="center" @close="show = false">
				<view class="popup">
					<view class="popup_titname">上传微信提现二维码</view>
					<view class="popup_tit">请提交微信收款二维码</view>
					<view class="popup_close" @click="show = false">
						<up-icon name="close-circle-fill" color="#999999" size="28"></up-icon>
					</view>
					<view class="popup_Image " @click="addImages">
						<image v-if="!wxImg" src="../static/erweima.png" mode="aspectFill"></image>
						<image v-else :src="wxImg"></image>
					</view>
					<view class="popup_bth" @click="SaveWxImg">保存</view>
				</view>
			</up-popup>
		</view>

		<view class="boxbot">
			<view @click="goNav('/my/wallet/zhifubao')" v-if="zhifubaoTiXian">提现账号</view>
			<view v-if="weixinTiXian" @click="openWxImg()">微信收款码</view>
			<view @click="goNav('/my/wallet/moneydetail')">钱包明细</view>
			<view @click="goNav('/my/wallet/cashList')">提现记录</view>
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
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	import config from '@/common/config.js'
	const money = ref(0); //余额
	const moneyNum = ref(''); //提现金额
	const zhifuSelect = ref(1); //提现类型  1支付宝 2微信
	const zhifubaoTiXian = ref(false); //是否开启支付宝提现
	const weixinTiXian = ref(false); //是否开启微信提现
	const placeholder = ref(''); //提现提示文字
	const charge = ref(''); //手续费
	const minMoney = ref(''); //最低提现
	const maxMoney = ref(''); //最高提现

	const sp = ref(''); //手续费
	const wxImg = ref(''); //微信收款码
	const mywxImg = ref('');

	const WxTXFangShi = ref(1); //微信提现类型  1自动 2手动
	const show = ref(false); //微信收款码弹框


	onLoad(option => {

		//是否开启微信提现
		proxy.$http.getT('/app/common/type/243').then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					if (res.data.value === '是') {
						weixinTiXian.value = true;
						zhifuSelect.value = 2;
					} else {
						weixinTiXian.value = false;
						if (!zhifubaoTiXian.value) {
							zhifuSelect.value = -1;
						}
					}
				}
			}
		});
		//是否开启支付宝提现
		proxy.$http.getT('/app/common/type/245').then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					if (res.data.value === '是') {
						zhifubaoTiXian.value = true;
						zhifuSelect.value = 1;
					} else {
						zhifubaoTiXian.value = false;
						if (!weixinTiXian.value) {
							zhifuSelect.value = -1;
						}
					}
				}
			}
		});
		// 微信提现方式 1自动 2手动
		proxy.$http.getT('/app/common/type/244').then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					WxTXFangShi.value = res.data.value;
				}
			}
		});

	});
	onShow(() => {
		getUserInfo();
		getCharge(); //提现手续费
		getMinMoney(); //最低提现金额
		getMaxMoney(); //最高提现金额
		getTeamMoney(); //获取收益

	});
	// 打开微信收款码弹框
	const openWxImg = () => {
		show.value = true
	};
	// 跳转
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	};
	// 提现类型切换
	const zhifuSelectTap = (index) => {
		zhifuSelect.value = index;
	};
	// 邀请收益
	const getTeamMoney = () => {
		proxy.$http.getT('/app/userMoney/selectMyMoney').then(res => {
			if (res.code == 0) {
				money.value = res.data.money //我的余额
	
			}
		});
	};
	const getUserInfo = () => {
		proxy.$http.get("/app/user/selectUserById").then(des => {
			if (des.code == 0) {
				mywxImg.value = des.data.wxImg ? des.data.wxImg : '';
				wxImg.value = des.data.wxImg ? des.data.wxImg : '';
				uni.setStorageSync('zhiFuBao', des.data.zhiFuBao)
				uni.setStorageSync('zhiFuBaoName', des.data.zhiFuBaoName)
			}
		});
	};

	// 提现手续费 （用户提现时候的手续费，可以作为平台服务费使用，设置0.3就是百分之30，举例提现100服务费30，到账70）
	const getCharge = () => {
		proxy.$http.get("/app/common/type/152").then(res => {
			if (res.code == 0) {
				charge.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最低提现额度
	const getMinMoney = () => {
		proxy.$http.get("/app/common/type/112").then(res => {
			if (res.code == 0) {
				minMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最高提现额度
	const getMaxMoney = () => {
		proxy.$http.get("/app/common/type/153").then(res => {
			if (res.code == 0) {
				maxMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};

	// 提现
	const cashMoney = () => {

		if (!moneyNum.value) {
			uni.showToast({
				icon: 'none',
				title: '请输入提现金额'
			})
			return
		}
		if (moneyNum.value > money.value * 1) {
			uni.showToast({
				icon: 'none',
				title: '您的余额不足'
			})
			return
		}
		if (Number(moneyNum.value) < Number(minMoney.value)) {
			uni.showToast({
				icon: 'none',
				title: '提现金额不能小于'+minMoney.value+'元'
			})
			return
		}
		if (moneyNum.value > maxMoney.value * 1) {
			uni.showToast({
				icon: 'none',
				title: '提现金额不能大于'+maxMoney.value+'元'
			})
			return
		}
		if (zhifuSelect.value == 1) {
			let zhiFuBao = uni.getStorageSync('zhiFuBao')
			let zhiFuBaoName = uni.getStorageSync('zhiFuBaoName')
			if (!zhiFuBao && !zhiFuBaoName) {
				uni.navigateTo({
					url: '/my/wallet/zhifubao'
				})
				return
			}

			sp.value = (moneyNum.value * charge.value).toFixed(2)
			uni.showModal({
				title: '提现申请提示',
				content: '本次提现' + moneyNum.value + '元，服务费' + sp.value + '元，是否确认提现？',
				success: function(res) {
					if (res.confirm) {
						proxy.$http.get("/app/cash/cashMoney", {
							money: moneyNum.value,
							classify: zhifuSelect.value
						}).then(res => {
							if (res.code == 0) {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
								moneyNum.value = null
							} else {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
							}
							getTeamMoney()
						});
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			});
		} else if (zhifuSelect.value == 2) {
			if (WxTXFangShi.value == 2) {
				let classify = ''
				// #ifdef H5
				let ua = navigator.userAgent.toLowerCase();
				if (ua.indexOf('micromessenger') !== -1) {
					classify = 3
				} else {
					classify = 2
				}
				// #endif
				// #ifdef MP-WEIXIN
				classify = 2
				// #endif
				if (wxImg.value) {
					sp.value = (moneyNum.value * charge.value).toFixed(2)
					uni.showModal({
						title: "提现申请提示",
						content: '本次提现' + moneyNum.value + '元，服务费' + sp.value + '元，是否确认提现？',
						success: (e) => {
							if (e.confirm) {
								uni.showLoading({
									title: '提现中...',
									icon: 'none'
								})
								proxy.$http.get("/app/cash/cashMoney", {
									money: moneyNum.value,
									classify: classify,
									userType: 1
								}).then(res => {
									uni.hideLoading();
									if (res.code == 0) {
										uni.showToast({
											icon: 'none',
											title: res.msg
										})
										moneyNum.value = null
									} else {
										uni.showToast({
											icon: 'none',
											title: res.msg
										})
									}
									getTeamMoney()
								});
							}
						}
					});
				} else {
					show.value = true;
				}
			} else {
				uni.showLoading({
					title: '提现中...',
					icon: 'none'
				})
				proxy.$http.get("/app/cash/cashMoney", {
					money: moneyNum.value,
					classify: classify,
					userType: 1
				}).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
						moneyNum.value = null
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
					getTeamMoney()
				});
			}

		}
	};
	const SaveWxImg = () => {
		uni.showLoading({
			title: '上传中'
		});
		if (wxImg.value == '') {
			uni.hideLoading();
			uni.showToast({
				title: '请上传微信二维码！',
				icon: 'none'
			})
			return;
		}

		let data = {
			wxImg: wxImg.value
		}
		proxy.$http.postJson('/app/user/updateUser', data).then(res => {
			uni.hideLoading();
			if (res.code == 0) {
				show.value = false;
				uni.showToast({
					title: '绑定成功！',
					icon: 'none'
				})
				proxy.$http.getT('/app/user/selectUserById').then(ret => {
					if (ret.code == 0) {
						mywxImg.value = ret.data.wxImg ? ret.data.wxImg : '';
					}
				});
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 图片上传
	const addImages = () => {
		uni.chooseImage({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < 1; i++) {
					uni.showLoading({
						title: '上传中'
					});
					uni.uploadFile({ // 上传接口
						// url: config.APIHOST + '/alioss/upload',
						url: 'https://mydz.xianmxkj.com/sqx_fast/alioss/upload', //真实的接口地址
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading();
							let url = JSON.parse(uploadFileRes.data)
							if (url.code == 0) {
								wxImg.value = url.data
							} else {
								uni.showToast({
									title: url.msg,
									icon: 'none'
								})
							}

						}
					});
				}
			}
		})
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F2F2F2;
	}

	.boxtx {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
	}

	.price {
		font-size: 40rpx;
		font-weight: bold;
		margin-top: 10rpx;

		text {
			font-size: 28rpx;
			font-weight: 500;
			margin-left: 6rpx;
		}
	}

	.boxType {
		display: flex;
		align-items: center;
		justify-content: space-between;

		.zhifuSlect_btn {
			justify-content: center;
			border-radius: 15rpx;
			padding: 20rpx;
			display: flex;
			background-color: #FFFFFF;
			align-items: center;
			color: #000000;
			flex: 1;
			margin: 0 15rpx;

			image {
				width: 50rpx;
				height: 50rpx;
				border-radius: 50rpx;
			}
		}

	}

	.tit {
		color: #999999;
		font-size: 28rpx;
		margin-top: 20rpx;
	}

	.btn {
		margin: 60rpx 0;
		height: 90rpx;
		background: #3578FE;
		border-radius: 55rpx;
		 
		font-weight: 500;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.popup {
		position: relative;
		width: 500rpx;
		padding: 30rpx;

		.popup_titname {
			font-size: 34rpx;
			font-weight: bold;
			text-align: center;
		}

		.popup_tit {
			color: red;
			text-align: center;
			padding: 20rpx 0;

		}

		.popup_close {
			position: absolute;
			top: 20rpx;
			right: 30rpx;

			image {
				width: 42rpx;
				height: 42rpx;
			}
		}

		.popup_Image {
			width: 400rpx;
			height: 400rpx;
			margin: 10rpx auto;

			image {
				width: 400rpx;
				height: 400rpx;
				border-radius: 12rpx;
			}
		}

		.popup_bth {
			height: 80rpx;
			background: #3578FE;
			border-radius: 26rpx;
			 
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 40rpx;
		}
	}

	.boxbot {
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 28rpx;
		margin: 0 30rpx;
	}
</style>