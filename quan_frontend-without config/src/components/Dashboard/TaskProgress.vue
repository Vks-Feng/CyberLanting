<template>
  <div class="progress-bar">
    <h2 class="title">🎯 目标进度</h2>
    <el-scrollbar height="95%">
      <div v-for="obj in objList">
        <p>{{ obj.name }}</p>
        <el-progress :color="customColors" :percentage="(obj.progress * 100).toFixed(2)" :stroke-width="10" />
      </div>
    </el-scrollbar>
  </div>
</template>


<script lang="ts" setup>
import { ref, watch } from 'vue'
import store from '@/data/vuex-data'


const objList = ref(null);

watch([() => store.state.objList], ([newObjList]) => {
  objList.value = newObjList;
});

const customColors = [
  { color: 'rgba(150, 180, 150, 1)', percentage: 20 },  // 淡墨绿
  { color: 'rgba(100, 150, 100, 1)', percentage: 40 },
  { color: 'rgba(60, 120, 60, 1)', percentage: 60 },
  { color: 'rgba(40, 100, 40, 1)', percentage: 80 },
  { color: 'rgba(20, 80, 20, 1)', percentage: 100 }     // 最墨绿
]

</script>


<style scoped>

.progress-bar{
  border: 1px solid #ccc;
  padding: 20px;
  box-sizing: border-box;
  backdrop-filter: blur(2px);
  /* 设置磨砂效果的强度 */
  background: rgba(255, 255, 255, 0.3);
  /* 半透明背景 */
  border-radius: 10px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  border-bottom: 1px solid #ddd;
  margin: 0px 0px 10px 0px;
}
</style>