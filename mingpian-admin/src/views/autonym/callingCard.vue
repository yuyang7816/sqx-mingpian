<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">采购名片</span>
			<h2 class="nf-page-hero__title">采购名片列表</h2>
			<p class="nf-page-hero__desc">管理采购名片发布与审核，支持按状态、昵称、电话、姓名、采购电话及岗位筛选查询</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="statusT" class="nf-filter-form__input" @change="select()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label"
							:value="item.value">
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
					<span class="nf-filter-form__label">电话</span>
					<el-input v-model="phoneT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入电话" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">姓名</span>
					<el-input v-model="realNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入姓名" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">采购电话</span>
					<el-input v-model="purchasePhoneT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入采购电话" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">岗位</span>
					<el-select v-model="dataTitleT" class="nf-filter-form__input nf-filter-form__input--wide"
						@change="select()">
						<el-option v-for="item in gangweiData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="select">查询</el-button>
					<el-button icon="document" type="primary" @click="cleans">重置</el-button>
					<el-button icon="document" type="primary" :disabled="!isAuth('callingCard:add')"
						@click="addBtn(1)">添加</el-button>
				</div>
			</div>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="采购名片列表" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column label="编号" prop="purchaseCardId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="用户昵称" prop="userName">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="电话" prop="phone">
					</el-table-column>
					<el-table-column label="采购头像" prop="purchaseAvatar" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.purchaseAvatar == null || scope.row.purchaseAvatar == ''">
								暂无图片
							</div>
							<div v-if="scope.row.purchaseAvatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.purchaseAvatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.purchaseAvatar" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="姓名" prop="purchaseName">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">{{
									scope.row.purchaseName ? scope.row.purchaseName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<!-- <el-table-column label="采购电话" prop="purchasePhone">
					</el-table-column> -->
					<el-table-column label="公司名称" prop="companyName">
					</el-table-column>
					<el-table-column label="岗位" prop="postName">
					</el-table-column>
					<!-- <el-table-column label="微信号" prop="purchaseWxCode">
					</el-table-column> -->
					<el-table-column label="岗位证明" prop="purchaseWxImg" width="100">
						<template slot-scope="scope">
							<div
								v-if="scope.row.purchaseWxImg == null || scope.row.purchaseWxImg == ''||scope.row.purchaseWxImg == 'null' ">
								暂无图片
							</div>
							<div v-else-if="scope.row.purchaseWxImg">
								<el-popover v-for="(item,index) in scope.row.purchaseWxImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="采购物料" prop="purchaseContent" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" style="margin-right: 5px" title="" trigger="hover">
								<div slot="reference"
									style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
									{{scope.row.purchaseContent}}
								</div>
								<div style="width: 400px;height: auto;">{{scope.row.purchaseContent}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="公司简介" prop="companyDetails" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" style="margin-right: 5px" title="" trigger="hover">
								<div slot="reference"
									style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
									{{scope.row.companyDetails}}
								</div>
								<div style="width: 400px;height: auto;" v-html="scope.row.companyDetails"></div>
							</el-popover>
						</template>
					</el-table-column>
					<!-- <el-table-column label="公司图片" prop="companyImg" width="300">
						<template slot-scope="scope">
							<div
								v-if="scope.row.companyImg == null || scope.row.companyImg == ''||scope.row.companyImg == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.companyImg">
								<el-popover v-for="(item,index) in scope.row.companyImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column label="地址" prop="province" width="200">
						<template slot-scope="scope">
							<span v-if="scope.row.province">{{ scope.row.province + '' + scope.row.city + '' + scope.row.district + '' + scope.row.address }}</span>
							<span v-else>-</span>
						</template>
					</el-table-column>
					<!-- <el-table-column label="所需点数" prop="countMoney">
					</el-table-column> -->
					<el-table-column label="奖励积分数" prop="rewardPoints">
					</el-table-column>
					<el-table-column label="注册资本" prop="registeredCapital">
					</el-table-column>
					<el-table-column label="标签" prop="label" width="200">
					</el-table-column>
					<!-- <el-table-column label="拜访所需金额" prop="countMoney">
					</el-table-column> -->
					<el-table-column label="审核备注" prop="auditContent" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.auditContent?scope.row.auditContent:'暂无'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="创建时间" prop="createTime" width="160">
					</el-table-column>
					<el-table-column fixed='right' label="状态" prop="status" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1">待审核</span>
							<span v-if="scope.row.status == 2">审核成功</span>
							<span v-if="scope.row.status == 3">审核未通过</span>
						</template>
					</el-table-column>
					<el-table-column label="是否上下架" prop="isEnable" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isEnable" @change="changeTj(scope.row)"
								:disabled="!isAuth('callingCard:update')" :active-value="openValue"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<el-button v-if="scope.row.status == 1 " :disabled=" !isAuth('callingCard:shenhe')"
								size="mini" style='margin:5px;' type="primary" @click="refuseClick(scope.row)">审核
							</el-button>
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(2,scope.row)" :disabled="!isAuth('callingCard:update')">修改
							</el-button>
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" :disabled="!isAuth('callingCard:delete')">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
					<div class="nf-table-card__foot">
						<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
							:total="tableData.totalCount" layout="total,sizes, prev, pager, next,jumper"
							@size-change="handleSizeChange" @current-change="handleCurrentChange">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
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
				<!-- <div style="margin-bottom: 10px;" v-if="status==2">
					<span style="width: 200px;display: inline-block;text-align: right;">所需点数：</span>
					<el-input style="width:50%;" v-model="countMoney" type="number" :min="0" placeholder="请输入所需点数">
					</el-input>
				</div> -->
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
					<span style="width: 200px;display: inline-block;text-align: right;">发布用户：</span>
					<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布用户">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
					<el-input style="width:50%;" v-model="purchaseName" type="text" placeholder="请输入姓名"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购电话：</span>
					<el-input style="width:50%;" v-model="purchasePhone" type="number" placeholder="请输入采购电话"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">微信号：</span>
					<el-input style="width:50%;" v-model="purchaseWxCode" type="text" placeholder="请输入微信号"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购头像：</span>
					<div style="display: inline-block;" class="imgs">
						<el-upload v-model="purchaseAvatar" action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
							:show-file-list="false" :on-success="handleUploadSuccess2">
							<img v-if="purchaseAvatar" :src="purchaseAvatar" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">岗位证明：</span>
					<div style="display: inline-block;" class="imgs">
						<el-upload v-model="purchaseWxImg" action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
							:show-file-list="false" :on-success="handleAvatarSuccess1">
							<img v-if="purchaseWxImg" :src="purchaseWxImg" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<!-- <div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司图片：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in companyImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear4(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								class="avatar-uploader" list-type="picture-card" :show-file-list="false"
								:on-success="handleUploadSuccess4" :on-progress="onprogress3">
								<el-progress v-if="percentage3>0 && percentage3<100" type="circle"
									:percentage="percentage3"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司名称：</span>
					<el-input style="width:50%;" v-model="companyName" type="text" placeholder="请输入公司名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">注册资本：</span>
					<el-input style="width:50%;" v-model="registeredCapital" type="text"
						placeholder="请输入注册资本"></el-input>
					<!-- <el-select v-model="registeredCapital" style="width:50%;">
						<el-option v-for="item in zhangqiData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select> -->
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">岗位：</span>
					<!-- <el-input style="width:50%;" v-model="postName" type="text" placeholder="请输入岗位"></el-input> -->
					<el-select v-model="postName" style="width:50%;">
						<el-option v-for="item in gangweiData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购物料：</span>
					<el-input style="width:50%;" v-model="purchaseContent" type="textarea" :rows="3"
						placeholder="请输入采购物料"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">所需点数：</span>
					<el-input style="width:50%;" v-model="countMoney" type="number" placeholder="请输入所需点数"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">奖励积分数：</span>
					<el-input style="width:50%;" v-model="rewardPoint" type="text" placeholder="请输入奖励积分数"></el-input>
				</div>
				
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">拜访所需金额：</span>
					<el-select v-model="countMoney" style="width:50%;">
						<el-option v-for="item in xinghaoData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
					<span class="bqList" v-for="(item,index) in companyFlair" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</span>
					<el-button type="info" class="tj" size="mini" @click="btnQk">重置
					</el-button></br>
					<div style="margin-left: 200px;">
						<span>标签选项：</span>
						<el-checkbox-group v-model="companyFlair" style="display: inline-block;">
							<el-checkbox v-for="(item,index) in biaoqianData" :key="index" style="margin: 5px;"
								:label="item.code">{{item.code}}</el-checkbox>
						</el-checkbox-group>
					</div>
					
					
					<!-- <span class="bqList" v-for="(item,index) in companyFlair" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</span>
					<el-input v-model="bq" style="width:120px;" placeholder="请输入标签"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-plus" @click="btnTj"> 添加
					</el-button> -->
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司简介：</span>
					<!-- <el-input style="width:50%;" v-model="companyDetails" type="text" placeholder="请输入公司简介"></el-input> -->
					<quill-editor ref="myTextEditor" v-model="companyDetails" :options="quillOption"
					  style="padding-bottom: 30px;width: 50%;display: inline-table;">
					</quill-editor>
				</div>
				

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司地区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" filterable remote
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
					<el-input style="width:50%;" @keydown.enter.native="selectDz" v-model="address" type="text"
						placeholder="请输入公司详细地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
					<el-input v-model="latitude" style="width:50%;" disabled placeholder="请输入纬度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
					<el-input v-model="longitude" style="width:50%;" disabled placeholder="请输入经度"></el-input>
				</div>
				<div>
					<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleR = false">取 消</el-button>
					<el-button type="primary" @click="refusetoUp()">确 定</el-button>
				</div>

			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号</span>
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<span>昵称</span>
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
					<el-table-column label="头像" prop="avatar" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
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
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
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
				realNameT: '',
				purchasePhoneT: '',
				phoneT: '',
				industryFirstNameT: '',
				industrySecondNameT: '',
				content: '',
				status: '',
				statusT: '',
				cityT: '',
				screen: '',
				statesnum2: [{
						value: 1,
						label: '个人'
					},
					{
						value: 2,
						label: '企业'
					},
					{
						value: 0,
						label: '未认证'
					}
				],
				statesnum: [{
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
					}, {
						value: 3,
						label: '审核未通过'
					},
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
				titles: '添加',
				dialogFormVisibleR: false,
				purchaseCardId: '', //名片id
				userId: '', //用户id
				userType: 1, //用户类型 1个人认证 2企业认证
				cardStatus: 1, //状态 0待审核 1审核成功 2审核未通过 3会员到期已下架
				examineRemark: '', //审核备注
				purchasePhone: '', //联系电话
				purchaseName: '', //真实姓名
				companyName: '', //公司名称
				postName: '', //所属岗位(字典)
				province: '', //公司省
				city: '', //公司市
				district: '', //公司区
				address: '', //公司详细地址
				latitude: '', //纬度
				longitude: '', //经度
				firstProductImg: '', //主营产品图
				purchaseWxCode: '', //微信号
				purchaseWxImg: '', //微信图片
				purchaseAvatar: '', //采购头像
				companyAmbient: [], //公司环境
				companyOutputValue: '', //年产值(单位:万元)
				companyWebsite: '', //公司官网
				companyFlairImg: [], //资质认证图片
				companyFlair: [], //资质认证
				purchaseContent: '', //采购物料
				firstProduct: '', //主营产品
				countMoney: 0, //浏览次数
				industryFirstId: '', //行业一级id
				industrySecondId: '', //行业二级id
				industryFirstName: '', //行业一级名称
				industrySecondName: '', //行业二级名称‘
				keyWord: '', //搜索关键词
				companyFlairArr: [],
				bqImg: '',
				registeredCapital: '',//注册资本
				payVisitMoney: '',//拜访所需金额
				companyDetails: '',//公司简介
				companyImg: [],//公司图片
				label:'',//标签



				dataJson: [], //分类动态字段
				percentage1: 0,
				percentage2: 0,
				percentage3: 0,
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

				flxmData: [],
				classifyIds: [],
				tableDataFer: [],
				dialogVisibleZzrz: false,
				quillOption: quillConfig,
				rewardPoint: '',
				// countMoney: '', //加群所需点数
				
				zhangqiData: [],
				xinghaoData:[],
				biaoqianData:[],
				gangweiData:[],
				

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
				if (tab._props.label === '采购名片列表') {
					this.page = 1
					this.limit = 10
					this.statusT = ''
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

				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('admin/purchaseCard/auditPurchaseCard'),
					method: 'post',
					data: this.$http.adornData({
						'status': 2,
						'purchaseCardId': row.purchaseCardId,
						'auditContent': ''
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
							onClose: () => {}
						})
					}

				})
			},
			//审核拒绝
			refuseClick(rows) {
				this.status = ''
				this.rewardPoint = 0
				this.helpTakeId = rows.purchaseCardId
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
				if (this.status == 2) {
					if (this.rewardPoint === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入奖励积分数',
							type: 'warning'
						})
						return
					}
					if (Number(this.rewardPoint) < 0) {
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
					// if (Number(this.countMoney) < 0) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '加群所需点数必须大于或者等于0',
					// 		type: 'warning'
					// 	})
					// 	return
					// }

				}
				if (this.status == 3) {
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
					url: this.$http.adornUrl('admin/purchaseCard/auditPurchaseCard'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'purchaseCardId': this.helpTakeId,
						'auditContent': this.content,
						'countMoney': this.countMoney,
						'rewardPoint': this.rewardPoint,
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
							type: 'error',
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
			},
			// 重置
			cleans() {
				this.statusT = ''
				this.dataTitleT = ''
				this.userNameT = ''
				this.realNameT = ''
				this.purchasePhoneT = ''
				this.industryFirstNameT = ''
				this.industrySecondNameT = ''
				this.phoneT = ''
				this.screen = ''
				this.dataSelect()
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
					url: this.$http.adornUrl('admin/purchaseCard/selectPurchaseCardList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'postName': this.dataTitleT,
						'purchaseName': this.realNameT,
						'status': this.statusT,
						'purchasePhone': this.purchasePhoneT,
						'userName': this.userNameT,
						'phone': this.phoneT,


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
			// 上下架
			changeTj(row) {
				this.$http({
					url: this.$http.adornUrl('admin/purchaseCard/upOrDownNews'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'purchaseCardId': row.purchaseCardId,
						'downRemark': '',
						'isEnable': row.isEnable,
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

					this.purchaseCardId = ''
					this.userId = ''
					this.purchasePhone = ''
					this.purchaseName = ''
					this.companyName = ''
					this.postName = ''
					this.countMoney = 0
					this.rewardPoint = ''
					this.purchaseWxCode = ''
					this.purchaseWxImg = ''
					this.purchaseAvatar = ''
					this.purchaseContent = ''
					this.nickName1 = ''
					this.province = ''
					this.city = ''
					this.district = ''
					this.address = ''
					this.longitude = ''
					this.latitude = ''
					this.storeAddress = []
					this.storeAddre = '请选择城市'
					this.companyFlair = []
					this.bq = ''
					this.registeredCapital = ''
					this.payVisitMoney = ''
					this.companyDetails = ''
					this.companyImg = []


				} else {
					this.titles = '修改'
					this.purchaseCardId = row.purchaseCardId
					this.userId = row.userId

					this.purchasePhone = row.purchasePhone
					this.purchaseName = row.purchaseName
					this.companyName = row.companyName
					this.postName = row.postName
					this.countMoney = row.countMoney
					this.rewardPoint = row.rewardPoints
					this.purchaseWxCode = row.purchaseWxCode
					this.purchaseWxImg = row.purchaseWxImg
					this.purchaseAvatar = row.purchaseAvatar
					if (row.label) {
						this.companyFlair = row.label.split(',')
					} else {
						this.companyFlair = []
					}

					this.purchaseContent = row.purchaseContent
					this.nickName1 = row.userName

					this.province = row.province
					this.city = row.city
					this.district = row.district
					this.address = row.address
					this.longitude = row.longitude
					this.latitude = row.latitude
					this.storeAddress = []
					this.storeAddre = this.province + '/' + this.city + '/' + this.district
					this.bq = ''
					this.registeredCapital = row.registeredCapital
					this.payVisitMoney = row.payVisitMoney
					this.companyDetails = row.companyDetails
					if (row.companyImg) {
						this.companyImg = row.companyImg.split(',')
					} else {
						this.companyImg = []
					}









				}
				this.flxmSelect()
				this.getMyLocation()

				this.dialogFormVisibleR = true
			},
			// 确认 添加、修改
			refusetoUp() {
				// if (this.userIdss == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择发布人',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.classId == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择服务分类',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.titleImg == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入标题',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.phone == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入联系电话',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.titleImg == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传封面图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.rotationImg.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传轮播图',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.label.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入标签',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.province == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择服务地区',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.priceInfo === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入价格信息',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.browseCount === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入浏览记录',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.details == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入描述信息',
				// 		type: 'warning'
				// 	})
				// 	return
				// }

				this.$http({
					url: this.$http.adornUrl('admin/purchaseCard/insertPurchaseCard'),
					method: 'post',
					data: this.$http.adornData({
						'purchaseCardId': this.purchaseCardId,
						'userId': this.userId,
						'purchasePhone': this.purchasePhone,
						'purchaseName': this.purchaseName,
						'companyName': this.companyName,
						'postName': this.postName,
						'countMoney': this.countMoney,
						'purchaseWxCode': this.purchaseWxCode,
						'purchaseWxImg': this.purchaseWxImg,
						'purchaseAvatar': this.purchaseAvatar,
						'purchaseContent': this.purchaseContent,
						'rewardPoints': this.rewardPoint,
						'registeredCapital': this.registeredCapital,
						'payVisitMoney': this.payVisitMoney,
						'companyDetails': this.companyDetails,
						'companyImg': this.companyImg.toString(),
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'address': this.address,
						'longitude': this.longitude,
						'latitude': this.latitude,
						'label': this.companyFlair.toString()

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
				this.purchaseWxImg = file.data
			},
			handleUploadSuccess2(file, fileList) {
				// this.companyFlairImg.push(file.data)
				this.purchaseAvatar = file.data
			},
			//上传轮播图
			handleUploadSuccess3(file, fileList) {
				this.firstProductImg.push(file.data)
			},
			//上传轮播图
			handleUploadSuccess4(file, fileList) {
				this.companyImg.push(file.data)
			},
			onprogress1(event, file, fileList) {
				this.percentage1 = parseInt(event.percent)
			},
			onprogress2(event, file, fileList) {
				this.percentage2 = parseInt(event.percent)
			},
			onprogress3(event, file, fileList) {
				this.percentage3 = parseInt(event.percent)
			},
			// 删除轮播图
			clear1(index) {
				this.companyFlairImg.splice(index, 1);
			},
			// 删除轮播图
			clear3(index) {
				this.firstProductImg.splice(index, 1);
			},
			// 删除轮播图
			clear4(index) {
				this.companyImg.splice(index, 1);
			},
			// 删除标签
			clear2(index) {
				this.companyFlairArr.splice(index, 1);
				this.companyFlair.splice(index, 1);
				this.companyFlairImg.splice(index, 1);
			},
			// 清空标签
			btnQk(){
				this.companyFlair = []
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
					this.companyFlair.push(this.bq)
					this.bq = ''
					// this.bqImg = ''
					this.dialogVisibleZzrz = true
					}
				},
				refusetoZzrz() {
						if (this.bq == '' || this.bq == ' ') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '请输入标签',
								type: 'warning'
							});
							return
						}
						if (this.bqImg == '') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '请上传认证图片',
								type: 'warning'
							});
							return
						}
						var datas = {
							img: this.bqImg,
							text: this.bq
						}
						this.companyFlairArr.push(datas)
						this.companyFlair.push(this.bq)
						this.companyFlairImg.push(this.bqImg)
						this.dialogVisibleZzrz = false

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
						this.userId = row.userId
						this.nickName1 = row.userName
						this.purchaseAvatar = row.avatar
						this.purchasePhone = row.phone
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
						this.$confirm('确定删除该信息吗？', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							this.$http({
								url: this.$http.adornUrl('admin/purchaseCard/deletePurchaseCard'),
								method: 'POST',
								params: this.$http.adornParams({
									'purchaseCardId': row.purchaseCardId
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
					industryFirstBtn() {
						this.industrySecondId = ''
						this.dataSelectFer()
					},
					//定位获得当前位置信息
					getMyLocation() {

						var geolocation = new qq.maps.Geolocation("PHPBZ-TTJCD-7V34L-HTOWA-U5Q2E-ARBT2", "省钱兄活动报名");

						geolocation.getIpLocation(this.showPosition, this.showErr);
						// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
					},
					showPosition(position) {
						console.log(position);
						// this.latitude = position.lat;
						// this.longitude = position.lng;
						// this.city = position.city;
						this.setMap();
					},
					showErr(e) {
						console.log("定位失败", e);
						this.getMyLocation(); //定位失败再请求定位，测试使用
					},
					//位置信息在地图上展示
					setMap() {
						//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
						//设置地图中心点

						var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
						//定义工厂模式函数
						var myOptions = {
							zoom: 13, //设置地图缩放级别
							center: myLatlng, //设置中心点样式
							mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
						}
						// //获取dom元素添加地图信息
						var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
						//给地图添加点击事件

						//给定位的位置添加图片标注
						var marker = new qq.maps.Marker({
							position: myLatlng,
							map: map
						});
						// `````````````
						var that = this;
						if (that.longitude == '') {
							var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
						} else {
							var center = new qq.maps.LatLng(that.latitude, that.longitude);
						}

						var map = new qq.maps.Map(document.getElementById("container1"), {
							center: center,
							zoom: 13
						});
						var marker = new qq.maps.Marker({
							position: center,
							map: map
						});
						var latlngBounds = new qq.maps.LatLngBounds();
						qq.maps.event.addListener(map, "click", function(event) {
							console.log(event, qq.maps);
							that.longitude = event.latLng.getLng(); // 经度
							that.latitude = event.latLng.getLat(); // 纬度

							jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() +
								',' + event
								.latLng.getLng() +
								'&key=PHPBZ-TTJCD-7V34L-HTOWA-U5Q2E-ARBT2&get_poi=1&output=jsonp', {
									myCustomUrlParam: 'veryNice'
								}).then(response => {
								console.log('response', response, response.result.address_component.city)
								that.address = response.result.formatted_addresses.recommend
								that.city = response.result.address_component.city
							}).catch(error => {
								// handle error
							}).then(() => {
								// always executed
							});
							if (markersArray) {
								for (let i in markersArray) {
									markersArray[i].setMap(null);
								}
							}
							if (!marker) {
								marker = new qq.maps.Marker({
									map: map,
									position: event.latLng
								});
							} else {
								marker.setPosition(event.latLng)
							}

							// markersArray.push(marker);

						});
						geocoder = new qq.maps.Geocoder({
							complete: function(result) {
								console.log(result);
								that.longitude = result.detail.location.lng;
								that.latitude = result.detail.location.lat;
								map.setCenter(result.detail.location);
								var marker = new qq.maps.Marker({
									map: map,
									position: result.detail.location
								});
								markersArray.push(marker);
							}
						});
						console.log('-----------------', that.city, )
					},
					// 地图定位
					selectDz() {


						// if (this.city == '') {
						// 	this.$notify({
						// 		title: '提示',
						// 		duration: 1800,
						// 		message: '请在地图上定位获取经纬度',
						// 		type: 'warning'
						// 	});
						// 	return
						// } else {

						var add = this.province + this.city + this.district + this.address
						let that = this
						jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
							'&key=PHPBZ-TTJCD-7V34L-HTOWA-U5Q2E-ARBT2&get_poi=1&output=jsonp', {
								myCustomUrlParam: 'veryNice'
							}).then(response => {
							// handle success
							if (response.message == '查询无结果') {
								this.$notify({
									title: '提示',
									duration: 1800,
									message: '详细地址输入有误，请重新输入',
									type: 'warning'
								});
								return
							}

							console.log('response', response)
							that.longitude = response.result.location.lng; // 经度
							that.latitude = response.result.location.lat; // 纬度
							// that.city = response.result.address_components.city;
							// that.address = response.result.address_components.province + response.result.address_components
							// 	.city + response.result.address_components.district + response.result.title
							that.setMap()
						}).catch(error => {
							// handle error
						}).then(() => {
							// always executed
						});
						// }

					},
					// 字典
					flxmSelect() {
						// 拜访所需金额
						this.$http({
							url: this.$http.adornUrl('sys/dict/selectDictList'),
							method: 'get',
							params: this.$http.adornParams({
								'type': '拜访所需金额'
							})
						}).then(({
							data
						}) => {
							let returnData = data.data
							this.xinghaoData = returnData
						})
						// 注册资本
						this.$http({
							url: this.$http.adornUrl('sys/dict/selectDictList'),
							method: 'get',
							params: this.$http.adornParams({
								'type': '注册资本'
							})
						}).then(({
							data
						}) => {
							let returnData = data.data
							this.zhangqiData = returnData
						})
						// 标签
						this.$http({
							url: this.$http.adornUrl('sys/dict/selectDictList'),
							method: 'get',
							params: this.$http.adornParams({
								'type': '名片标签'
							})
						}).then(({
							data
						}) => {
							let returnData = data.data
							this.biaoqianData = returnData
						})
						// 岗位名称
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
							this.gangweiData = returnData
						})
						// 
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
							this.flxmData = returnData
						})
					},
			},
			mounted() {
				if (this.$route.query.status) {
					this.statusT = Number(this.$route.query.status)
				} else {
					this.statusT = ''
				}
				//名片列表
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

	.delss2 {
		display: inline-block;
		position: absolute;
		right: 21px;
		bottom: 24px;
	}
	p img{
		width: 100%;
	}
	.ql-container{
		height: 200px;
	}
</style>