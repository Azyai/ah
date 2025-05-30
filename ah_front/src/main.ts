
import './assets/main.css'
import 'element-plus/dist/index.css' // 全局样式
import 'element-plus/es/components/message/style/css' // Message 样式
import 'element-plus/es/components/notification/style/css'
import 'nprogress/nprogress.css';

import {createPinia} from 'pinia'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { size: 'small', zIndex: 3000 })

app.mount('#app')