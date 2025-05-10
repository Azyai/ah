<template>
  <div class="auth-container">
    <el-dialog v-model="dialogVisible" custom-class="custom-login-dialog" @close="handleClose">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>用户登录</h3>

        </div>
        <div class="dialog-body">
          <h2>欢迎回来</h2>
          <p style="text-align: center; color: #666; margin-bottom: 30px;">请输入你的账号信息以继续</p>

          <el-form :model="loginForm" label-width="80px" @submit.prevent="submitLogin">
            <el-form-item label="账号">
              <el-input v-model="loginForm.username" placeholder="请输入用户名或者邮箱"/>
            </el-form-item>

            <el-form-item label="密码">
              <el-input
                  v-model="loginForm.password"
                  :type="showPassword ? 'text' : 'password'"
                  show-password
                  @click:visible="showPassword = !showPassword"
                  placeholder="请输入密码"
              />
            </el-form-item>

            <el-button type="primary" native-type="submit" block>登录</el-button>
          </el-form>

          <p class="link-text">
            还没有账号？<a href="/register">立即注册</a>
          </p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {postForm} from "@/api/axios.ts";

const router = useRouter()
const dialogVisible = ref(true)
const showPassword = ref(false)

interface LoginForm {
  username: string
  password: string
}

const loginForm = ref<LoginForm>({
  username: '',
  password: ''
})

const submitLogin = () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    alert('请填写所有字段')
    return
  }
  postForm("api/auth/login", loginForm.value)
      .then((response) => {
        console.log(response)
        if(response.code === "200"){
          alert('登录成功')
          localStorage.setItem('token', response.data.token)
          router.push('/')
        }else {
          alert('登录失败，'+ response.data.message)
          router.push('/')
        }
      }).catch((error) => {
    console.error('登录失败', error)
    alert('登录失败，请检查用户名和密码')
  }).finally(() => {
    // 登录结束后，重置表单
    loginForm.value = {
      username: '',
      password: ''
    }
  })


  console.log('提交登录信息:', loginForm.value)
  router.push('/')
}

// 处理关闭弹窗事件
const handleClose = () => {
  dialogVisible.value = false
  router.push('/')
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea, #764ba2);
  font-family: 'Segoe UI', sans-serif;
}

.custom-login-dialog {
  --el-dialog-border-radius: 12px;
  --el-dialog-padding-primary: 0;
  --el-dialog-bg-color: rgba(255, 255, 255, 0.9);
  --el-dialog-box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.dialog-content {
  border: 2px solid #e0e0e0;
  border-radius: var(--el-dialog-border-radius);
  overflow: hidden;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #e0e0e0;
}

.dialog-header h3 {
  font-size: 18px;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.dialog-body {
  padding: 30px;
}

.dialog-body h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.el-form-item__label {
  font-size: 14px;
}

.el-input__inner {
  font-size: 14px;
}

.el-button {
  font-size: 14px;
  width: 100%;
  margin-top: 10px;
  transition: transform 0.2s;
}

.el-button:hover {
  transform: translateY(-2px);
}

.link-text {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.link-text a {
  color: #409EFF;
  text-decoration: none;
  font-weight: bold;
}

.link-text a:hover {
  text-decoration: underline;
}
</style>
