<template>
  <a-drawer
    title="核对"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <appealData-module
    ref="appealDataModule"
    :ybAppealDataModule="ybReconsiderVerifyView"
    >
    </appealData-module>
    <inpatientfees-module
    ref="inpatientfeesModule"
    :inpatientfeesModule="ybReconsiderVerifyView"
    >
    </inpatientfees-module>
    <template>
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <a-form :form="form" >
          <div style="margin:20px 0px">
            <a-row
              justify="center"
              type="flex"
            >
              <a-col :span=8>
                  <a-form-item
                  v-bind="{
                    labelCol: { span: 7 },
                    wrapperCol: { span: 16 }
                  }"
                  label="参考复议科室">
                    <input-select
                      ref="inputSelectVerifyDept"
                      :type=1
                      @selectChange=selectDeptChang
                    >
                    </input-select>
                  </a-form-item>
              </a-col>
              <a-col :span=8>
                <a-form-item
                  v-bind="{
                    labelCol: { span: 7 },
                    wrapperCol: { span: 16 }
                  }"
                  label="参考复议医生">
                  <input-select
                  ref="inputSelectVerifyDoctor"
                  :type=2
                  @selectChange=selectDoctorChang
                  >
                  </input-select>
                  </a-form-item>
              </a-col>
              <a-col :span=2>
                <a-popconfirm
                  title="确定放弃提交？"
                  @confirm="handleSubmit"
                  okText="确定"
                  cancelText="取消"
                >
                  <a-button type="primary" style="margin-right: .8rem">提交</a-button>
                </a-popconfirm>
              </a-col>
              <a-col :span=3>
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
        </a-form>
      </div>
      </a-spin>
    </template>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import InputSelect from '../../common/InputSelect'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'

export default {
  name: 'YbReconsiderVerifyViewData',
  components: {
    AppealDataModule, InpatientfeesModule, InputSelect },
  props: {
    detailVisiable: {
      default: false
    }
  },
  computed: {
  },
  data () {
    return {
      loading: false,
      ybReconsiderVerifyView: {},
      ybReconsiderVerify: {},
      spinning: false,
      delayTime: 500,
      form: this.$form.createForm(this)
    }
  },
  mounted () {
  },
  methods: {
    moment,
    reset () {
      this.loading = false
      this.spinning = false
      this.ybReconsiderVerify = {}
      this.ybReconsiderVerifyView = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.loading = true
      this.spinning = true
      let arrData = [{
        id: this.ybReconsiderVerifyView.isVerify === 0 ? '' : this.ybReconsiderVerify.id,
        applyDataId: this.ybReconsiderVerifyView.applyDataId,
        verifyDoctorCode: this.ybReconsiderVerify.verifyDoctorCode,
        verifyDoctorName: this.ybReconsiderVerify.verifyDoctorName,
        verifyDeptCode: this.ybReconsiderVerify.verifyDeptCode,
        verifyDeptName: this.ybReconsiderVerify.verifyDeptName,
        dataType: this.ybReconsiderVerifyView.dataType,
        applyDateStr: this.ybReconsiderVerifyView.applyDateStr,
        orderNumber: this.ybReconsiderVerifyView.orderNumber,
        orderNum: this.ybReconsiderVerifyView.orderNum,
        typeno: this.ybReconsiderVerifyView.typeno}]

      let jsonString = JSON.stringify(arrData)
      this.ybReconsiderVerify = {}
      this.$put('ybReconsiderVerify/updateReviewerState', {
        dataJson: jsonString
      }).then(() => {
        this.reset()
        this.$emit('success')
      }).catch(() => {
        this.loading = false
        this.spinning = false
      })
    },
    selectDoctorChang (item) {
      this.ybReconsiderVerify.verifyDoctorCode = item.value
      this.ybReconsiderVerify.verifyDoctorName = item.text
    },
    selectDeptChang (item) {
      this.ybReconsiderVerify.verifyDeptCode = item.value
      this.ybReconsiderVerify.verifyDeptName = item.text
    },
    setFormValues ({ ...ybReconsiderVerifyView }) {
      this.ybReconsiderVerifyView = ybReconsiderVerifyView
      this.ybReconsiderVerify.applyDataId = ybReconsiderVerifyView.applyDataId
      setTimeout(() => {
        this.setForms(ybReconsiderVerifyView)
      }, 200)
    },
    setForms (target) {
      this.$refs.inputSelectVerifyDept.dataSource = [{
        text: target.verifyDeptName,
        value: target.verifyDeptCode
      }]
      this.$refs.inputSelectVerifyDept.value = target.verifyDeptCode

      this.$refs.inputSelectVerifyDoctor.dataSource = [{
        text: target.verifyDoctorName,
        value: target.verifyDoctorCode
      }]
      this.$refs.inputSelectVerifyDoctor.value = target.verifyDoctorCode

      this.ybReconsiderVerify = {
        id: target.id,
        verifyDoctorCode: target.verifyDoctorCode,
        verifyDoctorName: target.verifyDoctorName,
        verifyDeptCode: target.verifyDeptCode,
        verifyDeptName: target.verifyDeptName,
        state: 2
      }

      this.$refs.inpatientfeesModule.search()
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
