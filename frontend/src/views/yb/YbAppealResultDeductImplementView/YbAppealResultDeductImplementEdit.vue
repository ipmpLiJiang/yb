<template>
    <div>
      <a-form :form="form">
      <a-row justify="center" type="flex">
        <a-col :span=9>
          <a-form-item
            label="绩效落实年月"
            v-bind="formItemLayout"
          >
            <a-month-picker
              placeholder="请输入绩效落实年月"
              @change="monthChange"
              v-decorator="['implementDateStr', {rules: [{ required: true, message: '绩效落实年月不能为空' }] }]"
              :format="monthFormat"
            />
          </a-form-item>
        </a-col>
        <a-col :span=12>
          <a-form-item
            label="分摊方式"
            v-bind="formItemLayout"
          >
            <a-radio-group  v-decorator="['shareState']">
              <a-radio value="0">
                个人分摊
              </a-radio>
              <a-radio value="1">
                科室分摊
              </a-radio>
              <a-radio value="2">
                其他分摊
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row type="flex" justify="start">
        <a-col :span=21>
          <a-form-item
            v-bind="{
              labelCol: { span: 5 },
              wrapperCol: { span: 17, offset: 1 }
            }"
            label="分摊方案"
          >
            <a-textarea
              placeholder="请输入分摊方案"
              v-decorator="['shareProgramme']"
              :rows="6"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <br>
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
      <a-popconfirm
        title="确定提交数据？"
        @confirm="handleSubmit"
        okText="确定"
        cancelText="取消"
      >
        <a-button type="primary" style="margin-right: .8rem">提交</a-button>
      </a-popconfirm>
    </div>
    </div>
</template>
<script>
import moment from 'moment'
const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 13, offset: 1 }
}
export default {
  name: 'YbAppealResultDeductImplementEdit',
  props: {
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      selectImplementDateStr: this.formatDate(),
      form: this.$form.createForm(this),
      // checkSp: false,
      ybAppealResult: {
      },
      ybAppealResultDeductImplement: {
      }
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.ybAppealResult = {}
      this.ybAppealResultDeductImplement = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleChange (e) {
      // if (e.target.value === '0') {
      //   this.checkSp = false
      //   this.$nextTick(() => {
      //     this.form.validateFields(['shareProgramme'], { force: true })
      //   })
      // } else {
      //   this.checkSp = true
      // }
    },
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.selectImplementDateStr = dateString
    },
    setFormValues ({ ...ybAppealResult }) {
      // this.checkSp = false
      this.ybAppealResult = ybAppealResult
      this.selectImplementDateStr = this.formatDate()

      this.form.getFieldDecorator('shareState')
      this.form.getFieldDecorator('shareProgramme')
      this.form.getFieldDecorator('implementDateStr')
      this.form.setFieldsValue({
        shareState: '0',
        shareProgramme: '',
        implementDateStr: this.selectImplementDateStr
      })
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.ybAppealResultDeductImplement.resetDataId = this.ybAppealResult.resetDataId
          this.ybAppealResultDeductImplement.relatelDataId = this.ybAppealResult.relatelDataId
          this.ybAppealResultDeductImplement.implementDateStr = this.selectImplementDateStr
          this.ybAppealResultDeductImplement.applyDateStr = this.ybAppealResult.applyDateStr
          this.ybAppealResultDeductImplement.dataType = this.ybAppealResult.dataType
          this.ybAppealResultDeductImplement.typeno = this.ybAppealResult.typeno
          this.$post('ybAppealResultDeductimplement', {
            ...this.ybAppealResultDeductImplement
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
      let values = this.form.getFieldsValue(['shareState', 'shareProgramme'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybAppealResultDeductImplement[_key] = values[_key] })
      }
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
