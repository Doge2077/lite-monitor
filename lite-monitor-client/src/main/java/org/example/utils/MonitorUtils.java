package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ClientDetail;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.platform.linux.LinuxNetworkIF;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
@Component
public class MonitorUtils {
    private final double DISK_BASE = 1024.0;

    public ClientDetail getClientDetail() {
        SystemInfo systemInfo = new SystemInfo();
        Properties properties = System.getProperties();

        OperatingSystem osSoftwareInfo = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer osHardwareInfo = systemInfo.getHardware();
        NetworkIF networkInfo = this.getClientAddress(osHardwareInfo);

        Double osMemory = osHardwareInfo.getMemory().getTotal() / (DISK_BASE * DISK_BASE * DISK_BASE);
        Double diskMemory = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / (DISK_BASE * DISK_BASE * DISK_BASE);
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
            log.error("获取客户端地址失败：{}", e.getCause());
        }
        return null;
    }

}
