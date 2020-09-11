<template>
  <a-modal
    title="修改"
    :keyboard="false"
    :width=900
    :centered="true"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    @ok="handleSubmit"
    @cancel="onClose"
  >
    <a-form :form="form">
      <a-row>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="编码"
          >
            <a-input
              placeholder="请输入编码"
              v-decorator="['code', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="订单号"
          >
            <a-input
              placeholder="请输入订单号"
              v-decorator="['ebeln', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="项目号"
          >
            <a-input
              placeholder="请输入项目号"
              v-decorator="['ebelp', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="供应商账号"
          >
            <a-input
              placeholder="请输入供应商账号"
              v-decorator="['lifnr', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="药品编码"
          >
            <a-input
              placeholder="请输入药品编码"
              v-decorator="['matnr', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="药品名称"
          >
            <a-input
              placeholder="请输入药品名称"
              v-decorator="['txz01', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="院区编码"
          >
            <a-input
              placeholder="请输入院区编码"
              v-decorator="['werks', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="院区"
          >
            <a-input
              placeholder="请输入院区"
              v-decorator="['werkst', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="药房"
          >
            <a-input
              placeholder="请输入药房"
              v-decorator="['lgort', {}]"
            />
          </a-form-item>
        </a-col>
        <a-col
          :md="8"
          :sm="24"
        >
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="订单数量"
          >
            <a-input
              placeholder="请输入订单数量"
              v-decorator="['menge', {}]"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script>
import moment from 'moment'

const formItemLayout = {
  labelCol: { span: 10 },
  wrapperCol: { span: 13, offset: 1 }
}
export default {
  name: 'ScmDPlanEdit',
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
      scmDPlan: {}
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
    setFormValues ({ ...scmDPlan }) {
      let fields = ['code', 'ebeln', 'ebelp', 'lifnr', 'matnr', 'txz01', 'werks', 'werkst', 'lgort']
      let fieldDates = ['createTime', 'modifyTime', 'dataTimestamp']
      Object.keys(scmDPlan).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (scmDPlan[key] !== '') {
              obj[key] = moment(scmDPlan[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = scmDPlan[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.scmDPlan.id = scmDPlan.id
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let scmDPlan = this.form.getFieldsValue()
          scmDPlan.id = this.scmDPlan.id
          this.$put('scmDPlan', {
            ...scmDPlan
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
<style lang="less" scoped>
@import "ScmDPlanEdit";
</style>
