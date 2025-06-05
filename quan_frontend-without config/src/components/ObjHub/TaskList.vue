<!-- 主界面右侧的任务清单 -->
<template>

  <ObjHeader @update="update" :ObjContent="objHeaderContent" />

  <el-divider />

  <!-- 任务详情侧栏 -->
  <el-drawer v-model="drawerTask" title="Task Detail" size='70%' :with-header="false">
    <TaskShow :taskContent="chooseTaskItem" />
  </el-drawer>

  <!-- 目标详情侧栏 -->
  <el-drawer v-model="drawerObj" title="Obj Detail" size='70%' :with-header="false">
    <ObjShow :objContent="chooseObjItem" />
  </el-drawer>

  <!-- 创建任务侧栏 -->
  <el-drawer :before-close="handleCreateClose" v-model="drawerCreateTask" title="I am the title" size='70%'
    :with-header="false">
    <CreateTaskPage ref="createTaskPageRef" />
    <template #footer>
      <div class="dialog-footer">
        <el-button class="ink-drawer-button" @click="drawerCreateTask = false">取消</el-button>
        <el-button class="ink-drawer-button" type="primary" @click="clickCreate">
          创建
        </el-button>
      </div>
    </template>
  </el-drawer>


  <el-menu>

    <div v-for="(objItem, index1) in objList">
      <!-- v-for 不可以放到el-menu上，否则会形成多个menu。从而导致选择异常 -->

      <!-- 副目标 -->
      <el-sub-menu :index="String(index1 + 1)">

        <template #title class="title-container">
          <h3 @click.stop="clickObj(objItem)">{{ objItem.name }}</h3>
          <div class="icon-container" @click.stop="">

            <el-icon @click.stop="drawerCreateTask = true; selectSubObjId = objItem.id">
              <Plus />
            </el-icon>
            <DeleteButton @delete-done="deleteObj(objItem.id)" :id="objItem.id" type="obj" />
          </div>
        </template>

        <!-- 任务结点 -->
        <el-menu-item v-for="(taskItem, index2) in objItem.tasks" :index="String(taskItem.id)"
          @click="() => clickTask(taskItem)">
          <template #title>
            <div class="taskItem-container"
              :style="taskItem.status === 'completed' ? { textDecoration: 'line-through', textDecorationThickness: '2.5px', padding: '0 0 0 40px' } : { padding: '0 0 0 40px' }">
              <CheckButton :taskId="taskItem.id" :status="taskItem.status"
                @check="onClickBtnClick(taskItem)" />
              <span>{{ taskItem.name }}</span>

            </div>
            <div class="icon-container">
              <DeleteButton @delete-done="deleteTask(objItem, taskItem.id)" :id="taskItem.id" type="task" />
            </div>
          </template>
        </el-menu-item>

      </el-sub-menu>
    </div>

  </el-menu>

</template>

<script setup lang="ts">
import TaskShow from './TaskShow.vue'
import { ref, watch } from 'vue'
import ObjHeader from './ObjHeader.vue'
import ObjShow from './ObjShow.vue';
import CreateTaskPage from './CreateTaskPage.vue';
import DeleteButton from './DeleteButton.vue';
import CheckButton from './CheckButton.vue';
import objhubApi from '@/api/objhub-api';

const onClickBtnClick = (taskItem) => {
  (checkValue) => taskItem.status = checkValue ? 'completed' : 'pending'
  update()
}
async function update() {
  const response = await objhubApi.getSingleObjectiveDetails(props.mainObjId)
  objList.value = response.children; // 最终层，进行更新
  objHeaderContent.value = response;
}

const objList = ref(null);
const drawerTask = ref(false)
const drawerObj = ref(false)
const drawerCreateTask = ref(false)
const selectSubObjId = ref(false)
const props = defineProps(['mainObjId']);
const objHeaderContent = ref(null);

const createTaskPageRef = ref<{ submitForm: (string) => boolean, clear: () => void } | null>(null); // 定义 ref，确保类型安全

const handleCreateClose = () => {
  drawerCreateTask.value = false
  createTaskPageRef.value?.clear()
}

const clickCreate = async () => {
  const ans = await createTaskPageRef.value?.submitForm(selectSubObjId.value)
  if (ans === true) {
    drawerCreateTask.value = false
  }

  try {
    const response = await objhubApi.getSingleObjectiveDetails(props.mainObjId);

    if (response) {
      objList.value = response.children; // Assuming the API returns an object with children/tasks
      objHeaderContent.value = response;
    } else {
      console.error("response is null ", response);
    }
  } catch (error) {
    console.error("Error fetching single objective details:", error);
  }

}

function deleteTask(objItem: any, taskId: number) {
  const taskIndex = objItem.tasks.findIndex((task: any) => task.id === taskId);
  if (taskIndex !== -1) {
    objItem.tasks.splice(taskIndex, 1);
  }
}

function deleteObj(objId: number) {
  const objIndex = objList.value.findIndex((obj: any) => obj.id === objId);
  if (objIndex !== -1) {
    objList.value.splice(objIndex, 1);
  }
}

watch(
  [() => props.mainObjId],
  async ([newMainObjId]) => {
    if (!newMainObjId) return;

    try {
      const response = await objhubApi.getSingleObjectiveDetails(newMainObjId);

      if (response) {
        objList.value = response.children; // Assuming the API returns an object with children/tasks
        objHeaderContent.value = response;
      } else {
        console.error("response is null ", response);
      }
    } catch (error) {
      console.error("Error fetching single objective details:", error);
    }
  },
  { deep: true, immediate: true }
);

const activeNames = ref(['1'])
const chooseTaskItem = ref(null);
const chooseObjItem = ref(null);
function clickObj(objItem: any) {
  drawerObj.value = true;
  chooseObjItem.value = objItem;
}

function clickTask(taskItem: any) {
  drawerTask.value = true;
  chooseTaskItem.value = taskItem;
}

</script>

<style scoped>
::v-deep .el-collapse-item__header {
  font-size: medium;
  font-weight: 700;
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

::v-deep .el-sub-menu__title {
  justify-content: space-between;
}

::v-deep .el-menu-item {
  justify-content: space-between;
  vertical-align: middle;
}
</style>