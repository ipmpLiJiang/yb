<template>
  <a-drawer
    title="管理员变更详情"
    :maskClosable="false"
    width=82%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="adminVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybChsData-module
    ref="ybChsDataModule"
    :ybChsData="ybChsManageChangeAdminHandle"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsManageChangeAdminHandle"
    >
    </ybChsJk-module>
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
                  label="汇总科室"
                >
                  <inputSelectChs-dks
                  ref="inputSelectVerifyDks"
                  @selectChange=selectDksChang
                  >
                  </inputSelectChs-dks>
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
import InputSelectChsDks from '../../common/InputSelectChsDks'
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
import { fy } from '../../js/custom'
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 20 }
}
const formItemLayout1 = {
  labelCol: { span: 7 },
  wrapperCol: { span: 16 }
}
export default {
  name: 'YbChsManageChangeAdminHandle',
  components: {
    YbChsDataModule, YbChsJkModule, InputSelect, InputSelectChsDks},
  props: {
    adminVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      form: this.$form.createForm(this),
      ybChsManageChangeAdminHandle: {},
      ybChsManage: {},
      formItemLayout,
      formItemLayout1,
      selectAcceptStateDataSource: [],
      state: 0,
      accStateDisabled: true,
      spinning: false,
      fy,
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
      this.ybChsManageChangeAdminHandle = {}
      this.selectAcceptStateDataSource = []
      this.form.resetFields()
    },
    onClose () {
      this.ybChsManageChangeAdminHandle = {}
      this.ybChsManage = {}
      this.$emit('close')
    },
    handleChange (val) {
      this.state = val
    },
    selectDoctorChang (item) {
      this.ybChsManage.readyDoctorCode = item.value
      this.ybChsManage.readyDoctorName = item.personName
    },
    selectDksChang (item) {
      console.log(item)
      this.ybChsManage.readyDksId = item.value
      this.ybChsManage.readyDksName = item.dksName
      this.ybChsManage.readyFyid = item.fyid
    },
    setFormValues (ybChsManageChangeAdminHandle, type) {
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
      this.ybChsManageChangeAdminHandle = ybChsManageChangeAdminHandle
      this.ybChsManage.id = ybChsManageChangeAdminHandle.id
      this.ybChsManage.verifyId = ybChsManageChangeAdminHandle.verifyId
      this.ybChsManage.applyDataId = ybChsManageChangeAdminHandle.applyDataId
      this.ybChsManage.state = ybChsManageChangeAdminHandle.state

      this.changePersons = fy.getDksFyName(this.ybChsManageChangeAdminHandle.readyDksName, this.ybChsManageChangeAdminHandle.readyFyid) + '  -  ' + this.ybChsManageChangeAdminHandle.readyDoctorCode + '-' + this.ybChsManageChangeAdminHandle.readyDoctorName
      setTimeout(() => {
        this.setForms(ybChsManageChangeAdminHandle)
      }, 200)
    },
    setForms (target) {
      this.$refs.inputSelectVerifyDks.dataSource = [{
        text: fy.getDksFyName(target.readyDksName, target.readyFyid),
        value: target.readyDksId
      }]
      this.$refs.inputSelectVerifyDks.value = target.readyDksId

      this.$refs.inputSelectVerifyDoctor.dataSource = [{
        text: target.readyDoctorCode + '-' + target.readyDoctorName,
        value: target.readyDoctorCode
      }]
      this.$refs.inputSelectVerifyDoctor.value = target.readyDoctorCode

      this.ybChsManage.readyDoctorCode = target.readyDoctorCode
      this.ybChsManage.readyDoctorName = target.readyDoctorName

      this.ybChsManage.readyDksId = target.readyDksId
      this.ybChsManage.readyDksName = target.readyDksName
      this.ybChsManage.readyFyid = target.readyFyid

      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
    },
    handleSubmit () {
      this.loading = true
      this.spinning = true
      let ybChsManage = this.ybChsManage
      ybChsManage.state = this.state
      console.log(ybChsManage)
      // ybChsManage.id = this.ybChsManageChangeAdminHandle.id
      // ybChsManage.sourceType = this.ybChsManageChangeAdminHandle.sourceType
      // ybChsManage.verifyId = this.ybChsManageChangeAdminHandle.verifyId
      // ybChsManage.applyDataId = this.ybChsManageChangeAdminHandle.applyDataId
      // ybChsManage.readyDoctorCode = this.ybChsManageChangeAdminHandle.readyDoctorCode
      // ybChsManage.readyDoctorName = this.ybChsManageChangeAdminHandle.readyDoctorName
      // ybChsManage.readyDksName = this.ybChsManageChangeAdminHandle.readyDksName

      if (this.type === 0 || this.type === 3) {
        if (ybChsManage.readyDoctorCode === this.ybChsManageChangeAdminHandle.readyDoctorCode &&
            ybChsManage.readyDksId === this.ybChsManageChangeAdminHandle.readyDksId) {
          this.$message.error('未更改 汇总科室 和 复议医生 , 不可提交数据.')
          this.loading = false
          this.spinning = false
          return
        }
      }
      this.$put('ybChsManage/updateCreateAdminChsManage', {
        ...ybChsManage
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
