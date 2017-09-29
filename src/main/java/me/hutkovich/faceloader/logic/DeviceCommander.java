package me.hutkovich.faceloader.logic;

import com.google.common.collect.ImmutableList;
import me.hutkovich.faceloader.logic.exception.DeviceCommanderException;
import org.apache.commons.collections4.CollectionUtils;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.managers.Package;
import se.vidstige.jadb.managers.PackageManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceCommander {
  private final String ADB_INSTALL_COMMAND = "adb.cmd";
  private JadbConnection connection;
  private Map<String, PackageManager> packageManagerPool;

  public DeviceCommander() throws DeviceCommanderException {
    try {
      connection = new JadbConnection();
      packageManagerPool = new HashMap<>();
    } catch (IOException e) {
      throw new DeviceCommanderException(e);
    }
  }
  public List<String> getDevicesID() throws DeviceCommanderException{
    try {
      List<JadbDevice> devices = connection.getDevices();
      if (CollectionUtils.isEmpty(devices)) {
        return ImmutableList.of();
      }
      final List<String> strings = new ArrayList<>(3);
      devices.forEach(device -> strings.add(device.getSerial()));
      return strings;
    } catch (IOException | JadbException e) {
      throw new DeviceCommanderException(e);
    }
  }

  public void installPackageToDevice(final String deviceID, final String fileName) throws DeviceCommanderException {
    File file = new File(fileName);
    if (!file.exists()) {
      throw new DeviceCommanderException(String.format("File %s does not exist!", fileName));
    }
    try {
      JadbDevice device = getDevice(deviceID);
      PackageManager pm = getPackageManager(device);
      pm.install(file);
    } catch (IOException | JadbException e) {
      throw new DeviceCommanderException(e);
    }
  }

  public void deletePackageFromDevice(final String deviceID, final String packageName) throws DeviceCommanderException {
    try {
      JadbDevice device = getDevice(deviceID);
      PackageManager pm = getPackageManager(device);
      Package pkg = pm.getPackages().stream()
          .filter(aPackage -> aPackage.equals(packageName))
          .findFirst()
          .get();
      pm.uninstall(pkg);
    } catch (IOException | JadbException e) {
      throw new DeviceCommanderException(e);
    }
  }

  public List<String> packagesList(final String deviceID) throws DeviceCommanderException {
    try {
      JadbDevice device = getDevice(deviceID);
      PackageManager pm = new PackageManager(device);
      List<Package> packages = pm.getPackages();
      if (CollectionUtils.isEmpty(packages)) {
        return ImmutableList.of();
      }
      List<String> packageList = new ArrayList<>(pm.getPackages().size());
      packages.forEach(pkg -> packageList.add(pkg.toString()));
      return  packageList;
    } catch (IOException | JadbException e) {
      throw new DeviceCommanderException(e);
    }
  }

  private JadbDevice getDevice(final String id) throws IOException, JadbException {
    return connection.getDevices().stream()
        .filter(jadbDevice -> jadbDevice.getSerial().equalsIgnoreCase(id))
        .findFirst()
        .get();
  }

  private PackageManager getPackageManager(final JadbDevice device) {
    PackageManager pm = packageManagerPool.get(device.getSerial());
    if (pm == null) {
      pm = new PackageManager(device);
      packageManagerPool.put(device.getSerial(), pm);
    }
    return pm;
  }


  public static void main(String[] args) throws Exception {
    DeviceCommander dc = new DeviceCommander();
    List<String> devices = dc.getDevicesID();
    String id = devices.get(0);
    dc.packagesList(id).forEach(pkg-> System.out.println(pkg));
    dc.installPackageToDevice(id, "C:\\Users\\Raman_Hutkovich\\Desktop\\kek\\meter.apk");
    Thread.sleep(10000);
    dc.deletePackageFromDevice(id, "com.meterwatchface");
    dc.packagesList(id).forEach(pkg-> System.out.println(pkg));
  }
}
