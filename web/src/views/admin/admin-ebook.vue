<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
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
            <img class="img-wh" v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <!--一整行的数据-->
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复,确定要删除吗?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="primary">
                删除
              </a-button>
            </a-popconfirm>
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
import {message}  from "ant-design-vue";//ant ui 消息组件

export default defineComponent({
  name: 'AdminEbook',
  setup(){
    const param = ref();
    param.value={};
    const ebooks = ref();//响应式数据 获取的书籍实时反馈到页面上
    const pagination =ref({
      current:1,//当前页
      pageSize:10,//分页条数
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
        dataIndex:'category2Id',
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
         size:params.size,
         name:param.value.name,//从响应式变量拿来的
       }
     }).then((response) =>{
       loading.value=false;
       const data = response.data;
        if (data.success){
          ebooks.value = data.content.list;
       ebooks.value = data.content.list;
       //重置分页按钮
       pagination.value.current = params.page;//页码
       pagination.value.total = data.content.total;//页数
        }else{
          message.error(data.message);
        }
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
      //下面那个ebook就是 ebook=ref()绑定到表单的ebook
      axios.post("/ebook/save",ebook.value).then((response) =>{
        modalLoading.value=false;
        const data = response.data;//data = commonResp 返回提交的业务是成功的话success=true
        if (data.success){
          modalVisible.value=false;
          modalLoading.value=false;
          //重新加载列表
          handleQuery({
            page:pagination.value.current,//所在页码
            size:pagination.value.pageSize//一次显示多少
          });
        }else{
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (receord:any) => {
      modalVisible.value=true;
      ebook.value = receord;
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value=true;
      ebook.value = {};
    };
    /**
     * 删除
     */
    const handleDelete = (id : number) => {
      axios.delete("/ebook/delete/" + id ).then((response) =>{
        const data = response.data;//data = commonResp 返回提交的业务是成功的话success=true
        if (data.success){
          //重新加载列表
          handleQuery({
            page:pagination.value.current,//所在页码
            size:pagination.value.pageSize,//一次显示多少
          });
        }
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
      param,
      ebooks,//表格
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,//表单
      add,
      ebook,//ebook返回到html
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete
    }
  }
});
</script>

<!-- #scoped表示当前组件才有用 -->
<style scoped>
.img-wh {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>