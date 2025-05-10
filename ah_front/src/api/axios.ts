import axios from 'axios'
import type {
    AxiosInstance,
    AxiosResponse,
} from 'axios'

import { ElMessage } from 'element-plus'
import qs from 'qs'

// 引入自定义类型
import type { ResultData } from '@/api/api'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://127.0.0.1:8081'

// 创建 Axios 实例
const instance: AxiosInstance = axios.create({
    baseURL: BASE_URL,
    timeout: 20000,
    responseType: 'json',
})

instance.defaults.withCredentials = true

// 请求拦截器
// import { useUserStore } from '@/stores/user'
instance.interceptors.request.use(
    (config) => {
        // Do something before request is sent
        // 添加 token
        // config.headers.Authorization = getToken();
        // config 是一个配置对象，对象里面有一个属性很重要，就是header请求头

        // 示例：添加 token 到 header（可选）
        // config.headers.Authorization = getToken()
        // 必须返回 config，否则会报类型错误
        // const userStore = useUserStore()
        // if (userStore.token) {
        //     config.headers = {
        //         ...config.headers,
        //         Authorization: `Bearer ${userStore.token}`
        //     }
        // }
        const token = localStorage.getItem('Authorization')
        if(token){
            config.headers.Authorization =  `Bearer ${token}`;
        }
        console.log('请求参数',config)
        return config
    },
    (error) => {
        console.error('请求失败:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    <T = any>(response: AxiosResponse<ResultData<T>>) => {
        const res = response.data
        console.log(res)
        if (res.code == '200') {
            return Promise.resolve(res.data)
        }
        if (res.code == '401') {
            ElMessage.error(res.message || '请重新登录')
            localStorage.removeItem('Authorization')
        }

        ElMessage.error(res.message || '请求失败')
        return Promise.reject(new Error(res.message))

    },
    (error) => {
        console.error('网络异常:', error)
        ElMessage.error('网络异常，请检查连接')
        return Promise.reject(error)
    }
)



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

export function postForm<T = any>(
    shortURL: string,
    data?: Record<string, any>
): Promise<T> {
    return instance.post<T>(shortURL, qs.stringify(data), {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }).then(res => res.data)
}

export function del<T = any>(shortURL: string): Promise<T> {
    return instance.delete<T>(shortURL).then(res => res.data)
}
