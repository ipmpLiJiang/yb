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
      <a-form-item v-bind="formItemLayout" label="drg申请明细">
        <a-input
          placeholder="请输入drg申请明细"
          v-decorator="[
            'applyDataId',
            { rules: [{ required: true, message: 'drg申请明细不能为空' }] },
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
      <a-form-item v-bind="formItemLayout" label="drg医生编码">
        <a-input
          placeholder="请输入drg医生编码"
          v-decorator="[
            'readyDoctorCode',
            { rules: [{ required: true, message: 'drg医生编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg医生">
        <a-input
          placeholder="请输入drg医生"
          v-decorator="[
            'readyDoctorName',
            { rules: [{ required: true, message: 'drg医生不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg科室编码">
        <a-input
          placeholder="请输入drg科室编码"
          v-decorator="[
            'readyDeptCode',
            { rules: [{ required: true, message: 'drg科室编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg科室">
        <a-input
          placeholder="请输入drg科室"
          v-decorator="[
            'readyDeptName',
            { rules: [{ required: true, message: 'drg科室不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg变更医生编码">
        <a-input
          placeholder="请输入drg变更医生编码"
          v-decorator="[
            'changeDoctorCode',
            { rules: [{ required: true, message: 'drg变更医生编码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg变更医生">
        <a-input
          placeholder="请输入drg变更医生"
          v-decorator="[
            'changeDoctorName',
            { rules: [{ required: true, message: 'drg变更医生不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg变更复议科室编码">
        <a-input
          placeholder="请输入drg变更复议科室编码"
          v-decorator="[
            'changeDeptCode',
            {
              rules: [
                { required: true, message: 'drg变更复议科室编码不能为空' },
              ],
            },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="drg变更复议科室">
        <a-input
          placeholder="请输入drg变更复议科室"
          v-decorator="[
            'changeDeptName',
            { rules: [{ required: true, message: 'drg变更复议科室不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="操作理由">
        <a-input
          placeholder="请输入操作理由"
          v-decorator="[
            'operateReason',
            { rules: [{ required: true, message: '操作理由不能为空' }] },
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
      <a-form-item v-bind="formItemLayout" label="拒绝人代码">
        <a-input
          placeholder="请输入拒绝人代码"
          v-decorator="[
            'refuseId',
            { rules: [{ required: true, message: '拒绝人代码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="拒绝人">
        <a-input
          placeholder="请输入拒绝人"
          v-decorator="[
            'refuseName',
            { rules: [{ required: true, message: '拒绝人不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="拒绝理由">
        <a-input
          placeholder="请输入拒绝理由"
          v-decorator="[
            'refuseReason',
            { rules: [{ required: true, message: '拒绝理由不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="拒绝日期">
        <a-date-picker
          showTime
          format="YYYY-MM-DD HH:mm:ss"
          v-decorator="[
            'refuseDate',
            { rules: [{ required: true, message: '拒绝日期不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="管理员代码">
        <a-input
          placeholder="请输入管理员代码"
          v-decorator="[
            'adminPersonId',
            { rules: [{ required: true, message: '管理员代码不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="管理员">
        <a-input
          placeholder="请输入管理员"
          v-decorator="[
            'adminPersonName',
            { rules: [{ required: true, message: '管理员不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="更改日期">
        <a-date-picker
          showTime
          format="YYYY-MM-DD HH:mm:ss"
          v-decorator="[
            'adminChangeDate',
            { rules: [{ required: true, message: '更改日期不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="管理员理由">
        <a-input
          placeholder="请输入管理员理由"
          v-decorator="[
            'adminReason',
            { rules: [{ required: true, message: '管理员理由不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="可操作日期">
        <a-date-picker
          showTime
          format="YYYY-MM-DD HH:mm:ss"
          v-decorator="[
            'enableDate',
            { rules: [{ required: true, message: '可操作日期不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="操作过程">
        <a-input
          placeholder="请输入操作过程"
          v-decorator="[
            'operateProcess',
            { rules: [{ required: true, message: '操作过程不能为空' }] },
          ]"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayout" label="审批状态">
        <a-input
          placeholder="请输入审批状态"
          v-decorator="[
            'approvalState',
            { rules: [{ required: true, message: '审批状态不能为空' }] },
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
  name: 'YbDrgManageAdd',
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
      ybDrgManage: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybDrgManage = {}
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
          this.$post('ybDrgManage', {
            ...this.ybDrgManage
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
      let values = this.form.getFieldsValue(['applyDataId', 'verifyId', 'readyDoctorCode', 'readyDoctorName', 'readyDeptCode', 'readyDeptName', 'changeDoctorCode', 'changeDoctorName', 'changeDeptCode', 'changeDeptName', 'operateReason', 'operateDate', 'refuseId', 'refuseName', 'refuseReason', 'refuseDate', 'adminPersonId', 'adminPersonName', 'adminChangeDate', 'adminReason', 'enableDate', 'operateProcess', 'approvalState', 'applyDateStr', 'orderNumber', 'orderNum', 'areaType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybDrgManage[_key] = values[_key]
        })
      }
    }
  }
}
</script>
