<template>
  <a-drawer
    title="修改"
    :maskClosable="false"
    width=850
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-form-item
          label="匹配类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['zymzType']">
          <a-radio value="2">
            住院
          </a-radio>
          <a-radio value="1">
            门诊
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
          label="数据类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['plType']">
          <a-radio value="1">
            药品
          </a-radio>
          <a-radio value="2">
            项目
          </a-radio>
          <a-radio value="3">
            材料
          </a-radio>
          <a-radio value="4">
            其他
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        label="替换科室"
      >
        <inputSelectChs-dks
          ref="inputSelectThDks"
          v-decorator="['dksId', {rules: [{ required: checkThDeptType, message: '替换科室不能为空' }] }]"
          @selectChange=selectThDksChange
        >
        </inputSelectChs-dks>
      </a-form-item>
      <a-form-item
          label="是否规则"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['isRule']" @change="handleRuleChange">
          <a-radio value="1">
            是
          </a-radio>
          <a-radio value="2">
            否
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        v-if="checkRuleType"
        label="规则名称"
      >
        <a-input
          placeholder="请输入规则名称"
          v-decorator="['ruleName', {rules: [{ required: checkRuleType, message: '规则名称不能为空' }] }]"
        />
      </a-form-item>
      <a-form-item
          label="是否项目"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['isProject']" @change="handleProjectChange">
          <a-radio value="1">
            是
          </a-radio>
          <a-radio value="2">
            否
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-bind="formItemLayout"
        v-if="checkProjectType"
        label="项目名称"
      >
        <a-input
          placeholder="请输入项目名称"
          v-decorator="['projectName', {rules: [{ required: checkProjectType, message: '项目名称不能为空' }] }]"
        />
      </a-form-item>
      <a-divider orientation="left">更改信息</a-divider>
      <a-form-item
          label="复议科室类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['deptType']" @change="handleDeptChange">
          <a-radio value="5">
            主治科室
          </a-radio>
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
        <inputSelectChs-dks
          ref="inputSelectDks"
          v-decorator="['dksIdTo', {rules: [{ required: checkDeptType, message: '科室名称不能为空' }] }]"
          @selectChange=selectDksChange
        >
        </inputSelectChs-dks>
      </a-form-item>
      <a-form-item
          label="复议医生类型"
          v-bind="formItemLayout"
        >
        <a-radio-group  v-decorator="['personType']" @change="handlePersonChange">
          <a-radio value="5">
            主治医生
          </a-radio>
          <a-radio value="1">
            开单医生
          </a-radio>
          <a-radio value="2">
            执行医生
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
            v-decorator="['doctorCodeTo', {rules: [{ required: checkPersonType, message: '医生名称不能为空' }] }]"
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
import InputSelectChsDks from '../../common/InputSelectChsDks'
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
  name: 'YbChsPriorityLevelEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect, InputSelectChsDks },
  data () {
    return {
      loading: false,
      checkPersonType: true,
      checkDeptType: true,
      checkThDeptType: true,
      checkRuleType: true,
      checkProjectType: true,
      formItemLayout,
      form: this.$form.createForm(this),
      ybPriorityLevel: {},
      ybChsPriorityLevel: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybPriorityLevel = {}
      this.$refs.inputSelectDoctor.dataSource = []
      this.$refs.inputSelectDoctor.value = ''
      this.$refs.inputSelectDks.dataSource = []
      this.$refs.inputSelectDks.value = ''
      this.$refs.inputSelectThDks.dataSource = []
      this.$refs.inputSelectThDks.value = ''
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    selectDoctorChange (item) {
      this.ybPriorityLevel.doctorCodeTo = item.value
      this.ybPriorityLevel.doctorNameTo = item.text
    },
    selectDksChange (item) {
      this.ybPriorityLevel.dksIdTo = item.value
      this.ybPriorityLevel.dksNameTo = item.text
    },
    selectThDksChange (item) {
      this.ybPriorityLevel.dksId = item.value
      this.ybPriorityLevel.dksName = item.text
      this.checkThDeptType = false
    },
    handleRuleChange (e) {
      if (e.target.value === '1') {
        this.checkRuleType = true
        this.$nextTick(() => {
          this.form.validateFields(['ruleName'], { force: this.checkRuleType })
        })
      } else {
        this.checkRuleType = false
        this.$nextTick(() => {
          this.form.validateFields(['ruleName'], { force: this.checkRuleType })
        })
        this.ybPriorityLevel.ruleName = ''
        this.form.getFieldDecorator('ruleName')

        this.form.setFieldsValue({
          ruleName: ''
        })
      }
    },
    handleProjectChange (e) {
      if (e.target.value === '1') {
        this.checkProjectType = true
        this.$nextTick(() => {
          this.form.validateFields(['projectName'], { force: this.checkProjectType })
        })
      } else {
        this.checkProjectType = false
        this.$nextTick(() => {
          this.form.validateFields(['projectName'], { force: this.checkProjectType })
        })
        this.ybPriorityLevel.projectName = ''
        this.form.getFieldDecorator('projectName')

        this.form.setFieldsValue({
          projectName: ''
        })
      }
    },
    handleDeptChange (e) {
      if (e.target.value === '4') {
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
        this.ybPriorityLevel.dksIdTo = ''
        this.ybPriorityLevel.dksNameTo = ''
        this.form.getFieldDecorator('dksIdTo')
        this.form.getFieldDecorator('dksNameTo')

        this.form.setFieldsValue({
          dksIdTo: '',
          dksNameTo: ''
        })
      }
    },
    handlePersonChange (e) {
      if (e.target.value === '4') {
        this.checkPersonType = true
        this.$nextTick(() => {
          this.form.validateFields(['doctorCodeTo'], { force: this.checkPersonType })
        })
      } else {
        this.checkPersonType = false
        this.$nextTick(() => {
          this.form.validateFields(['doctorCodeTo'], { force: this.checkPersonType })
        })
        this.$refs.inputSelectDoctor.dataSource = []
        this.$refs.inputSelectDoctor.value = ''
        this.ybPriorityLevel.doctorCodeTo = ''
        this.ybPriorityLevel.doctorNameTo = ''
        this.form.getFieldDecorator('doctorCodeTo')
        this.form.getFieldDecorator('doctorNameTo')

        this.form.setFieldsValue({
          doctorCodeTo: '',
          doctorNameTo: ''
        })
      }
    },
    setFormValues ({
      ...ybChsPriorityLevel
    }) {
      let fields = ['ruleName', 'projectName']
      let fieldDates = []
      Object.keys(ybChsPriorityLevel).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          let obj = {}
          if (fieldDates.indexOf(key) !== -1) {
            if (ybChsPriorityLevel[key] !== '') {
              obj[key] = moment(ybChsPriorityLevel[key])
            } else {
              obj[key] = ''
            }
          } else {
            obj[key] = ybChsPriorityLevel[key]
          }
          this.form.setFieldsValue(obj)
        }
      })
      this.ybChsPriorityLevel.id = ybChsPriorityLevel.id
      if (ybChsPriorityLevel.personType === 4) {
        this.checkPersonType = true
      } else {
        this.checkPersonType = false
      }
      if (ybChsPriorityLevel.deptType === 4) {
        this.checkDeptType = true
      } else {
        this.checkDeptType = false
      }
      if (ybChsPriorityLevel.isRule === 1) {
        this.checkRuleType = true
      } else {
        this.checkRuleType = false
      }
      if (ybChsPriorityLevel.isProject === 1) {
        this.checkProjectType = true
      } else {
        this.checkProjectType = false
      }
      this.form.getFieldDecorator('zymzType')
      this.form.getFieldDecorator('plType')
      this.form.getFieldDecorator('personType')
      this.form.getFieldDecorator('deptType')
      this.form.getFieldDecorator('isRule')
      this.form.getFieldDecorator('isProject')
      this.form.setFieldsValue({
        zymzType: ybChsPriorityLevel.zymzType.toString(),
        plType: ybChsPriorityLevel.plType.toString(),
        personType: ybChsPriorityLevel.personType.toString(),
        deptType: ybChsPriorityLevel.deptType.toString(),
        isRule: ybChsPriorityLevel.isRule.toString(),
        isProject: ybChsPriorityLevel.isProject.toString()
      })
      setTimeout(() => {
        if (ybChsPriorityLevel.dksIdTo !== '' && ybChsPriorityLevel.dksIdTo !== null) {
          this.$refs.inputSelectDks.dataSource = [{
            text: ybChsPriorityLevel.dksIdTo + '-' + ybChsPriorityLevel.dksNameTo,
            value: ybChsPriorityLevel.dksIdTo
          }]
        }
        this.$refs.inputSelectDks.value = ybChsPriorityLevel.dksIdTo

        if (ybChsPriorityLevel.dksId !== '' && ybChsPriorityLevel.dksId !== null) {
          this.$refs.inputSelectThDks.dataSource = [{
            text: ybChsPriorityLevel.dksId + '-' + ybChsPriorityLevel.dksName,
            value: ybChsPriorityLevel.dksId
          }]
          this.checkThDeptType = false
        } else {
          this.checkThDeptType = true
        }
        this.$refs.inputSelectThDks.value = ybChsPriorityLevel.dksId

        if (ybChsPriorityLevel.doctorCodeTo !== '' && ybChsPriorityLevel.doctorCodeTo !== null) {
          this.$refs.inputSelectDoctor.dataSource = [{
            text: ybChsPriorityLevel.doctorCodeTo + '-' + ybChsPriorityLevel.doctorNameTo,
            value: ybChsPriorityLevel.doctorCodeTo
          }]
        }
        this.$refs.inputSelectDoctor.value = ybChsPriorityLevel.doctorCodeTo

        this.ybPriorityLevel.doctorCodeTo = ybChsPriorityLevel.doctorCodeTo
        this.ybPriorityLevel.doctorNameTo = ybChsPriorityLevel.doctorNameTo
        this.ybPriorityLevel.dksIdTo = ybChsPriorityLevel.dksIdTo
        this.ybPriorityLevel.dksNameTo = ybChsPriorityLevel.dksNameTo
      }, 200)
    },
    handleSubmit () {
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
        this.form.getFieldDecorator('dksIdTo')
        this.form.setFieldsValue({
          dksIdTo: this.ybPriorityLevel.dksIdTo
        })
        this.form.getFieldDecorator('dksNameTo')
        this.form.setFieldsValue({
          dksNameTo: this.ybPriorityLevel.dksNameTo
        })
      }
      if (this.ybPriorityLevel.dksId !== '' && this.ybPriorityLevel.dksId !== undefined) {
        this.form.getFieldDecorator('dksId')
        this.form.setFieldsValue({
          dksId: this.ybPriorityLevel.dksId
        })
        this.form.getFieldDecorator('dksName')
        this.form.setFieldsValue({
          dksName: this.ybPriorityLevel.dksName
        })
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let ybChsPriorityLevel = this.form.getFieldsValue()
          ybChsPriorityLevel.id = this.ybChsPriorityLevel.id
          if (ybChsPriorityLevel.doctorCodeTo === '') {
            ybChsPriorityLevel.doctorNameTo = ''
          }
          if (ybChsPriorityLevel.dksIdTo === '') {
            ybChsPriorityLevel.dksNameTo = ''
          }
          if (this.ybChsPriorityLevel.dksId === '') {
            this.ybChsPriorityLevel.dksName = ''
          }
          this.$put('ybChsPriorityLevel', {
            ...ybChsPriorityLevel
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
