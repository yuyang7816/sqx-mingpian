<template>
	<div class="nf-data-center-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">数据中心</span>
			<h2 class="nf-page-hero__title">数据概览</h2>
			<p class="nf-page-hero__desc">查看用户、名片、支付与待办等核心指标，支持按维度筛选与趋势分析</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">查询维度</span>
					<el-select v-model="flag" class="nf-filter-form__input" @change="orderfenxi">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">统计日期</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="info.stockDate"
						align="right" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
						@change="animeOrder">
					</el-date-picker>
				</div>
			</div>
		</div>

		<div class="nf-kpi-row">
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__label">总用户数</div>
				<div class="nf-kpi-card__value"><span>{{tableData.totalUsers ? tableData.totalUsers : 0}}</span>人</div>
			</div>
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__label">今日新增</div>
				<div class="nf-kpi-card__value"><span>{{tableData.newToday ? tableData.newToday : 0}}</span>人</div>
			</div>
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__label">本月新增</div>
				<div class="nf-kpi-card__value"><span>{{tableData.newMonth ? tableData.newMonth : 0}}</span>人</div>
			</div>
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__label">本年新增</div>
				<div class="nf-kpi-card__value"><span>{{tableData.newYear ? tableData.newYear : 0}}</span>人</div>
			</div>
		</div>

		<div class="nf-stat-grid">
			<div class="nf-stat-tile">
				<div class="nf-stat-tile__label">总收入</div>
				<div class="nf-stat-tile__value"><span>{{tableData.totalRevenue ? tableData.totalRevenue : 0}}</span>元</div>
			</div>
			<div class="nf-stat-tile">
				<div class="nf-stat-tile__label">今日收入</div>
				<div class="nf-stat-tile__value"><span>{{tableData.todayRevenue ? tableData.todayRevenue : 0}}</span>元</div>
			</div>
			<div class="nf-stat-tile">
				<div class="nf-stat-tile__label">本月收入</div>
				<div class="nf-stat-tile__value"><span>{{tableData.monthRevenue ? tableData.monthRevenue : 0}}</span>元</div>
			</div>
			<div class="nf-stat-tile">
				<div class="nf-stat-tile__label">本年收入</div>
				<div class="nf-stat-tile__value"><span>{{tableData.yearRevenue ? tableData.yearRevenue : 0}}</span>元</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户分析" name="third">
				<div class="nf-table-card">
					<div class="nf-stat-grid nf-stat-grid--compact">
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">总用户统计</div>
							<div class="nf-stat-tile__value"><span>{{userStat.totalNumber ? userStat.totalNumber : 0}}</span>人</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">普通用户</div>
							<div class="nf-stat-tile__value"><span>{{userStat.userNumber ? userStat.userNumber : 0}}</span>人</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">vip用户</div>
							<div class="nf-stat-tile__value"><span>{{userStat.vipUserNumber ? userStat.vipUserNumber : 0}}</span>人</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">H5用户</div>
							<div class="nf-stat-tile__value"><span>{{userStat.h5UserNumber ? userStat.h5UserNumber : 0}}</span>人</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">APP用户</div>
							<div class="nf-stat-tile__value"><span>{{userStat.appUserNumber ? userStat.appUserNumber : 0}}</span>人</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">小程序用户</div>
							<div class="nf-stat-tile__value"><span>{{userStat.maUserNumber ? userStat.maUserNumber : 0}}</span>人</div>
						</div>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="名片统计" name="one">
				<div class="nf-table-card">
					<div class="nf-stat-grid nf-stat-grid--compact">
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">名片总量</div>
							<div class="nf-stat-tile__value"><span>{{tableData2.total ? tableData2.total : 0}}</span></div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">个人名片量</div>
							<div class="nf-stat-tile__value"><span>{{tableData2.user ? tableData2.user : 0}}</span></div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">企业名片量</div>
							<div class="nf-stat-tile__value"><span>{{tableData2.company ? tableData2.company : 0}}</span></div>
						</div>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="支付统计" name="sixth">
				<div class="nf-table-card">
					<div class="nf-stat-grid nf-stat-grid--compact">
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">支付总金额</div>
							<div class="nf-stat-tile__value"><span>{{chongzhiData.allMoney ? chongzhiData.allMoney : 0}}</span>元</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">小程序支付金额</div>
							<div class="nf-stat-tile__value"><span>{{chongzhiData.wxAppMoney ? chongzhiData.wxAppMoney : 0}}</span>元</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">APP支付金额</div>
							<div class="nf-stat-tile__value"><span>{{chongzhiData.appMoney ? chongzhiData.appMoney : 0}}</span>元</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">微信二维码支付金额</div>
							<div class="nf-stat-tile__value"><span>{{chongzhiData.wxRwmMoney ? chongzhiData.wxRwmMoney : 0}}</span>元</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">支付宝APP支付金额</div>
							<div class="nf-stat-tile__value"><span>{{chongzhiData.zfbMoney ? chongzhiData.zfbMoney : 0}}</span>元</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">支付宝H5支付金额</div>
							<div class="nf-stat-tile__value"><span>{{chongzhiData.zfbH5Money ? chongzhiData.zfbH5Money : 0}}</span>元</div>
						</div>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="待审核统计" name="seven">
				<div class="nf-table-card">
					<div class="nf-stat-grid nf-stat-grid--compact">
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">认证待审核</div>
							<div class="nf-stat-tile__value">
								<span class="nf-stat-link" @click="btnDsh(1)">{{chatNumRz ? chatNumRz : 0}}</span>个
							</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">提现待审核</div>
							<div class="nf-stat-tile__value">
								<span class="nf-stat-link" @click="btnDsh(6)">{{chatNumTx ? chatNumTx : 0}}</span>个
							</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">消息待处理</div>
							<div class="nf-stat-tile__value">
								<span class="nf-stat-link" @click="btnDsh(7)">{{chatNumXx ? chatNumXx : 0}}</span>个
							</div>
						</div>
						<div class="nf-stat-tile">
							<div class="nf-stat-tile__label">名片申请待处理</div>
							<div class="nf-stat-tile__value">
								<span class="nf-stat-link" @click="btnDsh(11)">{{chatNumSq ? chatNumSq : 0}}</span>个
							</div>
						</div>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<div class="nf-chart-grid">
			<div class="nf-chart-card">
				<div class="nf-chart-card__head">
					<span class="nf-chart-card__title">名片增长折线图</span>
					<el-date-picker v-model="selectYear" :picker-options="pickerOptions" type="daterange"
						range-separator="至" start-placeholder="开始日期" @change="selectYears"
						end-placeholder="结束日期">
					</el-date-picker>
				</div>
				<div id="Chart1" class="nf-chart-card__body" v-loading="tableDataLoading1"></div>
			</div>
			<div class="nf-chart-card">
				<div class="nf-chart-card__head">
					<span class="nf-chart-card__title">用户增长折线图</span>
					<el-date-picker v-model="selectDayDate" :picker-options="pickerOptions" type="daterange"
						range-separator="至" start-placeholder="开始日期" @change="selectTime"
						end-placeholder="结束日期">
					</el-date-picker>
				</div>
				<div id="Chart2" class="nf-chart-card__body" v-loading="tableDataLoading2"></div>
			</div>
		</div>
	</div>
