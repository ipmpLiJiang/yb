<template>
  <a-drawer
    title="手动剔除"
    :maskClosable="false"
    width=90%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="exceptResetVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <resetData-module
      ref="resetDataModule"
      :ybResetDataModule="ybReconsiderResetExceptDetail"
    >
    </resetData-module>
    <template>
      <!-- 剔除表格区域 -->
      <a-table
        ref="TableInfo1"
        :columns="columns1"
        :rowKey="record => record.id"
        :dataSource="dataSource1"
        :pagination="pagination1"
        :loading="loading1"
        :rowSelection="{selectedRowKeys: selectedRowKeys1, onChange: onSelectChange1}"
        @change="handleTableChange1"
        :customRow="handleClickRow1"
        :bordered="bordered1"
        :scroll="{ x: 900 }"
      >
        <template
          slot="operationDeductReason"
          slot-scope="text, record, index"
        >
          <span :title="record.deductReason">{{record.deductReason}}</span>
        </template>
      </a-table>
    </template>
    <div style="font-weight:bolder;color:red;margin-top:10px;margin-bottom:10px;">
      <a-row
        justify="end"
        type="flex"
      >
        <a-col :span=16>
        </a-col>
        <a-col :span=4>
          剔除汇总扣款金额： {{totalDeductPrice}}
        </a-col>
        <a-col :span=4>
          申诉汇总扣款金额： {{resultDeductPrice}}
        </a-col>
      </a-row>
    </div>
    <template>
      <!-- 申诉表格区域 -->
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClickRow"
        :bordered="bordered"
        :scroll="{ x: 700 }"
      >
        <template
          slot="operationDeductReason"
          slot-scope="text, record, index"
        >
          <span :title="record.deductReason">{{record.deductReason}}</span>
        </template>
        <template
          slot="operationReason"
          slot-scope="text, record, index"
        >
          <span :title="record.operateReason">{{record.operateReason}}</span>
        </template>
        <a
          slot="action"
          slot-scope="text"
        >action</a>
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
    <br>
    <template>
      <a-row
        justify="center"
        type="flex"
      >
        <a-col :span=3>
          <a-popconfirm
            title="确定强制剔除该数据？"
            @confirm="handleReset(1)"
            v-show="!isUpdate"
            :disabled="isUpdate"
            okText="确定"
            cancelText="取消"
          >
            <a-button
              type="primary"
              style="margin-right: .8rem"
            >强制剔除</a-button>
          </a-popconfirm>
        </a-col>
        <a-col :span=3>
          <a-popconfirm
            title="确定剔除该数据？"
            @confirm="handleReset(2)"
            v-show="!isUpdate"
            :disabled="isUpdate"
            okText="确定"
            cancelText="取消"
          >
            <a-button
              type="primary"
              style="margin-right: .8rem"
            >剔除</a-button>
          </a-popconfirm>
        </a-col>
        <a-col :span=1>
          <a-button
            style="margin-right: .8rem"
            @click="onClose"
          >
            取消
          </a-button>
        </a-col>
      </a-row>
    </template>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import ResetDataModule from '../ybFunModule/ResetDataModule'
