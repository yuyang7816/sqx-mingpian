<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">会员配置</span>
			<h2 class="nf-page-hero__title">会员配置列表</h2>
			<p class="nf-page-hero__desc">管理会员套餐名称、月数、价格与排序，支持新增、编辑、删除及启用状态切换</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">操作</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="VipType()">刷新</el-button>
					<el-button icon="document" type="primary" @click="addVipType(1,{})"
						:disabled="!isAuth('coupon:add')">新增</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="list.list">
			<el-table-column label="编号" prop="id" width="80">
			</el-table-column>
			<!-- 	<el-table-column label="权限" prop="power">
				<template slot-scope="scope">
					<span v-if="scope.row.power == 1">只能发布名片</span>
					<span v-if="scope.row.power == 2">只能发布行业资讯</span>
					<span v-if="scope.row.power == 3">两者都能发布</span>
				</template>
			</el-table-column> -->
			<el-table-column label="会员名称" prop="vipName">
			</el-table-column>
			<!-- <el-table-column prop="image" label="图片">
				<template slot-scope="scope">
					　　<img v-if="scope.row.image" :src="scope.row.image" width="60" height="60" />
				</template>
			</el-table-column> -->
			<!-- <el-table-column prop="iconImg" label="角标图">
				<template slot-scope="scope">
					　　<img v-if="scope.row.iconImg" :src="scope.row.iconImg" width="60" height="60" />
				</template>
			</el-table-column>
			<el-table-column prop="productIconImg" label="产品角标图">
				<template slot-scope="scope">
					　　<img v-if="scope.row.productIconImg" :src="scope.row.productIconImg" width="60" height="60" />
				</template>
			</el-table-column> -->
			<el-table-column label="月数" prop="vipMonthCount">
			</el-table-column>
			<!-- <el-table-column label="查看次数" prop="viewNum">
			</el-table-column>
			<el-table-column label="查看采购名片次数" prop="cardViewNum">
			</el-table-column> -->
			<!-- <el-table-column label="需求查看次数" prop="demandCount">
			</el-table-column> -->
			<!-- <el-table-column label="产品发布次数" prop="productCount">
			</el-table-column> -->
			<el-table-column label="会员价格" prop="money">
			</el-table-column>
			<el-table-column label="排序" prop="sort">
			</el-table-column>
			<!-- <el-table-column label="行业咨询可发布条数(0为不限制条数)" prop="industryCount">
			</el-table-column>
			<el-table-column label="行业咨询发布条数计算周期" prop="industryCycle">
			</el-table-column> -->
			<el-table-column label="创建时间" prop="createTime">
			</el-table-column>
			<el-table-column fixed="right" label="是否启用">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.isEnable" :active-value="1" :inactive-value="0" active-color="#13ce66"
						inactive-color="#ff4949" @change="e=>change(e,scope.row)" :disabled="!isAuth('coupon:update')">
					</el-switch>
				</template>
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="160">
				<template slot-scope="scope">
					<!-- <el-button size="mini" type="primary" @click="tequanBtn(scope.row)"
						style="margin:5px;">会员特权</el-button> -->
					<el-button size="mini" type="primary" @click="addVipType(2,scope.row)"
						:disabled="!isAuth('coupon:update')">编辑</el-button>
					<el-button size="mini" type="danger" @click="deleteVipType(scope.row.id)"
						:disabled="!isAuth('coupon:delete')">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div class="nf-table-card__foot">
			<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]" :total="list.total"
				layout="total,sizes, prev, pager, next,jumper" @size-change="handleSizeChange"
				@current-change="handleCurrentChange">
			</el-pagination>
		</div>
		</div>
		<!--新增弹窗-->
		<el-dialog :title="dialogVisibleTitle" :visible.sync="dialogVisible" width="60%" @close="resetForm('ruleForm')">
			<el-form ref="ruleForm" :model="ruleForm" :rules="rules" class="demo-ruleForm" label-width="200px">
				<!-- <el-form-item label="权限" prop="power">
					<el-radio v-model="ruleForm.power" :label="1">只能发布名片</el-radio>
					<el-radio v-model="ruleForm.power" :label="2">只能发布行业资讯</el-radio>
					<el-radio v-model="ruleForm.power" :label="3">两者都能发布</el-radio>
				</el-form-item> -->
				<el-form-item label="会员名称" prop="vipName">
					<el-input v-model="ruleForm.vipName" placeholder="请输入会员名称"></el-input>
				</el-form-item>
				<el-form-item label="会员价格" prop="money">
					<el-input v-model="ruleForm.money" placeholder="请输入会员价格" type="number"></el-input>
				</el-form-item>
				<el-form-item label="月数" prop="vipMonthCount">
					<el-input-number v-model="ruleForm.vipMonthCount" :min="1" label="请输入月数"></el-input-number>
				</el-form-item>
				<!-- <el-form-item label="查看次数" prop="viewNum">
					<el-input-number v-model="ruleForm.viewNum" :min="0" label="请输入查看次数"></el-input-number>
				</el-form-item>
				<el-form-item label="查看采购名片次数" prop="cardViewNum"> 
					<el-input-number v-model="ruleForm.cardViewNum" :min="0" label="请输入查看采购名片次数"></el-input-number>
				</el-form-item> -->
				<!-- <el-form-item label="需求查看次数" prop="demandCount">
					<el-input-number v-model="ruleForm.demandCount" :min="0" label="请输入需求查看次数"></el-input-number>
				</el-form-item> -->
				<!-- <el-form-item label="产品发布次数" prop="productCount">
					<el-input-number v-model="ruleForm.productCount" :min="0" label="请输入产品发布次数"></el-input-number>
				</el-form-item> -->
				<el-form-item label="排序" prop="sort">
					<el-input-number v-model="ruleForm.sort" :min="1" label="请输入排序"></el-input-number>
				</el-form-item>
				<!-- <el-form-item label="行业咨询可发布条数" prop="industryCount">
					<el-input v-model="ruleForm.industryCount" placeholder="请输入行业咨询可发布条数" type="number"></el-input>
					<span style="color: red;">* 0为不限制条数</span>
				</el-form-item>
				<el-form-item label="行业咨询发布条数计算周期" prop="industryCycle">
					<el-radio v-model="ruleForm.industryCycle" :label="1">天</el-radio>
					<el-radio v-model="ruleForm.industryCycle" :label="2">月</el-radio>
					<el-radio v-model="ruleForm.industryCycle" :label="3">年</el-radio>
				</el-form-item> -->
				<el-form-item label="是否启用" prop="isEnable">
					<el-radio v-model="ruleForm.isEnable" :label="1">启用</el-radio>
					<el-radio v-model="ruleForm.isEnable" :label="0">不启用</el-radio>
				</el-form-item>
				<!-- <el-form-item label="图片" prop="memberImg">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="memberImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="memberImg" :src="memberImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="角标图" prop="iconImg">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="iconImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="iconImg" :src="iconImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="产品角标图" prop="productIconImg">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="productIconImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="productIconImg" :src="productIconImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item> -->
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
			</span>
		</el-dialog>
		<!-- 特权列表 -->
		<el-dialog title="会员特权" :visible.sync="dialogVisibleTq" width="80%">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('coupon:add')" size="mini" type="primary"
					icon="document" @click="addNotice"
					v-if="tableDataT.records&&tableDataT.records.length==0">添加会员特权</el-button>
			</div>
			<el-table v-loading="tableDataLoadingT" :data="tableDataT.records">
				<el-table-column fixed prop="memberId" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="memberImg" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.memberImg" width="60" height="60" />
					</template>
				</el-table-column>
				<!-- <el-table-column prop="memberName" label="特权名称">
        </el-table-column>
        <el-table-column prop="memberDescribe" label="特权说明">
        </el-table-column>
        <el-table-column prop="sort" label="排序">
        </el-table-column> -->
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
							@click="amendBanner(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('coupon:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeT" @current-change="handleCurrentChangeT"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitT" :current-page="pageT"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataT.total">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="memberImg"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="memberImg" :src="memberImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<!-- <div style="margin-bottom: 10px;">
    		<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
    		<el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
    	</div>
      <div style="margin-bottom: 10px;">
      	<span style="width: 200px;display: inline-block;text-align: right;">说明：</span>
      	<el-input style="width:50%;" v-model="memberDescribe" placeholder="请输入说明"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
      	<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
      	<el-input style="width:50%;" v-model="sort" placeholder="请输入排序" onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
      </div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
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
				list: {},
				dialogVisible: false,
				tableDataLoading: false,
				dialogVisibleTitle: '',
				ruleForm: {
					vipName: '',
					money: '',
					vipMonthCount: '',
					isEnable: 1,
					id: '',
					sort: 0,

				},
				rules: { //表单验证
					vipName: [{
						required: true,
						message: '请输入会员名称',
						trigger: 'blur'
					}, ],
					money: [{
						required: true,
						message: '请输入会员价格',
						trigger: 'blur'
					}, ],
					// discountMoney: [{
					// 	required: true,
					// 	message: '请输入折扣价格',
					// 	trigger: 'blur'
					// }, ],
					vipMonthCount: [{
						required: true,
						message: '请输入月数',
						trigger: 'blur'
					}, ],
					isEnable: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}, ],
				},
				dialogVisibleTq: false,
				dialogFormVisible: false,
				tableDataLoadingT: false,
				tableDataT: {},
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				title: '添加',
				pageT: 1,
				limitT: 10,
				vipId: '',
				memberDescribe: '',
				iconImg: '',
				productIconImg: '',


			}
		},
		mounted() {
			this.dataSelect()
		},
		methods: {
			//删除
			deleteVipType(id) {
				this.$confirm('确定要删除该信息?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/vipDetails/deleteVipDetailsById'),
						method: 'post',
						params: this.$http.adornParams({
							id: id
						})
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
							})
							this.dataSelect()
						} else {
							this.$message.error(data.msg)
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			//启用不启用
			change(e, row) {
				let obj = {
					vipName: row.vipName,
					money: row.money,
					vipMonthCount: row.vipMonthCount,
					isEnable: e,
					id: row.id,
					sort: row.sort,
				}
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/save'),
					method: 'post',
					data: this.$http.adornData(obj)
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
						})
						this.dialogVisible = false
						this.dataSelect()
					} else {
						this.dialogVisible = false
						this.$message.error(data.msg)
					}
				})
			},
			//重置表单
			resetForm(formName) {
				this.$refs[formName].resetFields()
				this.memberImg = ''
				this.iconImg = ''
				this.productIconImg = ''
				this.ruleForm = {
					vipName: '',
					money: '',
					vipMonthCount: '',
					isEnable: 1,
					id: '',
					sort: 0,
				}
			},
			//提交
			submitForm(formName) {
				if (this.ruleForm.vipName === '' || this.ruleForm.vipName == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '会员名称不能为空',
						type: 'warning'
					});
					return
				}
				if (this.ruleForm.money === '' || this.ruleForm.money == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '会员价格不能为空',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/save'),
					method: 'post',
					data: this.$http.adornData(this.ruleForm)
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
						})
						this.dialogVisible = false
						this.dataSelect()
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			//新增弹窗
			addVipType(type, row) { // type:  1:新增,2:修改
				if (type === 1) {
					this.dialogVisibleTitle = '新增'
				} else {
					this.dialogVisibleTitle = '修改'
					this.ruleForm = {
						vipName: row.vipName,
						money: row.money,
						vipMonthCount: row.vipMonthCount,
						isEnable: row.isEnable,
						id: row.id,
						sort: row.sort,
					}
				}
				this.dialogVisible = true
			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			VipType() {
				this.page = 1
				this.dataSelect()
			},
			//获取会员列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/list'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.page,
						'pageSize': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data && data.code === 0) {
						let returnData = data.data.data
						this.list = returnData
					}
				})
			},
			// 会员特权 弹框
			tequanBtn(row) {
				this.pageT = 1
				this.vipId = row.id
				this.dataSelectT()
				this.dialogVisibleTq = true
			},
			dataSelectT() {
				this.tableDataLoadingT = true
				this.$http({
					url: this.$http.adornUrl('admin/member/getMemberList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageT,
						'limit': this.limitT,
						'vipId': this.vipId,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoadingT = false
						let returnData = data.data;
						this.tableDataT = returnData;
					}
				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.memberImg = file.data
			},
			handleAvatarSuccess1(file) {
				this.iconImg = file.data
			},
			handleAvatarSuccess2(file) {
				this.productIconImg = file.data
			},

			handleCurrentChangeT(val) {
				this.pageT = val
				this.dataSelectT()
			},
			handleSizeChangeT(val) {
				this.limitT = val
				this.dataSelectT()
			},
			addNotice() {
				this.memberImg = ''
				this.memberId = ''
				this.memberName = ''
				this.sort = ''
				this.memberDescribe = ''
				this.title = '添加'
				this.dialogFormVisible = true
			},
			// 修改
			amendBanner(rows) {
				this.dialogFormVisible = true
				this.memberImg = rows.memberImg
				this.memberId = rows.memberId
				this.sort = rows.sort
				this.memberName = rows.memberName
				this.memberDescribe = rows.memberDescribe
				this.title = '修改'
			},
			// 添加banner图
			addNoticeTo() {
				if (this.memberImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				// if (this.memberName == '') {
				//     this.$notify({
				//         title: '提示',
				//         duration: 1800,
				//         message: '请输入名称',
				//         type: 'warning'
				//     });
				//     return
				// }
				//  if (this.memberDescribe == '') {
				//      this.$notify({
				//          title: '提示',
				//          duration: 1800,
				//          message: '请输入说明',
				//          type: 'warning'
				//      });
				//      return
				//  }

				if (this.title == '添加') {
					var url = 'admin/member/saveMember'
				} else {
					var url = 'admin/member/saveMember'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'memberImg': this.memberImg,
						'memberId': this.memberId,
						'memberName': this.memberName,
						'sort': this.sort,
						'vipId': this.vipId,
						'memberDescribe': this.memberDescribe,
					}, false, 'from')
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

								this.dataSelectT()
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
			},
			deletes(row) {
				let delid = row.memberId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`admin/member/deleteMemberById?memberId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectT()
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
</style>