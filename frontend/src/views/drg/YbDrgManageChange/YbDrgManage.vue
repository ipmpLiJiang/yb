<template>
  <a-card :bordered="false" class="card-area">
    <template>
      <div style="margin-bottom: 16px">
        <a-row>
          复议年月：
          <a-month-picker
            placeholder="请输入复议年月"
            @change="monthChange"
            style="width: 105px;margin-right: 6px"
            :default-value="searchApplyDate"
            :format="monthFormat"
          />
          <a-select v-model="searchItem.keyField" style="width: 115px">
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
            <ybDrgManage-change
              ref="ybDrgManageChange"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @onHistoryLook="onHistoryLook"
              @examine="examine"
            >
            </ybDrgManage-change>
          </a-tab-pane>
          <a-tab-pane key="2" :forceRender="true" tab="已审核记录">
            <!-- 已审核记录 -->
            <ybDrgManageChange-end
              ref="ybDrgManageChangeEnd"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @onHistoryLook="onHistoryLook"
              @detail="detail"
            >
            </ybDrgManageChange-end>
          </a-tab-pane>
          <a-tab-pane key="3" :forceRender="true" tab="管理员更改(接受申请)">
            <!-- 接受申请 -->
            <ybDrgManage-accept
              ref="ybDrgManageAccept"
              :applyDate="searchApplyDate"
              :searchItem="searchItem"
              @adminChange="adminChange"
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybDrgManage-accept>
          </a-tab-pane>
          <a-tab-pane key="4" :forceRender="true" tab="管理员更改(待申诉)">
            <!-- 待申诉 -->
            <ybDrgManage-stayed
              ref="ybDrgManageStayed"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybDrgManage-stayed>
          </a-tab-pane>
          <a-tab-pane key="5" :forceRender="true" tab="管理员更改(已申诉)">
            <!-- 已申诉 -->
            <ybDrgManage-completed
              ref="ybDrgManageCompleted"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybDrgManage-completed>
          </a-tab-pane>
          <a-tab-pane key="6" :forceRender="true" tab="管理员更改(未申诉)">
            <!-- 未申诉 -->
            <ybDrgManage-overdue
              ref="ybDrgManageOverdue"
              :searchItem="searchItem"
              :applyDate="searchApplyDate"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybDrgManage-overdue>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <!-- 接受申请-查看 -->
    <ybDrgManage-look
      ref="ybDrgManageLook"
      @close="handleLookClose"
      @success="handleLookSuccess"
      :lookVisiable="lookVisiable"
    >
    </ybDrgManage-look>
    <!-- 历史 -->
    <ybDrgManage-history
      ref="ybDrgManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybDrgManage-history>
    <!-- 变更处理 -->
    <ybDrgManageChange-handle
      ref="ybDrgManageChangeHandle"
      @close="handleExamineClose"
      @success="handleExamineSuccess"
      :handleVisiable="handleVisiable"
    >
    </ybDrgManageChange-handle>
    <!-- 变更详情 -->
    <ybDrgManageChange-detail
      ref="ybDrgManageChangeDetail"
      @close="handleDetailClose"
      @success="handleDetailSuccess"
      :detailVisiable="detailVisiable"
    >
    </ybDrgManageChange-detail>
    <ybDrgManageChangeAdmin-handle
      ref="ybDrgManageChangeAdminHandle"
      @close="handleAdminClose"
      @success="handleAdminSuccess"
      :adminVisiable="adminVisiable"
    >
    </ybDrgManageChangeAdmin-handle>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbDrgManageAccept from './YbDrgManageAccept'
import YbDrgManageChange from './YbDrgManageChange'
import YbDrgManageChangeEnd from './YbDrgManageChangeEnd'
import YbDrgManageStayed from './YbDrgManageStayed'
import YbDrgManageCompleted from './YbDrgManageCompleted'
import YbDrgManageLook from './YbDrgManageLook'
import YbDrgManageHistory from '../YbDrgFunModule/YbDrgManageHistoryModule'
import YbDrgManageChangeHandle from './YbDrgManageChangeHandle.vue'
import YbDrgManageChangeDetail from './YbDrgManageChangeDetail'
import YbDrgManageChangeAdminHandle from './YbDrgManageChangeAdminHandle'
import YbDrgManageOverdue from './YbDrgManageOverdue'

