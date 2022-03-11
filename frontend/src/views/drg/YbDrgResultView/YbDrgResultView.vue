<template>
  <a-card :bordered="false" class="card-area">
    <div>
        <a-row
          justify="center"
          align="middle"
        >
          <a-col :span=5>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
              <a-month-picker
                placeholder="请输入复议年月"
                style="width: 105px"
                @change="monthChange"
                v-model="searchApplyDate"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=8>
            <a-select v-model="searchItem.keyField" style="width: 110px">
              <a-select-option
              v-for="d in searchDropDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            =
            <a-input-search placeholder="请输入关键字" v-model="searchItem.value" style="width: 160px" enter-button @search="search" />
          </a-col>
          <a-col :span=3 >
            <a-button
            type="primary"
            @click="onHistoryLook"
            >历史记录</a-button>
          </a-col>
          <a-col :span=8 >
            <a-button type="primary" style="margin-left: 15px" @click.stop="hideExport">
              导出Excel
            </a-button>
            <a-popover v-model="visibleExport" trigger="click" title="导出Excel">
              <a-button
                slot="content"
                style="margin-right:15px"
                @click="exportExcel"
                >导出详情</a-button>
              <a-button
                slot="content"
                style="margin-right:15px"
                @click="exportExcelYj"
                >导出意见</a-button>
              <a-button
                slot="content"
                @click="exportExcelWj"
                >导出文件</a-button>
            </a-popover>
            <a-button type="primary" style="margin-left: 15px" @click="showModal">图片打包下载</a-button>
          </a-col>
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
        <template slot="operationLy" slot-scope="text, record, index">
          <span :title="record.ly">{{record.ly}}</span>
        </template>
        <template slot="operationOperateReason" slot-scope="text, record, index">
          <span :title="record.operateReason">{{record.operateReason}}</span>
        </template>
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
      <div>
        <a-modal width="65%" :maskClosable="false" :footer="null" v-model="downLoadVisible" title="DRG图片打包下载" @ok="handleOk">
          <ybDrgResult-downLoad
          ref="ybDrgResultDownLoad"
          >
          </ybDrgResult-downLoad>
        </a-modal>
      </div>
    </template>
    <!-- 历史 -->
    <ybDrgManage-history
      ref="ybDrgManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybDrgManage-history>
    <!-- 接受申请-查看 -->
    <ybDrgResult-look
      ref="ybDrgResultLook"
      @close="handleLookClose"
      @success="handleLookSuccess"
      :lookVisiable="lookVisiable"
    >
    </ybDrgResult-look>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbDrgManageHistory from '../YbDrgFunModule/YbDrgManageHistoryModule'
import YbDrgResultLook from './YbDrgResultLook'
import YbDrgResultDownLoad from './YbDrgResultDownLoad.vue'
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
  name: 'YbDrgResult',
  components: {
    YbDrgManageHistory, YbDrgResultLook, YbDrgResultDownLoad
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
      searchItem: {keyField: 'ks', value: ''},
      searchDropDataSource: [
        {text: '科室', value: 'ks'},
        {text: '就诊记录号', value: 'jzjlh'},
        {text: '病案号', value: 'bah'},
        {text: '医生工号', value: 'doctorCode'},
        {text: '医生姓名', value: 'doctorName'},
        {text: '序号', value: 'orderNumber'}
      ],
      user: this.$store.state.account.user
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
        title: '科室',
        dataIndex: 'ks',
        fixed: 'left',
        width: 130
      },
      {
        title: '就诊记录号',
        dataIndex: 'jzjlh',
        fixed: 'left',
        width: 100
      },
      {
        title: '病案号',
        dataIndex: 'bah',
        fixed: 'left',
        width: 90
      },
      {
        title: '违规类型',
        dataIndex: 'wglx',
        width: 90
      },
      {
        title: '问题描述',
        dataIndex: 'wtms',
        width: 300
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
        title: '医院意见',
        dataIndex: 'operateReason',
        scopedSlots: { customRender: 'operationOperateReason' },
        ellipsis: true,
        width: 250
      },
      {
        title: '复议科室',
        dataIndex: 'dksName',
        fixed: 'right',
        width: 130
      },
      {
        title: '复议医生',
        dataIndex: 'doctorName',
        customRender: (text, row, index) => {
          if (text !== '' && text !== null) {
            return row.doctorCode + '-' + row.doctorName
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
      this.$refs.ybDrgResultLook.setFormValues(record)
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
        this.$refs.ybDrgManageHistory.setFormValues(target)
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
      this.$export('ybDrgResultView/exportResult', {
        ...queryParams
      })
    },
    exportExcelYj () {
      let queryParams = {}
      queryParams.applyDateStr = this.searchApplyDate
      queryParams.areaType = this.user.areaType.value
      this.$export('ybDrgResultView/exportResultYj', {
        ...queryParams
      })
    },
    exportExcelWj () {
      let queryParams = {}
      queryParams.applyDateStr = this.searchApplyDate
      queryParams.areaType = this.user.areaType.value
      this.$export('ybDrgResultView/exportResultWj', {
        ...queryParams
      })
    },
    showModal (type) {
      this.downLoadVisible = true
      let drgResult = { applyDateStr: this.searchApplyDate, areaType: this.user.areaType.value }
      setTimeout(() => {
        this.$refs.ybDrgResultDownLoad.setFormValues(drgResult)
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
      this.$get('ybDrgResultView', {
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
