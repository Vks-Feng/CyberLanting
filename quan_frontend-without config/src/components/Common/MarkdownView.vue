<template>
    <div class="markdown-body" v-html="compiledMarkdown"></div>
</template>

<script setup>
import { computed } from "vue";
import MarkdownIt from "markdown-it";
import MarkdownItTaskLists from "markdown-it-task-lists";
import MarkdownItKatex from "markdown-it-katex";
import MarkdownItTOC from "markdown-it-toc-done-right";
import hljs from "highlight.js";
import "highlight.js/styles/github-dark.css";
import "katex/dist/katex.min.css";

const props = defineProps({
content: {
    type: String,
    required: true,
},
});

// ✅ 创建 Markdown 解析实例
const md = new MarkdownIt({
html: true, // 允许 HTML
linkify: true, // 自动识别链接
typographer: true, // 更好的排版
breaks: true, // 换行时自动生成 <br>
highlight: (code, lang) => {
    if (lang && hljs.getLanguage(lang)) {
    return `<pre class="hljs"><code>${hljs.highlight(code, { language: lang }).value}</code></pre>`;
    }
    return `<pre class="hljs"><code>${code}</code></pre>`;
},
})
.use(MarkdownItTaskLists)
.use(MarkdownItKatex)
.use(MarkdownItTOC);

// ✅ 解析 Markdown 内容
const compiledMarkdown = computed(() => md.render(props.content || ""));
</script>

<style scoped>
/* 背景渐变效果 */
.markdown-body {
  font-family: "KaiTi", "SimHei", serif;
  font-size: 22px;
  line-height: 1.5;
  padding: 20px;

  /* 淡原浆纸底色 + 柔和渐变 */
  background-color: #fdfaf4;
  background-image: 
    url('https://cdn.jsdelivr.net/gh/chatgpt-pic/paper-texture-light.png'),
    radial-gradient(circle, #f7f2e8 5%, #fdfaf4 100%);
  background-size: cover;
  background-blend-mode: overlay;

  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
  word-break: break-word;
  position: relative;
  overflow: hidden;
}

/* 水墨风格的涟漪效果 */
.markdown-body::after {
content: "";
position: absolute;
top: 0;
left: 0;
width: 100%;
height: 100%;
background: url('path_to_ink_splash_image.png') no-repeat center center;
background-size: cover;
opacity: 0.1;
pointer-events: none;
}

/* 代码块样式 */
.hljs {
background: rgba(0, 0, 0, 0.1);
color: #333;
padding: 10px;
border-radius: 6px;
overflow-x: auto;
border: 1px solid #c0c0c0;
font-size: 14px; /* 适当缩小代码块的字体 */
}

/* 表格样式 */
.markdown-body table {
width: 100%;
border-collapse: collapse;
border: 1px solid #bbb;
margin-bottom: 20px;
}

.markdown-body th,
.markdown-body td {
border: 1px solid #bbb;
padding: 8px; /* 调整表格单元格的内边距 */
text-align: left;
}

.markdown-body th {
background-color: #e2e2e2;
font-weight: bold;
color: #333;
}

/* 数学公式 */
.katex {
font-size: 1.1em;
color: #333;
}

/* 让链接看起来更有水墨感 */
a {
color: #4a4a4a;
text-decoration: none;
border-bottom: 1px dashed #999;
}

a:hover {
color: #2c2c2c;
border-bottom: 1px solid #333;
}
</style>
