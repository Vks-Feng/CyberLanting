<template>
  <!-- 任务统计 -->
  <el-row :gutter="12" class="task-stats">
    <el-col :span="6">
      <el-card class="stat-card">
        <div class="stat-value">{{ objectiveCount || 0 }}</div>
        <div class="stat-label">总目标数</div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card class="stat-card">
        <div class="stat-value">{{ objectiveCompletedRate+"%"|| "0%" }}</div>
        <div class="stat-label">目标完成率</div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card class="stat-card">
        <div class="stat-value">{{ taskCount || 0 }}</div>
        <div class="stat-label">总任务数</div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card class="stat-card">
        <div class="stat-value">{{ taskCompletedRate + "%" || "0%" }}</div>
        <div class="stat-label">任务完成率</div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { ref, onMounted, watch, computed } from "vue";
import peerApi from "@/api/peer-api";

export default {
  props: {
    friendId: {
      type: [String, Number],
      required: true,
    },
  },
  setup(props) {
    const objectiveCount = ref(0);
    const taskCount = ref(0);
    const objectiveCompletedCount = ref(0);
    const taskDoneCompletedCount = ref(0);

    // 获取任务数据
    const getTaskStats = async () => {
      try {
        const response = await peerApi.getObjHubStatus(props.friendId);
        if (response) {
          objectiveCount.value = response.objectiveCount || 0;
          objectiveCompletedCount.value = response.objectiveCompletedCount || 0;
          taskCount.value = Number(response.taskCount) || 0;
          taskDoneCompletedCount.value = response.taskDoneCompletedCount || 0;
        }
      } catch (error) {
        console.error("获取任务数据失败:", error);
      }
    };

    //使用watch监听friendId变化，重新获取任务数据
    watch(
      () => props.friendId,
      async (newVal) => {
        if (newVal) {
          getTaskStats();
        }
      },
      { immediate: true }
    );

    // 计算目标完成率
    const objectiveCompletedRate = computed(() => {
      if (objectiveCount.value === 0) return 0; // 避免除以零
      return (
        (objectiveCompletedCount.value / objectiveCount.value) *100).toFixed(2);
    });

    // 计算任务完成率
    const taskCompletedRate = computed(() => {
      if (taskCount.value === 0) return 0; // 避免除以零
      console.log(taskDoneCompletedCount.value, taskCount.value);
      return ((taskDoneCompletedCount.value / taskCount.value) * 100).toFixed(2);
    });

    // 组件加载时调用 API 获取数据
    onMounted(() => {
      getTaskStats();
    });

    return {
      objectiveCount,
      objectiveCompletedRate,
      taskCount,
      taskCompletedRate,
    };
  },
};
</script>

<style scoped>
.task-stats {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
  padding: 15px;
  min-height: 80px;
  box-shadow: none;
  border-radius: 8px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #3A5F5A;
}

.stat-label {
  font-size: 12px;
  color: gray;
}
</style>
