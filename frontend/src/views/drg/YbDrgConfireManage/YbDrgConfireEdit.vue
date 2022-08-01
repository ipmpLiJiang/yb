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
            <input-selectdks
            ref="inputSelectDks"
            @selectChange=selectDksChange
            >
            </input-selectdks>
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
      <ybDrgConfire-data
        ref="ybDrgConfireData"
        :pid="ybDrgConfire.id"
        @del="del"
      >
      </ybDrgConfire-data>
    </template>
  </a-drawer>
</template>
<script>
import InputSelect from '../../common/InputSelect'
import InputSelectdks from '../../common/InputSelectDks'
import YbDrgConfireData from './YbDrgConfireData'
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15 }
}
export default {
  name: 'YbDrgConfireEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect, InputSelectdks, YbDrgConfireData },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      ybAcData: {},
      isUpdate: false,
      isEdit: false,
      txtValue: '',
      selectAdminTypeDataSource: [],
      user: this.$store.state.account.user,
      ybDrgConfire: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.ybDrgConfire = {}
      this.ybAcData = {}
      this.$refs.inputSelectDoctor.dataSource = []
      this.$refs.inputSelectDoctor.value = ''
      this.$refs.inputSelectDks.dataSource = []
      this.$refs.inputSelectDks.value = ''
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
    selectDoctorChange (item) {
      this.ybDrgConfire.doctorCode = item.value
      this.ybDrgConfire.doctorName = item.text
    },
    handleAdminTypeChange (value) {
      this.ybDrgConfire.adminType = value
    },
    selectDksChange (item) {
      this.ybAcData.dksId = item.value
      this.ybAcData.dksName = item.text
    },
    setFormValues (obj, areaType, atDataSource) {
      this.ybAcData.dksId = ''
      this.ybAcData.dksName = ''
      this.ybDrgConfire.areaType = areaType
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
          this.$refs.ybDrgConfireData.searchPage('')
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

          this.ybDrgConfire.doctorCode = obj.doctorCode
          this.ybDrgConfire.doctorName = obj.doctorName
          this.ybDrgConfire.id = obj.id
          this.ybDrgConfire.adminType = obj.adminType
          this.$refs.ybDrgConfireData.searchPage(obj.id)
        }, 200)
      }
    },
    handleSubmit () {
      this.isUpdate = true
      let isData = false
      if (this.ybDrgConfire.doctorCode !== '' && this.ybDrgConfire.doctorCode !== undefined) {
        this.form.getFieldDecorator('doctorCode')
        this.form.setFieldsValue({
          doctorCode: this.ybDrgConfire.doctorCode
        })
        this.form.getFieldDecorator('doctorName')
        this.form.setFieldsValue({
          doctorName: this.ybDrgConfire.doctorName
        })
      }
      if (this.ybAcData.dksId !== '' && this.ybAcData.dksId !== undefined) {
        this.ybDrgConfire.child = [
          { dksId: this.ybAcData.dksId, dksName: this.ybAcData.dksName }
        ]
        isData = true
      } else {
        this.ybDrgConfire.child = []
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          let jsonString = JSON.stringify(this.ybDrgConfire)
          if (this.ybDrgConfire.id === undefined || this.ybDrgConfire.id === null || this.ybDrgConfire.id === '') {
            this.$post('ybDrgConfire/addDrgConfire', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                this.ybDrgConfire.id = r.data.data.data
                if (isData) {
                  this.$refs.ybDrgConfireData.searchPage(this.ybDrgConfire.id)
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
            this.$put('ybDrgConfire/updateDrgConfire', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                if (isData) {
                  this.$refs.ybDrgConfireData.searchPage(this.ybDrgConfire.id)
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
        this.$refs.inputSelectDks.dataSource = []
        this.$refs.inputSelectDks.value = ''
        this.ybAcData.dksId = ''
        this.ybAcData.dksName = ''
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['doctorCode', 'doctorName', 'adminType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybDrgConfire[_key] = values[_key] })
      }
    }
  }
}
</script>
