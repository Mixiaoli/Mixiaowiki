import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import * as Icons from '@ant-design/icons-vue';
//导入Ant
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
//配置环境地址
import axios from 'axios'
import {Tool} from "@/util/tool";

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

//集成第三方插件Vue都是在main.ts里配置
//链式代码 改掉
const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

/**
 * axios拦截器
 * 去store把token拿出来 不为空就加入一个header增加这个token 打印日志
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数:', config);
    const token = store.state.user.token;
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("请求headers增加token:", token);
    }
    return config;
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回参数', response);
    return response;
}, error => {
    console.log('返回错误:', error);
    return Promise.reject(error);
});

// 全局使用图标
const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}
//测试多环节是否成功
console.log('环境', process.env.NODE_ENV);
console.log('地址', process.env.VUE_APP_SERVER);