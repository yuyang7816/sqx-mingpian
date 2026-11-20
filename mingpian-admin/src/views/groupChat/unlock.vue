<template>
	<div>
		<div style="display: inline-block;">
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>支付状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="animeDat2()">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>支付类型：</span>
				<el-select v-model="payType" style="width:150px;margin-left: 10px;" @change="animeDat2()">
					<el-option v-for="item in statesnumType" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>

			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>用户昵称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入用户昵称" v-model="minMoney">
				</el-input>
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>手机号：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入手机号" v-model="maxMoney">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>群组名称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入群组名称"
					v-model="couponName">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>订单编号：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号" v-model="orderNo">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin: 3px;">
				<span>解锁状态：</span>
				<el-select v-model="unlockStatus" style="width:150px;margin-left: 10px;" @change="animeDat2()">
					<el-option v-for="item in homeData1" :key="item.value" :label="item.name" :value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>


			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">刷新
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column prop="id" label="编号" width="80">
				<template slot-scope="scope">
					{{scope.$index+1}}
				</template>
			</el-table-column>
			<el-table-column prop="id" label="编号" width="160">
			</el-table-column>
			<el-table-column prop="userName" label="用户昵称">
				<template slot-scope="scope">
					　<span v-if="scope.row.userName" style="color: #2f3968;cursor: pointer;"
						@click="updatesvideo(scope.row.userId)">
						{{scope.row.userName? scope.row.userName : '未绑定'}}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="手机号">
			</el-table-column>
			<el-table-column prop="groupName" label="群组名称">
			</el-table-column>
			<el-table-column prop="orderNo" label="订单编号">
			</el-table-column>
			<el-table-column prop="payMoney" label="支付金额">
			</el-table-column>
			<el-table-column prop="payType" label="支付类型">
				<template slot-scope="scope">
					<span v-if="scope.row.payType==1" style="color: #2f3968;">支付宝</span>
					<span v-if="scope.row.payType==2" style="color: #f56c6c;">微信</span>
					<span v-if="scope.row.payType==3" style="color: #999;">余额</span>
				</template>
			</el-table-column>
			<el-table-column prop="payStatus" label="支付状态">
				<template slot-scope="scope">
					<span v-if="scope.row.payStatus==0" style="color: #f56c6c;">待支付</span>
					<span v-if="scope.row.payStatus==1" style="color: #2f3968;">支付成功</span>
					<span v-if="scope.row.payStatus==2" style="color: #f56c6c;">支付失败</span>
					<span v-if="scope.row.payStatus==3" style="color: #999;">已退款</span>
				</template>
			</el-table-column>
			<el-table-column prop="unlockStatus" label="解锁状态">
				<template slot-scope="scope">
					<span v-if="scope.row.unlockStatus==1" style="color: #f56c6c;">已解锁</span>
					<span v-else style="color: #2f3968;">未解锁</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间">
			</el-table-column>
			<el-table-column prop="createTime" fixed="right" label="操作" width="100">
				<template slot-scope="scope">
					<el-button size="mini" style="margin: 2px;" type="warning" :disabled="!isAuth('matching:delete')"
						@click="deleteOrder(scope.row)" v-if="scope.row.payStatus==1">退款</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">
			本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
			</el-pagination>
		</div>
		<!-- 任务拒绝弹框 -->
		<el-dialog title="任务拒绝" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				content: '',
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待支付'
					}, {
						value: 1,
						label: '支付成功'
					}, {
						value: 2,
						label: '支付失败'
					}, {
						value: 3,
						label: '已退款'
					},

				],
				payType: '',
				statesnumType: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '支付宝'
					}, {
						value: 2,
						label: '微信'
					}, {
						value: 3,
						label: '余额'
					},

				],
				userName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				couponName: '',
				orderNo: '',
				unlockStatus: '',
				homeData1: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '解锁'
					},
					{
						id: 0,
						name: '未解锁'
					}
				],
				maxMoney: '',
				minMoney: '',
				times: '',
				sex: 0, //1男 2女 不限0
				sexList: [{
						value: 0,
						label: '不限'
					},
					{
						value: 1,
						label: '男'
					}, {
						value: 2,
						label: '女'
					}
				],
				totalMoney: 0,

			}
		},
		methods: {
			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},

			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},



			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.couponName = ''
				this.orderNo = ''
				this.userName = ''
				this.maxMoney = ''
				this.minMoney = ''
				this.status = ''
				this.payType = ''
				this.unlockStatus = ''
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				// if(this.times){}
				this.totalMoney = 0
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('groupUserInfo/unlockGroupListInfo'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.page,
						'pageSize': this.limit,
						'groupName': this.couponName,
						'userName': this.minMoney,
						'phone': this.maxMoney,
						'unlockStatus': this.unlockStatus,
						'payStatus': this.status,
						'orderNo': this.orderNo,
						'payType': this.payType,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.list[i].payMoney
						}
					}
					let returnData = data.data;
					this.tableData = returnData
				})
			},

			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},

			cancelOrder(row, status) {
				let content = '确定取消此订单'

				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityApply/cancelActivityApply'),
						method: 'post',
						params: this.$http.adornParams({
							'activityApplyId': row.activityApplyId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			},
			// 删除
			deleteOrder(row) {
				this.$confirm('确定此订单要退款', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('refund/groupMoney/' + row.orderNo),
						method: 'get',
						params: this.$http.adornParams({
							// 'activityApplyId': row.activityApplyId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>