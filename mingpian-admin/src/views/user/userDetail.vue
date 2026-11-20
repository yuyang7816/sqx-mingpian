<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<a href="#" class="nf-page-hero__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-page-hero__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户详情</h2>
			<p class="nf-page-hero__desc">查看用户基本信息、钱包与会员状态，支持多 Tab 查看提现、访客、名片、申请等关联记录</p>
		</div>
		<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户详情" name="first">
				<div class="nf-table-card nf-detail-card">
					<div class="nf-filter-card__head">基本信息</div>
				<div class="detailtabel nf-detail-table">
					<div class="table_main">
						<table>
							<tbody>

								<tr>
									<!-- <th>邀请人邀请码</th>
									<td>
										<span style="color: #2f3968;cursor: pointer;"
											@click="updates2(tableData.inviterCode)">
											{{ tableData.inviterCode }}
										</span>

										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChangeU(tableData)">修改</el-button>
									</td> -->
									<th>上级邀请码</th>
									<td>
										<span style="color: #2f3968;cursor: pointer;"> <!-- @click="updates2(tableData.staffInviterCode)" -->
											{{ tableData.staffInviterCode }}
										</span>
									
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>钱包</th>
									<td style="color: rgb(245, 108, 108)">
										{{ tablemoney.money ? `${tablemoney.money}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #2f3968;background: #fff;border: none;"
											@click="rechargenone(3)">修改</el-button>
									</td>
									<th>总收入</th>
									<td class="border-rt">{{tableData.balance?tableData.balance:'0元'}}</td>
								</tr>

								<tr>
									<!-- <th>金币</th>
									<td>
										{{ tablemoney.gold ? `${tablemoney.gold}` : '0' }}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #2f3968;background: #fff;border: none;"
											@click="rechargenone(4)">修改</el-button>
									</td> -->
									<!--< th>剩余查看次数</th>
									<td>
										{{tableData.viewNum?tableData.viewNum:0}}
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #2f3968;background: #fff;border: none;"
											@click="rechargenone(2)">修改</el-button>
									</td> -->
									<th>小程序openId</th>
									<td>{{ tableData.openId ? tableData.openId : '未绑定' }}</td>
									<th>收款二维码</th>
									<td>
										<div
											v-if="tableData.wxImg == null || tableData.wxImg == ''||tableData.wxImg == 'null'">
											暂无收款二维码
										</div>
										<div v-else style="display:flex;flex-wrap: wrap;">
											<div style="margin: 2px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="tableData.wxImg" alt=""
														slot="reference">
													<img style="width: 300px; height: 300px" :src="tableData.wxImg"
														alt="">
												</el-popover>
											</div>
										</div>
									</td>
									<th>图像</th>
									<td class="border-rt">
										<img v-if="tableData.avatar==null||tableData.avatar=='null'"
											src="~@/assets/img/avatar.png" alt="" width="80" height="80">
										<img v-else :src="tableData.avatar" width="80" height="80" />
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td>
										{{ tableData.userName }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>手机号</th>
									<td>
										{{ tableData.phone ? tableData.phone : '未绑定' }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>支付宝姓名</th>
									<td class="border-rt">{{ tableData.zhiFuBaoName ? tableData.zhiFuBaoName : '未绑定' }}
									</td>
								</tr>
								<tr>
									<th>邀请码</th>
									<td>
										{{ tableData.staffInvitationCode }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>支付宝账号</th>
									<td>{{ tableData.zhiFuBao ? tableData.zhiFuBao : '未绑定' }}</td>
									<th>用户状态</th>
									<td class=" border-rt">
										<span v-if="tableData.status == 1 ">正常</span>
										<span v-if="tableData.status == 2 " style="color: #f56c6c;">禁用</span>
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											style="color: #2f3968;background: #fff;border: none;"
											@click="stateChange(tableData.userId)">更改状态</el-button>
									</td>
								</tr>
								
								<!-- <tr>
									<th>代理发布需求奖励积分系数</th>
									<td>
										{{ tableData.addRate ? tableData.addRate : 0 }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(2,tableData.addRate)">修改</el-button>
									</td>
									<th>代理每日发布需求奖励积分上限次数</th>
									<td>
										{{ tableData.addNum ? tableData.addNum : 0 }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(3,tableData.addNum)">修改</el-button>
									</td>
									<th>代理需求被会员首次查看系数</th>
									<td class="border-rt">
										{{ tableData.vipViewRate ? tableData.vipViewRate : 0 }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(4,tableData.vipViewRate)">修改</el-button>
									</td>
								</tr> -->
								<!-- <tr>
									<th>普通用户每日发布需求奖励上限</th>
									<td>
										{{ tableData.userAddRate ? tableData.userAddRate : 0 }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(5,tableData.userAddRate)">修改</el-button>
									</td>
									<th>普通用户发布奖励系数</th>
									<td>
										{{ tableData.userAddNum ? tableData.userAddNum : 0 }}
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(6,tableData.userAddNum)">修改</el-button>
									</td>
									<th>公众号OpenId</th>
									<td class="border-rt">
										{{tableData.wxOpenId}}
									</td>
								</tr> -->
								<tr>
									<th>创建时间</th>
									<td>
										{{ tableData.createTime }}
									</td>
									<th>性别</th>
									<td>
										<span v-if="tableData.sex==0">未设置</span>
										<span v-if="tableData.sex==1">男</span>
										<span v-if="tableData.sex==2">女</span>
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="sexChange(tableData.userId,tableData.sex)">修改</el-button>
									</td>
									<th>年龄</th>
									<td class="border-rt">
										<span v-if="tableData.age">{{tableData.age}}</span>
										<span v-else>未设置</span>
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="ageChange(1,tableData.age)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>自动回复</th>
									<td>
										<span>{{tableData.msg?tableData.msg:'暂无'}}</span>
										<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
											:disabled="!isAuth('userList:update')"
											@click="msgChange(tableData.userId,tableData.msg)">修改</el-button>
									</td>
									<th>是否是会员</th>
									<td>
										<span v-if="tableData.isVip==1">是</span>
										<span v-else>否</span>
										<el-button size="mini" type="warning" @click="quxiaoVip(tableData)"
											:disabled="!isAuth('userList:updateVip')" v-if="tableData.isVip==1"
											plain>取消会员</el-button>
									</td>
									<th>会员到期时间</th>
									<td class="border-rt">
										{{tableData.vipEndTime}}
										<el-button size="mini" type="warning" @click="zengsongVip(tableData)"
											:disabled="!isAuth('userList:updateVip')">修改会员</el-button>
									</td>
								</tr>
								<tr>
									<th class="border-bt">会员申请名片次数</th>
									<td class="border-bt">
										<div  v-if="tableData.isVip==1">{{tableData.vipCardNum}}
											
												<el-button size="mini" style="color: #2f3968;background: #fff;border: none;"
													:disabled="!isAuth('userList:update')"
													@click="ageChange(7,tableData.vipCardNum)">修改</el-button>
										</div>
										<span v-else>非会员</span>
									</td>
									<th class="border-bt"></th>
									<td class="border-bt">
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="提现记录" name="sixth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="withdrawData.list">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;"
								@click="updates(scope.row.userId)">{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="classify" label="提现方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.classify == 1">支付宝</span>
							<span v-if="scope.row.classify == 2">微信小程序</span>
							<span v-if="scope.row.classify == 3">微信公众号</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="提现金额" width="100">
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100" fixed="right">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" v-if="scope.row.state == 0">待转账</span>
							<span v-if="scope.row.state == 1">已转账</span>
							<span v-if="scope.row.state == -1">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop="refund" label="退款原因" width="220">
					</el-table-column>
					<el-table-column prop="createAt" label="申请时间" width="170">
					</el-table-column>
					<el-table-column prop="outAt" label="转账/退款时间" width="180">
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeTx" @current-change="handleCurrentChangeTx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="withdrawData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="钱包明细" name="eighth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeQbmx" @current-change="handleCurrentChangeQbmx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="我的访客" name="shoucang">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="fangkeData.records">
					<el-table-column label="编号" prop="userId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="用户昵称" prop="agentName">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">{{
									scope.row.userName ? scope.row.userName : '-'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="用户头像" prop="avatar" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="用户来源">
					</el-table-column>
					<el-table-column prop="browseTime" label="访问时间">
					</el-table-column>

				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeCp" @current-change="handleCurrentChangeCp"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fangkeData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="我的邀请" name="dongtai">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tuanduiData.records">
					<el-table-column label="编号" prop="id" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="用户昵称" prop="agentName">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">{{
									scope.row.userName ? scope.row.userName : '-'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="用户头像" prop="avatar" width="200">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="180"></el-table-column>
					<el-table-column prop="inviteTime" label="邀请时间" width="180"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeDt" @current-change="handleCurrentChangeDt"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tuanduiData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的查看" name="fabu">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="takeData.records">
					<el-table-column label="编号" prop="cardId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="名片类型" prop="cardType" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.cardType == 1">个人认证</span>
							<span v-if="scope.row.cardType == 2">企业认证</span>
						</template>
					</el-table-column>
					<el-table-column label="用户昵称" prop="userName">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipName" label="会员等级">
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
					<el-table-column label="联系电话" prop="cardPhone">
					</el-table-column>
					<el-table-column label="微信二维码" prop="wxCodeImg" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.wxCodeImg == null || scope.row.wxCodeImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.wxCodeImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.wxCodeImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.wxCodeImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="真实姓名" prop="realName">
					</el-table-column>
					<el-table-column label="微信号" prop="wxCode" width="160">
					</el-table-column>
					<el-table-column label="一级分类" prop="industryFirstName">
					</el-table-column>
					<el-table-column label="二级分类" prop="industrySecondName">
					</el-table-column>
					<el-table-column label="公司名称" prop="companyName" width="200">
					</el-table-column>
					<el-table-column label="营业执照" prop="businessLicenseImg" width="100">
						<template slot-scope="scope">
							<div
								v-if="scope.row.businessLicenseImg == null || scope.row.businessLicenseImg == ''||scope.row.businessLicenseImg == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.businessLicenseImg">
								<el-popover v-for="(item,index) in scope.row.businessLicenseImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 400px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="所属岗位" prop="companyPosition">
					</el-table-column>
					<el-table-column label="面积(单位:平方米)" prop="companyArea">
					</el-table-column>
					<el-table-column label="公司环境" prop="companyAmbient" width="300">
						<template slot-scope="scope">
							<div
								v-if="scope.row.companyAmbient == null || scope.row.companyAmbient == ''||scope.row.companyAmbient == 'null' ">
								暂无图片
							</div>
							<div v-else-if="scope.row.companyAmbient">
								<el-popover v-for="(item,index) in scope.row.companyAmbient.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="注册资本(单位:万元)" prop="companyOutputValue">
					</el-table-column>
					<el-table-column label="公司官网" prop="companyWebsite" width="200">
					</el-table-column>
					<el-table-column label="资质认证" prop="companyFlair" width="300">
						<template slot-scope="scope">
							<div
								v-if="scope.row.companyFlair == null || scope.row.companyFlair == ''||scope.row.companyFlair == 'null' ">
								暂无图片
							</div>
							<div v-else-if="scope.row.companyFlair">
								<el-popover v-for="(item,index) in scope.row.companyFlair.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="主营产品" prop="firstProduct" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.firstProduct}}</div>
								<div style="width: 500px;max-height: 600px;overflow-y: auto;"
									v-html="scope.row.firstProduct"></div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="地址" prop="companyProvince" width="200">
						<template slot-scope="scope">
							<span>{{ scope.row.companyProvince + '' + scope.row.companyCity + '' + scope.row.companyDistrict + '' + scope.row.companyAddress }}</span>
						</template>
					</el-table-column>
					<el-table-column label="公司LGOGO" prop="companyLogo" width="100">
						<template slot-scope="scope">
							<div
								v-if="scope.row.companyLogo == null || scope.row.companyLogo == ''|| scope.row.companyLogo == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.companyLogo" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.companyLogo" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.companyLogo" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="资质认证图片" prop="companyFlairImg" width="230">
						<template slot-scope="scope">
							<div
								v-if="scope.row.companyFlairImg == null || scope.row.companyFlairImg == ''||scope.row.companyFlairImg == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.companyFlairImg">
								<el-popover v-for="(item,index) in scope.row.companyFlairImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="主营产品图" prop="firstProductImg" width="300">
						<template slot-scope="scope">
							<div
								v-if="scope.row.firstProductImg == null || scope.row.firstProductImg == ''||scope.row.firstProductImg == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.firstProductImg">
								<el-popover v-for="(item,index) in scope.row.firstProductImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="验厂报告图" prop="factoryReport" width="300">
						<template slot-scope="scope">
							<div
								v-if="scope.row.factoryReport == null || scope.row.factoryReport == ''||scope.row.factoryReport == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.factoryReport">
								<el-popover v-for="(item,index) in scope.row.factoryReport.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="公司简介" prop="companyBlurb" width="300">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">{{scope.row.companyBlurb}}</div>
								<div style="width: 500px;max-height: 600px;overflow-y: auto;"
									v-html="scope.row.companyBlurb"></div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="公司人数" prop="peopleNum">
					</el-table-column>
					<el-table-column label="样式code" prop="cardStyleCode">
					</el-table-column>
					<el-table-column label="名片照片" prop="cardImg">
						<template slot-scope="scope">
							<div
								v-if="scope.row.cardImg == null || scope.row.cardImg == ''||scope.row.cardImg == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.cardImg">
								<el-popover v-for="(item,index) in scope.row.cardImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: auto">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="分享次数" prop="shareCount">
					</el-table-column>
					<el-table-column label="人气值" prop="popularityValue">
					</el-table-column>
					<el-table-column label="法人" prop="legalPerson">
					</el-table-column>
					<el-table-column label="注册资金" prop="registeredCapital">
					</el-table-column>
					<el-table-column label="注册时间" prop="registeredTime">
					</el-table-column>
					<el-table-column label="浏览次数" prop="browseCount">
					</el-table-column>
					<el-table-column label="搜索关键词" prop="keyWord">
					</el-table-column>
					<el-table-column label="审核备注" prop="examineRemark" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.examineRemark?scope.row.examineRemark:'暂无'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="创建时间" prop="createTime" width="160">
					</el-table-column>
					<el-table-column fixed='right' label="名片状态" prop="cardStatus" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.cardStatus == 1">展示</span>
							<span v-else>不展示</span>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="审核状态" prop="auditStatus" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.auditStatus == 0">待审核</span>
							<span v-if="scope.row.auditStatus == 1">审核成功</span>
							<span v-if="scope.row.auditStatus == 2">审核未通过</span>
							<span v-if="scope.row.auditStatus == 3">会员到期已下架</span>
						</template>
					</el-table-column>
					<!-- <el-table-column label="操作" width="160" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" plain :disabled="!isAuth('materialsList:update')"
								style="margin: 5px;" @click="pinglunBtn(scope.row)">评论列表
							</el-button>
							<el-button size="mini" type="danger" plain :disabled="!isAuth('materialsList:update')"
								style="margin: 5px;" @click="dianzanBtn(scope.row)">点赞列表
							</el-button>
						</template>
					</el-table-column> -->
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeFb" @current-change="handleCurrentChangeFb"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="takeData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="我的消息" name="xiaoxi">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tousuData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeWdxx" @current-change="handleCurrentChangeWdxx"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tousuData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的团队分销" name="tuandui">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="yaoqingData.records">
					<el-table-column fixed prop="inviteeUserId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
						<template slot-scope="scope">
							<div style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.inviteeUserId)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<div style="margin-top: 5px;" v-if="scope.row.avatar">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
							<div v-else><img style="width: 50px; height: 50px" src="~@/assets/img/avatar.png" alt=""
									slot="reference"></div>
						</template>
					</el-table-column>
					<el-table-column prop="amount" label="邀请收益">
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeTd" @current-change="handleCurrentChangeTd"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="yaoqingData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="我的名片" name="caigoumingpian">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="kaitongData.list">
					<el-table-column label="编号" prop="cardId" width="80" fixed="left">
					</el-table-column>
					<el-table-column label="名片类型" prop="cardType" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.cardType == 1">个人</span>
							<span v-if="scope.row.cardType == 2">企业</span>
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
					<el-table-column label="二级级行业" prop="industrySecondName" width="100">
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
					<el-table-column label="审核说明" prop="examineRemark" width="160">
					</el-table-column>
					<el-table-column fixed='right' label="审核状态" prop="auditStatus" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.auditStatus == 0">待审核</span>
							<span v-if="scope.row.auditStatus == 1">审核成功</span>
							<span v-if="scope.row.auditStatus == 2">审核未通过</span>
							<span v-if="scope.row.auditStatus == 3">会员到期已下架</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeMp" @current-change="handleCurrentChangeMp"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="kaitongData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="名片分组" name="jihuo">
				<div class="nf-table-card">
				<div class="nf-table-card__toolbar">
					<el-button size="mini" type="primary" plain @click="jihuoSelect()">刷新 </el-button>
					<el-button size="mini" type="primary" @click="addMingpian()">添加 </el-button>
				</div>
				
				<el-table v-loading="tableDataLoading" :data="fenleiData">
					<el-table-column fixed prop="id" label="ID">
					</el-table-column>
					<el-table-column prop="groupsName" label="分组名称">
					</el-table-column>
					<el-table-column prop="remark" label="备注">
					</el-table-column>
					<el-table-column prop="sortId" label="排序">
					</el-table-column>
					<el-table-column prop="isDefault" label="是否默认">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.isDefault==1">是</span>
							<span style="color: green;" v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
					<el-table-column fixed='right' label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="addMingpian(scope.row)">修改 </el-button>
							<el-button size="mini" type="danger"  @click="deleteswzMpfl(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="分享次数明细" name="fenxiangcishu">
			</el-tab-pane> -->
			<el-tab-pane label="申请记录" name="shenqing">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="fenleiData.records">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="被申请人" width="100" fixed="left">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;"
								@click="updataDetails(scope.row.userId)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column label="姓名" prop="businessCard.realName" width="100">
					</el-table-column>
					<el-table-column label="微信号" prop="businessCard.wxCode" width="160">
					</el-table-column>
					
					<el-table-column prop="businessCard.companyName" label="公司" width="160">
					</el-table-column>
					<el-table-column label="一级行业" prop="businessCard.industryFirstName" width="200">
					</el-table-column>
					<el-table-column label="二级行业" prop="businessCard.industrySecondName" width="100">
					</el-table-column>
					<el-table-column label="城市" prop="businessCard.companyCity">
					</el-table-column>
					<el-table-column label="联系电话" prop="businessCard.cardPhone" width="160">
					</el-table-column>
					<el-table-column label="邮箱" prop="businessCard.email" width="160">
					</el-table-column>
					<el-table-column label="地址" prop="businessCard.address" width="200">
					</el-table-column>
					<el-table-column label="名片正面" prop="cardImg" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.businessCard == null || scope.row.businessCard.cardImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.businessCard.cardImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.businessCard.cardImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.businessCard.cardImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="status" label="状态" fixed="right">
						<template slot-scope="scope">
							<div v-if="scope.row.status==1">待审核</div>
							<div v-if="scope.row.status==2">已通过</div>
							<div v-if="scope.row.status==3">已拒绝</div>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeSq" @current-change="handleCurrentChangeSq"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fenleiData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="被申请记录" name="beishenqing">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="fenleiData.records">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="byUserName" label="申请人" width="100" fixed="left">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;"
								@click="updataDetails(scope.row.byUserId)">
								{{ scope.row.byUserName ? scope.row.byUserName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column label="姓名" prop="businessCard.realName" width="100">
					</el-table-column>
					<el-table-column label="微信号" prop="businessCard.wxCode" width="160">
					</el-table-column>
					
					<el-table-column prop="businessCard.companyName" label="公司" width="160">
					</el-table-column>
					<el-table-column label="一级行业" prop="businessCard.industryFirstName" width="200">
					</el-table-column>
					<el-table-column label="二级行业" prop="businessCard.industrySecondName" width="100">
					</el-table-column>
					<el-table-column label="城市" prop="businessCard.companyCity">
					</el-table-column>
					<el-table-column label="联系电话" prop="businessCard.cardPhone" width="160">
					</el-table-column>
					<el-table-column label="邮箱" prop="businessCard.email" width="160">
					</el-table-column>
					<el-table-column label="地址" prop="businessCard.address" width="200">
					</el-table-column>
					<el-table-column label="名片正面" prop="cardImg" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.businessCard == null || scope.row.businessCard.cardImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.businessCard.cardImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.businessCard.cardImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.businessCard.cardImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="status" label="状态" fixed="right">
						<template slot-scope="scope">
							<div v-if="scope.row.status==1">待审核</div>
							<div v-if="scope.row.status==2">已通过</div>
							<div v-if="scope.row.status==3">已拒绝</div>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-table-card__foot">
					<el-pagination @size-change="handleSizeChangeSq" @current-change="handleCurrentChangeSq"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fenleiData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="名片分享记录" name="fenxiangjilu">
			</el-tab-pane> -->
			
			<!-- 不可提现添加金额 -->
			<el-dialog :title="types==3?'修改钱包':'修改点数'" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;" v-if="types==1||types==3||types==4">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="types==1">
					<span style="width: 200px;display: inline-block;text-align: right;">点数：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入点数"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="types==2">
					<span style="width: 200px;display: inline-block;text-align: right;">查看次数：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入点数"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="types==3">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="types==4">
					<span style="width: 200px;display: inline-block;text-align: right;">金币：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金币"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="types==3||types==4">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="title" type="text" :controls="false"
						placeholder="请输入标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="types==3||types==4">
					<span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
					<el-input style="width:50%;" v-model="content" type="text" :controls="false"
						placeholder="请输入内容"></el-input>
				</div>
				
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;" v-if="indexType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="请输入年龄"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="indexType==2||indexType==3||indexType==4||indexType==5||indexType==6">
					<span style="width: 200px;display: inline-block;text-align: right;">数值：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="输入数值"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="indexType==7">
					<span style="width: 200px;display: inline-block;text-align: right;">会员申请名片次数：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="请输入会员申请名片次数"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改自动回复" :visible.sync="dialogFormVisible5" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">自动回复：</span>
					<el-input style="width:50%;" v-model="msg" type="text" placeholder="请输入自动回复"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible5 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo3()">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 聊天内容 -->
			<el-dialog title="聊天内容" :visible.sync="dialogFormVisible4" center width="80%">
				<el-table v-loading="tableDataLoading1" :data="pinglunDatas.records">
					<el-table-column prop="chatContentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
						<template slot-scope="scope">
							<div style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div>

						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''||scope.row.avatar == 'null'">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="信息内容" width="280">
						<template slot-scope="scope">
							<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
							<div v-else-if="scope.row.messageType==2">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
								</el-popover>
							</div>
							<div v-else-if="scope.row.messageType==3">
								<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
							</div>
							<div v-else-if="scope.row.messageType==4">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px"
										:src="'https://mpfxadmin.xianmaxiong.com/emoji/'+scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px"
										:src="'https://mpfxadmin.xianmaxiong.com/emoji/'+scope.row.content" alt="">
								</el-popover>
							</div>
							<!-- 微信号 -->
							<div v-else-if="scope.row.messageType==5">
								<div v-if="scope.row.messageStatus==1">
									<div>微信交换请求状态：待同意</div>
								</div>
								<div v-if="scope.row.messageStatus==2">
									<div>己方微信号：{{scope.row.content}}</div>
									<div>对方微信号：{{scope.row.byContent}}</div>
								</div>
								<div v-else>
									微信交换请求：已拒绝
								</div>

							</div>
							<!-- 手机号 -->
							<div v-else-if="scope.row.messageType==6">
								<div v-if="scope.row.messageStatus==1">
									<div>手机号交换请求状态：待同意</div>
								</div>
								<div v-if="scope.row.messageStatus==2">

									<div>己方手机号：{{scope.row.content}}</div>
									<div>对方手机号：{{scope.row.byContent}}</div>
								</div>
								<div v-else>
									手机号交换请求：已拒绝
								</div>
							</div>
							<!-- 查看名片 -->
							<div v-else-if="scope.row.messageType==7">
								<!-- <div v-if="scope.row.messageStatus==1">
									<div>名片交换请求状态：待同意</div>
								</div>  v-if="scope.row.messageStatus==2"-->
								<div>
									<div>名片：
										<div v-if="scope.row.businessCard"
											style="border: 1px solid #efefef;border-radius: 5px;">
											<div><img :src="scope.row.businessCard.companyLogo"
													style="float: left;width: 60px;height: 60px;margin: 5px;border-radius: 5px;" />
											</div>
											<div style="width: 80%;">
												<div
													style="font-size: 18px;font-weight: 600;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 1;overflow: hidden;max-height:28px;padding: 5px;">
													{{scope.row.businessCard.companyName}}
												</div>
												<div style="width: 93%;margin-left: 70px;margin-top: 10px;">

													<div
														style="display: inline-block;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 1;overflow: hidden;max-height: 65px;width: 98%;">
														<span
															style="display: inline-block;margin: 0 5px;color: #2f3968;">{{scope.row.businessCard.companyPosition}}</span>主营：{{scope.row.businessCard.industrySecondName}}
													</div>
												</div>
											</div>
											<div style="    padding: 5px;">
												<el-button style='width: 100%;' size="mini" type="info" icon="document"
													@click="selectMp(scope.row.businessCard.companyName)">查看详情</el-button>
											</div>
										</div>
									</div>
								</div>
								<!-- <div v-else>
									名片交换请求：已拒绝
								</div> -->
							</div>

							<div v-else>{{scope.row.content}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeLtnr" @current-change="handleCurrentChangeLtnr"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="pinglunDatas.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加/修改常用语 -->
			<el-dialog :title="title2" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">常用语：</span>
					<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入常用语"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="addGuanjianci()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改信息 -->
			<el-dialog title="修改信息" :visible.sync="dialogFormVisibleU" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">微信名称：</span>
					<el-input style="width:50%;" v-model="userNameU" type="text" placeholder="请输入微信名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="phoneU" type="text" placeholder="请输入手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
					<el-input style="width:50%;" v-model="inviterCodeU" type="text" placeholder="请输入邀请人邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
					<el-input style="width:50%;" v-model="invitationCodeU" type="text" placeholder="请输入邀请码"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">合作商邀请码：</span>
					<el-input style="width:50%;" v-model="staffInvitationCodeU" type="text"
						placeholder="请输入合作商邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">合作商上级邀请码：</span>
					<el-input style="width:50%;" v-model="staffInviterCodeU" type="text"
						placeholder="请输入合作商上级邀请码"></el-input>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">图像：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="avatarU"
							action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="avatarU" :src="avatarU" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleU = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToU()">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 评论列表 -->
			<el-dialog title="评论列表" :visible.sync="dialogFormVisiblePl" center width="80%">
				<el-table v-loading="tableDataLoadingEr" :data="tableDataPl.records">
					<el-table-column prop="commentId" label="编号" fixed="left" width="80">
					</el-table-column>
					<el-table-column label="用户昵称" prop="realName" width="100">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">{{
          				scope.row.userName ? scope.row.userName : '-'
          			}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''||scope.row.avatar == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="commentContent" label="评论内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.commentContent}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
								@click="deleteswzPl(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangePl" @current-change="handleCurrentChangePl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataPl.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 点赞列表 -->
			<el-dialog title="点赞列表" :visible.sync="dialogFormVisibleDz" center>
				<el-table v-loading="tableDataLoadingEr" :data="tableDataDz.records">
					<el-table-column prop="userId" label="编号" width="80">
					</el-table-column>
					<el-table-column label="用户昵称" prop="realName">
						<template slot-scope="scope">
							<span style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">{{
          				scope.row.userName ? scope.row.userName : '-'
          			}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="头像">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''||scope.row.avatar == 'null'">
								暂无图片
							</div>
							<div v-else-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.avatar" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.avatar" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeDz" @current-change="handleCurrentChangeDz"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="tableDataDz.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 修改关键词 -->
			<el-dialog title="修改关键词" :visible.sync="dialogFormVisibleGjc" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">关键词：</span>
					<!-- <el-input style="width:50%;" v-model="contentL" type="text" placeholder="请输入关键词"></el-input> -->
					<span class="bqList" v-for="(item,index) in contentL" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="dels1(index)"></i>
						</span>
					</span>
					<!-- 供求宝域名 绑定是否为会员 可手动输入 -->
					<!--  <el-input v-model="bq" style="width:200px;" placeholder="请输入关键词" v-if="tableData.isVip==1"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj"
						v-if="tableData.isVip==1">
					</el-button> -->
					<el-input v-model="bq" style="width:200px;" placeholder="请输入关键词"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">行业一级名称：</span>
					<el-select v-model="industryFirstNameT" placeholder="请选择行业一级名称" style="width: 250px;" filterable
						@change="industryFirstBtn(1)">
						<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.industryName"
							:value="item.industryName">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">行业二级名称：</span>
					<el-select v-model="industrySecondNameT" placeholder="请选择行业二级名称" style="width: 250px;" filterable
						:disabled="industryFirstNameT==''" @change="selectW()">
						<el-option v-for="(item,index) in tableDataFer" :key="item.index" :label="item.industryName"
							:value="item.industryName">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择的行业二级名称：</span>
					<span class="bqList" v-for="(item,index) in privilege" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="dels2(index)"></i>
						</span>
					</span>
					<el-button type="primary" plain @click="chongzhi()" size="mini">重置</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="isOpen">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleGjc = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToGjc()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改会员 -->
			<el-dialog title="修改会员" :visible.sync="dialogFormVisibleZs" center width="60%">
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
					<el-table v-loading="tableDataLoading" ref="singleTable" :data="list.list" highlight-current-row
						@current-change="handleCurrentBtn" height="400px">
						<el-table-column label="编号" prop="id" width="80">
						</el-table-column>
						<el-table-column label="权限" prop="power">
							<template slot-scope="scope">
								<span v-if="scope.row.power == 1">只能发布名片</span>
								<span v-if="scope.row.power == 2">只能发布行业资讯</span>
								<span v-if="scope.row.power == 3">两者都能发布</span>
							</template>
						</el-table-column>
						<el-table-column label="名称" prop="vipName">
						</el-table-column>
						<el-table-column label="月数" prop="vipMonthCount">
						</el-table-column>
						<el-table-column label="查看次数" prop="viewNum">
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
			<!-- 添加 修改 名片分类 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleMpfl" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分组名称：</span>
					<el-input style="width:50%;" v-model="groupsName" type="text" placeholder="请输入分组名称"></el-input>
					
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
					<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入备注"></el-input>
					
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input style="width:50%;" v-model="sortId" type="number" placeholder="请输入排序"></el-input>
					
				</div>
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="isDefault">
						<el-radio :label="1">默认</el-radio>
						<el-radio :label="2">不默认</el-radio>
					</el-radio-group>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleMpfl = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToMpfl()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 0,
				state: 0,
				limit: 10,
				page: 1,
				limit1: 10,
				page1: 1,
				size: 10,
				userId: '',
				money: '',
				title: '',
				content: '',
				phone: '',
				startTime: '',
				endTime: '',
				invitationCode: '',
				feiDatamin: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablejifen: '',
				autonymData: {},
				tablemoney: {},
				tablenum: {},
				tableData: {},
				tableDataMp: {},
				userData: {},
				userDataLl: {},
				userDataCp: {},
				userDataJp: {},
				fangkeData: {},
				withdrawData: {},
				walletData: {},
				walletDataHy: {},
				rechargeData: {},
				takeData: {},
				jiedanData: {},
				dingdanData: {},
				fensiData: {},
				guanzhuData: {},
				tableDataZz: {},
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				tableDataLoading: true,
				tableDataLoading1: true,
				relation_id: '',
				relationId: '',
				tkstatus: '',
				invitation: '',
				nowMonthOver: '',
				lastMonthOver: '',
				nowMonthPaid: '',
				lastMonthPaid: '',
				nowMonthOrderNumber: '',
				nowDayOrderNumber: '',
				hasCash: '',
				search: 0,
				classifyIds: 0,
				statusId: -1,
				myPhone: '',
				type: '',
				age: '',
				msg: '',
				chatConversationId: '',
				contents: '',
				guanjianciId: '',
				title2: '添加关键词',
				liaotianData: {},
				huihuaData: {},
				guanjianciData: {},
				tuanduiData: {},
				userId: '',
				tousuData: {},
				dongtaiData: {},
				dianzanData: {},
				pinglunData: {},
				pinglunDatas: {},
				dashangData: {},
				types: 1,
				typesList: [{
						type: 1,
						name: '一级'
					},
					{
						type: 2,
						name: '二级'
					}
				],
				states: 3,
				statesnum: [{
					value: 0,
					label: '投保'
				}, {
					value: 1,
					label: '竞拍中'
				}, {
					value: 2,
					label: '已结束'
				}],
				typeLl: 1,
				// 修改信息
				dialogFormVisibleU: false,
				inviterCodeU: '',
				avatarU: '',
				userNameU: '',
				phoneU: '',
				invitationCodeU: '',
				indexType: '',
				staffInvitationCodeU: '',
				staffInviterCodeU: '',

				page1: 1,
				limit1: 10,
				dialogFormVisiblePl: false,
				tableDataPl: {},
				dialogFormVisibleDz: false,
				tableDataDz: {},
				dialogFormVisibleS: false,
				tableDataLoadingEr: false,
				flag: 1,
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
				MoneyData: {},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				yaoqingData: {},
				kaitongData: {},
				startTime: '',
				endTime: '',
				vipId: '',
				dialogFormVisibleGjc: false,
				industryFirstNameT: '',
				industrySecondNameT: '',
				classifyIds: [],
				tableDataFer: [],
				industryFirstId: '',
				industryFirstNameY: [],
				privilege: [],
				bq: '',
				contentL: [],
				isOpen: '',
				sendMessageId: '',

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
				proportion: '',
				
				dialogFormVisibleMpfl:false,
				titles:'',
				id: "",
				groupsName: "", //分组名称
				remark: "", //备注
				sortId:'',
				isDefault: 1, //是否默认 1=默认 2=不默认
				fenleiData:[],
				
				

			}
		},
		methods: {
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
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail1',
					query: {
						userId: userId
					}
				})
			},
			// 详情跳转
			updates1(userId) {
				this.$router.push({
					path: '/userDetail1',
					query: {
						userId: userId
					}
				})
			},
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
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
					// this.autonymSelect()
				}

				if (tab._props.label == '提现记录') {
					this.limit = 10
						this.page = 1
						this.withdrawSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.limit = 10
					this.page = 1
					this.walletSelect()
				}
				if (tab._props.label == '充值明细') {
					this.limit = 10
					this.page = 1
					this.rechargeSelect()
				}
				
				
				if (tab._props.label == '我的查看') {
					this.limit = 10
					this.page = 1
					this.takeSelect()
				}
				
				if (tab._props.label == '我的访客') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.fangkeSelect()
				}
				
				if (tab._props.label == '我的点赞') {
					this.limit = 10
					this.page = 1
					this.search = 0
					this.jingpaiSelect()
				}

				if (tab._props.label == '我的消息') {
					this.limit = 10
					this.page = 1
					this.states = 5
					this.tousuSelect()
				}

				
				if (tab._props.label == '我的邀请') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.tuanduiSelect1()
				}


				if (tab._props.label == '合作商统计') {
					this.incomeSelect()
				}
				if (tab._props.label == '我的团队分销') {
					this.limit = 10
					this.page = 1
					this.yaoqingSelect()
				}
				
				if (tab._props.label == '我的名片') {
					this.limit = 10
					this.page = 1
					this.mingpianSelect()
				}
				if (tab._props.label == '名片分组') {
					this.limit = 10
					this.page = 1
					this.jihuoSelect()
				}
				// if (tab._props.label == '分享次数明细') {
				// 	this.limit = 10
				// 	this.page = 1
				// 	this.fenxiangmingxiSelect()
				// }
				if (tab._props.label == '申请记录') {
					this.limit = 10
					this.page = 1
					this.states = 1
					this.shenqingSelect()
				}
				if (tab._props.label == '被申请记录') {
					this.limit = 10
					this.page = 1
					this.states = 2
					this.shenqingSelect()
				}
				if (tab._props.label == '名片分享记录') {
					this.limit = 10
					this.page = 1
					this.fenxiangSelect()
				}
			

			},
			
			handleSizeChangeTx(val) {
				this.limit = val
				this.withdrawSelect()
			},
			handleCurrentChangeTx(val) {
				this.page = val
				this.withdrawSelect()
			},
			handleSizeChangeQbmx(val) {
				this.limit = val
				this.walletSelect()
			},
			handleCurrentChangeQbmx(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChangeJbmx(val) {
				this.limit = val
				this.walletSelectJb()
			},
			handleCurrentChangeJbmx(val) {
				this.page = val
				this.walletSelectJb()
			},
			handleSizeChangeCzmx(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChangeCzmx(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChangeDd(val) {
				this.limit = val
				this.dingdanSelect()
			},
			handleCurrentChangeDd(val) {
				this.page = val
				this.dingdanSelect()
			},
			handleSizeChangeLtnr(val) {
				this.limit1 = val
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChangeLtnr(val) {
				this.page1 = val
				this.neirongSelect(this.chatConversationId)
			},
			handleSizeChangeWdxx(val) {
				this.limit = val
				this.tousuSelect()
			},
			handleCurrentChangeWdxx(val) {
				this.page = val
				this.tousuSelect()
			},
			handleSizeChangeLl(val) {
				this.limit = val
				this.userSelect()
			},
			handleCurrentChangeLl(val) {
				this.page = val
				this.userSelect()
			},
			handleSizeChangeCp(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChangeCp(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChangeCpsc(val) {
				this.limit = val
				this.InformationSelectCp()
			},
			handleCurrentChangeCpsc(val) {
				this.page = val
				this.InformationSelectCp()
			},
			handleSizeChangeZz(val) {
				this.limit = val
				this.sendSelect()
			},
			handleCurrentChangeZz(val) {
				this.page = val
				this.sendSelect()
			},
			handleSizeChange6(val) {
				this.limit = val
				this.jiedanSelect()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.jiedanSelect()
			},

			handleSizeChange7(val) {
				this.limit = val
				this.feiSelect()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.feiSelect()
			},
			handleSizeChangeFs(val) {
				this.limit = val
				this.fensiSelect()
			},
			handleCurrentChangeFs(val) {
				this.page = val
				this.fensiSelect()
			},
			handleSizeChangeGz(val) {
				this.limit = val
				this.guanzhuSelect()
			},
			handleCurrentChangeGz(val) {
				this.page = val
				this.guanzhuSelect()
			},
			handleSizeChangeLtjl(val) {
				this.limit = val
				this.liaotianSelect()
			},
			handleCurrentChangeLtjl(val) {
				this.page = val
				this.liaotianSelect()
			},

			handleSizeChangeJp(val) {
				this.limit = val
				this.jingpaiSelect()
			},
			handleCurrentChangeJp(val) {
				this.page = val
				this.jingpaiSelect()
			},
		
			handleSizeChangeKtjl(val) {
				this.limit = val
				this.yhqSelect()
			},
			handleCurrentChangeKtjl(val) {
				this.page = val
				this.yhqSelect()
			},
			handleSizeChangeDt(val) {
				this.limit = val
				this.dongtaiSelect()
			},
			handleCurrentChangeDt(val) {
				this.page = val
				this.dongtaiSelect()
			},
			handleSizeChangeDz(val) {
				this.limit = val
				this.dianzanSelect()
			},
			handleCurrentChangeDz(val) {
				this.page = val
				this.dianzanSelect()
			},
			handleSizeChangeFb(val) {
				this.limit = val
				this.takeSelect()
			},
			handleCurrentChangeFb(val) {
				this.page = val
				this.takeSelect()
			},
			handleSizeChange19(val) {
				this.limit = val
				this.dashangSelect()
			},
			handleCurrentChange19(val) {
				this.page = val
				this.dashangSelect()
			},
			handleSizeChangeTd(val) {
				this.limit = val
				this.yaoqingSelect()
			},
			handleCurrentChangeTd(val) {
				this.page = val
				this.yaoqingSelect()
			},
			handleSizeChangePl(val) {
				this.limit1 = val;
				this.dataSelectPl()
			},
			handleCurrentChangePl(val) {
				this.page1 = val;
				this.dataSelectPl()
			},
			handleSizeChangeDz(val) {
				this.limit1 = val;
				this.dataSelectDz()
			},
			handleCurrentChangeDz(val) {
				this.page1 = val;
				this.dataSelectDz()
			},
			handleSizeChangeHy(val) {
				this.limit = val
				this.walletSelectHy()
			},
			handleCurrentChangeHy(val) {
				this.page = val
				this.walletSelectHy()
			},
			handleSizeChange8(val) {
				this.limit = val
				this.yaoqingSelect()
			},
			handleCurrentChange8(val) {
				this.page = val
				this.yaoqingSelect()
			},
			handleSizeChangeKt(val) {
				this.limit = val
				this.yaoqingSelect()
			},
			handleCurrentChangeKt(val) {
				this.page = val
				this.kaitongSelect()
			},
			handleSizeChangeMp(val) {
				this.limit = val
				this.mingpianSelect()
			},
			handleCurrentChangeMp(val) {
				this.page = val
				this.mingpianSelect()
			},
			handleSizeChangeJh(val) {
				this.limit = val
				this.jihuoSelect()
			},
			handleCurrentChangeJh(val) {
				this.page = val
				this.jihuoSelect()
			},
			handleSizeChangeTdhh(val) {
				this.limit = val
				this.tuanduihehuoSelect()
			},
			handleCurrentChangeTdhh(val) {
				this.page = val
				this.tuanduihehuoSelect()
			},
			handleSizeChangeSq(val) {
				this.limit = val
				this.shenqingSelect()
			},
			handleCurrentChangeSq(val) {
				this.page = val
				this.shenqingSelect()
			},
			timeDate2() {
				this.kaitongSelect()
			},
			// 可提现金额
			withdraw(id, val) {
				if (val == 0) {
					this.$message({
						message: '可提现金额为0元',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				} else {
					this.$confirm(`确定要推送提现消息吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`user/notification/${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							if (data.code == 0) {
								this.$message({
									message: '推送成功',
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
				}
			},
			// 修改点数、次数
			rechargenone(index) {
				this.types = index
				if (index == 2) {
					this.money = this.tableData.viewNum
				} else if (index == 4) {
					this.title = ''
					this.content = ''
					this.money = ''
				} else if (index == 3) {
					this.money = ''
					this.title = ''
					this.content = ''
				} else {
					this.money = ''
				}
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId

				if (this.types == 1) {
					if (this.type == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择修改类型',
							type: 'warning'
						})
						return
					}
					if (this.money == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入点数',
							type: 'warning'
						})
						return
					}
					this.$http({
						url: this.$http.adornUrl('user/updateUserCountMoney'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'count': this.money,
							'type': this.type,
						}, false, 'from')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.type = ''
									this.money = ''
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					})
				} else if (this.types == 4) {
					if (this.type == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择修改类型',
							type: 'warning'
						})
						return
					}
					if (this.money == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入金币',
							type: 'warning'
						})
						return
					}
					this.$http({
						url: this.$http.adornUrl('user/updateGold/'+userId+'/'+this.money+'/'+this.type),
						method: 'post',
						data: this.$http.adornData({
							'title': this.title,
							'content': this.content,
						}, false, 'from')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.type = ''
									this.money = ''
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					})
				} else if (this.types == 3) {
					if (this.type == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择修改类型',
							type: 'warning'
						})
						return
					}
					if (this.money == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入金额',
							type: 'warning'
						})
						return
					}
					this.$http({
						url: this.$http.adornUrl('user/addCannotMoney/' + userId + '/' + this.money + '/' + this
							.type),
						method: 'post',
						data: this.$http.adornData({
							'title': this.title,
							'content': this.content,
							// 'type': this.type,
						}, false, 'from')
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.type = ''
									this.money = ''
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}

					})
				} else {
					if (this.money == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入点数',
							type: 'warning'
						})
						return
					}
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'viewNum': this.money,
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.type = ''
									this.money = ''
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					})
				}
			},
			StairNoticeTo2() {
				let userId = this.$route.query.userId
				let datas = {}
				datas.userId = userId
				if (this.indexType == 2) {
					if (this.age === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.age) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\d+(?:\.\d{1,2})?$/;
					if (!regex.test(Number(this.age))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值最多可输入两位小数',
							type: 'warning'
						});
						return
					}
					datas.addRate = this.age
				} else if (this.indexType == 3) {
					if (this.age === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.age) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\+?[1-9][0-9]*$/;
					if (!regex.test(Number(this.age))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值应为正整数',
							type: 'warning'
						});
						return
					}
					datas.addNum = this.age
				} else if (this.indexType == 4) {
					if (this.age === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.age) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\d+(?:\.\d{1,2})?$/;
					if (!regex.test(Number(this.age))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值最多可输入两位小数',
							type: 'warning'
						});
						return
					}
					datas.vipViewRate = this.age
				} else if (this.indexType == 5) {
					if (this.age === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.age) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\+?[1-9][0-9]*$/;
					if (!regex.test(Number(this.age))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值应为正整数',
							type: 'warning'
						});
						return
					}
					datas.userAddRate = this.age
				} else if (this.indexType == 6) {
					if (this.age === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入数值',
							type: 'warning'
						});
						return
					}
					if (Number(this.age) < 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值不可小于0',
							type: 'warning'
						});
						return
					}
					var regex = /^\+?[1-9][0-9]*$/;
					if (!regex.test(Number(this.age))) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '数值应为正整数',
							type: 'warning'
						});
						return
					}
					datas.userAddNum = this.age
				}  else if (this.indexType == 7) {
					if (this.age === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入会员申请名片次数',
							type: 'warning'
						});
						return
					}
					datas.vipCardNum = this.age
				}else {
					datas.age = this.age
				}
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible2 = false
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
			// 更改状态
			stateChange(userId) {
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
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
			// 更改代理状态
			isAgentChange(userId, isAgent) {
				if (isAgent == 1) {
					var text = '确定要取消代理？'
					var isAgent = 0
				} else {
					var text = '确定要设置代理？'
					var isAgent = 1
				}
				this.$confirm(text, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'isAgent': isAgent
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
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
			// 更改合伙人状态
			isStaffChange(userId, isAgent) {
				if (isAgent == 1) {
					var text = '确定要取消合伙人？'
					var isAgent = 0
				} else {
					var text = '确定要设置合伙人？'
					var isAgent = 1
				}
				this.$confirm(text, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'isStaff': isAgent
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
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
			// 修改性别
			sexChange(userId, sex) {
				var sexs = 1;
				if (sex == 1) {
					sexs = 2
				}
				if (sex == 2) {
					sexs = 1
				}
				this.$confirm(`确定要更改用户性别吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'sex': sexs
						})
					}).then(({
						data
					}) => {
						console.log('data', data)
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
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
			ageChange(indexType, age) {
				this.indexType = indexType
				this.age = age
				this.dialogFormVisible2 = true
			},
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({
						// 'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data
						this.tablenum = returnData
						this.tablemoney = returnData
						this.tableData = returnData.userEntity
						this.phone = returnData.userEntity.phone
						this.invitationCode = returnData.userEntity.invitationCode
					}
				})
			},
			
			// 非直属
			feiSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectInvitationCodeByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'invitationCode': this.invitationCode
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.feiDatamin = returnData
				})
			},
			// 我的浏览数据
			userSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scBrowse/getMyBrowse'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.typeLl

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].orderTaking.homepageImg != '') {
							data.data.list[i].homepageImgs = data.data.list[i].orderTaking.homepageImg.split(',')
						}
					}
					let returnData = data.data
					this.userDataLl = returnData
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('/cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 获取金币明细
			walletSelectJb() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'classify':5
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 获取充值明细
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRechargeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {

						let returnData = data.data
						this.rechargeData = returnData
					}
					if (data.code == 500) {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
								this.tableDataLoading = false
							}
						})
					}
				})
			},
			// 我的铸造
			sendSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getMyCastingNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDataZz = returnData
				})
			},
			// 我的查看
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scBrowse/getMyBrowse'),
					method: 'get',
					params: this.$http.adornParams({
						// 'industryId': this.classifyId,
						// 'helpWordTitle': this.helpWordTitleT,
						'page': this.page,
						'limit': this.limit,
						'type': '',
						'userId': userId,

					})

					// url: this.$http.adornUrl('admin/nftCollection/getMyReleaseNftList'),
					// method: 'get',
					// params: this.$http.adornParams({
					// 	'page': this.page,
					// 	'limit': this.limit,
					// 	'userId': userId,
					// 	'status': ''
					// })
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					var arr = []
					for (var i in data.data.records) {
						
						if (data.data.records[i]) {
							arr.push(data.data.records[i])
							// data.data.records[i] = data.data.list[i].homepageImg.split(',')
						}
						

					}
					data.data.records = arr
					let returnData = data.data
					this.takeData = returnData
				})
			},
			// 我的接单
			jiedanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/selectMyTakeOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jiedanData = returnData
				})
			},
			// 我的买入/卖出 订单数据
			dingdanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getMyBusinessNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dingdanData = returnData
				})
			},
			// 实名认证
			autonymSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('student/selectStudentByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.autonymData = returnData
				})
			},
			// 我的收藏
			InformationSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/getFollowList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						// 'type': 2
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.records) {
					// 	if (data.data.records[i].img) {
					// 		data.data.records[i].imgs = data.data.records[i].img.split(',')
					// 	}
					// }
					let returnData = data.data
					this.userDataCp = returnData
				})
			},
			// 收藏的产品
			InformationSelectCp() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/getFollowList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'classify': 2
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.records) {
					// 	if (data.data.records[i].img) {
					// 		data.data.records[i].imgs = data.data.records[i].img.split(',')
					// 	}
					// }
					let returnData = data.data
					this.userDataCp = returnData
				})
			},
			// 我的点赞
			jingpaiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/industryNewsLike/getMyLikeIndustryNewsList'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.limit,
						// 'type': this.search,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}
					}
					let returnData = data.data
					this.userDataJp = returnData
				})
			},
			selectJp() {
				this.page = 1
				this.jingpaiSelect()
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 我的访客
			fangkeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scBrowse/browseMeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'lng':'',
						'lat':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fangkeData = returnData
				})
			},
			// 我的粉丝
			fensiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/getMyFansList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'typ': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fensiData = returnData
				})
			},
			// 我关注的服务
			guanzhuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/getMyFollowList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guanzhuData = returnData
				})
			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStateById?userId=${userId}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.fangkeSelect()
						}
					})
				})
			},
			//删除浏览记录
			deleteuser(row) {
				let delid = row.dataId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/scBrowse/deleteBrowse?browseId=' + delid),
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
									this.userSelect()
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
			// 取消关注店铺
			deleteuserGz(row) {
				let delid = row.browseId
				let userId = this.$route.query.userId
				this.$confirm(`确定取消关注该店铺?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/scBrowse/deleteBrowse'),
						method: 'get',
						params: this.$http.adornParams({
							'type': this.typeLl,
							'shopId': row.shopId,
							'userId': userId
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
									this.userSelect()
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
			// 删除我的访客
			deleteuser1(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userBrowse/deleteMyVisitor?id=' + delid),
						method: 'post',
						params: this.$http.adornData({
							'id': delid
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
									this.fangkeSelect()
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
			// 关注/取关
			select(row) {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/user/insert?userId=' + userId + '&followUserId=' + row.userId),
					method: 'post',
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
								this.fensiSelect()
								this.guanzhuSelect()
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
			// 取消关注、收藏
			selectShop(row, index) {
				var dataId = ''
				var shopId = ''
				if (index == 1) {
					dataId = row.dataId
					shopId = ''
				} else {
					dataId = ''
					shopId = row.shopId
				}
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/updateFollow'),
					method: 'post',
					params: this.$http.adornParams({
						'type': index,
						'dataId': dataId,
						'shopId': shopId,
						'userId': userId
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
								if (index == 1) {
									this.guanzhuSelect()
								} else {
									this.InformationSelect()
								}
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
			// 我的聊天会话
			liaotianSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.liaotianData = returnData;
				})
			},
			// 聊天内容
			complainDetails(row) {
				this.chatConversationId = row.chatConversationId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible4 = true
			},
			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.pinglunDatas = returnData
				})
			},
			// 我的常用语
			guanjianciSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/page/20/' + this.page + '/' + this.limit),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.guanjianciData = returnData
				})
			},
			// 添加关键词
			add() {
				this.title2 = '添加关键词'
				this.contents = ''
				this.guanjianciId = ''
				this.dialogFormVisible3 = true
			},
			// 修改关键词
			updateGjc(row) {
				this.title2 = '修改关键词'
				this.contents = row.content
				this.guanjianciId = row.id
				this.dialogFormVisible3 = true
			},
			addGuanjianci() {
				var datas = {}
				let userId = this.$route.query.userId
				if (this.guanjianciId == '') {
					var url = 'message/add'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
				}
				if (this.guanjianciId != '') {
					var url = 'message/update'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
					datas.id = this.guanjianciId
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.guanjianciSelect()
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
			deletes(row) {
				let userId = this.$route.query.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`message/delete/${row.id}`),
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
									this.guanjianciSelect()
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
			// 修改自动回复
			msgChange(userId, msg) {
				this.msg = msg
				this.dialogFormVisible5 = true
			},
			// 更改自动回复
			StairNoticeTo3() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'msg': this.msg
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible5 = false
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
			
			// 等级选择事件
			animeDat(state) {
				this.page = 1
				this.tuanduiSelect1()
			},
			// 我的邀请
			tuanduiSelect1() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('inviteRecord/myInviteRecord'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						// 'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.page;
					this.tuanduiData = returnData
				})
			},
			// 我的投诉
			tousuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.states,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].image) {
							data.data.list[i].image = data.data.list[i].image.split(',')
						}
					}
					let returnData = data.data;
					this.tousuData = returnData;
				})
			},
			// 我联系过的
			dongtaiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/procureDemandRole/getProcureDemandRoleListByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dongtaiData = returnData;
				})
			},
			// 谈论列表
			talkAbout(row) {
				this.talkId = row.talkId
				this.pinglunSelects(row.talkId)
				this.dialogFormVisible4 = true
			},
			// 获取动态评论数据
			pinglunSelects(talkId) {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectCommentPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.limit,
						'talkId': talkId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.pinglunDatas = returnData
				})
			},
			// 删除动态
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalkById`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
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
									this.dongtaiSelect()
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
			// 删除评论
			pldeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
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
									this.pinglunSelects(this.talkId)
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
			// 点数明细
			dianzanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/countMoneyDetails/getCountMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dianzanData = returnData;
				})
			},
			// 我的卖出
			pinglunSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getMyBusinessNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.pinglunData = returnData;
				})
			},
			// 我的打赏
			dashangSelect() {
				this.tableDataLoading1 = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1 - 1,
						'limit': this.limit1,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.dashangData = returnData;
				})
			},
			// 空投记录
			yhqSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftAirdropRecord/getUserReceiveList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dashangData = returnData;
				})
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('cash/sendMsg'),
					method: 'get',
					params: this.$http.adornParams({
						'title': this.title,
						'content': this.content,
						'phone': this.phone,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					if (data.status == -100) {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.content = ''
								this.userSelect()
							}
						})
					}
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.content = ''
								this.userSelect()
							}
						})
					}
				})
			},
			ageChangeU(row) {
				this.inviterCodeU = row.staffInviterCode
				this.avatarU = row.avatar
				this.userNameU = row.userName
				this.phoneU = row.phone
				this.invitationCodeU = row.staffInvitationCode
				// this.staffInvitationCodeU = row.staffInvitationCode
				// this.staffInviterCodeU = row.staffInviterCode

				this.dialogFormVisibleU = true
			},
			handleAvatarSuccess1(file, fileList) {
				this.avatarU = file.data
			},
			StairNoticeToU() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'staffInviterCode': this.inviterCodeU,
						'avatar': this.avatarU,
						'userName': this.userNameU,
						'phone': this.phoneU,
						'staffInvitationCode': this.invitationCodeU,
						// 'staffInvitationCode': this.staffInvitationCodeU,
						// 'staffInviterCode': this.staffInviterCodeU,
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisibleU = false
							this.dataSelect()
						}
					})
				})
			},
			ageChangeYj(age, index) {
				this.indexType = index
				this.age = age
				this.dialogFormVisible2 = true
			},
			// 获取 名片信息
			dataSelectMp() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/getBusinessCard'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						if (data.data) {
							let returnData = data.data
							this.tableDataMp = returnData
						}
					}
				})
			},
			// 评论列表弹框
			pinglunBtn(row) {
				this.page1 = 1
				this.newsId = row.newsId
				this.dataSelectPl()
				this.dialogFormVisiblePl = true
			},
			//评论列表
			dataSelectPl() {
				this.tableDataLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('admin/industryNewsComment/getCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'newsId': this.newsId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingEr = false
					let returnData = data.data;
					this.tableDataPl = returnData
				})
			},
			// 删除评论
			deleteswzPl(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/industryNewsComment/deleteIndustryNewsComment'),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
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
									this.name = '';
									this.dataSelectPl()
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
			// 点赞列表弹框
			dianzanBtn(row) {
				this.page1 = 1
				this.newsId = row.newsId
				this.dataSelectDz()
				this.dialogFormVisibleDz = true
			},
			//点赞列表
			dataSelectDz() {
				this.tableDataLoadingEr = true
				this.$http({
					url: this.$http.adornUrl('admin/industryNewsLike/getLikeUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'newsId': this.newsId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingEr = false
					let returnData = data.data;
					this.tableDataDz = returnData
				})
			},
			// 邀请用户的会员开通记录
			walletSelectHy() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/staffData/getVipRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'vipId': this.tableData.vipId,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletDataHy = returnData
				})
			},
			// 合作商统计
			incomeSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/staffData/getStaffUserInfo'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info1.stockDate1,
						'dateType': this.flag,
						'userId': userId,
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
			animeDatHzs() {
				this.incomeSelect()
			},
			// 我的团队分销列表
			yaoqingSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/invite/invitePage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.page
					this.yaoqingData = returnData
				})
			},
			// 会员开通记录
			kaitongSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/staffData/getVipRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'vipId': this.vipId,
						'startTime': this.startTime,
						'endTime': this.endTime,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.kaitongData = returnData
				})
			},
			// 我的名片
			mingpianSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/businessCard/queryAllList?userId='+userId),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'pageSize': this.limit,
						'pageNum': this.page,
						'userId': userId,

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
					let returnData = data.data
					this.kaitongData = returnData
				})
			},
			selectHy() {
				this.page = 1
				this.kaitongSelect()
			},
			// 导出
			exportBtn() {

				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/staffData/departmentExcelOut'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'staffUserId': userId,
						'vipId': this.vipId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'staffUserName': '',
						'userName': '',
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
						elink.download = '开通记录列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 获取用户 关键词配置
			dataSelectGjc() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('sendMessage/selectSendMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						if (data.data) {
							let returnData = data.data
							this.tableDataMp = returnData
						}
					}
				})
			},
			// 修改关键词
			ageChangeGjc(row) {
				// this.inviterCodeU = row.inviterCode
				// this.avatarU = row.avatar
				// this.userNameU = row.userName
				// this.phoneU = row.phone
				// this.invitationCodeU = row.invitationCode
				// this.staffInvitationCodeU = row.staffInvitationCode
				this.sendMessageId = row.sendMessageId
				this.isOpen = row.isOpen
				if (row.content) {
					this.contentL = row.content.split(',')
				} else {
					this.contentL = []
				}

				if (row.industryFirstName) {
					this.industryFirstNameY = row.industryFirstName.split(',')
				} else {
					this.industryFirstNameY = []
				}
				if (row.industrySecondName) {
					this.privilege = row.industrySecondName.split(',')
				} else {
					this.privilege = []
				}

				this.industryFirstNameT = ''
				this.industrySecondNameT = ''
				this.classifyIds = []
				this.tableDataFer = []
				this.industryFirstId = ''
				this.dataSelectW()
				this.dialogFormVisibleGjc = true
			},
			chongzhi() {
				this.industryFirstNameY = []
				this.privilege = []
			},
			// 确认修改关键词
			StairNoticeToGjc() {
				let userId = this.$route.query.userId
				// if (this.isOpen == 1) {
				// 	if (this.privilege.length == 0) {
				// 		this.$notify({
				// 			title: '提示',
				// 			duration: 1800,
				// 			message: '二级行业不能为空',
				// 			type: 'warning'
				// 		});
				// 		return
				// 	}

				// }
				this.$http({
					url: this.$http.adornUrl('sendMessage/insertSendMessage'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'sendMessageId': this.sendMessageId,
						'isOpen': this.isOpen,
						'content': this.contentL.toString(),
						'industrySecondName': this.privilege.toString(),
						'industryFirstName': this.industryFirstNameY.toString(),
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleGjc = false
								this.dataSelectGjc()
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
					console.log('index', index, this.industryFirstNameT)
					for (var i in this.classifyIds) {
						if (this.industryFirstNameT == this.classifyIds[i].industryName) {
							this.industryFirstId = this.classifyIds[i].industryId
							this.industrySecondNameT = ''
							this.dataSelectFer()
						}
					}
				}
			},
			selectW() {
				if (this.privilege.length == 0) {
					this.industryFirstNameY.push(this.industryFirstNameT)
					this.privilege.push(this.industrySecondNameT)
				} else {
					for (var j in this.privilege) {
						if (this.privilege[j] == this.industrySecondNameT) {
							console.log('===========1', this.privilege[j])
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '不可重复选择二级名称',
								type: 'warning'
							});
							return
						}
					}
					this.industryFirstNameY.push(this.industryFirstNameT)
					this.privilege.push(this.industrySecondNameT)
				}
			},
			// 删除行业分类
			dels2(index) {
				this.industryFirstNameY.splice(index, 1);
				this.privilege.splice(index, 1);
				console.log(this.privilege)
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入关键词',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.contentL.push(this.bq)
					this.bq = ''
				}
			},
			// 删除管检测
			dels1(index) {
				this.contentL.splice(index, 1);
				console.log(this.contentL)
			},
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
						message: '请输入赠送份数',
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
				this.tableDataLoading = true
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
					this.tableDataLoading = false
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
			// 更改激活用户状态
			isPriceUserChange(userId, isAgent) {
				if (isAgent == 1) {
					var text = '确定要取消激活用户？'
					var isAgent = 2
				} else {
					var text = '确定要激活用户？'
					var isAgent = 1
				}
				this.$confirm(text, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'isPriceUser': isAgent
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			dataSelect1() {
				// this.activeName = 'first'
				this.dataSelect()
			},
			selectMp(companyName) {

				this.$router.push({
					path: '/businessCard',
					query: {
						companyName: companyName
					}
				})
				this.dialogFormVisible = false
			},
			// 名片分组
			jihuoSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/cardGroups/query'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						'userId': userId,
						'isDefault':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fenleiData = returnData
				})
			},
			
			// 详情跳转
			updates2(inviterCode) {
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': inviterCode
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$router.push({
							path: '/userDetail1',
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
			// 添加/修改 名片分类
			addMingpian(row){
				if(row){
					this.titles = '修改分类'
					this.id = row.id
					this.groupsName = row.groupsName
					this.remark = row.remark
					this.isDefault = row.isDefault
					this.sortId = row.sortId
				}else{
					
					this.titles = '添加分类'
					this.id = ''
					this.groupsName = ''
					this.remark = ''
					this.isDefault = 2
					this.sortId = ''
				}
				this.dialogFormVisibleMpfl = true
			},
			// 确认添加、修改 分类
			StairNoticeToMpfl() {
				let userId = this.$route.query.userId
					if (this.groupsName == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入分组名称',
							type: 'warning'
						});
						return
					}
					
				var url = ''
				if(this.titles == '添加分类'){
					url = 'admin/cardGroups/insert?userId='+userId
				}else{
					url = 'admin/cardGroups/edit?userId='+userId
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'id': this.id,
						'groupsName': this.groupsName,
						'remark': this.remark,
						'isDefault': this.isDefault,
						'sortId':this.sortId,
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleMpfl = false
								this.jihuoSelect()
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
			// 删除评论
			deleteswzMpfl(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/cardGroups/del/'+row.id),
						method: 'post',
						params: this.$http.adornParams({
							// 'commentId': row.commentId
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
									this.name = '';
									this.jihuoSelect()
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
			// 分享明细
			fenxiangmingxiSelect(){
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/cardGroups/query'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						'userId': userId,
						'isDefault':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fenleiData = returnData
				})
			},
			// 申请记录
			shenqingSelect(){
				this.tableDataLoading = true
				let byUserId = ''
				let userId = ''
				if(this.states == 1){
					 byUserId = this.$route.query.userId
					 userId = ''
				}else{
					 byUserId = ''
					 userId = this.$route.query.userId
				}
				
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': 6,
						'type': 1,
						'status':'',
						// 'page': this.page,
						// 'limit': this.limit,
						'byUserId': byUserId,
						'userId':userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fenleiData = returnData
				})
			},
			// 名片分享记录
			fenxiangSelect(){
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/cardGroups/query'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						'userId': userId,
						'isDefault':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fenleiData = returnData
				})
			},
			
		},
		mounted() {
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

	.nf-page-hero__back {
		display: inline-flex;
		align-items: center;
		gap: 4px;
		margin-bottom: 10px;
		font-size: 13px;
		color: var(--nf-brand, #2f3968);
		text-decoration: none;
	}

	.nf-page-hero__back:hover {
		color: var(--nf-brand-deep, #1f2645);
	}

	.nf-page-hero__back-icon {
		width: 1.2em;
		height: 1.2em;
	}

	.nf-detail-table {
		overflow-x: auto;
	}

	.nf-detail-table table {
		width: 100%;
		border-collapse: collapse;
	}

	.detailtabel .table_main {
		border: 1px solid var(--nf-border, #e8eaed);
	}

	.nf-detail-table tr {
		border-bottom: 1px solid var(--nf-border, #e8eaed);
	}

	.nf-detail-table th {
		background: var(--nf-sidebar-bg-alt, #f7f8fa);
		padding: 12px 16px;
		border-right: 1px solid var(--nf-border, #e8eaed);
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		color: var(--nf-text-primary, #1a2433);
		font-weight: 600;
		font-size: 13px;
		line-height: 1.5;
		white-space: nowrap;
	}

	.nf-detail-table td {
		padding: 12px 16px;
		border-right: 1px solid var(--nf-border, #e8eaed);
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		color: var(--nf-text-regular, #303133);
		font-size: 13px;
		line-height: 1.5;
		word-break: break-all;
	}

	.border-rt {
		border-right: none !important;
	}

	.border-bt {
		border-bottom: none !important;
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

	.nf-detail-card {
		padding: 16px;
	}

	.nf-table-card__toolbar {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		gap: 8px;
		margin-bottom: 12px;
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

	/* .el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	} */

	.box {
		padding: 44px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: var(--nf-brand, #2f3968);
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #2f3968;
	}

	.text_color span {
		margin-right: 5px;
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