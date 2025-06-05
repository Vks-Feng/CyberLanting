<template>
  <div v-if="displayedText" class="stream-output">
    <div>{{ displayedText }}</div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import objApi from '@/api/objhub-api';

const props = defineProps({
  objectiveId: String, // 目标 ID
  speed: { type: Number, default: 50 }, // 逐字显示速度
});

const displayedText = ref('');
const isFetching = ref(false);
let index = 0;
let interval = null;

const startStreaming = (content) => {
  displayedText.value = '';
  index = 0;
  if (!content) return;

  interval = setInterval(() => {
    if (index < content.length) {
      displayedText.value += content[index];
      index++;
    } else {
      clearInterval(interval);
    }
  }, props.speed);
};

const fetchGuidance = async (Id) => {
  if (!Id) return;

  isFetching.value = true;
  displayedText.value = '';

  try {
    // console.log('Fetching AI guidance...',Id)
    const response = await objApi.getAiGuidanceForObjective(Id);
    // console.log('AI guidance:', response);
    if (typeof response === 'string' && response.trim()) {
      startStreaming("智能建议：" + response);
    } else {
      displayedText.value = ''; // 数据为空时，不显示组件
    }
  } catch (error) {
    console.error('Error fetching AI guidance:', error);
    displayedText.value = ''; // 请求失败时，也不显示组件
  } finally {
    isFetching.value = false;
  }
};

// 监听 objectiveId 变化，获取新数据
watch(() => props.objectiveId, (newId) => {
  if (newId) fetchGuidance(newId);
}, { immediate: true });

onMounted(() => {
  if (props.objectiveId) {
    fetchGuidance();
  }
});
</script>

<style scoped>
.stream-output {
  background-size: cover;
  color: #2c2c2c;
  font-family: "KaiTi", "STKaiti", serif;
  padding: 16px;
  border-radius: 8px;
  white-space: pre-wrap;
  word-wrap: break-word;
  box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.1);
  border: 2px solid rgba(0, 0, 0, 0.3);
  animation: fadeIn 1s ease-in-out, inkSpread 2s infinite alternate ease-in-out;
  font-size: auto;
}

/* 墨水渐入效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    filter: blur(5px);
  }

  to {
    opacity: 1;
    filter: blur(0);
  }
}

/* 墨迹扩散动画 */
@keyframes inkSpread {
  from {
    border-color: rgba(0, 0, 0, 0.3);
    box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.1);
  }

  to {
    border-color: rgba(0, 0, 0, 0.5);
    box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.2);
  }
}
</style>
