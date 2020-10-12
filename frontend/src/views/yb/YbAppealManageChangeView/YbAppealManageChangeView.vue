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
          <a-col :span=7>
            <a-button
            type="primary"
            style="margin-right:20px"
            @click="onHistory"
            >历史操作记录</a-button>
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
            tab="变更申请单"
          >
              <!-- 变更申请单 -->
            <ybAppealManage-change
              ref="ybAppealManageChange"
              :applyDate='formatDate()'
              :searchText = 'searchText'
              @examine="examine"
              @onHistoryLook="onHistoryLook"
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
              :searchText = 'searchText'
              :applyDate='formatDate()'
              @detail="detail"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManageChange-end>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="管理员更改"
          >
          <!-- 管理员更改 -->
          <ybAppealManageChange-admin
              ref="ybAppealManageChangeAdmin"
              :searchText = 'searchText'
              :applyDate='formatDate()'
              @detail="detail"
              @adminChange="adminChange"
              @onHistoryLook="onHistoryLook"
            >
            </ybAppealManageChange-admin>
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
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealManageChange from './YbAppealManageChange'
import YbAppealManageChangeEnd from './YbAppealManageChangeEnd'
import YbAppealManageChangeAdmin from './YbAppealManageChangeAdmin'
import YbAppealManageChangeDetail from './YbAppealManageChangeDetail'
import YbAppealManageChangeHandle from './YbAppealManageChangeHandle'
import YbAppealManageChangeAdminHandle from './YbAppealManageChangeAdminHandle'
import YbAppealManageHistory from '../ybFunModule/YbAppealManageHistoryModule'
export default {
  name: 'YbAppealManageChangeView',
  components: {
    YbAppealManageChange, YbAppealManageChangeEnd, YbAppealManageChangeAdmin, YbAppealManageChangeHandle, YbAppealManageChangeDetail, YbAppealManageHistory, YbAppealManageChangeAdminHandle},
  data () {
    return {
      monthFormat: 'YYYY-MM',
      searchApplyDate: this.formatDate(),
      detailVisiable: false,
      handleVisiable: false,
      historyVisiable: false,
      adminVisiable: false,
      searchText: '',
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
      let datemonth = moment().format('YYYY-MM')
      return datemonth
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybAppealManageChange.search()
      } else if (key === '2') {
        this.$refs.ybAppealManageChangeEnd.search()
      } else if (key === '3') {
        this.$refs.ybAppealManageChangeAdmin.search()
      } else {
        console.log('444')
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
    handleAdminSuccess () {
      this.adminVisiable = false
      this.$refs.ybAppealManageChangeAdmin.search()
    },
    handleAdminClose () {
      this.adminVisiable = false
    },
    adminChange (record) {
      this.adminVisiable = true
      this.$refs.ybAppealManageChangeAdminHandle.setFormValues(record)
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
      } else {
        console.log('4444')
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
