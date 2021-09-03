<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <a-col :span=5>
            <a-form-item
              label="复议年月："
              v-bind="formItemLayout"
            >
              <a-month-picker
                placeholder="请选择复议年月"
                style="width: 120px"
                @change="monthChange"
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=4>
              <a-form-item
                label="扣款类型"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="selectDataType"
                   @change="handleDataTypeChange"
                  style="width: 100px"
                >
                  <a-select-option
                    v-for="d in selectDataTypeSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
          </a-col>
          <a-col :span=4>
            <a-form-item
              label="复议结果"
              v-bind="formItemLayout"
            >
              <a-select
                :value="selectResultState"
                 @change="handleResultStateChange"
                style="width: 100px"
              >
                <a-select-option
                  v-for="d in selectResultStateSource"
                  :key="d.value"
                >
                  {{ d.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span=7>
            <a-select v-model="searchItem.keyField" style="width: 115px">
              <a-select-option
              v-for="d in searchDropDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            =
            <a-input-search placeholder="请输入关键字" v-model="searchItem.value" style="width: 170px" enter-button @search="searchPage" />
          </a-col>
          <a-col :span=2>
            <a-button
              style="margin-left: 18px"
              type="primary"
              @click="importExcel"
            >导出</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div>
      <!-- 表格区域 -->
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record. id                      "
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
          <a
            @click.stop="onHistoryLook(record)"
          >
            历史申诉记录
          </a>
        </template>
      </a-table>
    </div>
    <!-- 历史 -->
    <ybAppealManage-history
      ref="ybAppealManageHistory"
      @close="handleHistoryClose"
      @look="look"
      :historyVisiable="historyVisiable"
    >
    </ybAppealManage-history>
    <template>
      <div>
        <a-modal width="85%" :maskClosable="false" :footer="null" v-model="lookVisiable" title="查看申诉材料" @cancel="handleOk">
          <ybAppealManageResultLook-module
          ref="ybAppealManageResultLookModule"
          @close="lookClose"
          >
          </ybAppealManageResultLook-module>
        </a-modal>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealManageResultLookModule from '../ybFunModule/YbAppealManageResultLookModule'
import YbAppealManageHistory from '../ybFunModule/YbAppealManageHistoryReportModule'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbAppealResultReportView',
  components: { YbAppealManageHistory, YbAppealManageResultLookModule },
  data () {
    return {
      advanced: false,
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      formItemLayout,
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
      selectDataTypeSource: [
        { text: '全部', value: 2 },
        { text: '明细扣款', value: 0 },
        { text: '主单扣款', value: 1 }
      ],
      selectDataType: 0,
      selectResultStateSource: [
        { text: '成功', value: 1 },
        { text: '失败', value: 2 }
      ],
      selectResultState: 1,
      spinning: false,
      delayTime: 500,
      bordered: true,
      selectApplyDateStr: this.formatDate(),
      // selectToApplyDateStr: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      searchItem: {keyField: 'serialNo', value: ''},
      searchDropDataSource: [
        {text: '交易流水号', value: 'serialNo'},
        {text: '单据号', value: 'billNo'},
        {text: '项目编码', value: 'projectCode'},
        {text: '项目名称', value: 'projectName'},
        {text: '医生工号', value: 'arDoctorCode'},
        {text: '医生姓名', value: 'arDoctorName'},
        {text: '序号', value: 'orderNumber'}
      ],
      historyVisiable: false,
      lookVisiable: false,
      user: this.$store.state.account.user,
      monthFormat: 'YYYY-MM',
      tableFormat: 'YYYY-MM-DD'
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
        width: 150
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
        width: 170
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
        width: 160
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
        width: 110
      },
      {
        title: '科室名称',
        dataIndex: 'arDeptName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.arDeptCode + '-' + row.arDeptName
          }
        },
        fixed: 'right',
        width: 150
      },
      {
        title: '医生姓名',
        dataIndex: 'arDoctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.arDoctorCode + '-' + row.arDoctorName
          }
        },
        fixed: 'right',
        width: 130
      },
      {
        title: '状态',
        dataIndex: 'isSuccess',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '失败'
            case 1:
              return '成功'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 70
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        fixed: 'right',
        width: 120
      }]
    }
  },
  mounted () {
    // this.fetch()
  },
  methods: {
    moment,
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.selectApplyDateStr = dateString
    },
    // monthToChange (date, dateString) {
    //   this.selectToApplyDateStr = dateString
    // },
    handleHistoryClose () {
      this.historyVisiable = false
    },
    onHistoryLook (record) {
      this.historyVisiable = true
      this.$refs.ybAppealManageHistory.setFormValues(record)
    },
    handleResultStateChange (value) {
      this.selectResultState = value
    },
    handleDataTypeChange (value) {
      this.selectDataType = value
    },
    importExcel () {
      if (this.dataSource.length > 0) {
        let queryParams = {}
        queryParams.applyDateStr = this.selectApplyDateStr
        queryParams.state = this.selectResultState
        queryParams.areaType = this.user.areaType.value
        if (this.selectDataType !== 2) {
          queryParams.dataType = this.selectDataType
        }
        queryParams.sourceType = 0
        if (this.searchItem.value !== '') {
          queryParams.currencyField = this.searchItem.value
        }
        queryParams.keyField = this.searchItem.keyField
        this.$export('ybAppealResultReportView/reportExcel', {
          ...queryParams
        })
      } else {
        this.$message.warning('导出Excel，无数据!')
      }
    },
    // onSelectChange (selectedRowKeys) {
    //   this.selectedRowKeys = selectedRowKeys
    // },
    handleOk (e) {
      this.lookVisiable = false
    },
    lookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      setTimeout(() => {
        this.$refs.ybAppealManageResultLookModule.setFormValues(record)
      }, 200)
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
      params.applyDateStr = this.selectApplyDateStr
      params.state = this.selectResultState
      params.areaType = this.user.areaType.value
      if (this.selectDataType !== 2) {
        params.dataType = this.selectDataType
      }
      params.sourceType = 0
      if (this.searchItem.value !== '') {
        params.currencyField = this.searchItem.value
      }
      params.keyField = this.searchItem.keyField
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
      // params.sortField = 'art.applyDateStr,art.typeno,art.dataType,art.orderNum'
      // params.sortField = 'applyDateStr asc,typeno asc,orderNum'
      // params.sortOrder = 'descend'
      // params.sortOrder = 'ascend'

      this.$get('ybAppealResultReportView', {
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
<style scoped>
.ant-card-body {
  padding-top: 0px;
  zoom: 1;
}
</style>
