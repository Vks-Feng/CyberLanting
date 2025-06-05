<template>
  <div>
    <h2>目标名称</h2>
    <el-input v-model="input" style="width: 100%" placeholder="给你的目标取个帅气的名称吧" />


    <h2>目标描述</h2>
    <el-input v-model="textArea" style="width: 100%" :rows="10" type="textarea" :placeholder="STAR" />

    <h2>目标时间</h2>
    <el-date-picker v-model="timeValue" type="datetimerange" start-placeholder="开始时间" end-placeholder="结束时间"
      format="YYYY-MM-DD HH:mm:ss" date-format="YYYY/MM/DD ddd" time-format="A hh:mm:ss" />
  </div>
</template>

<script lang="ts" setup>
import { ref, defineExpose, defineEmits } from 'vue'
import objApi from '@/api/objhub-api'
import { getUserInfo } from '@/api/userinfo-api'
import { ElNotification } from 'element-plus'

const textArea = ref('')
const input = ref('')
const timeValue = ref<[Date, Date]>([new Date(), new Date()])
const STAR = `
    THE STAR PRINCIPLES
    - Situation - the situation you had to deal with
    - Task - the task you were given to do 
    - Action - the action you took
    - Result - what happened as a result and what you learned
    `
const emit = defineEmits(['update'])

const clear = () => {
  //将所有值进行清理
  input.value = '';
  textArea.value = '';
  timeValue.value = [null, null];
}


// 通过函数获取fatherId 和 主/子目标创建
const submitForm = async (fatherObjId: string, type: string) => {
  console.log("timeVable",timeValue.value)
  console.log("input",input.value)
  console.log("textArea", textArea.value)

  if (
    input.value.trim() === '' ||
    textArea.value.trim() === '' ||
    !timeValue.value ||
    timeValue.value.length !== 2 ||
    !timeValue.value[0] ||
    !timeValue.value[1]
  ) {
    console.log("信息填写不完整")
    ElNotification({
      message: "信息填写不完整",
      type: "error",
    })
    return Promise.resolve(false);
  }

  const userId = getUserInfo().id;

  // 1. 创建一个对象，包含所有表单数据
  const [startDate, endDate] = timeValue.value;

  const data = {
    userId: userId,
    name: input.value,
    description: textArea.value,
    startDate: startDate.toISOString().split('T')[0],
    endDate: endDate.toISOString().split('T')[0],
    rating: -1,
  };

  const dataAssociate = {
    childObjectiveId: "",
  }

  const dataChild = {
    name: input.value,
    description: textArea.value,
    startDate: startDate.toISOString().split('T')[0],
    endDate: endDate.toISOString().split('T')[0],
  }


  // 1. 判断是否为主目标创建
  if (type === "main") {
    try {
      const response = await objApi.postObj(data);
      dataAssociate.childObjectiveId = response.id;
    } catch (error) {
      console.error('创建主目标失败:', error);
      return Promise.resolve(false);
    }
  }
  // 2. 判断是否为子目标创建，执行目标关联操作(另一个为main)
  else if (type === "child") {
    try {
      // const response = await objApi.associateObj(fatherObjId, dataAssociate);
      const response = await objApi.createChildObj(fatherObjId, dataChild);
    }
    catch (error) {
      console.error('创建子目标失败:', error);
      return Promise.resolve(false);
    }
  }
  emit('update')  // 层层上传，呼唤父组件更新
  return Promise.resolve(true);
}

defineExpose({
  clear,
  submitForm
})
</script>

<style scoped></style>