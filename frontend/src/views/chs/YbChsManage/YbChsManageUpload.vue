<template>
  <a-drawer
    title="申诉填报"
    :maskClosable="false"
    width="82%"
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="chsVisiable"
    style="height: calc(100% - 15px); overflow: auto; padding-bottom: 53px"
  >
    <ybChsData-module ref="ybChsDataModule" :ybChsData="ybChsManageUpload">
    </ybChsData-module>
    <ybChsJk-module ref="ybChsJkModule" :ybChsData="ybChsManageUpload">
    </ybChsJk-module>
    <div style="margin-top: 10px">
      <div
        style="
          padding-top: 15px;
          padding-bottom: 15px;
          border: 1px solid #e8e8e8;
        "
      >
        <template>
          <a-form :form="form">
            <a-row type="flex" justify="start">
              <a-col :span="23">
                <!--汇总科室、医生-->
                <a-row type="flex" justify="center">
                  <a-col :span="8">
                    <a-form-item v-bind="formItemLayout" label="汇总科室">
                      {{ fy.getDksFyName(ybChsManageUpload.readyDksName, ybChsManageUpload.readyFyid) }}
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item v-bind="formItemLayout" label="复议医生">
                      {{ ybChsManageUpload.readyDoctorCode }}-{{ ybChsManageUpload.readyDoctorName }}
                    </a-form-item>
                  </a-col>
                  <a-col :span="1"> &nbsp; </a-col>
                  <a-col :span="6" style="color:red">
                    *复议上传附件：大小不得大于4MB，格式为.doc，.docx，.jpg，.png，附件只能上传一个
                  </a-col>
                </a-row>
                <!--复议理由-->
                <a-row type="flex" justify="center">
                  <a-col :span="16">
                    <a-form-item
                      v-bind="{
                        labelCol: { span: 4 },
                        wrapperCol: { span: 19, offset: 1 },
                      }"
                      label="复议理由"
                    >
                      <a-textarea
                        placeholder="请输入复议理由"
                        style="width: 100%"
                        :rows="7"
                        :maxLength="500"
                        v-decorator="[
                          'operateReason',
                          {
                            rules: [
                              { required: true, message: '复议理由不能为空' },
                            ],
                          },
                        ]"
                      />
                    <font style="color:red">复议理由不得超过500字</font>
                    </a-form-item>
                  </a-col>
                  <a-col :span="1"> &nbsp; </a-col>
                  <a-col :span="6">
                    <a-upload
                      accept=".doc,.docx,.jpg,.png"
                      :file-list="fileList"
                      :remove="handleImageRemove"
                      :beforeUpload="beforeUpload"
                    >
                      <a-button
                        v-if="fileBtnVisiable"
                      >
                        <a-icon type="upload" /> 上传附件
                      </a-button>
                    </a-upload>
                  </a-col>
                </a-row>
                <br />
                <a-row type="flex" justify="center">
                  <p style="color:red">{{lableErr}}</p>
                </a-row>
                <!--按钮-->
                <a-row type="flex" justify="center">
                  <a-col :span="5">
                    <a-popconfirm
                      title="确定提交数据？提交后不可更改！"
                      @confirm="handleSubmit(1)"
                      okText="确定"
                      cancelText="取消"
                    >
                      <a-button type="primary" style="margin-right: 0.8rem"
                        >提交</a-button
                      >
                    </a-popconfirm>
                  </a-col>
                  <a-col :span="5">
                    <a-button
                      @click="handleSubmit(0)"
                      type="primary"
                      :loading="loading"
                      >保存</a-button
                    >
                  </a-col>
                  <a-col :span="3">
                    <a-button style="margin-right: 0.8rem" @click="onClose">
                      取消
                    </a-button>
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
          </a-form>
        </template>
      </div>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import YbChsDataModule from '../YbChsFunModule/YbChsDataModule'
import YbChsJkModule from '../YbChsFunModule/YbChsJkModule'
import { fy } from '../../js/custom'
const formItemLayout = {
  labelCol: {
    span: 8
  },
  wrapperCol: {
    span: 15,
    offset: 1
  }
}

