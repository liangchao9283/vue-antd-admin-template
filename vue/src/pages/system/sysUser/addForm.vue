<template>
  <a-modal
    title="新增用户"
    :width="900"
    :visible="modalVisible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :destroyOnClose="true"
  >
      <a-form :form="form">
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
                          label="密码"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                          has-feedback
                  >
                      <a-input
                              placeholder="请输入密码"
                              type="password"
                              v-decorator="['password', {rules: [{required: true, message: '请输入密码！'},{validator: validateToNextPassword}]}]" />
                  </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24">
                  <a-form-item
                          label="重复密码"
                          :labelCol="labelCol"
                          :wrapperCol="wrapperCol"
                          has-feedback
                  >
                      <a-input
                              placeholder="请再次输入密码"
                              type="password"
                              v-decorator="['confirm', {rules: [{required: true, message: '请再次输入密码！'},
                                              {
                                                validator: compareToFirstPassword,
                                              }]}]" />
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
    import {userAdd} from "@/services/sysUser"
    import {MD5} from '@/utils/util'
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
        add () {
            this.modalVisible = true
            this.form.getFieldDecorator('sex',  {initialValue: 1} )
        },
        validateToNextPassword (rule, value, callback) {
            const form = this.form
            if (value) {
                let flag = false;
                if(value.length <8){
                    callback('密码过短,长度必须为8-18位!');
                    flag = true;
                }else if(value.length >18){
                    callback('密码过长,长度必须为8-18位!');
                    flag = true;
                }
                /*let reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$/;
                if(!reg.test(value)){
                    callback('密码必须由英文字母和数字组成!');
                    flag = true;
                }
                let reg2 = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/
                if(!reg2.test(value)){
                    callback('密码至少包含一个大写字母，一个小写字母和一个数字!');
                    flag = true;
                }*/
                let reg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,18}/;
                if(!reg.test(value)){
                    callback('密码中必须包含字母、数字、特殊字符!');
                    flag = true;
                }
                if(!flag){
                    form.validateFields(['confirm'], { force: true })
                }
            }
            callback()
        },

        compareToFirstPassword (rule, value, callback) {
            const form = this.form
            if (value && value !== form.getFieldValue('password')) {
                callback('请确认两次输入密码的一致性！')
            } else {
                callback()
            }
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
                    let params = JSON.parse(JSON.stringify(values))
                    params.password = MD5(params.password)
                    delete params.confirm
                    userAdd(params).then(res => {
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
        }
    },



  }
</script>
<style scoped>
  .editable-row-operations a {
    margin-right: 8px;
  }
</style>