export default {
  name: 'YbDrgManage',
  components: {
    YbDrgManageLook,
    YbDrgManageAccept,
    YbDrgManageChange,
    YbDrgManageChangeEnd,
    YbDrgManageStayed,
    YbDrgManageCompleted,
    YbDrgManageOverdue,
    YbDrgManageHistory,
    YbDrgManageChangeDetail,
    YbDrgManageChangeHandle,
    YbDrgManageChangeAdminHandle
  },
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      ybDrgManage: {},
      lookVisiable: false,
      historyVisiable: false,
      detailVisiable: false,
      handleVisiable: false,
      adminVisiable: false,
      searchItem: {
        keyField: 'ks',
        value: ''
      },
      searchDropDataSource: [{
        text: '科室',
        value: 'ks'
      },
      {
        text: '就诊记录号',
        value: 'jzjlh'
      },
      {
        text: '病案号',
        value: 'bah'
      },
      {
        text: '医生工号',
        value: 'readyDoctorCode'
      },
      {
        text: '医生姓名',
        value: 'readyDoctorName'
      },
      {
        text: '序号',
        value: 'orderNumber'
      }
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
        this.$refs.ybDrgManageChange.searchPage()
      } else if (key === '2') {
        this.$refs.ybDrgManageChangeEnd.searchPage()
      } else if (key === '3') {
        this.$refs.ybDrgManageAccept.searchPage()
      } else if (key === '4') {
        this.$refs.ybDrgManageStayed.searchPage()
      } else if (key === '5') {
        this.$refs.ybDrgManageCompleted.searchPage()
      } else if (key === '6') {
        this.$refs.ybDrgManageOverdue.searchPage()
      } else {
        console.log('7')
      }
    },
    handleAdminSuccess (type) {
      this.adminVisiable = false
      if (type === 0) {
        this.$refs.ybDrgManageAccept.search()
      } else if (type === 1) {
        this.$refs.ybDrgManageStayed.search()
      } else if (type === 3) {
        this.$refs.ybDrgManageCompleted.search()
      } else {
        this.$refs.ybDrgManageOverdue.search()
      }
    },
    handleAdminClose () {
      this.adminVisiable = false
    },
    adminChange (record, type) {
      this.adminVisiable = true
      this.$refs.ybDrgManageChangeAdminHandle.setFormValues(record, type)
    },
    handleDetailClose () {
      this.detailVisiable = false
    },
    handleDetailSuccess () {
      this.detailVisiable = false
    },
    detail (record) {
      this.detailVisiable = true
      this.$refs.ybDrgManageChangeDetail.setFormValues(record)
    },
    handleLookSuccess () {
      this.lookVisiable = false
    },
    handleLookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      this.$refs.ybDrgManageLook.setFormValues(record)
    },
    handleExamineSuccess () {
      this.handleVisiable = false
      this.$refs.ybDrgManageChange.search()
    },
    handleExamineClose () {
      this.handleVisiable = false
    },
    examine (record) {
      this.handleVisiable = true
      this.$refs.ybDrgManageChangeHandle.setFormValues(record)
    },
    handleHistorySuccess () {
      this.historyVisiable = false
    },
    handleHistoryClose () {
      this.historyVisiable = false
    },
    onHistoryLook (record) {
      this.historyVisiable = true
      this.$refs.ybDrgManageHistory.setFormValues(record)
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    },
    onHistory () {
      let key = this.tableSelectKey
      if (key === '1') {
        this.$refs.ybDrgManageChange.onHistory()
      } else if (key === '2') {
        this.$refs.ybDrgManageChangeEnd.onHistory()
      } else if (key === '3') {
        this.$refs.ybDrgManageAccept.onHistory()
      } else if (key === '4') {
        this.$refs.ybDrgManageStayed.onHistory()
      } else if (key === '5') {
        this.$refs.ybDrgManageCompleted.onHistory()
      } else if (key === '6') {
        this.$refs.ybDrgManageOverdue.onHistory()
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
