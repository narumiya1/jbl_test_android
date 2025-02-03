package com.example.jbl.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class dbPeriodeLalin {
  public int Type;
  public int[] Origin = new int[65];

  //private String _TableName = "";
  private int _Type = 0;
  private final SQLiteDatabase _Connection;

  private final String[] FieldNames = new String[] {"Type", "Origin1", "Origin2", "Origin3", "Origin4", "Origin5", "Origin6", "Origin7", "Origin8", "Origin9", "Origin10", "Origin11", "Origin12", "Origin13", "Origin14", "Origin15", "Origin16", "Origin17", "Origin18", "Origin19", "Origin20", "Origin21", "Origin22", "Origin23", "Origin24", "Origin25", "Origin26", "Origin27", "Origin28", "Origin29", "Origin30", "Origin31", "Origin32", "Origin33", "Origin34", "Origin35", "Origin36", "Origin37", "Origin38", "Origin39", "Origin40", "Origin41", "Origin42", "Origin43", "Origin44", "Origin45", "Origin46", "Origin47", "Origin48", "Origin49", "Origin50", "Origin51", "Origin52", "Origin53", "Origin54", "Origin55", "Origin56", "Origin57", "Origin58", "Origin59", "Origin60", "Origin61", "Origin62", "Origin63", "Origin64"};

  public dbPeriodeLalin(SQLiteDatabase Connection)
  {
    this._Connection = Connection;
  }

  private void updateorigin(int ParamIndex)
  {
    String cmdUpdate = "update lalin set " +
        FieldNames[ParamIndex] +
        "=" +
        "Origin" + String.valueOf(ParamIndex) +
        " where Type=" +
        String.valueOf(_Type) +
        ";";

    try
    {
      _Connection.execSQL(cmdUpdate);
    }
    catch (Exception ignored)
    {
    }

  }

  public static int[] GetLalinTypes(SQLiteDatabase Connection)
  {
    String cmdList = "select Type from lalin";
    List<Integer> result = new ArrayList<Integer>();

    try
    {
      Cursor c = Connection.rawQuery(cmdList, null);
      if (c.moveToFirst()) {
        do {
          result.add(c.getInt(1));
        } while (c.moveToNext());
      }
    }
    catch (Exception ignored)
    {
    }
    return result.stream().mapToInt(i -> i).toArray();
  }

  public static boolean CreateTable(SQLiteDatabase connection)
  {
    String cmbCreate = "create table if not exists lalin" +
        " (" +
        "`Type` int(10) DEFAULT '1'," +
        "`Origin1` int(10) DEFAULT '0'," +
        "`Origin2` int(10) DEFAULT '0'," +
        "`Origin3` int(10) DEFAULT '0'," +
        "`Origin4` int(10) DEFAULT '0'," +
        "`Origin5` int(10) DEFAULT '0'," +
        "`Origin6` int(10) DEFAULT '0'," +
        "`Origin7` int(10) DEFAULT '0'," +
        "`Origin8` int(10) DEFAULT '0'," +
        "`Origin9` int(10) DEFAULT '0'," +
        "`Origin10` int(10) DEFAULT '0'," +
        "`Origin11` int(10) DEFAULT '0'," +
        "`Origin12` int(10) DEFAULT '0'," +
        "`Origin13` int(10) DEFAULT '0'," +
        "`Origin14` int(10) DEFAULT '0'," +
        "`Origin15` int(10) DEFAULT '0'," +
        "`Origin16` int(10) DEFAULT '0'," +
        "`Origin17` int(10) DEFAULT '0'," +
        "`Origin18` int(10) DEFAULT '0'," +
        "`Origin19` int(10) DEFAULT '0'," +
        "`Origin20` int(10) DEFAULT '0'," +
        "`Origin21` int(10) DEFAULT '0'," +
        "`Origin22` int(10) DEFAULT '0'," +
        "`Origin23` int(10) DEFAULT '0'," +
        "`Origin24` int(10) DEFAULT '0'," +
        "`Origin25` int(10) DEFAULT '0'," +
        "`Origin26` int(10) DEFAULT '0'," +
        "`Origin27` int(10) DEFAULT '0'," +
        "`Origin28` int(10) DEFAULT '0'," +
        "`Origin29` int(10) DEFAULT '0'," +
        "`Origin30` int(10) DEFAULT '0'," +
        "`Origin31` int(10) DEFAULT '0'," +
        "`Origin32` int(10) DEFAULT '0'," +
        "`Origin33` int(10) DEFAULT '0'," +
        "`Origin34` int(10) DEFAULT '0'," +
        "`Origin35` int(10) DEFAULT '0'," +
        "`Origin36` int(10) DEFAULT '0'," +
        "`Origin37` int(10) DEFAULT '0'," +
        "`Origin38` int(10) DEFAULT '0'," +
        "`Origin39` int(10) DEFAULT '0'," +
        "`Origin40` int(10) DEFAULT '0'," +
        "`Origin41` int(10) DEFAULT '0'," +
        "`Origin42` int(10) DEFAULT '0'," +
        "`Origin43` int(10) DEFAULT '0'," +
        "`Origin44` int(10) DEFAULT '0'," +
        "`Origin45` int(10) DEFAULT '0'," +
        "`Origin46` int(10) DEFAULT '0'," +
        "`Origin47` int(10) DEFAULT '0'," +
        "`Origin48` int(10) DEFAULT '0'," +
        "`Origin49` int(10) DEFAULT '0'," +
        "`Origin50` int(10) DEFAULT '0'," +
        "`Origin51` int(10) DEFAULT '0'," +
        "`Origin52` int(10) DEFAULT '0'," +
        "`Origin53` int(10) DEFAULT '0'," +
        "`Origin54` int(10) DEFAULT '0'," +
        "`Origin55` int(10) DEFAULT '0'," +
        "`Origin56` int(10) DEFAULT '0'," +
        "`Origin57` int(10) DEFAULT '0'," +
        "`Origin58` int(10) DEFAULT '0'," +
        "`Origin59` int(10) DEFAULT '0'," +
        "`Origin60` int(10) DEFAULT '0'," +
        "`Origin61` int(10) DEFAULT '0'," +
        "`Origin62` int(10) DEFAULT '0'," +
        "`Origin63` int(10) DEFAULT '0'," +
        "`Origin64` int(10) DEFAULT '0'," +
        " PRIMARY KEY (`Type`)" +
        ");";
    boolean result = true;

    try
    {
      connection.execSQL(cmbCreate);
    }
    catch (Exception e)
    {
      result = false;
    }

    return result;
  }

  public static dbPeriodeLalin CreateTypeEntry(SQLiteDatabase Connection, int type)
  {
    String cmbInsert = "insert into lalin (Type) values (" + String.valueOf(type) + ");";
    dbPeriodeLalin prdl = new dbPeriodeLalin(Connection);
    try
    {
      Connection.execSQL(cmbInsert);
      prdl.Select(type);
    }
    catch (Exception e)
    {
    }
    return prdl;
  }

  public final void Select(int type)
  {
    String cmbSelect = "select " +
        "Type," +
        "Origin1," +
        "Origin2," +
        "Origin3," +
        "Origin4," +
        "Origin5," +
        "Origin6," +
        "Origin7," +
        "Origin8," +
        "Origin9," +
        "Origin10," +
        "Origin11," +
        "Origin12," +
        "Origin13," +
        "Origin14," +
        "Origin15," +
        "Origin16," +
        "Origin17," +
        "Origin18," +
        "Origin19," +
        "Origin20," +
        "Origin21," +
        "Origin22," +
        "Origin23," +
        "Origin24," +
        "Origin25," +
        "Origin26," +
        "Origin27," +
        "Origin28," +
        "Origin29," +
        "Origin30," +
        "Origin31," +
        "Origin32," +
        "Origin33," +
        "Origin34," +
        "Origin35," +
        "Origin36," +
        "Origin37," +
        "Origin38," +
        "Origin39," +
        "Origin40," +
        "Origin41," +
        "Origin42," +
        "Origin43," +
        "Origin44," +
        "Origin45," +
        "Origin46," +
        "Origin47," +
        "Origin48," +
        "Origin49," +
        "Origin50," +
        "Origin51," +
        "Origin52," +
        "Origin53," +
        "Origin54," +
        "Origin55," +
        "Origin56," +
        "Origin57," +
        "Origin58," +
        "Origin59," +
        "Origin60," +
        "Origin61," +
        "Origin62," +
        "Origin63," +
        "Origin64 " +
        "from lalin" +
        " where Type=" +
        String.valueOf(type) + ";";
    boolean result = true;
    Cursor cursor = _Connection.rawQuery(cmbSelect, null);
    if (cursor.moveToNext()) {
      Type = cursor.getInt(0);
      for (int i = 1; i < 65; i++)
      {
        Origin[i] = cursor.getInt(i);
      }
    }
  }

  public final boolean Drop()
  {
    boolean result = true;
    try
    {
      _Connection.execSQL("drop table if exists lalin");
    }
    catch (Exception e)
    {
      result = false;
    }

    return result;
  }
}
