<template>
  <a-drawer
    title="申诉填报"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="handleVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <appealData-module
    ref="appealDataModule"
    :ybAppealDataModule="ybAppealManageChangeHandle"
    >
    </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageChangeHandle"
    >
    </inpatientfees-module>
    <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <div style="margin-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
        <div style="margin-top:20px;margin-left:20px;">
          <template>
            <a-form :form="form">
              <a-row type="flex" justify="start">
                <a-col :span=12>
                  <a-row>
                    <a-col :span=10>
                      <a-form-item
                        v-bind="formItemLayout0"
                        label="申请科室"
                      >
                        {{ybAppealManageChangeHandle.readyDeptCode}}-{{ybAppealManageChangeHandle.readyDeptName}}
                      </a-form-item>
                    </a-col>
                    <a-col :span=14>
                      <a-form-item
                        v-bind="formItemLayout"
                        label="变更为"
                      >
                        <input-select
                        ref="inputSelectChangeDept"
                        :type=1
                        @selectChange=selectDeptChange
                        >
                        </input-select>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span=10>
                      <a-form-item
                        v-bind="formItemLayout0"
                        label="申请人"
                      >
                        {{ybAppealManageChangeHandle.readyDoctorCode}}-{{ybAppealManageChangeHandle.readyDoctorName}}
                      </a-form-item>
                    </a-col>
                    <a-col :span=14>
                      <a-form-item
                        v-bind="formItemLayout"
                        label="变更为"
                      >
                        <input-select
                        ref="inputSelectChangeDoctor"
                        :type=2
                        dept='医生'
                        @selectChange=selectDoctorChang
                        >
                        </input-select>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row type="flex" justify="start">
                    <a-col :span=24>
                      <a-form-item
                        v-bind="formItemLayout1"
                        label="申请时间"
                      >
                        {{ybAppealManageChangeHandle.operateDate}}
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row type="flex" justify="start">
                    <a-col :span=24>
                      <a-form-item
                        v-bind="formItemLayout1"
                        label="申请理由"
                      >
                        {{ybAppealManageChangeHandle.operateReason}}
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-col>
                <a-col :span=12>
                  <a-form-item
                      v-bind="formItemLayout2"
                      label="拒绝理由"
                    >
                      <a-textarea
                        placeholder="请输入拒绝理由"
                        :rows="7"
                        v-decorator="['refuseReason', {rules: [{ required: true, message: '拒绝理由不能为空' }] }]"
                      />
                    </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </template>
          <a-row>
            <a-col :span=4 :offset=8>
              <a-popconfirm
                title="确定同意？"
                @confirm="handleSubmit"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary" style="margin-right: .8rem">同意</a-button>
              </a-popconfirm>
            </a-col>
            <a-col :span=2>
              <a-popconfirm
                title="确定拒绝？"
                @confirm="handleRejectSubmit"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary" style="margin-right: .8rem">拒绝</a-button>
              </a-popconfirm>
            </a-col>
            <a-col :span=10>
              <a-popconfirm
                title="确定放弃编辑？"
                @confirm="onClose"
                okText="确定"
                cancelText="取消"
              >
                <a-button style="margin-right: .8rem">取消</a-button>
              </a-popconfirm>
            </a-col>
          </a-row>
        </div>
        </div>
      </div>
    </a-spin>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import InputSelect from '../../common/InputSelect'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
