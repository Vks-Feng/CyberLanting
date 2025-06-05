<template>
  <div class="heatmap-container">
    <div style="display: table-column">
      <div class="date-selector">
        <el-button type="text" @click="changeDate('last-month')">上个月</el-button>
        <el-button type="text" @click="changeDate('next-month')">下个月</el-button>
      </div>
    </div>
    <div v-if="loading" class="loading-state">
      <el-skeleton style="width: 100%; height: 200px" animated />
    </div>
    <div v-else-if="error" class="error-state">
      <el-empty description="获取数据失败" :image-size="100">
        <template #description>
          <p>{{ errorMessage }}</p>
        </template>
        <!-- <el-button type="primary" @click="retryFetch">重试</el-button> -->
      </el-empty>
    </div>
    <div
      v-else
      ref="heatmapRef"
      style="width: 100%; height: 100%; aspect-ratio: 1; top: 10px; left: -10px"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, onUnmounted, watch } from "vue";
import * as echarts from "echarts";
import heatmapApi from "@/api/heatmap-api";
import { ElNotification } from "element-plus";

const heatmapRef = ref();
let chart: echarts.ECharts | null = null;
const loading = ref(false);
const error = ref(false);
const errorMessage = ref("");
const currentDate = ref(new Date());

// 切换月份
const changeDate = (type: "last-month" | "next-month") => {
  if (type === "last-month") {
    currentDate.value.setMonth(currentDate.value.getMonth() - 1);
  } else if (type === "next-month") {
    currentDate.value.setMonth(currentDate.value.getMonth() + 1);
  }
  initChart();
};

// 获取数据
const fetchData = async () => {
  loading.value = true;
  error.value = false;

  try {
    const year = currentDate.value.getFullYear();
    const month = currentDate.value.getMonth() + 1;

    const response = await heatmapApi.getHeatmapData(year, month);
    const heatmapData = [];

    if (Array.isArray(response)) {
      if (response.length === 0) {
        ElNotification({
          message: "暂无数据",
          type: "warning",
        });
      } else {
        heatmapData.push(...response.map((item) => [item.date, item.count]));
      }
    } else {
      ElNotification({
        message: "数据格式错误",
        type: "error",
      });
    }

    loading.value = false;
    return heatmapData;
  } catch (error) {
    loading.value = false;
    ElNotification({
      message: "获取数据失败",
      type: "error",
    });
    return [];
  }
};

// 初始化图表
const initChart = async () => {
  if (!heatmapRef.value) return;

  try {
    const data = await fetchData();

    if (chart) {
      chart.dispose();
    }

    chart = echarts.init(heatmapRef.value);

    const option = {
      title: {
        top: 0,
        left: "center",
        text: `${currentDate.value.getMonth() + 1}月任务完成情况`,
      },
      tooltip: {
        formatter: function (params) {
          if (params.value) {
            return `${params.value[0]} 完成了 ${params.value[1]} 个任务`;
          }
          return "无数据";
        },
      },
      visualMap: {
        min: 0,
        max: 10,
        type: "piecewise",
        orient: "horizontal",
        left: "center",
        top: 220,
        pieces: [
          { value: 0, color: "#f5f5f5" },
          { value: 1, color: "#e0f7fa" },
          { value: 2, color: "#b2ebf2" },
          { value: 3, color: "#80deea" },
          { value: 4, color: "#4dd0e1" },
          { value: 5, color: "#26c6da" },
          { value: 6, color: "#00bcd4" },
          { value: 7, color: "#00acc1" },
          { value: 8, color: "#0097a7" },
          { value: 9, color: "#00798c" },
        ],
      },
      calendar: {
        top: 50,
        left: 10,
        right: 10,
        cellSize: [15, 15], // Set small square cells
        range: `${currentDate.value.getFullYear()}-${
          currentDate.value.getMonth() + 1
        }`,
        itemStyle: {
          borderWidth: 1,
          borderColor: "#607B8B", // Use the ink-blue color
        },
        yearLabel: { show: false },
        monthLabel: { show: false },
        dayLabel: {
          firstDay: 1,
          nameMap: "cn",
        },
      },
      series: {
        type: "heatmap",
        coordinateSystem: "calendar",
        data: data,
      },
    };

    chart.setOption(option);
  } catch (err) {
    ElNotification({
      message: "初始化图表失败",
      type: "error",
    });
    error.value = true;
    errorMessage.value = "初始化图表失败";
  }
};

onMounted(() => {
  initChart();
});

onUnmounted(() => {
  chart?.dispose();
});
</script>

<style scoped>
.heatmap-container {
  padding: 20px;
  top: 150px;
  background-color: transparent !important;
  display: flex;
}

.loading-state,
.error-state {
  width: 100%;
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.error-state p {
  color: var(--el-text-color-secondary);
  margin-bottom: 16px;
}

.date-selector {
  display: flex;
  align-items: center;
}
</style>
