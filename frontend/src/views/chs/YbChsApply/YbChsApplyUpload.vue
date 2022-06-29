<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div style="text-align:center;">
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
          <div>
        <a-row justify='center'>
          <a-col :span=13>
            <a-form-item
              v-bind="formItemLayout"
              label="复议年月"
            >
            <a-input
                placeholder="复议年月"
                disabled
                width="110px"
                v-model="ybChsApply.applyDateStr"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row justify='center'>
          <a-col :span=13>
            <a-form-item
              v-bind="formItemLayout"
              label="上传文件名称"
            >
              <a-input
                placeholder="上传文件名称"
                disabled
                v-model="uploadFileName"
              />
            </a-form-item>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey == '1' ? showBtn : false">
            <template>
              <a-upload
                name="file"
                accept=".xlsx,.xls"
                :fileList="fileList"
                :remove="removeUpload"
                :beforeUpload="beforeUpload"
              >
                <a-button type="primary">
                  <a-icon type="upload" /> 上传 </a-button>
              </a-upload>
            </template>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey == '1' ? showDelBtn:false">
            <a-popconfirm
              title="确定删除明细？"
              @confirm="deleteData"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: .8rem">删除明细</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=7 v-show="tableSelectKey == 3 ? true:false">
            <a-select :value="searchOutpfees" style="width: 100px" @change="handleOutpfeesChange">
              <a-select-option
              v-for="d in selectOutpfeesDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            <a-popconfirm
              title="确定获取HIS数据？"
              @confirm="addChsJk"
              okText="确定"
              style="margin-left: 8px"
              cancelText="取消"
            >
              <a-button type="primary">获取HIS数据</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定删除HIS数据？"
              @confirm="delChsJk"
              okText="确定"
              style="margin-left: 8px"
              cancelText="取消"
            >
              <a-button type="primary">删除HIS数据</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey == '1' ? showBtn : false">
            <a-button
              type="primary"
              style="margin-left: 8px"
              @click="downloadFile"
            >模板</a-button>
          </a-col>
          <a-col :span=2>
            <a-button
              style="margin-left: 8px"
              @click="onClose"
            >关闭</a-button>
          </a-col>
        </a-row>
      </div>
        </a-spin>
    </div>
    <!--表格-->
    <template>
      <div id="tab">
        <a-tabs
          type="card"
          :activeKey="tableSelectKey"
          @change="callback"
        >
          <a-tab-pane
            key="1"
            tab="明细数据"
          >
            <ybChsApply-data
            ref="ybChsApplyData"
            :pid="ybChsApply.id"
            >
            </ybChsApply-data>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="HIS数据"
          >
            <ybChs-jk
              ref="ybChsJk"
              :applyDateStr="ybChsApply.applyDateStr"
              :areaType="ybChsApply.areaType"
            >
            </ybChs-jk>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="获取HIS"
          >
            <ybChsApply-task
              ref="ybChsApplyTask"
              :applyDateStr="ybChsApply.applyDateStr"
              :areaType="ybChsApply.areaType"
            >
            </ybChsApply-task>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbChsApplyData from './YbChsApplyData'
