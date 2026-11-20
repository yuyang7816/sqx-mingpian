<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">聊天管理</span>
			<h2 class="nf-page-hero__title">聊天记录</h2>
			<p class="nf-page-hero__desc">查看用户聊天会话列表，支持按用户昵称筛选，可查看详细聊天内容与回复</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">用户昵称</span>
					<el-input class="nf-filter-form__input" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="nickName">
					</el-input>
				</div>
				<div class="nf-filter-form__actions">
					<el-button type="primary" icon="document" @click="select">查询</el-button>
					<el-button type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column fixed prop="chatConversationId" label="id" width="80">
			</el-table-column>
			<el-table-column prop="userName" label="用户昵称" width="150">
				<template slot-scope="scope">
					<div class="nf-table-link" @click="updates(scope.row.userId)">
						{{scope.row.userName ? scope.row.userName : '未绑定'}}
					</div>
					<div class="nf-table-link" @click="updates(scope.row.byUserId)">
						{{scope.row.byUserName ? scope.row.byUserName : '未绑定'}}
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="picture" label="用户头像">
				<template slot-scope="scope">
					<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
						暂无图片
					</div>
					<div v-else>
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
							<img style="width: 200px; height: auto" :src="scope.row.avatar" alt="">
						</el-popover>
					</div>
					<div v-if="scope.row.byAvatar == null || scope.row.byAvatar == ''">
						暂无图片
					</div>
					<div v-else style="margin-top: 5px;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.byAvatar" alt="" slot="reference">
							<img style="width: 200px; height: auto" :src="scope.row.byAvatar" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="content" label="最新聊天内容">
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
								:src="'https://mpfxadmin.xianmaxiong.com/emoji/'+scope.row.content" alt="" slot="reference">
							<img style="width: 200px; height: 200px"
								:src="'https://mpfxadmin.xianmaxiong.com/emoji/'+scope.row.content" alt="">
						</el-popover>
					</div>
					<!-- 微信号 -->
					<div v-else-if="scope.row.messageType==5">
						<div v-if="scope.row.messageStatus==1">
							<div>微信交换请求状态：待同意</div>
						</div>
						<div v-else-if="scope.row.messageStatus==2">
							<div>己方微信号：{{scope.row.content}}</div>
							<div>对方微信号：{{scope.row.byContent}}</div>
						</div>
						<div v-else>
							微信交换请求
						</div>

					</div>
					<!-- 手机号 -->
					<div v-else-if="scope.row.messageType==6">
						<div v-if="scope.row.messageStatus==1">
							<div>手机号交换请求状态：待同意</div>
						</div>
						<div v-else-if="scope.row.messageStatus==2">

							<div>己方手机号：{{scope.row.content}}</div>
							<div>对方手机号：{{scope.row.byContent}}</div>
						</div>
						<div v-else>
							手机号交换请求
						</div>
					</div>
					<!-- 查看名片 -->
					<div v-else-if="scope.row.messageType==7">
						<!-- <div v-if="scope.row.messageStatus==1">
							<div>名片交换请求状态：待同意</div>
						</div>  v-if="scope.row.messageStatus==2"-->
						<div>
							<div v-if="scope.row.businessCard">名片：
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
							<span v-else>发送名片</span>
						</div>
						<!-- <div v-else>
							名片交换请求：已拒绝
						</div> -->
					</div>

					<div v-else>{{scope.row.content}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="updateTime" label="创建时间" width="160">
			</el-table-column>
			<!-- <el-table-column fixed='right' prop="contentCount" label="状态" width="100">
        <template slot-scope="scope">
          <span style="color: #2f3968;" v-if="scope.row.contentCount==0 ">未读</span>
          <span v-if="scope.row.contentCount == 1 ">已读</span>
        </template>
      </el-table-column> -->
			<el-table-column fixed='right' label="操作" width="150">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="complainDetails(scope.row)">聊天内容
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div class="nf-table-card__foot">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next" :total="tableData.total">
			</el-pagination>
		</div>
		</div>
		<!-- 聊天内容 -->
		<el-dialog title="聊天内容" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin:2% 0;display: inline-block;">
				<span>内容:</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入聊天内容"
					v-model="contentT">
				</el-input>
			</div>
			<div style="display: inline-block;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
					@click="select1">查询</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
					@click="cleans1">重置</el-button>
				<el-button style='margin-left:15px;' size="mini" type="warning" icon="document"
					@click="huifuBtn">回复</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="userData.records">
				<el-table-column prop="chatContentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<span class="nf-table-link" @click="updates(scope.row.userId)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}
						</span>
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
							<div v-else-if="scope.row.messageStatus==2">
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
							<div v-else-if="scope.row.messageStatus==2">

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
				<el-table-column prop="createTime" label="操作" width="100" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
							@click="deleteuser(scope.row)">删除</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="userData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 处理任务 -->
		<el-dialog title="处理任务" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">任务状态：</span>
				<el-radio-group v-model="state">
					<el-radio :label="2">下架</el-radio>
					<el-radio :label="3">不下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="complainTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 回复 弹框 -->
		<el-dialog title="回复" :visible.sync="dialogFormVisibleHf" center width="70%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">回复用户：</span>
				<el-radio-group v-model="userId"  @change="messageTypeBtn()">
					<el-radio :label="byUsers.userId" border style="height: auto;padding-bottom: 10px;">
						<img :src="byUsers.avatar" style="width: 25px;height: 25px;border-radius: 50%;" />
						<span style="margin: 5px;">{{byUsers.userName}}</span>
					</el-radio>
					<el-radio :label="users.userId" border style="height: auto;padding-bottom: 10px;">
						<img :src="users.avatar" style="width: 25px;height: 25px;border-radius: 50%;" />
						<span style="margin: 5px;">{{users.userName}}</span>
					</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">消息类型：</span>
				<el-radio-group v-model="messageType" @change="messageTypeBtn()">
					<el-radio :label="1">文本</el-radio>
					<el-radio :label="2">图片</el-radio>
					<el-radio :label="4">表情包</el-radio>
					<el-radio :label="5">发送微信号</el-radio>
					<el-radio :label="6">发送手机号</el-radio>
					<el-radio :label="7">发送名片</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="messageType==1">
				<span style="width: 200px;display: inline-block;text-align: right;">回复内容：</span>
				<el-input type="textarea" :rows="3" placeholder="请输入回复内容" v-model="content"
					style="width:50%;display: inline-block;">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="messageType==2">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style="display: inline-block; width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="content"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="content" :src="content" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
				<!-- <div
					>
					<el-upload class="avatar-uploader" v-model="imageUrl"
						action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div> -->
			</div>
			<div style="margin-bottom: 10px;" v-if="messageType==4">
				<span style="width: 200px;display: inline-block;text-align: right;">表情包：</span>
				<div class="icon-item" title="表情" style="position: relative;height: 195px;margin-left: 200px;">
					<!-- <el-popover v-model="emojiShow" placement="top-start" width="400" trigger="click" class="emoBox"> -->
					<div class="emotionList" style="width: 600px;">
						<a href="javascript:void(0);" @click="getEmo(index)" v-for="(item,index) in 220" :key="index"
							:class="indexX==index?'emotionItem1':'emotionItem' " style="display: inline-block;height: 27px;">
							<img v-if="index+1>200&&index+1<=219"
								:src="'https://mpfxadmin.xianmaxiong.com/emoji/'+index+'.png'" alt=""
								style="width: 20px;height: 20px;display: inline-block;margin: 3px;" />
							<img v-if="index+1>100&&index+1<=200"
								:src="'https://mpfxadmin.xianmaxiong.com/emoji/'+index+'.gif'" alt=""
								style="width: 20px;height: 20px;display: inline-block;margin: 3px;" />
						</a>

					</div>
					<!-- <el-button style="background: transparent;border: none;" class="emotionSelect"
							icon="iconfont icon-biaoqing" slot="reference">??</el-button>
					</el-popover> -->
				</div>
			</div>


			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleHf = false">取 消</el-button>
				<el-button type="primary" @click="sendTextarea()">确 定</el-button>
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
				limit1: 10,
				page1: 1,
				userId: '',
				content: '',
				contentT: '',
				nickName: '',
				form: {
					id: '',
					content: '',
					picture: '',
					state: ''
				},
				state: '',
				imgs: [],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: {},
				userData: {},
				chatConversationId: '',
				byUsers: {},
				users: {},
				dialogFormVisibleHf: false,
				messageType: '',
				faceList: [],
				emojiShow: true,
				messageStatus: 1,
				tableDataMp: {},
				purchaseCard: '',
				indexX:'',
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit1 = val;
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChange1(val) {
				this.page1 = val;
				this.neirongSelect(this.chatConversationId)
			},
			// 用户详情跳转
			updates(userid) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userid
					}
				})
			},
			//删除任务投诉
			deleteStair(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`helpClassify/deleteClassifyById/?id=${delid}`),
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
								this.name = '';
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.nickName = ''
				this.dataSelect()
			},
			// 聊天内容
			complainDetails(row) {
				this.byUsers.userId = row.byUserId
				this.byUsers.userName = row.byUserName
				this.byUsers.avatar = row.byAvatar
				this.users.userId = row.userId
				this.users.userName = row.userName
				this.users.avatar = row.avatar
				this.page1 = 1
				this.chatConversationId = row.chatConversationId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible = true
			},
			select1() {
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 重置
			cleans1() {
				this.contentT = ''
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 处理
			amendNoticeTo() {
				this.dialogFormVisible1 = true
			},
			// 处理投诉
			complainTo() {
				this.$http({
					url: this.$http.adornUrl(`/helpComplaint/solveHelpComplaint/${this.form.id}/${this.state}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible = false
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					}
				})
			},
			// 获取聊天会话列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'userId': this.userId
						'nickName': this.nickName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'chatConversationId': this.chatConversationId,
						'content': this.contentT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData
				})
			},
			//删除聊天
			deleteuser(row) {
				// let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('chat/deleteChatContentById?chatContentId=' + row
							.chatContentId),
						method: 'post',
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
									this.neirongSelect(this.chatConversationId)
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
			selectMp(companyName) {

				this.$router.push({
					path: '/businessCard',
					query: {
						companyName: companyName
					}
				})
				this.dialogFormVisible = false
			},
			// 回复弹框
			huifuBtn() {
				this.messageType = 1
				this.content = ''
				this.userId = ''
				this.messageStatus = 1
				this.dialogFormVisibleHf = true
				this.loadEmojis()
			},
			handleAvatarSuccess1(file, fileList) {
				this.content = file.data
			},
			//加载表情，存放到表情列表中
			loadEmojis() {

				const appData = require("@/assets/img/emojis.json");
				console.log('appData', appData)
				this.faceList = []
				var arr = 219
				for (var i = 0; i < arr; i++) {
					console.log(i)
					if (i > 99 && i < 219) {
						var datas = {}
						datas.imgUrl = ('https://mpfxadmin.xianmaxiong.com/emoji/' + i + '.gif')
						datas.content = (i + '.gif')

						this.faceList.push(datas);
					}

				}
			},
			// 点击表情包
			getEmo(index) {
				
				this.indexX = index
				console.log(index,this.indexX)
				if (index <= 200) {
					this.content = index + '.gif'
				} else {
					this.content = index + '.png'
				}

			},
			messageTypeBtn() {
				this.content = ''
				if (this.messageType == 4) {
					this.loadEmojis()
				}
				if (this.messageType == 7) {
					if (this.userId == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请先选择用户',
							type: 'warning'
						})
						return
					} else {
						this.dataSelectMp(this.userId)
					}

				}

			},
			sendTextarea() {
				if (this.userId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请先选择用户',
						type: 'warning'
					})
					return
				}
				if (this.messageType == 1) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入内容',
							type: 'warning'
						})
						return
					}
				}
				if (this.messageType == 2) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请上传图片',
							type: 'warning'
						})
						return
					}
				}
				if (this.messageType == 7) {
					this.purchaseCard = this.tableDataMp.cardId
				}
				this.$http({
					url: this.$http.adornUrl('chat/sendChatContent'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						'chatConversationId': this.chatConversationId,
						'content': this.content,
						'messageType': this.messageType,
						'status': 0,
						'messageStatus': this.messageStatus,
						'byContent': this.purchaseCard,
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
								this.dialogFormVisibleHf = false
								this.neirongSelect(this.chatConversationId)
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
			// 名片
			dataSelectMp(userId) {
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
		},
		mounted() {
			this.dataSelect()
		}
	};
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

	.emotionItem1 {
		background: #efefef;
		border-radius: 5px;
	}
</style>