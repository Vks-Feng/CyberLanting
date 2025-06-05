<template>
    <!-- <el-button class="sidebar-btn ink-button"  plain @click="drawer = true">
        {{text}}
    </el-button> -->
    <el-button class="sidebar-icon-btn"  @click="drawer = true">
        <div class="icon-text-column">
            <el-icon style="font-size: 25px; margin-top: 3px;">
                <Plus />
            </el-icon>
            <span class="btn-text">{{text}}</span>
        </div>
    </el-button>

    <el-drawer :before-close="clickCancel" v-model="drawer" title="I am the title" size="70%" :with-header="false">
        <CreateObjPage ref="createObjPageRef" @update="update" />
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

const createObjPageRef = ref<{
    submitForm: (fatherObjId: string, type: string) => boolean;
    clear: () => void;
} | null>(null); // 定义 ref，确保类型安全

const drawer = ref(false); //drawer是否显示

const clickCreate = async () => {
    const ans = await createObjPageRef.value?.submitForm(
        props.fatherObjId,
        props.type
    );
    console.log("ans", ans);
    if (ans === true) {
        drawer.value = false;
        createObjPageRef.value?.clear();
    } else {
        console.error("Error in CreateObjButton")
    }
};

const clickCancel = () => {
    drawer.value = false;
    createObjPageRef.value?.clear();
};
</script>

<style scoped>
.sidebar-btn {
    display: block;
    width: 90%;
    margin: 10px 10px 10px 12px;
    background: transparent !important;
    /* background-color: transparent; */
}
</style>
