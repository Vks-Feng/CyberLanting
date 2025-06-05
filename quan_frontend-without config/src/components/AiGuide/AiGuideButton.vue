<template>
    <el-button class="sidebar-icon-btn"  plain @click="dialogVisible = true">
        <div class="icon-text-column">
        <img src="/images/icon/AI.svg" style="width: 25px;margin-top:5px;margin-bottom: 5px;"/>
        <span class="btn-text" >智能规划</span>
    </div>
    </el-button>

    <el-dialog v-model="dialogVisible" title="Tips" width="500" @close="clearAiRequirement">
        <AiGuideBar v-if="stage === 'choice'" @quit="closeDialog"/>
        <AiGuideFront v-if="stage === 'front'" @close="closeDialog" @changeStage="changeStage" ref="aiGuideFrontRef"/>
    </el-dialog>

</template>

<script setup lang="ts">
import AiGuideBar from './AiGuideBar.vue';
import AiGuideFront from './AiGuideFront.vue';
import store from '@/data/vuex-data';
import { ref } from 'vue';


const stage = ref("front")

const aiGuideFrontRef = ref<InstanceType<typeof AiGuideFront> | null>(null);
const closeDialog = () => {
    dialogVisible.value = false
}

const clearAiRequirement= () => {
    store.commit('clearAiRequirement')
    store.commit('clearQuestion')
    stage.value = "front"
    aiGuideFrontRef.value?.clear()
}

const dialogVisible = ref(false)

const changeStage = (_stage: string) => {
    console.log("changeStage", _stage)
    stage.value = _stage
}
</script>

<style scoped>

.sidebar-btn {
  display: block;
  width: 90%;
  margin: 10px 10px 10px 12px;
  color: gray;
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>