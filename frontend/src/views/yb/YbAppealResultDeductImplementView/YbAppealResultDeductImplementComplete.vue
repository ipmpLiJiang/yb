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
          @change="handleTableChange"
          :bordered="bordered"
          :scroll="{ x: 900 }"
        >
          <template slot="operationDeductReason" slot-scope="text, record, index">
            <span :title="record.deductReason">{{record.deductReason}}</span>
          </template>
        </a-table>
  </div>
</template>

<script>
import moment from 'moment'
import { custom } from '../../js/custom'
export default {
  name: 'YbAppealResultDeductImplementComplete',
  props: {
    applyDateStr: {
      default: ''
    },
    applyDateToStr: {
      default: ''
    },
    defaultFormatDate: {
      default: ''
    },
    searchText: {
      default: ''
    },
    searchDataType: {
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
      ybAppealResultDeductImplement: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 70
      },
      {
        title: '交易流水号',
        dataIndex: 'serialNo',
        fixed: 'left',
        width: 140
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 140
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 200
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 105
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 180
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 100
      },
      {
        title: '扣除原因',
        scopedSlots: { customRender: 'operationDeductReason' },
        ellipsis: true,
        width: 250
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
        width: 130
      },
      {
        title: '科室名称',
        dataIndex: 'arDeptName',
        width: 120
      },
      {
        title: '医生姓名',
        dataIndex: 'arDoctorName',
        width: 110
      },
      {
        title: '扣款类型',
        dataIndex: 'dataType',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '明细扣款'
            case 1:
              return '主单扣款'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 90
      },
      {
        title: '落实年月',
        dataIndex: 'implementDateStr',
        fixed: 'right',
        width: 90
      },
      {
        title: '分摊方式',
        dataIndex: 'shareState',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '个人分摊'
            case 1:
              return '科室分摊'
            case 2:
              return '其他分摊'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 90
      },
      {
        title: '分摊方案',
        dataIndex: 'shareProgramme',
        fixed: 'right',
        width: 250
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
    searchPage () {
      this.pagination.defaultCurrent = 1
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
      }
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
      let dateStr = this.applyDateStr
      let dateToStr = this.applyDateToStr

      let arrDateStr = custom.resetApplyDateStr(dateStr, dateToStr, this.defaultFormatDate)
      dateStr = arrDateStr[0]
      dateToStr = arrDateStr[1]

      let msg = custom.checkApplyDateStr(dateStr, dateToStr, 3)
      if (msg === '') {
        this.loading = true
        params.applyDateFrom = dateStr
        params.applyDateTo = dateToStr
        params.currencyField = this.searchText
        if (this.searchDataType !== 2) {
          params.dataType = this.searchDataType
        }
        params.deductImplementId = '1' // 随便传一个值，代表已经填过扣款落实表
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
        params.sortField = 'applyDateStr asc,orderNum'
        // params.sortOrder = 'descend'
        params.sortOrder = 'ascend'
        this.$get('ybAppealResultDeductimplementView', {
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
      } else {
        this.$message.error(msg)
      }
    }
  }
}
</script>

<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
