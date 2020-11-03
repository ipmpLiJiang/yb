<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div style="text-align:center;margin-bottom:20px">
        <a-row >
          <a-col :span=5>
            复议年月：
            <a-month-picker
              placeholder="请输入复议年月"
              @change="monthChange"
              :default-value="formatDate()"
              :format="monthFormat"
            />
          </a-col>
          <a-col :span=5>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            <template>
              <a-upload
                name="file"
                accept=".xlsx,.xls"
                :fileList="fileList"
                :beforeUpload="beforeUpload"
                :disabled="fileDisabled"
              >
                <a-button type="primary" :disabled="fileDisabled">
                  上传剔除数据 </a-button>
              </a-upload>
            </template>
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            <a-popconfirm
              title="确定下载剔除模板？"
              @confirm="downloadFile"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">下载剔除模板</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            <a-popconfirm
              title="确定数据剔除？"
              @confirm="update"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">数据剔除</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==1?true:false">
            <a-popconfirm
              title="确定导出扣款数据？"
              @confirm="exportDeductimplementExcel"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" >导出扣款数据</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=3 v-show="tableSelectKey==4?true:false">
            <a-popconfirm
              title="确定导出未知数据？"
              @confirm="exportExcel"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" >导出未知数据</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=2 >
            <a-popconfirm
              title="确定完成剔除？"
              @confirm="updateApplyState"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" >完成剔除</a-button>
            </a-popconfirm>
          </a-col>
        </a-row>
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
            tab="明细扣款"
          >
            <ybReconsiderReset-data
              ref="ybReconsiderResetData"
              :applyDate="searchApplyDate"
              :searchText="searchText"
              @look="look"
              @uploadDisabled="uploadDisabled"
            >
            </ybReconsiderReset-data>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="主单扣款"
          >
            <ybReconsiderReset-main
              ref="ybReconsiderResetMain"
              :applyDate="searchApplyDate"
              :searchText="searchText"
              @look="look"
            >
            </ybReconsiderReset-main>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="异常匹配"
          >
            <ybReconsiderReset-except
              ref="ybReconsiderResetExcept"
              :applyDate="searchApplyDate"
              :searchText="searchText"
              @exceptReset="exceptReset"
              @look="look"
            >
            </ybReconsiderReset-except>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="未知数据"
          >
            <ybReconsiderReset-unknown
              ref="ybReconsiderResetUnknown"
              :applyDate="searchApplyDate"
              :searchText="searchText"
              @look="look"
            >
            </ybReconsiderReset-unknown>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <!-- 查看 -->
    <ybReconsiderResetData-module
      ref="ybReconsiderResetDataModule"
      @close="handleLookClose"
      :lookVisiable="lookVisiable"
    >
    </ybReconsiderResetData-module>
    <!-- 异常 -->
    <ybReconsiderResetExcept-detail
      ref="ybReconsiderResetExceptDetail"
      @close="handleExceptResetClose"
      :exceptResetVisiable="exceptResetVisiable"
    >
    </ybReconsiderResetExcept-detail>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbReconsiderResetData from './YbReconsiderResetData'
import YbReconsiderResetMain from './YbReconsiderResetMain'
import YbReconsiderResetDataModule from '../ybFunModule/YbReconsiderResetDataModule'
import YbReconsiderResetExcept from './YbReconsiderResetExcept'
import YbReconsiderResetUnknown from './YbReconsiderResetUnknown'
import YbReconsiderResetExceptDetail from './YbReconsiderResetExceptDetail'

