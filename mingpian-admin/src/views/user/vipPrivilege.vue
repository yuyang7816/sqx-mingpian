<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">会员管理</span>
      <h2 class="nf-page-hero__title">会员特权</h2>
      <p class="nf-page-hero__desc">管理会员特权项，支持上传图标、维护名称说明与排序，可添加、修改及删除特权</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">快捷操作</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__actions">
          <el-button type="primary" plain icon="document" @click="Notice">刷新</el-button>
          <el-button type="primary" :disabled="!isAuth('bannerList:add')" icon="document"
            @click="addNotice">添加会员特权</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card">
      <el-table v-loading="tableDataLoading" :data="tableData.records">
        <el-table-column fixed prop="memberId" label="编号" width="50">
        </el-table-column>
        <el-table-column prop="memberImg" label="图片">
          <template slot-scope="scope">
            <img :src="scope.row.memberImg" width="60" height="60" />
          </template>
        </el-table-column>
        <el-table-column prop="memberName" label="特权名称">
        </el-table-column>
        <el-table-column prop="memberDescribe" label="说明">
        </el-table-column>
        <!-- <el-table-column prop="state" label="是否启用">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)" :disabled="!isAuth('bannerList:update')"
              :active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </template>
        </el-table-column> -->
        <el-table-column prop="sort" label="排序">
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
              @click="amendBanner(scope.row)">修改
            </el-button>
            <el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')" @click="deletes(scope.row)">删除
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
        <span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
        <div
          style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload class="avatar-uploader" v-model="memberImg"
            action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess">
            <img v-if="memberImg" :src="memberImg" class="avatar"
              style="border-radius: 6px;width: 148px;height: 148px;" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">说明：</span>
        <el-input style="width:50%;" v-model="describes" placeholder="请输入说明"></el-input>
      </div>
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
        tableDataLoading: true,
        dialogFormVisible: false,
        tableData: [],
        memberName: '',
        memberImg: '',
        memberId: '',
        describes: '',
        sort: '',
        title: '添加',
        state: '',
        openValue: 1,
        closeValue: 2,
      }
    },
    methods: {
      Notice() {
        this.page = 1
        this.dataSelect()
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/member/getMemberList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            // 'state': -1,
            // 'classify': 10,
            // 'type':''
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
        this.memberImg = file.data
      },
      addNotice() {
        this.memberImg = ''
        this.memberId = ''
        this.memberName = ''
        this.describes = ''
        this.sort = ''
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
        if (this.memberImg == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传图片',
            type: 'warning'
          });
          return
        }
        if (this.memberName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入名称',
            type: 'warning'
          });
          return
        }
        if (this.describes == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入说明',
            type: 'warning'
          });
          return
        }

        // this.classify = 1
        if (this.title == '添加') {
          var url = 'admin/member/saveMember'
        } else {
          var url = 'admin/member/saveMember'
        }
        this.$http({

          url: this.$http.adornUrl(url),
          method: 'post',
          // data: this.$http.adornData({
          params: this.$http.adornParams({
            // 'classify': 10,
            'memberImg': this.memberImg,
            // 'state': 1,
            'sort': this.sort,
            'memberName': this.memberName,
            'memberDescribe': this.describes,
            'memberId': this.memberId
          })

          // url: this.$http.adornUrl(url),
          // method: 'post',
          // data: this.$http.adornData({
          // 	'memberImg':this.memberImg,
          // 	'memberId':this.memberId,
          // 	'memberName':this.memberName,
          // 	'sort':this.sort
          // })
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
        this.id = rows.id
        this.memberImg = rows.memberImg
        this.memberId = rows.memberId
        this.sort = rows.sort
        this.memberName = rows.memberName
        this.describes = rows.memberDescribe
        this.title = '修改'
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
            url: this.$http.adornUrl(`admin/member/deleteMemberById?memberId=${delid}`),
            method: 'post',
            data: this.$http.adornData({})
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
      // 是否启用
      change(val, row) {
        this.$http({
          url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
          method: 'post',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          if (data.code == 0) {
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
              onClose: () => {
                this.dataSelect()
              }
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

<style scoped>
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

  .el-button+.el-button {
    margin-left: 0 !important;
    margin-top: 5px !important;
  }
</style>
