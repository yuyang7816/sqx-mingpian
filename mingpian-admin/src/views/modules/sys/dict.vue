<template>
	<div class="mod-dict">
		<el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
			<el-form-item>
				<el-input v-model="dataForm.name" placeholder="字典名称" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
				<el-button v-if="isAuth('sys:dict:save')" type="primary" @click="addOrUpdateHandle(1)">新增</el-button>
				<el-button v-if="isAuth('sys:dict:delete')" type="danger" @click="deleteHandle()"
					:disabled="dataListSelections.length <= 0">批量删除</el-button>
			</el-form-item>
		</el-form>
		<el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
			style="width: 100%;">
			<el-table-column type="selection" header-align="center" align="center" width="50">
			</el-table-column>
			<el-table-column prop="id" header-align="center" align="center" width="80" label="ID">
			</el-table-column>
			<el-table-column prop="name" header-align="center" align="center" label="字典名称">
			</el-table-column>
			<el-table-column prop="type" header-align="center" align="center" label="字典类型">
			</el-table-column>
			<!-- <el-table-column prop="code" header-align="center" align="center" label="字典码">
			</el-table-column>
			<el-table-column prop="value" header-align="center" align="center" width="180" label="字典值">
			</el-table-column> -->
			<el-table-column prop="orderNum" header-align="center" align="center" width="180" label="排序">
			</el-table-column>
			<el-table-column prop="remark" header-align="center" align="center" width="180" label="备注">
			</el-table-column>
			<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
				<template slot-scope="scope">
					<el-button v-if="isAuth('sys:dict:list')" type="text" size="small"
						@click="listOrUpdateHandle(scope.row.id)">列表</el-button>
					<el-button v-if="isAuth('sys:dict:update')" type="text" size="small"
						@click="addOrUpdateHandle(1,scope.row.id,scope.row.parentId)">修改</el-button>
					<el-button v-if="isAuth('sys:dict:delete')" type="text" size="small"
						@click="deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
			:page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
			layout="total, sizes, prev, pager, next, jumper">
		</el-pagination>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList" @refreshDataLister="getDataLister"></add-or-update>
		
		<el-dialog title="列表" :visible.sync="dialogFormVisible" center width="70%">
			<div style="margin: 5px 0pt;">
				<el-button v-if="isAuth('sys:dict:save')" type="primary" @click="addOrUpdateHandle(2,'',parentId)">新增</el-button>
				<el-button v-if="isAuth('sys:dict:delete')" type="danger" @click="deleteHandle()"
					:disabled="dataListSelections.length <= 0">批量删除</el-button>
			</div>
			
			<el-table :data="dataListEr.list" border v-loading="dataListLoadingEr" @selection-change="selectionChangeHandle"
				style="width: 100%;">
				<el-table-column type="selection" header-align="center" align="center" width="50">
				</el-table-column>
				<el-table-column prop="id" header-align="center" align="center" width="80" label="ID">
				</el-table-column>
				<!-- <el-table-column prop="name" header-align="center" align="center" label="字典名称">
				</el-table-column>
				<el-table-column prop="type" header-align="center" align="center" label="字典类型">
				</el-table-column> -->
				<el-table-column prop="code" header-align="center" align="center" label="字典码">
				</el-table-column>
				<el-table-column prop="value" header-align="center" align="center" width="180" label="字典值">
				</el-table-column>
				<el-table-column prop="type" header-align="center" align="center" label="图片" v-if="parentId==55" width="100">
					<template slot-scope="scope">
						<img :src="scope.row.image" width="60" height="60" v-if="scope.row.image" />
						<span v-else> - </span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNum" header-align="center" align="center" width="180" label="排序">
				</el-table-column>
				<el-table-column prop="remark" header-align="center" align="center" width="180" label="备注">
				</el-table-column>
				<el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
					<template slot-scope="scope">
						<!-- <el-button v-if="isAuth('sys:dict:list')" type="text" size="small"
							@click="listOrUpdateHandle(scope.row.parentId)">列表</el-button> -->
						<el-button v-if="isAuth('sys:dict:update')" type="text" size="small"
							@click="addOrUpdateHandle(2,scope.row.id,scope.row.parentId)">修改</el-button>
						<el-button v-if="isAuth('sys:dict:delete')" type="text" size="small"
							@click="deleteHandle(scope.row.id)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination @size-change="sizeChangeHandleL" @current-change="currentChangeHandleL" :current-page="pageIndex1"
				:page-sizes="[10, 20, 50, 100]" :page-size="pageSize1" :total="dataListEr.totalCount"
				layout="total, sizes, prev, pager, next, jumper">
			</el-pagination>
			
		</el-dialog>
	</div>
</template>

<script>
	import AddOrUpdate from './dict-add-or-update'
	export default {
		data() {
			return {
				dataForm: {
					name: ''
				},
				dataList: [],
				dataListEr:{},
				pageIndex: 1,
				pageSize: 10,
				pageIndex1: 1,
				pageSize1: 10,
				totalPage: 0,
				dataListLoading: false,
				dataListLoadingEr:false,
				dataListSelections: [],
				addOrUpdateVisible: false,
				dialogFormVisible:false,
				parentId:0
			}
		},
		components: {
			AddOrUpdate
		},
		activated() {
			this.getDataList()
			this.getDataLister()
		},
		methods: {
			// 获取数据列表
			getDataList() {
				this.dataListLoading = true
				this.$http({
					url: this.$http.adornUrl('/sys/dict/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageIndex,
						'limit': this.pageSize,
						'name': this.dataForm.name,
						'parentId':0
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dataList = data.page.list
						this.totalPage = data.page.totalCount
					} else {
						this.dataList = []
						this.totalPage = 0
					}
					this.dataListLoading = false
				})
			},
			// 每页数
			sizeChangeHandle(val) {
				this.pageSize = val
				this.pageIndex = 1
				this.getDataList()
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val
				this.getDataList()
			},
			sizeChangeHandleL(val) {
				this.pageSize1 = val
				this.pageIndex1 = 1
				this.getDataLister()
			},
			// 当前页
			currentChangeHandleL(val) {
				this.pageIndex1 = val
				
				this.getDataLister()
			},
			// 多选
			selectionChangeHandle(val) {
				this.dataListSelections = val
			},
			// 新增 / 修改
			addOrUpdateHandle(index,id,parentId) {
				this.addOrUpdateVisible = true
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(index,id,parentId)
				})
			},
			// 查看列表
			getDataLister() {
				console.log('this.parentId',this.parentId)
				this.dataListLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('/sys/dict/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageIndex1,
						'limit': this.pageSize1,
						'name': this.dataForm.name,
						'parentId':this.parentId
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dataListEr = data.page
						// this.totalPage = data.page.totalCount
					} else {
						this.dataListEr = {}
						// this.totalPage = 0
					}
					this.dataListLoadingEr = false
				})
			},
			// 查看列表
			listOrUpdateHandle(parentId){
				this.parentId = parentId
				this.getDataLister()
				this.dialogFormVisible = true
			},
			// 删除
			deleteHandle(id) {
				var ids = id ? [id] : this.dataListSelections.map(item => {
					return item.id
				})
				this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('/sys/dict/delete'),
						method: 'post',
						data: this.$http.adornData(ids, false)
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.getDataList()
									this.getDataLister()
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				}).catch(() => {})
			}
		}
	}
</script>
