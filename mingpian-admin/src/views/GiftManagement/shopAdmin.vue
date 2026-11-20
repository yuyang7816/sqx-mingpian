<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">礼品管理</span>
			<h2 class="nf-page-hero__title">礼品管理</h2>
			<p class="nf-page-hero__desc">管理礼品商品发布与上下架，支持按商品标题、类型、状态筛选，可发布、修改、删除及查看评价</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">商品标题</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="select" clearable
						placeholder="请输入商品标题" v-model="title">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">商品类型</span>
					<el-select v-model="typeId" placeholder="请选择商品类型" class="nf-filter-form__input" @change="select()">
						<el-option v-for="item in classDatas" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">商品状态</span>
					<el-select v-model="status" placeholder="请选择商品状态" class="nf-filter-form__input" @change="select()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('shopAdminGift:add')" type="primary" icon="document"
						@click="addShop">发布商品</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="礼品列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.content">
						<el-table-column fixed prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column fixed prop="coverImg" label="商品图片">
							<template slot-scope="scope">
								<img :src="scope.row.coverImg" alt="" width="60" height="60">
							</template>
						</el-table-column>
						<el-table-column prop="type" label="商品类型">
							<template slot-scope="scope">
								<span v-if="scope.row.type == null">暂无分类</span>
								<span v-else>{{scope.row.type.name}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="title" label="商品标题" width="200">
						</el-table-column>
						<el-table-column prop="originalPrice" label="商品原价(金币)">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="price" label="商品价格(金币)">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="sales" label="商品销量">
						</el-table-column>
						<el-table-column prop="buyReason" label="必买理由" width="200">
						</el-table-column>
						<el-table-column label="是否需要发货" width="150">
							<template slot-scope="scope">
								<span v-if="scope.row.isExpress == 2">虚拟商品无需发货</span>
								<span v-if="scope.row.isExpress == 1">普通商品需要发货</span>
							</template>
						</el-table-column>
						<el-table-column prop="status" label="商品状态">
							<template slot-scope="scope">
								<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.status === 1 ">上架</span>
								<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.status === 2 ">下架</span>
							</template>
						</el-table-column>
						<el-table-column prop="createAt" label="创建时间" width="160">
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="240">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="discuss(scope.$index, scope.row)">评价
								</el-button>
								<el-button size="mini" type="primary" :disabled="!isAuth('shopAdminGift:update')"
									@click="shopUpdates(scope.$index, scope.row)">修改
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('shopAdminGift:delete')"
									@click="shopdeletes(scope.row)">
									删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="totalnum">
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
				size: 10,
				page: 1,
				pagelist: [],
				id: '',
				img: '',
				title: '',
				type: '',
				typeId: '',
				readonly: true,
				merchants: '',
				price: '',
				memberPrice: '',
				originalPrice: '',
				sales: '',
				status: 0,
				status2: '',
				descrition: '',
				totalnum: 0,
				imgUrl: '',
				linkUrl: '',
				value: [],
				classDatas: [],
				form1: {
					id: '',
					title: '',
					img: '',
					typeId: '',
					merchants: '',
					price: '',
					memberPrice: '',
					status: '',
					sales: '',
					descrition: '',
					originalPrice: '',
					createAt: ''
				},
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: {},
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后三位
					let tempVal = parseFloat(value).toFixed(3)
					realVal = tempVal.substring(0, tempVal.length - 1)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		computed: {

		},
		methods: {
			handleClick(tab, event) {
				if (tab._props.label == '礼品列表') {
					this.page = 1
					this.shopSelect()
				}
			},
			// 商品评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: '/discuss',
					query: {
						goodsId: rows.id
					}
				});
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.typeId = element;
				});
			},

			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleAvatarSuccess2(file) {
				this.form1.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.shopSelect()
			},
			//搜索
			select() {
				this.page = 1
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = 0
				this.typeId = ''
				this.value = []
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublishGift'
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: '/shopAmendGift',
					query: {
						id: rows.id
					}
				});
			},
			// 删除商品
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('shopGoods/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取商品数据
			shopSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopGoods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.totalnum = returnData.totalElements
				})
			},
			// 商品分类
			dataSelect() {
				this.$http({
					url: this.$http.adornUrl('goodsType/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classDatas = returnData
				})
			}
		},
		mounted() {
			this.shopSelect()
			this.dataSelect()
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

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>
