<template>
  <a-modal
    title="授权菜单"
    :width="600"
    :visible="modalVisible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :destroyOnClose="true"
  >
      <a-spin :spinning="formLoading">
          <a-form :form="form">

              <a-form-item
                      label="菜单权限"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol">

                  <a-tree
                          v-model="checkedKeys"
                          multiple
                          checkable
                          checkStrictly
                          :auto-expand-parent="autoExpandParent"
                          :expanded-keys="expandedKeys"
                          :tree-data="menuTreeData"
                          :replaceFields="replaceFields"
                          @expand="onExpand"
                          @check="onCheck"
                  />
              </a-form-item>

          </a-form>

      </a-spin>

  </a-modal>
</template>

<script>
    import {treeForGrant} from "@/services/sysMenu"
    import {roleOwnMenu,roleGrantMenu} from "@/services/sysRole"
  export default {
    components: { },

    data () {
      return {
          form: this.$form.createForm(this),
          confirmLoading: false,
          modalVisible:false,
          labelCol: {
              style: { 'padding-right': '20px' },
              xs: { span: 24 },
              sm: { span: 5 }
          },
          wrapperCol: {
              xs: { span: 24 },
              sm: { span: 15 }
          },
          menuTreeData: [],
          expandedKeys: [],
          checkedKeys: [],
          formLoading: true,
          autoExpandParent: true,
          roleId: null,
          replaceFields: {
              key: 'id'
          },
          commitKeys: [],
          leastChilds: []

      }
    },

    methods: {
      // 打开页面初始化
      init (roleId) {
          this.modalVisible = true
          this.formLoading = true
          this.roleId = roleId
          this.treeForGrant()
      },
        treeForGrant(){
            const _this = this
            treeForGrant({roleId: _this.roleId}).then((res) => {
                if (res.success) {
                    _this.menuTreeData = res.data
                    _this.getLeastChilds(res.data)
                    _this.menuTreeData.forEach(item => {
                        _this.expandedKeys.push(item.id)
                    })
                    _this.checkedRoleOwnMenu()
                }
            })
        },

        getLeastChilds(data) {
            for (let i = 0; i < data.length; i++) {
                this.pushLeastChilds(data[i])
            }
        },

        pushLeastChilds(e) {
           /* if (e.children.length > 0) {
                this.getLeastChilds(e.children)
                return
            }
            this.leastChilds.push(e.id)*/
            this.leastChilds.push(e.id)
            if (e.children.length > 0) {
                this.getLeastChilds(e.children)
            }else {
                return
            }
        },

        /**
         * 获取角色已有菜单权限并勾中
         */
        checkedRoleOwnMenu() {
            const _this = this
            roleOwnMenu({ id: this.roleId }).then((res) => {
                if (res.success) {
                    _this.pickCheckedKeys(res.data)
                    _this.commitKeys = res.data
                }
                _this.formLoading = false
            })
        },

        pickCheckedKeys(data) {
            for (let i = 0; i < data.length; i++) {
                if (this.leastChilds.includes(data[i])) {
                    this.checkedKeys.push(data[i])
                }
            }
        },

        onExpand(expandedKeys){
            this.expandedKeys = expandedKeys
            this.autoExpandParent = false
        },

        onCheck(checkedKeys,info){
            /*this.checkedKeys = checkedKeys
            this.commitKeys = checkedKeys.concat(info.halfCheckedKeys)*/
            this.checkedKeys = checkedKeys
            this.commitKeys = checkedKeys.checked
        },

        handleCancel(){
            // 清空已选择的
            this.checkedKeys = []
            // 清空已展开的
            this.expandedKeys = []
            this.modalVisible = false
        },
        handleSubmit(){
            const _this = this
            this.confirmLoading = true
            roleGrantMenu( _this.roleId, _this.commitKeys ).then((res) => {
                if (res.success) {
                    _this.$message.success('授权成功')
                    _this.handleCancel()
                }
            }).finally((res) => {
                _this.confirmLoading = false
            })
        },
    },



  }
</script>
<style scoped>
  .editable-row-operations a {
    margin-right: 8px;
  }
</style>
