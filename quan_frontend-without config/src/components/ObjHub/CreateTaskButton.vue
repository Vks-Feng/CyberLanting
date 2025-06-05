<template>
    <el-icon @click.stop="drawer = true">
        <Plus />
    </el-icon>

    <el-drawer :before-close="handleClose" v-model="drawer" title="I am the title" size='70%' :with-header="false">
        <p>{{ objId }}</p>
        <CreateTaskPage ref="createTaskPageRef" />
        <template #footer>
            <div class="dialog-footer">
                <el-button class="ink-drawer-button" @click="drawer = false">取消</el-button>
                <el-button class="ink-drawer-button" type="primary" @click="clickCreate">
                    创建
                </el-button>
            </div>
        </template>
    </el-drawer>

</template>

<script setup lang="ts">
import CreateTaskPage from './CreateTaskPage.vue';
import { ref } from 'vue';


const props = defineProps(['objId']);

const drawer = ref(false);
const createTaskPageRef = ref<{ submitForm: (string) => boolean, clear: () => void } | null>(null); // 定义 ref，确保类型安全

const handleClose = () => {
    drawer.value = false
    createTaskPageRef.value?.clear()
}

const clickCreate = async () => {
    const ans = await createTaskPageRef.value?.submitForm(props.objId)
    if (ans === true) {
        drawer.value = false
    }
}
</script>