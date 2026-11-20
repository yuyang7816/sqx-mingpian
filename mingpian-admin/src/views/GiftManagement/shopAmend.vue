<template>
  <div class="nf-list-page nf-shop-amend">
    <div class="nf-page-hero">
      <a href="#" class="nf-page-hero__back" @click.prevent="prev">
        <icon-svg name="jiantou" class="nf-page-hero__back-icon"></icon-svg>
        返回
      </a>
      <span class="nf-page-hero__badge">礼品管理</span>
      <h2 class="nf-page-hero__title">商品修改</h2>
      <p class="nf-page-hero__desc">编辑礼品商品基本信息、图片、规格属性及详情内容，保存后返回礼品列表</p>
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
          <el-select v-model="status" placeholder="请选择商品类型" class="nf-filter-form__input nf-filter-form__input--wide">
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
      <div class="nf-form-row nf-form-row--top nf-shop-amend__carousel-row">
        <span class="nf-form-row__label">商品轮播图</span>
        <div class="nf-form-row__control nf-shop-amend__carousel-list">
          <div v-for="(item,index) of imgs" :key="index" class="nf-carousel-item">
            <img :src="item" class="nf-upload-preview nf-upload-preview--carousel" />
            <div @click="clear(index)" class="divhove">
              <i class="el-icon-delete nf-carousel-item__delete"></i>
            </div>
          </div>
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
        <div class="nf-form-row__control nf-shop-amend__spec-row">
          <el-radio-group v-model="guige" @change="guigeChange">
            <el-radio :label="0">单规格</el-radio>
            <el-radio :label="1">多规格</el-radio>
          </el-radio-group>
          <el-select v-if="guigeshow" v-model="ruleId" placeholder="请选择商品规格" class="nf-filter-form__input nf-filter-form__input--wide nf-shop-amend__spec-select" @change="selectTrigger(ruleId)">
            <el-option v-for="(item,index) in specifdata" :key="item.index" :label="item.ruleName" :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div v-if="ruleValue" class="nf-shop-amend__rule-block">
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
        <div class="nf-shop-amend__rule-actions" v-if="ruleshow">
          <div v-if="isshow" class="btn_specif">
            <el-button type="primary" @click="btnToclick()">添加新规格</el-button>
            <el-button type="success" @click="create()">立即生成</el-button>
          </div>
        </div>
        <div v-if="!isshow" class="nf-shop-amend__new-spec">
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

    <div class="nf-table-card" v-if="valueshow">
      <div class="nf-filter-card__head">商品属性</div>
      <div class="property_table nf-shop-amend__property">
        <table class="nf-shop-amend__attr-header">
          <tr>
            <th v-for="item in headerData" :key="item">{{item}}</th>
          </tr>
        </table>
        <el-table :data="valueData">
            <el-table-column v-if="detailJson.length == 1 || detailJson.length == 0" prop="value0" width="120">
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
            <el-table-column width="100">
              <template slot-scope="scope">
                <div class="imgWrap nf-upload-box nf-upload-box--sku">
                  <el-upload class="avatar-uploader" v-model="scope.row.skuImg"
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
            <!-- <el-table-column prop="memberPrice" width="120">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.memberPrice" style="width:80px;font-size:14px;">
                  </el-input>
                </div>
              </template>
            </el-table-column> -->
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

    <div class="nf-table-card" v-if="valueshow1">
      <div class="nf-filter-card__head">商品属性</div>
      <div class="property_table nf-shop-amend__property">
        <table class="nf-shop-amend__attr-header">
          <tr>
            <th v-for="item in headerData2" :key="item">{{item}}</th>
          </tr>
        </table>
        <el-table :data="sku">
            <el-table-column width="130">
              <template slot-scope="scope">
                <div class="imgWrap nf-upload-box nf-upload-box--sku">
                  <el-upload class="avatar-uploader" v-model="scope.row.skuImg"
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
                  <el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuPrice" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.skuPrice" style="width:80px;font-size:14px;" onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')" min="0">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <!-- <el-table-column prop="memberPrice" width="150">
              <template slot-scope="scope">
                <div>
                  <el-input type="text" v-model="scope.row.memberPrice" style="width:80px;font-size:14px;">
                  </el-input>
                </div>
              </template>
            </el-table-column> -->
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
                  <el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;"  min="0">
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
        <div class="nf-form-row__control nf-shop-amend__postage-row">
          <el-radio-group v-model="positage" @change="agreeChange">
            <el-radio :label="0">是</el-radio>
            <el-radio :label="1">否</el-radio>
          </el-radio-group>
          <el-input v-if="priceshow" class="nf-filter-form__input nf-filter-form__input--wide nf-shop-amend__postage-input" placeholder="请输入邮费" type="number" min="0"
            v-model="postagePrice" autosize onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
        </div>
      </div>
    </div>

    <div class="nf-table-card nf-form-card">
      <div class="nf-filter-card__head">商品详情</div>
      <div class="tinymce-content nf-shop-amend__editor">
        <quill-editor ref="myTextEditor" v-model="descrition" :options="quillOption"
          class="nf-shop-amend__quill">
        </quill-editor>
      </div>
      <div class="nf-form-footer">
        <el-button @click="prev">返回上一页</el-button>
        <el-button type="primary" icon="document" @click="artiReleass">保存</el-button>
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
        type: '',
        typeId: '',
        title: '',
        merchants: '',
        originalPrice: '',
        price: '',
        memberPrice: '',
        coverImg: '',
        guige: '',
        commissionPrice: '',
        postagePrice: 0,
        img: '',
        buyReason: '',
        sales: '',
        status: '',
        typeDatas: [],
        detailJson: [],
        imgs: [],
        value: [],
        potost: '',
        attr: [],
        brandId: '',
        brandIds: [],
        statusmain2: [{
            value: 1,
            label: '上架'
          },
          {
            value: 2,
            label: '下架'
          }
        ],
        isExpress: '',
        isExpressmain: [{
            value: 1,
            label: '普通商品需要发货'
          },
          {
            value: 2,
            label: '虚拟商品无需发货'
          },
          // {
          //   value: 3,
          //   label: '门店核销无需发货'
          // }
        ],
        positage: 0,
        valueshow: true,
        valueshow1: true,
        priceshow: false,
        specifdata: [],
        ruleValue: [],
        headerData: [],
        valueData: [],
        sku: [],
        releobject: {
          value: '',
          detail: ''
        },
        isshow: true,
        ruleshow: false,
        inputValues: {
          inputValue0: '',
          inputValue1: '',
          inputValue2: '',
          inputValue3: '',
          inputValue4: '',
          inputValue5: ''
        },
        ruleId: '',
        specif: '',
        propshow: false,
        guigeshow: true,
        attrName: '',
        headerData2: [],
        attrValue: [],
        skuImg: '',
        isSelect: '',
        homeGoods: '',
        isRecommend: '',
        isShow: false,
        hideUpload: false,
        limit: 1,
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
        // 富文本内容
        descrition: '',
        curRowIndex: null,
				quillOption: quillConfig,
      }
    },
    methods: {
      // 返回上一级
      prev() {
        this.$router.back()
      },
      // 图标上传
      handleAvatarSuccess(file) {
        if (this.curRowIndex == 0) {
          this.skuImg = file.data;
          this.valueData[0].skuImg = this.skuImg
          return
        } else {
          this.skuImg = file.data;
          this.valueData[this.curRowIndex].skuImg = this.skuImg
        }
      },
      // 图标上传
      handleAvatarSuccess3(file) {
        this.skuImg = file.data;
        this.sku[0].skuImg = this.skuImg
      },
      // 是否包邮
      agreeChange(val) {
        if (val == 1) {
          this.priceshow = true
          this.postagePrice = ''
        } else {
          this.priceshow = false
          this.postagePrice = 0
        }
      },
      // 是否多规格
      guigeChange(val) {
        if (val == 0) {
          this.ruleId = 0
          this.valueshow1 = true
          this.valueshow = false
          this.guigeshow = false
          this.attr = []
          this.ruleshow = false
          this.ruleValue = []
          this.dansku()
        }
        if (val == 1) {
          this.ruleId = ''
          this.sku = []
          this.guigeshow = true
          this.valueshow1 = false
        }
      },
      // 获取单规格的sku
      dansku() {
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
          this.sku = returnData.value
          this.headerData2 = returnData.header
          this.valueshow = false
          this.propshow = true
          this.guigeshow = false
        })
      },
      // 回显规格
      echo(goodsId) {
        this.$http({
          url: this.$http.adornUrl('shopGoods/formatAttr'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsId': goodsId,
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.sku = returnData.value
          this.headerData2 = returnData.header
          this.valueshow = false
          this.propshow = true
          this.guigeshow = false
        })
      },
      handleChange1(value) {
        this.value.forEach(element => {
          this.typeId = element;
        });
      },
      // 删除
      clear(index) {
        this.imgs.splice(index, 1);
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
      //上传成功
      handleUploadSuccess(file, fileList) {
        this.potost += file.data + ','
      },
      init() {
        
        let id = this.$route.query.id
        this.$http({
          url: this.$http.adornUrl('shopGoods/find'),
          method: 'get',
          params: this.$http.adornParams({
            'id': id
          })
        }).then(({
          data
        }) => {
          console.log('data.data',data.data)
          let returnData = data.data;
          let imgs = returnData.img.split(',')
          this.title = returnData.title
          this.id = returnData.id;
          this.imgs = imgs;
          this.buyReason = returnData.buyReason
          this.coverImg = returnData.coverImg
          this.title = returnData.title;
          this.commissionPrice = returnData.commissionPrice,
            this.originalPrice = returnData.originalPrice;
          this.typeId = Number(returnData.typeId);
          this.merchants = returnData.merchants;
          this.price = returnData.price;
          this.sales = returnData.sales;
          this.memberPrice = returnData.price;
          this.descrition = returnData.descrition;
          this.status = returnData.status;
          this.createAt = returnData.createAt;
          this.isSelect = returnData.isSelect;
          this.homeGoods = returnData.homeGoods;
          this.isRecommend = returnData.isRecommend;
          this.isExpress = returnData.isExpress;
          this.createAt = returnData.createAt;
          this.brandId = returnData.brandId;
          let label = JSON.parse(returnData.type.parentId)
          let value = JSON.parse(returnData.type.id)
          this.value = [label, value]
          // this.postagePrice = returnData.postagePrice
					if(returnData.postagePrice){
						if(returnData.postagePrice==''){
							this.postagePrice = 0
						}else{
							this.postagePrice = returnData.postagePrice
						}
						
					}else{
						this.postagePrice = 0
					}

          if (this.postagePrice > 0) {
            this.positage = 1
            this.priceshow = true
          } else {
            this.positage = 0
            this.priceshow = false
          }
          if(returnData.attr){
             this.attr = returnData.attr
          }

          if (this.attr.length > 0) {
            this.guige = 1
            this.Triggerselect()
            this.create2()
            this.attrName = returnData.attr[0].attrName
            this.attrValue = returnData.attr[0].attrValue
            this.ruleId = returnData.attr[0].ruleId
            this.goodsId = returnData.attr[0].goodsId
            this.id = returnData.attr[0].id
          } else {
            this.attrValue= returnData.attr.attrValue
            this.guige = 0
            this.echo(this.id)
            this.ruleId = 0
            this.sku = returnData.sku
          }
        })
      },
      // 修改商品
      artiReleass() {
        console.log(this.attr)
        let id = this.$route.query.id
        if (this.imgs == '') {
          this.img = this.potost
          this.img = this.img.substr(0, this.img.length - 1)
        } else {
          this.img = this.potost + this.imgs
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
        if (this.postagePrice === '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入邮费',
            type: 'warning'
          });
          return
        }
		// if (this.commissionPrice === '') {
		//   this.$notify({
		//     title: '提示',
		//     duration: 1800,
		//     message: '请输入商品佣金',
		//     type: 'warning'
		//   });
		//   return
		// }
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

        this.$http({
          url: this.$http.adornUrl('shopGoods/update'),
          method: 'post',
          data: this.$http.adornData({
            'id': id,
            'img': this.img,
            'title': this.title,
            'typeId': this.typeId,
            'merchants': this.merchants,
            'originalPrice': this.originalPrice,
            'price': this.price,
            'memberPrice': this.price,
            'sales': this.sales,
            'brandId': this.brandId,
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
            'createAt': this.createAt
          })
        }).then(({
          data
        }) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.$router.push({
                path: '/shopAdminGift'
              })
            }
          })
        })
      },
      // 商品规格
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
      // 商品规格
      Triggerselect() {
        let id = this.$route.query.id
        this.$http({
          url: this.$http.adornUrl('shopGoods/findAttrValue'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsId': id
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.ruleValue = returnData.attrValue
          this.attrName = returnData.attrName
          this.ruleshow = true
        })
      },
      // 删除商品属性
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
      // 查询商品规格
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
      // 初始化立即生成
      create2() {
        let id = this.$route.query.id
        this.$http({
          url: this.$http.adornUrl('shopGoods/formatAttr'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsId': id
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.headerData = returnData.header
          this.valueData = returnData.value
          this.valueshow = true
          this.valueshow1 = false
          this.sku = returnData.value
          this.detailJson = returnData.value[0].detailJson.split(',')
        })
      },
      // 立即生成
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
          this.valueshow = true
          this.valueshow1 = false
          this.sku = returnData.value
          this.detailJson = returnData.value[0].detailJson.split(',')
        })
      },
      // 获取品牌
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
      // 商品分类
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
	  dataSelect1(){
		  this.init()
		  this.dataSelect()
		  this.specifSelect()
	  },
    },
    mounted() {
      this.init()
      this.dataSelect()
      this.specifSelect()
      // this.brandSelect()
    },
	watch: {
	  '$route': 'dataSelect1'
	}
    
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

  .nf-upload-preview--carousel {
    width: 148px;
    height: 148px;
  }

  .nf-upload-preview--sku {
    width: 50px;
    height: 50px;
  }

  .nf-upload-placeholder {
    font-size: 28px;
    color: #8c939d;
  }

  .nf-shop-amend__carousel-row {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid var(--nf-border, #e8eaed);
  }

  .nf-shop-amend__carousel-list {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    gap: 8px;
  }

  .nf-carousel-item {
    position: relative;
  }

  .nf-carousel-item__delete {
    margin: 0;
    font-size: 18px;
    color: #fff;
  }

  .nf-shop-amend__spec-row {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
  }

  .nf-shop-amend__spec-select {
    margin-left: 0;
  }

  .nf-shop-amend__rule-block {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid var(--nf-border, #e8eaed);
  }

  .nf-shop-amend__rule-actions {
    margin-top: 12px;
  }

  .nf-shop-amend__new-spec {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
    margin-top: 12px;
  }

  .nf-shop-amend__property {
    margin-top: 12px;
    overflow-x: auto;
  }

  .nf-shop-amend__attr-header {
    width: 100%;
    margin-bottom: 8px;
  }

  .nf-shop-amend__attr-header tr {
    display: flex;
    text-align: left;
  }

  .nf-shop-amend__attr-header th {
    width: 120px;
    font-size: 13px;
    font-weight: 600;
    color: var(--nf-text-primary, #1a2433);
    padding: 8px 0;
  }

  .nf-shop-amend__postage-row {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
  }

  .nf-shop-amend__postage-input {
    margin-left: 0;
  }

  .nf-shop-amend__editor {
    margin-top: 12px;
    position: relative;
    line-height: normal;
  }

  .nf-shop-amend__quill {
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

  .divhove {
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    opacity: 0;
    cursor: default;
    text-align: center;
    padding-top: 50%;
    border-radius: 6px;
    background-color: rgba(0, 0, 0, .5);
    -webkit-transition: opacity .3s;
    transition: opacity .3s;
  }

  .divhove:hover {
    opacity: 0.8;
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
