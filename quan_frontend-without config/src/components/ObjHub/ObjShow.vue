<template>

  <el-row>
    <el-col :span="12">
      <h2>目标名称</h2>
    </el-col>

    <el-col v-if="!editAble" :span="12" style="display: flex; justify-content: right; align-items: center;">
      <el-icon :size="35" @click="editAble = !editAble">
        <Edit />
      </el-icon>
    </el-col>
  </el-row>

  <el-input :disabled="!editAble" v-model="input" style="width: 100%" :placeholder="objContent.name" />

  <h2>目标描述</h2>
  <el-input :disabled="!editAble" v-model="textarea" style="width: 100%" :rows="10" type="textarea"
    :placeholder="STAR" />

  <h2>目标时间</h2>
  <el-date-picker :disabled="!editAble" v-model="timeValue" type="datetimerange" start-placeholder="开始时间"
    end-placeholder="结束时间" format="YYYY-MM-DD HH:mm:ss" date-format="YYYY/MM/DD ddd" time-format="A hh:mm:ss" />


  <div v-if="editAble" style="display: flex; justify-content: flex-end;">
    <el-button class="ink-drawer-button" type="primary" @click="clickSave">保存</el-button>
    <el-button class="ink-drawer-button" type="primary" @click="editAble = !editAble">取消</el-button>
  </div>

</template>

<script lang="ts" setup>
import { watch, ref, defineProps } from 'vue'
import objhubApi from '@/api/objhub-api';

const editAble = ref(false)
const objContent = ref(null)
const props = defineProps(['objContent']);

const textarea = ref('')
const input = ref('')
const timeValue = ref<[Date, Date]>([new Date(), new Date()])
const rating = ref('')
const progress = ref(0)
const STAR = `
    THE STAR PRINCIPLES
    - Situation - the situation you had to deal with
    - Obj - the objective you were given to do 
    - Action - the action you took
    - Result - what happened as a result and what you learned
    `

const clickSave = async () => {
  editAble.value = false;
  const data = {
    name: input.value,
    description: textarea.value,
    startDate: timeValue.value[0],
    endDate: timeValue.value[1],
  }
  try {
    const response = await objhubApi.updateObj(props.objContent.id, data);
  } catch (error) {
    console.error(error);
  }
}

watch(
  [() => props.objContent],
  ([newObjContent]) => {
    if (newObjContent) {
      objContent.value = newObjContent ? newObjContent : {};
      input.value = objContent.value.name;
      textarea.value = objContent.value.description;
      timeValue.value = [
        new Date(objContent.value.startDate),
        new Date(objContent.value.endDate)
      ];
      rating.value = (objContent.value.weight / 20).toString();//将百分制转化为5分制
      progress.value = objContent.value.progress;
    } else {
      console.log("NULL");
    }
  },
  { deep: true, immediate: true }
);

</script>