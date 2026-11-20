<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">群聊管理</span>
			<h2 class="nf-page-hero__title">群组分类</h2>
			<p class="nf-page-hero__desc">管理群组分类信息，支持添加、修改、删除分类及配置模版类型扩展字段</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">操作</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" :disabled="!isAuth('autonymType:add')"
						@click="addNotice">添加</el-button>
					<el-button icon="document" type="primary" @click="shauxin">刷新</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="payClassifyId" label="编号" width="50">
					<template slot-scope="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID">
				</el-table-column>
				<el-table-column prop="typeName" label="分类名称">
				</el-table-column>
				<el-table-column prop="remark" label="备注">
				</el-table-column>
				<el-table-column prop="typeStatus" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.typeStatus" @change="changeOne(scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue" :inactive-value="closeValue"
							active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" plain style="margin: 3px;" @click="mobanBtn(scope.row)">模版类型
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('autonymType:update')" style="margin: 3px;"
							@click="amendBanner(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('autonymType:delete')" style="margin: 3px;"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="typeName" placeholder="请输入分类名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="remark" placeholder="请输入备注"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 模版类型 -->
		<el-dialog title="模版类型" :visible.sync="dialogFormVisibleMb" center width="80%">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('autonymType:add')" size="mini" type="primary"
					icon="document" @click="amendBannerMb()">添加</el-button>
				<el-button style='margin: 10px 0;' size="mini" type="primary" plain icon="document"
					@click="shauxinMb">刷新</el-button>
			</div>
			<el-table v-loading="tableDataLoadingMb" :data="tableDataMb.list">

				<el-table-column fixed prop="payClassifyId" label="编号" width="50">
					<template slot-scope="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="ID">
				</el-table-column>
				<el-table-column prop="template" label="扩展字段模版">
					<template slot-scope="scope">
						<div v-if="scope.row.template">
							<el-tag v-for="(item,index) in scope.row.template" :key="index"
								style="margin: 3px;">{{item.label}}</el-tag>
						</div>

					</template>
				</el-table-column>
				<el-table-column prop="remark" label="备注">
				</el-table-column>
				<el-table-column prop="configStatus" label="状态">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.configStatus" @change="change(scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('autonymType:update')"
							style="margin: 3px;" @click="amendBannerMb(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('autonymType:delete')"
							style="margin: 3px;" @click="deletesMb(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeMb" @current-change="handleCurrentChangeMb"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitMb" :current-page="pageMb"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataMb.total">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog :title="title" :visible.sync="dialogFormVisiblemb2" center width="70%">

			<div style="margin-bottom: 10px;position: relative;">
				<span style="width: 200px;display: inline-block;text-align: right;position: absolute;">扩展字段：</span>
				<el-checkbox-group v-model="checkListId" style="margin-left: 200px;" @change="btns()">

					<el-checkbox v-for="(item,index) in templateList" :key="index"
						:label="item.id">{{item.filedExplain}}</el-checkbox>
				</el-checkbox-group>

			</div>
			<div v-if="checkList.length>0">
				<div style="margin-bottom: 10px;" v-for="(item,index) in checkList" :key="index">
					<div v-if="item.id==1">
						<span style="width: 200px;display: inline-block;text-align: right;">是否展示群地址：</span>
						<el-radio-group v-model="item.value">
							<el-radio label="true">是</el-radio>
							<el-radio label="false">否</el-radio>
						</el-radio-group>
					</div>
					<div v-else>
						<span style="width: 200px;display: inline-block;text-align: right;">{{item.label}}：</span>
						<el-input style="width:50%;" v-model="item.value" :placeholder='"请输入"+item.label'></el-input>
					</div>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="remark" placeholder="请输入备注"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisiblemb2 = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeToMb()">确 定</el-button>
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
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: {},
				typeName: '',
				remark: '',
				typeStatus: 0,
				typeImg: '',
				id: '',
				sort: '',
				title: '添加',

				tableDataMb: {},
				dialogFormVisibleMb: false,
				tableDataLoadingMb: false,
				dialogFormVisiblemb2: false,
				pageMb: 1,
				limitMb: 10,
				typeId: '',
				templateList: [],
				checkListId: [],
				checkList: [],
				configStatus: 1,
				openValue: 0,
				closeValue: 1,


			}
		},
		methods: {
			shauxin() {
				this.page = 1
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('groupTypeInfo/page'),
					method: 'post',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
						'pageNum': this.page,
						'pageSize': this.limit,
						'typeName': '',
						'typeStatus': '',
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.typeImg = file.data
			},
			addNotice() {
				this.typeName = ''
				this.id = ''
				this.remark = ''
				this.typeStatus = 0
				// this.sort = ''
				// this.typeImg = ''
				this.title = '添加'
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 添加banner图
			addNoticeTo() {
				if (this.typeName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					});
					return
				}
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入备注',
						type: 'warning'
					});
					return
				}
				// if (this.typeImg == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传图片',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.title == '添加') {
					var url = 'groupTypeInfo/save'
				} else {
					var url = 'groupTypeInfo/edit'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'id': this.id,
						'typeName': this.typeName,
						'remark': this.remark,
						'typeStatus': this.typeStatus,
						// 'sort': this.sort,
						// 'typeImg': this.typeImg,
						// 'payClassifyId': this.payClassifyId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
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
							message: data,
							msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 修改
			amendBanner(rows) {
				this.title = '修改'
				this.typeName = rows.typeName
				this.id = rows.id
				this.remark = rows.remark
				this.typeStatus = rows.typeStatus
				// this.typeImg = rows.typeImg
				this.dialogFormVisible = true
			},
			//删除一级
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`groupTypeInfo/del/${delid}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
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
				}).catch(() => {})
			},
			// 模版配置 弹框
			mobanBtn(row) {
				this.typeId = row.id
				this.pageMb = 1
				this.dataSelectMb()
				this.dialogFormVisibleMb = true
			},
			shauxinMb() {
				this.pageMb = 1
				this.dataSelectMb()
			},
			// 获取数据列表
			dataSelectMb() {
				this.tableDataLoadingMb = true
				this.$http({
					url: this.$http.adornUrl('groupTypeConfig/page'),
					method: 'post',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
						'pageNum': this.pageMb,
						'pageSize': this.limitMb,
						'typeId': this.typeId,
						'configStatus': '',
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoadingMb = false
						if (data.data.list.length > 0) {
							for (var i in data.data.list) {
								data.data.list[i].template = JSON.parse(data.data.list[i].template)
								console.log(data.data.list[i].template)
							}
						}

						let returnData = data.data;
						this.tableDataMb = returnData;
					}

				})
			},

			handleCurrentChangeMb(val) {
				this.pageMb = val
				this.dataSelectMb()
			},
			handleSizeChangeMb(val) {
				this.limitMb = val
				this.dataSelectMb()
			},
			// 添加、修改模版类型
			addNoticeToMb() {
				if (this.checkList.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择扩展字段',
						type: 'warning'
					});
					return
				}
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入备注',
						type: 'warning'
					});
					return
				}
				if (this.title == '添加模版类型') {
					var url = 'groupTypeConfig/save'
				} else {
					var url = 'groupTypeConfig/edit'
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'id': this.id,
						'typeId': this.typeId,
						'template': JSON.stringify(this.checkList),
						'remark': this.remark,
						'configStatus': this.configStatus,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisiblemb2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.dataSelectMb()
							}
						})
					} else {
						this.$message({
							message: data,
							msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 修改
			amendBannerMb(rows) {
				this.dataSelectMbZd()
				if (rows) {
					this.title = '修改模版类型'
					this.id = rows.id
					this.remark = rows.remark
					this.checkList = rows.template
					this.configStatus = rows.configStatus
					this.checkListId = []
					for (var j in this.checkList) {
						this.checkListId.push(this.checkList[j].id)
					}
				} else {
					this.id = ''
					this.remark = ''
					this.checkList = []
					this.checkListId = []
					this.configStatus = 1
					this.title = '添加模版类型'
				}

				this.dialogFormVisiblemb2 = true
			},
			//删除模版
			deletesMb(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`/groupTypeConfig/deleteById/${delid}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectMb()
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
				}).catch(() => {})
			},
			// 获取模版可添加字段
			dataSelectMbZd() {
				this.$http({
					url: this.$http.adornUrl('groupTypeConfig/queryTypeFiled'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						if (data.data.length > 0) {
							for (var i in data.data) {
								data.data[i].label = data.data[i].filedExplain
								data.data[i].field = data.data[i].filedName
								if (data.data[i].id == 1) {
									data.data[i].value = 'true'
								} else {
									data.data[i].value = ''
								}

							}
						}
						let returnData = data.data;
						this.templateList = returnData;
						if (this.title == '修改模版类型') {
							for (var i in this.templateList) {
								for (var j in this.checkList) {
									if (this.checkList[j].label == this.templateList[i].label) {
										this.templateList[i].value = this.checkList[j].value
									}
								}
							}
						}
					}

				})
			},
			// 选择
			btns() {
				var arr = []
				for (var i in this.templateList) {
					for (var j in this.checkListId) {
						if (this.checkListId[j] == this.templateList[i].id) {
							arr.push(this.templateList[i])
						}
					}
				}
				this.checkList = arr
			},
			// 分类 是否启用
			changeOne(row) {
				this.$http({
					url: this.$http.adornUrl('groupTypeInfo/edit'),
					method: 'post',
					data: this.$http.adornData({
						'id': row.id,
						'typeName': row.typeName,
						'remark': row.remark,
						'typeStatus': row.typeStatus,
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
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 模板是否启用
			change(row) {
				this.$http({
					url: this.$http.adornUrl(`groupTypeConfig/updateStatus/${row.id}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectMb()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelectMb()
							}
						})
					}
				})
			},

		},
		mounted() {
			this.dataSelect()
		}
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
</style>
