<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {postForm, post} from '@/api/axios'
import type {CommonResponse, ResultData} from '@/api/api'
import {
  ElForm,
  ElFormItem,
  ElInput,
  ElSelect,
  ElOption,
  ElDatePicker,
  ElCheckbox,
  ElButton,
  ElCard,
  ElRow,
  ElCol,
  ElMessage
} from 'element-plus'
import type {Prize} from "@/stores/activity.ts";

// 活动数据模型
const activity = ref({
  name: '',
  type: '3', // 默认为福袋类型
  startTime: '',
  endTime: '',
  status: 1,
  ruleConfig: {
    backgroundImg: '',
    animationSpeed: 5,
    prizeList: [] as number[],
    allowShare: true,
    shareReward: 1
  },
  maxParticipants: 1000,
  currentParticipants: 0,
  autoClose: false
})

// 奖品列表
const prizes = ref([{
  prizeId: null,
  totalStock: 0,
  probability: '0.0000'
}])

// 活动限制条件
const activityRestriction = ref({
  limitType: '1',
  limitValue: 5,
  intervalSeconds: 86400
})

// 表单引用
const formRef = ref()

// 奖品列表数据
const prizeOptions = ref<Array<{ id: number, name: string }>>([])

// 获取奖品列表
const fetchPrizeList = async () => {
  try {
    const response = await post<ResultData<CommonResponse<Prize>>>('/draw/prize/selectPrize', {
      page: 1,
      limit: 10000,
      name: '',
    })
    if (response.data && response.code === '200') {
      prizeOptions.value = response.data.data.map(prize => ({
        id: prize.id,
        name: prize.name
      }))
    }
  } catch (error) {
    console.error('获取奖品列表失败:', error)
  }
}

// 在组件初始化时调用 fetchPrizeList
onMounted(() => {
  fetchPrizeList()
})

// 时间格式化函数
const formatTime = (isoDate: string | Date): string => {
  const date = new Date(isoDate)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}


// 提交表单
const submitForm = async () => {

  // 1. 校验活动基本信息
  if (!activity.value.name.trim()) {
    ElMessage.error('请输入活动名称')
    return
  }

  if (!activity.value.startTime) {
    ElMessage.error('请选择开始时间')
    return
  }

  if (!activity.value.endTime) {
    ElMessage.error('请选择结束时间')
    return
  }

  // 2. 大转盘配置校验（仅当 type === '1'）
  if (activity.value.type === '1') {
    if (!activity.value.ruleConfig.backgroundImg.trim()) {
      ElMessage.error('请输入背景图片链接')
      return
    }
  }

  // 奖品设置校验
  for (let i = 0; i < prizes.value.length; i++) {
    const prize = prizes.value[i]

    if (prize.prizeId === null || prize.prizeId === undefined || prize.prizeId === 0) {
      ElMessage.error(`请为第 ${i + 1} 个奖品选择奖品`)
      return
    }

    if (prize.totalStock <= 0) {
      ElMessage.error(`第 ${i + 1} 个奖品库存必须大于 0`)
      return
    }

    const probabilityNum = parseFloat(prize.probability)
    if (isNaN(probabilityNum) || probabilityNum < 0 || probabilityNum > 1) {
      ElMessage.error(`第 ${i + 1} 个奖品中奖概率必须在 0 到 1 之间`)
      return
    }
  }

  // 4. 活动限制校验
  if (!activityRestriction.value.limitType) {
    ElMessage.error('请选择限制类型')
    return
  }

  if (activityRestriction.value.limitValue < 1) {
    ElMessage.error('限制次数必须大于等于 1')
    return
  }

  if (
      activityRestriction.value.limitType === '3' &&
      activityRestriction.value.intervalSeconds < 1
  ) {
    ElMessage.error('间隔秒数必须大于等于 1')
    return
  }

  const requestData = {
    activity: {
      ...activity.value,
      startTime: formatTime(activity.value.startTime),
      endTime: formatTime(activity.value.endTime)
    },
    prizes: prizes.value,
    activityRestriction: activityRestriction.value
  }


  try {
    const response = await post('/draw/activity/addActivity', requestData)
    console.log('活动添加成功:', response)
    ElMessage.success('活动添加成功')
  } catch (error) {
    console.error('活动添加失败:', error)
    ElMessage.error('活动添加失败')
  }
}

// 添加新奖品
const addPrize = () => {
  prizes.value.push({prizeId: null, totalStock: 0, probability: '0.0000'})
}

