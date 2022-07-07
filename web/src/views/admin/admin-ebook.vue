<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <!--列,key id,数据ebook,分页,等待框,分页执行方法-->
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{text:cover}">
            <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="primary">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';//写上onMounted VUE3.0 setup集成了 导入ref 做响应式数据
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup(){
    const ebooks = ref();//响应式数据 获取的书籍实时反馈到页面上
    const pagination =ref({
      current:1,//当前页
      pageSize:4,//分页条数
      total:0
    });
    const loading = ref(false);

    const  columns =[
      {
        title:'封面',
        dataIndex:'cover',
        slots:{customRender:'cover'}
      },
      {
        title:'名称',
        dataIndex:'name',
      },
      {
        title:'分类一',
        key:'category1id',
        dataIndex:'category1Id',
      },
      {
        title:'分类二',
        dataIndex:'category2id',
      },
      {
        title:'阅读数',
        dataIndex:'viewCount',
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title:'Action',
        key: 'action',
        slots:{customRender:'action'}
      }
    ];
    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
     axios.get("/ebook/list",{
       params:{
         page:params.page,
         size:params.size
       }
     }).then((response) =>{
       loading.value=false;
       const data = response.data;
       ebooks.value = data.content.list;
       //重置分页按钮
       pagination.value.current = params.page;
       pagination.value.total = data.content.total;

     });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };
    onMounted(() => {
      handleQuery({
        page:1,
        size:pagination.value.pageSize
      });
    });
    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
    }
  }
});
</script>