const formItemLayout = {
  labelCol: {
    span: 7
  },
  wrapperCol: {
    span: 14
  }
}
export default {
  name: 'YbReconsiderResetExceptDetail',
  components: {
    ResetDataModule
  },
  props: {
    exceptResetVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
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
      bordered1: true,
      sortedInfo1: null,
      paginationInfo1: null,
      dataSource1: [],
      selectedRowKeys1: [],
      pagination1: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        onChange: (current, size) => {
          this.pagination1.defaultCurrent = current
          this.pagination1.defaultPageSize = size
        },
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      loading1: false,
      queryParams1: {
      },
      isUpdate: false,
      state: 1,
      seekState: 0,
      totalDeductPrice: 0,
      resultDeductPrice: 0,
      ybReconsiderResetExceptDetail: {}
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
        width: 160
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
        width: 105
      },
      {
        title: '结算日期',
        dataIndex: 'settlementDateStr',
        width: 105
      },
      {
        title: '申请理由',
        scopedSlots: { customRender: 'operationReason' },
        ellipsis: true,
        width: 200
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
        width: 105
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
        width: 140
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
        width: 120
      },
      {
        title: '状态',
        dataIndex: 'state',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return '未剔除'
            case 2:
              return '已剔除'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 90
      }]
    },
    columns1 () {
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
        width: 135
      },
      {
        title: '单据号',
        dataIndex: 'billNo',
        fixed: 'left',
        width: 110
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 160
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
        title: '结算日期',
        dataIndex: 'settlementDateStr',
        width: 105
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
        title: '个人编号',
        dataIndex: 'personalNo',
        width: 110
      },
      {
        title: '科室名称',
        dataIndex: 'deptName',
        width: 120
      },
      {
        title: '状态',
        dataIndex: 'seekState',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '未剔除'
            case 1:
              return '已剔除'
            default:
              return text
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
    handleClickRow (record, index) {
      return {
        on: {
          click: () => {
            let target = this.selectedRowKeys.filter(key => key === record.id)[0]
            if (target === undefined) {
              this.selectedRowKeys.push(record.id)
              this.resultDeductPrice += record.deductPrice
            } else {
              this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1)
              this.resultDeductPrice -= record.deductPrice
            }
          }
        }
      }
    },
    handleClickRow1 (record, index) {
      return {
        on: {
          click: () => {
            let target = this.selectedRowKeys1.filter(key => key === record.id)[0]
            if (target === undefined) {
              this.selectedRowKeys1.push(record.id)
              this.totalDeductPrice += record.deductPrice
            } else {
              this.selectedRowKeys1.splice(this.selectedRowKeys1.indexOf(record.id), 1)
              this.totalDeductPrice -= record.deductPrice
            }
          }
        }
      }
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

      // 取消选中
      this.selectedRowKeys1 = []
      // 重置分页
      this.pagination1.defaultCurrent = 1
      this.$refs.TableInfo1.pagination.current = this.pagination1.defaultCurrent
      if (this.paginationInfo1) {
        this.paginationInfo1.current = this.pagination1.defaultCurrent
        this.paginationInfo1.pageSize = this.pagination1.defaultPageSize
      }
      // 重置列排序规则
      this.sortedInfo1 = null
      this.paginationInfo1 = null
      // 重置查询参数
      this.queryParams1 = {}
      this.dataSource1 = []
    },
    onClose () {
      this.loading = false
      this.loading1 = false
      this.state = 1
      this.seekState = 0
      this.resultDeductPrice = 0
      this.totalDeductPrice = 0
      this.deductPriceReset = 0
      this.deductPriceResult = 0
      this.ybReconsiderResetExceptDetail = {}
      this.reset()
      this.$emit('close', this.isUpdate)
    },
    handleReset (type) {
      if (this.selectedRowKeys.length >= 1) {
        let resetIds = this.ybReconsiderResetExceptDetail.id
        if (this.selectedRowKeys1.length > 0) {
          resetIds += ',' + this.selectedRowKeys1
        }
        let updateParams = {
          resultIds: this.selectedRowKeys,
          resetIds: resetIds
        }
        // let resultIdArr = updateParams.resultIds.split(',')
        let resetIdArr = updateParams.resetIds.split(',')
        if (updateParams.resultIds.length >= 2 && resetIdArr.length >= 2) {
          this.resetDeductPrice()
          this.$message.warning('手动剔除只支持一对多或多对一的数据剔除.')
        } else {
          if (this.totalDeductPrice === this.resultDeductPrice || type === 1) {
            this.deductPriceReset = this.totalDeductPrice
            this.deductPriceResult = this.resultDeductPrice
            this.$put('ybReconsiderResetData/updateHandleResetData', {
              ...updateParams
            }).then((r) => {
              if (r.data.data.success === 1) {
                this.isUpdate = true
                this.state = 2
                this.totalDeductPrice = this.deductPriceReset
                this.resultDeductPrice = this.deductPriceResult
                this.search()
                if (this.dataSource1.length > 0) {
                  this.seekState = 1
                  this.search1()
                }
                this.$message.success(r.data.data.message)
              } else {
                this.resetDeductPrice()
                this.$message.warning(r.data.data.message)
              }
            }).catch(() => {
              this.resetDeductPrice()
              this.$message.error('剔除数据操作失败.')
            })
          } else {
            this.resetDeductPrice()
            this.$message.warning('剔除汇总扣款金额必须等于申诉汇总扣款金额.')
          }
        }
      } else {
        this.resetDeductPrice()
        this.$message.error('列表未选择或选中多个.')
      }
      this.selectedRowKeys = []
      this.selectedRowKeys1 = []
    },
    resetDeductPrice () {
      this.resultDeductPrice = 0
      this.totalDeductPrice = this.ybReconsiderResetExceptDetail.deductPrice
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      this.resultDeductPrice = 0
      for (var id of selectedRowKeys) {
        var target = this.dataSource.filter(item => id === item.id)[0]
        this.resultDeductPrice += target.deductPrice
      }
    },
    onSelectChange1 (selectedRowKeys) {
      this.selectedRowKeys1 = selectedRowKeys
      this.totalDeductPrice = this.ybReconsiderResetExceptDetail.deductPrice
      for (var id of selectedRowKeys) {
        var target = this.dataSource1.filter(item => id === item.id)[0]
        this.totalDeductPrice += target.deductPrice
      }
    },
    setFormValues ({ ...ybReconsiderResetExceptDetail }) {
      this.isUpdate = false
      this.ybReconsiderResetExceptDetail = ybReconsiderResetExceptDetail
      this.totalDeductPrice = ybReconsiderResetExceptDetail.deductPrice
      this.state = 1
      this.resultDeductPrice = 0
      this.deductPriceReset = 0
      this.deductPriceResult = 0
      this.seekState = 0
      this.search1()
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
    search1 () {
      let { sortedInfo } = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch1({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams1
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
    handleTableChange1 (pagination, filters, sorter) {
      this.sortedInfo1 = sorter
      this.paginationInfo1 = pagination
      this.fetch1({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams1
      })
    },
    fetch1 (params = {}) {
      params.id = this.ybReconsiderResetExceptDetail.id
      params.applyDateStr = this.ybReconsiderResetExceptDetail.applyDateStr
      params.areaType = this.ybReconsiderResetExceptDetail.areaType
      params.serialNo = this.ybReconsiderResetExceptDetail.serialNo
      params.billNo = this.ybReconsiderResetExceptDetail.billNo
      params.projectCode = this.ybReconsiderResetExceptDetail.projectCode
      params.projectName = this.ybReconsiderResetExceptDetail.projectName
      params.dataType = this.ybReconsiderResetExceptDetail.dataType
      params.seekState = this.seekState
      this.loading1 = true
      if (this.paginationInfo1) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo1.pagination.current = this.paginationInfo1.current
        this.$refs.TableInfo1.pagination.pageSize = this.paginationInfo1.pageSize
        params.pageSize = this.paginationInfo1.pageSize
        params.pageNum = this.paginationInfo1.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination1.defaultPageSize
        params.pageNum = this.pagination1.defaultCurrent
      }
      this.$get('ybReconsiderResetDataView/findReconsiderResetDataView', {
        ...params
      }).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination1 }
        pagination.total = data.total
        this.loading1 = false
        this.dataSource1 = data.rows
        this.pagination1 = pagination
      })
    },
    fetch (params = {}) {
      params.applyDateStr = this.ybReconsiderResetExceptDetail.applyDateStr
      params.areaType = this.ybReconsiderResetExceptDetail.areaType
      params.serialNo = this.ybReconsiderResetExceptDetail.serialNo
      params.billNo = this.ybReconsiderResetExceptDetail.billNo
      params.projectCode = this.ybReconsiderResetExceptDetail.projectCode
      params.projectName = this.ybReconsiderResetExceptDetail.projectName
      params.dataType = this.ybReconsiderResetExceptDetail.dataType
      params.sourceType = 0
      params.state = this.state
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
