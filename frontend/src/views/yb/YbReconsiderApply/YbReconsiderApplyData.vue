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
      :scroll="{ x: 900 }"
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
    </a-table>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbReconsiderApplyData',
  props: {
    pid: {
      default: '0'
    },
    typeno: {
      default: 1
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
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {
      },
      loading: false,
      bordered: true,
      ybReconsiderApplyData: {},
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        width: 70
      },
      {
        title: '交易流水号',
        dataIndex: 'serialNo',
        width: 120
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        width: 120
      },
      {
        title: '意见书编码',
        dataIndex: 'proposalCode',
        width: 120
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        width: 100
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        width: 100
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 100
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 110
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 150
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 100
      },
      {
        title: '扣除原因',
        dataIndex: 'deductReason',
        width: 150
      },
      {
        title: '还款原因',
        dataIndex: 'repaymentReason',
        width: 150
      },
      {
        title: '医生姓名',
        dataIndex: 'doctorName',
        width: 100
      },
      {
        title: '科室编码',
        dataIndex: 'deptCode',
        width: 100
      },
      {
        title: '科室名称',
        dataIndex: 'deptName',
        width: 100
      },
      {
        title: '入院日期',
        dataIndex: 'enterHospitalDateStr',
        customRender: (text, row, index) => {
          return moment(text).format('YYYY-MM-DD')
        },
        width: 120
      },
      {
        title: '出院日期',
        dataIndex: 'outHospitalDateStr',
        customRender: (text, row, index) => {
          return moment(text).format(this.tableFormat)
        },
        width: 120
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
        customRender: (text, row, index) => {
          return moment(text).format(this.tableFormat)
        },
        width: 120
      },
      {
        title: '住院号',
        dataIndex: 'hospitalizedNo',
        width: 100
      },
      {
        title: '就医方式',
        dataIndex: 'treatmentMode',
        width: 100
      },
      {
        title: '结算日期',
        dataIndex: 'settlementDateStr',
        customRender: (text, row, index) => {
          return moment(text).format(this.tableFormat)
        },
        width: 120
      },
      {
        title: '个人编号',
        dataIndex: 'personalNo',
        width: 110
      },
      {
        title: '参保人',
        dataIndex: 'insuredName',
        width: 100
      },
      {
        title: '医保卡号',
        dataIndex: 'cardNumber',
        width: 100
      },
      {
        title: '统筹区',
        dataIndex: 'areaName',
        width: 100
      },
      {
        title: '版本号',
        dataIndex: 'versionNumber',
        width: 100
      },
      {
        title: '反馈申诉',
        dataIndex: 'backAppeal',
        width: 100
      }]
    }
  },
  mounted () {
    // this.search()
  },
  methods: {
    moment,
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
      params.pid = this.pid
      params.typeno = this.typeno
      params.dataType = 0
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
      params.sortField = 'orderNumber'
      params.sortOrder = 'ascend'
      this.$get('ybReconsiderApplyData/ListReconsiderApplyData', {
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
