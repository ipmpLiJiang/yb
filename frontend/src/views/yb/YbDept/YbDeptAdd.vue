<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=45%
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="部门编码"
      >
        <a-input
          placeholder="请输入部门编码"
          v-decorator="['deptId', {rules: [{ required: true, message: '部门编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="部门名称"
      >
        <a-input
          placeholder="请输入部门名称"
          v-decorator="['deptName', {rules: [{ required: true, message: '部门名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="拼音编码"
      >
        <a-input
          placeholder="请输入拼音编码"
          v-decorator="['spellCode', {rules: [{ required: true, message: '拼音编码不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="科室">
        <a-select
          allowClear
          :showSearch="true"
          v-decorator="['dksName']"
        >
          <a-select-option :value="d.text" v-for="d in ksList" :key="d.text">
            {{d.text}}
          </a-select-option>
        </a-select>
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
  name: 'YbDeptAdd',
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
      ksList: [],
      ybDept: {}
    }
  },
  methods: {
    setFormValues (ksList) {
      this.ksList = ksList
    },
    reset () {
      this.loading = false
      this.ybDept = {}
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
          this.$post('ybDept', {
            ...this.ybDept
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
      let values = this.form.getFieldsValue(['deptId', 'deptName', 'dksName', 'spellCode'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybDept[_key] = values[_key] })
      }
    }
  }
}
</script>
