<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<a href="#" class="nf-page-hero__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-page-hero__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">任务管理</span>
			<h2 class="nf-page-hero__title">派单详情</h2>
			<p class="nf-page-hero__desc">查看派单任务基本信息与接单记录，支持查看接单人提交内容与审核状态</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">任务信息</div>
			<div class="nf-detail-table">
				<table>
					<tbody>
						<tr>
							<th>用户名称</th>
							<td>{{tableData.nickName}}</td>
							<th>标题</th>
							<td>{{tableData.title}}</td>
							<th class="nf-detail-table__th-wide">任务要求</th>
							<td class="border-rt">{{tableData.content}}</td>
						</tr>
						<tr>
							<th>验证信息</th>
							<td>{{tableData.verifyContent}}</td>
							<th>打开内容</th>
							<td class="nf-detail-table__td-wide">{{tableData.openContent}}</td>
							<th>编号</th>
							<td class="border-rt">{{tableData.id}}</td>
						</tr>
						<tr>
							<th>数量</th>
							<td>{{tableData.taskNum}}</td>
							<th>接单数量</th>
							<td>{{tableData.endNum}}</td>
							<th>发布时间</th>
							<td class="border-rt">{{tableData.createTime}}</td>
						</tr>
						<tr>
							<th>截至时间</th>
							<td>{{tableData.endTime}}</td>
							<th>审核时间（按分）</th>
							<td>{{tableData.auditTime}}</td>
							<th>任务限时（按分）</th>
							<td class="border-rt">{{tableData.restrictTime}}</td>
						</tr>
						<tr>
							<th>状态</th>
							<td v-if="tableData.state == 0">待审核</td>
							<td v-if="tableData.state == 1">进行中</td>
							<td v-if="tableData.state == 2">已结束</td>
							<td v-if="tableData.state == 3">已拒绝</td>
							<td v-if="tableData.state == 4">已结算</td>
							<th>打开方式</th>
							<td>
								<span v-if="tableData.openType== 1">链接打开</span>
								<span v-if="tableData.openType== 2">口令打开</span>
								<span v-if="tableData.openType== 3">扫码打开</span>
							</td>
							<th>打开app分类</th>
							<td class="border-rt">{{tableData.openApp}}</td>
						</tr>
						<tr>
							<th class="border-bt">验证信息</th>
							<td class="border-bt">{{tableData.verifyContent}}</td>
							<th class="border-bt">价格(原价)</th>
							<td class="border-bt">{{tableData.taskOriginalPrice}}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName">
			<el-tab-pane label="接单列表" name="first">
				<div class="nf-table-card">
					<el-table
						v-loading="tableDataLoading"
						:data="taskData.list">
						<el-table-column
							fixed
							prop="id"
							label="接单id">
						</el-table-column>
						<el-table-column
							prop="nickName"
							label="接单人名称"
							width="150">
						</el-table-column>
						<el-table-column
							prop="content"
							label="提交内容"
							width="150">
							<template slot-scope="scope">
								<span>{{scope.row.content}}</span>
							</template>
						</el-table-column>
						<el-table-column
							prop="money"
							label="价格(接单时收取的价格)"
							width="180">
						</el-table-column>
						<el-table-column
							prop="zhiMoney"
							label="直属收益">
						</el-table-column>
						<el-table-column
							prop="feiMoney"
							label="非直属收益"
							width="150">
						</el-table-column>
						<el-table-column
							prop="pingMoney"
							label="平台收益">
						</el-table-column>
						<el-table-column
							prop="auditContent"
							label="拒绝原因"
							width="160">
						</el-table-column>
						<el-table-column
							prop="category"
							label="拒绝类型"
							width="160">
						</el-table-column>
						<el-table-column
							prop="createTime"
							label="创建时间"
							width="160">
						</el-table-column>
						<el-table-column
							prop="auditTime"
							label="审核时间"
							width="160">
						</el-table-column>
						<el-table-column
							fixed='right'
							prop="state"
							label="状态"
							width="100">
							<template slot-scope="scope">
								<span class="nf-state-text" v-if="scope.row.state === 0">接单成功</span>
								<span class="nf-state-text" v-if="scope.row.state === 1">提交待审核</span>
								<span class="nf-state-text" v-if="scope.row.state === 2">审核成功</span>
								<span class="nf-state-text" v-if="scope.row.state === 3">已拒绝</span>
								<span class="nf-state-text" v-if="scope.row.state === 4">已维权</span>
								<span class="nf-state-text" v-if="scope.row.state === 5">已放弃</span>
							</template>
						</el-table-column>
						<el-table-column
							fixed='right'
							label="操作">
							<template slot-scope="scope">
								<el-button
									size="mini"
									type="primary"
									@click="check(scope.$index, scope.row)">查看
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination
							@size-change="handleSizeChange"
							@current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]"
							:page-size="limit"
							:current-page="page"
							layout="total,sizes, prev, pager, next,jumper"
							:total="taskData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 任务查看弹框 -->
		<el-dialog title="查看" :visible.sync="dialogFormVisible" center>
			<div v-for="(item,index) in helpTaskData" :key="index" style="text-align: center;">
				<span>步骤 {{index+1}}</span>
				<p class="red">验证图</p>
				<img :src="item.picture" alt="" width="260" height="460">
			</div>
			<div v-if="helpTaskData == '' " style="text-align: center;">暂无内容</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
	    data() {
	        return {
				state:0,
				limit:5,
				page:1,
				activeName: 'first',
				dialogFormVisible:false,
				tableDataLoading:true,
				tableData:{},
			    taskData:[],
				helpTaskData:[]
	        }
	    },
	    methods: {
			prev(){
				this.$router.back()
			},
			handleSizeChange(val) {
				this.limit = val;
				this.taskSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.taskSelect()
			},
			check(index,rows){
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderDetailsById'),
					method: 'get',
					params: this.$http.adornParams({
						'id':rows.id
					})
				}).then(({data}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.dialogFormVisible = true
						this.helpTaskData = returnData
					}
				})
			},
			dataSelect () {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectTakeOrderDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'id':id
					})
				}).then(({data}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.tableData = returnData;
					}
				})
			},
			taskSelect () {
				this.tableDataLoading = true
				let page = this.page-1
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderByTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'page':page,
						'limit':this.limit,
						'id':id
					})
				}).then(({data}) => {
					if (data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.taskData = returnData;
					}
				})
			},
	    },
		mounted() {
			this.dataSelect()
			this.taskSelect()
		},
	}
