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
            <a-select v-decorator="['adminType']" style="width: 150px" @change="handleAdminTypeChange">
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
            <inputSelectChs-dks
              ref="inputSelectDept"
              :type=1
              @selectChange=selectDeptChange
            >
            </inputSelectChs-dks>
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-button
            @click="handleSubmit"
            type="primary"
          >添加科室/保存</a-button>
        </a-col>
        <a-col :span=10>
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
      <ybChsConfire-data
        ref="ybChsConfireData"
        :pid="ybChsConfire.id"
        @del="del"
      >
      </ybChsConfire-data>
    </template>
  </a-drawer>
</template>
<script>
import InputSelect from '../../common/InputSelect'
import InputSelectChsDks from '../../common/InputSelectChsDks'
import YbChsConfireData from './YbChsConfireData'
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15 }
}
export default {
  name: 'YbChsConfireEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect, InputSelectChsDks, YbChsConfireData },
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
      // vdksName: '',
      selectAdminTypeDataSource: [],
      user: this.$store.state.account.user,
      ybChsConfire: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ksList = []
      this.ybChsConfire = {}
      this.ybAcData = {}
      this.$refs.inputSelectDoctor.dataSource = []
      this.$refs.inputSelectDoctor.value = ''
      this.$refs.inputSelectDept.dataSource = []
      this.$refs.inputSelectDept.value = ''
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
    del () {
      this.isUpdate = true
    },
    findComType3 (obj) {
      this.findComTypeAdmin3()
      // this.findComTypeUser3()
    },
    findComTypeAdmin3 () {
      let ctParams = {ctType: 3, isDeletemark: 1}
      this.ksList = []
      this.$get('comType/findList', {
        ...ctParams
      }).then((r) => {
        if (r.data.data.length > 0) {
          for (var i in r.data.data) {
            var at = {text: r.data.data[i].ctName}
            this.ksList.push(at)
          }
        }
      }
      )
    },
    findComTypeUser3 () {
      this.ksList = []
      this.$get('ybChsConfireData/findChsConfireDataList', {
        areaType: this.ybChsConfire.areaType
      }).then((r) => {
        if (r.data.data.length > 0) {
          for (var i in r.data.data) {
            // var at = {text: r.data.data[i].dksName}
            var at = {text: r.data.data[i].dksName}
            this.ksList.push(at)
          }
        }
      }
      )
    },
    selectDoctorChange (item) {
      this.ybChsConfire.doctorCode = item.value
      this.ybChsConfire.doctorName = item.personName
    },
    handleAdminTypeChange (value) {
      this.ybChsConfire.adminType = value
    },
    selectDeptChange (item) {
      this.ybAcData.dksId = item.value
      this.ybAcData.dksName = item.dksName
      this.ybAcData.fyid = item.fyid
    },
    setFormValues (obj, areaType, atDataSource) {
      // this.ybAcData.dksName = ''
      this.ybAcData = {}
      this.ybChsConfire.areaType = areaType
      this.findComType3(obj)
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
        setTimeout(() => {
          this.$refs.ybChsConfireData.searchPage('')
        }, 200)
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

          this.ybChsConfire.doctorCode = obj.doctorCode
          this.ybChsConfire.doctorName = obj.doctorName
          this.ybChsConfire.id = obj.id
          this.ybChsConfire.adminType = obj.adminType
          this.$refs.ybChsConfireData.searchPage(obj.id)
        }, 200)
      }
    },
    handleSubmit () {
      this.isUpdate = true
      let isData = false
      if (this.ybChsConfire.doctorCode !== '' && this.ybChsConfire.doctorCode !== undefined) {
        this.form.getFieldDecorator('doctorCode')
        this.form.setFieldsValue({
          doctorCode: this.ybChsConfire.doctorCode
        })
        this.form.getFieldDecorator('doctorName')
        this.form.setFieldsValue({
          doctorName: this.ybChsConfire.doctorName
        })
      }
      if (this.ybAcData.dksId !== '' && this.ybAcData.dksId !== undefined) {
      // if (this.ybAcData.dksName !== '' && this.ybAcData.dksName !== undefined) {
        this.ybChsConfire.child = [
          { dksId: this.ybAcData.dksId, dksName: this.ybAcData.dksName, fyid: this.ybAcData.fyid }
          // { dksName: this.ybAcData.dksName }
        ]
        isData = true
      } else {
        this.ybChsConfire.child = []
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          let jsonString = JSON.stringify(this.ybChsConfire)
          if (this.ybChsConfire.id === undefined || this.ybChsConfire.id === null || this.ybChsConfire.id === '') {
            this.$post('ybChsConfire/addChsConfire', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                this.ybChsConfire.id = r.data.data.data
                if (isData) {
                  this.$refs.ybChsConfireData.searchPage(this.ybChsConfire.id)
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
            this.$put('ybChsConfire/updateChsConfire', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                if (isData) {
                  this.$refs.ybChsConfireData.searchPage(this.ybChsConfire.id)
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
        this.$refs.inputSelectDept.dataSource = []
        this.$refs.inputSelectDept.value = ''
        // this.ybAcData.dksName = ''
        // this.vdksName = ''
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['doctorCode', 'doctorName', 'adminType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybChsConfire[_key] = values[_key] })
      }
    }
  }
}
</script>
