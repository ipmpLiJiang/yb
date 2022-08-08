<template>
  <a-drawer
    title="新增"
    :maskClosable="false"
    width=800
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="addVisiable"
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
          label="复议医生类型"
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
        label="默认复议医生名称"
      >
        <input-select
            ref="inputSelectDoctor"
            v-decorator="['doctorCode', {rules: [{required: checkPersonType, message: '医生名称不能为空' }] }]"
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
const formItemLayout = {
  labelCol: {
    span: 7
  },
  wrapperCol: {
    span: 14
  }
}
export default {
  name: 'YbReconsiderPriorityLevelAdd',
  props: {
    addVisiable: {
      default: false
    }
  },
  components: { InputSelect },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      areaType: undefined,
      checkPersonType: true,
      ybPriorityLevel: {},
      isDefaultCheck: false,
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
      this.$refs.inputSelectDept.dataSource = []
      this.$refs.inputSelectDept.value = ''
      this.form.resetFields()
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
    setFormValues (areaType) {
      // this.$refs.inputSelectDept.dataSource = []
      this.form.getFieldDecorator('deptState')
      this.form.getFieldDecorator('personType')
      this.form.getFieldDecorator('isFixDept')
      this.form.setFieldsValue({
        deptState: '1',
        personType: '4',
        isFixDept: false
      })
      this.isDefaultCheck = false
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
          this.setFields()
          this.ybReconsiderPriorityLevel.state = 3
          if (this.ybReconsiderPriorityLevel.doctorCode === '') {
            this.ybReconsiderPriorityLevel.doctorName = ''
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
      // let values = this.form.getFieldsValue(['rplName', 'doctorCode', 'doctorName', 'deptCode', 'deptName'])
      let values = this.form.getFieldsValue(['doctorCode', 'doctorName', 'deptCode', 'deptName', 'deptState', 'personType', 'isFixDept'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => {
          this.ybReconsiderPriorityLevel[_key] = values[_key]
        })
      }
    }
  }
}
</script>
