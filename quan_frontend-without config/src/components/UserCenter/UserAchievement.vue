<template>
    <el-timeline style="max-width: 600px">
        <el-timeline-item v-for="(activity, index) in activities" :key="index" :timestamp="activity.timestamp"
            :color="activity.color" :icon="activity.icon ? activity.icon : undefined">
            <div class="content">
                <img v-if="activity.iconUrl" :src="activity.iconUrl" class="icon" />
                <div class="text">
                    <div class="name">{{ activity.name }}</div>
                    <div class="description">{{ activity.description }}</div>
                </div>
            </div>
        </el-timeline-item>
    </el-timeline>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { getUserAchievements } from '@/api/userinfo-api'

interface Achievement {
    name: string
    description: string
    iconUrl?: string
    achievedAt: string
}

interface TimelineItem {
    name: string
    description: string
    iconUrl?: string
    timestamp: string
    color?: string
    icon?: any
}

const activities = ref<TimelineItem[]>([])



const raw_data =
    [
        {
            "name": "启途之印",
            "description": "创建第一个目标",
            "iconUrl": "https://cyberlanting-quan.oss-cn-hangzhou.aliyuncs.com/1143613c-08d7-4fc0-a612-5d92834b3614.png",
            "achievedAt": "2025-04-07T08:55:24"
        },
        {
            "name": "星火初燃",
            "description": "完成了第一个任务，星辰之火，自此点燃。",
            "iconUrl": "https://cyberlanting-quan.oss-cn-hangzhou.aliyuncs.com/e7a95d66-1fd2-4d77-ab68-cb5e6f2fb98b.png",
            "achievedAt": "2025-04-08T10:33:21"
        },
        {
            "name": "星轨初成",
            "description": "累计完成五个任务，星轨初织，修行初定",
            "iconUrl": "https://cyberlanting-quan.oss-cn-hangzhou.aliyuncs.com/c49812ba-94d1-4f33-985d-95488d058c36.png",
            "achievedAt": "2025-04-08T10:33:21"
        },
        {
            "name": "筑语初鸣",
            "description": "第一次分享资源，言出成筑。",
            "iconUrl": "https://cyberlanting-quan.oss-cn-hangzhou.aliyuncs.com/6f2127eb-796f-469a-bc04-ef69d9843054.png",
            "achievedAt": "2025-04-08T11:06:17"
        },
        {
            "name": "筑庐传灯",
            "description": "发布五个资源，并收到至少一条评论，学识有光，愿与人共。",
            "iconUrl": "https://cyberlanting-quan.oss-cn-hangzhou.aliyuncs.com/1b4a0fde-57e7-4a66-89de-fc41e82f5040.png",
            "achievedAt": "2025-04-08T11:06:17"
        }
    ]


onMounted(async () => {
    const list: Achievement[] = await getUserAchievements()
    // const list = raw_data;
    activities.value = list.map(item => ({
        name: item.name,
        description: item.description,
        iconUrl: item.iconUrl,
        timestamp: new Date(item.achievedAt).toLocaleString(),
        color: 'rgba(92,138,110,0.9)', // 可根据条件定制颜色
    }))
})
</script>

<style scoped>
.content {
    display: flex;
    align-items: center;
}

.icon {
    width: 40px;
    height: 40px;
    margin-right: 12px;
    border-radius: 8px;
}

.text {
    display: flex;
    flex-direction: column;
}

.name {
    font-weight: bold;
    margin-bottom: 4px;
}

.description {
    color: #666;
    font-size: 14px;
}
</style>
