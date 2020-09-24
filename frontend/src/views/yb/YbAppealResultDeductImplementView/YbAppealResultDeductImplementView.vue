
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
                placeholder="请选择复议年月"
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
          <a-col :span=4>
              <a-form-item
                label="扣款类型"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="selectDataType"
                   @change="handleDataTypeChange"
                  style="width: 100px"
                >
                  <a-select-option
                    v-for="d in selectDataTypeSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
          </a-col>
          <a-col :span=5>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="search" />
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
            tab="待扣款"
          >
            <ybAppealResultDeductImplement-stayed
              ref="ybAppealResultDeductImplementStayed"
            >
            </ybAppealResultDeductImplement-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已扣款"
          >
            <ybAppealResultDeductImplement-complete
              ref="ybAppealResultDeductImplementComplete"
            >
            </ybAppealResultDeductImplement-complete>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <template>
      <div>
        <a-modal :width="800" :maskClosable="false" :footer="null" v-model="editVisible" title="导出图片" @ok="handleOk">
          <ybAppealResultDeductImplement-edit
          ref="ybAppealResultDeductImplementEdit"
          @close="handleOk"
          @success="handleSuccess"
          >
          </ybAppealResultDeductImplement-edit>
        </a-modal>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealResultDeductImplementComplete from './YbAppealResultDeductImplementComplete'
import YbAppealResultDeductImplementStayed from './YbAppealResultDeductImplementStayed'
import YbAppealResultDeductImplementEdit from './YbAppealResultDeductImplementEdit'
const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbAppealResultDeductImplementView',
  components: {
    YbAppealResultDeductImplementStayed, YbAppealResultDeductImplementComplete, YbAppealResultDeductImplementEdit},
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      searchDataType: 0,
      editVisible: false,
      searchApplyDate: this.formatDate(),
      selectDataTypeDataSource: [
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
      this.selectApplyDateStr = dateString
    },
    monthToChange (date, dateString) {
      this.selectToApplyDateStr = dateString
    },
    showModal () {
      this.editVisible = true
      let editParams = {}
      editParams.applyDateStr = this.searchApplyDate
      if (this.tableSelectKey === '1') {
        editParams.typeno = 1
        editParams.sourceType = 0
      } else if (this.tableSelectKey === '2') {
        editParams.typeno = 2
        editParams.sourceType = 0
      } else {
        editParams.typeno = undefined
        editParams.sourceType = 1
      }
      editParams.dataType = this.searchDataType
      setTimeout(() => {
        this.$refs.ybAppealResultDeductImplementEdit.setFormValues(editParams)
      }, 200)
    },
    handleOk (e) {
      this.editVisible = false
    },
    handleSuccess () {
      this.editVisible = false
      this.$refs.ybAppealResultDeductImplementStayed.search()
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
        this.$refs.ybAppealResultDeductImplementStayed.search()
      } else if (key === '2') {
        this.$refs.ybAppealResultDeductImplementComplete.search()
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
