<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">素材管理</span>
			<h2 class="nf-page-hero__title">行业分类</h2>
			<p class="nf-page-hero__desc">管理行业一级与二级分类，支持按分类名称筛选，可添加、编辑、删除及维护二级分类</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">分类名称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable v-model="helpClassifyNameT"
						placeholder="请输入分类名称"></el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('materialsList:add')" type="primary" icon="document"
						@click="addNotice(0)">添加分类</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="行业分类" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableDataF.records">
					<el-table-column fixed prop="industryId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="industryName" label="分类名称">
					</el-table-column>
					<el-table-column prop="sort" label="排序">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" width="160">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="erjiList(scope.row)" style="margin: 5px;">二级分类
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
								style="margin: 5px;" @click="updates(scope.row)">编辑
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
								style="margin: 5px;" @click="deletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeF" @current-change="handleCurrentChangeF"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataF.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
		</el-tabs>

			<!-- 二级分类 -->
			<el-dialog title="二级分类" :visible.sync="dialogFormVisibleEr" center>
				<div style="margin:10px;display: inline-block;float: right;">
					<el-button style='margin-left:15px;' :disabled="!isAuth('materialsList:add')" size="mini"
						type="primary" icon="document" @click="addNotice(1)">添加分类</el-button>
				</div>
				<el-table v-loading="tableDataLoadingEr" :data="tableDataFer.records">
					<el-table-column prop="industryId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="industryName" label="分类名称">
					</el-table-column>
					<el-table-column prop="sort" label="排序">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
								@click="updates(scope.row)">编辑
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
								@click="deletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 添加弹框 -->
			<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
					<el-input style="width:50%;" v-model="industryName" placeholder="请输入分类名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input-number size="medium" v-model="sort"></el-input-number>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<el-form-item label="分类名称：" :label-width="formLabelWidth">
						<el-input v-model="form.industryName" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<el-form :model="form">
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input-number size="medium" v-model="form.sort"></el-input-number>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 评论列表 -->
			<el-dialog title="评论列表" :visible.sync="dialogFormVisiblePl" center width="80%">
				<el-table v-loading="tableDataLoadingEr" :data="tableDataPl.records">
					<el-table-column prop="commentId" label="编号" fixed="left" width="80">
					</el-table-column>
					<el-table-column label="用户昵称" prop="realName" width="100">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updatesD(scope.row)">{{
          				scope.row.userName ? scope.row.userName : '-'
          			}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="commentContent" label="评论内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.commentContent}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="remarks" label="审核原因" width="160">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status==0">待审核</span>
							<span v-if="scope.row.status==1">正常</span>
							<span v-if="scope.row.status==2">审核未通过</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary"
								:disabled="scope.row.status !== 0 || !isAuth('agent:shenhe')" style="margin: 5px;"
								@click="refuseClick(scope.row,2)">
								审核
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
								style="margin: 5px;" @click="deleteswzPl(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangePl" @current-change="handleCurrentChangePl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataPl.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 点赞列表 -->
			<el-dialog title="点赞列表" :visible.sync="dialogFormVisibleDz" center>
				<el-table v-loading="tableDataLoadingEr" :data="tableDataDz.records">
					<el-table-column prop="userId" label="编号" width="80">
					</el-table-column>
					<el-table-column label="用户昵称" prop="realName">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updatesD(scope.row)">{{
          				scope.row.userName ? scope.row.userName : '-'
          			}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeDz" @current-change="handleCurrentChangeDz"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataDz.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 任务拒绝弹框 -->
			<el-dialog title="审核" :visible.sync="dialogFormVisibleS" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
					<el-radio-group v-model="status">
						<el-radio :label="1">通过</el-radio>
						<el-radio :label="2">拒绝</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">审核理由：</span>
					<!-- <el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入审核理由">
					</el-input> -->
					<quill-editor ref="myTextEditor1" v-model="content" :options="quillOption1"
						style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleS = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()" v-if="checkBoxData.length==0">确 定</el-button>
					<el-button type="primary" @click="querenClcik()" v-else>确 定</el-button>
				</div>
			</el-dialog>
			<!-- 下架原因 -->
			<el-dialog title="下架原因" :visible.sync="dialogFormVisibleXj" center @close="cloces">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">下架原因：</span>
					<!-- <el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入下架原因">
					</el-input> -->
					<quill-editor ref="myTextEditor2" v-model="content" :options="quillOption2"
						style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="cloces()">取 消</el-button>
					<el-button type="primary" @click="refusetoXj()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
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
					 <el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="nickName" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime"label="创建时间">
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
			<!-- 复用资讯 弹框 -->
			<el-dialog title="复用资讯" :visible.sync="dialogFormVisibleFyzx" center width="70%">

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">资讯类型：</span>
					<!-- <el-select v-model="industryType" style="width:50%;" filterable>
						<el-option v-for="(item,index) in flxmData1" :key="index" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select> -->
					<el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
						@change="handleCheckAllChange">全选</el-checkbox>
					<el-checkbox-group v-model="industryTypes" style="display: inline-block;margin-left: 200px;">
						<el-checkbox v-for="(item,index) in flxmData1" :key="index" style="margin: 5px;"
							:label="item.value">{{item.value}}</el-checkbox>
					</el-checkbox-group>




				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleFyzx = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeFuyong()">确 定</el-button>
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
				limit: 10,
				page: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				industryName: '',
				industryId: '',
				classifyId: 1,
				classifyIds: [],
				form: {
					industryId: '',
					isEnable: '',
					sort: '',
					industryName: '',
					parentId: '',

				},
				helpWordTitle: '',
				helpWordContent: '',
				classifyIdFl: '',
				formwz: {
					helpWordId: '',
					picture: '',
					articleUrl: '',
					helpWordContent: '',
					helpWordTitle: '',
					industryId: '',
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
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: {},
				tableDataF: {},
				parentIdnum: '',
				helpClassifyNameT: '',
				classnum: [{
						value: 0,
						label: '一级分类'
					},
					{
						value: 1,
						label: '二级分类'
					}
				],
				value: [],
				quillOption: quillConfig,
				quillOption1: quillConfig,
				quillOption2: quillConfig,
				helpWordTitleT: '',
				industryTypeT: '',
				userNameT: '',
				classDatas: [],
				value: [],
				parentId: 0,
				dialogFormVisibleEr: false,
				tableDataFer: {},
				tableDataLoadingEr: false,

				newsId: '', //新闻id（新增不传）
				releaseType: '', //发布类型 1用户发布 2平台发布
				userId: '', //用户id
				sysUserId: '', //平台用户id
				userName: '', //发布人昵称
				sysUserName: '', //平台用户名
				industryFirstName: '', //一级分类名称
				industrySecondName: '', //二级分类名称
				industryFirstId: '', //一级分类id
				industrySecondId: '', //二级分类id
				title: '', //标题
				content: '', //正文
				status: 1, //状态 1正常 2会员到期下架
				titleImg: '', //封面图
				percentage1: 0,
				enterpriseName: '', //企业名称
				keyWord: '', //搜索关键词
				contentImg: [], //内容图
				titles: '添加文章',
				page1: 1,
				limit1: 10,
				dialogFormVisiblePl: false,
				tableDataPl: {},
				dialogFormVisibleDz: false,
				tableDataDz: {},
				dialogFormVisibleS: false,
				industryType: '',
				releaseType: 2,
				flxmData: [],
				flxmData1: [],
				openValue: 1,
				closeValue: 0,
				dialogFormVisibleXj: false,
				statusT: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '正常'
					}, {
						value: 2,
						label: '审核未通过'
					}, {
						value: 3,
						label: '会员到期已下架'
					}
				],
				statusTP: '',
				statesnum1: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '正常'
					}, {
						value: 2,
						label: '审核未通过'
					},
				],
				indexType: 1,

				tableDataLoading4: false,
				phone1: '',
				nickName2: '',
				limitU: 10,
				pageU: 1,
				userIds: {},
				nickName1: '',
				userIdss: '',
				commentId: '',
				chatNumXx1: 0,
				chatNumXx2: 0,
				chatNumXx3: 0,
				checkBoxData: [],
				dialogFormVisibleFyzx: false,

				industryTypes: [],
				isIndeterminate: true,
				checkAll: false,

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
			handleClick(tab, event) {
				this.page = 1
				this.statusT = ''
				this.statusTP = ''
				if (tab._props.label == '行业分类') {
					this.dataSelect()
					this.flxmSelect()
				}
				

			},
			handleSizeChangeF(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChangeF(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleSizeChangePl(val) {
				this.limit1 = val;
				this.dataSelectPl()
			},
			handleCurrentChangePl(val) {
				this.page1 = val;
				this.dataSelectPl()
			},
			handleSizeChangeDz(val) {
				this.limit1 = val;
				this.dataSelectDz()
			},
			handleCurrentChangeDz(val) {
				this.page1 = val;
				this.dataSelectDz()
			},
			handleAvatarSuccess(file) {
				this.picture = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			handleSizeChange2(val) {
				this.limitU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element;
				});
			},
			// 添加分类弹框
			addNotice(index) {
				if (index == 0) {
					this.parentId = 0
				}

				this.industryName = ''
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {
				if (this.industryName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/industry/saveIndustry'),
					method: 'post',
					data: this.$http.adornData({
						'sort': this.sort,
						'industryName': this.industryName,
						'parentId': this.parentId,
						'isEnable': 1,
					}, false, 'from')
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
						if (this.parentId == 0) {
							this.dataSelect()
						} else {
							this.dataSelectFer()
						}
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
			// 编辑分类弹框
			updates(rows) {
				this.form.isEnable = rows.isEnable;
				this.form.sort = rows.sort;
				this.form.industryId = rows.industryId;
				this.form.industryName = rows.industryName;
				this.form.parentId = rows.parentId;
				this.dialogFormVisible1 = true;
			},
			// 修改类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('admin/industry/saveIndustry'),
					method: 'post',
					data: this.$http.adornData({
						'industryId': this.form.industryId,
						'industryName': this.form.industryName,
						'isEnable': this.form.isEnable,
						'sort': this.form.sort,
						'parentId': this.form.parentId,
					}, false, 'from')
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
						if (this.form.parentId == 0) {
							this.dataSelect()
						} else {
							this.dataSelectFer()
						}
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
			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							`admin/industry/deleteIndustryById?industryId=${row.industryId}`),
						method: 'post',
						params: this.$http.adornParams({})
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
							if (row.parentId == 0) {
								this.dataSelect()
							} else {
								this.dataSelectFer()
							}
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
			// 添加文章弹框
			addNoticewz() {
				this.titles = '添加文章'
				this.industryType = ''
				this.title = ''
				this.content = ''
				this.titleImg = []
				this.newsId = ''
				this.userId = ''
				this.nickName1 = ''
				this.releaseType = 2

				this.dialogFormVisible2 = true
				this.flxmSelect()
			},
			// 上传封面图
			handleAvatarSuccess1(file, fileList) {
				this.titleImg.push(file.data)
			},
			//上传轮播图
			handleUploadSuccess3(file, fileList) {
				this.contentImg.push(file.data)
			},
			onprogress1(event, file, fileList) {
				this.percentage1 = parseInt(event.percent)
			},
			// 删除轮播图
			clear1(index) {
				this.titleImg.splice(index, 1);
			},
			industryFirstBtn() {
				this.industrySecondId = ''
				this.parentId = this.industryFirstId
				this.dataSelectFer()
			},
			// 添加文章
			releasNoticeTowz() {

				// if (this.industryFirstId == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择一级分类',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.industryType == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择资讯类型',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入文章标题',
						type: 'warning'
					});
					return
				}

				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				// if (this.titleImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传封面图',
				// 		type: 'warning'
				// 	});
				// 	return
				// }


				this.$http({
					url: this.$http.adornUrl('admin/industryNews/adminSaveIndustryNews'),
					method: 'post',
					data: this.$http.adornData({
						'newsId': this.newsId,
						'userId': this.userId,
						// 'industryFirstId': this.industryFirstId,
						// 'industrySecondId': this.industrySecondId,
						'title': this.title,
						'content': this.content,
						'titleImg': this.titleImg.toString(),
						'industryType': this.industryType,
						'isEnable': 1,
						'releaseType': this.releaseType,
						// 'contentImg':this.contentImg.toString()
					}, false, 'from')
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
			updateswz(rows) {
				console.log('-----',rows.industryType)
				this.titles = '修改文章'
				this.newsId = rows.newsId
				this.industryFirstId = rows.industryFirstId
				this.industrySecondId = rows.industrySecondId
				this.title = rows.title
				this.content = rows.content
				this.industryType = rows.industryType
				if (rows.releaseType) {
					this.releaseType = Number(rows.releaseType)
				} else {
					this.releaseType = 2
				}

				this.userId = rows.userId
				this.nickName1 = rows.userName
				if (rows.titleImg) {
					this.titleImg = rows.titleImg.split(',')
				} else {
					this.titleImg = []
				}
				this.parentId = rows.industryFirstId
				this.flxmSelect()
				// this.dataSelectFer()
				this.dialogFormVisible2 = true
			},

			// 删除文章
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/industryNews/deleteIndustryNewsById'),
						method: 'post',
						params: this.$http.adornParams({
							'newsId': row.newsId
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
					url: this.$http.adornUrl('admin/industry/getIndustryList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': 100,
						'industryName': this.helpClassifyNameT,
						'isEnable': '',
						'parentId': 0,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataF = returnData
				})
			},
			// 分类无分页
			dataSelectW() {
				this.tableDataLoading = true
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
					this.tableDataLoading = false
					let returnData = data.data;
					this.classifyId = data.data.records[0].industryId
					this.classifyIds = returnData.records
					this.articleSelect()
				})
			},
			// 获取行业资讯数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/industryNews/getIndustryNewsList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'industryId': this.classifyId,
						// 'helpWordTitle': this.helpWordTitleT,
						'page': this.page,
						'limit': this.limit,
						'industryFirstName': this.industryFirstName,
						'industrySecondName': this.industrySecondName,
						'title': this.helpWordTitleT,
						'industryType': this.industryTypeT,
						'status': this.statusT,
						'userName': this.userNameT,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.helpClassifyNameT = ''
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
				this.industryFirstName = ''
				this.industrySecondName = ''
				this.helpWordTitleT = ''
				this.industryTypeT = ''
				this.statusT = ''
				this.userNameT = ''
				this.page = 1
				this.articleSelect()
			},
			// 二级分类弹框
			erjiList(row) {
				this.parentId = row.industryId
				this.dataSelectFer()
				this.dialogFormVisibleEr = true
			},
			// 获取分类管理数据
			dataSelectFer() {
				this.tableDataLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('admin/industry/getIndustryList'),
					method: 'get',
					params: this.$http.adornParams({
						'industryName': '',
						'parentId': this.parentId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingEr = false
					let returnData = data.data;
					this.tableDataFer = returnData
				})
			},
			// 详情跳转
			updatesD(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				}

			},
			selectPl() {
				this.page1 = 1
				this.dataSelectPl()
			},
			// 评论列表弹框
			pinglunBtn(row) {
				this.page1 = 1
				this.statusTP = ''
				this.newsId = row.newsId
				this.dataSelectPl()
				this.dialogFormVisiblePl = true
			},
			//评论列表
			dataSelectPl() {
				this.tableDataLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('admin/industryNewsComment/getCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'newsId': this.newsId,
						'status': this.statusTP
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingEr = false
					let returnData = data.data;
					this.tableDataPl = returnData
				})
			},
			// 删除评论
			deleteswzPl(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/industryNewsComment/deleteIndustryNewsComment'),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
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
									this.dataSelectPl()
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
			// 点赞列表弹框
			dianzanBtn(row) {
				this.page1 = 1
				this.newsId = row.newsId
				this.dataSelectDz()
				this.dialogFormVisibleDz = true
			},
			//点赞列表
			dataSelectDz() {
				this.tableDataLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('admin/industryNewsLike/getLikeUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'newsId': this.newsId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingEr = false
					let returnData = data.data;
					this.tableDataDz = returnData
				})
			},
			//审核弹框
			refuseClick(rows, index) {
				this.indexType = index
				if (index == 1) {
					this.newsId = rows.newsId
				} else {
					this.commentId = rows.commentId
				}
				this.status = ''
				this.content = ''
				// this.refuseto()
				this.dialogFormVisibleS = true
			},
			refuseto() {
				if (this.status == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}

				if (this.status == 2) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						});
						return
					}
				}

				if (this.indexType == 1) {
					this.$http({
						url: this.$http.adornUrl('admin/industryNews/examineNews'),
						method: 'post',
						data: this.$http.adornData({
							'status': this.status,
							'newsId': this.newsId,
							'examineRemark': this.content,

						}, false, 'from')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.dialogFormVisibleS = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.content = ''
									this.status = ''
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
				} else {
					this.$http({
						url: this.$http.adornUrl('/admin/industryNewsComment/auditComment'),
						method: 'post',
						data: this.$http.adornData({
							'status': this.status,
							'commentId': this.commentId,
							'remarks': this.content,
						}, false, 'from')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.dialogFormVisibleS = false
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectPl()
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
				}

			},
			// 资讯类型
			flxmSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '资讯类型'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.flxmData = [{
						value: '全国-制造业总群',
						label: ''
					}]
					this.flxmData = [...this.flxmData, ...returnData]
					// this.flxmData.unshift(datas)
					this.flxmData1 = returnData
					console.log('this.flxmData-----', this.flxmData)
					console.log('this.flxmData1', this.flxmData1)
				})
			},
			// 上下架
			changeTj(val, row) {
				console.log('---------val', val)
				// if (val == '0') {
				// 	this.newsId = row.newsId
				// 	this.content = ''
				// 	this.dialogFormVisibleXj = true
				// } else {
				this.$http({
					url: this.$http.adornUrl('admin/industryNews/upOrDownNews'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'newsId': row.newsId,
						'downRemark': '',
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
							onClose: () => {
								this.articleSelect()
							}
						})
					}
				})
				// }

			},
			refusetoXj() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入下架原因',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('admin/industryNews/upOrDownNews'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'newsId': this.newsId,
						'downRemark': this.content,
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
								this.dialogFormVisibleXj = false
								this.articleSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleXj = false
								this.articleSelect()
							}
						})
					}
				})
			},
			cloces() {
				this.dialogFormVisibleXj = false
				this.articleSelect()
			},
			dataSelect1() {
				if (this.$route.query.status != null) {
					this.statusT = this.$route.query.status
				} else {
					this.statusT = ''
				}
				this.dataSelect()
				this.flxmSelect()
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
				this.userId = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				this.dialogFormVisible3 = false
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 批量审核
			transferClcik(index) {
				this.indexType = index
				this.status = ''
				this.content = ''
				// this.refuseto()
				this.dialogFormVisibleS = true



			},
			// 确认批量审核
			querenClcik(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					console.log('this.indexType---', this.indexType)
					console.log('item-', item)
					if (this.indexType == 1) {
						item.id = item.newsId
					} else {
						item.id = item.commentId
					}
					return item.id
				})

				for (var i in ids) {
					if (this.indexType == 1) {
						this.$http({
							url: this.$http.adornUrl('admin/industryNews/examineNews'),
							method: 'post',
							data: this.$http.adornData({
								'status': this.status,
								'newsId': ids[i],
								'examineRemark': this.content,

							}, false, 'from')
						}).then(({
							data
						}) => {
							if (data.code == 0) {
								this.dialogFormVisibleS = false
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.content = ''
										this.status = ''

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
					} else {
						this.$http({
							url: this.$http.adornUrl('/admin/industryNewsComment/auditComment'),
							method: 'post',
							data: this.$http.adornData({
								'status': this.status,
								'commentId': ids[i],
								'remarks': this.content,
							}, false, 'from')
						}).then(({
							data
						}) => {
							if (data.code == 0) {
								this.dialogFormVisibleS = false
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.dataSelectPl()
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
					}
				}


			},
			//判断勾选
			selectable(row, index) {
				if (row.status != 0) {
					return false;
				} else {
					return true;
				}
			},
			// 复用资讯 弹框
			fuyongBtn(rows) {
				this.isIndeterminate = true
				this.checkAll = false
				this.newsId = ''
				this.industryFirstId = rows.industryFirstId
				this.industrySecondId = rows.industrySecondId
				this.title = rows.title
				this.content = rows.content
				this.industryTypes = []
				if (rows.releaseType) {
					this.releaseType = Number(rows.releaseType)
				} else {
					this.releaseType = 2
				}

				this.userId = rows.userId
				this.nickName1 = rows.userName
				if (rows.titleImg) {
					this.titleImg = rows.titleImg.split(',')
				} else {
					this.titleImg = []
				}
				this.parentId = rows.industryFirstId

				this.flxmSelect()
				this.dialogFormVisibleFyzx = true

			},
			releasNoticeFuyong(id) {
				if (this.industryTypes.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择资讯类型',
						type: 'warning'
					});
					return
				}

				var ids = id ? [id] : this.industryTypes.map(item => {
					console.log(item)
					return item
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl('admin/industryNews/adminSaveIndustryNews'),
						method: 'post',
						data: this.$http.adornData({
							'newsId': this.newsId,
							'userId': this.userId,
							'title': this.title,
							'content': this.content,
							'titleImg': this.titleImg.toString(),
							'industryType': ids[i],
							'isEnable': 1,
							'releaseType': this.releaseType,
						}, false, 'from')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.dialogFormVisibleFyzx = false
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

				}

			},
			// 全选
			handleCheckAllChange(val) {
				var arr = []
				for (var i in this.flxmData1) {
					arr.push(this.flxmData1[i].value)
				}

				this.industryTypes = val ? arr : [];
				this.isIndeterminate = false;
			},

		},
		mounted() {
			
			this.dataSelect()


		},
		watch: {
			'$route': 'dataSelect1'
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

	._div p,
	h2 {
		width: 100%;
	}

	._div img {
		width: 100% !important;
	}

	.item0 {
		margin-top: 10px;
		margin-right: 40px;
		position: absolute;
		top: 18px;
	}

	.item1 {
		left: 85px;
	}

	.item2 {
		left: 180px;
	}

	.item3 {
		left: 375px;
	}
</style>