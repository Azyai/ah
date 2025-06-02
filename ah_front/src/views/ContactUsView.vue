<script setup lang="ts">
import {
  Message,
  Phone,
  Location,
  Promotion, Link
} from '@element-plus/icons-vue'
</script>

<template>
  <div class="contact-us-container">
    <el-container>
      <el-row justify="center" style="width: 100%">
        <el-col :xs="24" :sm="20" :md="16" :lg="12" :xl="10">
          <el-card class="contact-card" shadow="hover">
            <h1 class="title">联系我们</h1>
            <p class="description">
              如果您有任何问题，请通过以下方式与我们联系：
            </p>

            <!-- 邮箱 -->
            <el-row align="middle" class="contact-item">
              <el-col :span="4" class="icon-wrapper">
                <el-icon><Message /></el-icon>
              </el-col>
              <el-col :span="20">
                <p>发送邮件至：<a href="mailto:1115060927@qq.com">1115060927@qq.com</a></p>
              </el-col>
            </el-row>

            <!-- 微信群 -->
            <el-row align="middle" class="contact-item">
              <el-col :span="4" class="icon-wrapper">
                <el-icon><Promotion /></el-icon>
              </el-col>
              <el-col :span="20">
                <p>或者加入我们的微信群，欢迎各位小伙伴加入！</p>
              </el-col>
            </el-row>

            <!-- GITHUB -->
            <el-row align="middle" class="contact-item">
              <el-col :span="4" class="icon-wrapper">
                <el-icon><Link /></el-icon>
              </el-col>
              <el-col :span="20">
                <p>本系统源码请查看GitHub：<a href="https://github.com/Azyai/ah">https://github.com/Azyai/ah</a></p>
              </el-col>
            </el-row>

            <!-- 其他方式 -->
            <el-row align="middle" class="contact-item">
              <el-col :span="4" class="icon-wrapper">
                <el-icon><Phone /></el-icon>
              </el-col>
              <el-col :span="20">
                <p>联系电话：+86 123-4567-8901（工作日 9:00 - 18:00）</p>
              </el-col>
            </el-row>

            <el-row align="middle" class="contact-item">
              <el-col :span="4" class="icon-wrapper">
                <el-icon><Location /></el-icon>
              </el-col>
              <el-col :span="20">
                <p>地址：中国 · 某某市某某区某某路 123 号</p>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </el-container>
  </div>
</template>

<style scoped>
.contact-us-container {
  padding: 40px 20px;
  background-color: #f9f9f9;
  min-height: 100vh;
  display: flex;
  align-items: center;
}

.contact-card {
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  background-color: #ffffff;
}

.title {
  text-align: center;
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.description {
  text-align: center;
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.contact-item {
  margin-bottom: 20px;
}

.icon-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: #409EFF;
}

.contact-item p {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.contact-item a {
  color: #409EFF;
  text-decoration: none;
}

.contact-item a:hover {
  text-decoration: underline;
}
</style>
