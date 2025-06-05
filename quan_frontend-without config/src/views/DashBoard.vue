<template>
  <div class="main-container">

    <!-- row1:分为三列 -->
    <el-row :gutter="20" style="height: 40%; margin-bottom: 20px">

      <!-- col1:左侧环形图-->
      <el-col :span="6">
        <div class="chart">
          <Pie />
        </div>
      </el-col>

      <!-- col2:中间今日任务列表 -->
      <el-col :span="12">
        <div class="task-list">
          <h2 class="task-list-title">📅 今日任务列表</h2>
          <div class="task-scroll">
            <TodayTaskPage />
            <el-footer style="text-align: center; margin-top: 10px">
            </el-footer>
          </div>
        </div>
      </el-col>

      <!-- col3:右侧折线图进度 -->
      <el-col :span="6">
        <div class="line-chart" >
          <Last7daysTaskCompeletion />
        </div>
      </el-col>
    </el-row>

    <!-- row2：分为三列 -->
    <el-row :gutter="20" style="height: 50%">
      <!-- col1:左侧目标进度-->
      <el-col :span="9">
        <TaskProgress />
      </el-col>

      <!-- col2:中间好友动态+资源概览（上下） -->
      <el-col :span="9">
        <el-row :gutter="20" style="height: 200px; margin-bottom: 20px">
          <el-col :span="24">
            <div class="friend-dynamic" style="">
              <PeerFeed />
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <div class="resource-overview">
              <ResOverview />
            </div>
          </el-col>
        </el-row>
      </el-col>

      <!-- col3:右侧排行榜 -->
      <el-col :span="6">
        <div class="ranking" style="">
          <Ranking />
        </div>
      </el-col>

    </el-row>

  </div>
</template>

<script lang="ts" setup>
import TodayTaskPage from "@/components/ObjHub/TodayTaskPage.vue";
import { onMounted } from "vue";
import objhubApi from "@/api/objhub-api";
import { getUserInfo } from "@/api/userinfo-api";
import Last7daysTaskCompeletion from "@/components/Analysis/Last7daysTaskCompeletion.vue";
import Pie from "@/components/Dashboard/Pie.vue";
import TaskProgress from "@/components/Dashboard/TaskProgress.vue";
import PeerFeed from "@/components/Dashboard/PeerFeed.vue";
import ResOverview from "@/components/Dashboard/ResOverview.vue";
import Ranking from "@/components/Dashboard/Ranking.vue";


// 获取目标信息和任务信息，并存放到store中
onMounted(async () => {
  try {
    await objhubApi.getUserObjectives(getUserInfo().id);
    await objhubApi.getUserTodayTasks(getUserInfo().id);
  } catch (error) {
    console.log(error);
  }
});


</script>

<style scoped>
.main-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.el-row {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.el-col {
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart,
.task-list,
.progress-bar,
.line-chart,
.friend-dynamic,
.resource-overview,
.ranking {
  border: 1px solid #ccc;
  padding: 20px;
  box-sizing: border-box;
  backdrop-filter: blur(2px);
  /* 设置磨砂效果的强度 */
  background: rgba(255, 255, 255, 0.3);
  /* 半透明背景 */
  border-radius: 10px;
}

.task-list {
  overflow-y: hidden;
}

.task-list-title {
  font-size: 24px;
  font-weight: bold;
  border-bottom: 1px solid #ddd;
  margin: 0px 0px 10px 0px; 
}

.task-scroll {
  height: 100%;
  overflow-y: auto;
}

.chart {
  height: 330px;
}

.progress-bar {
  height: 430px;
}

.ranking {
  height: 430px;
}

.task-list {
  height: 330px;
}

.friend-dynamic {
  height: 200px;
}

.line-chart {
  height: 330px;
}

.resource-overview {
  height: 210px;
}
</style>
