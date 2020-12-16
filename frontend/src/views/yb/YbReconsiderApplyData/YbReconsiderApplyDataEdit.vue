<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="申请编号"
      >
        <a-input
          placeholder="请输入申请编号"
          v-decorator="['pid', {rules: [{ required: true, message: '申请编号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="交易流水号"
      >
        <a-input
          placeholder="请输入交易流水号"
          v-decorator="['serialNo', {rules: [{ required: true, message: '交易流水号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="单据号"
      >
        <a-input
          placeholder="请输入单据号"
          v-decorator="['billNo', {rules: [{ required: true, message: '单据号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="意见书编码"
      >
        <a-input
          placeholder="请输入意见书编码"
          v-decorator="['proposalCode', {rules: [{ required: true, message: '意见书编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目编码"
      >
        <a-input
          placeholder="请输入项目编码"
          v-decorator="['projectCode', {rules: [{ required: true, message: '项目编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目名称"
      >
        <a-input
          placeholder="请输入项目名称"
          v-decorator="['projectName', {rules: [{ required: true, message: '项目名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="数量"
      >
        <a-input
          placeholder="请输入数量"
          v-decorator="['num', {rules: [{ required: true, message: '数量不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医保内金额"
      >
        <a-input
          placeholder="请输入医保内金额"
          v-decorator="['medicalPrice', {rules: [{ required: true, message: '医保内金额不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="规则名称"
      >
        <a-input
          placeholder="请输入规则名称"
          v-decorator="['ruleName', {rules: [{ required: true, message: '规则名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="扣除金额"
      >
        <a-input
          placeholder="请输入扣除金额"
          v-decorator="['deductPrice', {rules: [{ required: true, message: '扣除金额不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="扣除原因"
      >
        <a-input
          placeholder="请输入扣除原因"
          v-decorator="['deductReason', {rules: [{ required: true, message: '扣除原因不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="还款原因"
      >
        <a-input
          placeholder="请输入还款原因"
          v-decorator="['repaymentReason', {rules: [{ required: true, message: '还款原因不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医生姓名"
      >
        <a-input
          placeholder="请输入医生姓名"
          v-decorator="['doctorName', {rules: [{ required: true, message: '医生姓名不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="科室编码"
      >
        <a-input
          placeholder="请输入科室编码"
          v-decorator="['deptCode', {rules: [{ required: true, message: '科室编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="科室名称"
      >
        <a-input
          placeholder="请输入科室名称"
          v-decorator="['deptName', {rules: [{ required: true, message: '科室名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="入院日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'enterHospitalDate', {rules: [{ required: true, message: '入院日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="出院日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'outHospitalDate', {rules: [{ required: true, message: '出院日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="费用日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'costDate', {rules: [{ required: true, message: '费用日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="住院号"
      >
        <a-input
          placeholder="请输入住院号"
          v-decorator="['hospitalizedNo', {rules: [{ required: true, message: '住院号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="就医方式"
      >
        <a-input
          placeholder="请输入就医方式"
          v-decorator="['treatmentMode', {rules: [{ required: true, message: '就医方式不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="结算日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'settlementDate', {rules: [{ required: true, message: '结算日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="个人编号"
      >
        <a-input
          placeholder="请输入个人编号"
          v-decorator="['personalNo', {rules: [{ required: true, message: '个人编号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="参保人姓名"
      >
        <a-input
          placeholder="请输入参保人姓名"
          v-decorator="['insuredName', {rules: [{ required: true, message: '参保人姓名不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医保卡号"
      >
        <a-input
          placeholder="请输入医保卡号"
          v-decorator="['cardNumber', {rules: [{ required: true, message: '医保卡号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="统筹区名称"
      >
        <a-input
          placeholder="请输入统筹区名称"
          v-decorator="['areaName', {rules: [{ required: true, message: '统筹区名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="版本号"
      >
        <a-input
          placeholder="请输入版本号"
          v-decorator="['versionNumber', {rules: [{ required: true, message: '版本号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="反馈申诉"
      >
        <a-input
          placeholder="请输入反馈申诉"
          v-decorator="['backAppeal', {rules: [{ required: true, message: '反馈申诉不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="版本类型"
      >
        <a-input
          placeholder="请输入版本类型"
          v-decorator="['typeno', {rules: [{ required: true, message: '版本类型不能为空' }] }]"
        />
      </a-form-item>
    </a-form>
    <div class="drawer-bootom-button">
      <a-popconfirm
        title="确定放弃编辑？"
        @confirm="onClose"
        okText="确定"
        cancelText="取消"
      >
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button
        @click="handleSubmit"
        type="primary"
        :loading="loading"
      >提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'YbReconsiderApplyDataEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      ybReconsiderApplyData: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues ({ ...ybReconsiderApplyData }) {
      let fields = ['pid', 'serialNo', 'billNo', 'proposalCode', 'projectCode', 'projectName', 'num', 'medicalPrice', 'ruleName', 'deductPrice', 'deductReason', 'repaymentReason', 'doctorName', 'deptCode', 'deptName', 'enterHospitalDate', 'outHospitalDate', 'costDate', 'hospitalizedNo', 'treatmentMode', 'settlementDate', 'personalNo', 'insuredName', 'cardNumber', 'areaName', 'versionNumber', 'backAppeal', 'typeno']
      let fieldDates = ['enterHospitalDate', 'outHospitalDate', 'costDate', 'settlementDate']
      Object.keys(ybReconsiderApplyData).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybReconsiderApplyData[key] !== '') {
              obj[key] = moment(ybReconsiderApplyData[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybReconsiderApplyData[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybReconsiderApplyData.id = ybReconsiderApplyData.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybReconsiderApplyData = this.form.getFieldsValue()
          ybReconsiderApplyData.id = this.ybReconsiderApplyData.id
          this.$put('ybReconsiderApplyData', {
            ...ybReconsiderApplyData
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>
