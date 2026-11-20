<template>
  <div class="nf-list-page">
    <div class="nf-page-hero">
      <span class="nf-page-hero__badge">账户安全</span>
      <h2 class="nf-page-hero__title">修改密码</h2>
      <p class="nf-page-hero__desc">修改当前登录账号密码，需输入原密码并设置新密码，确认一致后提交保存</p>
    </div>

    <div class="nf-table-card nf-form-card">
      <div class="nf-filter-card__head">密码信息</div>
      <div class="nf-form-row">
        <span class="nf-form-row__label">原密码：</span>
        <el-input
          class="nf-form-row__control"
          v-model="password"
          type="password"
          placeholder="请输入原密码"
        ></el-input>
      </div>
      <div class="nf-form-row">
        <span class="nf-form-row__label">新密码：</span>
        <el-input
          class="nf-form-row__control"
          v-model="newPassword"
          type="password"
          placeholder="请输入新密码"
        ></el-input>
      </div>
      <div class="nf-form-row">
        <span class="nf-form-row__label">确认密码：</span>
        <el-input
          class="nf-form-row__control"
          v-model="qurrenPassword"
          type="password"
          placeholder="请输入确认密码"
        ></el-input>
      </div>
      <div class="nf-form-footer">
        <el-button
          type="primary"
          @click="releasNoticeTo()"
          :disabled="!isAuth('updatePasswordRm:update')"
        >确 定</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        password:'',
		newPassword:'',
		qurrenPassword:'',
      }
    },
    methods: {

      //添加会员配置
      releasNoticeTo() {

        if (this.password == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入原密码',
            type: 'warning'
          });
          return
        }

        if (this.newPassword == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入新密码',
            type: 'warning'
          });
          return
        }
		if(this.qurrenPassword==''){
			this.$notify({
			  title: '提示',
			  duration: 1800,
			  message: '请输入确认密码',
			  type: 'warning'
			});
			return
		}else{
			if (this.newPassword != this.qurrenPassword) {
			  this.$notify({
			    title: '提示',
			    duration: 1800,
			    message: '新密码与确认密码不一致，请重新输入',
			    type: 'warning'
			  });
			  return
			}
		}

        this.$http({
          url: this.$http.adornUrl('sys/user/password'),
          method: 'post',
          data: this.$http.adornData({
            'password': this.password,
            'newPassword': this.newPassword
          })
        }).then(({
          data
        }) => {
          if(data.code==0){
			  var that  = this
			  that.$message({
			    message: '操作成功',
			    type: 'success',
			    duration: 1500,
			    onClose: () => {
			      that.password = ''
			      that.newPassword = ''
				  that.qurrenPassword = ''
			    }
			  })
		  }else{
			  this.$message({
			    message: data.msg,
			    type: 'warning',
			    duration: 1500,
			    onClose: () => {
			    }
			  })
		  }

        })
      },

    },
    mounted() {
    }
  }
</script>

<style scoped="scoped">
  .nf-list-page {
    padding: 0;
  }

  .nf-form-card {
    margin-top: 0;
    margin-bottom: 0;
    padding: 16px;
  }

  .nf-form-row {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
  }

  .nf-form-row__label {
    flex-shrink: 0;
    width: 200px;
    padding-right: 12px;
    font-size: 13px;
    color: var(--nf-text-regular, #303133);
    text-align: right;
    line-height: 32px;
  }

  .nf-form-row__control {
    flex: 1;
    max-width: 50%;
    min-width: 0;
  }

  .nf-form-footer {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-top: 8px;
    padding-left: 212px;
  }
</style>
