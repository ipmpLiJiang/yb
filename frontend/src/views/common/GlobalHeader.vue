<template>
  <a-layout-header :class="[fixHeader && 'ant-header-fixedHeader', layout === 'side' ? (sidebarOpened ? 'ant-header-side-opened' : 'ant-header-side-closed') : null, theme, 'global-header' ]">
    <div :class="['global-header-wide', layout]">
      <router-link v-if="isMobile || layout === 'head'" to="/" :class="['logo', isMobile ? null : 'pc', theme]">
        <img width="32" src="static/img/logo.png"  alt=""/>
        <h1 v-if="!isMobile">{{systemName}}</h1>
      </router-link>
      <a-divider v-if="isMobile" type="vertical" />
      <a-icon v-if="layout === 'side'" class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="toggleCollapse"/>
      <div v-if="layout === 'head'" class="global-header-menu">
        <i-menu style="height: 64px; line-height: 64px;" class="system-top-menu" :theme="theme" mode="horizontal" :menuData="menuData" @select="onSelect"/>
      </div>
      <div :class="['global-header-right', theme]">
        <b>院区：&nbsp;&nbsp;{{hdAreaName}}</b>
        <!-- <b>院区：&nbsp;&nbsp;{{$store.state.account.user.areaType === 0 ? '本院' : '西院'}}</b> -->
        <!-- <b>院区：</b>
        <a-select v-model="hdAreaType" style="width: 100px" @change="handleAreaTypeChange">
          <a-select-option
          v-for="d in areaTypeDataSource"
          :key="d.value"
          >
          {{ d.text }}
          </a-select-option>
        </a-select> -->
          <header-avatar class="header-item"/>
      </div>
    </div>
  </a-layout-header>
</template>

<script>
import HeaderAvatar from './HeaderAvatar'
import IMenu from '@/components/menu/menu'
import { mapState } from 'vuex'

export default {
  name: 'GlobalHeader',
  components: {IMenu, HeaderAvatar},
  data () {
    return {
      hdAreaName: ''
      // hdAreaType: 0,
      // areaTypeDataSource: []
      // areaTypeDataSource: [{value: 0, text: '本部'}, {value: 1, text: '西院'}]
    }
  },
  props: ['collapsed', 'menuData'],
  computed: {
    ...mapState({
      isMobile: state => state.setting.isMobile,
      layout: state => state.setting.layout,
      systemName: state => state.setting.systemName,
      sidebarOpened: state => state.setting.sidebar.opened,
      fixHeader: state => state.setting.fixHeader
    }),
    theme () {
      return this.layout === 'side' ? 'light' : this.$store.state.setting.theme
    }
  },
  mounted () {
    this.findComArea()
  },
  methods: {
    findComArea () {
      // this.areaTypeDataSource = []
      this.$get('comConfiguremanage/getAreaList').then((r) => {
        if (r.data.length > 0) {
          let areaType = this.$store.state.account.user.areaType
          for (var i in r.data) {
            if (areaType === r.data[i].areaType) {
              this.hdAreaName = r.data[i].areaName
              break
            }
            // var at = {text: r.data[i].areaName, value: r.data[i].areaType}
            // this.areaTypeDataSource.push(at)
          }
          // this.hdAreaType = this.$store.state.account.user.areaType
        } else {
          this.hdAreaName = '本部'
        }
      }).catch(() => {
        this.hdAreaName = '本部'
      })
      // this.hdAreaName = this.$store.state.account.user.areaType === 0 ? '本院' : '西院'
      // this.hdAreaType = this.$store.state.account.user.areaType
    },
    toggleCollapse () {
      this.$emit('toggleCollapse')
    },
    onSelect (obj) {
      this.$emit('menuSelect', obj)
    }
    // handleAreaTypeChange (value) {
    //   this.hdAreaType = value
    //   this.$store.state.account.user.areaType = value
    // }
  }
}
</script>

<style lang="less" scoped>
  .trigger {
    font-size: 20px;
    line-height: 64px;
    padding: 0 24px;
    cursor: pointer;
    transition: color .3s;
  }
  .header-item{
    padding: 0 19px;
    display: inline-block;
    height: 100%;
    cursor: pointer;
    vertical-align: middle;
    i{
      font-size: 16px;
      color: rgba(0,0,0,.65);
    }
  }
  .global-header{
    padding: 0 12px 0 0;
    -webkit-box-shadow: 0 1px 4px rgba(0,21,41,.08);
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
    position: relative;
    &.light{
      background: #fff;
    }
    &.dark{
      background: #393e46;
    }
    .global-header-wide{
      &.head{
        padding: 0 24px;
      }
      &.side{
      }
      .logo {
        height: 64px;
        line-height: 58px;
        vertical-align: top;
        display: inline-block;
        padding: 0 12px 0 24px;
        cursor: pointer;
        font-size: 20px;
        &.pc{
          padding: 0 12px 0 0;
        }
        img {
          display: inline-block;
          vertical-align: middle;
        }
        h1{
          display: inline-block;
          font-size: 16px;
        }
        &.dark h1{
          color: #fff;
        }
      }
      .global-header-menu{
        display: inline-block;
      }
      .global-header-right{
        float: right;
        &.dark{
          color: #fff;
          i{
            color: #fff;
          }
        }
      }
    }
  }
  .ant-header-fixedHeader {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 15;
    width: 100%;
    transition: width .2s;

    &.ant-header-side-opened {
      width: 100%;
      padding-left: 254px;
    }

    &.ant-header-side-closed {
      width: 100%;
      padding-left: 80px;
    }
  }
</style>
