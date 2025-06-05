<!-- 主界面右侧的任务清单 -->
<template>

  <!-- 任务详情侧栏 -->
  <el-drawer v-model="drawerTask" title="Task Detail" size='70%' :with-header="false">
    <TaskShow :taskContent="chooseTaskItem" />
  </el-drawer>

  <el-menu default-active="" v-model="activeNames">


    <!-- 任务结点 -->
    <el-menu-item v-for="(taskItem, index) in taskList" :index="String(taskItem.id)" @click="() => clickTask(taskItem)">
      <template #title>
        <div class="taskItem-container"
          :style="taskItem.status === 'completed' ? { textDecoration: 'line-through', textDecorationThickness: '2.5px', padding: '0 0 0 40px' } : { padding: '0 0 0 40px' }">
          <CheckButton  :taskId="taskItem.id" :status="taskItem.status" @check="(checkValue)=>taskItem.status = checkValue ? 'completed' : 'pending'"/>
          <span>{{ taskItem.name }}</span>

        </div>
      </template>
    </el-menu-item>


  </el-menu>

</template>

<script setup lang="ts">
import TaskShow from './TaskShow.vue'
import store from '@/data/vuex-data';
import { ref, watch } from 'vue'
import CheckButton from './CheckButton.vue';

const taskList = ref(null);
const drawerTask = ref(false)

watch(
  [() => store.state.todayTaskList],
  ([newtaskList]) => {
    if (newtaskList && newtaskList.length > 0) {
      taskList.value = newtaskList ? newtaskList : {};
    } else {
      console.log("taskList empty");
      taskList.value = [];
    }
  },
  { deep: true, immediate: true }
);



const activeNames = ref(['1'])
const chooseTaskItem = ref(null);

function clickTask(taskItem: any) {
  console.log("clickItem", taskItem);
  drawerTask.value = true;
  chooseTaskItem.value = taskItem;
}

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

.taskItem-container {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.icon-container {
  display: flex;
  padding-right: 20px;
  justify-content: space-around;
  align-items: center;
  flex-shrink: 0;
}
</style>