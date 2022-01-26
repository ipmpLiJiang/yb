<template>
  <a-drawer
    title="变更详情"
    :maskClosable="false"
    width=75%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
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
    <div v-show="ybAppealManageChangeDetail.acceptState === 4?true:false">
    <br>
    <div style="padding-top:20px;padding-bottom:20px;border: 1px solid #e8e8e8;">
    <a-row type="flex" justify="start">
    <a-col :span=22>
      <!--科室、医生-->
      <a-row>
        <a-col :span=8>
          <a-form-item
            v-bind="formItemLayout"
            label="申请科室"
          >
            {{ybAppealManageChangeDetail.readyDeptCode}}-{{ybAppealManageChangeDetail.readyDeptName}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="申请人"
          >
            {{ybAppealManageChangeDetail.readyDoctorCode}}-{{ybAppealManageChangeDetail.readyDoctorName}}
          </a-form-item>
        </a-col>
        <a-col :span=8>
          <a-form-item
            v-bind="formItemLayout"
            label="更改科室"
          >
            {{ybAppealManageChangeDetail.changeDeptCode}}-{{ybAppealManageChangeDetail.changeDeptName}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="更改人"
          >
            {{ybAppealManageChangeDetail.changeDoctorCode}}-{{ybAppealManageChangeDetail.changeDoctorName}}
          </a-form-item>
        </a-col>
      </a-row>
      <a-row type="flex" justify="start">
        <a-col :span=22>
            <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19 }
            }"
            label="审核结果"
          >
          {{ybAppealManageChangeDetail.approvalState === 1 ? '同意' : '拒绝'}}
          </a-form-item>
        </a-col>
      </a-row>
      <!--申诉理由-->
      <a-row type="flex" justify="start">
        <a-col :span=22>
            <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19 }
            }"
            label="申请理由"
          >
          {{ybAppealManageChangeDetail.operateReason}}
          </a-form-item>
        </a-col>
      </a-row>
      <a-row type="flex" justify="start" v-if="ybAppealManageChangeDetail.approvalState === 1 ? false : true">
        <a-col :span=22>
            <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19 }
            }"
            label="拒绝理由"
          >
          {{ybAppealManageChangeDetail.refuseReason}}
          </a-form-item>
        </a-col>
      </a-row>
    </a-col>
    </a-row>
    <br>
    <!--按钮-->
    <a-row type="flex" justify="center">
      <a-col :span=3>
        <a-button
          style="margin-right: .8rem"
          @click="onClose"
        >
          取消
        </a-button>
      </a-col>
    </a-row>
    </div>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbAppealManageChangeDetail',
  components: {
    AppealDataModule, InpatientfeesModule},
  props: {
    detailVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      ybAppealManageChangeDetail: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.ybAppealManageChangeDetail = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybAppealManageChangeDetail }) {
      this.ybAppealManageChangeDetail = ybAppealManageChangeDetail
      setTimeout(() => {
        this.$refs.inpatientfeesModule.search()
      }, 200)
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
