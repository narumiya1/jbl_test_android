package com.example.jbl.db;

import java.time.LocalDateTime;

public class LogTransaction {
  public LocalDateTime Stamp = LocalDateTime.MIN;
  public int Type;
  public int Origin;
  public int TransactionNumber;
  public int StrukNumber;
  public String TicketNumber;
  public int NotranType;
  public boolean IsEtoll;
  public String TicketNumberAsal;
  public int PeriodeIdx;
  public String Nopol;
}
