<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="人员编码"
      >
        <a-input
          placeholder="请输入人员编码"
          v-decorator="['personCode', {rules: [{ required: true, message: '人员编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="人员名称"
      >
        <a-input
          placeholder="请输入人员名称"
          v-decorator="['personName', {rules: [{ required: true, message: '人员名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="科室编码"
      >
        <a-input
          placeholder="请输入科室编码"
          v-decorator="['deptCode', {rules: [{ required: true, message: '科室编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="科室名称"
      >
        <a-input
          placeholder="请输入科室名称"
          v-decorator="['deptName', {rules: [{ required: true, message: '科室名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="性别"
      >
        <a-select
          default-value="1" style="width: 120px"
          placeholder="请选择性别"
          v-decorator="['sex', {rules: [{ required: true, message: '性别不能为空' }] }]"
        >
          <a-select-option value="1">
            男
          </a-select-option>
          <a-select-option value="2">
            女
          </a-select-option>
          <a-select-option value="3">
            未知
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="邮箱"
      >
        <a-input
          placeholder="请输入邮箱"
          v-decorator="['email', {rules: [{ required: true, message: '邮箱不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="联系电话"
      >
        <a-input
          placeholder="请输入联系电话"
          v-decorator="['tel', {rules: [{ required: true, message: '联系电话不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="拼音码"
      >
        <a-input
          placeholder="请输入拼音码"
          v-decorator="['spellCode', {rules: [{ required: true, message: '拼音码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="五笔码"
      >
        <a-input
          placeholder="请输入五笔码"
          v-decorator="['strokeCode', {rules: [{ required: true, message: '五笔码不能为空' }] }]"
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
  name: 'YbPersonEdit',
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
      ybPerson: {}
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
    setFormValues ({ ...ybPerson }) {
      let fields = ['personCode', 'personName', 'deptCode', 'deptName', 'sex', 'tel', 'email', 'spellCode', 'strokeCode']
      let fieldDates = []
      Object.keys(ybPerson).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybPerson[key] !== '') {
              obj[key] = moment(ybPerson[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybPerson[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybPerson.id = ybPerson.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybPerson = this.form.getFieldsValue()
          ybPerson.id = this.ybPerson.id
          this.$put('ybPerson', {
            ...ybPerson
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
