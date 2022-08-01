<template>
  <a-select
    show-search
    :value="value"
    placeholder="请输入关键词"
    style="width: 100%"
    :default-active-first-option="false"
    :filter-option="false"
    :not-found-content="null"
    @search="handleSearch"
    @change="e => handleChange(e)"
  >
    <a-icon
      slot="suffixIcon"
      type="search"
    ></a-icon>
    <a-select-option
      v-for="d in dataSource"
      :key="d.value"
    >
      {{ d.text }}
    </a-select-option>
  </a-select>
</template>

<script>
import { fy } from '../js/custom'
export default {
  props: {
    type: {
      default: 1
    }
  },
  data () {
    return {
      value: '',
      user: this.$store.state.account.user,
      dataSource: []
    }
  },
  methods: {
    // 输入框事件
    handleSearch (keyword) {
      // 模拟往服务器发送请求
      if (keyword !== '') {
        this.dataSource = this.ajax(keyword)
      }
    },
    handleChange (value) {
      this.value = value
      this.selectChange(value)
    },
    selectChange (value) {
      const selectItem = this.dataSource.filter(item => value === item.value)[0]
      this.$emit('selectChange', selectItem)
    },
    // 模拟往服务器发送请求
    ajax (keyword) {
      let body = []
      let fyname = ''
      let params = {comments: keyword}
      if (this.type === 1) {
        this.$get('ybDks/findChsDksList', {
          ...params
        }).then((r) => {
          r.data.data.forEach((item, i) => {
            fyname = fy.getFyName(item.fyid)
            body.push({
              value: item.dksFyid,
              fyid: item.fyid,
              text: item.dksName + '(' + fyname + ')',
              dksName: item.dksName
            })
          })
        })
      }
      if (this.type === 2) {
        params = {comments: keyword, areaType: this.user.areaType.value}
        this.$get('ybDks/findDksChsConfireList', {
          ...params
        }).then((r) => {
          r.data.data.forEach((item, i) => {
            body.push({
              value: item.dksFyid,
              fyid: item.fyid,
              text: item.dksName + '(' + fyname + ')',
              dksName: item.dksName
            })
          })
        })
      }
      return body
    },
    Load (value, data) {
      this.dataSource = data
      this.value = value
    }
  }
}
</script>
