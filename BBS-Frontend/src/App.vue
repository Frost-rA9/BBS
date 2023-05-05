<script setup>

import {get} from "@/net";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";
import router from "@/router";

const store = useStore()

if (store.auth == null) {
  get('/api/user/info ', (message) => {
    store.auth.user = message
    router.push('/index')
  }, () => {
    store.auth.user = null
  })
}


</script>

<template>
  <router-view></router-view>
</template>

<style scoped>

</style>
