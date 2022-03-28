<template>
  <a-drawer
    title="DRG管理员变更详情"
    :maskClosable="false"
    width=80%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="adminVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgManageChangeAdminHandle"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgManageChangeAdminHandle"
    >
    </ybDrgJk-module>
    <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
    <div style="margin-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
    <div style="margin-top:20px;margin-left:20px;">
      <template>
        <a-form :form="form">
        <a-row type="flex" justify="start">
          <a-col :span=24>
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="变更人"
                >
                {{changePersons}}
                </a-form-item>
              </a-col>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout1"
                  label="更新状态"
                >
                  <a-select v-model="state" :disabled="accStateDisabled" style="width: 150px" @change="handleChange">
                  <a-select-option
                  v-for="d in selectAcceptStateDataSource"
                  :key="d.value"
                  >
                  {{ d.text }}
                  </a-select-option>
                </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout1"
                  label="复议科室"
                >
                  <input-selectdks
                  ref="inputSelectVerifyDks"
                  @selectChange=selectDksChang
                  >
                  </input-selectdks>
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
                  dept='医生'
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
import InputSelectdks from '../../common/InputSelectDks'
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 20 }
}
const formItemLayout1 = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'YbDrgManageChangeAdminHandle',
  components: {
    YbDrgDataModule, YbDrgJkModule, InputSelect, InputSelectdks},
  props: {
    adminVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      form: this.$form.createForm(this),
      ybDrgManageChangeAdminHandle: {},
      ybDrgManage: {},
      formItemLayout,
      formItemLayout1,
      selectAcceptStateDataSource: [],
      state: 0,
      accStateDisabled: true,
      spinning: false,
      type: 0,
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
      this.state = 0
      this.accStateDisabled = true
      this.ybDrgManageChangeAdminHandle = {}
      this.selectAcceptStateDataSource = []
      this.form.resetFields()
    },
    onClose () {
      this.ybDrgManageChangeAdminHandle = {}
      this.ybDrgManage = {}
      this.$emit('close')
    },
    handleChange (val) {
      this.state = val
    },
    selectDoctorChang (item) {
      this.ybDrgManage.readyDoctorCode = item.value
      this.ybDrgManage.readyDoctorName = item.text
    },
    selectDksChang (item) {
      this.ybDrgManage.readyDksId = item.value
      this.ybDrgManage.readyDksName = item.text
    },
    setFormValues (ybDrgManageChangeAdminHandle, type) {
      this.type = type
      if (type === 0) {
        this.selectAcceptStateDataSource = [{value: 0, text: '接受申请'}]
        this.state = 0
        this.accStateDisabled = true
      } else if (type === 7) {
        this.selectAcceptStateDataSource = [{value: 6, text: '已申诉'}]
        this.state = 6
        this.accStateDisabled = true
      } else {
        this.selectAcceptStateDataSource = [{value: 0, text: '接受申请'}, {value: 1, text: '待申诉'}]
        this.state = 0
        this.accStateDisabled = false
      }
      this.ybDrgManageChangeAdminHandle = ybDrgManageChangeAdminHandle
      this.ybDrgManage.id = ybDrgManageChangeAdminHandle.id
      this.ybDrgManage.verifyId = ybDrgManageChangeAdminHandle.verifyId
      this.ybDrgManage.applyDataId = ybDrgManageChangeAdminHandle.applyDataId
      this.ybDrgManage.state = ybDrgManageChangeAdminHandle.state
      this.changePersons = this.ybDrgManageChangeAdminHandle.readyDksId + '-' + this.ybDrgManageChangeAdminHandle.readyDksName + ' - ' + this.ybDrgManageChangeAdminHandle.readyDoctorCode + '-' + this.ybDrgManageChangeAdminHandle.readyDoctorName
      setTimeout(() => {
        this.setForms(ybDrgManageChangeAdminHandle)
      }, 200)
    },
    setForms (target) {
      this.$refs.inputSelectVerifyDks.dataSource = [{
        text: target.readyDksId + '-' + target.readyDksName,
        value: target.readyDksId
      }]
      this.$refs.inputSelectVerifyDks.value = target.readyDksId

      this.$refs.inputSelectVerifyDoctor.dataSource = [{
        text: target.readyDoctorCode + '-' + target.readyDoctorName,
        value: target.readyDoctorCode
      }]
      this.$refs.inputSelectVerifyDoctor.value = target.readyDoctorCode

      this.ybDrgManage.readyDoctorCode = target.readyDoctorCode
      this.ybDrgManage.readyDoctorName = target.readyDoctorName

      this.ybDrgManage.readyDksId = target.readyDksId
      this.ybDrgManage.readyDksName = target.readyDksName

      setTimeout(() => {
        this.$refs.ybDrgJkModule.search()
      }, 200)
    },
    handleSubmit () {
      this.loading = true
      this.spinning = true
      let ybDrgManage = this.ybDrgManage
      ybDrgManage.state = this.state
      // ybDrgManage.id = this.ybDrgManageChangeAdminHandle.id
      // ybDrgManage.sourceType = this.ybDrgManageChangeAdminHandle.sourceType
      // ybDrgManage.verifyId = this.ybDrgManageChangeAdminHandle.verifyId
      // ybDrgManage.applyDataId = this.ybDrgManageChangeAdminHandle.applyDataId
      // ybDrgManage.readyDoctorCode = this.ybDrgManageChangeAdminHandle.readyDoctorCode
      // ybDrgManage.readyDoctorName = this.ybDrgManageChangeAdminHandle.readyDoctorName
      // ybDrgManage.readyDksName = this.ybDrgManageChangeAdminHandle.readyDksName

      if (this.type === 0 || this.type === 3) {
        if (ybDrgManage.readyDoctorCode === this.ybDrgManageChangeAdminHandle.readyDoctorCode &&
            ybDrgManage.readyDksId === this.ybDrgManageChangeAdminHandle.readyDksId) {
          this.$message.error('未更改 复议科室 和 复议医生 , 不可提交数据.')
          this.loading = false
          this.spinning = false
          return
        }
      }
      this.$put('ybDrgManage/updateCreateAdminDrgManage', {
        ...ybDrgManage
      }).then(() => {
        this.reset()
        this.$emit('success', this.type)
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
