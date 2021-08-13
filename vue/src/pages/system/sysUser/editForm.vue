<template>
  <a-modal
    title="编辑用户"
    :width="900"
    :visible="modalVisible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :destroyOnClose="true"
  >
      <a-form :form="form">
          <a-form-item v-show="false" >
              <a-input v-decorator="['id']" />
          </a-form-item>

          <a-row :gutter="24">
              <a-col :md="12" :sm="24">
                  <a-form-item
                          label="账号"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                          has-feedback
                  >
                      <a-input placeholder="请输入账号" v-decorator="['username', {rules: [{required: true, min: 6, message: '请输入至少六个字符的账号！'}]}]" />
                  </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24" >
                  <a-form-item
                          label="姓名"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                          has-feedback
                  >
                      <a-input placeholder="请输入姓名" v-decorator="['name', {rules: [{required: true, message: '请输入姓名！'}]}]" />
                  </a-form-item>
              </a-col>
          </a-row>

          <a-row :gutter="24">

              <a-col :md="12" :sm="24">
                  <a-form-item
                          label="手机号"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                          has-feedback
                  >
                      <a-input placeholder="请输入手机号" v-decorator="['phone',{rules: [{ required: true, message: '请输入手机号！' },
                                              {
                                                validator: validatorPhone,
                                              }]}]" />
                  </a-form-item>
              </a-col>

              <a-col :md="12" :sm="24">
                  <a-form-item
                          label="性别"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                  >
                      <a-radio-group v-decorator="['sex',{rules: [{ required: true, message: '请选择性别！' }]}]" >
                          <a-radio :value="1">男</a-radio>
                          <a-radio :value="2">女</a-radio>
                      </a-radio-group>
                  </a-form-item>
              </a-col>
          </a-row>
          <a-row :gutter="24">
              <a-col :md="12" :sm="24">
                  <a-form-item
                          label="邮箱"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                          has-feedback
                  >
                      <a-input placeholder="请输入邮箱" v-decorator="['email']" />
                  </a-form-item>
              </a-col>

          </a-row>
      </a-form>
  </a-modal>
</template>

<script>
    import {userEdit} from "@/services/sysUser"
  export default {
    components: { },

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
          form: this.$form.createForm(this),
          modalVisible:false,
          confirmLoading: false
      }
    },

    methods: {
        edit (record) {
            this.modalVisible = true
            setTimeout(() => {
                this.setUserItem(record)
            }, 100)
        },

        setUserItem(record){
            this.form.setFieldsValue(
                {
                    id: record.id,
                    username: record.username,
                    name: record.name,
                    phone: record.phone,
                    sex: record.sex,
                    email: record.email,
                }
            )
        },

        validatorPhone(rule, value, callback){
            let reg = /^1[0-9]{10}$/
            if (value === '' || value === null){
                callback('账号不能为空!');
            }else if( value.length <= 10 ||  !reg.test(value)) {
                callback('请输入正确的手机号!');
            }
            callback();
        },


        handleCancel(){
            this.form.resetFields()
            this.modalVisible = false
        },
        handleSubmit(){
            this.confirmLoading = true
            this.form.validateFields((errors, values) => {
                if(!errors){
                    userEdit(values).then((res) => {
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
