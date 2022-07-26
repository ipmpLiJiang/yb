<template>
  <a-drawer
    title="拒绝"
    :maskClosable="false"
    width=82%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="rejectVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
  <ybChsData-module
    ref="ybChsDataModule"
    :ybChsData="ybChsManageReject"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsManageReject"
    >
    </ybChsJk-module>
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
            <inputSelectChs-dks
            ref="inputSelectChangeDks"
            @selectChange=selectDksChange
            >
            </inputSelectChs-dks>
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
import InputSelectChsDks from '../../common/InputSelectChsDks'
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
const formItemLayout = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16, offset: 1 }
}
export default {
  name: 'YbChsManageReject',
  components: {
    YbChsDataModule, YbChsJkModule, InputSelect, InputSelectChsDks},
  props: {
    rejectVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      ybChsManageReject: {},
      ybChsManage: {},
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
      this.ybChsManage = {}
      this.ybChsManageReject = {}
      this.form.resetFields()
    },
    selectDoctorChang (item) {
      this.ybChsManageReject.changeDoctorCode = item.value
      this.ybChsManageReject.changeDoctorName = item.text
    },
    selectDksChange (item) {
      this.ybChsManageReject.changeDksId = item.value
      this.ybChsManageReject.changeDksName = item.text
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
          let ybChsManage = this.ybChsManage

          let dksId = this.ybChsManageReject.changeDksId !== this.ybChsManageReject.readyDksId ? this.ybChsManageReject.changeDksId : ''
          let dksName = this.ybChsManageReject.changeDksName !== this.ybChsManageReject.readyDksName ? this.ybChsManageReject.changeDksName : ''
          let doctorName = this.ybChsManageReject.changeDoctorCode !== this.ybChsManageReject.readyDoctorCode ? this.ybChsManageReject.changeDoctorName : ''
          let doctorCode = this.ybChsManageReject.changeDoctorCode !== this.ybChsManageReject.readyDoctorCode ? this.ybChsManageReject.changeDoctorCode : ''

          ybChsManage.state = 2
          ybChsManage.refuseReason = fromData.refuseReason
          ybChsManage.changeDoctorCode = dksId !== '' && dksName !== '' && (doctorCode === '' || doctorCode === undefined) ? this.ybChsManageReject.readyDoctorCode : doctorCode
          ybChsManage.changeDoctorName = dksId !== '' && dksName !== '' && (doctorCode === '' || doctorCode === undefined) ? this.ybChsManageReject.readyDoctorName : doctorName
          ybChsManage.changeDksId = (dksId === '' || dksId === undefined) && doctorCode !== '' ? this.ybChsManageReject.readyDksId : dksId
          ybChsManage.changeDksName = (dksName === '' || dksName === undefined) && doctorCode !== '' ? this.ybChsManageReject.readyDksName : dksName

          if (ybChsManage.changeDksId !== '' && ybChsManage.changeDksId !== undefined) {
            this.loading = true
            let data = [{
              id: ybChsManage.id,
              state: ybChsManage.state,
              operateReason: ybChsManage.refuseReason,
              changeDoctorCode: ybChsManage.changeDoctorCode,
              changeDoctorName: ybChsManage.changeDoctorName,
              changeDksId: ybChsManage.changeDksId,
              changeDksName: ybChsManage.changeDksName
            }]
            let jsonString = JSON.stringify(data)
            this.$put('ybChsManage/updateAcceptRejectState', {
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
    setFormValues ({ ...ybChsManageReject }) {
      this.ybChsManageReject = ybChsManageReject
      this.ybChsManage.id = ybChsManageReject.id
      this.ybChsManage.state = ybChsManageReject.state
      this.ybChsManageReject.changeDoctorCode = ybChsManageReject.readyDoctorCode
      this.ybChsManageReject.changeDoctorName = ybChsManageReject.readyDoctorName
      this.ybChsManageReject.changeDksId = ybChsManageReject.readyDksId
      this.ybChsManageReject.changeDksName = ybChsManageReject.readyDksName
      setTimeout(() => {
        this.fromSetTimeoutValue(ybChsManageReject)
      }, 200)
    },
    fromSetTimeoutValue (item) {
      this.$refs.inputSelectChangeDks.dataSource = [{
        text: item.readyDksId + '-' + item.readyDksName,
        value: item.readyDksId
      }]
      this.$refs.inputSelectChangeDks.value = item.readyDksId

      this.$refs.inputSelectChangeDoctor.dataSource = [{
        text: item.readyDoctorCode + '-' + item.readyDoctorName,
        value: item.readyDoctorCode
      }]
      this.$refs.inputSelectChangeDoctor.value = item.readyDoctorCode
      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
