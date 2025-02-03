package com.example.jbl.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.jbl.Config;
import com.example.jbl.model.TarifBarrier;
import com.example.jbl.model.TarifRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class dbTarif {
  private static Config cfg = Config.getInstance();
  public int TarifCode;
  public String TarifName;
  public LocalDateTime ValidityDate;
  public int DiscountMethod;
  public int Status;
  public ArrayList<dbTarifEntry> TarifEntry = new ArrayList<>();
  private SQLiteDatabase db;
  public int Discount;

  public dbTarif(SQLiteDatabase dbMaster) {
    db = dbMaster;
    TarifEntry.clear();
  }

  public static void CreateTable(SQLiteDatabase db) {
    String sb = "CREATE TABLE IF NOT EXISTS `tbl_tarif` (" +
        "`TarifCode` int(10) NOT NULL," +
        "`TarifName` varchar(64) DEFAULT 'SK'," +
        "`ValidityDate` datetime DEFAULT '2001-01-01 00:00:00'," +
        "`Status` int(10) DEFAULT '1'," +
        "`Discount` int(10) DEFAULT '0'," +
        "PRIMARY KEY (`TarifCode`)" +
        ");";

    db.execSQL(sb);
    db.execSQL(DefTarifDetail("tbl_tarif_detail"));
  }

  private static String DefTarifDetail(String tblName)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLE IF NOT EXISTS `").append(tblName.strip()).append("` (");
    sb.append("`TarifCode` int(10) NOT NULL,");
    sb.append("`Type` int(10) DEFAULT '1',");
    sb.append("`IdBayar` VarChar(3),");
    for (int i = 0; i < cfg.MaximumGerbang; i++)
    {
      sb.append("`Origin").append(String.valueOf(i)).append("` int(10) DEFAULT '0',");
    }
    sb.append("PRIMARY KEY (`TarifCode`,`Type`,`IdBayar`)");
    sb.append(");");

    return sb.toString();
  }

  public static int[] GetTarifCodeList(SQLiteDatabase Connection, int StatusA, int DiscountA)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("select TarifCode from tbl_tarif");
    if (StatusA != 999999 || DiscountA != 999999)
    {
      sb.append(" where ");
    }
    if (StatusA != 999999)
    {
      sb.append(" Status=");
      sb.append(String.valueOf(StatusA));
      sb.append(" and ");
    }
    if (DiscountA != 999999)
    {
      sb.append(" Discount=");
      sb.append(String.valueOf(DiscountA));
    }
    sb.append(" order by ValidityDate");
    sb.append(";");

    ArrayList<Integer> TarifCodeList = new ArrayList<Integer>();
    try
    {
      Cursor c = Connection.rawQuery(sb.toString(), null);
      if (c.moveToFirst()) {
        do {
          TarifCodeList.add(c.getInt(0));
        } while (c.moveToNext());
      }
    }
    catch (Exception ignored)
    {
    }
    return TarifCodeList.stream().mapToInt(i -> i).toArray();
  }

  public static dbTarif LoadTable(SQLiteDatabase connection, int TarifCode)
  {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    StringBuilder sb = new StringBuilder();
    sb.append("select TarifName, ValidityDate, Status, Discount ");
    sb.append("from tbl_tarif where TarifCode=");
    sb.append(String.valueOf(TarifCode));
    sb.append(" and Status=1;");

    dbTarif dbTr = null;
    Cursor c = connection.rawQuery(sb.toString(), null);
    if (c.moveToFirst())
    {
      dbTr = new dbTarif(connection);
      dbTr.TarifCode = TarifCode;
      dbTr.TarifName = c.getString(0);
      dbTr.ValidityDate = LocalDateTime.parse(c.getString(1), formatter);
      dbTr.Status = c.getInt(2);
      dbTr.Discount = c.getInt(3);
    }
    c.close();

    sb.setLength(0);
    sb.ensureCapacity(sb.length());
    sb.append("select Origin0");
    for (int i = 1; i < cfg.MaximumGerbang; i++)
    {
      sb.append(",Origin").append(String.valueOf(i));
    }
    sb.append(",IdBayar,Type from tbl_tarif_detail where TarifCode=");
    sb.append(String.valueOf(TarifCode));

    dbTarifEntry te = null;

    c = connection.rawQuery(sb.toString(), null);
    if (c.moveToFirst())
    {
      do {
        te = new dbTarifEntry(connection);
        te.TarifCode = TarifCode;
        te.Type = c.getInt(66);
        te.IdBayar = c.getString(65);
        for (int i = 0; i < cfg.MaximumGerbang; i++)
        {
          te.Origin[i] = c.getInt(i);
        }
        assert dbTr != null;
        dbTr.TarifEntry.add(te);
      } while (c.moveToNext());
    }
    c.close();

//    te = new dbTarifEntry(connection);
//    te.TarifCode = TarifCode;
//    te.Type = c.getInt(66);
//    te.IdBayar = c.getString(65);
//    for (int i = 0; i < cfg.MaximumGerbang; i++)
//    {
//      te.Origin[i] = c.getInt(i);
//    }
//    assert dbTr != null;
//    dbTr.TarifEntry.add(te);

    return dbTr;
  }

  public static dbTarif setTarif(SQLiteDatabase conn, TarifRequest tarif) {
    dbTarif dbTrf = new dbTarif(conn);
    dbTrf.TarifCode = tarif.Tarif.TarifId;
    dbTrf.ValidityDate = tarif.Tarif.TglEfektif;
    dbTrf.DiscountMethod = tarif.Tarif.DiscMethod;
    dbTrf.Discount = tarif.Tarif.DiscStatus;
    if (dbTrf.ValidityDate.isAfter(LocalDateTime.now()))
    {
      dbTrf.Status = 1;
    }
    else
    {
      dbTrf.Status = 0;
    }

    if (dbTrf.Status == 1)
      conn.execSQL("update tbl_tarif set Status=0 where Status=1;");
    conn.execSQL("delete from tbl_tarif where TarifCode="+String.valueOf(dbTrf.TarifCode));
    String sql = "insert into tbl_tarif (TarifCode,TarifName,ValidityDate,Status,Discount) values (?,?,?,?,?);";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    SQLiteStatement stmt = conn.compileStatement(sql);
    stmt.bindLong(1, tarif.Tarif.TarifId);
    stmt.bindString(2, tarif.Tarif.NoKepres);
    stmt.bindString(3, tarif.Tarif.TglEfektif.format(formatter));
    stmt.bindLong(4, dbTrf.Status);
    stmt.bindLong(5, tarif.Tarif.DiscStatus);
    //stmt.bindLong(5, dbTrf.DiscountMethod);
    stmt.executeInsert();

    conn.execSQL("DELETE FROM tbl_tarif_detail WHERE tarifcode=@code and type=@_type and idbayar=@_bayar;");
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO tbl_tarif_detail (tarifcode, type, idbayar");

    for (short i = 0; i < cfg.MaximumGerbang; i++) {
      sb.append(",origin").append(String.valueOf(i));
    }
    sb.append(") VALUES (?,?,?");
    for (short i = 0; i < cfg.MaximumGerbang; i++) {
      sb.append(",?");
    }
    sb.append(")");
    stmt = conn.compileStatement(sb.toString());
    dbTrf.TarifEntry.clear();
    for (TarifBarrier data: tarif.TarifBarrier) {
      dbTarifEntry entry = new dbTarifEntry(conn);
      entry.TarifCode = Integer.parseInt(data.TarifId);
      entry.Type = Integer.parseInt(data.IdGolongan);
      entry.IdBayar = data.IdPembayaran;
      entry.Origin[0] = data.Gb01;
      entry.Origin[1] = data.Gb02;
      entry.Origin[2] = data.Gb03;
      entry.Origin[3] = data.Gb04;
      entry.Origin[4] = data.Gb05;
      entry.Origin[5] = data.Gb06;
      entry.Origin[6] = data.Gb07;
      entry.Origin[7] = data.Gb08;
      entry.Origin[8] = data.Gb09;
      entry.Origin[9] = data.Gb10;
      entry.Origin[10] = data.Gb11;
      entry.Origin[11] = data.Gb12;
      entry.Origin[12] = data.Gb13;
      entry.Origin[13] = data.Gb14;
      entry.Origin[14] = data.Gb15;
      entry.Origin[15] = data.Gb16;
      entry.Origin[16] = data.Gb17;
      entry.Origin[17] = data.Gb18;
      entry.Origin[18] = data.Gb19;
      entry.Origin[19] = data.Gb20;
      entry.Origin[20] = data.Gb21;
      entry.Origin[21] = data.Gb22;
      entry.Origin[22] = data.Gb23;
      entry.Origin[23] = data.Gb24;
      entry.Origin[24] = data.Gb25;
      entry.Origin[25] = data.Gb26;
      entry.Origin[26] = data.Gb27;
      entry.Origin[27] = data.Gb28;
      entry.Origin[28] = data.Gb29;
      entry.Origin[29] = data.Gb30;
      entry.Origin[30] = data.Gb31;
      entry.Origin[31] = data.Gb32;
      entry.Origin[32] = data.Gb33;
      entry.Origin[33] = data.Gb34;
      entry.Origin[34] = data.Gb35;
      entry.Origin[35] = data.Gb36;
      entry.Origin[36] = data.Gb37;
      entry.Origin[37] = data.Gb38;
      entry.Origin[38] = data.Gb39;
      entry.Origin[39] = data.Gb40;
      entry.Origin[40] = data.Gb41;
      entry.Origin[41] = data.Gb42;
      entry.Origin[42] = data.Gb43;
      entry.Origin[43] = data.Gb44;
      entry.Origin[44] = data.Gb45;
      entry.Origin[45] = data.Gb46;
      entry.Origin[46] = data.Gb47;
      entry.Origin[47] = data.Gb48;
      entry.Origin[48] = data.Gb49;
      entry.Origin[49] = data.Gb50;
      entry.Origin[50] = data.Gb51;
      entry.Origin[51] = data.Gb52;
      entry.Origin[52] = data.Gb53;
      entry.Origin[53] = data.Gb54;
      entry.Origin[54] = data.Gb55;
      entry.Origin[55] = data.Gb56;
      entry.Origin[56] = data.Gb57;
      entry.Origin[57] = data.Gb58;
      entry.Origin[58] = data.Gb59;
      entry.Origin[59] = data.Gb60;
      entry.Origin[60] = data.Gb61;
      entry.Origin[61] = data.Gb62;
      entry.Origin[62] = data.Gb63;
      entry.Origin[63] = data.Gb64;
      dbTrf.TarifEntry.add(entry);

      stmt.bindString(1, data.TarifId);
      stmt.bindString(2, data.IdGolongan);
      stmt.bindString(3, data.IdPembayaran);
      stmt.bindLong(4, 0);
      stmt.bindLong(5, data.Gb01);
      stmt.bindLong(6, data.Gb02);
      stmt.bindLong(7, data.Gb03);
      stmt.bindLong(8, data.Gb04);
      stmt.bindLong(9, data.Gb05);
      stmt.bindLong(10, data.Gb06);
      stmt.bindLong(11, data.Gb07);
      stmt.bindLong(12, data.Gb08);
      stmt.bindLong(13, data.Gb09);
      stmt.bindLong(14, data.Gb10);
      stmt.bindLong(15, data.Gb11);
      stmt.bindLong(16, data.Gb12);
      stmt.bindLong(17, data.Gb13);
      stmt.bindLong(18, data.Gb14);
      stmt.bindLong(19, data.Gb15);
      stmt.bindLong(20, data.Gb16);
      stmt.bindLong(21, data.Gb17);
      stmt.bindLong(22, data.Gb18);
      stmt.bindLong(23, data.Gb19);
      stmt.bindLong(24, data.Gb20);
      stmt.bindLong(25, data.Gb21);
      stmt.bindLong(26, data.Gb22);
      stmt.bindLong(27, data.Gb23);
      stmt.bindLong(28, data.Gb24);
      stmt.bindLong(29, data.Gb25);
      stmt.bindLong(30, data.Gb26);
      stmt.bindLong(31, data.Gb27);
      stmt.bindLong(32, data.Gb28);
      stmt.bindLong(33, data.Gb29);
      stmt.bindLong(34, data.Gb30);
      stmt.bindLong(35, data.Gb31);
      stmt.bindLong(36, data.Gb32);
      stmt.bindLong(37, data.Gb33);
      stmt.bindLong(38, data.Gb34);
      stmt.bindLong(39, data.Gb35);
      stmt.bindLong(40, data.Gb36);
      stmt.bindLong(41, data.Gb37);
      stmt.bindLong(42, data.Gb38);
      stmt.bindLong(43, data.Gb39);
      stmt.bindLong(44, data.Gb40);
      stmt.bindLong(45, data.Gb41);
      stmt.bindLong(46, data.Gb42);
      stmt.bindLong(47, data.Gb43);
      stmt.bindLong(48, data.Gb44);
      stmt.bindLong(49, data.Gb45);
      stmt.bindLong(50, data.Gb46);
      stmt.bindLong(51, data.Gb47);
      stmt.bindLong(52, data.Gb48);
      stmt.bindLong(53, data.Gb49);
      stmt.bindLong(54, data.Gb50);
      stmt.bindLong(55, data.Gb51);
      stmt.bindLong(56, data.Gb52);
      stmt.bindLong(57, data.Gb53);
      stmt.bindLong(58, data.Gb54);
      stmt.bindLong(59, data.Gb55);
      stmt.bindLong(60, data.Gb56);
      stmt.bindLong(61, data.Gb57);
      stmt.bindLong(62, data.Gb58);
      stmt.bindLong(63, data.Gb59);
      stmt.bindLong(64, data.Gb60);
      stmt.bindLong(65, data.Gb61);
      stmt.bindLong(66, data.Gb62);
      stmt.bindLong(67, data.Gb63);
      stmt.bindLong(68, data.Gb64);
      stmt.executeInsert();
    }
    return dbTrf;
  }

  public dbTarifEntry GetTarif(int gol, String idbyr) {
    for (dbTarifEntry entry : TarifEntry)
    {
      if (entry.Type == gol && Objects.equals(entry.IdBayar, idbyr))
      {
        // Kode Origin untuk Cikupa = 1
        return entry;
      }
    }
    return null;
  }

  public void InsertTarif() {
    // If this is to be the next active, must Inactivate the previous
    if (Status == 1)
    {
      int[] codes = GetTarifCodeList(db, 1, Discount);
      for (int i = 0; i < codes.length; i++)
      {
        DeactivateTarif(db, codes[i]);
      }
    }

    // Make sure this is the only one table with this Code
    DeleteTarif(db, TarifCode);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String sb = "insert into tbl_tarif " +
        "(TarifCode,TarifName,ValidityDate,Status,Discount) values (?,?,?,?,?);";
    SQLiteStatement stmt = db.compileStatement(sb);
    stmt.bindLong(1, TarifCode);
    stmt.bindString(2, TarifName);
    stmt.bindString(3, ValidityDate.format(formatter));
    stmt.bindLong(4, Status);
    stmt.bindLong(5, Discount);
    stmt.executeInsert();

    // Insert all entries
    for (int i = 0; i < TarifEntry.size(); i++)
    {
      TarifEntry.get(i).TarifCode = TarifCode;
      TarifEntry.get(i).InsertEntry();
    }
    stmt.close();
  }

  public void DeleteTarif(SQLiteDatabase Connection, int TarifCode)
  {
    String sb = "delete from tbl_tarif where TarifCode=" + String.valueOf(TarifCode);
    Connection.execSQL(sb);

    sb = "delete from tbl_tarif_detail where TarifCode=" +String.valueOf(TarifCode);
    Connection.execSQL(sb);
  }

  public void DeactivateTarif(SQLiteDatabase Connection, int TarifCode)
  {
    String sb = "update tbl_tarif set Status=0 where TarifCode=" + String.valueOf(TarifCode);
    Connection.execSQL(sb);
  }
}
