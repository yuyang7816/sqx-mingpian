<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">财务中心</span>
			<h2 class="nf-page-hero__title">财务中心</h2>
			<p class="nf-page-hero__desc">管理用户提现、支付记录与财务统计，支持多条件筛选、批量转账与数据导出</p>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="提现管理" name="first">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">筛选条件</div>
				<div class="nf-filter-form">
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">支付宝账号</span>
						<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" placeholder="请输入支付宝账号"
							v-model="zhifubao"></el-input>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">支付宝名称</span>
						<el-input class="nf-filter-form__input nf-filter-form__input--wide" @keydown.enter.native="phoneSelect" placeholder="请输入支付宝名称"
							v-model="zhifubaoName"></el-input>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">提现类型</span>
						<el-select v-model="classify" placeholder="请选择状态" class="nf-filter-form__input" @change="phoneSelect(classify)">
							<el-option v-for="item in classifyList" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">用户昵称</span>
						<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" placeholder="请输入用户昵称"
							v-model="userNameT"></el-input>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">手机号</span>
						<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
							v-model="phone"></el-input>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">转账订单号</span>
						<el-input class="nf-filter-form__input" @keydown.enter.native="phoneSelect" placeholder="请输入转账订单号"
							v-model="orderNumberT"></el-input>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">状态</span>
						<el-select v-model="state1" placeholder="请选择状态" class="nf-filter-form__input" @change="phoneSelect(state)">
							<el-option v-for="item in states1" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">开始时间</span>
						<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime" align="right"
							type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
						</el-date-picker>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">截止时间</span>
						<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime" align="right"
							type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
						</el-date-picker>
					</div>
					<div class="nf-filter-form__actions">
						<el-button type="primary" icon="document" @click="phoneSelect">查询</el-button>
						<el-button type="primary" icon="document" @click="cleans2">重置</el-button>
						<el-button type="primary" icon="document"
							@click="transferClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">
							批量转账
						</el-button>
						<el-button type="warning" icon="document" @click="exportBtn" :disabled="!isAuth('financeList:daochu')">导出Excel</el-button>
					</div>
				</div>
				<p class="nf-filter-tip">* 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</p>
			</div>

			<div class="nf-table-card">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="150">
					<template slot-scope="scope">
						<span class="nf-table-link"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="150">
					<template slot-scope="scope">
						<span class="nf-table-link"
							@click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhifubao" label="支付宝账号" width="150">
					<template slot-scope="scope">
						<span class="nf-table-link"
							@click="updates(scope.row)">{{scope.row.zhifubao ? scope.row.zhifubao : '暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhifubaoName" label="支付宝名称" width="100">
					<template slot-scope="scope">
						<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : '暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="cardFront" label="收款二维码" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.wxImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.wxImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="orderNumber" label="转账订单号" width="150">
				</el-table-column>
				<el-table-column prop="classify" label="提现方式" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.classify == 1">支付宝</span>
						<span v-else-if="scope.row.classify == 2">微信小程序</span>
						<span v-else-if="scope.row.classify == 3">微信公众号</span>
						<span v-else>暂无</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="提现金额" width="100">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="nf-table-link" v-if="scope.row.state == 0">待转账</span>
						<span v-if="scope.row.state == 1">已转账</span>
						<span v-if="scope.row.state == -1">已拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="refund" label="拒绝原因" width="220">
				</el-table-column>
				<el-table-column prop="createAt" label="申请时间" width="170">
				</el-table-column>
				<el-table-column prop="outAt" label="转账/拒绝时间" width="180">
				</el-table-column>
				
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('financeList:transfer') || scope.row.state != 0"
							@click="batch(scope.row)">转账
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('financeList:refund') || scope.row.state != 0"
							@click="refund(scope.row)">拒绝
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="收入统计" name="second">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeSelect">
				</el-date-picker>
			</div>
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">发布任务支出金额</div>
							<div class="text_color"><span>{{MoneyData.sumPrice ? MoneyData.sumPrice : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">用户收益</div>
							<div class="text_color">
								<span>{{MoneyData.sumMoneyBySend ? MoneyData.sumMoneyBySend : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">平台任务收益</div>
							<div class="text_color">
								<span>{{MoneyData.sumMoneyByProfit ? MoneyData.sumMoneyByProfit  : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
			</el-row>
		</el-tab-pane> -->
		<el-tab-pane label="提现统计" name="third">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">筛选条件</div>
				<div class="nf-filter-form">
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">查询维度</span>
						<el-select v-model="flag" class="nf-filter-form__input" @change="animeDat2">
							<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">统计日期</span>
						<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="info1.stockDate1" align="right"
							type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
							@change="animeSelect2">
						</el-date-picker>
					</div>
				</div>
			</div>
			<div class="nf-kpi-row">
				<div class="nf-kpi-card">
					<div class="nf-kpi-card__label">累计提现金额</div>
					<div class="nf-kpi-card__value"><span>{{withdrawData.sumMoney}}</span>元</div>
				</div>
				<div class="nf-kpi-card">
					<div class="nf-kpi-card__label">提现次数</div>
					<div class="nf-kpi-card__value"><span>{{withdrawData.countMoney}}</span>笔</div>
				</div>
				<div class="nf-kpi-card">
					<div class="nf-kpi-card__label">待转账次数</div>
					<div class="nf-kpi-card__value"><span>{{withdrawData.stayMoney}}</span>笔</div>
				</div>
			</div>
		</el-tab-pane>
		<el-tab-pane label="支付统计" name="sixth">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">筛选条件</div>
				<div class="nf-filter-form">
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">查询维度</span>
						<el-select v-model="flag" class="nf-filter-form__input" @change="animeDat3">
							<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">统计日期</span>
						<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="info2.stockDate2" align="right"
							type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
							@change="animeSelect3">
						</el-date-picker>
					</div>
				</div>
			</div>
			<div class="nf-stat-grid">
				<div class="nf-stat-tile">
					<div class="nf-stat-tile__label">支付总金额</div>
					<div class="nf-stat-tile__value"><span>{{chongzhiData.allMoney ? chongzhiData.allMoney : 0}}</span>元</div>
				</div>
				<div class="nf-stat-tile">
					<div class="nf-stat-tile__label">小程序支付金额</div>
					<div class="nf-stat-tile__value"><span>{{chongzhiData.wxAppMoney ? chongzhiData.wxAppMoney : 0}}</span>元</div>
				</div>
				<div class="nf-stat-tile">
					<div class="nf-stat-tile__label">APP支付金额</div>
					<div class="nf-stat-tile__value"><span>{{chongzhiData.appMoney ? chongzhiData.appMoney : 0}}</span>元</div>
				</div>
				<div class="nf-stat-tile">
					<div class="nf-stat-tile__label">微信二维码支付金额</div>
					<div class="nf-stat-tile__value"><span>{{chongzhiData.wxRwmMoney ? chongzhiData.wxRwmMoney : 0}}</span>元</div>
				</div>
				<div class="nf-stat-tile">
					<div class="nf-stat-tile__label">支付宝APP支付金额</div>
					<div class="nf-stat-tile__value"><span>{{chongzhiData.zfbMoney ? chongzhiData.zfbMoney : 0}}</span>元</div>
				</div>
				<div class="nf-stat-tile">
					<div class="nf-stat-tile__label">支付宝H5支付金额</div>
					<div class="nf-stat-tile__value"><span>{{chongzhiData.zfbH5Money ? chongzhiData.zfbH5Money : 0}}</span>元</div>
				</div>
			</div>
		</el-tab-pane>
		<el-tab-pane label="支付记录" name="fourth">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">筛选条件</div>
				<div class="nf-filter-form">
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">状态</span>
						<el-select v-model="state" placeholder="请选择状态" class="nf-filter-form__input" @change="selectTrigger(state)">
							<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">开始时间</span>
						<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime" align="right"
							type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
						</el-date-picker>
					</div>
					<div class="nf-filter-form__item">
						<span class="nf-filter-form__label">截止时间</span>
						<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime" align="right" type="date"
							format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
						</el-date-picker>
					</div>
					<div class="nf-filter-form__actions">
						<el-button type="primary" icon="document" @click="timeDate">查询</el-button>
					</div>
				</div>
			</div>
			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="rechargeData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
					<template slot-scope="scope">
						<span class="nf-table-link"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : 'null'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderId" label="订单号" width="250">
				</el-table-column>
				<el-table-column prop="money" label="金额" width="100">
				</el-table-column>
				<el-table-column label="支付方式" width="120">
					<template slot-scope="scope">
						<span class="nf-table-link" v-if="scope.row.classify == 1">app微信</span>
						<span class="nf-table-link" v-if="scope.row.classify == 2">微信公众号</span>
						<span class="nf-table-link" v-if="scope.row.classify == 3">微信小程序</span>
						<span class="nf-table-link" v-if="scope.row.classify == 4">app支付宝</span>
						<span class="nf-table-link" v-if="scope.row.classify == 5">支付宝H5</span>
						<span class="nf-table-link" v-if="scope.row.classify == 6">点数</span> 
						
						<!-- <span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 1">微信</span>
						<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 2">支付宝</span> -->
						<!-- <span style="color: #2f3968;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
						<span style="color: #2f3968;cursor: pointer;" v-else-if="scope.row.classify == 2">微信公众号</span>
						<span style="color: #2f3968;cursor: pointer;" v-else-if="scope.row.classify == 3">微信小程序</span>
						<span style="color: #2f3968;cursor: pointer;" v-else-if="scope.row.classify == 4">支付宝</span>
            <span style="color: #2f3968;cursor: pointer;" v-else-if="scope.row.classify == 5">零钱</span>
						<span style="color: #2f3968;cursor: pointer;" v-else>暂无</span> -->
					</template>
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span class="nf-table-link" v-if="scope.row.state == 0">待支付</span>
						<span class="nf-table-link" v-if="scope.row.state == 1">支付成功</span>
						<span class="nf-table-link" v-if="scope.row.state == 2">支付失败</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="170">
				</el-table-column>
				<el-table-column prop="payTime" label="支付时间" width="170">
				</el-table-column>
			</el-table>
			<div class="nf-table-card__foot">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="收入统计" name="shouru">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeSelect">
				</el-date-picker>
			</div>
				<el-row>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">服务费收益</div>
								<div class="text_color"><span>{{MoneyData.serviceCharge ? MoneyData.serviceCharge : 0}}</span>元
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">预售订单收益</div>
								<div class="text_color">
									<span>{{MoneyData.preSale ? MoneyData.preSale : 0}}</span>元</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">转赠收益</div>
								<div class="text_color">
									<span>{{MoneyData.giftTransfer ? MoneyData.giftTransfer : 0}}</span>元</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">出售订单收益</div>
								<div class="text_color">
									<span>{{MoneyData.sell ? MoneyData.sell : 0}}</span>元</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">盲盒订单收益</div>
								<div class="text_color">
									<span>{{MoneyData.blindBox ? MoneyData.blindBox : 0}}</span>元</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">平台订单总收益</div>
								<div class="text_color">
									<span>{{MoneyData.profit ? MoneyData.profit : 0}}</span>元</div>
							</div>
						</div>
					</el-col>
					<el-col :span="6" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">拍卖订单收益</div>
								<div class="text_color">
									<span>{{MoneyData.auction ? MoneyData.auction : 0}}</span>元</div>
							</div>
						</div>
					</el-col>
				</el-row>
		</el-tab-pane> -->
		</el-tabs>

		<!-- 拒绝弹框 -->
		<el-dialog title="拒绝" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="拒绝原因：" :label-width="formLabelWidth">
					<el-input v-model="form.content" type="textarea" rows="4" placeholder="请输入拒绝原因"
						style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refundNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				balance: -1,
				zhifubao: '',
				userNameT:'',
				orderNumberT:'',
				tkstatus: -1,
				state: 1,
				states: [{
						label: '全部',
						value: -1
					},
					{
						label: '待支付',
						value: 0
					},
					{
						label: '支付成功',
						value: 1
					},
					{
						label: '支付失败',
						value: 2
					}
				],
				state1: '',
				states1: [
					{
						label: '全部',
						value: ''
					},

					{
						label: '待提现',
						value: 0
					},
					{
						label: '已转账',
						value: 1
					},
					{
						label: '已拒绝',
						value: -1
					}
				],
				classify:'',
				classifyList:[
					{
							label: '全部',
							value: ''
						},
						{
							label: '支付宝',
							value: 1
						},
						{
							label: '微信',
							value: 2
						}
				],
				zhifubaoName: '',
				content: '',
				startTime: '',
				endTime: '',
				cashId: '',
				type: -1,
				totalnum: 0,
				totalnum2: 0,
				sqxTotal: '',
				userTotal: '',
				taunTotal: '',
				relationId: '',
				sumMoneyByTime: '',
				money: '',
				activeName: 'first',
				formLabelWidth: '200px',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				tableDataLoading: false,
				tableData: {},
				rechargeData: {},
				chongzhiData: {},
				withdrawData: {},
				depositData: [],
				MoneyData: {},
				checkBoxData: [], //多选框选择的值
				flag: 1,
				form: {
					cashId: '',
					content: '',
				},
				form2: {
					id: '',
					money: '',
					createTime: ''
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				phone:'',
			}
		},
		methods: {
			// 会员详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 批量转账
			transferClcik(id) {
				this.$confirm('此操作将进行批量转账, 是否继续?', '提示', {
				          confirmButtonText: '确定',
				          cancelButtonText: '取消',
				          type: 'warning'
				        }).then(() => {
				          var ids = id ? [id] : this.checkBoxData.map(item => {
				          	return item.id
				          })
				          for (var i in ids) {
				          	this.$http({
				          		// url: this.$http.adornUrl(`cash/alipay/${ids[i]}`),
				          		// method: 'post',
				          		// data: this.$http.adornData({})
								url: this.$http.adornUrl(`cash/alipay`),
								method: 'post',
								params: this.$http.adornParams({
									'cashId':ids[i]
								})
				          	}).then(({
				          		data
				          	}) => {
				          		if (data.code == 0) {
				          			this.$notify({
				          				title: '提示',
				          				duration: 1800,
				          				message: data.msg,
				          				type: 'warning'
				          			})
				          			return
				          		}
				          		if (data.code == 9999) {
				          			this.$notify({
				          				title: '提示',
				          				duration: 1800,
				          				message: data.msg,
				          				type: 'error'
				          			})
				          			return
				          		}
				          	})
				          }
				        }).catch(() => {
				          this.$message({
				            type: 'info',
				            message: '已取消批量转账'
				          });
				        });

			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//转账
			batch(row) {
				if (row.state == 1) {
					this.$message({
						message: '已转账，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$confirm(`确定转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`cash/alipay`),
						method: 'post',
						params: this.$http.adornParams({
							'cashId':row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						if (data.code == 9999) {
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
						if (data.code == 500) {
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
			// 拒绝
			refund(row) {
				if (row.state == -1) {
					this.$message({
						message: '已拒绝，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
				} else {
					this.dialogFormVisible = true
					this.form.cashId = row.id
				}
			},
			// 拒绝操作
			refundNoticeTo() {
				if (this.form.content == '') {
					this.$message({
						message: '请输入拒绝原因',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(`cash/refund`),
					method: 'post',
					params: this.$http.adornParams({
						'cashId':this.form.cashId,
						'content':this.form.content
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
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.code == 500) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.code == -100) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
				})
			},
			timeDate() {
				this.rechargeSelect()
			},
			// 日期选择
			animeDat(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.incomeSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.incomeSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.incomeSelect()
				}
			},
			// 日期选择2
			animeDat2(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.withdrawSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.withdrawSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.withdrawSelect()
				}
			},
			// 日期选择3
			animeDat3(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.rechSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.rechSelect()
				}
			},
			animeSelect() {
				this.incomeSelect()
			},
			animeSelect2() {
				this.withdrawSelect()
			},
			animeSelect3() {
				this.rechSelect()
			},
			//处理默认选中当前日期
			getNowTime1() {
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
				// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '提现管理') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '收入统计') {
					this.flag = 1
					this.incomeSelect()
				}
				if (tab._props.label == '支付统计') {
					this.flag = 1
					this.rechSelect()
				}
				if (tab._props.label == '提现统计') {
					this.flag = 1
					this.withdrawSelect()
				}
				if (tab._props.label == '支付记录') {
					this.page = 1
					this.limit = 10
					this.rechargeSelect()
				}
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans2() {
				this.zhifubao = ''
				this.zhifubaoName = ''
				this.state1 = ''
				this.classify = ''
				this.startTime= ''
				this.endTime= ''
				this.phone = ''
				this.userNameT= ''
				this.orderNumberT= ''
				
				this.page = 1
				this.dataSelect()
			},
			// 支付宝账号查询
			//     zfbselect () {
			//       if (this.zhifubao == '') {
			//         this.$notify({
			//           title: '提示',
			//           duration: 1800,
			//           message: '请输入支付宝账号',
			//           type: 'error'
			//         })
			//         return
			//       }
			// this.page = 1
			//       this.tableDataLoading = true
			//       this.$http({
			//         url: this.$http.adornUrl('user/selectPayDetails'),
			//         method: 'get',
			//         params: this.$http.adornParams({
			// 	  'page':this.page,
			// 	  'limit':this.limit,
			// 	  'zhifubao':this.zhifubao
			//   })
			//       }).then(({data}) => {
			//         this.tableDataLoading = false
			//         let returnData = data.data
			//        this.tableData = returnData.list
			//        this.totalnum = returnData.totalCount
			//       })
			//     },
			//     // 支付宝名称
			//     qdselect () {
			//       if (this.zhifubaoName == '') {
			//         this.$notify({
			//           title: '提示',
			//           duration: 1800,
			//           message: '请输入渠道账号',
			//           type: 'error'
			//         })
			//         return
			//       }
			//       this.tableDataLoading = true
			//       this.$http({
			//         url: this.$http.adornUrl('user/selectPayDetails'),
			//         method: 'get',
			//         params: this.$http.adornParams({
			// 	  'page':this.page,
			// 	  'limit':this.limit,
			// 	  'zhifubaoName':this.zhifubaoName
			//   })
			//       }).then(({data}) => {
			//         this.tableDataLoading = false
			//         let returnData = data.data
			//         this.tableData = returnData.list
			//         this.totalnum = returnData.totalCount
			//       })
			//     },
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectAdminHelpProfit'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'phone':this.phone,
						'classify':this.classify,
						'userName':this.userNameT,
						'orderNumber':this.orderNumberT,
            'state':this.state1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
					this.totalnum = returnData.totalCount
				})
			},
			// 收入统计
			incomeSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/order/incomeStatistics'),
					method: 'get',
					params: this.$http.adornParams({
						'dateTime': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.MoneyData = returnData
					}
				})
			},
			// 充值统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl('user/payMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info2.stockDate2,
						'type': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.chongzhiData = returnData
					}
				})
			},
			// 提现统计
			withdrawSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/statisticsCashMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			selectTrigger() {
				this.rechargeSelect()
			},
			// 获取支付记录
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info2.stockDate2
				}
				if (this.startTime == '') {
					this.startTime = this.info.stockDate
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRecharge'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'startTime': this.startTime,
						'endTime': this.endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
					this.totalnum2 = returnData.totalCount
				})
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('cash/exportExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'phone':this.phone,
						'classify':this.classify,
						'userName':this.userNameT,
						'orderNumber':this.orderNumberT,
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
						elink.download = '提现列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
      dataSelect1() {
        if (this.$route.query.status != null) {
          this.state1 = this.$route.query.status
        } else {
          this.state1 = ''
        }
        this.dataSelect()
      },
		},
		mounted() {
      if (this.$route.query.status != null) {
        this.state1 = this.$route.query.status
      } else {
        this.state1 = ''
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

	.nf-filter-tip {
		margin: 12px 0 0;
		font-size: 12px;
		color: #e6a23c;
		line-height: 1.5;
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

	.nf-kpi-row {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 12px;
		margin-bottom: 0;
	}

	.nf-kpi-card {
		padding: 16px;
		background: #fff;
		border-radius: 8px;
		border: 1px solid var(--nf-border, #e8eaed);
		box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
	}

	.nf-kpi-card__label {
		font-size: 13px;
		color: var(--nf-text-regular, #303133);
		margin-bottom: 8px;
	}

	.nf-kpi-card__value {
		font-size: 14px;
		color: var(--nf-text-secondary, #909399);
	}

	.nf-kpi-card__value span {
		font-size: 22px;
		font-weight: 600;
		color: var(--nf-brand, #2f3968);
		margin-right: 4px;
	}

	.nf-stat-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 12px;
		margin-bottom: 0;
	}

	.nf-stat-tile {
		padding: 14px 16px;
		background: var(--nf-sidebar-bg-alt, #f7f8fa);
		border-radius: 8px;
		border: 1px solid var(--nf-border, #e8eaed);
	}

	.nf-stat-tile__label {
		font-size: 13px;
		color: var(--nf-text-regular, #303133);
		margin-bottom: 6px;
	}

	.nf-stat-tile__value {
		font-size: 14px;
		color: var(--nf-text-secondary, #909399);
	}

	.nf-stat-tile__value span {
		font-size: 20px;
		font-weight: 600;
		color: var(--nf-brand, #2f3968);
		margin-right: 4px;
	}
</style>
