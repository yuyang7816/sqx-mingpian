<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">订单中心</h2>
			<p class="nf-page-hero__desc">查看平台交易订单，支持按订单类型、状态、订单号筛选，可跳转用户详情查看买卖双方信息</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">订单类型</span>
					<el-select v-model="type" class="nf-filter-form__input" @change="animeDat()">
						<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="status" class="nf-filter-form__input" @change="animeDat(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">订单号</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="select"
						placeholder="请输入订单号" v-model="orderNum"></el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="订单中心" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="orderId" label="编号" width="80" fixed="left">
						</el-table-column>
						<el-table-column prop="ordersUserName" label="订单用户" fixed="left">
							<template slot-scope="scope">
								<span class="nf-table-link"
									@click="updates(scope.row.buyerId)">{{ scope.row.buyerName }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="userName" label="卖家用户">
							<template slot-scope="scope">
								<span class="nf-table-link"
									@click="updates(scope.row.lastOwnerId)">{{ scope.row.lastOwnerName }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="userName" label="买家用户">
							<template slot-scope="scope">
								<span class="nf-table-link"
									@click="updates(scope.row.buyerId)">{{ scope.row.buyerName }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="userName" label="铸造人">
							<template slot-scope="scope">
								<span class="nf-table-link" v-if="scope.row.box"
									@click="updates(scope.row.box.originalId)">{{ scope.row.box?scope.row.box.originalName:'' }}</span>
								<span class="nf-table-link" v-else
									@click="updates(scope.row.originalId)">{{ scope.row?scope.row.originalName:'' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="orderNumber" label="订单编号" width="180">
						</el-table-column>
						<el-table-column prop="orderType" label="订单类型">
							<template slot-scope="scope">
								<span v-if="scope.row.orderType==1">出售</span>
								<span v-if="scope.row.orderType==2">盲盒</span>
								<span v-if="scope.row.orderType==3">拍卖</span>
								<span v-if="scope.row.orderType==4">转赠</span>
								<span v-if="scope.row.orderType==5">预售</span>
							</template>
						</el-table-column>
						<el-table-column prop="collection.boxName" label="作品名称">
							<template slot-scope="scope">
								<span v-if="scope.row.orderType==2">{{scope.row.box.boxName}}</span>
								<span v-else>{{scope.row.collection?scope.row.collection.nftName:''}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="nftTitleImg" label="封面图" width="100">
							<template slot-scope="scope">
								<block v-if="scope.row.orderType==2">
									<img v-if="scope.row.box&&scope.row.box.nftTitleImg" :src="scope.row.box.nftTitleImg"
										width="40" height="40" />
									<span v-else>暂无图片</span>
								</block>
								<block v-else>
									<img v-if="scope.row.collection&&scope.row.collection.nftTitleImg"
										:src="scope.row.collection.nftTitleImg" width="40" height="40" />
									<span v-else>暂无图片</span>
								</block>
							</template>
						</el-table-column>
						<el-table-column prop="nftImg" label="轮播图" width="200">
							<template slot-scope="scope">
								<block v-if="scope.row.orderType==2">
									<div v-if="scope.row.box&&scope.row.box.boxImg">
										<div v-for="(item,index) in scope.row.box.boxImg.split(',')" :key="index"
											style="display: inline-block; margin: 3px;">
											<el-popover placement="top-start" title="" trigger="hover">
												<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
												<img style="width: 300px; height: auto" :src="item" alt="">
											</el-popover>
										</div>
									</div>
								</block>
								<block v-else>
									<div v-if="scope.row.collection&&scope.row.collection.nftImg">
										<div v-for="(item,index) in scope.row.collection.nftImg.split(',')" :key="index"
											style="display: inline-block; margin: 3px;">
											<el-popover placement="top-start" title="" trigger="hover">
												<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
												<img style="width: 300px; height: auto" :src="item" alt="">
											</el-popover>
										</div>
									</div>
								</block>
							</template>
						</el-table-column>
						<el-table-column prop="num" label="购买数量">
						</el-table-column>
						<el-table-column prop="price" label="价格">
						</el-table-column>
						<el-table-column prop="totalPrice" label="总价">
						</el-table-column>
						<el-table-column prop="payMoney" label="支付金额">
						</el-table-column>
						<el-table-column prop="nftRateMoney" label="版权费">
						</el-table-column>
						<el-table-column prop="serviceCharge" label="服务费">
						</el-table-column>
						<el-table-column prop="sellerAmount" label="卖方最终获得的金额">
						</el-table-column>
						<el-table-column prop="gasMoney" label="gas总费">
						</el-table-column>
						<el-table-column prop="payMoney" label="支付金额">
						</el-table-column>
						<el-table-column prop="payType" label="支付方式">
							<template slot-scope="scope">
								<span v-if="scope.row.payType==0">余额支付</span>
								<span v-if="scope.row.payType==1">微信支付</span>
								<span v-if="scope.row.payType==2">二维码支付</span>
								<span v-if="scope.row.payType==3">公众号支付</span>
								<span v-if="scope.row.payType==4">支付宝支付</span>
								<span v-if="scope.row.payType==5">h5支付宝支付</span>
							</template>
						</el-table-column>
						<el-table-column prop="remarks" label="备注">
						</el-table-column>
						<el-table-column prop="status" label="订单状态" fixed="right">
							<template slot-scope="scope">
								<span style="color: #f56c6c;" v-if="scope.row.status==0">待支付</span>
								<span style="color: #E87D0D;" v-if="scope.row.status==1">已支付</span>
								<span style="color: #009900;" v-if="scope.row.status==2">已完成</span>
								<span style="color: #999;" v-if="scope.row.status==3">已取消</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<!-- <el-table-column prop="createTime" fixed="right" label="操作" width="100">
							<template slot-scope="scope">
								<el-button size="mini" style="margin: 2px;" type="primary"
									:disabled="!isAuth('orderCenter:accomplish')" v-if="scope.row.state==1"
									@click="cancelOrder(scope.row,2)">完成</el-button>
								<el-button size="mini" style="margin: 2px;" type="warning"
									:disabled="!isAuth('orderCenter:refund')" v-if="scope.row.state==1"
									@click="cancelOrder(scope.row,3)">退款</el-button>
								<el-button size="mini" style="margin: 2px;" type="danger"
									:disabled="!isAuth('orderCenter:delete')" @click="deletes(scope.row)">删除</el-button>
							</template>
						</el-table-column> -->
					</el-table>
					<div class="nf-table-card__summary">本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				status: '',
				orderNum: '',
				type: '',
				id: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待支付'
					},
					{
						value: 1,
						label: '已支付'
					},
					{
						value: 2,
						label: '已完成'
					},
					{
						value: 3,
						label: '已取消'
					},
					// {
					// 	value: 4,
					// 	label: '已删除'
					// }
				],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '出售'
					},
					{
						value: 2,
						label: '盲盒'
					},
					{
						value: 3,
						label: '拍卖'
					},
					{
						value: 4,
						label: '转赠'
					},
					{
						value: 5,
						label: '预售'
					},
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: {},
				totalMoney: 0,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.dataSelect()
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('admin/order/getOrderList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'orderType': this.type,
						'status': this.status,
						'orderNumber': this.orderNum
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.records[i].payMoney
						}
					}
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.userName = ''
				this.status = ''
				this.type = ''
				this.orderNum = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/deleteOrders?id=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},
			cancelOrder(row, status) {
				let delid = row.ordersId
				let content = ''
				if (status == 2) {
					content = "确定完成此订单？"
				} else {
					content = "确定退款此订单？"
				}
				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${delid}&status=${status}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},

		},
		mounted() {
			this.dataSelect()
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

	.nf-table-link {
		color: var(--nf-brand, #2f3968);
		cursor: pointer;
	}

	.nf-table-link:hover {
		color: var(--nf-brand-deep, #1f2645);
	}

	.nf-table-card__summary {
		margin-top: 12px;
		font-size: 14px;
		font-weight: 600;
		color: var(--nf-brand, #2f3968);
	}

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>
