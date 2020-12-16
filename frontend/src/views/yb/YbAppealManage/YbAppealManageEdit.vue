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
        label="复议申请明细"
      >
        <a-input
          placeholder="请输入复议申请明细"
          v-decorator="['applyDataId', {rules: [{ required: true, message: '复议申请明细不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="核对Id"
      >
        <a-input
          placeholder="请输入核对Id"
          v-decorator="['verifyId', {rules: [{ required: true, message: '核对Id不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="复议医生编码"
      >
        <a-input
          placeholder="请输入复议医生编码"
          v-decorator="['readyDoctorCode', {rules: [{ required: true, message: '复议医生编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="复议医生"
      >
        <a-input
          placeholder="请输入复议医生"
          v-decorator="['readyDoctorName', {rules: [{ required: true, message: '复议医生不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="复议科室编码"
      >
        <a-input
          placeholder="请输入复议科室编码"
          v-decorator="['readyDeptCode', {rules: [{ required: true, message: '复议科室编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="复议科室"
      >
        <a-input
          placeholder="请输入复议科室"
          v-decorator="['readyDeptName', {rules: [{ required: true, message: '复议科室不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更医生编码"
      >
        <a-input
          placeholder="请输入变更医生编码"
          v-decorator="['changDoctorCode', {rules: [{ required: true, message: '变更医生编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更医生"
      >
        <a-input
          placeholder="请输入变更医生"
          v-decorator="['changDoctorName', {rules: [{ required: true, message: '变更医生不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更复议科室编码"
      >
        <a-input
          placeholder="请输入变更复议科室编码"
          v-decorator="['changDeptCode', {rules: [{ required: true, message: '变更复议科室编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更复议科室"
      >
        <a-input
          placeholder="请输入变更复议科室"
          v-decorator="['changDeptName', {rules: [{ required: true, message: '变更复议科室不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="接受状态"
      >
        <a-input
          placeholder="请输入接受状态"
          v-decorator="['acceptState', {rules: [{ required: true, message: '接受状态不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="操作理由"
      >
        <a-input
          placeholder="请输入操作理由"
          v-decorator="['operateReason', {rules: [{ required: true, message: '操作理由不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="操作日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'operateDate', {rules: [{ required: true, message: '操作日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医保人代码"
      >
        <a-input
          placeholder="请输入医保人代码"
          v-decorator="['refuseId', {rules: [{ required: true, message: '医保人代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医保人"
      >
        <a-input
          placeholder="请输入医保人"
          v-decorator="['refuseName', {rules: [{ required: true, message: '医保人不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="拒绝理由"
      >
        <a-input
          placeholder="请输入拒绝理由"
          v-decorator="['refuseReason', {rules: [{ required: true, message: '拒绝理由不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="拒绝日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'refuseDate', {rules: [{ required: true, message: '拒绝日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="管理员代码"
      >
        <a-input
          placeholder="请输入管理员代码"
          v-decorator="['adminPersonId', {rules: [{ required: true, message: '管理员代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="管理员"
      >
        <a-input
          placeholder="请输入管理员"
          v-decorator="['adminPersonName', {rules: [{ required: true, message: '管理员不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="更改日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'adminChangeDate', {rules: [{ required: true, message: '更改日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="管理员理由"
      >
        <a-input
          placeholder="请输入管理员理由"
          v-decorator="['adminReason', {rules: [{ required: true, message: '管理员理由不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="通用"
      >
        <a-input
          placeholder="请输入通用"
          v-decorator="['currencyField', {rules: [{ required: true, message: '通用不能为空' }] }]"
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
  name: 'YbAppealManageEdit',
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
      ybAppealManage: {}
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
    setFormValues ({ ...ybAppealManage }) {
      let fields = ['applyDataId', 'verifyId', 'readyDoctorCode', 'readyDoctorName', 'readyDeptCode', 'readyDeptName', 'changDoctorCode', 'changDoctorName', 'changDeptCode', 'changDeptName', 'acceptState', 'operateReason', 'operateDate', 'refuseId', 'refuseName', 'refuseReason', 'refuseDate', 'adminPersonId', 'adminPersonName', 'adminChangeDate', 'adminReason', 'currencyField']
      let fieldDates = ['operateDate', 'refuseDate', 'adminChangeDate']
      Object.keys(ybAppealManage).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybAppealManage[key] !== '') {
              obj[key] = moment(ybAppealManage[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybAppealManage[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybAppealManage.id = ybAppealManage.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybAppealManage = this.form.getFieldsValue()
          ybAppealManage.id = this.ybAppealManage.id
          this.$put('ybAppealManage', {
            ...ybAppealManage
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
