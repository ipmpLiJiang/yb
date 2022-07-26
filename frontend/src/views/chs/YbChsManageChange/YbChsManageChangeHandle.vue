<template>
  <a-drawer
    title="申诉填报"
    :maskClosable="false"
    width=82%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="handleVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybChsData-module
    ref="ybChsDataModule"
    :ybChsData="ybChsManageChangeHandle"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsManageChangeHandle"
    >
    </ybChsJk-module>
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
                        {{ybChsManageChangeHandle.readyDksId}}-{{ybChsManageChangeHandle.readyDksName}}
                      </a-form-item>
                    </a-col>
                    <a-col :span=14>
                      <a-form-item
                        v-bind="formItemLayout"
                        label="变更为"
                      >
                        <inputSelectChs-dks
                        ref="inputSelectChangeDks"
                        @selectChange=selectDksChange
                        >
                        </inputSelectChs-dks>
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span=10>
                      <a-form-item
                        v-bind="formItemLayout0"
                        label="申请人"
                      >
                        {{ybChsManageChangeHandle.readyDoctorCode}}-{{ybChsManageChangeHandle.readyDoctorName}}
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
                        {{ybChsManageChangeHandle.operateDate}}
                      </a-form-item>
                    </a-col>
                  </a-row>
                  <a-row type="flex" justify="start">
                    <a-col :span=24>
                      <a-form-item
                        v-bind="formItemLayout1"
                        label="医院意见"
                      >
                        {{ybChsManageChangeHandle.operateReason}}
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
import InputSelectChsDks from '../../common/InputSelectChsDks'
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
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
  name: 'YbChsManageChangeHandle',
  components: {
    YbChsDataModule, YbChsJkModule, InputSelect, InputSelectChsDks},
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
      ybChsManageChangeHandle: {}
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
      this.ybChsManageChangeHandle = {}
      this.form.resetFields()
    },
    onClose () {
      this.ybChsManageChangeHandle = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybChsManageChangeHandle }) {
      this.ybChsManageChangeHandle = ybChsManageChangeHandle
      setTimeout(() => {
        this.setForms(ybChsManageChangeHandle)
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

      this.ybChsManageChangeHandle.changeDoctorCode = doctorCode
      this.ybChsManageChangeHandle.changeDoctorName = doctorName
      this.ybChsManageChangeHandle.changeDksName = deptName
      this.ybChsManageChangeHandle.changeDksId = deptId

      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
    },
    selectDoctorChang (item) {
      this.ybChsManageChangeHandle.changeDoctorCode = item.value
      this.ybChsManageChangeHandle.changeDoctorName = item.text
    },
    selectDksChange (item) {
      this.ybChsManageChangeHandle.changeDksId = item.value
      this.ybChsManageChangeHandle.changeDksName = item.text
    },
    handleRejectSubmit () {
      this.form.validateFields(['refuseReason'], (err, values) => {
        if (!err) {
          this.loading = true
          this.spinning = true
          let fromReason = this.form.getFieldsValue(['refuseReason'])
          let ybChsManage = {}
          ybChsManage.state = 4
          ybChsManage.refuseReason = fromReason.refuseReason
          ybChsManage.id = this.ybChsManageChangeHandle.id
          ybChsManage.applyDataId = this.ybChsManageChangeHandle.applyDataId
          ybChsManage.verifyId = this.ybChsManageChangeHandle.verifyId
          ybChsManage.readyDksId = this.ybChsManageChangeHandle.readyDksId
          ybChsManage.readyDksName = this.ybChsManageChangeHandle.readyDksName
          ybChsManage.readyDoctorCode = this.ybChsManageChangeHandle.readyDoctorCode
          ybChsManage.readyDoctorName = this.ybChsManageChangeHandle.readyDoctorName
          ybChsManage.areaType = this.user.areaType.value
          this.$put('ybChsManage/updateCreateChsManage', {
            ...ybChsManage, type: 2
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
          let ybChsManage = {}
          ybChsManage.state = 4
          ybChsManage.id = this.ybChsManageChangeHandle.id
          ybChsManage.applyDataId = this.ybChsManageChangeHandle.applyDataId
          ybChsManage.verifyId = this.ybChsManageChangeHandle.verifyId
          ybChsManage.changeDksId = this.ybChsManageChangeHandle.changeDksId
          ybChsManage.changeDksName = this.ybChsManageChangeHandle.changeDksName
          ybChsManage.changeDoctorCode = this.ybChsManageChangeHandle.changeDoctorCode
          ybChsManage.changeDoctorName = this.ybChsManageChangeHandle.changeDoctorName
          ybChsManage.readyDksId = this.ybChsManageChangeHandle.readyDksId
          ybChsManage.readyDksName = this.ybChsManageChangeHandle.readyDksName
          ybChsManage.readyDoctorCode = this.ybChsManageChangeHandle.readyDoctorCode
          ybChsManage.readyDoctorName = this.ybChsManageChangeHandle.readyDoctorName
          ybChsManage.areaType = this.user.areaType.value
          this.$put('ybChsManage/updateCreateChsManage', {
            ...ybChsManage, type: 1
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
