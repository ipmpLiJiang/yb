<template>
  <a-drawer
    title="管理员变更详情"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="adminVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <appealData-module
    ref="appealDataModule"
    :ybAppealDataModule="ybAppealManageChangeDetail"
    >
    </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageChangeDetail"
    >
    </inpatientfees-module>
    <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
    <div style="margin-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
    <div style="margin-top:20px;margin-left:20px;">
      <template>
        <a-form :form="form">
        <a-row type="flex" justify="start">
          <a-col :span=24>
            <a-row>
              <a-col :span=24>
                <a-form-item
                  v-bind="formItemLayout"
                  label="变更人"
                >
                {{changePersons}}
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout1"
                  label="复议科室"
                >
                  <input-select
                  ref="inputSelectVerifyDept"
                  :type=1
                  @selectChange=selectDeptChang
                  >
                  </input-select>
                </a-form-item>
              </a-col>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout1"
                  label="复议医生"
                >
                  <input-select
                  ref="inputSelectVerifyDoctor"
                  :type=2
                  @selectChange=selectDoctorChang
                  >
                  </input-select>
                </a-form-item>
              </a-col>
              <a-col :span=4>
               &nbsp;
              </a-col>
            </a-row>
          </a-col>
        </a-row>
        </a-form>
      </template>
      <a-row>
        <a-col :span=4 :offset=8>
          <a-popconfirm
            title="确定提交？"
            @confirm="handleSubmit"
            okText="确定"
            cancelText="取消"
          >
            <a-button type="primary" style="margin-right: .8rem">提交</a-button>
          </a-popconfirm>
        </a-col>
        <a-col :span=12>
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
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 20 }
}
const formItemLayout1 = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'YbAppealManageChangeDetail',
  components: {
    AppealDataModule, InpatientfeesModule, InputSelect},
  props: {
    adminVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      form: this.$form.createForm(this),
      ybAppealManageChangeDetail: {},
      ybAppealManage: {},
      formItemLayout,
      formItemLayout1,
      spinning: false,
      delayTime: 500,
      changePersons: ''
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
      this.ybAppealManageChangeDetail = {}
      this.form.resetFields()
    },
    onClose () {
      this.ybAppealManageChangeDetail = {}
      this.ybAppealManage = {}
      this.$emit('close')
    },
    selectDoctorChang (item) {
      this.ybAppealManage.readyDoctorCode = item.value
      this.ybAppealManage.readyDoctorName = item.text
    },
    selectDeptChang (item) {
      this.ybAppealManage.readyDeptCode = item.value
      this.ybAppealManage.readyDeptName = item.text
    },
    setFormValues (ybAppealManageChangeDetail) {
      this.ybAppealManageChangeDetail = ybAppealManageChangeDetail
      this.ybAppealManage.id = ybAppealManageChangeDetail.id
      this.ybAppealManage.sourceType = ybAppealManageChangeDetail.sourceType
      this.ybAppealManage.dataType = ybAppealManageChangeDetail.dataType
      this.ybAppealManage.verifyId = ybAppealManageChangeDetail.verifyId
      this.ybAppealManage.applyDataId = ybAppealManageChangeDetail.applyDataId
      this.ybAppealManage.acceptState = ybAppealManageChangeDetail.acceptState
      this.changePersons = this.ybAppealManageChangeDetail.readyDeptCode + '-' + this.ybAppealManageChangeDetail.readyDeptName + ' - ' + this.ybAppealManageChangeDetail.readyDoctorCode + '-' + this.ybAppealManageChangeDetail.readyDoctorName
      setTimeout(() => {
        this.setForms(ybAppealManageChangeDetail)
      }, 200)
    },
    setForms (target) {
      this.$refs.inputSelectVerifyDept.dataSource = [{
        text: target.readyDeptCode + '-' + target.readyDeptName,
        value: target.readyDeptCode
      }]
      this.$refs.inputSelectVerifyDept.value = target.readyDeptCode

      this.$refs.inputSelectVerifyDoctor.dataSource = [{
        text: target.readyDoctorCode + '-' + target.readyDoctorName,
        value: target.readyDoctorCode
      }]
      this.$refs.inputSelectVerifyDoctor.value = target.readyDoctorCode

      this.ybAppealManage.readyDoctorCode = target.readyDoctorCode
      this.ybAppealManage.readyDoctorName = target.readyDoctorName

      this.ybAppealManage.readyDeptCode = target.readyDeptCode
      this.ybAppealManage.readyDeptName = target.readyDeptName

      this.$refs.inpatientfeesModule.search()
    },
    handleSubmit () {
      this.loading = true
      this.spinning = true
      let ybAppealManage = this.ybAppealManage
      // ybAppealManage.id = this.ybAppealManageChangeDetail.id
      // ybAppealManage.sourceType = this.ybAppealManageChangeDetail.sourceType
      // ybAppealManage.verifyId = this.ybAppealManageChangeDetail.verifyId
      // ybAppealManage.applyDataId = this.ybAppealManageChangeDetail.applyDataId
      // ybAppealManage.readyDoctorCode = this.ybAppealManageChangeDetail.readyDoctorCode
      // ybAppealManage.readyDoctorName = this.ybAppealManageChangeDetail.readyDoctorName
      // ybAppealManage.readyDeptCode = this.ybAppealManageChangeDetail.readyDeptCode
      // ybAppealManage.readyDeptName = this.ybAppealManageChangeDetail.readyDeptName
      if (ybAppealManage.readyDoctorCode === this.ybAppealManageChangeDetail.readyDoctorCode &&
          ybAppealManage.readyDeptCode === this.ybAppealManageChangeDetail.readyDeptCode) {
        this.$message.error('未更改 复议科室 和 复议医生 , 不可提交数据.')
        this.loading = false
        this.spinning = false
        return
      }

      this.$put('ybAppealManage/updateCreateAdminAppealManage', {
        ...ybAppealManage
      }).then(() => {
        this.reset()
        this.$emit('success')
      }).catch(() => {
        this.loading = false
        this.spinning = false
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
