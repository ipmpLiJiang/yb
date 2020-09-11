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
        label="数字"
      >
        <a-input
          placeholder="请输入数字"
          v-decorator="['intField', {rules: [{ required: true, message: '数字不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="小数"
      >
        <a-input
          placeholder="请输入小数"
          v-decorator="['numericField', {rules: [{ required: true, message: '小数不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="字符"
      >
        <a-input
          placeholder="请输入字符"
          v-decorator="['stringField', {rules: [{ required: true, message: '字符不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="通用字段"
      >
        <a-input
          placeholder="请输入通用字段"
          v-decorator="['currencyField', {rules: [{ required: true, message: '通用字段不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="类型"
      >
        <a-input
          placeholder="请输入类型"
          v-decorator="['configureType', {rules: [{ required: true, message: '类型不能为空' }] }]"
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
  name: 'ComConfiguremanageAdd',
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
      comConfiguremanage: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.comConfiguremanage = {}
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
          this.$post('comConfiguremanage', {
            ...this.comConfiguremanage
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
      let values = this.form.getFieldsValue(['intField', 'numericField', 'stringField', 'currencyField', 'configureType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.comConfiguremanage[_key] = values[_key] })
      }
    }
  }
}
</script>
