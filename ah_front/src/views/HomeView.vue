<template>
  <div class="home-container">
    <!-- 轮播图 -->
    <el-carousel trigger="click" height="400px" :interval="5000">
      <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
        <div class="carousel-item">
          <h1>{{ item.title }}</h1>
          <p>{{ item.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>

    <router-view></router-view>

    <el-button type="success" @click="sendTestRequest">发送测试请求</el-button>

    <!-- 产品介绍模块 -->
    <section class="features-section">
      <div class="container">
        <h2 class="section-title">我们的产品</h2>
        <div class="features-grid">
          <el-card shadow="hover" v-for="(feature, index) in features" :key="index" class="feature-card">
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </el-card>
        </div>
      </div>
    </section>

    <!-- 公司简介 -->
    <section class="about-section">
      <div class="container">
        <h2>关于我们</h2>
        <p>这是一个基于 Vue 3 和 Element Plus 构建的现代化前端应用。我们专注于提供高质量的产品与服务，助力企业数字化转型。</p>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {get} from "@/api/axios.ts";
import {ElMessage} from "element-plus";

const sendTestRequest = () => {
  const token = localStorage.getItem("token")
  if (!token) {
    ElMessage({
      message: '获取测试消息成功',
      type: 'success',
      duration: 3000
    })
    return
  }

  get("/user/getUserInfo")
      .then((response) => {
        console.log("接口响应:", response)
        if (response.code === "200") {
          alert("请求成功: " + response.data)
        } else {
          alert("请求失败: " + (response.message || "未知错误"))
        }
      })
      .catch((error) => {
        console.error("请求异常:", error)
        alert("请求失败，请检查网络或权限")
      })
}


// 轮播图数据
const carouselItems = ref([
  {
    title: '欢迎来到 Cloud AH',
    description: '打造智能化云端解决方案'
  },
  {
    title: '高效 · 稳定 · 可靠',
    description: '我们致力于为企业提供最优质的云服务体验'
  }
])

// 产品功能列表
const features = ref([
  {
    title: '智能社区',
    description: '连接用户与平台，构建开放生态体系'
  },
  {
    title: '生态系统',
    description: '整合资源，打造一体化服务平台'
  },
  {
    title: '开发工具',
    description: '提供完善的开发者支持与文档说明'
  },
  {
    title: '技术文档',
    description: '详尽的技术资料与API接口说明'
  },
  {
    title: '技术支持',
    description: '7x24小时在线客服与问题响应机制'
  }
])
</script>

<style scoped>
.home-container {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9; /* 背景色可自定义 */
}

.carousel-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: white;
  background: linear-gradient(135deg, #4ecdc4, #45b7d1);
  height: 400px;
}

.carousel-item h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.carousel-item p {
  font-size: 1.5rem;
}

.features-section {
  padding: 60px 20px;
  background-color: #ffffff;
}

.container {
  width: 100%; /* 确保容器宽度为 100% */
  max-width: 1200px; /* 可选：最大宽度限制 */
  margin: 0 auto;
  padding: 0 20px; /* 内边距可根据需要调整 */
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
  font-size: 2rem;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  text-align: center;
  padding: 20px;
}

.feature-card h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.about-section {
  padding: 60px 20px;
  background-color: #ffffff;
  border-top: 1px solid #eee;
}

.about-section h2 {
  text-align: center;
  margin-bottom: 20px;
}

.about-section p {
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
  font-size: 1rem;
  color: #666;
}
</style>
