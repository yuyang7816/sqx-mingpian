<template>
  <div class="nf-list-page nf-mission-redact">
    <div class="nf-page-hero">
      <a href="#" class="nf-page-hero__back" @click.prevent="prev">
        <icon-svg name="jiantou" class="nf-page-hero__back-icon"></icon-svg>
        返回
      </a>
      <span class="nf-page-hero__badge">活动任务</span>
      <h2 class="nf-page-hero__title">修改任务</h2>
      <p class="nf-page-hero__desc">编辑任务标题、分类、幸运值与标题图片，并在下方富文本编辑器中修改任务内容</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">基本信息</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">任务标题</span>
          <el-input
            class="nf-filter-form__input nf-filter-form__input--wide"
            v-model="title"
            autosize
          ></el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">任务分类</span>
          <el-select
            class="nf-filter-form__input nf-filter-form__input--wide"
            v-model="classifyId"
            placeholder="请选择任务分类"
          >
            <el-option
              v-for="(item, index) in artiCletypes"
              :key="item.index"
              :label="item.classifyName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">幸运值</span>
          <el-input
            class="nf-filter-form__input"
            type="number"
            v-model="luckyValue"
            autosize
          ></el-input>
        </div>
      </div>
      <div class="nf-form-row nf-form-row--top nf-mission-redact__upload-row">
        <span class="nf-form-row__label">标题图片</span>
        <div class="nf-form-row__control nf-form-row__control--upload">
          <el-upload
            v-model="titlePicture"
            :class="{ hide: hideUpload }"
            action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
            :limit="1"
            :on-success="handleUploadSuccess"
            list-type="picture-card"
            :on-remove="handleRemove"
            :on-change="handleChange"
          >
            <img
              v-if="titlePicture"
              :src="titlePicture"
              class="nf-upload-preview"
            />
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </div>
    </div>

    <div class="nf-table-card nf-form-card">
      <div class="nf-filter-card__head">任务内容</div>
      <div class="tinymce-content nf-mission-redact__editor">
        <textarea id="tinymceId" :value="content" class="tinymce-textarea" />
      </div>
      <div class="nf-form-footer">
        <el-button @click="prev">取 消</el-button>
        <el-button type="primary" icon="document" @click="artiReleass">修改任务</el-button>
      </div>
    </div>
  </div>
</template>

