<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">采购管理</span>
			<h2 class="nf-page-hero__title">采购列表</h2>
			<p class="nf-page-hero__desc">管理采购需求发布与审核，支持按状态、发布类型、姓名、行业及时间筛选查询</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="statusT" class="nf-filter-form__input" @change="select()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">发布类型</span>
					<el-select v-model="screen" class="nf-filter-form__input" @change="select()">
						<el-option v-for="(item,index) in statesnum2" :key="index" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">姓名</span>
					<el-select v-model="userNameT" class="nf-filter-form__input nf-filter-form__input--wide" filterable
						@change="select()" placeholder="请选择/输入姓名">
						<el-option v-for="(item,index) in tableDataUser" :key="index" :label="item" :value="item">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">求购标题</span>
					<el-input v-model="dataTitleT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入求购标题" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">行业一级名称</span>
					<el-select v-model="phoneT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请选择行业一级名称" @change="industryFirstBtn(1)">
						<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.industryName"
							:value="item.industryName">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">行业二级名称</span>
					<el-select v-model="cityT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请选择行业二级名称" :disabled="phoneT==''" @change="select()">
						<el-option v-for="(item,index) in tableDataFer" :key="item.index" :label="item.industryName"
							:value="item.industryName">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">开始时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime"
						align="right" type="datetime" format="yyyy-MM-dd HH:ss:mm" value-format="yyyy-MM-dd HH:ss:mm"
						placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">截止时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime"
						align="right" type="datetime" format="yyyy-MM-dd HH:ss:mm" value-format="yyyy-MM-dd HH:ss:mm"
						placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="select">查询</el-button>
					<el-button icon="document" type="primary" @click="cleans">重置</el-button>
					<el-button icon="document" type="primary" :disabled="!isAuth('autonym:add')" @click="addBtn(1)">添加
					</el-button>
					<el-button type="warning" icon="document" @click="exportBtn"
						:disabled="!isAuth('financeList:daochu')">导出Excel</el-button>
				</div>
			</div>
			<p class="nf-filter-tip">* 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</p>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="采购列表" name="first">
				<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column label="编号" prop="demandId" width="80" fixed="left">
			</el-table-column>
			<el-table-column prop="releaseType" label="发布类型">
				<template slot-scope="scope">
					<span v-if="scope.row.releaseType==2">代理发布</span>
					<span v-if="scope.row.releaseType==1">平台发布</span>
					<span v-if="scope.row.releaseType==3">普通用户发布</span>
				</template>
			</el-table-column>
			<el-table-column label="平台用户" prop="realName">
				<template slot-scope="scope">
					<span class="nf-table-link">{{
        				scope.row.sysUserName ? scope.row.sysUserName : '-'
        			}}</span>
				</template>
			</el-table-column>
			<el-table-column label="姓名" prop="agentName">
				<template slot-scope="scope">
					<span class="nf-table-link" v-if="scope.row.releaseType!=2"
						@click="updates(scope.row)">{{
        				scope.row.agentName ? scope.row.agentName : '-'
        			}}</span>
					<span class="nf-table-link" v-else-if="scope.row.releaseType==2"
						@click="updates(scope.row)">{{
						scope.row.agentAuth ? scope.row.agentAuth.realName : '-'
					}}</span>
					<span class="nf-table-link" v-else>-</span>
				</template>
			</el-table-column>
			<el-table-column prop="industryFirstName" label="一级分类">
			</el-table-column>
			<el-table-column prop="industrySecondName" label="二级分类">
			</el-table-column>
			<el-table-column prop="cooperationModels" label="合作模式">
				<template slot-scope="scope">
					<!-- <span v-if="scope.row.cooperationModel==1">直接对接</span>
					<span v-if="scope.row.cooperationModel==2">平台筛选对接</span> -->
					<span v-if="scope.row.cooperationModels==1">直接联系</span>
					<span v-if="scope.row.cooperationModels==2">平台对接</span>
				</template>
			</el-table-column>
			
			
			<el-table-column prop="title" label="求购标题" width="200">
				<template slot-scope="scope">
					<div
						style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
						{{scope.row.title}}
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="describeDetails" label="产品描述" width="250">
				<template slot-scope="scope">
					<div
						style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
						{{scope.row.describeDetails}}
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="needCount" label="采购数">
			</el-table-column>
			<!-- <el-table-column prop="needUnit" label="单位">
					</el-table-column> -->
			<!-- <el-table-column prop="estimateMoney" label="预估价格（单位:万元）">
					</el-table-column> -->
			<el-table-column label="地址" prop="receiveProvince" width="200">
				<template slot-scope="scope">
					<span>{{ scope.row.receiveProvince + '' + scope.row.receiveCity + '' + scope.row.receiveDistrict  }}</span>
					<!-- + '' + scope.row.receiveAddress -->
				</template>
			</el-table-column>
			<el-table-column prop="deadLineTime" label="报名截止时间">
			</el-table-column>
			<!-- <el-table-column prop="procureCompanyName" label="采购方公司名称">
					</el-table-column> -->
			<el-table-column prop="procureRealName" label="采购方联系人">
			</el-table-column>
			<el-table-column prop="procurePhone" label="采购方联系电话">
			</el-table-column>
			<el-table-column prop="procureEmail" label="微信号">
			</el-table-column>
			<el-table-column prop="channel" label="渠道">
			</el-table-column>
			<el-table-column prop="countMoney" label="所需点数">
			</el-table-column>
			<el-table-column prop="peopleNum" label="对接人数">
			</el-table-column>

			<el-table-column prop="rewardPoints" label="奖励积分数">
			</el-table-column>
			<el-table-column prop="supplierQualification" label="供应商资质" width="200">
			</el-table-column>
			<!-- <el-table-column prop="customerServicePhone" label="采购方电话">
					</el-table-column> -->
			<!-- <el-table-column prop="customerServiceImg" label="客户微信二维码">
						<template slot-scope="scope">
							<div v-if="scope.row.customerServiceImg == null || scope.row.customerServiceImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.customerServiceImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.customerServiceImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.customerServiceImg" alt="" style="width: 500px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column> -->
			<el-table-column prop="channelImg" label="渠道图片证明">
				<template slot-scope="scope">
					<div v-if="scope.row.channelImg == null || scope.row.channelImg == ''">
						暂无图片
					</div>
					<div v-if="scope.row.channelImg" style="display:flex;flex-wrap: wrap;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img slot="reference" :src="scope.row.channelImg" alt="" style="width: 50px; height: 50px">
							<img :src="scope.row.channelImg" alt="" style="width: 500px; height: auto">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="产品照片" prop="procureImg" width="230">
				<template slot-scope="scope">
					<div v-if="scope.row.procureImg == null || scope.row.procureImg == ''">
						暂无图片
					</div>
					<div v-if="scope.row.procureImg">
						<el-popover v-for="(item,index) in scope.row.procureImg.split(',')" :key="index"
							placement="top-start" style="margin-right: 5px" title="" trigger="hover">
							<img slot="reference" :src="item" alt="" style="width: 50px; height: 50px;margin: 3px;">
							<img :src="item" alt="" style="width: 500px; height: auto">
						</el-popover>
					</div>
				</template>
			</el-table-column>

			<!-- <el-table-column prop="keyWord" label="搜索关键词" width="140">
				</el-table-column> -->
			<el-table-column prop="createTime" label="发布时间" width="160">
			</el-table-column>
			<!-- 					<el-table-column label="是否上下架" prop="createTime"  fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isEnable" @change="changeTj(scope.row.isEnable,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column> -->

			<el-table-column prop="examineRemark" label="审核说明" width="200">
			</el-table-column>
			<el-table-column prop="isFactoryReport" fixed='right' label="是否需要验厂报告" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.isFactoryReport==1">是</span>
					<span v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column fixed='right' label="状态" prop="checkCertification" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.status == 0">待审核</span>
					<span v-if="scope.row.status == 1">审核通过未成交</span>
					<span v-if="scope.row.status == 2">审核不通过</span>
					<span v-if="scope.row.status == 3">已成交</span>
					<span v-if="scope.row.status == 4">已下架</span>
					<!-- <span v-if="scope.row.state == 2">(VIP到期)</span> -->
					<el-button icon="document" size="mini" style='margin:5px;' type="primary" plain
						v-if="scope.row.status == 3" @click="statusBtn(scope.row)"
						:disabled="!isAuth('autonym:update')">修改
					</el-button>

				</template>
			</el-table-column>
			<el-table-column fixed='right' label="操作" width="180">
				<template slot-scope="scope">
					<el-button :disabled="scope.row.status !== 0 ||!isAuth('autonym:jujue')" size="mini"
						style='margin:5px;' type="primary" @click="refuseClick(scope.row)">
						审核
					</el-button>
					<el-button icon="document" size="mini" style='margin:5px;' type="primary"
						@click="addBtn(2,scope.row)" :disabled="!isAuth('autonym:update')">修改
					</el-button>
					<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
						@click="deleteBtn(scope.row)" :disabled="!isAuth('autonym:delete')">删除
					</el-button>
					<el-button size="mini" type="warning" style='margin:5px;'
						@click="updateBans(scope.row)">评论</el-button>
					<el-button size="mini" type="primary" plain style='margin:5px;' v-if="scope.row.status == 1"
						:disabled="!isAuth('autonym:update')" @click="updateStatus(scope.row)">成交</el-button>
					<el-button size="mini" type="warning" style='margin:5px;' plain
						@click="jiluBans(scope.row)">购买记录</el-button>
					<el-button icon="document" size="mini" plain style='margin:5px;' type="primary"
						@click="tuisongBtn(scope.row)" :disabled="!isAuth('autonym:tuisong')">推送消息
					</el-button>
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
		<el-dialog :visible.sync="dialogFormVisible" center title="审核">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布类型：</span>
				<span v-if="releaseType==1">平台</span>
				<span v-if="releaseType==2">代理</span>
				<span v-if="releaseType==3">普通用户</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<span v-if="releaseType==1">{{sysUserName}}</span>
				<span v-else>{{agentName}}</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">通过</el-radio>
					<el-radio :label="2">拒绝</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="status==1">
				<span style="width: 200px;display: inline-block;text-align: right;">奖励积分数：</span>
				<el-input style="width:50%;" v-model="rewardPoint" type="number" :disabled="releaseType==3" :min="0"
					placeholder="请输入奖励积分数">
				</el-input>
			</div>

			<div style="margin-bottom: 10px;" v-if="status==1">
				<span style="width: 200px;display: inline-block;text-align: right;">查看所需点数：</span>
				<el-input style="width:50%;" v-model="countMoney" type="number" :min="0" placeholder="请输入查看所需点数">
				</el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;" v-if="status==1">
					<span style="width: 200px;display: inline-block;text-align: right;">客服联系电话：</span>
					<el-input style="width:50%;" v-model="customerServicePhone" type="text" placeholder="请输入客服联系电话">
					</el-input>
				</div> -->
			<!-- <div style="margin-bottom: 10px;" v-if="status==1">
					<span style="width: 200px;display: inline-block;text-align: right;">客户微信二维码：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="customerServiceImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccessKf">
							<img v-if="customerServiceImg" :src="customerServiceImg" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div> -->
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
				<span style="width: 200px;display: inline-block;text-align: right;">发布类型：</span>
				<el-radio-group v-model="releaseType" @change="releaseTypeBtn()">
					<el-radio :label="1">平台</el-radio>
					<el-radio :label="2">代理</el-radio>
					<el-radio :label="3">普通用户</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布用户：</span>
				<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布用户">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">一级分类：</span>
				<el-select v-model="industryFirstId" placeholder="请选择类型" style="width: 50%;"
					@change="industryFirstBtn(2)">
					<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.industryName"
						:value="item.industryId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">二级分类：</span>
				<el-select v-model="industrySecondId" placeholder="请选择类型" style="width: 50%;"
					:disabled="industryFirstId==''">
					<el-option v-for="(item,index) in tableDataFer" :key="item.index" :label="item.industryName"
						:value="item.industryId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">求购标题：</span>
				<el-input style="width:50%;" v-model="title" type="text" placeholder="请输入求购标题"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">采购数：</span>
				<el-input style="width:50%;" v-model="needCount" type="text" placeholder="请输入采购数"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">单位：</span>
					<el-input style="width:50%;" v-model="needUnit" type="text" placeholder="请输入单位"></el-input>
				</div> -->
			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">预估价格(单位:万元)：</span>
					<el-input style="width:50%;" v-model="estimateMoney" type="number" :min="0"
						placeholder="请输入预估价格(单位:万元)"></el-input>
				</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">报名截止时间：</span>
				<!-- <el-input style="width:50%;" v-model="deadLineTime" type="text" placeholder="请输入报名截止时间"></el-input> -->
				<el-date-picker style="width: 50%;" v-model="deadLineTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布时间：</span>
				<!-- <el-input style="width:50%;" v-model="deadLineTime" type="text" placeholder="请输入报名截止时间"></el-input> -->
				<el-date-picker style="width: 50%;" v-model="createTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间">
				</el-date-picker>
			</div>

			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购方公司名称：</span>
					<el-input style="width:50%;" v-model="procureCompanyName" type="text"
						placeholder="请输入采购方公司名称"></el-input>
				</div> -->
			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购方联系人：</span>
					<el-input style="width:50%;" v-model="procureRealName" type="text"
						placeholder="请输入采购方联系人"></el-input>
				</div>
				

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">合作模式：</span>
					<el-radio-group v-model="cooperationModel">
						<el-radio :label="1">直接对接</el-radio>
						<el-radio :label="2">平台筛选对接</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">渠道：</span>
					<el-select v-model="channel" style="width:50%;">
						<el-option v-for="item in flxmData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select>
				</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">合作模式：</span>
				<el-radio-group v-model="cooperationModels">
					<el-radio :label="1">直接联系</el-radio>
					<el-radio :label="2">平台对接</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="cooperationModels==1">
				<span style="width: 200px;display: inline-block;text-align: right;">采购方联系电话：</span>
				<el-input style="width:50%;" v-model="procurePhone" type="number"
					placeholder="请输入采购方联系电话"></el-input>
			</div>
			<div style="margin-bottom: 10px;"  v-if="cooperationModels==1">
				<span style="width: 200px;display: inline-block;text-align: right;">微信号：</span>
				<el-input style="width:50%;" v-model="procureEmail" type="text" placeholder="请输入微信号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">供应商资质：</span>
				<span class="bqList" v-for="(item,index) in supplierQualification" :key="index">{{item}}
					<span class="delss">
						<i class="el-icon-delete" @click="clear3(index)"></i>
					</span>
				</span>
				<el-button type="info" class="tj" size="mini" @click="btnQk">重置
				</el-button></br>
				<div style="margin-left: 200px;">
					<span>资质选项：</span>
					<el-checkbox-group v-model="supplierQualification" style="display: inline-block;">
						<el-checkbox v-for="(item,index) in zhengshuData" :key="index"  style="margin: 5px;"
							:label="item.code">{{item.code}}</el-checkbox>
					</el-checkbox-group>
				</div>
				
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">查看所需点数：</span>
				<el-input style="width:50%;" v-model="countMoney" type="text" placeholder="请输入查看所需点数"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">对接人数：</span>
				<el-input style="width:50%;" v-model="peopleNum" type="text" placeholder="请输入对接人数"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">所在区域：</span>
				<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" filterable remote
					v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
				</el-cascader>

			</div>
			<!-- <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">接收详细地址：</span>
          <el-input style="width:50%;" v-model="receiveAddress" type="text" placeholder="请输入接收详细地址"></el-input>
        </div> -->
			<!-- <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">浏览次数：</span>
          <el-input style="width:50%;" v-model="browseCount" type="number" placeholder="请输入浏览次数"></el-input>
        </div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">产品描述：</span>
				<!-- <el-input style="width:50%;" v-model="describeDetails" type="textarea" :rows="3"
						placeholder="请输入描述信息"></el-input> -->
				<quill-editor ref="myTextEditor" v-model="describeDetails" :options="quillOption"
					style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
				</quill-editor>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否需要验厂报告：</span>
				<el-radio-group v-model="isFactoryReport">
					<el-radio :label="1">是</el-radio>
					<el-radio :label="2">否</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">渠道图片证明：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="channelImg"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="channelImg" :src="channelImg" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"
							style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">客户微信二维码：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="customerServiceImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess3">
							<img v-if="customerServiceImg" :src="customerServiceImg" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div> -->
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">产品照片：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in procureImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" list-type="picture-card"
							:show-file-list="false" :on-success="handleUploadSuccess1" :on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>

			</div>
			<div style="margin-bottom: 10px;" v-if="dataJson.length>0">
				<div v-for="(item,index) in dataJson" style="margin: 10px 0;">
					<span style="width: 200px;display: inline-block;text-align: right;">{{item.columnComment}}：</span>

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
					<el-date-picker style="width: 50%;" v-if="item.htmlType==6" v-model="item.columnValue" align="right"
						type="date" format="yyyy年MM月dd日" value-format="yyyy年MM月dd日" placeholder="选择日期">
					</el-date-picker>
				</div>

			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleR = false">取 消</el-button>
				<el-button type="primary" @click="refusetoUp()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
			<div style="margin:2% 0;display: inline-block;">
				<span>编号</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入编号"
					v-model="userId2"></el-input>&nbsp;&nbsp;
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
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.records">
				<el-table-column prop="userId" label="编号" width="80"></el-table-column>
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
				<el-table-column prop="createTime" width="160" label="创建时间">
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
		<el-dialog title="评论列表" :visible.sync="dialogVisibleBan" width="80%" center>
			<div>
				<el-button icon="document" size="mini" style='margin:5px;' type="primary" @click="addBtnPl(1)"
					:disabled="!isAuth('autonym:update')">添加
				</el-button>
				<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleansP">刷新
				</el-button>&nbsp;&nbsp;
			</div>
			<el-table v-loading="tableDataLoading1" :data="homeData.records">
				<el-table-column prop="commentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="120">
					<template slot-scope="scope">
						<div style="color: #2f3968;cursor: pointer;">
							{{scope.row.userName?scope.row.userName:'未绑定'}}
						</div>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="avatar" label="头像">
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
					</el-table-column> -->
				<el-table-column prop="content" label="评论内容">
				</el-table-column>
				<!-- <el-table-column prop="contentImg" label="评论图片">
						<template slot-scope="scope">
							<div v-if="scope.row.contentImg == null || scope.row.contentImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.contentImg">
								<el-popover v-for="(item,index) in scope.row.contentImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column> -->
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right' width="120">
					<template slot-scope="scope">
						<el-button icon="document" size="mini" style='margin:5px;' type="primary"
							@click="addBtnPl(2,scope.row)" :disabled="!isAuth('autonym:update')">修改
						</el-button>
						<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
							@click="deleteBtnPl(scope.row)" :disabled="!isAuth('autonym:delete')">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog :title="titles" :visible.sync="dialogFormVisiblePl" width="80%" center>
			<div style="margin-bottom: 10px;">

				<span style="width: 200px;display: inline-block;text-align: right;">昵称：</span>
				<el-input style="width:50%;" v-model="userName" type="text" placeholder="请输入昵称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评论内容：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入评论内容"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="titleImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="titleImg" :src="titleImg" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div> -->
			<!-- <div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">评论图片：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in rotationImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear1(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" list-type="picture-card"
								:show-file-list="false" :on-success="handleUploadSuccess3" :on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>

				</div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisiblePl = false">取 消</el-button>
				<el-button type="primary" @click="refusetoUpPl()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 购买记录 -->
		<el-dialog title="购买记录" :visible.sync="dialogVisibleBanJl" width="80%" center>
			<div>
				<div style="display: inline-block;margin:5px">
					<span>购买类型：</span>
					<el-select v-model="payWayJ" style="width:150px;margin-left: 10px;" @change="cleansJl()">
						<el-option v-for="(item,index) in statesnum3" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px">
					<span>用户昵称：</span>
					<el-input v-model="userNameJ" placeholder="请输入用户昵称" style="width: 200px;"
						@keydown.enter.native="cleansJl">
					</el-input>&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px">
					<span>电话：</span>
					<el-input v-model="phoneJ" placeholder="请输入电话" style="width: 200px;"
						@keydown.enter.native="cleansJl">
					</el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleansJl">刷新
				</el-button>&nbsp;&nbsp;
			</div>
			<el-table v-loading="tableDataLoading1" :data="homeData.records">
				<el-table-column prop="roleId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="120">
					<template slot-scope="scope">
						<div style="color: #2f3968;cursor: pointer;" @click="updates(scope.row)">
							{{scope.row.userName?scope.row.userName:'未绑定'}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="avatar" label="头像">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
							暂无图片
						</div>
						<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img slot="reference" :src="scope.row.avatar" alt="" style="width: 50px; height: 50px">
								<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="电话">
				</el-table-column>
				<el-table-column prop="payWay" label="购买类型">
					<template slot-scope="scope">
						<span v-if="scope.row.payWay==1">点数购买</span>
						<span v-if="scope.row.payWay==2">会员购买</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<!-- <el-table-column label="操作" fixed='right' width="120">
						<template slot-scope="scope">
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtnPl(scope.row)" :disabled="!isAuth('autonym:delete')">删除
							</el-button>
						</template>
					</el-table-column> -->
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeJl" @current-change="handleCurrentChangeJl"
					:page-sizes="[10, 20, 30, 40]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
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
			quillEditor
		},
		data() {
			return {
				limit: 10,
				page: 1,
				dataTitleT: '',
				userNameT: '',
				phoneT: '',
				content: '',
				status: '',
				rewardPoint: '',
				customerServicePhone: '',
				customerServiceImg: '',

				startTime:'',
				endTime:'',
				cityT: '',
				screen: '',
				statesnum2: [{
						value: 1,
						label: '平台发布'
					},
					{
						value: 2,
						label: '代理发布'
					},
					{
						value: 3,
						label: '普通用户发布'
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
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '审核通过未成交'
					}, {
						value: 2,
						label: '审核不通过'
					}, {
						value: 3,
						label: '已成交'
					}, {
						value: 4,
						label: '已下架'
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
				tableDataLoading1: false,
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
				dialogFormVisiblePl: false,

				title: '', //需求标题
				describeDetails: '', //需求描述
				industryFirstId: '', //行业一级id
				industrySecondId: '', //行业二级id
				needCount: '', //采购数
				needUnit: '', //单位
				estimateMoney: '', //预估价格（单位:万元）
				receiveProvince: '', //接收省
				receiveCity: '', //接收市
				receiveDistrict: '', //接收区
				receiveAddress: '', //接收详细地址
				deadLineTime: '', //报名截止时间
				createTime: '', //发布时间
				procureImg: [], //产品照片
				procureCompanyName: '', //采购方公司名称
				procureRealName: '', //采购方联系人
				procurePhone: '', //采购方联系电话
				procureEmail: '', //采购方邮箱
				channel: '', //渠道（字典）
				supplierQualification: [],
				channelImg: '', //渠道图片证明
				countMoney: '', //查看所需点数
				customerServicePhone: '', //客服联系电话
				customerServiceImg: '', //客户微信二维码
				peopleNum: '',

				statusT: '',




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
				userId2: '',
				limitU: 10,
				pageU: 1,
				userIds: {},
				nickName1: '',
				userIdss: '',
				dialogVisibleBan: false,
				banRemark: '',
				shopId: '',
				demandId: '',
				homeData: {},
				commentId: "", //评论id
				avatar: "", //头像
				userName: "", //昵称
				content: "", //评论内容
				contentImg: "", //评论图片
				createTime: "", //创建时间

				flxmData: [],
				zhengshuData: [],
				classifyIds: [],
				tableDataFer: [],
				status: 1,
				releaseType: 1,
				userId: '',
				cooperationModel: 1,
				cooperationModels:1,
				isFactoryReport:1,
				dialogVisibleBanJl: false,
				payWayJ: '',
				userNameJ: '',
				phoneJ: '',
				statesnum3: [{
						value: 1,
						label: '点数购买'
					},
					{
						value: 2,
						label: '会员购买'
					}
				],

				sysUserName: '',
				agentName: '',
				morenImg: '',
				quillOption: quillConfig,
				tableDataUser: '',
				countFb:0,
				countDlfb:0,
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
				if (tab._props.label === '采购列表') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
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
			handleSizeChange1(val) {
				this.limitU = val
				this.updateBan()
			},
			handleCurrentChange1(val) {
				this.pageU = val
				this.updateBan()
			},
			handleSizeChangeJl(val) {
				this.limitU = val
				this.updateBanJl()
			},
			handleCurrentChangeJl(val) {
				this.pageU = val
				this.updateBanJl()
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
				this.demandId = rows.demandId
				this.content = ''

				this.customerServicePhone = ''
				this.customerServiceImg = ''

				this.releaseType = rows.releaseType
				this.sysUserName = rows.sysUserName,
					this.agentName = rows.agentName
				this.countMoney = rows.countMoney
				if (this.releaseType == 3) {
					this.rewardPoint = 0

				} else {
					this.rewardPoint = 0
				}

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
				if (this.status == 1) {
					var zz = /^[1-9]\d*$|^0$/
					if (this.releaseType != 3) {
						if (this.rewardPoint === '') {

							this.$notify({
								title: '提示',
								duration: 1800,
								message: '请输入奖励积分数',
								type: 'warning'
							})
							return
						}
						if (!(zz.test(this.rewardPoint))) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '奖励积分数应为整数',
								type: 'warning'
							})
							return
						}
						if (this.countMoney === '') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '请输入查看所需点数',
								type: 'warning'
							})
							return
						}
						if (Number(this.countMoney) < 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '查看所需点数最小值为0',
								type: 'warning'
							})
							return
						}
						if (!(zz.test(this.countMoney))) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '所需点数应为整数',
								type: 'warning'
							})
							return
						}
					}


				}
				if (this.status == 2) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入审核理由',
							type: 'warning'
						})
						return
					}
				}

				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/examineDemand'),
					method: 'POST',
					params: this.$http.adornParams({
						'status': this.status,
						'demandId': this.demandId,
						'examineRemark': this.content,
						'rewardPoint': this.rewardPoint,
						'customerServicePhone': this.customerServicePhone,
						'customerServiceImg': this.customerServiceImg,
						'countMoney': this.countMoney,
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
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
				this.dataSelectUser()
			},
			// 重置
			cleans() {
				this.statusT = ''
				this.dataTitleT = ''
				this.cityT = ''
				this.userNameT = ''
				this.phoneT = ''
				this.startTime = ''
				this.endTime = ''
				
				
				this.classIdT = ''
				this.screen = ''
				this.dataSelect()
				this.dataSelectW()
				// this.getClassList()
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
						'industryFirstName': this.phoneT,
						'industrySecondName': this.cityT,
						'status': this.statusT,
						'releaseType': this.screen,
						'sysUserName': '',
						'agentName': this.userNameT,
						'startTime': this.startTime,
						'endTime': this.endTime,
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
				this.xianshi()
				if (index == 1) {
					this.titles = '添加'
					this.demandId = ''
					this.title = ''
					this.describeDetails = ''
					this.industryFirstId = ''
					this.industrySecondId = ''
					this.needCount = ''
					this.needUnit = ''
					this.estimateMoney = ''
					this.receiveProvince = ''
					this.receiveCity = ''
					this.receiveDistrict = ''
					this.receiveAddress = ''
					this.deadLineTime = ''
					this.createTime = ''
					this.procureImg = []
					this.procureCompanyName = ''
					this.procureRealName = ''
					this.procurePhone = ''
					this.procureEmail = ''
					this.channel = ''
					this.supplierQualification = []
					this.channelImg = ''
					this.countMoney = ''
					this.peopleNum = ''
					this.customerServicePhone = ''
					this.customerServiceImg = ''
					this.status = 1
					this.storeAddress = []
					this.storeAddre = '请选择城市'
					this.releaseType = 1
					this.userId = ''
					this.nickName1 = ''
					// this.cooperationModel = 1
					this.cooperationModels = 1
					
					this.isFactoryReport = 1
					// if(this.releaseType==2){
					// 	this.countMoney = this.countDlfb
					// }else{
						this.countMoney = this.countFb
					// }
				} else {
					this.titles = '修改'
					this.demandId = row.demandId
					this.title = row.title
					this.describeDetails = row.describeDetails
					this.industryFirstId = row.industryFirstId
					this.industrySecondId = row.industrySecondId
					this.needCount = row.needCount
					this.needUnit = row.needUnit
					this.estimateMoney = row.estimateMoney
					this.receiveProvince = row.receiveProvince
					this.receiveCity = row.receiveCity
					this.receiveDistrict = row.receiveDistrict
					this.receiveAddress = row.receiveAddress
					this.deadLineTime = row.deadLineTime
					this.createTime = row.createTime
					if (row.procureImg) {
						this.procureImg = row.procureImg.split(',')
					} else {
						this.procureImg = []
					}
					if (row.supplierQualification) {
						this.supplierQualification = row.supplierQualification.split(',')
					} else {
						this.supplierQualification = []
					}

					this.procureCompanyName = row.procureCompanyName
					this.procureRealName = row.procureRealName
					this.procurePhone = row.procurePhone
					this.procureEmail = row.procureEmail
					this.channel = row.channel
					this.channelImg = row.channelImg
					this.countMoney = row.countMoney
					this.customerServicePhone = row.customerServicePhone
					this.customerServiceImg = row.customerServiceImg
					if (row.status != '' || row.status != null) {
						this.status = row.status
					} else {
						this.status = 1
					}
					if (row.releaseType) {
						this.releaseType = row.releaseType
					} else {
						this.releaseType = 1
					}
					if (row.userId) {
						this.userId = row.userId
					} else {
						this.userId = ''
					}
					// if (row.cooperationModel) {
					// 	this.cooperationModel = row.cooperationModel
					// } else {
					// 	this.cooperationModel = 1
					// }
					if (row.cooperationModels) {
						this.cooperationModels = row.cooperationModels
					} else {
						this.cooperationModels = 1
					}
					
					if (row.isFactoryReport) {
						this.isFactoryReport = row.isFactoryReport
					} else {
						this.isFactoryReport = 2
					}
					
					if (row.peopleNum) {
						this.peopleNum = row.peopleNum
					} else {
						this.peopleNum = ''
					}
					this.nickName1 = row.agentName
					this.storeAddress = []
					this.storeAddre = this.receiveProvince + '/' + this.receiveCity + '/' + this.receiveDistrict

				}
				this.flxmSelect()
				this.dataSelectW()
				this.dataSelectFer()
				this.dialogFormVisibleR = true
			},
			// 确认 添加、修改
			refusetoUp() {
				if (this.industryFirstId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择一级分类',
						type: 'warning'
					})
					return
				}
				if (this.industrySecondId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择二级分类',
						type: 'warning'
					})
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '求购标题不能为空',
						type: 'warning'
					})
					return
				}

				if (this.needCount === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '采购数不能为空',
						type: 'warning'
					})
					return
				}
				// if (this.needUnit === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '单位不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if(this.cooperationModels==1){
					if (this.procurePhone === ''&&this.procureEmail==='') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '采购方联系电话和微信号不能同时为空',
							type: 'warning'
						})
						return
					}
				}
				
				// if (this.estimateMoney === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '预估价格不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.deadLineTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '报名截止时间不能为空',
						type: 'warning'
					})
					return
				}
				// if (this.procureCompanyName == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '采购方公司名称不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.procureRealName == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '采购方联系人不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.channel == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '渠道不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.countMoney === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '查看所需点数不能为空',
						type: 'warning'
					})
					return
				}
				if (this.receiveProvince == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择所在区域',
						type: 'warning'
					})
					return
				}
				if (this.describeDetails == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '产品描述不能为空',
						type: 'warning'
					})
					return
				}


				// if (this.channelImg == ''||this.channelImg ==null) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传渠道图片证明',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.peopleNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入对接人数',
						type: 'warning'
					})
					return
				}
				if (this.procureImg.length == 0) {
					// this.$notify({
					// 	title: '提示',
					// 	duration: 1800,
					// 	message: '请上传产品照片',
					// 	type: 'warning'
					// })
					// return
					this.procureImg.push(this.morenImg)

				}


				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/saveProcureDemand'),
					method: 'post',
					data: this.$http.adornData({
						'classId': this.classId,
						'demandId': this.demandId,
						'title': this.title,
						'describeDetails': this.describeDetails,
						'industryFirstId': this.industryFirstId,
						'industrySecondId': this.industrySecondId,
						'needCount': this.needCount,
						'needUnit': this.needUnit,
						'estimateMoney': this.estimateMoney,
						'receiveProvince': this.receiveProvince,
						'receiveCity': this.receiveCity,
						'receiveDistrict': this.receiveDistrict,
						'receiveAddress': this.receiveAddress,
						'deadLineTime': this.deadLineTime,
						'procureImg': this.procureImg.toString(),
						'procureCompanyName': this.procureCompanyName,
						'procureRealName': this.procureRealName,
						'procurePhone': this.procurePhone,
						'procureEmail': this.procureEmail,
						'channel': this.channel,
						'channelImg': this.channelImg,
						'countMoney': this.countMoney,
						'customerServicePhone': this.customerServicePhone,
						'customerServiceImg': this.customerServiceImg,
						'status': this.status,
						'releaseType': this.releaseType,
						'userId': this.userId,
						// 'cooperationModel': this.cooperationModel,
						'cooperationModels': this.cooperationModels,
						'peopleNum': this.peopleNum,
						'createTime': this.createTime,
						'agentName': this.nickName1,
						'supplierQualification': this.supplierQualification.toString(),
						'isFactoryReport':this.isFactoryReport,

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
			handleAvatarSuccessKf(file, fileList) {
				this.customerServiceImg = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.channelImg = file.data
			},
			handleAvatarSuccess3(file, fileList) {
				this.customerServiceImg = file.data
			},
			handleUploadSuccess1(file, fileList) {
				this.procureImg.push(file.data)
				console.log('this.contentImg', this.rotationImg)
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
			clear3(index){
				this.supplierQualification.splice(index, 1);
			},
			btnQk(){
				this.supplierQualification = []
			},
			// 删除标签
			clear2(index) {
				this.procureImg.splice(index, 1);
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
				this.receiveProvince = value[0]
				this.receiveCity = value[1]
				this.receiveDistrict = value[2]
				if (this.receiveCity == '市辖区') {
					this.receiveCity = this.receiveProvince
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
				this.userId2 = ''
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
						'userId': this.userId2,
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
				this.userId2 = ''
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

			// 删除
			deleteBtn(row) {
				this.$confirm('确定删除该信息吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/procureDemand/deleteProcureDemandById'),
						method: 'post',
						params: this.$http.adornParams({
							'demandId': row.demandId
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
			// 推送消息
			tuisongBtn(row) {
				this.$confirm('确定发送推送信息？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/procureDemand/sendMessage'),
						method: 'get',
						params: this.$http.adornParams({
							'demandId': row.demandId
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
				}).catch(() => {});
			},
			//打开评论列表
			updateBans(row) {
				this.demandId = row.demandId
				this.pageU = 1
				this.updateBan()
				this.dialogVisibleBan = true
			},
			cleansP() {
				this.pageU = 1
				this.updateBan()
			},
			//评论列表
			updateBan() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/procureDemandComment/getProcureDemandCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'demandId': this.demandId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					if (data.code == 0) {
						this.homeData = data.data
					}
				})
			},
			// 添加、修改 评论
			addBtnPl(index, row) {
				if (index == 1) {
					this.titles = '添加评论'
					this.commentId = ''
					this.titleImg = ''
					this.userName = ''
					this.content = ''
					this.rotationImg = []
				} else {
					this.titles = '修改评论'
					this.commentId = row.commentId
					this.titleImg = row.avatar
					this.userName = row.userName
					this.content = row.content
					if (row.contentImg) {
						this.rotationImg = row.contentImg.split(',')

					} else {
						this.rotationImg = []

					}

				}
				this.dialogFormVisiblePl = true
			},
			// 确认 添加、修改
			refusetoUpPl() {

				if (this.userName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入用户昵称',
						type: 'warning'
					})
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入评论内容',
						type: 'warning'
					})
					return
				}
				// if (this.titleImg == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传头像',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.rotationImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传评论图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }


				this.$http({
					url: this.$http.adornUrl('admin/procureDemandComment/saveProcureDemandComment'),
					method: 'post',
					data: this.$http.adornData({
						'commentId': this.commentId,
						'avatar': this.titleImg,
						'userName': this.userName,
						'content': this.content,
						'contentImg': this.rotationImg.toString(),
						'demandId': this.demandId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.$message({
							type: 'success',
							message: '操作成功'
						})
						this.updateBan()
						this.dialogFormVisiblePl = false
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				});
			},
			// 删除
			deleteBtnPl(row) {
				this.$confirm('确定删除该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							'admin/procureDemandComment/deleteProcureDemandCommentById'),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.updateBan()
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
			// 渠道
			flxmSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '渠道'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.flxmData = returnData
				})
				// 供应商资质
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '证书管理'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.zhengshuData = returnData
				})
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
					console.log('index', index, this.phoneT)
					for (var i in this.classifyIds) {
						if (this.phoneT == this.classifyIds[i].industryName) {
							this.industryFirstId = this.classifyIds[i].industryId
							this.cityT = ''
							this.select()
							this.dataSelectFer()
						}
					}

				} else {
					this.industrySecondId = ''
					this.dataSelectFer()
				}

			},
			dataSelect1() {
				this.page = 1
				console.log('--------------', this.statusT, this.$route.query.status)
				if (this.$route.query.status != null) {
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = ''
				}
				if (this.$route.query.title != null) {
					this.dataTitleT = this.$route.query.title
				} else {
					this.dataTitleT = ''
				}
				this.dataSelectW()
				this.dataSelect()

			},
			// 成交
			updateStatus(row) {
				this.$confirm('确定该需求已成交？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/procureDemand/saveProcureDemand'),
						method: 'post',
						data: this.$http.adornData({
							'demandId': row.demandId,
							'status': 3,
							'classId': row.classId,
							'title': row.title,
							'describeDetails': row.describeDetails,
							'industryFirstId': row.industryFirstId,
							'industrySecondId': row.industrySecondId,
							'needCount': row.needCount,
							'needUnit': row.needUnit,
							'estimateMoney': row.estimateMoney,
							'receiveProvince': row.receiveProvince,
							'receiveCity': row.receiveCity,
							'receiveDistrict': row.receiveDistrict,
							'receiveAddress': row.receiveAddress,
							'deadLineTime': row.deadLineTime,
							'procureImg': row.procureImg,
							'procureCompanyName': row.procureCompanyName,
							'procureRealName': row.procureRealName,
							'procurePhone': row.procurePhone,
							'procureEmail': row.procureEmail,
							'channel': row.channel,
							'channelImg': row.channelImg,
							'countMoney': row.countMoney,
							'customerServicePhone': row.customerServicePhone,
							'customerServiceImg': row.customerServiceImg,
							'releaseType': row.releaseType,
							// 'cooperationModel': row.cooperationModel,
							'cooperationModels': row.cooperationModels,
							'peopleNum': row.peopleNum,
							'createTime': row.createTime,
							'supplierQualification': row.supplierQualification,
							'isFactoryReport':row.isFactoryReport,
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
			statusBtn(row) {
				this.$confirm('确定该需求未成交？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/procureDemand/saveProcureDemand'),
						method: 'post',
						data: this.$http.adornData({
							'demandId': row.demandId,
							'status': 1,
							'classId': row.classId,
							'title': row.title,
							'describeDetails': row.describeDetails,
							'industryFirstId': row.industryFirstId,
							'industrySecondId': row.industrySecondId,
							'needCount': row.needCount,
							'needUnit': row.needUnit,
							'estimateMoney': row.estimateMoney,
							'receiveProvince': row.receiveProvince,
							'receiveCity': row.receiveCity,
							'receiveDistrict': row.receiveDistrict,
							'receiveAddress': row.receiveAddress,
							'deadLineTime': row.deadLineTime,
							'procureImg': row.procureImg,
							'procureCompanyName': row.procureCompanyName,
							'procureRealName': row.procureRealName,
							'procurePhone': row.procurePhone,
							'procureEmail': row.procureEmail,
							'channel': row.channel,
							'channelImg': row.channelImg,
							'countMoney': row.countMoney,
							'customerServicePhone': row.customerServicePhone,
							'customerServiceImg': row.customerServiceImg,
							'releaseType': row.releaseType,
							// 'cooperationModel': row.cooperationModel,
							'cooperationModels': row.cooperationModels,
							'peopleNum': row.peopleNum,
							'createTime': row.createTime,
							'supplierQualification': row.supplierQualification,
							'isFactoryReport':row.isFactoryReport,
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
			//打开查看记录
			jiluBans(row) {
				this.demandId = row.demandId
				this.pageU = 1
				this.updateBanJl()
				this.dialogVisibleBanJl = true
			},
			cleansJl() {
				this.pageU = 1
				this.updateBanJl()
			},
			//查看记录列表
			updateBanJl() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/procureDemandRole/selectAdminProcureDemandRoleList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'demandId': this.demandId,
						'payWay': this.payWayJ,
						'userName': this.userNameJ,
						'phone': this.phoneJ,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					if (data.code == 0) {
						this.homeData = data.data
					}
				})
			},
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('common/type/433'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.morenImg = data.data.value
					}
				})
				//发布采购点数
				this.$http({
					url: this.$http.adornUrl('common/type/441'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.countFb = data.data.value
					}
				})
				
			},
			// 获取服务数据列表
			dataSelectUser() {
				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/selectProcureDemandRealName'),
					method: 'get',
					params: this.$http.adornParams({
						'title': this.dataTitleT,
						'industryFirstName': this.phoneT,
						'industrySecondName': this.cityT,
						'status': this.statusT,
						'releaseType': this.screen,
						'sysUserName': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUser = returnData
				})
			},
			select1() {
				this.page = 1
				this.dataSelect()
				this.dataSelectUser()
			},
			releaseTypeBtn(){
				// if(this.releaseType==2){
				// 	this.countMoney = this.countDlfb
				// }else{
					this.countMoney = this.countFb
				// }
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/excelProcureDemandList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'title': this.dataTitleT,
						'industryFirstName': this.phoneT,
						'industrySecondName': this.cityT,
						'status': this.statusT,
						'releaseType': this.screen,
						'sysUserName': '',
						'agentName': this.userNameT,
						'startTime': this.startTime,
						'endTime': this.endTime,
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
						elink.download = '采购列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
		},
		mounted() {
			this.page = 1
			if (this.$route.query.status != null) {
				this.statusT = Number(this.$route.query.status)
			} else {
				this.statusT = ''
			}
			if (this.$route.query.title != null) {
				this.dataTitleT = this.$route.query.title
			} else {
				this.dataTitleT = ''
			}

			//分类列表
			// this.getClassList()
			this.dataSelectW()
			//服务列表
			this.dataSelect()
			this.xianshi()
			this.dataSelectUser()
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