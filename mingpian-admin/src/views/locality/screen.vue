<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">本地配置</span>
      <h2 class="nf-page-hero__title">筛选配置</h2>
      <p class="nf-page-hero__desc">管理价格筛选区间配置，支持查询、添加、修改与删除筛选规则</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">快捷操作</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__actions">
          <el-button icon="document" type="primary" @click="phoneSelect">查询</el-button>
          <el-button :disabled="!isAuth('screen:add')" icon="document" type="primary"
            @click="openDialog(null,1)">添加</el-button>
        </div>
      </div>
      <p class="nf-screen-tip">注：价格单位为万元</p>
    </div>

    <div class="nf-table-card">
      <el-table v-loading="tableDataLoading" :data="classList.records">
        <el-table-column label="编号" prop="screenId" width="80">
        </el-table-column>
        <el-table-column label="类型" prop="type">
          <template slot-scope="scope">
            <div v-if="scope.row.type==1">价格筛选</div>
            <div v-if="scope.row.type==2">车龄筛选</div>
            <div v-if="scope.row.type==3">里程筛选</div>
          </template>
        </el-table-column>
        <el-table-column label="最小值" prop="minMoney">
        </el-table-column>
        <el-table-column label="最大值" prop="maxMoney">
        </el-table-column>
        <el-table-column label="排序" prop="sort">
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160">
        </el-table-column>
        <el-table-column align="center" fixed='right' label="操作" prop="id" width="240">
          <template slot-scope="scope">
            <el-button :disabled="!isAuth('screen:update')" size="mini" type="primary" @click="openDialog(scope.row,2)">修改
            </el-button>
            <el-button :disabled="!isAuth('screen:delete')" size="mini" type="danger" @click="kongtoudelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="nf-table-card__foot">
        <el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
          :total="classList.total" layout="total,sizes, prev, pager, next,jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </div>
    <!--新增分类弹窗-->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="60%" center>
      <el-form ref="dynamicValidateForm" :model="dynamicValidateForm" class="demo-dynamic" label-width="100px">
        <el-form-item label="类型" prop="minMoney">
          <el-radio-group v-model="dynamicValidateForm.type">
            <el-radio :label="1">价格筛选</el-radio>
            <!-- <el-radio :label="2">车龄筛选</el-radio>
            <el-radio :label="3">里程筛选</el-radio> -->
          </el-radio-group>
        </el-form-item>
        <el-form-item label="最小值" prop="minMoney">
          <el-input v-model="dynamicValidateForm.minMoney" placeholder="请输入最小值"></el-input>
        </el-form-item>
        <el-form-item label="最大值" prop="maxMoney">
          <el-input v-model="dynamicValidateForm.maxMoney" placeholder="请输入最大值"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="dynamicValidateForm.sort" placeholder="请输入排序" type="number"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('dynamicValidateForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        mubanData2: [],
        mubanData: [],
        typeT: 1,
        show: false,
        dynamicValidateForm: {
          minMoney: '',
          maxMoney: '',
          sort: '',
          type: 1,
        },
        title: '新增',
        dialogVisible: false,
        closeValue: 0,
        openValue: 1,
        statusKt: '',
        statuss: [{
            value: '',
            title: '全部'
          },
          {
            value: 1,
            title: '价格筛选'
          },
          // {
          //   value: 2,
          //   title: '车龄筛选'
          // },
          // {
          //   value: 3,
          //   title: '里程筛选'
          // }
        ],
        search: '',
        page: 1,
        limit: 10,
        classList: {},
        tableDataLoading: false,
        show2: false,
        columnId: '',
        classId: '',
      }
    },
    mounted() {
      this.getClassList()
    },
    methods: {
      //新增/修改分类
      submitForm(formName) {

        if (this.dynamicValidateForm.type === '' || this.dynamicValidateForm.type == null) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择类型',
            type: 'warning'
          })
          return
        }
        if (this.dynamicValidateForm.minMoney === '' && this.dynamicValidateForm.maxMoney === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '最小值和最大值不能同时为空',
            type: 'warning'
          })
          return
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.title == '修改') {
              this.$http({
                url: this.$http.adornUrl('admin/screenMoney/saveScreenMoney'),
                method: 'post',
                params: this.$http.adornParams(this.dynamicValidateForm)
              }).then(({
                data
              }) => {
                if (data.code == 0) {
                  this.$message({
                    type: 'success',
                    message: '修改成功!'
                  })
                  this.dialogVisible = false
                  this.getClassList()
                } else {
                  this.$message({
                    type: 'error',
                    message: data.msg
                  })
                  // this.dialogVisible = false
                  // this.getClassList()
                }
              })
            } else {
              this.$http({
                url: this.$http.adornUrl('admin/screenMoney/saveScreenMoney'),
                method: 'post',
                params: this.$http.adornParams(this.dynamicValidateForm)
              }).then(({
                data
              }) => {
                if (data.code == 0) {
                  this.$message({
                    type: 'success',
                    message: '新增成功!'
                  })
                  this.dialogVisible = false
                  this.getClassList()
                } else {
                  this.$message({
                    type: 'error',
                    message: data.msg
                  })
                  // this.dialogVisible = false
                  // this.getClassList()
                }
              })
            }

          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      //打开弹窗
      openDialog(row, type) {
        if (type == 1) {
          this.title = '新增'
          this.dynamicValidateForm = {
            minMoney: '',
            maxMoney: '',
            sort: '',
            screenId: '',
            type: 1,
          }
        } else {
          this.title = '修改'
          this.dynamicValidateForm = {
            minMoney: row.minMoney,
            maxMoney: row.maxMoney,
            sort: row.sort,
            screenId: row.screenId,
            type: row.type
          }
        }
        this.dialogVisible = true
      },

      //删除分类
      kongtoudelete(row) {
        this.$http({
          url: this.$http.adornUrl('admin/screenMoney/deleteScreenMoney'),
          method: 'post',
          params: this.$http.adornParams({
            'screenId': row.screenId,
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getClassList()
          } else {
            this.$message({
              showClose: true,
              message: data.msg,
              type: 'error'
            })
          }
        })
      },
      //查询
      phoneSelect() {
        this.page = 1
        this.getClassList()
      },
      //获取分类列表数据
      getClassList() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/screenMoney/getScreenMoneyList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'type': this.typeT
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          if (data.code == 0) {
            this.classList = data.data
          }
        })
      },
      //分页
      handleSizeChange(val) {
        this.limit = val
        this.getClassList()
      },
      handleCurrentChange(val) {
        this.page = val
        this.getClassList()
      },
      select() {
        this.page = 1
        this.getClassList()
      },
    },

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

  .nf-screen-tip {
    margin: 12px 0 0;
    font-size: 13px;
    color: #e6a23c;
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
