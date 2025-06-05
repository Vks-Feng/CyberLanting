<template>
  <h1>PeerRank</h1>
  <div class="leaderboard">
    <!-- 好友任务数月排 -->
    <div class="task-month-rank">
      <h2>好友任务排名</h2>
      <div ref="taskMonthChart" class="bar-chart"></div>
      <div class="button-group">
        <button class="ink-button" @click="changeRank('taskMonth', 'month')">月度</button>
        <button class="ink-button" @click="changeRank('taskMonth', 'week')">周度</button>
      </div>
    </div>

    <!-- 好友目标数月排 -->
    <!-- <div class="target-month-rank">
      <h2>好友目标排名</h2>
      <div ref="obMonthChart" class="bar-chart"></div>
      <div>
        <button @click="changeRank('obMonth', 'month')">月度</button>
        <button @click="changeRank('obMonth', 'week')">周度</button>
      </div>
    </div> -->
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import * as echarts from 'echarts';
import { colProps, ElNotification } from 'element-plus';
import peerApi from '@/api/peer-api';


const taskMonthChart = ref();
const obMonthChart = ref();
const currentTaskRank = ref('month'); // 默认选择月度任务排行
// const currentObRank = ref('month'); // 默认选择月度目标排行

// 获取数据函数
const fetchData = async (type, timeFrame) => {
  try {
    const url = timeFrame === 'month' ? `monthly` : `weekly`;

    //获取任务排行榜信息
    const response = await peerApi.getTaskRankList(url);
    console.log("获取排行榜信息成功");
    const data = response;

    if (Array.isArray(data)) {
      if (data.length === 0) {
        ElNotification({
          message: '暂无数据',
          type: 'warning',
        });
        console.log('no data');
      }
      return data.map(item => ({
        name: item.username,
        value: item.completeCount, 
      }));
    } else {
      ElNotification({
        message: '数据格式错误',
        type: 'error',
      });
      console.log('data format error', data);
      return [];
    }
  } catch (error) {
    ElNotification({
      message: '获取数据失败',
      type: 'error',
    });
    console.log('fetch data error', error);
    return [];

  }
};

// 更新图表
// 返回数据有序
const updateChart = async (chartRef, rankType, timeFrame) => {
  const data = await fetchData(rankType, timeFrame);
  if (!data.length) return;

  const chart = echarts.init(chartRef.value);

  let yMax = 400;

  let dataShadow = [];
  for(let i = 0; i < data.length; i ++){
    dataShadow.push(yMax);
  }

  let dataAxis = data.map(item => item.name);
  const option = {
    title: {
      text: rankType === 'task' ? `好友任务${timeFrame === 'month' ? '月度' : '周度'}排名` : `好友目标${timeFrame === 'month' ? '月度' : '周度'}排名`,
    },
    tooltip: {
      trigger: 'item',
    },
    xAxis: {
      type: 'category',
      data: dataAxis,
      axisLabel: {
        inside:true,
    },
  },
    yAxis: {
      type: 'value',
    },
    dataZoomed:[
      {
        type: 'inside'
      }
    ],
    series: [
      {
        data: data.map(item => item.value),
        showBackground:true,  //显示阴影
        type: 'bar',
        itemStyle:{
          //实现渐变色
          color : new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {offset: 1, color: '#000000'}, //底部
            {offset: 0.5, color: '#4e6469'}, //中间
            {offset: 0, color: '#b4bdc5'} //顶部
          ])
        },
        emphasis: {
          itemStyle: {
            //鼠标悬浮的渐变色
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {offset: 0, color: '#6a8296'}, //顶部
              {offset: 0.5, color: '#506b7d'}, //中间
              {offset: 1, color: '#3c5a6b'} //底部
            ])
          }
        },
      },
    ],
  };

  //点击缩放
  const zonmSize = 6;
  chart.on('click',function(params){
    console.log(dataAxis[Math.max(params.dataIndex - zonmSize/2, 0)]);
    chart.dispatchAction({
      type: 'dataZoom',
      startValue: dataAxis[Math.max(params.dataIndex - zonmSize/2, 0)],
      endValue: dataAxis[Math.min(params.dataIndex + zonmSize/2, dataAxis.length - 1)],
    });
  });


  chart.setOption(option);
};

// 按钮点击切换月份/周次排行
const changeRank = (type, timeFrame) => {
  if (type === 'taskMonth') {
    currentTaskRank.value = timeFrame;
    updateChart(taskMonthChart, 'task', timeFrame);
  } else if (type === 'obMonth') {
    currentObRank.value = timeFrame;
    updateChart(obMonthChart, 'ob', timeFrame);
  }
};

// 初始化图表
const initCharts = async () => {
  await updateChart(taskMonthChart, 'task', 'month');
  await updateChart(obMonthChart, 'ob', 'month');
};

initCharts();
</script>

<style scoped>
.leaderboard {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.bar-chart {
  width: 100%;
  height: 400px;
}

/* button {
  margin: 5px;
  padding: 5px 10px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
} */

/* button:hover {
  background-color: #357a4e;
} */

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
