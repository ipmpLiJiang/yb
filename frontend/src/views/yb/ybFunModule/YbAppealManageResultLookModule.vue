<template>
<div>
  <a-row type="flex" justify="start">
    <a-col :span=20>
      <!--科室、医生-->
      <a-row>
        <a-col :span=10>
          <a-form-item
            v-bind="formItemLayout"
            label="科室名称"
          >
            {{ybAppealManage.readyDeptName}}
          </a-form-item>
        </a-col>
        <a-col :span=14>
          <a-form-item
            v-bind="formItemLayout"
            label="医生姓名"
          >
            {{ybAppealManage.readyDoctorName}}
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
          {{ybAppealManage.operateReason}}
          </a-form-item>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
  <a-row>
    <a-col :span=24>
      <div style="margin:15px">
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
  name: 'YbAppealManageResultLookModule',
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
      ybAppealManage: {}
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
      this.ybAppealManage = {}
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
    setFormValues ({ ...ybAppealManage }) {
      this.fileList = []
      this.ybAppealManage = ybAppealManage
      this.findFileList(ybAppealManage)
    },
    downloadFile () {
      let formData = {}
      formData.id = this.ybAppealManage.id
      formData.deptName = this.ybAppealManage.readyDeptName
      formData.applyDateStr = this.ybAppealManage.applyDateStr
      formData.typeno = this.ybAppealManage.typeno
      formData.sourceType = this.ybAppealManage.sourceType
      formData.fileName = formData.applyDateStr + formData.deptName + '-' + this.ybAppealManage.typeno

      this.$download('comFile/fileImgZip', {
        ...formData
      }, formData.fileName + '.zip')
    },
    findFileList (ybAppealManage) {
      let formData = {}
      formData.id = ybAppealManage.id
      formData.deptName = ybAppealManage.readyDeptName
      formData.applyDateStr = ybAppealManage.applyDateStr
      formData.sourceType = ybAppealManage.sourceType
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
