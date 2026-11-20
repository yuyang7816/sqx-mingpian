<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">企业管理</span>
			<h2 class="nf-page-hero__title">企业管理</h2>
			<p class="nf-page-hero__desc">审核企业认证申请，支持按昵称、法人姓名、手机号与状态筛选，可审核通过或拒绝及查看成员与动态</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">昵称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入昵称"
						v-model="userNameT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">法人姓名</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入法人姓名"
						v-model="userName"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">手机号</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="status" class="nf-filter-form__input" @change="select()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="企业列表" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userEntity" label="昵称" width="100">
						<template slot-scope="scope">
							<span class="nf-table-link" v-if="scope.row.userEntity"
								@click="updates(scope.row)">{{scope.row.userEntity.userName}}</span>
						</template>
					</el-table-column>
					<el-table-column fixed prop="realName" label="法人姓名" width="100">
						<template slot-scope="scope">
							<span class="nf-table-link"
								@click="updates(scope.row)">{{scope.row.realName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="identityCardNumber" label="法人身份证" width="200"></el-table-column>
					<el-table-column prop="enterprisePhone" label="认证手机号" width="150"></el-table-column>
					<el-table-column prop="enterpriseName" label="企业名称" width="150"></el-table-column>
					<el-table-column label="注册资本" prop="registeredCapital">
					</el-table-column>
					<el-table-column prop="companyPost" label="公司职务" width="150"></el-table-column>
					<el-table-column prop="businessLicense" label="营业执照注册号" width="150"></el-table-column>
					<el-table-column prop="businessLicenseImg" label="营业执照" width="150">
						<template slot-scope="scope">
								<div v-if="scope.row.businessLicenseImg">
									<div v-for="(item,index) in scope.row.businessLicenseImg.split(',')" :key="index"
										style="display: inline-block; margin: 3px;">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
											<img style="width: 300px; height: auto" :src="item" alt="">
										</el-popover>
									</div>
								</div>
						</template>
					</el-table-column>
					<el-table-column prop="enterpriseAddress" label="企业地址" width="150"></el-table-column>
					<el-table-column prop="remark" label="审核原因" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.remark?scope.row.remark:'暂无'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<span style="color: green;" v-if="scope.row.status==0">审核中</span>
							<span style="color: green;" v-if="scope.row.status==1">已通过</span>
							<span style="color: red;" v-if="scope.row.status==2">已拒绝</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary"
								:disabled="!isAuth('authentication:pass') || scope.row.status!==0"
								@click="tonguo(scope.row)"  style="margin: 5px;">通过</el-button>
							<el-button size="mini" type="danger"
								:disabled="!isAuth('authentication:refuse') || scope.row.status!==0"
								@click="deleteuser(scope.row)"  style="margin: 5px;">拒绝</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('authentication:update')"
								@click="updateClicks(scope.row)" style="margin: 5px;" plain>
								修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('authentication:delete')"
								@click="deletes(scope.row)"  style="margin: 5px;" plain>删除</el-button>
							<el-button size="mini" type="primary" 
								@click="chengyuanBtn(scope.row)"  style="margin: 5px;">成员</el-button>
							<el-button size="mini" type="warning" 
								@click="dongtaiBtn(scope.row)"  style="margin: 5px;">动态</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
		</el-tabs>
		<el-dialog title="审核拒绝" :visible.sync="dialogVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">操作：</span>
				<el-radio-group v-model="isSubmit">
					<el-radio :label="0">下次可再次提交</el-radio>
					<el-radio :label="1">下次不可再次提交</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 54px;">审核原因：</span>
				<el-input type="textarea" :rows="3" placeholder="请输入审核原因" v-model="remark"
					style="width:50%;display: inline-block;">
				</el-input>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="jujue()">确 定</el-button>
			</span>
		</el-dialog>
		<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleR" center width="80%">
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">认证类型：</span>
				<el-radio-group v-model="userType">
					<el-radio :label="1">个人认证</el-radio>
					<el-radio :label="2">企业认证</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<el-input style="width:50%;" v-model="realName" type="text" placeholder="请输入姓名"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">电话：</span>
				<el-input style="width:50%;" v-model="enterprisePhone" type="text" placeholder="请输入电话"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
				<el-radio-group v-model="sex">
					<el-radio :label="'1'">男</el-radio>
					<el-radio :label="'2'">女</el-radio>
				</el-radio-group>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">生日：</span>
				<el-date-picker style="width: 50%;" v-model="birth" align="right" type="date" format="yyyy年MM月dd日"
					value-format="yyyy年MM月dd日" placeholder="选择日期">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
				<el-input style="width:50%;" v-model="identityCardNumber" type="text" placeholder="请输入身份证号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">认证地址：</span>
				<el-input style="width:50%;" v-model="enterpriseAddress" type="text" placeholder="请输入认证地址"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">认证备注：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入认证备注"></el-input>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="avatar"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess5">
						<img v-if="avatar" :src="avatar" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div> -->
			<div style="margin-bottom: 10px;" v-if="userType == 1">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证正面图：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="identityCardFront"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="identityCardFront" :src="identityCardFront" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;" v-if="userType == 1">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证反面图：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="identityCardVerso"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="identityCardVerso" :src="identityCardVerso" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>

			<div style="margin-bottom: 10px;" v-if="userType == 2">
				<span style="width: 200px;display: inline-block;text-align: right;">企业名称：</span>
				<!-- <el-input style="width:50%;" v-model="enterpriseName" type="text" placeholder="请输入企业名称"></el-input> -->
				<el-select v-model="enterpriseName" style="width:50%;" filterable remote :remote-method="remoteMethod"
					:loading="loading" @change="companyBtn()">
					<el-option v-for="(item,index) in nczData" :key="index" :label="item.companyName"
						:value="item.companyName">
						<span style="display: inline-block;float: left">{{item.companyName}}</span>
						<span style="color: #999;float: right;">{{item.creditCode}}<span
								style="color: #999;margin-left: 10px;"
								v-if="item.regCapital">(注册资本：{{item.regCapital}})</span></span>
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;" v-if="userType == 2">
				<span style="width: 200px;display: inline-block;text-align: right;">注册资本(单位:万元)：</span>
				<el-input style="width:50%;" v-model="registeredCapital" type="text"
					placeholder="请输入注册资本(单位:万元)"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="userType == 2">
				<span style="width: 200px;display: inline-block;text-align: right;">公司职务：</span>
				<el-input style="width:50%;" v-model="companyPost" type="text" placeholder="请输入公司职务"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="userType == 2">
				<span style="width: 200px;display: inline-block;text-align: right;">营业执照注册号：</span>
				<el-input style="width:50%;" v-model="businessLicense" type="text" placeholder="请输入营业执照注册号"></el-input>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;" v-if="userType == 2">
				<span style="width: 200px;display: inline-block;text-align: right;">营业执照：</span>
				<div style="display: flex;align-items: center;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in businessLicenseImg" :key="index" style="margin: 3px;">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels(index)"></i>
						</span>
					</div>
					<div class="imgs" style="">
						<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" list-type="picture-card"
							:show-file-list="false" :on-success="handleRemove">
							<i class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>

			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleR = false">取 消</el-button>
				<el-button type="primary" @click="refusetoUp()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 企业成员 -->
		<el-dialog title="企业成员" :visible.sync="dialogFormVisibleXy" center width="80%">
			<div style="margin:5px;display: inline-block;">
				<span>状态：</span>
				<el-select v-model="statusCy" style="width:150px;margin-left: 10px;" @change="selectCy()">
					<el-option v-for="item in statesnumCy" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;
			</div>
			<el-button type="primary" @click="selectCy()" size="mini">查询</el-button>
			<el-table v-loading="tableDataLoadingCy" :data="tableDataCy.records">
				<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
				<el-table-column fixed prop="userEntity" label="用户昵称">
					<template slot-scope="scope">
						<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.userEntity"
							@click="updates(scope.row)">{{scope.row.userEntity.userName}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed prop="realName" label="成员姓名">
					<template slot-scope="scope">
						<span style="color: #2f3968;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.realName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="enterprisePhone" label="手机号" width="150"></el-table-column>
				<el-table-column prop="identityCardNumber" label="职位"></el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<span style="color: green;" v-if="scope.row.status==0">审核中</span>
						<span style="color: green;" v-if="scope.row.status==1">已通过</span>
						<span style="color: red;" v-if="scope.row.status==2">已拒绝</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary"
							:disabled="!isAuth('authentication:pass') || scope.row.status!==0"
							@click="chengyuanShenhe(scope.row)"  style="margin: 5px;">审核</el-button>
						<el-button size="mini" type="danger" v-if="scope.row.status==1"
							@click="tuichuBtnYg(scope.row)"  style="margin: 5px;">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataCy.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 企业成员动态 -->
		<el-dialog title="企业成员动态" :visible.sync="dialogFormVisibleCyDt" center width="80%">
			<el-table v-loading="tableDataLoadingCy" :data="tableDataCy.records">
				<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
				<el-table-column fixed prop="userEntity" label="用户昵称">
					<template slot-scope="scope">
						<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.userEntity"
							@click="updates(scope.row)">{{scope.row.userEntity.userName}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed prop="realName" label="成员姓名">
					<template slot-scope="scope">
						<span style="color: #2f3968;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.realName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="enterprisePhone" label="手机号" width="150"></el-table-column>
				<el-table-column prop="identityCardNumber" label="职位"></el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<span style="color: green;" v-if="scope.row.status==0">审核中</span>
						<span style="color: green;" v-if="scope.row.status==1">已通过</span>
						<span style="color: red;" v-if="scope.row.status==2">已拒绝</span>
						<span style="color: red;" v-if="scope.row.status==3">退出</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status==0"
							@click="shenheCy(scope.row,1)"  style="margin: 5px;">通过</el-button>
						<el-button size="mini" type="danger" v-if="scope.row.status==0"
							@click="shenheCy(scope.row,2)"  style="margin: 5px;">拒绝</el-button>
						<el-button size="mini" type="danger" v-if="scope.row.status==1"
							@click="tuichuBtn(scope.row)"  style="margin: 5px;">退出</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataCy.total">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog title="审核成员" :visible.sync="dialogVisibleCy" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">操作：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">通过</el-radio>
					<el-radio :label="2">拒绝</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 54px;">审核原因：</span>
				<el-input type="textarea" :rows="3" placeholder="请输入审核原因" v-model="remark"
					style="width:50%;display: inline-block;">
				</el-input>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisibleCy = false">取 消</el-button>
				<el-button type="primary" @click="jujueCh()">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				remark: '',
				dialogVisible: false,
				userName: '',
				userNameT:'',
				phone: '',
				limit: 10,
				page: 1,
				activeName: 'first',
				tableDataLoading: true,
				tableData: {},
				id: '',
				userTypeT: 2,
				// ````
				userType: '',
				classifyIds: '',
				enterprisePhone: '',
				realName: '',
				sex: '',
				identityCardNumber: '',
				identityCardFront: '',
				identityCardVerso: '',
				enterpriseAddress: '',
				businessLicense: '',
				enterpriseName: '',
				companyPost:'',
				avatar: '',
				address: '',
				userId: '',
				dialogFormVisibleR: false,
				userIds: '',
				percentage1: 0,
				businessLicenseImg: [],
				imagePhoto: [],
				status:'',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					},{
						value: 1,
						label: '已通过'
					}, {
						value: 2,
						label: '已拒绝'
					}
				],
				// info: {
				// 	stockDate: this.getNowTime(), //日期
				// },
				isSubmit:0,
				nczData: [],
				loading: false,
				registeredCapital:'',
				dialogFormVisibleXy:false,
				dialogFormVisibleCyDt:false,
				tableDataLoadingCy:false,
				limit1: 10,
				page1: 1,
				tableDataCy:{},
				statusCy:'',
				statesnumCy: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					},{
						value: 1,
						label: '已通过'
					}, {
						value: 2,
						label: '已拒绝'
					}
				],
				dialogVisibleCy:false,
			}
		},
		methods: {
			//切换认证类型数据 //first 个人认证  second 企业认证
			handleClick(tab, event) {
				this.page = 1
				this.userName = ''
				this.userNameT = ''
				this.phone = ''
				if (tab._props.label == '企业列表') {
					this.userTypeT = 2
						this.dataSelect()
				}
			},
			//拒绝审核
			jujue() {
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入审核原因',
						type: 'warning'
					})
					return
				}
				if (this.remark.length > 100) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '审核理由最多可输入100字',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('userCertification/auditorUserCertification'),
					method: 'post',
					data: this.$http.adornData({
						'status': 2,
						'id': this.id,
						'remark': this.remark,
						'isSubmit':this.isSubmit
					}, false, 'form')
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							type: 'success',
							message: '审核成功',
							duration: 1500,
							onClose: () => {
								this.dialogVisible = false
								this.dataSelect()
							}
						});


					} else {
						this.$message.error(data.msg);
						// this.dataSelect()
					}
				})
			},
			//拒绝审核弹窗
			deleteuser(item) {
				this.id = item.id
				this.remark = ''
				this.dialogVisible = true
			},
			//通过审核
			tonguo(item) {
				this.$confirm('确认通过审核吗?', '提示', {
					confirmButtonText: '确认',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userCertification/auditorUserCertification'),
						method: 'post',
						data: this.$http.adornData({
							'status': 1,
							'id': item.id,
							'remark': '',

						}, false, 'form')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								type: 'success',
								message: '审核成功'
							});
							this.dataSelect()

						} else {
							this.$message.error(data.msg);
							this.dataSelect()
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消审核'
					});
				});
			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit1 = val
				this.dataSelectCy()
			},
			handleCurrentChange1(val) {
				this.page1 = val
				this.dataSelectCy()
			},
			handleSizeChange2(val) {
				this.limit1 = val
				this.dataSelectCyDt()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.dataSelectCyDt()
			},
			//重置
			cleans() {
				this.page = 1
				this.limit = 10
				this.userName = ''
				this.userNameT = ''
				this.phone = ''
				this.status = ''
				this.dataSelect()
			},
			//查询
			select() {
				this.dataSelect()
			},
			
			// 获取认证数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userType': this.userTypeT,
						'realName': this.userName,
						'enterprisePhone': this.phone,
						'status':this.status,
						'userName':this.userNameT,
					})
				}).then(({
					data
				}) => {
					// console.log('---',data,this.tableDataLoading)
					this.tableDataLoading = false

					let returnData = data.data
					this.tableData = returnData
					console.log('---',this.tableData)
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
			updateClicks(row) {
				this.ids = row.id
				if (row.businessLicenseImg) {
					this.businessLicenseImg = row.businessLicenseImg.split(',')
				}
				this.userType = row.userType
				this.realName = row.realName
				this.enterprisePhone = row.enterprisePhone
				this.identityCardNumber = row.identityCardNumber
				this.enterpriseAddress = row.enterpriseAddress
				this.identityCardFront = row.identityCardFront
				this.identityCardVerso = row.identityCardVerso
				this.remark = row.remark
				this.businessLicense = row.businessLicense
				this.enterpriseName = row.enterpriseName
				this.companyPost = row.companyPost
				this.userId = row.userId
				this.registeredCapital = row.registeredCapital
				this.dialogFormVisibleR = true
			},
			handleAvatarSuccess1(file, fileList) {
				this.identityCardFront = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.identityCardVerso = file.data
			},
			// 轮播图片上传
			handleRemove(file) {
				this.businessLicenseImg.push(file.data);
			},
			handleDownload(file) {

				console.log(file, this.img)
			},
			// 删除轮播图
			dels(index) {
				this.businessLicenseImg.splice(index, 1);
				console.log(this.img)
			},
			refusetoUp() {
				if (this.realName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入姓名',
						type: 'warning'
					})
					return
				}
				if (this.enterprisePhone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入电话',
						type: 'warning'
					})
					return
				}
				if (this.identityCardNumber == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入身份证号',
						type: 'warning'
					})
					return
				}
				if (this.enterpriseAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入认证地址',
						type: 'warning'
					})
					return
				}

				if (this.userType == 1) {
					if (this.identityCardFront == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请上传身份证正面照',
							type: 'warning'
						})
						return
					}
					if (this.identityCardVerso == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请上传身份证背面照',
							type: 'warning'
						})
						return
					}
					this.enterpriseName = ''
					this.companyPost = ''
					this.businessLicense = ''
					this.businessLicenseImg = []
				} else {
					if (this.enterpriseName == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入企业名称',
							type: 'warning'
						})
						return
					}
					if (this.companyPost == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入公司职务',
							type: 'warning'
						})
						return
					}
					
					if (this.businessLicense == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入营业执照注册号',
							type: 'warning'
						})
						return
					}
					if (this.businessLicenseImg.length == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请上传营业执照',
							type: 'warning'
						})
						return
					}
					this.identityCardFront = ''
					this.identityCardVerso = ''

				}
				this.$http({
					url: this.$http.adornUrl('userCertification/updateUserCertification'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						'realName': this.realName,
						'remark': this.remark,
						'enterpriseAddress': this.enterpriseAddress,
						'businessLicense': this.businessLicense,
						'businessLicenseImg': this.businessLicenseImg.toString(),
						'enterprisePhone': this.enterprisePhone,
						'identityCardNumber': this.identityCardNumber,
						'identityCardFront': this.identityCardFront,
						'userType': this.userType,
						'identityCardVerso': this.identityCardVerso,
						'enterpriseName': this.enterpriseName,
						'id': this.ids,
						'companyPost':this.companyPost,
						'registeredCapital':this.registeredCapital,
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
								this.dialogFormVisibleR = false
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
			//删除
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userCertification/deleteCertification?id=' + delid),
						method: 'post',
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
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			dataSelect1(){
				if( this.$route.query.status!=null){
					this.status = this.$route.query.status
				}else{
					this.status =''
				}
				this.dataSelect()
			},
			remoteMethod(query) {
				console.log('query', query)
				if (query != '') {
					this.loading = true;
					setTimeout(() => {
						
						this.$http({
							url: this.$http.adornUrl('admin/businessCard/selectSearchCompanyName'),
							method: 'get',
							params: this.$http.adornParams({
								'companyName': query
							})
						}).then(({
							data
						}) => {
							this.loading = false;
							if (data.data.code == 0) {
								let returnData = data.data
								this.nczData = returnData.data.items
							} else {
								this.nczData = []
							}
			
						})
					}, 200);
				} else {
					this.nczData = [];
				}
			},
			companyBtn(){
				console.log(this.nczData,'------',this.enterpriseName)
				if(this.nczData.length>0){
					for(var i in this.nczData){
						if(this.enterpriseName==this.nczData[i].companyName){
							this.registeredCapital = this.nczData[i].regCapital
						}
					}
				}
			},
			// 成员 弹框
			chengyuanBtn(row){
				this.ids = row.id
				this.page1 = 1
				this.dataSelectCy()
				this.dialogFormVisibleXy = true
			},
			// 查询
			selectCy(){
				this.page1 = 1
				this.dataSelectCy()
			},
			dataSelectCy(){
				this.tableDataLoadingCy = true
				this.$http({
					url: this.$http.adornUrl('admin/comRecord/page'),
					method: 'get',
					params: this.$http.adornParams({
						'pageNum': this.page1,
						'pageSize': this.limit1,
						'cardId': this.ids,
						'status':this.statusCy,
						// 'realName': this.userName,
						// 'enterprisePhone': this.phone,
						// 'status':this.status,
						// 'userName':this.userNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingCy = false
					let returnData = data.data
					this.tableDataCy = returnData
					console.log('---',this.tableDataCy)
				})
			},
			// 审核成员
			shenheCy(row,index){
				var text =''
				if(index==1){
					text = '确定审核通过？'
				}else{
					text = '确定审核拒绝？'
				}
				this.$confirm(index, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/comRecord/auditApply'),
						method: 'post',
						data: this.$http.adornData({
							
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
				})
			},
			// 动态 弹框
			dongtaiBtn(row){
				this.ids = row.id
				this.dataSelectCyDt()
				this.dialogFormVisibleCyDt = true
			},  
			dataSelectCyDt(){
				this.tableDataLoadingCy = true
				this.$http({
					url: this.$http.adornUrl('admin/cDynamic/page'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'pageNum': this.page1,
						'pageSize': this.limit1,
						'cardId': this.ids,
						// 'realName': this.userName,
						// 'enterprisePhone': this.phone,
						// 'status':this.status,
						// 'userName':this.userNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingCy = false
					let returnData = data.data
					this.tableDataCy = returnData
					console.log('---',this.tableDataCy)
				})
			},
			// 成员审核 弹框
			chengyuanShenhe(row){
				this.id = item.recordId
				this.remark = ''
				this.status = 1
				this.dialogVisibleCy = true
			},
			//拒绝审核
			jujueCh() {
				if (this.status == 2) {
					if (this.remark == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入审核原因',
							type: 'warning'
						})
						return
					}
					if (this.remark.length > 100) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '审核理由最多可输入100字',
							type: 'warning'
						})
						return
					}
				}
				
				this.$http({
					url: this.$http.adornUrl('userCertification/auditorUserCertification'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'recordId': this.id,
						'content': this.remark,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							type: 'success',
							message: '审核成功',
							duration: 1500,
							onClose: () => {
								for(var i in this.tableDataCy.records){
									if(this.id==this.tableDataCy.records[i].recordId){
										this.tableDataCy.records[i].status = this.status
									}
								}
								this.dialogVisible = false
								this.dataSelect()
							}
						});
			
			
					} else {
						this.$message.error(data.msg);
						// this.dataSelect()
					}
				})
			},
			// 删除员工
			tuichuBtnYg(row){
				let delid = row.id
				this.$confirm(`确定删除员工?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/comRecord/delStaffCard?cardId=' + row.cardId+'&staffId='+row.staffId),
						method: 'get',
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									for(var i in this.tableDataCy.records){
										if(row.staffId==this.tableDataCy.records[i].staffId){
											this.tableDataCy.records.splice(i,1)
										}
									}
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
				})
			}
		},
		mounted() {
			
			if(this.$route.query.status!=null){
				this.status = Number(this.$route.query.status)
			}else{
				this.status =''
			}
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

	.el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
		border-color: #2f3968;
		width: 148px;
		height: 148px;
	}

	.el-upload:hover {
		border-color: #2f3968;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 148px;
		height: 148px;
		line-height: 148px;
		text-align: center;
	}

	.avatar {
		width: 148px;
		height: 148px;
		display: block;
	}

/* 	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	} */

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
