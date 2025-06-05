<template>
    <div class="chart-wrapper">
      <div id="last7" class="chart-box"></div>
    </div>
  </template>

<script setup>
import * as echarts from 'echarts';
import { onMounted, onBeforeUnmount } from 'vue';
import analysisApi from '@/api/analysis-api.js';

let myChart = null;
let resizeObserver = null;

onMounted(async () => {
  const chartDom = document.getElementById('last7');
  if (!chartDom) return;

  myChart = echarts.init(chartDom);

  // 监听父容器大小变化，自动 resize
  resizeObserver = new ResizeObserver(() => {
    if (myChart) {
      myChart.resize();
    }
  });
  resizeObserver.observe(chartDom);

  try {
    const response = await analysisApi.getLast7daysTaskCompletionRate();
    console.log("近7日任务完成率走势:", response);

    // 实际使用中发现调整为任务数观感和效果更好
    let dates = response.map(item => item.date).reverse();
    let completionRates = response.map(item => item.completeCount).reverse();

    const option = {
      color: ['#8DA4A9'],
      title: {
        text: '近7日任务完成数走势',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      grid: {
        left: '15%',   // 给 Y 轴留出空间，防止数字被截断
        right: '15%',
        top: '15%',
        bottom: '10%'
    },
      xAxis: {
        type: 'category',
        data: dates
      },
      yAxis: {
        type: 'value',
        minInterval: 1, 
        axisLabel: {
          formatter: '{value}'
        }
      },
      series: [
        {
          name: '任务完成数',
          type: 'line',
          data: completionRates,
        //   markPoint: {
        //     data: [
        //       { type: 'max', name: 'Max' },
        //       { type: 'min', name: 'Min' }
        //     ]
        //   },
        //   markLine: {
        //     data: [
        //       { type: 'average', name: 'Avg' }
        //     ]
        //   }
        }
      ]
    };

    myChart.setOption(option);
  } catch (error) {
    console.error('Error fetching task completion rate data:', error);
  }
});

onBeforeUnmount(() => {
  if (resizeObserver && myChart) {
    resizeObserver.disconnect();
  }
});
</script>


<style scoped>
.chart-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
.chart-box {
  width: 100%;
  height: 100%;
}
</style>