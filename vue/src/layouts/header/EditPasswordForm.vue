<template>
    <a-modal
            title="修改密码"
            :width="600"
            :visible="modalVisible"
            :confirmLoading="confirmLoading"
            @ok="handleSubmit"
            @cancel="handleCancel"
            :destroyOnClose="true"
    >
            <a-form :form="form" >

                <a-form-item
                        label="原密码"
                        :labelCol="{span: 7}"
                        :wrapperCol="{span: 13}"
                        hasFeedback
                >
                    <a-input placeholder="请输入原密码"  type="password" v-decorator="['oldPassword',{rules: [{required: true, message: '请输入原密码！'}] }]" />
                </a-form-item>


                <a-form-item
                        label="新密码"
                        :labelCol="{span: 7}"
                        :wrapperCol="{span: 13}"
                        hasFeedback
                >
                    <a-input placeholder="请输入新密码"  type="password" v-decorator="['newPassword',{rules: [{required: true, message: '请输入新密码！'},{validator: validateToNextPassword}] }]" />
                </a-form-item>

                <a-form-item
                        label="重复新密码"
                        :labelCol="{span: 7}"
                        :wrapperCol="{span: 13}"
                        hasFeedback
                >
                    <a-input placeholder="请再次输入新密码"  type="password" v-decorator="['confirmNewPassword',{rules: [{required: true, message: '请再次输入新密码！'},{validator: compareToFirstPassword}] }]" />
                </a-form-item>

            </a-form>
    </a-modal>
</template>

<script>
    import {userEditPassword} from "@/services/sysUser"
    import {MD5} from "@/utils/util";

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
            init () {
                this.modalVisible = true
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
                    let reg = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,18}/;
                    if(!reg.test(value)){
                        callback('密码中必须包含字母、数字、特殊字符!');
                        flag = true;
                    }
                    /*let reg2 = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/
                    if(!reg2.test(value)){
                        callback('密码至少包含一个大写字母，一个小写字母和一个数字!');
                        flag = true;
                    }*/
                    if(!flag){
                        form.validateFields(['confirmNewPassword'], { force: true })
                    }
                }
                callback()
            },

            compareToFirstPassword (rule, value, callback) {
                const form = this.form
                if (value && value !== form.getFieldValue('newPassword')) {
                    callback('请确认两次输入密码的一致性！')
                } else {
                    callback()
                }
            },

            handleCancel(){
                this.modalVisible = false
                this.form.resetFields()
            },
            handleSubmit(){
                this.confirmLoading = true
                this.form.validateFields((errors, values) => {
                    if(!errors){
                        let params = JSON.parse(JSON.stringify(values))
                        params.oldPassword = MD5(params.oldPassword)
                        params.newPassword = MD5(params.newPassword)
                        delete params.confirmNewPassword
                        userEditPassword(params).then(res => {
                            if(res.success){
                                this.$message.success('修改成功,请重新登录')
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

</style>
