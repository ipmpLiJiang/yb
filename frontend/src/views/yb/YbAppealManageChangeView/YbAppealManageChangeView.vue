<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <div style="text-align:center;margin-bottom:20px">
        <a-row justify="center"
          align="middle">
          <a-col :span=5>
              复议年月：
              <a-month-picker
                placeholder="请输入复议年月"
                style="width: 120px"
                @change="monthChange"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
          </a-col>
          <a-col :span=5>
            版本类型：
            <a-select :value="searchTypeno" style="width: 110px" @change="handleTypenoChange">
              <a-select-option
              v-for="d in selectTypenoDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
          </a-col>
          <a-col :span=8>
            <a-select v-model="searchItem.keyField" style="width: 115px">
              <a-select-option
              v-for="d in searchDropDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            =
            <a-input-search placeholder="请输入关键字" v-model="searchItem.value" style="width: 170px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=3>
            <a-button
            type="primary"
            style="margin-right:10px"
            @click="onHistory"
            >历史操作记录</a-button>
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
            tab="变更申请单"
          >
              <!-- 变更申请单 -->
            <ybAppealManage-change
              ref="ybAppealManageChange"
              :applyDate='searchApplyDate'
              :searchItem = 'searchItem'
              :searchTypeno='searchTypeno'
              @examine="examine"
              @onHistoryLook="onHistoryLook"
              @setTypeno="setTypeno"
            >
            </ybAppealManage-change>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已审核记录"
          >
          <!-- 已审核记录 -->
            <ybAppealManageChange-end
              ref="ybAppealManageChangeEnd"
              :searchItem = 'searchItem'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @detail="detail"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManageChange-end>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="管理员更改(接受申请)"
          >
          <!-- 管理员更改 -->
          <ybAppealManageChange-admin
              ref="ybAppealManageChangeAdmin"
              :searchItem = 'searchItem'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @detail="detail"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManageChange-admin>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="管理员更改(待申诉)"
          >
          <!-- 管理员更改 -->
          <ybAppealManageChange-admin1
              ref="ybAppealManageChangeAdmin1"
              :searchItem = 'searchItem'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @detail="detail"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybAppealManageChange-admin1>
          </a-tab-pane>
          <a-tab-pane
            key="5"
            :forceRender="true"
            tab="管理员更改(已申诉)"
          >
          <!-- 管理员更改 -->
          <ybAppealManageChange-Completed
              ref="ybAppealManageChangeCompleted"
              :searchItem = 'searchItem'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @detail="detail"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybAppealManageChange-Completed>
          </a-tab-pane>
          <a-tab-pane
            key="6"
            :forceRender="true"
            tab="管理员更改(未申诉)"
          >
          <!-- 管理员更改 -->
          <ybAppealManageChange-Expire
              ref="ybAppealManageChangeExpire"
              :searchItem = 'searchItem'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              @detail="detail"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
              @look="look"
            >
            </ybAppealManageChange-Expire>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <!-- 变更处理 -->
    <ybAppealManageChange-handle
      ref="ybAppealManageChangeHandle"
      @close="handleExamineClose"
      @success="handleExamineSuccess"
      :handleVisiable="handleVisiable"
    >
    </ybAppealManageChange-handle>
    <!-- 变更详情 -->
    <ybAppealManageChange-detail
      ref="ybAppealManageChangeDetail"
      @close="handleDetailClose"
      @success="handleDetailSuccess"
      :detailVisiable="detailVisiable"
    >
    </ybAppealManageChange-detail>
    <!-- 历史 -->
    <ybAppealManage-history
      ref="ybAppealManageHistory"
      @close="handleHistoryClose"
      @success="handleHistorySuccess"
      :historyVisiable="historyVisiable"
    >
    </ybAppealManage-history>
    <ybAppealManageChangeAdmin-handle
    ref="ybAppealManageChangeAdminHandle"
      @close="handleAdminClose"
      @success="handleAdminSuccess"
      :adminVisiable="adminVisiable"
    >
    </ybAppealManageChangeAdmin-handle>
     <!-- 接受申请-查看 -->
    <ybAppealManage-look
      ref="ybAppealManageLook"
      @close="handleLookClose"
      @success="handleLookSuccess"
      :lookVisiable="lookVisiable"
    >
    </ybAppealManage-look>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealManageChange from './YbAppealManageChange'
