<template>
    <div id="pie" style="width: 100%; height: 330px;"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { onMounted, watch, ref } from 'vue';
import store from '@/data/vuex-data';

const myChart = ref(null);
const finishTasksNum = ref(0);
const undoneTasksNum = ref(0);

const getRateText = () => {
  const total = finishTasksNum.value + undoneTasksNum.value;
  if (total === 0) return '暂无任务';
  const rate = ((finishTasksNum.value / total) * 100).toFixed(0);
  return `今日任务完成率\n${rate}%`;
};

const updateChart = () => {
  if (myChart.value) {
    myChart.value.setOption({
      series: [
        {
          label: {
            show: true,
            position: 'center',
            formatter: getRateText(),
            fontSize: 18,
            fontWeight: 'bold',
            color: '#333'
          },
          data: [
            { value: finishTasksNum.value, name: '完成任务' },
            { value: undoneTasksNum.value, name: '未完成任务' },
          ]
        }
      ]
    });
  }
};

watch(
  () => store.state.todayTaskList,
  (newTaskList) => {
    if (!Array.isArray(newTaskList)) {
      finishTasksNum.value = 0;
      undoneTasksNum.value = 0;
      updateChart();
      return;
    }

    finishTasksNum.value = newTaskList.filter(task => task.status === "completed").length;
    undoneTasksNum.value = newTaskList.filter(task => task.status === "pending").length;
    updateChart();
  },
  { immediate: true }
);


onMounted(() => {
  const chartDom = document.getElementById('pie');
  if (!chartDom) return;

  myChart.value = echarts.init(chartDom);

  const option = {
    color: ['#C67171', '#607B8B'],
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '任务完成情况',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'center',
          formatter: getRateText(),
          fontSize: 18,
          fontWeight: 'bold',
          color: '#333'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: finishTasksNum.value, name: '完成任务' },
          { value: undoneTasksNum.value, name: '未完成任务' },
        ]
      }
    ]
  };

  myChart.value.setOption(option);
});
</script>

<style scoped>
</style>
