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
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'ComConfiguremanageEdit',
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
      comConfiguremanage: {}
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
    setFormValues ({ ...comConfiguremanage }) {
      let fields = ['intField', 'numericField', 'stringField', 'currencyField', 'configureType']
      let fieldDates = []
      Object.keys(comConfiguremanage).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (comConfiguremanage[key] !== '') {
              obj[key] = moment(comConfiguremanage[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = comConfiguremanage[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.comConfiguremanage.id = comConfiguremanage.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let comConfiguremanage = this.form.getFieldsValue()
          comConfiguremanage.id = this.comConfiguremanage.id
          this.$put('comConfiguremanage', {
            ...comConfiguremanage
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
