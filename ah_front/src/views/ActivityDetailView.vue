<template>
  <div class="activity-detail-container">
    <el-card v-if="activityDetail">
      <h2>{{ activityDetail.name }}</h2>

      <el-divider />

      <!-- 抽奖按钮区域 -->
      <div class="draw-button-container">
        <el-button
            class="draw-button"
            type="primary"
            :disabled="activityDetail.status !== 2"
            @click="handleDraw"
        >
          <span class="button-text">立即抽奖</span>
          <span class="button-icon">🎁</span>
        </el-button>
        <p v-if="activityDetail.status !== 2" class="draw-tip">
          {{ getStatusText(activityDetail.status) }}的活动不可参与
        </p>
      </div>

      <h3>活动信息</h3>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动类型">
          {{ getActivityType(activityDetail.type) }}
        </el-descriptions-item>
        <el-descriptions-item label="活动状态">
          <el-tag :type="getStatusTagType(activityDetail.status)">
            {{ getStatusText(activityDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">
          {{ formatDate(activityDetail.startTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="结束时间">
          {{ formatDate(activityDetail.endTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="参与人数">
          {{ activityDetail.currentParticipants }}/{{ activityDetail.maxParticipants || '不限' }}
        </el-descriptions-item>
        <el-descriptions-item label="自动关闭">
          {{ activityDetail.autoClose ? '是' : '否' }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <h3>奖品信息</h3>
      <el-table :data="activityDetail.prizes" style="width: 100%">
        <el-table-column prop="prizeName" label="奖品名称" width="180" />
        <el-table-column prop="totalStock" label="总库存" width="100" />
        <el-table-column prop="usedStock" label="已使用" width="100" />
        <el-table-column prop="probability" label="中奖概率" width="120">
          <template #default="{ row }">
            {{ (row.probability * 100).toFixed(2) }}%
          </template>
        </el-table-column>
      </el-table>

      <el-divider />

      <h3>活动限制</h3>
      <el-descriptions :column="1" border v-if="activityDetail.restriction.limitType">
        <el-descriptions-item label="限制类型">
          {{ getLimitTypeText(activityDetail.restriction.limitType) }}
        </el-descriptions-item>
        <el-descriptions-item label="限制值">
          {{ activityDetail.restriction.limitValue }}
        </el-descriptions-item>
        <el-descriptions-item label="间隔时间(秒)">
          {{ activityDetail.restriction.intervalSeconds }}
        </el-descriptions-item>
      </el-descriptions>
      <p v-else>无限制</p>
    </el-card>
    <el-empty v-else description="活动详情加载中..." />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted,ref } from 'vue';
import { useRoute } from 'vue-router';
import { useActivityStore } from '@/stores/activity';
import {ElMessage} from 'element-plus'
import {post} from "@/api/axios.ts";
import {useUserStore} from "@/stores/counter.ts";

const route = useRoute();
const userStore = useUserStore();
const activityStore = useActivityStore();

// 先从store中查找已有数据
const activityDetail = computed(() => {
  const activityId = Number(route.params.id);
  // 先在详情缓存中查找
  if (activityStore.activityDetail?.id === activityId) {
    return activityStore.activityDetail;
  }
  // 再从活动列表中查找
  return activityStore.activities.find(a => a.id === activityId) || null;
});

const getActivityType = (type: number) => {
  return ['未知', '大转盘', '其他'][type] || '未知';
};

const getStatusText = (status: number) => {
  return ['已关闭','未开始', '进行中', '已结束', ][status] || '未知';
};

const getStatusTagType = (status: number) => {
  return ['warning','info', 'success', 'danger', ][status] || '';
};

const getLimitTypeText = (type: number) => {
  return ['', '每日限制', '总限制'][type] || '未知';
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const deviceFingerprint = ref(''); // 设备指纹，实际项目中需要获取真实值

// 获取设备指纹（简化版，实际项目中需要更复杂的实现）
const getDeviceFingerprint = () => {
  // 这里应该是一个获取设备指纹的真实实现
  // 示例中使用简单随机字符串代替
  return 'fp_' + Math.random().toString(36).substring(2, 15);
};

// 参与抽奖
const handleDraw = async () => {
  try {
    if (!userStore.userInfo?.id) {
      ElMessage.warning('请先登录');
      return;
    }

    const activityId = Number(route.params.id);

    const response = await post('/draw/participation/participate', {
      activityId: activityId,
      deviceFingerprint: deviceFingerprint.value || getDeviceFingerprint()
    });

    if (response.code === '200') {
      ElMessage({
        message: response.data || '参与抽奖成功',
        type: 'success',
        duration: 5000
      });

      // 关键修改：抽奖成功后重新获取活动详情数据
      await activityStore.fetchActivityDetailById(activityId,true);

      // 可以在这里添加抽奖结果展示逻辑
    } else {
      ElMessage.error(response.message || '参与抽奖失败');
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试');
    console.error('参与抽奖失败:', error);
  }
};

onMounted(async () => {
  deviceFingerprint.value = getDeviceFingerprint();
  const activityId = Number(route.params.id);
  // 只有当没有缓存数据时才请求接口
  if (!activityDetail.value) {
    await activityStore.fetchActivityDetailById(activityId);
  }
});
</script>

<style scoped>
.activity-detail-container {
  padding: 20px;
}
</style>