import YbAppealManageChangeEnd from './YbAppealManageChangeEnd'
import YbAppealManageLook from './YbAppealManageLook'
import YbAppealManageChangeAdmin from './YbAppealManageChangeAdmin'
import YbAppealManageChangeAdmin1 from './YbAppealManageChangeAdmin1'
import YbAppealManageChangeCompleted from './YbAppealManageChangeCompleted'
import YbAppealManageChangeExpire from './YbAppealManageChangeExpire'
import YbAppealManageChangeDetail from './YbAppealManageChangeDetail'
import YbAppealManageChangeHandle from './YbAppealManageChangeHandle'
import YbAppealManageChangeAdminHandle from './YbAppealManageChangeAdminHandle'
import YbAppealManageHistory from '../ybFunModule/YbAppealManageHistoryModule'
export default {
  name: 'YbAppealManageChangeView',
  components: {
    YbAppealManageChange, YbAppealManageChangeEnd, YbAppealManageChangeAdmin, YbAppealManageChangeAdmin1, YbAppealManageChangeHandle, YbAppealManageChangeDetail, YbAppealManageHistory, YbAppealManageChangeAdminHandle, YbAppealManageChangeCompleted, YbAppealManageLook, YbAppealManageChangeExpire},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      detailVisiable: false,
      handleVisiable: false,
      historyVisiable: false,
      adminVisiable: false,
      lookVisiable: false,
      searchItem: {keyField: 'serialNo', value: ''},
      searchDropDataSource: [
        {text: '交易流水号', value: 'serialNo'},
        {text: '单据号', value: 'billNo'},
        {text: '项目编码', value: 'projectCode'},
        {text: '项目名称', value: 'projectName'},
        {text: '医生工号', value: 'readyDoctorCode'},
        {text: '医生姓名', value: 'readyDoctorName'},
        {text: '序号', value: 'orderNumber'}
      ],
      tableSelectKey: '1',
      searchTypeno: 1,
      user: this.$store.state.account.user,
      selectTypenoDataSource: [{text: '全部', value: 0}, {text: '版本一', value: 1}, {text: '版本二', value: 2}, {text: '非常规复议', value: 3}]
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
        applyDateStr: applyDateStr, areaType: this.user.areaType.value
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.searchTypeno = parseInt(r.data.data.data)
        } else {
          this.searchTypeno = 1
        }
      })
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
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybAppealManageChange.searchPage()
      } else if (key === '2') {
        this.$refs.ybAppealManageChangeEnd.searchPage()
      } else if (key === '3') {
        this.$refs.ybAppealManageChangeAdmin.searchPage()
      } else if (key === '4') {
        this.$refs.ybAppealManageChangeAdmin1.searchPage()
      } else if (key === '5') {
        this.$refs.ybAppealManageChangeCompleted.searchPage()
      } else if (key === '6') {
        this.$refs.ybAppealManageChangeExpire.searchPage()
      } else {
        console.log('7')
      }
    },
    handleExamineSuccess () {
      this.handleVisiable = false
      this.$refs.ybAppealManageChange.search()
    },
    handleExamineClose () {
      this.handleVisiable = false
    },
    examine (record) {
      this.handleVisiable = true
      this.$refs.ybAppealManageChangeHandle.setFormValues(record)
    },
    handleDetailSuccess () {
      this.detailVisiable = false
    },
    handleDetailClose () {
      this.detailVisiable = false
    },
    detail (record) {
      this.detailVisiable = true
      this.$refs.ybAppealManageChangeDetail.setFormValues(record)
    },
    handleAdminSuccess (type) {
      this.adminVisiable = false
      if (type === 0) {
        this.$refs.ybAppealManageChangeAdmin.search()
      } else if (type === 1) {
        this.$refs.ybAppealManageChangeAdmin1.search()
      } else if (type === 3) {
        this.$refs.ybAppealManageChangeCompleted.search()
      } else {
        this.$refs.ybAppealManageChangeExpire.search()
      }
    },
    handleAdminClose () {
      this.adminVisiable = false
    },
    adminChange (record, type) {
      this.adminVisiable = true
      this.$refs.ybAppealManageChangeAdminHandle.setFormValues(record, type)
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
    searchTable () {
      this.callback(this.tableSelectKey)
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
        this.$refs.ybAppealManageChange.onHistory()
      } else if (key === '2') {
        this.$refs.ybAppealManageChangeEnd.onHistory()
      } else if (key === '3') {
        this.$refs.ybAppealManageChangeAdmin.onHistory()
      } else if (key === '4') {
        this.$refs.ybAppealManageChangeAdmin1.onHistory()
      } else if (key === '5') {
        this.$refs.ybAppealManageChangeCompleted.onHistory()
      } else if (key === '6') {
        this.$refs.ybAppealManageChangeExpire.onHistory()
      } else {
        console.log('7')
      }
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
