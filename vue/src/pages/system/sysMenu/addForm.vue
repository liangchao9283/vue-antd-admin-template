<template>
  <a-modal
    title="新增菜单"
    :width="1000"
    :visible="modalVisible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :destroyOnClose="true"
  >
    <a-spin :spinning="formLoading">
      <a-form :form="form" >

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              label="菜单名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              hasFeedback
            >
              <a-input placeholder="请输入菜单名称" v-decorator="['name',{rules: [{required: true, min: 1,whitespace:true, message: '请输入菜单名称！'}]}]" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="菜单层级"
            >
              <a-radio-group v-decorator="['type',{rules: [{ required: true, message: '请选择菜单层级！' }]}]" >
                <a-radio v-for="(item,index) in typeData" :key="index" :value="item.code" @click="meneTypeFunc(item.code)">{{ item.value }}</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>



        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="父级菜单"
                has-feedback
              >
                <a-tree-select
                  v-decorator="['parentId', {rules: [{ required: true, message: '请选择父级菜单！' }]}]"
                  style="width: 100%"
                  :dropdownStyle="{ maxHeight: '300px', overflow: 'auto' }"
                  :treeData="menuTreeData"
                  placeholder="请选择父级菜单"
                  treeDefaultExpandAll
                >
                  <!--<span slot="title" slot-scope="{ id }">{{ id }}</span>-->
                </a-tree-select>
              </a-form-item>

          </a-col>
          <a-col :md="12" :sm="24">
              <a-form-item
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                      label="图标"
              >
                <a-input placeholder="请选择图标" disabled="disabled" v-decorator="['icon']" >
                  <a-icon slot="addonAfter" @click="openIconSele()" type="setting" />
                </a-input>
              </a-form-item>
          </a-col>
        </a-row>


        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="排序"
            >
              <a-input-number style="width: 100%" v-decorator="['sort', { initialValue: 100 }]" :min="1" :max="1000" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="备注"
                    hasFeedback
            >
              <a-input placeholder="请输入备注" v-decorator="['remark']"></a-input>
            </a-form-item>
          </a-col>
        </a-row>


        <a-row :gutter="24" >
          <a-col :md="12" :sm="24">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                hasFeedback
              >
                <span slot="label">
                  <a-tooltip title="前端vue组件,页面在pages文件夹下路径，例：system/sysRole。注：目录级填写：view.page(带面包屑),view.blank(不带面包屑)。规范:不要以/或pages开头">
                    <a-icon type="question-circle-o" />
                  </a-tooltip>&nbsp;
                  前端组件
                </span>
                <a-input placeholder="请输入前端组件"  prop="component"
                         v-decorator="['component',
                         {rules: [{required: true,whitespace:true, message: '请输入前端组件'},{validator: validatorComponent} ]}]"/>
              </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                hasFeedback
              >
                <span slot="label">
                  <a-tooltip title="浏览器显示的URL，填写示例：sysRole，对应打开页面为角色管理页面.规范:不能以/开头,通常以页面文件名或页面文件上级目录名作为路由地址">
                    <a-icon type="question-circle-o" />
                  </a-tooltip>&nbsp;
                  路由地址
                </span>
                <a-input placeholder="请输入路由" v-decorator="['path',
                                                    {rules: [{required: true,whitespace:true, message: '请输入路由！'},{validator: validatorPath} ]}]" />
              </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <span slot="label">
                <a-tooltip title="系统权重：菜单可分配给任何角色，业务权重：菜单对超级管理员不可见">
                  <a-icon type="question-circle-o" />
                </a-tooltip>&nbsp;
                权重
              </span>
              <a-radio-group v-decorator="['weight',{rules: [{ required: true, message: '请选择权重！' }]}]">
                <a-radio v-for="(item,index) in weightData" :key="index" :value="item.code" >{{ item.value }}</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
               <span slot="label">
                  <a-tooltip title="否:菜单将不显示在左侧菜单栏">
                    <a-icon type="question-circle-o" />
                  </a-tooltip>&nbsp;
                  是否可见
                </span>

              <a-switch id="visible" checkedChildren="是" unCheckedChildren="否" v-decorator="['visible',{valuePropName: 'checked',rules: [{ required: true, message: '请选择是否可见！'}]}]"/><!-- defaultChecked -->
            </a-form-item>
          </a-col>
        </a-row>

        <a-card class="card" title="页面按钮权限:" :bordered="false" v-show="permissionTableShow" >
          <a-space style="margin-bottom: 18px;">
              <span>
                  <a-tooltip title="对于不需要权限控制的按钮请勿添加权限">
                    <a-icon type="question-circle-o" />
                  </a-tooltip>&nbsp;
                  <a-button type="primary" @click="addPermission">添加权限</a-button>
              </span>

          </a-space>

          <a-table :columns="permissionsColumns" :data-source="permissionsData" :pagination="false" rowKey="name">
            <template v-for="col in ['name', 'permissionCode']"  :slot="col" slot-scope="text, record,index">
              <div :key="col">
                <a-input v-if="record.editable" style="margin: -5px 0" :value="text"
                        @change="e => permissionHandleChange(e.target.value, index, col)"/>

                <template v-else>{{ text }}</template>
              </div>
            </template>

            <template slot="action" slot-scope="text, record,index">
              <div class="editable-row-operations">

                  <span v-if="record.editable">
                    <a @click="() => savePermission(index)">Save</a>
                    <a-popconfirm title="Sure to cancel?" @confirm="() => cancelPermission(index)">
                      <a>Cancel</a>
                    </a-popconfirm>
                  </span>
                  <span v-else>
                      <a :disabled="editingKey !== ''" @click="() => editPermission(record,index)" style="margin-right: 15px">
                          <a-icon type="edit"/>
                      </a>
                      <a :disabled="editingKey !== ''" @click="() => deletePermission(index)">
                          <a-icon type="delete" />
                      </a>
                  </span>
              </div>
            </template>

          </a-table>
        </a-card>

      </a-form>
    </a-spin>
    <a-modal
      :width="850"
      :visible="visibleIcon"
      @cancel="handleCancelIcon"
      footer=""
      :mask="false"
      :closable="false"
      :destroyOnClose="true"
    >
      <icon-selector v-model="currentSelectedIcon" @change="handleIconChange"/>
    </a-modal>


    <a-modal
            title="添加权限"
            :width="850"
            :visible="visibleAddPermission"
            @cancel="handleCancelAddPermission"
            @ok="submitPermission"
            :mask="true"
            :closable="false"
            :destroyOnClose="true"
    >
      <a-form :form="permissionForm" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="按钮名">
          <a-input
                  v-decorator="['name', { rules: [{ required: true, whitespace:true,message: '请输入按钮名' }] }]"
          />
        </a-form-item>

        <a-form-item >

          <span slot="label">
              <a-tooltip title="权限值全局唯一，作为操作按钮和接口的权限依据.填写规范可参照: '页面路由地址:按钮操作类型',例:菜单管理页面路由地址为: menu,
              页面有个删除按钮,则此删除按钮权限值为: menu:delete">
                <a-icon type="question-circle-o" />
              </a-tooltip>&nbsp;
              权限值(全局唯一)
          </span>
          <a-input
                  v-decorator="['permissionCode', { rules: [{ required: true,whitespace:true, message: '请输入权限值' }] }]"
          />
        </a-form-item>
      </a-form>
    </a-modal>

  </a-modal>
