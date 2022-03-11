<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <div style="text-align:center;margin-bottom:16px">
        <a-row justify="center"
          align="middle">
          <a-col :span=5>
              复议年月：
              <a-month-picker
                placeholder="请输入复议年月"
                @change="monthChange"
                style="width: 105px"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
          </a-col>
          <a-col :span=8>
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
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==1?true:false">
            <a-popconfirm
              title="确定批量接受？"
              @confirm="batchAccept"
              okText="确定"
              cancelText="取消"
              :disabled="!hasSelected"
            >
              <a-button type="primary" :disabled="!hasSelected" style="margin-right: 15px">批量接受</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=3 >
            <a-button
            type="primary"
            @click="onHistory"
            >历史记录</a-button>
          </a-col>
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
            tab="DRG接受申请"
          >
              <!-- 接受申请 -->
            <ybDrgManage-accept
              ref="ybDrgManageAccept"
              :applyDate='searchApplyDate'
              :searchItem='searchItem'
              @look="look"
              @acceptSelectedRow="acceptSelectedRow"
              @reject="reject"
              @onHistoryLook="onHistoryLook"
            >
            </ybDrgManage-accept>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="DRG已拒绝"
          >
          <!-- 已拒绝 -->
            <ybDrgManage-refused
              ref="ybDrgManageRefused"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybDrgManage-refused>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="DRG待申诉"
          >
          <!-- 待申诉 -->
          <ybDrgManage-stayed
              ref="ybDrgManageStayed"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @drg='drg'
              @onHistoryLook="onHistoryLook"
            >
            </ybDrgManage-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="DRG已申诉"
          >
          <!-- 已申诉 -->
          <ybDrgManage-completed
              ref="ybDrgManageCompleted"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @drgComplete='drgComplete'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybDrgManage-completed>
          </a-tab-pane>
          <a-tab-pane
            key="5"
            :forceRender="true"
            tab="DRG未申诉"
          >
          <!-- 未申诉 -->
          <ybDrgManage-overdue
              ref="ybDrgManageOverdue"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
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
    <!-- 接受申请-拒绝 -->
    <ybDrgManage-reject
      ref="ybDrgManageReject"
      @close="handleRejectClose"
      @success="handleRejectSuccess"
      :rejectVisiable="rejectVisiable"
    >
    </ybDrgManage-reject>
    <!-- 历史 -->
    <ybDrgManage-history
      ref="ybDrgManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybDrgManage-history>
    <ybDrgManage-upload
      ref="ybDrgManageUpload"
      @close="handleDrgClose"
      @success="handleDrgSuccess"
      :drgVisiable="drgVisiable"
    >
    </ybDrgManage-upload>
    <ybDrgManageUpload-complete
      ref="ybDrgManageUploadComplete"
      @close="handleDrgCompleteClose"
      :drgCompleteVisiable="drgCompleteVisiable"
    >
    </ybDrgManageUpload-complete>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbDrgManageAccept from './YbDrgManageAccept'
import YbDrgManageRefused from './YbDrgManageRefused'
import YbDrgManageStayed from './YbDrgManageStayed'
import YbDrgManageCompleted from './YbDrgManageCompleted'
import YbDrgManageLook from './YbDrgManageLook'
import YbDrgManageReject from './YbDrgManageReject'
import YbDrgManageHistory from '../YbDrgFunModule/YbDrgManageHistoryModule'
import YbDrgManageUpload from './YbDrgManageUpload'
import YbDrgManageUploadComplete from './YbDrgManageUploadComplete'
import YbDrgManageOverdue from './YbDrgManageOverdue'

export default {
  name: 'YbDrgManageView',
  components: {
    YbDrgManageLook, YbDrgManageAccept, YbDrgManageRefused, YbDrgManageReject, YbDrgManageStayed, YbDrgManageCompleted, YbDrgManageOverdue, YbDrgManageUpload, YbDrgManageUploadComplete, YbDrgManageHistory},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      ybDrgManage: {},
      rejectVisiable: false,
      lookVisiable: false,
      historyVisiable: false,
      drgVisiable: false,
      drgCompleteVisiable: false,
      searchItem: {keyField: 'ks', value: ''},
      searchDropDataSource: [
        {text: '科室', value: 'ks'},
        {text: '就诊记录号', value: 'jzjlh'},
        {text: '病案号', value: 'bah'},
        {text: '序号', value: 'orderNumber'}
      ],
      tableSelectKey: '1',
      selectedRowKeys: [],
      hasSelected: false,
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
    acceptSelectedRow (isDisabled) {
      this.hasSelected = isDisabled
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybDrgManageAccept.searchPage()
      } else if (key === '2') {
        this.$refs.ybDrgManageRefused.searchPage()
      } else if (key === '3') {
        this.$refs.ybDrgManageStayed.searchPage()
      } else if (key === '4') {
        this.$refs.ybDrgManageCompleted.searchPage()
      } else {
        this.$refs.ybDrgManageOverdue.searchPage()
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
      this.$refs.ybDrgManageLook.setFormValues(record)
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
    handleDrgSuccess () {
      this.drgVisiable = false
      this.$refs.ybDrgManageStayed.search()
    },
    handleDrgClose () {
      this.drgVisiable = false
      // 保存数据返回时，申诉理由未更新，刷新后更新数据
      this.$refs.ybDrgManageStayed.search()
    },
    drg (record) {
      this.drgVisiable = true
      this.$refs.ybDrgManageUpload.setFormValues(record)
    },
    handleDrgCompleteClose () {
      this.drgCompleteVisiable = false
      // 保存数据返回时，申诉理由未更新，刷新后更新数据
      this.$refs.ybDrgManageCompleted.search()
    },
    drgComplete (record) {
      this.drgCompleteVisiable = true
      this.$refs.ybDrgManageUploadComplete.setFormValues(record)
    },
    handleRejectSuccess () {
      this.rejectVisiable = false
      this.$refs.ybDrgManageAccept.search()
    },
    handleRejectClose () {
      this.rejectVisiable = false
    },
    reject (record) {
      this.rejectVisiable = true
      this.$refs.ybDrgManageReject.setFormValues(record)
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    },
    batchAccept () {
      this.$refs.ybDrgManageAccept.batchAccept()
    },
    onHistory () {
      let key = this.tableSelectKey
      if (key === '1') {
        this.$refs.ybDrgManageAccept.onHistory()
      } else if (key === '2') {
        this.$refs.ybDrgManageRefused.onHistory()
      } else if (key === '3') {
        this.$refs.ybDrgManageStayed.onHistory()
      } else if (key === '4') {
        this.$refs.ybDrgManageCompleted.onHistory()
      } else {
        this.$refs.ybDrgManageOverdue.onHistory()
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
