<template>
  <div style="text-align: center;margin: 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold;">注册新用户</div>
      <div style="font-size: 15px;color: grey">欢迎注册论坛系统，请在下面填写相关信息</div>
    </div>
    <div style="margin-top: 50px">
      <el-input v-model="from.username" type="text" placeholder="用户名">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="from.password" type="password" placeholder="密码" style="margin-top: 10px">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="from.password_repeat" type="password" placeholder="重复密码" style="margin-top: 10px">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
      <el-input v-model="from.email" type="email" placeholder="电子邮件地址" style="margin-top: 10px">
        <template #prefix>
          <el-icon>
            <Message/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-row :gutter="28">
        <el-col :span="16">
          <el-input v-model="from.code" type="email" placeholder="验证码">
            <template #prefix>
              <el-icon>
                <EditPen/>
              </el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="success">获取验证码</el-button>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 80px">
      <el-button style="width: 300px" type="warning" plain>立即注册</el-button>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 15px;line-height: 15px;color: grey">已有账号？</span>
      <el-link style="translate: 0 -1.5px" type="primary" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<script setup>

import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive} from "vue";

const from = reactive({
  username:'',
  password:'',
  password_repeat:'',
  email:'',
  code:''
})
</script>

<style scoped>

</style>