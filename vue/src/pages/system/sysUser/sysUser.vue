<template>
    <a-card>
        <div>
            <a-space class="tableOperator">
                <a-button @click="$refs.addForm.add()" type="primary" v-auth="`sysUser:add`">新建</a-button>
            </a-space>
            <toolbar-table
                    :loading="loading"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="localPagination"
                    :selectedRows.sync="selectedRows"
                    @change="onChange"
                    @reload="loadData"
            >

                <span slot="sex" slot-scope="{text}">
                    {{ text == 1? "男":"女" }}
                </span>

                <span slot="status" slot-scope="{text,record}" >
                    <a-popconfirm placement="top" :title="text===1? '确定禁用该用户？':'确定启用该用户？'" @confirm="() => editUserStatus(record)" >
                        <a-tag color="blue" v-if="text == 1"><a>正常</a></a-tag>
                        <a-tag color="cyan" v-else><a>禁用</a></a-tag>

                    </a-popconfirm>
                </span>

                <div slot="action" slot-scope="{text, record}">
                    <a style="margin-right: 8px" @click="editRecord(record)">
                        <a-icon type="edit"/>编辑
                    </a>
                    <a-popconfirm style="margin-right: 8px" title="Sure to delete?" @confirm="() => deleteRecord(record)">
                        <a href="#"><a-icon type="delete" />删除</a>
                    </a-popconfirm>

                    <a-dropdown>
                        <a class="ant-dropdown-link">
                            更多 <a-icon type="down" />
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item >
                                <a-popconfirm style="margin-right: 8px" title="确定要重置吗?" @confirm="() => restPassword(record)" >
                                    <a href="#"> <a-icon type="rest" />重置密码</a>
                                </a-popconfirm>
                            </a-menu-item>
                            <a-menu-item>
                                <a style="margin-right: 8px" @click="grantRole(record)">
                                    <a-icon type="key"/>授权角色
                                </a>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </div>

            </toolbar-table>
        </div>
        <add-form ref="addForm" @ok="loadData"/>
        <edit-form ref="editForm" @ok="loadData"/>
        <user-role-form ref="userRoleForm" @ok="loadData"></user-role-form>
    </a-card>
</template>
<script>
    import ToolbarTable from '@/components/table/toolbarTable/ToolbarTable'
    import {userListAll,userDeleteById,userRestPassword,editUserStatus} from '@/services/sysUser'
    import addForm from './addForm'
    import editForm from './editForm'
    import userRoleForm from './userRoleForm'
    export default {
        components: {
            ToolbarTable,addForm,editForm,userRoleForm
        },
        authorize: {
            deleteRecord: 'sysUser:delete',
            editRecord: 'sysUser:edit',
            restPassword: 'sysUser:restPassword',
            grantRole: 'sysUser:grantRole',
            editUserStatus: 'sysUser:editUserStatus'
        },
        data() {
            return {
                desc: '用户管理是用来管理用户的,可以改变用户状态,重置密码,分配角色等.',
                columns: [
                    {
                        title: '账号',
                        dataIndex: 'username',
                    },
                    {
                        title: '姓名',
                        dataIndex: 'name',
                    },
                    {
                        title: '性别',
                        dataIndex: 'sex',
                        scopedSlots: { customRender: 'sex' }
                    },
                    {
                        title: '手机',
                        dataIndex: 'phone',
                    },

                    {
                        title: '角色',
                        dataIndex: 'roles',
                        ellipsis: true,
                        width: 150
                    },

                    {
                        title: '状态',
                        dataIndex: 'status',
                        scopedSlots: { customRender: 'status' }
                    },

                    {
                        title: '操作',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                localPagination: {
                    current: 1,
                    pageSize: 10,
                    total: 0
                },
                dataSource: [],
                selectedRows: [],
                loading: false
            }
        },
        methods: {
            loadData(pagination){
                this.loading = true
                const parameter = {
                    current: (pagination && pagination.current) || this.localPagination.current ,
                    size: (pagination && pagination.pageSize) || this.localPagination.pageSize
                }
                userListAll(parameter).then(res => {
                    const records = res.data.records
                    if (records === 0  && this.localPagination.current > 1) {
                        // 为防止删除数据后导致页面当前页面数据长度为 0 ,自动翻页到上一页
                        this.localPagination.current--
                        this.loadData()
                        return
                    }else{
                        this.dataSource = records
                        this.localPagination.total = res.data.total;
                    }
                }).finally(() =>{
                    this.loading = false
                })
            },
            onChange(pagination, filters, sorter){
                this.localPagination.current = pagination.current;
                this.loadData(pagination)
            },
            deleteRecord(record){
                userDeleteById({id: record.id}).then(res => {
                    if(res.success){
                        this.$message.success('删除成功')
                        this.loadData()
                    }
                })
            },
            editRecord(record){
                this.$refs.editForm.edit(record)
            },
            editUserStatus(record){
                let params = {}
                if(record.status === 1){
                    params.status = 2
                }else{
                    params.status = 1
                }
                params.id = record.id

                editUserStatus(params).then(res => {
                    if(res.success){
                        this.$message.success('修改状态成功')
                        this.loadData()
                    }
                })
            },

            restPassword(record){
                userRestPassword({id: record.id}).then(res => {
                    if(res.success){
                        this.$message.success('重置成功')
                    }
                })

            },
            grantRole(record){
                this.$refs.userRoleForm.init(record)
            }

        },
        created() {
        },
        mounted() {
            this.loadData()
        }
    }
</script>
