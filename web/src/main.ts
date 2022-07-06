import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//导入Ant
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
//集成第三方插件Vue都是在main.ts里配置

createApp(App).use(store).use(router).use(Antd).mount('#app')

