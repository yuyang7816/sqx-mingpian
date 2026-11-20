<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">积分管理</span>
			<h2 class="nf-page-hero__title">订单列表</h2>
			<p class="nf-page-hero__desc">查看与管理用户充值、会员购买等订单，支持多条件筛选、导出 Excel 与退款操作</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="status" class="nf-filter-form__input" @change="timeDate2()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户名</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="timeDate2"
						placeholder="请输入用户名" v-model="nickName"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">手机号</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="timeDate2"
						placeholder="请输入手机号" v-model="phone"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">标题</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="timeDate2"
						placeholder="请输入标题" v-model="titleT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">订单类型</span>
					<el-select v-model="typeT" class="nf-filter-form__input" @change="timeDate2()">
						<el-option v-for="item in classifyNum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">是否退款</span>
					<el-select v-model="refundStatusT" class="nf-filter-form__input" @change="timeDate2()">
						<el-option v-for="item in classifyNum1" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">开始时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime"
						align="right" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">截止时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime"
						align="right" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="timeDate2">查询</el-button>
					<el-button type="primary" icon="document" @click="cleaes">重置</el-button>
					<el-button type="warning" icon="document" @click="exportBtn">导出Excel</el-button>
					<el-button plain type="primary" icon="document" :disabled="!isAuth('exchangeList:add')"
						@click="addOrederBtn()">新增订单</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column prop="id" label="编号" width="80" fixed="left"></el-table-column>
			<el-table-column prop="sysUserName" label="录入人" width="100">
				<template slot-scope="scope">
					<span style="color: #f56c6c;" v-if="scope.row.sysUserName">{{ scope.row.sysUserName }}</span>
					<span v-else> - </span>
				</template>
			</el-table-column>
			<el-table-column prop="userName" label="用户名" width="160">
				<template slot-scope="scope">
					<span class="nf-table-link"
						@click="updates(scope.row.userId)">{{ scope.row.userName?scope.row.userName:'-' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="parentUserName" label="合伙人用户" width="160">
				<template slot-scope="scope">
					<span class="nf-table-link"
						@click="updates(scope.row.parentUserId)">{{ scope.row.parentUserName?scope.row.parentUserName:'-' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="agentUserName" label="采购资源代理用户" width="160">
				<template slot-scope="scope">
					<span class="nf-table-link"
						@click="updates(scope.row.agentUserId)">{{ scope.row.agentUserName?scope.row.agentUserName:'-' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="agentMoney" label="代理业绩"></el-table-column>
			<el-table-column prop="phone" label="手机号" width="160"></el-table-column>
			<el-table-column prop="orderId" label="订单号" width="200"></el-table-column>
			<el-table-column prop="title" label="标题" width="200">
				<template slot-scope="scope">
					<span v-if="scope.row.type == 1">
						<span
							v-if="scope.row.classify == 7||scope.row.classify == 8||scope.row.classify == 9||scope.row.classify == 10">
							{{scope.row.sysTitle}}
						</span>
						<span v-else>
							{{scope.row.countMoney}}
						</span>

					</span>
					<span v-else-if="scope.row.type == 2">
						<span
							v-if="scope.row.classify == 7||scope.row.classify == 8||scope.row.classify == 9||scope.row.classify == 10">
							{{scope.row.sysTitle}}
						</span>
						<span v-else>
							{{scope.row.vipName}}
						</span>
					</span>
					<span v-else-if="scope.row.type == 3"> {{scope.row.title}}</span>
					<span v-else-if="scope.row.type == 4"> {{scope.row.groupName}}</span>
					<span v-else-if="scope.row.type == 5"> {{scope.row.purchaseName}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="money" label="金额"></el-table-column>
			
			<el-table-column prop="userId" label="会员编号"></el-table-column>
			<el-table-column label="订单类型" fixed="left">
				<template slot-scope="scope">
					<span v-if="scope.row.type == 1">充值点数</span>
					<span v-if="scope.row.type == 2">
						购买会员
						<span v-if="scope.row.parentId == 1">(月会员)</span>
						<span v-if="scope.row.parentId == 2">(季会员)</span>
						<span v-if="scope.row.parentId == 3">(年会员)</span>
						<span v-if="scope.row.parentId == 4">(周会员)</span>
					</span>
					<span v-if="scope.row.type == 3">查看需求</span>
					<span v-if="scope.row.type == 4">查看群二维码</span>
					<span v-if="scope.row.type == 5">查看采购名片</span>
					<span v-if="scope.row.type == 6">激活订单</span>
				</template>
			</el-table-column>
			<el-table-column label="支付类型">
				<template slot-scope="scope">
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 2">微信公众号</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 3">微信小程序</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 4">app支付宝</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 5">支付宝H5</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 6">点数</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 7">系统订单-现金</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 8">系统订单-银行卡转账</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 9">系统订单-支付宝</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 10">系统订单-微信</span>

				</template>
			</el-table-column>
			<el-table-column label="状态">
				<template slot-scope="scope">
					<span v-if="scope.row.state == 0">待支付</span>
					<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.state == 1">支付成功</span>
					<span style="color: red;cursor: pointer;" v-if="scope.row.state == 2">支付失败</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="170">
			</el-table-column>
			<el-table-column prop="payTime" label="支付时间" width="170">
			</el-table-column>
			<el-table-column prop="refundStatus" label="是否退款" width="80" fixed="right">
				<template slot-scope="scope">
					<span style="color: red;" v-if="scope.row.refundStatus == 1">是</span>
					<span style="color: #999;" v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="80">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('exchangeList:tuikuan')"
						v-if="scope.row.state == 1 &&scope.row.refundStatus != 1" @click="batch(scope.row)">
						退款
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div class="nf-table-card__summary">
			本页累计总交易统计：{{totalMoney.toFixed(2)}}元
		</div>
		<div class="nf-table-card__foot">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
			</el-pagination>
		</div>
		</div>
		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="memberImg"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="memberImg" :src="memberImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" placeholder="请输入排序"
					onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 新增订单  -->
		<el-dialog title="新增订单" :visible.sync="dialogFormVisibleOr" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单用户：</span>
				<el-input style="width:50%;" v-model="userName" @focus="orerUserBtn()" placeholder="请输入订单用户"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单类型：</span>
				<el-radio-group v-model="type" @change="orderTypeBtn()">
					<el-radio :label="1">充值点数</el-radio>
					<el-radio :label="2">购买会员</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="title" placeholder="请输入名称"></el-input>
				<el-button size="mini" type="primary" plain @click="orderTypeBtn()">重选
				</el-button>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
				<el-input style="width:50%;" v-model="money" placeholder="请输入金额" type="number" :min="0"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">付款方式：</span>
				<el-radio-group v-model="classify">
					<el-radio :label="7">现金</el-radio>
					<el-radio :label="8">银行卡转账</el-radio>
					<el-radio :label="9">支付宝</el-radio>
					<el-radio :label="10">微信</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">合伙人上级邀请码：</span>
				<el-input style="width:50%;" v-model="staffInviterCode" disabled placeholder="合伙人上级邀请码"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleOr = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeBtn()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表  -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisibleUser" center width="70%">
			<div style="margin:2% 0;display: inline-block;">
				<span>昵称</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>&nbsp;&nbsp;
				<span>手机号</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;

				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading4" :data="userIds.records">
				<el-table-column fixed prop="userId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" width="220" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="120">
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
		<!-- 充值点数列表  -->
		<el-dialog title="充值点数列表" :visible.sync="dialogFormVisibleDs" center width="70%" :show-close="false"
			:close-on-click-modal="false">
			<el-table v-loading="tableDataLoading" :data="tableDataDs">

				<el-table-column fixed prop="configId" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="remarkImg" label="说明图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.remarkImg" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="image" label="图片">
					<template slot-scope="scope">
						　　<img v-if="scope.row.image" :src="scope.row.image" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="money" label="价格">
				</el-table-column>
				<el-table-column prop="countMoney" label="支付点数">
				</el-table-column>
				<el-table-column prop="isOnlyOnce" label="是否限购一次">
					<template slot-scope="scope">
						<span v-if="scope.row.isOnlyOnce == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary"
							@click="xuanzeBtn(scope.row)">选择
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 购买会员列表  -->
		<el-dialog title="购买会员列表" :visible.sync="dialogFormVisibleHy" center width="70%" :show-close="false"
			:close-on-click-modal="false">
			<el-table v-loading="tableDataLoading" :data="tableDataHy" style="width: 100%">
				<el-table-column label="编号" prop="id" width="80">
				</el-table-column>
				<el-table-column label="名称" prop="vipName">
				</el-table-column>
				<el-table-column prop="image" label="图片">
					<template slot-scope="scope">
						　　<img v-if="scope.row.image" :src="scope.row.image" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="iconImg" label="角标图">
					<template slot-scope="scope">
						　　<img v-if="scope.row.iconImg" :src="scope.row.iconImg" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column label="月数" prop="vipMonthCount">
				</el-table-column>
				<el-table-column label="查看次数" prop="viewNum">
				</el-table-column>
				<el-table-column label="会员价格" prop="money">
				</el-table-column>
				<el-table-column label="行业咨询可发布条数(0为不限制条数)" prop="industryCount">
				</el-table-column>
				<el-table-column label="行业咨询发布条数计算周期" prop="industryCycle">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary"
							@click="xuanzeBtn(scope.row)">选择
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				title: '添加',
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				totalMoney: 0,
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待支付'
					},
					{
						value: 1,
						label: '支付成功'
					},
					{
						value: 2,
						label: '支付失败'
					},

				],
				refundStatusT:'',
				classifyNum1: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}],
				typeT: '',
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: 'app微信'
					},
					{
						value: 2,
						label: '微信公众号'
					},
					{
						value: 3,
						label: '微信小程序'
					},
					{
						value: 4,
						label: 'app支付宝'
					},
					{
						value: 5,
						label: '支付宝H5'
					},
					{
						value: 6,
						label: '点数'
					},
					{
						value: 7,
						label: '系统订单-现金'
					},
					{
						value: 8,
						label: '系统订单-银行卡转账'
					},
					{
						value: 9,
						label: '系统订单-支付宝'
					},
					{
						value: 10,
						label: '系统订单-微信'
					},

				],
				nickName: '',
				orderId: '',
				titleT: '',
				phone: '',
				classifyT: '',
				classifyNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '充值点数'
					},
					{
						value: 2,
						label: '购买会员'
					},
					{
						value: 3,
						label: '查看需求'
					},
					{
						value: 4,
						label: '查看群二维码'
					},
					{
						value: 5,
						label: '查看采购名片'
					},
					{
						value: 6,
						label: '激活订单'
					},

				],
				dialogFormVisibleOr: false,
				userName: '',
				userId: '',
				type: 1,
				money: '',
				classify: 1,
				staffInviterCode: '',
				parentId: '',
				tableDataLoading4: false,
				dialogFormVisibleUser: false,
				phone1: '',
				nickName2: '',
				limitU: 10,
				pageU: 1,
				userIds: {},
				dialogFormVisibleTg: false,
				dialogFormVisibleDs: false,
				dialogFormVisibleHy: false,
				tableDataDs: [],
				tableDataHy: [],
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 详情跳转
			updates(userId) {
				if(userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
				
			},
			// 获取数据列表
			dataSelect() {
				this.totalMoney = 0
				// this.tableDataLoading = true
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRecharge'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.status,
						'phone': this.phone,
						'userName': this.nickName,
						'classify': this.classifyT,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'type': this.typeT,
						'orderId': this.orderId,
						'title': this.titleT,
						'refundStatus':this.refundStatusT,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						for (var i in data.data.list) {
							if (data.data.list[i].money) {
								this.totalMoney = this.totalMoney + Number(data.data.list[i].money)
							}
						}
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			timeDate2() {
				this.page = 1
				this.dataSelect()
			},
			cleaes() {
				this.page = 1
				this.endTime = ''
				this.startTime = ''
				this.typeT = ''
				this.status = ''
				this.nickName = ''
				this.classifyT = ''
				this.orderId = ''
				this.titleT = ''
				this.phone = ''
				this.refundStatusT = ''
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
			handleSizeChange2(val) {
				this.limitU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			handleSizeChange3(val) {
				this.limitU = val
				this.userClass()
			},
			handleCurrentChange3(val) {
				this.pageU = val
				this.userClass()
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
			//退款
			batch(row) {
				this.$confirm(`确定要退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/payDetails/refundPayDetails'),
						method: 'post',
						params: this.$http.adornParams({
							'payId': row.id
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
							return
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
					})
				})
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('cash/excelPayDetails'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'state': this.status,
						'phone': this.phone,
						'userName': this.nickName,
						'classify': this.classifyT,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'type': this.typeT,
						'orderId': this.orderId,
						'title': this.titleT,
						'refundStatus':this.refundStatusT,
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 新增订单 弹框
			addOrederBtn() {
				this.staffInviterCode = ''
				this.userName = ''
				this.userId = ''
				this.type = ''
				this.money = ''
				this.title = ''
				this.parentId = ''
				this.classify = 7
				this.dialogFormVisibleOr = true
			},
			// 确认新增 
			addNoticeBtn() {
				if (this.userId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择订单用户',
						type: 'warning'
					});
					return
				}
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择订单类型',
						type: 'warning'
					});
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					});
					return
				}
				var sysUserId = this.$cookie.get('sysUserId')
				var sysUserName = this.$cookie.get('sysUserName')
				this.$http({
					url: this.$http.adornUrl('cash/insertPayDetails'),
					method: 'post',
					data: this.$http.adornData({
						'staffInviterCode': this.staffInviterCode,
						'userName': this.userName,
						'userId': this.userId,
						'type': this.type,
						'money': this.money,
						'classify': this.classify,
						'sysTitle': this.title,
						'parentId': this.parentId,
						'sysUserName': sysUserName,
						'sysUserId': sysUserId,
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
								this.dialogFormVisibleOr = false
								this.dataSelect()
							}
						})
						return
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						return
					}
				})

			},
			// 订单用户 弹框
			orerUserBtn() {
				this.pageU = 1
				this.nickName2 = ''
				this.phone1 = ''

				this.userClass()
				this.dialogFormVisibleUser = true
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
				this.userName = row.userName
				if (row.staffInviterCode) {
					this.staffInviterCode = row.staffInviterCode
				} else {
					this.staffInviterCode = ''
				}
				this.dialogFormVisibleUser = false
			},
			// 选择订单类型
			orderTypeBtn() {
				if (this.type == 1) {
					this.dialogFormVisibleDs = true
					this.dataSelectDs()
				} else {
					this.dataSelectHy()
					this.dialogFormVisibleHy = true
				}
			},
			// 确认选择
			xuanzeBtn(row) {
				if (this.type == 1) {
					this.parentId = row.configId
					this.money = row.money
					this.title = row.countMoney
					this.dialogFormVisibleDs = false
				} else {
					this.parentId = row.id
					this.money = row.money
					this.title = row.vipName
					this.dialogFormVisibleHy = false
				}
			},
			// 获取点数数据列表
			dataSelectDs() {
				this.$http({
					url: this.$http.adornUrl('admin/countMoneyConfig/getCountMoneyConfigList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 50
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						let returnData = data.data;
						this.tableDataDs = returnData.records;
					}

				})
			},
			// 获取会员数据列表
			dataSelectHy() {
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/getVipDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 50
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						let returnData = data.data;
						this.tableDataHy = returnData.records;
					}

				})
			},


		},
		mounted() {
			this.dataSelect()
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

	.nf-filter-form__input--date {
		width: 180px;
	}

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		flex-wrap: wrap;
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

	.nf-table-card__summary {
		margin-top: 12px;
		font-size: 16px;
		font-weight: 600;
		color: #b94a48;
	}

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>