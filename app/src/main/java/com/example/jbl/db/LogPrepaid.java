package com.example.jbl.db;

import java.time.LocalDateTime;

public class LogPrepaid {
  public LocalDateTime Tanggal = LocalDateTime.MIN;
  public String CardNumber;
  public int TransType;
  public int Origin;
  public String TerminalID;
  public long LastBalance;
  public String MAC;
  public int TransNo;
  public int Shift;
  public int PeriodNo;
  public int Tarif;
  public String Id;
  public int CardType;
  public int StrukNo;
  public boolean EventCode;
  public LocalDateTime DateReport = LocalDateTime.MIN;
  public int ETollNo;
  public String TarifID;
  public int GarduType;
  public int GarduId;
  public String IdBayar;
  public LocalDateTime WaktuEntrance = LocalDateTime.MIN;
  public int GarduAsal;
}
