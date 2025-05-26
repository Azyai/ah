<template>
  <div class="home-container">

    <!-- 骨架屏 -->
    <el-skeleton :rows="10" animated v-if="loading"/>
    <template  v-else>
    <div class="content-wrapper" style="width: 538px;height: 400px;">

      <!-- 左侧轮播图 -->
      <div class="carousel-wrapper">
        <el-carousel trigger="click" :height="'400px'" style="width: 100%;height: 100%" :interval="5000">
          <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
            <div class="carousel-item">
              <h1>{{ item.title }}</h1>
              <p>{{ item.description }}</p>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 其他代码保持不变... -->
      <div class="activity-grid" style="padding-left: 10px">
        <div
            v-for="(activity, index) in displayedActivities"
            :key="activity.id"
            class="activity-item"
            @click="navigateToActivityDetail(activity.id)"
        >
          <h3>{{ activity.name }}</h3>
          <p style="white-space: pre-line">{{ activity.description }}</p>
          <div class="activity-meta">
<!--        <span class="participants">-->
<!--          {{ activity.currentParticipants }}/{{ activity.maxParticipants || '∞' }}-->
<!--        </span>-->
            <span class="status" :class="getStatusClass(activity.status)">
          {{ getStatusText(activity.status) }}
        </span>
          </div>
        </div>
      </div>
    </div>
    </template>


    <!-- 产品介绍模块 -->
    <section class="features-section">
      <div class="container">
        <h2 class="section-title">我们的产品</h2>
        <div class="features-grid">
          <el-card shadow="hover" v-for="(feature, index) in features" :key="index" class="feature-card">
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </el-card>
        </div>
      </div>
    </section>

    <!-- 公司简介 -->
    <section class="about-section">
      <div class="container">
        <h2>关于我们</h2>
        <p>我们是一家专注于智能抽奖活动策划与执行的数字化平台，致力于为企业、品牌及个人提供高效、公平、创意十足的互动营销解决方案。通过多样化的抽奖形式、精准的数据分析及一站式运营支持，我们帮助客户快速提升用户参与度、扩大品牌曝光，并实现高效引流转化。

          ✨ 为什么选择我们？
          ✅ 极简操作：3分钟创建专属抽奖活动，零技术门槛
          ✅ 丰富玩法：大转盘、砸金蛋、红包雨等20+趣味模板
          ✅ 安全可靠：区块链级防作弊机制，结果100%公平可查
          ✅ 深度赋能：从活动策划到数据复盘，全程陪跑增长

          已服务10,000+企业用户，累计生成500,000+场抽奖活动，我们是您值得信赖的互动营销伙伴！
        </p>
      </div>
    </section>

  </div>
</template>

<script setup lang="ts">
import { onMounted, ref,computed} from 'vue'
import { useRouter } from 'vue-router'
import { useActivityStore } from '@/stores/activity';
const activityStore = useActivityStore();

const router = useRouter()

// 加载状态
const loading = ref(true);

// 轮播图数据
const carouselItems = ref([
  {
    title: '欢迎来到 Cloud AH',
    description: '打造智能化云端解决方案'
  },
  {
    title: '高效 · 稳定 · 可靠',
    description: '我们致力于为企业提供最优质的云服务体验'
  }
])

const navigateToActivityDetail = (activityId: number) => {
  router.push({ name: 'ActivityDetailView', params: { id: activityId } });
};

// 计算属性：限制展示的活动数量为前6条
const displayedActivities = computed(() => {
  return activityStore.activities.slice(0, 6);
});

// 截断描述的方法
const truncatedDescription = (description: string) => {
  if (description.length > 32) {
    return description.slice(0, 32) + '...';
  }
  return description;
};

const getStatusText = (status: number) => {
  return ['已关闭','未开始', '进行中', '已结束', ][status] || '未知';
};

const getStatusClass = (status: number) => {
  return ['closed','not-started', 'ongoing', 'ended', ][status] || '';
};



onMounted(async () => {
  await activityStore.fetchActivities().finally(()=>{
        loading.value = false
      });
})


// 产品功能列表
const features = ref([
  {
    title: '操作简单，轻松上手',
    description: '平台界面简洁直观，用户无需复杂操作即可快速创建抽奖活动，降低使用门槛。'
  },
  {
    title: '多样化抽奖形式',
    description: '支持多种抽奖方式（如立即开奖、福袋抽奖等）满足不同场景需求，提升趣味性。'
  },
  {
    title: '安全公平，杜绝作弊',
    description: '采用智能算法确保抽奖过程公正透明，结果随机可验证，增强用户信任感。'
  },
  {
    title: '高效引流，快速涨粉',
    description: '通过抽奖活动吸引用户参与并转发分享，帮助品牌快速扩大曝光，提升粉丝增长。'
  },
  {
    title: '数据统计，精准分析',
    description: '提供详细的参与数据及用户行为分析，帮助运营者优化活动策略，提升转化效果。'
  },
  {
    title: '灵活定制，品牌专属',
    description: '支持自定义活动页面、奖品及规则，完美匹配品牌调性，强化品牌形象。'
  },
  {
    title: '多渠道推广覆盖',
    description: '一键同步至微信、微博、抖音等主流社交平台，最大化活动传播范围。'
  },
  {
    title: '7×24小时稳定支持',
    description: '专业服务器保障，高并发承载能力，确保活动全程流畅无卡顿，用户体验更佳。'
  }
])
</script>

<style scoped>
.home-container {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
}

.content-wrapper {
  display: flex;
  margin: 20px;
}

.carousel-wrapper {
  width: 538px !important;
  height: 400px !important;
  flex: none; /* 取消flex增长 */
}

.el-carousel__item {
  width: 100%;
  height: 100%;
}
.el-carousel {
  width: 100%;
  height: 100%;
}


.carousel-item {
  width: 100%;
  height: 100%;
  /* 保持原有其他样式 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: white;
  background: linear-gradient(135deg, #4ecdc4, #45b7d1);
}

.carousel-item h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.carousel-item p {
  font-size: 1.5rem;
}

.activities-wrapper {
  flex: 1;
}

.activity-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 20px;
}

.activity-item {
  border: 1px solid #ddd;
  padding: 20px;
  cursor: pointer;
  background-color: #fff;
  display: flex;
  width: 250px;
  height: 150px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.activity-item:hover {
  background-color: #f0f0f0;
}

.features-section {
  padding: 60px 20px;
  background-color: #ffffff;
}

.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
  font-size: 2rem;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  text-align: center;
  padding: 20px;
  height: 200px;
}

.feature-card h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
}


.feature-card p {
  text-align: left; /* 文本左对齐 */
  margin-top: 10px; /* 调整与标题的间距 */
  line-height: 1.6; /* 行高适中 */
  color: #666; /* 文字颜色 */
}

.about-section {
  padding: 60px 20px;
  background-color: #ffffff;
  border-top: 1px solid #eee;
}

.about-section h2 {
  text-align: center;
  margin-bottom: 20px;
}

.about-section p {
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
  font-size: 1rem;
  color: #666;
}
</style>
