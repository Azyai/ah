// src/stores/activity.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { post } from '@/api/axios';

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

            activities.value = response.data.data.map((item: any) => ({
                id: item.activity.id,
                name: item.activity.name,
                description: `活动类型: ${getActivityType(item.activity.type)} | 
                             状态: ${getActivityStatus(item.activity.status)} | 
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

    // 获取活动详情
    const fetchActivityDetail = async (activityId: number) => {
        try {
            const response = await post('/draw/activity/selectActivityDetailById', {
                id: activityId
            });

            const item = response.data.data.find((a: any) => a.activity.id === activityId);
            if (item) {
                activityDetail.value = {
                    id: item.activity.id,
                    name: item.activity.name,
                    description: `活动类型: ${getActivityType(item.activity.type)} | 
                                 状态: ${getActivityStatus(item.activity.status)} | 
                                 时间: ${formatDate(item.activity.startTime)} 至 ${formatDate(item.activity.endTime)} | 
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
                };
            }
        } catch (error) {
            console.error('Failed to fetch activity detail:', error);
        }
    };

    // 辅助方法
    const getActivityType = (type: number) => {
        const types = ['未知', '大转盘', '其他'];
        return types[type] || types[0];
    };

    const getActivityStatus = (status: number) => {
        const statuses = ['未开始', '进行中', '已结束', '已关闭'];
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
        fetchActivityDetail
    };
});
