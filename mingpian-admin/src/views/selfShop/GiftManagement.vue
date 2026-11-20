<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="礼品列表" name="first">
				<div style="display: inline-block;margin:5px">
					<span>状态：</span>
					<el-select v-model="statusT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<!-- <div style="display: inline-block;margin:5px">
					<span>发布用户：</span>
					<el-input v-model="userNameT" placeholder="请输入发布用户" style="width: 200px;"
						@keydown.enter.native="select">
					</el-input>&nbsp;&nbsp;
				</div>
				
				<div style="display: inline-block;margin:5px">
					<span>公司名称：</span>
					<el-input v-model="companyNameT" placeholder="请输入公司名称" style="width: 200px;"
						@keydown.enter.native="select">
					</el-input>&nbsp;&nbsp;
				</div> -->
				<div style="display: inline-block;margin:5px">
					<span>产品标题：</span>
					<el-input v-model="dataTitleT" placeholder="请输入产品标题" style="width: 200px;"
						@keydown.enter.native="select">
					</el-input>&nbsp;&nbsp;
				</div>
				<!-- <div style="display: inline-block;margin:5px">
					<span>行业一级名称：</span>
					<el-select v-model="phoneT" placeholder="请选择行业一级名称" style="width: 250px;"
						@change="industryFirstBtn(1)">
						<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.industryName"
							:value="item.industryId">
						</el-option>
					</el-select>
				</div> -->
				<!-- <div style="margin:5px;display: inline-block;">
				  <span>会员等级:</span>
				  <el-select v-model="vipIdT" style="width:150px;margin-left: 10px;" @change="select()">
				    <el-option v-for="item in list.records" :key="item.id" :label="item.vipName" :value="item.id">
				    </el-option>
				  </el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;margin: 5px;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;margin: 5px;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div> -->
				<div style="display: inline-block;margin:5px">
					<el-button icon="document" size="mini" style='margin-left:15px;' type="primary" @click="select">查询
					</el-button>
					<el-button icon="document" size="mini" style='margin-left:15px;' type="primary" @click="cleans">重置
					</el-button>
					<el-button icon="document" size="mini" style='margin-left:15px;' type="primary"
						:disabled="!isAuth('shopAdmin:add')" @click="addBtn(1)">添加
					</el-button>
					<el-button size="mini" type="warning" icon="document" @click="exportBtn">导出Excel
					</el-button>
				</div>
				<div style="color: orange;"> * 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column label="编号" prop="productId" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column label="发布用户" prop="realName">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">{{
						scope.row.user ? scope.row.user.userName : '-'
					}}</span>
						</template>
					</el-table-column>
					<el-table-column label="公司名称" prop="companyName" width="200">
					</el-table-column>
					<el-table-column prop="vipName" label="会员等级">
					</el-table-column>
					<el-table-column prop="industryFirstName" label="一级分类">
					</el-table-column> -->
					<el-table-column prop="productTitle" label="产品标题" width="200">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.productTitle}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="productImage" label="产品图片" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.productImage == null || scope.row.productImage == ''">
								暂无图片
							</div>
							<div v-else-if="scope.row.productImage">
								<el-popover v-for="(item,index) in scope.row.productImage.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="modelName" label="型号" width="200">
					</el-table-column>
					<el-table-column prop="paymentPeriod" label="接受账期">
					</el-table-column>
					<el-table-column prop="supplyType" label="供货类型">
					</el-table-column>
					<el-table-column prop="label" label="标签" width="150">
					</el-table-column>
					<el-table-column label="产品详情图" prop="productDetailsImage" width="300">
						<template slot-scope="scope">
							<div
								v-if="scope.row.productDetailsImage == null || scope.row.productDetailsImage == ''||scope.row.productDetailsImage == 'null' ">
								暂无图片
							</div>
							<div v-else-if="scope.row.productDetailsImage">
								<el-popover v-for="(item,index) in scope.row.productDetailsImage.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column prop="productDetails" label="产品详情" width="250">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.productDetails}}
							</div>
						</template>
					</el-table-column>
					<!-- <el-table-column label="收货地址" prop="province" width="200">
						<template slot-scope="scope">
							<span>{{ scope.row.province + '' + scope.row.city + '' + scope.row.district  + scope.row.address }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="longitude" label="经度">
					</el-table-column>
					<el-table-column prop="latitude" label="纬度">
					</el-table-column> -->
					<el-table-column prop="createTime" label="发布时间" width="160">
					</el-table-column>
					<el-table-column prop="remark" label="审核说明" width="200">
					</el-table-column>
					<el-table-column fixed='right' label="状态" prop="checkCertification" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1">上架</span>
							<span v-if="scope.row.status == 2">下架</span>
							<el-switch v-model="scope.row.status" @change="changeTj(scope.row.status,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
							<!-- <el-button icon="document" size="mini" style='margin:5px;' type="primary" plain
								v-if="scope.row.status != 1" @click="statusBtn(scope.row)"
								:disabled="!isAuth('autonym:update')">修改
							</el-button> -->

						</template>
					</el-table-column>
					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<!-- <el-button :disabled="scope.row.status !== 1 ||!isAuth('autonym:jujue')" size="mini"
								style='margin:5px;' type="primary" @click="refuseClick(scope.row)">
								审核
							</el-button> -->
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(2,scope.row)" :disabled="!isAuth('shopAdmin:update')">修改
							</el-button>
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" :disabled="!isAuth('shopAdmin:delete')">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
						:total="tableData.totalCount" layout="total,sizes, prev, pager, next,jumper"
						@size-change="handleSizeChange" @current-change="handleCurrentChange">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 审核拒绝弹框 -->
			<el-dialog :visible.sync="dialogFormVisible" center title="审核">
			
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
					<el-radio-group v-model="status">
						<el-radio :label="2">通过</el-radio>
						<el-radio :label="3">拒绝</el-radio>
					</el-radio-group>
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
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">二级分类：</span>
					<el-select v-model="industrySecondId" placeholder="请选择类型" style="width: 50%;"
						:disabled="industryFirstId==''">
						<el-option v-for="(item,index) in tableDataFer" :key="item.index" :label="item.industryName"
							:value="item.industryId">
						</el-option>
					</el-select>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">产品标题：</span>
					<el-input style="width:50%;" v-model="productTitle" type="text" placeholder="请输入产品标题"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购数：</span>
					<el-input style="width:50%;" v-model="needCount" type="text" placeholder="请输入采购数"></el-input>
				</div> -->
			<!-- 	<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购方联系人：</span>
					<el-input style="width:50%;" v-model="procureRealName" type="text"
						placeholder="请输入采购方联系人"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">采购方联系电话：</span>
					<el-input style="width:50%;" v-model="procurePhone" type="number"
						placeholder="请输入采购方联系电话"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">型号：</span>
					<el-input style="width:50%;" v-model="modelName" type="text"
						placeholder="请输入型号"></el-input>
					<!-- <el-select v-model="modelName" style="width:50%;">
						<el-option v-for="item in xinghaoData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select> -->
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">接受账期：</span>
					<el-select v-model="paymentPeriod" style="width:50%;">
						<el-option v-for="item in zhangqiData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">供货类型：</span>
					<el-select v-model="supplyType" style="width:50%;">
						<el-option v-for="item in gonghuoData" :key="item.value" :label="item.value"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
					<el-checkbox-group v-model="label" style="display: inline-block;">
						<el-checkbox v-for="(item,index) in biaoqianData" :key="index"
							:label="item.code">{{item.code}}</el-checkbox>
					</el-checkbox-group>
				</div> -->

				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">产品描述：</span>
					<el-input style="width:50%;" v-model="productDetails" type="textarea" :rows="3"
						placeholder="请输入描述信息"></el-input>
					<!-- <quill-editor ref="myTextEditor" v-model="productDetails" :options="quillOption"
					  style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
					</quill-editor> -->
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">产品图片：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in productImage" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear3(index)"></i>
							</span>
						</div>
						<div class="imgs"  v-if="productImage.length<9">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" multiple :limit="9"
								list-type="picture-card" :show-file-list="false" :on-success="handleAvatarSuccess2"
								:on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">产品图片：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="productImage"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="productImage" :src="productImage" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div> -->
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">产品详情图：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in productDetailsImage" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear2(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess1"
								:on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>

				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">所在区域：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" remote
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">接收详细地址：</span>
					<el-input style="width:50%;" v-model="address" @keydown.enter.native="selectDz" type="text"
						placeholder="请输入接收详细地址"></el-input>
					<el-button type="primary" plain size="mini" @click="selectDz()">确 定</el-button>
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
				</div> -->
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
					<el-table-column prop="userId" label="编号"  width="80"></el-table-column>
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
			
		</el-tabs>
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
				companyNameT:'',
				userNameT: '',
				phoneT: '',
				content: '',
				status: '',
				rewardPoint: '',
				customerServicePhone: '',
				customerServiceImg: '',


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
					{
						value: 1,
						label: '上架'
					}, {
						value: 2,
						label: '下架'
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
				closeValue: 2,
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
				userId2:'',
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
				xinghaoData: [],
				zhangqiData: [],
				gonghuoData: [],
				biaoqianData: [],
				classifyIds: [],
				tableDataFer: [],
				status: 1,
				releaseType: 1,
				userId: '',
				cooperationModel: 1,
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
				// ---- 产品
				productId: 1,
				productTitle: "", //产品标题
				productImage: [], //产品图片
				industryFirstId: 1, //行业id
				industryFirstName: "", //行业名称
				modelName: "", //型号
				paymentPeriod: "", //接受账期
				supplyType: "", //供货类型
				label: [], //标签
				province: "", //省
				city: "", //市
				district: "", //区
				address: "", //详细地址
				longitude: "", //经度
				latitude: "", //未读
				productDetails: "", //产品详情
				productDetailsImage: [], //产品详情图
				userId: 1, //用户id
				list: {},
				vipIdT:'',
				startTime:'',
				endTime:'',
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
				if (tab._props.label === '礼品列表') {
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
				this.demandId = rows.productId
				this.content = ''


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
				if (this.status == 3) {
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
					url: this.$http.adornUrl('admin/product/updateProduct'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'productId': this.demandId,
						'remark': this.content,
						
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
				this.cityT = ''
				this.userNameT = ''
				this.phoneT = ''
				this.companyNameT = ''
				this.classIdT = ''
				this.screen = ''
				this.vipIdT = ''
				this.startTime = ''
				this.endTime = ''
				this.dataSelect()
				this.dataSelectW()
				this.dataSelectHylist()
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
					url: this.$http.adornUrl('admin/product/selectProductList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'productTitle': this.dataTitleT,
						'industryFirstId': this.phoneT,
						'city': this.cityT,
						'status': this.statusT,
						'vipId':this.vipIdT,
						'startTime':this.startTime,
						'endTime':this.endTime,
						'companyName':this.companyNameT,
						// 'releaseType': this.screen,
						// 'sysUserName': '',
						'userName': this.userNameT,
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
					url: this.$http.adornUrl('admin/product/updateProduct'),
					method: 'post',
					// params: this.$http.adornParams({
						data: this.$http.adornData({
						'productId': row.productId,
						'status': row.status,
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
					this.productId = ''
					this.productTitle = ''
					this.productImage = []
					this.industryFirstId = ''
					this.modelName = ''
					this.paymentPeriod = ''
					this.supplyType = ''
					this.label = []
					this.productDetails = ''
					this.productDetailsImage = []
					this.status = 1
					this.userId = ''
					this.province = ''
					this.city = ''
					this.district = ''
					this.address = ''
					this.longitude = ''
					this.latitude = ''
					this.storeAddress = []
					this.storeAddre = '请选择城市'
					this.nickName1 = ''
				} else {
					this.titles = '修改'
					this.productId = row.productId
					this.productTitle = row.productTitle
					if (row.productImage) {
						this.productImage = row.productImage.split(',')
					} else {
						this.productImage = []
					}
					
					this.industryFirstId = row.industryFirstId
					this.modelName = row.modelName
					this.paymentPeriod = row.paymentPeriod
					this.supplyType = row.supplyType
					if (row.label) {
						this.label = row.label.split(',')
					} else {
						this.label = []
					}
					this.productDetails = row.productDetails
					if (row.productDetailsImage) {
						this.productDetailsImage = row.productDetailsImage.split(',')
					} else {
						this.productDetailsImage = []
					}
					this.userId = row.userId
					this.nickName1 = row.user.userName
					this.province = row.province
					this.city = row.city
					this.district = row.district
					this.address = row.address
					this.longitude = row.longitude
					this.latitude = row.latitude
					this.storeAddress = []
					this.storeAddre = this.province + '/' + this.city + '/' + this.district
					if (row.status != '' || row.status != null) {
						this.status = row.status
					} else {
						this.status = 1
					}
				}
				this.flxmSelect()
				this.dataSelectW()
				this.dataSelectFer()
				this.getMyLocation()
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
				
				if (this.productTitle == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '求购标题不能为空',
						type: 'warning'
					})
					return
				}
				if (this.modelName === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '型号不能为空',
						type: 'warning'
					})
					return
				}
				if (this.paymentPeriod === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '接受账期不能为空',
						type: 'warning'
					})
					return
				}
				if (this.supplyType === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '供货类型不能为空',
						type: 'warning'
					})
					return
				}
				

				if (this.province == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择所在区域',
						type: 'warning'
					})
					return
				}
				if (this.address === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '详细地址不能为空',
						type: 'warning'
					})
					return
				}
				if (this.longitude == ''||this.latitude=='') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '经纬度不能为空',
						type: 'warning'
					})
					return
				}
				// if (this.productDetails == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '产品描述不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.productImage.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传产品图片',
						type: 'warning'
					})
					return
				
				}
				// if (this.productDetailsImage.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传产品详情图',
				// 		type: 'warning'
				// 	})
				// 	return
				
				// }
				if (this.label.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择标签',
						type: 'warning'
					})
					return
				
				}
				
				
				if(this.titles == '添加'){
					var url = 'admin/product/insertProduct'
				}else{
					var url = 'admin/product/updateProduct'
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'productId': this.productId,
						'productTitle': this.productTitle,
						'productImage': this.productImage.toString(),
						'industryFirstId': this.industryFirstId,
						'modelName': this.modelName,
						'paymentPeriod': this.paymentPeriod,
						'supplyType': this.supplyType,
						'label': this.label.toString(),
						'productDetails': this.productDetails,
						'productDetailsImage': this.productDetailsImage.toString(),
						'status': this.status,
						'userId': this.userId,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'address': this.address,
						'longitude': this.longitude,
						'latitude': this.latitude,
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
				this.productImage.push(file.data)
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
			clear3(index) {
				this.productImage.splice(index, 1);
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
						'userId':this.userId2,
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
						url: this.$http.adornUrl('admin/product/deleteProduct'),
						method: 'post',
						params: this.$http.adornParams({
							'productId': row.productId
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
			// 字典
			flxmSelect() {
				// 型号
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '型号'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.xinghaoData = returnData
				})
				// 接受账期
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '接受账期'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.zhangqiData = returnData
				})
				// 供货类型
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '供货类型'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.gonghuoData = returnData
				})
				// 标签
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '产品标签'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.biaoqianData = returnData
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
					this.dataSelect()

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
							'cooperationModel': row.cooperationModel,
							'peopleNum': row.peopleNum,
							'createTime': row.createTime,
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
							'cooperationModel': row.cooperationModel,
							'peopleNum': row.peopleNum,
							'createTime': row.createTime,
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
			},
			// 获取服务数据列表
			dataSelectUser() {
				this.$http({
					url: this.$http.adornUrl('admin/procureDemand/selectProcureDemandRealName'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUser = returnData
				})
			},
			// 地图定位
			selectDz() {
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
					// that.companyCity = response.result.address_components.city;
					// that.address = response.result.address_components.province + response.result.address_components
					// 	.city + response.result.address_components.district + response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("PHPBZ-TTJCD-7V34L-HTOWA-U5Q2E-ARBT2", "省钱兄活动报名");
				geolocation.getIpLocation(this.showPosition, this.showErr);
			},
			showPosition(position) {
				console.log(position);
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
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
			
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=PHPBZ-TTJCD-7V34L-HTOWA-U5Q2E-ARBT2&get_poi=1&output=jsonp', {
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
			//获取会员列表
			dataSelectHylist() {
			  this.$http({
			    url: this.$http.adornUrl('admin/vipDetails/getVipDetailsList'),
			    method: 'get',
			    params: this.$http.adornParams({
			      // 'page': this.page,
			      // 'limit': this.limit
			    })
			  }).then(({
			    data
			  }) => {
			    if (data && data.code === 0) {
			      let returnData = data.data
			      this.list = returnData
			    }
			
			  })
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('admin/product/excelProductList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'productTitle': this.dataTitleT,
						'industryFirstId': this.phoneT,
						'city': this.cityT,
						'status': this.statusT,
						'vipId':this.vipIdT,
						'startTime':this.startTime,
						'endTime':this.endTime,
						'companyName':this.companyNameT,
						'userName': this.userNameT,
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
						elink.download = '礼品列表.xlsx'
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
			this.dataSelectHylist()
			// this.dataSelectUser()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	}
</script>

<style>
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