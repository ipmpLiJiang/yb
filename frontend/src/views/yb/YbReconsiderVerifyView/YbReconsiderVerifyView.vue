
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <a-row
          justify="center"
          align="middle"
        >
          <a-col :span=6>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
              <a-month-picker
                placeholder="请输入复议年月"
                @change="monthChange"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=2>
            <a-button
              type="primary"
              @click="showSearchModal"
            >筛选</a-button>
          </a-col>
          <a-col :span=10>
            <a-popconfirm
              title="确定自动匹配？"
              @confirm="addImport"
              v-show="tableSelectKey==1||tableSelectKey==2?true:false"
              okText="确定"
              style="margin-right: 15px"
              cancelText="取消"
            >
              <a-button type="primary">自动匹配</a-button>
            </a-popconfirm>
            <a-button
              type="primary"
              style="margin-right: 15px"
              v-show="tableSelectKey==1||tableSelectKey==2?true:false"
              @click="showUpdateModal"
            >手动匹配</a-button>
            <a-popconfirm
              title="确定批量核对?"
              style="margin-right: 15px"
              :visible="pcmVisible"
              ok-text="确定"
              cancel-text="取消"
              v-show="tableSelectKey==1?true:false"
              @visibleChange="handleVisibleChange"
              @confirm="batchVerify"
              @cancel="confirmCancel"
            >
              <a-button type="primary">批量核对</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部核对？"
              style="margin-right: 15px"
              v-show="tableSelectKey==1?true:false"
              @confirm="batchVerifyA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部核对</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定批量发送？"
              style="margin-right: 15px"
              v-show="tableSelectKey==2||tableSelectKey==3?true:false"
              @confirm="batchSend"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">批量发送</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部发送？"
              style="margin-right: 15px"
              v-show="tableSelectKey==2||tableSelectKey==3?true:false"
              @confirm="batchSendA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部发送</a-button>
            </a-popconfirm>
            <a-select :value="searchDataType" style="width: 100px" @change="handleDataTypeChange"
            v-show="tableSelectKey==4?true:false"
            >
              <a-select-option
              v-for="d in selectDataTypeDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            <a-select :value="searchTypeno" style="width: 100px" @change="handleTypenoChange"
            v-show="tableSelectKey==4?true:false"
            >
              <a-select-option
              v-for="d in selectTypenoDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            <a-button
              type="primary"
              style="margin-right: 15px"
              @click="searchTable"
            >刷新</a-button>
          </a-col>

          <a-col :span=2 v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            <a-upload
                name="file"
                accept=".xlsx,.xls"
                style="margin-right: 15px"
                :fileList="fileList"
                :beforeUpload="beforeUpload"
              >
                <a-button type="primary">
                  <a-icon type="upload" /> 上传 </a-button>
              </a-upload>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            <a-popconfirm
              title="确定导出数据？"
              @confirm="exportExcel"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" >导出数据</a-button>
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
            tab="明细待核对"
          >
            <ybReconsiderVerify-stayed
              ref="ybReconsiderVerifyStayed"
              :applyDate='searchApplyDate'
              :searchItem='searchItem'
              @selectChangeKeyVerify="selectChangeKeyVerify"
              @detail="detail"
              @showImport="showImport"
              @handImport="handImport"
              @batchVerify="batchVerify"
              @batchVerifyA="batchVerifyA"
              @verifySpin="verifySpin"
            >
            </ybReconsiderVerify-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="主单待核对"
          >
            <ybReconsiderSendStayed-main
              ref="ybReconsiderSendStayedMain"
              :applyDate='searchApplyDate'
              :searchItem='searchItem'
              @showImport="showImport"
              @handImport="handImport"
              @verifySpin="verifySpin"
            >
            </ybReconsiderSendStayed-main>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="已核对"
          >
            <ybReconsiderSend-stayed
              ref="ybReconsiderSendStayed"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @verifySpin="verifySpin"
            >
            </ybReconsiderSend-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="已完成"
          >
            <ybReconsiderSend-end
              ref="ybReconsiderSendEnd"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              :searchTypeno='searchTypeno'
              :searchDataType='searchDataType'
            >
            </ybReconsiderSend-end>
          </a-tab-pane>
        </a-tabs>
      </div>
      <!-- 修改字典 -->
      <ybReconsiderVerifyView-detail
        ref="ybReconsiderVerifyViewDetail"
        @close="handleDetailClose"
        @success="handleDetailSuccess"
        :detailVisiable="detailVisiable"
      >
      </ybReconsiderVerifyView-detail>
    </template>
    <!--筛选Modal-->
    <template>
      <div>
        <a-modal
          v-model="visibleSearch"
          title="筛选"
          width="35%"
          on-ok="handleSearchOk"
        >
          <template slot="footer">
            <a-button
              key="back"
              @click="handleSearchCancel"
            >
              取消
            </a-button>
            <a-button
              key="submit"
              type="primary"
              @click="handleSearchOk"
            >
              确定
            </a-button>
          </template>
          <p>
            <a-form-item
              v-bind="formItemLayout"
              label="项目名称："
            >
            <a-input-group compact>
              <a-select style="width: 85px" v-model="searchItem.project.type" default-value="LIKE">
                <a-select-option v-for="item in handleQuerySymbol" :key="item.value" :value="item.value">
                  {{item.text}}
                </a-select-option>
              </a-select>
              <a-input style="width: 220px"  v-model="searchItem.project.projectName" />
            </a-input-group>
            </a-form-item>
          </p>
          <p>
            <a-form-item
              v-bind="formItemLayout"
              label="规则名称："
            >
            <a-input-group compact>
              <a-select style="width: 85px" v-model="searchItem.rule.type" default-value="LIKE">
                <a-select-option v-for="item in handleQuerySymbol" :key="item.value" :value="item.value">
                  {{item.text}}
                </a-select-option>
              </a-select>
              <a-input style="width: 220px" v-model="searchItem.rule.ruleName" />
            </a-input-group>
            </a-form-item>
          </p>
          <p>
            <a-form-item
              v-bind="formItemLayout"
              label="科室名称："
            >
            <a-input-group compact>
              <a-select style="width: 85px" v-model="searchItem.dept.type" default-value="LIKE">
                <a-select-option v-for="item in handleQuerySymbol" :key="item.value" :value="item.value">
                  {{item.text}}
                </a-select-option>
              </a-select>
              <a-input style="width: 220px" v-model="searchItem.dept.deptName" />
            </a-input-group>
            </a-form-item>
          </p>
          <p>
            <a-form-item
              v-bind="formItemLayout"
              label="序号编码："
            >
            <a-input-group compact>
              <a-select style="width: 85px" v-model="searchItem.order.type" default-value="EQ">
                <a-select-option v-for="item in handleQuerySymbol" :key="item.value" :value="item.value">
                  {{item.text}}
                </a-select-option>
              </a-select>
              <a-input style="width: 220px" v-model="searchItem.order.orderNumber" />
            </a-input-group>
            </a-form-item>
          </p>
        </a-modal>
      </div>
    </template>
    <!--变更Modal-->
    <template>
      <div>
        <a-modal
          v-model="visibleUpdate"
          title="手动匹配"
          on-ok="handleUpdateOk"
        >
          <template slot="footer">
            <a-button
              key="back"
              @click="handleUpdateCancel"
            >
              取消
            </a-button>
            <a-popconfirm
              title="确定匹配？"
              @confirm="handleUpdateOk"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: .8rem">确定</a-button>
            </a-popconfirm>
          </template>
          <a-form-item
            v-bind="formItemLayout"
            label="参考复议科室"
          >
            <input-select
              ref="inputSelectVerifyDept"
              :type=1
              @selectChange=selectDeptChange
            >
            </input-select>
          </a-form-item>
          <a-form-item
            v-bind="formItemLayout"
            label="参考复议医生"
          >
            <input-select
              ref="inputSelectVerifyDoctor"
              :type=2
              @selectChange=selectDoctorChange
            >
            </input-select>
          </a-form-item>
        </a-modal>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbReconsiderVerifyStayed from './YbReconsiderVerifyStayed'
