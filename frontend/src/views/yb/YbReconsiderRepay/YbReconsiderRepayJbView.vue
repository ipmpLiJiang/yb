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
              v-bind="{
            labelCol: { span: 7 },
            wrapperCol: { span: 16 }
          }"
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
              label="还款年月"
            >
              <a-select
                :value="searchBelongDateStr"
                style="width: 100px"
                @change="handleBelongDateStrChange"
              >
                <a-select-option
                  v-for="d in selectBelongDateStrDataSource"
                  :key="d.value"
                >
                  {{ d.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col
            :span=2
            v-show="tableSelectKey==1?true:false"
          >
            <a-button
              type="primary"
              style="margin-left: 12px"
              @click="update"
            >自动还款</a-button>
          </a-col>
          <a-col
            :span=3
            v-show="tableSelectKey==3?true:false"
          >
            <a-button
              type="primary"
              style="margin-left: 12px"
            >未知数据导出</a-button>
          </a-col>
          <a-col :span=3>
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
import YbReconsiderRepayData from './YbReconsiderRepayJbData'
import YbReconsiderRepayExcept from './YbReconsiderRepayJbExcept'
import YbReconsiderRepayUnknown from './YbReconsiderRepayJbUnknown'
import YbReconsiderRepayExceptDetail from './YbReconsiderRepayExceptJbDetail'
import YbReconsiderResetResultModule from '../ybFunModule/YbReconsiderResetResultJbModule'

const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 13 }
}
export default {
  name: 'YbReconsiderRepayJbView',
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
      this.$emit('cancel')
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
      this.ybReconsiderRepay = ybReconsiderRepay
      this.tableSelectKey = '1'
      this.uploadFileName = ybReconsiderRepay.uploadFileName
      this.repayTypeName = ybReconsiderRepay.repayType === 0 ? '居保' : '职保'
      this.dataTypeName = ybReconsiderRepay.dataType === 0 ? '明细扣款' : '主单扣款'
      this.selectBelongDateStrDataSource = []
      this.findBelongDateStr(ybReconsiderRepay.id)
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
