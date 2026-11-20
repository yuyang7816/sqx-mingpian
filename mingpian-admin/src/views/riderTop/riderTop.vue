<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">排行榜</span>
			<h2 class="nf-page-hero__title">名片排行榜</h2>
			<p class="nf-page-hero__desc">查看平台热门名片排行数据，支持刷新列表，可点击真实姓名跳转用户详情</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">快捷操作</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">刷新</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="名片排行" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="homeData.list">
						<el-table-column fixed prop="card_id" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="real_name" label="真实姓名">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updatesvideo(scope.row)">
									{{ scope.row.real_name ? scope.row.real_name : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column label="头像">
							<template slot-scope="scope">
								<img v-if="scope.row.card_avatar == null" src="~@/assets/img/avatar.png" alt="" width="40"
									height="40">
								<img v-else :src="scope.row.card_avatar" alt="" width="40" height="40">
							</template>
						</el-table-column>
						<el-table-column prop="company_name" label="公司名称">
						</el-table-column>
						<el-table-column prop="company_position" label="所属岗位">
						</el-table-column>
						<el-table-column prop="sortNum" label="数量">
							<template slot-scope="scope">
								{{ scope.row.sortNum ? scope.row.sortNum : 0 }}
							</template>
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
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				size: 10,
				page: 1,
				state: '',
				limit: 10,
				classify: 6,
				classifys: 6,
				title: '',
				type: '',
				nav: '',
				name: '',
				keyword: '',
				describes: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: true,
				tableDataLoading4: false,
				dialogFormVisible3: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				homeData: {},
				homeData1: [{
						title: '帮我送',
						id: 1
					},
					{
						title: '帮我取',
						id: 2
					},
				],
				choicenData2: [],
				choicenData: [],

				url: '',
				imageUrl: '',
				imageUrl2: '',
				id: '',
				bannerData: [],
				form1: {
					// id: '',
					// url: '',
					// imageUrl: ''
				},
				formcomp: {
					id: '',
					state: '',
					title: '',
					url: '',
					imageUrl: ''
				},
				userId: '',
				search: '',
				myPhone: '',
				classifyIds: 0,
				statusId: 1,

				homeData2: [],
				quillOption: quillConfig,
				statusIdd: 1,
				statesnum: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '待支付'
					}, {
						value: 2,
						label: '待接单'
					}, {
						value: 3,
						label: '已接单'
					}, {
						value: 4,
						label: '已完成'
					}, {
						value: 5,
						label: '已取消'
					}
				],
				// x新的
				indentType: 1,
				deliveryUserPhone: '', //收货人手机号
				indentState: 0, //任务状态
				flag: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: '1',
					label: '按天查询'
				}, {
					value: '2',
					label: '按月查询'
				}, {
					value: '3',
					label: '按年查询'
				}],
				address: '',
				tableDataLoading1: false,
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
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
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
				this.indentState = 0
				this.phone = ''
				this.flag = ''
				if (tab._props.label == '名片排行') {
					this.indentType = 1
					this.homeSelect()
				}

			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				if (this.indentType == 1) {
					var url = 'admin/businessCard/hotList'
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'pageNum': this.page,
						'pageSize': this.limit,
						// 'time': this.info.stockDate,
						// 'flag': this.flag,
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
			// 信息数据
			InformationSelect() {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl(`information/selectInformationList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'search': this.search,
						'classify': this.classifyIds,
						'status': this.statusId,
						'phone': this.myPhone,
						'campus': this.campus1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}

					}
					let returnData = data.data
					this.choicenData = returnData


				})
			},

			// 审核
			shenhe(row) {
				this.shenheId = row.id
				this.dialogFormVisible8 = true
			},
			// 提交审核
			refuseto(row) {
				if (this.radio == 2 && this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入驳回理由',
						type: 'warning'
					})
					return
				} else {
					let ids = this.shenheId
					let status = this.radio
					let content = this.contents
					this.$http({
						url: this.$http.adornUrl(
							`information/auditInformation?ids=${ids}&status=${status}&content=${content}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.dialogFormVisible8 = false
						this.$message({
							message: '审核完成',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.radio = 1
								this.contents = ''
								this.InformationSelect()
							}
						})
					})
				}
			},
			// 修改信息弹框
			choiCompile(index, row) {
				console.log(index, row)

			},
			// 删除信息
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`information/deleteInformationById?ids=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 筛选信息
			animeDat2(classifyIds) {
				this.classifyIds = classifyIds
				this.homeSelect()
			},
			animeDat3(state) {
				this.InformationSelect()
			},
			// 多选
			handleSelectionChange(val) {

				var arr = []
				for (var i in val) {
					arr.push(val[i].id)
				}
				this.multipleSelection = arr;
				console.log('val', val, this.multipleSelection)
			},
			// 批量删除
			choideletes() {
				console.log(this.multipleSelection)
				let delid = this.multipleSelection.toString()
				this.$confirm(`确定删除信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`information/deleteInformationById?ids=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取社区数据列表
			homeSelect1() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData2 = returnData
				})
			},
			// 分类是否启用
			change2(val, row) {
				this.$http({
					url: this.$http.adornUrl(`activity/updateActivityStatus?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.homeSelect()
						}
					})
				})
			},

			// 任务上下架
			change3(val, row) {
				this.$http({
					url: this.$http.adornUrl(`information/updateInformationStatus?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.homeSelect()
						}
					})
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
				this.indentState = 0
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
				// if (vanumber === 1) {
				this.flag = value
				this.homeSelect()
				// this.colonel()
				// }
				// if (vanumber === 2) {
				// 	this.flag = 2
				// 	this.taskData()
				// 	this.colonel()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.taskData()
				// 	this.colonel()
				// }
			},
			// 导出
			exportBtn1() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/excelLeaderboardUserList'),
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
						elink.download = '代理发布采购需求数量排行列表.xlsx'
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
					url: this.$http.adornUrl('admin/procureDemand/excelLeaderboardMoneyList'),
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
						elink.download = '代理用户佣金排行列表.xlsx'
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

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
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
