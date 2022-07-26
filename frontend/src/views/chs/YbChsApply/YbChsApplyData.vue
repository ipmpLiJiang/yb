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
      size="small"
      :bordered="bordered"
      :scroll="{ x: 900 }"
    >
      <template slot="operationLy" slot-scope="text, record, index">
        <span :title="record.ly">{{record.ly}}</span>
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
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: 'YbChsApplyData',
  props: {
    pid: {
      default: '0'
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
      ybChsApplyData: {},
      tableFormat: 'YYYY-MM-DD'
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNum',
        width: 65
      },
      {
        title: '申诉截止日期',
        dataIndex: 'appealEndDate',
        width: 110
      },
      {
        title: '支付地点类别',
        dataIndex: 'payPlaceType',
        width: 110
      },
      {
        title: '疑点状态',
        dataIndex: 'ydState',
        width: 90
      },
      {
        title: '医保区划',
        dataIndex: 'areaName',
        width: 180
      },
      {
        title: '医药机构编码',
        dataIndex: 'yyjgCode',
        width: 150
      },
      {
        title: '医药机构名称',
        dataIndex: 'yyjgName',
        width: 200
      },
      {
        title: '科室',
        dataIndex: 'deptName',
        width: 150
      },
      {
        title: '医生',
        dataIndex: 'doctorName',
        width: 120
      },
      {
        title: '医疗类别',
        dataIndex: 'medicalType',
        width: 120
      },
      {
        title: '住院门诊号',
        dataIndex: 'zymzNumber',
        width: 120
      },
      {
        title: '参保人',
        dataIndex: 'insuredName',
        width: 120
      },
      {
        title: '入院日期',
        dataIndex: 'enterHospitalDate',
        width: 110
      },
      {
        title: '出院日期',
        dataIndex: 'outHospitalDate',
        width: 110
      },
      {
        title: '结算日期',
        dataIndex: 'settlementDate',
        width: 110
      },
      {
        title: '身份证号',
        dataIndex: 'cardNumber',
        width: 160
      },
      {
        title: '医保项目编码',
        dataIndex: 'projectCode',
        width: 300
      },
      {
        title: '医保项目名称',
        dataIndex: 'projectName',
        width: 300
      },
      {
        title: '医院项目名称',
        dataIndex: 'projectYyName',
        width: 250
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 250
      },
      {
        title: '初审违规金额（元）',
        dataIndex: 'violateCsPrice',
        width: 120
      },
      {
        title: '违规金额（元）',
        dataIndex: 'violatePrice',
        width: 120
      },
      {
        title: '违规内容',
        dataIndex: 'violateReason',
        width: 300
      },
      {
        title: '诊断信息',
        dataIndex: 'zdNote',
        width: 120
      },
      {
        title: '费用日期',
        dataIndex: 'costDate',
        width: 110
      },
      {
        title: '险种类型',
        dataIndex: 'insuredType',
        width: 150
      },
      {
        title: '单价（元）',
        dataIndex: 'price',
        width: 120
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 120
      },
      {
        title: '金额（元）',
        dataIndex: 'medicalPrice',
        width: 120
      },
      {
        title: '统筹支付（元）',
        dataIndex: 'tcPayPrice',
        width: 120
      },
      {
        title: '规格',
        dataIndex: 'specs',
        width: 120
      },
      {
        title: '剂型',
        dataIndex: 'jx',
        width: 100
      },
      {
        title: '机构等级',
        dataIndex: 'jgLevel',
        // scopedSlots: { customRender: 'operationLy' },
        ellipsis: true,
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
      params.sortField = 'orderNum'
      params.sortOrder = 'ascend'
      this.$get('ybChsApplyData/findChsApplyDataList', {
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
