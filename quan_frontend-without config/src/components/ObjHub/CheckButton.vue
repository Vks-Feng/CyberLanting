<template>
    <el-checkbox @click.stop="handleClick" v-model="checked" label="" size="large" />
</template>

<script lang="ts" setup>
import { watch ,ref ,defineProps ,onMounted} from 'vue'
import objhubApi from '@/api/objhub-api'
import { getUserInfo } from '@/api/userinfo-api'

const props = defineProps(['taskId','status'])
const emits = defineEmits(['check'])

const checked = ref(null)
var firstMount = true 

// 根据传进来的taskId获取任务状态，并设置checkbox的checked属性
onMounted(
    ()=>{
        checked.value = props.status === 'completed' ;
    }
)

watch(() => props.taskId,async (newId) => {
    if (!newId) return;
    try {
        const res = await objhubApi.getSingleTaskDetails(newId);
        checked.value = res.status === 'completed' ;
    } catch (error) { }
});

const handleClick = async () => {

    if (!checked.value) {
        const result = await submitComplte();
        if (result) {
            console.log('提交完成任务成功');
        }
    } else {
        const result = await submitCancel();
        if (result) {
            console.log('提交取消完成任务成功');
        }
    }
    objhubApi.getUserTodayTasks(getUserInfo().id);
    emits('check', checked.value);
}

// watch(checked, async (newValue) => {
//     if (firstMount) {
//         firstMount = false;
//         return;
//     }
//     if (newValue) {
//         const result = await submitComplte();
//         if (result) {
//             console.log('提交完成任务成功');
//         }
//     } else {
//         const result = await submitCancel();
//         if (result) {
//             console.log('提交取消完成任务成功');
//         }
//     }
//     objhubApi.getUserTodayTasks(getUserInfo().id);
//     emits('check', checked.value);
// });

const submitComplte = async () => {
    try {
        const data =
        {
            id: props.taskId,
        }
        const response = await objhubApi.completeTask(data);
    } catch (error) {
        console.error('提交完成任务失败:', error);
        return false;
    }
}

const submitCancel = async () => {
    try {
        const response = await objhubApi.cancelCompleteTask(props.taskId);
    } catch (error) {
        console.error('提交取消完成任务失败:', error);
        return false;
    }
}

</script>