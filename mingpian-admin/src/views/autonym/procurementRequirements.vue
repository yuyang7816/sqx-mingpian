<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">采购管理</span>
			<h2 class="nf-page-hero__title">服务列表</h2>
			<p class="nf-page-hero__desc">管理服务发布与审核，支持按状态、排序、分类、用户昵称、手机号、标题及城市筛选查询</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="status" class="nf-filter-form__input" @change="select()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">排序</span>
					<el-select v-model="screen" class="nf-filter-form__input" @change="select()">
						<el-option v-for="(item,index) in statesnum2" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">分类</span>
					<el-select v-model="classIdT" class="nf-filter-form__input" @change="select()">
						<el-option v-for="(item,index) in classList" :key="index" :label="item.className"
							:value="item.classId">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户昵称</span>
					<el-input v-model="userNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入用户昵称" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">手机号</span>
					<el-input v-model="phoneT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入手机号" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">服务标题</span>
					<el-input v-model="dataTitleT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入服务标题" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">城市</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable
						placeholder="请输入城市" v-model="cityT"></el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="select">查询</el-button>
					<el-button icon="document" type="primary" @click="cleans">重置</el-button>
					<el-button icon="document" type="primary" :disabled="!isAuth('procurementRequirements:add')"
						@click="addBtn(1)">添加</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="服务列表" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column label="编号" prop="dataId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="推荐状态" prop="createTime" fixed="left">
						<template slot-scope="scope">
							<el-switch :disabled="scope.row.status===3" v-model="scope.row.isRecommend"
								active-color="#13ce66" inactive-color="#ff4949" :active-value="1" inactive-value="0"
								@change="change(e,scope.row.dataId)">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column label="服务名称" prop="userName" fixed="left">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row.userId)">{{
									scope.row.dataTitle ? scope.row.dataTitle : '暂无'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="用户昵称" prop="realName">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="手机号" prop="phone">
						<template slot-scope="scope">
							<span v-if="scope.row.phone">{{ scope.row.phone }}</span>
						</template>
					</el-table-column>
					<el-table-column label="地址" prop="province">
						<template slot-scope="scope">
							<span>{{ scope.row.province + '' + scope.row.city + '' + scope.row.county }}</span>
						</template>
					</el-table-column>
					<el-table-column label="封面图" prop="cardFront" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.titleImg == null || scope.row.titleImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.titleImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.titleImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.titleImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="轮播图" prop="cardFront" width="230">
						<template slot-scope="scope">
							<div v-if="scope.row.rotationImg == null || scope.row.rotationImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.titleImg">
								<el-popover v-for="(item,index) in scope.row.rotationImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="服务详情" prop="dataJson" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.dataJson.length>0">
								<el-popover placement="top-start" title="" trigger="hover">
									<div slot="reference"
										style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
										<div v-for="(item,index) in scope.row.dataJson" :key="index">
											<div style="margin: 3px 0;">
												{{item.columnComment}}:{{item.columnValue}}
											</div>

										</div>
									</div>
									<div style="margin: 3px 0;width: 400px;">
										<div v-for="(item,index) in scope.row.dataJson" :key="index">
											<div style="margin: 3px 0;">
												{{item.columnComment}}:{{item.columnValue}}
											</div>
										</div>
									</div>
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="描述信息" prop="details" width="200">
					</el-table-column>
					<el-table-column label="浏览次数" prop="browseCount">
					</el-table-column>
					<el-table-column label="拒绝原因" prop="refuseReason">
						<template slot-scope="scope">
							<span>{{scope.row.refuseReason?scope.row.refuseReason:'暂无'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="创建时间" prop="createTime">
					</el-table-column>
					<el-table-column label="VIP是否到期" prop="createTime">
						<template slot-scope="scope">
							<span>{{scope.row.state==2?'是':'否'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="是否上下架" prop="createTime"  fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isEnable" @change="changeTj(scope.row.isEnable,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="状态" prop="checkCertification" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 0">待提交</span>
							<span v-if="scope.row.status == 1">待审核</span>
							<span v-if="scope.row.status == 2">上架中</span>
							<span v-if="scope.row.status == 3">已拒绝</span>
							<span v-if="scope.row.status == 4">已封禁</span>
							<!-- <span v-if="scope.row.state == 2">(VIP到期)</span> -->

						</template>
					</el-table-column>

					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<el-button :disabled="scope.row.status !== 1 || !isAuth('procurementRequirements:jujue')" size="mini"
								style='margin:5px;' type="primary" @click="refuseClick(scope.row)">
								拒绝
							</el-button>
							<el-button :disabled="scope.row.status !== 1 || !isAuth('procurementRequirements:tongguo')" size="mini"
								style='margin:5px;' type="primary" @click="passClicks(scope.row)">通过
							</el-button>
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(2,scope.row)" :disabled="!isAuth('procurementRequirements:update')">修改
							</el-button>
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" :disabled="!isAuth('procurementRequirements:delete')">删除
							</el-button>
							<el-button size="mini" type="danger" style='margin:5px;'  v-if="scope.row.status == 2"
								:disabled="!isAuth('procurementRequirements:fengjin')" @click="updateBans(scope.row)">封禁</el-button>
							<el-button size="mini" type="danger" style='margin:5px;' plain v-if="scope.row.status == 4"
								:disabled="!isAuth('procurementRequirements:fengjin')" @click="updateBansJ(scope.row)">解封</el-button>
						</template>
					</el-table-column>
				</el-table>
					<div class="nf-table-card__foot">
						<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
							:total="tableData.total" layout="total,sizes, prev, pager, next,jumper"
							@size-change="handleSizeChange" @current-change="handleCurrentChange">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
			<!-- 审核拒绝弹框 -->
			<el-dialog :visible.sync="dialogFormVisible" center title="审核拒绝">
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
					<el-input v-model="content" :rows="4" placeholder="请输入拒绝理由" style="width:50%;" type="textarea">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 添加、修改弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleR" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
					<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布人">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">服务分类：</span>
					<el-select v-model="classId" style="width:50%;" @change="selectAu()">
						<el-option v-for="item in classListA" :key="item.classId" :label="item.className"
							:value="item.classId">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="dataTitle" type="text" placeholder="请输入标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">联系电话：</span>
					<el-input style="width:50%;" v-model="phone" type="text" placeholder="请输入联系电话"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">服务地区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" filterable remote
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>

				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">价格信息：</span>
					<el-input style="width:50%;" v-model="priceInfo" type="text"
						placeholder="请输入价格信息,例:10元/斤"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">浏览次数：</span>
					<el-input style="width:50%;" v-model="browseCount" type="number" placeholder="请输入浏览次数"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">描述信息：</span>
					<el-input style="width:50%;" v-model="details" type="textarea" :rows="3"
						placeholder="请输入描述信息"></el-input>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
					<span class="bqList" v-for="(item,index) in label" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</span>
					<el-input v-model="bq" style="width:120px;" placeholder="请输入标签"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="titleImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="titleImg" :src="titleImg" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">轮播图：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in rotationImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear1(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
								:on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>

				</div>
				<div style="margin-bottom: 10px;" v-if="dataJson.length>0">
					<div v-for="(item,index) in dataJson" style="margin: 10px 0;">
						<span
							style="width: 200px;display: inline-block;text-align: right;">{{item.columnComment}}：</span>

						<el-input style="width:50%;display: none;" v-model="item.columnName" type="text"></el-input>
						<el-input style="width:50%;" v-if="item.htmlType==1" v-model="item.columnValue" type="text"
							:placeholder="item.columnComment"></el-input>
						<el-input style="width:50%;" v-if="item.htmlType==2" v-model="item.columnValue" type="textarea"
							:rows="3" :placeholder="item.columnComment"></el-input>
						<el-select v-model="item.columnValue" v-if="item.htmlType==3" style="width:50%;">
							<el-option v-for="(it,index2) in item.optionJson" :key="index2" :label="it" :value="it">
							</el-option>
						</el-select>

						<el-radio-group v-model="item.columnValue" v-if="item.htmlType==5">
							<el-radio v-for="(it,index3) in item.optionJson" :key="index3" :label="it">{{it}}</el-radio>
						</el-radio-group>
						<el-checkbox-group v-model="item.columnValueA" v-if="item.htmlType==4"
							style="display: inline-block;" @change="duoxian(item,index)">
							<el-checkbox v-for="(it,index1) in item.optionJson" :key="index1"
								:label="it">{{it}}</el-checkbox>
						</el-checkbox-group>
						<el-date-picker style="width: 50%;" v-if="item.htmlType==6" v-model="item.columnValue"
							align="right" type="date" format="yyyy年MM月dd日" value-format="yyyy年MM月dd日"
							placeholder="选择日期">
						</el-date-picker>
					</div>

				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleR = false">取 消</el-button>
					<el-button type="primary" @click="refusetoUp()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
				<div style="margin:2% 0;display: inline-block;">
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
						v-model="nickName2"></el-input>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
						查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
						重置
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.records">
					<el-table-column fixed prop="nickName" width="220" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" width="220" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" width="220" label="创建时间">
					</el-table-column>
					</el-table-column>
					<el-table-column label="操作" fixed='right' width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
					</el-pagination>
				</div>
			</el-dialog>
			<el-dialog title="封禁服务" :visible.sync="dialogVisibleBan" width="30%">
				<el-form ref="form" label-width="80px">
					<el-form-item label="封禁理由">
						<el-input v-model="banRemark" type="textarea" :rows="3" placeholder="请输入封禁理由"></el-input>
					</el-form-item>

				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="dialogVisibleBan = false">取 消</el-button>
					<el-button type="primary" @click="updateBan()">确 定</el-button>
				</span>
			</el-dialog>
	</div>
</template>

<script>
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				dataTitleT: '',
				userNameT: '',
				phoneT: '',
				content: '',
				status: '',
				cityT:'',
				screen: 1,
				statesnum2: [{
						value: 1,
						label: '发布时间'
					},
					{
						value: 2,
						label: '浏览次数'
					}
				],
				statesnum: [{
						value: '',
						label: '全部'
					},
					// {
					// 	value: 0,
					// 	label: '待提交'
					// },
					{
						value: 1,
						label: '待审核'
					}, {
						value: 2,
						label: '上架中'
					}, {
						value: 3,
						label: '已拒绝'
					}, {
						value: 4,
						label: '已封禁'
					}
				],
				classIdT: '',
				classList: [{
					classId: '',
					className: '全部'
				}],

				nickName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				activeName: 'first',
				openValue: 1,
				closeValue: 0,
				// 添加、修改
				classListA: [],
				titles: '添加服务',
				dialogFormVisibleR: false,
				classId: '', //分类id
				titleImg: '', //首页图
				dataTitle: '', //标题
				phone: '', //联系方式
				rotationImg: [], //轮播图
				label: [], //标签
				province: '', //省
				city: '', //市
				county: '', //县
				priceInfo: '', //价格信息,这里不是仅填写货币,而是包括货币及单位
				browseCount: '', //浏览次数
				details: '', //描述信息
				dataJson: [], //分类动态字段
				percentage1: 0,
				storeAddress: [],
				storeAddre: '请选择城市',
				options: regionData,
				bq: '',
				dialogFormVisible3: false,
				tableDataLoading4: false,
				phone1: '',
				nickName2: '',
				limitU: 10,
				pageU: 1,
				userIds: {},
				nickName1: '',
				userIdss: '',
				dialogVisibleBan: false,
				banRemark: '',
				shopId: '',

			}
		},
		methods: {
			change(e, dataId) {
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/recommend'),
					method: 'get',
					params: this.$http.adornParams({
						'dataId': dataId,
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
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}

				})
			},
			handleClick(tab, event) {
				if (tab._props.label === '服务列表') {
					this.page = 1
					this.limit = 10
					this.status = ''
					this.dataSelect()
				}
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
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
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limitU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			//单条任务通过
			passClicks(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/examine'),
					method: 'get',
					params: this.$http.adornParams({
						'res': this.status,
						'dataId': row.dataId,
						'refuseReason': ''
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
			//审核拒绝
			refuseClick(rows) {
				this.status = 2
				this.helpTakeId = rows.dataId
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
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/examine'),
					method: 'get',
					params: this.$http.adornParams({
						'res': this.status,
						'dataId': this.helpTakeId,
						'refuseReason': this.content
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
				this.dataTitleT = ''
				this.cityT = ''
				this.userNameT = ''
				this.phoneT = ''

				this.classIdT = ''
				this.screen = 1
				this.dataSelect()
				this.getClassList()
			},
			//获取分类列表
			getClassList() {
				this.$http({
					url: this.$http.adornUrl('admin/scTableClass/getClassList'),
					method: 'get',
					params: this.$http.adornParams({
						'className': '',
						'isEnable': 1
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.records
					this.classList = [...this.classList, ...returnData]
					this.classListA = data.data.records
				})
			},
			// 获取服务数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/getProcureDemandList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'title': this.dataTitleT,
						'industryFirstName': this.classIdT,
            'industrySecondName': this.classIdT,
						'status': this.status,
						'agentName': '',
						'county': '',
						'releaseType': this.screen,
						'sysUserName': this.userNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 查询
			selectZ() {
				this.page = 1
				this.limit = 10
				this.dataSelectC()
			},
			// 重置
			cleansZ() {
				this.status = ''
				this.userName = ''
				this.nickName = ''
				this.identityCardNumber = ''
				this.dataSelectC()
			},
			// 是否推荐
			changeTj(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/showAndDown'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'dataId': row.dataId,
						'userId': row.userId,
						// 'isEnable': row.isEnable,
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
			// 添加、修改 服务
			addBtn(index, row) {
				if (index == 1) {
					this.titles = '添加服务'
					this.dataId = ''
					this.classId = ''
					this.titleImg = ''
					this.dataTitle = ''
					this.phone = ''
					this.rotationImg = []
					this.label = []
					this.province = ''
					this.city = ''
					this.county = ''
					this.priceInfo = ''
					this.browseCount = ''
					this.details = ''
					this.dataJson = []
					this.userIdss = ''
					this.nickName1 = ''
					this.storeAddress = []
					this.storeAddre = '请选择城市'
				} else {
					this.titles = '修改服务'
					this.dataId = row.dataId
					this.classId = row.classId
					this.titleImg = row.titleImg
					this.dataTitle = row.dataTitle
					this.phone = row.phone
					this.rotationImg = row.rotationImg.split(',')
					this.label = row.label.split(',')
					this.province = row.province
					this.city = row.city
					this.county = row.county
					this.priceInfo = row.priceInfo
					this.browseCount = row.browseCount
					this.details = row.details
					this.dataJson = row.dataJson
					this.userIdss = row.userId
					this.nickName1 = row.userName

					this.storeAddre = this.province + '/' + this.city + '/' + this.county
					if (this.dataJson.length > 0) {
						for (var i in this.dataJson) {
							if (this.dataJson[i].htmlType == 4) {
								console.log("**********" + this.dataJson[i])
								this.dataJson[i].columnValueA = this.dataJson[i].columnValue.split(',')
								console.log('this.dataJson[i].columnValueA', this.dataJson[i].columnValueA)
							}

						}
					}
				}
				this.dialogFormVisibleR = true
			},
			// 确认 添加、修改
			refusetoUp() {
				if (this.userIdss == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					})
					return
				}
				if (this.classId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择服务分类',
						type: 'warning'
					})
					return
				}
				if (this.titleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					})
					return
				}
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入联系电话',
						type: 'warning'
					})
					return
				}
				if (this.titleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传封面图',
						type: 'warning'
					})
					return
				}
				if (this.rotationImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传轮播图',
						type: 'warning'
					})
					return
				}
				if (this.label.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签',
						type: 'warning'
					})
					return
				}
				if (this.province == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择服务地区',
						type: 'warning'
					})
					return
				}
				if (this.priceInfo === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入价格信息',
						type: 'warning'
					})
					return
				}
				if (this.browseCount === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入浏览记录',
						type: 'warning'
					})
					return
				}
				if (this.details == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入描述信息',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/insertData'),
					method: 'post',
					data: this.$http.adornData({
						'classId': this.classId,
						'titleImg': this.titleImg,
						'dataTitle': this.dataTitle,
						'phone': this.phone,
						'rotationImg': this.rotationImg.toString(),
						'label': this.label.toString(),
						'province': this.province,
						'city': this.city,
						'county': this.county,
						'priceInfo': this.priceInfo,
						'browseCount': this.browseCount,
						'dataJson': this.dataJson,
						'userId': this.userIdss,
						'dataId': this.dataId,
						'details': this.details

					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.$message({
							type: 'success',
							message: '操作成功'
						})
						this.dataSelect()
						this.dialogFormVisibleR = false
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				});
			},
			// 上传封面图
			handleAvatarSuccess1(file, fileList) {
				this.titleImg = file.data
			},
			//上传轮播图
			handleUploadSuccess3(file, fileList) {
				this.rotationImg.push(file.data)
				console.log('this.contentImg', this.rotationImg)
			},
			onprogress1(event, file, fileList) {
				this.percentage1 = parseInt(event.percent)
			},
			// 删除轮播图
			clear1(index) {
				this.rotationImg.splice(index, 1);
			},
			// 删除标签
			clear2(index) {
				this.label.splice(index, 1);
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.label.push(this.bq)
					this.bq = ''
				}
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.county = value[2]

				if (this.city == '市辖区') {
					this.city = this.province
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			// 获取分类模板
			selectAu() {
				// this.classId = classId
				this.$http({
					url: this.$http.adornUrl('admin/scTableColumn/getTableByClassId'),
					method: 'get',
					params: this.$http.adornParams({
						'classId': this.classId
					})
				}).then(({
					data
				}) => {
					// if (data.code == 0) {
					if (data.data.length > 0) {
						for (var i in data.data) {
							if (data.data[i].htmlType == 4) {
								data.data[i].columnValueA = []
							}
						}
					}
					this.dataJson = data.data


					// this.show = true
					// } else {
					// 	this.$message({
					// 		type: 'error',
					// 		message: data.msg
					// 	})
					// }
				})
			},
			// 多选框
			duoxian(item, index) {
				this.$forceUpdate()
				console.log("------" + JSON.stringify(item))
				this.dataJson[index].columnValue = this.dataJson[index].columnValueA.toString()
				console.log(this.dataJson[index].columnValue)
				console.log('this.dataJson[index].columnValueA', this.dataJson[index].columnValueA)
				/*
				// this.dataJson[index].columnValue = this.dataJson[index].columnValueA.toString()
				console.log(this.dataJson[index].columnValue )
				console.log('this.dataJson[index].columnValueA',this.dataJson[index].columnValueA) */
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.pageU = 1
				this.nickName2 = ''
				this.phone1 = ''
				this.userClass()
			},
			// 获取用户列表
			userClass() {

				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'isVip': '',
						'userName': this.nickName2,
						'phone': this.phone1,
						'platform': '',
						'userType': '',
						'inviterCode': '',
					})

				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.pageU = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.pageU = 1
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				this.dialogFormVisible3 = false
			},
			//打开封禁店铺弹窗
			updateBans(row) {
				this.shopId = row.dataId
				this.banRemark = ''
				this.dialogVisibleBan = true

			},
			//封禁店铺
			updateBan() {
				if (!this.banRemark) {
					this.$message({
						message: '请输入封禁理由',
						type: 'warning'
					});
					return
				}
				let that = this
				that.$confirm('确定封禁该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					that.$http({
						url: that.$http.adornUrl('admin/ScTableData/banData'),
						method: 'post',
						params: that.$http.adornParams({
							'dataId': that.shopId,
							'refuseReason': that.banRemark
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success'
							});
							that.dialogVisibleBan = false
							that.dataSelect()
						} else {
							that.$message.error(data.msg);
						}
					})
				}).catch(() => {
					that.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			// 解封服务
			updateBansJ(row){
				this.$confirm('确定解禁该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/ScTableData/banData'),
						method: 'post',
						params: this.$http.adornParams({
							'dataId': row.dataId,
							'refuseReason': '解禁服务'
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.dataSelect()
						} else {
							this.$message.error(data.msg);
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			dataSelect1() {
				if (this.$route.query.status) {
					this.status = Number(this.$route.query.status)
				} else {
					this.status = ''
				}
				this.dataSelect()
			},
			// 删除
			deleteBtn(row){
				this.$confirm('确定删除该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/ScTableData/deleteData'),
						method: 'get',
						params: this.$http.adornParams({
							'dataId': row.dataId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.dataSelect()
						} else {
							this.$message.error(data.msg);
						}
					})
				}).catch(() => {
					that.$message({
						type: 'info',
						message: '已取消'
					});
				});
			}
		},
		mounted() {
			if (this.$route.query.status) {
				this.status = Number(this.$route.query.status)
			} else {
				this.status = ''
			}
			//分类列表
			this.getClassList()
			//服务列表
			this.dataSelect()
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

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		flex-wrap: wrap;
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

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>
