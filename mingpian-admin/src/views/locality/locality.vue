<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">名片样式</span>
			<h2 class="nf-page-hero__title">名片样式管理</h2>
			<p class="nf-page-hero__desc">管理名片展示样式模板，支持样式编码、名称、图片与排序配置，可添加、修改与删除</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">快捷操作</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="phoneSelect">刷新</el-button>
					<el-button type="primary" icon="document" :disabled="!isAuth('locality:add')"
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
				:total="classList.total" layout="total,sizes, prev, pager, next,jumper" @size-change="handleSizeChange"
				@current-change="handleCurrentChange">
			</el-pagination>
		</div>
		</div>
		<!--新增分类弹窗-->
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
				sort: '', //备注
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
				id: "", //id
				styleCode: "", //样式编码
				styleName: "", //样式名称
				styleImg: "", //样式图片
				sortId: 1, //序号
			}
		},
		mounted() {
			this.getClassList()
		},
		methods: {

			//上传图标
			handleAvatarSuccess(res, file) {
				this.styleImg = res.data
			},
			//新增/修改分类
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
			//打开弹窗
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

			//删除分类
			kongtoudelete(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('cardStyle/del/' + row.id),
						method: 'get',
						params: this.$http.adornParams({
							// 'classId': row.classId,
						})
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

			//重置
			cleans() {
				this.page = 1
				this.statusKt = ''
				this.search = ''
				this.getClassList()

			},
			//查询
			phoneSelect() {
				this.page = 1
				this.getClassList()
			},
			//获取分类列表数据
			getClassList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cardStyle/page'),
					method: 'post',
					// params: this.$http.adornParams({
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
			//分页
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

	.customWidth {
		width: 80% !important;
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

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #2f3968;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #2f3968 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>