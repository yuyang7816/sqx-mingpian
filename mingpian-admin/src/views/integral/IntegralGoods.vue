<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">积分管理</span>
      <h2 class="nf-page-hero__title">点数充值配置</h2>
      <p class="nf-page-hero__desc">管理点数充值套餐配置，支持设置价格、支付点数、说明图片与启用状态，可添加、修改与删除</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">快捷操作</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__actions">
          <el-button type="primary" icon="document" @click="Notice">刷新</el-button>
          <el-button type="primary" icon="document" :disabled="!isAuth('IntegralGoods:add')" @click="addNotice">添加</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card">
    <el-table v-loading="tableDataLoading" :data="tableData.records">

      <el-table-column fixed prop="configId" label="编号" width="50">
      </el-table-column>
      <el-table-column prop="remarkImg" label="说明图片">
        <template slot-scope="scope">
          　　<img :src="scope.row.remarkImg" width="60" height="60" />
        </template>
      </el-table-column>
	  <el-table-column prop="image" label="图片">
	    <template slot-scope="scope">
	      　　<img v-if="scope.row.image" :src="scope.row.image" width="60" height="60" />
	    </template>
	  </el-table-column>
      <el-table-column prop="money" label="价格">
      </el-table-column>
      <el-table-column prop="countMoney" label="支付点数">
      </el-table-column>
      <el-table-column prop="isOnlyOnce" label="是否限购一次">
				<template slot-scope="scope">
					<span v-if="scope.row.isOnlyOnce == 1">是</span>
					<span v-else>否</span>
				</template>
      </el-table-column>
     <!-- <el-table-column prop="remark" label="充值说明">
      </el-table-column> -->
      <el-table-column prop="isEnable" label="是否启用">
        <template slot-scope="scope">
        	<el-switch v-model="scope.row.isEnable" :active-value="openValue"
        		:disabled="!isAuth('userList:delete')" :inactive-value="closeValue" active-color="#13ce66"
        		inactive-color="#ff4949" @change="change(scope.row)">
        	</el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序">
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" :disabled="!isAuth('IntegralGoods:update')"
            @click="amendBanner(scope.row)">修改
          </el-button>
          <el-button size="mini" type="danger" :disabled="!isAuth('IntegralGoods:delete')"
            @click="deletes(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="nf-table-card__foot">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
        layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
      </el-pagination>
    </div>
    </div>
    <el-dialog :title="title" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;display: flex;">
    		<span style="width: 200px;display: inline-block;text-align: right;">说明图片：</span>
    		<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
    			<el-upload
    				class="avatar-uploader"
    				v-model="remarkImg"
    				:action="$http.adornUrlUp('alioss/upload')"
    				:show-file-list="false"
    				:on-success="handleAvatarSuccess"
    				>
    				<img v-if="remarkImg" :src="remarkImg" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
    				<i v-else class="el-icon-plus avatar-uploader-icon"></i>
    			</el-upload>
    		</div>
    	</div>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload
						class="avatar-uploader"
						v-model="image"
						:action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false"
						:on-success="handleAvatarSuccess2"
						>
						<img v-if="image" :src="image" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">价格：</span>
        <el-input style="width:50%;" v-model="money" placeholder="请输入价格"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">支付点数：</span>
        <el-input style="width:50%;" v-model="countMoney" placeholder="请输入支付点数"></el-input>
      </div>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否限购一次：</span>
        <el-radio-group v-model="isOnlyOnce">
        	<el-radio :label="1">是</el-radio>
        	<el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否启用：</span>
        <el-radio-group v-model="isEnable">
        	<el-radio :label="1">启用</el-radio>
        	<el-radio :label="0">不启用</el-radio>
        </el-radio-group>
      </div>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">充值说明：</span>
        <el-input style="width:50%;" v-model="remark" placeholder="请输入充值说明"></el-input>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" placeholder="请输入排序"
          onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
      </div>
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
        closeValue: 0,
        openValue: 1,
        tableDataLoading: true,
        dialogFormVisible: false,
        tableData: {},
        configId: 1, //配置id(新增不传)
        money: 0.01, //价格
        countMoney: 10, //支付点数
        isEnable: 1, //是否启用 0不启用 1启用
        isOnlyOnce: 1, //是否限购一次
        sort: 1, //排序
        remark:'',
        remarkImg:'',
				image:'',
        title: '添加'
      }
    },
    methods: {
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/countMoneyConfig/getCountMoneyConfigList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit
          })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            console.log('会员列表', data)
            this.tableDataLoading = false
            let returnData = data.data;
            this.tableData = returnData;
          }

        })
      },
      //上传成功
      handleAvatarSuccess(file) {
        this.remarkImg = file.data
      },
			handleAvatarSuccess2(file) {
			  this.image = file.data
			},
      Notice() {
        this.page = 1
        this.dataSelect()
      },
      addNotice() {
        this.configId = ''
        this.money = ''
        this.countMoney = ''
        this.isEnable = 1
        this.isOnlyOnce = 1
        this.sort = ''
        this.remarkImg = ''
				this.image = ''
        this.remark = ''
        this.title = '添加'
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
      // 添加banner图
      addNoticeTo() {
        if (this.price === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入售价',
            type: 'warning'
          });
          return
        }
        if (this.money === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入到账金额',
            type: 'warning'
          });
          return
        }
        if (this.giveMoney === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入赠送金额',
            type: 'warning'
          });
          return
        }
        this.classify = 1
        if (this.title == '添加') {
          var url = 'admin/countMoneyConfig/saveCountMoneyConfig'
        } else {
          var url = 'admin/countMoneyConfig/saveCountMoneyConfig'
        }
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post',
          data: this.$http.adornData({
          // params: this.$http.adornParams({
            'money': this.money,
            'configId': this.configId,
            'countMoney': this.countMoney,
            'sort': this.sort,
            'isEnable': this.isEnable,
            'isOnlyOnce': this.isOnlyOnce,
            'remarkImg': this.remarkImg,
						'image':this.image,
            'remark': this.remark,
          },false,'from')
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
      // 修改
      amendBanner(rows) {
        this.dialogFormVisible = true
        this.configId = rows.configId
        this.money = rows.money
        this.countMoney = rows.countMoney
        this.isEnable = rows.isEnable
        this.isOnlyOnce = rows.isOnlyOnce
        this.sort = rows.sort
        if(rows.remarkImg){
          this.remarkImg = rows.remarkImg
        }else{
          this.remarkImg = ''
        }
				if(rows.image){
				  this.image = rows.image
				}else{
				  this.image = ''
				}
        if(rows.remark){
          this.remark = rows.remark
        }else{
          this.remark = ''
        }

        this.title = '修改'
      },
      //删除一级
      deletes(row) {
        let delid = row.configId
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`admin/countMoneyConfig/deleteCountMoneyConfigById?configId=${delid}`),
            method: 'post',
            params: this.$http.adornParams({})
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
        }).catch(() => {})
      },
      //启用/不启用
      change(row) {
      	this.$http({
      		url: this.$http.adornUrl('admin/countMoneyConfig/saveCountMoneyConfig'),
      		method: 'post',
      		data: this.$http.adornData({
      		// params: this.$http.adornParams({
      		  'money': row.money,
      		  'configId': row.configId,
      		  'countMoney': row.countMoney,
      		  'sort': row.sort,
      		  'isEnable': row.isEnable,
      		  'isOnlyOnce': row.isOnlyOnce,
      		  'remarkImg': row.remarkImg,
      			'image':row.image,
      		  'remark': row.remark,
      		},false,'from')
      	}).then(({
      		data
      	}) => {
      		if (data.code == 0) {
      			this.$message({
      				showClose: true,
      				message: '修改成功',
      				type: 'success'
      			})
      			this.dataSelect()
      		} else {
      			this.$message({
      				showClose: true,
      				message: data.msg,
      				type: 'error'
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

  .nf-filter-form__actions {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
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
