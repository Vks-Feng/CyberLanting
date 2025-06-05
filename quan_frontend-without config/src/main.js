import { createApp } from 'vue'
// import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import VueApexCharts from 'vue3-apexcharts'

import store from '@/data/vuex-data'

import '@/assets/styles/global.css'

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(router);
app.use(ElementPlus,{locale:zhCn,});
app.use(VueApexCharts);

app.use(store);

app.mount("#app");
