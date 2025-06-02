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

// 后端定义的分页响应结构：对应 CommonResponse<T>
export interface CommonResponse<T = any> {
    data: T[];       // 当前页数据列表
    total: number;   // 总记录数
    current: number; // 当前页码
    size: number;    // 每页数量
}