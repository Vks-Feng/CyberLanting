<template>
  <div v-loading="loading">
    <h2>目标名称</h2>
    <el-input v-model="input" style="width: 100%" placeholder="准确描述您的目标" />

    <h2>目标描述</h2>
    <el-input v-model="textArea" style="width: 100%" :rows="10" type="textarea" :placeholder="desHolder" />

    <h2>目标时间</h2>
    <el-date-picker v-model="timeValue" type="datetimerange" start-placeholder="开始时间" end-placeholder="结束时间"
      format="YYYY-MM-DD HH:mm:ss" date-format="YYYY/MM/DD ddd" time-format="A hh:mm:ss" />

    <div style="display: flex; justify-content: flex-end;margin-top: 15px;">
      <el-button class="ink-button" type="primary" @click="submitForm()">生成任务</el-button>
    </div>

  </div>
</template>

<script lang="ts" setup>
import { ref, defineExpose } from 'vue'
import aiApi from '@/api/aiguide-api'
import { ElNotification } from 'element-plus'
import store from '@/data/vuex-data'

const textArea = ref('')
const input = ref('')
const timeValue = ref<[Date, Date]>([new Date(), new Date()])
const desHolder = `
    详细描述您的目标以及您的当前状况，以方便我们更好为您提供定制化目标生成服务
    `
const emit = defineEmits(['changeStage','close']);
const loading = ref(false);
const clear = () => {
  //将所有值进行清理
  input.value = '';
  textArea.value = '';
  timeValue.value = [null, null];
}

const submitForm = async () => {
  if (!input.value || !textArea.value || !timeValue.value) {
    ElNotification({
      message: '请填写完整信息',
      type: 'warning',
    });
    return false;
  }

  const [startDate, endDate] = timeValue.value;

  const data = {
    name: input.value,
    description: textArea.value,
    startDate: startDate.toISOString().split('T')[0],
    endDate: endDate.toISOString().split('T')[0],
  };

  store.commit('setAiRequirement', data)

  try {
    loading.value = true
    const response = await aiApi.postRequirement(data);
    console.log('Response', response);
    if(response==null)throw new Error('response is null')
    emit('changeStage', 'choice'); //切换状态到选择
    loading.value = false

  } catch (error) {
    console.error('提交失败:', error);
    loading.value = false
    emit('close');
    ElNotification({
      message: '非常抱歉，AI生成失败。请稍后重试，或联系管理员',
      type: 'warning',
    });
    return false;
  }
}

defineExpose({
  clear,
  submitForm
})
</script>

<style scoped></style>