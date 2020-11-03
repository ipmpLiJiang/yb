
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
        <a-col :span=2>
          &nbsp;
        </a-col>
          <a-col :span=6>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
              <a-month-picker
                placeholder="请输入复议年月"
                disabled
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey==1||tableSelectKey==4?true:false">
            <a-button
              type="primary"
              @click="showSearchModal"
            >筛选</a-button>
          </a-col>
          <a-col :span=12>
            <a-popconfirm
              title="确定自动匹配？"
              @confirm="addImport"
              v-show="tableSelectKey==1||tableSelectKey==4?true:false"
              okText="确定"
              style="margin-right: 15px"
              cancelText="取消"
            >
              <a-button type="primary">自动匹配</a-button>
            </a-popconfirm>
            <a-button
              type="primary"
              style="margin-right: 15px"
              v-show="tableSelectKey==1||tableSelectKey==4?true:false"
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
              title="确定批量发送？"
              style="margin-right: 15px"
              v-show="tableSelectKey==2||tableSelectKey==4?true:false"
              @confirm="batchSend"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">批量发送</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部发送？"
              style="margin-right: 15px"
              v-show="tableSelectKey==2||tableSelectKey==4?true:false"
              @confirm="batchSendA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部发送</a-button>
            </a-popconfirm>
            <a-button
              type="primary"
              @click="searchTable"
            >刷新</a-button>
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
              :applyDate='defaultApplyDate'
              :searchItem='searchItem'
              @selectChangeKeyVerify="selectChangeKeyVerify"
              @detail="detail"
              @showImport="showImport"
              @handImport="handImport"
              @batchVerify="batchVerify"
            >
            </ybReconsiderVerify-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="主单待核对"
          >
            <ybReconsiderSendStayed-main
              ref="ybReconsiderSendStayedMain"
              :applyDate='defaultApplyDate'
              :searchItem='searchItem'
              @showImport="showImport"
              @handImport="handImport"
            >
            </ybReconsiderSendStayed-main>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已核对"
          >
            <ybReconsiderSend-stayed
              ref="ybReconsiderSendStayed"
              :applyDate='defaultApplyDate'
            >
            </ybReconsiderSend-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="已完成"
          >
            <ybReconsiderSend-end
              ref="ybReconsiderSendEnd"
              :applyDate='defaultApplyDate'
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
      visibleSearch: false,
      visibleUpdate: false,
      pcmVisible: false,
      selectedPcmRowKeys: [],
      spinning: false,
      delayTime: 500,
      selectDate: {},
      defaultApplyDate: this.formatDate(),
      handleQuerySymbol: [
        {text: '等于', value: 'EQ'},
        {text: '包含', value: 'LIKE'},
        {text: '不包含', value: 'NOTLIKE'}
      ],
      searchItem: {
        project: {type: 'LIKE', projectName: ''},
        rule: {type: 'LIKE', ruleName: ''},
        dept: {type: 'LIKE', deptName: ''}
      }
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
      if (this.tableSelectKey === '1') {
        this.$refs.ybReconsiderVerifyStayed.search()
      } else {
        this.$refs.ybReconsiderSendStayedMain.search()
      }
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
    addImport () {
      this.spinning = true
      let url = 'importReconsiderVerify'
      if (this.tableSelectKey === '4') {
        url = 'importMainReconsiderVerify'
      }

      this.$post('ybReconsiderVerify/' + url, {
        applyDate: this.defaultApplyDate
      }).then(() => {
        this.spinning = false
        this.$message.success('匹配完成')
        if (this.tableSelectKey === '4') {
          this.$refs.ybReconsiderSendStayedMain.search()
        } else {
          this.$refs.ybReconsiderVerifyStayed.search()
        }
      }).catch(() => {
        this.spinning = false
        this.$message.error('自动匹配操作失败.')
      })
    },
    batchVerify () {
      this.$refs.ybReconsiderVerifyStayed.batchVerify()
      this.pcmVisible = false
    },
    batchSend () {
      if (this.tableSelectKey === '4') {
        this.$refs.ybReconsiderSendStayedMain.batchSend()
      } else {
        this.$refs.ybReconsiderSendStayed.batchSend()
      }
    },
    batchSendA () {
      if (this.tableSelectKey === '4') {
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
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.clearValue()
        this.$refs.ybReconsiderVerifyStayed.search()
      } else if (key === '2') {
        this.$refs.ybReconsiderSendStayed.search()
      } else if (key === '3') {
        this.$refs.ybReconsiderSendEnd.search()
      } else if (key === '4') {
        this.$refs.ybReconsiderSendStayedMain.search()
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
