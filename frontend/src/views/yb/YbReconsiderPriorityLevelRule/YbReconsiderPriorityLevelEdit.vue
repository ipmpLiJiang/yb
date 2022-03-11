<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=800
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
        v-bind="formItemLayout"
        label="规则名称"
      >
        <a-input
          placeholder="请输入规则名称"
          v-decorator="['rplName', {rules: [{ required: true, message: '规则名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
          label="复议科室类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['deptType']" @change="handleDeptChange">
          <a-radio value="1">
            开单科室
          </a-radio>
          <a-radio value="2">
            执行科室
          </a-radio>
          <a-radio value="3">
            计费科室
          </a-radio>
          <a-radio value="4">
            固定科室
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="科室名称"
      >
        <input-select
            ref="inputSelectDept"
            v-decorator="['deptCode', {rules: [{ required: checkDeptType, message: '科室名称不能为空' }] }]"
            :type=1
            @selectChange=selectDeptChange
        >
        </input-select>
      </a-form-item>
      <a-form-item
          label="复议医生类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['personType']" @change="handlePersonChange">
          <a-radio value="1">
            开单人员
          </a-radio>
          <a-radio value="2">
            执行人员
          </a-radio>
          <a-radio value="3">
            计费人员
          </a-radio>
          <a-radio value="4">
            固定人员
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="医生名称"
      >
        <input-select
            ref="inputSelectDoctor"
            v-decorator="['doctorCode', {rules: [{ required: checkPersonType, message: '医生名称不能为空' }] }]"
            :type=2
            dept='医生'
            @selectChange=selectDoctorChange
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
import moment from 'moment'

const formItemLayout = {
  labelCol: {
    span: 6
  },
  wrapperCol: {
    span: 15
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
      checkPersonType: true,
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
      this.$refs.inputSelectDept.dataSource = []
      this.$refs.inputSelectDept.value = ''
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
    selectDeptChange (item) {
      this.ybPriorityLevel.deptCode = item.value
      this.ybPriorityLevel.deptName = item.text
    },
    handleDeptChange (e) {
      if (e.target.value === '4') {
        this.checkDeptType = true
        this.$nextTick(() => {
          this.form.validateFields(['deptCode'], { force: this.checkDeptType })
        })
      } else {
        this.checkDeptType = false
        this.$nextTick(() => {
          this.form.validateFields(['deptCode'], { force: this.checkDeptType })
        })
        this.$refs.inputSelectDept.dataSource = []
        this.$refs.inputSelectDept.value = ''
        this.ybPriorityLevel.deptCode = ''
        this.ybPriorityLevel.deptName = ''
        this.form.getFieldDecorator('deptCode')
        this.form.getFieldDecorator('deptName')

        this.form.setFieldsValue({
          deptCode: '',
          deptName: ''
        })
      }
    },
    handlePersonChange (e) {
      if (e.target.value === '4') {
        this.checkPersonType = true
        this.$nextTick(() => {
          this.form.validateFields(['doctorCode'], { force: this.checkPersonType })
        })
      } else {
        this.checkPersonType = false
        this.$nextTick(() => {
          this.form.validateFields(['doctorCode'], { force: this.checkPersonType })
        })
        this.$refs.inputSelectDoctor.dataSource = []
        this.$refs.inputSelectDoctor.value = ''
        this.ybPriorityLevel.doctorCode = ''
        this.ybPriorityLevel.doctorName = ''
        this.form.getFieldDecorator('doctorCode')
        this.form.getFieldDecorator('doctorName')

        this.form.setFieldsValue({
          doctorCode: '',
          doctorName: ''
        })
      }
    },
    setFormValues ({
      ...ybReconsiderPriorityLevel
    }) {
      let fields = ['rplName']
      let fieldDates = []
      Object.keys(ybReconsiderPriorityLevel).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybReconsiderPriorityLevel[key] !== '') {
              obj[key] = moment(ybReconsiderPriorityLevel[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybReconsiderPriorityLevel[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybReconsiderPriorityLevel.id = ybReconsiderPriorityLevel.id
      if (ybReconsiderPriorityLevel.personType === 4) {
        this.checkPersonType = true
      } else {
        this.checkPersonType = false
      }
      if (ybReconsiderPriorityLevel.deptType === 4) {
        this.checkDeptType = true
      } else {
        this.checkDeptType = false
      }
      this.form.getFieldDecorator('personType')
      this.form.getFieldDecorator('deptType')
      this.form.setFieldsValue({
        personType: ybReconsiderPriorityLevel.personType.toString(),
        deptType: ybReconsiderPriorityLevel.deptType.toString()
      })
      setTimeout(() => {
        if (ybReconsiderPriorityLevel.deptCode !== '' && ybReconsiderPriorityLevel.deptCode !== null) {
          this.$refs.inputSelectDept.dataSource = [{
            text: ybReconsiderPriorityLevel.deptCode + '-' + ybReconsiderPriorityLevel.deptName,
            value: ybReconsiderPriorityLevel.deptCode
          }]
        }
        this.$refs.inputSelectDept.value = ybReconsiderPriorityLevel.deptCode

        if (ybReconsiderPriorityLevel.doctorCode !== '' && ybReconsiderPriorityLevel.doctorCode !== null) {
          this.$refs.inputSelectDoctor.dataSource = [{
            text: ybReconsiderPriorityLevel.doctorCode + '-' + ybReconsiderPriorityLevel.doctorName,
            value: ybReconsiderPriorityLevel.doctorCode
          }]
        }
        this.$refs.inputSelectDoctor.value = ybReconsiderPriorityLevel.doctorCode

        this.ybPriorityLevel.doctorCode = ybReconsiderPriorityLevel.doctorCode
        this.ybPriorityLevel.doctorName = ybReconsiderPriorityLevel.doctorName
        this.ybPriorityLevel.deptCode = ybReconsiderPriorityLevel.deptCode
        this.ybPriorityLevel.deptName = ybReconsiderPriorityLevel.deptName
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
          ybReconsiderPriorityLevel.state = 1
          if (ybReconsiderPriorityLevel.doctorCode === '') {
            ybReconsiderPriorityLevel.doctorName = ''
          }
          if (ybReconsiderPriorityLevel.deptCode === '') {
            ybReconsiderPriorityLevel.deptName = ''
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
