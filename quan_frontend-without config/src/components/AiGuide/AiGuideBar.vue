<template>
  <div v-loading="loading"> <!-- 进度条 -->
    <el-steps style="max-width: 600px" :active="active" finish-status="success">
      <el-step v-for="(item, index) in questionList" :title="'问题' + (index + 1)" :key="index" />
    </el-steps>

    <div v-if="questionList.length > 0">
      <h3> {{ questionList[active]?.question }} </h3>

      <el-radio-group v-model="choose" class="vertical-radio-group">
        <el-radio v-for="(item, index) in questionList[active].options" :value="index" size="large">
          {{ item }}
        </el-radio>
      </el-radio-group>

      <div style="display: flex; justify-content: flex-end;margin-top: 15px;">
        <el-button class="sidebar-btn ink-button" v-if="active != 0" style="margin-top: 12px"
          @click="back">上一个问题</el-button>
        <el-button class="sidebar-btn ink-button" v-if="active != questionList.length - 1" style="margin-top: 12px"
          @click="next">下一个问题</el-button>
        <el-button class="sidebar-btn ink-button" v-if="active == questionList.length - 1" style="margin-top: 12px"
          @click="finish">提交答案</el-button>
      </div>

      <WebSocket ref="webSocketRef" />

    </div>
  </div>

</template>

<script lang="ts" setup>
import store from '@/data/vuex-data'
import { ref, watch, defineEmits ,onBeforeUnmount} from 'vue'
import aiguideApi from '@/api/aiguide-api'
import { ElNotification } from 'element-plus'
import WebSocket from '../Common/WebSocket.vue'

const loading = ref(false)
const questionList = ref([])
const targetRequirement = ref(null)
const webSocketRef = ref<InstanceType<typeof WebSocket> | null>(null)


watch(
  [() => store.state.question, () => store.state.aiRequirement],
  ([newQuestion, newTarget]) => {
    questionList.value = newQuestion
    targetRequirement.value = newTarget
  },
  { deep: true, immediate: true }
);

const active = ref(0)
const choose = ref(0)
const feedback = ref([])

const emit = defineEmits(['quit']);
function clamp(value: number, min: number, max: number) {
  return Math.min(Math.max(value, min), max)
}

const next = () => {
  if (active.value != questionList.value.length - 1) {
    const currentQuestion = questionList.value[active.value].question
    const currentAnswer = questionList.value[active.value].options[choose.value]
    feedback.value.push({ question: currentQuestion, answer: currentAnswer })
  }

  active.value = clamp(active.value + 1, 0, questionList.value.length)
}

const back = () => {
  if (active.value != -1 && feedback.value.length != 0) feedback.value.pop()
  active.value = clamp(active.value - 1, 0, questionList.value.length)
}

const finish = () => {
  if (active.value == questionList.value.length - 1) {
    const currentQuestion = questionList.value[active.value].question
    const currentAnswer = questionList.value[active.value].options[choose.value]
    feedback.value.push({ question: currentQuestion, answer: currentAnswer })
  }

  submitForm()
}

var exitNormal = false
const submitForm = async () => {
  const data = {
    name: targetRequirement.value.name,
    description: targetRequirement.value.description,
    startDate: targetRequirement.value.startDate,
    endDate: targetRequirement.value.endDate,
    feedback: feedback.value
  };

  try {
    loading.value = true;
    const response = await aiguideApi.postAnswer(data);
    loading.value = false;
    ElNotification({
      message: '您的目标规划正在制定中，请稍后',
      type: 'success',
      duration: 3000
    });
    emit('quit');
    if (webSocketRef.value) {
      webSocketRef.value.send() // 发送消息，等待回应显示
    }
    exitNormal = true  // 退出正常流程，不会自动结束WebSocket

  } catch (error) {
    console.error('提交失败:', error);
    return false;
  }
}

onBeforeUnmount(() => {
  if (webSocketRef.value && !exitNormal) {
    webSocketRef.value.closeWebSocket()
    console.log('WebSocket已关闭 in AI GuideBar');
  }
})
</script>

<style scoped></style>