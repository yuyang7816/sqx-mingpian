<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">入驻管理</span>
      <h2 class="nf-page-hero__title">入驻申请</h2>
      <p class="nf-page-hero__desc">查看技师入驻申请列表，支持按姓名、电话筛选查询</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">筛选条件</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">姓名</span>
          <el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入姓名"
            v-model="userName">
          </el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">电话</span>
          <el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入电话"
            v-model="phone">
          </el-input>
        </div>
        <div class="nf-filter-form__actions">
          <el-button type="primary" icon="document" @click="select">查询</el-button>
          <el-button type="primary" icon="document" @click="cleans">重置</el-button>
        </div>
      </div>
    </div>

    <el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="申请列表" name="first">
        <div class="nf-table-card">
          <el-table v-loading="tableDataLoading" :data="tableData.records">
            <el-table-column prop="id" label="编号" width="80">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="100">
              <template slot-scope="scope">
                <span class="nf-table-link"
                  @click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="cardFront" label="头像" width="150">
              <template slot-scope="scope">
                <div v-if="scope.row.img == null || scope.row.img == ''">
                  暂无图片
                </div>
                <div v-if="scope.row.img" style="display:flex;flex-wrap: wrap;">
                  <el-popover placement="top-start" title="" trigger="hover">
                    <img style="width: 50px; height: 50px" :src="scope.row.img" alt="" slot="reference">
                    <img style="width: 200px; height: 200px" :src="scope.row.img" alt="">
                  </el-popover>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="age" label="年龄">
            </el-table-column>
            <el-table-column prop="phone" label="电话">
            </el-table-column>
            <el-table-column prop="city" label="城市">
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间">
            </el-table-column>
          </el-table>
          <div class="nf-table-card__foot">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
              layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 任务拒绝弹框 -->
    <el-dialog title="任务拒绝" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
        <el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由">
        </el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="refuseto()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        limit: 10,
        page: 1,
        content: '',
        status: '',
        statesnum: [{
            value: '',
            label: '全部'
          },
          {
            value: 0,
            label: '待审核'
          }, {
            value: 1,
            label: '审核成功'
          }, {
            value: 2,
            label: '拒绝'
          }
        ],
        classify: 1,
        classlyList: [{
          value: '',
          label: '全部'
        }, {
          value: 1,
          label: '个人'
        }, {
          value: 2,
          label: '企业'
        }],
        userName: '',
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        tableData: {},
        checkBoxData: [], //多选框选择的值
        ids: '',
        phone: '',
      }
    },
    methods: {
      handleClick(tab, event) {
        this.page = 1
        if (tab._props.label == '申请列表') {
          this.classify = 1
          this.dataSelect()
        }
        if (tab._props.label == '企业认证') {
          this.classify = 2
          this.dataSelect()
        }
      },
      // 多选
      changeFun(val) {
        this.checkBoxData = val;
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
      handleSizeChange(val) {
        this.limit = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
      //任务通过
      passClick(id) {
        this.status = 1
        this.content = '同意'
        var ids = id ? [id] : this.checkBoxData.map(item => {
          return item.id
        })
        this.$http({
          url: this.$http.adornUrl(
            `student/auditStudentAuthentication/${ids}/${this.status}/${this.content}`),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = ''
                this.dataSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = ''
                this.dataSelect()
              }
            })
          }
        })
      },
      //单条任务通过
      passClicks(row) {
        this.status = 1
        this.content = '同意'
        this.$http({
          url: this.$http.adornUrl(`userCertification/auditorUserCertification`),
          method: 'post',
          params: this.$http.adornParams({
            'status': this.status,
            'id': row.id,
            'remek': '通过'
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = ''
                this.dataSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = ''
                this.dataSelect()
              }
            })
          }

        })
      },
      //任务拒绝
      refuseClick(rows) {
        this.status = 2
        this.helpTakeId = rows.userId
        this.ids = rows.id
        this.content = ''
        this.dialogFormVisible = true
      },
      refuseto() {
        if (this.content == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入拒绝理由',
            type: 'warning'
          });
          return
        }
        this.$http({
          url: this.$http.adornUrl(`userCertification/auditorUserCertification`),
          method: 'post',
          params: this.$http.adornParams({
            'status': this.status,
            'id': this.ids,
            'remek': this.content
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.content = ''
              this.status = ''
              this.dataSelect()
            }
          })
        })
      },
      // 查询
      select() {
        this.page = 1
        this.limit = 10
        this.dataSelect()
      },
      // 重置
      cleans() {
        this.status = ''
        this.userName = ''
        this.phone = ''
        this.dataSelect()
      },
      // 获取派单数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('artificer/selectAgencyList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'status': this.status,
            'userName': this.userName,
            'phone': this.phone
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData

        })
      }
    },
    mounted() {
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
    width: 200px;
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
