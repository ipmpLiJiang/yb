
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
          <a-col :span=5 style="margin-bottom:16px">
            复议年月：
            <a-month-picker
              placeholder="请输入复议年月"
              style="width: 105px;margin-right: 3px"
              @change="monthChange"
              v-model="searchApplyDate"
              :default-value="searchApplyDate"
              :format="monthFormat"
            />
          </a-col>
          <a-col :span=15>
            <a-button
              type="primary"
              v-show="tableSelectKey==4?false:true"
              style="margin-right: 8px"
              @click="showSearchModal"
            >筛选</a-button>
          <a-button
            type="primary"
            style="margin-right: 8px"
            @click.stop="hideMatch"
            v-show="tableSelectKey==1?true:false">
            自动匹配
          </a-button>
          <a-popover v-model="visibleMatch" trigger="click" title="自动匹配">
            <a-popconfirm
              title="确定执行匹配？"
              slot="content"
              @confirm="addImport"
              okText="确定"
              cancelText="取消"
            >
              <a-button >执行匹配</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定删除匹配？"
              slot="content"
              style="margin-left: 8px"
              @confirm="deleteVerify"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="danger">删除匹配</a-button>
            </a-popconfirm>
            <a-button slot="content" style="margin-left: 8px" @click="handleWppShow">显示</a-button>
          </a-popover>
            <a-button
              type="primary"
              style="margin-right: 8px"
              v-show="tableSelectKey==1?true:false"
              @click="showUpdateModal"
            >手动匹配</a-button>
            <a-popconfirm
              title="确定批量核对?"
              style="margin-right: 8px"
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
              style="margin-right: 8px"
              v-show="tableSelectKey==1?true:false"
              @confirm="batchVerifyA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部核对</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部返回？"
              style="margin-right: 8px"
              v-show="tableSelectKey==2?true:false"
              @confirm="batchAllBack"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部返回</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定批量发送？"
              style="margin-right: 8px"
              v-show="tableSelectKey==2?true:false"
              @confirm="batchSend"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">批量发送</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定全部发送？"
              style="margin-right: 8px"
              v-show="tableSelectKey==2?true:false"
              @confirm="batchSendA"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary">全部发送</a-button>
            </a-popconfirm>
            <a-button type="danger"
              @click="showDateModal"
              v-show="tableSelectKey==2||tableSelectKey==3||tableSelectKey==4?true:false"
              style="margin-right: 8px">日期</a-button>
            <a-button  v-show="tableSelectKey==4?false:true"
              type="primary"
              style="margin-right: 8px"
              @click="searchTable"
            >刷新</a-button>
          <a-checkbox :checked="checked"  @change="onChange" v-show="tableSelectKey==4?true:false">
            是否发送
          </a-checkbox>
          <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 160px" enter-button @search="searchTable" v-show="tableSelectKey==4?true:false" />
          <a-popconfirm
              title="确定发送短信？"
              @confirm="sendSms"
              okText="确定"
              style="margin-left: 8px"
              cancelText="取消"
              v-show="tableSelectKey==4?true:false"
            >
              <a-button type="primary">发送短信</a-button>
            </a-popconfirm>
            <a-button type="primary" style="margin-left: 15px" @click.stop="hideJob"  v-show="tableSelectKey==3 || tableSelectKey==4?true:false">
              开启服务
            </a-button>
            <a-popover v-model="visibleJob" trigger="click" title="开启服务">
              <a-popconfirm
                slot="content"
                title="确定开启截止服务？"
                style="margin-right: 8px"
                @confirm="startJob(1)"
                okText="确定"
                cancelText="取消"
              >
              <a>1.截止服务</a>
              </a-popconfirm>
              <a-popconfirm
                slot="content"
                title="确定开启确认截止服务？"
                style="margin-right: 8px"
                @confirm="startJob(2)"
                okText="确定"
                cancelText="取消"
              >
              <a>2.确认截止服务</a>
              </a-popconfirm>
              <a-popconfirm
                slot="content"
                title="确定开启截止提醒服务？"
                style="margin-right: 8px"
                @confirm="startJob(3)"
                okText="确定"
                cancelText="取消"
              >
              <a>3.截止提醒服务</a>
              </a-popconfirm>
              <!-- <a-popconfirm
                slot="content"
                title="确定开启1和2服务？"
                style="margin-right: 8px"
                @confirm="startJob(4)"
                okText="确定"
                cancelText="取消"
              >
              <a>1和2服务</a>
              </a-popconfirm> -->
              <a-popconfirm
                slot="content"
                title="确定开启全部服务？"
                style="margin-right: 8px"
                @confirm="startJob(5)"
                okText="确定"
                cancelText="取消"
              >
              <a>全部服务</a>
              </a-popconfirm>
            </a-popover>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey==1?true:false">
            <a-upload
                name="file"
                accept=".xlsx,.xls"
                :fileList="fileList"
                :beforeUpload="beforeUpload"
              >
                <a-button type="primary"> 上传 </a-button>
              </a-upload>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey==1?true:false">
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
      <template>
      <a-modal width="55%" :maskClosable="false" :footer="null"
        v-model="wppVisible" title="未匹配规则项目" @ok="handleWppOk">
        <a-table
          :columns="wppColumns"
          :rowKey="record => record.ids"
          :pagination="{defaultPageSize: 15}"
          :data-source="wppDataSource"
          size="small"
          bordered :scroll="{ x: 500 }">
        </a-table>
      </a-modal>
    </template>
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
            tab="待核对"
          >
            <ybChsVerify-stayed
              ref="ybChsVerifyStayed"
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
            </ybChsVerify-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已核对"
          >
            <ybChsSend-stayed
              ref="ybChsSendStayed"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
              @verifySpin="verifySpin"
            >
            </ybChsSend-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="已完成"
          >
            <ybChsSend-end
              ref="ybChsSendEnd"
              :searchItem='searchItem'
              :applyDate='searchApplyDate'
            >
            </ybChsSend-end>
          </a-tab-pane>
          <a-tab-pane
            key="4"
            :forceRender="true"
            tab="核对短信"
          >
            <ybChsVerify-sms
              ref="ybChsVerifySms"
              :applyDateStr="searchApplyDate"
              :searchText="searchText"
              :checked="checked"
            >
            </ybChsVerify-sms>
          </a-tab-pane>
        </a-tabs>
      </div>
      <!-- 修改字典 -->
      <ybChsVerify-detail
        ref="ybChsVerifyDetail"
        @close="handleDetailClose"
        @success="handleDetailSuccess"
        :detailVisiable="detailVisiable"
      >
      </ybChsVerify-detail>
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
              label="住院门诊号"
            >
              <a-input style="width: 255px"  v-model="searchItem.item.zymzNumber" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="参保人"
            >
              <a-input style="width: 255px"  v-model="searchItem.item.insuredName" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="项目名称"
            >
              <a-input style="width: 255px" v-model="searchItem.item.projectName" />
            </a-form-item>
            <a-form-item
              v-bind="formItemLayout"
              label="汇总科室："
            >
              <a-input style="width: 255px" v-model="searchItem.dept.dksName" />
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
              <a-input-number style="width: 255px" v-model="searchItem.order.orderNum" />
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
            label="汇总科室"
          >
            <inputSelectChs-dks
              ref="inputSelectVerifyChsDks"
              @selectChange=selectDksChange
            >
            </inputSelectChs-dks>
          </a-form-item>
          <a-form-item
            v-bind="formItemLayout"
            label="复议医生"
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
              :disabled="chsApply.id == null?true:false"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" :disabled="chsApply.id == null?true:false" style="margin-right: .8rem">确定</a-button>
            </a-popconfirm>
          </template>
          <a-row>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
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
            label="截止日期"
          >
          <a-date-picker
            placeholder="请输入截止日期"
            style="width:220px"
            v-model="chsApply.endDate"
            show-time
            :format="dayFormat"/>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item
            v-bind="formItemLayout"
            label="确认日期"
          >
          <a-date-picker
            placeholder="请输入确认日期"
            style="width:220px"
            v-model="chsApply.enableDate"
            :format="enableFormat"/>
          </a-form-item>
        </a-row>
        <a-row  v-show="tableSelectKey==4?true:false">
        <a-form-item
          v-bind="formItemLayout"
          label="更改已发送数据日期"
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
import YbChsVerifyStayed from './YbChsVerifyStayed'
import YbChsSendStayed from './YbChsSendStayed'
import YbChsSendEnd from './YbChsSendEnd'
import YbChsVerifyDetail from './YbChsVerifyDetail'
import YbChsVerifySms from './YbChsVerifySms'
import InputSelect from '../../common/InputSelect'
import InputSelectChsDks from '../../common/InputSelectChsDks'
import { fy } from '../../js/custom'
const formItemLayout = {
  labelCol: { span: 7 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'ybChsVerify',
  components: { InputSelect, InputSelectChsDks, YbChsVerifyDetail, YbChsVerifyStayed, YbChsSendStayed, YbChsSendEnd, YbChsVerifySms },
  data () {
    return {
      formItemLayout,
      loading: false,
      monthFormat: 'YYYY-MM',
      enableFormat: 'YYYY-MM-DD',
      dayFormat: 'YYYY-MM-DD HH:mm:ss',
      detailVisiable: false,
      ybChsVerify: {},
      tableSelectKey: '1',
      searchApplyDate: this.formatDate(),
      visibleSearch: false,
      visibleUpdate: false,
      visibleJob: false,
      visibleMatch: false,
      pcmVisible: false,
      wppVisible: false,
      wppDataSource: [],
      selectedPcmRowKeys: [],
      spinning: false,
      delayTime: 500,
      selectDate: {},
      fileList: [],
      chsApply: {id: null},
      checked: false,
      dateChecked: false,
      searchText: '',
      visibleDate: false,
      searchItem: {
        item: {zymzNumber: '', insuredName: '', projectName: ''},
        dept: {dksName: ''},
        doctor: {docName: '', docCode: ''},
        order: {orderNum: null}
      },
      user: this.$store.state.account.user
    }
  },
  computed: {
    wppColumns () {
      return [{
        title: '规则名称',
        dataIndex: 'ruleName',
        width: 180
      },
      {
        title: '项目名称',
        dataIndex: 'projectName',
        width: 180
      },
      {
        title: '接口是否匹配',
        dataIndex: 'currencyField',
        width: 60
      },
      {
        title: '类型',
        dataIndex: 'zymzName',
        width: 80
      }]
    }
  },
  mounted () {
    this.initApplyDate(this.searchApplyDate)
  },
  methods: {
    moment,
    formatDate () {
      let datemonth = moment().subtract(1, 'months').format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
      this.initApplyDate(dateString)
    },
    initApplyDate (applyDateStr) {
      this.$get('ybChsApply/getChsApply', {
        applyDateStr: applyDateStr, areaType: this.user.areaType.value
      }).then((r) => {
        if (r.data.data.success === 1) {
          if (r.data.data.apply != null) {
            this.chsApply = {
              id: r.data.data.apply.id,
              endDate: r.data.data.apply.endDate,
              enableDate: r.data.data.apply.enableDate
            }
          } else {
            this.chsApply = {id: null}
          }
        } else {
        }
      })
    },
    showImport (data) {
      this.visibleUpdate = true
      setTimeout(() => {
        this.setSelect(data[0])
      }, 200)
    },
    setSelect (item) {
      if (item.verifyFyid) {
        this.$refs.inputSelectVerifyChsDks.dataSource = [{
          text: fy.getDksFyName(item.verifyDksName, item.verifyFyid),
          value: item.verifyDksId
        }]
        this.$refs.inputSelectVerifyChsDks.value = item.verifyDksId
      } else {
        this.$refs.inputSelectVerifyChsDks.dataSource = []
        this.$refs.inputSelectVerifyChsDks.value = ''
      }

      if (item.verifyDoctorCode) {
        this.$refs.inputSelectVerifyDoctor.dataSource = [{
          text: item.verifyDoctorCode + '-' + item.verifyDoctorName,
          value: item.verifyDoctorCode
        }]
        this.$refs.inputSelectVerifyDoctor.value = item.verifyDoctorCode
      } else {
        this.$refs.inputSelectVerifyDoctor.dataSource = []
        this.$refs.inputSelectVerifyDoctor.value = ''
      }

      this.selectDate.doctorCode = item.verifyDoctorCode
      this.selectDate.doctorName = item.verifyDoctorName
      this.selectDate.dksId = item.verifyDksId
      this.selectDate.dksName = item.verifyDksName
      this.selectDate.fyid = item.verifyFyid
    },
    selectDoctorChange (item) {
      this.selectDate.doctorCode = item.value
      this.selectDate.doctorName = item.personName
    },
    selectDksChange (item) {
      this.selectDate.dksId = item.value
      this.selectDate.dksName = item.dksName
      this.selectDate.fyid = item.fyid
    },
    onIsDateChange () {
      this.dateChecked = !this.dateChecked
    },
    showUpdateModal () {
      this.selectDate = {}
      this.$refs.ybChsVerifyStayed.showImport()
    },
    handleUpdateOk (e) {
      this.$refs.ybChsVerifyStayed.handImport(this.selectDate)
    },
    handleDateOk (e) {
      if (this.tableSelectKey !== '4') {
        this.dateChecked = false
        this.chsApply.isChangDate = 0
      }
      if (this.dateChecked) {
        this.chsApply.isChangDate = 1
      } else {
        this.chsApply.isChangDate = 0
      }
      if (this.chsApply.id !== null) {
        if (this.chsApply.endDate === null) {
          this.$message.warning('当前' + this.searchApplyDate + ',截止日期 不能为空.')
          return false
        } else {
          this.chsApply.endDate = moment(this.chsApply.endDate)
        }
        if (this.chsApply.enableDate === null) {
          this.$message.warning('当前' + this.searchApplyDate + ',确认日期 不能为空.')
          return false
        } else {
          this.chsApply.enableDate = moment(this.chsApply.enableDate)
        }
        let ybChsApply = this.chsApply
        this.$put('ybChsApply/updateChsApply', {
          ...ybChsApply
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.$message.success('当前' + this.searchApplyDate + '修改日期成功.')
            this.visibleDate = false
            if (this.dateChecked && this.tableSelectKey === '4') {
              this.searchTable()
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
      if (this.chsApply.id !== null) {
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
      this.$put('ybChsVerify/startJob', {
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
        areaType: this.user.areaType.value
      }
      this.$delete('ybChsVerify/deleteVerifyState', param).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('删除成功.')
          this.wppDataSource = []
          this.$refs.ybChsVerifyStayed.searchPage()
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
      this.$refs.ybChsVerifyStayed.search()
    },
    handleDetailClose () {
      this.detailVisiable = false
    },
    detail (record) {
      this.$refs.ybChsVerifyDetail.setFormValues(record)
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
      formData.append('areaType', this.user.areaType.value)

      this.$upload('ybChsVerify/importChsDataVerify', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('Excel导入成功.')
          this.spinning = false
          this.$refs.ybChsVerifyStayed.searchPage()
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
      queryParams.areaType = this.user.areaType.value
      this.$export('ybChsVerifyView/exportVerify', {
        ...queryParams
      })
    },
    handleWppOk () {
      this.wppVisible = false
    },
    handleWppShow () {
      this.wppVisible = true
      this.wppDataSource = []
      let params = {}
      params.applyDateStr = this.searchApplyDate
      params.areaType = this.user.areaType.value
      this.$get('ybChsVerifyMsg/getList', {
        ...params
      }).then((r) => {
        this.wppDataSource = r.data.data
      })
    },
    addImport () {
      this.spinning = true
      let param = {
        applyDate: this.searchApplyDate, areaType: this.user.areaType.value
      }
      this.$post('ybChsVerify/importChsVerify', param).then((r) => {
        if (r.data.data.success === 1) {
          this.spinning = false
          this.$message.success('匹配完成')
          this.$refs.ybChsVerifyStayed.searchPage()
          if (r.data.data.data.length > 0) {
            this.wppVisible = true
            this.wppDataSource = r.data.data.data
          }
        } else {
          this.$message.error(r.data.data.message)
          this.spinning = false
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
      this.$refs.ybChsSendStayed.batchAllBack()
    },
    batchVerify () {
      this.spinning = true
      this.$refs.ybChsVerifyStayed.batchVerify()
      this.pcmVisible = false
    },
    batchVerifyA () {
      this.spinning = true
      this.$refs.ybChsVerifyStayed.batchVerifyA()
    },
    batchSend () {
      this.spinning = true
      this.$refs.ybChsSendStayed.batchSend()
    },
    batchSendA () {
      this.spinning = true
      this.$refs.ybChsSendStayed.batchSendA()
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
      this.searchItem.item.zymzNumber = ''
      this.searchItem.item.insuredName = ''
      this.searchItem.item.projectName = ''
      this.searchItem.dept.dksName = ''
      this.searchItem.doctor.docCode = ''
      this.searchItem.doctor.docName = ''
      this.searchItem.order.orderNum = null
    },
    sendSms () {
      if (this.tableSelectKey === '4') {
        this.spinning = true
        let key = this.tableSelectKey
        this.$put('comSms/sendSms', {
          applyDateStr: this.searchApplyDate,
          areaType: this.user.areaType.value,
          sendType: 21,
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
        this.$refs.ybChsVerifyStayed.searchPage()
      } else if (key === '2') {
        this.$refs.ybChsSendStayed.searchPage()
      } else if (key === '3') {
        this.$refs.ybChsSendEnd.searchPage()
      } else if (key === '4') {
        this.$refs.ybChsVerifySms.searchPage()
      } else {
        // console.log('ok')
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