// 删除奖品
const removePrize = (index: number) => {
  if (prizes.value.length > 1) {
    prizes.value.splice(index, 1)
  }
}
</script>

<template>
  <div class="launch-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>创建新活动</span>
        </div>
      </template>

      <el-form ref="formRef" label-position="right" label-width="130px" :model="activity">
        <!-- 活动基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动名称">
              <el-input v-model="activity.name"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="活动类型">
              <el-select v-model="activity.type" placeholder="请选择活动类型">
                <el-option label="大转盘" value="1"/>
                <el-option label="福袋" value="2"/>
                <el-option label="立即开奖" value="3"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间">
              <el-date-picker
                  v-model="activity.startTime"
                  type="datetime"
                  placeholder="选择开始时间"
                  style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-date-picker
                  v-model="activity.endTime"
                  type="datetime"
                  placeholder="选择结束时间"
                  style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 大转盘配置 -->
        <el-card v-if="activity.type === '1'" class="config-card">
          <template #header>
            <div class="card-header">
              <span>大转盘配置</span>
            </div>
          </template>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="背景图片链接">
                <el-input v-model="activity.ruleConfig.backgroundImg"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="动画速度等级">
                <el-input-number v-model="activity.ruleConfig.animationSpeed" :min="1" :max="10"/>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="奖品ID列表">
                <el-input
                    @change="() => {
                    activity.ruleConfig.prizeList = $event.target.value.split(',').map(Number)
                  }"
                    placeholder="请输入逗号分隔的奖品ID"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="允许分享增加机会">
                <el-checkbox v-model="activity.ruleConfig.allowShare"/>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="分享奖励次数">
                <el-input-number v-model="activity.ruleConfig.shareReward" :min="1"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 参与人数限制 -->
        <el-card class="config-card">
          <template #header>
            <div class="card-header">
              <span>参与设置</span>
            </div>
          </template>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="最大参与人数">
                <el-input-number v-model="activity.maxParticipants" :min="1" style="width: 100%"/>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="是否自动关闭">
                <el-checkbox v-model="activity.autoClose"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 奖品设置 -->
        <el-card class="config-card">
          <template #header>
            <div class="card-header">
              <span>奖品设置</span>
            </div>
          </template>

          <div v-for="(prize, index) in prizes" :key="index" class="prize-config">
            <el-card class="inner-card">
              <template #header>
                <div class="card-header">
                  <span>奖品 {{ index + 1 }}</span>
                  <el-button @click="removePrize(index)" text v-if="prizes.length > 1">删除奖品</el-button>
                </div>
              </template>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="奖品名称">
                    <el-select placeholder="请选择奖品" v-model="prize.prizeId">
                      <el-option
                          v-for="option in prizeOptions"
                          :key="option.id"
                          :label="option.name"
                          :value="option.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>

                <el-col :span="8">
                  <el-form-item label="库存数量">
                    <el-input-number v-model="prize.totalStock" :min="0" style="width: 100%"/>
                  </el-form-item>
                </el-col>

                <el-col :span="8">
                  <el-form-item label="中奖概率(0-1)">
                    <el-input-number v-model.number="prize.probability" :step="0.0001" :min="0" :max="1"
                                     style="width: 100%"/>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-card>
          </div>

          <el-button @click="addPrize" type="primary" plain>添加新奖品</el-button>
        </el-card>

        <!-- 活动限制 -->
        <el-card class="config-card">
          <template #header>
            <div class="card-header">
              <span>活动限制</span>
            </div>
          </template>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="限制类型">
                <el-select v-model="activityRestriction.limitType" placeholder="请选择限制类型">
                  <el-option label="总次数限制" value="1"/>
                  <el-option label="每日次数限制" value="2"/>
                  <el-option label="频率限制" value="3"/>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="限制次数">
                <el-input-number v-model="activityRestriction.limitValue" :min="1" style="width: 100%"/>
              </el-form-item>
            </el-col>

            <el-col :span="8" v-if="activityRestriction.limitType === '3'">
              <el-form-item label="间隔秒数">
                <el-input-number v-model="activityRestriction.intervalSeconds" :min="1" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitForm" style="width: 100%">提交创建活动</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.launch-container {
  padding: 20px;
}

.box-card {
  margin: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
}

.config-card {
  margin-top: 20px;
}

.prize-config {
  margin-bottom: 15px;
}

.inner-card {
  margin-bottom: 10px;
}
</style>