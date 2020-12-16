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
        label="管理Id"
      >
        <a-input
          placeholder="请输入管理Id"
          v-decorator="['manageId', {rules: [{ required: true, message: '管理Id不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医生编码"
      >
        <a-input
          placeholder="请输入医生编码"
          v-decorator="['doctorCode', {rules: [{ required: true, message: '医生编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医生"
      >
        <a-input
          placeholder="请输入医生"
          v-decorator="['doctorName', {rules: [{ required: true, message: '医生不能为空' }] }]"
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
        label="科室"
      >
        <a-input
          placeholder="请输入科室"
          v-decorator="['deptName', {rules: [{ required: true, message: '科室不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="理由"
      >
        <a-input
          placeholder="请输入理由"
          v-decorator="['operateReason', {rules: [{ required: true, message: '理由不能为空' }] }]"
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
  name: 'YbAppealResultEdit',
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
      ybAppealResult: {}
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
    setFormValues ({ ...ybAppealResult }) {
      let fields = ['applyDataId', 'verifyId', 'manageId', 'doctorCode', 'doctorName', 'deptCode', 'deptName', 'operateReason', 'operateDate', 'currencyField']
      let fieldDates = ['operateDate']
      Object.keys(ybAppealResult).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybAppealResult[key] !== '') {
              obj[key] = moment(ybAppealResult[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybAppealResult[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybAppealResult.id = ybAppealResult.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybAppealResult = this.form.getFieldsValue()
          ybAppealResult.id = this.ybAppealResult.id
          this.$put('ybAppealResult', {
            ...ybAppealResult
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
