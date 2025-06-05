<!-- 年度任务完成情况热力图 -->
<template>
  <div class="heatmap-container">
<div class="heatmap-wrapper">
    <div v-if="loading" class="loading-state">
      <el-skeleton style="width: 100%; height: 200px" animated />
    </div>
    <div v-else-if="error" class="error-state">
      <el-empty description="获取数据失败" :image-size="100">
        <template #description>
          <p>{{ errorMessage }}</p>
        </template>
      </el-empty>
    </div>


    <div v-else ref="heatmapRef" class="heatmap" ></div>
  
    <div class="wrapper">
      <div class="date-selector">
        <el-button type="text" @click="changeDate('last-year')" class="ink-button">上一年</el-button>
        <el-button type="text" @click="changeDate('next-year')" class="ink-button">下一年</el-button>
      </div>
    </div>
  </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, onUnmounted } from "vue";
import * as echarts from "echarts";
import heatmapApi from "@/api/heatmap-api";
import { ElNotification } from "element-plus";

const heatmapRef = ref();
let chart: echarts.ECharts | null = null;
const loading = ref(false);
const error = ref(false);
const errorMessage = ref("");
const currentDate = ref(new Date());

// 切换年份
const changeDate = (type: "last-year" | "next-year") => {
  if (type === "last-year") {
    currentDate.value.setFullYear(currentDate.value.getFullYear() - 1);
  } else if (type === "next-year") {
    currentDate.value.setFullYear(currentDate.value.getFullYear() + 1);
  }
  initChart();
};

// 获取数据
const fetchData = async () => {
  loading.value = true;
  error.value = false;

  try {
    const year = currentDate.value.getFullYear();

    //获取年度数据
    const response = await heatmapApi.getHeatmapData(year);
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
        text: `${currentDate.value.getFullYear()}年度任务完成情况`,
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
        max: 20,
        type: "piecewise",
        orient: "horizontal",
        left: "center",
        top: 30,
        pieces: [
          // { value: 0, color: "#f5f5f5" },
          // {value:0,color:'transparent'},
          // { value: 1, color: "#e0f7fa" },
          // { value: 2, color: "#b2ebf2" },
          // { value: 3, color: "#80deea" },
          // { value: 4, color: "#4dd0e1" },
          // { value: 5, color: "#26c6da" },
          // { value: 6, color: "#00bcd4" },
          // { value: 7, color: "#00acc1" },
          // { value: 8, color: "#0097a7" },
          // { value: 10, color: "#00798c" },
          { value: 0, color: "transparent" },
          { min: 1, max: 2, color: "#c0d6cd" },
          { min: 3, max: 4, color: "#a0bfb3" },
          { min: 5, max: 6, color: "#80998f" },
          { min: 7, max: 8, color: "#4f6f6f" },
          { min: 9, max: 10, color: "#2f4f4f" },
        ],
      },
      calendar: {
        top: 90,
        left: 50,
        right: 10,
        cellSize: ['auto', 13], // Set small square cells
        //设置数据显示范围
        range: `${currentDate.value.getFullYear()}`,
        // 单元格样式
        itemStyle: {
          borderWidth: 0.5,
          color: "transparent",
          // borderColor: "#607B8B", // Use the ink-blue color
        },
        yearLabel: { show: false },
        // monthLabel: { show: false },
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
  padding: 10px;
  background-color: transparent !important;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.loading-state,
.error-state {
  width: 100%;
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.heatmap {
  width: 100%;
  aspect-ratio: 1;
  position: relative;
}

.heatmap-wrapper {
  width: 100%;
  max-width: 600px; 
  display: flex;
  flex-direction: column;
  align-items: center;
}

.error-state p {
  color: var(--el-text-color-secondary);
  margin-bottom: 16px;
}

.wrapper{
  display: flex;      /* 使用 flex 布局 */
  justify-content: center; /* 居中对齐按钮 */
  gap: 10px;          /* 控制按钮之间的间距 */
  margin-top: -400px;

}

.date-selector {
  display: flex;
  /* justify-content: center; */
  gap: 12px;
}
</style>