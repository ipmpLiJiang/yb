<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width="650"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 55px); overflow: auto; padding-bottom: 53px"
  >
    <a-form :form="form">
      <a-row>
      <a-form-item
        v-bind="formItemLayout"
        label="复议年月"
      >
        <a-month-picker
          placeholder="请输入复议年月"
          v-decorator="['applyDate', {rules: [{ required: true, message: '复议年月不能为空' }] }]"
          :default-value="moment('2020-08-19', monthFormat)"
          :format="monthFormat"
        />
      </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="截止日期"
        >
        <a-date-picker
          placeholder="请输入截止日期"
          style="width:250px"
          v-decorator="['endDate', {rules: [{ required: true, message: '截止日期不能为空' }] }]"
          show-time
          :format="dayFormat"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
          v-bind="formItemLayout"
          label="确认日期"
        >
        <a-date-picker
          placeholder="请输入确认日期"
          style="width:250px"
          v-decorator="['enableDate', {rules: [{ required: true, message: '确认日期不能为空' }] }]"
          :format="enableFormat"/>
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
    span: 17
  }
}
export default {
  name: 'YbDrgApplyAdd',
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
      ybDrgApply: {},
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
      this.ybDrgApply = {}
      this.form.resetFields()
    },
    setFormValues () {
      this.form.getFieldDecorator('applyDate')
      this.form.setFieldsValue({
        applyDate: moment(new Date()).subtract(1, 'months')
      })
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.ybDrgApply.areaType = this.user.areaType.value
          this.$post('ybDrgApply/addYbDrgApplyCheck', {
            ...this.ybDrgApply
          }).then((r) => {
            if (r.data.data.success === 1) {
              this.reset()
              this.$emit('success')
            } else {
              this.$message.error(r.data.data.message)
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['applyDate', 'endDate', 'enableDate'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybDrgApply[_key] = values[_key]
        })
      }
    }
  }
}
</script>
