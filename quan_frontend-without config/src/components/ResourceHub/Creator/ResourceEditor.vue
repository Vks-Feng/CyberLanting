<template>

  <div class="resource-form">
    <el-scrollbar>
    <h1>发布资源</h1>

    <form @submit.prevent="submitForm">
      <!-- 标题 -->

      <div class="form-group">
        <label for="title">标题<span class="required">*</span></label>
        <input v-model="formData.title" id="title" type="text" required />
        <p v-if="!formData.title && isSubmitted" class="error">标题不能为空</p>
      </div>

      <!-- 分类 -->
      <div style="padding-left: 20px;">
        <label for="category">分类<span class="required">*</span></label>
        <select v-model="formData.category" id="category" style="width:100%" required>
          <option value="book">书籍</option>
          <option value="article">文章</option>
          <option value="course">课程</option>
          <option value="tool">工具</option>
          <option value="insight">笔记/心得</option>
        </select>
      </div>

      <!-- 描述 -->
      <div class="form-group">
        <label for="description">描述<span class="required">*</span></label>
        <textarea v-model="formData.description" id="description" required></textarea>
        <p v-if="!formData.description && isSubmitted" class="error">描述不能为空</p>
      </div>

      <!-- 链接 -->
      <div class="form-group">
        <label for="url">资源链接</label>
        <input v-model="formData.url" id="url" type="url" />
      </div>

      <!-- 内容：使用 Markdown 编辑器替代 textarea -->
      <div class="form-group">
        <label for="content">内容</label>
        <v-md-editor
          v-model="formData.content"
          height="300px"
          :disabled-menus="[]"
          @upload-image="handleUploadImage"
        />
      </div>

      <div class="form-group">
        <button type="submit">发布</button>
      </div>


    </form>

    <el-footer></el-footer>

    <!-- 预览
    <div class="preview-section">
      <h2>预览</h2>
      <MarkdownView :content="formData.content" />
    </div> -->
  </el-scrollbar>
  </div>

</template>

<script setup>
import { ref } from "vue";
import MarkdownView from "@/components/Common/MarkdownView.vue";
import fileApi from "@/api/file-api";
import resourceCenter from "@/api/resource-center";
import { ElNotification } from "element-plus";

// 引入 v-md-editor（GitHub 主题）
import VMdEditor from "@kangc/v-md-editor";
import VMdPreview from "@kangc/v-md-editor/lib/preview";
import githubTheme from "@kangc/v-md-editor/lib/theme/github.js";
import "@kangc/v-md-editor/lib/style/base-editor.css";
import "@kangc/v-md-editor/lib/theme/style/github.css";
import Prism from "prismjs";

// 注册主题
VMdEditor.use(githubTheme, { Prism });
VMdPreview.use(githubTheme, { Prism });

const formData = ref({
  title: "",
  category: "book",
  description: "",
  url: "",
  content: "",
});

const isSubmitted = ref(false);

const submitForm = () => {
  isSubmitted.value = true;

  if (!formData.value.title || !formData.value.description) {
    return;
  }

  resourceCenter.createResource(formData.value).then(() => {
    ElNotification({
      message: "资源发布成功",
      type: "success",
    });
    isSubmitted.value = false;
    formData.value = {
      title: "",
      category: "book",
      description: "",
      url: "",
      content: "",
    };
  });
};

// 配置 Markdown 编辑器上传图片
const handleUploadImage = async (event, insertImage, file) => {

  if (!file) {
    ElNotification({
      message: "请选择文件再上传。",
      type: "error",
    });
    return;
  }

  try {
    console.log("上传文件:", file);
    console.log(file)

    const res = await fileApi.uploadFile(file[0]); // 上传文件
    
    insertImage({
      url:res,
      desc: 'description',
    })


  } catch (error) {
    console.error("图片上传失败:", error);
    ElNotification({
      message: error.response?.data?.message || "图片上传失败，请重试。",
      type: "error",
    });
    return ""; // 返回空字符串，上传失败时，插入图片会失败
  }
}

</script>


<style scoped>
.resource-form {
  width: auto;
  height: 80vh;
  margin: 50px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease-in-out;
  overflow-y: scroll;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin:20px;
  margin-bottom: 16px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 6px;
}

.required {
  color: red;
  /* margin-left: 4px; */
}

input,
select,
textarea {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 6px;
  transition: border-color 0.3s;
}

input:focus,
select:focus,
textarea:focus {
  border-color: #4caf50;
  outline: none;
}

button {
  width: 100%;
  padding: 12px;
  font-size: 18px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #45a049;
}

.preview-section {
  margin-top: 20px;
  padding: 16px;
  margin-left: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.preview-section h2 {
  font-size: 20px;
  margin-bottom: 10px;
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 4px;
}

</style>
