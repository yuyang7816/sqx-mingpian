<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">任务管理</span>
			<h2 class="nf-page-hero__title">任务配置</h2>
			<p class="nf-page-hero__desc">管理任务相关系统配置项，支持查看配置类型与内容，可编辑更新任务配置参数</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">快捷操作</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="dataSelect">刷新</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="renwuData">
				<el-table-column fixed prop="type" label="编号" align="center" width="80">
				</el-table-column>
				<el-table-column prop="min" label="类型">
				</el-table-column>
				<el-table-column prop="value" label="内容" width="500">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
							@click="renwuAmend(scope.$index, scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 修改任务弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form1">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form1.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<el-input v-model="form1.value" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="renwuNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				state: 1,
				openValue: 1,
				closeValue: 2,
				classify: 1,
				money: '',
				condition: 'renwu',
				activeName: 'first',
				dialogFormVisible: false,
				tableDataLoading: true,
				dialogFormVisible1: false,
				formLabelWidth: '200px',
				tableData: [],
				renwuData: [],
				form: {
					id: '',
					rate: ''
				},
				form1: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect();
			},
			// 修改任务弹框
			renwuAmend(index, rows) {
				this.dialogFormVisible1 = true;
				this.form1.id = rows.id;
				this.form1.type = rows.type
				this.form1.min = rows.min;
				this.form1.value = rows.value;
				this.form1.max = rows.max;
				this.form1.createAt = rows.createAt;
				this.form1.conditionFrom = rows.conditionFrom
			},
			// 修改任务
			renwuNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form1.id,
						'type': this.form1.type,
						'value': this.form1.value,
						'min': this.form1.min,
						'max': this.form1.max,
						'createAt': this.form1.createAt,
						'conditionFrom': this.form1.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.renwuData = returnData
					}
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

	.nf-filter-form {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		gap: 12px;
	}

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		flex-wrap: wrap;
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
