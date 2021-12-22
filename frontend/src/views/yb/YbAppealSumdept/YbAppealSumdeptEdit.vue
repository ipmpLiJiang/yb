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
            label="汇总科室"
          >
            <a-input
              placeholder="请输入汇总科室"
              v-decorator="['asName', {rules: [{ required: true, message: '汇总科室不能为空' }] }]"
            />
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
            <input-select
              ref="inputSelectDept"
              :type=1
              @selectChange=selectDeptChange
            >
            </input-select>
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
      <ybAppealSumdept-data
        ref="ybAppealSumdeptData"
        :pid="ybAppealSumdept.id"
      >
      </ybAppealSumdept-data>
    </template>
  </a-drawer>
</template>
<script>
import InputSelect from '../../common/InputSelect'
import YbAppealSumdeptData from './YbAppealSumdeptData'
const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'ybAppealSumdeptEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { InputSelect, YbAppealSumdeptData },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      ybAcData: {},
      isUpdate: false,
      isEdit: false,
      txtValue: '',
      ybAppealSumdept: {}
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.txtValue = ''
      this.ybAppealSumdept = {}
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
    selectDeptChange (item) {
      this.ybAcData.deptId = item.value
      this.ybAcData.deptName = item.text
    },
    setFormValues (obj, areaType) {
      this.isUpdate = false
      this.form.getFieldDecorator('asName')
      this.ybAppealSumdept.areaType = areaType
      if (obj === undefined || obj === null || obj === '') {
        this.isEdit = false
        setTimeout(() => {
          this.$refs.ybAppealSumdeptData.searchPage('')
        }, 200)
      } else {
        this.isEdit = true
        this.form.setFieldsValue({
          asName: obj.asName
        })
        this.txtValue = obj.asName
        this.ybAppealSumdept.id = obj.id
        setTimeout(() => {
          this.$refs.ybAppealSumdeptData.searchPage(obj.id)
        }, 200)
      }
    },
    handleSubmit () {
      this.isUpdate = true
      let isData = false
      if (this.ybAcData.deptId !== '' && this.ybAcData.deptId !== undefined) {
        this.ybAppealSumdept.child = [
          { deptId: this.ybAcData.deptId, deptName: this.ybAcData.deptName }
        ]
        isData = true
      } else {
        this.ybAppealSumdept.child = []
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          let jsonString = JSON.stringify(this.ybAppealSumdept)
          if (this.ybAppealSumdept.id === undefined || this.ybAppealSumdept.id === null || this.ybAppealSumdept.id === '') {
            this.$post('ybAppealSumdept/addAppealSumdept', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                this.ybAppealSumdept.id = r.data.data.data
                if (isData) {
                  this.$refs.ybAppealSumdeptData.searchPage(this.ybAppealSumdept.id)
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
            this.$put('ybAppealSumdept/updateAppealSumdept', {
              dataJson: jsonString
            }).then((r) => {
              if (r.data.data.success === 1) {
                if (isData) {
                  this.$refs.ybAppealSumdeptData.searchPage(this.ybAppealSumdept.id)
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
        this.ybAcData.deptId = ''
        this.ybAcData.deptName = ''
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['asName'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybAppealSumdept[_key] = values[_key] })
      }
    }
  }
}
</script>
