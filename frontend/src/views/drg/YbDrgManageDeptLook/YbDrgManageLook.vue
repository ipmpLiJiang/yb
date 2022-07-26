<template>
  <a-drawer
    title="查看"
    :maskClosable="false"
    width=82%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 15px);overflow: auto;padding-bottom: 53px;"
  >
    <ybDrgData-module
    ref="ybDrgDataModule"
    :ybDrgData="ybDrgManageLook"
    >
    </ybDrgData-module>
    <ybDrgJk-module
    ref="ybDrgJkModule"
    :ybDrgData="ybDrgManageLook"
    >
    </ybDrgJk-module>
    <a-divider v-show="ybDrgManageLook.state === 2?true:false">变更信息</a-divider>
    <div v-show="ybDrgManageLook.state === 6 || ybDrgManageLook.state === 1 || ybDrgManageLook.state === 2?true:false">
      <div style="padding-bottom:20px;border: 1px solid #e8e8e8;">
        <br>
        <div v-show="ybDrgManageLook.state === 6 || ybDrgManageLook.state === 1 ?true:false">
          <a-row type="flex" justify="start">
          <a-col :span=20>
            <!--科室、医生-->
            <a-row>
              <a-col :span=10>
                <a-form-item
                  v-bind="formItemLayout"
                  label="科室名称"
                >
                  {{ ybDrgManageLook.readyDksId }}-{{ybDrgManageLook.readyDksName}}
                </a-form-item>
              </a-col>
              <a-col :span=14>
                <a-form-item
                  v-bind="formItemLayout"
                  label="医生姓名"
                >
                  {{ybDrgManageLook.readyDoctorCode}}-{{ybDrgManageLook.readyDoctorName}}
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
                {{ybDrgManageLook.operateReason}}
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
        </div>
        <!--拒绝-->
        <div v-show="ybDrgManageLook.state === 2?true:false">
        <a-row type="flex" justify="start">
        <a-col :span=20>
          <!--科室、医生-->
          <a-row>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请科室"
              >
                {{ybDrgManageLook.readyDksId}}-{{ybDrgManageLook.readyDksName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="申请人"
              >
                {{ybDrgManageLook.readyDoctorCode}}-{{ybDrgManageLook.readyDoctorName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="更改科室"
              >
                {{ybDrgManageLook.changeDksName}}
              </a-form-item>
            </a-col>
            <a-col :span=6>
              <a-form-item
                v-bind="formItemLayout"
                label="更改人"
              >
                {{ybDrgManageLook.changeDoctorCode}}-{{ybDrgManageLook.changeDoctorName}}
              </a-form-item>
            </a-col>
          </a-row>
          <!--医院意见-->
          <a-row type="flex" justify="start">
            <a-col :span=20>
                <a-form-item
                v-bind="{
                  labelCol: { span: 3 },
                  wrapperCol: { span: 19 }
                }"
                label="医院意见"
              >
              {{ybDrgManageLook.operateReason}}
              </a-form-item>
            </a-col>
          </a-row>
        </a-col>
        </a-row>
        </div>
        <br>
        <!--按钮-->
        <a-row type="flex" justify="center">
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
      ybDrgManageLook: {},
      user: this.$store.state.account.user,
      ybDrgManage: {}
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
      this.ybDrgManage = {}
      this.ybDrgManageLook = {}
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
    setFormValues ({ ...ybDrgManageLook }) {
      this.ybDrgManageLook = ybDrgManageLook
      setTimeout(() => {
        this.$refs.ybDrgJkModule.search()
      }, 200)
      if (ybDrgManageLook.state === 6 || ybDrgManageLook.state === 1) {
        this.previewVisible = false
        this.fileList = []
        this.previewImage = ''
        this.findFileList(ybDrgManageLook)
      }
    },
    findFileList (ybDrgManageLook) {
      let formData = {}
      formData.id = ybDrgManageLook.id
      formData.applyDateStr = ybDrgManageLook.applyDateStr
      formData.orderNumber = ybDrgManageLook.orderNumber
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
