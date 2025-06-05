
<template>
    <div id="main" style="width: 100%; height: 400px;"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { onMounted, defineProps } from 'vue';
import analysisApi from '@/api/analysis-api.js';

const props = defineProps([
    'objList'
]);

onMounted(async () => {
    const chartDom = document.getElementById('main');
    if (!chartDom) return;

    const myChart = echarts.init(chartDom);

    try {
        // Fetch data from the API
        const response = await analysisApi.getLast7daysTaskCompletionRate();

        console.log("近7日任务完成率走势:", response);

        // Process the API response to extract data
        let dates = response.map(item => item.date);
        let completionRates = response.map(item => item.completionRate);

        // Reverse the data to display the latest date first
        dates.reverse();
        completionRates.reverse();

        option = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                top: '5%',
                left: 'center'
            },
            series: [
                {
                    name: 'Access From',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: 40,
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: [
                        { value: 1048, name: 'Search Engine' },
                        { value: 735, name: 'Direct' },
                        { value: 580, name: 'Email' },
                        { value: 484, name: 'Union Ads' },
                        { value: 300, name: 'Video Ads' }
                    ]
                }
            ]
        };

        myChart.setOption(option);
    } catch (error) {
        console.error('Error fetching task completion rate data:', error);
    }
});
</script>

<style scoped>
/* Add any custom styles here */
</style>

















<!-- <template>
  <div>
    <h1>用户位置</h1>
    <button @click="getLocation">获取我的位置</button>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    <div v-if="latitude && longitude">
      <p>纬度: {{ latitude }}</p>
      <p>经度: {{ longitude }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      latitude: null, // 存储纬度
      longitude: null, // 存储经度
      errorMessage: null // 存储错误信息
    };
  },
  methods: {
    getLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            this.latitude = position.coords.latitude;
            this.longitude = position.coords.longitude;
            this.errorMessage = null; // 清空错误信息
          },
          (error) => {
            this.errorMessage = this.getErrorMessage(error.code);
          }
        );
      } else {
        this.errorMessage = "浏览器不支持地理位置功能。";
      }
    },
    getErrorMessage(errorCode) {
      switch (errorCode) {
        case 1:
          return "用户拒绝访问地理位置";
        case 2:
          return "无法获取位置信息";
        case 3:
          return "请求超时";
        default:
          return "未知错误";
      }
    }
  }
};
</script>

<style scoped>
.error {
  color: red;
  font-size: 14px;
}
</style> -->
