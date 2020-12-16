<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=45%
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="规则描述"
      >
        <a-input
          placeholder="请输入规则描述"
          v-decorator="['rdescribe', {rules: [{ required: true, message: '规则描述不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="规则解释"
      >
        <a-textarea
          placeholder="请输入规则解释"
          v-decorator="['rxplain', {rules: [{ required: true, message: '规则解释不能为空' }] }]"
          :rows="6"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="复议重点"
      >
        <a-input
          placeholder="请输入复议重点"
          v-decorator="['rkeypoints', {rules: [{ required: true, message: '复议重点不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="复议资料"
      >
        <a-textarea
          placeholder="请输入复议资料"
          v-decorator="['rmaterials', {rules: [{ required: true, message: '复议资料不能为空' }] }]"
          :rows="6"
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
  name: 'YbReconsiderRuleEdit',
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
      ybReconsiderRule: {}
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
    setFormValues ({ ...ybReconsiderRule }) {
      let fields = ['rno', 'rdescribe', 'rxplain', 'rkeypoints', 'rmaterials', 'operatorid', 'operatorname']
      let fieldDates = []
      Object.keys(ybReconsiderRule).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybReconsiderRule[key] !== '') {
              obj[key] = moment(ybReconsiderRule[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybReconsiderRule[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybReconsiderRule.id = ybReconsiderRule.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybReconsiderRule = this.form.getFieldsValue()
          ybReconsiderRule.id = this.ybReconsiderRule.id
          this.$put('ybReconsiderRule', {
            ...ybReconsiderRule
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
