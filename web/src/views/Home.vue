<template>
  <a-layout>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-sub-menu key="sub1">
        <template #title>
              <span>
                <user-outlined />
                发布文章
              </span>
        </template>
        <a-menu-item key="1">文章列表</a-menu-item>
        <a-menu-item key="2">option2</a-menu-item>
        <a-menu-item key="3">option3</a-menu-item>
        <a-menu-item key="4">option4</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub2">
        <template #title>
              <span>
                <laptop-outlined />
                  用户管理
              </span>
        </template>
        <a-menu-item key="5">option5</a-menu-item>
        <a-menu-item key="6">option6</a-menu-item>
        <a-menu-item key="7">option7</a-menu-item>
        <a-menu-item key="8">option8</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub3">
        <template #title>
              <span>
                <notification-outlined />
                系统设置
              </span>
        </template>
        <a-menu-item key="9">option9</a-menu-item>
        <a-menu-item key="10">option10</a-menu-item>
        <a-menu-item key="11">option11</a-menu-item>
        <a-menu-item key="12">option12</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
    <pre>{{ ebooks }}</pre>
    <pre>{{ ebooks2 }}</pre>
  </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent,onMounted,ref,reactive,toRef } from 'vue';//写上onMounted VUE3.0 setup集成了 导入ref 做响应式数据
import axios from 'axios';
export default defineComponent({
  name: 'Home',
  setup(){
      console.log("setup");
      const ebooks = ref();//响应式数据 获取的书籍实时反馈到页面上
      const ebooks1 = reactive({books:[]});//json数据，返回给books这个数组

      onMounted(()=> {
        console.log("onMounted");
        axios.get("http://localhost:8081/ebook/list?name=Vue").then((response) => {//获取后端接口数据
          const data =response.data;
          ebooks.value=data.content;
          ebooks1.books= data.content;
          console.log(response);
        });
      })
    return {
        ebooks,
        ebooks2: toRef(ebooks1,"books")
    }
  }
});
</script>
