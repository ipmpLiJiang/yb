
<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <template>
    <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
      <div>
        <a-row justify="center" type="flex">
          <a-col :span=5>
            <a-form-item
              :label="applyDateText"
              v-bind="formItemLayout1"
            >
              <a-month-picker
                @change="monthChange"
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=4>
            <a-form-item
              label="至"
              v-bind="formItemLayout"
            >
              <a-month-picker
                placeholder="请选择复议年月"
                @change="monthToChange"
                :default-value="defaultApplyDate"
                :format="monthFormat"
              />
            </a-form-item>
          </a-col>
          <a-col :span=5>
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
                    v-for="d in selectDataTypeDataSource"
                    :key="d.value"
                  >
                    {{ d.text }}
                  </a-select-option>
                </a-select>
              </a-form-item>
          </a-col>
          <a-col :span=5>
            <a-input-search placeholder="请输入关键字" v-model="searchText" style="width: 200px" enter-button @search="searchTable" />
          </a-col>
          <a-col :span=3 >
              <template>
                <a-upload
                  name="file"
                  accept=".xlsx,.xls"
                  :fileList="fileList"
                  :beforeUpload="beforeUpload"
                >
                  <a-button type="primary">
                    <a-icon type="upload" /> 上传扣款落实数据 </a-button>
                </a-upload>
              </template>
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
            tab="待扣款"
          >
            <ybAppealResultDeductImplement-stayed
              ref="ybAppealResultDeductImplementStayed"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="defaultApplyDate"
              :searchDataType="searchDataType"
              :searchText="searchText"
              @edit="edit"
            >
            </ybAppealResultDeductImplement-stayed>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="已扣款"
          >
            <ybAppealResultDeductImplement-complete
              ref="ybAppealResultDeductImplementComplete"
              :applyDateStr="searchApplyDate"
              :applyDateToStr="searchToApplyDate"
              :defaultFormatDate="defaultApplyDate"
              :searchText="searchText"
              :searchDataType="searchDataType"
            >
            </ybAppealResultDeductImplement-complete>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <template>
      <div>
        <a-modal :width="800" :maskClosable="false" :footer="null" v-model="editVisible" title="录入扣款方案" @ok="handleOk">
          <ybAppealResultDeductImplement-edit
          ref="ybAppealResultDeductImplementEdit"
          @close="handleOk"
          @success="handleSuccess"
          >
          </ybAppealResultDeductImplement-edit>
        </a-modal>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbAppealResultDeductImplementComplete from './YbAppealResultDeductImplementComplete'
import YbAppealResultDeductImplementStayed from './YbAppealResultDeductImplementStayed'
import YbAppealResultDeductImplementEdit from './YbAppealResultDeductImplementEdit'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 }
}
const formItemLayout1 = {
  labelCol: { span: 10 },
  wrapperCol: { span: 13, offset: 1 }
}
export default {
  name: 'YbAppealResultDeductImplementView',
  components: {
    YbAppealResultDeductImplementStayed, YbAppealResultDeductImplementComplete, YbAppealResultDeductImplementEdit},
  data () {
    return {
      formItemLayout,
      formItemLayout1,
      loading: false,
      monthFormat: 'YYYY-MM',
      searchText: '',
      searchDataType: 0,
      editVisible: false,
      searchApplyDate: this.formatDate(),
      searchToApplyDate: this.formatDate(),
      defaultApplyDate: this.formatDate(),
      selectDataTypeDataSource: [
        {text: '全部', value: 2},
        {text: '明细扣款', value: 0},
        {text: '主单扣款', value: 1}
      ],
      applyDateText: '复议年月',
      fileList: [],
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
    moment,
    formatDate () {
      let datemonth = moment().format('YYYY-MM')
      return datemonth
    },
    monthChange (date, dateString) {
      this.searchApplyDate = dateString
    },
    monthToChange (date, dateString) {
      this.searchToApplyDate = dateString
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
      this.$upload('ybAppealResultDeductimplement/importDeductimplement', formData).then((r) => {
        // r.data.data.message
        if (r.data.data.success === 1) {
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
    edit (record) {
      this.showModal(record)
    },
    showModal (record) {
      this.editVisible = true
      setTimeout(() => {
        this.$refs.ybAppealResultDeductImplementEdit.setFormValues(record)
      }, 200)
    },
    handleOk (e) {
      this.editVisible = false
    },
    handleSuccess () {
      this.editVisible = false
      this.$refs.ybAppealResultDeductImplementStayed.search()
    },
    handleDataTypeChange (value) {
      this.searchDataType = value
      setTimeout(() => {
        this.searchTable()
      }, 200)
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.applyDateText = '复议年月'
        this.$refs.ybAppealResultDeductImplementStayed.search()
      } else if (key === '2') {
        this.applyDateText = '绩效落实年月'
        this.$refs.ybAppealResultDeductImplementComplete.search()
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
