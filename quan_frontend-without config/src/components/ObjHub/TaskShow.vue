<template>

  <el-row>
    <el-col :span="12">
      <h2>任务名称</h2>
    </el-col>

    <el-col v-if="!editAble" :span="12"  style="display: flex; justify-content: right; align-items: center;">
      <el-icon :size="35" @click="editAble = !editAble">
        <Edit />
      </el-icon>
    </el-col>
  </el-row>

  <el-input v-model="input" style="width: 100%" :disabled="!editAble" placeholder="给你的任务取个帅气的名称吧" />

  <h2>任务类型</h2>
  <el-select :disabled="!editAble" v-model="repeatType" placeholder="任务重复次数" style="width: 240px">
    <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
  </el-select>

  <div v-if="repeatType === 'single'">
    <h2>结束时间</h2>
    <el-date-picker :disabled="!editAble" v-model="ddl" type="datetime" placeholder="Select date and time"
      :shortcuts="shortcuts" />
  </div>

  <div v-if="repeatType === 'recurring'">
    <h2>任务周期</h2>
    <el-select :disabled="!editAble" v-model="period" placeholder="任务重复周期" style="width: 240px">
      <el-option v-for="item in periodOptions" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>

    <h2>任务时间</h2>
    <el-date-picker :disabled="!editAble" v-model="timeValue" type="datetimerange" start-placeholder="开始时间"
      end-placeholder="结束时间" format="YYYY-MM-DD HH:mm:ss" date-format="YYYY/MM/DD ddd" time-format="A hh:mm:ss" />
  </div>

  <h2>任务内容</h2>
  <el-input :disabled="!editAble" v-model="textarea" style="width: 100%" :rows="10" type="textarea"
    :placeholder="STAR" />

  <!-- <h2>完成进度</h2>
  <el-progress :percentage="(progressPercentage*100).toFixed(2)"/> -->

  <div v-if="editAble" style="display: flex; justify-content: flex-end;">
    <el-button class="ink-drawer-button" type="primary" @click="clickSave">保存</el-button>
    <el-button class="ink-drawer-button" type="primary" @click="editAble = !editAble">取消</el-button>
  </div>

</template>

<script lang="ts" setup>
import { defineProps, computed, ref, watch } from 'vue'
import objhubApi from '@/api/objhub-api';

const props = defineProps(['taskContent']);

const editAble = ref(false);
const taskContent = ref(null);
const textarea = ref('')
const period = ref('')
const ddl = ref<Date>(new Date())
const input = ref('')
const repeatType = ref('single')
const timeValue = ref<[Date, Date]>([new Date(), new Date()])
const STAR = `
    THE STAR PRINCIPLES
    - Situation - the situation you had to deal with
    - Task - the task you were given to do 
    - Action - the action you took
    - Result - what happened as a result and what you learned
    `

const clear = () => {

  //将所有值进行清理
  input.value = '';
  textarea.value = '';
  period.value = '';
  ddl.value = null;
  timeValue.value = [null, null];
  repeatType.value = 'single';
}

watch(
  [() => props.taskContent],
  ([newTaskContent]) => {
    if (newTaskContent) {
      console.log("newTaskContent", newTaskContent)
      taskContent.value = newTaskContent ? newTaskContent : {};
      input.value = taskContent.value.name;
      repeatType.value=taskContent.value.type;
      textarea.value = taskContent.value.description;
      timeValue.value = [
        new Date(taskContent.value.startDate),
        new Date(taskContent.value.endDate)
      ];
      ddl.value = new Date(taskContent.value.ddl);
      period.value = taskContent.value.period;
    } else {
      console.log("NULL");
    }
  },
  { deep: true, immediate: true }
);

const clickSave = async () => {
  editAble.value = false;

  const data = ref(null);
  // 总基本判定
  if (!input.value || !repeatType.value || !textarea.value) {
    return false;
  }

  // 重复字段判定
  if (repeatType.value === 'recurring' && (!period.value || !timeValue.value)) {
    return false;
  }
  // 单次任务判定
  else if (repeatType.value === 'single' && !ddl.value) {
    return false;
  }

  if (repeatType.value === 'single') {
    data.value = {
      name: input.value,
      description: textarea.value,
      ddl: ddl.value.toISOString().split('T')[0],
    };
  }
  else if (repeatType.value === 'recurring') {
    const [startDate, endDate] = timeValue.value;

    data.value = {
      name: input.value,
      description: textarea.value,
      period: period.value,
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0],
    };
  }
  else {
    console.error('选择类型错误');
    return false;
  }

  try {
    const response = ref(null);

    if (repeatType.value === 'recurring') {
      response.value = await objhubApi.updateRecurringTask(props.taskContent.id , data.value);
    }
    else {
      response.value = await objhubApi.updateSingleTask(props.taskContent.id, data.value);
    }

    console.log('Response:', response);

    clear();


    return true;
  } catch (error) {
    console.error('提交失败:', error);
    clear();
    return false;
  }
}



const typeOptions = [
  {
    value: 'recurring',
    label: '重复',
  },
  {
    value: 'single',
    label: '单次',
  },
]

const periodOptions = [
  {
    value: 'daily',
    label: '每天',
  },
  {
    value: 'weekly',
    label: '每周',
  },
  {
    value: 'monthly',
    label: '每月',
  },
]


const progressPercentage = computed(() => {
  if (!taskContent.value || !taskContent.value.startDate || !taskContent.value.endDate) {
    return 0;
  }

  const startDate = new Date(taskContent.value.startDate);
  const endDate = new Date(taskContent.value.endDate);
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

const shortcuts = [
  {
    text: 'Today',
    value: new Date(),
  },
  {
    text: 'Yesterday',
    value: () => {
      const date = new Date()
      date.setDate(date.getDate() - 1)
      return date
    },
  },
  {
    text: 'A week ago',
    value: () => {
      const date = new Date()
      date.setDate(date.getDate() - 7)
      return date
    },
  },
]



</script>