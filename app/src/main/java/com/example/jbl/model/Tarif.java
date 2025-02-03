package com.example.jbl.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Tarif {
  public int TarifId;
  public LocalDateTime TglEfektif;
  public String NoKepres;
  public boolean Approve;
  public boolean IncludePpn;
  public int DiscStatus;
  // discMethod
  // 1 = by Time
  // 2 = by Shift
  public int DiscMethod;
  public LocalDateTime AwalDiscEToll;
  public LocalDateTime AkhirDiscEToll;
  public LocalDateTime AwalDisc;
  public LocalDateTime AkhirDisc;
  public LocalDateTime AwalPPn;
  public LocalDateTime AkhirPPn;
}