const formItemLayout = {
  labelCol: { span: 9 },
  wrapperCol: { span: 13, offset: 1 }
}
export default {
  name: 'YbReconsiderResetView',
  components: {
    YbReconsiderResetMain, YbReconsiderResetData, YbReconsiderResetDataModule, YbReconsiderResetExcept, YbReconsiderResetUnknown, YbReconsiderResetExceptDetail},
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      lookVisiable: false,
      fileDisabled: false,
      fileList: [],
      searchApplyDate: this.formatDate(),
      spinning: false,
      delayTime: 500,
      exceptResetVisiable: false,
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
    downloadFile () {
      this.$download('ybReconsiderResetData/downFile', {
      }, '剔除数据模板.xlsx')
    },
    uploadDisabled (isDisabled) {
      this.fileDisabled = isDisabled
    },
    update () {
      let updateParam = {
        applyDateStr: this.searchApplyDate,
        dataType: 0
      }
      if (this.tableSelectKey === '2') {
        updateParam.dataType = 1
      }
      this.spinning = true
      this.$put('ybReconsiderResetData/updateResetData', {
        ...updateParam
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.updateSearch()
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
    exportExcel () {
      // if (this.tableSelectKey === '1') {
      //   this.$refs.ybReconsiderResetData.exportExcel()
      // } else if (this.tableSelectKey === '2') {
      //   this.$refs.ybReconsiderResetMain.exportExcel()
      // } else if (this.tableSelectKey === '3') {
      //   this.$refs.ybReconsiderResetExcept.exportExcel()
      // } else if (this.tableSelectKey === '4') {
      //   this.$refs.ybReconsiderResetUnknown.search()
      // } else {
      //   console.log('exportExcel')
      // }
      this.$refs.ybReconsiderResetUnknown.exportExcel()
    },
    exportDeductimplementExcel () {
      this.$refs.ybReconsiderResetData.exportDeductimplementExcel()
    },
    updateSearch () {
      if (this.tableSelectKey === '1') {
        this.$refs.ybReconsiderResetData.search()
      } else if (this.tableSelectKey === '2') {
        this.$refs.ybReconsiderResetMain.search()
      }
    },
    beforeUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      let isExcel = testmsg === 'xlsx'
      if (!isExcel) {
        isExcel = testmsg === 'xls'
      }
      // const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      if (!(isExcel)) {
        this.$error({
          title: '只能上传.xlsx,.xls格式的Excel文档~'
        })
        return
      }
      const isLt2M = file.size / 1024 / 1024 < 5
      if (!isLt2M) {
        this.$error({
          title: '超5M限制，不允许上传~'
        })
        return
      }
      return isExcel && isLt2M && this.handleUpload(file)
    },
    handleUpload (file) {
      // 点击删除文件调用removeUpload后会自动调用本方法handleUpload 待解决
      const formData = new FormData()
      formData.append('file', file)
      formData.append('applyDateStr', this.searchApplyDate)
      this.spinning = true
      this.$upload('ybReconsiderReset/importReconsiderReset', formData).then((r) => {
        // r.data.data.message
        if (r.data.data.success === 1) {
          this.updateSearch()
          this.$message.success('Excel导入成功.')
          this.spinning = false
        } else {
          this.$message.error(r.data.data.message)
          this.spinning = false
        }
      }).catch(() => {
        this.spinning = false
        this.fileList = []
        this.$message.error('Excel导入失败.')
      })
    },
    updateApplyState () {
      let updateParam = {
        applyDateStr: this.searchApplyDate
      }
      this.$put('ybReconsiderReset/updateApplyState', {
        ...updateParam
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success(r.data.data.message)
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('剔除完成操作失败.')
      })
    },
    handleExceptResetClose (isUpdate) {
      this.exceptResetVisiable = false
      if (isUpdate) {
        this.$refs.ybReconsiderResetExcept.search()
      }
    },
    exceptReset (record) {
      this.exceptResetVisiable = true
      this.$refs.ybReconsiderResetExceptDetail.setFormValues(record)
    },
    handleLookClose () {
      this.lookVisiable = false
    },
    look (record) {
      this.lookVisiable = true
      this.$refs.ybReconsiderResetDataModule.setFormValues(record)
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybReconsiderResetData.search()
      } else if (key === '2') {
        this.$refs.ybReconsiderResetMain.search()
      } else if (key === '3') {
        this.$refs.ybReconsiderResetExcept.search()
      } else if (key === '4') {
        this.$refs.ybReconsiderResetUnknown.search()
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
<style scoped>
.spin-content {
  border: 1px solid #91d5ff;
  background-color: #e6f7ff;
  padding: 30px;
}
.editable-row-operations a {
  margin-right: 8px;
}
</style>
