<template>
	<el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
		<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
			label-width="80px">
			<el-form-item label="字典名称" prop="name" v-if="index==1">
				<el-input v-model="dataForm.name" placeholder="参数名"></el-input>
			</el-form-item>
			<el-form-item label="字典类型" prop="type" v-if="index==1">
				<el-input v-model="dataForm.type" placeholder="参数值"></el-input>
			</el-form-item>
			<el-form-item label="字典码" prop="code" v-if="index==2">
				<el-input v-model="dataForm.code" placeholder="参数值"></el-input>
			</el-form-item>
			
			<el-form-item label="图片" prop="value" v-if="index==2&&dataForm.parentId==55">
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="dataForm.image"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="dataForm.image" :src="dataForm.image" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</el-form-item>
			<el-form-item label="字典值" prop="value" v-if="index==2">
				<el-input v-model="dataForm.value" placeholder="参数值"></el-input>
			</el-form-item>
			<el-form-item label="排序" prop="orderNum">
				<el-input-number v-model="dataForm.orderNum" controls-position="right" :min="0" label="排序号">
				</el-input-number>
			</el-form-item>
			<el-form-item label="备注" prop="remark">
				<el-input v-model="dataForm.remark" placeholder="备注"></el-input>
			</el-form-item>
		</el-form>
		<span slot="footer" class="dialog-footer">
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="dataFormSubmit()">确定</el-button>
		</span>
	</el-dialog>
</template>

<script>
	export default {
		data() {
			return {
				visible: false,
				dataForm: {
					id: 0,
					name: '',
					type: '',
					code: '',
					value: '',
					orderNum: 0,
					remark: '',
					parentId: 0,
					image:'',
				},
				dataRule: {
					// name: [
					//   { required: true, message: '字典名称不能为空', trigger: 'blur' }
					// ],
					// type: [
					//   { required: true, message: '字典类型不能为空', trigger: 'blur' }
					// ],
					// code: [
					//   { required: true, message: '字典码不能为空', trigger: 'blur' }
					// ],
					// value: [
					//   { required: true, message: '字典值不能为空', trigger: 'blur' }
					// ]
				},
				index: 0
			}
		},
		methods: {
			handleAvatarSuccess2(file2) {
				this.dataForm.image = file2.data
				console.log('file2.data',file2.data)
			},
			init(index, id, parentId) {
				this.index = index
				this.dataForm.id = id || 0
				if (index == 1) {
					this.dataForm.parentId = 0
					this.dataForm.code = ''
					this.dataForm.value = ''

				} else {
					// if(parentId){
					this.dataForm.parentId = parentId
					this.dataForm.name = ''
					this.dataForm.type = ''
					// }else{
					// 	this.dataForm.parentId = id
					// }

				}
				console.log('parentId', this.dataForm.parentId)
				this.visible = true
				this.$nextTick(() => {
					this.$refs['dataForm'].resetFields()
					if (this.dataForm.id) {
						this.$http({
							url: this.$http.adornUrl(`/sys/dict/info/${this.dataForm.id}`),
							method: 'get',
							params: this.$http.adornParams()
						}).then(({
							data
						}) => {
							if (data && data.code === 0) {
								this.dataForm.name = data.dict.name
								this.dataForm.type = data.dict.type
								this.dataForm.code = data.dict.code
								this.dataForm.value = data.dict.value
								this.dataForm.orderNum = data.dict.orderNum
								this.dataForm.remark = data.dict.remark
								this.dataForm.image = data.dict.image
								if (this.index == 1) {
									this.dataForm.code = ''
									this.dataForm.value = ''
								} else {
									this.dataForm.name = ''
									this.dataForm.type = ''
								}
							}
						})
					}
				})
			},
			// 表单提交
			dataFormSubmit() {
				this.$refs['dataForm'].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(`/sys/dict/${!this.dataForm.id ? 'save' : 'update'}`),
							method: 'post',
							data: this.$http.adornData({
								'id': this.dataForm.id || undefined,
								'name': this.dataForm.name,
								'type': this.dataForm.type,
								'code': this.dataForm.code,
								'value': this.dataForm.value,
								'orderNum': this.dataForm.orderNum,
								'remark': this.dataForm.remark,
								'parentId': this.dataForm.parentId,
								'image':this.dataForm.image,
							})
						}).then(({
							data
						}) => {
							if (data && data.code === 0) {
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.visible = false
										console.log('this.index', this.index)
										if (this.index == 1) {
											this.$emit('refreshDataList')
										} else {
											this.$emit('refreshDataLister')
										}

									}
								})
							} else {
								this.$message.error(data.msg)
							}
						})
					}
				})
			}
		}
	}
</script>
