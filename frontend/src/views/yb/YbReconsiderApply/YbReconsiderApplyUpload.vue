<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div style="text-align:center;">
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
          <div>
        <a-row justify='center'>
          <a-col :span=17>
            <a-form-item
              v-bind="{
                labelCol: { span: 8 },
                wrapperCol: { span: 4, offset: 1 }
              }"
              label="复议年月"
            >
            <a-input
                placeholder="复议年月"
                v-model="applyDateStr"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row justify='center'>
          <a-col :span=17>
            <a-form-item
              v-bind="formItemLayout"
              label="上传文件名称"
            >
              <a-input
                placeholder="上传文件名称"
                v-model="uploadFileName"
              />
            </a-form-item>
          </a-col>
          <a-col :span=2>
            <template>
              <a-upload
                name="file"
                accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                :fileList="fileList"
                :remove="removeUpload"
                :beforeUpload="beforeUpload"
              >
                <a-button type="primary" v-show="showBtn">
                  <a-icon type="upload" /> 上传 </a-button>
              </a-upload>
            </template>
          </a-col>
          <a-col :span=2>
            <a-button
              type="primary"
                v-show="showBtn"
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
            tab="明细扣款"
          >
            <ybReconsiderApply-data
            ref="ybReconsiderApplyData"
            :pid="pid"
            :typeno="typeno"
            >
            </ybReconsiderApply-data>
          </a-tab-pane>
          <a-tab-pane
            key="2"
            :forceRender="true"
            tab="主单扣款"
          >
            <ybReconsiderApply-main
            ref="ybReconsiderApplyMain"
            :pid="pid"
            :typeno="typeno"
            >
            </ybReconsiderApply-main>
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
  </a-card>
</template>

<script>
import moment from 'moment'
import YbReconsiderApplyData from './YbReconsiderApplyData'
import YbReconsiderApplyMain from './YbReconsiderApplyMain'

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbReconsiderApplyUpload',
  components: {YbReconsiderApplyData, YbReconsiderApplyMain},
  props: {
  },
  data () {
    return {
      formItemLayout,
      ybReconsiderApplyUpload: {},
      monthFormat: 'YYYY-MM',
      fileList: [],
      pid: '0',
      showBtn: false,
      typeno: 1,
      tableSelectKey: '1',
      spinning: false,
      delayTime: 500,
      applyDateStr: '',
      uploadFileName: '',
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
      let datemonth = moment().format('YYYY-MM')
      return datemonth
    },
    downloadFile () {
      this.$download('ybReconsiderApplyData/downFile', {
      }, '复议审核模板.xlsx')
    },
    onClose () {
      this.ybReconsiderApplyUpload = {}
      this.showBtn = false
      this.pid = '0'
      this.typeno = 1
      this.tableSelectKey = '1'
      this.fileList = []
      this.$emit('cancel')
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybReconsiderApplyData.search()
      } else if (key === '2') {
        this.$refs.ybReconsiderApplyMain.search()
      } else {
        console.log('ok')
      }
    },
    setFormValues ({ ...ybReconsiderApply }, typeno) {
      this.applyDateStr = ybReconsiderApply.applyDateStr
      this.pid = ybReconsiderApply.id
      this.typeno = typeno
      if (this.pid !== null && this.pid !== 0 && this.pid !== undefined) {
        if (this.typeno !== null && this.typeno !== 0 && this.typeno !== undefined) {
          setTimeout(() => {
            this.$refs.ybReconsiderApplyData.search()
          }, 200)
        }
      }
      if (typeno === 1) {
        this.uploadFileName = ybReconsiderApply.uploadFileNameOne
        if (ybReconsiderApply.state === 1) {
          this.showBtn = true
        } else {
          this.showBtn = false
        }
      } else {
        this.uploadFileName = ybReconsiderApply.uploadFileNameTwo
        if (ybReconsiderApply.state === 2) {
          this.showBtn = true
        } else {
          this.showBtn = false
        }
      }
    },
    beforeUpload (file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      if (!(isExcel)) {
        this.$error({
          title: '只能上传.xlsx格式的Excel文档~'
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
      formData.append('pid', this.pid)
      formData.append('typeno', this.typeno)
      this.$upload('ybReconsiderApplyData/importReconsiderApplyData', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.uploadFileName = r.data.data.fileName
          this.searchTable()
          this.showBtn = false
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
