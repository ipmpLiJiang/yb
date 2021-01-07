<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=750
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
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
          v-decorator="['applyDate', {rules: [{ required: true, message: '复议年月不能为空' }] }]"
          :default-value="moment('2020-08-19', monthFormat)"
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
  labelCol: { span: 5 },
  wrapperCol: { span: 17, offset: 1 }
}
export default {
  name: 'YbReconsiderApplyAdd',
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
      ybReconsiderApply: {},
      monthFormat: 'YYYY-MM',
      dayFormat: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.ybReconsiderApply = {}
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
          this.$post('ybReconsiderApply/addYbReconsiderApplyCheck', {
            ...this.ybReconsiderApply
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
    setFormValues () {
      this.form.getFieldDecorator('applyDate')
      this.form.setFieldsValue({
        applyDate: moment(new Date()).subtract(1, 'months')
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['applyDate', 'endDateOne', 'endDateTwo'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybReconsiderApply[_key] = values[_key] })
      }
    }
  }
}
</script>
