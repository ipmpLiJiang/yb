<template>
  <a-card :bordered="false" class="card-area">
    <template>
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
        <div style="margin-bottom:16px">
          <a-row>
            <a-col :span="5">
              复议年月：
              <a-month-picker
                placeholder="请输入复议年月"
                style="width: 105px"
                @change="monthChange"
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-col>
            <a-col :span="18" >
              <a-select
                :value="searchDataType"
                v-show="tableSelectKey != 3 ? true : false"
                @change="handleDataTypeChange"
                style="width: 100px"
              >
                <a-select-option
                  v-for="d in searchDataTypeSource"
                  :key="d.value"
                >
                  {{ d.text }}
                </a-select-option>
              </a-select>
              <a-checkbox
                :checked="checked"
                style="margin-top:5px"
                v-show="tableSelectKey == 3 ? true : false"
                @change="onChange"
              >
                是否发送
              </a-checkbox>
              <a-input-search
                placeholder="请输入关键字"
                v-model="searchText"
                style="width: 160px"
                enter-button
                @search="searchTable"
              />
              <a-popconfirm
                title="确定发送短信？"
                v-show="tableSelectKey == 3 ? true : false"
                @confirm="sendSms"
                okText="确定"
                style="margin-left: 6px"
                cancelText="取消"
              >
                <a-button type="primary">发送短信</a-button>
              </a-popconfirm>
              <a-popconfirm
                title="确定获取剔除数据剔除？"
                @confirm="importData"
                 v-show="tableSelectKey == 1 ? true : false"
                style="margin-left: 6px"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary">获取剔除数据</a-button>
              </a-popconfirm>
              <a-popconfirm
                title="确定批量发送？"
                @confirm="batchSend"
                 v-show="tableSelectKey == 1 ? true : false"
                style="margin-left: 6px"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary">批量发送</a-button>
              </a-popconfirm>
              <a-popconfirm
                title="确定全部发送？"
                @confirm="batchSendA"
                 v-show="tableSelectKey == 1 ? true : false"
                style="margin-left: 6px"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary">全部发送</a-button>
              </a-popconfirm>
              <a-button type="primary" @click="searchTable">刷新</a-button>
              <a-button
                type="danger"
                @click="showDateModal">
                  日期
              </a-button>
            </a-col>
          </a-row>
        </div>
      </a-spin>
    </template>
    <!--表格-->
    <template>
      <div id="tab">
        <a-tabs type="card" @change="callback">
          <a-tab-pane key="1" tab="非常规复议待发送">
            <ybHandleVerifyData-send
              ref="ybHandleVerifyDataSend"
              :applyDate="searchApplyDate"
              :searchDataType="searchDataType"
              :searchText="searchText"
              @closeSpin="closeSpin"
            >
            </ybHandleVerifyData-send>
          </a-tab-pane>
          <a-tab-pane key="2" :forceRender="true" tab="非常规复议已发送">
            <ybHandleVerifyData-end
              ref="ybHandleVerifyDataEnd"
              :applyDate="searchApplyDate"
              :searchDataType="searchDataType"
              :searchText="searchText"
            >
            </ybHandleVerifyData-end>
          </a-tab-pane>
          <a-tab-pane key="3" :forceRender="true" tab="非常规复议短信">
            <ybHandleVerify-sms
              ref="ybHandleVerifySms"
              :applyDateStr="searchApplyDate"
              :searchText="searchText"
              :checked="checked"
            >
            </ybHandleVerify-sms>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <a-modal v-model="visibleDate" title="变更日期" on-ok="handleDateOk">
      <template slot="footer">
        <a-button key="back" @click="handleDateCancel"> 取消 </a-button>
        <a-popconfirm
          title="确定匹配？"
          @confirm="handleDateOk"
          :disabled="reconsiderApply.applyId == null ? true : false"
          okText="确定"
          cancelText="取消"
        >
          <a-button
            type="primary"
            :disabled="reconsiderApply.applyId == null ? true : false"
            style="margin-right: 0.8rem"
            >确定</a-button
          >
        </a-popconfirm>
      </template>
      <a-row>
        <a-form-item v-bind="formItemLayout" label="复议年月">
          <a-month-picker
            placeholder="请输入复议年月"
            style="width: 125px"
            disabled
            v-model="searchApplyDate"
            :default-value="searchApplyDate"
            :format="monthFormat"
          />
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="非常规截止时间"
        >
          <a-date-picker
            placeholder="请输入非常规截止时间"
            style="width: 220px"
            v-model="reconsiderApply.endDateReset"
            show-time
            :format="dayFormat"
          />
        </a-form-item>
      </a-row>
    </a-modal>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbHandleVerifyDataSend from './YbHandleVerifyDataSend'
