<template>
  <div id="app">
    <!-- 头部导航 -->
    <Header />

    <!-- 主体内容 -->
    <el-main class="main-content">
      <router-view />
    </el-main>

    <!-- 底部 -->
    <el-footer class="footer">
      © 2025 Your Company. All rights reserved.
    </el-footer>
  </div>
</template>

<script setup lang="ts">
import Header from './components/Header.vue'
import { RouterView } from 'vue-router'
</script>

<style scoped>
#app {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding: 0; /* 移除默认内边距 */
  background-color: #f9f9f9;
}

.footer {
  background-color: #1a1a1a;
  color: white;
  text-align: center;
}
</style>
