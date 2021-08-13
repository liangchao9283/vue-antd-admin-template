<template>
  <div class="standard-table">
    <div class="alert">
      <a-alert type="info" :show-icon="true" >
        <div class="message" slot="message">
          共计{{pagination.total}}条记录

          <a-dropdown :trigger="['click']">
            <a-tooltip class="clear" title="列设置">
              <a class="clear" ><a-icon type="setting" /></a>
            </a-tooltip>
            <columnSetting slot="overlay" :columns="columns" @columnChange="columnSettingChange" />
          </a-dropdown>

          <a-dropdown :trigger="['click']">
            <a-tooltip class="clear" title="密度">
              <a class="clear" ><a-icon type="column-height" /></a>
            </a-tooltip>
            <a-menu slot="overlay" @click="customSizeChange">
              <a-menu-item key="default">
                默认
              </a-menu-item>
              <a-menu-item key="middle">
                中等
              </a-menu-item>
              <a-menu-item key="small">
                紧凑
              </a-menu-item>
            </a-menu>
          </a-dropdown>


          <a-tooltip>
            <template slot="title">
              刷新列表
            </template>
            <a class="clear" @click="onReload" ><a-icon type="reload" /></a>
          </a-tooltip>

          <a class="clear" @click="onClear" v-if="selectedRows">清空</a>
        </div>
      </a-alert>
    </div>
    <a-table
      :bordered="bordered"
      :loading="loading"
      :columns="finalColumns"
      :dataSource="dataSource"
      :rowKey="rowKey"
      :pagination="pagination"
      :expandedRowKeys="expandedRowKeys"
      :expandedRowRender="expandedRowRender"
      :size="customSize"
      @change="onChange"
      :rowSelection="selectedRows ? {selectedRowKeys: selectedRowKeys, onChange: updateSelect} : undefined"
    >
      <template slot-scope="text, record, index" :slot="slot" v-for="slot in Object.keys($scopedSlots).filter(key => key !== 'expandedRowRender') ">
        <slot :name="slot" v-bind="{text, record, index}"></slot>
      </template>
      <template :slot="slot" v-for="slot in Object.keys($slots)">
        <slot :name="slot"></slot>
      </template>
      <template slot-scope="record, index, indent, expanded" :slot="$scopedSlots.expandedRowRender ? 'expandedRowRender' : ''">
        <slot v-bind="{record, index, indent, expanded}" :name="$scopedSlots.expandedRowRender ? 'expandedRowRender' : ''"></slot>
      </template>
    </a-table>
  </div>
</template>

<script>
  import columnSetting from './columnSetting'
export default {
  name: 'StandardTable',
  components:{columnSetting},
  props: {
    bordered: Boolean,
    loading: [Boolean, Object],
    columns: Array,
    dataSource: Array,
    rowKey: {
      type: [String, Function],
      default: 'id'
    },
    pagination: {
      type: [Object, Boolean],
      default: true
    },
    selectedRows: Array,
    expandedRowKeys: Array,
    expandedRowRender: Function
  },
  data () {
    return {
      customSize: null,
      columnsSetting: []
    }
  },
  methods: {
    updateSelect (selectedRowKeys, selectedRows) {
      this.$emit('update:selectedRows', selectedRows)
      this.$emit('selectedRowChange', selectedRowKeys, selectedRows)
    },
    onClear() {
      this.updateSelect([], [])
      this.$emit('clear')
    },
    onChange(pagination, filters, sorter, {currentDataSource}) {
      this.$emit('change', pagination, filters, sorter, {currentDataSource})
    },
    onReload(){
      this.$emit('reload')
    },
    customSizeChange({key}){
      this.customSize = key
    },
    columnSettingChange(val){
      debugger
      this.columnsSetting = val
    }
  },
  created () {
    this.columnsSetting = this.columns
  },
  watch: {
    selectedRows (selectedRows) {
    },
    columns(value) {
      this.columnsSetting = value
    },

  },
  computed: {
    selectedRowKeys() {
      return this.selectedRows.map(record => {
        return (typeof this.rowKey === 'function') ? this.rowKey(record) : record[this.rowKey]
      })
    },
    finalColumns(){
      return this.columnsSetting.filter(value => value.checked === undefined || value.checked)
    }
  }
}
</script>

<style scoped lang="less">
.standard-table{
  .alert{
    margin-bottom: 16px;
    .message{
      a{
        font-weight: 600;
      }
    }
    .clear{
      float: right;
      margin-left: 15px;
    }
  }
}
</style>
