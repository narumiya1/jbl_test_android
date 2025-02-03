package com.example.jbl.db;

public enum PeriodStatus {
  PER_open(0x00),
  PER_close(1),
  PER_power_fail(2);

  private final int value;

  PeriodStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
