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
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="编码"
      >
        <a-input
          placeholder="请输入编码"
          v-decorator="['code', {}]"
        />
      </a-form-item>
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
  name: 'ScmDPlanAdd',
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
      scmDPlan: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.scmDPlan = {}
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
          this.$post('scmDPlan', {
            ...this.scmDPlan
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
      let values = this.form.getFieldsValue(['code', 'ebeln', 'ebelp', 'lifnr', 'matnr', 'txz01', 'werks', 'werkst', 'lgort', 'menge'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.scmDPlan[_key] = values[_key] })
      }
    }
  }
}
</script>
