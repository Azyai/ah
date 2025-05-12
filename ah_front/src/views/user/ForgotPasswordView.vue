<template>
  <div class="auth-container">
    <el-dialog v-model="dialogVisible" custom-class="custom-forgot-password-dialog" @close="handleClose">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>忘记密码</h3>
        </div>
        <!-- 步骤条 -->
        <el-steps :active="step" align-center>
          <el-step title="邮箱验证" />
          <el-step title="修改密码" />
        </el-steps>
        <div class="dialog-body">
          <!-- Step 1: 邮箱验证 -->
          <div v-if="step === 1">
            <h2>重置密码</h2>
            <p style="text-align: center; color: #666; margin-bottom: 30px;">
              请输入你的邮箱以重置密码
            </p>
            <el-form :model="step1Form" label-width="80px">
              <!-- 邮箱输入框和发送验证码按钮在同一行 -->
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="邮箱">
                    <el-input v-model="step1Form.email" placeholder="请输入邮箱" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-button type="primary" @click="sendVerificationCode" style="width: 100%;">发送验证码</el-button>
                </el-col>
              </el-row>

              <!-- 验证码输入框和校验验证码按钮 -->
              <el-form-item label="验证码">
                <el-input v-model="step1Form.code" placeholder="请输入验证码" />
              </el-form-item>
              <el-button type="primary" @click="validateCode" style="width: 100%;">校验验证码</el-button>
            </el-form>
          </div>

          <!-- Step 2: 修改密码 -->
          <div v-else>
            <h2>设置新密码</h2>
            <p style="text-align: center; color: #666; margin-bottom: 30px;">
              请输入验证码并设置新密码
            </p>
            <el-form :model="step2Form" label-width="80px" @submit.prevent="submitResetPassword">

              <el-form-item label="新密码">
                <el-input
                    v-model="step2Form.password"
                    :type="showPassword ? 'text' : 'password'"
                    show-password
                    @click:visible="showPassword = !showPassword"
                    placeholder="请输入新密码"
                />
              </el-form-item>
              <el-form-item label="确认密码">
                <el-input
                    v-model="step2Form.confirmPassword"
                    :type="showConfirmPassword ? 'text' : 'password'"
                    show-password
                    @click:visible="showConfirmPassword = !showConfirmPassword"
                    placeholder="请再次输入新密码"
                />
              </el-form-item>
              <el-button type="primary" native-type="submit" block>重置密码</el-button>
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
import { postForm } from "@/api/axios.ts"
import { ElMessage, ElNotification } from "element-plus"

const router = useRouter()

// 控制弹窗和步骤
const dialogVisible = ref(true)
const step = ref(1) // 当前步骤：1=邮箱验证，2=修改密码
const showPassword = ref(false)
const showConfirmPassword = ref(false)

// 步骤1 表单
interface Step1Form {
  email: string
  code: string
}

const step1Form = ref<Step1Form>({
  email: '',
  code: ''
})

// 步骤2 表单
interface Step2Form {
  password: string
  confirmPassword: string
}

const step2Form = ref<Step2Form>({
  password: '',
  confirmPassword: ''
})

// 发送验证码
const sendVerificationCode = async () => {
  if (!step1Form.value.email) {
    ElMessage({
      message: '请输入邮箱',
      type: 'warning',
      duration: 3000
    })
    return
  }

  const res = await postForm("api/auth/valid-reset-email", { email: step1Form.value.email })
  if (res.code === "200") {
    ElNotification({
      title: 'Success',
      message: res.message,
      type: 'success',
    })
  } else {
    ElNotification({
      title: 'Warning',
      message: res.message,
      type: 'warning',
    })
  }
}

// 校验验证码
const validateCode = async () => {
  if (!step1Form.value.email || !step1Form.value.code) {
    ElMessage({
      message: '请填写邮箱和验证码',
      type: 'warning',
      duration: 3000
    })
    return false
  }

  const res = await postForm("api/auth/start-reset", {
    email: step1Form.value.email,
    code: step1Form.value.code
  })
  if (res.code === "200") {
    step.value = 2 // 验证码校验通过，进入下一步
    return true
  } else {
    ElNotification({
      title: 'Warning',
      message: res.message,
      type: 'warning',
    })
    return false
  }
}

// 提交重置密码
const submitResetPassword = async () => {
  const { password, confirmPassword } = step2Form.value
  if (!password || !confirmPassword) {
    ElMessage({
      message: '请填写所有字段',
      type: 'warning',
      duration: 3000
    })
    return
  }

  if (password !== confirmPassword) {
    ElMessage({
      message: '两次输入的密码不一致',
      type: 'warning',
      duration: 3000
    })
    return
  }

  // 调用重置密码接口
  const res = await postForm("api/auth/do-reset", {
    password: password
  })
  if (res.code === "200") {
    ElNotification({
      title: 'Success',
      message: '密码重置成功',
      type: 'success',
    })
    router.push('/login')
  } else {
    ElNotification({
      title: 'Warning',
      message: res.message,
      type: 'warning',
    })
    step.value = 1
  }
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

.custom-forgot-password-dialog {
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

/* 步骤条样式 */
.el-steps {
  margin: 20px 0;
}

/* 邮箱输入框和发送验证码按钮在同一行 */
.el-row {
  margin-bottom: 20px;
}
</style>