import YbHandleVerifyDataEnd from './YbHandleVerifyDataEnd'
import YbHandleVerifySms from './YbHandleVerifySms'
const formItemLayout = {
  labelCol: {
    span: 7
  },
  wrapperCol: {
    span: 13
  }
}
export default {
  name: 'YbHandleVerifyView',
  components: {
    YbHandleVerifyDataSend,
    YbHandleVerifyDataEnd,
    YbHandleVerifySms
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      dayFormat: 'YYYY-MM-DD HH:mm:ss',
      searchText: '',
      searchApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      searchDataTypeSource: [{
        text: '全部',
        value: 2
      },
      {
        text: '明细扣款',
        value: 0
      },
      {
        text: '主单扣款',
        value: 1
      }
      ],
      searchDataType: 0,
      spinning: false,
      checked: false,
      delayTime: 500,
      visibleDate: false,
      reconsiderApply: {
        applyId: null,
        endDateReset: null
      },
      user: this.$store.state.account.user,
      tableSelectKey: '1'
    }
  },
  computed: {},
  mounted () {
    this.initTypeno(this.searchApplyDate)
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
    handleDataTypeChange (value) {
      this.searchDataType = value
    },
    initTypeno (applyDateStr) {
      this.$get('ybReconsiderApply/getReconsiderApply', {
        applyDateStr: applyDateStr, areaType: this.user.areaType.value
      }).then((r) => {
        if (r.data.data.success === 1 && r.data.data.apply !== null) {
          this.reconsiderApply.endDateReset = r.data.data.apply.endDateReset
          this.reconsiderApply.applyId = r.data.data.apply.id
        } else {
          this.reconsiderApply.applyId = null
          this.reconsiderApply.endDateReset = null
        }
      })
    },
    handleDateOk () {
      let ybReconsiderApply = {
        id: this.reconsiderApply.applyId,
        endDateReset: this.reconsiderApply.endDateReset
      }
      if (ybReconsiderApply.id != null && ybReconsiderApply.endDateReset != null) {
        ybReconsiderApply.endDateReset = moment(ybReconsiderApply.endDateReset)
        this.$put('ybReconsiderApply/updateEndResetDate', {
          ...ybReconsiderApply
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.$message.success('当前' + this.searchApplyDate + '修改非常规截止时间成功.')
            this.visibleDate = false
          } else {
            this.$message.warning('当前' + this.searchApplyDate + '修改非常规截止时间失败,请检查是否剔除完成操作..')
          }
        }).catch(() => {
          this.$message.error('当前' + this.searchApplyDate + '修改非常规截止时间失败.')
        })
      } else {
        this.$message.error(this.searchApplyDate + '请选择正确的非常规截止时间.')
      }
    },
    handleDateCancel () {
      this.visibleDate = false
    },
    showDateModal () {
      if (this.reconsiderApply.applyId !== null) {
        this.visibleDate = true
      } else {
        this.$message.error('当前' + this.searchApplyDate + '无复议申请数据.')
      }
    },
    onChange () {
      this.checked = !this.checked
      setTimeout(() => {
        this.searchTable()
      }, 500)
    },
    sendSms () {
      if (this.tableSelectKey === '3') {
        this.spinning = true
        let key = this.tableSelectKey
        this.$put('comSms/sendSms', {
          applyDateStr: this.searchApplyDate,
          areaType: this.user.areaType.value,
          sendType: 6,
          state: 0
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.$message.success('发送成功.')
            if (this.tableSelectKey === key) {
              this.callback(key)
            }
            this.spinning = false
          } else {
            this.$message.warning(r.data.data.message)
            this.spinning = false
          }
        }).catch(() => {
          this.$message.error('发送失败.')
          this.spinning = false
        })
      }
    },
    importData () {
      this.spinning = true
      this.$post('ybHandleVerifyData/importCreateHandleVerifyData', {
        applyDateStr: this.searchApplyDate,
        areaType: this.user.areaType.value
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.spinning = false
          this.$refs.ybHandleVerifyDataSend.search()
          this.$message.success(r.data.data.message)
        } else {
          this.spinning = false
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.spinning = false
        this.$message.error('获得剔除数据操作失败.')
      })
    },
    batchSend () {
      this.spinning = true
      this.$refs.ybHandleVerifyDataSend.batchSend()
    },
    batchSendA () {
      this.spinning = true
      this.$refs.ybHandleVerifyDataSend.batchSendA()
    },
    closeSpin () {
      this.spinning = false
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybHandleVerifyDataSend.searchPage()
      } else if (key === '2') {
        this.$refs.ybHandleVerifyDataEnd.searchPage()
      } else if (key === '3') {
        this.$refs.ybHandleVerifySms.searchPage()
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
