<template>
  <a-card
    :bordered="false"
    class="card-area"
  >
    <div style="text-align:center;">
      <a-spin tip="Loading..." :spinning="spinning" :delay="delayTime">
          <div>
        <a-row justify='center'>
          <a-col :span=15>
            <a-form-item
              v-bind="{
                labelCol: { span: 8 },
                wrapperCol: { span: 4, offset: 1 }
              }"
              label="复议年月"
            >
            <a-input
                placeholder="复议年月"
                disabled
                v-model="ybReconsiderApply.applyDateStr"
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
          <a-col :span=2 v-show="tableSelectKey == '3' ? false:showBtn">
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
          <a-col :span=2 v-show="tableSelectKey == '3' ? false:showDelBtn">
            <a-popconfirm
              title="确定删除明细？"
              @confirm="deleteData"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: .8rem">删除明细</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=5 v-show="tableSelectKey == 3 ? true:false">
            <a-select :value="searchOutpfees" style="width: 100px" @change="handleOutpfeesChange">
              <a-select-option
              v-for="d in selectOutpfeesDataSource"
              :key="d.value"
              >
              {{ d.text }}
              </a-select-option>
            </a-select>
            <a-popconfirm
              title="确定获取His数据？"
              @confirm="addHis"
              okText="确定"
              style="margin-left: 8px"
              cancelText="取消"
            >
              <a-button type="primary">获取His数据</a-button>
            </a-popconfirm>
          </a-col>
          <a-col :span=2 v-show="tableSelectKey == '3' ? false:showBtn">
            <a-button
              type="primary"
              style="margin-left: 8px"
              @click="downloadFile"
            >模板</a-button>
          </a-col>
          <a-col :span=2 >
            <a-popconfirm
              title="确定缓存明细？"
              @confirm="updateCache"
              okText="确定"
              cancelText="取消"
            >
              <a-button type="primary" style="margin-right: .8rem">缓存明细</a-button>
            </a-popconfirm>
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
            :pid="ybReconsiderApply.id"
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
            :pid="ybReconsiderApply.id"
            :typeno="typeno"
            >
            </ybReconsiderApply-main>
          </a-tab-pane>
          <a-tab-pane
            key="3"
            :forceRender="true"
            tab="His数据"
          >
            <ybReconsiderApply-task
              ref="ybReconsiderApplyTask"
              :applyDateStr="ybReconsiderApply.applyDateStr"
              :typeno="typeno"
              :areaType="ybReconsiderApply.areaType"
              :isOutpfees="searchOutpfees"
            >
            </ybReconsiderApply-task>
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
import YbReconsiderApplyTask from './YbReconsiderApplyTask'
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 14, offset: 1 }
}
export default {
  name: 'YbReconsiderApplyUpload',
  components: {YbReconsiderApplyData, YbReconsiderApplyMain, YbReconsiderApplyTask},
  props: {
  },
  data () {
    return {
      formItemLayout,
      ybReconsiderApply: {},
      monthFormat: 'YYYY-MM',
      fileList: [],
      showBtn: false,
      showDelBtn: false,
      typeno: 1,
      tableSelectKey: '1',
      spinning: false,
      searchOutpfees: 2,
      selectOutpfeesDataSource: [{text: '住院', value: 2},
        {text: '门诊', value: 1}],
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
      this.$download('ybReconsiderApplyData/downFile', {
      }, '复议审核模板.xlsx')
    },
    startUp () {
      this.$put('ybReconsiderApply/startJob', {
        applyDateStr: this.ybReconsiderApply.applyDateStr,
        areaType: this.ybReconsiderApply.areaType
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
    deleteData () {
      if (this.ybReconsiderApply.state === 2 || this.ybReconsiderApply.state === 4) {
        let params = { pid: this.ybReconsiderApply.id, typeno: this.typeno, state: this.ybReconsiderApply.state }
        this.$delete('ybReconsiderApplyData/deleteData', params).then((r) => {
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
    updateCache () {
      this.$put('ybReconsiderApplyData/updateCache', {
        applyDateStr: this.ybReconsiderApply.applyDateStr,
        areaType: this.ybReconsiderApply.areaType
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.$message.success('缓存成功')
        } else {
          this.$message.warning(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('缓存失败')
      })
    },
    handleOutpfeesChange (value) {
      this.searchOutpfees = value
      setTimeout(() => {
        this.callback('3')
      }, 200)
    },
    onClose () {
      this.ybReconsiderApply = {}
      this.showBtn = false
      this.showDelBtn = false
      this.typeno = 1
      this.tableSelectKey = '1'
      this.fileList = []
      this.$emit('cancel')
    },
    addHis () {
      let key = '3'
      if (this.tableSelectKey === key) {
        this.spinning = true
        this.$put('ybReconsiderApplyData/getHis', {
          applyDateStr: this.ybReconsiderApply.applyDateStr,
          areaType: this.ybReconsiderApply.areaType,
          typeno: this.typeno,
          isOutpfees: this.searchOutpfees
        }).then((r) => {
          if (r.data.data.success === 1) {
            this.$message.success('His数据获取成功.')
            if (this.tableSelectKey === key) {
              this.callback(key)
            }
            this.spinning = false
          } else {
            this.$message.warning(r.data.data.message)
            if (this.tableSelectKey === key) {
              this.callback(key)
            }
            this.spinning = false
          }
        }).catch(() => {
          this.$message.error('His数据获取失败.')
          this.spinning = false
        })
      }
    },
    callback (key) {
      this.tableSelectKey = key
      if (key === '1') {
        this.$refs.ybReconsiderApplyData.searchPage()
      } else if (key === '2') {
        this.$refs.ybReconsiderApplyMain.searchPage()
      } else if (key === '3') {
        this.$refs.ybReconsiderApplyTask.searchPage()
      } else {
        console.log('ok')
      }
    },
    setFormValues ({ ...ybReconsiderApply }, typeno) {
      this.tableSelectKey = '1'
      this.ybReconsiderApply = ybReconsiderApply
      let pid = ybReconsiderApply.id
      this.typeno = typeno
      if (pid !== null && pid !== 0 && pid !== undefined) {
        if (this.typeno !== null && this.typeno !== 0 && this.typeno !== undefined) {
          setTimeout(() => {
            this.$refs.ybReconsiderApplyData.searchPage()
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
        if (this.ybReconsiderApply.state === 2) {
          this.showDelBtn = true
        } else {
          this.showDelBtn = false
        }
      } else {
        this.uploadFileName = ybReconsiderApply.uploadFileNameTwo
        if (ybReconsiderApply.state === 2 || ybReconsiderApply.state === 3) {
          this.showBtn = true
        } else {
          this.showBtn = false
        }
        if (this.ybReconsiderApply.state === 4) {
          this.showDelBtn = true
        } else {
          this.showDelBtn = false
        }
      }
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
      formData.append('pid', this.ybReconsiderApply.id)
      formData.append('typeno', this.typeno)
      formData.append('areaType', this.ybReconsiderApply.areaType)
      this.$upload('ybReconsiderApplyData/importReconsiderApplyData', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.uploadFileName = r.data.data.fileName
          this.searchTable()
          this.showBtn = false
          if (this.ybReconsiderApply.state === 1) {
            this.ybReconsiderApply.state = 2
          } else if (this.ybReconsiderApply.state === 3) {
            this.ybReconsiderApply.state = 4
          }
          if (this.ybReconsiderApply.state === 2 || this.ybReconsiderApply.state === 4) {
            this.showDelBtn = true
          }
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
