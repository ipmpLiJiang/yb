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
      <a-form-item v-bind="formItemLayout" label="bq编码">
        <a-input
          placeholder="请输入bq编码"
          v-decorator="[
            'bqCode',
            { rules: [{ required: true, message: 'bq编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="bq名称">
        <a-input
          placeholder="请输入bq名称"
          v-decorator="[
            'bqName',
            { rules: [{ required: true, message: 'bq名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="ks编码">
        <a-input
          placeholder="请输入ks编码"
          v-decorator="[
            'ksCode',
            { rules: [{ required: true, message: 'ks编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="ks名称">
        <a-input
          placeholder="请输入ks名称"
          v-decorator="[
            'ksName',
            { rules: [{ required: true, message: 'ks名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="dzy编码">
        <a-input
          placeholder="请输入dzy编码"
          v-decorator="[
            'dzyCode',
            { rules: [{ required: true, message: 'dzy编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="dzy名称">
        <a-input
          placeholder="请输入dzy名称"
          v-decorator="[
            'dzyName',
            { rules: [{ required: true, message: 'dzy名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="院区">
        <a-input
          placeholder="请输入院区"
          v-decorator="[
            'yq',
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
  name: 'YbDrgRelateEdit',
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
      ybDrgRelate: {}
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
    setFormValues ({ ...ybDrgRelate }) {
      let fields = ['bqCode', 'bqName', 'ksCode', 'ksName', 'dzyCode', 'dzyName', 'yq', 'currencyField']
      let fieldDates = []
      Object.keys(ybDrgRelate).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybDrgRelate[key] !== '') {
              obj[key] = moment(ybDrgRelate[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybDrgRelate[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybDrgRelate.id = ybDrgRelate.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybDrgRelate = this.form.getFieldsValue()
          ybDrgRelate.id = this.ybDrgRelate.id
          this.$put('ybDrgRelate', {
            ...ybDrgRelate
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
