<template>
  <div class="activity-detail-container">
    <el-card v-if="activityDetail">
      <h2>{{ activityDetail.name }}</h2>
      <p>{{ activityDetail.description }}</p>
      <h3>奖品信息</h3>
      <ul>
        <li v-for="prize in activityDetail.prizes" :key="prize.id">
          {{ prize.name }} - {{ prize.description }}
        </li>
      </ul>
      <h3>活动限制</h3>
      <ul>
        <li v-for="restriction in activityDetail.restrictions" :key="restriction.id">
          类型: {{ restriction.limitType }}, 限制值: {{ restriction.limitValue }}, 间隔秒数: {{ restriction.intervalSeconds }}
        </li>
      </ul>
    </el-card>
    <el-empty v-else description="活动详情加载中..." />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useActivityStore } from '@/stores/activity';

const route = useRoute();
const activityStore = useActivityStore();

const activityId = Number(route.params.id);

// 从仓库中获取活动详情
const activityDetail = computed(() => {
  return activityStore.getActivityById(activityId);
});

onMounted(async () => {
  const activityId = Number(route.params.id); // 确保正确获取 id
  console.log("activityId:  " + activityId)
  await activityStore.fetchActivityDetail(activityId); // 从仓库获取活动详情
});
</script>

<style scoped>
.activity-detail-container {
  padding: 20px;
}
</style>
