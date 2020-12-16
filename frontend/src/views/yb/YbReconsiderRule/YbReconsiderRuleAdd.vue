<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=50%
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
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
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'YbReconsiderRuleAdd',
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
      ybReconsiderRule: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybReconsiderRule = {}
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
          this.$post('ybReconsiderRule', {
            ...this.ybReconsiderRule
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
      let values = this.form.getFieldsValue(['rno', 'rdescribe', 'rxplain', 'rkeypoints', 'rmaterials', 'operatorid', 'operatorname'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybReconsiderRule[_key] = values[_key] })
      }
    }
  }
}
</script>
