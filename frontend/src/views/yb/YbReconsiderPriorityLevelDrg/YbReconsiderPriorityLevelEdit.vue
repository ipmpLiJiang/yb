<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="医生名称"
      >
        <input-select
            ref="inputSelectDoctor"
            v-decorator="['doctorCode', {rules: [{ required: true, message: '医生名称不能为空' }] }]"
            :type=2
            dept='医生'
            @selectChange=selectDoctorChange
        >
        </input-select>
      </a-form-item>
      <a-divider orientation="left">更改信息</a-divider>
      <a-form-item
          label="复议科室类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['deptType']" @change="handleDeptChange">
          <a-radio value="1">
            医疗组科室
          </a-radio>
          <a-radio value="2">
            固定科室
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="更改科室名称"
      >
        <input-selectdks
          ref="inputSelectDks"
          v-decorator="['dksIdTo', {rules: [{ required: checkDeptType, message: '更改科室名称不能为空' }] }]"
          @selectChange=selectDksChang
        >
        </input-selectdks>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="更改医生名称"
      >
        <input-select
            ref="inputSelectDoctorTo"
            v-decorator="['doctorCodeTo', {rules: [{ required: true, message: '医生名称不能为空' }] }]"
            :type=2
            dept='医生'
            @selectChange=selectDoctorToChange
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
import InputSelectdks from '../../common/InputSelectDks'

