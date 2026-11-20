<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">积分管理</span>
      <h2 class="nf-page-hero__title">点数充值记录</h2>
      <p class="nf-page-hero__desc">查看用户点数变动明细，包含充值、消费等用途记录，支持刷新列表数据</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">操作</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__actions">
          <el-button type="primary" icon="document" @click="timeDate2">刷新</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card">
      <el-table v-loading="tableDataLoading" :data="tableData.records">
        <el-table-column prop="detailsId" label="编号" width="50"></el-table-column>
        <el-table-column prop="userId" label="用户编号">
          <template slot-scope="scope">
            <span class="nf-table-link" @click="updates(scope.row)">{{ scope.row.userId ? scope.row.userId : '未绑定' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="用途">
          <template slot-scope="scope">
            <span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 1">获取加群方式</span>
            <span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 2">充值</span>
            <span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 3">查看采购联系方式</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastCount" label="上一次点数"></el-table-column>
        <el-table-column prop="count" label="支付点数">
          <template slot-scope="scope">
            <span style="color: #2f3968;" v-if="scope.row.type == 1">+ {{ scope.row.count }}</span>
            <span style="color: #f56c6c;" v-if="scope.row.type == 2">- {{ scope.row.count }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="nextCount" label="当前点数"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170">
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
          <el-upload class="avatar-uploader" v-model="memberImg" :action="$http.adornUrl('alioss/upload')"
            :show-file-list="false" :on-success="handleAvatarSuccess">
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
        sort: '',
        title: '添加',
        startTime: '',
        endTime: '',
        info: {
          stockDate: this.getNowTime(), //日期
        },
        info2: {
          stockDate2: this.getNowTime2(), //日期
        },
      }
    },
    methods: {
      //处理默认选中当前日期
      getNowTime() {
        var now = new Date()
        var year = now.getFullYear() //得到年份
        var month = now.getMonth() //得到月份
        var date = now.getDate() //得到日期
        month = month + 1
        month = month.toString().padStart(2, '0')
        date = date.toString().padStart(2, '0')
        var defaultDate = `${year}-${month}-${date}`
        return defaultDate
        this.$set(this.info, 'stockDate', defaultDate)
      },
      //处理默认选中当前日期
      getNowTime2() {
        var now = new Date()
        var year = now.getFullYear() //得到年份
        var month = now.getMonth() - now.getMonth() //得到月份
        var date = now.getDate() - now.getDate() + 1 //得到日期
        month = month + 1
        month = month.toString().padStart(2, '0')
        date = date.toString().padStart(2, '0')
        var defaultDate = `${year}-${month}-${date}`
        return defaultDate
        this.$set(this.info, 'stockDate', defaultDate)
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
      // 获取数据列表
      dataSelect() {

        this.tableDataLoading = true
        if (this.endTime == '') {
          this.endTime = this.info.stockDate
        }
        if (this.startTime == '') {
          this.startTime = this.info2.stockDate2
        }
        this.$http({
          url: this.$http.adornUrl('admin/countMoneyDetails/getCountMoneyDetailsList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'type': '',
            'endTime': this.endTime,
            'startTime': this.startTime,
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
      timeDate2() {
        this.dataSelect()
      },
      //上传成功
      handleAvatarSuccess(file) {
        this.memberImg = file.data
      },
      addNotice() {
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
        this.classify = 1
        if (this.title == '添加') {
          var url = 'member/insertMember'
        } else {
          var url = 'member/updateMember'
        }
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post',
          data: this.$http.adornData({
            'memberImg': this.memberImg,
            'memberId': this.memberId,
            'memberName': this.memberName,
            'sort': this.sort
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
              this.memberImg = ''
              this.memberId = ''
              this.memberName = ''
              this.sort = ''
              this.title = '添加'
              this.dataSelect()
            }
          })
        })
      },
      // 修改
      amendBanner(index, rows, a) {
        if (a == 1) {
          this.pl = 1
        } else {
          this.pl = 0
        }
        this.dialogFormVisible = true
        this.id = rows.id
        this.memberImg = rows.memberImg
        this.memberId = rows.memberId
        this.sort = rows.sort
        this.memberName = rows.memberName
        this.title = '修改'
      },
      // 修改
      amendNoticeTo() {
        this.$http({
          url: this.$http.adornUrl(
            `?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`
          ),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
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
      //删除一级
      deletes(row) {
        let delid = row.memberId
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`member/deleteMemberById/?memberId=${delid}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dataSelect()
              }
            })
          })
        }).catch(() => {})
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
