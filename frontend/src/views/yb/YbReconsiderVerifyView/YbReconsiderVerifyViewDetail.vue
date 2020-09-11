<template>
  <a-drawer
    title="核对"
    :maskClosable="false"
    width=70%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="detailVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
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
    <a-form :form="form" >
      <div style="margin:20px 0px">
        <a-row
          justify="center"
          align="middle"
        >
          <a-col :span=1>
            &nbsp;
          </a-col>
          <a-col :span=7>
              参考复议科室：
              <a-select
                show-search
                :value="selectDeptValue"
                placeholder="请输入关键词"
                style="width: 180px"
                :default-active-first-option="false"
                :filter-option="false"
                :not-found-content="null"
                @search="handleDeptSearch"
                @change="handleDeptChange"
              >
                <a-icon
                  slot="suffixIcon"
                  type="search"
                ></a-icon>
                <a-select-option
                  v-for="d in selectDeptDataSource"
                  :key="d.value"
                >
                  {{ d.text }}
                </a-select-option>
              </a-select>
          </a-col>
          <a-col :span=10>
              参考复议医生：
              <a-select
                show-search
                :value="selectDoctorValue"
                placeholder="请输入关键词"
                style="width: 180px"
                :default-active-first-option="false"
                :filter-option="false"
                :not-found-content="null"
                @search="handleDoctorSearch"
                @change="handleDoctorChange"
              >
                <a-icon
                  slot="suffixIcon"
                  type="search"
                ></a-icon>
                <a-select-option
                  v-for="d in selectDoctorDataSource"
                  :key="d.value"
                >
                  {{ d.text }}
                </a-select-option>
              </a-select>
          </a-col>
          <a-col :span=6>
            <a-button
              @click="handleSubmit"
              type="primary"
              :loading="loading"
            >提交</a-button>
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
    </template>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import AppealDataModule from '../ybFunModule/AppealDataModule'
import InpatientfeesModule from '../ybFunModule/InpatientfeesModule'
export default {
  name: 'YbReconsiderVerifyViewData',
  components: {
    AppealDataModule, InpatientfeesModule},
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
      form: this.$form.createForm(this),
      selectDeptDataSource: [],
      selectDeptValue: undefined,
      selectDoctorDataSource: [],
      selectDoctorValue: undefined
    }
  },
  mounted () {
  },
  methods: {
    moment,
    reset () {
      this.loading = false
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
      let arrData = [{
        id: this.ybReconsiderVerify.id,
        applyDataId: this.ybReconsiderVerify.applyDataId,
        verifyDoctorCode: this.ybReconsiderVerify.verifyDoctorCode,
        verifyDoctorName: this.ybReconsiderVerify.verifyDoctorName,
        verifyDeptCode: this.ybReconsiderVerify.verifyDeptCode,
        verifyDeptName: this.ybReconsiderVerify.verifyDeptName}]

      let jsonString = JSON.stringify(arrData)
      this.ybReconsiderVerify = {}
      this.$put('ybReconsiderVerify/updateReviewerState', {
        dataJson: jsonString
      }).then(() => {
        this.reset()
        this.$emit('success')
      }).catch(() => {
        this.loading = false
      })
    },
    // 输入框事件
    handleDeptSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDeptDataSource = this.ajax(keyword)
    },
    handleDeptChange (value) {
      const itemText = this.selectDeptDataSource.filter(item => value === item.value)[0]
      this.selectDeptValue = value
      this.ybReconsiderVerify.verifyDeptCode = value
      this.ybReconsiderVerify.verifyDeptName = itemText.text
    },
    // 输入框事件
    handleDoctorSearch (keyword) {
      // 模拟往服务器发送请求
      this.selectDoctorDataSource = this.ajaxDoc(keyword)
    },
    handleDoctorChange (value) {
      const itemText = this.selectDoctorDataSource.filter(item => value === item.value)[0]
      this.selectDoctorValue = value
      this.ybReconsiderVerify.verifyDoctorCode = value
      this.ybReconsiderVerify.verifyDoctorName = itemText.text
    },
    // 模拟往服务器发送请求
    ajax (keyword) {
      let dataSource = [{value: '101A', text: '测试科室1'}, {value: '102A', text: '测试科室2'}, {value: '103A', text: '测试科室3'}]
      return dataSource
    },
    ajaxDoc (keyword) {
      let dataSource = [{value: '101A', text: '测试医生1'}, {value: '102A', text: '测试医生2'}, {value: '103A', text: '测试医生3'}]
      return dataSource
    },
    setFormValues ({ ...ybReconsiderVerifyView }) {
      this.ybReconsiderVerifyView = ybReconsiderVerifyView
      this.selectDeptDataSource = [{
        text: ybReconsiderVerifyView.verifyDeptName,
        value: ybReconsiderVerifyView.verifyDeptCode
      }]
      this.selectDeptValue = ybReconsiderVerifyView.verifyDeptCode
      this.selectDoctorDataSource = [{
        text: ybReconsiderVerifyView.verifyDoctorName,
        value: ybReconsiderVerifyView.verifyDoctorCode
      }]
      this.selectDoctorValue = ybReconsiderVerifyView.verifyDoctorCode
      this.ybReconsiderVerify = {
        id: ybReconsiderVerifyView.id,
        verifyDoctorCode: ybReconsiderVerifyView.verifyDoctorCode,
        verifyDoctorName: ybReconsiderVerifyView.verifyDoctorName,
        verifyDeptCode: ybReconsiderVerifyView.verifyDeptCode,
        verifyDeptName: ybReconsiderVerifyView.verifyDeptName,
        state: 2
      }

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
