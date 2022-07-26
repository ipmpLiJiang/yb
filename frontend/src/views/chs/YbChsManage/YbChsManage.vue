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
            style="width: 105px;margin-right: 3px"
            :default-value="searchApplyDate"
            :format="monthFormat"
          />
          <a-select v-model="searchItem.keyField"
            style="width: 115px">
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
          <a-popconfirm
            title="确定批量接受？"
            v-show="tableSelectKey==1?true:false"
            @confirm="batchAccept"
            okText="确定"
            cancelText="取消"
            :disabled="!hasSelected"
          >
            <a-button type="primary" :disabled="!hasSelected" style="margin-right: 22px">批量接受</a-button>
          </a-popconfirm>
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
              @acceptSelectedRow="acceptSelectedRow"
              @reject="reject"
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
              @chs='chs'
              @onHistoryLook="onHistoryLook"
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
              @chsComplete='chsComplete'
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
    <!-- 接受申请-拒绝 -->
    <ybChsManage-reject
      ref="ybChsManageReject"
      @close="handleRejectClose"
      @success="handleRejectSuccess"
      :rejectVisiable="rejectVisiable"
    >
    </ybChsManage-reject>
    <!-- 历史 -->
    <ybChsManage-history
      ref="ybChsManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybChsManage-history>
    <ybChsManage-upload
      ref="ybChsManageUpload"
      @close="handleChsClose"
      @success="handleChsSuccess"
      :chsVisiable="chsVisiable"
    >
    </ybChsManage-upload>
    <ybChsManageUpload-complete
      ref="ybChsManageUploadComplete"
      @close="handleChsCompleteClose"
      :chsCompleteVisiable="chsCompleteVisiable"
    >
    </ybChsManageUpload-complete>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbChsManageAccept from './YbChsManageAccept'
import YbChsManageRefused from './YbChsManageRefused'
import YbChsManageStayed from './YbChsManageStayed'
import YbChsManageCompleted from './YbChsManageCompleted'
import YbChsManageLook from './YbChsManageLook'
import YbChsManageReject from './YbChsManageReject'
import YbChsManageHistory from '../YbChsFunModule/YbChsManageHistoryModule'
import YbChsManageUpload from './YbChsManageUpload'
import YbChsManageUploadComplete from './YbChsManageUploadComplete'
import YbChsManageOverdue from './YbChsManageOverdue'

export default {
  name: 'YbChsManageView',
  components: {
    YbChsManageLook, YbChsManageAccept, YbChsManageRefused, YbChsManageReject, YbChsManageStayed, YbChsManageCompleted, YbChsManageOverdue, YbChsManageUpload, YbChsManageUploadComplete, YbChsManageHistory},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      ybChsManage: {},
      rejectVisiable: false,
      lookVisiable: false,
      historyVisiable: false,
      chsVisiable: false,
      chsCompleteVisiable: false,
      searchItem: {keyField: 'zymzNumber', value: ''},
      searchDropDataSource: [
        {text: '住院门诊号', value: 'zymzNumber'},
        {text: '参保人', value: 'insuredName'},
        {text: '项目名称', value: 'projectName'},
        {text: '序号', value: 'orderNum'}
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
    handleChsSuccess () {
      this.chsVisiable = false
      this.$refs.ybChsManageStayed.search()
    },
    handleChsClose () {
      this.chsVisiable = false
      // 保存数据返回时，医院意见未更新，刷新后更新数据
      this.$refs.ybChsManageStayed.search()
    },
    chs (record) {
      this.chsVisiable = true
      this.$refs.ybChsManageUpload.setFormValues(record)
    },
    handleChsCompleteClose () {
      this.chsCompleteVisiable = false
      // 保存数据返回时，医院意见未更新，刷新后更新数据
      this.$refs.ybChsManageCompleted.search()
    },
    chsComplete (record) {
      this.chsCompleteVisiable = true
      this.$refs.ybChsManageUploadComplete.setFormValues(record)
    },
    handleRejectSuccess () {
      this.rejectVisiable = false
      this.$refs.ybChsManageAccept.search()
    },
    handleRejectClose () {
      this.rejectVisiable = false
    },
    reject (record) {
      this.rejectVisiable = true
      this.$refs.ybChsManageReject.setFormValues(record)
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    },
    batchAccept () {
      this.$refs.ybChsManageAccept.batchAccept()
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
