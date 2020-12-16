<template>
  <a-drawer
    title="拒绝"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="rejectVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
  <appealData-module
  ref="appealDataModule"
  :ybAppealDataModule="ybAppealManageReject"
  >
  </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybAppealManageReject"
    >
    </inpatientfees-module>
    <template>
      <p style="color:red;margin-top:20px;">
      {{warningMessage}}</p>
      <div style="margin:0px 0px 20px 0px;padding-bottom:20px;border: 1px solid #e8e8e8;">
        <div style="margin-top:20px;">
        <a-row
          justify="center"
          align="bottom"
        >
        <a-col :span=3>
          &nbsp;
        </a-col>
        <a-col :span=8>
          <a-form-item
            v-bind="{
              labelCol: { span: 7 },
              wrapperCol: { span: 15, offset: 2 }
            }"
            label="复议科室"
          >
            <input-select
            ref="inputSelectChangeDept"
            :type=1
            @selectChange=selectDeptChange
            >
            </input-select>
          </a-form-item>
        </a-col>
        <a-col :span=10>
          <a-form-item
            v-bind="{
              labelCol: { span: 5 },
              wrapperCol: { span: 11, offset: 1 }
            }"
            label="复议医生"
          >
            <input-select
            ref="inputSelectChangeDoctor"
            :type=3
            @selectChange=selectDoctorChang
            >
            </input-select>
          </a-form-item>
        </a-col>
        <a-col :span=2>
          &nbsp;
        </a-col>
        </a-row>
        <a-row
          justify="center"
          align="bottom"
        >
          <a-col :span=18>
            <a-form :form="form">
              <a-form-item
                v-bind="formItemLayout"
                label="拒绝理由："
              >
                <a-textarea
                  placeholder="请输入拒绝理由"
                  :rows="7"
                  v-decorator="['refuseReason', {rules: [{ required: true, message: '拒绝理由不能为空' }] }]"
                />
              </a-form-item>
            </a-form>
          </a-col>
          <a-col :span=6>
            &nbsp;
          </a-col>
        </a-row>
        <a-row
          justify="center"
          align="bottom"
        >
          <a-col :span=15>
            &nbsp;
          </a-col>
          <a-col :span=8>
            <a-popconfirm
              title="确定提交？"
              @confirm="handleSubmit"
              okText="确定"
              cancelText="取消"
            >
              <a-button
                type="primary"
                style="margin-right: .8rem"
              >提交</a-button>
            </a-popconfirm>
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
    </template>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import InputSelect from '../../common/InputSelect'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
const formItemLayout = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16, offset: 1 }
}
export default {
  name: 'YbAppealManageReject',
  components: {
    AppealDataModule, InpatientfeesModule, InputSelect},
  props: {
    rejectVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      ybAppealManageReject: {},
      ybAppealManage: {},
      warningMessage: '非本人复议，请线下确认并 更换 复议科室 和 复议医生',
      form: this.$form.createForm(this)
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
      this.ybAppealManage = {}
      this.ybAppealManageReject = {}
      this.form.resetFields()
    },
    selectDoctorChang (item) {
      this.ybAppealManageReject.changeDoctorCode = item.value
      this.ybAppealManageReject.changeDoctorName = item.text
    },
    selectDeptChange (item) {
      this.ybAppealManageReject.changeDeptCode = item.value
      this.ybAppealManageReject.changeDeptName = item.text
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    warning () {
      this.$warning({
        title: '操作提示',
        content: this.warningMessage + '........'
      })
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          let fromData = this.form.getFieldsValue(['refuseReason'])
          let ybAppealManage = this.ybAppealManage

          let deptName = this.ybAppealManageReject.changeDeptCode !== this.ybAppealManageReject.readyDeptCode ? this.ybAppealManageReject.changeDeptName : ''
          let deptCode = this.ybAppealManageReject.changeDeptCode !== this.ybAppealManageReject.readyDeptCode ? this.ybAppealManageReject.changeDeptCode : ''
          let doctorName = this.ybAppealManageReject.changeDoctorCode !== this.ybAppealManageReject.readyDoctorCode ? this.ybAppealManageReject.changeDoctorName : ''
          let doctorCode = this.ybAppealManageReject.changeDoctorCode !== this.ybAppealManageReject.readyDoctorCode ? this.ybAppealManageReject.changeDoctorCode : ''

          ybAppealManage.acceptState = 2
          ybAppealManage.refuseReason = fromData.refuseReason
          ybAppealManage.changeDoctorCode = deptCode !== '' && (doctorCode === '' || doctorCode === undefined) ? this.ybAppealManageReject.readyDoctorCode : doctorCode
          ybAppealManage.changeDoctorName = deptCode !== '' && (doctorCode === '' || doctorCode === undefined) ? this.ybAppealManageReject.readyDoctorName : doctorName
          ybAppealManage.changeDeptCode = (deptCode === '' || deptCode === undefined) && doctorCode !== '' ? this.ybAppealManageReject.readyDeptCode : deptCode
          ybAppealManage.changeDeptName = (deptCode === '' || deptCode === undefined) && doctorCode !== '' ? this.ybAppealManageReject.readyDeptName : deptName

          if (ybAppealManage.changeDeptCode !== '' && ybAppealManage.changeDeptCode !== undefined) {
            this.loading = true
            let data = [{
              id: ybAppealManage.id,
              sourceType: ybAppealManage.sourceType,
              acceptState: ybAppealManage.acceptState,
              operateReason: ybAppealManage.refuseReason,
              changeDoctorCode: ybAppealManage.changeDoctorCode,
              changeDoctorName: ybAppealManage.changeDoctorName,
              changeDeptCode: ybAppealManage.changeDeptCode,
              changeDeptName: ybAppealManage.changeDeptName
            }]
            let jsonString = JSON.stringify(data)
            this.$put('ybAppealManage/updateAcceptRejectState', {
              dataJson: jsonString
            }).then(() => {
              this.reset()
              this.$emit('success')
            }).catch(() => {
              this.loading = false
            })
          } else {
            this.warning()
          }
        }
      })
    },
    setFormValues ({ ...ybAppealManageReject }) {
      this.ybAppealManageReject = ybAppealManageReject
      this.ybAppealManage.id = ybAppealManageReject.id
      this.ybAppealManage.sourceType = ybAppealManageReject.sourceType
      this.ybAppealManage.acceptState = ybAppealManageReject.acceptState
      this.ybAppealManageReject.changeDoctorCode = ybAppealManageReject.readyDoctorCode
      this.ybAppealManageReject.changeDoctorName = ybAppealManageReject.readyDoctorName
      this.ybAppealManageReject.changeDeptCode = ybAppealManageReject.readyDeptCode
      this.ybAppealManageReject.changeDeptName = ybAppealManageReject.readyDeptName
      setTimeout(() => {
        this.fromSetTimeoutValue(ybAppealManageReject)
      }, 200)
    },
    fromSetTimeoutValue (item) {
      this.$refs.inputSelectChangeDept.dataSource = [{
        text: item.readyDeptCode + '-' + item.readyDeptName,
        value: item.readyDeptCode
      }]
      this.$refs.inputSelectChangeDept.value = item.readyDeptCode

      this.$refs.inputSelectChangeDoctor.dataSource = [{
        text: item.readyDoctorCode + '-' + item.readyDoctorName,
        value: item.readyDoctorCode
      }]
      this.$refs.inputSelectChangeDoctor.value = item.readyDoctorCode
      this.$refs.inpatientfeesModule.search()
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
