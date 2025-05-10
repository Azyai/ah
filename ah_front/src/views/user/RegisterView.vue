<template>
  <div class="auth-container">
    <el-dialog v-model="dialogVisible" custom-class="custom-register-dialog" @close="handleClose">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>{{ step === 1 ? '注册第一步 - 邮箱验证' : '注册第二步 - 设置账户' }}</h3>
        </div>
        <div class="dialog-body">
          <!-- Step 1 -->
          <div v-if="step === 1">
            <h2>注册新账号</h2>
            <p style="text-align: center; color: #666; margin-bottom: 30px;">
              我们将向你的邮箱发送验证码，请注意查收。
            </p>
            <el-form :model="step1Form" label-width="80px" @submit.prevent="sendVerificationCode">
              <el-form-item label="邮箱">
                <el-input v-model="step1Form.email" placeholder="请输入邮箱" />
              </el-form-item>
              <el-button type="primary" native-type="submit" block>发送验证码</el-button>
            </el-form>
          </div>

          <!-- Step 2 -->
          <div v-else>
            <h2>设置你的账户</h2>
            <p style="text-align: center; color: #666; margin-bottom: 30px;">
              完成以下信息，开启你的旅程
            </p>
            <el-form :model="step2Form" label-width="80px" @submit.prevent="submitRegister">
              <el-form-item label="用户名">
                <el-input v-model="step2Form.username" placeholder="请输入用户名" />
              </el-form-item>

              <el-form-item label="密码">
                <el-input
                    v-model="step2Form.password"
                    :type="showPassword ? 'text' : 'password'"
                    show-password
                    @click:visible="showPassword = !showPassword"
                    placeholder="请输入密码"
                />
              </el-form-item>

              <el-form-item label="确认密码">
                <el-input
                    v-model="step2Form.confirmPassword"
                    :type="showConfirmPassword ? 'text' : 'password'"
                    show-password
                    @click:visible="showConfirmPassword = !showConfirmPassword"
                    placeholder="请再次输入密码"
                />
              </el-form-item>

              <el-form-item label="验证码">
                <el-input v-model="step2Form.code" placeholder="请输入验证码" />
              </el-form-item>

              <el-button type="primary" native-type="submit" block>注册</el-button>
            </el-form>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 控制弹窗和步骤
const dialogVisible = ref(true)
const step = ref(1) // 当前步骤：1=邮箱验证，2=设置账户
const showPassword = ref(false)
const showConfirmPassword = ref(false)

// 步骤1 表单
interface Step1Form {
  email: string
}

const step1Form = ref<Step1Form>({
  email: ''
})

// 步骤2 表单
interface Step2Form {
  username: string
  password: string
  confirmPassword: string
  code: string
}

const step2Form = ref<Step2Form>({
  username: '',
  password: '',
  confirmPassword: '',
  code: ''
})

// 模拟发送验证码
const sendVerificationCode = () => {
  if (!step1Form.value.email) {
    alert('请输入邮箱')
    return
  }
  alert('验证码已发送，请查收邮件')
  step.value = 2
}

// 提交注册
const submitRegister = () => {
  const { username, password, confirmPassword, code } = step2Form.value
  if (!username || !password || !confirmPassword || !code) {
    alert('请填写所有字段')
    return
  }

  if (password !== confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }

  console.log('提交注册信息:', step2Form.value)
  router.push('/login')
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
  background: linear-gradient(135deg, #fbc2eb, #a6c1ee);
  font-family: 'Segoe UI', sans-serif;
}

.custom-register-dialog {
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
</style>
