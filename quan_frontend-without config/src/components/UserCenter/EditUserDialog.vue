<template>
  <el-dialog v-model="localVisible" title="编辑资料" width="50vw" @close="handleClose">

    <el-row>

      <el-col :span="8" style="display: flex; justify-content: center; align-items: center;">
        <el-avatar :size="100" :src="HeaderUrl" />
      </el-col>

      <el-col :span="16">
        <div class="upload-container">
          <file-pond name="file" label-idle="上传头像(小于1MB)" allow-multiple="false" accepted-file-types="image/*"
            :server="serverOptions" :instant-upload="true" @processfile="handleUploadSuccess" @processfilerevert="" />
        </div>
      </el-col>

    </el-row>

    <el-form :model="localForm" label-width="80px">

      <el-form-item label="昵称">
        <el-input v-model="localForm.nickname" />
      </el-form-item>

    </el-form>

    <template #footer>
      <el-button class="ink-button" @click="handleClose">取消</el-button>
      <el-button class="ink-button" type="primary" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { getUserInfo } from '@/api/userinfo-api'
import { onMounted, reactive, ref, watch } from 'vue'
import vueFilePond from "vue-filepond";
import "filepond/dist/filepond.min.css";
import { ElNotification } from "element-plus";
import { updateUserInfo } from '@/api/userinfo-api'
import { serverAddress} from '@/api/axios-api';

const props = defineProps({
  modelValue: Boolean,
})
const emit = defineEmits(['update:modelValue', 'update:form', 'submit', 'close','refresh'])
const HeaderUrl = ref("")
const localVisible = ref(false)

onMounted(() => {
  HeaderUrl.value = getUserInfo().avatarUrl
})

// https://cyberlanting-quan.oss-cn-hangzhou.aliyuncs.com/f23cb115-2cc5-4bd5-bb09-5daad9c24573.jpg

watch(() => props.modelValue, val => {
  localVisible.value = val
})


interface UserInfo {
  nickname: string;
  avatarUrl: string;
}


const localForm = reactive<UserInfo>({
  nickname: getUserInfo().nickname,
  avatarUrl: "call me baby! ok , unused..."
})

// 同步关闭事件
const handleClose = () => {
  emit('close')
}

// 提交编辑
const handleSubmit = async () => {
  if(localForm.nickname===getUserInfo().nickname && HeaderUrl.value===getUserInfo().avatarUrl){
    emit('close')
    return
  }

  try {
    const response = await updateUserInfo(localForm.nickname,HeaderUrl.value);
    emit('close')
    emit('refresh')  //触发UserInfoCard的更新
  } catch (error) {
    console.error(error);
  }
}

// 创建 FilePond 组件
const FilePond = vueFilePond();
const token = localStorage.getItem('token')
const imageLink = ref("");

// 服务器上传配置
const serverOptions = {
  process: {
    // url: "http://47.96.136.70:8081/files/upload",
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
    HeaderUrl.value = file.serverId
    console.log("图片URL", imageLink.value)
  }
  else {
    ElNotification.error({
      message: `上传失败,失败代码${error.code}`,
    });

  }
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
  gap: 10%;
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
