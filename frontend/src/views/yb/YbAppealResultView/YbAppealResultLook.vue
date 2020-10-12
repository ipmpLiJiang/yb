<template>
  <a-drawer
    title="查看申诉材料"
    :maskClosable="false"
    width=50%
    placement="right"
    :closable="true"
    @close="onClose"
    :visible="lookVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
  <a-row type="flex" justify="start">
    <a-col :span=20>
      <!--科室、医生-->
      <a-row>
        <a-col :span=10>
          <a-form-item
            v-bind="formItemLayout"
            label="科室名称"
          >
            {{ybAppealResult.arDeptName}}
          </a-form-item>
        </a-col>
        <a-col :span=14>
          <a-form-item
            v-bind="formItemLayout"
            label="医生姓名"
          >
            {{ybAppealResult.arDoctorName}}
          </a-form-item>
        </a-col>
      </a-row>
      <!--申诉理由-->
      <a-row type="flex" justify="start">
        <a-col :span=24>
            <a-form-item
            v-bind="{
              labelCol: { span: 4 },
              wrapperCol: { span: 19, offset: 1 }
            }"
            label="申诉理由"
          >
          {{ybAppealResult.operateReason}}
          </a-form-item>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
  <br>
  <a-row>
    <a-col :span=24>
      <div style="margin:25px">
      <a-row type="flex" justify="center">
        <a-col>
          <template>
            <!--上传图片-->
            <div>
              <a-upload
                list-type="picture-card"
                :file-list="fileList"
                disabled="disabled"
                @preview="handlePreview"
              >
              </a-upload>
              <a-modal :width="600" :visible="previewVisible" :footer="null" @cancel="handleCancel">
                <img alt="example" style="width: 100%" :src="previewImage" />
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
  </a-drawer>
</template>

<script>
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
  name: 'YbAppealResultLook',
  props: {
    lookVisiable: {
      default: false
    }
  },
  data () {
    return {
      loading: false,
      formItemLayout,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      showDownLoad: true,
      ybAppealResult: {}
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    onClose () {
      this.previewVisible = false
      this.loading = false
      this.fileList = []
      this.previewImage = ''
      this.ybAppealResult = {}
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
    setFormValues ({ ...ybAppealResult }) {
      this.ybAppealResult = ybAppealResult
      this.findFileList(ybAppealResult)
    },
    downloadFile () {
      let formData = {}
      formData.id = this.ybAppealResult.id
      formData.deptName = this.ybAppealResult.arDeptName
      formData.applyDateStr = this.ybAppealResult.applyDateStr
      formData.typeNo = this.ybAppealResult.typeno
      formData.sourceType = this.ybAppealResult.sourceType
      formData.fileName = formData.applyDateStr + formData.deptName + '-' + this.ybAppealResult.typeno

      this.$download('comFile/fileImgZip', {
        ...formData
      }, formData.fileName + '.zip')
    },
    findFileList (ybAppealResult) {
      let formData = {}
      formData.id = ybAppealResult.id
      formData.deptName = ybAppealResult.arDeptName
      formData.applyDateStr = ybAppealResult.applyDateStr
      formData.sourceType = ybAppealResult.sourceType
      this.$post('comFile/listImgComFile', {
        ...formData
      }).then((r) => {
        for (let data of r.data.data) {
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
</style>
