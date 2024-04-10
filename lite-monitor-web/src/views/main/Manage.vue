<script setup>

import PreviewCard from "@/component/PreviewCard.vue";
</script>

<template>
  <div class="manage-main">
    <div class="title"><i class="fa-solid fa-server"></i> 管理主机列表</div>
    <div class="desc">在这里管理所有已经注册的主机实例，实时监控主机状态，快速管理</div>
    <el-divider style="margin: 10px 0;"/>
    <div class="card-list">
      <preview-card/>
      <preview-card/>
      <preview-card/>
    </div>
  </div>
</template>

<style scoped>
.manage-main{
  margin: 0 50px;
  .title{
    font-size: 22px;
    font-weight: bold;
  }

  .desc{
    font-size: 15px;
    color: gray;
  }

  .card-list {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
  }
}
</style>