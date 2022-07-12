<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=80%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgResultLook"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgResultLook"
    >
    </ybDrgJk-module>
    <a-divider>复议信息</a-divider>
    <div>
      <div style="padding-bottom:20px;border: 1px solid #e8e8e8;">
        <a-row type="flex" justify="start">
          <a-col :span=20>
            <!--科室、医生-->
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="科室名称"
                >
                  {{ybDrgResultLook.dksId}}-{{ybDrgResultLook.dksName}}
                </a-form-item>
              </a-col>
              <a-col :span=14>
                <a-form-item
                  v-bind="formItemLayout"
                  label="医生姓名"
                >
                  {{ybDrgResultLook.doctorCode}}-{{ybDrgResultLook.doctorName}}
                </a-form-item>
              </a-col>
            </a-row>
            <!--医院意见-->
            <a-row type="flex" justify="start">
              <a-col :span=24>
                  <a-form-item
                  v-bind="{
                    labelCol: { span: 4 },
                    wrapperCol: { span: 19, offset: 1 }
                  }"
                  label="医院意见"
                >
                {{ybDrgResultLook.operateReason}}
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
          </a-row>
          <br>
          <a-row>
            <a-col :span=24>
              <div style="margin:0px 60px">
              <a-row>
                <a-col>
                  <template>
                    <!--上传图片-->
                    <div>
                      <a-upload
                        list-type="picture"
                        :file-list="fileList"
                        disabled="disabled"
                        @preview="handlePreview"
                        class="upload-list-inline"
                      >
                      </a-upload>
                      <a-modal width="85%" :visible="previewVisible" :footer="null" @cancel="handleCancel">
                        <div style="text-align:center">
                        <img alt="example" style="width: auto; height: auto; max-width: 100%; max-height: 100%;" :src="previewImage" />
                      </div>
                      </a-modal>
                    </div>
                  </template>
                </a-col>
              </a-row>
              </div>
            </a-col>
        </a-row>
        <br>
        <!--按钮-->
        <a-row type="flex" justify="center">
          <a-col :span=3>
            <a-button type="primary" v-show="showDownLoad" @click="downloadFile" style="margin-right: .10rem">下载</a-button>
          </a-col>
          <a-col :span=3>
            <a-button
              style="margin-right: .8rem"
              @click="onClose"
            >
              取消
            </a-button>
          </a-col>
        </a-row>
      </div>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import YbDrgDataModule from '../YbDrgFunModule/YbDrgDataModule'
import YbDrgJkModule from '../YbDrgFunModule/YbDrgJkModule'
const formItemLayout = {
  labelCol: { span: 10 },
  wrapperCol: { span: 13, offset: 1 }
}
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'YbDrgManageLook',
  components: {
    YbDrgDataModule, YbDrgJkModule},
  props: {
    lookVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      ybDrgResultLook: {},
      user: this.$store.state.account.user,
      showDownLoad: true
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    moment,
    onClose () {
      this.previewVisible = false
      this.fileList = []
      this.previewImage = ''
      this.ybDrgResultLook = {}
      this.$emit('close')
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    setFormValues ({ ...ybDrgResultLook }) {
      this.ybDrgResultLook = ybDrgResultLook
      setTimeout(() => {
        this.$refs.ybDrgJkModule.search()
      }, 200)
      this.previewVisible = false
      this.fileList = []
      this.previewImage = ''
      this.findFileList(ybDrgResultLook)
    },
    downloadFile () {
      let formData = {}
      formData.id = this.ybDrgResultLook.id
      formData.applyDateStr = this.ybDrgResultLook.applyDateStr
      formData.orderNumber = this.ybDrgResultLook.orderNumber
      formData.areaType = this.user.areaType.value
      formData.fileName = formData.applyDateStr + '-' + this.ybDrgResultLook.dksName + '-' + this.ybDrgResultLook.doctorCode + this.ybDrgResultLook.doctorName
      this.$download('comFile/fileDrgImgZip', {
        ...formData
      }, formData.fileName + '.zip')
    },
    findFileList (ybDrgResultLook) {
      let formData = {}
      formData.id = ybDrgResultLook.id
      formData.applyDateStr = ybDrgResultLook.applyDateStr
      formData.orderNumber = ybDrgResultLook.orderNumber
      formData.areaType = this.user.areaType.value
      formData.isOn = 1
      this.$post('comFile/listDrgImgComFile', {
        ...formData
      }).then((r) => {
        for (let data of r.data.data) {
          data.url = this.$baseURL + data.url
          data.thumbUrl = this.$baseURL + data.thumbUrl
          this.fileList.push(data)
        }
        if (r.data.data.length > 0) {
          this.showDownLoad = true
        } else {
          this.showDownLoad = false
        }
      }).catch(() => {
        console.log('异常')
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style><style scoped>
.upload-list-inline >>> .ant-upload-list-item {
  float: left;
  width: 230px;
  margin-right: 8px;
}

.upload-list-inline >>> .ant-upload-animate-enter {
  animation-name: uploadAnimateInlineIn;
}

.upload-list-inline >>> .ant-upload-animate-leave {
  animation-name: uploadAnimateInlineOut;
}
</style>
