﻿<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=750
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-row>
      <a-form-item
        v-bind="formItemLayout"
        label="复议年月"
      >
        <a-month-picker
          placeholder="请输入复议年月"
          disabled="disabled"
          v-decorator="['applyDate', {rules: [{ required: true, message: '复议年月不能为空' }] }]"
          :default-value="moment('2020-08-20', monthFormat)"
          :format="monthFormat"
        />
      </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="第一版结束日期"
        >
        <a-date-picker
          placeholder="请输入第一版结束日期"
          style="width:250px"
          v-decorator="['endDateOne', {rules: [{ required: true, message: '第一版结束日期不能为空' }] }]"
          show-time
          :format="dayFormat"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="第一版确认日期"
        >
        <a-date-picker
          placeholder="请输入第一版确认日期"
          style="width:250px"
          v-decorator="['enableDateOne', {rules: [{ required: true, message: '第一版确认日期不能为空' }] }]"
          :format="enableFormat"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="第二版结束日期"
        >
        <a-date-picker
          placeholder="请输入第二版结束日期"
          style="width:250px"
          v-decorator="['endDateTwo', {rules: [{ required: true, message: '第二版结束日期不能为空' }] }]"
          show-time
          :format="dayFormat"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="第二版确认日期"
        >
        <a-date-picker
          placeholder="请输入第二版确认日期"
          style="width:250px"
          v-decorator="['enableDateTwo', {rules: [{ required: true, message: '第二版确认日期不能为空' }] }]"
          :format="enableFormat"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="未申诉更新"
        >
        <a-checkbox :checked="checked" @change="onChange">
          是否更新
        </a-checkbox>
        </a-form-item>
      </a-row>
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
  labelCol: { span: 6 },
  wrapperCol: { span: 16, offset: 1 }
}
export default {
  name: 'YbReconsiderApplyEdit',
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
      ybReconsiderApply: {},
      checked: false,
      user: this.$store.state.account.user,
      monthFormat: 'YYYY-MM',
      enableFormat: 'YYYY-MM-DD',
      dayFormat: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    onChange () {
      this.checked = !this.checked
    },
    setFormValues ({ ...ybReconsiderApply }) {
      this.checked = false
      let fields = ['applyDate', 'endDateOne', 'endDateTwo', 'enableDateOne', 'enableDateTwo']
      let fieldDates = ['applyDate', 'endDateOne', 'endDateTwo', 'enableDateOne', 'enableDateTwo']
      Object.keys(ybReconsiderApply).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybReconsiderApply[key] !== '' && ybReconsiderApply[key] !== null) {
              obj[key] = moment(ybReconsiderApply[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybReconsiderApply[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybReconsiderApply.id = ybReconsiderApply.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          let ybReconsiderApply = this.form.getFieldsValue()
          ybReconsiderApply.id = this.ybReconsiderApply.id
          ybReconsiderApply.isUpOverdue = this.checked
          ybReconsiderApply.areaType = this.user.areaType.value
          this.$put('ybReconsiderApply', {
            ...ybReconsiderApply
          }).then((r) => {
            if (r.data.data.success === 1) {
              if (!this.checked) {
                this.reset()
                this.$emit('success')
                if (r.data.data.message === 'ok') {
                  this.$message.success('修改成功')
                } else {
                  this.$message.warning(r.data.data.message)
                }
              } else {
                if (r.data.data.message === 'ok') {
                  this.$message.success('修改成功')
                } else {
                  this.$message.warning(r.data.data.message)
                }
              }
            } else {
              this.$message.warning(r.data.data.message)
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['endDateOne', 'endDateTwo', 'enableDateOne', 'enableDateTwo'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybReconsiderApply[_key] = values[_key] })
      }
    }
  }
}
</script>
