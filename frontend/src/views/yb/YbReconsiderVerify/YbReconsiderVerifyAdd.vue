<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
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
        label="参考复议医生编码"
      >
        <a-input
          placeholder="请输入参考复议医生编码"
          v-decorator="['verifyDoctorCode', {rules: [{ required: true, message: '参考复议医生编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="参考复议医生"
      >
        <a-input
          placeholder="请输入参考复议医生"
          v-decorator="['verifyDoctorName', {rules: [{ required: true, message: '参考复议医生不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="参考复议科室编码"
      >
        <a-input
          placeholder="请输入参考复议科室编码"
          v-decorator="['verifyDeptCode', {rules: [{ required: true, message: '参考复议科室编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="参考复议科室"
      >
        <a-input
          placeholder="请输入参考复议科室"
          v-decorator="['verifyDeptName', {rules: [{ required: true, message: '参考复议科室不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更复议医生编码"
      >
        <a-input
          placeholder="请输入变更复议医生编码"
          v-decorator="['changVerifyDoctorCode', {rules: [{ required: true, message: '变更复议医生编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更复议医生"
      >
        <a-input
          placeholder="请输入变更复议医生"
          v-decorator="['changVerifyDoctorName', {rules: [{ required: true, message: '变更复议医生不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更复议科室编码"
      >
        <a-input
          placeholder="请输入变更复议科室编码"
          v-decorator="['changVerifyDeptCode', {rules: [{ required: true, message: '变更复议科室编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="变更复议科室"
      >
        <a-input
          placeholder="请输入变更复议科室"
          v-decorator="['changVerifyDeptName', {rules: [{ required: true, message: '变更复议科室不能为空' }] }]"
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
        label="匹配人代码"
      >
        <a-input
          placeholder="请输入匹配人代码"
          v-decorator="['matchPersonId', {rules: [{ required: true, message: '匹配人代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="匹配人"
      >
        <a-input
          placeholder="请输入匹配人"
          v-decorator="['matchPersonName', {rules: [{ required: true, message: '匹配人不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="匹配日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'matchDate', {rules: [{ required: true, message: '匹配日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="审核人代码"
      >
        <a-input
          placeholder="请输入审核人代码"
          v-decorator="['reviewerId', {rules: [{ required: true, message: '审核人代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="审核人"
      >
        <a-input
          placeholder="请输入审核人"
          v-decorator="['reviewerName', {rules: [{ required: true, message: '审核人不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="审核日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'reviewerDate', {rules: [{ required: true, message: '审核日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发送人代码"
      >
        <a-input
          placeholder="请输入发送人代码"
          v-decorator="['sendPersonId', {rules: [{ required: true, message: '发送人代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发送人"
      >
        <a-input
          placeholder="请输入发送人"
          v-decorator="['sendPersonName', {rules: [{ required: true, message: '发送人不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="发送日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'sendDate', {rules: [{ required: true, message: '发送日期不能为空' }] }]"
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
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'YbReconsiderVerifyAdd',
  props: {
    addVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      ybReconsiderVerify: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybReconsiderVerify = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.$post('ybReconsiderVerify', {
            ...this.ybReconsiderVerify
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['applyDataId', 'verifyDoctorCode', 'verifyDoctorName', 'verifyDeptCode', 'verifyDeptName', 'changVerifyDoctorCode', 'changVerifyDoctorName', 'changVerifyDeptCode', 'changVerifyDeptName', 'acceptState', 'refuseReason', 'refuseDate', 'matchPersonId', 'matchPersonName', 'matchDate', 'reviewerId', 'reviewerName', 'reviewerDate', 'sendPersonId', 'sendPersonName', 'sendDate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybReconsiderVerify[_key] = values[_key] })
      }
    }
  }
}
</script>
