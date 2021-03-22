<template>
  <a-drawer
    title="编辑"
    :maskClosable="false"
    width=75%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="editVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-form :form="form">
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=20>
          <a-form-item
            v-bind="formItemLayout"
            label="标题"
          >
            <a-input
              placeholder="请输入标题"
              v-decorator="['ntTitle', {rules: [{ required: true, message: '标题不能为空' }] }]"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=20>
          <a-form-item
            v-bind="formItemLayout"
            label="内容简介"
          >
            <a-textarea
            placeholder="请输入内容简介"
            v-decorator="['ntExplain', {rules: [{ required: true, message: '内容简介不能为空' }] }]"
            :rows="3"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=20>
          <a-form-item
            v-bind="formItemLayout"
            label="内容详情"
          >
            <a-textarea v-show="false"
            placeholder="请输入内容详情"
            v-decorator="['ntDetail', {rules: [{ required: true, message: '内容详情不能为空' }] }]"
            :rows="1"
            />
            <editor v-model="detailContent" :isClear="isClear" @change="editorChange"></editor>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=22>
          <a-form-item
            v-bind="formItemLayout"
            label="发送人员"
          >
          <a-radio-group v-decorator="['sendType']" :disabled="ybNotice.state!==0?true:false"  size="large" @change="handleSendTypeChange">
            <a-radio style="display:block;height:20px;lineHeight:20px" value="1">
              所有人
            </a-radio>
            <a-radio style="display:block;height:120px;lineHeight:120px" value="2">
              医管人员&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <a-select
                mode="multiple"
                :default-value="defaultAdminType"
                :value="defaultAdminType"
                :disabled="adminTypeDisabled"
                style="width: 100%"
                placeholder="请输入关键词"
                @change="handleAdminTypeChange"
              >
                <a-select-option
                  v-for="d in selectAdminTypeDataSource"
                  :key="d.value"
                  >
                  {{ d.text }}
                  </a-select-option>
              </a-select>
            </a-radio>
             <a-radio style="display:block;height:20px;lineHeight:20px" value="3">
              指定人员&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <a-select
                  show-search
                  :value="selectPersonValue"
                  placeholder="请输入关键词"
                  :disabled="personDisabled"
                  style="width: 100%"
                  :default-active-first-option="false"
                  :filter-option="false"
                  :not-found-content="null"
                  @search="handlePersonSearch"
                  @change="e => handlePersonChange(e)"
              >
                  <a-icon
                  slot="suffixIcon"
                  type="search"
                  ></a-icon>
                  <a-select-option
                  v-for="d in selectPersonDataSource"
                  :key="d.value"
                  >
                  {{ d.text }}
                  </a-select-option>
              </a-select>
              <a-button
                @click="addPerson"
                :disabled="personDisabled"
                type="primary"
              >添加人员</a-button>
            </a-radio>
          </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row
        justify="start"
        type="flex"
      >
        <a-col :span=4>
          &nbsp;
        </a-col>
        <a-col :span=15>
          <ybNotice-person
          ref="ybNoticePerson"
          @delPerson = "delPerson"
          :pid="ybNotice.id"
        >
        </ybNotice-person>
        </a-col>
      </a-row>
      <br>
      <a-row
        justify="center"
        type="flex"
      >
        <a-col :span=3>
          <a-button
            type="primary"
            @click="handleSubmit(0)"
            :disabled="saveDisabled"
            style="margin-right: .8rem">
          保存</a-button>
        </a-col>
        <a-col :span=3>
          <a-popconfirm
            title="确定提交？"
            :disabled="fileDisabled"
            @confirm="handleSubmit(1)"
            okText="确定"
            cancelText="取消"
          >
            <a-button type="primary" :disabled="fileDisabled" style="margin-right: .8rem">提交</a-button>
          </a-popconfirm>
        </a-col>
        <a-col :span=3>
          <a-popconfirm
            title="确定发布？"
            :disabled="releaseDisabled"
            @confirm="onRelease"
            okText="确定"
            cancelText="取消"
          >
            <a-button type="primary" :disabled="releaseDisabled" style="margin-right: .8rem">发布</a-button>
          </a-popconfirm>
        </a-col>
        <a-col :span=3>
          <a-popconfirm
            title="确定放弃编辑？"
            @confirm="onClose"
            okText="确定"
            cancelText="取消"
          >
            <a-button style="margin-right: .8rem">取消</a-button>
          </a-popconfirm>
        </a-col>
      </a-row>
    </a-form>
    <br>
    <!--表格-->
    <template>
      <a-row
          justify="start"
          type="flex"
        >
        <a-col :span=4>
          <a-upload
            name="file"
            accept=".xlsx,.xls"
            :disabled="fileDisabled"
            :fileList="fileList"
            :beforeUpload="beforeUpload"
          >
            <a-button type="primary" :disabled="fileDisabled">
              <a-icon type="upload" /> 上传附件 </a-button>
          </a-upload>
        </a-col>
        <a-col :span=15>
          <ybNotice-file
            ref="ybNoticeFile"
            @delFile="delFile"
            :pid="ybNotice.id"
          >
          </ybNotice-file>
        </a-col>
      </a-row>
    </template>
  </a-drawer>
