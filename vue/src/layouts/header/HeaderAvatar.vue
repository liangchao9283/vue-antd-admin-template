<template>
  <div>
  <a-dropdown>
    <div class="header-avatar" style="cursor: pointer">
      <a-avatar class="avatar" size="small" shape="circle" :src="user.avatar"/>
      <span class="name">{{user.name}}</span>
    </div>
    <a-menu :class="['avatar-menu']" slot="overlay">
      <a-menu-item>
        <a-icon type="user" />
        <span>个人中心</span>
      </a-menu-item>
      <a-menu-item>
        <a-icon type="setting" />
        <span>设置</span>
      </a-menu-item>

      <a-menu-item @click="editPasswordForm()">
        <a-icon  type="edit" />
        <span>修改密码</span>
      </a-menu-item>

      <a-menu-divider />

      <a-menu-item @click="logout">
        <a-icon style="margin-right: 8px;" type="poweroff" />
        <span>退出登录</span>
      </a-menu-item>
    </a-menu>
  </a-dropdown>

  <edit-password-form ref="EditPasswordForm" @ok="logout"></edit-password-form>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {logout} from '@/services/user'
import EditPasswordForm from './EditPasswordForm'
export default {
  name: 'HeaderAvatar',
  components: {EditPasswordForm},
  computed: {
    ...mapGetters('account', ['user']),
  },
  data() {
    return {

    }
  },
  methods: {
    logout() {
      logout()
      this.$router.push('/login')
    },

    editPasswordForm(){
      this.$refs.EditPasswordForm.init()
    }

  }
}
</script>

<style lang="less">
  .header-avatar{
    display: inline-flex;
    .avatar, .name{
      align-self: center;
    }
    .avatar{
      margin-right: 8px;
    }
    .name{
      font-weight: 500;
    }
  }
  .avatar-menu{
    width: 150px;
  }

</style>
