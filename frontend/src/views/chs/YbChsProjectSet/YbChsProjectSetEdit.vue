<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width="650"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px); overflow: auto; padding-bottom: 53px"
  >
    <a-form :form="form">
      <a-form-item v-bind="formItemLayout" label="规则名称">
        <a-input
          placeholder="请输入规则名称"
          v-decorator="[
            'ruleName',
            { rules: [{ required: true, message: '规则名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="项目名称">
        <a-input
          placeholder="请输入项目名称"
          v-decorator="[
            'projectName',
            { rules: [{ required: true, message: '项目名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="匹配项目名称">
        <a-input
          placeholder="请输入匹配项目名称"
          v-decorator="[
            'qdName',
            { rules: [{ required: true, message: '匹配项目名称不能为空' }] },
          ]"
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
        <a-button style="margin-right: 0.8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading"
        >提交</a-button
      >
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'

const formItemLayout = {
  labelCol: {
    span: 4
  },
  wrapperCol: {
    span: 18
  }
}
export default {
  name: 'YbChsProjectSetEdit',
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
      ybChsProjectSet: {}
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
    setFormValues ({
      ...ybChsProjectSet
    }) {
      let fields = ['ruleName', 'projectName', 'qdName']
      let fieldDates = []
      Object.keys(ybChsProjectSet).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybChsProjectSet[key] !== '') {
              obj[key] = moment(ybChsProjectSet[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybChsProjectSet[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybChsProjectSet.id = ybChsProjectSet.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybChsProjectSet = this.form.getFieldsValue()
          ybChsProjectSet.id = this.ybChsProjectSet.id
          this.$put('ybChsProjectSet', {
            ...ybChsProjectSet
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