</script>

<style scoped="scoped">
	.nf-list-page {
		padding: 0;
	}

	.nf-page-hero__back {
		display: inline-flex;
		align-items: center;
		gap: 4px;
		margin-bottom: 10px;
		font-size: 13px;
		color: var(--nf-brand, #2f3968);
		text-decoration: none;
	}

	.nf-page-hero__back:hover {
		color: var(--nf-brand-deep, #1f2645);
	}

	.nf-page-hero__back-icon {
		width: 1.2em;
		height: 1.2em;
	}

	.nf-detail-table {
		overflow-x: auto;
	}

	.nf-detail-table table {
		width: 100%;
		border-collapse: collapse;
	}

	.nf-detail-table tr {
		border-bottom: 1px solid var(--nf-border, #e8eaed);
	}

	.nf-detail-table th {
		background: var(--nf-sidebar-bg-alt, #f7f8fa);
		padding: 12px 16px;
		border-right: 1px solid var(--nf-border, #e8eaed);
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		color: var(--nf-text-primary, #1a2433);
		font-weight: 600;
		font-size: 13px;
		line-height: 1.5;
		white-space: nowrap;
	}

	.nf-detail-table td {
		padding: 12px 16px;
		border-right: 1px solid var(--nf-border, #e8eaed);
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		color: var(--nf-text-regular, #303133);
		font-size: 13px;
		line-height: 1.5;
		word-break: break-all;
	}

	.nf-detail-table__th-wide {
		width: 180px;
	}

	.nf-detail-table__td-wide {
		width: 180px;
	}

	.border-rt {
		border-right: none !important;
	}

	.border-bt {
		border-bottom: none !important;
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

	.nf-state-text {
		color: var(--nf-brand, #2f3968);
	}

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>
