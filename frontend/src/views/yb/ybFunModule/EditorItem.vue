<template>
  <div class="editor">
    <div ref="toolbar" class="toolbar">
    </div>
    <div ref="editor" class="editorContent">
    </div>
  </div>
</template>

<script>
import E from 'wangeditor-antd'
export default {
  name: 'editor',
  data () {
    return {
      editor: null,
      info_: null
    }
  },
  model: {
    prop: 'value',
    event: 'change'
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    isClear: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    isClear (val) {
      // 触发清除文本域内容
      if (val) {
        this.editor.txt.clear()
        this.info_ = null
      }
    },
    value: function (value) {
      if (value !== this.editor.txt.html()) {
        this.editor.txt.html(this.value)
      }
    }
    // value为编辑框输入的内容，这里我监听了一下值，
    // 当父组件调用得时候，如果给value赋值了，子组件将会显示父组件赋给的值
  },
  mounted () {
    this.seteditor()
    this.editor.txt.html(this.value)
  },
  methods: {
    setMenus () {
      this.editor.customConfig.menus = [
        'head', // 标题
        'bold', // 粗体
        'fontSize', // 字号
        'fontName', // 字体
        'italic', // 斜体
        'underline', // 下划线
        'strikeThrough', // 删除线
        'foreColor', // 文字颜色
        'backColor', // 背景颜色
        'link', // 插入链接
        'list', // 列表
        'justify', // 对齐方式
        'quote', // 引用
        // 'emoticon', // 表情
        'image', // 插入图片
        'table', // 表格
        'undo', // 撤销
        'redo', // 重复
        'fullscreen' // 全屏
      ]
    },
    seteditor () {
      this.editor = new E(this.$refs.toolbar, this.$refs.editor)
      // 开启debug模式
      // this.editor.customConfig.debug = true
      // 关闭粘贴内容中的样式
      this.editor.customConfig.pasteFilterStyle = false
      // 忽略粘贴内容中的图片
      this.editor.customConfig.pasteIgnoreImg = true
      // 使用 base64 保存图片
      // this.editor.customConfig.uploadImgShowBase64 = true
      this.editor.customConfig.uploadImgServer = this.$baseURL + 'comFile/weUploadFile'// 填写配置服务器端地址
      this.editor.customConfig.uploadImgHeaders = {Authentication: this.$store.state.account.token}// 自定义 header
      this.editor.customConfig.uploadFileName = 'file' // 后端接受上传文件的参数名
      this.editor.customConfig.uploadImgAccept = ['jpg', 'jpeg', 'png']
      this.editor.customConfig.uploadImgParams = {
        // 如果版本 <=v3.1.0 ，属性值会自动进行 encode ，此处无需 encode
        // 如果版本 >=v3.1.1 ，属性值不会自动 encode ，如有需要自己手动 encode
      }
      this.editor.customConfig.uploadImgMaxSize = 0.3 * 1024 * 1024 // 将图片大小限制为 10M
      this.editor.customConfig.uploadImgMaxLength = 1 // 限制一次最多上传 6 张图片
      this.editor.customConfig.uploadImgTimeout = 3 * 60 * 1000 // 设置超时时间
      // 自定义 onchange 触发的延迟时间，默认为 200 ms
      // this.editor.customConfig.onchangeTimeout = 1000 // 单位 ms
      // 隐藏�网络图片�tab
      this.editor.customConfig.showLinkImg = false

      this.editor.customConfig.uploadImgHooks = {
        fail: (xhr, editor, result) => {
          // 插入图片失败回调
        },
        success: (xhr, editor, result) => {
          // 图片上传成功回调
        },
        timeout: (xhr, editor) => {
          // 网络超时的回调
        },
        error: (xhr, editor) => {
          // 图片上传错误的回调
        },
        customInsert: (insertImg, result, editor) => {
          // 图片上传成功，插入图片的回调
          // result为上传图片成功的时候返回的数据，这里我打印了一下发现后台返回的是data：[{url:"路径的形式"},...]
          // console.log(result.data[0].url)
          // insertImg()为插入图片的函数
          // 循环插入图片
          // for (let i = 0; i < 1; i++) {
          // let url = result.data.image_url
          let url = result.data[0]
          insertImg(url)
          // }
        }
      }
      // this.editor.customConfig.customUploadImg = function (files, insert) {
      //   var data = new FormData()
      //   // for (var i = 0; i < files.length; i++) {
      //   //   data.append('files', files[i])
      //   // }
      //   data.append('file', files[0])
      //   this.$post('comFile/weUploadFile', {
      //     ...data
      //   }).then((r) => {
      //     if (r.error === 0) {
      //       for (var j = 0; j < r.data.length; j++) {
      //         insert(r.data[j])
      //       }
      //     } else {
      //       alert('111')
      //     }
      //   }).catch(() => {
      //     this.$message.error('图片上传失败.')
      //   })
      // }
      // 配置菜单
      this.setMenus()
      this.editor.customConfig.onchange = (html) => {
        this.info_ = html // 绑定当前逐渐地值
        this.$emit('change', this.info_) // 将内容同步到父组件中
      }
      // 创建富文本编辑器
      this.editor.create()
    }
  }
}
</script>
<style lang="css">
  .editor {
    width: 100%;
    margin: 0 auto;
    position: relative;
    z-index: 0;
  }
  .toolbar {
    border: 1px solid #ccc;
  }
  .editorContent {
    height:380px;
    max-height:380px;
    border:1px solid #ccc;
    word-wrap: break-all;
  }
  .editorContent p {
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    white-space: normal;
  }
</style>
