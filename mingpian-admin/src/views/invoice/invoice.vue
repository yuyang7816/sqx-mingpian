<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">财务中心</span>
			<h2 class="nf-page-hero__title">发票管理</h2>
			<p class="nf-page-hero__desc">管理用户发票申请，支持按企业名称、用户电话、开票信息与状态筛选，可批量开票</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">企业名称</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="timeDate2"
						placeholder="请输入企业名称" v-model="companyName" clearable>
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户电话</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="timeDate2" placeholder="请输入用户电话"
						v-model="phone" clearable>
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">开票信息</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="timeDate2"
						placeholder="请输入开票信息" v-model="title" clearable>
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">发票状态</span>
					<el-select v-model="status" class="nf-filter-form__input" @change="timeDate2()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="timeDate2">查询</el-button>
					<el-button type="primary" icon="document" @click="clear">重置</el-button>
					<el-button type="primary" icon="document" @click="transferClcik()"
						:disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">
						批量开票
					</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list" @selection-change="changeFun">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column prop="invoiceId" fixed label="编号" width="50"></el-table-column>
				<el-table-column prop="userName" label="用户名">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="!isAuth('userList:phone') && scope.row.userName1">{{ scope.row.userName1 ? scope.row.userName1 : '未绑定' }}</span>
						<span style="color: #f56c6c;" v-else>{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="companyName" label="企业名称">
				</el-table-column>
				<el-table-column prop="phone" label="用户电话">
					<template slot-scope="scope">
						<span class="nf-table-link" @click="updates(scope.row)" v-if="isAuth('userList:phone')">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
						<span class="nf-table-link" @click="updates(scope.row)" v-else>
							{{ scope.row.phone1 ? scope.row.phone1 : '-' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="开票信息" width="300">
					<template slot-scope="scope">
						<el-popover placement="top-start" title="" trigger="hover">
							<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" v-html="scope.row.title" slot="reference"></div>
							<div style="width: 500px;" v-html="scope.row.title"></div>
						</el-popover>
					</template>
				</el-table-column>
				<el-table-column prop="invoiceType" label="发票类型">
					<template slot-scope="scope">
						<span v-if="scope.row.invoiceType==1">专票</span>
						<span v-if="scope.row.invoiceType==2">普票</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="发票内容">
				</el-table-column>
				<el-table-column prop="money" label="发票金额">
				</el-table-column>
				<el-table-column prop="status" fixed="right" label="发票状态">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" style="color:red">待开票</span>
						<span v-if="scope.row.status==2" style="color: darkgreen;">已开票</span>
						<el-switch v-model="scope.row.status" @change="change3(scope.row.status,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="170">
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
				page: 1,
				limit: 10,
				openValue:'2',
				closeValue:'1',
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				userId:'',
				userName:'',
				companyName:'',
				title:'',
				content:'',
				phone:'',
				status:'',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '待开票'
					}, {
						value: 2,
						label: '已开票'
					}
				],
				checkBoxData: [], //多选框选择的值
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 详情跳转
			updates(row) {
				if(row.userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				}
				
			},
			// 获取数据列表
			dataSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('invoice/selectInvoiceList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.userId,
						'userName': this.userName,
						'companyName':this.companyName,
						'title':this.title,
						'content':this.content,
						'phone':this.phone,
						'status':this.status,
						
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						if(data.data.list.length>0){
							for(var i in data.data.list){
								var pattern = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
								var phone1 = data.data.list[i].phone
								var userName1 = data.data.list[i].userName
								if(pattern.test(phone1)){
									var one = phone1.slice(0, 3)
									var two = phone1.slice(7, 11)
									data.data.list[i].phone1 = one+'****'+two
								}
								if(pattern.test(userName1)){
									var one = userName1.slice(0, 3)
									var two = userName1.slice(7, 11)
									data.data.list[i].userName1 = one+'****'+two
								}
								
								
							}
						}
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			timeDate2() {
				this.page = 1
				this.dataSelect()
			},
			clear(){
				this.page = 1
				this.userName = ''
				this.companyName = ''
				this.title = ''
				this.content = ''
				this.phone = ''
				this.status = ''
				this.dataSelect()
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.memberImg = file.data
			},
			addNotice() {
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
			// 修改
			amendBanner(index, rows, a) {
				if (a == 1) {
					this.pl = 1
				} else {
					this.pl = 0
				}
				this.dialogFormVisible = true
				this.id = rows.id
				this.memberImg = rows.memberImg
				this.memberId = rows.memberId
				this.sort = rows.sort
				this.memberName = rows.memberName
				this.title = '修改'
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						`?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`
					),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			//删除一级
			deletes(row) {
				let delid = row.memberId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`member/deleteMemberById/?memberId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 批量操作
			transferClcik(id) {
			
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.invoiceId
				})
					this.$http({
						url: this.$http.adornUrl('invoice/updateInvoiceStatus'),
						method: 'post',
						params: this.$http.adornParams({
							'ids':ids.toString(),
							'status':2
							
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '修改成功',
								type: 'warning'
							})
							this.dataSelect()
							return
						}
						if (data.code == 9999) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
							return
						}
					})
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 单独修改
			change3(isRecommend,row){
				this.$http({
					url: this.$http.adornUrl('invoice/updateInvoiceStatus'),
					method: 'post',
					params: this.$http.adornParams({
						'ids':row.invoiceId,
						'status':isRecommend
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
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
			},
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
