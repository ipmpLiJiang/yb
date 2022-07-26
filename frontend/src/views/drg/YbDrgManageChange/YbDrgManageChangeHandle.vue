<template>
  <a-drawer
    title="DRG申诉填报"
    :maskClosable="false"
    width=82%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="handleVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgManageChangeHandle"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgManageChangeHandle"
    >
    </ybDrgJk-module>
    <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <div style="margin-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
        <div style="margin-top:20px;margin-left:20px;">
          <template>
            <a-form :form="form">
              <a-row type="flex" justify="start">
                <a-col :span=14>
                  <a-row>
                    <a-col :span=10>
                      <a-form-item
                        v-bind="formItemLayout0"
                        label="申请科室"
                      >
                        {{ybDrgManageChangeHandle.readyDksId}}-{{ybDrgManageChangeHandle.readyDksName}}
                      </a-form-item>
                    </a-col>
                    <a-col :span=14>
                      <a-form-item
                        v-bind="formItemLayout"
                        label="变更为"
                      >
                        <input-selectdks
                        ref="inputSelectChangeDks"
                        @selectChange=selectDksChange
                        >
                        </input-selectdks>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span=10>
                      <a-form-item
                        v-bind="formItemLayout0"
                        label="申请人"
                      >
                        {{ybDrgManageChangeHandle.readyDoctorCode}}-{{ybDrgManageChangeHandle.readyDoctorName}}
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
                        {{ybDrgManageChangeHandle.operateDate}}
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row type="flex" justify="start">
                    <a-col :span=24>
                      <a-form-item
                        v-bind="formItemLayout1"
                        label="医院意见"
                      >
                        {{ybDrgManageChangeHandle.operateReason}}
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-col>
                <a-col :span=10>
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
import InputSelectdks from '../../common/InputSelectDks'
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'
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
  name: 'YbDrgManageChangeHandle',
  components: {
    YbDrgDataModule, YbDrgJkModule, InputSelect, InputSelectdks},
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
      ybDrgManageChangeHandle: {}
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
      this.ybDrgManageChangeHandle = {}
      this.form.resetFields()
    },
    onClose () {
      this.ybDrgManageChangeHandle = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybDrgManageChangeHandle }) {
      this.ybDrgManageChangeHandle = ybDrgManageChangeHandle
      setTimeout(() => {
        this.setForms(ybDrgManageChangeHandle)
      }, 200)
    },
    setForms (amch) {
      let deptId = amch.changeDksId !== '' && amch.changeDksId !== undefined ? amch.changeDksId : amch.readyDksId
      let deptName = amch.changeDksName !== '' && amch.changeDksName !== undefined ? amch.changeDksName : amch.readyDksName
      let doctorName = amch.changeDoctorCode !== '' && amch.changeDoctorCode !== undefined ? amch.changeDoctorName : amch.readyDoctorName
      let doctorCode = amch.changeDoctorCode !== '' && amch.changeDoctorCode !== undefined ? amch.changeDoctorCode : amch.readyDoctorCode

      this.$refs.inputSelectChangeDks.dataSource = [{
        text: deptId + '-' + deptName,
        value: deptId
      }]
      this.$refs.inputSelectChangeDks.value = deptId

      this.$refs.inputSelectChangeDoctor.dataSource = [{
        text: doctorCode + '-' + doctorName,
        value: doctorCode
      }]
      this.$refs.inputSelectChangeDoctor.value = doctorCode

      this.ybDrgManageChangeHandle.changeDoctorCode = doctorCode
      this.ybDrgManageChangeHandle.changeDoctorName = doctorName
      this.ybDrgManageChangeHandle.changeDksName = deptName
      this.ybDrgManageChangeHandle.changeDksId = deptId

      setTimeout(() => {
        this.$refs.ybDrgJkModule.search()
      }, 200)
    },
    selectDoctorChang (item) {
      this.ybDrgManageChangeHandle.changeDoctorCode = item.value
      this.ybDrgManageChangeHandle.changeDoctorName = item.text
    },
    selectDksChange (item) {
      this.ybDrgManageChangeHandle.changeDksId = item.value
      this.ybDrgManageChangeHandle.changeDksName = item.text
    },
    handleRejectSubmit () {
      this.form.validateFields(['refuseReason'], (err, values) => {
        if (!err) {
          this.loading = true
          this.spinning = true
          let fromReason = this.form.getFieldsValue(['refuseReason'])
          let ybDrgManage = {}
          ybDrgManage.state = 4
          ybDrgManage.refuseReason = fromReason.refuseReason
          ybDrgManage.id = this.ybDrgManageChangeHandle.id
          ybDrgManage.applyDataId = this.ybDrgManageChangeHandle.applyDataId
          ybDrgManage.verifyId = this.ybDrgManageChangeHandle.verifyId
          ybDrgManage.readyDksId = this.ybDrgManageChangeHandle.readyDksId
          ybDrgManage.readyDksName = this.ybDrgManageChangeHandle.readyDksName
          ybDrgManage.readyDoctorCode = this.ybDrgManageChangeHandle.readyDoctorCode
          ybDrgManage.readyDoctorName = this.ybDrgManageChangeHandle.readyDoctorName
          ybDrgManage.areaType = this.user.areaType.value
          this.$put('ybDrgManage/updateCreateDrgManage', {
            ...ybDrgManage, type: 2
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
      this.form.validateFields(['changeDksName', 'changeDoctorName'], (err, values) => {
        if (!err) {
          this.loading = true
          this.spinning = true
          let ybDrgManage = {}
          ybDrgManage.state = 4
          ybDrgManage.id = this.ybDrgManageChangeHandle.id
          ybDrgManage.applyDataId = this.ybDrgManageChangeHandle.applyDataId
          ybDrgManage.verifyId = this.ybDrgManageChangeHandle.verifyId
          ybDrgManage.changeDksId = this.ybDrgManageChangeHandle.changeDksId
          ybDrgManage.changeDksName = this.ybDrgManageChangeHandle.changeDksName
          ybDrgManage.changeDoctorCode = this.ybDrgManageChangeHandle.changeDoctorCode
          ybDrgManage.changeDoctorName = this.ybDrgManageChangeHandle.changeDoctorName
          ybDrgManage.readyDksId = this.ybDrgManageChangeHandle.readyDksId
          ybDrgManage.readyDksName = this.ybDrgManageChangeHandle.readyDksName
          ybDrgManage.readyDoctorCode = this.ybDrgManageChangeHandle.readyDoctorCode
          ybDrgManage.readyDoctorName = this.ybDrgManageChangeHandle.readyDoctorName
          ybDrgManage.areaType = this.user.areaType.value
          this.$put('ybDrgManage/updateCreateDrgManage', {
            ...ybDrgManage, type: 1
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
