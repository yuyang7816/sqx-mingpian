<template>
	<el-submenu
		v-if="menu.list && menu.list.length >= 1"
		:index="menu.menuId + ''"
		:popper-class="'site-sidebar--' + sidebarLayoutSkin + '-popper'">
		<template slot="title">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</template>
		<sub-menu
			v-for="item in menu.list"
			:key="item.menuId"
			:menu="item"
			:dynamicMenuRoutes="dynamicMenuRoutes">
		</sub-menu>
	</el-submenu>
	<el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)">
		<div class="nf-menu-item-inner">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span class="nf-menu-item-label">{{ menu.name }}</span>
			<span v-if="badgeCount > 0" class="nf-menu-count">{{ badgeDisplay }}</span>
		</div>
	</el-menu-item>
</template>

<script>
	import SubMenu from './main-sidebar-sub-menu'
	export default {
		name: 'sub-menu',
		data() {
			return {
				chatNumRz: 0,
				chatNumDp: 0,
				chatNumFw: 0,
				chatNumDls: 0,
				chatNumHyzx: 0,
				chatNumTx: 0,
				chatNumXx: 0,
				chatNumMp: 0,
				chatNumQz: 0,
				chatNumDh: 0,
				chatNumSq: 0,
				_badgeTimer: null
			}
		},
		props: {
			menu: {
				type: Object,
				required: true
			},
			dynamicMenuRoutes: {
				type: Array,
				required: true
			}
		},
		components: {
			SubMenu
		},
		computed: {
			sidebarLayoutSkin: {
				get() {
					return this.$store.state.common.sidebarLayoutSkin
				}
			},
			badgeCount() {
				const id = this.menu.menuId
				const n = (v) => Number(v) || 0
				if (id == 150 && this.isAuth('authentication:pass')) return n(this.chatNumRz)
				if (id == 189 && this.isAuth('businessCard:shenhe')) return n(this.chatNumFw)
				if (id == 154 && this.isAuth('authentication:pass')) return n(this.chatNumDp)
				if (id == 199 && this.isAuth('agent:shenhe')) return n(this.chatNumDls)
				if (id == 202) return n(this.chatNumHyzx)
				if (id == 34 && this.isAuth('financeList:transfer')) return n(this.chatNumTx)
				if (id == 41) return n(this.chatNumXx)
				if (id == 228) return n(this.chatNumMp)
				if (id == 194) return n(this.chatNumQz)
				if (id == 257) return n(this.chatNumDh)
				if (id == 231) return n(this.chatNumSq)
				return 0
			},
			badgeDisplay() {
				return this.badgeCount > 99 ? '99+' : String(this.badgeCount)
			}
		},
		methods: {
			gotoRouteHandle(menu) {
				var route = this.dynamicMenuRoutes.filter(item => item.meta.menuId === menu.menuId)
				if (route.length >= 1) {
					this.$router.push({
						name: route[0].name
					})
				}
			},
			syncBadgeCounts() {
				const cookie = (k) => this.$cookie.get(k)
				if (cookie('numberFw')) this.chatNumFw = cookie('numberFw')
				if (cookie('numberDp')) this.chatNumDp = cookie('numberDp')
				if (cookie('numberRz')) this.chatNumRz = cookie('numberRz')
				if (cookie('numberDls')) this.chatNumDls = cookie('numberDls')
				if (cookie('numberHyzx') || cookie('numberPl')) {
					this.chatNumHyzx = Number(cookie('numberHyzx') || 0) + Number(cookie('numberPl') || 0)
				}
				if (cookie('numberTx')) this.chatNumTx = cookie('numberTx')
				if (cookie('numberXx1') || cookie('numberXx3') || cookie('numberXx4')) {
					this.chatNumXx = Number(cookie('numberXx1') || 0) + Number(cookie('numberXx3') || 0) + Number(cookie('numberXx4') || 0)
				}
				if (cookie('numberMp')) this.chatNumMp = cookie('numberMp')
				if (cookie('numberQz')) this.chatNumQz = cookie('numberQz')
				if (cookie('numberDh') || cookie('numberDhTk')) {
					this.chatNumDh = Number(cookie('numberDh') || 0) + Number(cookie('numberDhTk') || 0)
				}
				if (cookie('numberSq')) this.chatNumSq = cookie('numberSq')
			}
		},
		mounted() {
			this.syncBadgeCounts()
			this._badgeTimer = setInterval(this.syncBadgeCounts, 1000)
		},
		beforeDestroy() {
			if (this._badgeTimer) clearInterval(this._badgeTimer)
		}
	}
</script>

<style scoped>
	.nf-menu-item-inner {
		display: flex;
		align-items: center;
		width: 100%;
		min-width: 0;
		height: 100%;
	}

	.nf-menu-item-label {
		flex: 1;
		min-width: 0;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.nf-menu-count {
		flex-shrink: 0;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		min-width: 18px;
		height: 18px;
		margin-left: 8px;
		padding: 0 6px;
		font-size: 11px;
		font-weight: 600;
		line-height: 1;
		color: #fff;
		background: #f56c6c;
		border-radius: 9px;
		box-sizing: border-box;
	}
</style>
