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
    span: 3
  },
  wrapperCol: {
    span: 18
  }
}
export default {
  name: 'YbDrgApplyEdit',
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
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues ({
      ...ybDrgApply
    }) {
      let fields = ['applyDate', 'endDate', 'enableDate']
      let fieldDates = ['applyDate', 'endDate', 'enableDate']
      Object.keys(ybDrgApply).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybDrgApply[key] !== '') {
              obj[key] = moment(ybDrgApply[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybDrgApply[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybDrgApply.id = ybDrgApply.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybDrgApply = this.form.getFieldsValue()
          ybDrgApply.id = this.ybDrgApply.id
          ybDrgApply.areaType = this.user.areaType.value
          this.$put('ybDrgApply', {
            ...ybDrgApply
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
