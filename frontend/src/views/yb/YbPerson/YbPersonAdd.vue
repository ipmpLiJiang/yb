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
        :validateStatus="validateStatus"
        :help="help"
        label="人员编码"
      >
        <a-input
          placeholder="请输入人员编码"
          @blur="handlePersonCodeBlur"
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
        label="科室名称"
      >
        <a-input
          placeholder="请输入科室名称" readOnly
          v-decorator="['deptName', {rules: [{ required: true, message: '科室名称不能为空'}], initialValue: '医生' }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="性别"
      >
        <a-select
          style="width: 120px"
          placeholder="请选择性别"
          v-decorator="['sex', {rules: [{ required: true, message: '性别不能为空' }], initialValue: '2' }]"
        >
          <a-select-option value="0">
            男
          </a-select-option>
          <a-select-option value="1">
            女
          </a-select-option>
          <a-select-option value="2">
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
          v-decorator="['email', {rules: [
          { type: 'email', message: '请输入正确的邮箱' } ,
          { max: 50, message: '长度不能超过50个字符'} ] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="联系电话"
      >
        <a-input
          placeholder="请输入联系电话"
          v-decorator="['tel', {rules: [
          { required: true, message: '联系电话不能为空' } ,
          { pattern: '^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$', message: '请输入正确的手机号'}
          ] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="拼音码"
      >
        <a-input
          placeholder="请输入拼音码"
          v-decorator="['spellCode', {rules: [{ max: 10, message: '长度不能超过10个字符'}] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="五笔码"
      >
        <a-input
          placeholder="请输入五笔码"
          v-decorator="['strokeCode', {rules: [{ max: 10, message: '长度不能超过10个字符'}] }]"
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
  name: 'YbPersonAdd',
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
      ybPerson: {},
      validateStatus: '',
      help: ''
    }
  },
  methods: {
    reset () {
      this.validateStatus = ''
      this.help = ''
      // this.ybPerson.username = ''
      this.loading = false
      this.ybPerson = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      if (this.validateStatus !== 'success') {
        this.handlePersonCodeBlur()
      }
      this.form.validateFields((err, values) => {
        if (!err && this.validateStatus === 'success') {
          this.setFields()
          this.$post('ybPerson', {
            ...this.ybPerson
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    handlePersonCodeBlur () {
      let personCode = this.form.getFieldValue('personCode')
      personCode = typeof personCode === 'undefined' ? '' : personCode.trim()
      if (personCode.length) {
        if (personCode.length > 10) {
          this.validateStatus = 'error'
          this.help = '用户名不能超过10个字符'
        } else if (personCode.length < 4) {
          this.validateStatus = 'error'
          this.help = '用户名不能少于4个字符'
        } else {
          this.validateStatus = 'validating'
          this.$get(`ybPerson/check/${personCode}`).then((r) => {
            if (r.data) {
              this.validateStatus = 'success'
              this.help = ''
            } else {
              this.validateStatus = 'error'
              this.help = '抱歉，该用户名已存在'
            }
          })
        }
      } else {
        this.validateStatus = 'error'
        this.help = '用户名不能为空'
      }
    },
    setFields () {
      let values = this.form.getFieldsValue(['personCode', 'personName', 'deptCode', 'deptName', 'sex', 'tel', 'email', 'spellCode', 'strokeCode'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybPerson[_key] = values[_key] })
      }
    }
  }
}
</script>
