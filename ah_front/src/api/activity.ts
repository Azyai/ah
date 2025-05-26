// api/activity.ts
import { post } from '@/api/axios';

export async function getActivities(params: {
    name: string;
    page: number;
    limit: number;
}) {
    return post('/draw/activity/selectActivityInfo', params);
}

export async function getActivityDetail(activityId: number) {
    return post('/draw/activity/selectActivityInfo', { id: activityId });
}
