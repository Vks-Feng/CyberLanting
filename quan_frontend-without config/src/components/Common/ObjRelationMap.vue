<template>
    <h3>目标关系图</h3>
    <div ref="relationMapRef" style="width: 100%; height: 500px;"></div>
</template>

<script lang="ts" setup>
import * as echarts from 'echarts';
import { watch, ref, onMounted } from 'vue';
import store from '@/data/vuex-data';
import { ElNotification } from 'element-plus';
import objhubApi from '@/api/objhub-api';
import { getUserInfo } from '@/api/userinfo-api';

const relationMapRef = ref();
let chart: echarts.ECharts | null = null;

const formedData = ref(null);


function parseObjectivesToGraph(data: any) {
    if (data == null || data.length == 0) {
        ElNotification({
            message: '暂无数据',
            type: 'warning',
            duration: 2000,
        });
        return
    }

    const nodes = [];
    const links = [];
    const categories = [
        { name: "主目标" },
        { name: "副目标" },
        { name: "任务" }
    ];

    const id = ref(0)

    data.forEach((mainObjective) => {
        const mainObjId = id.value;

        // 添加主目标节点
        nodes.push({
            name: mainObjective.name,
            category: 0,
            value: mainObjective.id,
            // id: mainObjective.id
            id: id.value++
        });

        mainObjective.children.forEach((subObjective) => {
            const subObjId = id.value;

            // 添加副目标节点
            nodes.push({
                name: subObjective.name,
                category: 1,
                value: subObjective.id,
                // id: subObjective.id
                id: id.value++
            });

            // 连接主目标与副目标
            links.push({
                source: mainObjId,
                target: subObjId
            });

            subObjective.tasks.forEach((task) => {
                const taskId = id.value;
                // 添加任务节点
                nodes.push({
                    name: task.name,
                    category: 2,
                    value: task.id,
                    // id: task.id
                    id: id.value++
                });

                // 连接副目标与任务
                links.push({
                    source: subObjId,
                    target: taskId
                });
            });
        });
    });

    return { nodes, links, categories };
}

const initChart = async () => {
    if (!relationMapRef.value || !formedData.value) return;

    if (chart) {
        chart.dispose();
    }

    chart = echarts.init(relationMapRef.value);


    const option = {
        legend: {
            data: formedData.value.categories
        },
        color: ['#2F4F4F', '#708070', '#C0D6CD'], 
        series: [
            {
                type: 'graph',
                layout: 'force',
                animation: true,
                symbolSize: 15,
                label: {
                    position: 'right',
                    formatter: '{b}'
                },
                draggable: true,
                data: formedData.value.nodes.map(function (node, idx) {
                    node.id = idx;
                    return node;
                }),
                categories: formedData.value.categories,
                force: {
                    edgeLength: 20, // 调整边的长度（适当增加）
                    repulsion: 20, // 斥力，值越大，节点间距离越大
                    gravity: 0.1 // 重力，值越小，整体更松散
                },
                edges: formedData.value.links
            }
        ]
    };
    chart.setOption(option);
}

onMounted(() => {
    objhubApi.getUserObjectives(getUserInfo().id);
    initChart();
});


watch(
    [() => store.state.objList],
    ([newObjList]) => {
        if (newObjList && newObjList.length > 0) {
            console.log("formedData", formedData.value)
            formedData.value = parseObjectivesToGraph(newObjList);
            initChart()
        } else {
            console.log("NULL");
        }
    },
    { deep: true, immediate: true }
);


</script>