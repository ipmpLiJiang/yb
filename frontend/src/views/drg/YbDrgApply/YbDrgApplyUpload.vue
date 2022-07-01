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
                v-model="ybDrgApply.applyDateStr"
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
          <a-col :span=2 v-show="tableSelectKey == '2' ? false:showBtn">
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
          <a-col :span=2 v-show="tableSelectKey == '2' ? false:showDelBtn">
            <a-popconfirm
              title="确定删除明细？"
              @confirm="deleteData"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: .8rem">删除明细</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=5 v-show="tableSelectKey == 2 ? true:false">
            <a-popconfirm
              title="确定获取DRG数据？"
              @confirm="addDrgJk"
              okText="确定"
              style="margin-left: 8px"
              cancelText="取消"
            >
              <a-button type="primary">获取DRG数据</a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定删除DRG数据？"
              @confirm="delDrgJk"
              okText="确定"
              style="margin-left: 8px"
              cancelText="取消"
            >
              <a-button type="primary">删除DRG数据</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey == '2' ? false:showBtn">
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
            <ybDrgApply-data
            ref="ybDrgApplyData"
            :pid="ybDrgApply.id"
            >
            </ybDrgApply-data>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="DRG数据"
          >
            <ybDrg-jk
              ref="ybDrgJk"
              :applyDateStr="ybDrgApply.applyDateStr"
              :areaType="ybDrgApply.areaType"
            >
            </ybDrg-jk>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbDrgApplyData from './YbDrgApplyData'
import YbDrgJk from './YbDrgJk'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbDrgApplyUpload',
  components: {YbDrgApplyData, YbDrgJk},
  props: {
  },
  data () {
    return {
      formItemLayout,
      ybDrgApply: {},
      monthFormat: 'YYYY-MM',
      fileList: [],
      tableSelectKey: '1',
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
      this.$download('ybDrgApplyData/downFile', {
      }, 'DRG审核模板.xlsx')
    },
    deleteData () {
      if (this.ybDrgApply.state === 2) {
        let params = { pid: this.ybDrgApply.id, state: this.ybDrgApply.state }
        this.$delete('ybDrgApplyData/deleteData', params).then((r) => {
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
      this.ybDrgApply = {}
      this.showBtn = false
      this.showDelBtn = false
      this.tableSelectKey = '1'
      this.fileList = []
      this.$emit('cancel')
    },
    addDrgJk () {
      let key = '2'
      if (this.tableSelectKey === key) {
        this.spinning = true
        this.$post('ybDrgApplyData/getJk', {
          applyDateStr: this.ybDrgApply.applyDateStr,
          areaType: this.ybDrgApply.areaType
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.showDelBtn = false
            this.$message.success('DRG数据获取成功.')
            if (this.tableSelectKey === key) {
              this.callback(key)
            }
            this.spinning = false
          } else {
            this.$message.warning(r.data.data.message)
            this.spinning = false
          }
        }).catch(() => {
          this.$message.error('DRG数据获取失败.')
          this.spinning = false
        })
      }
    },
    delDrgJk () {
      let key = '2'
      if (this.tableSelectKey === key) {
        this.spinning = true
        this.$post('ybDrgApplyData/delJk', {
          applyDateStr: this.ybDrgApply.applyDateStr,
          areaType: this.ybDrgApply.areaType
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.showDelBtn = true
            this.$message.success('删除DRG数据成功.')
            if (this.tableSelectKey === key) {
              this.callback(key)
            }
            this.spinning = false
          } else {
            this.$message.warning(r.data.data.message)
            this.spinning = false
          }
        }).catch(() => {
          this.$message.error('删除DRG数据失败.')
          this.spinning = false
        })
      }
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybDrgApplyData.searchPage()
      } else if (key === '2') {
        this.$refs.ybDrgJk.searchPage()
      } else {
        console.log('ok')
      }
    },
    setFormValues ({ ...ybDrgApply }) {
      this.tableSelectKey = '1'
      this.ybDrgApply = ybDrgApply
      let pid = ybDrgApply.id
      if (pid !== null && pid !== 0 && pid !== undefined) {
        setTimeout(() => {
          this.$refs.ybDrgApplyData.searchPage()
        }, 200)
      }
      if (ybDrgApply.state === 1) {
        this.showBtn = true
      } else {
        this.showBtn = false
      }
      if (this.ybDrgApply.state === 2) {
        this.showDelBtn = true
      } else {
        this.showDelBtn = false
      }
      this.uploadFileName = ybDrgApply.uploadFileName
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
      formData.append('pid', this.ybDrgApply.id)
      this.$upload('ybDrgApplyData/importDrgApplyData', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.ybDrgApply.state = 2
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
