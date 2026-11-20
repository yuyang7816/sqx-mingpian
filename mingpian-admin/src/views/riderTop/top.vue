<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">排行榜</span>
			<h2 class="nf-page-hero__title">合伙人排行榜</h2>
			<p class="nf-page-hero__desc">查看合伙人数量与业绩排行，支持按天/月/年筛选与导出，可点击用户昵称跳转用户详情</p>
		</div>

		<div class="nf-filter-card" v-if="type != 8">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">时间类型</span>
					<el-select v-model="flag" class="nf-filter-form__input" @change="orderfenxi">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item" v-if="flag == 1">
					<span class="nf-filter-form__label">选择时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="info.stockDate"
						align="right" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择时间"
						@change="select">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item" v-if="flag == 2">
					<span class="nf-filter-form__label">选择时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="info.stockDate"
						align="right" type="month" format="yyyy-MM" value-format="yyyy-MM-dd" placeholder="选择时间"
						@change="select">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item" v-if="flag == 3">
					<span class="nf-filter-form__label">选择时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="info.stockDate"
						align="right" type="year" format="yyyy" value-format="yyyy-MM-dd" placeholder="选择时间"
						@change="select">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button type="warning" icon="document" v-loading="tableDataLoading1" v-if="type == 1"
						@click="exportBtn1" :disabled="!isAuth('financeList:daochu')">导出Excel</el-button>
					<el-button type="warning" icon="document" v-loading="tableDataLoading1" v-if="type == 2"
						@click="exportBtn2" :disabled="!isAuth('financeList:daochu')">导出Excel</el-button>
				</div>
			</div>
			<p class="nf-filter-tip">* 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</p>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="合伙人数量排行" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="homeData.records">
						<el-table-column fixed prop="userId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="合伙人用户昵称">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updatesvideo(scope.row)">
									{{ scope.row.userName ? scope.row.userName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column label="头像">
							<template slot-scope="scope">
								<img v-if="scope.row.avatar == null" src="~@/assets/img/avatar.png" alt="" width="40"
									height="40">
								<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="counts" label="数量">
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
							:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="合伙人业绩排行榜" name="second">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="homeData.records">
						<el-table-column fixed prop="userId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="合伙人用户昵称">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updatesvideo(scope.row)">
									{{ scope.row.userName ? scope.row.userName : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column label="头像">
							<template slot-scope="scope">
								<img v-if="scope.row.avatar == null" src="~@/assets/img/avatar.png" alt="" width="40"
									height="40">
								<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="counts" label="收益">
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
							:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
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
				size: 10,
				page: 1,
				state: '',
				limit: 10,
				title: '',
				type: 1,
				activeName: 'first',
				tableDataLoading: true,
				homeData: [],
				flag: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				activeName: 'first',
				tableDataLoading1:false,
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			handleSizeChange1(val) {
				this.limit = val
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.homeSelect()
			},
			handleClick(tab, event) {
				this.page = 1
				this.flag = ''
				if (tab._props.label == '合伙人数量排行') {
					this.type = 1
					this.homeSelect()
				}
				if (tab._props.label == '合伙人业绩排行榜') {
					this.type = 2
					this.homeSelect()
				}
				
			},
			// 信息数据
			homeSelect() {
				this.tableDataLoading = true
				if(this.type == 1){
					var url = 'admin/staffData/geLeaderboardUserList'
				}else{
					var url = 'admin/staffData/geLeaderboardMoneyList'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'type': this.type,
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data

					console.log(data.data.state)
					this.homeData = data.data
				})
			},

			// 详情跳转
			updatesvideo(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				} else {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '未绑定用户',
						type: 'warning'
					})
					return
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.homeSelect()
			},
			// 重置
			cleans() {
				this.address = ''
				this.flag = ''
				this.info.stockDate = this.getNowTime()
				this.homeSelect()
			},
			// 订单分析选择日期
			animeOrder() {
				console.log('info', this.info)
				this.homeSelect()
				// this.colonel()
			},
			// 订单分析年月日
			orderfenxi(value) {
				this.page = 1
				let vanumber = value
				this.flag = value
				this.homeSelect()

			},
			// 导出
			exportBtn1() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/staffData/excelLeaderboardUserList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '合伙人数量排行列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 导出
			exportBtn2() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/staffData/excelLeaderboardMoneyList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '合伙人业绩排行列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
		},
		mounted() {
			this.homeSelect()
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
		width: 180px;
	}

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		flex-wrap: wrap;
	}

	.nf-filter-tip {
		margin: 12px 0 0;
		font-size: 12px;
		color: #e6a23c;
		line-height: 1.5;
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
