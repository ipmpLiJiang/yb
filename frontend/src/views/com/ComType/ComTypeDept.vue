<template>
  <div>
    <a-form :form="form">
    <a-row type="flex" justify="start">
      <a-col :span=8>
      <a-form-item
        v-bind="formItemLayout"
        label="名称"
      >
        <a-input
          placeholder="请输入名称"
          v-decorator="['ctName', {rules: [{ required: true, message: '名称不能为空' }] }]"
        />
      </a-form-item>
      </a-col>
      <a-col>
        <a-button
            @click="handleSubmit"
            type="primary"
          >添加/保存</a-button>
        <a-button
            style="margin-left: 20px"
            @click="search"
            type="primary"
          >刷新</a-button>
        <a-popconfirm
          style="margin-left: 20px"
            title="确定返回列表？"
            @confirm="onClose"
            okText="确定"
            cancelText="取消"
          >
            <a-button style="margin-right: .8rem">返回列表</a-button>
          </a-popconfirm>
      </a-col>
    </a-row>
    </a-form>
    <!-- 表格区域 -->
    <a-table
      ref="TableInfo"
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="dataSource"
      :pagination="pagination"
      :loading="loading"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      @change="handleTableChange"
      :bordered="bordered"
      :scroll="{ x: 900 }"
    >
      <template slot="operation" slot-scope="text, record">
      <div class="editable-row-operations">
        <span v-if="record.editable">
            <a @click.stop="() => save(record.id)">确定</a>
            <a-divider type="vertical" />
            <a-popconfirm
            title="确定放弃编辑？"
            @confirm="() => cancel(record.id)"
            >
            <a>取消</a>
            </a-popconfirm>
        </span>
        <span v-else>
          <a
            :disabled="editingKey !== ''"
            @click.stop="() => edit(record.id)"
            >编辑</a>
        </span>
      </div>
    </template>
    </a-table>
  </div>
</template>

<script>
const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'ComTypeDept',
  props: {
  },
  data () {
    return {
      formItemLayout,
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        onChange: (current, size) => {
          this.pagination.defaultCurrent = current
          this.pagination.defaultPageSize = size
        },
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {
      },
      ctType: 0,
      editingKey: '',
      cacheData: [],
      loading: false,
      bordered: true,
      form: this.$form.createForm(this),
      ybAppealConfireData: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        customRender: (text, row, index) => {
          return this.rowNo(index)
        },
        width: 90,
        fixed: 'left'
      },
      {
        title: '名称',
        dataIndex: 'ctName',
        scopedSlots: { customRender: 'ctName' }
      }]
      // {
      //   title: '操作',
      //   dataIndex: 'operation',
      //   scopedSlots: { customRender: 'operation' },
      //   fixed: 'right',
      //   width: 130
      // }
    }
  },
  mounted () {
    // this.search()
  },
  methods: {
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.pagination.defaultCurrent = 1
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列排序规则
      this.sortedInfo = null
      this.paginationInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.dataSource = []
      this.form.resetFields()
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    del (record) {
      this.$delete('comType/' + record.id).then(() => {
        this.$message.success('删除成功')
        this.selectedRowKeys = []
        this.search()
      }
      )
    },
    onClose () {
      this.form.resetFields()
      this.editingKey = ''
      this.cacheData = ''
      this.$emit('close')
    },
    handleChange (val, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = val
        this.dataSource = newData
      }
    },
    edit (key) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      this.editingKey = key
      if (target) {
        target.editable = true
        this.dataSource = newData
      }
    },
    save (key) {
      const newData = [...this.dataSource]
      const newCacheData = [...this.cacheData]

      const target = newData.filter(item => key === item.id)[0]
      if (target !== undefined) {
        const targetCache = newCacheData.filter(item => key === item.id)[0]
        if (target && targetCache) {
          delete target.editable
          let comTy = {
            id: target.id,
            ctName: target.ctName,
            ctType: target.ctType
          }
          this.$put('comType', {
            ...comTy
          }).then((r) => {
            if (r.data.data.success === 1) {
              this.$message.success('操作成功.')
              this.search()
            } else {
              this.$message.error(r.data.data.message)
              this.dataSource = newCacheData
              Object.assign(target, targetCache)
              this.cacheData = newData
            }
          }).catch(() => {
            this.$message.error('操作失败.')
          })
        }
        this.editingKey = ''
      } else {
        this.$message.warning('未找到对象')
      }
    },
    cancel (key) {
      const newData = [...this.dataSource]
      const target = newData.filter(item => key === item.id)[0]
      this.editingKey = ''
      if (target) {
        Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
        delete target.editable
        this.dataSource = newData
      }
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let comTy = this.form.getFieldsValue()
          comTy.ctType = this.ctType
          this.$put('comType', {
            ...comTy
          }).then((r) => {
            if (r.data.data.success === 1) {
              this.searchPage(this.ctType)
              this.form.resetFields()
            } else {
              this.$message.error(r.data.data.message)
            }
          }).catch(() => {
            this.$message.error('操作失败.')
          })
        }
      })
    },
    searchPage (type) {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      this.ctType = type
      this.editingKey = ''
      this.cacheData = ''
      this.search()
    },
    search () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams
      })
    },
    handleTableChange (pagination, filters, sorter) {
      this.sortedInfo = sorter
      this.paginationInfo = pagination
      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams
      })
    },
    fetch (params = {}) {
      params.ctType = this.ctType
      this.loading = true
      this.editingKey = ''
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      // params.sortField = 'IS_DELETEMARK asc, orderNum'
      // params.sortOrder = 'ascend'
      this.$get('comType', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
        this.cacheData = this.dataSource.map(item => ({ ...item }))
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
</style>
