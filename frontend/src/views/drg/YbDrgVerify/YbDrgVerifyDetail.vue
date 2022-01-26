<template>
  <a-drawer
    title="DRG核对"
    :maskClosable="false"
    width=80%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgVerifyView"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgVerifyView"
    >
    </ybDrgJk-module>
    <template>
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <a-form :form="form" >
          <a-divider>更改科室/医生</a-divider>
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
                  label="复议科室">
                    <input-selectdks
                      ref="inputSelectVerifyDks"
                      :ctType=3
                      @selectChange=selectDksChang
                    >
                    </input-selectdks>
                  </a-form-item>
              </a-col>
              <a-col :span=8>
                <a-form-item
                  v-bind="{
                    labelCol: { span: 7 },
                    wrapperCol: { span: 16 }
                  }"
                  label="复议医生">
                  <input-select
                  ref="inputSelectVerifyDoctor"
                  :type=2
                  dept='医生'
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
import InputSelectdks from '../../common/InputSelectDks'
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'

export default {
  name: 'YbDrgVerifyDetail',
  components: {
    YbDrgDataModule, YbDrgJkModule, InputSelect, InputSelectdks },
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
      ybDrgVerifyView: {},
      ybDrgVerify: {},
      spinning: false,
      delayTime: 500,
      user: this.$store.state.account.user,
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
      this.ybDrgVerify = {}
      this.ybDrgVerifyView = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      let dtc = this.ybDrgVerify.verifyDoctorCode
      let dec = this.ybDrgVerify.verifyDksName
      if (dtc !== undefined && dec !== undefined && dtc !== null && dec !== null && dtc !== '' && dec !== '') {
        this.loading = true
        this.spinning = true
        let arrData = [{
          id: this.ybDrgVerifyView.isVerify === 0 ? '' : this.ybDrgVerify.id,
          applyDataId: this.ybDrgVerifyView.applyDataId,
          verifyDoctorCode: this.ybDrgVerify.verifyDoctorCode,
          verifyDoctorName: this.ybDrgVerify.verifyDoctorName,
          verifyDksName: this.ybDrgVerify.verifyDksName,
          applyDateStr: this.ybDrgVerifyView.applyDateStr,
          orderNumber: this.ybDrgVerifyView.orderNumber,
          orderNum: this.ybDrgVerifyView.orderNum,
          areaType: this.user.areaType.value
        }]

        let jsonString = JSON.stringify(arrData)
        this.ybDrgVerify = {}
        this.$put('ybDrgVerify/updateReviewerState', {
          dataJson: jsonString
        }).then(() => {
          this.reset()
          this.$emit('success')
        }).catch(() => {
          this.loading = false
          this.spinning = false
        })
      } else {
        this.$message.warning('未选择，参考复议科室 或 参考复议医生.')
      }
    },
    selectDoctorChang (item) {
      this.ybDrgVerify.verifyDoctorCode = item.value
      this.ybDrgVerify.verifyDoctorName = item.text
    },
    selectDksChang (item) {
      this.ybDrgVerify.verifyDksName = item.text
    },
    setFormValues ({ ...ybDrgVerifyView }) {
      this.ybDrgVerifyView = ybDrgVerifyView
      this.ybDrgVerify.applyDataId = ybDrgVerifyView.applyDataId
      setTimeout(() => {
        this.setForms(ybDrgVerifyView)
      }, 200)
    },
    setForms (target) {
      this.$refs.inputSelectVerifyDks.dataSource = [{
        text: target.verifyDksName,
        value: target.verifyDksName
      }]
      this.$refs.inputSelectVerifyDks.value = target.verifyDksName

      this.$refs.inputSelectVerifyDoctor.dataSource = [{
        text: target.verifyDoctorName,
        value: target.verifyDoctorCode
      }]
      this.$refs.inputSelectVerifyDoctor.value = target.verifyDoctorCode

      this.ybDrgVerify = {
        id: target.id,
        verifyDoctorCode: target.verifyDoctorCode,
        verifyDoctorName: target.verifyDoctorName,
        verifyDksName: target.verifyDksName,
        state: 2
      }

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
