<template>
  <div class="nf-list-page nf-shop-publish">
    <div class="nf-page-hero">
      <a href="#" class="nf-page-hero__back" @click.prevent="prev">
        <icon-svg name="jiantou" class="nf-page-hero__back-icon"></icon-svg>
        返回
      </a>
      <span class="nf-page-hero__badge">礼品管理</span>
      <h2 class="nf-page-hero__title">商品发布</h2>
      <p class="nf-page-hero__desc">填写礼品商品基本信息、图片、规格属性及详情内容，发布后可在礼品列表中管理</p>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">基本信息</div>
      <div class="nf-filter-form">
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">商品标题</span>
          <el-input class="nf-filter-form__input nf-filter-form__input--wide" placeholder="请输入商品标题" v-model="title" autosize></el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">商品分类</span>
          <el-select v-model="typeId" placeholder="请选择商品分类" class="nf-filter-form__input nf-filter-form__input--wide">
            <el-option v-for="item in typeDatas" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">商品原价(金币)</span>
          <el-input class="nf-filter-form__input nf-filter-form__input--wide" placeholder="请输入商品原价" type="number" :min="0" :controls="false"
            v-model="originalPrice" autosize></el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">商品价格(金币)</span>
          <el-input class="nf-filter-form__input nf-filter-form__input--wide" placeholder="请输入商品价格" type="number" :min="0" :controls="false"
            v-model="price" autosize></el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">商品销量</span>
          <el-input class="nf-filter-form__input nf-filter-form__input--wide" placeholder="请输入商品销量" type="number" :min="0" :controls="false"
            v-model="sales" autosize></el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">商品状态</span>
          <el-select v-model="status" placeholder="请选择商品状态" class="nf-filter-form__input nf-filter-form__input--wide">
            <el-option v-for="(item,index) in statusmain2" :key="item.index" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">必买理由</span>
          <el-input class="nf-filter-form__input nf-filter-form__input--wide" placeholder="请输入必买理由" v-model="buyReason" autosize></el-input>
        </div>
        <div class="nf-filter-form__item">
          <span class="nf-filter-form__label">是否发货</span>
          <el-select v-model="isExpress" placeholder="请选择商品是否发货" class="nf-filter-form__input nf-filter-form__input--wide">
            <el-option v-for="(item,index) in isExpressmain" :key="item.index" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">商品图片</div>
      <div class="nf-form-row nf-form-row--top">
        <span class="nf-form-row__label">商品封面图</span>
        <div class="nf-form-row__control">
          <div class="nf-upload-box nf-upload-box--cover">
            <el-upload class="avatar-uploader" v-model="coverImg"
              :action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
              :on-success="handleAvatarSuccess1">
              <img v-if="coverImg" :src="coverImg" class="nf-upload-preview nf-upload-preview--cover" />
              <i v-else class="el-icon-plus avatar-uploader-icon nf-upload-placeholder"></i>
            </el-upload>
          </div>
        </div>
      </div>
      <div class="nf-form-row nf-form-row--top nf-shop-publish__carousel-row">
        <span class="nf-form-row__label">商品轮播图</span>
        <div class="nf-form-row__control nf-shop-publish__carousel-list">
          <el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
            :on-success="handleUploadSuccess" :on-change="handleChange" :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">商品规格</div>
      <div class="nf-form-row">
        <span class="nf-form-row__label">规格类型</span>
        <div class="nf-form-row__control nf-shop-publish__spec-row">
          <el-radio-group v-model="guige" @change="guigeChange">
            <el-radio :label="0">单规格</el-radio>
            <el-radio :label="1">多规格</el-radio>
          </el-radio-group>
          <el-select v-if="guigeshow" v-model="ruleId" placeholder="请选择商品规格" class="nf-filter-form__input nf-filter-form__input--wide nf-shop-publish__spec-select" @change="selectTrigger(ruleId)">
            <el-option v-for="(item,index) in specifdata" :key="item.index" :label="item.ruleName" :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div v-if="ruleValue" class="nf-shop-publish__rule-block">
        <div class="ruleitem" v-for="(item,index) in ruleValue" :key="index">
          <div>{{item.value}} <span @click="deleterule(index)">
              <icon-svg name="shanchu" class="ruleicon"></icon-svg>
            </span></div>
          <div>
            <el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable :disable-transitions="false"
              @close="handleClose(tag,j,item)">
              {{tag}}
            </el-tag>
            <el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
              style="width:120px;" placeholder="请输入属性名" @keyup.enter.native="handleInputConfirm(index,item)">
            </el-input>
            <el-button class="button-new-tag" size="small" @click="handleInputConfirm(index,item)">添加</el-button>
          </div>
        </div>
        <div class="nf-shop-publish__rule-actions" v-if="ruleshow">
          <div v-if="isshow" class="btn_specif">
            <el-button type="primary" @click="btnToclick()">添加新规格</el-button>
            <el-button type="success" @click="create()">立即生成</el-button>
          </div>
        </div>
        <div v-if="!isshow" class="nf-shop-publish__new-spec">
          <div class="nf-filter-form__item">
            <span class="nf-filter-form__label">规格</span>
            <el-input class="nf-filter-form__input" v-model="releobject.value" placeholder="例:颜色"></el-input>
          </div>
          <div class="nf-filter-form__item">
            <span class="nf-filter-form__label">规格值</span>
            <el-input class="nf-filter-form__input" v-model="releobject.detail" placeholder="例:黑色"></el-input>
          </div>
          <div class="nf-filter-form__item">
            <el-button type="primary" @click="speciTo()">确 定</el-button>
            <el-button @click="speciTotwo()">取 消</el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="nf-table-card" v-if="propshow">
      <div class="nf-filter-card__head">商品属性</div>
      <div class="property_table nf-shop-publish__property">
        <table class="nf-shop-publish__attr-header">
          <tr>
            <th v-for="item in headerData" :key="item">{{item}}</th>
          </tr>
        </table>
        <el-table :data="valueData">
            <el-table-column v-if="detailJson.length == 1" prop="value0" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 2" prop="value0" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 2" prop="value1" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 3" prop="value0" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 3" prop="value1" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 3" prop="value2" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 4" prop="value0" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 4" prop="value1" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 4" prop="value2" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 4" prop="value3" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 5" prop="value0" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 5" prop="value1" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 5" prop="value2" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 5" prop="value3" width="120">
            </el-table-column>
            <el-table-column v-if="detailJson.length == 5" prop="value4" width="120">
            </el-table-column>
            <el-table-column prop="skuImg" width="100">
              <template slot-scope="scope">
                <div class="imgWrap nf-upload-box nf-upload-box--sku">
                  <el-upload style="width: 60px;height: 60px;" class="avatar-uploader" v-model="scope.row.skuImg"
                    :action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
                    :on-success="handleAvatarSuccess">
                    <img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="nf-upload-preview nf-upload-preview--sku"
                      @click="curRowIndex=scope.$index" />
                    <i v-else class="el-icon-plus avatar-uploader-icon" @click="curRowIndex=scope.$index"></i>
                  </el-upload>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuOriginalPrice" width="120">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuPrice" width="120">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuPrice" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="stock" width="120">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.stock" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="sales" width="120">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column width="120">
              <template slot-scope="scope">
                <el-button :disabled="!isAuth('userList:delete')" size="mini" type="danger"
                  @click="deleter(scope.$index)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
      </div>
    </div>

    <div class="nf-table-card" v-if="propshow1">
      <div class="nf-filter-card__head">商品属性</div>
      <div class="property_table nf-shop-publish__property">
        <table class="nf-shop-publish__attr-header">
          <tr>
            <th v-for="item in headerData2" :key="item">{{item}}</th>
          </tr>
        </table>
        <el-table :data="valueData2">
            <el-table-column prop="skuImg" width="150">
              <template slot-scope="scope">
                <div class="imgWrap nf-upload-box nf-upload-box--sku">
                  <el-upload style="width: 60px;height: 60px;" class="avatar-uploader" v-model="scope.row.skuImg"
                    :action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
                    :on-success="handleAvatarSuccess3">
                    <img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="nf-upload-preview nf-upload-preview--sku"
                      @click="curRowIndex=scope.$index" />
                    <i v-else class="el-icon-plus avatar-uploader-icon" @click="curRowIndex=scope.$index"></i>
                  </el-upload>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuOriginalPrice" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuPrice" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuPrice" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="stock" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.stock" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="sales" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
          </el-table>
      </div>
    </div>

    <div class="nf-filter-card">
      <div class="nf-filter-card__head">邮费设置</div>
      <div class="nf-form-row">
        <span class="nf-form-row__label">是否包邮</span>
        <div class="nf-form-row__control nf-shop-publish__postage-row">
          <el-radio-group v-model="positage" @change="agreeChange">
            <el-radio :label="0">是</el-radio>
            <el-radio :label="1">否</el-radio>
          </el-radio-group>
          <el-input v-if="priceshow" class="nf-filter-form__input nf-filter-form__input--wide nf-shop-publish__postage-input" placeholder="请输入邮费" type="number" min="0"
            v-model="postagePrice" autosize onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
        </div>
      </div>
    </div>

    <div class="nf-table-card nf-form-card">
      <div class="nf-filter-card__head">商品详情</div>
      <div class="tinymce-content nf-shop-publish__editor">
        <quill-editor ref="myTextEditor" v-model="descrition" :options="quillOption"
          class="nf-shop-publish__quill">
        </quill-editor>
      </div>
      <div class="nf-form-footer">
        <el-button @click="prev">返回上一页</el-button>
        <el-button type="primary" icon="document" @click="artiReleass">发布商品</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  
  import {
    quillEditor
  } from 'vue-quill-editor'
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'
  import quillConfig from '../locality/quill-config.js'
  export default {
    name: 'news',
    components: {
      quillEditor
    },
    data() {
      return {
        specif: '',
        typeId: '',
        title: '',
        merchants: '',
        originalPrice: '',
        price: '',
        memberPrice: '',
        positage: 1,
        guige: 1,
        skuImg: '',
        ruleId: '',
        postagePrice: 0,
        priceshow: true,
        isshow: true,
        commissionPrice: '',
        buyReason: '',
        ruleshow: false,
        propshow: false,
        guigeshow: true,
        propshow1: false,
        ruleValue: [],
        attr: [],
        attrs: [],
        headerData: [],
        valueData: [],
        detailJson: [],
        inputValues: {
          inputValue0: '',
          inputValue1: '',
          inputValue2: '',
          inputValue3: '',
          inputValue4: '',
          inputValue5: ''
        },
        sku: [],
        img: '',
        sales: '',
        status: '',
        photos: '',
        typeDatas: [],
        value: [],
        isSelect: 1,
        homeGoods: 1,
        isRecommend: 1,
        isExpress: '',
        coverImg: '',
        valueData2: [],
        headerData2: [],
        statusmain2: [{
            value: 1,
            label: '上架'
          },
          {
            value: 2,
            label: '下架'
          }
        ],
        isExpressmain: [{
            value: 1,
            label: '普通商品需要发货'
          },
          {
            value: 2,
            label: '虚拟商品无需发货'
          },
        ],
        attrName: '',
        ruleName: '',
        attrValue: [],
        releobject: {
          value: '',
          detail: ''
        },
        brandId: '',
        brandIds: [],
        specifdata: [],
        dialogVisible: false,
        isShow: false,
        hideUpload: false,
        limit: 1,
        id: '',
        hasChange: false,
        hasInit: false,
        tinymceId: 'tinymceId',
        height: 435,
        toolbar: [],
        menubar: 'file edit insert view format table',
        languageTypeList: {
          'en': 'en',
          'zh': 'zh_CN'
        },
        artiCletypes: [],
        descrition: '',
		quillOption: quillConfig,
      }
    },
    
    methods: {
      handleAvatarSuccess(file) {
        if (this.curRowIndex == 0) {
          this.skuImg = file.data;
          this.valueData[0].skuImg = this.skuImg
          console.log('this.valueData',this.valueData)
          return
        } else {
          this.skuImg = file.data;
          this.valueData[this.curRowIndex].skuImg = this.skuImg
        }
        console.log('this.valueData',this.valueData)
      },
      handleAvatarSuccess3(file) {
        this.skuImg = file.data;
        this.valueData2[0].skuImg = this.skuImg
        console.log('this.valueData2',this.valueData2)
      },
      agreeChange(val) {
        if (val == 1) {
          this.priceshow = true
          this.postagePrice = ''
        } else {
          this.priceshow = false
          this.postagePrice = 0
        }
      },
      guigeChange(val) {
        if (val == 1) {
          this.ruleId = ''
          this.guigeshow = true
          this.propshow1 = false
        } else {
          this.ruleId = 0
          this.guigeshow = false
          this.propshow1 = true
          this.propshow = false
          this.ruleshow = false
          this.attr = []
          this.ruleValue = []
          this.guigeclect()
        }
      },
      guigeclect() {
        if (this.originalPrice == '') {
          this.originalPrice = '0'
        }
        if (this.price == '') {
          this.price = '0'
        }
        if (this.memberPrice == '') {
          this.memberPrice = '0'
        }
        this.$http({
          url: this.$http.adornUrl('shopGoods/onlyFormatAttr'),
          method: 'get',
          params: this.$http.adornParams({
            'originalPrice': this.originalPrice,
            'price': this.price,
            'memberPrice': this.price,
            'coverImg': this.coverImg,
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.headerData2 = returnData.header
          this.valueData2 = returnData.value
          this.propshow = false
          this.propshow1 = true
          this.sku = returnData.value
        })
      },
      handleChange1(value) {
        this.value.forEach(element => {
          this.typeId = element;
        });
      },
      handleChange(file, fileList) {
        this.hideUpload = fileList.length >= this.limit;
      },
      handleRemove(file, fileList) {
        this.hideUpload = fileList.length >= this.limit;
      },
      handleAvatarSuccess1(file, fileList) {
        this.coverImg = file.data
      },
      handleUploadSuccess(file, fileList) {
        this.photos += file.data + ','
      },
      prev() {
        this.$router.back()
      },
      artiReleass() {
        let photost = this.photos;
        photost = photost.substr(0, photost.length - 1)
        if (this.title == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品标题',
            type: 'warning'
          });
          return
        }
        if (this.typeId == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择商品类型',
            type: 'warning'
          });
          return
        }
        if (this.originalPrice == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品原价',
            type: 'warning'
          });
          return
        }
        if (this.originalPrice < 1) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '商品原价不能小于1元',
            type: 'warning'
          });
          return
        }
        if (this.price == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品价格',
            type: 'warning'
          });
          return
        }
        if (this.price < 1) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '商品价格不能小于1元',
            type: 'warning'
          });
          return
        }
        if (this.status == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择商品状态',
            type: 'warning'
          });
          return
        }
        if (this.sales == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品销量',
            type: 'warning'
          });
          return
        }
        if (this.buyReason == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入必买理由',
            type: 'warning'
          });
          return
        }
        if (this.isExpress == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择商品是否发货',
            type: 'warning'
          });
          return
        }
        if (this.coverImg == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择商品封面图',
            type: 'warning'
          });
          return
        }
        if (this.descrition == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入商品详情',
            type: 'warning'
          });
          return
        }
        if (this.sku.length == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择商品规格',
            type: 'warning'
          });
          return
        }
        for(var i  in this.valueData){
          if(this.valueData[i].skuImg==''){
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '请上传商品属性图片',
              type: 'warning'
            });
            return
          }
        }
        if (this.postagePrice === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入邮费',
            type: 'warning'
          });
          return
        }
        this.$http({
          url: this.$http.adornUrl('shopGoods/save'),
          method: 'post',
          data: this.$http.adornData({
            'img': photost,
            'title': this.title,
            'typeId': this.typeId,
            'merchants': this.merchants,
            'originalPrice': this.originalPrice,
            'price': this.price,
            'memberPrice': this.price,
            'sales': this.sales,
            'status': this.status,
            'coverImg': this.coverImg,
            'descrition': this.descrition,
            'commissionPrice': this.commissionPrice,
            'isSelect': this.isSelect,
            'homeGoods': this.homeGoods,
            'buyReason': this.buyReason,
            'isRecommend': this.isRecommend,
            'isExpress': this.isExpress,
            'postagePrice': this.postagePrice,
            'sku': this.sku,
            'attr': this.attr,
            'brandId': this.brandId
          })
        }).then(({
          data
        }) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.title = ''
              this.type = ''
              this.merchants = ''
              this.price = ''
              this.memberPrice = ''
              this.originalPrice = ''
              this.sales = ''
              this.descrition = ''
              this.postagePrice = ''
              this.status = ''
              this.buyReason = ''
              this.brandId = ''
              this.$router.push({
                path: '/shopAdminGift'
              })
            }
          })
        })
      },
      
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('goodsType/result'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.typeDatas = returnData
        })
      },
      specifSelect() {
        this.$http({
          url: this.$http.adornUrl('shopGoods/info'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.specifdata = returnData;
        })
      },
      deleter(index) {
        let length = this.sku.length
        if (length == 1) {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '属性至少要有一个',
            type: 'warning'
          });
          return
        } else {
          this.sku.splice(index, 1);
        }
      },
      selectTrigger(val) {
        this.$http({
          url: this.$http.adornUrl('shopSelfGoodsRule/find'),
          method: 'get',
          params: this.$http.adornParams({
            'id': val
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.ruleValue = returnData.ruleValue
          this.attrName = returnData.ruleName
          this.ruleshow = true
          this.ruleId = returnData.id
          this.create()
        })
      },
      deleterule(index) {
        this.ruleValue.splice(index, 1);
      },
      btnToclick() {
        this.isshow = false
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      speciTotwo() {
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      speciTo() {
        if (this.releobject.value == '') {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '请输入规格',
            type: 'warning'
          });
          return
        }
        if (this.releobject.detail == '') {
          this.$message({
            title: '提示',
            duration: 1800,
            type: 'error',
            message: '请输入规格值',
            type: 'warning'
          });
          return
        }
        this.ruleValue.push({
          value: this.releobject.value,
          detail: this.releobject.detail
        });
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      handleClose(tag, j, item) {
        console.log('删除·········',tag, j, item)
        var detailarr = item.detail.split(',')
        detailarr.splice(j, 1);
        if (detailarr.length < 1) {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '规格值至少要有一个',
            type: 'warning'
          });
          return
        } else {
          item.detail = String(detailarr)
        }
      },
      handleInputConfirm(index, item) {
        let inputValue = this.inputValues[index];
        if (inputValue) {
          var detailarr = item.detail.split(',')
          detailarr.push(inputValue)
          for (var i = 0; i < detailarr.length; i++) {
            for (var j = 0; j < detailarr.length; j++) {
              if (detailarr[i] == detailarr[j] && i != j) {
                detailarr.splice(j, 1);
              }
            }
          }
          item.detail = String(detailarr)
          this.inputValues[index] = '';
        }
        this.inputVisible = false;
      },
      brandSelect() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('selfGoodsBrand/result'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.brandIds = returnData
        })
      },
      create() {
        this.attr = []
        this.attr.push({
          attrName: this.attrName,
          attrValue: this.ruleValue,
          ruleId: this.ruleId
        })
        if (this.originalPrice == '') {
          this.originalPrice = '0'
        }
        if (this.price == '') {
          this.price = '0'
        }
        if (this.memberPrice == '') {
          this.memberPrice = '0'
        }
        this.$http({
          url: this.$http.adornUrl(
            `shopGoods/isFormatAttr?coverImg=${this.coverImg}&originalPrice=${this.originalPrice}&price=${this.price}&memberPrice=${this.price}`
            ),
          method: 'post',
          data: this.$http.adornData(this.attr[0])
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.headerData = returnData.header
          this.valueData = returnData.value
          this.propshow = true
          this.propshow1 = false
          this.sku = returnData.value
          this.detailJson = returnData.value[0].detailJson.split(',')
        })
      }
    },
    mounted() {
      this.dataSelect()
      this.specifSelect()
    },
    
  }
