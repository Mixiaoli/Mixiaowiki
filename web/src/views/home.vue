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
    <!--内容列表: grid gutter为间距,column一行变三列-->
    <a-list item-layout="vertical" size="large" :grid="{gutter:20,column:3}" :data-source="ebooks">
      <template #renderItem="{ item }"> <!--item 表示 一个个的数据 可以自定义 自动循环ebooks的数据出来-->
        <a-list-item key="item.name"><!--访问数据 这边拿name-->
          <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
          </template>
          <a-list-item-meta :description="item.description">
            <template #title>
              <a :href="item.href">{{ item.name }}</a>
            </template>
            <template #avatar><a-avatar :src="item.cover" /></template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list>

  </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';//写上onMounted VUE3.0 setup集成了 导入ref 做响应式数据
import axios from 'axios';

//内容列表的typescript代码 为listdata初始化24条数据 测试用注释吊
//const listData: any = [];

// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: 'https://www.antdv.com/',
//     title: `ant design vue part ${i}`,
//     avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
//     description:
//         'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//     content:
//         'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//   });
// }

export default defineComponent({
  name: 'Home',
  setup(){
      const ebooks = ref();//响应式数据 获取的书籍实时反馈到页面上
      //const ebooks1 = reactive({books:[]});//json数据，返回给books这个数组

      onMounted(()=> {
        axios.get("/ebook/list",{
          params:{
            page:1,
            size:1000
          }
        }).then((response) => {//获取后端接口数据
          const data =response.data;//定义常量data
          ebooks.value=data.content.list;
          //不需要分页
         // ebooks1.books= data.content;
        });
      })
    return {
        ebooks,
        //ebooks2: toRef(ebooks1,"books"),
        //listData,//内容列表的代码
        pagination:{
        onChange: (page: number) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions:[
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ],
    }
  }
});
</script>

<!-- #scoped表示当前组件才有用 -->
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>