<template>
  <div class="welcome no-select">
    <el-image class="bg" fit="cover" :src="bgImg"/>
    <div class="welcome-title">
      <div style="font-size: 36px; font-weight: bold">欢迎来到 Lite-Monitor 监控终端~</div>
      <div style="margin-top: 12px; font-size: 17px; font-weight: bold">
        我们将为您的服务器提供专业的监控服务
      </div>
      <div style="margin-top: 12px; font-size: 17px; font-weight: bold">
        为您的数据安全保驾护航！
      </div>
    </div>
    <div class="sign-container">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" style="height: 100%"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import bgImg from '../assets/img/bg.jpg'

let max = setInterval(() => {})
for (let i = 0; i <= max; i ++) {
  clearInterval(i)
}

</script>

<style scoped>
.welcome {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  position: relative;
}

.bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  animation: blurIn 0.7s;
}

@keyframes blurIn {
  from {
    transform: scale(1.3);
    filter: blur(1em);
  }
}

.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 20px black;
  animation: bottomToTop 700ms;
}

.sign-container {
  width: 400px;
  background-color: rgba(255, 255, 255, 0.8);
  -webkit-backdrop-filter: blur(1em) contrast(1.1);
  backdrop-filter: blur(4px);
  box-shadow: var(--el-box-shadow);
  position: absolute;
  z-index: 1;
  right: 0;
  top: 0;
  bottom: 0;
  animation: rightToLeft 500ms;
}

@keyframes bottomToTop {
  from {
    transform: translateY(50px);
  }
}

@keyframes rightToLeft {
  from {
    transform: translateX(100px);
  }
}
</style>