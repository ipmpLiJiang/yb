<template>
  <a-drawer
    title="查看还款明细"
    :maskClosable="false"
    width=75%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <resetResult-module
    ref="resetResultModule"
    :ybResetResultModule="ybReconsiderRepayExcep"
    >
    </resetResult-module>
    <template>
      <!-- 表格区域 -->
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{type: 'radio', selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClickRow"
        :bordered="bordered"
        :scroll="{ x: 700 }"
      >
        <template slot="operationDeductReason" slot-scope="text, record, index">
          <span :title="record.deductReason">{{record.deductReason}}</span>
        </template>
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
    <br>
    <template>
      <a-row justify="center" type="flex">
        <a-col :span=3>
          <a-popconfirm
            title="确定还款该数据？"
            @confirm="handleRepay"
            v-show="!isUpdate"
            :disabled="isUpdate"
            okText="确定"
            cancelText="取消"
          >
            <a-button type="primary" style="margin-right: .8rem">还款</a-button>
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
import ResetResultModule from '../ybFunModule/ResetResultModule'
export default {
  name: 'YbReconsiderRepayExceptDetail',
  components: {
    ResetResultModule},
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
      isUpdate: false,
      ybReconsiderRepayExcep: {}
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
        title: '单据号',
        dataIndex: 'billNo',
        fixed: 'left',
        width: 120
      },
      {
        title: '项目编码',
        dataIndex: 'projectCode',
        fixed: 'left',
        width: 100
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        fixed: 'left',
        width: 140
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
        title: '还款金额',
        dataIndex: 'repaymentPrice',
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
        title: '剔除状态',
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
    onClose () {
      this.loading = false
      this.ybReconsiderRepayExcep = {}
      this.reset()
      this.$emit('close', this.isUpdate)
    },
    handleRepay () {
      if (this.selectedRowKeys.length === 1) {
        const target = this.dataSource.filter(item => this.selectedRowKeys[0] === item.id)[0]
        let updateParams = {
          resultId: target.resultId,
          repayId: this.ybReconsiderRepayExcep.id
        }
        this.$put('ybReconsiderRepayData/updateHandleRepayData', {
          ...updateParams
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.isUpdate = true
            this.ybReconsiderRepayExcep.orderNumberNew = target.orderNumber
            if (this.dataSource.length !== 1) {
              this.search()
            }
            this.$message.success(r.data.data.message)
          } else {
            this.$message.warning(r.data.data.message)
          }
        }).catch(() => {
          this.$message.error('还款数据操作失败.')
        })
      } else {
        this.$message.error('列表未选择或选中多个.')
      }
      this.selectedRowKeys = []
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    setFormValues ({ ...ybReconsiderRepayExcep }) {
      this.isUpdate = false
      this.ybReconsiderRepayExcep = ybReconsiderRepayExcep
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
      params.applyDateStr = this.ybReconsiderRepayExcep.belongDateStr
      params.billNo = this.ybReconsiderRepayExcep.billNo
      console.log(this.ybReconsiderRepayExcep)
      if (this.ybReconsiderRepayExcep.warnType === 3 && this.ybReconsiderRepayExcep.orderNumberNew !== '' && this.ybReconsiderRepayExcep.orderNumberNew != null) {
        params.orderNumber = this.ybReconsiderRepayExcep.orderNumberNew
      }
      if (this.ybReconsiderRepayExcep.dataType === 0) {
        params.projectName = this.ybReconsiderRepayExcep.projectName
      }
      // let warnType = this.ybReconsiderRepayExcep.warnType
      params.dataType = this.ybReconsiderRepayExcep.dataType
      params.deductPrice = this.ybReconsiderRepayExcep.deductPrice
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

      this.$get('ybReconsiderResetResultView', {
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
