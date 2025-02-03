package com.example.jbl.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.jbl.Config;


public class dbTarifEntry {
  public int TarifCode;
  public int Type;
  public String IdBayar;
  public int[] Origin = new int[65];

  private Config cfg = Config.getInstance();
  private SQLiteDatabase Connection;

  public dbTarifEntry(SQLiteDatabase connection) {
    Connection = connection;
  }

  public final boolean InsertEntry()
  {
    if (IsExists(TarifCode, Type, IdBayar))
    {
      return false;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("insert into tbl_tarif_detail ");
    sb.append("(TarifCode,Type,IdBayar,Origin0");
    for (int i = 1; i < cfg.MaximumGerbang; i++)
    {
      sb.append(",Origin").append(String.valueOf(i));
    }
    sb.append(")values (?,?,?,?");

    for (int i = 1; i < cfg.MaximumGerbang; i++)
    {
      sb.append(",@Origin").append(String.valueOf(i));
    }
    sb.append(");");

    boolean result = true;
    SQLiteStatement stmt = Connection.compileStatement(sb.toString());
    stmt.bindLong(1, TarifCode);
    stmt.bindLong(2, Type);
    stmt.bindString(3, IdBayar);
    for (int i = 0; i < cfg.MaximumGerbang; i++)
    {
      stmt.bindLong(i+4, Origin[i]);;
    }
    stmt.executeInsert();

    return result;
  }

  public boolean IsExists(int TarifCode, int Type, String bayar)
  {
    String chk = "select count(Type) from tbl_tarif_detail where TarifCode=" +
        String.valueOf(TarifCode) +
        " and Type=" +
        String.valueOf(Type) +
        " and IdBayar='" + bayar.strip() + "';";

    int cnt = 0;
    try
    {
      Cursor c = Connection.rawQuery(chk, null);
      if (c.moveToFirst()) {
        cnt = c.getInt(1);
      }
    }
    catch (Exception ignored)
    {
    }

    return cnt != 0;
  }
}
