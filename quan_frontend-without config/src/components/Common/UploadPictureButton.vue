<template>
  <el-button style="width:100%" class="sidebar-btn ink-button" plain @click="dialog = true">
    上传图片
  </el-button>


  <el-dialog :before-close="clickCancel" v-model="dialog" title="图片上传" size='80%' :with-header="false">

    <div class="upload-container">
      <file-pond name="file" label-idle="拖拽文件到这里或点击上传(文件小于1MB)" allow-multiple="false" accepted-file-types="image/*"
        :server="serverOptions" :instant-upload="true" @processfile="handleUploadSuccess" @processfilerevert=""/>
      <div v-if="imageLink != null" class="result-container">
        <input class="link-result" v-model="imageLink" type="text" disabled />
        <button @click="copyToClipboard(imageLink)" class="ink-button">
          {{ "复制链接" }}
        </button>
      </div>
    </div>

    <div class="image-container">
      <el-popover v-for="(url, index) in imageUrls" class="box-item" title="" content="Right Top prompts info"
        placement="left">
        <el-image :src="url" />
        <template #reference>
          <el-button @click="copyToClipboard(url)" style="margin:0 ; width:100%" class="ink-button">图片{{ index + 1
            }}</el-button>
        </template>
      </el-popover>
    </div>
  </el-dialog>

</template>

<script setup>
import { ref } from "vue";
import vueFilePond from "vue-filepond";
import "filepond/dist/filepond.min.css";
import { ElNotification } from "element-plus";
import { serverAddress } from "@/api/axios-api";

// 创建 FilePond 组件
const FilePond = vueFilePond();
const token = localStorage.getItem('token')
const imageLink = ref("");
const imageUrls = ref([]);
const dialog = ref(false);

// 服务器上传配置
const serverOptions = {
  process: {
    // url: serverUrl+"files/upload",
    url: `${serverAddress}/files/upload`,
    method: "POST",
    withCredentials: false,
    headers: {
      Authorization: `Bearer ${token}`, // 如果需要认证
    },
    onload: (response) => JSON.parse(response).data, // 解析返回的 URL
  },
};

// 处理上传成功事件
const handleUploadSuccess = (error, file) => {
  if (!error) {
    ElNotification.success({
      message: `文件上传成功,文件URL为:${file.serverId}`,
    });
    imageLink.value = file.serverId
    imageUrls.value.push(file.serverId)
  }
  else {
    ElNotification.error({
      message: error,
    });

  }
};

// 复制到剪贴板
const copied = ref(false); // 复制成功状态
const copyToClipboard = async (link) => {
  if (!link) return; // 避免复制空内容
  try {
    await navigator.clipboard.writeText(`![](${link})`);
    copied.value = true;
    setTimeout(() => (copied.value = false), 2000); // 2秒后恢复按钮状态
    ElNotification.success({
      message: "可以直接粘贴到MarkDown中进行使用"
    });

  } catch (err) {
    console.error("复制失败:", err);
  }
};

const clickCancel = () => {
  dialog.value = false;
  imageLink.value = "";
};

</script>

<style>
.upload-container {
  width: 400px;
  margin: 20px auto;
}

.result-container {
  width: auto;
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap:10%;
}

.link-result {
  background-color: #f5f5f5;
  /* color: var(--paper-white); */
  border: 2px solid black;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
  cursor: pointer;
  font-family: Arial, serif;
  font-size: 18px;
  height: auto;
}

.image-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 10px;
}
</style>
