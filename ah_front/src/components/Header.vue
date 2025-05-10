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

    <!-- 注册和登录按钮 -->
    <div class="action-buttons">
      <el-button type="success" plain @click="navigateTo('/register')">注册</el-button>
      <el-button type="primary" @click="navigateTo('/login')">登录</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
}

.el-button {
  height: 40px;
  font-size: 16px;
}

.el-button--primary {
  background-color: #1890ff;
  border-color: #1890ff;
}

.el-button--primary.plain {
  background-color: transparent;
  border-color: #ffa500;
  color: #ffa500;
}
</style>
