<template>
</template>

<script setup>
import { getUserInfo } from '@/api/userinfo-api';
import { ElNotification } from 'element-plus';
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { defineExpose } from 'vue';
import objhubApi from '@/api/objhub-api';
import { serverIp} from '@/api/axios-api';

const message = ref('');
const messages = ref([]);
const clientId = getUserInfo().id;


let websocket = null;

// 初始化WebSocket连接
const initWebSocket = () => {
    console.log('初始化WebSocket')
    if ('WebSocket' in window) {
        websocket = new WebSocket(`ws://${serverIp}/ws/${clientId}`);

        websocket.onerror = (e) => {
            setMessageInnerHTML("error");
            console.log(e);
        };

        websocket.onopen = () => {
            setMessageInnerHTML("连接成功");
        };

        websocket.onmessage = (event) => {
            // setMessageInnerHTML(event.data);
            console.log("get response", event);
            ElNotification({
                message: event.data,
                type: 'success',
                duration: 3000,
            });
            objhubApi.getUserObjectives(clientId);
            closeWebSocket();
            // 按道理此时Server会关闭Socket
            // 此处为了保险我们再次进行关闭
            console.log('WebSocket已关闭');
        };

        websocket.onclose = () => {
            setMessageInnerHTML("close");
        };
    } else {
        alert('Not support websocket');
    }
};

// 设置消息内容
const setMessageInnerHTML = (innerHTML) => {
    messages.value.push(innerHTML);
};

// 发送消息
const send = () => {
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(message.value);
        message.value = ''; // 清空输入框
    }
};

// 关闭WebSocket连接
const closeWebSocket = () => {
    console.log('WebSocket已关闭');
    if (websocket) {
        websocket.close();
    }
};


// 监听窗口关闭事件
// onBeforeUnmount(() => {
//     closeWebSocket();
//     console.log('窗口关闭，WebSocket已关闭');
// });

// 组件挂载时初始化WebSocket
onMounted(() => {
    initWebSocket();
});

defineExpose({ send ,closeWebSocket});

</script>
