<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">话题论坛</span>
			<h2 class="nf-page-hero__title">动态列表</h2>
			<p class="nf-page-hero__desc">管理公告动态，支持按状态、公告标题筛选查询，可新增、修改与删除</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="status" class="nf-filter-form__input" placeholder="请选择状态"
						@change="selectTrigger(status)">
						<el-option v-for="item in statusList" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">公告标题</span>
					<el-input v-model="title" class="nf-filter-form__input nf-filter-form__input--wide" clearable
						placeholder="请输入公告标题" @keydown.enter.native="select"></el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="select">查询</el-button>
					<el-button icon="document" type="primary" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('dynamic:add')" icon="document" type="primary"
						@click="addOrEditGongGao('新增',{})">新增</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="gongGaoList.records">
				<el-table-column fixed label="编号" width="80">
					<template slot-scope="scope">
						{{ scope.$index + 1 }}
					</template>
				</el-table-column>
				<el-table-column label="公告标题" prop="title" width="120">
				</el-table-column>
				<el-table-column label="公告内容" prop="content">
				</el-table-column>
				<el-table-column label="发布人" prop="userName" width="100">
					<template slot-scope="scope">
						<span class="nf-table-link" @click="updates(scope.row)">{{ scope.row.userName }}</span>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="150">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button :disabled="!isAuth('dynamic:update')" size="mini" type="primary"
							@click="addOrEditGongGao('修改',scope.row)">修改
						</el-button>
						<el-button :disabled="!isAuth('dynamic:delete')" size="mini" type="danger"
							@click="advertdeletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 40]"
					:total="gongGaoList.total" layout="total,sizes, prev, pager, next,jumper"
					@size-change="handleSizeChange" @current-change="handleCurrentChange">
				</el-pagination>
			</div>
		</div>

		<!--新增/修改弹窗-->
		<el-dialog @close="clearFrom" :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
			<el-form ref="dynamicValidateForm" :model="from" class="demo-dynamic" label-width="100px">
				<el-form-item :rules="[
      					{ required: true, message: '请输入公告标题', trigger: 'blur' },
    				]" label="公告标题" prop="title">
					<el-input v-model="from.title"></el-input>
				</el-form-item>
				<el-form-item :rules="[
      					{ required: true, message: '状态', trigger: 'blur' },
    				]" label="状态" prop="status">
					<el-radio-group v-model="from.status">
						<el-radio :label='1'>已发布</el-radio>
						<el-radio :label='0'>未发布</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item :rules="[
      					{ required: true, message: '请输入公告内容', trigger: 'blur' },
    				]" label="公告内容" prop="content">
					<el-input type="textarea" :rows="4" placeholder="请输入公告内容" v-model="from.content">
					</el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="submitForm('dynamicValidateForm')">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				from: {
					title: '',
					content: '',
					status: 1,
					noticeId: '',
				},
				dialogTitle: '',
				dialogVisible: false,
				page: 1,
				limit: 10,
				title: '',
				status: '',
				statusList: [{
						label: '全部',
						value: ''
					},
					{
						label: '未发布',
						value: 0
					}, {
						label: '已发布',
						value: 1
					}
				],
				gongGaoList: {},
				tableDataLoading: false,
			}
		},
		mounted() {
			this.getGongGaoList()
		},
		methods: {
			//删除公告
			advertdeletes(item) {
				this.$confirm('此操作将删除该公告, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/notice/deleteNoticeById'),
						method: 'get',
						params: this.$http.adornParams({
							noticeId: item.noticeId
						})
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: '删除成功',
								type: 'success'
							});
							this.getGongGaoList()
						} else {
							this.$message.error(data.msg);
							this.getGongGaoList()
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			//关闭弹窗后重置表单
			clearFrom() {
				this.$refs['dynamicValidateForm'].resetFields();
				this.from.noticeId = ''
			},
			//提交公告(修改/删除)
			submitForm(formName) {
				let url = ''
				if (this.dialogTitle == '修改') {
					url = 'admin/notice/updateNotice'
				} else {
					url = 'admin/notice/insertNotice'
				}
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(url),
							method: 'post',
							data: this.$http.adornData(this.from)
						}).then(({
							data
						}) => {
							if (data && data.code === 0) {
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.getGongGaoList()
							} else {
								this.$message.error(data.msg);
								this.getGongGaoList()
							}
							this.dialogVisible = false
						})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			//新增/修改公告
			addOrEditGongGao(title, row) {
				this.dialogTitle = title
				if (JSON.stringify(row) !== '{}') {
					this.from = {
						title: row.title,
						content: row.content,
						status: row.status,
						noticeId: row.noticeId,
					}
				}
				this.dialogVisible = true
			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.getGongGaoList()
			},
			handleCurrentChange(val) {
				this.page = val
				this.getGongGaoList()
			},
			//重置查询条件
			cleans() {
				this.status = ''
				this.title = ''
				this.page = 1
				this.getGongGaoList()
			},
			//点击查询按钮查询
			select() {
				this.page = 1
				this.getGongGaoList()
			},
			//状态筛选
			selectTrigger(status) {
				this.page = 1
				this.getGongGaoList()
			},
			// 获取公告数据
			getGongGaoList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/notice/getNoticeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'title': this.title,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					this.gongGaoList = data.data
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
		},

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
		width: 180px;
	}

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
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
