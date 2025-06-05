<!-- 任务主界面 -->
<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="25%" class="ob-bar" style="display: flex; flex-direction: column; height: 100vh">
        <h2>星枢台</h2>
        <!-- 目标详情侧栏 -->
        <el-drawer v-model="drawerObj" title="Obj Detail" size="70%" :with-header="false">
          <ObjShow :objContent="chooseObjItem" />
        </el-drawer>

        <div class="sidebar-btn">
          <CreateObjButton :fatherObjId="-1" type="main" text="新增目标" />
          <AiGuideButton />
          <el-button plain @click="handleSelectToday" class="sidebar-icon-btn">
            <div class="icon-text-column">
              <img src="/images/icon/todayTask.svg" style="width: 30px;margin-bottom: 3px;" />
              <span class="btn-text">今日任务</span>
            </div>
          </el-button>
        </div>

        <el-row class="tac">
          <el-col :span="24">
            <h5 class="mb-2">目标列表</h5>

            <el-menu class="el-menu-vertical-demo" @select="handleSelect">
              <div v-for="(item, index) in objs">
                <el-menu-item :index="String(item.id)">
                  <template #title style="
                      display: flex;
                      justify-content: space-between;
                      align-content: center;
                      padding-left: 20px;
                    ">
                    <div>
                      <el-icon style="padding-left: 10px" :size="35" @click="handleEdit(item)">
                        <Edit />
                      </el-icon>
                      <span style="padding-left: 6px">{{ item.name }}</span>
                    </div>

                    <div style="
                        display: flex;
                        justify-content: space-between;
                        align-content: center;
                        margin: 20px;
                      ">
                      <DeleteButton :id="item.id" type="obj" class="deleteButton" />
                    </div>
                  </template>
                </el-menu-item>
              </div>
            </el-menu>
          </el-col>
        </el-row>

      </el-aside>

      <el-container class="vks-tmp-objside">
        <el-divider />

        <!-- 根据按钮选择触发选择展示总目标列表还是今日目标列表 -->
        <el-main>
          <TaskList v-if="mode === 'taskList'" :mainObjId="chooseId" :key="chooseId" />

          <TodayTaskPage v-if="mode === 'todayTasks'" />
        </el-main>

        <el-footer class="footer"> </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { defineEmits, onBeforeMount, ref, watch } from "vue";
import ObjShow from "./ObjShow.vue";
import TaskList from "./TaskList.vue";
import objApi from "@/api/objhub-api";
import CreateObjButton from "./CreateObjButton.vue";
import AiGuideButton from "../AiGuide/AiGuideButton.vue";
import DeleteButton from "./DeleteButton.vue";
import store from "@/data/vuex-data";
import { getUserInfo } from "@/api/userinfo-api";
import TodayTaskPage from "./TodayTaskPage.vue";
import objhubApi from "@/api/objhub-api";

const objs = ref([]);
const chooseId = ref(0);
const drawerObj = ref(false);
const chooseObjItem = ref(null);

const token = localStorage.getItem("token");
console.log("token:", token);

const mode = ref("taskList"); // taskList or todayTasks

const handleSelect = (key: string, keyPath: string[]) => {
  mode.value = "taskList";
  chooseId.value = parseInt(key);
};

const handleSelectToday = () => {
  mode.value = "todayTasks";
  objhubApi.getUserTodayTasks(getUserInfo().id);
};

const handleEdit = (item: any) => {
  chooseObjItem.value = item;
  drawerObj.value = true;
};

onBeforeMount(async () => {
  const userId = getUserInfo().id;
  try {
    const response = await objApi.getUserObjectives(userId);
    if (response && Array.isArray(response) && response.length > 0)
      chooseId.value = response[0].id; // 右侧任务栏默认显示第一个主目标
  } catch (error) {
    console.error("Failed to fetch objectives:", error);
  }
  try {
    const response = await objApi.getUserTodayTasks(userId);
  } catch (error) {
    console.error("Failed to fetch todayTasks", error);
  }
});

watch(
  [() => store.state.objList],
  ([newObjList]) => {
    if (newObjList && newObjList.length > 0) {
      objs.value = newObjList;
    } else {
      console.log("objList empty");
    }
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
.header {
  font-size: 30px;
  align-self: center;
}

.btn-group-header {
  width: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* siderbar 和 main的间距 */
.el-main {
  padding: 20px !important;
}

.footer {
  display: flex;
  justify-content: flex-end;
}

.el-menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* 垂直居中 */
}

.el-menu-item .el-icon,
.el-menu-item .delete-button {
  margin-left: auto;
  /* 强制推到右边 */
}

.sidebar-btn {
  display: flex;
  flex-direction: row;
  width: 90%;
  margin: 10px 10px 10px 12px;
  color: gray;
}

.common-layout {
  height: 100vh;
  overflow: hidden;
  /* 禁用整体滚动 */
}

/* 保留 vks-tmp-objside 的滚动功能并淡化滚动条 */
.vks-tmp-objside {
  height: 100vh;
  overflow-y: auto;
  scrollbar-width: thin;
  /* Firefox */
  scrollbar-color: rgba(0, 0, 0, 0.2) transparent;
  /* Firefox */
}

/* Webkit 浏览器滚动条淡化 */
.vks-tmp-objside::-webkit-scrollbar {
  width: 6px;
}

.vks-tmp-objside::-webkit-scrollbar-track {
  background: transparent;
}

.vks-tmp-objside::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  transition: background-color 0.3s ease;
}

.vks-tmp-objside::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.3);
}
</style>
