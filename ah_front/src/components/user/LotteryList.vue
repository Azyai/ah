<template>
  <div class="participation-container">
    <!-- 表格展示数据 -->
    <el-table :data="participationList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" />
      <el-table-column prop="activityName" label="活动名称" />
      <el-table-column prop="participationTime" label="参与时间" />
      <el-table-column prop="ip" label="IP地址" />
      <el-table-column prop="isWinning" label="是否中奖">
        <template #default="scope">
          <el-tag :type="scope.row.isWinning ? 'success' : 'info'">
            {{ scope.row.isWinning ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="limit"
        :current-page="page"
        @current-change="handlePageChange"
    />
  </div>
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { post } from '@/api/axios';

// 分页数据
const page = ref(1); // 当前页码
const limit = ref(10); // 每页条数
const total = ref(0); // 总条数

// 参与活动列表
const participationList = ref([]);

// 获取参与活动信息
const fetchParticipation = async () => {
  try {
    const response = await post<{
      code: string;
      data: {
        list: any[];
        total: number;
      };
    }>('/draw/participation/getParticipation', {
      page: page.value,
      limit: limit.value,
    });

    console.log('获取数据成功:', response)

    if (response.code === '200') {
      participationList.value = response.data.data;
      total.value = response.data.total;
    } else {
      console.error('获取数据失败:', response);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
};

// 分页切换
const handlePageChange = (newPage: number) => {
  page.value = newPage;
  fetchParticipation();
};

// 挂载时加载数据
onMounted(() => {
  fetchParticipation();
});
</script>

<style scoped>
.participation-container {
  padding: 20px;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
