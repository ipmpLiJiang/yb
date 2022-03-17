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
export default {
  props: {
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
      let params = {comments: keyword, areaId: this.user.areaType.value}
      this.$get('ybDrgDks/findDksList', {
        ...params
      }).then((r) => {
        r.data.data.forEach((item, i) => {
          body.push({
            value: item.dksId,
            text: item.dksId + '-' + item.dksName + '(' + item.areaName + ')'
          })
        })
      })
      return body
    },
    Load (value, data) {
      this.dataSource = data
      this.value = value
    }
  }
}
</script>
