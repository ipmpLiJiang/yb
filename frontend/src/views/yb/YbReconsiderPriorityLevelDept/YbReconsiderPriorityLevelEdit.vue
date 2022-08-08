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
      <a-row justify="center" type="flex">
        <a-col :span="14">
      <a-form-item
          label="科室类型"
          v-bind="{
                labelCol: {
                  span: 12
                },
                wrapperCol: {
                  span: 12
                }
              }"
        >
        <a-radio-group  v-decorator="['deptState']">
          <a-radio value="1">
            执行科室
          </a-radio>
        </a-radio-group>
      </a-form-item>
        </a-col>
      <a-col :span="9">
          <a-form-item
              label=""
              v-bind="{
                labelCol: {
                  span: 10
                },
                wrapperCol: {
                  span: 14
                }
              }"
            >
            <a-checkbox v-decorator="[
              'isFixDept',
              {
                valuePropName: 'checked',
                initialValue: isDefaultCheck,
              },
            ]">
              是否固定科室
            </a-checkbox>
          </a-form-item>
        </a-col>
        <a-col :span="1">
        </a-col>
      </a-row>
      <a-row justify="start" type="flex">
        <a-col :span="24">
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
      </a-col>
      </a-row>
      <a-row justify="start" type="flex">
        <a-col :span="24">
      <a-form-item
          label="默认复议医生类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['personType']" @change="handleChange">
          <a-radio value="1">
            开方人员
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
      </a-col>
      </a-row>
      <a-row justify="start" type="flex">
        <a-col :span="24">
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
      </a-col>
      </a-row>
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
    span: 7
  },
  wrapperCol: {
    span: 14
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
      checkPersonType: true,
      ybPriorityLevel: {},
      isDefaultCheck: false,
      ybReconsiderPriorityLevel: {}
    }
  },
  methods: {
    moment,
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
    handleChange (e) {
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
    selectDoctorChange (item) {
      this.ybPriorityLevel.doctorCode = item.value
      this.ybPriorityLevel.doctorName = item.text
    },
    selectDeptChange (item) {
      this.ybPriorityLevel.deptCode = item.value
      this.ybPriorityLevel.deptName = item.text
    },
    setFormValues ({
      ...ybReconsiderPriorityLevel
    }) {
      this.ybReconsiderPriorityLevel.id = ybReconsiderPriorityLevel.id
      if (ybReconsiderPriorityLevel.personType === 4) {
        this.checkPersonType = true
      } else {
        this.checkPersonType = false
      }
      this.form.getFieldDecorator('deptState')
      this.form.getFieldDecorator('personType')
      this.form.getFieldDecorator('isFixDept')

      this.form.setFieldsValue({
        deptState: ybReconsiderPriorityLevel.deptState.toString(),
        personType: ybReconsiderPriorityLevel.personType.toString(),
        isFixDept: ybReconsiderPriorityLevel.isFixDept
      })
      this.isDefaultCheck = ybReconsiderPriorityLevel.isFixDept
      setTimeout(() => {
        this.$refs.inputSelectDept.dataSource = [{
          text: ybReconsiderPriorityLevel.deptCode + '-' + ybReconsiderPriorityLevel.deptName,
          value: ybReconsiderPriorityLevel.deptCode
        }]
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
          ybReconsiderPriorityLevel.state = 3
          if (ybReconsiderPriorityLevel.doctorCode === '') {
            ybReconsiderPriorityLevel.doctorName = ''
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
