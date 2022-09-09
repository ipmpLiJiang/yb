<template>
  <a-card :bordered="false" class="card-area">
    <template>
      <div style="margin-bottom: 16px">
        <a-row>
          复议年月：
          <a-month-picker
            placeholder="请输入复议年月"
            @change="monthChange"
            style="width: 105px;margin-right: 3px"
            :default-value="searchApplyDate"
            :format="monthFormat"
          />
          <a-select v-model="searchItem.keyField"
            style="width: 115px">
            <a-select-option v-for="d in searchDropDataSource" :key="d.value">
              {{ d.text }}
            </a-select-option>
          </a-select>
          =
          <a-input-search
            placeholder="请输入关键字"
            v-model="searchItem.value"
            style="width: 180px;margin-right: 22px"
            enter-button
            @search="searchTable"
          />
          <a-button type="primary" @click="onHistory">历史记录</a-button>
        </a-row>
      </div>
    </template>
    <template>
      <div id="tab">
        <a-tabs type="card" @change="callback">
          <a-tab-pane key="1" tab="变更申请单">
            <!-- 变更申请单 -->
            <ybChsManage-change
              ref="ybChsManageChange"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @onHistoryLook="onHistoryLook"
              @examine="examine"
            >
            </ybChsManage-change>
          </a-tab-pane>
          <a-tab-pane key="2" :forceRender="true" tab="已审核记录">
            <!-- 已审核记录 -->
            <ybChsManageChange-end
              ref="ybChsManageChangeEnd"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @onHistoryLook="onHistoryLook"
              @detail="detail"
            >
            </ybChsManageChange-end>
          </a-tab-pane>
          <a-tab-pane key="3" :forceRender="true" tab="管理员更改(接受申请)">
            <!-- 接受申请 -->
            <ybChsManage-accept
              ref="ybChsManageAccept"
              :applyDate="searchApplyDate"
              :searchItem="searchItem"
              @adminChange="adminChange"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybChsManage-accept>
          </a-tab-pane>
          <a-tab-pane key="4" :forceRender="true" tab="管理员更改(待申诉)">
            <!-- 待申诉 -->
            <ybChsManage-stayed
              ref="ybChsManageStayed"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybChsManage-stayed>
          </a-tab-pane>
          <a-tab-pane key="5" :forceRender="true" tab="管理员更改(已申诉)">
            <!-- 已申诉 -->
            <ybChsManage-completed
              ref="ybChsManageCompleted"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybChsManage-completed>
          </a-tab-pane>
          <a-tab-pane key="6" :forceRender="true" tab="管理员更改(未申诉)">
            <!-- 未申诉 -->
            <ybChsManage-overdue
              ref="ybChsManageOverdue"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybChsManage-overdue>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <!-- 接受申请-查看 -->
    <ybChsManage-look
      ref="ybChsManageLook"
      @close="handleLookClose"
      @success="handleLookSuccess"
      :lookVisiable="lookVisiable"
    >
    </ybChsManage-look>
    <!-- 历史 -->
    <ybChsManage-history
      ref="ybChsManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybChsManage-history>
    <!-- 变更处理 -->
    <ybChsManageChange-handle
      ref="ybChsManageChangeHandle"
      @close="handleExamineClose"
      @success="handleExamineSuccess"
      :handleVisiable="handleVisiable"
    >
    </ybChsManageChange-handle>
    <!-- 变更详情 -->
    <ybChsManageChange-detail
      ref="ybChsManageChangeDetail"
      @close="handleDetailClose"
      @success="handleDetailSuccess"
      :detailVisiable="detailVisiable"
    >
    </ybChsManageChange-detail>
    <ybChsManageChangeAdmin-handle
      ref="ybChsManageChangeAdminHandle"
      @close="handleAdminClose"
      @success="handleAdminSuccess"
      :adminVisiable="adminVisiable"
    >
    </ybChsManageChangeAdmin-handle>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbChsManageAccept from './YbChsManageAccept'
import YbChsManageChange from './YbChsManageChange'
import YbChsManageChangeEnd from './YbChsManageChangeEnd'
import YbChsManageStayed from './YbChsManageStayed'
import YbChsManageCompleted from './YbChsManageCompleted'
import YbChsManageLook from './YbChsManageLook'
import YbChsManageHistory from '../YbChsFunModule/YbChsManageHistoryModule'
import YbChsManageChangeHandle from './YbChsManageChangeHandle.vue'
import YbChsManageChangeDetail from './YbChsManageChangeDetail'
import YbChsManageChangeAdminHandle from './YbChsManageChangeAdminHandle'
import YbChsManageOverdue from './YbChsManageOverdue'

