<template>
  <a-drawer
    title="查看剔除明细"
    :maskClosable="false"
    width=75%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <resetData-module
    ref="resetDataModule"
    :ybResetDataModule="ybReconsiderResetData"
    >
    </resetData-module>
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
import ResetDataModule from './ResetDataModule'
export default {
  name: 'YbReconsiderResetDataModule',
  components: {
    ResetDataModule},
  props: {
    lookVisiable: {
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
      ybReconsiderResetData: {}
    }
  },
  computed: {
    columns () {
      return [{
        title: '意见书编码',
        dataIndex: 'proposalCode',
        fixed: 'left',
        width: 140
      },
      {
        title: '交易流水号',
        dataIndex: 'serialNo',
        fixed: 'left',
        width: 135
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        fixed: 'left',
        width: 105
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 140
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 70
      },
      {
        title: '医保内金额',
        dataIndex: 'medicalPrice',
        width: 105
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 140
      },
      {
        title: '扣除金额',
        dataIndex: 'deductPrice',
        width: 100
      },
      {
        title: '扣除原因',
        dataIndex: 'deductReason',
        width: 250
      },
      {
        title: '费用日期',
        dataIndex: 'costDateStr',
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
        title: '申请理由',
        dataIndex: 'operateReason',
        width: 120
      },
      {
        title: '申请日期',
        dataIndex: 'operateDate',
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
        title: '科室名称',
        dataIndex: 'arDeptName',
        fixed: 'right',
        width: 120
      },
      {
        title: '医生姓名',
        dataIndex: 'arDoctorName',
        fixed: 'right',
        width: 105
      },
      {
        title: '状态',
        dataIndex: 'resetDataId',
        customRender: (text, row, index) => {
          if (text !== null) {
            return '已剔除'
          } else {
            return '未剔除'
          }
        },
        fixed: 'right',
        width: 90
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
      this.ybReconsiderResetData = {}
      this.reset()
      this.$emit('close')
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    setFormValues ({ ...ybReconsiderResetData }) {
      this.ybReconsiderResetData = ybReconsiderResetData
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
      params.applyDateStr = this.ybReconsiderResetData.applyDateStr
      params.billNo = this.ybReconsiderResetData.billNo
      params.serialNo = this.ybReconsiderResetData.serialNo
      params.ruleName = this.ybReconsiderResetData.ruleName
      params.projectCode = this.ybReconsiderResetData.projectCode
      params.projectName = this.ybReconsiderResetData.projectName
      params.personalNo = this.ybReconsiderResetData.personalNo
      params.dataType = this.ybReconsiderResetData.dataType
      params.sourceType = 0
      params.state = 2
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
      params.sortField = 'create_Time'
      params.sortOrder = 'descend'
      this.$get('ybAppealResultView/findAppealResultViewReset', {
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
