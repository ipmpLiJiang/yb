<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width="750"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
    style="height: calc(100% - 55px); overflow: auto; padding-bottom: 53px"
  >
    <a-form :form="form">
      <a-form-item v-bind="formItemLayout" label="大科室分院编码">
        <a-input
          placeholder="请输入大科室分院编码"
          v-decorator="[
            'dksFyid',
            { rules: [{ required: true, message: '大科室分院编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="院区"
      >
        <a-select
          style="width: 120px"
          placeholder="请选择院区"
          v-decorator="['fyid', {rules: [{ required: true, message: '院区不能为空' }], initialValue: '1001' }]"
        >
          <a-select-option value="1001">
            本院
          </a-select-option>
          <a-select-option value="1002">
            西院
          </a-select-option>
          <a-select-option value="1003">
            金银湖
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="大科室编码">
        <a-input
          placeholder="请输入大科室编码"
          v-decorator="[
            'dksId',
            { rules: [{ required: true, message: '大科室编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="大科室名称">
        <a-input
          placeholder="请输入大科室名称"
          v-decorator="[
            'dksName',
            { rules: [{ required: true, message: '大科室名称不能为空' }] },
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
    span: 17
  }
}
export default {
  name: 'YbDksAdd',
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
      ybDks: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybDks = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    setFormValues () {
      this.form.getFieldDecorator('fyid')
      this.form.setFieldsValue({
        fyid: '1001'
      })
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.$post('ybDks', {
            ...this.ybDks
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
      let values = this.form.getFieldsValue(['dksId', 'dksName', 'spellCode', 'fyid', 'dksFyid'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybDks[_key] = values[_key]
        })
      }
    }
  }
}
</script>
