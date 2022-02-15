<template>
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
    <a slot="action" slot-scope="text">action</a>
    <template
        slot="remark"
        slot-scope="text,record"
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
<script>
import moment from 'moment'
export default {
  name: 'InpatientfeesModule',
  props: {
    inpatientfeesModule: {}
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
      orderDocTitle: '开方医生',
      deptTitle: '住院科室',
      excuteDocTitle: '执行医生',
      excuteDeptTitle: '执行科室',
      loading: false,
      bordered: true
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        customRender: (text, row, index) => {
          return this.rowNo(index)
        },
        width: 70,
        fixed: 'left'
      },
      {
        title: '住院号',
        dataIndex: 'inpatientId',
        width: 100,
        fixed: 'left'
      },
      {
        title: '患者姓名',
        dataIndex: 'patientName',
        width: 100,
        fixed: 'left'
      },
      {
        title: 'HIS结算序号',
        dataIndex: 'settlementId',
        width: 120
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        width: 110
      },
      {
        title: '交易流水号',
        dataIndex: 'transNo',
        width: 140
      },
      {
        title: '项目代码',
        dataIndex: 'itemId',
        width: 130
      },
      {
        title: '项目医保编码',
        dataIndex: 'itemCode',
        width: 130
      },
      {
        title: '项目名称',
        dataIndex: 'itemName',
        width: 160
      },
      {
        title: '项目数量',
        dataIndex: 'itemCount',
        width: 100
      },
      {
        title: '项目单价',
        dataIndex: 'itemPrice',
        width: 100
      },
      {
        title: '项目金额',
        dataIndex: 'itemAmount',
        width: 100
      },
      {
        title: '费用日期',
        dataIndex: 'feeDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        width: 110
      },
      {
        title: '项目类型',
        dataIndex: 'itemTypeName',
        width: 100
      },
      {
        title: '门诊卡号',
        dataIndex: 'jzkh',
        width: 100
      },
      {
        title: this.deptTitle,
        dataIndex: 'deptName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.deptId + '-' + row.deptName
          }
        },
        width: 150
      },
      {
        title: '主治医生',
        dataIndex: 'attendDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.attendDocId + '-' + row.attendDocName
          }
        },
        width: 130
      },
      {
        title: this.orderDocTitle,
        dataIndex: 'orderDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.orderDocId + '-' + row.orderDocName
          }
        },
        width: 130
      },
      {
        title: this.excuteDeptTitle,
        dataIndex: 'excuteDeptName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.excuteDeptId + '-' + row.excuteDeptName
          }
        },
        width: 150
      },
      {
        title: this.excuteDocTitle,
        dataIndex: 'excuteDocName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.excuteDocId + '-' + row.excuteDocName
          }
        },
        width: 130
      },
      {
        title: '计费科室',
        dataIndex: 'feeDeptName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.feeDeptId + '-' + row.feeDeptName
          }
        },
        width: 150
      },
      {
        title: '计费人',
        dataIndex: 'feeOperatorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.feeOperatorId + '-' + row.feeOperatorName
          }
        },
        width: 130
      },
      {
        title: '结算时间',
        dataIndex: 'settlementDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        fixed: 'right',
        width: 120
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
      this.form.resetFields()
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
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
      if (this.inpatientfeesModule.dataType === 0) {
        this.orderDocTitle = '开方医生'
        this.deptTitle = '住院科室'
        this.excuteDocTitle = '执行医生'
        this.excuteDeptTitle = '执行科室'
      } else {
        this.orderDocTitle = '入院责任人'
        this.deptTitle = '入院责任科室'
        this.excuteDocTitle = '办入院操作员'
        this.excuteDeptTitle = '办入院操作员科室'
      }
      this.dataSource = []
      params.applyDateStr = this.inpatientfeesModule.applyDateStr
      params.applyDataId = this.inpatientfeesModule.applyDataId
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
      this.$get('ybReconsiderInpatientfees/ListEq', {
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
