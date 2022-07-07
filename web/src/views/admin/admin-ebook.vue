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
            <!--一整行的数据-->
            <a-button type="primary" @click="edit(record)">
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
  <a-modal
      title="Title"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <!--弹出表单-->
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
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
       pagination.value.current = params.page;//一次多少
       pagination.value.total = data.content.total;//分页

     });
    };
    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const ebook = ref({});//表单
    const modalVisible = ref(false);//显示弹窗
    const modalLoading = ref(false);//时间加载
    const handleModalOk = () => {
      modalLoading.value = true;
      setTimeout(()=>{
        modalVisible.value=false;
        modalLoading.value=false;
      },2000);
    };

    /**
     * 编辑
     */
    const edit = (receord:any) => {
      modalVisible.value=true;
      ebook.value = receord;
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
      handleTableChange,

      edit,

      ebook,//ebook返回到html
      modalVisible,
      modalLoading,
      handleModalOk
    }
  }
});
</script>