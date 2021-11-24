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
      <a-form-item v-bind="formItemLayout" label="dgr申请明细">
        <a-input
          placeholder="请输入dgr申请明细"
          v-decorator="[
            'applyDataId',
            { rules: [{ required: true, message: 'dgr申请明细不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="核对Id">
        <a-input
          placeholder="请输入核对Id"
          v-decorator="[
            'verifyId',
            { rules: [{ required: true, message: '核对Id不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="管理Id">
        <a-input
          placeholder="请输入管理Id"
          v-decorator="[
            'manageId',
            { rules: [{ required: true, message: '管理Id不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="医生编码">
        <a-input
          placeholder="请输入医生编码"
          v-decorator="[
            'doctorCode',
            { rules: [{ required: true, message: '医生编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="医生">
        <a-input
          placeholder="请输入医生"
          v-decorator="[
            'doctorName',
            { rules: [{ required: true, message: '医生不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="科室编码">
        <a-input
          placeholder="请输入科室编码"
          v-decorator="[
            'deptCode',
            { rules: [{ required: true, message: '科室编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="科室">
        <a-input
          placeholder="请输入科室"
          v-decorator="[
            'deptName',
            { rules: [{ required: true, message: '科室不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="理由">
        <a-input
          placeholder="请输入理由"
          v-decorator="[
            'operateReason',
            { rules: [{ required: true, message: '理由不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="操作日期">
        <a-date-picker
          showTime
          format="YYYY-MM-DD HH:mm:ss"
          v-decorator="[
            'operateDate',
            { rules: [{ required: true, message: '操作日期不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="复议年月Str">
        <a-input
          placeholder="请输入复议年月Str"
          v-decorator="[
            'applyDateStr',
            { rules: [{ required: true, message: '复议年月Str不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="序号">
        <a-input
          placeholder="请输入序号"
          v-decorator="[
            'orderNumber',
            { rules: [{ required: true, message: '序号不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="排序">
        <a-input
          placeholder="请输入排序"
          v-decorator="[
            'orderNum',
            { rules: [{ required: true, message: '排序不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="院区">
        <a-input
          placeholder="请输入院区"
          v-decorator="[
            'areaType',
            { rules: [{ required: true, message: '院区不能为空' }] },
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
    span: 3
  },
  wrapperCol: {
    span: 18
  }
}
export default {
  name: 'YbDrgResultAdd',
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
      ybDrgResult: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybDrgResult = {}
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
          this.$post('ybDrgResult', {
            ...this.ybDrgResult
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
      let values = this.form.getFieldsValue(['applyDataId', 'verifyId', 'manageId', 'doctorCode', 'doctorName', 'deptCode', 'deptName', 'operateReason', 'operateDate', 'applyDateStr', 'orderNumber', 'orderNum', 'areaType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybDrgResult[_key] = values[_key]
        })
      }
    }
  }
}
</script>
