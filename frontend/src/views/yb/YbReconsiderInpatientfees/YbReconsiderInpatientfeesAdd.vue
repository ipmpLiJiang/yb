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
        label="住院号"
      >
        <a-input
          placeholder="请输入住院号"
          v-decorator="['inpatientId', {rules: [{ required: true, message: '住院号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="患者姓名"
      >
        <a-input
          placeholder="请输入患者姓名"
          v-decorator="['patientName', {rules: [{ required: true, message: '患者姓名不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="HIS结算序号"
      >
        <a-input
          placeholder="请输入HIS结算序号"
          v-decorator="['settlementId', {rules: [{ required: true, message: 'HIS结算序号不能为空' }] }]"
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
        label="交易流水号"
      >
        <a-input
          placeholder="请输入交易流水号"
          v-decorator="['transNo', {rules: [{ required: true, message: '交易流水号不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目代码"
      >
        <a-input
          placeholder="请输入项目代码"
          v-decorator="['itemId', {rules: [{ required: true, message: '项目代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目医保编码"
      >
        <a-input
          placeholder="请输入项目医保编码"
          v-decorator="['itemCode', {rules: [{ required: true, message: '项目医保编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目名称"
      >
        <a-input
          placeholder="请输入项目名称"
          v-decorator="['itemName', {rules: [{ required: true, message: '项目名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目数量"
      >
        <a-input
          placeholder="请输入项目数量"
          v-decorator="['itemCount', {rules: [{ required: true, message: '项目数量不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目单价"
      >
        <a-input
          placeholder="请输入项目单价"
          v-decorator="['itemPrice', {rules: [{ required: true, message: '项目单价不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="项目金额"
      >
        <a-input
          placeholder="请输入项目金额"
          v-decorator="['itemAmount', {rules: [{ required: true, message: '项目金额不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="费用日期"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'feeDate', {rules: [{ required: true, message: '费用日期不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="住院科室代码"
      >
        <a-input
          placeholder="请输入住院科室代码"
          v-decorator="['deptId', {rules: [{ required: true, message: '住院科室代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="住院科室名称"
      >
        <a-input
          placeholder="请输入住院科室名称"
          v-decorator="['deptName', {rules: [{ required: true, message: '住院科室名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="开方医生代码"
      >
        <a-input
          placeholder="请输入开方医生代码"
          v-decorator="['orderDocId', {rules: [{ required: true, message: '开方医生代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="开方医生名称"
      >
        <a-input
          placeholder="请输入开方医生名称"
          v-decorator="['orderDocName', {rules: [{ required: true, message: '开方医生名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="执行科室代码"
      >
        <a-input
          placeholder="请输入执行科室代码"
          v-decorator="['excuteDeptId', {rules: [{ required: true, message: '执行科室代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="执行科室名称"
      >
        <a-input
          placeholder="请输入执行科室名称"
          v-decorator="['excuteDeptName', {rules: [{ required: true, message: '执行科室名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="执行医生代码"
      >
        <a-input
          placeholder="请输入执行医生代码"
          v-decorator="['excuteDocId', {rules: [{ required: true, message: '执行医生代码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="执行医生名称"
      >
        <a-input
          placeholder="请输入执行医生名称"
          v-decorator="['excuteDocName', {rules: [{ required: true, message: '执行医生名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="结算时间"
      >
        <a-date-picker
          showTime
          format='YYYY-MM-DD HH:mm:ss'
          v-decorator="[ 'settlementDate', {rules: [{ required: true, message: '结算时间不能为空' }] }]"
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
  name: 'YbReconsiderInpatientfeesAdd',
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
      ybReconsiderInpatientfees: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybReconsiderInpatientfees = {}
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
          this.$post('ybReconsiderInpatientfees', {
            ...this.ybReconsiderInpatientfees
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
      let values = this.form.getFieldsValue(['inpatientId', 'patientName', 'settlementId', 'billNo', 'transNo', 'itemId', 'itemCode', 'itemName', 'itemCount', 'itemPrice', 'itemAmount', 'feeDate', 'deptId', 'deptName', 'orderDocId', 'orderDocName', 'excuteDeptId', 'excuteDeptName', 'excuteDocId', 'excuteDocName', 'settlementDate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybReconsiderInpatientfees[_key] = values[_key] })
      }
    }
  }
}
</script>
