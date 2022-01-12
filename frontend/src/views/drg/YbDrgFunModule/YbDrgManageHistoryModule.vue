<template>
  <a-drawer
    title="DRG历史记录"
    :maskClosable="false"
    width=85%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="historyVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgManageHistory"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgManageHistory"
    >
    </ybDrgJk-module>
    <template>
      <!-- 表格区域 -->
      <a-divider>操作历史</a-divider>
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        :bordered="bordered"
        :scroll="{ x: 700 }"
      >
      <a slot="action" slot-scope="text">action</a>
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
      </a-table>
    </template>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'
export default {
  name: 'YbDrgManageHistoryModule',
  components: {
    YbDrgDataModule, YbDrgJkModule},
  props: {
    historyVisiable: {
      default: false
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
      ybDrgManageHistory: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        customRender: (text, row, index) => {
          return this.rowNo(index)
        },
        width: 70
      },
      {
        title: '复议科室',
        dataIndex: 'readyDksName',
        width: 250
      },
      {
        title: '复议医生',
        dataIndex: 'readyDoctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.readyDoctorCode + '-' + row.readyDoctorName
          }
        },
        width: 180
      },
      {
        title: '状态',
        dataIndex: 'state',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '待接受'
            case 1:
              return '已接受'
            case 2:
              return '已拒绝'
            case 3:
              return '管理员更改'
            case 4:
              return '医保拒绝'
            case 6:
              return '已申诉'
            case 7:
              return '未申诉'
            default:
              return text
          }
        },
        width: 130
      },
      {
        title: '操作过程',
        dataIndex: 'operateProcess',
        width: 150
      },
      {
        title: '操作时间',
        dataIndex: 'operateDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        }
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
    },
    onClose () {
      this.loading = false
      this.ybDrgManageHistory = {}
      this.reset()
      this.$emit('close')
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    setFormValues ({ ...ybDrgManageHistory }) {
      console.log(ybDrgManageHistory)
      this.ybDrgManageHistory = ybDrgManageHistory
      this.fetch()
      setTimeout(() => {
        this.$refs.ybDrgJkModule.search()
      }, 200)
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
      params.applyDateStr = this.ybDrgManageHistory.applyDateStr
      params.applyDataId = this.ybDrgManageHistory.applyDataId
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
      params.sortField = 'operateDate'
      params.sortOrder = 'descend'
      this.$get('ybDrgManage', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      }
      )
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