const formItemLayout = {
  labelCol: {
    span: 5
  },
  wrapperCol: {
    span: 16
  }
}
export default {
  name: 'YbReconsiderPriorityLevelEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect, InputSelectdks },
  data () {
    return {
      loading: false,
      checkDeptType: true,
      formItemLayout,
      form: this.$form.createForm(this),
      ybPriorityLevel: {},
      ybReconsiderPriorityLevel: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybPriorityLevel = {}
      this.$refs.inputSelectDoctor.dataSource = []
      this.$refs.inputSelectDoctor.value = ''
      this.$refs.inputSelectDoctorTo.dataSource = []
      this.$refs.inputSelectDoctorTo.value = ''
      this.$refs.inputSelectDks.dataSource = []
      this.$refs.inputSelectDks.value = ''
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    selectDoctorChange (item) {
      this.ybPriorityLevel.doctorCode = item.value
      this.ybPriorityLevel.doctorName = item.text
    },
    selectDoctorToChange (item) {
      this.ybPriorityLevel.doctorCodeTo = item.value
      this.ybPriorityLevel.doctorNameTo = item.text
    },
    selectDksChang (item) {
      this.ybPriorityLevel.dksIdTo = item.value
      this.ybPriorityLevel.dksNameTo = item.text
    },
    handleDeptChange (e) {
      if (e.target.value === '2') {
        this.checkDeptType = true
        this.$nextTick(() => {
          this.form.validateFields(['dksIdTo'], { force: this.checkDeptType })
        })
      } else {
        this.checkDeptType = false
        this.$nextTick(() => {
          this.form.validateFields(['dksIdTo'], { force: this.checkDeptType })
        })
        this.$refs.inputSelectDks.dataSource = []
        this.$refs.inputSelectDks.value = ''
        this.ybPriorityLevel.dksNameTo = ''
        this.ybPriorityLevel.dksIdTo = ''
        this.form.getFieldDecorator('dksNameTo')
        this.form.getFieldDecorator('dksIdTo')
        this.form.setFieldsValue({
          dksNameTo: '',
          dksIdTo: ''
        })
      }
    },
    setFormValues ({
      ...ybReconsiderPriorityLevel
    }) {
      this.ybReconsiderPriorityLevel.id = ybReconsiderPriorityLevel.id
      if (ybReconsiderPriorityLevel.deptType === 2) {
        this.checkDeptType = true
      } else {
        this.checkDeptType = false
      }
      this.form.getFieldDecorator('deptType')
      this.form.setFieldsValue({
        deptType: ybReconsiderPriorityLevel.deptType.toString()
      })
      setTimeout(() => {
        if (ybReconsiderPriorityLevel.dksIdTo !== '' && ybReconsiderPriorityLevel.dksIdTo !== null) {
          this.$refs.inputSelectDks.dataSource = [{
            text: ybReconsiderPriorityLevel.dksIdTo + '-' + ybReconsiderPriorityLevel.dksNameTo,
            value: ybReconsiderPriorityLevel.dksIdTo
          }]
        }
        this.$refs.inputSelectDks.value = ybReconsiderPriorityLevel.dksIdTo

        if (ybReconsiderPriorityLevel.doctorCode !== '' && ybReconsiderPriorityLevel.doctorCode !== null) {
          this.$refs.inputSelectDoctor.dataSource = [{
            text: ybReconsiderPriorityLevel.doctorCode + '-' + ybReconsiderPriorityLevel.doctorName,
            value: ybReconsiderPriorityLevel.doctorCode
          }]
        }
        this.$refs.inputSelectDoctor.value = ybReconsiderPriorityLevel.doctorCode

        if (ybReconsiderPriorityLevel.doctorCodeTo !== '' && ybReconsiderPriorityLevel.doctorCodeTo !== null) {
          this.$refs.inputSelectDoctorTo.dataSource = [{
            text: ybReconsiderPriorityLevel.doctorCodeTo + '-' + ybReconsiderPriorityLevel.doctorNameTo,
            value: ybReconsiderPriorityLevel.doctorCodeTo
          }]
        }
        this.$refs.inputSelectDoctorTo.value = ybReconsiderPriorityLevel.doctorCodeTo

        this.ybPriorityLevel.doctorCode = ybReconsiderPriorityLevel.doctorCode
        this.ybPriorityLevel.doctorName = ybReconsiderPriorityLevel.doctorName
        this.ybPriorityLevel.doctorCodeTo = ybReconsiderPriorityLevel.doctorCodeTo
        this.ybPriorityLevel.doctorNameTo = ybReconsiderPriorityLevel.doctorNameTo
        this.ybPriorityLevel.dksIdTo = ybReconsiderPriorityLevel.dksIdTo
        this.ybPriorityLevel.dksNameTo = ybReconsiderPriorityLevel.dksNameTo
      }, 200)
    },
    handleSubmit () {
      if (this.ybPriorityLevel.doctorCode !== '' && this.ybPriorityLevel.doctorCode !== undefined) {
        this.form.getFieldDecorator('doctorCode')
        this.form.setFieldsValue({
          doctorCode: this.ybPriorityLevel.doctorCode
        })
        this.form.getFieldDecorator('doctorName')
        this.form.setFieldsValue({
          doctorName: this.ybPriorityLevel.doctorName
        })
      }
      if (this.ybPriorityLevel.doctorCodeTo !== '' && this.ybPriorityLevel.doctorCodeTo !== undefined) {
        this.form.getFieldDecorator('doctorCodeTo')
        this.form.setFieldsValue({
          doctorCodeTo: this.ybPriorityLevel.doctorCodeTo
        })
        this.form.getFieldDecorator('doctorNameTo')
        this.form.setFieldsValue({
          doctorNameTo: this.ybPriorityLevel.doctorNameTo
        })
      }
      if (this.ybPriorityLevel.dksIdTo !== '' && this.ybPriorityLevel.dksIdTo !== undefined) {
        this.form.getFieldDecorator('dksNameTo')
        this.form.getFieldDecorator('dksIdTo')
        this.form.setFieldsValue({
          dksIdTo: this.ybPriorityLevel.dksIdTo,
          dksNameTo: this.ybPriorityLevel.dksNameTo
        })
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybReconsiderPriorityLevel = this.form.getFieldsValue()
          ybReconsiderPriorityLevel.id = this.ybReconsiderPriorityLevel.id
          ybReconsiderPriorityLevel.state = 4
          if (ybReconsiderPriorityLevel.doctorCode === '') {
            ybReconsiderPriorityLevel.doctorName = ''
          }
          if (ybReconsiderPriorityLevel.doctorCodeTo === '') {
            ybReconsiderPriorityLevel.doctorNameTo = ''
          }
          if (ybReconsiderPriorityLevel.dksIdTo === '') {
            ybReconsiderPriorityLevel.dksIdTo = ''
            ybReconsiderPriorityLevel.dksNameTo = ''
          }
          if (ybReconsiderPriorityLevel.deptType === '1') {
            ybReconsiderPriorityLevel.dksIdTo = ''
            ybReconsiderPriorityLevel.dksNameTo = ''
          }
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
