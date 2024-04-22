import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios"
import '@/assets/css/elemenet.less'
import 'flag-icon-css/css/flag-icons.min.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import {createPinia} from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

axios.defaults.baseURL = '/api'
// axios.defaults.baseURL = 'http://localhost:8080/api'


const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
pinia.use(piniaPluginPersistedstate)
app.use(router)

app.mount('#app')