<script>
    import load from './tinymce-components/dynamicLoadScript'
    import toolbar from './tinymce-components/toolbar'
    import plugins from './tinymce-components/plugins'

    const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

    export default {
        name: 'Tinymce',
        data() {
            return {
                classifyId: '',
                title: '',
                luckyValue: '',
				titlePicture:'',
				isShow: false,
				hideUpload: false,
				limit:1,
				userId:'',
				id:'',
                hasChange: false,
                hasInit: false,
                tinymceId: 'tinymceId',
                height: 235,
                toolbar: [],
                menubar: 'file edit insert view format table',
                languageTypeList: {
                    'en': 'en',
                    'zh': 'zh_CN'
                },
				artiCletypes: [],
                // 富文本内容
                content: ''
            }
        },
        watch: {
            content(val) {
                if (!this.hasChange && this.hasInit) {
                    this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ''))
                }
            }
        },
        methods: {
            prev() {
                this.$router.push({ path: '/missionsye' })
            },
			handleChange(file, fileList){
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			//上传成功
			handleUploadSuccess(file,fileList) {
			  this.titlePicture= file.data
			},
			// 修改任务
			artiReleass(){
				if (this.title == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入任务标题',
				        type: 'warning'
				    });
				    return
				}
				if (this.classifyId == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务分类',
				        type: 'warning'
				    });
				    return
				}
				if (this.luckyValue == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务幸运值',
				        type: 'warning'
				    });
				    return
				}
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务内容',
				        type: 'warning'
				    });
				    return
				}
				let id = this.$route.query.id
				this.$http({
				  url: this.$http.adornUrl('helpTaskPlatform/updateHelpTaskPlatform'),
				  method: 'post',
				  data: this.$http.adornData({
					 'id':id,
				    'title':this.title,
					'state':this.state,
				    'classifyId':this.classifyId,
				    'content':this.content,
				    'luckyValue':this.luckyValue,
				    'titlePicture':this.titlePicture,
					'createTime' : this.createTime
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '修改成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.$router.push({path: '/missionsye'});
				      }
				    })
				})
			},
            init() {
                load(tinymceCDN, (err) => {
                    if (err) {
                        this.$message.error(err.message)
                        return
                    }
                    this.initTinymce()
                })
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTaskPlatform/selectHelpTaskPlatformById'),
					method: 'get',
					params: this.$http.adornParams({
						'id':id
					})
				}).then(({data}) => {
					let returnData = data.data;
					this.title = returnData.title
					this.classifyId = returnData.classifyId
					this.title = returnData.title
					this.state = returnData.state
					this.content = returnData.content
					this.titlePicture = returnData.titlePicture
					this.luckyValue = returnData.luckyValue
					this.createTime = returnData.createTime
                    if (this.titlePicture) {
                        this.hideUpload = true
                    }
				})
            },

            initTinymce() {
                const _this = this
                window.tinymce.init({
                    selector: `#${this.tinymceId}`,
                    images_upload_handler: function (blobInfo, success, failure) {
                        var xhr, formData;

                        xhr = new XMLHttpRequest();
                        xhr.withCredentials = false;
                        xhr.open('POST', "https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload");
                        xhr.onload = function () {
                            var json;

                            if (xhr.status != 200) {
                                failure('HTTP Error: ' + xhr.status);
                                return;
                            }
                            json = xhr.responseText;
                            if (json == "" || json == null) {
                                failure('Invalid JSON: ' + xhr.responseText);
                                return;
                            }
                            success(json);
                        };
                        formData = new FormData();
                        formData.append('file', blobInfo.blob(), blobInfo.filename());
                        xhr.send(formData);
                    },
                    language: this.languageTypeList['zh'],
                    height: this.height,
                    body_class: 'panel-body ',
                    object_resizing: false,
                    toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
                    plugins,
                    menubar: this.menubar,
                    end_container_on_empty_block: true,
                    powerpaste_word_import: 'clean',
                    code_dialog_height: 450,
                    code_dialog_width: 1000,
                    advlist_bullet_styles: 'square',
                    advlist_number_styles: 'default',
                    imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
                    default_link_target: '_blank',
                    link_title: false,
                    // Image
                    imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
                    nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
                    init_instance_callback: editor => {
                        if (_this.content) {
                            editor.setContent(_this.content)
                        }
                        _this.hasInit = true
                        editor.on('NodeChange Change KeyUp SetContent', () => {
                            this.hasChange = true
                            this.content = editor.getContent()
                        })
                    },
                    setup(editor) {
                        editor.on('FullscreenStateChanged', (e) => {
                            _this.fullscreen = e.state
                        })
                    }
                })
            },
            destroyTinymce() {
                const tinymce = window.tinymce.get(this.tinymceId)
                if (this.fullscreen) {
                    tinymce.execCommand('mceFullScreen')
                }
                if (tinymce) {
                    tinymce.destroy()
                }
            },
			// 获取分类
			classifySelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('platformClassify/selectClassifyList'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.artiCletypes = returnData
			  })
			},
        },
        mounted() {
            this.init()
			this.classifySelect()
        },
        beforeDestroy() {
            this.destroyTinymce()
        },
        activated() {
            if (window.tinymce) {
                this.initTinymce()
            }
        },
        deactivated() {
            this.destroyTinymce()
        }
    }
</script>

<style scoped>
  .nf-list-page {
    padding: 0;
  }

  .nf-page-hero__back {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    margin-bottom: 10px;
    font-size: 13px;
    color: var(--nf-brand, #2f3968);
    text-decoration: none;
  }

  .nf-page-hero__back:hover {
    color: var(--nf-brand-deep, #1f2645);
  }

  .nf-page-hero__back-icon {
    width: 1.2em;
    height: 1.2em;
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
    width: 150px;
  }

  .nf-filter-form__input--wide {
    width: 200px;
  }

  .nf-mission-redact__upload-row {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid var(--nf-border, #e8eaed);
  }

  .nf-form-row {
    display: flex;
    align-items: center;
  }

  .nf-form-row--top {
    align-items: flex-start;
  }

  .nf-form-row__label {
    flex-shrink: 0;
    width: 72px;
    padding-right: 12px;
    font-size: 13px;
    color: var(--nf-text-regular, #303133);
    line-height: 32px;
  }

  .nf-form-row__control--upload {
    flex: 1;
    min-width: 0;
  }

  .nf-upload-preview {
    width: 100%;
    height: 100%;
    border-radius: 6px;
    object-fit: cover;
  }

  .nf-form-card {
    margin-top: 0;
    margin-bottom: 0;
    padding: 16px;
  }

  .nf-mission-redact__editor {
    margin-top: 12px;
    position: relative;
    line-height: normal;
  }

  .nf-mission-redact__editor >>> .mce-fullscreen {
    z-index: 10000;
  }

  .tinymce-textarea {
    visibility: hidden;
    z-index: -1;
  }

  .nf-form-footer {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 12px;
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid var(--nf-border, #e8eaed);
  }

  .hide >>> .el-upload--picture-card {
    display: none;
  }
</style>
