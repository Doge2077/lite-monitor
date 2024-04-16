<script setup>
import * as echarts from "echarts";
import {onMounted, watch} from "vue";
import {defaultOption, doubleSeries, singleSeries} from "@/echarts";

const charts = []

const props = defineProps({
  data: Object
})

const localTimeLine = list => list.map(item => new Date(item.timestamp).toLocaleString())

function updateCpuUsage(list) {
  const chart = charts[0]
  let data = list.map(item => (item.cpuUsage * 100).toFixed(1))
  const option = defaultOption('CPU(%)', localTimeLine(list))
  singleSeries(option, 'CPU使用率(%)', data, ['#72c4fe', '#72d5fe', '#2b6fd733'])
  chart.setOption(option)
}

function updateMemoryUsage(list) {
  const chart = charts[1]
  let data = list.map(item => (item.memoryUsage * 1024).toFixed(1));
  const option = defaultOption('内存(MB)', localTimeLine(list))
  singleSeries(option, '内存使用(MB)', data, ['#6be3a3', '#bbfad4', '#A5FFD033'])
  chart.setOption(option);
}

function updateNetworkUsage(list) {
  const chart = charts[2]
  let data = [
    list.map(item => item.networkUpload),
    list.map(item => item.networkDownload)
  ]
  const option = defaultOption('网络(KB/s)', localTimeLine(list))
  doubleSeries(option, ['上传(KB/s)', '下载(KB/s)'], data, [
    ['#f6b66e', '#ffd29c', '#fddfc033'],
    ['#79c7ff', '#3cabf3', 'rgba(192,242,253,0.2)']
  ])
  chart.setOption(option);
}

function updateDiskUsage(list) {
  const chart = charts[3]
  let data = [
    list.map(item => item.diskRead.toFixed(1)),
    list.map(item => item.diskWrite.toFixed(1))
  ]
  const option = defaultOption('磁盘(MB/s)', localTimeLine(list))
  doubleSeries(option, ['读取(MB/s)', '写入(MB/s)'], data, [
    ['#d2d2d2', '#d5d5d5', 'rgba(199,199,199,0.2)'],
    ['#757575', '#7c7c7c', 'rgba(94,94,94,0.2)']
  ])
  chart.setOption(option);
}

function initCharts() {
  const chartList = [
    document.getElementById('cpuUsage'),
    document.getElementById('memoryUsage'),
    document.getElementById('networkUsage'),
    document.getElementById('diskUsage')
  ]
  for (let i = 0; i < chartList.length; i++) {
    const chart = chartList[i]
    charts[i] = echarts.init(chart)
  }
}

onMounted(() => {
  initCharts()
  watch(() => props.data, list => {
    updateCpuUsage(list)
    updateMemoryUsage(list)
    updateNetworkUsage(list)
    updateDiskUsage(list)
  }, {immediate: true, deep: true})
})
</script>

<template>
  <div class="charts">
    <div id="cpuUsage" style="width: 100%;height:170px"></div>
    <div id="memoryUsage" style="width: 100%;height:170px"></div>
    <div id="networkUsage" style="width: 100%;height:170px"></div>
    <div id="diskUsage" style="width: 100%;height:170px"></div>
  </div>
</template>

<style scoped>
.charts {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 20px;
}
</style>
