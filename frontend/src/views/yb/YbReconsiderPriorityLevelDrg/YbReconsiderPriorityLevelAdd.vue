<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
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
  name: 'YbReconsiderPriorityLevelAdd',
  props: {
    addVisiable: {
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
      areaType: undefined,
      ybPriorityLevel: {},
      ybReconsiderPriorityLevel: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybReconsiderPriorityLevel = {}
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
    setFormValues (areaType) {
      this.form.getFieldDecorator('deptType')
      this.form.setFieldsValue({
        deptType: '2'
      })
      this.checkDeptType = true
      this.areaType = areaType
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
          dksNameTo: this.ybPriorityLevel.dksNameTo,
          dksIdTo: this.ybPriorityLevel.dksIdTo
        })
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.ybReconsiderPriorityLevel.state = 4
          if (this.ybReconsiderPriorityLevel.doctorCode === '') {
            this.ybReconsiderPriorityLevel.doctorName = ''
          }
          if (this.ybReconsiderPriorityLevel.doctorCodeTo === '') {
            this.ybReconsiderPriorityLevel.doctorNameTo = ''
          }
          if (this.ybReconsiderPriorityLevel.dksIdTo === '') {
            this.ybReconsiderPriorityLevel.dksIdTo = ''
            this.ybReconsiderPriorityLevel.dksNameTo = ''
          }
          if (this.ybReconsiderPriorityLevel.deptType === '1') {
            this.ybReconsiderPriorityLevel.dksIdTo = ''
            this.ybReconsiderPriorityLevel.dksNameTo = ''
          }
          this.ybReconsiderPriorityLevel.areaType = this.areaType
          this.$post('ybReconsiderPriorityLevel', {
            ...this.ybReconsiderPriorityLevel
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
      let values = this.form.getFieldsValue(['doctorCode', 'doctorName', 'deptType', 'dksIdTo', 'dksNameTo', 'doctorCodeTo', 'doctorNameTo'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybReconsiderPriorityLevel[_key] = values[_key]
        })
      }
    }
  }
}
</script>
