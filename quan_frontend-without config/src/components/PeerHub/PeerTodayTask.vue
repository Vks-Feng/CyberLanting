<!-- 主界面右侧的任务清单 -->
<template>
  <el-menu default-active="" v-model="activeNames">
    <!-- 任务结点 -->
    <el-menu-item v-for="(taskItem, index) in taskList" :index="String(taskItem.id)">
      <template #title>
        <span style="padding-left: 20px;" class="taskItem">{{ taskItem.name }}</span>
        <div style="padding-right:20px;" class="icon-container">
          <CheckButton @click.stop="" :taskId="taskItem.id" :status="taskItem.status" disabled />
        </div>
      </template>
    </el-menu-item>

  </el-menu>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import CheckButton from "@/components/ObjHub/CheckButton.vue";
import { defineProps } from "vue";
import objhubApi from "@/api/objhub-api";

const taskList = ref(null);
const props = defineProps(["id"]);

watch(
  [() => props.id],
  async ([newId]) => {
    console.log("Peer's Id", newId);
    try {
      const res = await objhubApi.getUserTodayTasks(newId);
      taskList.value = res;
    } catch (error) {
      console.log(error);
    }
  },
  { deep: true, immediate: true }
);

const activeNames = ref(["1"]);
</script>

<style scoped>
::v-deep .el-collapse-item__header {
  font-size: medium;
  font-weight: 700;
}

.taskItem {
  font-size: medium;
  font-weight: 700;
  text-align: left !important;
  margin-left: 0;
}

.icon-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

::v-deep .el-sub-menu__title {
  justify-content: space-between;
}

::v-deep .el-menu-item {
  justify-content: space-between;
  /* margin-left: 40px; */
}
</style>
