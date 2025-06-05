<template>
    <el-icon @click.stop="click" size="35">
        <Delete />
    </el-icon>
</template>


<script setup>
import objhubApi from '@/api/objhub-api';

const emit = defineEmits(['deleteDone'])

const click = async() => {
    if (props.type === 'task') {
        objhubApi.deleteTask(props.id).then(() => {
        }).catch((error) => {
            console.error('delete task error', error);
        })
    }
    else if (props.type === "obj") {
        objhubApi.deleteObj(props.id).then(() => {
        }).catch((error) => {
            console.error('delete obj error', error);
        })
    }
    else {
        console.error('delete type error');
    }
    emit('deleteDone')
}

const props = defineProps(['id', 'type'])

</script>