</template>

<script>
	// 使用 index.html / init.js 中 CDN 引入的全局 echarts（webpack externals）
	import echarts from 'echarts'
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				tableData: {},
				tableData2: {
					list: [],
					totalCount: 0
				},
				colonelData: [],
				commodityData: [],
				userjfData: [],
				incomeData: [],
				activeName: 'third',
				tableDataLoading: true,
				flag: 1,
				usersumData: {},
				taskStat: {},
				data: '',
				time: '',
				platform: '',
				type: '',
				balance: -1,
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				rechgeData: {},
				chongzhiData: {},
				userStat: {},

				chatNumRz: 0,
				chatNumDp: 0,
				chatNumFw: 0,
				chatNumDls: 0,
				chatNumHyzx: 0,
				chatNumTx: 0,
				chatNumXx: 0,
				chatNumMp: 0,
				chatNumQz: 0,
				chatNumDh:0,
				chatNumSq:0,
				
				// 图表----------
				tableDataLoading1: true,
				tableDataLoading2: true,
				tableDataLoading3: false,
				tableDataLoading4: false,
				tableDataLoading5: false,
				tableDataLoading6: false,
				tableDataLoading7: false,
				tableDataLoading8: false,
				tableDataLoading9: false,
				
				orderEcharts: {}, //订单折线图
				increaseEcharts: {}, //用户增长折线图
				selectDayDate: '',
				pickerOptions: {
					onPick: ({
						maxDate,
						minDate
					}) => {
						// 把选择的第一个日期赋值给一个变量。
						this.choiceDate = minDate.getTime()
						// 如何你选择了两个日期了，就把那个变量置空
						if (maxDate) this.choiceDate = ''
					},
					disabledDate: time => {
						// 如何选择了一个日期
						if (this.choiceDate) {
							// 7天的时间戳
							const one = 6 * 24 * 3600 * 1000
							// 当前日期 - one = 7天之前
							const minTime = this.choiceDate - one
							// 当前日期 + one = 7天之后
							const maxTime = this.choiceDate + one
							return (
								time.getTime() < minTime ||
								time.getTime() > maxTime
							)
						} else {
							return false
						}
					}
				},
				bodyStyle: {
					padding: '0'
				},
				selectYear: '',
				qiehuan_type: 1,
				lirunStat: {},
			}
		},
		mounted() {
			// this.colonel()
			// this.taskData()
			this._getData() //用户统计
			this.userData()
			// this.incomeSelect()//收入
			this.getUserOrderEcharts();
			this.getUserIncreaseEcharts()
			
			
			setInterval(() => {
				if (this.$cookie.get('numberFw')) {
					this.chatNumFw = this.$cookie.get('numberFw')
				}
				if (this.$cookie.get('numberDp')) {
					this.chatNumDp = this.$cookie.get('numberDp')
				}
				if (this.$cookie.get('numberRz')) {
					this.chatNumRz = this.$cookie.get('numberRz')
				}
				if (this.$cookie.get('numberDls')) {
					this.chatNumDls = this.$cookie.get('numberDls')
				}
				if (this.$cookie.get('numberHyzx') || this.$cookie.get('numberPl')) {
					this.chatNumHyzx = Number(this.$cookie.get('numberHyzx')) + Number(this.$cookie.get(
						'numberPl'))
				}
				if (this.$cookie.get('numberTx')) {
					this.chatNumTx = this.$cookie.get('numberTx')
				}
				if (this.$cookie.get('numberXx1') || this.$cookie.get('numberXx2') || this.$cookie.get(
						'numberXx3')) {
					this.chatNumXx = Number(this.$cookie.get('numberXx1')) + Number(this.$cookie.get(
						'numberXx2')) + Number(this.$cookie.get('numberXx3'))
				}
				if (this.$cookie.get('numberMp')) {
					this.chatNumMp = this.$cookie.get('numberMp')
				}
				if (this.$cookie.get('numberQz')) {
					this.chatNumQz = this.$cookie.get('numberQz')
				}
				if (this.$cookie.get('numberDh')|| this.$cookie.get('numberDhTk')) {
					this.chatNumDh = Number(this.$cookie.get('numberDh'))+Number(this.$cookie.get('numberDhTk'))
				}
				if (this.$cookie.get('numberSq')) {
					this.chatNumSq = this.$cookie.get('numberSq')
				}
				
				
			}, 1000)
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updates1(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId1
					}
				})
			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId2
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.colonel();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.colonel();
			},
			// 用户分析选择日期
			animeDat() {
				this._getData()
			},
			// 订单分析选择日期
			animeOrder() {
				// this.taskData()
				this.colonel()
				this.userData()
				this.rechSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					// this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					// this.taskData()
					// this.colonel()
					this.userData()
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					// this.taskData()
					// this.colonel()
					this.userData()
					this.rechSelect()
				}
			},
			// Tabs点击事件
			handleClick(tab, event) {
				if (tab._props.label == '购买分析') {
					this.taskData()
				}
				if (tab._props.label == '名片统计') {
					this.colonel()
				}
				if (tab._props.label == '用户分析') {
					this.info.stockDate = this.getNowTime()
					this.userData()

				}
				if (tab._props.label == '支付统计') {
					this.info.stockDate = this.getNowTime()
					this.rechSelect()
				}
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 用户统计
			_getData() {
				this.$http({
					url: this.$http.adornUrl('user/homeMessage'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			// 收入统计
			//  incomeSelect(){
			//  this.$http({
			// url: this.$http.adornUrl('statistical/income'),
			// method: 'get',
			// params: this.$http.adornParams({
			// })
			//  }).then(({data}) => {
			//   if(data.code == 0){
			// 	  let returnData = data.data;
			// 	  this.incomeData = returnData
			//   }
			//  })
			//  },
			//购买分析
			taskData() {
				this.$http({
					url: this.$http.adornUrl('admin/order/buyAnalysis'),
					method: 'get',
					params: this.$http.adornParams({
						'dateTime': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.rechgeData = returnData
				})
			},
			// 名片统计 分析
			colonel() {
				this.tableDataLoading = true
				if( this.flag==1){
					var flags = 'day'
				}
				if( this.flag==2){
					var flags = 'month'
				}
				if( this.flag==3){
					var flags = 'year'
				}
				this.$http({
					url: this.$http.adornUrl('admin/dataCenter/card'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'findDate': this.info.stockDate,
						'findType': flags,
						'type':1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.tableData2 = returnData
					}
				})
			},
			// 用户分析
			userData(platform, type) {
				this.$http({
					url: this.$http.adornUrl('user/userMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.userStat = returnData

				})
			},
			// 充值统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl('user/payMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.chongzhiData = returnData
					}
				})
			},
			// 待审核跳转
			btnDsh(index) {
				if (index == 1) {
					this.$router.replace({
						name: 'authentication',
						query: {
							status: 0
						}
					})
				}
				if (index == 2) {
					this.$router.replace({
						name: 'businessCard',
						query: {
							status: 0
						}
					})
				}
				if (index == 3) {
					this.$router.replace({
						name: 'shopManage',
						query: {
							status: 0
						}
					})
				}
				if (index == 4) {
					this.$router.replace({
						name: 'agent',
						query: {
							status: 0
						}
					})
				}
				if (index == 5) {
					this.$router.replace({
						name: 'information',
						query: {
							status: 0
						}
					})
				}
				if (index == 6) {
					this.$router.replace({
						name: 'financeList',
						query: {
							status: 0
						}
					})
				}
				if (index == 7) {
					this.$router.replace({
						name: 'message',
						query: {}
					})
				}
				if (index == 8) {
					this.$router.replace({
						name: 'callingCard',
						query: {
							status: 1
						}
					})
				}
				if (index == 9) {
					this.$router.replace({
						name: 'crowd',
						query: {
							status: 1
						}
					})
				}
				if (index == 10) {
					this.$router.replace({
						name: 'GiftOrder',
						query: {
							status: 2
						}
					})
				}
				if (index == 11) {
					this.$router.replace({
						name: 'messageSq',
						query: {
							status: 1
						}
					})
				}


			},
			
			//名片增长折线图
			getUserOrderEcharts(startTime, endTime) {
				this.tableDataLoading1 = true
				var times = this.info.stockDate.slice(0, 10)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					// var now = Date.parse(new Date())
					// var oldD = new Date(now)
					// // var year = oldD.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// // this.selectYear = year
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;
			
					var startTime = defaultDate
					var endTime = times
					this.selectYear = [startTime, endTime]
				}
			
				this.$http({
					url: this.$http.adornUrl('admin/dataCenter/cardLine'),
					method: 'get',
					params: this.$http.adornParams({
						// 'time': year ? year : '',
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.orderEcharts = data.data
						//使用数据初始化图表
						this.setEcharts()
						this.tableDataLoading1 = false
					}
					// this.dataInfo = data.data
				})
			},
			//用户增长折线图
			getUserIncreaseEcharts(startTime, endTime) {
				this.tableDataLoading2 = true
				var times = this.info.stockDate.slice(0, 10)
				console.log('times', times)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;
			
					var startTime = defaultDate
					var endTime = times
					this.selectDayDate = [startTime, endTime]
				}
				this.$http({
					url: this.$http.adornUrl('admin/dataCenter/userLine'),
					method: 'get',
					params: this.$http.adornParams({
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.increaseEcharts = data.data
						//使用数据初始化图表
						this.setEcharts2()
						this.tableDataLoading2 = false
					}
					// this.dataInfo = data.data
				})
			},
			//使用数据初始化图表
			setEcharts() {
				var chartDom = document.getElementById('Chart1');
				var myChart = echarts.init(chartDom);
				var option;
			
				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['企业名片量', '用户名片量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},
			
						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.orderEcharts.xdate
					},
					yAxis: {
						type: 'value'
					},
					series: [{
							name: '企业名片量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.cseries
						},
						{
							name: '用户名片量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.useries
						},
						
			
					]
				};
			
				option && myChart.setOption(option);
			},
			//使用数据初始化图表
			setEcharts2() {
				var chartDom = document.getElementById('Chart2');
				var myChart = echarts.init(chartDom);
				var option;
			
				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['用户增长量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},
			
						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.increaseEcharts.xdate
					},
					yAxis: {
						type: 'value'
					},
					series: [{
						name: '用户增长量',
						type: 'line',
						stack: 'Total',
						data: this.increaseEcharts.series
					}, ]
				};
			
				option && myChart.setOption(option);
			},
			//根据年份筛选
			selectYears(e) {
				console.log(e, '`````````', this.info.stockDate)
				if (e == null) {
					this.getUserOrderEcharts()
				} else {
					// var now = new Date();
					// // var year = now.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// let startTime = (new Date(e)).getTime() / 1000;
					// this.getUserOrderEcharts(year)
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;
			
					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;
			
					let startTime = defaultDate
					let endTime = defaultDate1
					console.log(startTime, '`````````', endTime)
					this.getUserOrderEcharts(startTime, endTime)
				}
			},
			//根据日期区间筛选
			selectTime(e) {
				console.log(e)
				if (e == null) {
					this.getUserIncreaseEcharts()
				} else {
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;
			
					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;
			
					let startTime = defaultDate
					let endTime = defaultDate1
			
					// let startTime = (new Date(e[0])).getTime() / 1000;
					// let endTime = (new Date(e[1])).getTime() / 1000;
					this.getUserIncreaseEcharts(startTime, endTime)
				}
			},
			
		}
	}
