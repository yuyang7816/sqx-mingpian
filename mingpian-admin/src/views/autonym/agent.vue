<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">代理管理</span>
			<h2 class="nf-page-hero__title">代理申请</h2>
			<p class="nf-page-hero__desc">审核代理商入驻申请，支持按昵称、电话、姓名、任职公司与状态筛选查询</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">昵称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入昵称"
						v-model="userNameT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">电话</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入电话"
						v-model="phoneT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">姓名</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入姓名"
						v-model="realNameT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">任职公司</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入任职公司"
						v-model="idNumberT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">邀请码</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入邀请码"
						v-model="inviterCodeT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="statusT" class="nf-filter-form__input" @change="phoneSelect()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">性别</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入性别"
						v-model="sexT"></el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column fixed prop="authId" label="编号" width="50">
					</el-table-column>
					<el-table-column prop="userName" label="昵称" width="160" fixed>
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updatesvideo(scope.row)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
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
					<el-table-column prop="realName" label="姓名">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updatesvideo(scope.row)"
								v-text="scope.row.realName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="电话" width="160">
					</el-table-column>
					<el-table-column prop="idNumber" label="任职公司" width="160">
					</el-table-column>
					<el-table-column label="注册资本" prop="registeredCapital">
					</el-table-column>
					<el-table-column prop="positionName" label="岗位名称" width="160">
					</el-table-column>
					<el-table-column prop="purchaseName" label="采购物料类型" width="160">
					</el-table-column>
					<el-table-column prop="idFrontImg" label="资质证明文件图片">
						<template slot-scope="scope">
							<div v-if="scope.row.idFrontImg == null || scope.row.idFrontImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.idFrontImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.idFrontImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.idFrontImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="idSideImg" label="其他证明图片">
						<template slot-scope="scope">
							<div
								v-if="scope.row.idSideImg == null ||scope.row.idSideImg == 'null' || scope.row.idSideImg == ''">
								暂无图片
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.idSideImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.idSideImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="inviterCode" label="邀请码">
          </el-table-column>
          <el-table-column prop="sex" label="性别">
          </el-table-column> -->
					<el-table-column prop="address" label="地址" width="250">
						<template slot-scope="scope">
							　{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="examineTime" label="审核时间" width="160">
					</el-table-column>
					<el-table-column prop="examineRemark" label="审核备注" width="200">
					</el-table-column>
					<el-table-column prop="status" label="状态" fixed="right">
						<template slot-scope="scope">
							<span v-if="scope.row.status===0">待审核</span>
							<span v-if="scope.row.status==1">通过</span>
							<span v-if="scope.row.status==2">拒绝</span>
							<span v-if="scope.row.status==3">会员到期已下架</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="操作" fixed="right">
						<template slot-scope="scope"> <!-- || !isAuth('agent:jujue') -->
							<el-button size="mini" type="primary"
								:disabled="scope.row.status !== 0 || !isAuth('agent:shenhe')"
								@click="refuseClick(scope.row)">
								审核
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('agent:delete')"
								@click="deleteBtn(scope.row)">
								删除
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('authentication:update')"
								@click="updateClicks(scope.row)">
								修改
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
		</div>
			<!-- </el-tab-pane> -->
			<!-- <el-tab-pane label="代理商收益排行榜" name="second">
        <div>
          <div style="position: relative;display: inline-block;">
            <span>姓名：</span>
            <el-input style="width: 200px;" @keydown.enter.native="phoneSelectP" placeholder="请输入姓名" v-model="userName">
            </el-input>&nbsp;&nbsp;
          </div>
          <div style="position: relative;display: inline-block;">
            <span>电话：</span>
            <el-input style="width: 200px;" @keydown.enter.native="phoneSelectP" placeholder="请输入电话" v-model="phone">
            </el-input>&nbsp;&nbsp;
          </div>
          <el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelectP">查询
          </el-button>
          <el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleansP">重置
          </el-button>&nbsp;&nbsp;
        </div>
        <el-table v-loading="tableDataLoading" :data="homeData.list">
          <el-table-column fixed prop="rankNum" label="排名" width="80">
            <template slot-scope="scope">
              <div>{{scope.$index+1}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="推广用户昵称" width="120">
            <template slot-scope="scope">
              <div style="color: #2f3968;cursor: pointer;" @click="updatesvideo(scope.row)">
                {{scope.row.userName?scope.row.userName:'未绑定'}}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号">
          </el-table-column>
          <el-table-column prop="sumMoney" label="总收益">
          </el-table-column>
          <el-table-column prop="artificerMoney" label="邀请助教收益">
          </el-table-column>
          <el-table-column prop="userMoney" label="邀请用户收益">
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
            :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
            layout="total,sizes, prev, pager, next,jumper" :total="homeData.totalCount">
          </el-pagination>
        </div>
      </el-tab-pane> -->
			<!-- 任务拒绝弹框 -->
			<el-dialog title="拒绝" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
					<el-radio-group v-model="status">
						<el-radio :label="1">通过</el-radio>
						<el-radio :label="2">拒绝</el-radio>
					</el-radio-group>
				</div>
				<!-- <div style="margin-bottom: 10px;" v-if="status==1">
          <span style="width: 200px;display: inline-block;text-align: right;">分成比例：</span>
          <el-input style="width:50%;" v-model="divideProp" type="text" placeholder="请输入分成比例">
          </el-input>
        </div> -->
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">审核理由：</span>
					<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入审核理由">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleR" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
					<el-input style="width:50%;" v-model="realName" type="text" placeholder="请输入姓名"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">电话：</span>
					<el-input style="width:50%;" v-model="enterprisePhone" type="text" placeholder="请输入电话"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">任职公司：</span>
					<!-- <el-input style="width:50%;" v-model="identityCardNumber" type="text"
						placeholder="请输入任职公司"></el-input> -->
					<el-select v-model="identityCardNumber" style="width:50%;" filterable remote :remote-method="remoteMethod"
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
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">注册资本(单位:万元)：</span>
					<el-input style="width:50%;" v-model="registeredCapital" type="text"
						placeholder="请输入注册资本(单位:万元)"></el-input>
				
				
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">岗位名称：</span>
					<el-select v-model="positionName" style="width:50%;">
						<el-option v-for="item in flxmData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购物料类型：</span>
					<el-input style="width:50%;" v-model="purchaseName" type="text" placeholder="请输入采购物料类型"></el-input>
					<!-- <el-select v-model="purchaseName" style="width:50%;">
						<el-option v-for="item in wllxData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select> -->
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">省市区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" filterable remote
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">地址：</span>
					<el-input style="width:50%;" v-model="enterpriseAddress" type="text"
						placeholder="请输入认证地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">资质证明文件图片：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="identityCardFront"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="identityCardFront" :src="identityCardFront" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">其他证明图片：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="identityCardVerso"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="identityCardVerso" :src="identityCardVerso" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleR = false">取 消</el-button>
					<el-button type="primary" @click="refusetoUp()">确 定</el-button>
				</div>
			</el-dialog>
		<!-- </el-tabs> -->
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
		components: {

		},
		data() {
			return {
				page: 1,
				limit: 10,
				tableDataLoading: true,
				tableDataLoading1: true,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				tableData: [],
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				title: '添加',
				activeName: 'first',
				classify: 1,
				userName: '',
				phone: '',
				homeData: {},
				userName: '',
				phone: '',
				content: '',
				divideProp: '',

				authId: '',
				userId: '', //用户id
				realName: "", //姓名
				idNumber: "", //身份证号
				positionName: '',
				purchaseName: '',
				phone: "", //联系电话
				province: "", //省
				city: "", //市
				district: "", //区
				address: "", //详细地址
				idFrontImg: "", //身份证正面照片
				idSideImg: "", //身份证反面照片
				inviterCode: '', //邀请码
				status: 1, //状态 0待审核 1审核成功 2审核未通过 3会员到期已下架
				examineRemark: "", //审核备注
				userName: "", //用户昵称
				avatar: "", //用户头像
				sex: 2, //性别 1男 2女

				userNameT: '',
				phoneT: '',
				realNameT: '',
				idNumberT: '',
				inviterCodeT: '',
				statusT: '',
				sexT: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					// {
					// 	value: 0,
					// 	label: '待提交'
					// },
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '通过'
					}, {
						value: 2,
						label: '拒绝'
					}, {
						value: 3,
						label: '会员到期已下架'
					}
				],

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
				avatar: '',
				address: '',
				userId: '',
				dialogFormVisibleR: false,
				userIds: '',
				percentage1: 0,
				businessLicenseImg: [],
				imagePhoto: [],
				province: '', //省
				city: '', //市
				district: '', //县
				storeAddress: [],
				storeAddre: '请选择城市',
				options: regionData,
				flxmData: [],
				wllxData: [],
				nczData: [],
				loading: false,
				registeredCapital:'',
			}
		},
		methods: {
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '代理商申请') {
					this.classify = 1
					this.dataSelect()
				}
				if (tab._props.label == '代理商收益排行榜') {
					this.homeSelect()
				}

			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/agentAuth/getAgentAuthList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'realName': this.realNameT,
						'idNumber': this.idNumberT,
						'inviterCode': this.inviterCodeT,
						'status': this.statusT,
						'sex': this.sexT,


					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('列表', data)
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			cleans() {
				this.page = 1
				this.userNameT = ''
				this.phoneT = ''
				this.realNameT = ''
				this.idNumberT = ''
				this.inviterCodeT = ''
				this.statusT = ''
				this.sexT = ''
				this.dataSelect()
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.memberImg = file.data
			},
			addNotice() {
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.homeSelect()
			},
			// 添加banner图
			addNoticeTo() {
				if (this.memberImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (this.memberName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				this.classify = 1
				if (this.title == '添加') {
					var url = 'member/insertMember'
				} else {
					var url = 'member/updateMember'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'memberImg': this.memberImg,
						'memberId': this.memberId,
						'memberName': this.memberName,
						'sort': this.sort
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
							this.memberImg = ''
							this.memberId = ''
							this.memberName = ''
							this.sort = ''
							this.title = '添加'
							this.dataSelect()
						}
					})
				})
			},
			// 修改
			amendBanner(index, rows, a) {
				if (a == 1) {
					this.pl = 1
				} else {
					this.pl = 0
				}
				this.dialogFormVisible = true
				this.id = rows.id
				this.memberImg = rows.memberImg
				this.memberId = rows.memberId
				this.sort = rows.sort
				this.memberName = rows.memberName
				this.title = '修改'
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						`?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`
					),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			//删除一级
			deletes(row) {
				let delid = row.memberId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`member/deleteMemberById/?memberId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},

			// 详情跳转
			updatesvideo(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			phoneSelectP() {
				this.page = 1
				this.homeSelect()
			},
			cleansP() {
				this.page = 1
				this.userName = ''
				this.phone = ''
				this.homeSelect()
			},
			// 获取排行数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectAgencyOrdersMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.userName,
						'phone': this.phone,
						'startTime': '',
						'endTime': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data

					this.homeData = data.data
				})
			},
			//单条任务通过
			passClicks(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('apply/auditApply'),
					method: 'post',
					params: this.$http.adornParams({
						'status': this.status,
						'ids': row.applyId,
						// 'remek': '通过'
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
			//任务拒绝
			refuseClick(rows) {
				this.status = ''
				this.divideProp = ''
				this.helpTakeId = rows.authId
				this.content = ''
				// this.refuseto()
				this.dialogFormVisible = true
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
				// if(this.status == 1){
				//   if (this.divideProp === '') {
				//   	this.$notify({
				//   		title: '提示',
				//   		duration: 1800,
				//   		message: '请输入分成比例',
				//   		type: 'warning'
				//   	});
				//   	return
				//   }
				// }
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

				this.$http({
					url: this.$http.adornUrl('admin/agentAuth/examineAgent'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'authId': this.helpTakeId,
						'divideProp': this.divideProp,
						'examineRemark': this.content,

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
							onClose: () => {
								this.content = ''
								this.status = ''
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
			// 删除
			deleteBtn(row) {
				this.$confirm('确定删除该信息吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/agentAuth/deleteAgentAuthById'),
						method: 'post',
						params: this.$http.adornParams({
							'authId': row.authId
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
			dataSelect1() {
				if (this.$route.query.status != null) {
					this.statusT = this.$route.query.status
				} else {
					this.statusT = ''
				}
				this.dataSelect()
			},
			updateClicks(row) {

				this.ids = row.authId

				this.realName = row.realName
				this.enterprisePhone = row.phone
				this.identityCardNumber = row.idNumber
				this.positionName = row.positionName
				this.purchaseName = row.purchaseName
				this.enterpriseAddress = row.address
				this.identityCardFront = row.idFrontImg
				if (row.idSideImg && row.idSideImg != 'null') {
					this.identityCardVerso = row.idSideImg
				} else {
					this.identityCardVerso = ''
				}

				this.province = row.province
				this.city = row.city
				this.district = row.district
				this.storeAddress = []
				this.storeAddre = this.province + '/' + this.city + '/' + this.district

				this.userId = row.userId
				this.registeredCapital = row.registeredCapital
				this.dialogFormVisibleR = true
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
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
						message: '请输入任职公司',
						type: 'warning'
					})
					return
				}
				if (this.positionName == '' || this.positionName == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择岗位名称',
						type: 'warning'
					})
					return
				}
				if (this.purchaseName == '' || this.purchaseName == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择采购物料类型',
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

				if (this.identityCardFront == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传资质证明文件图片',
						type: 'warning'
					})
					return
				}
				if (this.registeredCapital === ''||this.registeredCapital==null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '注册资本不能为空',
						type: 'warning'
					})
					return
				}
				

				this.$http({
					url: this.$http.adornUrl('admin/agentAuth/updateAgentAuth'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						'realName': this.realName,
						'remark': this.remark,
						'address': this.enterpriseAddress,
						'phone': this.enterprisePhone,
						'idNumber': this.identityCardNumber,
						'idFrontImg': this.identityCardFront,
						'userType': this.userType,
						'idSideImg': this.identityCardVerso,
						'authId': this.ids,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'positionName': this.positionName,
						'purchaseName': this.purchaseName,
						'registeredCapital':this.registeredCapital
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
			// 岗位名称
			flxmSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '岗位名称'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.flxmData = returnData
				})
				// this.$http({
				// 	url: this.$http.adornUrl('sys/dict/selectDictList'),
				// 	method: 'get',
				// 	params: this.$http.adornParams({
				// 		'type': '采购物料类型'
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	let returnData = data.data
				// 	this.wllxData = returnData
				// })

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
				console.log(this.nczData,'------',this.identityCardNumber)
				if(this.nczData.length>0){
					for(var i in this.nczData){
						if(this.identityCardNumber==this.nczData[i].companyName){
							this.registeredCapital = this.nczData[i].regCapital
						}
					}
				}
				
			}
		},
		mounted() {
			if (this.$route.query.status != null) {
				this.statusT = this.$route.query.status
			} else {
				this.statusT = ''
			}
			this.dataSelect()
			this.flxmSelect()
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