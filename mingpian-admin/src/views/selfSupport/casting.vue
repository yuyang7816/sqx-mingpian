<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">关键词管理</span>
			<h2 class="nf-page-hero__title">关键词列表</h2>
			<p class="nf-page-hero__desc">管理用户关键词推送记录，支持按状态、行业、关键词、昵称与电话筛选查询</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="statusId" class="nf-filter-form__input" @change="animeDat(statusId)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">行业一级名称</span>
					<el-select v-model="industryFirstNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请选择行业一级名称" filterable @change="industryFirstBtn(1)">
						<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.industryName"
							:value="item.industryName">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">行业二级名称</span>
					<el-select v-model="industrySecondNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请选择行业二级名称" filterable :disabled="industryFirstNameT==''" @change="selectW()">
						<el-option v-for="(item,index) in tableDataFer" :key="item.index" :label="item.industryName"
							:value="item.industryName">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">关键词</span>
					<el-input v-model="search" class="nf-filter-form__input" placeholder="请输入关键词"
						@keydown.enter.native="selectW">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">昵称</span>
					<el-input v-model="nftName" class="nf-filter-form__input" placeholder="请输入昵称"
						@keydown.enter.native="selectW">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">电话</span>
					<el-input v-model="phoneT" class="nf-filter-form__input" placeholder="请输入电话"
						@keydown.enter.native="selectW">
					</el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="selectW">查询</el-button>
					<el-button type="primary" icon="document" @click="cleansW">重置</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="关键词列表" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="sendMessageId" label="编号" width="80" fixed="left">
						</el-table-column>
						<el-table-column prop="userName" label="昵称">
							<template slot-scope="scope">
								<span class="nf-table-link" @click="updatesvideo(scope.row.userId)"
									v-text="scope.row.userName"></span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="电话" width="140">
						</el-table-column>
						<el-table-column prop="industryFirstName" label="行业一级名称">
						</el-table-column>
						<el-table-column prop="industrySecondName" label="行业二级名称">
						</el-table-column>
						<el-table-column prop="content" label="关键词" width="140">
						</el-table-column>
						<el-table-column prop="sendTime" label="推送时间" width="140">
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column prop="isOpen" label="状态">
							<template slot-scope="scope">
								<div v-if="scope.row.isOpen==1">开启</div>
								<div v-if="scope.row.isOpen==2">关闭</div>
							</template>
						</el-table-column>
						<!-- <el-table-column label="操作" width="100" fixed="right">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="shenhe(scope.row)" :disabled="scope.row.status!=1"
									style="margin: 5px;">审核
								</el-button>
							</template>
						</el-table-column> -->
					</el-table>
					<div class="nf-table-card__foot">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
		<!-- 添加弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible2" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">创造人：</span>
				<el-input v-model="nickName1" :readonly="true" @click.native="userselect" style="width:80%;"
					placeholder="请选择创造人">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">分类：</span>
				<el-radio-group v-model="classifyId" style="display: inline-block;">
					<!-- <el-radio :label="0">合集</el-radio> -->
					<el-radio :label="1">作品</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">权益类型：</span>
				<el-radio-group v-model="rights" style="display: inline-block;">
					<el-radio :label="1">版权品</el-radio>
					<el-radio :label="2">衍生品</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">作品名称：</span>
				<el-input style="width: 80%;" v-model="nftNameL" placeholder="请输入作品名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 100px;display: inline-block;text-align: right;">主页图：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="nftTitleImg"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess5" :on-progress="onprogress">
						<img v-if="nftTitleImg!=''&&percentage==100" :src="nftTitleImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						<el-progress v-if="percentage>0&&percentage<100" type="circle" :percentage="percentage">
						</el-progress>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 100px;display: inline-block;text-align: right;">轮播图：</span>
				<div class="imgs" v-for="(item,index) in nftImg" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="dels(index)"></i>
					</span>
				</div>
				<div class="imgs" style="width: 50%;">
					<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" list-type="picture-card"
						:show-file-list="false" :on-success="handleRemove" v-if="nftImg.length<5">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">版税(%)：</span>
				<el-input-number v-model="nftRate" placeholder="请输入版税" controls-position="right" :min="1"
					:max="100"></el-input-number>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">铸造数量：</span>
				<!-- <el-input style="width: 80%;" v-model="quantity" placeholder="请输入铸造数量"></el-input> -->
				<el-input-number v-model="quantity" :min="1" label="请输入铸造数量"></el-input-number>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">作品简介：</span>
				<el-input style="width: 80%;" v-model="nftDetails" type="textarea" :rows="3" placeholder="请输入作品简介">
				</el-input>
				<!-- <quill-editor ref="myTextEditor" v-model="nftDetails" :options="quillOption"
					style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
				</quill-editor> -->
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 服务审核弹框 -->
		<el-dialog title="审核" :visible.sync="dialogFormVisible8" center>
			<el-radio-group v-model="radio" style="width: 100%;margin-bottom: 30px;margin-left: 200px;">
				<el-radio :label="1">通过</el-radio>
				<el-radio :label="2">驳回</el-radio>
			</el-radio-group>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核理由：</span>
				<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入审核理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible8 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible4" center width="60%">
			<div style="margin:2% 0;display: inline-block;">
				<el-select :disabled="true" v-model="userTypes" @change="userclick" placeholder="请选择用户类型">
					<el-option v-for="item in userType" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				<!-- <el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input> -->
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
				<el-table-column prop="userId" width="80" label="用户Id" fixed>
				</el-table-column>
				<el-table-column fixed prop="userName" label="用户昵称">
				</el-table-column>
				<el-table-column fixed prop="typeUser" label="用户类型">
					<template slot-scope="scope">
						<span>{{scope.row.typeUser==1?'平台用户':'普通用户'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
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
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				userTypes: '',
				userType: [{
						value: '',
						label: '全部用户'
					},
					{
						value: 1,
						label: '平台用户'
					},
					{
						value: 2,
						label: '普通用户'
					}
				],
				userList: [],
				openValue: 2,
				closeValue: 4,
				limit: 10,
				page: 1,
				size1: 10,
				page1: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				helpClassifyName: '',
				helpClassifyId: '',
				classifyId: 1,

				helpWordContent: '',
				classifyIdFl: '',
				formwz: {
					helpWordId: '',
					picture: '',
					articleUrl: '',
					helpWordContent: '',
					helpWordTitle: '',
					helpClassifyId: '',
					createTime: '',
					sort: 0,
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				picture: '',
				picture2: '',
				articleUrl: '',

				title: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				tableDataLoading4: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				tableData: {},
				tableDataF: {},
				parentIdnum: '',
				helpClassifyNameT: '',

				value: [],
				quillOption: quillConfig,
				helpWordTitleT: '',
				dialogFormVisible8: false,
				radio: 1,
				contents: '',
				shenheId: '',
				nftName: '',
				search: '',
				nftTitleImg: '',
				nftImg: [],
				percentage: 0, //进度条
				percentage2: 0, //进度条
				nftNameL: '',
				nftDetails: '',
				classifyId: 1,
				rights: 1,
				originalId: 0,
				nftRate: '',
				quantity: '',
				phone1: '',
				nickName1: '',
				nickName2: '',
				userIds: {},
				userIdss: '',
				statusId: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '开启'
					},
					{
						value: 2,
						label: '关闭'
					},

				],
				dialogVisible: false,
				dialogImageUrl: '',
				industryFirstNameT: '',
				industrySecondNameT: '',
				phoneT: '',
				classifyIds: [],
				tableDataFer: [],
				industryFirstId:'',
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 详情跳转
			updatesvideo(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '分类管理') {}
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.userClass()
			},
			handleAvatarSuccess(file) {
				this.picture = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			onprogress(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.nftTitleImg = file.data;
			},
			// 详情图片上传
			handleRemove(file, ) {
				this.nftImg.push(file.data);
			},
			// 删除详情图
			dels(index) {
				console.log(this.nftImg)
				this.nftImg.splice(index, 1);

			},

			// 添加分类弹框
			addNotice() {
				this.helpClassifyName = ''
				this.sort = 0
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {
				if (this.helpClassifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('help/insertHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.state,
						'sort': this.sort,
						'remark': this.remark,
						'helpClassifyName': this.helpClassifyName,
						'createTime': this.info.stockDate,
					})
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
			// 添加文章弹框
			addNoticewz(type) { //1:管理员铸造  2:协助铸造
				if (type == 1) {
					this.userTypes = 1
					this.nftTitleImg = ''
					this.nftImg = []
					this.nftNameL = ''
					this.nftDetails = ''
					this.classifyId = 1
					this.rights = 1
					this.nftRate = ''
					this.quantity = ''
					this.originalId = 0
					this.userIdss = ''
					this.nickName1 = ''
					this.dialogFormVisible2 = true
				} else {
					this.userTypes = 2
					this.$confirm('此操作为危险操作, 是否继续?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.nftTitleImg = ''
						this.nftImg = []
						this.nftNameL = ''
						this.nftDetails = ''
						this.classifyId = 1
						this.rights = 1
						this.nftRate = ''
						this.quantity = ''
						this.originalId = 0
						this.userIdss = ''
						this.nickName1 = ''
						this.dialogFormVisible2 = true
					}).catch(() => {
						this.$message({
							type: 'info',
							message: '已取消操作'
						});
					});
				}

			},
			// 添加文章
			releasNoticeTowz() {
				if (this.userIdss === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择创造人',
						type: 'warning'
					});
					return
				}
				if (this.nftNameL == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入作品名称',
						type: 'warning'
					});
					return
				}
				if (this.nftTitleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传作品封面图',
						type: 'warning'
					});
					return
				}
				if (this.nftImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传作品内容图',
						type: 'warning'
					});
					return
				}
				if (this.nftRate == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入版税',
						type: 'warning'
					});
					return
				}
				if (this.quantity == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入铸造数量',
						type: 'warning'
					});
					return
				}
				if (this.nftDetails == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入作品详情介绍',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/adminCastingNft'),
					method: 'post',
					params: this.$http.adornParams({
						'nftTitleImg': this.nftTitleImg,
						'nftImg': this.nftImg.toString(),
						'nftName': this.nftNameL,
						'nftDetails': this.nftDetails,
						'classifyId	': this.classifyId,
						'rights': this.rights,
						'nftRate': this.nftRate,
						'quantity': this.quantity,
						'originalId': this.userIdss
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
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
			// 编辑文章弹框
			updateswz(index, rows) {
				this.formwz.picture = rows.picture;
				this.dialogFormVisible3 = true;
				this.formwz.helpWordId = rows.helpWordId;
				this.formwz.helpClassifyId = rows.helpClassifyId
				this.formwz.sort = rows.sort;
				this.formwz.helpWordContent = rows.helpWordContent;
				this.formwz.helpWordTitle = rows.helpWordTitle;
				this.formwz.createTime = rows.createTime;
			},
			// 修改文章
			amendNoticeTowz() {
				this.$http({
					url: this.$http.adornUrl('help/updateHelpWord'),
					method: 'post',
					params: this.$http.adornParams({
						'helpWordId': this.formwz.helpWordId,
						'helpWordContent': this.formwz.helpWordContent,
						'helpClassifyId': this.formwz.helpClassifyId,
						'helpWordTitle': this.formwz.helpWordTitle,
						'createTime': this.info.stockDate,
						'sort': this.formwz.sort
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
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
			// 删除文章
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/deleteHelpWord'),
						method: 'post',
						params: this.$http.adornParams({
							'helpWordId': row.helpWordId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.name = '';
									this.articleSelect()
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
			// 获取分类管理数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectHelpClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataF = returnData
				})
			},

			// 获取铸造列表数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('sendMessage/selectSendMessageList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'industryFirstName': this.industryFirstNameT,
						'industrySecondName': this.industrySecondNameT,
						'content': this.search,
						'isOpen': this.statusId,
						'userName': this.nftName,
						'phone': this.phoneT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},

			// 查询
			selectW() {
				this.page = 1
				this.articleSelect()
			},
			// 重置
			cleansW() {
				this.search = ''
				this.industryFirstNameT = ''
				this.industrySecondNameT = ''
				this.nftName = ''
				this.statusId = ''
				this.phoneT = ''
				this.page = 1
				this.articleSelect()
			},
			manghe(row) {},
			// 上下架
			change3(val, row) {
				console.log('上下架')
				this.$http({
					url: this.$http.adornUrl('admin/nftBranch/shelfNft'),
					method: 'post',
					params: this.$http.adornParams({
						'branchId': row.branchId
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
								this.articleSelect()
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
			// 审核
			shenhe(row) {
				this.shenheId = row.nftId
				this.radio = 1
				this.content = ''
				this.dialogFormVisible8 = true
			},
			// 提交审核
			refuseto(row) {

				if (this.radio == 2 && this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入驳回理由',
						type: 'warning'
					})
					return
				}
				let ids = this.shenheId
				let status = this.radio
				let content = this.contents
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/examine'),
					method: 'get',
					params: this.$http.adornParams({
						'nftId': ids,
						'status': status,
						'examineReason': content
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible8 = false
					this.$message({
						message: '审核完成',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.radio = 1
							this.contents = ''
							this.articleSelect()
						}
					})
				})
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible4 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.$http({
					url: this.$http.adornUrl('user/getAdminUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'typeUser': this.userTypes,
						'userName': this.nickName2
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
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
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
				this.dialogFormVisible4 = false
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.articleSelect()
			},
			// 分类无分页
			dataSelectW() {
				this.$http({
					url: this.$http.adornUrl('admin/industry/getIndustryList'),
					method: 'get',
					params: this.$http.adornParams({
						'industryName': this.helpClassifyNameT,
						'parentId': 0,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classifyIds = returnData.records
				})
			},
			// 获取分类管理数据
			dataSelectFer() {
				this.tableDataLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('admin/industry/getIndustryList'),
					method: 'get',
					params: this.$http.adornParams({
						'industryName': '',
						'parentId': this.industryFirstId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingEr = false
					let returnData = data.data;
					this.tableDataFer = returnData.records
				})
			},
			industryFirstBtn(index) {
				if (index == 1) {
					console.log('index', index, this.industryFirstNameT)
					for (var i in this.classifyIds) {
						if (this.industryFirstNameT == this.classifyIds[i].industryName) {
							this.industryFirstId = this.classifyIds[i].industryId
							this.industrySecondNameT = ''
							this.articleSelect()
							this.dataSelectFer()
						}
					}

				}

			},
		},
		mounted() {
			this.articleSelect()
			this.dataSelectW()
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
</style>