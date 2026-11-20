<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商机大厅</span>
			<h2 class="nf-page-hero__title">购买记录</h2>
			<p class="nf-page-hero__desc">查看求购权限购买记录，支持按购买类型、用户昵称、电话、求购标题、发布人筛选，可跳转用户详情</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">购买类型</span>
					<el-select v-model="payWayJ" class="nf-filter-form__input" @change="cleansJl()">
						<el-option v-for="(item,index) in statesnum3" :key="index" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户昵称</span>
					<el-input v-model="userNameJ" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入用户昵称" @keydown.enter.native="cleansJl">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">电话</span>
					<el-input v-model="phoneJ" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入电话" @keydown.enter.native="cleansJl">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">求购标题</span>
					<el-input v-model="titleJ" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入求购标题" @keydown.enter.native="cleansJl">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">发布人</span>
					<el-input v-model="agentNameJ" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入发布人" @keydown.enter.native="cleansJl">
					</el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="cleansJl">查询</el-button>
					<el-button type="primary" icon="document" @click="select">重置</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName">
			<el-tab-pane label="购买记录" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="guangaoData.records">
						<el-table-column prop="roleId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="用户昵称" width="120">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updates(scope.row.userId)">
									{{ scope.row.userName ? scope.row.userName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="电话">
						</el-table-column>
						<el-table-column prop="agentName" label="发布人" width="120">
							<template slot-scope="scope">
								<div v-if="scope.row.procureDemand">
									<span class="nf-table-link"
										@click="updates(scope.row.procureDemand.userId)">
										{{ scope.row.procureDemand.agentName ? scope.row.procureDemand.agentName : '未绑定' }}
									</span>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="procureDemand.title" label="求购标题" width="250">
						</el-table-column>
						<el-table-column prop="payWay" label="购买类型">
							<template slot-scope="scope">
								<span v-if="scope.row.payWay==1">点数购买</span>
								<span v-if="scope.row.payWay==2">会员购买</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="guangaoData.total">
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
				activeName: 'first',
				page: 1,
				size: 10,
				classify: 1,
				cateId: '',
				content: '',
				createTime: '',
				tableDataLoading: false,
				guangaoData: {},
				payWayJ: '',
				userNameJ: '',
				phoneJ: '',
				titleJ: '',
				agentNameJ: '',
				statesnum3: [{
						value: 1,
						label: '点数购买'
					},
					{
						value: 2,
						label: '会员购买'
					}
				],
			}
		},
		methods: {
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
			handleSizeChange(val) {
				this.size = val
				this.guangaoSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.guangaoSelect()
			},
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCateById`),
						method: 'post',
						params: this.$http.adornParams({
							'cateId': row.cateId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guangaoSelect()
							}
						})
					})
				})
			},
			guangaoSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/procureDemandRole/selectAdminProcureDemandRoleList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'payWay': this.payWayJ,
						'userName': this.userNameJ,
						'phone': this.phoneJ,
						'title': this.titleJ,
						'agentName': this.agentNameJ,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guangaoData = returnData
				})
			},
			cleansJl() {
				this.page = 1
				this.guangaoSelect()
			},
			select() {
				this.payWayJ = ''
				this.userNameJ = ''
				this.phoneJ = ''
				this.titleJ = ''
				this.agentNameJ = ''
				this.page = 1
				this.guangaoSelect()
			}
		},
		mounted() {
			this.guangaoSelect()
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

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>
