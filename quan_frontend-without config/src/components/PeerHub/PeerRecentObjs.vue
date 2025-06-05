<template>

  <el-row class="tac">
    <el-col :span="24">

      <el-menu class="el-menu-vertical-demo">

        <div v-for="(item, index) in objs">
          <el-row :gutter="4">
            <el-col :span="10">
              <span>{{ item.name }}</span>
            </el-col>

            <el-col :span="14">
              <el-progress :percentage="item.progress * 100"  :stroke-width="12" />
            </el-col>
          </el-row>
        </div>

      </el-menu>
    </el-col>
  </el-row>

</template>


<script setup>
import { defineProps, ref, watch } from 'vue';
import objhubApi from '@/api/objhub-api';

const objs = ref([]);
const props = defineProps({
  id: {
    type: Object,
  },
});

watch(
  [() => props.id],
  async ([newId]) => {
    if (!newId) return;
    try {
      const newObjList = await objhubApi.getPeerObjectives(newId);
      if (newObjList==null) {
        objs.value = [];
      }
      objs.value = newObjList;

    }
    catch (error) {
      console.error("Error fetching user objectives:", error);
    }
  },
  { deep: true, immediate: true }
);


</script>