import YbChsApplyTask from './YbChsApplyTask'
import YbChsJk from './YbChsJk'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbChsApplyUpload',
  components: {YbChsApplyData, YbChsJk, YbChsApplyTask},
  props: {
  },
  data () {
    return {
      formItemLayout,
      ybChsApply: {},
      monthFormat: 'YYYY-MM',
      fileList: [],
      tableSelectKey: '1',
      selectOutpfeesDataSource: [{text: '住院', value: 2},
        {text: '门诊', value: 1}],
      searchOutpfees: 2,
      showBtn: false,
      showDelBtn: false,
      spinning: false,
      delayTime: 500,
      uploadFileName: '',
      user: this.$store.state.account.user,
      searchApplyDate: this.formatDate()
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
    downloadFile () {
      this.$download('ybChsApplyData/downFile', {
      }, '医保明细审核模板.xlsx')
    },
    handleOutpfeesChange (value) {
      this.searchOutpfees = value
      setTimeout(() => {
        this.callback('3')
      }, 200)
    },
    deleteData () {
      if (this.ybChsApply.state === 2) {
        let params = { pid: this.ybChsApply.id, state: this.ybChsApply.state }
        this.$delete('ybChsApplyData/deleteData', params).then((r) => {
          if (r.data.data.success === 1) {
            this.$message.success('删除明细成功')
            this.uploadFileName = ''
            this.searchTable()
            this.showBtn = true
            this.showDelBtn = false
          } else {
            this.$message.error('删除明细失败或状态已更新无法删除？')
          }
        })
      } else {
        this.$message.warning('当前状态无法删除.')
      }
    },
    onClose () {
      this.ybChsApply = {}
      this.showBtn = false
      this.showDelBtn = false
      this.tableSelectKey = '1'
      this.fileList = []
      this.$emit('cancel')
    },
    addChsJk () {
      let key = '3'
      if (this.tableSelectKey === key) {
        this.spinning = true
        this.$post('ybChsApplyData/getJk', {
          applyDateStr: this.ybChsApply.applyDateStr,
          areaType: this.ybChsApply.areaType,
          isOutpfees: this.searchOutpfees
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.showDelBtn = false
            this.$message.success('HIS数据获取成功.')
          } else {
            this.$message.warning(r.data.data.message)
          }
          if (this.tableSelectKey === key) {
            this.callback(key)
          }
          this.spinning = false
        }).catch(() => {
          this.$message.error('HIS数据获取失败.')
          this.spinning = false
        })
      }
    },
    delChsJk () {
      let key = '3'
      if (this.tableSelectKey === key) {
        this.spinning = true
        this.$post('ybChsApplyData/delJk', {
          applyDateStr: this.ybChsApply.applyDateStr,
          areaType: this.ybChsApply.areaType
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.showDelBtn = true
            this.$message.success('删除HIS数据成功.')
            if (this.tableSelectKey === key) {
              this.callback(key)
            }
            this.spinning = false
          } else {
            this.$message.warning(r.data.data.message)
            this.spinning = false
          }
        }).catch(() => {
          this.$message.error('删除HIS数据失败.')
          this.spinning = false
        })
      }
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybChsApplyData.searchPage()
      } else if (key === '2') {
        this.$refs.ybChsJk.searchPage()
      } else if (key === '3') {
        this.$refs.ybChsApplyTask.searchPage()
      } else {
        console.log('ok')
      }
    },
    setFormValues ({ ...ybChsApply }) {
      this.tableSelectKey = '1'
      this.ybChsApply = ybChsApply
      let pid = ybChsApply.id
      if (pid !== null && pid !== 0 && pid !== undefined) {
        setTimeout(() => {
          this.$refs.ybChsApplyData.searchPage()
        }, 200)
      }
      if (ybChsApply.state === 1) {
        this.showBtn = true
      } else {
        this.showBtn = false
      }
      if (this.ybChsApply.state === 2) {
        this.showDelBtn = true
      } else {
        this.showDelBtn = false
      }
      this.uploadFileName = ybChsApply.uploadFileName
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
      formData.append('pid', this.ybChsApply.id)
      this.$upload('ybChsApplyData/importChsApplyData', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.ybChsApply.state = 2
          this.uploadFileName = r.data.data.fileName
          this.showBtn = false
          this.searchTable()
          this.showDelBtn = true
          this.spinning = false
          this.$message.success('Excel导入成功.')
        } else {
          this.spinning = false
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.fileList = []
        this.$message.error('Excel导入失败.')
        this.spinning = false
      })
    },
    searchTable () {
      this.callback(this.tableSelectKey)
    },
    removeUpload (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
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
}
</style>