export default {
  name: 'YbChsManage',
  components: {
    YbChsManageLook,
    YbChsManageAccept,
    YbChsManageChange,
    YbChsManageChangeEnd,
    YbChsManageStayed,
    YbChsManageCompleted,
    YbChsManageOverdue,
    YbChsManageHistory,
    YbChsManageChangeDetail,
    YbChsManageChangeHandle,
    YbChsManageChangeAdminHandle
  },
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      ybChsManage: {},
      lookVisiable: false,
      historyVisiable: false,
      detailVisiable: false,
      handleVisiable: false,
      adminVisiable: false,
      searchItem: {
        keyField: 'zymzNumber',
        value: ''
      },
      searchDropDataSource: [
        {text: '住院门诊号', value: 'zymzNumber'},
        {text: '参保人', value: 'insuredName'},
        {text: '项目名称', value: 'projectName'},
        {text: '医生工号', value: 'readyDoctorCode'},
        {text: '医生姓名', value: 'readyDoctorName'},
        {text: '汇总科室', value: 'readyDksName'},
        {text: '序号', value: 'orderNum'}
      ],
      tableSelectKey: '1',
      selectedRowKeys: [],
      user: this.$store.state.account.user
    }
  },
  computed: {},
  mounted () { },
  methods: {
    moment,
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybChsManageChange.searchPage()
      } else if (key === '2') {
        this.$refs.ybChsManageChangeEnd.searchPage()
      } else if (key === '3') {
        this.$refs.ybChsManageAccept.searchPage()
      } else if (key === '4') {
        this.$refs.ybChsManageStayed.searchPage()
      } else if (key === '5') {
        this.$refs.ybChsManageCompleted.searchPage()
      } else if (key === '6') {
        this.$refs.ybChsManageOverdue.searchPage()
      } else {
        console.log('7')
      }
    },
    handleAdminSuccess (type) {
      this.adminVisiable = false
      if (type === 0) {
        this.$refs.ybChsManageAccept.search()
      } else if (type === 1) {
        this.$refs.ybChsManageStayed.search()
      } else if (type === 3) {
        this.$refs.ybChsManageCompleted.search()
      } else {
        this.$refs.ybChsManageOverdue.search()
      }
    },
    handleAdminClose () {
      this.adminVisiable = false
    },
    adminChange (record, type) {
      this.adminVisiable = true
      this.$refs.ybChsManageChangeAdminHandle.setFormValues(record, type)
    },
    handleDetailClose () {
      this.detailVisiable = false
    },
    handleDetailSuccess () {
      this.detailVisiable = false
    },
    detail (record) {
      this.detailVisiable = true
      this.$refs.ybChsManageChangeDetail.setFormValues(record)
    },
    handleLookSuccess () {
      this.lookVisiable = false
    },
    handleLookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      this.$refs.ybChsManageLook.setFormValues(record)
    },
    handleExamineSuccess () {
      this.handleVisiable = false
      this.$refs.ybChsManageChange.search()
    },
    handleExamineClose () {
      this.handleVisiable = false
    },
    examine (record) {
      this.handleVisiable = true
      this.$refs.ybChsManageChangeHandle.setFormValues(record)
    },
    handleHistorySuccess () {
      this.historyVisiable = false
    },
    handleHistoryClose () {
      this.historyVisiable = false
    },
    onHistoryLook (record) {
      this.historyVisiable = true
      this.$refs.ybChsManageHistory.setFormValues(record)
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    },
    onHistory () {
      let key = this.tableSelectKey
      if (key === '1') {
        this.$refs.ybChsManageChange.onHistory()
      } else if (key === '2') {
        this.$refs.ybChsManageChangeEnd.onHistory()
      } else if (key === '3') {
        this.$refs.ybChsManageAccept.onHistory()
      } else if (key === '4') {
        this.$refs.ybChsManageStayed.onHistory()
      } else if (key === '5') {
        this.$refs.ybChsManageCompleted.onHistory()
      } else if (key === '6') {
        this.$refs.ybChsManageOverdue.onHistory()
      } else {
        console.log('7')
      }
    },
    reset () { }
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
