<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <a-spin
      tip="Loading..."
      :spinning="spinning"
      :delay="delayTime"
    >
      <div>
        <a-row
          justify='start'
          type="flex"
        >
          <a-col :span=6>
            <a-form-item
              v-bind="formItemLayout"
              label="文件名称"
            >
              <a-input
                placeholder="文件名称"
                v-model="uploadFileName"
              />
            </a-form-item>
          </a-col>
          <a-col :span=3>
            <a-form-item
              v-bind="formItemLayout"
              label="保险类型"
            >
              <a-input
                placeholder="保险类型"
                v-model="repayTypeName"
              />
            </a-form-item>
          </a-col>
          <a-col :span=3>
            <a-form-item
              v-bind="formItemLayout"
              label="扣款类型"
            >
              <a-input
                placeholder="扣款类型"
                v-model="dataTypeName"
              />
            </a-form-item>
          </a-col>
          <a-col :span=3>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
            <a-input
                placeholder="复议年月"
                v-model="searchBelongDateStr"
                disabled="disabled"
              />
            </a-form-item>
          </a-col>
          <a-col
            :span=2
            v-show="tableSelectKey==1?true:false"
          >
            <a-popconfirm
              title="确定自动还款？"
              @confirm="update"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right:10px" >自动还款</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=2 >
            <a-popconfirm
              title="确定完成还款？"
              @confirm="updateApplyState"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right:10px" >完成还款</a-button>
            </a-popconfirm>
          </a-col>
          <a-col
            :span=3
            v-show="tableSelectKey==3?true:false"
          >
            <a-button
              type="primary"
              style="margin-right: 10px"
              @click="exportExcel"
            >未知数据导出</a-button>
          </a-col>
          <a-col>
            <a-button
              style="margin-left: 12px"
              @click="onClose"
            >关闭</a-button>
          </a-col>
        </a-row>
      </div>
    </a-spin>
    <!--表格-->
    <template>
      <div id="tab">
        <a-tabs
          :activeKey="tableSelectKey"
          type="card"
          @change="callback"
        >
          <a-tab-pane
            key="1"
            tab="还款单明细"
          >
            <ybReconsiderRepay-data
              ref="ybReconsiderRepayData"
              :pid="this.ybReconsiderRepay.id"
              :belongDateStr="searchBelongDateStr"
              @look="look"
            >
            </ybReconsiderRepay-data>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="异常数据"
          >
            <ybReconsiderRepay-except
              ref="ybReconsiderRepayExcept"
              :pid="this.ybReconsiderRepay.id"
              :belongDateStr="searchBelongDateStr"
              @look="look"
              @exceptRepay="exceptRepay"
            >
            </ybReconsiderRepay-except>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="未知数据"
          >
            <ybReconsiderRepay-unknown
              ref="ybReconsiderRepayUnknown"
              :pid="this.ybReconsiderRepay.id"
              :belongDateStr="searchBelongDateStr"
              @look="look"
            >
            </ybReconsiderRepay-unknown>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <!-- 查看 -->
    <ybReconsiderResetResult-module
      ref="ybReconsiderResetResultModule"
      @close="handleLookClose"
      :lookVisiable="lookVisiable"
    >
    </ybReconsiderResetResult-module>

    <!-- 明细还款 -->
    <ybReconsiderRepayExcept-detail
      ref="ybReconsiderRepayExceptDetail"
      @close="handleExceptRepayClose"
      :lookVisiable="exceptRepayVisiable"
    >
    </ybReconsiderRepayExcept-detail>
  </a-card>
</template>

<script>
import YbReconsiderRepayData from './YbReconsiderRepayData'
import YbReconsiderRepayExcept from './YbReconsiderRepayExcept'
import YbReconsiderRepayUnknown from './YbReconsiderRepayUnknown'
import YbReconsiderRepayExceptDetail from './YbReconsiderRepayExceptDetail'
import YbReconsiderResetResultModule from '../ybFunModule/YbReconsiderResetResultModule'

