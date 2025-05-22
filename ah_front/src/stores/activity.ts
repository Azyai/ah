// src/stores/activity.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getActivities, getActivityDetail } from '@/api/activity';

export interface Activity {
    id: number;
    name: string;
    description: string;
}

export interface ActivityDetail extends Activity {
    prizes: {
        id: number;
        name: string;
        description: string;
    }[];
    restrictions: {
        id: number;
        limitType: number;
        limitValue: number;
        intervalSeconds: number;
    }[];
}

// src/stores/activity.ts
export const useActivityStore = defineStore('activity', () => {
    // 活动列表
    const activities = ref<Activity[]>([]);

    // 活动详情
    const activityDetail = ref<ActivityDetail | null>(null);

    // 加载活动列表
    const fetchActivities = async () => {
        try {
            const data = await getActivities();
            activities.value = data;
        } catch (error) {
            console.error('Failed to fetch activities:', error);
        }
    };

    // 加载活动详情
    const fetchActivityDetail = async (activityId: number) => {
        try {
            const data = await getActivityDetail(activityId);
            activityDetail.value = data;
        } catch (error) {
            console.error('Failed to fetch activity detail:', error);
        }
    };

    // 根据 id 获取活动详情
    const getActivityById = (activityId: number) => {
        return activities.value.find(activity => activity.id === activityId) || activityDetail.value;
    };

    return {
        activities,
        activityDetail,
        fetchActivities,
        fetchActivityDetail,
        getActivityById,
    };
});