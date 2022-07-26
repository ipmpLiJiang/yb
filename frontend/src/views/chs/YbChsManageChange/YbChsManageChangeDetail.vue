<template>
  <a-drawer
    title="变更详情"
    :maskClosable="false"
    width=82%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybChsData-module
    ref="ybChsDataModule"
    :ybChsData="ybChsManageChangeDetail"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsManageChangeDetail"
    >
    </ybChsJk-module>
    <div v-show="ybChsManageChangeDetail.state === 4?true:false">
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
            {{ybChsManageChangeDetail.readyDksId}}-{{ybChsManageChangeDetail.readyDksName}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="申请人"
          >
            {{ybChsManageChangeDetail.readyDoctorCode}}-{{ybChsManageChangeDetail.readyDoctorName}}
          </a-form-item>
        </a-col>
        <a-col :span=8>
          <a-form-item
            v-bind="formItemLayout"
            label="更改科室"
          >
            {{ybChsManageChangeDetail.changeDksId}}-{{ybChsManageChangeDetail.changeDksName}}
          </a-form-item>
        </a-col>
        <a-col :span=4>
          <a-form-item
            v-bind="formItemLayout"
            label="更改人"
          >
            {{ybChsManageChangeDetail.changeDoctorCode}}-{{ybChsManageChangeDetail.changeDoctorName}}
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
          {{ybChsManageChangeDetail.approvalState === 1 ? '同意' : '拒绝'}}
          </a-form-item>
        </a-col>
      </a-row>
      <!--医院意见-->
      <a-row type="flex" justify="start">
        <a-col :span=22>
            <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19 }
            }"
            label="医院意见"
          >
          {{ybChsManageChangeDetail.operateReason}}
          </a-form-item>
        </a-col>
      </a-row>
      <a-row type="flex" justify="start"  v-if="ybChsManageChangeDetail.approvalState === 1 ? false : true">
        <a-col :span=22>
            <a-form-item
            v-bind="{
              labelCol: { span: 3 },
              wrapperCol: { span: 19 }
            }"
            label="拒绝理由"
          >
          {{ybChsManageChangeDetail.refuseReason}}
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
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbChsManageChangeDetail',
  components: {
    YbChsDataModule, YbChsJkModule},
  props: {
    detailVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      ybChsManageChangeDetail: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.ybChsManageChangeDetail = {}
      this.$emit('close')
    },
    setFormValues ({ ...ybChsManageChangeDetail }) {
      this.ybChsManageChangeDetail = ybChsManageChangeDetail
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