</script>

<style scoped="scoped">
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

  .nf-form-row {
    display: flex;
    align-items: center;
  }

  .nf-form-row--top {
    align-items: flex-start;
  }

  .nf-form-row__label {
    flex-shrink: 0;
    width: 88px;
    padding-right: 12px;
    font-size: 13px;
    color: var(--nf-text-regular, #303133);
    line-height: 32px;
  }

  .nf-form-row__control {
    flex: 1;
    min-width: 0;
  }

  .nf-form-card {
    margin-top: 0;
    margin-bottom: 0;
    padding: 16px;
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

  .nf-upload-box {
    background-color: #fbfdff;
    border: 1px dashed #c0ccda;
    border-radius: 6px;
    text-align: center;
  }

  .nf-upload-box--cover {
    width: 148px;
    height: 148px;
    line-height: 148px;
  }

  .nf-upload-box--sku {
    width: 60px;
    height: 60px;
    line-height: 60px;
    margin: 0 auto;
  }

  .nf-upload-preview {
    object-fit: cover;
    border-radius: 6px;
  }

  .nf-upload-preview--cover {
    width: 140px;
    height: 140px;
  }

  .nf-upload-preview--sku {
    width: 50px;
    height: 50px;
  }

  .nf-upload-placeholder {
    font-size: 28px;
    color: #8c939d;
  }

  .nf-shop-publish__carousel-row {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid var(--nf-border, #e8eaed);
  }

  .nf-shop-publish__carousel-list {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    gap: 8px;
  }

  .nf-shop-publish__spec-row {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
  }

  .nf-shop-publish__spec-select {
    margin-left: 0;
  }

  .nf-shop-publish__rule-block {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid var(--nf-border, #e8eaed);
  }

  .nf-shop-publish__rule-actions {
    margin-top: 12px;
  }

  .nf-shop-publish__new-spec {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
    margin-top: 12px;
  }

  .nf-shop-publish__property {
    margin-top: 12px;
    overflow-x: auto;
  }

  .nf-shop-publish__attr-header {
    width: 100%;
    margin-bottom: 8px;
  }

  .nf-shop-publish__attr-header tr {
    display: flex;
    text-align: left;
  }

  .nf-shop-publish__attr-header th {
    width: 120px;
    font-size: 13px;
    font-weight: 600;
    color: var(--nf-text-primary, #1a2433);
    padding: 8px 0;
  }

  .nf-shop-publish__postage-row {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
  }

  .nf-shop-publish__postage-input {
    margin-left: 0;
  }

  .nf-shop-publish__editor {
    margin-top: 12px;
    position: relative;
    line-height: normal;
  }

  .nf-shop-publish__quill {
    padding-bottom: 50px;
    height: 300px;
    width: 100%;
    max-width: 100%;
    display: inline-table;
    margin-bottom: 16px;
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

  .hide >>> .el-upload--picture-card {
    display: none;
  }

  .ruleitem {
    padding-left: 10px;
    margin-top: 15px;
  }

  .ruleitem .ruleicon {
    position: relative;
    top: 3px;
    left: 2px;
    width: 1.2em;
    height: 1.2em;
  }

  .el-tag--medium {
    margin-right: 10px;
  }

  .el-tag+.el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    height: 32px;
    line-height: 31px;
    padding-top: 0;
    padding-bottom: 0;
    border-radius: 4px;
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    background-color: #f5f7fa;
    position: relative;
    left: -5px;
    border-color: #dcdfe6;
    border: 1px solid #dcdfe6;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

  .el-input--small .el-input__inner {
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
  }

  .el-table--enable-row-transition .el-table__body td {
    text-align: center;
  }

  .imgWrap >>> .avatar-uploader .el-upload {
    width: 60px;
  }

  .nf-table-card >>> .el-table .cell {
    text-align: center !important;
  }
</style>
