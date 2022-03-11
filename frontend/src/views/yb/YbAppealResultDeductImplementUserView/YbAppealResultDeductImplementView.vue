
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <a-form layout="horizontal">
        <div style="margin-bottom:16px">
          {{applyDateText}}：
          <a-month-picker
            @change="monthChange"
            style="width: 105px"
            :default-value="defaultApplyDate"
            :format="monthFormat"
          />
          至：
          <a-month-picker
            placeholder="请选择复议年月"
            @change="monthToChange"
            style="width: 105px"
            :default-value="defaultApplyDate"
            :format="monthFormat"
          />
          <a-select
            :value="searchDataType"
            @change="handleDataTypeChange"
            style="width: 95px"
          >
            <a-select-option
              v-for="d in selectDataTypeDataSource"
              :key="d.value"
            >
              {{ d.text }}
            </a-select-option>
          </a-select>
          <a-select v-model="searchItem.keyField" style="width: 110px">
            <a-select-option
            v-for="d in searchDropDataSource"
            :key="d.value"
            >
            {{ d.text }}
            </a-select-option>
          </a-select>
          =
          <a-input-search placeholder="请输入关键字" v-model="searchItem.value" style="width: 160px" enter-button @search="searchTable" />
        </div>
      </a-form>
    </template>
    <!--表格-->
    <template>
      <div id="tab">
        <a-tabs
          type="card"
          @change="callback"
        >
          <a-tab-pane
            key="1"
            tab="待落实"
          >
            <ybAppealResultDeductImplement-stayed
              ref="ybAppealResultDeductImplementStayed"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="defaultApplyDate"
              :searchDataType="searchDataType"
              :searchItem="searchItem"
            >
            </ybAppealResultDeductImplement-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已落实"
          >
            <ybAppealResultDeductImplement-complete
              ref="ybAppealResultDeductImplementComplete"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="defaultApplyDate"
              :searchItem="searchItem"
              :searchDataType="searchDataType"
            >
            </ybAppealResultDeductImplement-complete>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealResultDeductImplementComplete from './YbAppealResultDeductImplementComplete'
import YbAppealResultDeductImplementStayed from './YbAppealResultDeductImplementStayed'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
const formItemLayout1 = {
  labelCol: { span: 10 },
  wrapperCol: { span: 13, offset: 1 }
}
export default {
  name: 'YbAppealResultDeductImplementView',
  components: {
    YbAppealResultDeductImplementStayed, YbAppealResultDeductImplementComplete },
  data () {
    return {
      formItemLayout,
      formItemLayout1,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchItem: {keyField: 'serialNo', value: ''},
      searchDropDataSource: [
        {text: '交易流水号', value: 'serialNo'},
        {text: '项目编码', value: 'projectCode'},
        {text: '项目名称', value: 'projectName'},
        {text: '序号', value: 'orderNumber'}
      ],
      searchDataType: 0,
      searchApplyDate: this.formatDate(),
      searchToApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      selectDataTypeDataSource: [
        {text: '全部', value: 2},
        {text: '明细扣款', value: 0},
        {text: '主单扣款', value: 1}
      ],
      applyDateText: '复议年月',
      tableSelectKey: '1'
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    monthToChange (date, dateString) {
      this.searchToApplyDate = dateString
    },
    handleDataTypeChange (value) {
      this.searchDataType = value
      // setTimeout(() => {
      //   this.searchTable()
      // }, 200)
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.applyDateText = '复议年月'
        this.$refs.ybAppealResultDeductImplementStayed.searchPage()
      } else if (key === '2') {
        this.applyDateText = '绩效落实年月'
        this.$refs.ybAppealResultDeductImplementComplete.searchPage()
      } else {
        console.log('ok')
      }
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
