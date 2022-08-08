<template>
  <a-drawer
    title="核对明细"
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
    :ybChsData="ybChsVerifyView"
    >
    </ybChsData-module>
    <ybChsJk-module
    ref="ybChsJkModule"
    :ybChsData="ybChsVerifyView"
    >
    </ybChsJk-module>
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
                  label="汇总科室">
                    <inputSelectChs-dks
                      ref="inputSelectVerifyChsDks"
                      @selectChange=selectDksChang
                    >
                    </inputSelectChs-dks>
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
import InputSelectChsDks from '../../common/InputSelectChsDks'
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
import { fy } from '../../js/custom'

export default {
  name: 'YbChsVerifyDetail',
  components: {
    YbChsDataModule, YbChsJkModule, InputSelect, InputSelectChsDks },
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
      ybChsVerifyView: {},
      ybChsVerify: {},
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
      this.ybChsVerify = {}
      this.ybChsVerifyView = {}
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      let dtc = this.ybChsVerify.verifyDoctorCode
      let dec = this.ybChsVerify.verifyDksId
      if (dtc !== undefined && dec !== undefined && dtc !== null && dec !== null && dtc !== '' && dec !== '') {
        this.loading = true
        this.spinning = true
        let arrData = [{
          id: this.ybChsVerifyView.isVerify === 0 ? '' : this.ybChsVerify.id,
          applyDataId: this.ybChsVerifyView.applyDataId,
          verifyDoctorCode: this.ybChsVerify.verifyDoctorCode,
          verifyDoctorName: this.ybChsVerify.verifyDoctorName,
          verifyDksId: this.ybChsVerify.verifyDksId,
          verifyDksName: this.ybChsVerify.verifyDksName,
          verifyFyid: this.ybChsVerify.verifyFyid,
          applyDateStr: this.ybChsVerifyView.applyDateStr,
          orderNum: this.ybChsVerifyView.orderNum,
          dataType: this.ybChsVerifyView.dataType,
          areaType: this.user.areaType.value
        }]

        let jsonString = JSON.stringify(arrData)
        this.ybChsVerify = {}
        this.$put('ybChsVerify/updateReviewerState', {
          dataJson: jsonString
        }).then(() => {
          this.reset()
          this.$emit('success')
        }).catch(() => {
          this.loading = false
          this.spinning = false
        })
      } else {
        this.$message.warning('未选择，汇总科室 或 复议医生.')
      }
    },
    selectDoctorChang (item) {
      this.ybChsVerify.verifyDoctorCode = item.value
      this.ybChsVerify.verifyDoctorName = item.personName
    },
    selectDksChang (item) {
      this.ybChsVerify.verifyDksId = item.value
      this.ybChsVerify.verifyDksName = item.dksName
      this.ybChsVerify.verifyFyid = item.fyid
    },
    setFormValues ({ ...ybChsVerifyView }) {
      this.ybChsVerifyView = ybChsVerifyView
      this.ybChsVerify.applyDataId = ybChsVerifyView.applyDataId
      setTimeout(() => {
        this.setForms(ybChsVerifyView)
      }, 200)
    },
    setForms (target) {
      if (target.verifyDksId) {
        this.$refs.inputSelectVerifyChsDks.dataSource = [{
          text: fy.getDksFyName(target.verifyDksName, target.verifyFyid),
          value: target.verifyDksId
        }]
        this.$refs.inputSelectVerifyChsDks.value = target.verifyDksId
      } else {
        this.$refs.inputSelectVerifyChsDks.dataSource = []
        this.$refs.inputSelectVerifyChsDks.value = ''
      }
      if (target.verifyDoctorCode) {
        this.$refs.inputSelectVerifyDoctor.dataSource = [{
          text: target.verifyDoctorCode + '-' + target.verifyDoctorName,
          value: target.verifyDoctorCode
        }]
        this.$refs.inputSelectVerifyDoctor.value = target.verifyDoctorCode
      } else {
        this.$refs.inputSelectVerifyDoctor.dataSource = []
        this.$refs.inputSelectVerifyDoctor.value = ''
      }
      this.ybChsVerify = {
        id: target.id,
        verifyDoctorCode: target.verifyDoctorCode,
        verifyDoctorName: target.verifyDoctorName,
        verifyDksId: target.verifyDksId,
        verifyFyid: target.verifyFyid,
        dataType: target.dataType,
        verifyDksName: target.verifyDksName,
        state: 2
      }

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
