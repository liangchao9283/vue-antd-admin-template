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
                                    label="访问类型"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 18, offset: 1}"
                            >
                                <a-select v-model="queryParam.visType" allow-clear placeholder="请选择访问类型">
                                    <a-select-option v-for="(item,index) in visTypeDict" :key="index" :value="item.code" >{{ item.name }}</a-select-option>
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
                                    label="访问时间"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 18, offset: 1}"
                            >
                                <a-range-picker
                                        v-model="queryParam.visTimeRange"
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
                      <a-button type="primary" @click="onSearch">查询</a-button>
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

                <span slot="visType" slot-scope="{text}">
                    {{ text === 1?"登入":"登出" }}
                </span>

                <span slot="success" slot-scope="{text}">
                    {{ dictSuccess(text) }}
                </span>

                <div slot="action" slot-scope="{text, record}">
                    <a @click="$refs.detailsVislog.init(record)">查看详情</a>
                </div>
            </toolbar-table>
        </div>
        <details-vislog ref="detailsVislog"/>

    </a-card>
</template>
<script>
    import ToolbarTable from '@/components/table/toolbarTable/ToolbarTable'
    import moment from 'moment'
    import {visLogList} from "@/services/sysVisLog";
    import  Ellipsis from '@/components/Ellipsis'
    import detailsVislog from './details'

    export default {
        components: {ToolbarTable,Ellipsis,detailsVislog},
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
                        title: '访问类型',
                        dataIndex: 'visType',
                        scopedSlots: { customRender: 'visType' }
                    },
                    {
                        title: '是否成功',
                        dataIndex: 'success',
                        scopedSlots: { customRender: 'success' }
                    },
                    {
                        title: 'ip',
                        dataIndex: 'ip'
                    },

                    {
                        title: '浏览器',
                        dataIndex: 'browser'
                    },

                    {
                        title: '访问时间',
                        dataIndex: 'visTime',
                        scopedSlots: { customRender: 'visTime' }
                    },
                    {
                        title: '访问人',
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
                visTypeDict: [
                    {code: 1,name: '登入'},
                    {code: 2,name: '登出'},
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
            onSearch(){
              //点击查询按钮时,要将当前页重置为第一页
              this.localPagination.current = 1;
              this.loadData()
            },
            loadData(pagination){
                this.loading = true
                const condition = this.switchingDate()
                const parameter = {
                    current: (pagination && pagination.current) || this.localPagination.current ,
                    size: (pagination && pagination.pageSize) || this.localPagination.pageSize
                }
                Object.assign(parameter, condition)
                visLogList(parameter).then(res => {
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
                const visTimeRange = this.queryParam.visTimeRange
                if (visTimeRange != null) {
                    this.queryParam.searchBeginTime = moment(visTimeRange[0]).format('YYYY-MM-DD HH:mm:ss')
                    this.queryParam.searchEndTime = moment(visTimeRange[1]).format('YYYY-MM-DD HH:mm:ss')
                    if (visTimeRange.length < 1) {
                        delete this.queryParam.searchBeginTime
                        delete this.queryParam.searchEndTime
                    }
                }
                const obj = JSON.parse(JSON.stringify(this.queryParam))
                delete obj.visTimeRange
                return obj
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