const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 13 }
}
export default {
  name: 'YbReconsiderRepayView',
  components: { YbReconsiderRepayData, YbReconsiderRepayExcept, YbReconsiderRepayExceptDetail, YbReconsiderRepayUnknown, YbReconsiderResetResultModule },
  props: {
  },
  data () {
    return {
      formItemLayout,
      ybReconsiderRepay: {},
      uploadFileName: '',
      lookVisiable: false,
      exceptRepayVisiable: false,
      selectBelongDateStrDataSource: [
      ],
      searchBelongDateStr: '',
      repayTypeName: '',
      dataTypeName: '',
      spinning: false,
      delayTime: 500,
      isUpdate: false,
      tableSelectKey: '1'
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    handleBelongDateStrChange (value) {
      this.searchBelongDateStr = value
      setTimeout(() => {
        this.searchTable()
      }, 200)
    },
    onClose () {
      this.ybReconsiderRepay = {}
      this.tableSelectKey = '1'
      this.searchBelongDateStr = ''
      this.selectBelongDateStrDataSource = []
      this.$emit('cancel', this.isUpdate)
    },
    handleLookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      this.$refs.ybReconsiderResetResultModule.setFormValues(record)
    },
    handleExceptRepayClose () {
      this.exceptRepayVisiable = false
      this.$refs.ybReconsiderRepayData.search()
    },
    exceptRepay (record) {
      this.exceptRepayVisiable = true
      this.$refs.ybReconsiderRepayExceptDetail.setFormValues(record)
    },
    exportExcel () {
      this.$refs.ybReconsiderRepayUnknown.exportExcel()
    },
    updateApplyState () {
      let updateParam = {
        applyDateStr: this.searchBelongDateStr,
        id: this.ybReconsiderRepay.id
      }
      this.$put('ybReconsiderRepay/updateApplyState', {
        ...updateParam
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success(r.data.data.message)
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('还款完成操作失败.')
      })
    },
    update () {
      let updateParam = {
        pid: this.ybReconsiderRepay.id,
        belongDateStr: this.searchBelongDateStr,
        seekState: 0,
        repayType: this.ybReconsiderRepay.repayType,
        dataType: this.ybReconsiderRepay.dataType,
        state: 0
      }
      this.spinning = true
      this.$put('ybReconsiderRepayData/updateRepayData', {
        ...updateParam
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.isUpdate = true
          this.searchTable()
          this.$message.success(r.data.data.message)
          this.spinning = false
        } else {
          this.$message.error(r.data.data.message)
          this.spinning = false
        }
      }).catch(() => {
        this.spinning = false
        this.$message.error('剔除数据操作失败.')
      })
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybReconsiderRepayData.search()
      } else if (key === '2') {
        this.$refs.ybReconsiderRepayExcept.search()
      } else if (key === '3') {
        this.$refs.ybReconsiderRepayUnknown.search()
      } else {
        console.log('ok')
      }
    },
    findBelongDateStr (pid) {
      let queryParams = { pid: pid }
      this.$get('ybReconsiderRepayData/findGroupBelongDateStr', {
        ...queryParams
      }).then((r) => {
        let data = r.data.data.data
        let oneValue = ''
        data.forEach(item => {
          if (oneValue === '') {
            oneValue = item
          }
          this.selectBelongDateStrDataSource.push({
            text: item, value: item
          })
        })
        this.searchBelongDateStr = oneValue
        setTimeout(() => {
          this.$refs.ybReconsiderRepayData.search()
        }, 200)
      }).catch(() => {
        this.$message.success('获取还款日期列表失败!')
      })
    },
    setFormValues ({ ...ybReconsiderRepay }) {
      this.isUpdate = false
      this.ybReconsiderRepay = ybReconsiderRepay
      this.tableSelectKey = '1'
      this.uploadFileName = ybReconsiderRepay.uploadFileName
      if (ybReconsiderRepay.repayType === 0) {
        this.repayTypeName = '居保'
      } else if (ybReconsiderRepay.repayType === 1) {
        this.repayTypeName = '职保'
      } else {
        this.repayTypeName = '无'
      }
      this.dataTypeName = ybReconsiderRepay.dataType === 0 ? '明细扣款' : '主单扣款'
      this.searchBelongDateStr = ybReconsiderRepay.applyDateStr
      this.selectBelongDateStrDataSource = []
      setTimeout(() => {
        this.$refs.ybReconsiderRepayData.search()
      }, 200)
      // this.findBelongDateStr(ybReconsiderRepay.id)
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
