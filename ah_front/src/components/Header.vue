<template>
  <div class="header">
    <!-- Logo -->
    <img src="@/assets/logo.svg" alt="Logo" class="logo" />

    <!-- 导航菜单 -->
    <el-menu mode="horizontal" :ellipsis="false" background-color="#fff" text-color="#555" active-text-color="#ff4d4f">
      <el-menu-item index="1" @click="navigateTo('/')">首页</el-menu-item>
      <el-menu-item index="2" @click="navigateTo('/product')">产品</el-menu-item>
      <el-menu-item index="3" @click="navigateTo('/demo')">演示</el-menu-item>
      <el-menu-item index="4" @click="navigateTo('/price')">价格</el-menu-item>
      <el-menu-item index="5" @click="navigateTo('/download')">下载</el-menu-item>
      <el-menu-item index="6" @click="navigateTo('/tutorial')">教程</el-menu-item>
    </el-menu>

    <!-- 动态按钮区 -->
    <div class="action-buttons">
      <template v-if="isAuthenticated">
        <!-- 用户名显示（橙色部分） -->
        <span class="username" style="font-size: 24px; margin-right: 10px;">
          {{ userInfo?.username }}
        </span>

        <div class="flex flex-wrap items-center">
          <el-dropdown>
            <el-button type="primary">
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="openDrawer('profile')">个人信息设置</el-dropdown-item>
                  <el-dropdown-item @click="openDrawer('lottery')">参与抽奖列表</el-dropdown-item>
                  <el-dropdown-item @click="openDrawer('middle-list')">中间列表</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>


        <!-- 抽屉组件 -->
        <el-drawer
            v-model="drawerVisible"
            :title="drawerTitle"
            size="40%"
            destroy-on-close
        >
          <component :is="currentDrawerComponent" />
        </el-drawer>



      </template>

      <template v-else>
        <el-button type="success" plain @click="navigateTo('/register')">注册</el-button>
        <el-button type="primary" @click="navigateTo('/login')">登录</el-button>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import {type Component, ref,onMounted} from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/counter'
import { storeToRefs } from 'pinia'
import { ArrowDown } from '@element-plus/icons-vue'

// 引入各个抽屉内容组件
import ProfileInfo from './user/ProfileInfo.vue'
import LotteryList from './user/LotteryList.vue'
import WinningList from './user/WinningList.vue'



const router = useRouter()
const userStore = useUserStore()

// ✅ 使用 storeToRefs 保留响应性
const { isAuthenticated, userInfo } = storeToRefs(userStore)

// 路由跳转方法
const navigateTo = (path: string) => {
  router.push(path)
}

// 处理退出登录
const handleLogout = () => {
  userStore.clearUserInfo()

  // router.push('/')
}

// 下拉菜单状态
const showDropdown = ref(false)

// 抽屉相关状态
const drawerVisible = ref(false)
const drawerTitle = ref('')
const drawerType = ref('')

// 当前要显示的组件
const currentDrawerComponent = ref<Component | null>(null)

// 映射关系
const componentsMap = {
  profile: ProfileInfo,
  lottery: LotteryList,
  winning: WinningList
}

// 打开抽屉
const openDrawer = (type: string) => {
  showDropdown.value = false // 关闭下拉菜单
  drawerType.value = type
  drawerTitle.value = getDrawerTitle(type)
  currentDrawerComponent.value = componentsMap[type as keyof typeof componentsMap] || null
  drawerVisible.value = true
}

onMounted(async () => {
  const token = localStorage.getItem("token")
  if (token) {
    await userStore.fetchUserInfo()
  }
})


// 获取标题
const getDrawerTitle = (type: string): string => {
  switch (type) {
    case 'profile':
      return '个人信息设置'
    case 'lottery':
      return '参与抽奖列表'
    case 'winning':
      return '中奖列表'
    default:
      return ''
  }
}
</script>


<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: 100px; /* 增加导航栏高度 */
  position: fixed; /* 固定在顶部 */
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000; /* 确保覆盖其他内容 */
  background-color: white; /* 避免透明背景导致的内容遮挡 */
}

.logo {
  height: 60px;
}

.el-menu {
  border-bottom: none;
}

.el-menu-item.is-active {
  color: #ff4d4f !important;
  border-bottom: 2px solid #ff4d4f !important;
}

.action-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.el-button {
  height: 40px;
  font-size: 16px;
}

.el-button--primary {
  background-color: #d8d9da;
  border-color: #c4c8cd;
}

.el-button--primary.plain {
  background-color: transparent;
  border-color: #ffa500;
  color: #ffa500;
}

/* 增大下拉菜单项字体 */
.el-dropdown-menu__item {
  font-size: 18px !important; /* 根据需要调整 */
}

/* 下拉菜单触发器样式 */
.el-button--text {
  padding: 0;
  color: #555;
}

.username {
  cursor: default;
}

/* 功能项容器样式 */
.function-items {
  display: flex;
  align-items: center;
  margin-left: 10px; /* 调整与用户名的间距 */
}

/* Tooltip 样式 */
.el-tooltip__popper {
  font-size: 14px;
}
</style>
