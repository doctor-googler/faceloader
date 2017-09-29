package me.hutkovich.faceloader.logic.exception;

public class DeviceCommanderException extends Exception {
  public DeviceCommanderException() {
    super();
  }

  public DeviceCommanderException(String message) {
    super(message);
  }

  public DeviceCommanderException(Throwable throwable) {
    super(throwable);
  }
}
