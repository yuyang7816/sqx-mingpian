<template>
	<div class="nf-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">聊天管理</span>
			<h2 class="nf-page-hero__title">聊天会话</h2>
			<p class="nf-page-hero__desc">查看与管理用户聊天会话，支持搜索联系人、实时收发消息，可跳转用户详情</p>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">筛选条件</div>
			<div class="nf-filter-form">
				<div class="nf-filter-form__item">
					<span class="nf-filter-form__label">联系人</span>
					<el-input class="nf-filter-form__input nf-filter-form__input--wide" placeholder="搜索联系人"
						suffix-icon="el-icon-search" v-model="userName" clearable @change="changeSearch"
						@input="searchChange"></el-input>
				</div>
			</div>
		</div>

		<div class="nf-chat-card">
			<div class="whole-wrapper">
				<!-- 左边的聊天列表 -->
				<div class="left_whole_wrapper">
					<div class="left_wrapper" style="overflow-y: auto" v-if="chatList">
						<div class="left-item" style="position: relative; cursor: pointer"
							v-bind:class="{ 'left-item--active': index == current }"
							v-for="(item, index) in chatList" :key="index" @click="clickItem(item, index)">
							<div class="left-item-left">
								<div class="left-item-head" v-if="item.userHead == null || item.userHead == ''">
									<img src="~@/assets/img/head.png" />
								</div>
								<div class="left-item-head" v-else><img :src="item.userHead" /></div>
								<div class="left-item-info">
									<div class="left-item-info_title">{{ item.userName }}</div>
									<div class="left-item-info_content">{{ item.content }}</div>
								</div>
							</div>
							<div class="left-item-time">
								<div class="left-item-time-time" v-if="item.contentTime == null"></div>
								<div class="left-item-time-time" v-else>
									{{ item.contentTime.substring(11, 16) }}
								</div>
								<div class="left-item-time-number" style="position: absolute; left: 59px; top: 8px"
									v-if="item.storeCount">
									{{ item.storeCount }}
								</div>
								<div class="" @click.stop="deletes(item)" style="position: relative; top: 6px"
									v-if="isAuth('vueMchat:delete')">
									<img src="~@/assets/img/delete.png" alt="" width="25" height="25" />
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 中间的对话-->
				<div class="center_whole_wrapper">
					<div class="center_wrapper">
						<div class="center_wrapper_top">
							<div class="center_wrapper_top_name">{{ name }}</div>
							<el-button v-if="name" size="mini" type="primary" class="nf-chat-detail-btn"
								@click="updates(userId)">用户详情
							</el-button>
						</div>
						<div class="center_wrapper_center" id="ele" style="overflow-y: auto" ref="dialogue_box">
							<div v-for="(item, index) in dialogueList" :key="index" :class="
                item.sendType == '1'
                  ? center_wrapper_center_item
                  : center_wrapper_center_item_reserve
              ">
								<div class="left-item-head">
									<img v-if="item.sendType == '1' && userHead !== null" :src="userHead" />
									<img v-else-if="item.sendType == '1' && userHead == null"
										src="~@/assets/img/head.png" />
									<img v-else src="~@/assets/img/avatar.png" />
								</div>
								<div class="center-item-info_wrapper" v-if="item.type == 1">
									<div class="center-item-tip">
										<div class="center-item-info">
											{{ item.content }}
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<div class="center-item-info_wrapper" v-if="item.type == 2">
									<div class="center-item-tip">
										<div>
											<el-popover placement="top-start" title="" trigger="hover">
												<img style="width: 100px; height: 100px" :src="item.content" alt=""
													slot="reference" />
												<img style="width: 300px; height: 300px" :src="item.content" alt="" />
											</el-popover>
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<div class="center-item-info_wrapper" v-if="item.type == 3">
									<div class="center-item-tip center-item-tip--card"
										style="cursor: pointer;">
										<div class="" @click="orderDetails(item.content.split(',')[2])">
											<div style="display: flex">
												<img :src="item.content.split(',')[0]" alt="" width="40" height="40" />
												<div style="
                          width: 145px;
                          font-size: 13px;
                          line-height: 20px;
                          margin-left: 5px;
                          overflow: hidden;
                          text-overflow: ellipsis;
                          display: -webkit-box;
                          -webkit-line-clamp: 2;
                          -webkit-box-orient: vertical;
                        ">
													{{ item.content.split(",")[1] }}
												</div>
											</div>
											<p class="hoverp" style="font-size: 13px; color: #999999">
												订单编号: {{ item.content.split(",")[3] }}
											</p>
											<p style="font-size: 13px; color: #999999">
												创建时间:{{ item.content.split(",")[4] }}
											</p>
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<div class="center-item-info_wrapper" v-if="item.type == 4">
									<div class="center-item-tip center-item-tip--card">
										<div class="">
											<img :src="item.content.split(',')[0]" alt="" width="160" height="120" />
											<p style="width: 160px; line-height: 20px; margin-bottom: 0">
												{{ item.content.split(",")[1] }}
											</p>
											<p style="color: #f56c6c; margin-bottom: 0">
												¥{{ item.content.split(",")[2] }}
											</p>
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
							</div>
						</div>
						<div class="center_wrapper_right">
							<div class="icon-list">
								<div class="icon-item">
									<el-upload class="avatar-uploader" v-model="coverImg"
										action="https://mpfxadmin.xianmaxiong.com/sqx_fast/alioss/upload"
										:show-file-list="false" :on-success="handleAvatarSuccess"
										:before-upload="beforeAvatarUpload">
										<div class="icon-item" title="发送文件">
											<img class="icon-item-img" src="~@/assets/img/wenjian.png" />
										</div>
									</el-upload>
								</div>
							</div>
							<el-input type="textarea" id="textarea" :rows="2" placeholder="请输入内容" v-model="content"
								@keyup.enter.native="sendTextarea(1)"></el-input>
							<el-button type="primary" class="nf-chat-send-btn" v-if="content"
								@click="sendTextarea(1)">发送
							</el-button>
							<el-button type="primary" class="nf-chat-send-btn" v-else disabled
								@click="sendTextarea(1)">发送
							</el-button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				chatList: [],
				page: 1,
				size: 5,
				storeId: 0,
				targetId: "",
				userName: "",
				userId: "",
				chatId: "",
				type: "",
				current: 0,
				coverImg: "",
				sendType: "2",
				faceList: [],
				content: "",
				name: "",
				userHead: "",
				numlist: {},
				info: {
					stockDate: this.getNowTime(),
				},
				emojiShow: false,
				dialogueInfo: {},
				dialogueList: [],
				loadText: "加载中...",
				pageNum: 1,
				loadText1: "加载中...",
				pageNum1: 1,
				center_wrapper_center_item: "center_wrapper_center_item",
				center_wrapper_center_item_reserve: "center_wrapper_center_item_reserve",
			};
		},
		created() {
			this.loadEmojis();
		},
		watch: {
			$route: function(to, from) {
				if (to.name == "vueMchat") {
					let ele = document.getElementById("ele");
					this.$nextTick(() => {
						ele.scrollTop = ele.scrollHeight;
					});
				}
			},
		},
		activated() {
			let {
				userId,
				userName
			} = this.$route.query;
			if (userId && userName) {
				this.userName = userName;
				this.current = 0;
			} else {
				this.userName = "";
			}
			this.dataSelect();
			this.initWebSocket();
		},
		methods: {
			initWebSocket() {

				this.websock = new WebSocket(
				this.$http.adornWss("websocket/0"));
				this.websock.onmessage = this.websocketonmessage;
				this.websock.onerror = this.websocketonerror;
				this.websock.onopen = this.websocketonopen;
				this.websock.onclose = this.websocketclose;
			},
			websocketonopen() {
				console.log("WebSocket连接成功");
			},
			websocketonerror() {
				console.log("WebSocket连接失败");
			},
			websocketonmessage(e) {
				let msg = JSON.parse(e.data);
				this.dialogueList.push(msg);
				console.log(msg);

				let ele = document.getElementById("ele");
				this.dataSelect();
				this.$nextTick(() => {
					ele.scrollTop = ele.scrollHeight;
				});
			},
			websocketclose(e) {
				console.log("已关闭连接", e);
			},
			loadEmojis() {
				const appData = require("@/assets/img/emojis.json");
				for (let i in appData) {
					this.faceList.push(appData[i].char);
				}
			},
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear();
				var month = now.getMonth();
				var date = now.getDate();
				var hour = now.getHours();
				var minute = now.getMinutes();
				var second = now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hour}:${minute}:${second}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			updates(userId) {
				this.$router.push({
					path: "/userDetail",
					query: {
						userId: userId
					}
				});
			},
			orderDetails(id) {
				this.$router.push({
					path: "/orderDetails",
					query: {
						id: id
					}
				});
			},
			getEmo(index) {
				var textArea = document.getElementById("textarea");

				function changeSelectedText(obj, str) {
					if (window.getSelection) {
						textArea.setRangeText(str);
						textArea.selectionStart += str.length;
						textArea.focus();
					} else if (document.selection) {
						obj.focus();
						var sel = document.selection.createRange();
						sel.text = str;
					}
				}
				changeSelectedText(textArea, this.faceList[index]);
				this.description = textArea.value;
				this.emojiShow = false;
				return;
			},
			changeSearch(e) {
				if (!e) {
					this.userName = ""
					this.dataSelect();
				}
			},
			searchChange() {
				this.dataSelect();
			},
			clickItem(item, index) {
				this.current = index;
				this.userId = item.userId;
				this.name = item.userName;
				this.userHead = item.userHead;
				this.chatId = item.chatId;
				this.chatList[index].storeCount = 0
				this.clickItem2();
				this.initWebSocket();
			},


			clickItem2() {
				this.$http({
					url: this.$http.adornUrl("chatsContent/storeList"),
					method: "get",
					params: this.$http.adornParams({
						chatId: this.chatId,
					}),
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.dialogueList = returnData;
					let ele = document.getElementById("ele");
					this.$nextTick(() => {
						ele.scrollTop = ele.scrollHeight;
					});
				});
			},
			sendTextarea(type) {
				let data = {
					content: this.content,
					type: type,
					createTime: this.info.stockDate,
					sendType: this.sendType,
					userId: this.userId,
					storeId: this.storeId,
					chatId: this.chatId,
				};
				this.numlist = data;
				data = JSON.stringify(data);
				this.websock.send(data);
				this.content = "";
				this.dialogueList.push(this.numlist);
				let ele = document.getElementById("ele");
				this.$nextTick(() => {
					ele.scrollTop = ele.scrollHeight;
				});
				this.initWebSocket();
				this.dataSelect();
			},
			deletes(item) {
				this.$confirm(`确定删除聊天记录，删除后不能恢复?`, "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						this.$http({
							url: this.$http.adornUrl(`chats/delete/?id=${item.chatId}`),
							method: "get",
							params: this.$http.adornParams({}),
						}).then(({
							data
						}) => {
							this.$message({
								message: "删除成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.dataSelect();
								},
							});
						});
					})
					.catch(() => {});
			},
			handleAvatarSuccess(file, fileList) {
				this.coverImg = file.data;
				this.content = this.coverImg;
				this.sendTextarea(2);
			},
			dataSelect() {
				this.tableDataLoading = true;
				this.$http({
					url: this.$http.adornUrl("chats/list"),
					method: "get",
					params: this.$http.adornParams({
						userName: this.userName,
						storeId: this.storeId,
					}),
				}).then(({
					data
				}) => {
					this.tableDataLoading = false;
					let returnData = data.data;
					this.chatList = returnData;

					this.clickItem(this.chatList[this.current], this.current);
				});
			},
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				var extension;
				if (testmsg == 'png'||testmsg == 'jpg'||testmsg == 'jpeg'||testmsg == 'gif'){
					 extension = testmsg
				}
				if (!extension) {
					this.$message({
						message: "只能上传图片！",
						type: 'error'
					})
				}
				return extension;
			},
		},
		mounted() {},
		destroyed() {
			window.clearInterval(this.timer);
			if (this.websock) {
				this.websock.close();
			}
		},
	};
