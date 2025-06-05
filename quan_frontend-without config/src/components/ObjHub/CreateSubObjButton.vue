<template>
    <el-button class="sidebar-icon-btn" @click="drawer = true">
        <div class="icon-text-row">
            <el-icon>
                <Plus />
            </el-icon>
            <span class="btn-text">{{ text }}</span>
        </div>
    </el-button>


    <el-drawer :before-close="clickCancel" v-model="drawer" title="I am the title" size="70%" :with-header="false">
        <CreateObjPage ref="createObjPageChildRef" @update="update" />
        <template #footer>
            <div class="dialog-footer">
                <el-button class="ink-drawer-button" @click="clickCancel">取消</el-button>
                <el-button class="ink-drawer-button" type="primary" @click="clickCreate">
                    创建
                </el-button>
            </div>
        </template>
    </el-drawer>
</template>

<script setup lang="ts">
import { ElNotification } from "element-plus";
import CreateObjPage from "./CreateObjPage.vue";
import { defineProps, ref, defineEmits } from "vue";

const props = defineProps(["fatherObjId", "type", "text"]);
const emit = defineEmits(["update"]);

function update() {
    emit("update"); // 层层上传
    // console.log('update at CreateObjButton')
}

const createObjPageChildRef= ref<{
    submitForm: (fatherObjId: string, type: string) => boolean;
    clear: () => void;
} | null>(null); // 定义 ref，确保类型安全

const drawer = ref(false); //drawer是否显示
const clickCreate = async () => {
    console.log("father",props.fatherObjId)
    console.log("type",props.type)
    const ans = await createObjPageChildRef.value?.submitForm(
        props.fatherObjId,
        props.type
    );

    if (ans === true) {
        drawer.value = false;
        createObjPageChildRef.value?.clear();
    } else {
        console.error("Error in CreateSubObjButton")
    }
};

const clickCancel = () => {
    drawer.value = false;
    createObjPageChildRef.value?.clear();
};
</script>

<style scoped>
.sidebar-icon-btn {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    width: 30%;
    height: 20%;
    margin: 10px auto;
    padding: 10px 16px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-radius: 12px;
    background-color: rgba(255, 255, 255, 0.4); /* 半透明背景 */
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.1s ease;
}

.sidebar-icon-btn:hover {
    background-color: rgba(255, 255, 255, 0.2);
    transform: scale(1.02);
}

.icon-text-row {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.icon-text-row .el-icon {
    font-size: 22px;
    margin-right: 10px;
    color: #333;
}

.btn-text {
    font-size: 15px;
    color: #333;
}

.sidebar-icon-btn:focus,
.sidebar-icon-btn:active {
    outline: none;
    border: 1px solid rgba(0, 0, 0, 0.2);
}
</style>
