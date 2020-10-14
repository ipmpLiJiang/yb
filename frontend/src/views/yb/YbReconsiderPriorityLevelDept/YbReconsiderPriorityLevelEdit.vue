<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="科室名称"
      >
        <input-select
            ref="inputSelectDept"
            v-decorator="['deptCode', {rules: [{ required: true, message: '科室名称不能为空' }] }]"
            :type=1
            @selectChange=selectDeptChange
        >
        </input-select>
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
import InputSelect from '../../common/InputSelect'

const formItemLayout = {
  labelCol: {
    span: 3
  },
  wrapperCol: {
    span: 18
  }
}
export default {
  name: 'YbReconsiderPriorityLevelEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      ybPriorityLevel: {},
      ybReconsiderPriorityLevel: {}
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
    selectDeptChange (item) {
      this.ybPriorityLevel.deptCode = item.value
      this.ybPriorityLevel.deptName = item.text
    },
    setFormValues ({
      ...ybReconsiderPriorityLevel
    }) {
      this.ybReconsiderPriorityLevel.id = ybReconsiderPriorityLevel.id
      setTimeout(() => {
        this.$refs.inputSelectDept.dataSource = [{
          text: ybReconsiderPriorityLevel.deptName,
          value: ybReconsiderPriorityLevel.deptCode
        }]
        this.$refs.inputSelectDept.value = ybReconsiderPriorityLevel.deptCode

        this.ybPriorityLevel.deptCode = ybReconsiderPriorityLevel.deptCode
        this.ybPriorityLevel.deptName = ybReconsiderPriorityLevel.deptName
      }, 200)
    },
    handleSubmit () {
      if (this.ybPriorityLevel.deptCode !== '' && this.ybPriorityLevel.deptCode !== undefined) {
        this.form.getFieldDecorator('deptCode')
        this.form.setFieldsValue({
          deptCode: this.ybPriorityLevel.deptCode
        })
        this.form.getFieldDecorator('deptName')
        this.form.setFieldsValue({
          deptName: this.ybPriorityLevel.deptName
        })
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybReconsiderPriorityLevel = this.form.getFieldsValue()
          ybReconsiderPriorityLevel.id = this.ybReconsiderPriorityLevel.id
          this.$put('ybReconsiderPriorityLevel', {
            ...ybReconsiderPriorityLevel
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
