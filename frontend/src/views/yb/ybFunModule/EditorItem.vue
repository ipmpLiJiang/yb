<template>
  <section ref="editor">

  </section>
</template>

<script>
import E from 'wangeditor-antd'
export default {
  name: 'editor',
  props: ['get-full-text', 'content'], // 回调方法
  mounted () {
    let editor = new E(this.$refs.editor)
    editor.customConfig.onchange = (html) => {
      this.$emit('change', html) // 将内容同步到父组件中
    }

    // 开启debug模式
    editor.customConfig.debug = true
    // 关闭粘贴内容中的样式
    editor.customConfig.pasteFilterStyle = false
    // 忽略粘贴内容中的图片
    editor.customConfig.pasteIgnoreImg = true
    // 使用 base64 保存图片
    // editor.customConfig.uploadImgShowBase64 = true
    // 上传图片到服务器
    editor.customConfig.uploadFileName = 'file' // 设置文件上传的参数名称
    editor.customConfig.uploadImgServer = 'comFile/weUploadFile' // 设置上传文件的服务器路径
    editor.customConfig.uploadImgMaxSize = 10 * 1024 * 1024 // 将图片大小限制为 3M
    editor.customConfig.uploadImgMaxLength = 5
    editor.customConfig.uploadImgHooks = {
      success: function (xhr, editor, result) {
        // 图片上传并返回结果，图片插入成功之后触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
      },
      fail: function (xhr, editor, result) {
        // 图片上传并返回结果，但图片插入错误时触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
      },
      error: function (xhr, editor) {
        // 图片上传出错时触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
      }

    }
    editor.customConfig.customUploadImg = function (files, insert) {
      var data = new FormData()
      for (var i = 0; i < files.length; i++) {
        data.append('files', files[i])
      }
      this.$post('comFile/weUploadFiles', {
        ...data
      }).then((r) => {
        if (r.error === 0) {
          for (var j = 0; j < r.data.length; j++) {
            insert(r.data[j])
          }
        } else {
          alert('111')
        }
      }).catch(() => {
        this.$message.error('图片上传失败.')
      })
    }
    // 新增
    editor.customConfig.zIndex = 100
    // 工具条高度 默认 50px small为40px
    editor.customConfig.toolBarSize = 'small'
    // 最小高度 默认 100px
    editor.customConfig.minHeight = '100px'
    // 最大高度 默认 500px
    editor.customConfig.maxHeight = '200px'

    editor.customConfig.menus = [
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
      'emoticon', // 表情
      'image', // 插入图片
      'table', // 表格
      'undo', // 撤销
      'redo' // 重复
    ]
    editor.customConfig.uploadImgParams = {
      from: 'editor'
    }
    editor.create()
    // 如果默认传递content值则渲染默认内容
    if (this.content) {
      editor.txt.html(this.content)
    }
  },
  methods: {
  }
}
// import EditorBar from '../ybFunModule/EditorItem'
// components: { EditorBar },
// <template>
// <div>
// <editor-bar v-model="detail" :content="111"  @change="change"></editor-bar>
// </div>
// </template>
</script>