</template>
<script>
import YbNoticeFile from './YbNoticeFile'
import YbNoticePerson from './YbNoticePerson'
import Editor from '../ybFunModule/EditorItem'
const formItemLayout = {
  labelCol: { span: 4 },
  wrapperCol: { span: 19 }
}
export default {
  name: 'YbNoticeEdit',
  props: {
    editVisiable: {
      default: false
    }
  },
  components: { YbNoticeFile, YbNoticePerson, Editor },
  data () {
    return {
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      isUpdate: false,
      adminTypeDisabled: true,
      personDisabled: true,
      fileDisabled: true,
      releaseDisabled: true,
      saveDisabled: false,
      selectPersonDataSource: [], // 搜索事件
      selectPersonValue: undefined,
      selectPerson: {},
      defaultPerson: [],
      defaultAdminType: [],
      selectAdminTypeDataSource: [],
      defaultFile: [],
      fileList: [],
      spinning: false,
      detailContent: '',
      isClear: false,
      ybNotice: {}
    }
  },
  methods: {
    reset () {
      this.defaultSet()
      this.form.resetFields()
    },
    defaultSet () {
      this.loading = false
      this.ybNotice = {}
      this.selectPerson = {}
      this.defaultPerson = []
      this.defaultAdminType = []
      this.selectAdminTypeDataSource = []
      this.fileList = []
      this.defaultFile = []
      this.selectPersonDataSource = [] // 搜索事件
      this.selectPersonValue = undefined
      this.adminTypeDisabled = true
      this.personDisabled = true
      this.fileDisabled = true
      this.releaseDisabled = true
      this.saveDisabled = false
      this.spinning = false
    },
    onClose () {
      this.reset()
      if (this.isUpdate) {
        this.$emit('success')
      } else {
        this.$emit('close')
      }
    },
    editorChange (text) {
      this.form.getFieldDecorator('ntDetail')
      this.form.setFieldsValue({
        ntDetail: text
      })
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
      formData.append('id', this.ybNotice.id)
      formData.append('fileName', 'NoticeDoc')
      formData.append('refTab', 'ybNotice')

      this.$upload('comFile/uploadFile', formData).then((r) => {
        if (r.data.data.success === 1) {
          this.spinning = false
          let fileData = {
            id: r.data.data.uid,
            clientName: r.data.data.name
          }
          this.$refs.ybNoticeFile.add(fileData)
          this.defaultFile.push(fileData)
          this.$message.success('文件上传成功.')
        } else {
          this.spinning = false
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.fileList = []
        this.$message.error('文件上传失败.')
        this.spinning = false
      })
    },
    handleAdminTypeChange (value) {
      if (this.ybNotice.state !== 0) {
        this.$message.error('当前状态无法添加/去除医管人员.')
        return
      }
      this.defaultAdminType = value
      this.$message.warning('医管人员添加/去除成功后，请保存或提交数据.')
    },
    handlePersonSearch (keyword) {
      this.selectPersonDataSource = this.ajaxPerson(keyword)
    },
    handlePersonChange (value) {
      if (this.selectPersonDataSource.length > 0) {
        const textData = this.selectPersonDataSource.filter(item => value === item.value)[0]
        this.selectPerson = {
          personCode: textData.value,
          personName: textData.text
        }
        this.selectPersonValue = value
      } else {
        this.selectPersonValue = undefined
        this.selectPerson = {}
      }
    },
    addPerson () {
      if (this.ybNotice.state !== 0) {
        this.$message.error('当前状态无法添加人员.')
        return
      }
      if (this.selectPersonValue !== undefined && this.selectPersonValue !== '') {
        if (this.defaultPerson.length > 0) {
          let target = this.defaultPerson.filter(item => this.selectPersonValue === item.personCode)[0]
          if (target) {
            this.$message.error('人员已存在.')
            this.selectPersonValue = undefined
            this.selectPerson = {}
            return
          }
        }
        this.$refs.ybNoticePerson.add(this.selectPerson)
        let item = {
          id: '',
          pid: '',
          personCode: this.selectPerson.personCode,
          personName: this.selectPerson.personName
        }
        this.defaultPerson.push(item)
        this.selectPersonValue = undefined
        this.selectPerson = {}
      }
      this.$message.warning('人员添加成功后，请保存或提交数据.')
    },
    delPerson (personCode) {
      let target = this.defaultPerson.filter(item => personCode === item.personCode)[0]
      const index = this.defaultPerson.indexOf(target)
      const newData = this.defaultPerson.slice()
      newData.splice(index, 1)
      this.defaultPerson = newData
      this.$message.warning('人员删除成功后，请保存或提交数据.')
    },
    delFile (id) {
      let target = this.defaultFile.filter(item => id === item.id)[0]
      const index = this.defaultFile.indexOf(target)
      const newData = this.defaultFile.slice()
      newData.splice(index, 1)
      this.defaultFile = newData
    },
    ajaxPerson (keyword) {
      let dataSource = []
      let params = {comments: keyword}
      this.$get('ybPerson/findPersonList', {
        ...params
      }).then((r) => {
        r.data.data.forEach((item, i) => {
          dataSource.push({
            value: item.personCode,
            text: item.personCode + '-' + item.personName
          })
        })
      })
      return dataSource
    },
    handleSendTypeChange (e) {
      let value = e.target.value
      this.ybNotice.sendType = value
      if (value === '1') {
        if (this.defaultAdminType.length > 0) {
          this.defaultAdminType = []
        }
        if (this.defaultPerson.length > 0) {
          this.defaultPerson = []
          this.$refs.ybNoticePerson.searchPage(undefined, 0)
        }
        this.adminTypeDisabled = true
        this.personDisabled = true
      } else if (value === '2') {
        if (this.defaultPerson.length > 0) {
          this.defaultPerson = []
          this.$refs.ybNoticePerson.searchPage(undefined, 0)
        }
        this.adminTypeDisabled = false
        this.personDisabled = true
      } else {
        if (this.defaultAdminType.length > 0) {
          this.defaultAdminType = []
          this.$refs.ybNoticePerson.searchPage(undefined, 0)
        }
        this.adminTypeDisabled = true
        this.personDisabled = false
      }
    },
    findData (type) {
      let params = {
        pid: this.ybNotice.id
      }
      let ntData = []
      if (type === '2') {
        this.$get('ybNoticeData/findList', {
          ...params
        }).then((r) => {
          if (r.data.data.length > 0) {
            ntData = r.data.data
            for (var i in ntData) {
              this.defaultAdminType.push(ntData[i].cmId)
            }
          }
        })
        this.$refs.ybNoticePerson.searchPage(undefined, this.ybNotice.state)
      } else {
        this.$get('ybNoticeData/findList', {
          ...params
        }).then((r) => {
          if (r.data.data.length > 0) {
            ntData = r.data.data
            for (var i in ntData) {
              let item = {
                id: ntData[i].id,
                pid: ntData[i].pid,
                personCode: ntData[i].personCode,
                personName: ntData[i].personName
              }
              this.defaultPerson.push(item)
            }
            this.$refs.ybNoticePerson.searchPage(this.defaultPerson, this.ybNotice.state)
          } else {
            this.$refs.ybNoticePerson.searchPage(undefined, this.ybNotice.state)
          }
        })
      }
    },
    findFileData () {
      let params = {
        refTabId: this.ybNotice.id
      }
      this.$get('comFile/findFileList', {
        ...params
      }).then((r) => {
        if (r.data.data.length > 0) {
          let ntData = r.data.data
          for (var i in ntData) {
            let item = {
              id: ntData[i].uid,
              clientName: ntData[i].name
            }
            this.defaultFile.push(item)
          }
          this.$refs.ybNoticeFile.searchPage(this.defaultFile, this.ybNotice.state)
        } else {
          this.$refs.ybNoticeFile.searchPage(undefined, this.ybNotice.state)
        }
      })
    },
    setFormValues (obj, areaType, atDataSource) {
      this.defaultSet()
      this.selectAdminTypeDataSource = atDataSource
      this.form.getFieldDecorator('sendType')
      this.ybNotice.areaType = areaType
      if (obj === undefined || obj === null || obj === '') {
        this.ybNotice.state = 0
        this.form.setFieldsValue({
          sendType: '1'
        })
        setTimeout(() => {
          this.isClear = true
          this.detailContent = ''
          this.$refs.ybNoticePerson.searchPage(undefined, 0)
          this.$refs.ybNoticeFile.searchPage(undefined, 0)
        }, 200)
      } else {
        this.form.getFieldDecorator('ntTitle')
        this.form.getFieldDecorator('ntExplain')
        this.form.getFieldDecorator('ntDetail')
        this.form.setFieldsValue({
          ntTitle: obj.ntTitle,
          ntExplain: obj.ntExplain,
          ntDetail: obj.ntDetail,
          sendType: obj.sendType.toString()
        })
        this.ybNotice.ntTitle = obj.ntTitle
        this.ybNotice.ntExplain = obj.ntExplain
        this.ybNotice.ntDetail = obj.ntDetail
        this.ybNotice.id = obj.id
        this.ybNotice.sendType = obj.sendType
        this.ybNotice.state = obj.state
        setTimeout(() => {
          this.isClear = true
          this.detailContent = obj.ntDetail
          let sendType = obj.sendType.toString()
          if (sendType === '2') {
            this.adminTypeDisabled = false
            this.personDisabled = true
            this.findData(sendType)
          }
          if (sendType === '3') {
            this.adminTypeDisabled = true
            this.personDisabled = false
            this.findData(sendType)
          }
          this.findFileData()
        }, 200)
        if (obj.state === 0) {
          this.fileDisabled = false
        } else if (obj.state === 1) {
          this.fileDisabled = true
          this.releaseDisabled = false
          this.saveDisabled = true
        } else {
          this.releaseDisabled = true
          this.saveDisabled = true
          this.fileDisabled = true
        }
      }
    },
    subCheck () {
      let isCheck = false
      let ms = ''
      if (this.ybNotice.sendType === '1') {
        isCheck = true
      } else if (this.ybNotice.sendType === '2') {
        if (this.defaultAdminType.length === 0) {
          ms = '医管人员未选择，不能为空.'
        } else {
          isCheck = true
        }
      } else {
        if (this.defaultPerson.length === 0) {
          ms = '指定人员未选择，不能为空.'
        } else {
          isCheck = true
        }
      }
      if (!isCheck) {
        this.$message.warning(ms)
      }
      return isCheck
    },
    subChild () {
      if (this.ybNotice.sendType === '2') {
        for (var i in this.defaultAdminType) {
          const data = this.selectAdminTypeDataSource.filter(item => this.defaultAdminType[i] === item.value)[0]
          let item = {
            id: '',
            pid: '',
            cmId: data.value,
            cmName: data.text,
            ndType: 1
          }
          this.ybNotice.child.push(item)
        }
      }
      if (this.ybNotice.sendType === '3') {
        for (var j in this.defaultPerson) {
          let item = {
            id: '',
            pid: '',
            personCode: this.defaultPerson[j].personCode,
            personName: this.defaultPerson[j].personName,
            ndType: 2
          }
          this.ybNotice.child.push(item)
        }
      }
    },
    onRelease () {
      this.isUpdate = false
      let notice = {
        id: this.ybNotice.id,
        sendType: this.ybNotice.sendType,
        ntTitle: this.ybNotice.ntTitle,
        state: 2
      }
      this.$put('ybNotice/updateReleaseNotice', {
        ...notice
      }).then((r) => {
        if (r.data.data.success === 1) {
          this.isUpdate = true
          this.releaseDisabled = true
          this.saveDisabled = true
          this.fileDisabled = true
          this.$message.success('发布成功.')
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.loading = false
        this.$message.success('发布失败.')
      })
    },
    handleSubmit (state) {
      this.isUpdate = false
      this.form.validateFields((err, values) => {
        if (!err) {
          this.setFields()
          this.ybNotice.child = []
          let isCheck = this.subCheck()
          if (isCheck) {
            this.subChild()
            this.ybNotice.state = state
            let jsonString = JSON.stringify(this.ybNotice)
            if (this.ybNotice.id === undefined || this.ybNotice.id === null || this.ybNotice.id === '') {
              this.$post('ybNotice/addNotice', {
                dataJson: jsonString
              }).then((r) => {
                if (r.data.data.success === 1) {
                  this.isUpdate = true
                  this.ybNotice.id = r.data.data.data
                  this.fileDisabled = false
                  this.$message.success('保存成功.')
                } else {
                  this.$message.error(r.data.data.message)
                }
              }).catch(() => {
                this.loading = false
                this.$message.success('保存失败.')
              })
            } else {
              let ms = '保存'
              if (state !== 0) {
                ms = '提交'
              }
              this.$put('ybNotice/updateNotice', {
                dataJson: jsonString
              }).then((r) => {
                if (r.data.data.success === 1) {
                  this.isUpdate = true
                  if (state === 0) {
                    this.fileDisabled = false
                  } else {
                    this.fileDisabled = true
                    this.releaseDisabled = false
                    this.saveDisabled = true
                    this.$refs.ybNoticeFile.subDisabled()
                    this.$refs.ybNoticePerson.subDisabled()
                  }
                  this.$message.success(ms + '成功.')
                } else {
                  this.$message.error(r.data.data.message)
                }
              }).catch(() => {
                this.loading = false
                this.$message.success(ms + '失败.')
              })
            }
          }
        }
      })
    },
    setFields () {
      let values = this.form.getFieldsValue(['ntTitle', 'ntExplain', 'ntDetail', 'sendType'])
      if (typeof values !== 'undefined') {
        Object.keys(values).forEach(_key => { this.ybNotice[_key] = values[_key] })
      }
    }
  }
}
</script>
