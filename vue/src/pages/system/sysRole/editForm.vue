<template>
  <a-modal
    title="编辑角色"
    :width="800"
    :visible="modalVisible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :destroyOnClose="true"
  >

      <a-form :form="form" >

          <a-form-item v-show="false" >
              <a-input v-decorator="['id']" />
          </a-form-item>

            <a-form-item
              label="角色名称"
              :labelCol="{span: 7}"
              :wrapperCol="{span: 10}"
              hasFeedback
            >
              <a-input placeholder="请输入角色名称" v-decorator="['roleName',{rules: [{required: true, min: 1,whitespace:true, message: '请输入角色名称！'}]}]" />
            </a-form-item>

          <a-form-item
                  label="角色编号"
                  :labelCol="{span: 7}"
                  :wrapperCol="{span: 10}"
                  hasFeedback
          >
              <a-input placeholder="编码只能以英文字母,数字,符号组成" v-decorator="['code',{rules: [{required: true,whitespace:true, message: '请输入角色编号！'},
                                              {
                                                validator: validatorCode,
                                              }]}]" />
          </a-form-item>

          <a-form-item
                  label="备注"
                  :labelCol="{span: 7}"
                  :wrapperCol="{span: 10}"
                  hasFeedback
          >
              <a-input placeholder="请输入备注" v-decorator="['remark']" />
          </a-form-item>

      </a-form>
  </a-modal>
</template>

<script>
    import {roleEdit} from "@/services/sysRole"
  export default {
    components: { },

    data () {
      return {
          form: this.$form.createForm(this),
          confirmLoading: false,
          modalVisible:false,
      }
    },

    methods: {
      // 打开页面初始化
      edit (record) {
        this.modalVisible = true
          setTimeout(() => {
              this.setRoleItem(record)
          }, 100)
      },
        setRoleItem(record){
            this.form.setFieldsValue(
                {
                    id: record.id,
                    roleName: record.roleName,
                    code: record.code,
                    remark: record.remark,
                }
            )
        },

        validatorCode(rule, value, callback){
            if (value) {
                if(value.toLocaleUpperCase().startsWith("ROLE_")){
                    callback('编码不能以ROLE_开头');
                }

                let reg = /^[^\u4e00-\u9fa5]+$/
                if(!reg.test(value)){
                    callback('编码只能以英文字母,数字,符号组成');
                }
            }
            callback()
        },
        handleCancel(){
            this.form.resetFields()
            this.modalVisible = false
        },
        handleSubmit(){
            this.confirmLoading = true
            this.form.validateFields((errors, values) => {
                if(!errors){
                    roleEdit(values).then((res) => {
                        if (res.success) {
                            this.$message.success('修改成功')
                            this.$emit('ok')
                            this.handleCancel()
                        }
                    }).finally((res) => {
                        this.confirmLoading = false
                    })
                }else{
                    this.confirmLoading = false
                }
            })
        }
    },



  }
</script>
<style scoped>
  .editable-row-operations a {
    margin-right: 8px;
  }
</style>
