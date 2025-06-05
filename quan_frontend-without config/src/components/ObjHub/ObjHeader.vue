<!-- 主页面上方的主任务相关信息  -->
<template>

  <el-row v-if="hasObj == true">
    <el-col :span="9">
      <h2>{{ objName }}</h2>

      <el-row>
        <p>时间: {{ startDateString }} : {{ endDateString }}</p>
      </el-row>
      <el-row>
        <p>主要内容: {{ textArea }}</p>
      </el-row>
    </el-col>

    <el-col :span="12">
      <!-- <h4>时间进度</h4>
      <el-progress :percentage="timePercentage" :color="customColor" /> -->
      <div >
      <h4>完成进度</h4>
      <el-progress :percentage="(finishPercentage*100).toFixed(2)" :stroke-width = "12" />
      <el-row class="bottom-row">
        <CreateSubObjButton @update="update" :fatherObjId="mainObjId" type="child" text="新增子目标" />
      </el-row>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <AiGuidanceBox :objectiveId="mainObjId" :speed="30"></AiGuidanceBox>
  </el-row>

  <!-- <el-row v-if="hasObj == false">
    <h3>您还没有创建目标噢,</h3>
    <h3>请速速创建一个吧</h3>
    <el-icon :size="25" style="align-self: center;">
      <CoffeeCup />
    </el-icon>
  </el-row> -->

</template>

<script lang="ts" setup>
import CreateSubObjButton from './CreateSubObjButton.vue'
import { defineEmits, ref, watch, computed } from 'vue'
import AiGuidanceBox from '../AiGuide/AiGuidanceBox.vue';

const props = defineProps(['mainObjId', 'ObjContent']);

const objList = ref(null)
const taskList = ref(null)
const objName = ref(null)
const textArea = ref(null)
const startDateString = ref(null)
const endDateString = ref(null)
const timeValue = ref<[Date, Date]>([new Date(), new Date()])
const progressColor = ref([  { color: '#f0f0f0', percentage: 20 },
  { color: '#d0d0d0', percentage: 40 },
  { color: '#a0a0a0', percentage: 60 },
  { color: '#707070', percentage: 80 },
  { color: '#404040', percentage: 100 }]);
const hasObj = ref(true)
const mainObjId = ref(null)
const finishPercentage = ref(0)

const emit = defineEmits(['update'])

function update() {
  emit('update')
}

watch(
  [() => props.ObjContent],
  ([newObjContent]) => {
    if (newObjContent) {
      hasObj.value = true;
      mainObjId.value = newObjContent.id;

      objList.value = newObjContent.children;
      taskList.value = newObjContent.tasks;
      objName.value = newObjContent.name;
      textArea.value = newObjContent.description;
      startDateString.value = newObjContent.startDate;
      endDateString.value = newObjContent.endDate;
      finishPercentage.value = newObjContent.progress;

      if (startDateString.value && endDateString.value) {
        timeValue.value = [
          new Date(startDateString.value),
          new Date(endDateString.value)
        ];
      } else {
        timeValue.value = [new Date(), new Date()]; // 默认值
      }
    } else {
      hasObj.value = false;
      objList.value = [];
    }
  },
  { deep: true, immediate: true }
);

// 计算进度百分比
const timePercentage = computed(() => {
  if (!startDateString.value || !endDateString.value) {
    return 0;
  }

  const startDate = new Date(startDateString.value);
  const endDate = new Date(endDateString.value);
  const currentDate = new Date();

  if (currentDate < startDate) {
    return 0;
  } else if (currentDate > endDate) {
    return 100;
  }

  const totalDuration = endDate.getTime() - startDate.getTime();
  const elapsedDuration = currentDate.getTime() - startDate.getTime();
  const percentage = (elapsedDuration / totalDuration) * 100;

  return Number(Math.min(percentage, 100).toFixed(2));
});
</script>

<style scoped>
.bottom-row {
  margin-top: 7vh;
  justify-content: right;
}

.progress-bar {
  width: 300px;
}

.progress-bar :deep(.el-progress-bar__inner){
  background-image: linear-gradient(
    to right,
    rgba(255,255,255,0.5),
    rbga(0,0,0,0.8)
  );
  border-radius: 5px;
}
</style>