</script>

<style scoped>
	.nf-data-center-page {
		padding: 0;
		overflow-x: hidden;
	}

	.nf-filter-form {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		gap: 12px;
	}

	.nf-filter-form__item {
		display: inline-flex;
		align-items: center;
		gap: 8px;
	}

	.nf-filter-form__label {
		flex-shrink: 0;
		font-size: 13px;
		color: var(--nf-text-regular, #303133);
	}

	.nf-filter-form__input {
		width: 150px;
	}

	.nf-filter-form__input--date {
		width: 200px;
	}

	.nf-kpi-row {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 12px;
		margin-bottom: 12px;
	}

	.nf-kpi-card {
		padding: 16px;
		background: #fff;
		border-radius: 8px;
		border: 1px solid var(--nf-border, #e8eaed);
		box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
	}

	.nf-kpi-card__label {
		font-size: 13px;
		color: var(--nf-text-regular, #303133);
		margin-bottom: 8px;
	}

	.nf-kpi-card__value {
		font-size: 14px;
		color: var(--nf-text-secondary, #909399);
	}

	.nf-kpi-card__value span {
		font-size: 22px;
		font-weight: 600;
		color: var(--nf-brand, #2f3968);
		margin-right: 4px;
	}

	.nf-stat-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 12px;
		margin-bottom: 12px;
	}

	.nf-stat-grid--compact {
		margin-bottom: 0;
	}

	.nf-stat-tile {
		padding: 14px 16px;
		background: var(--nf-sidebar-bg-alt, #f7f8fa);
		border-radius: 8px;
		border: 1px solid var(--nf-border, #e8eaed);
	}

	.nf-stat-tile__label {
		font-size: 13px;
		color: var(--nf-text-regular, #303133);
		margin-bottom: 6px;
	}

	.nf-stat-tile__value {
		font-size: 14px;
		color: var(--nf-text-secondary, #909399);
	}

	.nf-stat-tile__value span {
		font-size: 20px;
		font-weight: 600;
		color: var(--nf-brand, #2f3968);
		margin-right: 4px;
	}

	.nf-stat-link {
		color: #f56c6c;
		cursor: pointer;
		font-size: 20px;
		font-weight: 600;
		margin-right: 4px;
	}

	.nf-stat-link:hover {
		color: #f78989;
	}

	.nf-page-tabs {
		margin-bottom: 0;
	}

	.nf-page-tabs >>> .el-tabs__header {
		margin-bottom: 0;
	}

	.nf-page-tabs >>> .el-tabs__content {
		padding: 0;
	}

	.nf-table-card {
		margin-top: 0;
		margin-bottom: 12px;
		padding: 16px;
	}

	.nf-chart-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 12px;
		margin-bottom: 12px;
	}

	.nf-chart-card {
		background: #fff;
		border-radius: 8px;
		border: 1px solid var(--nf-border, #e8eaed);
		box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
		overflow: hidden;
	}

	.nf-chart-card__head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 12px;
		padding: 12px 16px;
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		background: var(--nf-sidebar-bg-alt, #f7f8fa);
	}

	.nf-chart-card__title {
		font-size: 14px;
		font-weight: 600;
		color: var(--nf-text-primary, #1a2433);
		flex-shrink: 0;
	}

	.nf-chart-card__body {
		width: 100%;
		height: 300px;
		padding: 8px;
		box-sizing: border-box;
	}
</style>