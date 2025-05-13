<template>
  <div class="profile-container">

    <el-form label-width="120px" :model="formData" :rules="rules" ref="formRef">
      <!-- 头像上传 -->
      <el-form-item label="头像">
        <!-- 第8-11行实现 -->
        <el-upload
            :show-file-list="false"
            :auto-upload="true"
            :http-request="handleUpload"
            :before-upload="beforeAvatarUpload"
            accept="image/*"
            class="avatar-uploader"
        >
          <el-avatar :size="100" :src="avatarUrl">
            <template #default>
              <div class="avatar-hover-text">点击更换头像</div>
            </template>
          </el-avatar>
        </el-upload>
      </el-form-item>

      <!-- 不可编辑字段 -->
      <el-form-item label="用户名">
        <el-input v-model="formData.username" disabled/>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="formData.email" disabled/>
      </el-form-item>

      <!-- 可编辑字段 -->
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="formData.realName"/>
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-select v-model="formData.gender" placeholder="请选择性别">
          <el-option label="男" :value="1"/>
          <el-option label="女" :value="0"/>
        </el-select>
      </el-form-item>

      <el-form-item label="出生日期" prop="birthDate">
        <el-date-picker
            v-model="formData.birthDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input v-model="formData.phone"/>
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input v-model="formData.address"/>
      </el-form-item>

      <el-form-item label="简介" prop="bio">
        <el-input v-model="formData.bio" type="textarea" :rows="3"/>
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            @click="handleSubmit"
            :loading="submitting"
        >
          确认修改
        </el-button>
      </el-form-item>
    </el-form>


  </div>
</template>

<script setup lang="ts">
import {ref, computed,watch} from 'vue'
import {ElMessage} from 'element-plus'
import {useUserStore} from '@/stores/counter.ts'
import {postForm, post} from '@/api/axios'

import type {FormRules} from 'element-plus'

// 定义表单验证规则
const rules = ref<FormRules>({
  realName: [
    {required: true, message: '真实姓名不能为空', trigger: 'blur'},
    {min: 2, max: 20, message: '真实姓名长度应在2到20个字符之间', trigger: 'blur'}
  ],
  gender: [
    {type: 'number', message: '请选择性别', trigger: ['change']}
  ],
  birthDate: [
    {type: 'date', message: '请选择正确的日期格式', trigger: 'change'}
  ],
  phone: [
    {pattern: /^1[3-9]\d{9}$/, message: '请输入有效的中国手机号码', trigger: 'blur'}
  ],
  address: [
    {max: 100, message: '地址不能超过100个字符', trigger: 'blur'}
  ],
  bio: [
    {max: 200, message: '简介不能超过200个字符', trigger: 'blur'}
  ]
})

const userStore = useUserStore()

// 表单数据：使用 ref 初始化
const formData = ref({
  id: userStore.userInfo?.id ?? 0,
  username: userStore.userInfo?.username ?? '',
  email: userStore.userInfo?.email ?? '',
  realName: userStore.userInfo?.realName ?? '',
  gender: userStore.userInfo?.gender ?? 1,
  birthDate: userStore.userInfo?.birthDate ?? '',
  phone: userStore.userInfo?.phone ?? '',
  address: userStore.userInfo?.address ?? '',
  bio: userStore.userInfo?.bio ?? '',
  avatar: userStore.userInfo?.avatar ?? ''
})

// 监听 userStore.userInfo 的变化，更新 formData
watch(
    () => userStore.userInfo,
    (newUserInfo) => {
      if (newUserInfo) {
        formData.value = {
          id: newUserInfo.id ?? 0,
          username: newUserInfo.username ?? '',
          email: newUserInfo.email ?? '',
          realName: newUserInfo.realName ?? '',
          gender: newUserInfo.gender ?? 1,
          birthDate: newUserInfo.birthDate ?? '',
          phone: newUserInfo.phone ?? '',
          address: newUserInfo.address ?? '',
          bio: newUserInfo.bio ?? '',
          avatar: newUserInfo.avatar ?? ''
        }
      }
    },
    { immediate: true }
)

// 提交状态
const submitting = ref(false)

// 计算头像URL
const avatarUrl = computed(() => {
  if (formData.value.avatar) {
    return "http://localhost:8081/static" + formData.value.avatar
  }
  return '/default-avatar.png' // 默认头像
})


// 上传处理逻辑
const handleUpload = async (options: any) => {
  try {
    const formData = new FormData()
    formData.append('file', options.file)

    // 使用封装的postForm方法
    const res = await postForm('/user/uploadAvatar', formData)

    // 更新头像显示
    userStore.userInfo!.avatar = res.data
    ElMessage.success('头像更新成功')
    return res
  } catch (error) {
    ElMessage.error('上传失败')
    return Promise.reject(error)
  }
}

// 文件验证
const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过10MB')
    return false
  }
  return true
}



// 提交修改
const handleSubmit = async () => {
  try {
    submitting.value = true
    const res = await post('/user/updateUserInfo', formData.value)

    if (res.code === '200') {
      await userStore.fetchUserInfo() // 刷新用户信息
      ElMessage.success('信息更新成功')
    }
  } catch (error) {
    ElMessage.error('更新失败，请重试')
  } finally {
    submitting.value = false
  }
}
</script>

<!-- 默认样式 -->
<style scoped>
.profile-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.avatar-uploader {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.avatar-hover-text {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  text-align: center;
  font-size: 12px;
  padding: 5px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-uploader:hover .avatar-hover-text {
  opacity: 1;
}
</style>