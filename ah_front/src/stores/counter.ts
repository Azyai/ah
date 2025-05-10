import { defineStore } from 'pinia'
import { ref } from 'vue'
import { get } from '@/api/axios'

export interface UserInfo {
    id: number
    username: string
    email?: string
    realName?: string
    gender?: number
    birthDate?: string | Date
    phone?: string
    address?: string
    avatar?: string
    bio?: string
}

export const useUserStore = defineStore('user', () => {
    const isAuthenticated = ref(false)
    const userInfo = ref<UserInfo | null>(null)
    const loading = ref(false)

    // 获取用户信息（也用于验证 token）
    const fetchUserInfo = async () => {
        if (loading.value) return
        loading.value = true

        try {
            const res = await get('/user/getUserInfo')
            if (res.code === '200') {
                userInfo.value = res.data
                isAuthenticated.value = true
            } else {
                userInfo.value = null
                isAuthenticated.value = false
            }
        } catch (error) {
            console.error('获取用户信息失败:', error)
            userInfo.value = null
            isAuthenticated.value = false
        } finally {
            loading.value = false
        }
    }

    // 清除用户信息
    const clearUserInfo = () => {
        userInfo.value = null
        isAuthenticated.value = false
    }

    return {
        isAuthenticated,
        userInfo,
        loading,
        fetchUserInfo,
        clearUserInfo
    }
})
