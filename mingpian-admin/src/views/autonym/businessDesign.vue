<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">名片样式</span>
			<h2 class="nf-page-hero__title">名片样式列表</h2>
			<p class="nf-page-hero__desc">管理名片展示样式配置，支持刷新列表、新增、修改与删除样式</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">操作</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="phoneSelect">刷新</el-button>
					<el-button :disabled="!isAuth('locality:add')" icon="document" type="primary"
						@click="openDialog(null,0)">添加</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="classList.list">
				<el-table-column label="编号" prop="id">
				</el-table-column>
				<el-table-column label="样式编码" prop="styleCode">
				</el-table-column>
				<el-table-column label="样式名称" prop="styleName">
				</el-table-column>
				<el-table-column label="样式图片" prop="styleImg">
					<template slot-scope="scope">
						<div v-if="scope.row.styleImg == null || scope.row.styleImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.styleImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img slot="reference" :src="scope.row.styleImg" alt="" style="width: 50px; height: 50px">
								<img :src="scope.row.styleImg" alt="" style="width: 200px; height: 200px">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="排序" prop="sortId">
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button :disabled="!isAuth('locality:update')" size="mini" type="primary"
							@click="openDialog(scope.row,2)">修改
						</el-button>
						<el-button :disabled="!isAuth('locality:delete')" size="mini" type="danger"
							@click="kongtoudelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
					:total="classList.total" layout="total,sizes, prev, pager, next,jumper"
					@size-change="handleSizeChange" @current-change="handleCurrentChange">
				</el-pagination>
			</div>
		</div>

		<el-dialog :title="title" :visible.sync="dialogVisible" width="60%" center>
			<el-form :model="dynamicValidateForm" class="demo-dynamic" label-width="200px">
				<el-form-item label="样式编码">
					<el-input v-model="styleCode" placeholder="请输入样式编码"></el-input>
				</el-form-item>
				<el-form-item label="样式名称">
					<el-input v-model="styleName" placeholder="请输入样式名称"></el-input>
				</el-form-item>
				<el-form-item label="样式图片">
					<el-upload :action="$http.adornUrlUp('alioss/upload')" :on-success="handleAvatarSuccess"
						:show-file-list="false" class="avatar-uploader">
						<img v-if="styleImg" :src="styleImg" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				<el-form-item label="排序">
					<el-input v-model="sortId" type="number" placeholder="请输入排序"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="submitForm()">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dynamicValidateForm: {},
				sort: '',
				title: '新增',
				dialogVisible: false,
				statusKt: '',
				statuss: [{
						value: '',
						title: '全部'
					},
					{
						value: 0,
						title: '不启用'
					},
					{
						value: 1,
						title: '启用'
					}
				],
				search: '',
				page: 1,
				limit: 10,
				classList: {},
				tableDataLoading: false,
				id: '',
				styleCode: '',
				styleName: '',
				styleImg: '',
				sortId: 1,
			}
		},
		mounted() {
			this.getClassList()
		},
		methods: {
			handleAvatarSuccess(res, file) {
				this.styleImg = res.data
			},
			submitForm(formName) {
				if (this.title == '新增') {
					var url = 'cardStyle/save'
				} else {
					var url = 'cardStyle/edit'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'id': this.id,
						'styleCode': this.styleCode,
						'styleName': this.styleName,
						'styleImg': this.styleImg,
						'sortId': this.sortId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							type: 'success',
							message: '操作成功!'
						})
						this.dialogVisible = false
						this.getClassList()
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				})
			},
			openDialog(row, type) {
				if (row) {
					this.title = '修改'
					this.id = row.id
					this.styleCode = row.styleCode
					this.styleName = row.styleName
					this.styleImg = row.styleImg
					this.sortId = row.sortId
				} else {
					this.title = '新增'
					this.id = ''
					this.styleCode = ''
					this.styleName = ''
					this.styleImg = ''
					this.sortId = ''
				}
				this.dialogVisible = true
			},
			kongtoudelete(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cardStyle/del/' + row.id),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								type: 'success',
								message: '操作成功!'
							})
							this.getClassList()
						} else {
							this.$message({
								showClose: true,
								message: data.msg,
								type: 'error'
							})
						}
					})
				})
			},
			cleans() {
				this.page = 1
				this.statusKt = ''
				this.search = ''
				this.getClassList()
			},
			phoneSelect() {
				this.page = 1
				this.getClassList()
			},
			getClassList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cardStyle/page'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.page,
						'pageSize': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						this.classList = data.data
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val
				this.getClassList()
			},
			handleCurrentChange(val) {
				this.page = val
				this.getClassList()
			},
		},
	}
</script>

<style scoped>
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

	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #2f3968;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}
</style>
