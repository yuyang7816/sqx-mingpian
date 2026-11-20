<template>
  <div
    class="site-wrapper site-wrapper--nf"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="拼命加载中">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div
        class="site-content__wrapper"
        :style="{ minHeight: documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // 刷新
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      }
    },
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
      // 重置窗口可视高度
      resetDocumentClientHeight () {
        this.documentClientHeight = document.documentElement['clientHeight']
        window.onresize = () => {
          this.documentClientHeight = document.documentElement['clientHeight']
        }
      },
      // 获取当前管理员信息
      getUserInfo () {
        this.$http({
          url: this.$http.adornUrl('/sys/user/info'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.loading = false
            this.userId = data.user.userId
            this.userName = data.user.username
            this.$cookie.set('sysUserId', data.user.userId)
            this.$cookie.set('sysUserName', data.user.username)
          }
        })
      }
    }
  }
</script>

<style lang="scss">
.site-wrapper.site-wrapper--nf {
  position: relative;
  min-width: 1180px;
  overflow-x: hidden;
  background: var(--nf-page-bg, #f4f6fa);
}

.site-wrapper--nf .site-content__wrapper {
  overflow-x: hidden;
  box-sizing: border-box;
  transition: margin-left 0.3s;
}

.site-wrapper--nf .el-loading-mask {
  background-color: rgba(244, 246, 250, 0.88);
}

.site-wrapper--nf .el-loading-spinner .path {
  stroke: var(--nf-brand, #2f3968);
}

.site-wrapper--nf .el-loading-text {
  color: var(--nf-text-regular, #303133);
  font-size: 14px;
}
</style>
