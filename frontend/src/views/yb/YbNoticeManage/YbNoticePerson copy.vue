<template>
  <div>
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
      :scroll="{ x: 500 }"
    >
      <template
        slot="remark"
        slot-scope="text, record"
      >
        <a-popover placement="topLeft">
          <template slot="content">
            <div style="max-width: 200px">{{text}}</div>
          </template>
          <p style="width: 200px;margin-bottom: 0">{{text}}</p>
        </a-popover>
      </template>
      <template
          slot="operation"
          slot-scope="text, record"
        >
          <a-popconfirm
          title="确定删除？"
          @confirm="() => del(record)"
          >
          <a>删除</a>
          </a-popconfirm>
        </template>
    </a-table>
  </div>
</template>

<script>
export default {
  name: 'YbNoticePerson',
  props: {
  },
  data () {
    return {
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
      pid: '',
      loading: false,
      bordered: true,
      ybNoticePerson: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '人员',
        dataIndex: 'personName'
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 100
      }]
    }
  },
  mounted () {
    // this.search()
  },
  methods: {
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
      this.$delete('YbNoticeData/' + record.id).then(() => {
        this.$message.success('删除成功')
        this.selectedRowKeys = []
        this.search()
      }
      )
    },
    searchPage (id) {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
      if (id === '') {
        this.dataSource = []
      } else {
        this.pid = id
        this.search()
      }
    },
    add (obj) {
      let ds = [{id: '', pid: '', personCode: obj.personCode, personName: obj.personName, ndType: 2}]
      for (var i in this.dataSource) {
        let item = {
          id: this.dataSource[i].id,
          pid: this.dataSource[i].pid,
          personCode: this.dataSource[i].personCode,
          personName: this.dataSource[i].personName,
          ndType: this.dataSource[i].ndType
        }
        ds.push(item)
      }
      const pagination = { ...this.pagination }
      pagination.total = ds.length
      this.dataSource = ds
      this.pagination = pagination
      console.log(this.dataSource)
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
      params.pid = this.pid
      params.ndType = 2
      this.loading = true
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

      this.$get('YbNoticeData', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
