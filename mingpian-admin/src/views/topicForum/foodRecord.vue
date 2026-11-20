<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">话题论坛</span>
      <h2 class="nf-page-hero__title">美食记录</h2>
      <p class="nf-page-hero__desc">查看用户发布的美食动态记录，支持查看发布用户详情与删除操作</p>
    </div>

    <div class="nf-table-card">
      <el-table v-loading="tableDataLoading" :data="guangaoData.content">
        <el-table-column fixed prop="cateTalkId" label="编号" width="80"></el-table-column>
        <el-table-column prop="nickName" label="发布用户" width="150">
          <template slot-scope="scope">
            <span class="nf-table-link" @click="updates(scope.row)">{{ scope.row.nickName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" width="180">
        </el-table-column>
        <el-table-column prop="content" label="内容">
          <template slot-scope="scope">
            <div class="nf-table-content">{{ scope.row.content }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="img" label="图片" width="100">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="80" height="80">
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" :disabled="!isAuth('materialLink:delete')"
              @click="advertdeletes(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="nf-table-card__foot">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
          layout="total,sizes, prev, pager, next,jumper" :total="guangaoData.totalElements">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        size:10,
        page:1,
        imgUrl: '',
        linkUrl: '',
        type: 3,
        form: {
          id: '',
          imgUrl: '',
          linkUrl: '',
          createAt: ''
        },
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible: false,
        guangaoData: {},
      }
    },
    methods: {
      handleAvatarSuccess(file) {
        this.imgUrl = file.data
        this.linkUrl = this.imgUrl
      },
      handleAvatarSuccess1(file) {
        this.form.imgUrl = file.data
        this.form.linkUrl = this.form.imgUrl
      },
      handleSizeChange(val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.guangaoSelect()
      },
      handleSizeChange1(val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange1(val) {
        this.page = val
        this.guangaoSelect()
      },
      // 刷新
      refresh() {
        this.choicenSelect()
        this.handpick()
      },
      // 添加话题弹框
      advertNotice() {
        this.dialogFormVisible = true
      },
      // 添加话题
      advertNoticeTo() {
        if (this.linkUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入跳转链接',
            type: 'warning'
          })
          return
        }
        if (this.imgUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传话题图',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`banner/update/-1?url=${this.linkUrl}&image=${this.imgUrl}&type=${this.type}`),
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
              this.imgUrl = ''
              this.linkUrl = ''
              this.guangaoSelect()
            }
          })
        })
      },
      // 谈论列表
      talkAbout(row) {
        this.dialogFormVisible1 = true
      },
      // 点击修改
      advertUpdates(rows) {
        this.form.id = rows.id
        this.form.imgUrl = rows.image_url
        this.form.linkUrl = rows.url
        this.dialogFormVisible1 = true
      },
      // 确定修改
      amendadvertTo() {
        if (this.form.linkUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入跳转链接',
            type: 'warning'
          })
          return
        }
        if (this.form.imgUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传话题图',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(
              `banner/update/${this.form.id}?url=${this.form.linkUrl}&image=${this.form.imgUrl}&type=${this.type}`
              ),
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
              this.guangaoSelect()
              this.dialogFormVisible1 = false
            }
          })
        })
      },
      // 删除广告
      advertdeletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`gambit/deleteCateTalkById`),
            method: 'post',
            params: this.$http.adornParams({
              'cateTalkId':row.cateTalkId
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.guangaoSelect()
              }
            })
          })
        })
      },
      // 获取话题数据
      guangaoSelect() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('gambit/selectCateTalkPage'),
          method: 'get',
          params: this.$http.adornParams({
            'page':this.page-1,
            'size':this.size
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.guangaoData = returnData
        })
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
    },
    mounted() {
      this.guangaoSelect()
    }
  }
</script>

<style scoped="scoped">
  .nf-list-page {
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

  .nf-table-content {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
    max-height: 80px;
  }

  .nf-table-card__foot {
    display: flex;
    justify-content: center;
    padding-top: 12px;
  }
</style>
