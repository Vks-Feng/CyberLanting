<template>
    <el-card class="info-card panel-card">
      <Calendar style="width: 120%;margin-left: -10%;"/>
    </el-card>
  </template>
  
  <script setup>
  import Calendar from "@/components/Analysis/Calendar.vue";
  </script>
  
  <style scoped>
  .info-card {
    padding: 15px;
    border-radius: 10px;
    background: rgba(255, 255, 255, 0.7);
    margin-bottom: 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }

  .panel-card {
    border-radius: 12px;
    background: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(6px);
    padding: 10px;
    transition: box-shadow 0.3s ease;
    margin-bottom: 20px;
  }
  
  .panel-card:hover {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  }
  </style>
  