import YbReconsiderSendStayed from './YbReconsiderSendStayed'
import YbReconsiderSendStayedMain from './YbReconsiderSendStayedMain'
import YbReconsiderSendEnd from './YbReconsiderSendEnd'
import YbReconsiderVerifyViewDetail from './YbReconsiderVerifyViewDetail'
import InputSelect from '../../common/InputSelect'
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbReconsiderVerifyView',
  components: { InputSelect, YbReconsiderVerifyViewDetail, YbReconsiderVerifyStayed, YbReconsiderSendStayed, YbReconsiderSendStayedMain, YbReconsiderSendEnd },
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      detailVisiable: false,
      ybReconsiderVerify: {},
      tableSelectKey: '1',
      searchApplyDate: this.formatDate(),
      visibleSearch: false,
      visibleUpdate: false,
      pcmVisible: false,
      selectedPcmRowKeys: [],
      spinning: false,
      delayTime: 500,
      selectDate: {},
      fileList: [],
      searchTypeno: 1,
      searchDataType: 0,
      handleQuerySymbol: [
        {text: '等于', value: 'EQ'},
        {text: '包含', value: 'LIKE'},
        {text: '不包含', value: 'NOTLIKE'}
      ],
      selectTypenoDataSource: [{text: '版本一', value: 1}, {text: '版本二', value: 2}],
      selectDataTypeDataSource: [{text: '明细扣款', value: 0}, {text: '主单扣款', value: 1}],
      searchItem: {
        project: {type: 'LIKE', projectName: ''},
        rule: {type: 'LIKE', ruleName: ''},
        dept: {type: 'LIKE', deptName: ''},
        order: {type: 'LIKE', orderNumber: ''}
      },
      user: this.$store.state.account.user
    }
  },
  computed: {
  },
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
    handleTypenoChange (value) {
      this.searchTypeno = value
    },
    handleDataTypeChange (value) {
      this.searchDataType = value
    },
    showImport (data) {
      this.visibleUpdate = true
      setTimeout(() => {
        this.setSelect(data[0])
      }, 200)
    },
    setSelect (item) {
      this.$refs.inputSelectVerifyDept.dataSource = [{
        text: item.verifyDeptName,
        value: item.verifyDeptCode
      }]
      this.$refs.inputSelectVerifyDept.value = item.verifyDeptCode

      this.$refs.inputSelectVerifyDoctor.dataSource = [{
        text: item.verifyDoctorName,
        value: item.verifyDoctorCode
      }]
      this.$refs.inputSelectVerifyDoctor.value = item.verifyDoctorCode

      this.selectDate.doctorCode = item.verifyDoctorCode
      this.selectDate.doctorName = item.verifyDoctorName
      this.selectDate.deptCode = item.verifyDeptCode
      this.selectDate.deptName = item.verifyDeptName
    },
    selectDoctorChange (item) {
      this.selectDate.doctorCode = item.value
      this.selectDate.doctorName = item.text
    },
    selectDeptChange (item) {
      this.selectDate.deptCode = item.value
      this.selectDate.deptName = item.text
    },
    showUpdateModal () {
      this.selectDate = {}
      if (this.tableSelectKey === '1') {
        this.$refs.ybReconsiderVerifyStayed.showImport()
      } else {
        this.$refs.ybReconsiderSendStayedMain.showImport()
      }
    },
    handleUpdateOk (e) {
      if (this.tableSelectKey === '1') {
        this.$refs.ybReconsiderVerifyStayed.handImport(this.selectDate)
      } else {
        this.$refs.ybReconsiderSendStayedMain.handImport(this.selectDate)
      }
    },
    handImport () {
      this.selectDate = {}
      this.visibleUpdate = false
    },
    handleUpdateCancel (e) {
      this.selectDate = {}
      this.visibleUpdate = false
    },
    showSearchModal () {
      this.visibleSearch = true
    },
    handleSearchOk (e) {
      this.searchPageService(this.tableSelectKey)
      this.visibleSearch = false
    },
    handleSearchCancel (e) {
      this.visibleSearch = false
    },
    handleDetailSuccess () {
      this.detailVisiable = false
      this.$refs.ybReconsiderVerifyStayed.search()
    },
    handleDetailClose () {
      this.detailVisiable = false
    },
    detail (record) {
      this.$refs.ybReconsiderVerifyViewDetail.setFormValues(record)
      this.detailVisiable = true
    },
    beforeUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      testmsg = testmsg.toLowerCase()
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
      return (isExcel) && isLt2M && this.handleUpload(file)
    },
    handleUpload (file) {
      this.spinning = true
      // 点击删除文件调用removeUpload后会自动调用本方法handleUpload 待解决
      const formData = new FormData()
      formData.append('file', file)
      formData.append('applyDateStr', this.searchApplyDate)
      let dataType = this.tableSelectKey === '1' ? 0 : 1
      formData.append('dataType', dataType)
      formData.append('areaType', this.user.areaType)

      this.$upload('ybReconsiderVerify/importReconsiderDataVerify', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('Excel导入成功.')
          this.spinning = false
          if (dataType === 0) {
            this.$refs.ybReconsiderVerifyStayed.searchPage()
          } else {
            this.$refs.ybReconsiderSendStayedMain.searchPage()
          }
        } else {
          this.$message.error(r.data.data.message)
          this.spinning = false
        }
      }).catch(() => {
        this.fileList = []
        this.$message.error('Excel导入失败 或 该年月无数据导出.')
        this.spinning = false
      })
    },
    exportExcel () {
      let queryParams = {}
      queryParams.applyDateStr = this.searchApplyDate
      queryParams.state = 1
      let dataType = this.tableSelectKey === '1' ? 0 : 1
      queryParams.dataType = dataType
      queryParams.areaType = this.user.areaType
      this.$export('ybReconsiderVerifyView/exportVerify', {
        ...queryParams
      })
    },
    addImport () {
      this.spinning = true
      let url = 'importReconsiderVerify'
      if (this.tableSelectKey === '2') {
        url = 'importMainReconsiderVerify'
      }

      this.$post('ybReconsiderVerify/' + url, {
        applyDate: this.searchApplyDate, areaType: this.user.areaType
      }).then(() => {
        this.spinning = false
        this.$message.success('匹配完成')
        if (this.tableSelectKey === '2') {
          this.$refs.ybReconsiderSendStayedMain.searchPage()
        } else {
          this.$refs.ybReconsiderVerifyStayed.searchPage()
        }
      }).catch(() => {
        this.spinning = false
        this.$message.error('自动匹配操作失败.')
      })
    },
    verifySpin () {
      this.spinning = false
    },
    batchVerify () {
      this.spinning = true
      this.$refs.ybReconsiderVerifyStayed.batchVerify()
      this.pcmVisible = false
    },
    batchVerifyA () {
      this.spinning = true
      this.$refs.ybReconsiderVerifyStayed.batchVerifyA()
    },
    batchSend () {
      this.spinning = true
      if (this.tableSelectKey === '2') {
        this.$refs.ybReconsiderSendStayedMain.batchSend()
      } else {
        this.$refs.ybReconsiderSendStayed.batchSend()
      }
    },
    batchSendA () {
      this.spinning = true
      if (this.tableSelectKey === '2') {
        this.$refs.ybReconsiderSendStayedMain.batchSendA()
      } else {
        this.$refs.ybReconsiderSendStayed.batchSendA()
      }
    },
    confirmCancel () {
      this.pcmVisible = false
    },
    selectChangeKeyVerify (selectedRowKeys) {
      this.selectedPcmRowKeys = selectedRowKeys
    },
    handleVisibleChange (pcmVisible) {
      if (this.selectedPcmRowKeys.length > 0) {
        this.pcmVisible = true
      } else {
        this.pcmVisible = false
      }
    },
    clearValue () {
      this.searchItem.project.projectName = ''
      this.searchItem.rule.ruleName = ''
      this.searchItem.dept.deptName = ''
      this.searchItem.order.orderNumber = ''
    },
    callback (key) {
      this.tableSelectKey = key
      this.clearValue()
      this.searchPageService(key)
    },
    searchPageService (key) {
      if (key === '1') {
        this.$refs.ybReconsiderVerifyStayed.searchPage()
      } else if (key === '2') {
        this.$refs.ybReconsiderSendStayedMain.searchPage()
      } else if (key === '3') {
        this.$refs.ybReconsiderSendStayed.searchPage()
      } else if (key === '4') {
        this.$refs.ybReconsiderSendEnd.searchPage()
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
.editable-row-operations a {
  margin-right: 8px;
}
.card-container {
  border: 1px solid #E0E0E0;
  overflow: hidden;
  padding-top: 3px;
}
</style>
