<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row>
        <a-col :span="8">
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
          <!--列,key id,数据doc,分页,等待框,分页执行方法-->      <!--pagintaion是否要分页 这里是否-->
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
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="primary">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>

        <a-col :span="16">
            <!--弹出表单-->
            <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-form-item label="名称">
                <a-input v-model:value="doc.name" placeholder="名称"/>
              </a-form-item>
              <a-form-item label="父文档">
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="treeSelectData"
                    placeholder="请选择父文档"
                    tree-default-expand-all
                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                >
                </a-tree-select>
              </a-form-item>
              <a-form-item label="顺序">
                <a-input v-model:value="doc.sort" />
              </a-form-item>
              <a-form-item label="内容">
                <div id="content">

                </div>
              </a-form-item>
            </a-form>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
<!--  <a-modal
      title="文档表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  ></a-modal>-->
</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';//写上onMounted VUE3.0 setup集成了 导入ref 做响应式数据
import axios from 'axios';
import {message, Modal} from "ant-design-vue";//ant ui 消息组件
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from 'wangeditor';

export default defineComponent({
  name: 'AdminDoc',
  setup(){
    const route = useRoute();//导入路由
    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);
    const param = ref();
    param.value={};
    const docs = ref();//响应式数据 获取的书籍实时反馈到页面上
    const loading = ref(false);

    const  columns =[
      {
        title:'名称',
        dataIndex:'name',
      },
      {
        title:'父文档',
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
     * 一级文档树，children属性就是二级文档
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
     axios.get("/doc/all").then((response) =>{
       loading.value=false;
       const data = response.data;
        if (data.success){
          docs.value = data.content;
          console.log("原始数组：", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);//调用tool.ts做树形结构
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
        // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const doc = ref({});//表单
    doc.value = {
      ebookId: route.query.ebookId
    };
    const treeSelectData = ref();
    treeSelectData.value = [];
    const modalVisible = ref(false);//显示弹窗
    const modalLoading = ref(false);//时间加载
    const editor = new E('#content');

    const handleModalOk = () => {
      modalLoading.value = true;
      //下面那个doc就是 doc=ref()绑定到表单的doc
      axios.post("/doc/save",doc.value).then((response) =>{
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
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };
    /**
     * 查找整根树枝 查找后来删除做操作
     */
    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];

    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          // 将目标ID放入结果集ids
          // node.disabled = true;
          deleteIds.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 编辑
     */
    const edit = (record:any) => {
      modalVisible.value=true;
      doc.value = Tool.copy(record);

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
      setTimeout(function (){
        editor.create();//等页面渲染好了 再去create
      },100);
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value=true;
      doc.value = {ebookId: route.query.ebookId};

      treeSelectData.value = Tool.copy(level1.value);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
      setTimeout(function (){
        editor.create();//等页面渲染好了 再去create
      },100);
    };
    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      // console.log(level1, level1.value, id)
      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
        onOk() {
          // console.log(ids)
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        },
      });
    };
    onMounted(() => {
      handleQuery();
    });
    return {
      param,
      level1,
      //docs,//表格
      columns,
      loading,
      handleQuery,

      edit,//表单
      add,
      doc,//doc返回到html
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,
      treeSelectData
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