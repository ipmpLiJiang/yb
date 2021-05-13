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
      @change="handleTableChange"
      :bordered="bordered"
      :scroll="{ x: 700 }"
    >
        <template slot="operationDeductReason" slot-scope="text, record, index">
          <span :title="record.deductReason">{{record.deductReason}}</span>
        </template>
    </a-table>
    <br>
    <a-form :form="form">
      <a-row type="flex" justify="start">
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="扣款金额"
          >
            {{ybAppealResultRepaymentView.deductPrice}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="还款金额"
          >
            {{ybAppealResultRepaymentView.repaymentPrice}}
          </a-form-item>
        </a-col>
        <a-col :span=16>
          <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19, offset: 1 }
            }"
            label="还款方案"
          >
            <a-textarea
              placeholder="请输入还款方案"
              v-decorator="['repaymentProgramme', {rules: [{ required: true, message: '还款方案不能为空' }] }]"
              :rows="6"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <br>
    </a-form>
    <div class="drawer-bootom-button">
      <a-popconfirm
        title="确定放弃编辑？"
        @confirm="onClose"
        v-show="isShow"
        okText="确定"
        cancelText="取消"
      >
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button
        @click="onClose"
        v-show="!isShow"
        style="margin-right: .8rem">
      取消</a-button>
      <a-popconfirm
        title="确定提交数据？"
        @confirm="handleSubmit"
        v-show="isShow"
        okText="确定"
        cancelText="取消"
      >
        <a-button type="primary" style="margin-right: .8rem">提交</a-button>
      </a-popconfirm>
    </div>
  </div>
</template>
<script>
import moment from 'moment'
const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 13, offset: 1 }
}
export default {
  name: 'YbAppealResultRepaymentEdit',
  props: {
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
      isShow: false,
      monthFormat: 'YYYY-MM',
      selectImplementDateStr: this.formatDate(),
      form: this.$form.createForm(this),
      ybAppealResultRepaymentView: {
      },
      ybAppealResultRepayment: {
      }
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
        width: 140
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
        width: 140
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
        width: 110
      },
      {
        title: '科室名称',
        dataIndex: 'arDeptName',
        width: 120
      },
      {
        title: '医生姓名',
        dataIndex: 'arDoctorName',
        width: 110
      },
      {
        title: '扣款类型',
        dataIndex: 'dataType',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '明细扣款'
            case 1:
              return '主单扣款'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 90
      },
      {
        title: '分摊方式',
        dataIndex: 'shareState',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return '个人分摊'
            case 1:
              return '科室分摊'
            case 2:
              return '其他分摊'
            default:
              return text
          }
        },
        fixed: 'right',
        width: 90
      },
      {
        title: '分摊方案',
        dataIndex: 'shareProgramme',
        fixed: 'right',
        width: 120
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

      this.loading = false
      this.ybAppealResultRepaymentView = {}
      this.ybAppealResultRepayment = {}
      this.form.resetFields()
    },
    onClose () {
      if (this.ybAppealResultRepaymentView.edit === 1) {
        this.reset()
      }
      this.isShow = false
      this.$emit('close')
    },
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.selectImplementDateStr = dateString
    },
    setFormValues ({ ...ybAppealResultRepaymentView }) {
      this.ybAppealResultRepaymentView = ybAppealResultRepaymentView
      this.form.getFieldDecorator('repaymentProgramme')
      if (ybAppealResultRepaymentView.edit === 1) {
        this.isShow = false
        this.form.setFieldsValue({
          repaymentProgramme: ybAppealResultRepaymentView.repaymentProgramme
        })
      } else {
        this.isShow = true
        if (ybAppealResultRepaymentView.deductPrice === ybAppealResultRepaymentView.repaymentPrice) {
          this.form.setFieldsValue({
            repaymentProgramme: ybAppealResultRepaymentView.shareProgramme
          })
        } else {
          this.form.setFieldsValue({
            repaymentProgramme: ''
          })
        }
      }
      setTimeout(() => {
        this.search()
      }, 200)
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
      params.deductImplementId = this.ybAppealResultRepaymentView.deductImplementId
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
      params.sortField = 'applyDateStr'
      params.sortOrder = 'descend'
      this.$get('ybAppealResultDeductimplementView/findAppealResultDeductimplementViewList', {
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
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.ybAppealResultRepayment.resetDataId = this.ybAppealResultRepaymentView.resetDataId
          this.ybAppealResultRepayment.resultId = this.ybAppealResultRepaymentView.resultId
          this.ybAppealResultRepayment.deductImplementId = this.ybAppealResultRepaymentView.deductImplementId
          this.ybAppealResultRepayment.applyDateStr = this.ybAppealResultRepaymentView.applyDateStr
          this.ybAppealResultRepayment.dataType = this.ybAppealResultRepaymentView.dataType
          this.ybAppealResultRepayment.areaType = this.ybAppealResultRepaymentView.areaType
          this.$post('ybAppealResultRepayment', {
            ...this.ybAppealResultRepayment
          }).then((r) => {
            if (r.data.data.success === 1) {
              this.reset()
              this.$emit('success')
            } else {
              this.$message.error(r.data.data.message)
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['repaymentProgramme'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybAppealResultRepayment[_key] = values[_key] })
      }
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
