<template>
  <div id="app">
    <!-- 头部导航 -->
    <Header />

    <!-- 主体内容 -->
    <el-main class="main-content">
      <router-view />
    </el-main>



    <div>
      <!-- 聊天机器人挂载点 -->
      <div ref="cozeContainer" class="coze-container"></div>
    </div>


    <!-- 底部 -->
    <el-footer class="footer">
      © 2025 Your Company. All rights reserved.
    </el-footer>
  </div>
</template>

<script setup lang="ts">
import Header from './components/Header.vue'
import { RouterView } from 'vue-router'
import {onMounted, ref} from "vue";
import {useUserStore} from "@/stores/counter.ts";

const userStore = useUserStore()


const cozeContainer = ref<HTMLElement | null>(null);


// coze助手，，，
const loadCozeChat = () => {
  // 检查是否已加载 SDK
  if (window.CozeWebSDK) {
    initCoze();
    return;
  }

  // 动态加载 SDK
  const script = document.createElement('script');
  script.src = 'https://lf-cdn.coze.cn/obj/unpkg/flow-platform/chat-app-sdk/1.2.0-beta.6/libs/cn/index.js';
  script.onload = initCoze;
  script.onerror = () => console.error('Coze SDK 加载失败');
  document.body.appendChild(script);
};

const initCoze = () => {
  if (!cozeContainer.value) return;

  new window.CozeWebSDK.WebChatClient({
    config: {
      bot_id: '7507178130542100506', // 替换你的 Bot ID
    },
    componentProps: {
      title: 'Coze助手',
      container: cozeContainer.value, // 挂载到指定容器
    },
    auth: {
      type: 'token',
      token: 'pat_c8oHoH0tk9APMXtOqNS3Jqxc7Nn2sw2j7lWRlqDC2Hz12TRwR31WUlTiLKRU1tDe', // 实际项目中应从后端获取
      onRefreshToken: () => 'pat_c8oHoH0tk9APMXtOqNS3Jqxc7Nn2sw2j7lWRlqDC2Hz12TRwR31WUlTiLKRU1tDe',
    },
  });
};


onMounted( async () => {
  loadCozeChat()
  const token = localStorage.getItem("token")
  if (token) {
    await userStore.fetchUserInfo()
  }
})

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
  padding: 100px 0 0; /* 设置顶部内边距，避免被 Header 压住 */
  background-color: #f9f9f9;
}

.footer {
  background-color: #1a1a1a;
  color: white;
  text-align: center;
}
</style>
