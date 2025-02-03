package com.example.jbl.model;

public class TcmCommand {
  public static class TcmCommandOpen {
    public int id_pengawas;
    public int id_pultol;
    public int shift;
    public String nama_pengawas;
    public String nama_pultol;
  }
  public static class TcmCommandClose {
    public int id_pengawas;
    public int id_pultol;
  }
}
