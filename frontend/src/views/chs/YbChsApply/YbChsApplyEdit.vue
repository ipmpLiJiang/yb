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
          disabled="disabled"
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
          @change="dateChange"
          v-decorator="['enableDate', {rules: [{ required: true, message: '确认日期不能为空' }] }]"
          :format="enableFormat"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-col :span=12>
        <a-form-item
          v-bind="{
            labelCol: {
              span: 8
            },
            wrapperCol: {
              span: 13
            }
          }"
          label="未申诉更新"
        >
        <a-tooltip>
          <template slot="title">
            打钩后提交数据，将会把未申诉数据更新至待申诉中，
            使用此操作需要重新启动截止服务，请谨慎使用.
          </template>
          <a-checkbox :checked="checked" @change="onChange">
            是否更新
          </a-checkbox>
        </a-tooltip>
        </a-form-item>
        </a-col>
        <a-col :span=12 style="margin-top:10px">
          <a-popconfirm
          v-show="isJob"
          title="确定开启截止服务？"
          @confirm="startJob"
          okText="确定"
          cancelText="取消"
        >
        <a>截止服务</a>
        </a-popconfirm>
        </a-col>
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
// import momenttimezone from 'moment-timezone'

const formItemLayout = {
  labelCol: {
    span: 4
  },
  wrapperCol: {
    span: 17
  }
}
export default {
  name: 'YbChsApplyEdit',
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
      ybChsApply: {},
      user: this.$store.state.account.user,
      monthFormat: 'YYYY-MM',
      enableFormat: 'YYYY-MM-DD',
      checked: false,
      isJob: false,
      dayFormat: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    dateChange (value) {
      if (moment(value).isDST()) {
        value = moment(value).add(1, 'hours')
        // subtract(1,'hours')
      }
      // console.log(value)
      // console.log(moment(value).format('YYYY-MM-DD HH:mm:ss'))
    },
    onChange () {
      this.checked = !this.checked
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    startJob () {
      this.$put('ybChsVerify/startJob', {
        applyDateStr: this.ybChsApply.applyDateStr,
        areaType: this.user.areaType.value,
        jobTypeList: [1]
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('启动Job成功')
        } else {
          this.$message.warning(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('启动Job失败')
      })
    },
    setFormValues ({
      ...ybChsApply
    }) {
      this.checked = false
      this.isJob = false
      let fields = ['applyDate', 'endDate', 'enableDate']
      let fieldDates = ['applyDate', 'endDate', 'enableDate']
      Object.keys(ybChsApply).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybChsApply[key] !== '') {
              obj[key] = moment(ybChsApply[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybChsApply[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybChsApply.id = ybChsApply.id
      this.ybChsApply.applyDateStr = ybChsApply.applyDateStr
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybChsApply = this.form.getFieldsValue()
          ybChsApply.id = this.ybChsApply.id
          ybChsApply.applyDateStr = this.ybChsApply.applyDateStr
          ybChsApply.isUpOverdue = this.checked
          ybChsApply.areaType = this.user.areaType.value
          if (moment(ybChsApply.enableDate).isDST()) {
            ybChsApply.enableDate = moment(ybChsApply.enableDate).add(1, 'hours')
          }
          this.$put('ybChsApply', {
            ...ybChsApply
          }).then((r) => {
            if (r.data.data.success === 1) {
              if (!this.checked) {
                if (r.data.data.message === 'ok') {
                  this.reset()
                  this.$emit('success')
                } else {
                  this.$message.warning(r.data.data.message)
                }
              } else {
                if (r.data.data.message === 'ok') {
                  this.isJob = true
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
    }
  }
}
</script>
