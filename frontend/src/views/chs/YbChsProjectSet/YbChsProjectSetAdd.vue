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
      <a-form-item v-bind="formItemLayout" label="规则名称">
        <a-input
          placeholder="请输入规则名称"
          v-decorator="[
            'ruleName',
            { rules: [{ required: true, message: '规则名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="项目名称">
        <a-input
          placeholder="请输入项目名称"
          v-decorator="[
            'projectName',
            { rules: [{ required: true, message: '项目名称不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="匹配项目名称">
        <a-input
          placeholder="请输入匹配项目名称"
          v-decorator="[
            'qdName',
            { rules: [{ required: true, message: '匹配项目名称不能为空' }] },
          ]"
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
        <a-button style="margin-right: 0.8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading"
        >提交</a-button
      >
    </div>
  </a-drawer>
</template>

<script>
const formItemLayout = {
  labelCol: {
    span: 4
  },
  wrapperCol: {
    span: 18
  }
}
export default {
  name: 'YbChsProjectSetAdd',
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
      areaType: 0,
      ybChsProjectSet: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybChsProjectSet = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues (areaType) {
      this.areaType = areaType
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.ybChsProjectSet.areaType = this.areaType
          this.$post('ybChsProjectSet', {
            ...this.ybChsProjectSet
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
      let values = this.form.getFieldsValue(['ruleName', 'projectName', 'qdName'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybChsProjectSet[_key] = values[_key]
        })
      }
    }
  }
}
</script>
