
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <a-form layout="horizontal">
        <a-row justify="center" type="flex">
          <a-col :span=5>
            <a-form-item
              label="复议年月"
              v-bind="formItemLayout"
            >
              <a-month-picker
                @change="monthChange"
                :default-value="formatDate()"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=4>
            <a-form-item
              label="至"
              v-bind="formItemLayout"
            >
              <a-month-picker
                placeholder="请选择复议年月"
                @change="monthToChange"
                :default-value="formatDate()"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=5>
              <a-form-item
                label="扣款类型"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="searchDataType"
                   @change="handleDataTypeChange"
                  style="width: 100px"
                >
                  <a-select-option
                    v-for="d in selectDataTypeDataSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
          </a-col>
          <a-col :span=5>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="searchTable" />
          </a-col>
        </a-row>
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
            tab="待还款"
          >
            <ybAppealResultRepayment-stayed
              ref="ybAppealResultRepaymentStayed"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="formatDate()"
              :searchDataType="searchDataType"
              :searchText="searchText"
            >
            </ybAppealResultRepayment-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已还款"
          >
            <ybAppealResultRepayment-complete
              ref="ybAppealResultRepaymentComplete"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="formatDate()"
              :searchText="searchText"
              :searchDataType="searchDataType"
            >
            </ybAppealResultRepayment-complete>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealResultRepaymentComplete from './YbAppealResultRepaymentComplete'
import YbAppealResultRepaymentStayed from './YbAppealResultRepaymentStayed'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbAppealResultRepaymentView',
  components: {
    YbAppealResultRepaymentStayed, YbAppealResultRepaymentComplete },
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      searchDataType: 0,
      searchApplyDate: this.formatDate(),
      searchToApplyDate: this.formatDate(),
      selectDataTypeDataSource: [
        {text: '全部', value: 2},
        {text: '明细扣款', value: 0},
        {text: '主单扣款', value: 1}
      ],
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
      let datemonth = moment().format('YYYY-MM')
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
      setTimeout(() => {
        this.searchTable()
      }, 200)
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybAppealResultRepaymentStayed.search()
      } else if (key === '2') {
        this.$refs.ybAppealResultRepaymentComplete.search()
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
