import axios from 'axios'
import type {
    AxiosInstance,
    AxiosResponse,
} from 'axios'

import { ElMessage } from 'element-plus'
import qs from 'qs'

// 引入自定义类型
import type { ResultData } from '@/api/api'

const BASE_URL =  'http://127.0.0.1:8081'

// 创建 Axios 实例
const instance: AxiosInstance = axios.create({
    baseURL: BASE_URL,
    timeout: 20000,
    responseType: 'json',
})

instance.defaults.withCredentials = true

// 请求拦截器
// import { useUserStore } from '@/stores/user'
// 请求拦截器：添加 token
instance.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

// 响应拦截器：不做判断，直接放行
instance.interceptors.response.use(response => {
    return response
}, error => {
    return Promise.reject(error)
})



// 定义通用请求函数
export function get<T = any>(
    shortURL: string,
    params?: Record<string, any>
): Promise<T> {
    return instance.get<T>(shortURL, {
        params,
        withCredentials: true
    }).then(res => res.data)
}

export function post<T = any>(
    shortURL: string,
    data?: Record<string, any>
): Promise<T> {
    return instance.post<T>(shortURL, data, {
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    }).then(res => res.data)
}

// 定义通用请求函数
export function postForm<T = any>(
    shortURL: string,
    data?: Record<string, any> | FormData,
    config?: { headers?: Record<string, string> }
): Promise<T> {
    // 默认不设置 Content-Type，让浏览器自动识别 multipart/form-data 和 boundary
    const requestConfig = {
        headers: config?.headers || {}
    }

    const isFormData = data instanceof FormData
    const body = isFormData ? data : qs.stringify(data)

    return instance.post<T>(shortURL, body, requestConfig).then(res => res.data)
}

export function del<T = any>(shortURL: string): Promise<T> {
    return instance.delete<T>(shortURL).then(res => res.data)
}
