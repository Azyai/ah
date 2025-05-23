<template>
  <div class="participation-container">
    <!-- 添加导出按钮 -->
    <el-button @click="exportToExcel">导出到 Excel</el-button>

    <!-- 表格展示数据 -->
    <el-table
        :data="participationList"
        show-overflow-tooltip
        style="width: 100%"
        height="485"
        class="custom-font-size"
    >
      <el-table-column type="index" label="序号" width="80" />
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
    <div class="demo-pagination-block">
      <el-pagination
          v-model:current-page="page"
          v-model:page-size="limit"
          :page-sizes="[30, 50, 80, 100]"
          :size="size"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { post } from '@/api/axios';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import type { ComponentSize } from 'element-plus';

// 分页数据
const page = ref(1); // 当前页码
const limit = ref(30); // 每页条数
const total = ref(0); // 总条数

// 分页组件参数
const size = ref<ComponentSize>('default');
const background = ref(true); // 启用背景色
const disabled = ref(false); // 禁用分页

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

// 每页数据量切换
const handleSizeChange = (newSize: number) => {
  limit.value = newSize;
  page.value = 1; // 重置为第一页
  fetchParticipation();
};

// 分页切换
const handleCurrentChange = (newPage: number) => {
  page.value = newPage;
  fetchParticipation();
};

// 挂载时加载数据
onMounted(() => {
  fetchParticipation();
});

// 导出到 Excel
const exportToExcel = () => {
  // 处理数据
  const data = participationList.value.map(item => ({
    序号: item.id,
    活动名称: item.activityName,
    参与时间: item.participationTime,
    IP地址: item.ip,
    是否中奖: item.isWinning ? '是' : '否',
  }));

  // 使用 xlsx 处理数据
  const worksheet = XLSX.utils.json_to_sheet(data);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');

  // 生成文件
  const excelBuffer = XLSX.write(workbook, {
    bookType: 'xlsx',
    type: 'array',
  });

  // 创建 Blob 对象
  const blob = new Blob([excelBuffer], {
    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8',
  });

  // 使用 file-saver 保存文件
  saveAs(blob, 'participation_list.xlsx');
};
</script>

<style scoped>
.participation-container {
  padding: 20px;
}

.demo-pagination-block {
  margin-top: 20px;
}

.demo-pagination-block .demonstration {
  margin-bottom: 16px;
  font-size: 14px;
  color: #909399;
}

/* 自定义表格字体大小 */
.custom-font-size ::v-deep .el-table__cell {
  font-size: 16px; /* 根据需要调整字体大小 */
}
</style>
