<template>
  <a-card :bordered="false" class="card-area">
    <div style="text-align:left;margin-bottom:16px">
      <a-row>
        复议年月：
        <a-month-picker
          placeholder="请输入复议年月"
          style="width: 105px;margin-right: 6px"
          @change="monthChange"
          v-model="searchApplyDate"
          :default-value="searchApplyDate"
          :format="monthFormat"
        />
        <a-select v-model="searchItem.keyField" style="width: 110px;margin-right: 6px">
          <a-select-option
          v-for="d in searchDropDataSource"
          :key="d.value"
          >
          {{ d.text }}
          </a-select-option>
        </a-select>
        =
        <a-input-search
          placeholder="请输入关键字"
          v-model="searchItem.value"
          style="width: 180px;margin-right: 22px"
          enter-button
          @search="search" />
        <a-button type="primary" style="margin-right: 22px" @click.stop="hideExport">
          导出Excel
        </a-button>
        <a-popover v-model="visibleExport" trigger="click" title="导出Excel">
          <a-button
            slot="content"
            style="margin-right: 6px"
            @click="exportExcel"
            >导出详情</a-button>
          <a-button
            slot="content"
            @click="exportExcelWj"
            >导出文件</a-button>
        </a-popover>
        <a-button type="primary" style="margin-right: 22px" @click="showModal">图片打包下载</a-button>
        <a-button
        type="primary"
        @click="onHistoryLook"
        >历史记录</a-button>
      </a-row>
    </div>
    <div>
      <!-- 表格区域 -->
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="(record) => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{
          type: 'radio',
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        @change="handleTableChange"
        size="small"
        :customRow="handleClickRow"
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
                  @click.stop="() => look(record,index)"
                >查看</a>
              </span>
            </div>
          </template>
      </a-table>
    </div>
    <template>
      <a-modal width="55%" :maskClosable="false" :footer="null" v-model="downLoadVisible" title="文件打包下载" @ok="handleOk">
        <ybChsResult-downLoad
        ref="ybChsResultDownLoad"
        >
        </ybChsResult-downLoad>
      </a-modal>
    </template>
    <!-- 历史 -->
    <ybChsManage-history
      ref="ybChsManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybChsManage-history>
    <!-- 接受申请-查看 -->
    <ybChsResult-look
      ref="ybChsResultLook"
      @close="handleLookClose"
      @success="handleLookSuccess"
      :lookVisiable="lookVisiable"
    >
    </ybChsResult-look>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbChsManageHistory from '../YbChsFunModule/YbChsManageHistoryModule'
