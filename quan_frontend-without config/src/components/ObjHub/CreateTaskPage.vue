<template>

  <h2>任务名称</h2>
  <el-input v-model="input" style="width: 100%" placeholder="给你的任务取个帅气的名称吧" />
  <p>{{ props.objId }}</p>

  <h2>任务类型</h2>
  <el-select v-model="repeatType" placeholder="任务重复次数" style="width: 240px">
    <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
  </el-select>

  <h2>任务内容</h2>
  <el-input v-model="textarea" style="width: 100%" :rows="10" type="textarea" :placeholder="STAR" />

  <div v-if="repeatType === 'recurring'">
    <h2>任务周期</h2>
    <el-select v-model="period" placeholder="任务重复周期" style="width: 240px">
      <el-option v-for="item in periodOptions" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>

    <h2>任务时间</h2>
    <el-date-picker v-model="timeValue" type="datetimerange" start-placeholder="开始时间" end-placeholder="结束时间"
      format="YYYY-MM-DD HH:mm:ss" date-format="YYYY/MM/DD ddd" time-format="A hh:mm:ss" />
  </div>

  <div v-if="repeatType === 'single'">
    <h2>结束时间</h2>
    <el-date-picker v-model="ddl" type="datetime" placeholder="Select date and time" :shortcuts="shortcuts" />
  </div>

</template>

<script lang="ts" setup>
import { defineProps , defineExpose, ref } from 'vue'
import { ElNotification } from 'element-plus'
import objApi from '@/api/objhub-api'
import { getUserInfo } from '@/api/userinfo-api'

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

const props = defineProps(['objId'])
const clear =()=>{

    //将所有值进行清理
    input.value = '';
    textarea.value = '';
    period.value = '';
    ddl.value = null;
    timeValue.value = [null,null];
    repeatType.value = 'single';
}

const submitForm = async (objId: string) => {
  const data = ref(null);

  // --------判定信息完整程度--------
  const judge = ref(true);
  // 总基本判定
  if (!input.value || !repeatType.value || !textarea.value) {
    judge.value = false;
  }
  // 重复字段判定
  if (repeatType.value === 'recurring' &&
    (!period.value ||
      (
        !timeValue.value ||
        timeValue.value.length !== 2 ||
        !timeValue.value[0] ||
        !timeValue.value[1]
      )
    )
  ) {
    judge.value = false;
  }
  // 单次任务判定
  else if (repeatType.value === 'single' && !ddl.value) {
    judge.value = false;
  }

  if (judge.value === false) {
    console.log("请补充完整信息")
    ElNotification({
      message: "请补充完整信息",
      type: "error",
    })
    return;
  }

  // --------检查选择类型-------
  if (repeatType.value === 'single') {
    data.value = {
      userId: getUserInfo().id, // 假设用户 ID 是固定的
      objectiveId: objId,
      name: input.value,
      description: textarea.value,
      ddl: ddl.value.toISOString().split('T')[0],
    };
  }

  else if (repeatType.value === 'recurring') {
    const [startDate, endDate] = timeValue.value;

    data.value = {
      userId: getUserInfo().id, // 假设用户 ID 是固定的
      objectiveId: objId,
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


  //--------根据选择类型执行相应提交数据--------
  try {
    const response = ref(null);
    if (repeatType.value === 'recurring') {
      response.value = await objApi.postRecurringTask(data.value, objId);
    }
    else {
      response.value = await objApi.postSingleTask(data.value, objId);
    }

    clear();
    return true;
  } catch (error) {
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

defineExpose({
  submitForm,
  clear,
})

</script>