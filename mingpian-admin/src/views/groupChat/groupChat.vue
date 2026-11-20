<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">群组管理</span>
			<h2 class="nf-page-hero__title">群组列表</h2>
			<p class="nf-page-hero__desc">管理用户创建的群组，支持多条件筛选、审核、修改、加群用户与解锁列表等操作</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户名称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="shauxin" clearable placeholder="请输入用户名称"
						v-model="userNameT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户手机号</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="shauxin" clearable placeholder="请输入用户手机号"
						v-model="phoneT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">群名称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="shauxin" clearable placeholder="请输入群名称"
						v-model="groupNameT"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">群状态</span>
					<el-select v-model="groupStatusT" class="nf-filter-form__input" placeholder="请选择群状态" @change="shauxin()">
						<el-option v-for="(item,index) in groupStatuslist" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">审核状态</span>
					<el-select v-model="groupAuditStatusT" class="nf-filter-form__input" placeholder="请选择审核状态" @change="shauxin()">
						<el-option v-for="(item,index) in groupAuditStatuslist" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">群分类</span>
					<el-select v-model="typeIdT" class="nf-filter-form__input" placeholder="请选择群分类" @change="shauxin()">
						<el-option v-for="(item,index) in typeIdList" :key="index" :label="item.typeName" :value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="shauxin">查询</el-button>
					<el-button type="primary" icon="document" @click="clear">重置</el-button>
					<el-button type="primary" :disabled="!isAuth('groupChat:add')" icon="document" @click="addNotice">添加</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.list">

			<el-table-column fixed prop="payClassifyId" label="编号" width="50">
				<template slot-scope="scope">
					{{scope.$index+1}}
				</template>
			</el-table-column>
			<el-table-column prop="groupId" label="ID" width="150">
			</el-table-column>
			<el-table-column prop="userName" label="用户名称" fixed="left">
				<template slot-scope="scope">
					<span class="nf-table-link"
						@click="updates(scope.row.userId)">{{ scope.row.userName?scope.row.userName:'未绑定' }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="用户手机号">
			</el-table-column>
			<el-table-column prop="groupQrcode" label="群二维码" width="250">
				<template slot-scope="scope">
					<div v-if="scope.row.groupQrcode">
						<div v-for="(item,index) in scope.row.groupQrcode.split(',')"
							style="display: inline-block;width: 60px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
									slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
					<div v-else>暂无图片</div>
				</template>
			</el-table-column>
			<el-table-column prop="circleName" label="圈子名称" width="160">
			</el-table-column>
			<el-table-column prop="typeName" label="群分类" fixed="left" width="150">
			</el-table-column>
			<el-table-column prop="groupName" label="群名称" fixed="left" width="150">
			</el-table-column>
			<el-table-column prop="groupCoverImg" label="群封面图" width="250">
				<template slot-scope="scope">
					<div v-if="scope.row.groupCoverImg">
						<div v-for="(item,index) in scope.row.groupCoverImg.split(',')"
							style="display: inline-block;width: 60px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
									slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
					<div v-else>暂无图片</div>
				</template>
			</el-table-column>
			<el-table-column prop="detailsImgsDTO" label="群详情图" width="300">
				<template slot-scope="scope">
					<div v-if="scope.row.detailsImgsDTO.length>0">
						<div v-for="(item,index) in scope.row.detailsImgsDTO"
							style="display: inline-block;width: 60px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item.groupImg" alt=""
									slot="reference">
								<img style="width: 300px; height: auto" :src="item.groupImg" alt="">
							</el-popover>
						</div>
					</div>
					<div v-else>暂无图片</div>
				</template>
			</el-table-column>
			<el-table-column prop="applyCondition" label="申请类型">
				<template slot-scope="scope">
					<span v-if="scope.row.applyCondition===0">免费</span>
					<span v-if="scope.row.applyCondition===1">付费</span>
				</template>
			</el-table-column>
			<el-table-column prop="groupCharge" label="群费用金额">
			</el-table-column>
			<!-- <el-table-column prop="groupExtendedFields" label="群扩展信息" width="200">
        <template slot-scope="scope">
            <div  v-for="(item,index) in scope.row.groupExtendedFields" :key="index">
              <div>
                {{item.label}}：{{item.value}}
              </div>
            </div>
          </template>
      </el-table-column> -->
			<el-table-column prop="groupProvince" label="省市区" width="150">
				<template slot-scope="scope">
					{{scope.row.groupProvince}}{{scope.row.groupCity}}{{scope.row.groupRegion}}
				</template>
			</el-table-column>
			<el-table-column prop="groupAddress" label="详细地址" width="150">
			</el-table-column>
			<el-table-column prop="peopleCount" label="群成员数量" width="150">
			</el-table-column>
			<el-table-column prop="peopleJoinCount" label="加入的总人数" width="80">
			</el-table-column>
			<el-table-column prop="realPeopleJoinCount" label="实际加入人数" width="80">
			</el-table-column>
			<el-table-column prop="virtualJoinCount" label="虚拟加入人数" width="110" align="center">
				<template slot-scope="scope">
					<div>{{scope.row.virtualJoinCount?scope.row.virtualJoinCount:0}}</div>
					<span class="nf-table-link" @click="updateBtn(scope.row,4)">修改
					</span>
				</template>
			</el-table-column>

			<el-table-column prop="viewCount" label="浏览次数" width="80">
			</el-table-column>
			<el-table-column prop="dayCount" label="创建天数" width="80">
			</el-table-column>
			<el-table-column prop="auditRemark" label="审核备注" width="150">
			</el-table-column>
			<el-table-column prop="remark" label="备注" width="150">
			</el-table-column>
			<el-table-column prop="groupAuditStatus" label="审核状态" fixed="right" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.groupAuditStatus===0" style="color: #999;">待审核</span>
					<span v-if="scope.row.groupAuditStatus===1" style="color: #13ce66;">已通过</span>
					<span v-if="scope.row.groupAuditStatus===2" style="color: #ff4949;">已拒绝</span>
				</template>
			</el-table-column>
			<el-table-column prop="groupStatus" label="群状态" fixed="right" width="80">
				<template slot-scope="scope">
					<span v-if="scope.row.groupStatus===0" style="color: #13ce66;">正常</span>
					<span v-if="scope.row.groupStatus===1" style="color: #ff4949;">禁用</span>
					<span v-if="scope.row.groupStatus===2" style="color: #999;">过期</span>
					<span v-if="scope.row.groupStatus===3" style="color: orangered;">人数已达最大</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" width="180" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" plain style="margin: 3px;" @click="mobanBtn(scope.row)">加群用户
					</el-button>
					<el-button size="mini" type="warning" :disabled="!isAuth('groupChat:update')"
						@click="UpdateStatus(scope.row)" v-if="scope.row.groupAuditStatus===0"
						style="margin-bottom: 5px;">审核</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('groupChat:update')" style="margin: 3px;"
						@click="amendBanner(scope.row)">修改
					</el-button>
					<el-button size="mini" type="warning" plain style="margin: 3px;" @click="jiesuoBtn(scope.row)">解锁列表
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('groupChat:delete')" style="margin: 3px;"
						@click="deletes(scope.row)">删除
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
		<el-dialog :title="title" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">创建用户：</span>
				<el-input style="width:50%;" v-model="userName" @focus="userBtn(1)" placeholder="请选择创建用户"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">圈子：</span>
				<el-select v-model="circleId" placeholder="请选择状态" style="width:50%;">
					<el-option v-for="item in tableDataQz" :key="item.circleId" :label="item.circleName"
						:value="item.circleId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">群名称：</span>
				<el-input style="width:50%;" v-model="groupName" placeholder="请输入群名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">群分类：</span>
				<el-radio-group v-model="typeId" @change="typeNameBtn()">
					<el-radio v-for="(item,index) in tableDataFl" :key="index"
						:label="item.id">{{item.typeName}}</el-radio>
				</el-radio-group>
			</div>
			<!-- //// -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">省市区：</span>
				<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr3"
					v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
				</el-cascader>
			</div>
			<div v-for="(item,index)  in tableDataMb" :key="index">
				<div style="margin-bottom: 10px;" v-if="item.field=='isAddress'">
					<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
					<el-input style="width:50%;" v-model="groupAddress" placeholder="请输入详细地址"></el-input>
				</div>
			</div>

			<div style="margin-bottom: 10px;position: relative;">
				<span style="width: 200px;display: inline-block;text-align: right;position: absolute;">群简介：</span>
				<el-input style="width:50%;margin-left: 200px;" v-model="groupDesc" type="textarea" :rows="5"
					placeholder="请输入群简介"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">申请类型：</span>
				<el-radio-group v-model="applyCondition">
					<el-radio :label="0">免费</el-radio>
					<el-radio :label="1">付费</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">群费用金额：</span>
				<el-input style="width:50%;" v-model="groupCharge" placeholder="请输入群费用金额"></el-input>
				<div v-for="(item,index)  in tableDataMb" :key="index" style="margin-left: 200px;">
					<span v-if="item.field=='minMoney'">最低金额为：{{item.value}}</span>
					<span v-if="item.field=='maxMoney'">最大金额为：{{item.value}}</span>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">群成员数量：</span>
				<el-input style="width:50%;" v-model="peopleCount" placeholder="请输入群成员数量"></el-input>
				<div v-for="(item,index)  in tableDataMb" :key="index" style="margin-left: 200px;">
					<span v-if="item.field=='minPepole'">最低人数为：{{item.value}}</span>
					<span v-if="item.field=='maxPeople'">最大人数为：{{item.value}}</span>
				</div>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">开始时间：</span>
				<el-date-picker style="width:50%;" v-model="qrStartTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">过期时间：</span>
				<el-date-picker style="width:50%;" v-model="groupExpiredDate" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择过期时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;position: relative;">
				<span style="width: 200px;display: inline-block;text-align: right;position: absolute;">备注：</span>
				<el-input style="width:50%;margin-left: 200px;" v-model="remark" type="textarea" :rows="3"
					placeholder="请输入备注"></el-input>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">群二维码：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in groupQrcode" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels1(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleRemove1" v-if="groupQrcode.length<9"
							:multiple="true">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
				</div>
			</div>
			<!-- <div style="margin-bottom: 10px;display: flex;">
      	<span style="width: 200px;display: inline-block;text-align: right;">群二维码：</span>
      	<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
      		<el-upload
      			class="avatar-uploader"
      			v-model="groupQrcode"
      			:action="$http.adornUrlUp('alioss/upload')"
      			:show-file-list="false"
      			:on-success="handleAvatarSuccess"
      			>
      			<img v-if="groupQrcode" :src="groupQrcode" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
      			<i v-else class="el-icon-plus avatar-uploader-icon"></i>
      		</el-upload>
      	</div>
      </div> -->
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">群详情图：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in groupCoverimg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels2(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleRemove2" v-if="groupCoverimg.length<9"
							:multiple="true">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 群用户 -->
		<el-dialog title="群用户" :visible.sync="dialogFormVisibleMb" center width="80%">
			<div>
				<div style="margin:5px;display: inline-block;">
					<span>用户名称：</span>
					<el-input style="width: 160px;" @keydown.enter.native="shauxinMb" clearable placeholder="请输入用户名称"
						v-model="nickNameT"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>用户手机号：</span>
					<el-input style="width: 160px;" @keydown.enter.native="shauxinMb" clearable placeholder="请输入用户手机号"
						v-model="phoneT"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>性别：</span>
					<el-select v-model="sexT1" placeholder="请选择性别" style="width:150px;" @change="shauxinMb()">
						<el-option v-for="(item,index) in sexlist1" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<el-button style='margin: 10px 0;' size="mini" type="primary" plain icon="document"
					@click="shauxinMb">查询</el-button>
				<el-button style='margin: 10px 0;' size="mini" type="primary" plain icon="document"
					@click="clearMb">重置</el-button>
				<el-button style='margin: 10px 0;' :disabled="!isAuth('autonymType:add')" size="mini" type="primary"
					icon="document" @click="amendBannerMb()">添加</el-button>

			</div>
			<el-table v-loading="tableDataLoadingMb" :data="tableDataQyh.list">

				<el-table-column fixed prop="userId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="id" label="ID">
				</el-table-column>
				<el-table-column prop="nickName" label="用户名称">
					<template slot-scope="scope">
						<span style="color: #2f3968;cursor: pointer;"
							@click="updates(scope.row.userId)">{{ scope.row.nickName?scope.row.nickName:'未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="用户手机号">
				</el-table-column>
				<el-table-column prop="avatar" label="头像">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar">
							<div v-for="(item,index) in scope.row.avatar.split(',')"
								style="display: inline-block;width: 60px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin-bottom: 10px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
						<div v-else>暂无图片</div>
					</template>
				</el-table-column>
				<el-table-column prop="sex" label="性别" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.sex===1" style="color: #13ce66;">男</span>
						<span v-if="scope.row.sex===2" style="color: #ff4949;">女</span>
					</template>
				</el-table-column>
				<el-table-column prop="payTime" label="加入时间">
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('autonymType:delete')"
							style="margin: 3px;" @click="deletesMb(scope.row)">移除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeMb" @current-change="handleCurrentChangeMb"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitMb" :current-page="pageMb"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataQyh.total">
				</el-pagination>
			</div>
		</el-dialog>

		<el-dialog title="用户列表" :visible.sync="dialogFormVisiblePt" center width="75%">
			<div style="display: inline-block;margin:10px;">
				<span>地区：</span>
				<el-cascader style="width:150px" size="large" :options="optionsT" ref="cascaderAddr1"
					v-model="storeAddressT" :placeholder="storeAddre" @change="handleChange5" :value="storeAddre">
				</el-cascader>
			</div>
			<div style="margin:10px;display: inline-block;">
				<span>用户昵称:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectUser" clearable placeholder="请输入用户昵称"
					v-model="userNameU"></el-input>
			</div>
			<div style="margin:10px;display: inline-block;">
				<span>手机号:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectUser" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
			</div>
			<div style="margin:10px;display: inline-block;">
				<span>性别:</span>
				<el-select v-model="sexT" style="width:150px;margin-left: 10px;" @change="selectUser()">
					<el-option v-for="item in sexList" :key="item.value" :label="item.title" :value="item.id">
					</el-option>
				</el-select>
				<el-button size="mini" type="primary" @click="selectUser()">查询</el-button>
				<el-button size="mini" type="primary" @click="clearUser()">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoadingPt" :data="tableDataUder.list" height="600px">
				<el-table-column fixed prop="userId" label="编号"></el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="150">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null||scope.row.avatar==''" src="../../assets/img/avatar.png"
							alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="sex" label="性别">
					<template slot-scope="scope">
						<span v-if="scope.row.sex==1">男</span>
						<span v-else-if="scope.row.sex==2">女</span>
						<span v-else>未设置</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span style="color: #2f3968;cursor: pointer;">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button v-if="types==1" size="mini" type="primary" @click="xuanze(scope.row)">选择</el-button>
						<el-button v-if="types==2" size="mini" type="primary"
							@click="xuanzeJr(scope.row)">加入</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
					:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataUder.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 群审核 -->
		<el-dialog title="群审核" :visible.sync="dialogFormVisibleCs" center width="80%">
			<div>
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="types">
					<el-radio :label="1">通过</el-radio>
					<el-radio :label="2">拒绝</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">审核原因：</span>
				<el-input style="width: 50%;" type="textarea" rows="4" v-model="content"
					placeholder="请输入审核原因"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleCs = false">取 消</el-button>
				<el-button type="primary" @click="changeSh()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改虚拟人数 -->
		<el-dialog title="修改虚拟加入人数" :visible.sync="dialogFormVisibleDz" center>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">虚拟加入人数：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入虚拟加入人数">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleDz = false">取 消</el-button>
				<el-button type="primary" @click="refusetoDz()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 解锁列表 -->
		<el-dialog title="解锁列表" :visible.sync="dialogFormVisibleJs" center width="80%">
			<div style="display: inline-block;">
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>支付状态：</span>
					<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="selectJs()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>用户昵称：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectJs" placeholder="请输入用户昵称"
						v-model="minMoney">
					</el-input>
				</div>
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>手机号：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectv" placeholder="请输入手机号"
						v-model="maxMoney">
					</el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>订单编号：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectJs" placeholder="请输入订单编号"
						v-model="orderNo">
					</el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>解锁状态：</span>
					<el-select v-model="unlockStatus" style="width:150px;margin-left: 10px;" @change="selectJs()">
						<el-option v-for="item in homeData1" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>

				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="selectJs">刷新
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleansJs">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingJs" :data="tableDataJs.list">
				<el-table-column prop="id" label="编号" width="80">
					<template slot-scope="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="id" label="编号" width="160">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						　<span v-if="scope.row.userName" style="color: #2f3968;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">
							{{scope.row.userName? scope.row.userName : '未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="orderNo" label="订单编号">
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额">
				</el-table-column>
				<el-table-column prop="payType" label="支付类型">
					<template slot-scope="scope">
						<span v-if="scope.row.payType==1" style="color: #2f3968;">支付宝</span>
						<span v-if="scope.row.payType==2" style="color: #f56c6c;">微信</span>
						<span v-if="scope.row.payType==3" style="color: #999;">余额</span>
					</template>
				</el-table-column>
				<el-table-column prop="payStatus" label="支付状态">
					<template slot-scope="scope">
						<span v-if="scope.row.payStatus==0" style="color: #f56c6c;">待支付</span>
						<span v-if="scope.row.payStatus==1" style="color: #2f3968;">支付成功</span>
						<span v-if="scope.row.payStatus==2" style="color: #f56c6c;">支付失败</span>
						<span v-if="scope.row.payStatus==3" style="color: #999;">已退款</span>
					</template>
				</el-table-column>
				<el-table-column prop="unlockStatus" label="解锁状态">
					<template slot-scope="scope">
						<span v-if="scope.row.unlockStatus==1" style="color: #f56c6c;">已解锁</span>
						<span v-else style="color: #2f3968;">未解锁</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<!-- <el-table-column prop="createTime" fixed="right" label="操作" width="100">
      		<template slot-scope="scope">
      			<el-button size="mini" style="margin: 2px;" type="warning" :disabled="!isAuth('matching:delete')"
      				@click="deleteOrder(scope.row)" v-if="scope.row.payStatus==1">退款</el-button>
      		</template>
      	</el-table-column> -->
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeJs" @current-change="handleCurrentChangeJs"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitJs" :current-page="pageJs"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataJs.total">
				</el-pagination>
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
				page: 1,
				limit: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: {},
				typeName: '',
				typeStatus: 0,
				typeImg: '',
				id: '',
				sort: '',
				title: '添加',
				userName: '',
				userId: '', //群所属用户ID
				typeId: '', //群类型id
				groupName: '', //群名称
				groupCharge: '', //群费用金额
				peopleCount: '', //群成员数量
				groupQrcode: [], //群二维码
				groupDesc: '', //群简介
				applyCondition: 1, //申请类型（0免费，1付费）
				groupCoverimg: [], //封面图
				detailsImgsDTO: [],
				groupProvince: '', //省
				groupCity: '', //市
				groupRegion: '', //区
				qrStartTime: '', //开始时间
				groupExpiredDate: '', //过期日期
				remark: '', //备注
				groupAddress: '', //详细地址
				userNameT: '',
				phoneT: '',
				groupNameT: '',
				groupStatusT: '',
				typeIdT: '',
				groupStatuslist: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '正常'
					},
					{
						value: 1,
						label: '禁用'
					},
					{
						value: 2,
						label: '过期'
					},
					{
						value: 3,
						label: '人数已达最大'
					},
				],
				groupAuditStatusT: '',
				groupAuditStatuslist: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					},
					{
						value: 1,
						label: '已通过'
					},
					{
						value: 2,
						label: '已拒绝'
					},
				],
				typeIdList: [],
				sexT1: '',
				nickNameT: '',
				sexlist1: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '男'
					},
					{
						value: 2,
						label: '女'
					},
				],


				tableDataMb: [],
				tableDataQyh: {},
				dialogFormVisibleMb: false,
				tableDataLoadingMb: false,
				dialogFormVisiblemb2: false,
				pageMb: 1,
				limitMb: 10,
				typeId: '',
				templateList: [],
				checkListId: [],
				checkList: [],
				configStatus: 0,
				openValue: 0,
				closeValue: 1,

				tableDataUder: {},
				limitU: 10,
				pageU: 1,
				userName: '',
				dialogFormVisiblePt: false,
				tableDataLoadingPt: false,
				sexT: '',
				userNameU: '',
				phoneU: '',
				sexList: [{
						id: '',
						title: '全部'
					},
					{
						id: 1,
						title: '男'
					},
					{
						id: 2,
						title: '女'
					}
				],
				optionsT: provinceAndCityData,
				storeAddress: [],
				storeAddressT: [],
				storeAddre: '请选择城市',
				options: regionData,
				optionsT: provinceAndCityData,
				cityT: '',
				provinceT: '',

				tableDataFl: [],
				templatelist: [],
				mobanId: '',
				dialogVisible: false,
				dialogImageUrl: [],
				dialogFormVisibleCs: false,
				types: '',
				content: '',
				dialogFormVisibleDz: false,

				dialogFormVisibleJs: false,
				tableDataLoadingJs: false,
				tableDataJs: {},
				pageJs: 1,
				limitJs: 10,
				couponName: '',
				orderNo: '',
				unlockStatus: '',
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待支付'
					}, {
						value: 1,
						label: '支付成功'
					}, {
						value: 2,
						label: '支付失败'
					}
				],

				homeData1: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '解锁'
					},
					{
						id: 0,
						name: '未解锁'
					}
				],
				maxMoney: '',
				minMoney: '',
				tableDataQz: [],
				pageQz: 1,
				limitQz: 100,
				circleId: '',
			}
		},
		methods: {
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			shauxin() {
				this.page = 1
				this.dataSelect()
			},
			clear() {
				this.page = 1
				this.userNameT = ''
				this.phoneT = ''
				this.groupNameT = ''
				this.groupStatusT = ''
				this.typeIdT = ''
				this.groupAuditStatusT = ''
				this.dataSelect()
				this.dataSelectFl()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('groupInfo/page'),
					method: 'post',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
						'pageNum': this.page,
						'pageSize': this.limit,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'groupName': this.groupNameT,
						'groupStatus': this.groupStatusT,
						'typeId': this.typeIdT,
						'groupAuditStatus': this.groupAuditStatusT,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						// if(data.data.list.length>0){
						//   for(var i in data.data.list){
						//     data.data.list[i].groupExtendedFields = JSON.parse(data.data.list[i].groupExtendedFields)
						//     console.log(data.data.list[i].groupExtendedFields)
						//   }
						// }
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.groupQrcode = file.data
			},

			beforeUpload(file) {
				this.beforeArr.push(file);
				this.ruleForm.picUrls.push(file); // 占位为上传成功之后的splice方法做准备
			},
			// 图片上传
			handleRemove1(file, ) {
				console.log('--------', file)
				this.groupQrcode.push(file.data);
			},
			// 删除1
			dels1(index) {
				console.log(this.groupQrcode)
				this.groupQrcode.splice(index, 1);

			},
			// 图片上传
			handleRemove2(file, ) {
				console.log('--------', file)
				this.groupCoverimg.push(file.data);
			},
			// 删除图图
			dels2(index) {
				console.log(this.groupCoverimg)
				this.groupCoverimg.splice(index, 1);

			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr3'].currentLabels
				this.groupProvince = value[0]
				this.groupCity = value[1]
				this.groupRegion = value[2]
				if (this.groupCity == '市辖区') {
					this.groupCity = this.groupProvince
				}

			},
			addNotice() {
				this.typeName = ''
				this.id = ''
				this.userName = ''
				this.userId = ''
				this.typeId = ''
				this.groupName = ''
				this.groupCharge = ''
				this.peopleCount = ''
				this.groupQrcode = []
				this.groupDesc = ''
				this.applyCondition = 1
				this.groupCoverimg = []
				this.detailsImgsDTO = []
				this.groupProvince = ''
				this.groupCity = ''
				this.groupRegion = ''
				this.qrStartTime = ''
				this.groupExpiredDate = ''
				this.remark = ''
				this.groupAddress = ''
				this.storeAddress = []
				this.storeAddre = '请选择省市区'
				this.title = '添加'
				this.circleId = ''
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
			handleSizeChangeU(val) {
				this.limitU = val;
				this.dataSelectUser()
			},
			handleCurrentChangeU(val) {
				this.pageU = val;
				this.dataSelectUser()
			},
			// 添加群
			addNoticeTo() {
				// if (this.typeName == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入分类名称',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.remark == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入备注',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.groupQrcode.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传群二维码',
						type: 'warning'
					});
					return
				}
				// if (this.circleId == '' || this.circleId == null) {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '请选择圈子',
				//     type: 'warning'
				//   });
				//   return
				// }
				var arr = []
				if (this.groupCoverimg.length > 0) {
					for (var i in this.groupCoverimg) {
						var datas = {}
						datas.imgUrl = this.groupCoverimg[i]
						datas.sortId = Number(i) + 1
						arr.push(datas)
					}

				}
				this.detailsImgsDTO = arr

				if (this.title == '添加') {

					var url = 'groupInfo/save'
				} else {
					var url = 'groupInfo/edit'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({

						'id': this.id,
						'userId': this.userId, //群所属用户ID
						'typeId': this.typeId, //群类型id
						'groupName': this.groupName, //群名称
						'groupCharge': this.groupCharge, //群费用金额
						'peopleCount': this.peopleCount, //群成员数量
						'groupQrcode': this.groupQrcode.toString(), //群二维码
						'groupDesc': this.groupDesc, //群简介
						'applyCondition': this.applyCondition, //申请类型（0免费，1付费）
						// 'groupImgList': this.groupCoverimg.toString(), //封面图
						'detailsImgsDTO': this.detailsImgsDTO, //封面图
						'groupProvince': this.groupProvince, //省
						'groupCity': this.groupCity, //市
						'groupRegion': this.groupRegion, //区
						'groupExpiredDate': this.groupExpiredDate, //过期日期
						'qrStartTime': this.qrStartTime,
						'remark': this.remark, //备注
						'groupAddress': this.groupAddress,
						'groupExtendedFields': JSON.stringify(this.templatelist),
						'groupPlanId': this.circleId,
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
			// 修改
			amendBanner(rows) {
				this.title = '修改'
				this.typeName = rows.typeName
				this.id = rows.groupId
				this.remark = rows.remark
				this.typeStatus = rows.typeStatus
				// this.typeImg = rows.typeImg
				this.userName = rows.userName
				this.userId = rows.userId
				this.typeId = rows.typeId
				this.groupName = rows.groupName
				this.groupCharge = rows.groupCharge
				this.peopleCount = rows.peopleCount
				if (rows.groupQrcode) {
					this.groupQrcode = rows.groupQrcode.split(',')
				} else {
					this.groupQrcode = []
				}

				this.groupDesc = rows.groupDesc
				this.applyCondition = rows.applyCondition
				if (rows.detailsImgsDTO) {
					this.detailsImgsDTO = rows.detailsImgsDTO
					var arr = []
					if (rows.detailsImgsDTO.length > 0) {
						for (var i in rows.detailsImgsDTO) {
							arr.push(rows.detailsImgsDTO[i].groupImg)
						}
						console.log('arr', arr)
					}
					this.groupCoverimg = arr
				} else {
					this.groupCoverimg = []
					this.detailsImgsDTO = []
				}
				this.groupProvince = rows.groupProvince
				this.groupCity = rows.groupCity
				this.groupRegion = rows.groupRegion
				this.storeAddress = []
				this.storeAddre = this.groupProvince + '/' + this.groupCity + '/' + this.groupRegion
				this.groupExpiredDate = rows.groupExpiredDate
				this.qrStartTime = rows.qrStartTime
				this.remark = rows.remark
				this.groupAddress = rows.groupAddress
				this.circleId = rows.groupPlanId
				this.typeNameBtn()
				this.dataSelectQz()
				this.dialogFormVisible = true
			},
			//删除一级
			deletes(row) {
				let delid = row.groupId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`groupInfo/del/${delid}`),
						method: 'get',
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
			// 模版配置 弹框
			mobanBtn(row) {
				this.typeId = row.groupId
				this.pageMb = 1
				this.dataSelectMb()
				this.dialogFormVisibleMb = true
			},
			shauxinMb() {
				this.pageMb = 1
				this.dataSelectMb()
			},
			clearMb() {
				this.pageMb = 1
				this.nickNameT = ''
				this.sexT1 = ''
				this.phoneT = ''
				this.dataSelectMb()
			},
			// 获取数据列表
			dataSelectMb() {
				this.tableDataLoadingMb = true
				this.$http({
					url: this.$http.adornUrl('groupUserInfo/queryUnlockInfo'),
					method: 'post',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
						'pageNum': this.pageMb,
						'pageSize': this.limitMb,
						'groupId': this.typeId,
						'nickName': this.nickNameT,
						'sex': this.sexT1,
						'phone': this.phoneT,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoadingMb = false
						let returnData = data.data;
						this.tableDataQyh = returnData;
					}

				})
			},

			handleCurrentChangeMb(val) {
				this.pageMb = val
				this.dataSelectMb()
			},
			handleSizeChangeMb(val) {
				this.limitMb = val
				this.dataSelectMb()
			},

			// 修改
			amendBannerMb() {
				this.userBtn(2)
				this.dialogFormVisiblemb2 = true
			},
			//删除模版
			deletesMb(row) {
				let delid = row.id
				this.$confirm(`确定移除用户?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`groupUserInfo/del//${delid}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelectMb()
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

			// 获取用户弹框
			userBtn(index) {
				this.types = index
				this.userName = ''
				this.sexT = ''
				this.cityT = ''
				this.provinceT = ''
				this.userNameU = ''
				this.phoneU = ''
				this.storeAddres = '请选择城市'
				this.storeAddressT = []
				this.pageU = 1
				this.dataSelectUser()
				this.dialogFormVisiblePt = true
			},
			selectUser() {
				this.pageU = 1
				this.dataSelectUser()
			},
			clearUser() {
				this.sexT = ''
				this.cityT = ''
				this.provinceT = ''
				this.userNameU = ''
				this.phoneU = ''
				this.storeAddres = '请选择城市'
				this.storeAddressT = []
				this.pageU = 1
				this.dataSelectUser()
			},
			// 获取平台用户
			dataSelectUser() {
				this.tableDataLoadingPt = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'isVip': '',
						'member': -1,
						'userName': this.userNameU,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': -1,
						'phone': this.phoneU,
						'sex': this.sexT,
						'isAgent': '',
						'sysUser': '',
						'city': this.cityT,
						'province': this.provinceT,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUder = returnData
					this.tableDataLoadingPt = false
				})
			},
			// 选择用户
			xuanze(row) {
				this.userName = row.userName
				this.userId = row.userId
				this.dataSelectQz()
				this.dialogFormVisiblePt = false
			},
			// 获取省市区
			handleChange5(value) {
				value = this.$refs['cascaderAddr1'].currentLabels
				this.provinceT = value[0]
				this.cityT = value[1]
				// this.district = value[2]
				if (this.cityT == '市辖区') {
					this.cityT = ''
				}
				this.pageU = 1
				this.selectUser()
			},
			// 获取分类
			dataSelectFl() {
				this.$http({
					url: this.$http.adornUrl('groupTypeInfo/page'),
					method: 'post',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
						'pageNum': 1,
						'pageSize': 30,
						'typeName': '',
						'typeStatus': '',
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						let returnData = data.data;
						this.tableDataFl = returnData.list;
					}

				})
			},
			// 获取群模版
			typeNameBtn() {
				this.$http({
					url: this.$http.adornUrl('groupTypeConfig/queryTemplate/' + this.typeId),
					method: 'get',
					data: this.$http.adornData({
						// params: this.$http.adornParams({
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.tableDataMb = returnData
				})
			},

			// 修改状态弹框
			UpdateStatus(row) {
				this.id = row.groupId
				this.types = ''
				this.content = ''
				this.dialogFormVisibleCs = true
			},
			// 上下架
			changeSh() {
				if (this.types === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				if (this.types == 2) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入审核原因',
							type: 'warning'
						});
						return
					}
				}
				this.$http({
					url: this.$http.adornUrl('groupInfo/auditGroup'),
					method: 'post',
					data: this.$http.adornData({
						'groupId': this.id,
						'auditStatus': this.types,
						'auditRemaek': this.content,
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
						})
						this.dialogFormVisibleCs = false
						this.dataSelect()
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
						})
					}
				})
			},
			// 选择用户 加入群
			xuanzeJr(row) {
				this.$http({
					url: this.$http.adornUrl('groupUserInfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'userId': row.userId,
						'groupId': this.typeId,
						'remark': '',
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisiblePt = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectMb()
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
			// 获取群分类
			xuanzeQfl() {
				this.$http({
					url: this.$http.adornUrl('groupTypeInfo/queryType'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.typeIdList = data.data
					}

				})
			},
			dataSelect1() {
				if (this.$route.query.status == 0) {
					this.groupAuditStatusT = Number(this.$route.query.status)
				} else {
					this.groupAuditStatusT = ''
				}
				this.dataSelect()
				this.dataSelectFl()
				this.xuanzeQfl()
			},
			// 修改虚拟人数
			updateBtn(row, index) {

				this.id = row.groupId

				this.content = ''
				this.dialogFormVisibleDz = true

			},
			refusetoDz() {

				if (this.content === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入虚拟加入人数',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('groupInfo/setJoinCount'),
					method: 'post',
					data: this.$http.adornData({
						'groupId': this.id,
						'virtuatJoinCount': this.content
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleDz = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
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
			// 解锁弹框
			jiesuoBtn(row) {
				this.typeId = row.groupId
				this.pageJs = 1
				this.dataSelectJs()
				this.dialogFormVisibleJs = true
			},
			handleSizeChangeJs(val) {
				this.limitJs = val;
				this.dataSelectJs()
			},
			handleCurrentChangeJs(val) {
				this.pageJs = val;
				this.dataSelectJs()
			},
			// 查询
			selectJs() {
				this.pageJs = 1
				this.dataSelectJs()
			},
			// 重置
			cleansJs() {
				this.pageJs = 1
				this.couponName = ''
				this.orderNo = ''
				this.userName = ''
				this.maxMoney = ''
				this.minMoney = ''
				this.status = ''
				this.unlockStatus = ''
				this.dataSelectJs()
			},
			// 获取数据列表
			dataSelectJs() {
				// if(this.times){}
				this.tableDataLoadingJs = true
				this.$http({
					url: this.$http.adornUrl('groupUserInfo/unlockGroupListInfo'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.pageJs,
						'pageSize': this.limitJs,
						'groupName': this.couponName,
						'userName': this.minMoney,
						'phone': this.maxMoney,
						'unlockStatus': this.unlockStatus,
						'payStatus': this.status,
						'orderNo': this.orderNo,
						'groupId': this.typeId,
						'payType': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingJs = false
					let returnData = data.data;
					this.tableDataJs = returnData
				})
			},
			// 用户的圈子
			dataSelectQz() {
				this.$http({
					url: this.$http.adornUrl('admin/groupPlan/groupPlanList'),
					method: 'post',
					data: this.$http.adornData({
						'pageNum': this.pageQz,
						'pageSize': this.limitQz,
						'adminUserName': '',
						'circleName': '',
						'circleStatus': '',
						'circleType': '',
						'startTime': '',
						'endTime': '',
						'userId': this.userId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.tableDataQz = returnData.list
				})
			},
		},
		mounted() {
			if (this.$route.query.status == 0) {
				this.groupAuditStatusT = Number(this.$route.query.status)
			} else {
				this.groupAuditStatusT = ''
			}
			this.dataSelect()
			this.dataSelectFl()
			this.xuanzeQfl()
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