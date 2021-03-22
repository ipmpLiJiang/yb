
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
        <a-row justify="center" type="flex">
          <a-col :span=6>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
            <a-month-picker
              placeholder="请输入复议年月"
              @change="monthChange"
              :default-value="defaultApplyDate"
              :format="monthFormat"
            />
            </a-form-item>
          </a-col>
          <a-col :span=3>
            <a-select :value="searchDataType" style="width: 100px" @change="handleDataTypeChange">
              <a-select-option
              v-for="d in selectDataTypeDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
          </a-col>
          <a-col :span=5>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=9>
            <a-button
            type="primary"
            style="margin-right:15px"
            @click="exportExcel"
            >导出表格</a-button>
            <a-popconfirm
              title="请选择导出类型"
              okText="单个科室"
              cancelText="汇总科室"
              style="margin-right:15px"
              @cancel="() => showModal(1)"
              @confirm="() => showModal(0)">
              <a-icon slot="icon" type="question-circle-o" style="color: orangered" />
              <a-button type="primary">导出图片</a-button>
            </a-popconfirm>
            <a-button
            type="primary"
            style="margin-right:15px"
            @click="onHistory"
            >历史操作记录</a-button>
            <a-button
              type="primary"
              @click="searchTable"
            >刷新</a-button>
          </a-col>
        </a-row>
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
            tab="审核意见一"
          >
            <ybAppealResult-one
              ref="ybAppealResultOne"
              :applyDate="searchApplyDate"
              :searchText ="searchText"
              :searchDataType="searchDataType"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealResult-one>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="审核意见二"
          >
            <ybAppealResult-two
              ref="ybAppealResultTwo"
              :applyDate="searchApplyDate"
              :searchText ="searchText"
              :searchDataType="searchDataType"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealResult-two>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="人工复议"
          >
            <ybAppealResult-handle
              ref="ybAppealResultHandle"
              :applyDate="searchApplyDate"
              :searchText ="searchText"
              :searchDataType="searchDataType"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealResult-handle>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <template>
      <div>
        <a-modal width="85%" :maskClosable="false" :footer="null" v-model="downLoadVisible" title="导出图片" @ok="handleOk">
          <ybAppealResult-downLoad
          ref="ybAppealResultDownload"
          :tableSelectKey="tableSelectKey"
          >
          </ybAppealResult-downLoad>
        </a-modal>
      </div>
    </template>
    <!-- 历史 -->
    <ybAppealManage-history
      ref="ybAppealManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybAppealManage-history>
    <!-- 查看 -->
    <ybAppealResult-look
      ref="ybAppealResultLook"
      @close="handleLookClose"
      :lookVisiable="lookVisiable"
    >
    </ybAppealResult-look>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealManageHistory from '../ybFunModule/YbAppealManageHistoryModule'
import YbAppealResultTwo from './YbAppealResultTwo'
import YbAppealResultOne from './YbAppealResultOne'
import YbAppealResultHandle from './YbAppealResultHandle'
import YbAppealResultLook from './YbAppealResultLook'
import YbAppealResultDownLoad from './YbAppealResultDownLoad'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 12, offset: 1 }
}
export default {
  name: 'YbAppealResultView',
  components: {
    YbAppealManageHistory, YbAppealResultOne, YbAppealResultTwo, YbAppealResultHandle, YbAppealResultLook, YbAppealResultDownLoad},
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      searchDataType: 0,
      historyVisiable: false,
      lookVisiable: false,
      downLoadVisible: false,
      searchApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
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
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    showModal (type) {
      this.downLoadVisible = true
      let appealResultDownLoad = {}
      appealResultDownLoad.applyDateStr = this.searchApplyDate
      if (this.tableSelectKey === '1') {
        appealResultDownLoad.typeno = 1
        appealResultDownLoad.sourceType = 0
      } else if (this.tableSelectKey === '2') {
        appealResultDownLoad.typeno = 2
        appealResultDownLoad.sourceType = 0
      } else {
        appealResultDownLoad.typeno = undefined
        appealResultDownLoad.sourceType = 1
      }
      appealResultDownLoad.dataType = this.searchDataType
      appealResultDownLoad.type = type
      setTimeout(() => {
        this.$refs.ybAppealResultDownload.setFormValues(appealResultDownLoad)
      }, 200)
    },
    handleOk (e) {
      this.downLoadVisible = false
    },
    exportExcel () {
      if (this.tableSelectKey === '1') {
        this.$refs.ybAppealResultOne.exportExcel()
      } else if (this.tableSelectKey === '2') {
        this.$refs.ybAppealResultTwo.exportExcel()
      } else if (this.tableSelectKey === '3') {
        this.$refs.ybAppealResultHandle.exportExcel()
      } else {
        console.log('exportExcel')
      }
    },
    handleLookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      this.$refs.ybAppealResultLook.setFormValues(record)
    },
    handleHistorySuccess () {
      this.historyVisiable = false
    },
    handleHistoryClose () {
      this.historyVisiable = false
    },
    onHistoryLook (record) {
      this.historyVisiable = true
      this.$refs.ybAppealManageHistory.setFormValues(record)
    },
    onHistory () {
      let key = this.tableSelectKey
      if (key === '1') {
        this.$refs.ybAppealResultOne.onHistory()
      } else if (key === '2') {
        this.$refs.ybAppealResultTwo.onHistory()
      } else if (key === '3') {
        this.$refs.ybAppealResultHandle.onHistory()
      } else {
        console.log('4444')
      }
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
        this.$refs.ybAppealResultOne.searchPage()
      } else if (key === '2') {
        this.$refs.ybAppealResultTwo.searchPage()
      } else if (key === '3') {
        this.$refs.ybAppealResultHandle.searchPage()
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
</style>
