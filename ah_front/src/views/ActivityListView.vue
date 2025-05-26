<template>
  <div class="activity-list-container">
    <el-card>
      <!-- 搜索框 -->
      <div class="search-wrapper">
        <el-input
            v-model="searchInput"
            placeholder="请输入活动名称搜索"
            clearable
            @clear="handleSearchClear"
            @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>

      <!-- 活动列表表格 -->
      <el-table
          :data="activityStore.activities"
          style="width: 100%"
          @row-click="handleRowClick"
          :header-cell-style="{ 'background-color': '#f5f7fa', 'color': '#606266' }"
          :cell-style="{ 'padding': '14px' }"
      >
        <el-table-column prop="name" label="活动名称" min-width="200" />
        <el-table-column prop="type" label="活动类型" width="120">
          <template #default="{ row }">
            {{ getActivityType(row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="currentParticipants" label="参与人数" width="160">
          <template #default="{ row }">
            {{ row.currentParticipants }}/{{ row.maxParticipants || '不限' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" @click.stop="navigateToDetail(row.id)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
            v-model:current-page="activityStore.currentPage"
            v-model:page-size="activityStore.pageSize"
            :total="activityStore.total"
            layout="prev, pager, next, jumper"
            @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useActivityStore } from '@/stores/activity';
import { useRouter } from 'vue-router';
import { Search } from '@element-plus/icons-vue';

const router = useRouter();
const activityStore = useActivityStore();
const searchInput = ref(activityStore.searchName);

// 监听搜索输入变化
watch(
    () => activityStore.searchName,
    (newVal) => {
      searchInput.value = newVal;
    }
);

// 搜索处理
const handleSearch = () => {
  activityStore.fetchActivities(1, activityStore.pageSize, searchInput.value);
};

// 清除搜索
const handleSearchClear = () => {
  searchInput.value = '';
  handleSearch();
};

// 分页处理
const handlePageChange = (page: number) => {
  activityStore.fetchActivities(page, activityStore.pageSize, searchInput.value);
};

// 其他方法保持不变...
const getActivityType = (type: number) => {
  return ['大转盘', '立即开奖', '福袋'][type] || '未知';
};

const getStatusText = (status: number) => {
  return [ '已关闭','未开始', '进行中', '已结束',][status] || '未知';
};

const getStatusTagType = (status: number) => {
  return ['warning','info', 'success', 'danger' ][status] || '';
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString();
};

const handleRowClick = (row: any) => {
  navigateToDetail(row.id);
};

const navigateToDetail = (activityId: number) => {
  router.push({ name: 'ActivityDetailView', params: { id: activityId } });
};

// 初始化加载数据
activityStore.fetchActivities();
</script>

<style scoped>
.activity-list-container {
  padding: 30px;
}

.search-wrapper {
  margin-bottom: 30px;
  width: 300px;
}

.pagination-wrapper {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.el-table {
  font-size: 14px;
}

.el-table th,
.el-table td {
  text-align: center;
}
</style>
