<script setup>

</script>

<template>
  <div class="instance-card">
    <div style="display: flex; justify-content: space-between">
      <div>
        <div class="name">
          <span class="flag-icon flag-icon-cn"></span>
          <span style="margin: 0 5px">LYS后端服务器</span>
          <i class="fa-regular fa-pen-to-square"></i>
        </div>
        <div class="os">
          操作系统：Debian 12.11
        </div>
      </div>
      <div class="status">
        <i style="color: #18cb18" class="fa-regular fa-circle-play"></i>
        <span style="margin-left: 5px">运行中</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0"/>
    <div class="network">
      <span style="margin-right: 10px">公网IP：127.0.0.1</span>
      <i class="fa-regular fa-copy" style="color: dodgerblue"></i>
    </div>
    <div class="hardware">
      <i class="fa-solid fa-microchip"></i>
      <span style="margin: 0 5px">2 CPU</span>
      <i class="fa-solid fa-memory"></i>
      <span style="margin: 0 5px">4 GB</span>
    </div>
    <div class="progress">
      <span style="margin-right: 10px;">CPU: 2.5%</span>
      <el-progress status="success" :percentage="2.5" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="progress">
      <span style="margin-right: 10px;">内存: 1.2%</span>
      <el-progress status="success" :percentage="1.2 / 10 * 100" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="network-flow">
      <div>
        <span style="margin-right: 10px;">网络流量</span>
      </div>
      <div>
        <i class="fa-solid fa-upload"></i>
        <span style="margin: 0 5px">52 KB.s</span>
        <el-divider direction="vertical"/>
        <i class="fa-solid fa-download"></i>
        <span style="margin: 0 5px">256 KB/s</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.instance-card{
  width: 320px;
  padding: 15px;
  border-radius: 15px;
  box-sizing: border-box;
  color: #6b6b6b;
  background: #e0e0e0;
  box-shadow:  9px 9px 30px #bebebe,
  -9px -9px 30px #ffffff;

  .name{
    font-size: 15px;
    font-weight: bold;
  }

  .status {
    font-size: 14px;
  }

  .os{
    font-size: 13px;
    color: gray;
  }

  .network{
    font-size: 13px;
  }

  .hardware{
    margin-top: 10px;
    font-size: 13px;
  }

  .progress{
    margin-top: 10px;
    font-size: 12px;
  }

  .network-flow{
    margin-top: 10px;
    font-size: 12px;
    display: flex;
    justify-content: space-between;
  }

}
</style>