<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width="45%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px); overflow: auto; padding-bottom: 53px"
  >
    <a-form :form="form">
      <a-form-item v-bind="formItemLayout" label="部门编码">
        <a-input
          placeholder="请输入部门编码"
          readonly
          v-decorator="[
            'deptId',
            { rules: [{ required: true, message: '部门编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="部门名称">
        <a-input
          placeholder="请输入部门名称"
          v-decorator="[
            'deptName',
            { rules: [{ required: true, message: '部门名称不能为空' }] },
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
      <a-form-item v-bind="formItemLayout" label="大专业">
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
  name: 'YbDeptEdit',
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
      ksList: [],
      ybDept: {}
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
      ...ybDept
    }) {
      let fields = ['deptId', 'deptName', 'dksName', 'spellCode']
      let fieldDates = []
      Object.keys(ybDept).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybDept[key] !== '') {
              obj[key] = moment(ybDept[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybDept[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybDept.id = ybDept.id
      this.ksList = ybDept.ksList
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybDept = this.form.getFieldsValue()
          debugger
          ybDept.id = this.ybDept.id
          this.$put('ybDept', {
            ...ybDept
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
