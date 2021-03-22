<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <div style="text-align:center;margin-bottom:20px">
        <a-row justify="center"
          align="middle">
          <a-col :span=6>
              复议年月：
              <a-month-picker
                placeholder="请输入复议年月"
                @change="monthChange"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
          </a-col>
          <a-col :span=4>
            版本类型：
            <a-select :value="searchTypeno" style="width: 100px" @change="handleTypenoChange">
              <a-select-option
              v-for="d in selectTypenoDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
          </a-col>
          <a-col :span=6>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==1?true:false">
            <a-popconfirm
              title="确定批量接受？"
              @confirm="batchAccept"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: 15px">批量接受</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=3 v-show="tableSelectKey!=1?true:false">
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
              :applyDate='searchApplyDate'
              :searchText='searchText'
              :searchTypeno='searchTypeno'
              @look="look"
              @reject="reject"
              @onHistoryLook="onHistoryLook"
              @setTypeno="setTypeno"
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
              :searchText='searchText'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @onHistoryLook="onHistoryLook"
              @look="look"
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
              :searchText='searchText'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
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
              :searchText='searchText'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @appealComplete='appealComplete'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybAppealManage-completed>
          </a-tab-pane>
          <a-tab-pane
            key="5"
            :forceRender="true"
            tab="未申诉"
          >
          <!-- 未申诉 -->
          <ybAppealManage-overdue
              ref="ybAppealManageOverdue"
              :searchText='searchText'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybAppealManage-overdue>
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
    <ybAppealManageUpload-complete
      ref="ybAppealManageUploadComplete"
      @close="handleAppealCompleteClose"
      :appealCompleteVisiable="appealCompleteVisiable"
    >
    </ybAppealManageUpload-complete>
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
import YbAppealManageUploadComplete from './YbAppealManageUploadComplete'
import YbAppealManageOverdue from './YbAppealManageOverdue'

export default {
  name: 'YbAppealManageView',
  components: {
    YbAppealManageAccept, YbAppealManageRefused, YbAppealManageLook, YbAppealManageReject, YbAppealManageStayed, YbAppealManageCompleted, YbAppealManageHistory, YbAppealManageUpload, YbAppealManageUploadComplete, YbAppealManageOverdue},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      ybAppealManage: {},
      rejectVisiable: false,
      lookVisiable: false,
      historyVisiable: false,
      appealVisiable: false,
      appealCompleteVisiable: false,
      searchText: '',
      tableSelectKey: '1',
      searchTypeno: 1,
      user: this.$store.state.account.user,
      selectTypenoDataSource: [{text: '全部', value: 0}, {text: '版本一', value: 1}, {text: '版本二', value: 2}, {text: '人工复议', value: 3}]
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
      this.initTypeno(dateString)
    },
    initTypeno (applyDateStr) {
      this.$get('ybReconsiderApply/getTypeno', {
        applyDateStr: applyDateStr, areaType: this.user.areaType
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.searchTypeno = parseInt(r.data.data.data)
        } else {
          this.searchTypeno = 1
        }
      })
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybAppealManageAccept.searchPage()
      } else if (key === '2') {
        this.$refs.ybAppealManageRefused.searchPage()
      } else if (key === '3') {
        this.$refs.ybAppealManageStayed.searchPage()
      } else if (key === '4') {
        this.$refs.ybAppealManageCompleted.searchPage()
      } else {
        this.$refs.ybAppealManageOverdue.searchPage()
      }
    },
    handleTypenoChange (value) {
      this.searchTypeno = value
      setTimeout(() => {
        this.callback(this.tableSelectKey)
      }, 100)
    },
    setTypeno (value) {
      this.searchTypeno = value
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
    handleAppealCompleteClose () {
      this.appealCompleteVisiable = false
      // 保存数据返回时，申诉理由未更新，刷新后更新数据
      this.$refs.ybAppealManageCompleted.search()
    },
    appealComplete (record) {
      this.appealCompleteVisiable = true
      this.$refs.ybAppealManageUploadComplete.setFormValues(record)
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
      } else if (key === '4') {
        this.$refs.ybAppealManageCompleted.onHistory()
      } else {
        this.$refs.ybAppealManageOverdue.onHistory()
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
