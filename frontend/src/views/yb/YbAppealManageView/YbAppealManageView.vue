<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <div style="text-align:center;margin-bottom:20px">
        <a-row justify="center"
          align="middle">
          <a-col :span=7>
              复议年月：
              <a-month-picker
                placeholder="请输入复议年月"
                :default-value="formatDate()"
                :format="monthFormat"
              />
          </a-col>
          <a-col :span=7>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==1?true:false">
            <a-button
            type="primary"
            @click="batchAccept"
            >批量接受</a-button>
          </a-col>
          <a-col :span=3>
            <a-button
            type="primary"
            @click="onHistory"
            >历史操作记录</a-button>
          </a-col>
          <a-col :span=2>
            <a-button
              type="primary"
              @click="searchTable"
            >刷新</a-button>
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
            tab="接受申请"
          >
              <!-- 接受申请 -->
            <ybAppealManage-accept
              ref="ybAppealManageAccept"
              :applyDate='formatDate()'
              :searchText = 'searchText'
              @look="look"
              @reject="reject"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManage-accept>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已拒绝"
          >
          <!-- 已拒绝 -->
            <ybAppealManage-refused
              ref="ybAppealManageRefused"
              :searchText = 'searchText'
              :applyDate='formatDate()'
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManage-refused>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="待申诉"
          >
          <!-- 待申诉 -->
          <ybAppealManage-stayed
              ref="ybAppealManageStayed"
              :searchText = 'searchText'
              :applyDate='formatDate()'
              @appeal='appeal'
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManage-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="已申诉"
          >
          <!-- 已申诉 -->
          <ybAppealManage-completed
              ref="ybAppealManageCompleted"
              :searchText = 'searchText'
              :applyDate='formatDate()'
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManage-completed>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <!-- 接受申请-查看 -->
    <ybAppealManage-look
      ref="ybAppealManageLook"
      @close="handleLookClose"
      @success="handleLookSuccess"
      :lookVisiable="lookVisiable"
    >
    </ybAppealManage-look>
    <!-- 接受申请-拒绝 -->
    <ybAppealManage-reject
      ref="ybAppealManageReject"
      @close="handleRejectClose"
      @success="handleRejectSuccess"
      :rejectVisiable="rejectVisiable"
    >
    </ybAppealManage-reject>
    <!-- 历史 -->
    <ybAppealManage-history
      ref="ybAppealManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybAppealManage-history>
    <ybAppealManage-upload
      ref="ybAppealManageUpload"
      @close="handleAppealClose"
      @success="handleAppealSuccess"
      :appealVisiable="appealVisiable"
    >
    </ybAppealManage-upload>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealManageAccept from './YbAppealManageAccept'
import YbAppealManageRefused from './YbAppealManageRefused'
import YbAppealManageStayed from './YbAppealManageStayed'
import YbAppealManageCompleted from './YbAppealManageCompleted'
import YbAppealManageLook from './YbAppealManageLook'
import YbAppealManageReject from './YbAppealManageReject'
import YbAppealManageHistory from '../ybFunModule/YbAppealManageHistoryModule'
import YbAppealManageUpload from './YbAppealManageUpload'

export default {
  name: 'YbAppealManageView',
  components: {
    YbAppealManageAccept, YbAppealManageRefused, YbAppealManageLook, YbAppealManageReject, YbAppealManageStayed, YbAppealManageCompleted, YbAppealManageHistory, YbAppealManageUpload},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: undefined,
      ybAppealManage: {},
      rejectVisiable: false,
      lookVisiable: false,
      historyVisiable: false,
      appealVisiable: false,
      searchText: '',
      tableSelectKey: '1'
    }
  },
  computed: {
  },
  mounted () {
    this.searchApplyDate = this.formatDate()
  },
  methods: {
    moment,
    formatDate () {
      return moment(Date.now()).format(this.monthFormat)
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybAppealManageAccept.search()
      } else if (key === '2') {
        this.$refs.ybAppealManageRefused.search()
      } else if (key === '3') {
        this.$refs.ybAppealManageStayed.search()
      } else {
        this.$refs.ybAppealManageCompleted.search()
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
      this.$refs.ybAppealManageLook.setFormValues(record)
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
    handleAppealSuccess () {
      this.appealVisiable = false
      this.$refs.ybAppealManageStayed.search()
    },
    handleAppealClose () {
      this.appealVisiable = false
      // 保存数据返回时，申诉理由未更新，刷新后更新数据
      this.$refs.ybAppealManageStayed.search()
    },
    appeal (record) {
      this.appealVisiable = true
      this.$refs.ybAppealManageUpload.setFormValues(record)
    },
    handleRejectSuccess () {
      this.rejectVisiable = false
      this.$refs.ybAppealManageAccept.search()
    },
    handleRejectClose () {
      this.rejectVisiable = false
    },
    reject (record) {
      this.rejectVisiable = true
      this.$refs.ybAppealManageReject.setFormValues(record)
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    },
    batchAccept () {
      this.$refs.ybAppealManageAccept.batchAccept()
    },
    onHistory () {
      let key = this.tableSelectKey
      if (key === '1') {
        this.$refs.ybAppealManageAccept.onHistory()
      } else if (key === '2') {
        this.$refs.ybAppealManageRefused.onHistory()
      } else if (key === '3') {
        this.$refs.ybAppealManageStayed.onHistory()
      } else {
        this.$refs.ybAppealManageCompleted.onHistory()
      }
    },
    reset () {
      console.log('reset')
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
