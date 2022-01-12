<template>
  <a-drawer
    title="编辑"
    :maskClosable="false"
    width=65%
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=10>
          <a-form-item
            v-bind="formItemLayout"
            label="职工"
          >
            <input-select
              ref="inputSelectDoctor"
              v-show="isEdit?false:true"
              v-decorator="['doctorCode', {rules: [{required: true, message: '职工不能为空' }] }]"
              :type=2
              @selectChange=selectDoctorChange
            >
            </input-select>
            <p v-show="!isEdit?false:true">
              {{txtValue}}
            </p>
          </a-form-item>
        </a-col>
        <a-col :span=14>
          <a-form-item
            label="管理员类型"
            v-bind="formItemLayout"
          >
            <a-select v-decorator="['adminType']" style="width: 150px" @change="handleAdminTypeChange"
              >
                <a-select-option
                v-for="d in selectAdminTypeDataSource"
                :key="d.value"
                >
                {{ d.text }}
                </a-select-option>
              </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=10>
          <a-form-item
            v-bind="formItemLayout"
            label="管理科室列表"
          >
            <a-select
              allowClear
              :showSearch="true"
              @change="selectKsTypeChange"
              v-model="vdksName"
            >
              <a-select-option :value="d.text" v-for="d in ksList" :key="d.text">
                {{d.text}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span=3>
          <a-button
            @click="handleSubmit"
            type="primary"
          >添加科室/保存</a-button>
        </a-col>
        <a-col :span=11>
          <a-popconfirm
            title="确定返回列表？"
            @confirm="onClose"
            okText="确定"
            cancelText="取消"
          >
            <a-button style="margin-right: .8rem">返回列表</a-button>
          </a-popconfirm>
        </a-col>
      </a-row>
    </a-form>
    <!--表格-->
    <template>
      <ybAppealConfire-data
        ref="ybAppealConfireData"
        :pid="ybAppealConfire.id"
      >
      </ybAppealConfire-data>
    </template>
  </a-drawer>
</template>
<script>
import InputSelect from '../../common/InputSelect'
import YbAppealConfireData from './YbAppealConfireData'
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15 }
}
export default {
  name: 'YbAppealConfireEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect, YbAppealConfireData },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      ybAcData: {},
      isUpdate: false,
      isEdit: false,
      txtValue: '',
      ksList: [],
      vdksName: '',
      selectAdminTypeDataSource: [],
      ybAppealConfire: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybAppealConfire = {}
      this.ksList = []
      this.$refs.inputSelectDoctor.dataSource = []
      this.$refs.inputSelectDoctor.value = ''
      // this.$refs.inputSelectDept.dataSource = []
      // this.$refs.inputSelectDept.value = ''
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      if (this.isUpdate) {
        this.$emit('success')
      } else {
        this.$emit('close')
      }
    },
    findComType3 () {
      this.ksList = []
      this.$get('ybAppealConfireData/findAppealConfireDataList', {
        areaType: this.ybAppealConfire.areaType
      }).then((r) => {
        if (r.data.data.length > 0) {
          for (var i in r.data.data) {
            var at = {text: r.data.data[i].dksName}
            this.ksList.push(at)
          }
        }
      }
      )
    },
    handleAdminTypeChange (value) {
      this.ybAppealConfire.adminType = value
    },
    selectDoctorChange (item) {
      this.ybAppealConfire.doctorCode = item.value
      this.ybAppealConfire.doctorName = item.text
    },
    selectDeptChange (item) {
      this.ybAcData.deptId = item.value
      this.ybAcData.deptName = item.text
    },
    selectKsTypeChange (value) {
      this.ybAcData.dksName = value
    },
    setFormValues (obj, areaType, atDataSource) {
      this.ybAcData.dksName = ''
      this.ybAppealConfire.areaType = areaType
      this.findComType3()
      this.isUpdate = false
      this.selectAdminTypeDataSource = atDataSource
      this.form.getFieldDecorator('adminType')
      if (obj === undefined || obj === null || obj === '') {
        this.isEdit = false
        if (this.selectAdminTypeDataSource.length > 0) {
          this.form.setFieldsValue({
            adminType: this.selectAdminTypeDataSource[0].value
          })
        }
        this.$refs.ybAppealConfireData.searchPage('')
      } else {
        this.txtValue = obj.doctorCode + '-' + obj.doctorName
        this.isEdit = true
        this.form.setFieldsValue({
          adminType: obj.adminType
        })
        setTimeout(() => {
          if (obj.doctorCode !== '' && obj.doctorCode !== null) {
            this.$refs.inputSelectDoctor.dataSource = [{
              text: obj.doctorCode + '-' + obj.doctorName,
              value: obj.doctorCode
            }]
          }
          this.$refs.inputSelectDoctor.value = obj.doctorCode

          this.ybAppealConfire.doctorCode = obj.doctorCode
          this.ybAppealConfire.doctorName = obj.doctorName
          this.ybAppealConfire.id = obj.id
          this.ybAppealConfire.adminType = obj.adminType
          this.$refs.ybAppealConfireData.searchPage(obj.id)
        }, 200)
      }
    },
    handleSubmit () {
      this.isUpdate = true
      let isData = false
      if (this.ybAppealConfire.doctorCode !== '' && this.ybAppealConfire.doctorCode !== undefined) {
        this.form.getFieldDecorator('doctorCode')
        this.form.setFieldsValue({
          doctorCode: this.ybAppealConfire.doctorCode
        })
        this.form.getFieldDecorator('doctorName')
        this.form.setFieldsValue({
          doctorName: this.ybAppealConfire.doctorName
        })
      }
      // if (this.ybAcData.deptId !== '' && this.ybAcData.deptId !== undefined) {
      if (this.ybAcData.dksName !== '' && this.ybAcData.dksName !== undefined) {
        this.ybAppealConfire.child = [
          // { deptId: this.ybAcData.deptId, deptName: this.ybAcData.deptName }
          { dksName: this.ybAcData.dksName }
        ]
        isData = true
      } else {
        this.ybAppealConfire.child = []
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          let jsonString = JSON.stringify(this.ybAppealConfire)
          if (this.ybAppealConfire.id === undefined || this.ybAppealConfire.id === null || this.ybAppealConfire.id === '') {
            this.$post('ybAppealConfire/addAppealConfire', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                this.ybAppealConfire.id = r.data.data.data
                if (isData) {
                  this.$refs.ybAppealConfireData.searchPage(this.ybAppealConfire.id)
                }
                this.$message.success('保存成功')
              } else {
                this.$message.error(r.data.data.message)
              }
            }).catch(() => {
              this.loading = false
              this.$message.success('保存失败')
            })
          } else {
            this.$put('ybAppealConfire/updateAppealConfire', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                if (isData) {
                  this.$refs.ybAppealConfireData.searchPage(this.ybAppealConfire.id)
                }
                this.$message.success('保存成功')
              } else {
                this.$message.error(r.data.data.message)
              }
            }).catch(() => {
              this.loading = false
              this.$message.success('保存失败')
            })
          }
        }
        // this.$refs.inputSelectDept.dataSource = []
        // this.$refs.inputSelectDept.value = ''
        // this.ybAcData.deptId = ''
        // this.ybAcData.deptName = ''
        this.ybAcData.dksName = ''
        this.vdksName = ''
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['doctorCode', 'doctorName', 'adminType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybAppealConfire[_key] = values[_key] })
      }
    }
  }
}
</script>
