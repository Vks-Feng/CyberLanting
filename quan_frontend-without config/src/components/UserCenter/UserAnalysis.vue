<template>
    <div class="analysis-view">
      <el-row :gutter="20">
        
        <!-- 第一排：日历 + 折线图 -->
        <!-- <el-col :span="12">
          <el-card shadow="never" class="panel-card small-card">
            <Calendar @data-selected="handleDataSelected" />
          </el-card>
        </el-col> -->
        
        <!-- <el-col :span="12">
          <el-card shadow="never" class="panel-card small-card line-chart">
            <Last7daysTaskCompeletion />
          </el-card>
        </el-col> -->
  
        <!-- 第二排：热力图 -->
        <el-col :span="24">
          <el-card shadow="never" class="panel-card mt-2">
            <Heatmap :selected-date="selectedDate" />
          </el-card>
        </el-col>
  
        <!-- 第三排：关系图 -->
        <el-col :span="24">
          <el-card shadow="never" class="panel-card large-map">
            <ObjRelationMap style="height: 800px;" />
          </el-card>
        </el-col>
  
      </el-row>
    </div>
  </template>
  
  
  
  <script setup>
  import { ref } from 'vue';
  import Calendar from '@/components/Analysis/Calendar.vue';
  import Heatmap from '@/components/Analysis/Heatmap.vue';
  import ObjRelationMap from '@/components/Common/ObjRelationMap.vue';
  import Last7daysTaskCompeletion from '@/components/Analysis/Last7daysTaskCompeletion.vue';
  
  // 定义响应式数据
  const selectedDate = ref(null); // 用于存储选中的日期
  
  // 定义事件处理方法
  const handleDataSelected = (date) => {
    selectedDate.value = date; // 更新选中的日期
  };
  </script>
  
  
  <style scoped>
  .analysis-view {
    padding: 20px;
  }
  
  .panel-card {
    border-radius: 12px;
    background: rgba(255, 255, 255, 0.4);
    backdrop-filter: blur(6px);
    padding: 10px;
    padding-bottom: 10px;
    transition: box-shadow 0.3s ease;
    margin-bottom: 20px;
  }
  
  .panel-card:hover {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  }
  
  .mt-2 {
    margin-top: 20px;
    /* height: 270px; */
  }
  
  .small-card {
    height: 400px;
    overflow: hidden;
  }
  
  .large-map {
    height: 730px;
  }
  
  /* .line-chart {
    height: 400px;
    padding: 10px;
  } */
  </style>
  