export default {
  name: 'YbChsManageUpload',
  components: {
    YbChsDataModule,
    YbChsJkModule
  },
  props: {
    chsVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      ybChsManageUpload: {},
      ybChsManage: {},
      fileList: [],
      lableErr: '',
      fileBtnVisiable: true,
      fy,
      user: this.$store.state.account.user,
      form: this.$form.createForm(this)
    }
  },
  computed: {},
  mounted () { },
  methods: {
    moment,
    reset () {
      this.form.resetFields()
    },
    beforeUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      testmsg = testmsg.toLowerCase()
      const isfile1 = testmsg === 'doc'
      const isfile2 = testmsg === 'docx'
      const isfile3 = testmsg === 'jpg'
      const isfile4 = testmsg === 'png'
      if (!(isfile1 || isfile2 || isfile3 || isfile4)) {
        this.$error({
          title: '只能上传.doc,.docx,.jpg,.png 格式的文件~'
        })
        return
      }
      const isLt = file.size / 1024 < 4001
      if (!isLt) {
        this.$error({
          title: '超4MB限制，不允许上传~'
        })
        return
      }
      return (isfile1 || isfile2 || isfile3 || isfile4) && isLt && this.customRequest(file)
    },
    customRequest (file) {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('id', this.ybChsManageUpload.id)
      formData.append('refTab', 'yb_chs_result')
      formData.append('refType', 'chs')
      formData.append('fileType', 'docjpg')
      formData.append('applyDateStr', this.ybChsManageUpload.applyDateStr)
      formData.append('areaType', this.user.areaType.value)
      this.uploading = true
      let that = this
      this.$upload('comFile/uploadCheck', formData).then((r) => {
        if (r.data.data.success === 1) {
          that.fileList.unshift(r.data.data)
          this.uploading = false
          this.$message.success('上传成功.')
          this.lableErr = ''
          if (that.fileList.length === 0) {
            that.fileBtnVisiable = true
          } else {
            that.fileBtnVisiable = false
          }
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    handleImageRemove (file) {
      // if (this.fileList.length === 1) {
      //   this.$message.warning('申诉附件无法删除，申诉复议必须上传附件！')
      //   this.lableErr = '申诉附件无法删除，申诉复议必须上传附件！'
      //   return false
      // }
      let that = this
      this.$confirm({
        title: '确定删除所选中的附件?',
        content: '当您点击确定按钮后，这些附件将会被彻底删除',
        centered: true,
        onOk () {
          let formData = {}
          formData.id = file.uid
          formData.applyDateStr = that.ybChsManageUpload.applyDateStr
          formData.areaType = that.user.areaType.value
          that.$post('comFile/deleteFile', {
            ...formData
          }).then((r) => {
            that.uploading = false
            if (r.data.data.success === 1) {
              that.$message.success('删除成功')
              that.fileBtnVisiable = true
              const index = that.fileList.indexOf(file)
              const newFileList = that.fileList.slice()
              newFileList.splice(index, 1)
              that.fileList = newFileList
            } else {
              that.$message.error(r.data.data.message)
            }
          }).catch(() => {
            that.$message.error('删除失败.')
          })
        },
        onCancel () { }
      })
    },
    onClose () {
      this.loading = false
      this.ybChsManage = {}
      this.ybChsManageUpload = {}
      this.fileList = []
      this.fileBtnVisiable = false
      this.$emit('close')
    },
    handleSubmit (type) {
      this.lableErr = ''
      this.form.validateFields((err, values) => {
        if (!err) {
          let fromData = this.form.getFieldsValue(['operateReason'])
          let state = type === 0 ? 1 : 6 // 1 保存 6提交
          // 复议判断附件必须上传
          if (state === 6 && this.fileList.length === 0) {
            this.$message.warning('未上传申诉附件，无法提交！')
            this.lableErr = '未上传申诉附件，无法提交！'
            return false
          }
          let data = {
            id: this.ybChsManageUpload.id,
            state: state,
            applyDataId: this.ybChsManageUpload.applyDataId,
            verifyId: this.ybChsManageUpload.verifyId,
            readyDksId: this.ybChsManageUpload.readyDksId,
            readyDksName: this.ybChsManageUpload.readyDksName,
            readyFyid: this.ybChsManageUpload.readyFyid,
            readyDoctorCode: this.ybChsManageUpload.readyDoctorCode,
            readyDoctorName: this.ybChsManageUpload.readyDoctorName,
            operateReason: fromData.operateReason
          }
          let jsonString = JSON.stringify(data)
          this.$put('ybChsManage/updateUploadState', {
            dataJson: jsonString
          }).then((r) => {
            if (r.data.data.success === 1) {
              if (state === 6) {
                this.$message.success('提交成功！')
                this.onClose()
                this.$emit('success')
              } else {
                this.loading = false
                this.$message.success('保存成功！')
              }
            } else {
              this.$message.error(r.data.data.message)
              this.lableErr = r.data.data.message
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    setFormValues ({
      ...ybChsManageUpload
    }) {
      this.fileBtnVisiable = false
      this.lableErr = ''
      this.ybChsManageUpload = ybChsManageUpload

      this.form.getFieldDecorator('operateReason')
      this.form.setFieldsValue({
        'operateReason': ybChsManageUpload.operateReason
      })

      this.findFileList(ybChsManageUpload.id)

      setTimeout(() => {
        this.$refs.ybChsJkModule.search()
      }, 200)
    },
    findCreate (ybChsResult) {
      // 列表点击申诉时创建复议审核上传数据，业务需求变更不调用此方法,更改为提交时创建复议审核上传数据
      this.$post('ybChsResult/findCreateChsResult', {
        ...ybChsResult
      }).then((r) => {
        if (r.data.data.success === 0) {
          this.$message.error('查询创建失败!')
          this.onClose()
        } else {
          ybChsResult = r.data.data.data
          this.ybChsManageUpload.operateReason = ybChsResult.operateReason
          this.ybChsManageUpload.readyDksId = ybChsResult.dksId
          this.ybChsManageUpload.readyDksName = ybChsResult.dksName
          this.ybChsManageUpload.readyFyid = ybChsResult.readyFyid
          this.ybChsManageUpload.readyDoctorCode = ybChsResult.doctorCode
          this.ybChsManageUpload.readyDoctorName = ybChsResult.doctorName

          this.form.setFieldsValue({
            'operateReason': ybChsResult.operateReason
          })
        }
      }).catch(() => {
        this.loading = false
      })
    },
    findFileList (id) {
      let formData = {}
      this.fileList = []
      formData.id = id
      formData.applyDateStr = this.ybChsManageUpload.applyDateStr
      formData.areaType = this.user.areaType.value
      this.$post('comFile/uploadFileList', {
        ...formData
      }).then((r) => {
        for (let data of r.data.data) {
          data.url = this.$baseURL + data.url
          data.thumbUrl = this.$baseURL + data.thumbUrl
          this.fileList.push(data)
        }
        if (this.fileList.length === 0) {
          this.fileBtnVisiable = true
        } else {
          this.fileBtnVisiable = false
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