import YbChsResultLook from './YbChsResultLook'
import YbChsResultDownLoad from './YbChsResultDownLoad.vue'
const formItemLayout = {
  labelCol: {
    span: 8
  },
  wrapperCol: {
    span: 15,
    offset: 1
  }
}
export default {
  name: 'YbChsResult',
  components: {
    YbChsManageHistory, YbChsResultLook, YbChsResultDownLoad
  },
  data () {
    return {
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
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      queryParams: {},
      loading: false,
      bordered: true,
      historyVisiable: false,
      lookVisiable: false,
      visibleExport: false,
      downLoadVisible: false,
      searchText: '',
      searchApplyDate: this.formatDate(),
      monthFormat: 'YYYY-MM',
      searchItem: {keyField: 'zymzNumber', value: ''},
      searchDropDataSource: [
        {text: '住院门诊号', value: 'zymzNumber'},
        {text: '参保人', value: 'insuredName'},
        {text: '项目名称', value: 'projectName'},
        {text: '医生工号', value: 'doctorCode'},
        {text: '医生姓名', value: 'doctorName'},
        {text: '序号', value: 'orderNum'}
      ],
      user: this.$store.state.account.user
    }
  },
  computed: {
    columns () {
      return [{
        title: '序号',
        dataIndex: 'orderNum',
        fixed: 'left',
        width: 70
      },
      {
        title: '医疗类别',
        dataIndex: 'medicalType',
        fixed: 'left',
        width: 100
      },
      {
        title: '住院门诊号',
        dataIndex: 'zymzNumber',
        fixed: 'left',
        width: 90
      },
      {
        title: '参保人',
        dataIndex: 'insuredName',
        fixed: 'left',
        width: 80
      },
      {
        title: '入院日期',
        dataIndex: 'enterHospitalDate',
        width: 100
      },
      {
        title: '出院日期',
        dataIndex: 'outHospitalDate',
        width: 100
      },
      {
        title: '结算日期',
        dataIndex: 'settlementDate',
        width: 100
      },
      {
        title: '身份证号',
        dataIndex: 'cardNumber',
        width: 100
      },
      {
        title: '医保项目名称',
        dataIndex: 'projectName',
        width: 220,
        ellipsis: true
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 150,
        ellipsis: true
      },
      {
        title: '初审违规金额（元）',
        dataIndex: 'violateCsPrice',
        width: 90
      },
      {
        title: '违规金额（元）',
        dataIndex: 'violatePrice',
        width: 90
      },
      {
        title: '违规内容',
        dataIndex: 'violateReason',
        width: 150,
        ellipsis: true
      },
      {
        title: '费用日期',
        dataIndex: 'costDate',
        width: 100
      },
      {
        title: '险种类型',
        dataIndex: 'insuredType',
        width: 110
      },
      {
        title: '单价（元）',
        dataIndex: 'price',
        width: 90
      },
      {
        title: '数量',
        dataIndex: 'num',
        width: 90
      },
      {
        title: '金额（元）',
        dataIndex: 'medicalPrice',
        width: 90
      },
      {
        title: '复议科室',
        dataIndex: 'resultDksName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.resultDksId + '-' + row.resultDksName
          }
        },
        fixed: 'right',
        width: 170
      },
      {
        title: '复议医生',
        dataIndex: 'resultDoctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.resultDoctorCode + '-' + row.resultDoctorName
          }
        },
        fixed: 'right',
        width: 120
      },
      {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {
          customRender: 'operation'
        },
        fixed: 'right',
        width: 80
      }
      ]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    moment,
    hideExport () {
      this.visibleExport = true
    },
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    handleClickRow (record, index) {
      return {
        on: {
          click: () => {
            let target = this.selectedRowKeys.filter(key => key === record.id)[0]
            if (target === undefined) {
              this.selectedRowKeys = []
              this.selectedRowKeys.push(record.id)
            } else {
              this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1)
            }
          }
        }
      }
    },
    rowNo (index) {
      return (this.pagination.defaultCurrent - 1) *
            this.pagination.defaultPageSize + index + 1
    },
    handleLookSuccess () {
      this.lookVisiable = false
    },
    handleLookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      this.$refs.ybChsResultLook.setFormValues(record)
    },
    handleHistorySuccess () {
      this.historyVisiable = false
    },
    handleHistoryClose () {
      this.historyVisiable = false
    },
    onHistoryLook () {
      let selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length === 1) {
        let target = this.dataSource.filter(item => selectedRowKeys[0] === item.id)[0]
        let indOf = this.dataSource.indexOf(target)
        target.rowNo = this.rowNo(indOf)
        this.historyVisiable = true
        this.$refs.ybChsManageHistory.setFormValues(target)
      } else if (selectedRowKeys.length === 0) {
        this.$message.warning('未选择行')
      } else {
        this.$message.warning('请选择单行')
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    exportExcel () {
      let queryParams = {}
      queryParams.applyDateStr = this.searchApplyDate
      queryParams.areaType = this.user.areaType.value
      this.$export('ybChsResultView/exportResult', {
        ...queryParams
      })
    },
    exportExcelWj () {
      let queryParams = {}
      queryParams.applyDateStr = this.searchApplyDate
      queryParams.areaType = this.user.areaType.value
      this.$export('ybChsResultView/exportResultWj', {
        ...queryParams
      })
    },
    showModal (type) {
      this.downLoadVisible = true
      let chsResult = { applyDateStr: this.searchApplyDate, areaType: this.user.areaType.value }
      setTimeout(() => {
        this.$refs.ybChsResultDownLoad.setFormValues(chsResult)
      }, 200)
    },
    handleOk (e) {
      this.downLoadVisible = false
    },
    search () {
      let {
        sortedInfo
      } = this
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
      params.applyDateStr = this.searchApplyDate
      params.currencyField = this.searchItem.value
      params.areaType = this.user.areaType.value
      params.keyField = this.searchItem.keyField
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
      this.$get('ybChsResultView', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = {
          ...this.pagination
        }
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