const formItemLayout0 = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15 }
}
const formItemLayout = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16 }
}
const formItemLayout1 = {
  labelCol: { span: 3 },
  wrapperCol: { span: 20 }
}
const formItemLayout2 = {
  labelCol: { span: 4 },
  wrapperCol: { span: 19 }
}
export default {
  name: 'YbAppealManageChangeHandle',
  components: {
    AppealDataModule, InpatientfeesModule, InputSelect},
  props: {
    handleVisiable: {
      default: false
    }
  },
  data () {
    return {
      form: this.$form.createForm(this),
      loading: false,
      formItemLayout0,
      formItemLayout,
      formItemLayout1,
      formItemLayout2,
      spinning: false,
      delayTime: 500,
      user: this.$store.state.account.user,
      ybAppealManageChangeHandle: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.spinning = false
      this.ybAppealManageChangeHandle = {}
      this.form.resetFields()
    },
    onClose () {
      this.ybAppealManageChangeHandle = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybAppealManageChangeHandle }) {
      this.ybAppealManageChangeHandle = ybAppealManageChangeHandle
      setTimeout(() => {
        this.setForms(ybAppealManageChangeHandle)
      }, 200)
    },
    setForms (amch) {
      let deptName = amch.changeDeptCode !== '' && amch.changeDeptCode !== undefined ? amch.changeDeptName : amch.readyDeptName
      let deptCode = amch.changeDeptCode !== '' && amch.changeDeptCode !== undefined ? amch.changeDeptCode : amch.readyDeptCode
      let doctorName = amch.changeDoctorCode !== '' && amch.changeDoctorCode !== undefined ? amch.changeDoctorName : amch.readyDoctorName
      let doctorCode = amch.changeDoctorCode !== '' && amch.changeDoctorCode !== undefined ? amch.changeDoctorCode : amch.readyDoctorCode

      this.$refs.inputSelectChangeDept.dataSource = [{
        text: deptCode + '-' + deptName,
        value: deptCode
      }]
      this.$refs.inputSelectChangeDept.value = deptCode

      this.$refs.inputSelectChangeDoctor.dataSource = [{
        text: doctorCode + '-' + doctorName,
        value: doctorCode
      }]
      this.$refs.inputSelectChangeDoctor.value = doctorCode

      this.ybAppealManageChangeHandle.changeDoctorCode = doctorCode
      this.ybAppealManageChangeHandle.changeDoctorName = doctorName
      this.ybAppealManageChangeHandle.changeDeptCode = deptCode
      this.ybAppealManageChangeHandle.changeDeptName = deptName

      this.$refs.inpatientfeesModule.search()
    },
    selectDoctorChang (item) {
      this.ybAppealManageChangeHandle.changeDoctorCode = item.value
      this.ybAppealManageChangeHandle.changeDoctorName = item.text
    },
    selectDeptChange (item) {
      this.ybAppealManageChangeHandle.changeDeptCode = item.value
      this.ybAppealManageChangeHandle.changeDeptName = item.text
    },
    handleRejectSubmit () {
      this.form.validateFields(['refuseReason'], (err, values) => {
        if (!err) {
          this.loading = true
          this.spinning = true
          let fromReason = this.form.getFieldsValue(['refuseReason'])
          let ybAppealManage = {}
          ybAppealManage.acceptState = 4
          ybAppealManage.sourceType = this.ybAppealManageChangeHandle.sourceType
          ybAppealManage.dataType = this.ybAppealManageChangeHandle.dataType
          ybAppealManage.refuseReason = fromReason.refuseReason
          ybAppealManage.id = this.ybAppealManageChangeHandle.id
          ybAppealManage.applyDataId = this.ybAppealManageChangeHandle.applyDataId
          ybAppealManage.verifyId = this.ybAppealManageChangeHandle.verifyId
          ybAppealManage.readyDeptCode = this.ybAppealManageChangeHandle.readyDeptCode
          ybAppealManage.readyDeptName = this.ybAppealManageChangeHandle.readyDeptName
          ybAppealManage.readyDoctorCode = this.ybAppealManageChangeHandle.readyDoctorCode
          ybAppealManage.readyDoctorName = this.ybAppealManageChangeHandle.readyDoctorName
          ybAppealManage.areaType = this.user.areaType.value
          this.$put('ybAppealManage/updateCreateAppealManage', {
            ...ybAppealManage, type: 2
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
            this.spinning = false
          })
        }
      })
    },
    handleSubmit () {
      this.form.validateFields(['changeDeptName', 'changeDoctorName'], (err, values) => {
        if (!err) {
          this.loading = true
          this.spinning = true
          let ybAppealManage = {}
          ybAppealManage.acceptState = 4
          ybAppealManage.sourceType = this.ybAppealManageChangeHandle.sourceType
          ybAppealManage.id = this.ybAppealManageChangeHandle.id
          ybAppealManage.dataType = this.ybAppealManageChangeHandle.dataType
          ybAppealManage.applyDataId = this.ybAppealManageChangeHandle.applyDataId
          ybAppealManage.verifyId = this.ybAppealManageChangeHandle.verifyId
          ybAppealManage.changeDeptCode = this.ybAppealManageChangeHandle.changeDeptCode
          ybAppealManage.changeDeptName = this.ybAppealManageChangeHandle.changeDeptName
          ybAppealManage.changeDoctorCode = this.ybAppealManageChangeHandle.changeDoctorCode
          ybAppealManage.changeDoctorName = this.ybAppealManageChangeHandle.changeDoctorName
          ybAppealManage.readyDeptCode = this.ybAppealManageChangeHandle.readyDeptCode
          ybAppealManage.readyDeptName = this.ybAppealManageChangeHandle.readyDeptName
          ybAppealManage.readyDoctorCode = this.ybAppealManageChangeHandle.readyDoctorCode
          ybAppealManage.readyDoctorName = this.ybAppealManageChangeHandle.readyDoctorName
          ybAppealManage.areaType = this.user.areaType.value
          this.$put('ybAppealManage/updateCreateAppealManage', {
            ...ybAppealManage, type: 1
          }).then(() => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
            this.spinning = false
          })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
