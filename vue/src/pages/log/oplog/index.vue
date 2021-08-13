<template>
    <a-card>
        <div :class="advanced ? 'search' : null">
            <a-form layout="horizontal">
                <div :class="advanced ? null: 'fold'">
                    <a-row >
                        <a-col :md="8" :sm="24" >
                            <a-form-item
                                    label="日志名称"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 18, offset: 1}"
                            >
                                <a-input v-model="queryParam.name" allow-clear placeholder="请输入日志名称"/>
                            </a-form-item>
                        </a-col>
                        <a-col :md="8" :sm="24" >
                            <a-form-item
                                    label="操作类型"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 18, offset: 1}"
                            >
                                <a-select v-model="queryParam.opType" allow-clear placeholder="请选择操作类型">
                                    <a-select-option v-for="(item,index) in opTypeDict" :key="index" :value="item.code" >{{ item.name }}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="8" :sm="24" >
                            <a-form-item
                                    label="是否成功"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 18, offset: 1}"
                            >
                                <a-select v-model="queryParam.success" placeholder="请选择是否成功" >
                                    <a-select-option v-for="(item,index) in successDict" :key="index" :value="item.code" >{{ item.name }}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row v-if="advanced">
                        <a-col :md="8" :sm="24" >
                            <a-form-item
                                    label="操作时间"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 18, offset: 1}"
                            >
                                <a-range-picker
                                        v-model="queryParam.opTimeRange"
                                        :show-time="{
                                            hideDisabledOptions: true,
                                            defaultValue: [moment('00:00:00', 'HH:mm:ss'), moment('23:59:59', 'HH:mm:ss')],
                                        }"
                                        format="YYYY-MM-DD HH:mm:ss"
                                />
                            </a-form-item>
                        </a-col>
                    </a-row>
                </div>
                <span class="searchOperator">
                      <a-button type="primary" @click="loadData">查询</a-button>
                      <a-button class="buttonMarginLeft" @click="() => queryParam = {}">重置</a-button>
                      <a @click="advanced = !advanced" class="buttonMarginLeft">
                        {{advanced ? '收起' : '展开'}}
                        <a-icon :type="advanced ? 'up' : 'down'" />
                      </a>
                </span>
            </a-form>
        </div>
        <div>
            <a-space class="tableOperator">
                <a-button  type="primary">清空日志</a-button>
                <a-dropdown>
                    <a-menu  slot="overlay">
                        <a-menu-item key="delete">导出</a-menu-item>
                    </a-menu>
                    <a-button>
                        更多操作 <a-icon type="down" />
                    </a-button>
                </a-dropdown>
            </a-space>

            <toolbar-table
                    :loading="loading"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="localPagination"
                    @change="onChange"
                    @reload="loadData"
            >

                <span slot="name" slot-scope="{text}">
                    <ellipsis :length="16" tooltip>{{ text }}</ellipsis>
                </span>

                <span slot="opType" slot-scope="{text}">
                    {{ dictOpType(text) }}
                </span>

                <span slot="success" slot-scope="{text}">
                    {{ dictSuccess(text) }}
                </span>

                <span slot="url" slot-scope="{text}">
                    <ellipsis :length="10" tooltip>{{ text }}</ellipsis>
                </span>

                <div slot="action" slot-scope="{text, record}">
                    <a @click="$refs.detailsOplog.init(record)">查看详情</a>
                </div>
            </toolbar-table>
        </div>

        <details-oplog ref="detailsOplog"/>
    </a-card>
</template>
<script>
    import ToolbarTable from '@/components/table/toolbarTable/ToolbarTable'
    import moment from 'moment'
    import {opLogList} from "@/services/sysOpLog";
    import  Ellipsis from '@/components/Ellipsis'
    import detailsOplog from './details'

    export default {
        components: {ToolbarTable,Ellipsis,detailsOplog},
        mixins: [],
        data() {
            return {
                advanced: true,
                loading: false,
                columns: [
                    {
                        title: '日志名称',
                        dataIndex: 'name',
                        scopedSlots: { customRender: 'name' }
                    },
                    {
                        title: '操作类型',
                        dataIndex: 'opType',
                        scopedSlots: { customRender: 'opType' }
                    },
                    {
                        title: '执行结果',
                        dataIndex: 'success',
                        scopedSlots: { customRender: 'success' }
                    },
                    {
                        title: 'ip',
                        dataIndex: 'ip'
                    },
                    {
                        title: '请求地址',
                        dataIndex: 'url',
                        scopedSlots: { customRender: 'url' }
                    },
                    {
                        title: '请求方式',
                        dataIndex: 'reqMethod',
                    },
                    {
                        title: '操作时间',
                        dataIndex: 'opTime',
                        scopedSlots: { customRender: 'opTime' }
                    },
                    {
                        title: '操作人',
                        dataIndex: 'username'
                    },
                    {
                        title: '详情',
                        dataIndex: 'action',
                        width: '150px',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                dataSource: [],
                localPagination: {
                    current: 1,
                    pageSize: 10,
                    total: 0
                },
                queryParam: {},
                opTypeDict: [
                    {code: 0,name: '其他'},
                    {code: 1,name: '增加'},
                    {code: 2,name: '删除'},
                    {code: 3,name: '编辑'},
                    {code: 4,name: '更新'},
                    {code: 5,name: '查询'},
                    {code: 6,name: '详情'},
                    {code: 7,name: '树'},
                    {code: 8,name: '导入'},
                    {code: 9,name: '导出'},
                    {code: 10,name: '授权'},
                    {code: 11,name: '强退'},
                    {code: 12,name: '清空'},
                    {code: 13,name: '修改状态'},
                ],
                successDict: [
                    {code: 'Y',name: '是'},
                    {code: 'N',name: '否'}
                ]
            }
        },
        methods: {
            moment,
            onChange(pagination){
                this.localPagination.current = pagination.current;
                this.loadData(pagination)
            },
            loadData(pagination){
                this.loading = true
                const condition = this.switchingDate()
                const parameter = {
                    current: (pagination && pagination.current) || this.localPagination.current ,
                    size: (pagination && pagination.pageSize) || this.localPagination.pageSize
                }
                Object.assign(parameter, condition)
                opLogList(parameter).then(res => {
                    if(res.success){
                        this.dataSource = res.data.records
                        this.localPagination.total = res.data.total;
                    }
                }).finally(() =>{
                    this.loading = false
                })
            },

            /**
             * 查询参数组装
             */
            switchingDate () {
                const opTimeRange = this.queryParam.opTimeRange
                if (opTimeRange != null) {
                    this.queryParam.searchBeginTime = moment(opTimeRange[0]).format('YYYY-MM-DD HH:mm:ss')
                    this.queryParam.searchEndTime = moment(opTimeRange[1]).format('YYYY-MM-DD HH:mm:ss')
                    if (opTimeRange.length < 1) {
                        delete this.queryParam.searchBeginTime
                        delete this.queryParam.searchEndTime
                    }
                }
                const obj = JSON.parse(JSON.stringify(this.queryParam))
                delete obj.opTimeRange
                return obj
            },

            dictOpType(text){
                let opType =  this.opTypeDict.filter(item => item.code === text)
                return opType[0].name
            },
            dictSuccess(text){
                let success =  this.successDict.filter(item => item.code === text)
                return success[0].name
            },

        },
        created() {
        },
        mounted() {
            this.loadData()
        }
    }
</script>
