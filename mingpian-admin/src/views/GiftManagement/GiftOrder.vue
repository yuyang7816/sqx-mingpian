<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">礼品管理</span>
			<h2 class="nf-page-hero__title">兑换订单</h2>
			<p class="nf-page-hero__desc">管理礼品兑换订单，支持按订单状态、订单号、手机号、商品标题及时间筛选，可发货、退款与导出</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">订单状态</span>
					<el-select v-model="statusT" class="nf-filter-form__input" placeholder="请选择订单状态" @change="select()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">订单号</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入订单号"
						v-model="orderNum"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">手机号</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="mobile">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">商品标题</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="select" clearable
						placeholder="请输入商品标题" v-model="title">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">开始时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">截止时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button type="primary" icon="document" @click="exportBtn">导出</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="订单中心" name="first">
				<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData">
				<el-table-column fixed prop="img" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.img" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="detailJson" label="规格" width="200">
				</el-table-column>
				<el-table-column prop="goodsType.name" label="商品类型">
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.price}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="number" label="商品个数">
				</el-table-column>
				<el-table-column prop="descrition" label="订单备注">
					<template slot-scope="scope">
						<span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNum" label="订单号" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.orderNum}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="consignee" label="收货人" width="120">
				</el-table-column>
				<el-table-column prop="isExpress" label="是否虚拟商品" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.isExpress == 1">否</span>
						<span v-else>是</span>
					</template>
				</el-table-column>
				<el-table-column prop="mobile" label="手机号" width="120">
				</el-table-column>
				<el-table-column prop="detail" label="详细地址" width="120">
					<template slot-scope="scope">
						<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="postagePrice" label="运费" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.postagePrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="expressName" label="快递公司" width="120">
				</el-table-column>
				<el-table-column prop="expressNumber" label="快递单号" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.expressNumber?scope.row.expressNumber:''}}</span>
						<el-button style="margin: 15px 0px;" size="mini" type="info" icon="document"
							v-if="scope.row.expressNumber"
							@click="refresh(scope.row.expressNumber,scope.row.mobile)">查看物流信息
						</el-button>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="refund" label="退款原因" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.refund ? scope.row.refund : '未退款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="refusedRefund" label="驳回原因" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.refusedRefund ? scope.row.refusedRefund : '未拒绝'}}</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="payWay" label="支付方式">
					<template slot-scope="scope">
						<span style="color: #2f3968;" v-if="scope.row.payWay === 1 ">app微信</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === 2 ">微信公众号</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === 3 ">微信小程序</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === 4 ">app支付宝</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === 6 ">金币</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === 7 ">积分</span>
						<span style="color: #2f3968;" v-if="scope.row.payWay === null ">未支付</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #2f3968;" v-if="scope.row.status === 1 ">待付款</span>
						<span style="color: #2f3968;" v-if="scope.row.status === 2 ">待发货 </span>
						<span style="color: #999999;" v-if="scope.row.status === 3 ">已发货 </span>
						<span style="color: #999999;" v-if="scope.row.status === 4 ">已收货</span>
						<span style="color: #999999;" v-if="scope.row.status === 5 ">已取消</span>
						<span style="color: #2f3968;" v-if="scope.row.status === 6 ">待退款</span>
						<span style="color: #999999;" v-if="scope.row.status === 7 ">已退款</span>
						<span style="color: #999999;" v-if="scope.row.status === 8 ">拒绝退款</span>
						<span style="color: #f56c6c;" v-if="scope.row.status === 9 ">拼团中</span>
						<span style="color: #999999;" v-if="scope.row.status === 10 ">已评价</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="finishTime" label="收货时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.finishTime ? scope.row.finishTime : '未收货'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)" style="margin: 3px;">详情
						</el-button> -->
						<el-button size="mini" type="danger" :disabled="!isAuth('GiftOrder:refund')"
							style="margin: 3px;" v-if="scope.row.status === 6 " @click="refundClick(scope.row)">退款
						</el-button>
						<el-button size="mini" type="primary" v-if="scope.row.status === 2 "
							:disabled="!isAuth('GiftOrder:deliver')" style="margin: 3px;"
							@click="colonelClick(scope.row)">发货
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 发货弹框 -->
		<el-dialog title="发货" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;" v-if="isExpress==1">
				<span style="width: 200px;display: inline-block;text-align: right;">快递公司：</span>
				<el-input style="width:50%;" v-model="expressName" placeholder="请输入快递公司"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="isExpress==1">
				<span style="width: 200px;display: inline-block;text-align: right;">快递单号：</span>
				<el-input style="width: 50%;" v-model="expressNumber" placeholder="请输入快递单号"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="isExpress!=1">
				<span style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 94px;">核销码：</span>
				<el-input style="width: 50%;" v-model="expressNumber" type="textarea" :rows="5" placeholder="请输入核销码"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="deliverNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		
		<el-dialog title="物流信息" :visible.sync="dialogVisible" width="60%">
			<!-- <div class="shxx">物流信息</div> -->
			<div class="block" v-if="wuliuDate.result">
				<!-- <el-timeline>
					<el-timeline-item timestamp="2018/4/3" placement="top" name="123" v-for="(activity, index) in wuliuDate.result.list" :key="index"
						:timestamp="activity.time" :type="activity.type" :icon="activity.icon">
						{{activity.status}}
					</el-timeline-item>
				</el-timeline> -->

				<el-steps direction="vertical" :active="1" :reverse="reverse">
					<el-step v-for="(activity, index) in wuliuDate.result.list" :key="index" :title="activity.time"
						:description="activity.status"></el-step>

				</el-steps>
			</div>
			<div class="block" v-else>
				暂无物流信息
			</div>
		</el-dialog>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				id: '',
				title: '',
				mobile: '',
				orderNum: '',
				status: 0,
				statusT:0,
				sumincome: '',
				totalnum: 0,
				income: '',
				wxincome: '',
				zfbincome: '',
				yueicome: '',
				ordersum0: '',
				ordersum1: '',
				ordersum2: '',
				expressName: '',
				expressNumber: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				way: 3,
				ways: [{
					value: 3,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 1,
					label: '按年查询'
				}],
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '待付款'
					},
					{
						value: 2,
						label: '待发货'
					},
					{
						value: 3,
						label: '已发货'
					},
					{
						value: 4,
						label: '已收货'
					},
					{
						value: 5,
						label: '已取消'
					},
					{
						value: 6,
						label: '待退款'
					},
					{
						value: 7,
						label: '已退款'
					},
					{
						value: 8,
						label: '拒绝退款'
					},
					{
						value: 9,
						label: '拼团中'
					},
					{
						value: 10,
						label: '已评价'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				fenxiaoData: [],
				userId: '',
				phone: '',
				statusnum: [{
					value: 0,
					label: '全部'
				}, {
					value: 1,
					label: '未到账'
				}, {
					value: 2,
					label: '已到账'
				}],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				dialogVisible: false,
				wuliuDate: {},
				kdName: '快递单号',
				isExpress:'',
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 发货
			colonelClick(rows) {
				this.isExpress = rows.isExpress
				if (rows.status == 2) {
					this.dialogFormVisible = true
					this.id = rows.id
				} else {
					this.$message({
						message: '只有待发货订单才能发货',
						type: 'error',
						duration: 2500
					})
				}
			},
			deliverNoticeTo() {
				if(this.isExpress==1){
					if (this.expressName == '') {
						this.$message({
							message: '请输入快递公司',
							type: 'error',
							duration: 1500
						})
						return
					}
					if (this.expressNumber == '') {
						this.$message({
							message: '请输入快递单号',
							type: 'error',
							duration: 1500
						})
						return
					}
					var datas={
						'id': this.id,
						'expressName': this.expressName,
						'expressNumber': this.expressNumber,
					}
				}else{
					if (this.expressNumber == '') {
						this.$message({
							message: '请输入核销码',
							type: 'error',
							duration: 1500
						})
						return
					}
					var datas={
						'id': this.id,
						'virtualContent':this.expressNumber,
					}
				}
				
				this.$http({
					url: this.$http.adornUrl('shopOrders/express'),
					method: 'get',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible = false
						let returnData = data.data;
						this.orderName = returnData.expressName
						this.orderNumber = returnData.expressNumber
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.expressName = ''
								this.expressNumber = ''
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
				})
			},
			// 详情
			orderDetails(id) {
				this.$router.push({
					path: '/orderDetailsZy',
					query: {
						id: id
					}
				})
			},
			// //处理默认选中当前日期
			// getNowTime() {
			// 	var now = new Date()
			// 	var year = now.getFullYear() //得到年份
			// 	var month = now.getMonth() //得到月份
			// 	var date = now.getDate() //得到日期
			// 	month = month + 1
			// 	month = month.toString().padStart(2, '0')
			// 	date = date.toString().padStart(2, '0')
			// 	var defaultDate = `${year}-${month}-${date}`
			// 	return defaultDate
			// 	this.$set(this.info, 'stockDate', defaultDate)
			// },
			// 日期选择
			animeDat(value) {
				let vanumber = value
				this.moneySelect()
				this.orderSelect()
				// if (vanumber == 3) {
				// 	this.moneySelect()
				// 	this.orderSelect()
				// }
				// if (vanumber == 2) {
				// 	// this.info.stockDate = this.info.stockDate.substring(0, 7)
				// 	this.moneySelect()
				// 	this.orderSelect()
				// 	// this.info.stockDate = this.getNowTime()
				// }
				// if (vanumber == 1) {
				// 	// this.info.stockDate = this.info.stockDate.substring(0, 4)
				// 	this.moneySelect()
				// 	this.orderSelect()
				// 	// this.info.stockDate = this.getNowTime()
				// }
			},
			timeDate() {
				this.moneySelect()
				this.orderSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			
			handleClick(tab, event) {
				if (tab._props.label == '订单中心') {
					this.page = 1
					this.size = 10
					this.dataSelect()
				}
				
			},
			//搜索
			select() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.orderNum = ''
				this.title = ''
				this.mobile = ''
				this.statusT = 0
				this.startTime = ''
				this.endTime = ''
				this.dataSelect()
			},

			// 退款
			refundClick(row) {
				if (row.status != 1 && row.status != 5 && row.status != 7 && row.status != 8) {
					this.$confirm(`确定退款?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl('shopOrders/refundMoney'),
							method: 'get',
							params: this.$http.adornParams({
								'ordersId': row.id
							})
						}).then(({
							data
						}) => {
							if (data.status == 0) {
								this.$message({
									message: '退款成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.dataSelect()
									}
								})
							}
							if (data.status == -1) {
								this.$message({
									message: data.msg,
									type: 'error',
									duration: 1500,
									onClose: () => {
										this.dataSelect()
									}
								})
							}
						})
					}).catch(() => {})
				} else {
					this.$message({
						message: '只有状态为待退款订单才能退款',
						type: 'error',
						duration: 2500
					})
				}
			},
			// 获取订单数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('shopOrders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'orderNum': this.orderNum,
						'title': this.title,
						'mobile': this.mobile,
						'status': this.statusT,
						'startTime': this.startTime,
						'endTime': endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 收入金额统计
			moneySelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopOrders/income'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
						'way': this.way
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.sumincome = returnData.sum0
					this.income = returnData.sum1
					this.wxincome = returnData.pay1
					this.zfbincome = returnData.pay2
					this.yueicome = returnData.pay3
				})
			},
			// 订单成交量统计
			orderSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopOrders/statistical'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.ordersum0 = returnData.sum0
					this.ordersum1 = returnData.sum1
					this.ordersum2 = returnData.sum2

				})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('shopOrders/listExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'orderNum': this.orderNum,
						'title': this.title,
						'mobile': this.mobile,
						'status': this.statusT,
						'startTime': this.startTime,
						'endTime': endTime

					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 查看物流信息
			refresh(expressNumber, mobile) {
				this.wuliu(expressNumber, mobile)
				this.dialogVisible = true
			},
			// 获取物流信息
			wuliu(expressNumber, mobile) {
				console.log('`````````', expressNumber)
				this.$http({
					url: this.$http.adornUrl('shopOrders/findExpress'),
					// url:'https://pcshop.xianmxkj.com/tao/orders/findExpress',
					method: 'get',
					params: this.$http.adornParams({
						'expressNumber': expressNumber,
						// 'mobile': mobile
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.wuliuDate = data.data;
						this.wuliuDate.result.list[0].icon = 'el-icon-success'
						this.wuliuDate.result.list[0].type = 'success'
						this.kdName = this.wuliuDate.result.expName
					} else {
						this.$message(data.msg);
					}




				})
			},
			dataSelect1(){
				if (this.$route.query.status != null) {
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = 0
				}
				this.page = 1
				this.dataSelect()
			}
		},
		mounted() {
			if (this.$route.query.status != null) {
				this.statusT = Number(this.$route.query.status)
			} else {
				this.statusT = 0
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	};
</script>
<style scoped="scoped">
	.nf-list-page {
		padding: 0;
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

	.nf-filter-form__input--wide {
		width: 200px;
	}

	.nf-filter-form__input--date {
		width: 180px;
	}

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		flex-wrap: wrap;
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
		margin-bottom: 0;
		padding: 16px;
	}

	.nf-table-card >>> .el-table th {
		background: var(--nf-sidebar-bg-alt, #f7f8fa);
		color: var(--nf-text-primary, #1a2433);
		font-weight: 600;
	}

	.nf-table-card >>> .el-table td,
	.nf-table-card >>> .el-table th {
		border-color: var(--nf-border, #e8eaed);
	}

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>