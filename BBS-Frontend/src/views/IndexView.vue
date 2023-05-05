<template>
  <div>欢迎{{ store.auth.user }}进入论坛系统</div>
  <div>
    <el-button @click="logout()" type="danger">退出登录</el-button>
  </div>
</template>

<script setup>
import {get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";

const store = useStore()

const logout = () => {
  get('/api/auth/logout', (message) => {
    ElMessage.success(message)
    store.auth.user = null
    router.push('/')
  })
}
</script>

<style scoped>

</style>