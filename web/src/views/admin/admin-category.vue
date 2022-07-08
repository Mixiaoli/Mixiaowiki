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
            <a-button type="primary" @click="handleQuery()">
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
      <!--列,key id,数据category,分页,等待框,分页执行方法-->      <!--pagintaion是否要分页 这里是否-->
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
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
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <!--弹出表单-->
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent,onMounted,ref } from 'vue';//写上onMounted VUE3.0 setup集成了 导入ref 做响应式数据
import axios from 'axios';
import {message}  from "ant-design-vue";//ant ui 消息组件
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  setup(){
    const param = ref();
    param.value={};
    const categorys = ref();//响应式数据 获取的书籍实时反馈到页面上
    const loading = ref(false);

    const  columns =[
      {
        title:'名称',
        dataIndex:'name',
      },
      {
        title:'父分类',
        key:'parent',
        dataIndex:'parent',
      },
      {
        title:'顺序',
        dataIndex:'sort',
      },
      {
        title:'Action',
        key: 'action',
        slots:{customRender:'action'}
      }
    ];
    /**
     * 一级分类树，children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const  level1 =ref();
    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
     axios.get("/category/all",).then((response) =>{
       loading.value=false;
       const data = response.data;
        if (data.success){
          categorys.value = data.content;
          console.log("原始数组：", categorys.value);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);//调用tool.ts做树形结构
          console.log("树形结构：", level1);
        }else{
          message.error(data.message);
        }
     });
    };
    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const category = ref({});//表单
    const modalVisible = ref(false);//显示弹窗
    const modalLoading = ref(false);//时间加载
    const handleModalOk = () => {
      modalLoading.value = true;
      //下面那个category就是 category=ref()绑定到表单的category
      axios.post("/category/save",category.value).then((response) =>{
        modalLoading.value=false;
        const data = response.data;//data = commonResp 返回提交的业务是成功的话success=true
        if (data.success){
          modalVisible.value=false;
          modalLoading.value=false;
          //重新加载列表
          handleQuery();
        }else{
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record:any) => {
      modalVisible.value=true;
      category.value = Tool.copy(record);
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value=true;
      category.value = {};
    };
    /**
     * 删除
     */
    const handleDelete = (id : number) => {
      axios.delete("/category/delete/" + id ).then((response) =>{
        const data = response.data;//data = commonResp 返回提交的业务是成功的话success=true
        if (data.success){
          //重新加载列表
          handleQuery();
        }
      });
    };
    onMounted(() => {
      handleQuery();
    });
    return {
      param,
      level1,
      //categorys,//表格
      columns,
      loading,
      handleQuery,

      edit,//表单
      add,
      category,//category返回到html
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