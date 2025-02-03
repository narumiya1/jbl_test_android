package com.example.jbl.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dbPeriodeHeader {
  public String PeriodeId;
  public int GarduType;
  public int GerbangCode;
  public int GarduCode;
  public int Shift;
  public int PeriodeNumber;
  public int PengawasId;
  public int PengawasCloseId;
  public int PultolId;
  public LocalDateTime OpenTimeStamp = LocalDateTime.MIN;
  public LocalDateTime CloseTimeStamp = LocalDateTime.MIN;
  public LocalDateTime ReportingDate = LocalDateTime.MIN;
  public int Status;
  public int TarifId;
  public int DiscountTarifId1;
  public int DiscountTarifId2;
  public int DiscountTarifId3;
  public int BatalCounterCount;
  public int BatalRupiahSummed;
  public int BatalSuCounterCount;
  public int BatalOlCounterCount;
  public int DeteksiCounterCount;
  public int ResetCounterCount;
  public int TransaksiCounterCount;
  public int NotranCounterCount;
  public int DeductPrepaidCounterCount;
  public int DeteksiLtnCounterCount;
  public int DeteksiLtnMekanikCounterCount;
  public int SelisihMekanikCounterCount;
  public int NoResiBcOlAwal;
  public int NoResiBcOlAkhir;
  public int NoResiBcSuAwal;
  public int NoResiBcSuAkhir;

  public int BatalGol_Cnt;
  public int BedaGol_Cnt;
  public int LTN_DetOBS_Cnt;
  public int LTN_DetLD_Cnt;
  public int LongOBS_Cnt;
  public int LongLD_Cnt;
  public int Notran_detimal_count;
  public int Notran_lain_lain_count;
  public int Notran_tdk_normal_count;
  public int Notran_vip_count;

  //public String LalinTableName;
  //public String AvcTableName;
  //public String EventLogTableName;
  //public String TransactionLogTableName;
  //public String PrepaidLogTableName;
  public int DinasCounterCount;
  public int OverloadCounterCount;
  public int GarduID;

  private static final String cmdCreatePeriodeTbl =
      "CREATE TABLE IF NOT EXISTS `tbl_periode` (" + "`PeriodeId` varchar(64) DEFAULT ''," + "`GarduType` int(10) DEFAULT '1'," + "`GerbangCode` int(10) DEFAULT '1'," + "`GarduCode` int(10) DEFAULT '1'," + "`Shift` int(10) DEFAULT '0'," + "`PeriodeNumber` int(10) DEFAULT '0'," + "`PengawasId` int(10) DEFAULT '0'," + "`PengawasCloseId` int(10) DEFAULT '0'," + "`PultolId` int(10) DEFAULT '0'," + "`OpenTimeStamp` datetime ," + "`CloseTimeStamp` datetime ," + "`ReportingDate` datetime ," + "`Status` int(10) DEFAULT '2'," + "`TarifId` int(10) DEFAULT '0'," + "`DiscountTarifId1` int(10) DEFAULT '0'," + "`DiscountTarifId2` int(10) DEFAULT '0'," + "`DiscountTarifId3` int(10) DEFAULT '0'," + "`BatalCounterCount` int(10) DEFAULT '0'," + "`BatalRupiahSummed` int(10) DEFAULT '0'," + "`BatalSuCounterCount` int(10) DEFAULT '0'," + "`BatalOlCounterCount` int(10) DEFAULT '0'," + "`DeteksiCounterCount` int(10) DEFAULT '0'," + "`ResetCounterCount` int(10) DEFAULT '0'," + "`TransaksiCounterCount` int(10) DEFAULT '0'," + "`NotranCounterCount` int(10) DEFAULT '0'," + "`DeductPrepaidCounterCount` int(10) DEFAULT '0'," + "`DeteksiLtnCounterCount` int(10) DEFAULT '0'," + "`DeteksiLtnMekanikCounterCount` int(10) DEFAULT '0'," + "`SelisihMekanikCounterCount` int(10) DEFAULT '0'," + "`NoResiBcOlAwal` int(10) DEFAULT '0'," + "`NoResiBcOlAkhir` int(10) DEFAULT '0'," + "`NoResiBcSuAwal` int(10) DEFAULT '0'," + "`NoResiBcSuAkhir` int(10) DEFAULT '0'," + "`BatalGol_Cnt` int(10) DEFAULT '0'," + "`BedaGol_Cnt` int(10) DEFAULT '0'," + "`LTN_DetOBS_Cnt` int(10) DEFAULT '0'," + "`LTN_DetLD_Cnt` int(10) DEFAULT '0'," + "`LongOBS_Cnt` int(10) DEFAULT '0'," + "`LongLD_Cnt` int(10) DEFAULT '0'," + "`Notran_detimal_count` int(10) DEFAULT '0'," + "`Notran_lain_lain_count` int(10) DEFAULT '0'," + "`Notran_tdk_normal_count` int(10) DEFAULT '0'," + "`Notran_vip_count` int(10) DEFAULT '0'," +
      //    "`LalinTableName` varchar(64) DEFAULT ''," + "`AvcTableName` varchar(64) DEFAULT ''," + "`EventLogTableName` varchar(64) DEFAULT ''," + "`TransactionLogTableName` varchar(64) DEFAULT ''," + "`PrepaidTableName` varchar(64) DEFAULT ''," +
      "`DinasCounterCount` int(10) default '0'," + "`OverloadCounterCount` int(10) default '0'," + "`GarduID` int(10) DEFAULT '1'," + "PRIMARY KEY (`PeriodeId`)" + ");";

  private static final String cmdInsertNewPeriode = "insert into tbl_periode (PeriodeId, Shift, PeriodeNumber, PengawasId, PultolId, OpenTimeStamp, ReportingDate) values (?, ?, ?, ?, ?, ?, ?)";
//  private String[] cmdInsertNewPeriodeParams = new String[] {"?PeriodeId", "?Shift", "?PeriodeNumber", "?PengawasId", "?PultolId", "?OpenTimeStamp", "?ReportingDate"};

  private String[] FieldNames = new String[] {"ROWID", "PeriodeId", "GarduType", "GerbangCode", "GarduCode", "Shift", "PeriodeNumber", "PengawasId", "PengawasCloseId", "PultolId", "OpenTimeStamp", "CloseTimeStamp", "ReportingDate", "Status", "TarifId", "DiscountTarifId1", "DiscountTarifId2", "DiscountTarifId3", "BatalCounterCount", "BatalRupiahSummed", "BatalSuCounterCount", "BatalOlCounterCount", "DeteksiCounterCount", "ResetCounterCount", "TransaksiCounterCount", "NotranCounterCount", "DeductPrepaidCounterCount", "DeteksiLtnCounterCount", "DeteksiLtnMekanikCounterCount", "SelisihMekanikCounterCount", "NoResiBcOlAwal", "NoResiBcOlAkhir", "NoResiBcSuAwal", "NoResiBcSuAkhir", "BatalGol_Cnt", "BedaGol_Cnt", "LTN_DetOBS_Cnt", "LTN_DetLD_Cnt", "LongOBS_Cnt", "LongLD_Cnt", "Notran_detimal_count", "Notran_lain_lain_count", "Notran_tdk_normal_count", "Notran_vip_count",
      //"LalinTableName", "AvcTableName", "EventLogTableName", "TransactionLogTableName", "PrepaidTableName",
      "DinasCounterCount", "OverloadCounterCount", "GarduID"};
  private SQLiteDatabase _Connection;
  private int SlotIdx = 0;
  public dbPeriodeHeader(SQLiteDatabase connection) {
    this._Connection = connection;
  }

  public final boolean CreateTable()
  {
    boolean result = true;
    try
    {
      _Connection.execSQL(cmdCreatePeriodeTbl);
    }
    catch (RuntimeException e)
    {
      result = false;
    }
    return result;
  }

  public final boolean CreatePeriode()
  {
    PeriodeId = GenerateID();
    boolean result = true;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    SQLiteStatement stmt = _Connection.compileStatement(cmdInsertNewPeriode);
    stmt.bindString(1, PeriodeId);
    stmt.bindLong(2, Shift);
    stmt.bindLong(3, PeriodeNumber);
    stmt.bindLong(4, PengawasId);
    stmt.bindLong(5, PultolId);
    stmt.bindString(6, OpenTimeStamp.format(formatter));
    stmt.bindString(7, ReportingDate.format(formatter));
    stmt.executeInsert();
    return result;
  }

  @SuppressLint("DefaultLocale")
  public final String GenerateID()
  {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
    String jam = OpenTimeStamp.format(formatter);
    return String.format("GB%02d-%02d-%d-%02d-%s", GerbangCode, GarduID, Shift, PeriodeNumber, jam);
  }
  // This is a note to sign this instance connected to spesific history

  public final void Select(int HistBackward)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("select ");
    for (int i = 0; i < FieldNames.length; i++)
    {
      if (i != 0)
      {
        sb.append(",");
      }
      sb.append(FieldNames[i]);
    }
    sb.append(" from tbl_periode where rowid=(select rowid from tbl_periode order by rowid desc limit 1 offset ");
    sb.append(String.valueOf(HistBackward));
    sb.append(")");
    try
    {
      Cursor cursor = _Connection.query("tbl_periode", FieldNames, null,null,null,null,null);
      if (cursor.moveToNext()) {
        SlotIdx = cursor.getInt(0);
        PeriodeId = cursor.getString(1);
        GarduType = cursor.getInt(2);
        GerbangCode = cursor.getInt(3);
        GarduCode = cursor.getInt(4);
        Shift = cursor.getInt(5);
        PeriodeNumber = cursor.getInt(6);
        PengawasId = cursor.getInt(7);
        PengawasCloseId = cursor.getInt(8);
        PultolId = cursor.getInt(9);
        try {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
          OpenTimeStamp = LocalDateTime.parse(cursor.getString(10), formatter);
          CloseTimeStamp = LocalDateTime.parse(cursor.getString(11), formatter);
          ReportingDate = LocalDateTime.parse(cursor.getString(12), formatter);
        }
        catch (Exception ignored) {}
        Status = cursor.getInt(13);
        TarifId = cursor.getInt(14);
        DiscountTarifId1 = cursor.getInt(15);
        DiscountTarifId2 = cursor.getInt(16);
        DiscountTarifId3 = cursor.getInt(17);
        BatalCounterCount = cursor.getInt(18);
        BatalRupiahSummed = cursor.getInt(19);
        BatalSuCounterCount = cursor.getInt(20);

        BatalOlCounterCount = cursor.getInt(21);
        DeteksiCounterCount = cursor.getInt(22);
        ResetCounterCount = cursor.getInt(23);
        TransaksiCounterCount = cursor.getInt(24);
        NotranCounterCount = cursor.getInt(25);
        DeductPrepaidCounterCount = cursor.getInt(26);
        DeteksiLtnCounterCount = cursor.getInt(27);
        DeteksiLtnMekanikCounterCount = cursor.getInt(28);
        SelisihMekanikCounterCount = cursor.getInt(29);
        NoResiBcOlAwal = cursor.getInt(30);
        NoResiBcOlAkhir = cursor.getInt(31);
        NoResiBcSuAwal = cursor.getInt(32);
        NoResiBcSuAkhir = cursor.getInt(33);
        BatalGol_Cnt = cursor.getInt(34);
        BedaGol_Cnt = cursor.getInt(35);
        LTN_DetOBS_Cnt = cursor.getInt(36);
        LTN_DetLD_Cnt = cursor.getInt(37);
        LongOBS_Cnt = cursor.getInt(38);
        LongLD_Cnt = cursor.getInt(39);
        Notran_detimal_count = cursor.getInt(40);
        Notran_lain_lain_count = cursor.getInt(41);
        Notran_tdk_normal_count = cursor.getInt(42);
        Notran_vip_count = cursor.getInt(43);
        DinasCounterCount = cursor.getInt(44);
        OverloadCounterCount = cursor.getInt(45);
        GarduID = cursor.getInt(46);
      }
      cursor.close();
    }
    catch (RuntimeException e)
    {
      SlotIdx = 0;
    }
  }

  public final void Update()
  {
    String sb = "update tbl_periode set " +
        "PeriodeId=@PeriodeId," +
        "GarduType=@GarduType," +
        "GerbangCode=@GerbangCode," +
        "GarduCode=@GarduCode," +
        "Shift=@Shift," +
        "PeriodeNumber=@PeriodeNumber," +
        "PengawasId=@PengawasId," +
        "PengawasCloseId=@PengawasCloseId," +
        "PultolId=@PultolId," +
        "OpenTimeStamp=@OpenTimeStamp," +
        "CloseTimeStamp=@CloseTimeStamp," +
        "ReportingDate=@ReportingDate," +
        "Status=@Status," +
        "TarifId=@TarifId," +
        "DiscountTarifId1=@DiscountTarifId1," +
        "DiscountTarifId2=@DiscountTarifId2," +
        "DiscountTarifId3=@DiscountTarifId3," +
        "BatalCounterCount=@BatalCounterCount," +
        "BatalRupiahSummed=@BatalRupiahSummed," +
        "BatalSuCounterCount=@BatalSuCounterCount," +
        "BatalOlCounterCount=@BatalOlCounterCount," +
        "DeteksiCounterCount=@DeteksiCounterCount," +
        "ResetCounterCount=@ResetCounterCount," +
        "TransaksiCounterCount=@TransaksiCounterCount," +
        "NotranCounterCount=@NotranCounterCount," +
        "DeductPrepaidCounterCount=@DeductPrepaidCounterCount," +
        "DeteksiLtnCounterCount=@DeteksiLtnCounterCount," +
        "DeteksiLtnMekanikCounterCount=@DeteksiLtnMekanikCounterCount," +
        "SelisihMekanikCounterCount=@SelisihMekanikCounterCount," +
        "NoResiBcOlAwal=@NoResiBcOlAwal," +
        "NoResiBcOlAkhir=@NoResiBcOlAkhir," +
        "NoResiBcSuAwal=@NoResiBcSuAwal," +
        "NoResiBcSuAkhir=@NoResiBcSuAkhir," +
        "BatalGol_Cnt=@BatalGol_Cnt," +
        "BedaGol_Cnt=@BedaGol_Cnt," +
        "LTN_DetOBS_Cnt=@LTN_DetOBS_Cnt," +
        "LTN_DetLD_Cnt=@LTN_DetLD_Cnt," +
        "LongOBS_Cnt=@LongOBS_Cnt," +
        "LongLD_Cnt=@LongLD_Cnt," +
        "Notran_detimal_count=@Notran_detimal_count," +
        "Notran_lain_lain_count=@Notran_lain_lain_count," +
        "Notran_tdk_normal_count=@Notran_tdk_normal_count," +
        "Notran_vip_count=@Notran_vip_count," +
//        "LalinTableName=@LalinTableName," +
//        "AvcTableName=@AvcTableName," +
//        "EventLogTableName=@EventLogTableName," +
//        "TransactionLogTableName=@TransactionLogTableName," +
//        "PrepaidTableName=@PrepaidTableName," +
        "DinasCounterCount=@DinasCounterCount," +
        "OverloadCounterCount=@OverloadCounterCount," +
        "GarduID=@GarduID" +
        " where rowid=" +
        String.valueOf(SlotIdx);

    try
    {
      ContentValues cmd = new ContentValues();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
      cmd.put("PeriodeId", PeriodeId);
      cmd.put("GarduType", GarduType);
      cmd.put("GerbangCode", GerbangCode);
      cmd.put("GarduCode", GarduCode);
      cmd.put("Shift", Shift);
      cmd.put("PeriodeNumber", PeriodeNumber);
      cmd.put("PengawasId", PengawasId);
      cmd.put("PengawasCloseId", PengawasCloseId);
      cmd.put("PultolId", PultolId);
      cmd.put("OpenTimeStamp", OpenTimeStamp.format(formatter));
      cmd.put("CloseTimeStamp", CloseTimeStamp.format(formatter));
      cmd.put("ReportingDate", ReportingDate.format(formatter));
      cmd.put("Status", Status);
      cmd.put("TarifId", TarifId);
      cmd.put("DiscountTarifId1", DiscountTarifId1);
      cmd.put("DiscountTarifId2", DiscountTarifId2);
      cmd.put("DiscountTarifId3", DiscountTarifId3);
      cmd.put("BatalCounterCount", BatalCounterCount);
      cmd.put("BatalRupiahSummed", BatalRupiahSummed);
      cmd.put("BatalSuCounterCount", BatalSuCounterCount);
      cmd.put("BatalOlCounterCount", BatalOlCounterCount);
      cmd.put("DeteksiCounterCount", DeteksiCounterCount);
      cmd.put("ResetCounterCount", ResetCounterCount);
      cmd.put("TransaksiCounterCount", TransaksiCounterCount);
      cmd.put("NotranCounterCount", NotranCounterCount);
      cmd.put("DeductPrepaidCounterCount", DeductPrepaidCounterCount);
      cmd.put("DeteksiLtnCounterCount", DeteksiLtnCounterCount);
      cmd.put("DeteksiLtnMekanikCounterCount", DeteksiLtnMekanikCounterCount);
      cmd.put("SelisihMekanikCounterCount", SelisihMekanikCounterCount);
      cmd.put("NoResiBcOlAwal", NoResiBcOlAwal);
      cmd.put("NoResiBcOlAkhir", NoResiBcOlAkhir);
      cmd.put("NoResiBcSuAwal", NoResiBcSuAwal);
      cmd.put("NoResiBcSuAkhir", NoResiBcSuAkhir);
      cmd.put("BatalGol_Cnt", BatalGol_Cnt);
      cmd.put("BedaGol_Cnt", BedaGol_Cnt);
      cmd.put("LTN_DetOBS_Cnt", LTN_DetOBS_Cnt);
      cmd.put("LTN_DetLD_Cnt", LTN_DetLD_Cnt);
      cmd.put("LongOBS_Cnt", LongOBS_Cnt);
      cmd.put("LongLD_Cnt", LongLD_Cnt);
      cmd.put("Notran_detimal_count", Notran_detimal_count);
      cmd.put("Notran_lain_lain_count", Notran_lain_lain_count);
      cmd.put("Notran_tdk_normal_count", Notran_tdk_normal_count);
      cmd.put("Notran_vip_count", Notran_vip_count);
//      cmd.put("LalinTableName", LalinTableName);
//      cmd.put("AvcTableName", AvcTableName);
//      cmd.put("EventLogTableName", EventLogTableName);
//      cmd.put("TransactionLogTableName", TransactionLogTableName);
//      cmd.put("PrepaidTableName", PrepaidLogTableName);
      cmd.put("DinasCounterCount", DinasCounterCount);
      cmd.put("OverloadCounterCount", OverloadCounterCount);
      cmd.put("GarduID", GarduID);

      _Connection.insert("tbl_perioda", null, cmd);
    }
    catch (RuntimeException ignored)
    {
    }
  }
}
