<template>
    <div>
        <h1>欢迎回来，主人！</h1>
        <div class="dashboard">
            <div class="card full-width">
                <h2>今日任务概览</h2>
                <p>完成任务：{{ taskCompletion }}/{{ totalTasks }} </p>
                <!-- <progress value="60" max="100"></progress> -->
                <el-progress :percentage="(taskCompletion / totalTasks * 100).toFixed(2)" />
            </div>
            <div class="card">
                <h2> 任务列表</h2>
                <TodayTaskPage />
            </div>
            <div class="card">
                <h2>目标进度</h2>
                <div v-for="obj in objList">
                    <p>{{ obj.name }}</p>
                    <el-progress :percentage="(obj.progress * 100).toFixed(2)" />
                </div>
            </div>
            <div class="card">
                <h2>社交动态</h2>
                <p>你的好友小明完成了一个新目标！🎉</p>
                <p>小红分享了一篇新文章：《高效学习技巧》📖</p>
                <p>张三新增了一个挑战：每周阅读一本书 📚</p>
            </div>
            <div class="card">
                <h2>排行榜</h2>

                <ol>
                    <li v-for="(user, index) in rankList" :key="user.username">
                        <span v-if="index === 0">🏆</span>
                        <span v-else-if="index === 1">🥈</span>
                        <span v-else-if="index === 2">🥉</span>
                        {{ user.username }} - {{ user.completeCount }} 任务完成
                    </li>
                </ol>
            </div>
            <div class="card full-width">
                <h2>学习资源推荐</h2>
                <p>📘 《深入理解Spring Boot 3》 - 推荐指数 ⭐⭐⭐⭐⭐</p>
                <p>📙 《算法导论》 - 推荐指数 ⭐⭐⭐⭐</p>
                <p>📗 《Java 并发编程实战》 - 推荐指数 ⭐⭐⭐⭐⭐</p>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
// 这里可以根据需要添加逻辑代码，比如获取任务、社交动态等数据
import TodayTaskPage from '@/components/ObjHub/TodayTaskPage.vue';
import { ref, onMounted, watch } from 'vue';
import objhubApi from '@/api/objhub-api';
import { getUserInfo } from '@/api/userinfo-api';
import store from '@/data/vuex-data';
import peerApi from '@/api/peer-api';

const taskList = ref(null);
const taskCompletion = ref(3);
const totalTasks = ref(5);
const objList = ref(null);
const rankList = ref(null);

onMounted(async () => {
    try {
        taskList.value = await objhubApi.getUserTodayTasks(getUserInfo().id)
        await objhubApi.getUserObjectives(getUserInfo().id);
        rankList.value = await peerApi.getTaskRankList('monthly');
    } catch (error) {
        console.log(error);
    }
}
)

watch(
    [() => store.state.todayTaskList],
    ([newTaskList]) => {
        totalTasks.value = newTaskList.length;
        taskCompletion.value = newTaskList.filter((task) => task.status === 'completed').length;
    }
)

watch(
    [() => store.state.objList],
    ([newObjList]) => {
        objList.value = newObjList;
    }
)

const taskProgress = ref(60);

const goals = ref([
    { name: '目标 1', progress: 80 },
    { name: '目标 2', progress: 50 },
]);

const socialUpdates = ref([
    '你的好友小明完成了一个新目标！🎉',
    '小红分享了一篇新文章：《高效学习技巧》📖',
    '张三新增了一个挑战：每周阅读一本书 📚',
]);

const leaderboard = ref([
    { name: '王五', completedTasks: 20 },
    { name: '李四', completedTasks: 18 },
    { name: '小明', completedTasks: 15 },
]);

const resources = ref([
    { title: '深入理解Spring Boot 3', rating: 5 },
    { title: '算法导论', rating: 4 },
    { title: 'Java 并发编程实战', rating: 5 },
]);
</script>

<style scoped>
body {
    font-family: Arial, sans-serif;
    background-color: #f5f5f5;
    margin: 0;
    padding: 20px;
}

.dashboard {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 20px;
    max-width: 1200px;
    margin: auto;
}

.card {
    background: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.full-width {
    grid-column: span 2;
}
</style>
