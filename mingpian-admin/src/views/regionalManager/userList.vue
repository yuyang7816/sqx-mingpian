<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">区域管理</span>
			<h2 class="nf-page-hero__title">用户中心</h2>
			<p class="nf-page-hero__desc">查看区域管辖用户列表，支持按昵称、创建时间筛选，可查看用户余额、实名与会员状态</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户昵称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">开始时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime" align="right"
						type="datetime" default-time="00:00:00" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">截止时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime" align="right"
						type="datetime" default-time="23:59:59" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全部用户" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column fixed prop="userName" label="昵称" width="150">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="isAuthentication" label="性别">
							<template slot-scope="scope">
								<span v-if="scope.row.sex==1">男</span>
								<span v-else-if="scope.row.sex==2">女</span>
								<span v-else>未设置</span>
							</template>
						</el-table-column>
						<el-table-column label="图像">
							<template slot-scope="scope">
								<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
								<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
							<template slot-scope="scope">
								<span class="nf-table-link">
									{{ scope.row.phone ? scope.row.phone : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
						<el-table-column prop="isAuthentication" label="是否实名">
							<template slot-scope="scope">
								<span v-if="scope.row.isAuthentication==1">已实名</span>
								<span v-else>未实名</span>
							</template>
						</el-table-column>
						<el-table-column prop="isVip" label="是否是会员">
							<template slot-scope="scope">
								<span v-if="scope.row.isVip==1">是</span>
								<span v-else>否</span>
							</template>
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
						<el-table-column label="邀请人邀请码">
							<template slot-scope="scope">
								<span class="nf-table-link">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBaoName" label="支付宝名称">
							<template slot-scope="scope">
								<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBao" label="支付宝账号">
							<template slot-scope="scope">
								<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="wxImg" label="收款二维码" width="150">
							<template slot-scope="scope">
								<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
									暂无收款二维码
								</div>
								<div v-else style="display:flex;flex-wrap: wrap;">
									<div style="margin: 2px;">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt="" slot="reference">
											<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
										</el-popover>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						<el-table-column prop="state " label="状态">
							<template slot-scope="scope">
								<span style="color: #13ce66;" v-if="scope.row.status==1">正常</span>
								<span style="color: #ff4949;" v-else>禁用</span>
							</template>
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="普通用户" name="second">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column fixed prop="userName" label="昵称" width="150">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="isAuthentication" label="性别">
							<template slot-scope="scope">
								<span v-if="scope.row.sex==1">男</span>
								<span v-else-if="scope.row.sex==2">女</span>
								<span v-else>未设置</span>
							</template>
						</el-table-column>
						<el-table-column label="图像">
							<template slot-scope="scope">
								<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
								<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
							<template slot-scope="scope">
								<span class="nf-table-link">
									{{ scope.row.phone ? scope.row.phone : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
						<el-table-column prop="isAuthentication" label="是否实名">
							<template slot-scope="scope">
								<span v-if="scope.row.isAuthentication==1">已实名</span>
								<span v-else>未实名</span>
							</template>
						</el-table-column>
						<el-table-column prop="isVip" label="是否是会员">
							<template slot-scope="scope">
								<span v-if="scope.row.isVip==1">是</span>
								<span v-else>否</span>
							</template>
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
						<el-table-column label="邀请人邀请码">
							<template slot-scope="scope">
								<span class="nf-table-link">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBaoName" label="支付宝名称">
							<template slot-scope="scope">
								<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBao" label="支付宝账号">
							<template slot-scope="scope">
								<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="wxImg" label="收款二维码" width="150">
							<template slot-scope="scope">
								<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
									暂无收款二维码
								</div>
								<div v-else style="display:flex;flex-wrap: wrap;">
									<div style="margin: 2px;">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt="" slot="reference">
											<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
										</el-popover>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						<el-table-column prop="state " label="状态">
							<template slot-scope="scope">
								<span style="color: #13ce66;" v-if="scope.row.status==1">正常</span>
								<span style="color: #ff4949;" v-else>禁用</span>
							</template>
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="会员用户" name="third">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.list">
						<el-table-column fixed prop="userName" label="昵称" width="150">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="isAuthentication" label="性别">
							<template slot-scope="scope">
								<span v-if="scope.row.sex==1">男</span>
								<span v-else-if="scope.row.sex==2">女</span>
								<span v-else>未设置</span>
							</template>
						</el-table-column>
						<el-table-column label="图像">
							<template slot-scope="scope">
								<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
								<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号">
							<template slot-scope="scope">
								<span class="nf-table-link">
									{{ scope.row.phone ? scope.row.phone : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
						<el-table-column prop="isAuthentication" label="是否实名">
							<template slot-scope="scope">
								<span v-if="scope.row.isAuthentication==1">已实名</span>
								<span v-else>未实名</span>
							</template>
						</el-table-column>
						<el-table-column prop="isVip" label="是否是会员">
							<template slot-scope="scope">
								<span v-if="scope.row.isVip==1">是</span>
								<span v-else>否</span>
							</template>
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
						<el-table-column label="邀请人邀请码">
							<template slot-scope="scope">
								<span class="nf-table-link">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBaoName" label="支付宝名称">
							<template slot-scope="scope">
								<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBao" label="支付宝账号">
							<template slot-scope="scope">
								<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="wxImg" label="收款二维码" width="150">
							<template slot-scope="scope">
								<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
									暂无收款二维码
								</div>
								<div v-else style="display:flex;flex-wrap: wrap;">
									<div style="margin: 2px;">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt="" slot="reference">
											<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
										</el-popover>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						<el-table-column prop="state " label="状态">
							<template slot-scope="scope">
								<span style="color: #13ce66;" v-if="scope.row.status==1">正常</span>
								<span style="color: #ff4949;" v-else>禁用</span>
							</template>
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
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
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 5,
				page: 1,
				userName: '',
				inviterCode: '',
				nickNam: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				member: -1,
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				platforms: [{
						value: 'all',
						label: '全部'
					}, {
						value: 'wap',
						label: '网站'
					}, {
						value: 'app',
						label: 'APP'
					}, {
						value: 'weixin',
						label: '微信公众号'
					},
					{
						value: 'mp',
						label: '微信小程序'
					}
				],
				campus: '',
				homeData1: [],
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime1() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.startTime = ''
				this.endTime = ''
				if (tab._props.label == '全部用户') {
					this.member = -1
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.member = 0
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.member = 1
					this.dataSelect()
				}
			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
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
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 详情跳转
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.superior
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
			handleSizeChange1(val) {
				this.limit = val
				this.paySelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.paySelect()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			select2() {
				this.paySelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				this.inviterCode = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
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
				})
			},
			// 封号
			updates1(row) {

			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page':this.page,
						// 'limit':this.limit,
						// 'content':this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData1 = returnData
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'userName': this.userName,
						'inviterCode': this.inviterCode,
						'startTime': this.startTime,
						'endTime': this.endTime
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
			// this.homeSelect()
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

	.nf-filter-form__input--date {
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

	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 0 !important;
	}
</style>
