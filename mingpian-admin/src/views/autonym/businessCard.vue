<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">名片管理</span>
			<h2 class="nf-page-hero__title">名片列表</h2>
			<p class="nf-page-hero__desc">管理个人与企业名片信息，支持按昵称、姓名、公司、行业及时间筛选查询</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item" v-if="activeName === 'second'">
					<span class="nf-filter-form__label">状态</span>
					<el-select v-model="status" class="nf-filter-form__input" @change="select()">
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
					<span class="nf-filter-form__label">真实姓名</span>
					<el-input v-model="realNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入真实姓名" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">公司名称</span>
					<el-input v-model="dataTitleT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入公司名称" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">一级分类名称</span>
					<el-input v-model="industryFirstNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入一级分类名称" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">二级分类名称</span>
					<el-input v-model="industrySecondNameT" class="nf-filter-form__input nf-filter-form__input--wide"
						placeholder="请输入二级分类名称" @keydown.enter.native="select">
					</el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">开始时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="startTime"
						align="right" type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
						placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">截止时间</span>
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime"
						align="right" type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
						placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button icon="document" type="primary" @click="select">查询</el-button>
					<el-button icon="document" type="primary" @click="cleans">重置</el-button>
					<el-button icon="document" type="primary" :disabled="!isAuth('businessCard:add')"
						@click="addBtn()">添加</el-button>
					<el-button type="warning" icon="document" @click="exportBtn">导出Excel</el-button>
				</div>
			</div>
			<p class="nf-filter-tip">* 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</p>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="个人名片" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column label="编号" prop="cardId" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column label="名片类型" prop="cardType" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.cardType == 1">个人</span>
							<span v-if="scope.row.cardType == 2">企业</span>
						</template>
					</el-table-column> -->
					<el-table-column label="用户昵称" prop="userName">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="头像" prop="cardAvatar" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.cardAvatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.cardAvatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.cardAvatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>

					<el-table-column label="姓名" prop="realName" width="100">
					</el-table-column>
					<el-table-column label="微信号" prop="wxCode" width="160">
					</el-table-column>
					
					<el-table-column prop="companyName" label="公司" width="160">
					</el-table-column>
					<el-table-column label="一级行业" prop="industryFirstName" width="200">
					</el-table-column>
					<el-table-column label="二级行业" prop="industrySecondName" width="100">
					</el-table-column>
					<el-table-column label="城市" prop="companyCity">
					</el-table-column>
					<el-table-column label="联系电话" prop="cardPhone" width="160">
					</el-table-column>
					<el-table-column label="邮箱" prop="email" width="160">
					</el-table-column>
					<el-table-column label="地址" prop="address" width="200">
					</el-table-column>
					<el-table-column label="名片正面" prop="cardImg" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.cardImg == null || scope.row.cardImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.cardImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.cardImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.cardImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="名片背面" prop="cardImgBack" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.cardImgBack == null || scope.row.cardImgBack == ''">
								暂无图片
							</div>
							<div v-if="scope.row.cardImgBack" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.cardImgBack" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.cardImgBack" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="工作经历" prop="workExperience" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.workExperience">
								<div v-for="(item,index) in scope.row.workExperience.split(';')" :key="index">
									<div>{{item}} </div>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="教育经历" prop="eduExperience" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.eduExperience">
								<div v-for="(item,index) in scope.row.eduExperience.split(';')" :key="index">
									<div>{{item}} </div>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="简介图片" prop="detailsImage" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.detailsImage == null || scope.row.detailsImage == ''">
								暂无图片
							</div>
							<div v-if="scope.row.detailsImage">
								<el-popover v-for="(item,index) in scope.row.detailsImage.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="个人简介" prop="detailsText" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" style="margin-right: 5px" title="" trigger="hover">
								<div slot="reference"
									style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
									{{scope.row.detailsText}}
								</div>
								<div style="width: 400px;height: auto;">{{scope.row.detailsText}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="简介视频" prop="detailsVideo" width="250">
						<template slot-scope="scope">
							<div>
								<div v-if="scope.row.detailsVideo">
									<span v-for="(item,index) in scope.row.detailsVideo.split(',')" :key="index">
										<video v-if="item&&item!=''" :src="item" ref="videoRef" width="400" controls
											style="width: 100px;height: 50px;"></video>
										<video v-else ref="videoRef" width="400" style="width: 100px;height: 50px;" type="audio/wav"
											controls="controls"></video>
									</span>
								</div>
								
							</div>
						</template>
					</el-table-column>
					<!-- <el-table-column label="审核说明" prop="examineRemark" width="160">
					</el-table-column>
					<el-table-column fixed='right' label="审核状态" prop="auditStatus" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.auditStatus == 0">待审核</span>
							<span v-if="scope.row.auditStatus == 1">审核成功</span>
							<span v-if="scope.row.auditStatus == 2">审核未通过</span>
							<span v-if="scope.row.auditStatus == 3">会员到期已下架</span>
						</template>
					</el-table-column> -->
					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<!-- <el-button :disabled="scope.row.auditStatus !== 0 || !isAuth('businessCard:shenhe')"
								size="mini" style='margin:5px;' type="primary" @click="refuseClick(scope.row)">
								拒绝
							</el-button>
							<el-button :disabled="scope.row.auditStatus !== 0 || !isAuth('businessCard:shenhe')"
								size="mini" style='margin:5px;' type="primary" @click="passClicks(scope.row)">通过
							</el-button> -->
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(scope.row)" :disabled="!isAuth('businessCard:update')">修改
							</el-button>
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" :disabled="!isAuth('businessCard:delete')">删除
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
			<el-tab-pane label="企业名片" name="second">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column label="编号" prop="cardId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="用户昵称" prop="userName">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="公司LOGO" prop="companyLogo" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.companyLogo" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.companyLogo" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.companyLogo" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="公司名称" prop="companyName" width="160">
					</el-table-column>
					<el-table-column label="姓名" prop="realName" width="100">
					</el-table-column>
					<el-table-column label="微信号" prop="wxCode" width="160">
					</el-table-column>
					<el-table-column label="公司职务" prop="companyPosition" width="100">
					</el-table-column>
					<el-table-column label="联系电话" prop="cardPhone" width="100">
					</el-table-column>
					<el-table-column label="营业执照" prop="companyLicense" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.companyLicense == null || scope.row.companyLicense == ''">
								暂无图片
							</div>
							<div v-if="scope.row.companyLicense" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.companyLicense" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.companyLicense" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="公司面积(㎡)" prop="companyArea" width="100">
					</el-table-column>

					<el-table-column label="年产值(万元)" prop="companyOutputValue" width="100">
					</el-table-column>
					<el-table-column label="一级行业" prop="industryFirstName" width="200">
					</el-table-column>
					<el-table-column label="二级行业" prop="industrySecondName" width="100">
					</el-table-column>
					<el-table-column label="公司地址" prop="companyAddress" width="200">
						<template slot-scope="scope">
							<!-- 	{{scope.row.companyProvince}}{{scope.row.companyCity}}{{scope.row.companyDistrict}} -->
							{{scope.row.companyAddress}}
						</template>
					</el-table-column>
					<el-table-column label="公司简介" prop="companyBlurb" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.companyBlurb}}</div>
								<div style="width: 500px;max-height: 600px;overflow-y: auto;"
									v-html="scope.row.companyBlurb"></div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="公司产品" prop="firstProduct" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.firstProduct}}</div>
								<div style="width: 500px;max-height: 600px;overflow-y: auto;"
									v-html="scope.row.firstProduct"></div>
							</el-popover>
						</template>
					</el-table-column>
					
					<el-table-column label="产品图片" prop="firstProductImg" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.firstProductImg">
								<el-popover v-for="(item,index) in scope.row.firstProductImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="公司人数" prop="companyNum">
					</el-table-column>
					<el-table-column label="公司环境" prop="companyAmbient" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.companyAmbient">
								<el-popover v-for="(item,index) in scope.row.companyAmbient.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="资质认证" prop="companyFlairs" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.companyFlairs&&scope.row.companyFlairs.length>0" style="display:flex;flex-wrap: wrap;">
								<div v-for="(item,index) in scope.row.companyFlairs" :key="index">
									{{item.name}}:
									<span v-if="item.pic">
										<el-popover v-for="(ite,ind) in item.pic.split(',')" :key="ind"
											placement="top-start" style="margin-right: 5px" title="" trigger="hover">
											<img slot="reference" :src="ite" alt=""
												style="width: 50px; height: 50px;margin: 3px;">
											<img :src="ite" alt="" style="width: 200px; height: auto">
										</el-popover>
									</span>
								</div>
							</div>
						</template>
					</el-table-column>
					
					<!-- <el-table-column label="工作经历" prop="workExperience" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.workExperience">
								<div v-for="(item,index) in scope.row.workExperience.split(';')" :key="index">
									<div>{{item}} </div>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="教育经历" prop="eduExperience" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.eduExperience">
								<div v-for="(item,index) in scope.row.eduExperience.split(';')" :key="index">
									<div>{{item}} </div>
								</div>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column label="审核说明" prop="examineRemark" width="160">
					</el-table-column>
					<el-table-column label="创建时间" prop="createTime" width="160">
					</el-table-column>
					<el-table-column fixed='right' label="审核状态" prop="auditStatus" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.auditStatus == 0">待审核</span>
							<span v-if="scope.row.auditStatus == 1">审核成功</span>
							<span v-if="scope.row.auditStatus == 2">审核未通过</span>
							<span v-if="scope.row.auditStatus == 3">会员到期已下架</span>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<el-button :disabled="scope.row.auditStatus !== 0 || !isAuth('businessCard:shenhe')"
								size="mini" style='margin:5px;' type="primary" @click="refuseClick(scope.row)">
								拒绝
							</el-button>
							<el-button :disabled="scope.row.auditStatus !== 0 || !isAuth('businessCard:shenhe')"
								size="mini" style='margin:5px;' type="primary" @click="passClicks(scope.row)">通过
							</el-button>
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(scope.row)" :disabled="!isAuth('businessCard:update')">修改
							</el-button>
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" :disabled="!isAuth('businessCard:delete')">删除
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
			<el-dialog :visible.sync="dialogFormVisible" center title="审核拒绝">
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
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">用户类型：</span>
					<el-radio-group v-model="cardType">
						<el-radio :label="1">个人</el-radio>
						<el-radio :label="2">企业</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">发布用户：</span>
					<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布用户">
					</el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">一级分类：</span>
					<el-select v-model="industryFirstId" placeholder="请选择类型" style="width: 50%;"
						@change="industryFirstBtn()">
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
					<span style="width: 200px;display: inline-block;text-align: right;">真实姓名：</span>
					<el-input style="width:50%;" v-model="realName" type="text" placeholder="请输入真实姓名"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">联系电话：</span>
					<el-input style="width:50%;" v-model="cardPhone" type="number" placeholder="请输入联系电话"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">微信号：</span>
					<el-input style="width:50%;" v-model="wxCode" type="text" placeholder="请输入微信号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">邮箱：</span>
					<el-input style="width:50%;" v-model="email" type="text" placeholder="请输入邮箱"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">公司名称：</span>
					<el-input style="width:50%;" v-model="companyName" type="text" placeholder="请输入公司名称"></el-input>
					<!-- <el-select v-model="companyName" style="width:50%;" filterable remote :remote-method="remoteMethod"
						:loading="loading" @change="companyBtn()">
						<el-option v-for="(item,index) in nczData" :key="index" :label="item.companyName"
							:value="item.companyName">
							<span style="display: inline-block;float: left">{{item.companyName}}</span>
							<span style="color: #999;float: right;">{{item.creditCode}}<span
									style="color: #999;margin-left: 10px;"
									v-if="item.regCapital">(注册资本：{{item.regCapital}})</span></span>
						</el-option>
					</el-select> -->
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">简介图片：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in detailsImage" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear8(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								class="avatar-uploader" list-type="picture-card" :show-file-list="false"
								:on-success="handleAvatarSuccess9" :on-progress="onprogress2">
								<el-progress v-if="percentage2>0 && percentage2<100" type="circle"
									:percentage="percentage2"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">个人简介：</span>
					<el-input style="width:50%;" v-model="detailsText" type="text" placeholder="请输入个人简介"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">简介视频：</span>
					<!-- <el-input style="width:50%;" v-model="detailsVideo" type="text" placeholder="请输入简介视频"></el-input> -->
					<!-- <el-upload class="upload-demo" style="display: inline-block;"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="videoSuccess1" :before-upload="beforeVideoUpload">
						<el-button size="small" type="primary" v-loading="tableDataLoading5">点击上传</el-button>
					</el-upload> -->
					<div style="display: flex;margin-left: 200px;">
						<div v-for="(item,index) of detailsVideo" :key="index" style="display: inline-block;">
							<div style="position: relative;margin: 5px;">
								<video v-if="item!=''" :src="item" ref="videoRef" width="200" controls></video>
								<div @click="clear(index)" class="divhove" v-if="detailsVideo">
									<i class="el-icon-delete" style="margin: 0;font-size: 18px;color: #fff;"></i>
								</div>
							</div>
						</div>
						<el-upload class="upload-demo"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="videoSuccess1" :before-upload="beforeVideoUpload"
							:on-progress="handlePreviewV">
							<el-button size="small" type="primary" v-loading="tableDataLoading5">点击上传</el-button>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">年产值(单位:万元)：</span>
					<el-input style="width:50%;" v-model="companyOutputValue" type="text"
						placeholder="请输入年产值(单位:万元)"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">所属岗位：</span>
					<el-input style="width:50%;" v-model="companyPosition" type="text" placeholder="请输入所属岗位"></el-input>
					<!-- <el-select v-model="companyPosition" style="width:50%;">
						<el-option v-for="item in flxmData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select> -->
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">面积(单位:平方米)：</span>
					<el-input style="width:50%;" v-model="companyArea" type="number" :min="0"
						placeholder="请输入面积(单位:平方米)"></el-input>
					<!-- <el-select v-model="companyArea" style="width:50%;">
						<el-option v-for="item in gsmjData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select> -->
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">公司人数：</span>
					<el-input style="width:50%;" v-model="companyNum" placeholder="请输入公司人数"></el-input>
					<!-- <el-select v-model="peopleNum" style="width:50%;">
						<el-option v-for="item in gsrsData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select> -->
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">公司简介：</span>
					<el-input style="width:50%;" v-model="companyBlurb" type="text" placeholder="请输入公司简介"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">主营产品：</span>
					<el-input style="width:50%;" v-model="firstProduct" type="text" placeholder="请输入主营产品"></el-input>
					<!-- <quill-editor ref="myTextEditor" v-model="firstProduct" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
					</quill-editor> -->
				</div>
				
				<div style="margin-bottom: 10px;display: flex;align-items: center;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">主营产品图：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in firstProductImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear3(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								class="avatar-uploader" list-type="picture-card" :show-file-list="false"
								:on-success="handleUploadSuccess3" :on-progress="onprogress2">
								<el-progress v-if="percentage2>0 && percentage2<100" type="circle"
									:percentage="percentage2"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">公司LOGO：</span>
					<div style="display: inline-block;" class="imgs">
						<el-upload class="avatar-uploader" v-model="companyLogo"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="companyLogo" :src="companyLogo" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">公司环境：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in companyAmbient" :key="index">
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
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">用户头像：</span>
					<div style="display: inline-block;" class="imgs">
						<el-upload class="avatar-uploader" v-model="cardAvatar"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="cardAvatar" :src="cardAvatar" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">工作经历：</span>

					<span class="bqList" v-for="(item,index) in workExperience" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="clear1(index)"></i>
						</span>
					</span>
					<el-input v-model="bq1" style="width:150px;" placeholder="请输入岗位名称"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj1"></el-input>
					<el-input v-model="bq2" style="width:150px;" placeholder="请输入企业"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj1"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-plus" @click="btnTj1"> 添加
					</el-button>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">教育经历：</span>
				
					<span class="bqList" v-for="(item,index) in eduExperience" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="clear5(index)"></i>
						</span>
					</span>
					<el-input v-model="bq3" style="width:150px;" placeholder="请输入学习名称"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj4"></el-input>
					<el-input v-model="bq4" style="width:150px;" placeholder="请输入学历"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj4"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-plus" @click="btnTj4"> 添加
					</el-button>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">资质认证：</span>
					<div style="display: inline-block;">
						<div style="display: inline-block;margin:5px;position: relative;border: 1px dashed #c0ccda;padding: 5px;"
							v-for="(item,index) in companyFlairArr" :key="index">
							<span v-if="item.pic" style="margin: 3px;">
								<img v-for="(ite,ind) in item.pic.split(',')" :key="ind" :src="ite" style="width: 140px;height: 140px;">
							</span>
							
							<div style="text-align: center;margin-top: 5px;">{{item.name}}</div>
							<span class="delss delss2">
								<i class="el-icon-delete" @click="clear2(index)"></i>
							</span>
						</div>
					</div>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-plus" @click="btnTj"> 添加
					</el-button>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">营业执照：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in companyLicense" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear6(index)"></i>
							</span>
						</div>
						<div class="imgs" v-if="companyLicense.length==0">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								class="avatar-uploader" list-type="picture-card" :show-file-list="false"
								:on-success="handleUploadSuccess6" :on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">名片正面：</span>
					<div style="display: inline-block;" class="imgs">
						<el-upload class="avatar-uploader" v-model="cardImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess7">
							<img v-if="cardImg" :src="cardImg" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">名片背面：</span>
					<div style="display: inline-block;" class="imgs">
						<el-upload class="avatar-uploader" v-model="cardImgBack"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess8">
							<img v-if="cardImgBack" :src="cardImgBack" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">地区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" filterable remote
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 2">
					<span style="width: 200px;display: inline-block;text-align: right;">公司地址：</span>
					<el-input style="width:50%;" v-model="companyAddress" type="text"
						placeholder="请输入公司详细地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="this.screen == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">用户地址：</span>
					<el-input style="width:50%;" v-model="address" type="text" placeholder="请输入用户地址"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;"> @keydown.enter.native="selectDz"
					<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
					<el-input v-model="companyLat" style="width:50%;" disabled placeholder="请输入纬度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
					<el-input v-model="companyLng" style="width:50%;" disabled placeholder="请输入经度"></el-input>
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
			<el-dialog title="资质认证" :visible.sync="dialogVisibleZzrz" width="50%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">资质认证：</span>
					<el-select v-model="bq" style="width:50%;">
						<el-option v-for="item in zsData" :key="item.value" :label="item.value" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">资质认证图片：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in bqImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear7(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
								class="avatar-uploader" list-type="picture-card" :show-file-list="false"
								:on-success="handleUploadSuccess2" :on-progress="onprogress4">
								<el-progress v-if="percentage4>0 && percentage4<100" type="circle"
									:percentage="percentage4"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
					<!-- <div style="display: inline-block;" class="imgs">
						<el-upload class="avatar-uploader" v-model="bqImg"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleUploadSuccess2">
							<img v-if="bqImg" :src="bqImg" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div> -->
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogVisibleZzrz = false">取 消</el-button>
					<el-button type="primary" @click="refusetoZzrz()">确 定</el-button>
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
				phoneT: '',
				industryFirstNameT: '',
				industrySecondNameT: '',
				content: '',
				status: '',
				cityT: '',
				screen: 1,
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
						value: 0,
						label: '待审核'
					},
					{
						value: 1,
						label: '审核成功'
					}, {
						value: 2,
						label: '审核未通过'
					}, {
						value: 3,
						label: '会员到期已下架'
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
				titles: '添加服务',
				dialogFormVisibleR: false,
				
				companyFlairArr: [],
				bqImg: [],

				dataJson: [], //分类动态字段
				percentage1: 0,
				percentage2: 0,
				percentage3: 0,
				percentage4: 0,
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

				zsData: [],
				flxmData: [],
				gsmjData: [],
				nczData: [],
				gsrsData: [],

				classifyIds: [],
				tableDataFer: [],
				dialogVisibleZzrz: false,
				quillOption: quillConfig,
				list: {},
				vipIdT: '',
				startTime: '',
				endTime: '',
				loading: false,
				// -----
				userType: 1,

				wxCodeImg: '',
				factoryReport: [],
				companyLat: '',
				companyLng: '',

				// companyWebsite: "", //公司官网
				// keyWord: "", //搜索关键词
				companyBlurb: "", //公司简介
				// cardStyleCode: "", //样式code
				// shareCount: 1, //分享次数
				// popularityValue: 1, //人气值
				// legalPerson: "", //法人
				// registeredCapital: "", //注册资金
				// registeredTime: "2026-02-03 09:41:18", //注册时间
				// quota: 1, //额度
				// usedQuota: 1, //已使用额度
				// parentId: 1,
				cardId: 1, //名片id
				userId: 1, //用户id
				cardType: 1, //名片类型 名片类型 1=个人名片 2企业名片
				cardStatus: 1, //名片状态  0不展示 1展示
				auditStatus: 1, //审核状态 0待审核 1审核成功 2审核未通过 3会员到期已下架
				examineRemark: "", //审核备注
				cardAvatar: "", //用户头像
				cardPhone: "", //联系电话
				realName: "", //真实姓名
				wxCode:'',//微信号
				companyName: "", //公司名称
				companyPosition: "", //所属岗位(字典)
				companyProvince: "", //公司省
				companyCity: "", //公司 市
				companyDistrict: "", //公司区
				firstProductImg: [], //主营产品图
				companyAddress: "", //公司详细地址
				companyArea: 1, //面积(单位:平方米)
				companyLogo: "", //公司LOGO
				companyAmbient: [], //公司环境
				companyOutputValue: 1, //年产值(单位:万元)
				companyFlairs: [], //资质认证列表
				companyFlair: [], //资质认证
				firstProduct: "", //主营产品
				industryFirstId: 1, //行业一级id
				industrySecondId: 1, //行业二级id
				industryFirstName: "", //行业一级名称
				industrySecondName: "", //行业二级名称
				companyLicense: [], //营业执照
				companyNum: 1, //公司人数
				userName: "", //用户名
				cardImg: '', //名片照片
				email: "", //邮箱
				address: "", //地址
				workExperience: [], //工作经历
				eduExperience: [], //教育经历
				cardImgBack: "", //名片背面
				bq1: '',
				bq2: '',
				bq3: '',
				bq4: '',
				detailsImage:[],
				detailsText:'',
				detailsVideo:[],
				tableDataLoading5:false,




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
				if (tab._props.label === '个人名片') {
					this.page = 1
					this.limit = 10
					this.screen = 1
					this.status = ''
					this.dataSelect()
				}
				if (tab._props.label === '企业名片') {
					this.page = 1
					this.limit = 10
					this.screen = 2
					this.status = ''
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
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/examineCard'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'cardId': row.cardId,
						'examineRemark': ''
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
							onClose: () => {}
						})
					}
				})
			},
			//审核拒绝
			refuseClick(rows) {
				this.status = 2
				this.helpTakeId = rows.cardId
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/examineCard'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'cardId': this.helpTakeId,
						'examineRemark': this.content
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
				this.status = ''
				this.dataTitleT = ''
				this.userNameT = ''
				this.realNameT = ''
				this.industryFirstNameT = ''
				this.industrySecondNameT = ''
				// this.screen = ''
				this.vipIdT = ''
				this.startTime = ''
				this.endTime = ''


				this.dataSelect()
				// this.dataSelectHylist()
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
					url: this.$http.adornUrl('admin/businessCard/list'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'pageNum': this.page,
						'pageSize': this.limit,
						'userType': this.screen,
						'cardStatus': this.status,
						'companyName': this.dataTitleT,
						'userName': this.userNameT,
						'realName': this.realNameT,
						'industryFirstName': this.industryFirstNameT,
						'industrySecondName': this.industrySecondNameT,
						'vipId': this.vipIdT,
						'startTime': this.startTime,
						'endTime': this.endTime,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.data.data) {
						for (var i in data.data.data.list) {
							if (data.data.data.list[i].companyFlair) {
								data.data.data.list[i].companyFlairs = JSON.parse(data.data.data.list[i]
									.companyFlair)
							}
						}

					}
					console.log('data.data.data.list', data.data.data.list)

					let returnData = data.data.data
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
			changeTj(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/upOrDownNews'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'cardId': row.cardId,
						'downRemark': '',
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
			addBtn( row) {
				if (!row) {
					this.titles = '添加'
					this.cardId = ''
					this.userId = ''
					// this.cardType = 1
					this.cardStatus = 1
					this.auditStatus = 1
					this.cardAvatar = ''
					this.cardPhone = ''
					this.realName = ''
					this.wxCode = ''
					this.companyName = ''
					this.companyPosition = ''
					this.companyProvince = ''
					this.companyCity = ''
					this.companyDistrict = ''
					this.companyAddress = ''
					this.companyOutputValue = ''
					this.companyArea = ''
					this.companyLogo = ''
					this.firstProductImg = []
					this.companyAmbient = []
					this.workExperience = []
					this.eduExperience = []
					this.companyFlairs = []
					this.companyFlair = []
					this.firstProduct = ''
					this.companyBlurb = ''
					this.industryFirstId = ''
					this.industrySecondId = ''
					this.industryFirstName = ''
					this.industrySecondName = ''
					this.companyLicense = []
					this.companyNum = ''
					this.userName = ''
					this.cardImg = ''
					this.cardImgBack = ''
					this.email = ''
					this.address = ''
					this.detailsImage = []
					this.bq1 = ''
					this.bq2 = ''
					this.bq3 = ''
					this.bq4 = ''
					this.storeAddress = []
					this.storeAddre = '请选择城市'
					this.bqImg = []
					this.bq = ''
					this.detailsText = ''
					this.detailsVideo = []
					
					
				} else {
					this.titles = '修改'
					this.cardId = row.cardId
					this.userId = row.userId
					this.cardType = row.cardType
					if (row.cardStatus) {
						this.cardStatus = row.cardStatus
					} else {
						this.cardStatus = 1
					}
					this.auditStatus = row.cardType
					this.cardAvatar = row.cardAvatar
					this.cardPhone = row.cardPhone
					this.realName = row.realName
					this.wxCode = row.wxCode
					this.companyName = row.companyName
					this.companyPosition = row.companyPosition
					this.companyProvince = row.companyProvince
					this.companyCity = row.companyCity
					this.companyDistrict = row.companyDistrict
					this.companyAddress = row.companyAddress
					if (row.firstProductImg && row.firstProductImg != 'null') {
						this.firstProductImg = row.firstProductImg.split(',')
					} else {
						this.firstProductImg = []
					}
					if (row.detailsImage && row.detailsImage != 'null') {
						this.detailsImage = row.detailsImage.split(',')
					} else {
						this.detailsImage = []
					}
					this.detailsText = row.detailsText
					if(row.detailsVideo){
						this.detailsVideo = row.detailsVideo.split(',')
					}else{
						this.detailsVideo = []
					}
					
					this.companyArea = row.companyArea
					if (row.companyLogo && row.companyLogo != 'null') {
						this.companyLogo = row.companyLogo
					} else {
						this.companyLogo = ''
					}
					if (row.companyAmbient && row.companyAmbient != 'null') {
						this.companyAmbient = row.companyAmbient.split(',')
					} else {
						this.companyAmbient = []
					}
					this.companyOutputValue = row.companyOutputValue
					if (row.companyFlair && row.companyFlair != 'null') {
						// this.companyFlair = row.companyFlair.split(',')
						// var arr = JSON.parse(row.companyFlair)
						this.companyFlair = row.companyFlair
					} else {
						this.companyFlair = []
					}
					this.firstProduct = row.firstProduct
					this.companyBlurb = row.companyBlurb
					this.industryFirstId = row.industryFirstId
					this.industrySecondId = row.industrySecondId
					if (row.companyLicense && row.companyLicense != 'null') {
						this.companyLicense = row.companyLicense.split(',')
					} else {
						this.companyLicense = []
					}
					
					this.companyNum = row.companyNum
					this.email = row.email
					this.cardImg = row.cardImg
					this.cardImgBack = row.cardImgBack
					this.emai = row.emai
					this.address = row.address
					this.workExperience = []
					this.eduExperience = []
					if (row.workExperience && row.workExperience != 'null') {
						var arr  = row.workExperience.split(';')
						
						for(var i in arr){
							if(arr[i]!=''){
								this.workExperience.push(arr[i])
							}
						}
						console.log('this.workExperience',this.workExperience)
					} else {
						this.workExperience = []
					}
					if (row.eduExperience && row.eduExperience != 'null') {
						 
						var arr  = row.eduExperience.split(';')
						
						for(var i in arr){
							if(arr[i]!=''){
								this.eduExperience.push(arr[i])
							}
						}
					} else {
						this.eduExperience = []
					}
					if (row.companyFlairs && row.companyFlairs.length>0) {
						this.companyFlairArr = row.companyFlairs
					} else {
						this.companyFlairArr = []
					}
					
					this.bq1 = ''
					this.bq2 = ''
					this.bq3 = ''
					this.bq4 = ''
					this.storeAddress = []
					if(this.screen == 1){
						this.storeAddre =this.companyCity
					}else{
						this.storeAddre = this.companyProvince + '/' + this.companyCity + '/' + this.companyDistrict
					}
					
					this.bqImg = []
					this.bq = ''
					// ---------
					
					
				}
				this.flxmSelect()
				this.dataSelectW()
				this.dataSelectFer()
				// this.getMyLocation()
				this.dialogFormVisibleR = true
			},
			// 确认 添加、修改
			refusetoUp() {
				
				if(this.industryFirstId==''||this.industryFirstId==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '一级分类不能为空',
					  type: 'warning'
					});
					return
				}
				if(this.industrySecondId==''||this.industrySecondId==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '二级分类不能为空',
					  type: 'warning'
					});
					return
				}
				if(this.cardPhone==''||this.cardPhone==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '联系电话不能为空',
					  type: 'warning'
					});
					return
				}
				if(this.realName==''||this.realName==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '真实姓名不能为空',
					  type: 'warning'
					});
					return
				}
				if(this.wxCode==''||this.wxCode==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '微信号不能为空',
					  type: 'warning'
					});
					return
				}
				
				if(this.companyName==''||this.companyName==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '公司名称不能为空',
					  type: 'warning'
					});
					return
				}
				if(this.companyPosition==''||this.companyPosition==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '所属岗位不能为空',
					  type: 'warning'
					});
					return
				}
				if(this.companyCity==''||this.companyCity==null){
					this.$notify({
					  title: '提示',
					  duration: 1800,
					  message: '地区不能为空',
					  type: 'warning'
					});
					return
				}
				
				
				
				
				
				if(this.screen==1){
					if(this.workExperience.length==0){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '工作经历不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.eduExperience.length==0){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '教育经历不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.cardImgBack==''||this.cardImgBack==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '名片背面不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.address==''||this.address==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '用户地址不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.cardImg==''||this.cardImg==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '名片正面不能为空',
						  type: 'warning'
						});
						return
					}
					
				}else{
					
					if(this.companyAddress==''||this.companyAddress==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '公司地址不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.companyArea==''||this.companyArea==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '面积不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.companyLogo==''||this.companyLogo==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '公司LOGO不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.companyBlurb==''||this.companyBlurb==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '请输入公司简介',
						  type: 'warning'
						});
						return
					}
					if(this.companyAmbient.length==0){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '公司环境不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.companyOutputValue===''||this.companyOutputValue==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '年产值不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.companyFlairArr.length==0){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '资质认证不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.companyLicense.length==0){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '营业执照不能为空',
						  type: 'warning'
						});
						return
					}
					if(this.firstProduct==''||this.firstProduct==null){
						this.$notify({
						  title: '提示',
						  duration: 1800,
						  message: '主营产品不能为空',
						  type: 'warning'
						});
						return
					}
					
				}
				
				
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/saveBusinessCard'),
					method: 'post',
					data: this.$http.adornData({
						
						'cardId': this.cardId,
						'userId': this.userId,
						'cardType': this.screen,
						'cardStatus': this.cardStatus,
						'auditStatus': this.auditStatus,
						'cardAvatar': this.cardAvatar,
						'cardPhone': this.cardPhone,
						'realName': this.realName,
						'wxCode':this.wxCode,
						'companyName': this.companyName,
						'companyPosition': this.companyPosition,
						'companyProvince': this.companyProvince,
						'companyCity': this.companyCity,
						'companyDistrict': this.companyDistrict,
						'firstProductImg': this.firstProductImg.toString(),
						'companyAddress': this.companyAddress,
						'companyArea': this.companyArea,
						'companyLogo': this.companyLogo,
						'companyAmbient': this.companyAmbient.toString(),
						'companyOutputValue': this.companyOutputValue,
						'companyFlair': JSON.stringify(this.companyFlairArr),
						'firstProduct': this.firstProduct,
						'industryFirstId': this.industryFirstId,
						'industrySecondId': this.industrySecondId,
						'companyLicense': this.companyLicense.toString(),
						'companyNum': this.companyNum,
						'cardImg': this.cardImg,
						'email': this.email,
						'address': this.address,
						'workExperience': this.workExperience.toString(),
						'eduExperience': this.eduExperience.toString(),
						'cardImgBack': this.cardImgBack,
						'companyBlurb':this.companyBlurb,
						'detailsImage':this.detailsImage.toString(),
						'detailsText':this.detailsText,
						'detailsVideo':this.detailsVideo.toString(),


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
			handleAvatarSuccess2(file, fileList) {
				this.cardAvatar = file.data
			},

			handleUploadSuccess2(file, fileList) {
				this.bqImg.push(file.data)
				// this.bqImg = file.data
			},
			//上传主营产品图
			handleUploadSuccess3(file, fileList) {
				this.firstProductImg.push(file.data)
			},
			//上传公司环境
			handleUploadSuccess4(file, fileList) {
				this.companyAmbient.push(file.data)
			},
			// 营业执照
			handleUploadSuccess6(file, fileList) {
				// this.companyFlairImg.push(file.data)
				this.companyLicense.push(file.data)
			},
			// 上传封面图
			handleAvatarSuccess7(file, fileList) {
				this.cardImg = file.data
			},
			handleAvatarSuccess8(file, fileList) {
				this.cardImgBack = file.data
			},
			handleAvatarSuccess9(file, fileList) {
				this.detailsImage.push(file.data)
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
			onprogress4(event, file, fileList) {
				this.percentage4 = parseInt(event.percent)
			},
			// 删除轮播图
			clear1(index) {
				this.workExperience.splice(index, 1);
			},
			// 删除主营产品图
			clear3(index) {
				this.firstProductImg.splice(index, 1);
			},
			// 删除公司环境
			clear4(index) {
				this.companyAmbient.splice(index, 1);
			},
			// 删除教育经历
			clear5(index) {
				this.eduExperience.splice(index, 1);
			},
			// 删除营业执照
			clear6(index) {
				this.companyLicense.splice(index, 1);
			},
			clear7(index) {
				this.bqImg.splice(index, 1);
			},
			clear8(index) {
				this.detailsImage.splice(index, 1);
			},
			// 验证只能上传视频
			beforeVideoUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'mp4'
				if (!extension) {
					this.$message({
						message: "上传文件只能是mp4格式！",
						type: 'error'
					})
				}
				return extension;
			},
			handlePreviewV(file) {
				this.tableDataLoading5 = true
			},
			videoSuccess1(file) {
				console.log(file)
				this.detailsVideo.push(file.data)
				this.tableDataLoading5 = false
			},
			// 删除
			clear(index) {
				this.detailsVideo.splice(index, 1);
			},
			// 删除标签
			clear2(index) {
				this.companyFlairArr.splice(index, 1);
				// this.companyFlair.splice(index, 1);
				this.companyFlairImg.splice(index, 1);
			},
			// 添加标签
			btnTj() {
				// if (this.bq == '' || this.bq == ' ') {
				//   this.$notify({
				//     title: '提示',
				//     duration: 1800,
				//     message: '请输入标签',
				//     type: 'warning'
				//   });
				//   return
				// } else {
				//   console.log('this.bq', this.bq)
				//   this.companyFlair.push(this.bq)
				this.bq = ''
				this.bqImg = []
				this.dialogVisibleZzrz = true
				// }
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
				if (this.bqImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传认证图片',
						type: 'warning'
					});
					return
				}
				var datas = {
					pic: this.bqImg.toString(),
					name: this.bq
				}
				this.companyFlairArr.push(datas)
				// this.companyFlair.push(this.bq)
				// this.companyFlairImg.push(this.bqImg.toString())
				this.dialogVisibleZzrz = false

			},
			// 添加学历
			btnTj1() {
				if (this.bq1 == '' || this.bq2 == ' ') {
				  this.$notify({
				    title: '提示',
				    duration: 1800,
				    message: '岗位名称和企业不能为空',
				    type: 'warning'
				  });
				  return
				} else {
					var datas=this.bq1+','+this.bq2
				  this.workExperience.push(datas)
				this.bq1 = ''
				this.bq2 = ''
				}
			},
			// 添加工作经历
			btnTj4() {
				if (this.bq3 == '' || this.bq4 == ' ') {
				  this.$notify({
				    title: '提示',
				    duration: 1800,
				    message: '学习名称和学历不能为空',
				    type: 'warning'
				  });
				  return
				} else {
					var datas=this.bq3+','+this.bq4
				  this.eduExperience.push(datas)
				this.bq3 = ''
				this.bq4 = ''
				}
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.companyProvince = value[0]
				this.companyCity = value[1]
				this.companyDistrict = value[2]

				if (this.companyCity == '市辖区') {
					this.companyCity = this.companyProvince
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
						'isVip': 1,
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
				if (this.$route.query.status != null) {
					this.status = this.$route.query.status
				} else {
					this.status = ''
				}
				if (this.$route.query.companyName != null) {
					this.dataTitleT = this.$route.query.companyName
				} else {
					this.dataTitleT = ''
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
						url: this.$http.adornUrl('admin/businessCard/del/' + row.cardId),
						method: 'get',
						params: this.$http.adornParams({
							// 'cardId': row.cardId
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
			// 证书管理
			flxmSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '资质证书'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.zsData = returnData
				})
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '岗位'
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
				// 		'type': '公司面积'
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	let returnData = data.data
				// 	this.gsmjData = returnData
				// })
				// this.$http({
				// 	url: this.$http.adornUrl('sys/dict/selectDictList'),
				// 	method: 'get',
				// 	params: this.$http.adornParams({
				// 		'type': '年产值'
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	let returnData = data.data
				// 	this.nczData = returnData
				// })
				// this.$http({
				// 	url: this.$http.adornUrl('sys/dict/selectDictList'),
				// 	method: 'get',
				// 	params: this.$http.adornParams({
				// 		'type': '公司人数'
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	let returnData = data.data
				// 	this.gsrsData = returnData
				// })
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

				var myLatlng = new qq.maps.LatLng(this.companyLat, this.companyLng);
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
				if (that.companyLng == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.companyLat, that.companyLng);
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
					that.companyLng = event.latLng.getLng(); // 经度
					that.companyLat = event.latLng.getLat(); // 纬度

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
						that.companyLng = result.detail.location.lng;
						that.companyLat = result.detail.location.lat;
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

				var add = this.companyProvince + this.companyCity + this.companyDistrict + this.companyAddress
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
					that.companyLng = response.result.location.lng; // 经度
					that.companyLat = response.result.location.lat; // 纬度
					// that.companyCity = response.result.address_components.city;
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
					url: this.$http.adornUrl('admin/businessCard/excelBusinessCardList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'userType': this.screen,
						'cardStatus': this.status,
						'companyName': this.dataTitleT,
						'userName': this.userNameT,
						'realName': this.realNameT,
						'industryFirstName': this.industryFirstNameT,
						'industrySecondName': this.industrySecondNameT,
						'vipId': this.vipIdT,
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
						elink.download = '名片列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
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
			companyBtn() {
				console.log(this.nczData, '------', this.companyName)
				if (this.nczData.length > 0) {
					for (var i in this.nczData) {
						if (this.companyName == this.nczData[i].companyName) {
							this.companyOutputValue = this.nczData[i].regCapital
						}
					}
				}

			}
		},
		mounted() {
			if (this.$route.query.status != null) {
				this.status = this.$route.query.status
			} else {
				this.status = ''
			}
			if (this.$route.query.companyName != null) {
				this.dataTitleT = this.$route.query.companyName
			} else {
				this.dataTitleT = ''
			}
			//名片列表
			this.dataSelect()
			// this.dataSelectHylist()
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
		padding: 6px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 13px;
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
	.eit {
		height: 120px;
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
	
	.divhove {
		position: absolute;
		width: 100%;
		height: 100px;
		line-height: 110px;
		left: 0;
		top: 0;
		opacity: 0;
		cursor: default;
		text-align: center;
		padding-top: 0;
		border-radius: 6px;
		background-color: rgba(0, 0, 0, .5);
		-webkit-transition: opacity .3s;
		transition: opacity .3s;
		z-index: 9999;
	}
	
	.el-tag--medium {
		margin-right: 10px;
	}
	
	.divhove:hover {
		opacity: 0.8;
	}
	
	.divhove:hover .divhove {
		line-height: 148px;
		color: red;
	}
	
	.text_color span {
		margin-right: 5px;
	}
</style>