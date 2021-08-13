<template>
  <a-modal
    title="授权角色"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >

    <a-card :bordered="false">

      <div>
        <a-table
          size="middle"
          :row-selection="rowSelection"
          :columns="columns"
          :dataSource="loadData"
          :pagination="false"
          :loading="loading"
          :rowKey="(record) => record.id"
        />
      </div>

    </a-card>

  </a-modal>
</template>

<script>
  import {roleFindAll} from "@/services/sysRole"
  import { userOwnRole, userGrantRole } from '@/services/sysUser'

  const columns = [
    {
      title: '角色名称',
      dataIndex: 'roleName'
    },
    {
      title: '角色编号',
      dataIndex: 'code'
    },
    {
      title: '备注',
      dataIndex: 'remark'
    }
  ]

  export default {

    data () {
      return {
        columns,
        loadData: [],
        selectedRowKeys: [], // Check here to configure the default column
        loading: true,
        visible: false,
        confirmLoading: false,
        recordEntity: []
      }
    },

    computed: {
      rowSelection() {
        const _this = this
        const { selectedRowKeys } = this
        return {
          selectedRowKeys,
          onChange: (selectedRowKeys) => {
            this.selectedRowKeys = selectedRowKeys
          },
          getCheckboxProps: (record) => ({
            props: {
              // 全部默认禁止选中
              // disabled: true,
              // 某几项默认禁止选中
              disabled: record.code === 'super_admin',
              // 某几项默认选中(R: 当state等于1时)
              // defaultChecked: record.state == 1,
            },
          }),
        }
      },
    },

    methods: {
      // 初始化方法
      init (record) {
        this.recordEntity = record
        this.visible = true
        // 加载已有数据
        this.userOwnRole()
        // 获取全部列表,无需分页
        roleFindAll().then((res) => {
          this.loadData = res.data
        })
      },

      /**
       * 获取用户已有角色
       */
      userOwnRole () {
        this.loading = true
        userOwnRole({ userId: this.recordEntity.id }).then((res) => {
          // 选中多选框
          this.selectedRowKeys = res.data
          this.loading = false
        })
      },

      handleSubmit () {
        this.confirmLoading = false
        this.visible = false
        userGrantRole(this.recordEntity.id, this.selectedRowKeys).then((res) => {
           if (res.success) {
             this.$message.success('授权成功')
             this.$emit('ok')
             this.handleCancel()
           }
        }).finally(() => {
           this.confirmLoading = false
        })
      },
      handleCancel () {
        this.recordEntity = []
        this.selectedRowKeys = []
        this.visible = false
      }
    }
  }
</script>
