
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
          <a-col :span=5>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
              <a-month-picker
                placeholder="请输入复议年月"
                style="width: 120px"
                @change="monthChange"
                v-model="searchApplyDate"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=15>
            <a-button
              type="primary"
              style="margin-right: 10px"
              v-show="tableSelectKey==5?false:true"
              @click="showSearchModal"
            >筛选</a-button>
          <a-button
            type="primary"
            style="margin-right: 10px"
            @click.stop="hideMatch"
            v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            自动匹配
          </a-button>
          <a-popover v-model="visibleMatch" trigger="click" :title="tableSelectKey==1? '明细自动匹配':'主单自动匹配'">
            <p slot="content" v-show="tableSelectKey==1?true:false">执行顺序：</p>
            <p slot="content" v-show="tableSelectKey==1?true:false">
              <a-select style="width: 180px" v-model="selectSunxu">
                <a-select-option v-for="item in handleQueryXunxu" :key="item.value" :value="item.value">
                  {{item.text}}
                </a-select-option>
              </a-select>
            </p>
            <p slot="content">
            <a-popconfirm
              title="确定执行匹配？"
              slot="content"
              @confirm="addImport"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">执行匹配</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定删除匹配？"
              slot="content"
              style="margin-left: 10px"
              @confirm="deleteVerify"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">删除匹配</a-button>
            </a-popconfirm>
            </p>
          </a-popover>
            <a-button
              type="primary"
              style="margin-right: 10px"
              v-show="tableSelectKey==1||tableSelectKey==2?true:false"
              @click="showUpdateModal"
            >手动匹配</a-button>
            <a-popconfirm
              title="确定批量核对?"
              style="margin-right: 10px"
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
              style="margin-right: 10px"
              v-show="tableSelectKey==1?true:false"
              @confirm="batchVerifyA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部核对</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部返回？"
              style="margin-right: 10px"
              v-show="tableSelectKey==3?true:false"
              @confirm="batchAllBack"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部返回</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定批量发送？"
              style="margin-right: 10px"
              v-show="tableSelectKey==2||tableSelectKey==3?true:false"
              @confirm="batchSend"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">批量发送</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部发送？"
              style="margin-right: 10px"
              v-show="tableSelectKey==2||tableSelectKey==3?true:false"
              @confirm="batchSendA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部发送</a-button>
            </a-popconfirm>
            <a-button type="danger" @click="showDateModal"
            v-show="tableSelectKey==2||tableSelectKey==3||tableSelectKey==4||tableSelectKey==5?true:false" style="margin-right: 15px">日期</a-button>
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
            v-show="tableSelectKey==4 || tableSelectKey==5?true:false"
            >
              <a-select-option
              v-for="d in selectTypenoDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            <a-button  v-show="tableSelectKey==5?false:true"
              type="primary"
              style="margin-right: 10px"
              @click="searchTable"
            >刷新</a-button>
          <a-checkbox :checked="checked"  @change="onChange" v-show="tableSelectKey==5?true:false">
            是否发送
          </a-checkbox>
          <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 170px" enter-button @search="searchTable" v-show="tableSelectKey==5?true:false" />
          <a-popconfirm
              title="确定发送短信？"
              @confirm="sendSms"
              okText="确定"
              style="margin-left: 10px"
              cancelText="取消"
              v-show="tableSelectKey==5?true:false"
            >
              <a-button type="primary">发送短信</a-button>
            </a-popconfirm>
            <a-button type="primary" style="margin-left: 15px" @click.stop="hideJob"  v-show="tableSelectKey==4 || tableSelectKey==5?true:false">
              开启服务
            </a-button>
            <a-popover v-model="visibleJob" placement="top" trigger="click" title="开启服务">
              <a-popconfirm
                slot="content"
                title="确定开启复议截止服务？"
                style="margin-right: 10px"
                @confirm="startJob(1)"
                okText="确定"
                cancelText="取消"
              >
              <a>1.复议截止服务</a>
              </a-popconfirm>
              <a-popconfirm
                slot="content"
                title="确定开启确认截止服务？"
                style="margin-right: 10px"
                @confirm="startJob(2)"
                okText="确定"
                cancelText="取消"
              >
              <a>2.确认截止服务</a>
              </a-popconfirm>
              <a-popconfirm
                slot="content"
                title="确定开启截止提醒服务？"
                style="margin-right: 10px"
                @confirm="startJob(3)"
                okText="确定"
                cancelText="取消"
              >
              <a>3.截止提醒服务</a>
              </a-popconfirm>
              <!-- <a-popconfirm
                slot="content"
                title="确定开启1和2服务？"
                style="margin-right: 10px"
                @confirm="startJob(4)"
                okText="确定"
                cancelText="取消"
              >
              <a>1和2服务</a>
              </a-popconfirm> -->
              <a-popconfirm
                slot="content"
                title="确定开启全部服务？"
                style="margin-right: 10px"
                @confirm="startJob(5)"
                okText="确定"
                cancelText="取消"
              >
              <a>全部服务</a>
              </a-popconfirm>
            </a-popover>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey==1||tableSelectKey==2?true:false">
            <a-upload
                name="file"
                accept=".xlsx,.xls"
                style="margin-right: 10px"
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
          <a-tab-pane
            key="5"
            :forceRender="true"
            tab="核对短信"
          >
            <ybReconsiderVerify-sms
              ref="ybReconsiderVerifySms"
              :applyDateStr="searchApplyDate"
              :searchText="searchText"
              :checked="checked"
              :searchTypeno="searchTypeno"
            >
            </ybReconsiderVerify-sms>
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
              label="交易流水号"
            >
              <a-input style="width: 255px"  v-model="searchItem.serial.serialNo" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="项目名称："
            >
              <a-input style="width: 255px"  v-model="searchItem.project.projectName" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="规则名称："
            >
              <a-input style="width: 255px" v-model="searchItem.rule.ruleName" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="科室名称："
            >
              <a-input style="width: 255px" v-model="searchItem.dept.deptName" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="医生工号"
            >
              <a-input style="width: 255px" v-model="searchItem.doctor.docCode" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="医生姓名"
            >
              <a-input style="width: 255px" v-model="searchItem.doctor.docName" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="序号编码："
            >
              <a-input style="width: 255px" v-model="searchItem.order.orderNumber" />
            </a-form-item>
          </p>
        </a-modal>
      </div>
    </template>
    <!--手动匹配Modal-->
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
              dept='医生'
              @selectChange=selectDoctorChange
            >
            </input-select>
          </a-form-item>
        </a-modal>
      </div>
    </template>
    <!--变更日期Modal-->
    <template>
      <div>
        <a-modal
          v-model="visibleDate"
          title="变更日期"
          on-ok="handleDateOk"
        >
          <template slot="footer">
            <a-button
              key="back"
              @click="handleDateCancel"
            >
              取消
            </a-button>
            <a-popconfirm
              title="确定匹配？"
              @confirm="handleDateOk"
              :disabled="reconsiderApply.id == null?true:false"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" :disabled="reconsiderApply.id == null?true:false" style="margin-right: .8rem">确定</a-button>
            </a-popconfirm>
          </template>
          <a-row>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
              <a-month-picker
                placeholder="请输入复议年月"
                style="width: 120px"
                @change="monthChange"
                v-model="searchApplyDate"
                :default-value="searchApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          <a-form-item
            v-bind="formItemLayout"
            v-show="searchTypeno==1?true:false"
            label="第一版截止日期"
          >
          <a-date-picker
            placeholder="请输入第一版截止日期"
            style="width:220px"
            v-model="reconsiderApply.endDateOne"
            show-time
            :format="dayFormat"/>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item
            v-bind="formItemLayout"
            v-show="searchTypeno==1?true:false"
            label="第一版确认日期"
          >
          <a-date-picker
            placeholder="请输入第一版确认日期"
            style="width:220px"
            v-model="reconsiderApply.enableDateOne"
            :format="enableFormat"/>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item
            v-bind="formItemLayout"
            v-show="searchTypeno==2?true:false"
            label="第二版截止日期"
          >
          <a-date-picker
            placeholder="请输入第二版截止日期"
            style="width:220px"
            v-model="reconsiderApply.endDateTwo"
            show-time
            :format="dayFormat"/>
          </a-form-item>
        </a-row>
        <a-row>
        <a-form-item
          v-bind="formItemLayout"
          v-show="searchTypeno==2?true:false"
          label="第二版确认日期"
        >
        <a-date-picker
          placeholder="请输入第二版确认日期"
          style="width:220px"
          v-model="reconsiderApply.enableDateTwo"
          :format="enableFormat"/>
        </a-form-item>
      </a-row>
      <a-row  v-show="tableSelectKey==5?true:false">
        <a-form-item
          v-bind="formItemLayout"
          label="是否更改短信日期"
        >
        <a-checkbox :checked="dateChecked" @change="onIsDateChange" />
        </a-form-item>
      </a-row>
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
import YbReconsiderVerifySms from './YbReconsiderVerifySms'
import InputSelect from '../../common/InputSelect'
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 15, offset: 1 }
}
export default {
  name: 'YbReconsiderVerifyView',
  components: { InputSelect, YbReconsiderVerifyViewDetail, YbReconsiderVerifyStayed, YbReconsiderSendStayed, YbReconsiderSendStayedMain, YbReconsiderSendEnd, YbReconsiderVerifySms },
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      enableFormat: 'YYYY-MM-DD',
      dayFormat: 'YYYY-MM-DD HH:mm:ss',
      detailVisiable: false,
      ybReconsiderVerify: {},
      tableSelectKey: '1',
      searchApplyDate: this.formatDate(),
      visibleSearch: false,
      visibleUpdate: false,
      visibleJob: false,
      visibleMatch: false,
      pcmVisible: false,
      selectedPcmRowKeys: [],
      spinning: false,
      delayTime: 500,
      selectDate: {},
      fileList: [],
      searchTypeno: 1,
      searchDataType: 0,
      reconsiderApply: {id: null},
      checked: false,
      searchText: '',
      selectSunxu: 1,
      dateChecked: false,
      visibleDate: false,
      handleQueryXunxu: [
        {text: '1、规则项目科室', value: 1},
        {text: '2、科室项目规则', value: 2},
        {text: '3、科室规则项目', value: 3},
        {text: '4、项目科室规则', value: 4},
        {text: '5、项目规则科室', value: 5},
        {text: '6、规则科室项目', value: 6}
      ],
      selectTypenoDataSource: [{text: '版本一', value: 1}, {text: '版本二', value: 2}],
      selectDataTypeDataSource: [{text: '明细扣款', value: 0}, {text: '主单扣款', value: 1}],
      searchItem: {
        serial: {serialNo: ''},
        project: {projectName: ''},
        rule: {ruleName: ''},
        dept: {deptName: ''},
        doctor: {docName: '', docCode: ''},
        order: {orderNumber: ''}
      },
      user: this.$store.state.account.user
    }
  },
  computed: {
  },
  mounted () {
    this.initTypeno(this.searchApplyDate)
    this.selectSunxu = this.user.areaType.value === 0 ? 1 : 2
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
      this.$get('ybReconsiderApply/getReconsiderApply', {
        applyDateStr: applyDateStr, areaType: this.user.areaType.value
      }).then((r) => {
        if (r.data.data.success === 1) {
          let typeno = parseInt(r.data.data.typeno)
          this.searchTypeno = typeno === 3 ? 2 : typeno
          if (r.data.data.apply != null) {
            if (this.searchTypeno === 1) {
              this.reconsiderApply = {
                id: r.data.data.apply.id,
                endDateOne: r.data.data.apply.endDateOne,
                enableDateOne: r.data.data.apply.enableDateOne
              }
            } else {
              this.reconsiderApply = {
                id: r.data.data.apply.id,
                endDateTwo: r.data.data.apply.endDateTwo,
                enableDateTwo: r.data.data.apply.enableDateTwo
              }
            }
          } else {
            this.reconsiderApply = {id: null}
          }
        } else {
          this.searchTypeno = 1
        }
      })
    },
    onIsDateChange () {
      this.dateChecked = !this.dateChecked
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
    handleDateOk (e) {
      if (this.tableSelectKey !== '5') {
        this.dateChecked = false
        this.reconsiderApply.isChangDate = 0
      }
      if (this.dateChecked) {
        this.reconsiderApply.isChangDate = 1
      } else {
        this.reconsiderApply.isChangDate = 0
      }
      if (this.reconsiderApply.id !== null) {
        if (this.searchTypeno === 1) {
          if (this.reconsiderApply.endDateOne === null) {
            this.$message.warning('当前' + this.searchApplyDate + ',第一版截止日期 不能为空.')
            return false
          } else {
            this.reconsiderApply.endDateOne = moment(this.reconsiderApply.endDateOne)
          }
          if (this.reconsiderApply.enableDateOne === null) {
            this.$message.warning('当前' + this.searchApplyDate + ',第一版确认日期 不能为空.')
            return false
          } else {
            this.reconsiderApply.enableDateOne = moment(this.reconsiderApply.enableDateOne)
          }
        } else {
          if (this.reconsiderApply.endDateTwo === null) {
            this.$message.warning('当前' + this.searchApplyDate + ',第二版截止日期 不能为空.')
            return false
          } else {
            this.reconsiderApply.endDateTwo = moment(this.reconsiderApply.endDateTwo)
          }
          if (this.reconsiderApply.enableDateTwo === null) {
            this.$message.warning('当前' + this.searchApplyDate + ',第二版确认日期 不能为空.')
            return false
          } else {
            this.reconsiderApply.enableDateTwo = moment(this.reconsiderApply.enableDateTwo)
          }
        }
        this.reconsiderApply.areaType = this.user.areaType.value
        let ybReconsiderApply = this.reconsiderApply
        this.$put('ybReconsiderApply/updateReconsiderApply', {
          ...ybReconsiderApply
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.$message.success('当前' + this.searchApplyDate + '修改日期成功.')
            this.visibleDate = false
            if (this.dateChecked && this.tableSelectKey === '5') {
              this.$refs.ybReconsiderVerifySms.searchPage()
            }
          } else {
            this.$message.warning(r.data.data.message)
          }
        }).catch(() => {
          this.$message.error('当前' + this.searchApplyDate + '修改日期失败.')
        })
      } else {
        this.$message.warning('当前' + this.searchApplyDate + '无法修改日期.')
      }
    },
    handleDateCancel (e) {
      this.visibleDate = false
    },
    showDateModal () {
      this.dateChecked = false
      if (this.reconsiderApply.id !== null) {
        this.visibleDate = true
      } else {
        this.$message.error('当前' + this.searchApplyDate + '无复议申请数据.')
      }
    },
    hideJob () {
      this.visibleJob = true
    },
    hideMatch () {
      this.visibleMatch = true
    },
    startJob (type) {
      let types = [type]
      if (type === 4) {
        types = [1, 2]
      }
      if (type === 5) {
        types = [1, 2, 3]
      }
      this.$put('ybReconsiderVerify/startJob', {
        applyDateStr: this.searchApplyDate,
        areaType: this.user.areaType.value,
        jobTypeList: types
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('启动Job成功')
        } else {
          this.$message.warning(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('启动Job失败')
      })
    },
    deleteVerify () {
      let param = {
        applyDateStr: this.searchApplyDate,
        areaType: this.user.areaType.value,
        dataType: this.tableSelectKey === '2' ? 1 : 0
      }
      this.$delete('ybReconsiderVerify/deleteVerifyState', param).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('删除成功.')
          if (this.tableSelectKey === '2') {
            this.$refs.ybReconsiderSendStayedMain.searchPage()
          } else {
            this.$refs.ybReconsiderVerifyStayed.searchPage()
          }
        } else {
          this.$message.warning(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('删除失败.')
      })
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
      const isLt2M = file.size / 1024 / 1024 < 10
      if (!isLt2M) {
        this.$error({
          title: '超10M限制，不允许上传~'
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
      formData.append('areaType', this.user.areaType.value)

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
    onChange () {
      this.checked = !this.checked
      setTimeout(() => {
        this.searchTable()
      }, 500)
    },
    exportExcel () {
      let queryParams = {}
      queryParams.applyDateStr = this.searchApplyDate
      queryParams.state = 1
      let dataType = this.tableSelectKey === '1' ? 0 : 1
      queryParams.dataType = dataType
      queryParams.areaType = this.user.areaType.value
      this.$export('ybReconsiderVerifyView/exportVerify', {
        ...queryParams
      })
    },
    addImport () {
      this.spinning = true
      let url = ''
      let param = {}
      if (this.tableSelectKey === '2') {
        url = 'importMainReconsiderVerify'
        param = {
          applyDate: this.searchApplyDate, areaType: this.user.areaType.value
        }
      } else {
        let sumxu = []
        if (this.selectSunxu === 1) {
          sumxu = [1, 2, 3] // 1、规则项目科室
        } else if (this.selectSunxu === 2) {
          sumxu = [3, 2, 1] // 2、科室项目规则
        } else if (this.selectSunxu === 3) {
          sumxu = [3, 1, 2] // 3、科室规则项目
        } else if (this.selectSunxu === 4) {
          sumxu = [2, 3, 1] // 4、项目科室规则
        } else if (this.selectSunxu === 5) {
          sumxu = [2, 1, 3] // 5、项目规则科室
        } else {
          sumxu = [1, 3, 2] // 6、规则科室项目
        }
        url = 'importReconsiderVerify'
        param = {
          applyDate: this.searchApplyDate, areaType: this.user.areaType.value, sumxu: sumxu
        }
      }
      this.$post('ybReconsiderVerify/' + url, param).then(() => {
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
      this.visibleMatch = false
    },
    verifySpin () {
      this.spinning = false
    },
    batchAllBack () {
      this.spinning = true
      this.$refs.ybReconsiderSendStayed.batchAllBack()
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
    handleVisibleChange () {
      if (this.selectedPcmRowKeys.length > 0) {
        this.pcmVisible = true
      } else {
        this.pcmVisible = false
      }
    },
    clearValue () {
      this.searchItem.serial.serialNo = ''
      this.searchItem.project.projectName = ''
      this.searchItem.rule.ruleName = ''
      this.searchItem.dept.deptName = ''
      this.searchItem.doctor.docCode = ''
      this.searchItem.doctor.docName = ''
      this.searchItem.order.orderNumber = ''
    },
    sendSms () {
      if (this.tableSelectKey === '5') {
        this.spinning = true
        let key = this.tableSelectKey
        this.$put('comSms/sendSms', {
          applyDateStr: this.searchApplyDate,
          areaType: this.user.areaType.value,
          typeno: this.searchTypeno,
          sendType: 1,
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
      } else if (key === '5') {
        this.$refs.ybReconsiderVerifySms.searchPage()
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
