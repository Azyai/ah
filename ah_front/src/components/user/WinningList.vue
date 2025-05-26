<template>
  <div class="participation-container">
    <!-- 骨架屏 -->
    <el-skeleton :rows="10" animated v-if="loading"/>

    <template v-else>
    <!-- 添加导出按钮 -->
    <el-button @click="exportToExcel">导出到 Excel</el-button>

    <!-- 表格展示数据 -->
    <el-table
        :data="WinningList"
        show-overflow-tooltip    style="width: 100%"
        height="485"
        class="custom-font-size"
    >
      <el-table-column type="index" label="序号" width="80" />
      <el-table-column prop="userName" label="用户名" />
      <el-table-column prop="activityName" label="活动名称" />
      <el-table-column prop="prizeName" label="奖品名称" />
      <el-table-column prop="winningTime" label="中奖时间" />
      <el-table-column prop="status" label="状态">

        <template #default="scope">
          <!-- 状态为1时代表未发放，2为已发放，3为发放失败-->
          <!--使用el-tag实现-->
          <el-tag v-if="scope.row.status === 1" type="info">未发放</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success">已发放</el-tag>
          <el-tag v-else type="danger">发放失败</el-tag>
        </template>

      </el-table-column>
      <el-table-column prop="mqMsgId" label="消息ID" />
      <el-table-column prop="participationId" label="参与ID" />
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
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { post } from '@/api/axios';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import {type ComponentSize, ElMessage} from 'element-plus';

// 分页数据
const page = ref(1); // 当前页码
const limit = ref(30); // 每页条数
const total = ref(0); // 总条数

// 分页组件参数
const size = ref<ComponentSize>('default');
const background = ref(true); // 启用背景色
const disabled = ref(false); // 禁用分页

// 加载状态
const loading = ref(true);

// 参与活动列表
const WinningList = ref([]);

// 获取参与活动信息
const fetchWinningRecord = async (retryCount = 3) => {
  try {
    const response = await post<{
      code: string;
      data: {
        data: any[];
        total: number;
      };
    }>('/draw/winningRecord/getWinningRecord', {
      page: page.value,
      limit: limit.value,
    });

    if (response.code === '200') {
      WinningList.value = response.data.data;

      console.log(WinningList.value)

      total.value = response.data.total;

    } else {
      console.error('获取数据失败:', response);
    }
  } catch (error) {
    if(retryCount > 0){
      console.error(`请求失败,剩余重试次数：${retryCount}`);
      await new Promise((resolve) => setTimeout(resolve, 1000)); // 延迟 1 秒重试
      fetchWinningRecord(retryCount - 1);
    }else {
      console.error('获取数据失败:', error);
    }
  }finally {
    loading.value = false
  }
};

// 每页数据量切换
const handleSizeChange = (newSize: number) => {
  limit.value = newSize;
  page.value = 1; // 重置为第一页
  fetchWinningRecord();
};

// 分页切换
const handleCurrentChange = (newPage: number) => {
  page.value = newPage;
  fetchWinningRecord();
};

// 挂载时加载数据
onMounted(() => {
  fetchWinningRecord();
});

const exportToExcel = () => {
  // 处理数据
  const data = WinningList.value.map(item => ({
    用户名: item.userName,
    活动名称: item.activityName,
    奖品名称: item.prizeName,
    中奖时间: item.winningTime,
    状态: item.status === 1 ? '已领取' : '未领取',
    消息ID: item.mqMsgId,
    参与ID: item.participationId,
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
  saveAs(blob, 'WinningList.xlsx');
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
