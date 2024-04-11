import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios"
import '@/assets/css/elemenet.less'
import 'flag-icon-css/css/flag-icons.min.css'
import 'element-plus/theme-chalk/dark/css-vars.css'

axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)

app.use(router)

app.mount('#app')
