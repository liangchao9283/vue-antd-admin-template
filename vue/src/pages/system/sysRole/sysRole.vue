<template>
    <a-card>
        <div>
            <a-space class="tableOperator">
                <a-button @click="$refs.addForm.add()" type="primary" v-auth="`sysRole:add`">新建</a-button>
            </a-space>
            <standard-table
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="localPagination"
                    :selectedRows.sync="selectedRows"
                    @change="onChange"
            >

                <div slot="action" slot-scope="{text, record}">
                    <a style="margin-right: 8px" @click="editRecord(record)">
                        <a-icon type="edit"/>编辑
                    </a>
                    <a-popconfirm style="margin-right: 8px" title="Sure to delete?" @confirm="() => deleteRecord(record)">
                        <a href="#"><a-icon type="delete" />删除</a>
                    </a-popconfirm>

                    <a @click="grantMenu(record)">
                        <a-icon type="key"/>授权菜单
                    </a>
                </div>

            </standard-table>
        </div>
        <add-form ref="addForm" @ok="loadData"/>
        <edit-form ref="editForm" @ok="loadData"/>
        <grant-menu-form ref="grantMenuForm" />
    </a-card>
</template>
<script>
    import StandardTable from '@/components/table/StandardTable'
    import {roleListAll,roleDeleteById} from "@/services/sysRole"
    import addForm from './addForm'
    import editForm from './editForm'
    import grantMenuForm from  './grantMenuForm'
    export default {
        components: {
            StandardTable,addForm,editForm,grantMenuForm
        },
        authorize: {
            deleteRecord: 'sysRole:delete',
            editRecord: 'sysRole:edit',
            grantMenu: 'sysRole:grantMenu'
        },
        data() {
            return {
                desc: '角色管理是用来管理角色的,一个用户可以分配多个角色,多个角色下的权限将进行合并',
                columns: [
                    {
                        title: '角色名称',
                        dataIndex: 'roleName',
                    },

                    {
                        title: '角色编号',
                        dataIndex: 'code',
                    },

                    {
                        title: '操作',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                localPagination: {
                    current: 1,
                    pageSize: 10
                },
                dataSource: [],
                selectedRows: []
            }
        },
        methods: {
            loadData(pagination){
                const parameter = {
                    current: (pagination && pagination.current) || this.localPagination.current ,
                    size: (pagination && pagination.pageSize) || this.localPagination.pageSize
                }
                roleListAll(parameter).then(res => {
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
                })
            },
            onChange(pagination, filters, sorter){
                this.localPagination.current = pagination.current;
                this.loadData(pagination)
            },
            deleteRecord(record){
                roleDeleteById({id: record.id}).then(res => {
                    if(res.success){
                        this.$message.success('删除成功')
                        this.loadData()
                    }
                })
            },
            editRecord(record){
                this.$refs.editForm.edit(record)
            },
            grantMenu(record){
                this.$refs.grantMenuForm.init(record.id)
            }
        },
        created() {
        },
        mounted() {
            this.loadData()
        }
    }
</script>
