<template>
  <div id="tab" style="margin: 0px!important">
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
          <template
            slot="operation"
            slot-scope="text, record, index"
          >
            <div class="editable-row-operations">
              <span>
                <a
                  @click="() => look(record,index)"
                >查看还款材料</a>
              </span>
            </div>
          </template>
        </a-table>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbReconsiderRepayUnknown',
  props: {
    pid: {
      default: ''
    },
    belongDateStr: {
      default: ''
    },
    dataType: {
      default: 0
    }
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
      loading: false,
      bordered: true,
      ybReconsiderRepay: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 100
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        fixed: 'left',
        width: 150
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        width: 250
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 150
      },
      {
        title: '还款金额',
        dataIndex: 'repaymentPrice',
        width: 150
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr'
      },
      {
        title: '状态',
        dataIndex: 'seekState',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '未还款'
            case 1:
              return '已还款'
            default:
              return text
          }
        },
        width: 120
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 150
      }]
    }
  },
  mounted () {
  },
  methods: {
    moment,
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    look (record, index) {
      record.rowNo = this.rowNo(index)
      this.$emit('look', record)
    },
    exportExcel () {
      if (this.dataSource.length > 0) {
        let queryParams = {}
        queryParams.pid = this.pid
        queryParams.state = 2
        queryParams.dataType = this.dataType
        if (this.belongDateStr !== '') {
          queryParams.belongDateStr = this.belongDateStr
        }

        this.$export('ybReconsiderRepayData/excelRepay', {
          ...queryParams
        })
      } else {
        this.$message.success('导出Excel,无数据!')
      }
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
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
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
      this.fetch()
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
      this.loading = true
      params.pid = this.pid
      params.state = 2
      params.dataType = 0
      if (this.belongDateStr !== '') {
        params.belongDateStr = this.belongDateStr
      }
      // params.dataType = 0
      // params.seekState = 0
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
      params.sortField = 'orderNumber'
      params.sortOrder = 'ascend'
      this.$get('ybReconsiderRepayData', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      })
      this.selectedRowKeys = []
    }
  }
}
</script>

<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
