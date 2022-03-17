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
      <a-form-item v-bind="formItemLayout" label="编码">
        <a-input
          placeholder="请输入编码"
          v-decorator="[
            'dksId',
            { rules: [{ required: true, message: '编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="名称">
        <a-input
          placeholder="请输入名称"
          v-decorator="[
            'dksName',
            { rules: [{ required: true, message: '名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="院区Id">
        <a-input
          placeholder="请输入院区Id"
          v-decorator="[
            'areaId',
            { rules: [{ required: true, message: '院区Id不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="院区">
        <a-input
          placeholder="请输入院区"
          v-decorator="[
            'areaName',
            { rules: [{ required: true, message: '院区不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="通用字段">
        <a-input
          placeholder="请输入通用字段"
          v-decorator="[
            'currencyField',
            { rules: [{ required: true, message: '通用字段不能为空' }] },
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
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'YbDrgDksEdit',
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
      ybDrgDks: {}
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
    setFormValues ({ ...ybDrgDks }) {
      let fields = ['dksId', 'dksName', 'areaId', 'areaName', 'currencyField']
      let fieldDates = []
      Object.keys(ybDrgDks).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybDrgDks[key] !== '') {
              obj[key] = moment(ybDrgDks[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybDrgDks[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybDrgDks.id = ybDrgDks.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybDrgDks = this.form.getFieldsValue()
          ybDrgDks.id = this.ybDrgDks.id
          this.$put('ybDrgDks', {
            ...ybDrgDks
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