</template>

<script>
  import IconSelector from '@/components/IconSelector'
  import {getMenuTree,menuAdd} from "@/services/sysMenu"
  export default {
    components: { IconSelector },

    data () {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        visibleIcon: false,
          modalVisible: false,
        confirmLoading: false,
        menuTreeData: [],
        permissionRequired: true,
        permissionTableShow: false,
        // 图标组件
        currentSelectedIcon: 'pause-circle',
        typeData: [
          {
            code: '0',
            value: '目录'
          },
          {
            code: '1',
            value: '页面'
          },
        ],
        weightData: [
          {
            code: '1',
            value: '业务权重'
          },
          {
            code: '2',
            value: '系统权重'
          },
        ],

        formLoading: false,
        form: this.$form.createForm(this),
        visibleAddPermission: false,
          editingKey: '',
          editingRecord: null,
          permissionsColumns: [
              {
                  title: '按钮名',
                  dataIndex: 'name',
                  scopedSlots: { customRender: 'name' },
              },
              {
                  title: '权限值(唯一)',
                  dataIndex: 'permissionCode',
                  scopedSlots: { customRender: 'permissionCode' },
              },
              {
                  title: '操作',
                  dataIndex: 'action',
                  scopedSlots: { customRender: 'action' }
              }
          ],
          permissionsData: [],
          permissionForm: this.$form.createForm(this),
      }
    },

    methods: {
      // 打开页面初始化
      add () {
        this.modalVisible = true

        this.visibleAddPermission = false
        // 图标
        //this.currentSelectedIcon = type

        // 菜单层级默认选中页面项，并初始化
        this.form.getFieldDecorator('type',  {initialValue: '1'} )
        this.meneTypeFunc('1')

        //权重默认为业务权重
        this.form.getFieldDecorator('weight', { initialValue: '2' })

        //是否可见,默认可见
        this.form.getFieldDecorator('visible', { initialValue: true })

        //获取上级菜单树
        this.getMenuTree()
      },
        getMenuTree(){
            getMenuTree().then((res) => {
                if (res.success) {
                    this.form.resetFields(`parentId`, [])
                    this.menuTreeData = [{
                        'id': '0',
                        'parentId': '0',
                        'title': '顶级',
                        'value': '0',
                        'children': res.data
                    }]
                }
            })
        },



      meneTypeFunc (type) {
        if (type == '0') {
          this.permissionTableShow= false
          this.form.getFieldDecorator('component', { initialValue: 'view.page' })
        }else{
          this.permissionTableShow = true;
          this.form.getFieldDecorator('component', { initialValue: '' })
        }
      },

      openIconSele () {
        this.visibleIcon = true
      },
      handleIconChange (icon) {
        this.form.getFieldDecorator('icon', { initialValue: icon })
        this.visibleIcon = false
      },
      handleCancelIcon () {
        this.visibleIcon = false
      },

      permissionHandleChange(value, index, column){
        const newData = [...this.permissionsData];
        const target = newData[index];
        if (target) {
          target[column] = value;
          this.permissionsData = newData;
        }
      },

      addPermission(){
        this.visibleAddPermission = true
      },

      editPermission(record,index) {
        this.editingRecord = {...record}
        const newData = [...this.permissionsData];
        const target = newData[index];
        this.editingKey = index;
        if (target) {
          target.editable = true;
          this.permissionsData = newData;
        }
      },
      savePermission(index) {
        const newData = [...this.permissionsData];
        const target = newData[index];
        delete target.editable;
        this.permissionsData = newData;
        this.editingKey = '';
        this.editingRecord = null
      },
      cancelPermission(index) {
        const newData = [...this.permissionsData];
        const target = newData[index];
        this.editingKey = '';
        if (target) {
          Object.assign(target, this.editingRecord);
          delete target.editable;
          this.permissionsData = newData;
          this.editingRecord = null
        }
      },

        deletePermission(index){
            this.permissionsData.splice(index,1)
        },

        handleCancelAddPermission(){
            this.permissionForm.resetFields()
            this.visibleAddPermission = false
        },
        submitPermission(){
            this.permissionForm.validateFields((errors, values) => {
                if (!errors) {
                    this.visibleAddPermission = false
                    this.permissionsData.push(values)
                }else{
                    this.$message.error('校验失败')
                }
            })
        },
        handleCancel() {
            this.form.resetFields()
            this.permissionsData = []
            this.modalVisible = false
        },
        handleSubmit() {
            this.confirmLoading = true
            this.form.validateFields((errors, values) => {
                if (!errors) {
                    if(values.type && values.type === '0'  ){
                      this.permissionsData = []
                    }
                    values.permissionsData = this.permissionsData

                    menuAdd(values).then((res) => {
                        if (res.success) {
                            this.$message.success('新增成功')
                            this.$emit('ok')
                            this.handleCancel()
                        }
                    }).finally(() => {
                        this.confirmLoading = false
                    })
                }else{
                    this.confirmLoading = false
                }
            })
        },

        validatorComponent(rule, value, callback){
            if (value) {
                if(value.startsWith("/")){
                    callback('不能以/作为开头');
                }
                if(value.startsWith("pages")){
                    callback('不能以pages作为开头');
                }
            }
            callback()
        },
        validatorPath(rule, value, callback){
            if (value) {
                if(value.startsWith("/")){
                    callback('不能以/作为开头');
                }
            }
            callback()
        }
    },



  }
</script>
<style scoped>
  .editable-row-operations a {
    margin-right: 8px;
  }
</style>
