<template>
	<div class="nf-list-page nf-message-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">消息管理</span>
			<h2 class="nf-page-hero__title">消息中心</h2>
			<p class="nf-page-hero__desc">查看用户反馈与用户消息，支持分页浏览，可跳转用户详情与查看聊天内容</p>
		</div>

		<div class="nf-tabs-wrap">
			<el-badge v-if="chatNumXx1>0" :value="chatNumXx1" class="nf-tab-badge nf-tab-badge--1"></el-badge>
			<!-- <el-badge v-if="chatNumXx2>0" :value="chatNumXx2" class="nf-tab-badge nf-tab-badge--2"></el-badge> -->
			<el-badge v-if="chatNumXx3>0" :value="chatNumXx3" class="nf-tab-badge nf-tab-badge--3"></el-badge>
			<el-badge v-if="chatNumXx4>0" :value="chatNumXx4" class="nf-tab-badge nf-tab-badge--4"></el-badge>
			<el-tabs class="nf-page-tabs" v-model="activeName" @tab-click="handleClick">

			<el-tab-pane label="用户反馈" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="联系方式">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
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

		
			<!-- <el-tab-pane label="用户投诉" name="secondYh">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="投诉用户">
						<template slot-scope="scope">
							<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.userId)">
								{{scope.row.userName}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="userName" label="被投诉用户">
						<template slot-scope="scope">
							<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.byUserId)">
								{{scope.row.byUserName}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="投诉内容" width="200">
					</el-table-column>
					<el-table-column prop="image" label="图片" width="150">
						<template slot-scope="scope">
							<div v-for="(item,index) in scope.row.image" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<div v-if="scope.row.status==0">待审核</div>
							<div v-else>已审核</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="处理结果">
						<template slot-scope="scope">
							<div v-if="scope.row.status==0">待处理</div>
							<div v-if="scope.row.status==1">不处理</div>
							<div v-if="scope.row.status==2">已封号</div>
							<div v-if="scope.row.status==3">已下架</div>
						</template>
					</el-table-column>
					<el-table-column prop="auditContent" label="审核内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" v-if="scope.row.status==0" :disabled="!isAuth('message:chuli')"
								@click="refuseClick(scope.row)">处理
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="用户消息" name="fourth">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户名称">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
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
			</el-tabs>
		</div>

			<el-dialog title="投诉处理" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
					<el-radio-group v-model="radio">
						<el-radio :label="0">通过</el-radio>
						<el-radio :label="1">驳回</el-radio>
					</el-radio-group>
				</div>

				<div style="margin-bottom: 10px;" v-if="radio===0">
					<span style="width: 200px;display: inline-block;text-align: right;">处理结果：</span>
					<el-radio-group v-model="clStatus" v-if="state != 11">
						<el-radio :label="0">封号</el-radio>
						<el-radio :label="1">下架</el-radio>
						<el-radio :label="2">不处理</el-radio>
					</el-radio-group>
					<el-radio-group v-model="clStatus" v-if="state == 11">
						<el-radio :label="0">封号</el-radio>
						<el-radio :label="2">不处理</el-radio>
					</el-radio-group>
				</div>
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
			<!-- 聊天内容 -->
			<el-dialog title="聊天内容" :visible.sync="dialogFormVisible1" center width="80%">
				<div style="margin:2% 0;display: inline-block;">
					<span>内容:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入聊天内容"
						v-model="content">
					</el-input>
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
						@click="select1">查询</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
						@click="cleans1">重置</el-button>
				</div>
				<el-table v-loading="tableDataLoading1" :data="userData.records">
					<el-table-column prop="chatContentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
						<template slot-scope="scope">
							<div style="color: #2f3968;cursor: pointer;" @click="updates(scope.row.userId)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像" width="">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
						</template>
			
					</el-table-column>
					<el-table-column prop="content" label="信息内容" width="350">
						<template slot-scope="scope">
							<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
							<div v-else-if="scope.row.messageType==2">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.content" alt="" slot="reference">
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
													{{scope.row.businessCard.companyName}}</div>
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
					<!-- <el-table-column prop="createTime" label="操作" width="100" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除</el-button>
						</template>
					</el-table-column> -->
			
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="userData.total">
					</el-pagination>
				</div>
			</el-dialog>
	</div>

</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				url: '',
				title: '',
				type: '',
				id: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				dialogFormVisible1:false,
				tableDataLoading1: false,
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				platformList: [],
				chatNumXx1: 0,
				chatNumXx2: 0,
				chatNumXx3: 0,
				chatNumXx4:0,
				limit1: 10,
				page1: 1,
				chatConversationId: '',
				userData:{},
				classify:'',
			}
		},
		methods: {
			// 详情跳转
			updataDetails(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					});
				}

			},
			// 商机跳转
			qgDetails(title){
				if (title) {
					this.$router.push({
						path: '/autonym',
						query: {
							title: title
						}
					});
				}
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
					this.classify = ''
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '采购投诉') {
					this.page = 1
					this.limit = 10
					this.state = 4
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '店铺投诉') {
					this.page = 1
					this.limit = 10
					this.state = 5
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '聊天投诉') {
					this.page = 1
					this.limit = 10
					this.state = 11
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '用户投诉') {
					this.page = 1
					this.limit = 10
					this.state = 7
					this.platform1 = ''
					this.dataSelect()
				}

				if (tab._props.label == '用户消息') {
					this.page = 1
					this.limit = 10
					this.state = 6
					this.dataSelect()
				}
				if (tab._props.label == '采购名片投诉') {
					this.page = 1
					this.limit = 10
					this.state = 10
					this.dataSelect()
				}
				if (tab._props.label == '消息推送') {
					this.page = 1
					this.limit = 5
					this.state = 8
					this.dataSelect()
				}
				if (tab._props.label == '产品投诉') {
					this.page = 1
					this.limit = 5
					this.state = 12
					this.dataSelect()
				}
				
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.flag == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
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
				if (this.flag == 1) {
					if (this.phone == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入用户手机号',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
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
						this.dialogFormVisible2 = false
						if (data.code == -100) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
				if (this.flag == 2) {
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'platform': this.platform1,
						'classify':this.classify,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (this.state == 2) {
						for (var i in data.data.records) {
							var a = data.data.records[i].content.toString()
							var b = JSON.parse(a)
							data.data.records[i].content = b.content
							console.log('key', b.content)
						}
					}
					for (var i in data.data.records) {
						if (data.data.records[i].image) {
							data.data.records[i].image = data.data.records[i].image.split(',')
						}

					}

					let returnData = data.data;
					this.tableData = returnData

				})
			},
			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.platform = rows.platform
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}

				if (this.radio === 0) {
					if (this.clStatus === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择处理结果',
							type: 'warning'
						});
						return
					}
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入审核理由',
						type: 'warning'
					});
					return
				}
				var status = ''
				if (this.radio === 1) {
					status = 1
				} else {
					if (this.clStatus === 0) {
						status = 2
					}
					if (this.clStatus === 1) {
						status = 3
					}
					if (this.clStatus === 2) {
						status = 1
					}
				}

				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.messageId,
						'status': status,
						'auditContent': this.content
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
								this.status = -1
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
			animeDat4() {
				this.dataSelect()
			},
			// 福利项目
			flxmSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '举报类型'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.platformList = returnData
				})
			},
			// 聊天内容
			complainDetails(row) {
				this.page1 = 1
				this.chatConversationId = row.platformId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible1 = true
			},
			select1() {
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 重置
			cleans1() {
				this.content = ''
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			handleSizeChange1(val) {
				this.limit1 = val;
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChange1(val) {
				this.page1 = val;
				this.neirongSelect(this.chatConversationId)
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
						'chatConversationId': this.chatConversationId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.userData = returnData
				})
			},
			selectMp(companyName){
				
				this.$router.push({
					path: '/businessCard',
					query: {
						companyName: companyName
					}
				})
				this.dialogFormVisible1 = false
			},
			select(){
				this.page = 1
				this.dataSelect()
			},
			chongzhiBtn(){
				this.page = 1
				this.classify = ''
				this.dataSelect()
			},
		},
		mounted() {
			setInterval(() => {
				this.chatNumXx1 = Number(this.$cookie.get('numberXx1'))
				// this.chatNumXx2 = Number(this.$cookie.get('numberXx2'))
				this.chatNumXx3 = Number(this.$cookie.get('numberXx3'))
				this.chatNumXx4 = Number(this.$cookie.get('numberXx4'))
			}, 1000)
			this.dataSelect()
			// this.flxmSelect()
		}
	};
</script>

<style scoped="scoped">
	.nf-list-page {
		padding: 0;
	}

	.nf-tabs-wrap {
		position: relative;
	}

	.nf-tab-badge {
		position: absolute;
		top: 10px;
		z-index: 2;
	}

	.nf-tab-badge--1 {
		left: 72px;
	}

	.nf-tab-badge--2 {
		left: 168px;
	}

	.nf-tab-badge--3 {
		left: 168px;
	}

	.nf-tab-badge--4 {
		left: 168px;
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

	.nf-table-card__foot {
		display: flex;
		justify-content: center;
		padding-top: 12px;
	}
</style>