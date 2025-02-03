package com.example.jbl;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Config {
  private String ipTcm;
  private int appNoPerioda;
  private int appState;
  private int gerbangNumber;
  private int garduNumber;
  private int garduID;
  private int applicationMode;
  private int bcTicketNumberOL;
  private int bcTicketNumberSU;
  private int tarifId;
  private int logPerioda;
  private LocalDateTime cycleTime;
  private LocalDateTime lastOpenTime;
  private LocalDateTime alarm1, alarm2, alarm3;

  public int MaximumGerbang = 65;
  private static volatile Config instance;
  private static SharedPreferences sp;
  private SharedPreferences.Editor myEdit;
  private static final String PREF_NAME = "MyPreferences";
  public Config()
  {
    cycleTime = LocalDateTime.of(2000, 1, 1, 5, 0, 0);
    lastOpenTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    appNoPerioda = 0;
    gerbangNumber = 5;
    garduNumber = garduID = 2;
    applicationMode = 3;
  }
  // To Do Config SharedPreferences
  private Config(Context context) {
    sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    myEdit = sp.edit();
  }
  // To Do Config SharedPreferences
  public static Config getInstance2(Context context) {
    if (instance == null) {
      synchronized (Config.class) {
        if (instance == null) {
          instance = new Config(context);
        }
      }
    }
    return instance;
  }
  public static Config getInstance() {
    if (instance == null) {
      synchronized (Config.class) {
        if (instance == null) {
          instance = new Config();
        }
      }
    }
    return instance;
  }
  public void setConfig(SharedPreferences sh)
  {
    sp = sh;
    myEdit = sp.edit();
  }
  public String getIpTcm() {
    ipTcm = sp.getString("ipTcm", "127.0.0.1");
    return ipTcm;
  }
  public void setIpTcm(String value) {
    sp.edit().putString("ipTcm", value).apply();
    this.ipTcm = value;
  }
  public int getAppNoPerioda() {
    appNoPerioda = sp.getInt("NoPerioda", 0);
    return appNoPerioda;
  }

  public void setAppNoPerioda(int appNoPerioda) {
    sp.edit().putInt("NoPerioda", appNoPerioda).apply();
    this.appNoPerioda = appNoPerioda;
  }

  public int getAppState() {
    appState = sp.getInt("AppState", 0);
    return appState;
  }

  public void setAppState(int appNoMTN) {
    sp.edit().putInt("AppState", appNoMTN).apply();
    this.appState = appNoMTN;
  }

  public int getGerbangNumber() {
    gerbangNumber = sp.getInt("GerbangNumber", 0);
    return gerbangNumber;
  }

  public void setGerbangNumber(int gerbangNumber) {
    sp.edit().putInt("GerbangNumber", gerbangNumber).apply();
    this.gerbangNumber = gerbangNumber;
  }

  public int getGarduNumber() {
    garduNumber = sp.getInt("GarduNumber", 0);
    return garduNumber;
  }

  public void setGarduNumber(int garduNumber) {
    sp.edit().putInt("GarduNumber", garduNumber).apply();
    this.garduNumber = garduNumber;
  }

  public int getGarduID() {
    garduID = sp.getInt("garduID", 0);
    return garduID;
  }

  public void setGarduID(int garduID) {
    sp.edit().putInt("garduID", garduID).apply();
    this.garduID = garduID;
  }

  public int getApplicationMode() {
    applicationMode = sp.getInt("applicationMode", 0);
    return applicationMode;
  }

  public void setApplicationMode(int applicationMode) {
    sp.edit().putInt("applicationMode", applicationMode).apply();
    this.applicationMode = applicationMode;
  }

  public int getBcTicketNumberOL() {
    return bcTicketNumberOL;
  }

  public void setBcTicketNumberOL(int bcTicketNumberOL) {
    this.bcTicketNumberOL = bcTicketNumberOL;
  }

  public int getBcTicketNumberSU() {
    return bcTicketNumberSU;
  }

  public void setBcTicketNumberSU(int bcTicketNumberSU) {
    this.bcTicketNumberSU = bcTicketNumberSU;
  }

  public int getTarifId() {
    tarifId = sp.getInt("tarifId", 0);
    return tarifId;
  }

  public void setTarifId(int tarifId) {
    sp.edit().putInt("tarifId", tarifId).apply();
    this.tarifId = tarifId;
  }

  public int getLogPerioda() {
    logPerioda = sp.getInt("logPerioda", 0);
    return logPerioda;
  }

  public void setLogPerioda(int logPerioda) {
    sp.edit().putInt("logPerioda", logPerioda).apply();
    this.logPerioda = logPerioda;
  }

  public LocalDateTime getCycleTime() {
    long ms = sp.getLong("cycleTime", 0);
    if (ms == 0)
      cycleTime = LocalDateTime.of(2000, 1, 1, 5, 0, 0);
    else
      cycleTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneOffset.UTC);
    return cycleTime;
  }

  public void setCycleTime(LocalDateTime cycleTime) {
    long ms = cycleTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    sp.edit().putLong("cycleTime", ms).apply();
    this.cycleTime = cycleTime;
  }

  public LocalDateTime getLastOpenTime() {
    long ms = sp.getLong("lastOpenTime", 0);
    if (ms == 0)
      lastOpenTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    else
      lastOpenTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneOffset.UTC);
    return lastOpenTime;
  }

  public void setLastOpenTime(LocalDateTime lastOpenTime) {
    long ms = lastOpenTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    sp.edit().putLong("lastOpenTime", ms).apply();
    this.lastOpenTime = lastOpenTime;
  }
}

