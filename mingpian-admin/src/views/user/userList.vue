<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户列表</h2>
			<p class="nf-page-hero__desc">管理平台注册用户，支持按昵称、手机号、邀请码、渠道来源等筛选，可查看详情、赠送会员及导出数据</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户昵称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">手机号</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">邀请码</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="inviterCode"></el-input>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">渠道来源</span>
					<el-select v-model="platformT" class="nf-filter-form__input" @change="select()">
						<el-option v-for="item in homeDataQ" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">是否是推广员</span>
					<el-select v-model="isPromoterT" class="nf-filter-form__input" @change="select()">
						<el-option v-for="item in homeDataT" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-filter-form__item" v-if="activeName === 'third'">
					<span class="nf-filter-form__label">会员等级</span>
					<el-select v-model="vipIdT" class="nf-filter-form__input" @change="select()">
						<el-option v-for="item in list.records" :key="item.id" :label="item.vipName" :value="item.id">
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
					<el-date-picker class="nf-filter-form__input nf-filter-form__input--date" v-model="endTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button type="warning" icon="document" @click="exportBtn"
						:disabled="!isAuth('financeList:daochu')">导出Excel</el-button>
				</div>
			</div>
			<p class="nf-export-tip">* 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</p>
		</div>

		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全部用户" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="isVip" label="是否为会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="staffInvitationCode" label="邀请码"></el-table-column>
					<el-table-column prop="staffInviterCode" label="上级邀请码"></el-table-column>
					
					<!-- <el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column> -->
					
					<!-- <el-table-column prop="zhiRate" label="一级佣金比例" width="150">
            <template slot-scope="scope">
              <span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
              <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
                style="color: #2f3968;background: #fff;border: none;"
                @click="xiugai(scope.row,'zhiRate')">修改</el-button>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="feiRate" label="二级佣金比例" width="150">
            <template slot-scope="scope">
              <span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
              <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
                style="color: #2f3968;background: #fff;border: none;"
                @click="xiugai(scope.row,'feiRate')">修改</el-button>
              </span>
            </template>
          </el-table-column> -->
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>

					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>

					<el-table-column prop="isPromoter" label="是否推广员" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isPromoter" @change="changeIp(scope.row)"
								:active-value="1" :disabled="!isAuth('userList:update')"
								:inactive-value="0" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')">赠送会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1"
								plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
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
			<el-tab-pane label="普通用户" name="second">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="isVip" label="是否为会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="staffInvitationCode" label="邀请码"></el-table-column>
					<el-table-column prop="staffInviterCode" label="上级邀请码"></el-table-column>
					<!-- <el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column> -->
					
					<!-- <el-table-column prop="zhiRate" label="一级佣金比例" width="150">
            <template slot-scope="scope">
              <span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
              <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
                style="color: #2f3968;background: #fff;border: none;"
                @click="xiugai(scope.row,'zhiRate')">修改</el-button>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="feiRate" label="二级佣金比例" width="150">
            <template slot-scope="scope">
              <span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
              <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
                style="color: #2f3968;background: #fff;border: none;"
                @click="xiugai(scope.row,'feiRate')">修改</el-button>
              </span>
            </template>
          </el-table-column> -->
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="isPromoter" label="是否推广员" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isPromoter" @change="changeIp(scope.row)"
								:active-value="1" :disabled="!isAuth('userList:update')"
								:inactive-value="0" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')">赠送会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1"
								plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
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
			<el-tab-pane label="会员用户" name="third">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records" height="500px">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="nf-table-link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="当前余额" width="80"></el-table-column>

					<el-table-column prop="isVip" label="是否为会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="staffInvitationCode" label="邀请码"></el-table-column>
					<el-table-column prop="staffInviterCode" label="上级邀请码"></el-table-column>
					<!-- <el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column> -->
					
					<!-- <el-table-column prop="zhiRate" label="一级佣金比例" width="150">
            <template slot-scope="scope">
              <span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
              <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
                style="color: #2f3968;background: #fff;border: none;"
                @click="xiugai(scope.row,'zhiRate')">修改</el-button>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="feiRate" label="二级佣金比例" width="150">
            <template slot-scope="scope">
              <span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
              <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
                style="color: #2f3968;background: #fff;border: none;"
                @click="xiugai(scope.row,'feiRate')">修改</el-button>
              </span>
            </template>
          </el-table-column> -->
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="isPromoter" label="是否推广员" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isPromoter" @change="changeIp(scope.row)"
								:active-value="1" :disabled="!isAuth('userList:update')"
								:inactive-value="0" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')">赠送会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1"
								plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
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

			<!-- 修改比例 -->
			<el-dialog title="修改数值" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">数值：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						placeholder="请输入数值"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 新增平台用户 -->
			<el-dialog :title="titles" :visible.sync="dialogVisible" width="60%" @close="close">
				<el-form :model="userinfo" ref="dynamicValidateForm" label-width="100px" class="demo-dynamic">
					<el-form-item prop="userName" label="用户名"
						:rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
						<el-input v-model="userinfo.userName" placeholder="请输入用户名"></el-input>
					</el-form-item>
					<el-form-item prop="nickName" label="作者昵称"
						:rules="[{ required: true, message: '请输入作者昵称', trigger: 'blur' }]">
						<el-input v-model="userinfo.nickName" placeholder="请输入作者昵称"></el-input>
					</el-form-item>
					<el-form-item prop="avatar" label="头像"
						:rules="[{ required: true, message: '请上传头像', trigger: 'blur' }]">
						<el-upload class="avatar-uploader" :action="$http.adornUrl(`/alioss/upload`)"
							:show-file-list="false" :on-success="handleAvatarSuccess">
							<img v-if="userinfo.avatar" :src="userinfo.avatar" class="avatar">
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</el-form-item>
					<el-form-item prop="sex" label="性别" :rules="[{ required: true, message: '请选择性别', trigger: 'blur'}]">
						<el-radio-group v-model="userinfo.sex">
							<el-radio :label="1">男</el-radio>
							<el-radio :label="2">女</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item prop="age" label="年龄"
						:rules="[{ required: true, message: '请输入年龄', trigger: 'blur' }]">
						<el-input v-model="userinfo.age" placeholder="请输入年龄"></el-input>
					</el-form-item>
					<el-form-item prop="phone" label="手机号"
						:rules="[{ required: true, message: '请输入手机号', trigger: 'blur' },{ pattern: /^1[3456789]\d{9}$/, message: '请输入正确手机号码', trigger: 'blur' }]">
						<el-input v-model="userinfo.phone" placeholder="请输入手机号"></el-input>
					</el-form-item>
					<el-form-item prop="password" label="密码">
						<el-input v-model="userinfo.password" show-password placeholder="请输入密码"></el-input>
					</el-form-item>
					<el-form-item prop="sampleReelsUrl" label="作品集地址"
						:rules="[{ required: true, message: '请输入作品集地址', trigger: 'blur' }]">
						<el-input v-model="userinfo.sampleReelsUrl" placeholder="请输入作品集地址"></el-input>
					</el-form-item>
					<el-form-item prop="email" label="邮箱"
						:rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' },{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}]">
						<el-input v-model="userinfo.email" placeholder="请输入邮箱"></el-input>
					</el-form-item>
					<el-form-item prop="introduction" label="个人简介"
						:rules="[{ required: true, message: '请输入个人简介', trigger: 'blur' }]">
						<el-input v-model="userinfo.introduction" type="textarea" placeholder="请输入个人简介"></el-input>
					</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="dialogVisible = false">取 消</el-button>
					<el-button type="primary" @click="submitForm('dynamicValidateForm')">确 定</el-button>
				</span>
			</el-dialog>
			<!-- 修改会员 -->
			<el-dialog title="赠送会员" :visible.sync="dialogFormVisibleZs" center width="60%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div> -->
				<div v-if="isVip==1">
					<div style="margin-bottom: 10px;">
						<span
							style="width: 200px;display: inline-block;text-align: right;">当前会员：</span>{{tableDataHy.vipName}}
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">会员到期时间：</span>{{vipEndTime}}
					</div>
				</div>
				<div v-else style="max-height: 500px;">
					<el-table v-loading="tableDataLoading1" ref="singleTable" :data="list.list" highlight-current-row
						@current-change="handleCurrentBtn" height="400px">
						<el-table-column label="编号" prop="id" width="80">
						</el-table-column>
						<el-table-column label="会员名称" prop="vipName">
						</el-table-column>
						<el-table-column label="月数" prop="vipMonthCount">
						</el-table-column>
						<el-table-column label="会员价格" prop="money">
						</el-table-column>
						<el-table-column label="操作" width="80">
							<template slot-scope="scope">

								<el-button size="mini" type="primary" @click="setCurrent(scope.row)">选择</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
				<div style="margin-bottom: 10px;margin-top: 20px;">
					<span style="width: 200px;display: inline-block;text-align: right;">赠送份数：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						placeholder="请输入赠送份数"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleZs = false">取 消</el-button>
					<el-button type="primary" @click="updateVip()">确 定</el-button>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				titles: '',
				dialogVisible: false,
				userinfo: {
					userName: '',
					nickName: '',
					avatar: '',
					sex: 1,
					age: '',
					phone: '',
					password: '',
					typeUser: 1,
					sampleReelsUrl: '',
					email: '',
					introduction: '',
					userId: '',
				},
				typeUser: '',
				openValue: 1,
				closeValue: 2,
				limit: 10,
				page: 1,
				userName: '',
				isAuthentication: '',
				nickNam: '',
				phone: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				isVipT: '',
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				tableDataLoading1: false,
				dialogFormVisible2: false,
				tableData: [],
				campus: '',
				homeData1: [],
				proportion: '',
				texts: '',
				userIdss: '',
				platformT: '',
				homeDataQ: [{
					value: '',
					label: '全部'
				}, {
					value: 'H5',
					label: 'H5'
				}, {
					value: 'app',
					label: 'app'
				}, {
					value: '小程序',
					label: '小程序'
				}, ],
				isPromoterT:'',
				homeDataT: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '是'
				}, {
					value: 0,
					label: '否'
				},],
				userType: '',
				inviterCode: '',
				dialogFormVisibleZs: false,
				isVip: '',
				type: 1,
				vipId: '',
				vipEndTime: '',
				tableDataHy: {},
				list: {},
				currentRow: null,
				isAgent: '',
				isStaff: '',
				vipIdT: '',
				startTime: '',
				endTime: '',
				isPriceUser: '',
			}
		},
		methods: {
			//修改弹窗赋值
			uploadUser(row) {
				this.userinfo = {
					userName: row.userName,
					nickName: row.nickName,
					sex: row.sex,
					age: row.age,
					avatar: row.avatar,
					phone: row.phone,
					userId: row.userId,
					// password: row.password,
					typeUser: 1,
					sampleReelsUrl: row.sampleReelsUrl,
					email: row.email,
					introduction: row.introduction,
				}
				this.titles = '修改';
				this.dialogVisible = true
			},
			//上传
			handleAvatarSuccess(res, file) {
				this.userinfo.avatar = res.data;
			},
			close() {
				this.userinfo = {
					userName: '',
					nickName: '',
					sex: 1,
					age: '',
					avatar: '',
					phone: '',
					password: '',
					typeUser: 1,
					sampleReelsUrl: '',
					email: '',
					introduction: '',
				}
			},
			//新增用户
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						let url = ""
						let from = this.userinfo
						if (this.titles == '新增') {
							url = "/user/addUser"
							delete from.userId
						} else {
							url = "/user/updateTbUser"
							// delete from.password
						}
						this.$http({
							url: this.$http.adornUrl(url),
							method: 'post',
							data: this.$http.adornData(from)
						}).then(({
							data
						}) => {
							if (data && data.code == 0) {
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.dialogVisible = false
										this.dataSelect()
									}
								})
							} else {
								this.$message({
									message: data.msg,
									type: 'error',
									duration: 1500,
									onClose: () => {
										this.dialogVisible = false
										this.dataSelect()
									}
								})
							}

						})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.phone = ''
				this.inviterCode = ''
				this.vipIdT = ''
				this.isPromoterT = ''


				if (tab._props.label == '全部用户') {
					this.userType = ''
					this.isVipT = ''
					this.isAgent = ''
					this.isStaff = ''
					this.isPriceUser = ''
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.userType = 1
					this.isVipT = 0
					this.isAgent = 0
					this.isStaff = 0
					this.isPriceUser = 2
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.userType = ''
					this.isVipT = 1
					this.isAgent = ''
					this.isStaff = ''
					this.isPriceUser = ''
					this.dataSelect()
					this.dataSelectHylist()
				}
				if (tab._props.label == '店铺用户') {
					this.userType = 2
					this.isVipT = ''
					this.isAgent = ''
					this.isStaff = ''
					this.isPriceUser = ''
					this.dataSelect()
				}
				if (tab._props.label == '合伙人用户') {
					this.userType = ''
					this.isVipT = ''
					this.isAgent = ''
					this.isPriceUser = ''
					this.isStaff = 1
					this.dataSelect()
				}
				if (tab._props.label == '采购资源代理用户') {
					this.userType = ''
					this.isVipT = ''
					this.isAgent = 1
					this.isStaff = ''
					this.isPriceUser = ''
					this.dataSelect()
				}
				if (tab._props.label == '激活用户') {
					this.userType = ''
					this.isVipT = ''
					this.isAgent = ''
					this.isStaff = ''
					this.isPriceUser = 1
					this.dataSelect()
				}



			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
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
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 详情跳转
			updates2(row) {
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': row.inviterCode
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$router.push({
							path: '/userDetail',
							query: {
								userId: data.data.userEntity.userId
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.userName = ''
				this.phone = ''
				this.inviterCode = ''
				this.platformT = ''
				this.vipIdT = ''
				this.startTime = ''
				this.endTime = ''
				this.isPromoterT = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'isVip': this.isVipT,
						'userName': this.userName,
						'phone': this.phone,
						'platform': this.platformT,
						'userType': this.userType,
						'staffInvitationCode': this.inviterCode,
						'isAgent': this.isAgent,
						'isStaff': this.isStaff,
						'vipId': this.vipIdT,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'isPriceUser': this.isPriceUser,
						'isPromoter':this.isPromoterT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			StairNoticeTo2() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}
				if (this.texts == 'zhiRate') {
					datas.zhiRate = this.proportion
				}
				if (this.texts == 'feiRate') {
					datas.feiRate = this.proportion
				}
				if (this.texts == 'addRate') {
					if (this.proportion === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.proportion) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\d+(?:\.\d{1,2})?$/;
					if (!regex.test(Number(this.proportion))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值最多可输入两位小数',
							type: 'warning'
						});
						return
					}

					datas.addRate = this.proportion
				}
				if (this.texts == 'addNum') {
					if (this.proportion === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.proportion) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\+?[1-9][0-9]*$/;
					if (!regex.test(Number(this.proportion))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值应为正整数',
							type: 'warning'
						});
						return
					}
					datas.addNum = this.proportion
				}
				if (this.texts == 'vipViewRate') {
					if (this.proportion === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.proportion) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\d+(?:\.\d{1,2})?$/;
					if (!regex.test(Number(this.proportion))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值最多可输入两位小数',
							type: 'warning'
						});
						return
					}
					datas.vipViewRate = this.proportion
				}
				if (this.texts == 'userAddRate') {
					if (this.proportion === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.proportion) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\d+(?:\.\d{1,2})?$/;
					if (!regex.test(Number(this.proportion))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值最多可输入两位小数',
							type: 'warning'
						});
						return
					}
					datas.userAddRate = this.proportion
				}
				if (this.texts == 'userAddNum') {
					if (this.proportion === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.proportion) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\d+(?:\.\d{1,2})?$/;
					if (!regex.test(Number(this.proportion))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值最多可输入两位小数',
							type: 'warning'
						});
						return
					}
					datas.userAddNum = this.proportion
				}
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible2 = false
							this.dataSelect()
						}
					})
				})
			},
			// 修改会员弹框
			zengsongVip(row) {
				console.log('row.isVip', row.isVip)
				this.type = 1
				this.isVip = row.isVip
				this.proportion = ''
				this.userIdss = row.userId
				if (row.isVip == 1) {
					console.log('----------')
					this.vipId = row.vipId
					this.vipEndTime = row.vipEndTime
					this.dataSelectHyxq()
					this.dialogFormVisibleZs = true
				} else {
					console.log('-----333333-----')
					this.dataSelectHylist()
					this.vipId = ''
					this.vipEndTime = ''
					this.dialogFormVisibleZs = true
				}


			},
			// 设置会员
			updateVip(row) {

				if (this.vipId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择赠送类型',
						type: 'warning'
					});
					return
				}
				if (this.proportion === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入份数',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/giveUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': this.userIdss,
						'count': this.proportion,
						'vipId': this.vipId,
						// 'type':this.type
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
								this.dialogFormVisibleZs = false
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
			// 取消会员
			quxiaoVip(row) {
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/vipDetails/cancelUserVip'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': row.userId
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
								onClose: () => {}
							})
						}

					})
				})
			},
			xiugai(row, text) {
				if (text == 'rate') {
					this.proportion = row.rate
				}
				if (text == 'zhiRate') {
					this.proportion = row.zhiRate
				}
				if (text == 'feiRate') {
					this.proportion = row.feiRate
				}

				if (text == 'addRate') {
					this.proportion = row.addRate
				}
				if (text == 'addNum') {
					this.proportion = row.addNum
				}
				if (text == 'vipViewRate') {
					this.proportion = row.vipViewRate
				}
				if (text == 'userAddRate') {
					this.proportion = row.userAddRate
				}
				if (text == 'userAddNum') {
					this.proportion = row.userAddNum
				}
				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			// StairNoticeTo2() {
			// 	var datas = {}
			// 	if (this.texts == 'rate') {
			// 		datas.rate = this.proportion
			// 	}
			// 	if (this.texts == 'zhiRate') {
			// 		datas.zhiRate = this.proportion
			// 	}
			// 	if (this.texts == 'feiRate') {
			// 		datas.feiRate = this.proportion
			// 	}
			// 	datas.userId = this.userIdss
			// 	this.$http({
			// 		url: this.$http.adornUrl('user/updateUserByUserId'),
			// 		method: 'post',
			// 		data: this.$http.adornData(datas)
			// 	}).then(({
			// 		data
			// 	}) => {
			// 		console.log('data', data)
			// 		this.$message({
			// 			message: '修改成功',
			// 			type: 'success',
			// 			duration: 1500,
			// 			onClose: () => {
			// 				this.dialogFormVisible2 = false
			// 				this.dataSelect()
			// 			}
			// 		})
			// 	})
			// },
			// 查看会员详情
			dataSelectHyxq() {
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/getVipDetailsById'),
					method: 'get',
					params: this.$http.adornParams({
						'id': this.vipId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataHy = returnData
				})
			},
			//获取会员列表
			dataSelectHylist() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/list'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'pageNum': 1,
						'pageSize': 50
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					if (data && data.code === 0) {
						let returnData = data.data.data
						this.list = returnData
					}

				})
			},
			setCurrent(row) {
				this.vipId = row.id
				this.$refs.singleTable.setCurrentRow(row);
			},
			handleCurrentBtn(val) {
				this.currentRow = val;
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('user/excelUserList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'isVip': this.isVipT,
						'userName': this.userName,
						'phone': this.phone,
						'platform': this.platformT,
						'userType': this.userType,
						'staffInvitationCode': this.inviterCode,
						'isAgent': this.isAgent,
						'isStaff': this.isStaff,
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
						elink.download = '用户列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 修改 是否推广员
			changeIp(row) {
				this.$http({
					url: this.$http.adornUrl(`user/updatePromoter`),
					method: 'get',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId': row.userId,
						'promoter': row.isPromoter,
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
		},
		mounted() {
			console.log(1)
			this.dataSelect()
		},
		// watch: {
		//   $route(to, from) {
		//     // 根据条件判断是否强制刷新组件
		//     if (!from.meta.keepAlive) {
		//       this.$nextTick(() => {
		//         this.$forceUpdate();
		//       });
		//     }
		//   }
		// }
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
		width: 160px;
	}

	.nf-filter-form__actions {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		flex-wrap: wrap;
	}

	.nf-export-tip {
		margin: 12px 0 0;
		font-size: 13px;
		color: #e6a23c;
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
		width: 178px;
		height: 178px;
	}

	.el-upload:hover {
		border-color: #2f3968;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}

	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	}
</style>