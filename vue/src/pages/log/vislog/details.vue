<template>
    <a-modal
            title="日志详情"
            :width="900"
            :visible="visible"
            @cancel="handleCancel"
            @ok="handleCancel"
    >
        <a-form :form="form">
            <a-row :gutter="24">
                <a-col :md="12" :sm="24">
                    <a-form-item
                            label="操作系统"
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                    >
                        <a-input v-decorator="['os']" />
                    </a-form-item>
                </a-col>


                <a-col :md="12" :sm="24">
                    <a-form-item
                            label="地址"
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                    >
                        <a-input v-decorator="['location']" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :md="12" :sm="24">
                    <a-form-item
                            label="具体消息"
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                    >
                        <a-textarea :rows="4" v-decorator="['message']"/>
                    </a-form-item>
                </a-col>

            </a-row>

        </a-form>
    </a-modal>
</template>
<script>
    export default {
        components: {},
        mixins: [],
        data() {
            return {
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 19 }
                },
                visible: false,
                confirmLoading: false,
                form: this.$form.createForm(this)
            }
        },
        methods: {
            init(record){
                this.visible = true
                setTimeout(() => {
                    this.form.setFieldsValue(
                        {
                            location: record.location,
                            os: record.os,
                            message: record.message
                        }
                    )
                }, 100)
            },
            handleCancel(){
                this.form.resetFields()
                this.visible = false
            }
        },
        created() {
        },
        mounted() {
        }
    }
</script>
