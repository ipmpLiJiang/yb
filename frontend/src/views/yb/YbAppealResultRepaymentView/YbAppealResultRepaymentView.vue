
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <a-form layout="horizontal">
        <a-row justify="center" type="flex">
          <a-col :span=4>
            <a-form-item
              label="复议年月"
              v-bind="formItemLayout"
            >
              <a-month-picker
                @change="monthChange"
                :default-value="defaultApplyDate"
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
                :default-value="defaultApplyDate"
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
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 170px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=3>
            <a-popconfirm
              title="确定批量还款？"
              v-show="tableSelectKey==='3'?true:false"
              @confirm="batchRepayment"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: 15px">批量还款</a-button>
            </a-popconfirm>
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
            tab="待还款[需录入]"
          >
            <ybAppealResultRepayment-stayed
              ref="ybAppealResultRepaymentStayed"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="defaultApplyDate"
              :searchDataType="searchDataType"
              :searchText="searchText"
              @edit="edit"
            >
            </ybAppealResultRepayment-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="待还款[不录入]"
          >
            <ybAppealResultRepaymentStayed-emp
              ref="ybAppealResultRepaymentStayedEmp"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="defaultApplyDate"
              :searchDataType="searchDataType"
              :searchText="searchText"
              @edit="edit"
            >
            </ybAppealResultRepaymentStayed-emp>
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
              :defaultFormatDate="defaultApplyDate"
              :searchText="searchText"
              :searchDataType="searchDataType"
              @edit="edit"
            >
            </ybAppealResultRepayment-complete>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <template>
      <div>
        <a-modal width="85%" :maskClosable="false" :footer="null" v-model="editVisible" title="录入还款方案" @cancel="handleOk">
          <ybAppealResultRepayment-edit
          ref="ybAppealResultRepaymentEdit"
          @close="handleOk"
          @success="handleSuccess"
          >
          </ybAppealResultRepayment-edit>
        </a-modal>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealResultRepaymentComplete from './YbAppealResultRepaymentComplete'
import YbAppealResultRepaymentStayed from './YbAppealResultRepaymentStayed'
import YbAppealResultRepaymentStayedEmp from './YbAppealResultRepaymentStayedEmp'
import YbAppealResultRepaymentEdit from './YbAppealResultRepaymentEdit'
import { data } from '../../js/custom'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbAppealResultRepaymentView',
  components: {
    YbAppealResultRepaymentStayed, YbAppealResultRepaymentStayedEmp, YbAppealResultRepaymentComplete, YbAppealResultRepaymentEdit},
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      searchDataType: 0,
      editVisible: false,
      searchApplyDate: this.formatDate(),
      searchToApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      selectDataTypeDataSource: [
      ],
      tableSelectKey: '1'
    }
  },
  computed: {
  },
  mounted () {
    this.selectDataTypeDataSource = data.seachDataTypeSource
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
    batchRepayment () {
      this.$refs.ybAppealResultRepaymentStayedEmp.batchRepayment()
    },
    edit (record) {
      this.showModal(record)
    },
    showModal (record) {
      this.editVisible = true
      setTimeout(() => {
        this.$refs.ybAppealResultRepaymentEdit.setFormValues(record)
      }, 200)
    },
    handleOk (e) {
      this.editVisible = false
    },
    handleSuccess () {
      this.editVisible = false
      this.$refs.ybAppealResultRepaymentStayed.search()
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
        this.$refs.ybAppealResultRepaymentStayed.searchPage()
      } else if (key === '2') {
        this.$refs.ybAppealResultRepaymentComplete.searchPage()
      } else if (key === '3') {
        this.$refs.ybAppealResultRepaymentStayedEmp.searchPage()
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
