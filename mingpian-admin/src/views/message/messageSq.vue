<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">消息管理</span>
			<h2 class="nf-page-hero__title">名片申请</h2>
			<p class="nf-page-hero__desc">查看用户名片交换申请，支持按状态、申请人、被申请人筛选，可审核通过或拒绝</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select class="nf-filter-form__input" v-model="statusT" @change="select()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">申请人</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" v-model="byUserNameT"
						placeholder="请输入申请人" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">被申请人</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" v-model="userNameT"
						placeholder="请输入被申请人" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="申请信息" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="id" label="编号" width="80" fixed="left">
						</el-table-column>
						<el-table-column prop="byUserName" label="申请人" width="100" fixed="left">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updataDetails(scope.row.byUserId)">
									{{ scope.row.byUserName ? scope.row.byUserName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="userName" label="被申请人" width="100" fixed="left">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updataDetails(scope.row.userId)">
									{{ scope.row.userName ? scope.row.userName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column label="姓名" prop="businessCard.realName" width="100">
						</el-table-column>
						<el-table-column label="微信号" prop="businessCard.wxCode" width="160">
						</el-table-column>
						<el-table-column prop="businessCard.companyName" label="公司" width="160">
						</el-table-column>
						<el-table-column label="一级行业" prop="businessCard.industryFirstName" width="200">
						</el-table-column>
						<el-table-column label="二级行业" prop="businessCard.industrySecondName" width="100">
						</el-table-column>
						<el-table-column label="城市" prop="businessCard.companyCity">
						</el-table-column>
						<el-table-column label="联系电话" prop="businessCard.cardPhone" width="160">
						</el-table-column>
						<el-table-column label="邮箱" prop="businessCard.email" width="160">
						</el-table-column>
						<el-table-column label="地址" prop="businessCard.address" width="200">
						</el-table-column>
						<el-table-column label="名片正面" prop="cardImg" width="100">
							<template slot-scope="scope">
								<div v-if="scope.row.businessCard == null || scope.row.businessCard.cardImg == ''">
									暂无图片
								</div>
								<div v-if="scope.row.businessCard.cardImg" style="display:flex;flex-wrap: wrap;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img slot="reference" :src="scope.row.businessCard.cardImg" alt=""
											style="width: 50px; height: 50px">
										<img :src="scope.row.businessCard.cardImg" alt="" style="width: 200px; height: 200px">
									</el-popover>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createAt" label="创建时间" width="160">
						</el-table-column>
						<el-table-column prop="status" label="状态" fixed="right">
							<template slot-scope="scope">
								<div v-if="scope.row.status==1">待审核</div>
								<div v-if="scope.row.status==2">已通过</div>
								<div v-if="scope.row.status==3">已拒绝</div>
							</template>
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="100">
							<template slot-scope="scope">
								<el-button size="mini" type="danger" :disabled="!isAuth('messageSq:shenhe')"
									v-if="scope.row.status==1" @click="refuseClick(scope.row)">审核</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<el-dialog title="审核处理" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="2">通过</el-radio>
					<el-radio :label="3">拒绝</el-radio>
				</el-radio-group>
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
				state: 6,
				content: '',

				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				statusT:'',
				byUserNameT:'',
				userNameT:'',
				statesnum: [{
						value: '',
						label: '全部'
					},{
						value: 1,
						label: '待审核'
					}, {
						value: 2,
						label: '已通过'
					}, {
						value: 3,
						label: '已拒绝'
					}
				],

			}
		},
		methods: {
			// 详情跳转
			updataDetails(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					});
				} else {
					this.$message({
						message: '当前用户已注销',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
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

			handleClick(tab, event) {
				if (tab._props.label == '申请信息') {
					this.page = 1
					this.limit = 10
					this.state = 6
					this.dataSelect()
				}
			},
			select(){
				this.page = 1
				this.dataSelect()
			},
			cleans(){
				this.page = 1
				this.statusT = ''
				this.byUserNameT = ''
				this.userNameT = ''
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'type': 1,
						'status':this.statusT,
						'byUserName':this.byUserNameT,
						'userName':this.userNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 注销
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定要注销该用户?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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

			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('message/updateMessageBody'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.messageId,
						'status': this.radio,
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
								this.content = ''
								this.status = -1
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
			},
			dataSelect1() {
				this.page = 1
				if (this.$route.query.status != null) {
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = ''
				}
				this.dataSelect()

			},
		},
		mounted() {
			if (this.$route.query.status != null) {
				this.statusT = Number(this.$route.query.status)
			} else {
				this.statusT = ''
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect1'
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
