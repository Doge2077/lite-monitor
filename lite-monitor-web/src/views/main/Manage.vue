<script setup>
import PreviewCard from "@/component/PreviewCard.vue";
import {reactive, ref} from "vue";
import {get} from '@/net'
import ClientDetails from "@/component/ClientDetails.vue";
const list = ref([])

const updateList = () => get('/api/monitor/list', data => list.value = data)
setInterval(updateList, 1000)
updateList()

const detail = reactive({
  show: false,
  clientId: -1
})

const displayClientDetails = (clientId) => {
  detail.show = true
  detail.clientId = clientId
}

</script>

<template>
  <div class="manage-main">
    <div class="title"><i class="fa-solid fa-server"></i> 管理主机列表</div>
    <div class="desc">在这里管理所有已经注册的主机实例，实时监控主机状态，快速管理</div>
    <el-divider style="margin: 10px 0;"/>
    <div class="card-list">
      <preview-card v-for="item in list" :data="item" :update="updateList"
                    @click="displayClientDetails(item.clientId)"/>
    </div>
    <el-drawer size="520" :show-close="false" v-model="detail.show"
               :with-header="false" v-if="list.length" @close="detail.clientId = -1">
      <client-details :client-id="detail.clientId" :update="updateList"/>
    </el-drawer>
  </div>
</template>

<style scoped>

:deep(.el-drawer) {
  margin: 10px;
  height: calc(100% - 20px);
  border-radius: 20px;
}

:deep(.el-drawer__body) {
  padding: 0;
}

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