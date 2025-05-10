// 响应数据统一结构
export interface ReturnCodeEnum {
    code: string
    message: string
}

export interface ResultData<T = any> {
    code: string
    message: string
    data: T
    timestamp: number
}
