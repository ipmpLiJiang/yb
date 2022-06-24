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
      <a-form-item v-bind="formItemLayout" label="大科室编码">
        <a-input
          placeholder="请输入大科室编码"
          v-decorator="[
            'dksId',
            { rules: [{ required: true, message: '大科室编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="大科室名称">
        <a-input
          placeholder="请输入大科室名称"
          v-decorator="[
            'dksName',
            { rules: [{ required: true, message: '大科室名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="拼音编码">
        <a-input
          placeholder="请输入拼音编码"
          v-decorator="[
            'spellCode',
            { rules: [{ required: true, message: '拼音编码不能为空' }] },
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
    span: 3
  },
  wrapperCol: {
    span: 18
  }
}
export default {
  name: 'YbDksEdit',
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
      ybDks: {}
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
      ...ybDks
    }) {
      let fields = ['dksId', 'dksName', 'spellCode']
      let fieldDates = []
      Object.keys(ybDks).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybDks[key] !== '') {
              obj[key] = moment(ybDks[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybDks[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybDks.id = ybDks.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybDks = this.form.getFieldsValue()
          ybDks.id = this.ybDks.id
          this.$put('ybDks', {
            ...ybDks
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
