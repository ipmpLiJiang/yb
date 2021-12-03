<template>
  <a-drawer
    title="DRG变更详情"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgManageChangeDetail"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgManageChangeDetail"
    >
    </ybDrgJk-module>
    <div v-show="ybDrgManageChangeDetail.state === 4?true:false">
    <a-divider>变更信息</a-divider>
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
            {{ybDrgManageChangeDetail.readyDeptCode}}-{{ybDrgManageChangeDetail.readyDeptName}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="申请人"
          >
            {{ybDrgManageChangeDetail.readyDoctorCode}}-{{ybDrgManageChangeDetail.readyDoctorName}}
          </a-form-item>
        </a-col>
        <a-col :span=8>
          <a-form-item
            v-bind="formItemLayout"
            label="更改科室"
          >
            {{ybDrgManageChangeDetail.changeDeptCode}}-{{ybDrgManageChangeDetail.changeDeptName}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="更改人"
          >
            {{ybDrgManageChangeDetail.changeDoctorCode}}-{{ybDrgManageChangeDetail.changeDoctorName}}
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
          {{ybDrgManageChangeDetail.approvalState === 1 ? '同意' : '拒绝'}}
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
          {{ybDrgManageChangeDetail.operateReason}}
          </a-form-item>
        </a-col>
      </a-row>
      <a-row type="flex" justify="start"  v-if="ybDrgManageChangeDetail.approvalState === 1 ? false : true">
        <a-col :span=22>
            <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19 }
            }"
            label="拒绝理由"
          >
          {{ybDrgManageChangeDetail.refuseReason}}
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
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'
const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbDrgManageChangeDetail',
  components: {
    YbDrgDataModule, YbDrgJkModule},
  props: {
    detailVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      ybDrgManageChangeDetail: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.ybDrgManageChangeDetail = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybDrgManageChangeDetail }) {
      this.ybDrgManageChangeDetail = ybDrgManageChangeDetail
      setTimeout(() => {
        this.$refs.ybDrgJkModule.search()
      }, 200)
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
