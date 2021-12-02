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
  <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgManageReject"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgManageReject"
    >
    </ybDrgJk-module>
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
            :type=2
            dept='医生'
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
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'
const formItemLayout = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16, offset: 1 }
}
export default {
  name: 'YbDrgManageReject',
  components: {
    YbDrgDataModule, YbDrgJkModule, InputSelect},
  props: {
    rejectVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      ybDrgManageReject: {},
      ybDrgManage: {},
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
      this.ybDrgManage = {}
      this.ybDrgManageReject = {}
      this.form.resetFields()
    },
    selectDoctorChang (item) {
      this.ybDrgManageReject.changeDoctorCode = item.value
      this.ybDrgManageReject.changeDoctorName = item.text
    },
    selectDeptChange (item) {
      this.ybDrgManageReject.changeDeptCode = item.value
      this.ybDrgManageReject.changeDeptName = item.text
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
          let ybDrgManage = this.ybDrgManage

          let deptName = this.ybDrgManageReject.changeDeptCode !== this.ybDrgManageReject.readyDeptCode ? this.ybDrgManageReject.changeDeptName : ''
          let deptCode = this.ybDrgManageReject.changeDeptCode !== this.ybDrgManageReject.readyDeptCode ? this.ybDrgManageReject.changeDeptCode : ''
          let doctorName = this.ybDrgManageReject.changeDoctorCode !== this.ybDrgManageReject.readyDoctorCode ? this.ybDrgManageReject.changeDoctorName : ''
          let doctorCode = this.ybDrgManageReject.changeDoctorCode !== this.ybDrgManageReject.readyDoctorCode ? this.ybDrgManageReject.changeDoctorCode : ''

          ybDrgManage.state = 2
          ybDrgManage.refuseReason = fromData.refuseReason
          ybDrgManage.changeDoctorCode = deptCode !== '' && (doctorCode === '' || doctorCode === undefined) ? this.ybDrgManageReject.readyDoctorCode : doctorCode
          ybDrgManage.changeDoctorName = deptCode !== '' && (doctorCode === '' || doctorCode === undefined) ? this.ybDrgManageReject.readyDoctorName : doctorName
          ybDrgManage.changeDeptCode = (deptCode === '' || deptCode === undefined) && doctorCode !== '' ? this.ybDrgManageReject.readyDeptCode : deptCode
          ybDrgManage.changeDeptName = (deptCode === '' || deptCode === undefined) && doctorCode !== '' ? this.ybDrgManageReject.readyDeptName : deptName

          if (ybDrgManage.changeDeptCode !== '' && ybDrgManage.changeDeptCode !== undefined) {
            this.loading = true
            let data = [{
              id: ybDrgManage.id,
              state: ybDrgManage.state,
              operateReason: ybDrgManage.refuseReason,
              changeDoctorCode: ybDrgManage.changeDoctorCode,
              changeDoctorName: ybDrgManage.changeDoctorName,
              changeDeptCode: ybDrgManage.changeDeptCode,
              changeDeptName: ybDrgManage.changeDeptName
            }]
            let jsonString = JSON.stringify(data)
            this.$put('ybDrgManage/updateAcceptRejectState', {
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
    setFormValues ({ ...ybDrgManageReject }) {
      this.ybDrgManageReject = ybDrgManageReject
      this.ybDrgManage.id = ybDrgManageReject.id
      this.ybDrgManage.state = ybDrgManageReject.state
      this.ybDrgManageReject.changeDoctorCode = ybDrgManageReject.readyDoctorCode
      this.ybDrgManageReject.changeDoctorName = ybDrgManageReject.readyDoctorName
      this.ybDrgManageReject.changeDeptCode = ybDrgManageReject.readyDeptCode
      this.ybDrgManageReject.changeDeptName = ybDrgManageReject.readyDeptName
      setTimeout(() => {
        this.fromSetTimeoutValue(ybDrgManageReject)
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
      this.$refs.ybDrgJkModule.search()
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
