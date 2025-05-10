import axios from "axios";
// import { getToken } from "./localStorage";

// 引入进度条
const BASE_URL = "http://127.0.0.1:80";

const instance = axios.create({
    baseURL: BASE_URL,
    timeout: 20000,
    responseType: "json",
});
instance.defaults.withCredentials = true

/**
 * Add a request interceptor
 * 全局请求拦截
 */
instance.interceptors.request.use(
    // config: 请求发送前的配置对象
    (config) =>{
        // Do something before request is sent
        // 添加 token
        // config.headers.Authorization = getToken();
        // config 是一个配置对象，对象里面有一个属性很重要，就是header请求头
        console.log('请求参数',config)
        return config;
    },
    function (error) {
        // Do something with request error
        console.log(error);
        return Promise.reject(error);
    }
);

/**
 * Add a response interceptor
 * 全局响应拦截
 */
instance.interceptors.response.use(
    (response) =>{
        // Any status code that lie within the range of 2xx cause this function to trigger
        // Do something with response data
        // 成功的回调函数，服务器响应的数据回来以后，响应拦截器可以检测到，可以做一些事情
        return response.data;
    },
    function (error) {
        // Any status codes that falls outside the range of 2xx cause this function to trigger
        // Do something with response error
        console.log(error)
        return Promise.reject(error);
    }
);

// 第一个是后缀接口地址  第二个是一个对象ref
export function get(shortURL, params?) {
    return instance.get(BASE_URL + shortURL, {
        withCredentials: true,
        params,
    });
}
// 第二个是一个复杂对象 用ref也能够实现
export function post(shortURL, data) {
    return instance.post(BASE_URL + shortURL, data,{
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
    });
}
export function postForm(shortURL, data) {
    return instance.post(BASE_URL + shortURL, data,{
        headers: {
            "Content-Type": 'application/x-www-form-urlencoded'
        },
    });
}

// 封装delete请求
export function del(shortURL) {
    return instance.delete(BASE_URL + shortURL);
}