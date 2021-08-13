<template>
    <a-card>
        <div>
            <a-space class="tableOperator">
                <a-button @click="$refs.addForm.add()" type="primary"  >新建</a-button>
            </a-space>
            <standard-table
                    :loading="loading"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="localPagination"
                    :selectedRows.sync="selectedRows"
                    @change="onChange"
            >
                <span slot="type" slot-scope="{text}">
                  <a-tag color="cyan" v-if="text === 0">
                    目录
                  </a-tag>
                  <a-tag color="blue" v-if="text === 1">
                    页面
                  </a-tag>
                  <a-tag color="red" v-if="text === 2">
                    按钮
                  </a-tag>
                </span>

                <span slot="icon" slot-scope="{text}">
                  <div v-if="text != null && text != ''">
                    <a-icon :type="text"/>
                  </div>
                </span>

                <div slot="action" slot-scope="{text, record}">
                    <a style="margin-right: 8px" @click="editRecord(record.id)" >
                        <a-icon type="edit" />编辑
                    </a>

                    <a-popconfirm title="Sure to delete?" @confirm="() => deleteRecord(record.id)">
                       <a href="#"><a-icon type="delete" />删除</a>
                    </a-popconfirm>
                </div>

            </standard-table>
        </div>
        <add-form ref="addForm" @ok="loadData"/>
        <edit-form ref="editForm" @ok="loadData"/>
    </a-card>
</template>

<script>
    import StandardTable from '@/components/table/StandardTable'
    import {menuListAll,menuDeleteById} from "@/services/sysMenu";
    import addForm from './addForm'
    import editForm from './editForm'

    const columns = [

        {
            title: '菜单名称',
            dataIndex: 'name',
        },
        {
            title: '菜单类型',
            dataIndex: 'type',
            scopedSlots: { customRender: 'type' },
        },
        {
            title: '图标',
            dataIndex: 'icon',
            scopedSlots: { customRender: 'icon' },
        },
        {
            title: '组件',
            dataIndex: 'component',
        },
        {
            title: '路由地址',
            dataIndex: 'path',
        },
        {
            title: '权限值',
            dataIndex: 'permissionCode',
        },
        {
            title: '排序',
            dataIndex: 'sort',
        },

        {
            title: '操作',
            scopedSlots: { customRender: 'action' }
        }
    ]

    export default {
        name: 'sysMenu',
        components: {StandardTable,addForm,editForm},


        data () {
            return {
                loading: false,
                advanced: false,
                columns: columns,
                localPagination: {
                    current: 1,
                    pageSize: 10
                },
                dataSource: [],
                selectedRows: []
            }
        },

        methods: {
            deleteRecord(id) {
                menuDeleteById({id: id}).then(res => {
                    if(res.success){
                        this.$message.success('删除成功')
                        this.loadData()
                    }
                })
            },
            editRecord(id){
                this.$refs.editForm.edit(id)
            },

            onChange(pagination, filters, sorter) {
                this.localPagination.current = pagination.current;
                this.loadData(pagination)
            },

            loadData(pagination){
                this.loading = true;
                const parameter = {
                    current: (pagination && pagination.current) || this.localPagination.current ,
                    size: (pagination && pagination.pageSize) || this.localPagination.pageSize
                }
                menuListAll(parameter).then(res => {
                    const records = res.data.records
                    if (records === 0  && this.localPagination.current > 1) {
                        // 为防止删除数据后导致页面当前页面数据长度为 0 ,自动翻页到上一页
                        this.localPagination.current--
                        this.loadData()
                        return
                    }else{
                        this.removeEmptyChildren(records)
                        this.dataSource = records
                        this.localPagination.total = res.data.total;
                    }
                }).finally(()=>{
                    this.loading = false
                })
            },
            /**
             * 去掉无用的支节点
             */
            removeEmptyChildren(records){
                if (records == null || records.length === 0) return
                for (let i = 0; i < records.length; i++) {
                    const item = records[i]
                    if (item.children != null && item.children.length === 0) {
                        item.children = null
                    } else {
                        this.removeEmptyChildren(item.children)
                    }
                }
            }
        },
        mounted() {
            this.loadData()
        }
    }
</script>

<style lang="less" scoped>

</style>
