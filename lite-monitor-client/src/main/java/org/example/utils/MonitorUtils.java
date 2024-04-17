package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ClientDetail;
import org.example.entity.RuntimeDetail;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.*;

@Slf4j
@Component
public class MonitorUtils {

    private static final double STATISTIC_TIME = 1.0;

    private static final double DISK_BASE = 1024.0;

    private static final double DISK_MB = DISK_BASE * DISK_BASE;

    private static final double DISK_GB = DISK_MB * DISK_BASE;

    public ClientDetail getClientDetail() {
        SystemInfo systemInfo = new SystemInfo();
        Properties properties = System.getProperties();

        OperatingSystem osSoftwareInfo = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer osHardwareInfo = systemInfo.getHardware();
        NetworkIF networkInfo = this.getClientAddress(osHardwareInfo);

        Double osMemory = osHardwareInfo.getMemory().getTotal() / DISK_GB;
        Double diskMemory = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / DISK_GB;
        return new ClientDetail()
                .setOsArch(properties.getProperty("os.arch"))
                .setOsName(osSoftwareInfo.getFamily())
                .setOsVersion(osSoftwareInfo.getVersionInfo().getVersion())
                .setOsBit(osSoftwareInfo.getBitness())
                .setCpuName(osHardwareInfo.getProcessor().getProcessorIdentifier().getName())
                .setCpuCores(osHardwareInfo.getProcessor().getLogicalProcessorCount())
                .setDiskMemory(diskMemory)
                .setOsMemory(osMemory)
                .setClientAddress(networkInfo != null ? networkInfo.getIPv4addr()[0] : null);
    }

    private NetworkIF getClientAddress(HardwareAbstractionLayer hardwareAbstractionLayer) {
        try {
            for (NetworkIF networkIF : hardwareAbstractionLayer.getNetworkIFs()) {
                String[] iPv4addr = networkIF.getIPv4addr();
                NetworkInterface networkInterface = networkIF.queryNetworkInterface();
                if (!networkInterface.isLoopback() && !networkInterface.isPointToPoint() && !networkInterface.isVirtual()
                && (networkInterface.getName().startsWith("eth") || networkInterface.getName().startsWith("en"))
                && iPv4addr.length > 0) {
                    return networkIF;
                }
            }
        } catch (IOException e) {
            log.error("获取客户端地址失败：{}", e.getMessage());
        }
        return null;
    }

    public RuntimeDetail getRuntimeDetail() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer osHardwareInfo = systemInfo.getHardware();
            NetworkIF networkIF = this.getClientAddress(osHardwareInfo);
            if (networkIF == null) {
                log.error("获取网络信息失败");
                return null;
            } else {
                CentralProcessor centralProcessor = osHardwareInfo.getProcessor();
                double networkUpload = networkIF.getBytesSent();
                double networkDownload = networkIF.getBytesRecv();
                double diskRead = osHardwareInfo.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum();
                double diskWrite = osHardwareInfo.getDiskStores().stream().mapToLong(HWDiskStore::getWriteBytes).sum();
                // 时间段统计 1s 的数据量取平均值
                Thread.sleep((long) STATISTIC_TIME * 1000);
                networkIF = this.getClientAddress(osHardwareInfo);
                networkUpload = (networkIF.getBytesSent() - networkUpload) / STATISTIC_TIME;
                networkDownload = (networkIF.getBytesRecv() - networkDownload) / STATISTIC_TIME;
                diskRead = (osHardwareInfo.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum() - diskRead) / STATISTIC_TIME;
                diskWrite = (osHardwareInfo.getDiskStores().stream().mapToLong(HWDiskStore::getWriteBytes).sum() - diskWrite) / STATISTIC_TIME;
                double memoryUsage = (osHardwareInfo.getMemory().getTotal() - osHardwareInfo.getMemory().getAvailable()) / DISK_GB;
                double diskUsage = Arrays.stream(File.listRoots()).mapToLong(
                        file -> file.getTotalSpace() - file.getFreeSpace()
                ).sum() / DISK_GB;
                return new RuntimeDetail()
                        .setTimestamp(new Date().getTime())
                        .setCpuUsage(this.getCpuUsage(centralProcessor))
                        .setMemoryUsage(memoryUsage)
                        .setDiskUsage(diskUsage)
                        .setNetworkUpload(networkUpload / DISK_BASE)
                        .setNetworkDownload(networkDownload / DISK_BASE)
                        .setDiskRead(diskRead / DISK_MB)
                        .setDiskWrite(diskWrite / DISK_MB);
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private double getCpuUsage(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

        double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
        double total = Arrays.stream(load).sum();
        return total / load.length + totalCpu / 100000.0;
    }


}
