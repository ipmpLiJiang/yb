<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <div style="text-align:center;">
          <a-row
            justify="center"
            align="middle"
          >
            <a-col :span=6>
              复议年月：
              <a-month-picker
                placeholder="请输入复议年月"
                style="width: 150px"
                @change="monthChange"
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-col>
            <a-col :span=4>
              <a-form-item
                label="扣款类型"
                v-bind="formItemLayout"
              >
                <a-select
                  :value="searchDataType"
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
              </a-form-item>
          </a-col>
            <a-col :span=4>
              <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 170px" enter-button @search="searchTable" />
            </a-col>
            <a-col :span=3 v-show="tableSelectKey==1?true:false">
              <a-popconfirm
              title="确定获取剔除数据剔除？"
              @confirm="importData"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">获取剔除数据</a-button>
            </a-popconfirm>
            </a-col>
            <a-col :span=2 v-show="tableSelectKey==1?true:false">
              <a-popconfirm
                title="确定批量发送？"
                @confirm="batchSend"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary">批量发送</a-button>
              </a-popconfirm>
            </a-col>
            <a-col :span=2 v-show="tableSelectKey==1?true:false">
              <a-popconfirm
                title="确定全部发送？"
                @confirm="batchSendA"
                okText="确定"
                cancelText="取消"
              >
                <a-button type="primary">全部发送</a-button>
              </a-popconfirm>
            </a-col>
            <a-col :span=2>
              <a-button
                type="primary"
                @click="searchTable"
              >刷新</a-button>
            </a-col>
          </a-row>
        </div>
      </div>
      </a-spin>
    </template>
    <!--表格-->
    <template>
      <div id="tab">
        <a-tabs
          type="card"
          @change="callback"
        >
          <a-tab-pane
            key="1"
            tab="人工复议待发送"
          >
            <ybHandleVerifyData-send
              ref="ybHandleVerifyDataSend"
              :applyDate="searchApplyDate"
              :searchDataType="searchDataType"
              :searchText = "searchText"
            >
            </ybHandleVerifyData-send>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="人工复议已发送"
          >
            <ybHandleVerifyData-end
              ref="ybHandleVerifyDataEnd"
              :applyDate="searchApplyDate"
              :searchDataType="searchDataType"
              :searchText = "searchText"
            >
            </ybHandleVerifyData-end>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbHandleVerifyDataSend from './YbHandleVerifyDataSend'
import YbHandleVerifyDataEnd from './YbHandleVerifyDataEnd'
const formItemLayout = {
  labelCol: { span: 7 },
  wrapperCol: { span: 13 }
}
export default {
  name: 'YbHandleVerifyView',
  components: {YbHandleVerifyDataSend, YbHandleVerifyDataEnd},
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      searchApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      searchDataTypeSource: [
        { text: '全部', value: 2 },
        { text: '明细扣款', value: 0 },
        { text: '主单扣款', value: 1 }
      ],
      searchDataType: 0,
      spinning: false,
      delayTime: 500,
      user: this.$store.state.account.user,
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
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    handleDataTypeChange (value) {
      this.searchDataType = value
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
      this.$refs.ybHandleVerifyDataSend.batchSend()
    },
    batchSendA () {
      this.$refs.ybHandleVerifyDataSend.batchSendA()
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybHandleVerifyDataSend.searchPage()
      } else if (key === '2') {
        this.$refs.ybHandleVerifyDataEnd.searchPage()
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
