// src/stores/activity.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import {get, post} from '@/api/axios';

export interface Activity {
    id: number;
    name: string;
    description: string;
    type: number;
    startTime: string;
    endTime: string;
    status: number;
    ruleConfig: any;
    maxParticipants?: number;
    currentParticipants: number;
    autoClose: boolean;
    prizes: Prize[];
    restriction: Restriction;
}

export interface Prize {
    id: number;
    prizeName: string;
    totalStock: string;
    usedStock: number;
    probability: number;
}

export interface Restriction {
    limitType?: number;
    limitValue?: number;
    intervalSeconds?: number;
}

export const useActivityStore = defineStore('activity', () => {
    const activities = ref<Activity[]>([]);
    const activityDetail = ref<Activity | null>(null);
    const total = ref(0);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const searchName = ref('');

    // 获取活动列表
    const fetchActivities = async (page = 1, size = 10, name = '') => {
        try {
            const response = await post('/draw/activity/selectActivityInfo', {
                name: name,
                page: page,
                limit: size
            });

            console.log(response)

            activities.value = response.data.data.map((item: any) => ({
                id: item.activity.id,
                name: item.activity.name,
                description: `活动类型: ${getActivityType(item.activity.type)} 
                             状态: ${getActivityStatus(item.activity.status)}  
                             参与人数: ${item.activity.currentParticipants}/${item.activity.maxParticipants || '不限'}`,
                type: item.activity.type,
                startTime: item.activity.startTime,
                endTime: item.activity.endTime,
                status: item.activity.status,
                ruleConfig: item.activity.ruleConfig,
                maxParticipants: item.activity.maxParticipants,
                currentParticipants: item.activity.currentParticipants,
                autoClose: item.activity.autoClose,
                prizes: item.prizes,
                restriction: item.restriction
            }));

            total.value = response.data.total;
            currentPage.value = page;
            pageSize.value = size;
            searchName.value = name;
        } catch (error) {
            console.error('Failed to fetch activities:', error);
        }
    };

    // 加载活动详情（优化版）
    const fetchActivityDetailById = async (activityId: number, forceRefresh = false) => {
        // 如果不需要强制刷新且已有缓存数据，则直接返回
        if (!forceRefresh) {
            const cachedActivity = activities.value.find(a => a.id === activityId);
            if (cachedActivity) {
                activityDetail.value = cachedActivity as Activity;
                return;
            }
        }

        try {
            // 1. 获取活动详情
            const res = await get('/draw/activity/fetchActivityDetailById', {
                id: activityId
            });

            // 2. 确保数据结构正确
            const activityData = res.data
            console.log(activityData)

            // 3. 更新活动详情
            activityDetail.value = {
                id: activityData.activity.id,
                name: activityData.activity.name,
                description: activityData.activity.description || '',
                type: activityData.activity.type,
                startTime: activityData.activity.startTime,
                endTime: activityData.activity.endTime,
                status: activityData.activity.status,
                ruleConfig: activityData.activity.ruleConfig || {},
                maxParticipants: activityData.activity.maxParticipants,
                currentParticipants: activityData.activity.currentParticipants,
                autoClose: activityData.activity.autoClose || false,
                prizes: activityData.prizes || [],
                restriction: activityData.restriction || {}
            };

            // 4. 更新活动列表中的对应项
            const index = activities.value.findIndex(a => a.id === activityId);
            if (index !== -1) {
                activities.value[index] = {
                    ...activities.value[index],
                    ...activityDetail.value,
                    // 保持原有的description格式
                    description: `活动类型: ${getActivityType(activityData.type)}  
                             状态: ${getActivityStatus(activityData.status)}  
                             参与人数: ${activityData.currentParticipants}/${activityData.maxParticipants || '不限'}`
                };
            }

        } catch (error) {
            console.error('Failed to fetch activity detail:', error);
        }
    };

    // 辅助方法
    const getActivityType = (type: number) => {
        const types = ['大转盘', '立即开奖', '福袋'];
        return types[type] || types[0];
    };

    const getActivityStatus = (status: number) => {
        const statuses = ['已关闭','未开始', '进行中', '已结束' ];
        return statuses[status] || statuses[0];
    };

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString();
    };

    return {
        activities,
        activityDetail,
        total,
        currentPage,
        pageSize,
        searchName,
        fetchActivities,
        fetchActivityDetailById
    };
});
