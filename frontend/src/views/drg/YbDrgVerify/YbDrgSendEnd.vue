
<template>
  <!-- 已审核 表格区域 -->
  <a-table
    ref="TableInfo"
    :columns="columns"
    :rowKey="record => record.id"
    :dataSource="dataSource"
    :pagination="pagination"
    :loading="loading"
    @change="handleTableChange"
    size="small"
    :bordered="bordered"
    :scroll="{ x: 900 }"
  >
    <template slot="operationDeductReason" slot-scope="text, record, index">
      <span :title="record.deductReason">{{record.deductReason}}</span>
    </template>
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

<script>
import moment from 'moment'
export default {
  name: 'YbDrgSendEnd',
  props: {
    applyDate: {
      default: ''
    },
    searchItem: {
      default () {
        return {}
      }
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
      ybDrgVerify: {},
      user: this.$store.state.account.user,
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNumber',
        fixed: 'left',
        width: 65
      },
      {
        title: '科室',
        dataIndex: 'ks',
        fixed: 'left',
        width: 120
      },
      {
        title: '就诊记录号',
        dataIndex: 'jzjlh',
        fixed: 'left',
        width: 90
      },
      {
        title: '病案号',
        dataIndex: 'bah',
        fixed: 'left',
        width: 80
      },
      {
        title: '违规类型',
        dataIndex: 'wglx',
        width: 120
      },
      {
        title: '问题描述',
        dataIndex: 'wtms',
        width: 350
      },
      {
        title: '医疗总费用',
        dataIndex: 'ylzfy',
        width: 100
      },
      {
        title: '违规金额',
        dataIndex: 'wgje',
        width: 100
      },
      {
        title: '是否编码造成直接错误',
        dataIndex: 'sfbmzczjcw',
        width: 110
      },
      {
        title: '理由',
        dataIndex: 'ly',
        scopedSlots: { customRender: 'operationLy' },
        ellipsis: true,
        width: 250
      },
      {
        title: '复议科室',
        dataIndex: 'verifyDksName',
        fixed: 'right',
        width: 150
      },
      {
        title: '复议医生',
        dataIndex: 'verifyDoctorName',
        fixed: 'right',
        width: 120
      },
      {
        title: '发送日期',
        dataIndex: 'sendDate',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return moment(text).format('YYYY-MM-DD')
          } else {
            return text
          }
        },
        fixed: 'right',
        width: 95
      }]
    }
  },
  mounted () {
    // this.fetch()
  },
  methods: {
    moment,
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    // onSelectChange (selectedRowKeys) {
    //   this.selectedRowKeys = selectedRowKeys
    // },
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
      this.loading = true
      params.applyDateStr = this.applyDate
      params.state = 3
      params.areaType = this.user.areaType.value
      if (this.searchItem !== undefined) {
        if (this.searchItem.item.ks !== '') {
          params.ks = this.searchItem.item.ks
        }
        if (this.searchItem.item.jzjlh !== '') {
          params.jzjlh = this.searchItem.item.jzjlh
        }
        if (this.searchItem.item.bah !== '') {
          params.bah = this.searchItem.item.bah
        }
        if (this.searchItem.doctor.docCode !== '') {
          params.verifyDoctorCode = this.searchItem.doctor.docCode
        }
        if (this.searchItem.doctor.docName !== '') {
          params.verifyDoctorName = this.searchItem.doctor.docName
        }
        if (this.searchItem.dept.dksName !== '') {
          params.verifyDksName = this.searchItem.dept.dksName
        }
        if (this.searchItem.order.orderNumber !== '') {
          params.orderNumber = this.searchItem.order.orderNumber
        }
      }
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
      // params.sortField = 'rv.orderNum'
      // params.sortOrder = 'ascend'
      this.$get('ybDrgVerifyView', {
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
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
