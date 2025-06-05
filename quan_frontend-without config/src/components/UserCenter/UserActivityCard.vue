<template>
              <el-card class="info-card">
            <h3>行迹录</h3>
            <p>任务完成数: {{ taskCompletedCount }}</p>
            <p>目标完成数: {{ objectiveCompletedCount }}</p>
            <p>资源分享数: {{ resourcePostedCount }}</p>
          </el-card>
</template>

<script setup>
import analysisApi from "@/api/analysis-api";
import { ref, onMounted } from "vue";

// 模拟数据
const taskCompletedCount = ref(0);
const objectiveCompletedCount = ref(0);
const resourcePostedCount = ref(0);
const likes = ref(0);
const comments = ref(0);
const favorites = ref(0);



//获取card数据
onMounted(async () => {
  //获取活跃度信息
  const response = await analysisApi.getUserActivity();
  taskCompletedCount.value = response.taskCompletedCount;
  objectiveCompletedCount.value = response.objectiveCompletedCount;
  resourcePostedCount.value = response.resourcePostedCount;

  //获取社交成就信息
  const socialResponse = await analysisApi.getUserSocialAchievement();
  likes.value = socialResponse.receivedLikes; //会报错，我先注释
  comments.value = socialResponse.receivedComments;
  favorites.value = socialResponse.receivedFavorites;
});
</script>