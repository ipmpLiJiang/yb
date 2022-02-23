
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <div style="text-align:center;margin-bottom:16px">
        <a-row justify="start" type="flex">
          <a-col :span=5>
            复议年月：
            <a-month-picker
              placeholder="请输入复议年月"
              style="width: 120px"
              @change="monthChange"
              :default-value="defaultApplyDate"
              :format="monthFormat"
            />
          </a-col>
          <a-col :span=18>
            <a-select :value="searchDataType" style="width: 110px;margin-right: 10px" @change="handleDataTypeChange">
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
            <a-button style="margin-left: 15px;margin-right: 10px" @click="showCheckDksModal">验证汇科</a-button>
            <a-button
            type="primary"
            style="margin-right:10px"
            @click="exportExcel"
            >导出表格</a-button>
            <a-popover v-model="visibleTup" placement="top" trigger="click" title="请选择导出类型">
              <a-space slot="content" :size="8">
              <a-button slot="content" size="small" @click="showModal(1)">汇总病区</a-button>
              <a-button slot="content" size="small" type="primary"  @click="showModal(0)">单个病区</a-button>
              <a-button slot="content" size="small" @click="showModal(2)">汇总科室</a-button>
              </a-space>
            </a-popover>
            <!-- <a-popconfirm
              title="请选择导出类型"
              okText="单个科室"
              cancelText="汇总科室"
              style="margin-right:15px"
              @cancel="() => showModal(1)"
              @confirm="() => showModal(0)">
              <a-icon slot="icon" type="question-circle-o" style="color: orangered" />
              <a-button type="primary">导出图片</a-button>
            </a-popconfirm> -->
            <a-button type="primary" @click="tupOpen">导出图片</a-button>
            <a-button
            type="primary"
            style="margin-right:10px"
            @click="onHistory"
            >历史操作记录</a-button>
          </a-col>
        </a-row>
      </div>
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
              :searchItem ="searchItem"
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
              :searchItem ="searchItem"
              :searchDataType="searchDataType"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealResult-two>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="非常规复议-意见书"
          >
            <ybAppealResult-handle
              ref="ybAppealResultHandle"
              :applyDate="searchApplyDate"
              :searchItem ="searchItem"
              :searchDataType="searchDataType"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealResult-handle>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="非常规复议-剔除"
          >
            <ybAppealResult-handle1
              ref="ybAppealResultHandle1"
              :applyDate="searchApplyDate"
              :searchItem ="searchItem"
              :searchDataType="searchDataType"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealResult-handle1>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <template>
      <a-modal width="85%" :maskClosable="false" :footer="null" v-model="downLoadVisible" title="导出图片" @ok="handleOk">
        <ybAppealResult-downLoad
        ref="ybAppealResultDownload"
        :tableSelectKey="tableSelectKey"
        >
        </ybAppealResult-downLoad>
      </a-modal>
    </template>
    <template>
      <a-modal width="55%" :maskClosable="false" :footer="null" v-model="checkDksVisible" title="验证缺失汇总科室列表" @ok="handleCheckDksOk">
        <ybAppealResultCheck-dks
        ref="ybAppealResultCheckDks"
        >
        </ybAppealResultCheck-dks>
      </a-modal>
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
import YbAppealResultHandle1 from './YbAppealResultHandle1'
import YbAppealResultLook from './YbAppealResultLook'
import YbAppealResultDownLoad from './YbAppealResultDownLoad'
import YbAppealResultCheckDks from './YbAppealResultCheckDks'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 12, offset: 1 }
}
export default {
  name: 'YbAppealResultView',
  components: {
    YbAppealManageHistory, YbAppealResultOne, YbAppealResultTwo, YbAppealResultHandle, YbAppealResultHandle1, YbAppealResultLook, YbAppealResultDownLoad, YbAppealResultCheckDks},
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchItem: {keyField: 'serialNo', value: ''},
      searchDropDataSource: [
        {text: '交易流水号', value: 'serialNo'},
        {text: '单据号', value: 'billNo'},
        {text: '项目编码', value: 'projectCode'},
        {text: '项目名称', value: 'projectName'},
        {text: '医生工号', value: 'doctorCode'},
        {text: '医生姓名', value: 'doctorName'},
        {text: '序号', value: 'orderNumber'}
      ],
      searchDataType: 0,
      historyVisiable: false,
      lookVisiable: false,
      downLoadVisible: false,
      checkDksVisible: false,
      searchApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      visibleTup: false,
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
      this.visibleTup = false
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
    showCheckDksModal () {
      this.checkDksVisible = true
      let appealResultCheck = {applyDateStr: this.searchApplyDate}
      setTimeout(() => {
        this.$refs.ybAppealResultCheckDks.setFormValues(appealResultCheck)
      }, 200)
    },
    handleOk (e) {
      this.downLoadVisible = false
    },
    handleCheckDksOk (e) {
      this.checkDksVisible = false
    },
    exportExcel () {
      if (this.tableSelectKey === '1') {
        this.$refs.ybAppealResultOne.exportExcel()
      } else if (this.tableSelectKey === '2') {
        this.$refs.ybAppealResultTwo.exportExcel()
      } else if (this.tableSelectKey === '3') {
        this.$refs.ybAppealResultHandle.exportExcel()
      } else if (this.tableSelectKey === '4') {
        this.$refs.ybAppealResultHandle1.exportExcel()
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
    tupOpen () {
      this.visibleTup = !this.visibleTup
    },
    onHistory () {
      let key = this.tableSelectKey
      if (key === '1') {
        this.$refs.ybAppealResultOne.onHistory()
      } else if (key === '2') {
        this.$refs.ybAppealResultTwo.onHistory()
      } else if (key === '3') {
        this.$refs.ybAppealResultHandle.onHistory()
      } else if (key === '4') {
        this.$refs.ybAppealResultHandle1.onHistory()
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
      } else if (key === '4') {
        this.$refs.ybAppealResultHandle1.searchPage()
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
