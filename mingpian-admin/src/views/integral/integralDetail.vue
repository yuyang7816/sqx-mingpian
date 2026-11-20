<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<a href="#" class="nf-page-hero__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-page-hero__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">积分管理</span>
			<h2 class="nf-page-hero__title">活动详情</h2>
			<p class="nf-page-hero__desc">查看积分推送活动的参与用户明细，包含昵称、活动名称、描述、金额及创建时间</p>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="nickName" label="昵称" width="150">
					<template slot-scope="scope">
						<span class="nf-table-link">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="integralName" label="活动名称" width="150">
				</el-table-column>
				<el-table-column prop="describes" label="活动描述">
				</el-table-column>
				<el-table-column prop="money" label="活动金额">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				tableDataLoading: true,
				tableData: []
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				let sendMoneyId = this.$route.query.sendMoneyId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('integral/selectSendMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sendMoneyId': sendMoneyId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
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

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>