</script>
<style scoped>
	.nf-list-page {
		padding: 0;
		overflow-x: hidden;
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
		width: 220px;
	}

	.nf-chat-card {
		background: #fff;
		border-radius: 8px;
		padding: 0;
		margin-bottom: 0;
		box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
		border: 1px solid var(--nf-border, #e8eaed);
		overflow: hidden;
	}

	.whole-wrapper {
		display: flex;
		background: #fff;
		height: calc(100vh - 280px);
		min-height: 480px;
		position: relative;
		overflow: hidden;
	}

	.left_whole_wrapper {
		display: flex;
		flex-direction: column;
		width: 28%;
		min-width: 240px;
		max-width: 320px;
		border-right: 1px solid var(--nf-border, #e8eaed);
	}

	.left_wrapper {
		flex: 1;
		background-color: var(--nf-sidebar-bg-alt, #f7f8fa);
	}

	.left-item {
		display: flex;
		justify-content: space-between;
		padding: 14px 16px;
		background-color: #fff;
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		transition: background-color 0.15s ease;
	}

	.left-item:hover {
		background-color: var(--nf-brand-lite, #e6e7ed);
	}

	.left-item--active {
		background-color: var(--nf-brand-lite, #e6e7ed);
		border-left: 3px solid var(--nf-brand, #2f3968);
		padding-left: 13px;
	}

	.left-item-left {
		display: flex;
		min-width: 0;
	}

	.left-item-head>img {
		width: 44px;
		height: 44px;
		border-radius: 6px;
		object-fit: cover;
	}

	.left-item-info {
		margin-left: 12px;
		max-width: 135px;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		min-width: 0;
	}

	.left-item-info_title {
		font-size: 14px;
		font-weight: 500;
		text-align: left;
		color: var(--nf-text-primary, #1a2433);
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.left-item-info_content {
		font-size: 13px;
		text-align: left;
		color: var(--nf-text-secondary, #909399);
		margin-top: 4px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.left-item-time {
		display: flex;
		flex-direction: column;
		text-align: right;
		align-items: flex-end;
		flex-shrink: 0;
	}

	.left-item-time-time {
		font-size: 12px;
		text-align: right;
		color: var(--nf-text-secondary, #909399);
	}

	.left-item-time-number {
		width: 20px;
		height: 20px;
		line-height: 20px;
		border-radius: 50%;
		background: #ff4949;
		color: #ffffff;
		text-align: center;
		font-size: 12px;
		margin-top: 5px;
	}

	.center_whole_wrapper {
		flex: 1;
		background: var(--nf-page-bg, #f4f6fa);
		min-width: 0;
	}

	.center_wrapper {
		height: 100%;
		display: flex;
		flex-direction: column;
	}

	.center_wrapper_top {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 16px 20px;
		background: #fff;
		border-bottom: 1px solid var(--nf-border, #e8eaed);
		flex-shrink: 0;
	}

	.center_wrapper_top_name {
		font-size: 16px;
		font-weight: 600;
		text-align: left;
		color: var(--nf-text-primary, #1a2433);
	}

	.nf-chat-detail-btn {
		border-radius: 6px;
	}

	.center_wrapper_center {
		flex: 1;
		padding: 16px 20px 0;
		overflow-y: auto;
		min-height: 0;
	}

	.center_wrapper_right {
		flex-shrink: 0;
		background-color: #ffffff;
		padding: 12px 20px 16px;
		position: relative;
		border-top: 1px solid var(--nf-border, #e8eaed);
	}

	.center-item-info_wrapper {
		margin: 0 16px;
	}

	.center-item-tip {
		display: flex;
		align-items: center;
	}

	.center-item-tip--card {
		background-color: #fff;
		padding: 10px;
		border-radius: 6px;
		border: 1px solid var(--nf-border, #e8eaed);
	}

	.center_wrapper_center_item .center-item-info {
		background: #ffffff;
		border-radius: 6px;
		border: 1px solid var(--nf-border, #e8eaed);
	}

	.center_wrapper_center_item_reserve .center-item-info {
		background: var(--nf-brand-lite, #e6e7ed);
		border-radius: 6px;
	}

	.center-item-info {
		max-width: 336px;
		font-size: 14px;
		text-align: left;
		color: var(--nf-text-primary, #1a2433);
		line-height: 22px;
		padding: 12px 16px;
		word-wrap: break-word;
		word-break: break-all;
	}

	.center-item-time {
		font-size: 12px;
		text-align: left;
		color: var(--nf-text-secondary, #909399);
		line-height: 22px;
		margin-top: 8px;
	}

	.center_wrapper_center_item {
		display: flex;
		margin-bottom: 24px;
		position: relative;
	}

	.center_wrapper_center_item_reserve {
		display: flex;
		flex-direction: row-reverse;
		margin-bottom: 24px;
		position: relative;
	}

	.center_wrapper_center_item_reserve .center-item-time {
		text-align: right;
	}

	.center_wrapper_center_item_reserve .center-item-tip {
		display: flex;
		flex-direction: row-reverse;
		align-items: center;
	}

	.icon-list {
		display: flex;
		margin-bottom: 8px;
	}

	.icon-list>div:not(:first-child) {
		margin-left: 15px;
	}

	.icon-item-img {
		width: 25px;
		cursor: pointer;
	}

	.whole-wrapper>>>.el-input__inner {
		border-radius: 6px;
		border-color: var(--nf-border, #e8eaed);
	}

	.whole-wrapper>>>.el-textarea__inner {
		min-height: 72px;
		resize: none;
		border: none;
		background: #ffffff;
		padding: 5px 0;
	}

	.nf-chat-send-btn {
		width: 88px;
		height: 36px;
		font-size: 13px;
		position: absolute;
		bottom: 16px;
		right: 20px;
		border-radius: 6px;
		background-color: transparent;
		border-color: var(--nf-brand, #2f3968);
		color: var(--nf-brand, #2f3968);
	}

	.nf-chat-send-btn:hover,
	.nf-chat-send-btn:focus {
		background-color: var(--nf-brand-lite, #e6e7ed);
		border-color: var(--nf-brand, #2f3968);
		color: var(--nf-brand, #2f3968);
	}

	.nf-chat-send-btn.is-disabled,
	.nf-chat-send-btn.is-disabled:hover {
		background-color: transparent;
		border-color: var(--nf-border, #e8eaed);
		color: var(--nf-text-secondary, #909399);
	}

	.loading {
		text-align: center;
		color: var(--nf-text-secondary, #909399);
	}

	.loading span {
		margin-left: 10px;
	}

	.center_wrapper_center_item_reserve .center-item-info::after {
		content: "";
		position: absolute;
		right: 55px;
		top: 12px;
		width: 0;
		height: 0;
		border: 5px solid var(--nf-brand-lite, #e6e7ed);
		border-top-color: transparent;
		border-right-color: transparent;
		border-bottom-color: transparent;
	}

	.center_wrapper_center_item .center-item-info::after {
		content: "";
		position: absolute;
		left: 55px;
		top: 12px;
		width: 0;
		height: 0;
		border: 5px solid #ffffff;
		border-top-color: transparent;
		border-left-color: transparent;
		border-bottom-color: transparent;
	}
</style>
<style lang="scss">
	.el-popover {
		height: 200px;
		width: 400px;
		overflow: scroll;
		overflow-x: auto;
		font-size: 20px;
	}
</style>
