<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <div style="margin-bottom:16px">
        <a-row>
          复议年月：
          <a-month-picker
            placeholder="请输入复议年月"
            @change="monthChange"
            style="width: 115px;margin-right: 6px"
            :default-value="searchApplyDate"
            :format="monthFormat"
          />
          <a-select v-model="searchItem.keyField" style="width: 110px">
            <a-select-option
            v-for="d in searchDropDataSource"
            :key="d.value"
            >
            {{ d.text }}
            </a-select-option>
          </a-select>
          =
          <a-input-search
            placeholder="请输入关键字"
            v-model="searchItem.value"
            style="width: 180px;margin-right: 22px"
            enter-button
            @search="searchTable" />
          <a-button
          type="primary"
          @click="onHistory"
          >历史记录</a-button>
        </a-row>
      </div>
    </template>
    <template>
      <div id="tab">
        <a-tabs
          type="card"
          @change="callback"
        >
          <a-tab-pane
            key="1"
            tab="接受申请"
          >
              <!-- 接受申请 -->
            <ybChsManage-accept
              ref="ybChsManageAccept"
              :applyDate='searchApplyDate'
              :searchItem='searchItem'
              @look="look"
              @onHistoryLook="onHistoryLook"
            >
            </ybChsManage-accept>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已拒绝"
          >
          <!-- 已拒绝 -->
            <ybChsManage-refused
              ref="ybChsManageRefused"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybChsManage-refused>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="待申诉"
          >
          <!-- 待申诉 -->
          <ybChsManage-stayed
              ref="ybChsManageStayed"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybChsManage-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="已申诉"
          >
          <!-- 已申诉 -->
          <ybChsManage-completed
              ref="ybChsManageCompleted"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybChsManage-completed>
          </a-tab-pane>
          <a-tab-pane
            key="5"
            :forceRender="true"
            tab="未申诉"
          >
          <!-- 未申诉 -->
          <ybChsManage-overdue
              ref="ybChsManageOverdue"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
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
  </a-card>
</template>

<script>
import moment from 'moment'
import YbChsManageAccept from './YbChsManageAccept'
import YbChsManageRefused from './YbChsManageRefused'
import YbChsManageStayed from './YbChsManageStayed'
import YbChsManageCompleted from './YbChsManageCompleted'
import YbChsManageLook from './YbChsManageLook'
import YbChsManageHistory from '../YbChsFunModule/YbChsManageHistoryModule'
import YbChsManageOverdue from './YbChsManageOverdue'

export default {
  name: 'YbChsManage',
  components: {
    YbChsManageLook, YbChsManageAccept, YbChsManageRefused, YbChsManageStayed, YbChsManageCompleted, YbChsManageOverdue, YbChsManageHistory},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      ybChsManage: {},
      lookVisiable: false,
      historyVisiable: false,
      searchItem: {keyField: 'zymzNumber', value: ''},
      searchDropDataSource: [
        {text: '住院门诊号', value: 'zymzNumber'},
        {text: '参保人', value: 'insuredName'},
        {text: '项目名称', value: 'projectName'},
        {text: '医生工号', value: 'readyDoctorCode'},
        {text: '医生姓名', value: 'readyDoctorName'},
        {text: '序号', value: 'orderNum'}
      ],
      tableSelectKey: '1',
      selectedRowKeys: [],
      user: this.$store.state.account.user
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
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybChsManageAccept.searchPage()
      } else if (key === '2') {
        this.$refs.ybChsManageRefused.searchPage()
      } else if (key === '3') {
        this.$refs.ybChsManageStayed.searchPage()
      } else if (key === '4') {
        this.$refs.ybChsManageCompleted.searchPage()
      } else {
        this.$refs.ybChsManageOverdue.searchPage()
      }
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
        this.$refs.ybChsManageAccept.onHistory()
      } else if (key === '2') {
        this.$refs.ybChsManageRefused.onHistory()
      } else if (key === '3') {
        this.$refs.ybChsManageStayed.onHistory()
      } else if (key === '4') {
        this.$refs.ybChsManageCompleted.onHistory()
      } else {
        this.$refs.ybChsManageOverdue.onHistory()
      }
    },
    reset () {
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
