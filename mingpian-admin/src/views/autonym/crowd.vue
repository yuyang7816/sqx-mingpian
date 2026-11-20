<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">模板管理</span>
			<h2 class="nf-page-hero__title">模板列表</h2>
			<p class="nf-page-hero__desc">管理名片模板信息，支持按用户昵称、手机号码、邮箱筛选查询，可添加与修改模板</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户昵称</span>
					<el-input v-model="userNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入用户昵称" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">手机号码</span>
					<el-input v-model="dataTitleT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入手机号码" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">邮箱</span>
					<el-input v-model="screen" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入邮箱" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="select">查询</el-button>
					<el-button icon="document" type="primary" @click="cleans">重置</el-button>
					<el-button icon="document" type="primary" :disabled="!isAuth('crowd:add')" @click="addBtn(1)">添加
					</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="模版列表" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column label="编号" prop="id" width="150" fixed="left">
					</el-table-column>
					<el-table-column label="发布用户" prop="userName">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					
					<!-- <el-table-column label="模板名称" prop="templateName" width="200">
					</el-table-column> -->
					<el-table-column label="手机号码" prop="phone">
					</el-table-column>
					<el-table-column label="邮箱" prop="email">
					</el-table-column>
					<el-table-column label="背景图片" prop="backImg" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.backImg == null || scope.row.backImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.backImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.backImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.backImg" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="公司LOGO" prop="companyLogo" width="230">
						<template slot-scope="scope">
							<div v-if="scope.row.companyLogo == null || scope.row.companyLogo == ''">
								暂无图片
							</div>
							<div v-if="scope.row.companyLogo">
								<el-popover v-for="(item,index) in scope.row.companyLogo.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<!-- <el-table-column label="群详情图" prop="groupContentImg" width="230">
						<template slot-scope="scope">
							<div v-if="scope.row.groupContentImg == null || scope.row.groupContentImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.groupContentImg">
								<el-popover v-for="(item,index) in scope.row.groupContentImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column label="公司名称" prop="companyName">
					</el-table-column>
					<el-table-column label="职务名称" prop="companyuPisition">
					</el-table-column>
					
					<el-table-column label="公司地址" prop="companyAddress" width="200">
					</el-table-column>
					<el-table-column label="公司简介" prop="companyDesc" width="200">
					</el-table-column>
					<!-- <el-table-column label="备注" prop="remark">
					</el-table-column> -->
					<el-table-column label="创建时间" prop="createTime">
					</el-table-column>
					<!-- <el-table-column fixed='right' label="是否启用" prop="isEnable" width="80">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isEnable" active-color="#13ce66" inactive-color="#ff4949"
								:disabled="!isAuth('crowd:update')" :active-value="1" :inactive-value="0"
								@change="change(scope.row)">
							</el-switch>

						</template>
					</el-table-column> -->
					<!-- <el-table-column fixed='right' label="状态" prop="status" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1">待审核</span>
							<span v-if="scope.row.status == 2">审核成功</span>
							<span v-if="scope.row.status == 3">审核未通过</span>
						</template>
					</el-table-column> -->
					<el-table-column fixed='right' label="操作" width="100">
						<template slot-scope="scope">
							<!-- <el-button :disabled="scope.row.status !== 1 || !isAuth('businessCard:shenhe')" size="mini"
								style='margin:5px;' type="primary" @click="refuseClick(scope.row)">审核
							</el-button> -->
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(2,scope.row)" :disabled="!isAuth('crowd:update')">修改
							</el-button>
							<!-- <el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" >删除
							</el-button> -->
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
			<!-- <el-tab-pane label="群分类" name="second">
				<div style="float: right;margin-right:2%;">
					<el-button style="margin: 10px 0;" :disabled="!isAuth('crowd:add')" size="mini" type="primary"
						icon="document" @click="addNotice2">添加</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableDataQfl.list">
					<el-table-column fixed prop="id" label="编号" width="50">
					</el-table-column>
					<el-table-column prop="name" label="分类名称">
					</el-table-column>
					<el-table-column label="排序" prop="sort">
					</el-table-column>
					<el-table-column prop="state" label="状态">
						<template slot-scope="scope">
							<span v-if="scope.row.state == 1">显示</span>
							<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
						</template>
					</el-table-column>
					<el-table-column prop="state" label="是否启用">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.state" @change="changeQ(scope.row.state,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('crowd:update')"
								@click="amendBanner(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('crowd:delete')"
								@click="deletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;float:right">
					<el-pagination @size-change="handleSizeChangeQfl" @current-change="handleCurrentChangeQfl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataQfl.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
		</el-tabs>
			<!-- 审核拒绝弹框 -->
			<el-dialog :visible.sync="dialogFormVisible" center title="审核">
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
					<el-radio-group v-model="status">
						<el-radio :label="2">通过</el-radio>
						<el-radio :label="3">拒绝</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="status==2">
					<span style="width: 200px;display: inline-block;text-align: right;">奖励积分数：</span>
					<el-input style="width:50%;" v-model="rewardPoint" type="number" :min="0" placeholder="请输入奖励积分数">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="status==2">
					<span style="width: 200px;display: inline-block;text-align: right;">加群所需点数：</span>
					<el-input style="width:50%;" v-model="countMoney" type="number" :min="0" placeholder="请输入加群所需点数">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">审核理由：</span>
					<el-input v-model="content" :rows="4" placeholder="请输入审核理由" style="width:50%;" type="textarea">
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
					<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
					<el-input v-model="sysUserName" @focus="userselect" style="width:50%;" placeholder="请选择用户">
					</el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">模板名称：</span>
					<el-input style="width:50%;" v-model="templateName" type="text" placeholder="请输入模板名称"></el-input>
				</div> -->
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司名称：</span>
					<el-input style="width:50%;" v-model="companyName" type="text" placeholder="请输入公司名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">职务名称：</span>
					<el-input style="width:50%;" v-model="companyuPisition" type="text" placeholder="请输入职务名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号码：</span>
					<el-input style="width:50%;" v-model="phone" type="text" placeholder="请输入手机号码"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邮箱：</span>
					<el-input style="width:50%;" v-model="email" type="text" placeholder="请输入邮箱"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司地址：</span>
					<el-input style="width:50%;" v-model="companyAddress" type="text" placeholder="请输入公司地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司简介：</span>
					<el-input style="width:50%;" v-model="companyDesc" type="text" placeholder="请输入公司简介"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
					<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入备注"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司LOGO：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="companyLogo"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="companyLogo" :src="companyLogo" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">背景图片：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in backImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear1(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" list-type="picture-card"
								:show-file-list="false" :on-success="handleUploadSuccess3" :on-progress="onprogress1"
								v-if="backImg.length<1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
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
			<!-- 添加群分类弹框 -->
			<el-dialog title="添加群分类" :visible.sync="dialogFormVisible2" center>
				<!-- <div style="margin-bottom: 10px;display: flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="imageUrl" :src="imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</div> -->
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
					<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
					<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
					<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
				</div> -->
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="addNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改群分类弹框 -->
			<el-dialog title="修改群分类" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<!-- <el-form-item label="图片：" :label-width="formLabelWidth">
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="imageUrl"
								action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
								:on-success="handleAvatarSuccess2">
								<img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</el-form-item>
					<el-form-item label="链接：" :label-width="formLabelWidth">
						<el-input v-model="form.url" style="width:65%;"></el-input>
					</el-form-item> -->
					<el-form-item label="名称：" :label-width="formLabelWidth">
						<el-input v-model="form.name" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input v-model="form.sort" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
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
				statusT:'',
				isEnableT:'',
				status: '',
				cityT: '',
				screen: '',
				statesnum2: [{
						value: 1,
						label: '省级群'
					},
					{
						value: 2,
						label: '市级群'
					},
					{
						value: 3,
						label: '全国群'
					}
				],
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '否'
					},
					{
						value: 1,
						label: '是'
					},
				],
				statesnumS: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '待审核'
					},
					{
						value: 2,
						label: '审核成功'
					},
					{
						value: 3,
						label: '审核未通过'
					},
				],

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
				groupId: '', //群id
				sysUserId: '', //平台用户id
				sysUserName: '', //平台用户名
				groupName: '', //群名称
				groupType: 1, //群分类 1省级群 2市级群 3全国群
				isEnable: 1, //是否启用 0不启用 1启用
				sort: '', //排序
				peopleCount: '', //群人数
				groupImg: '', //加群二维码
				groupTitleImg: [], //群封面图
				groupContentImg:[],//群详情图
				countMoney: '', //加群所需点数

				percentage1: 0,
				percentage4:0,
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
				formLabelWidth: '200px',
				dialogFormVisible2: false,
				dialogFormVisible1: false,
				form: {
					id: '',
					name: '',
					imageUrl: '',
					url: '',
					classify: '',
					createTime: '',
					sort: '',
					state: '',
					describes: ''

				},
				classify: 20,
				openValue: 1,
				closeValue: 2,
				name: '',
				imageUrl: '',
				url: '',
				state: -1,
				describes: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				tableDataQfl: {},
				rewardPoint:'',
				groupContent:'',
				
				id: "",
				companyCardId: 1, //公司名片id
				templateName: "", //模板名称
				backImg: "", //背景图片
				companyLogo: "", //公司logo图片
				userName: "", //用户名称
				companyName: "", //公司名称
				companyuPisition: "", //职务名称
				phone: "", //手机号码
				email: "", //邮箱
				companyAddress: "", //公司地址
				companyDesc: "", //公司简介
				remark: "" //备注

			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			change(row) {
				console.log('row------', row)
				this.$http({
					url: this.$http.adornUrl('admin/sourceGroup/saveSourceGroup'),
					method: 'post',
					data: this.$http.adornData({
						'groupId': row.groupId,
						// 'sysUserId': this.sysUserId,
						// 'sysUserName': this.sysUserName,
						'groupName': row.groupName,
						'groupType': row.groupType,
						'isEnable': row.isEnable,
						'sort': row.sort,
						'peopleCount': row.peopleCount,
						'groupImg': row.groupImg,
						'groupTitleImg': row.groupTitleImg,
						'countMoney': row.countMoney,
						'rewardPoint':row.rewardPoint,
						'groupContentImg':row.groupContentImg,
						'groupContent':row.groupContent,
						
					}, false, 'from')
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
				if (tab._props.label === '模版列表') {
					this.page = 1
					this.limit = 10
					this.status = ''
					this.dataSelect()
				}
				if (tab._props.label === '群分类') {
					this.page = 1
					this.limit = 10
					this.dataSelectQfl()
				}

			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 详情跳转
			updates(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				}

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
				this.status = ''
				this.rewardPoint = 0
				this.helpTakeId = rows.groupId
				this.content = ''
				this.countMoney = rows.countMoney
				this.dialogFormVisible = true
			},
			refuseto() {
				if (this.status == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					})
					return
				}
				if(this.status ==2){
					if (this.rewardPoint === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入奖励积分数',
							type: 'warning'
						})
						return
					}
					if (Number(this.rewardPoint)<0 ) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '奖励积分数必须大于或者等于0',
							type: 'warning'
						})
						return
					}
					// if (this.countMoney === '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请输入加群所需点数',
					// 		type: 'warning'
					// 	})
					// 	return
					// }
					// if (Number(this.countMoney)<0 ) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '加群所需点数必须大于或者等于0',
					// 		type: 'warning'
					// 	})
					// 	return
					// }
					
				}
				if(this.status ==3){
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						})
						return
					}
				}
				
				this.$http({
					url: this.$http.adornUrl('admin/sourceGroup/auditScoreGroup'),
					method: 'post',
					params: this.$http.adornParams({
						'status': this.status,
						'groupIds': this.helpTakeId,
						'auditContent': this.content,
						'countMoney':this.countMoney,
						'rewardPoint':this.rewardPoint,
						
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
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
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
				
			},
			// 重置
			cleans() {
				this.isEnableT = ''
				this.dataTitleT = ''
				this.cityT = ''
				this.userNameT = ''
				this.phoneT = ''
				this.statusT = ''
				this.screen = ''
				this.dataSelect()
				this.dataSelectQflList()
			},

			// 获取服务数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('card/template/query'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'pageNum': this.page,
						'pageSize': this.limit,
						'phone': this.dataTitleT,
						'email': this.screen,
						// 'isEnable': this.isEnableT,
						'templateName': this.cityT,
						'userName': this.userNameT,
						// 'status':this.statusT,

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
					this.titles = '添加'
					
					

					this.companyCardId = ''
					this.sysUserId = ''
					this.sysUserName = ''
					this.templateName = ''
					this.backImg = []
					this.companyLogo = ''
					this.companyName = ''
					this.companyuPisition = ''
					this.phone = ''
					this.email = ''
					this.companyAddress = ''
					this.companyDesc = ''
					this.remark = ''
					this.id = ''

				} else {
					this.titles = '修改'
					this.id = row.id
					this.companyCardId = row.companyCardId
					this.sysUserId = row.userId
					this.sysUserName = row.userName
					this.templateName = row.templateName
					if(row.backImg){
						this.backImg = row.backImg.split(',')
					}else{
						this.backImg = []
					}
					
					this.companyLogo = row.companyLogo
					this.companyName = row.companyName
					this.peopleCount = row.peopleCount
					this.companyuPisition = row.companyuPisition
					this.phone = row.phone
					this.email = row.email
					this.companyAddress = row.companyAddress
					this.companyDesc = row.companyDesc
					this.remark = row.remark

				}
				// this.dataSelectQflList()
				this.dialogFormVisibleR = true
			},
			// 确认 添加、修改
			refusetoUp() {
				// if (this.groupType === ''||this.groupType ==null) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择群分类',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.groupName == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入群名称',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.sort === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入排序',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.groupImg == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传加群二维码',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.groupTitleImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传群封面图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.groupContentImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传群详情图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				
				// if (this.peopleCount === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入群人数',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// // if (this.countMoney === '') {
				// // 	this.$notify({
				// // 		title: '提示',
				// // 		duration: 1800,
				// // 		message: '请输入加群所需点数',
				// // 		type: 'warning'
				// // 	})
				// // 	return
				// // }
				// if (this.rewardPoint === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入奖励积分数',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				var url = ''
				if(this.titles == '添加'){
					url = 'card/template/save'
				}else{
					url = 'card/template/update'
				}


				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'companyCardId': this.companyCardId,
						'userId': this.sysUserId,
						'userName': this.sysUserName,
						'templateName': this.templateName,
						'backImg': this.backImg.toString(),
						'companyLogo': this.companyLogo,
						'companyName': this.companyName,
						'companyuPisition': this.companyuPisition,
						'phone': this.phone,
						'email': this.email,
						'companyAddress': this.companyAddress,
						'companyDesc':this.companyDesc,
						'remark':this.remark,
						'id':this.id,
						
											

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
				this.companyLogo = file.data
			},
			//上传轮播图
			handleUploadSuccess3(file, fileList) {
				this.backImg.push(file.data)
			},
			onprogress1(event, file, fileList) {
				this.percentage1 = parseInt(event.percent)
			},
			// 删除轮播图
			clear1(index) {
				this.backImg.splice(index, 1);
			},
			onprogress4(event, file, fileList) {
				this.percentage4 = parseInt(event.percent)
			},
			//上传轮播图
			handleUploadSuccess4(file, fileList) {
				this.groupContentImg.push(file.data)
			},
			// 删除轮播图
			clear4(index) {
				this.groupContentImg.splice(index, 1);
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
				this.sysUserId = row.userId
				this.sysUserName = row.userName
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
			updateBansJ(row) {
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
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = ''
				}
				this.dataSelect()
			},
			// 删除
			deleteBtn(row) {
				this.$confirm('确定删除该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('card/template/delete'),
						method: 'post',
						params: this.$http.adornParams({
							'id': row.id
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
			},
			//添加任务分类
			addNotice2() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.sort = ''
				this.dialogFormVisible2 = true
			},
			// 添加添加任务分类
			addNoticeTo2() {
				
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				if (this.sort === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'type': this.types,
						'sort':this.sort,
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

								this.dataSelectQfl()
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
			// 修改
			amendBanner( rows) {
				this.dialogFormVisible1 = true
				this.form.id = rows.id
				this.form.imageUrl = rows.imageUrl
				this.form.url = rows.url
				this.form.sort = rows.sort
				this.form.state = rows.state
				this.form.name = rows.name
				this.form.describes = rows.describes
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						'banner/updateBannerById'
					),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'createTime': this.info.stockDate,
						'imageUrl': this.form.imageUrl,
						'url': this.form.url,
						'state': this.form.state,
						'sort': this.form.sort,
						'name': this.form.name,
						'describes': this.form.describes,
						'type': this.types
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectQfl()
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
			// 删除群分类
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`banner/deleteBannerById?ids=${delid}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectQfl()
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
			// 获群分类取数据列表
			dataSelectQfl() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': -1,
						'classify': this.classify,
						'type': ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableDataQfl = returnData
					}

				})
			},
			// 是否启用
			changeQ(val, row) {
				this.$http({
					url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
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
							this.dataSelectQfl()
						}
					})
				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.imageUrl = file.data
			},
			handleAvatarSuccess2(file2) {
				this.form.imageUrl = file2.data
				console.log('file2.data', file2.data)
			},
			handleSizeChangeQfl(val) {
				this.limit = val
				this.dataSelectQfl()
			},
			handleCurrentChangeQfl(val) {
				this.page = val
				this.dataSelectQfl()
			},
			dataSelectQflList() {
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': -1,
						'limit': 20,
						'state': 1,
						'classify': 20,
						'type': ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						let returnData = data.data
						this.statesnum2 = returnData.list
					}
			
				})
			},
			// 复制 功能
			copyBtn(row){
				this.$confirm(`确定复制此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/sourceGroup/saveSourceGroup'),
						method: 'post',
						data: this.$http.adornData({
							// 'sysUserId': this.sysUserId,
							// 'sysUserName': this.sysUserName,
							'groupName': row.groupName,
							'groupType': row.groupType,
							'isEnable': row.isEnable,
							'sort': row.sort,
							'peopleCount': row.peopleCount,
							'groupImg': row.groupImg,
							'groupTitleImg': row.groupTitleImg,
							'countMoney': row.countMoney,
							'rewardPoint':row.rewardPoint,
							'groupContentImg':row.groupContentImg,
							'groupContent':row.groupContent,
						
						}, false, 'from')
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
								onClose: () => {}
							})
						}
				
					})
				}).catch(() => {})
			}
		},
		mounted() {
			if (this.$route.query.status) {
				this.statusT = Number(this.$route.query.status)
			} else {
				this.statusT = ''
			}
			//模版列表
			this.dataSelect()
			// this.dataSelectQflList()
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