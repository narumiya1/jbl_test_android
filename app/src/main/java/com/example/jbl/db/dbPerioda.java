package com.example.jbl.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class dbPerioda {
  private final SQLiteDatabase _Connection;
  private dbPeriodeHeader Header;
  private final ArrayList<dbPeriodeLalin> Lalin = new ArrayList<dbPeriodeLalin>();
  private dbTransaction Transaction;
  private dbEvent Event;
  private dbPrepaid Prepaid;
  public final dbPeriodeHeader getCurrentHeader()
  {
    return Header;
  }
  public final void setCurrentHeader(dbPeriodeHeader value)
  {
    Header = value;
  }
  public final dbPeriodeLalin[] getCurrentLalin()
  {
    return Lalin.toArray(new dbPeriodeLalin[0]);
  }

  public dbPerioda(SQLiteDatabase dbTrx) {
    _Connection = dbTrx;
    Header = new dbPeriodeHeader(_Connection);
  }

  public final boolean Create()
  {
    // Make sure tbl_periode exists
    if (!Header.CreateTable())
    {
      return false;
    }

    // Create the Header
    if (!Header.CreatePeriode())
    {
      return false;
    }

    Lalin.clear();

    // Create table PeriodeLalin
    if (!dbPeriodeLalin.CreateTable(_Connection))
    {
      return false;
    }

    // Create Log Transaction
    Transaction = new dbTransaction(_Connection);
    if (!Transaction.CreateTable())
    {
      return false;
    }

    // Create Log Event
    Event = new dbEvent(_Connection);
    if (!Event.CreateTable())
    {
      return false;
    }

    Prepaid = new dbPrepaid(_Connection);
    return Prepaid.CreateTable();
  }

  private dbPeriodeLalin CreateLalinTypeEntry(int _Type)
  {
    dbPeriodeLalin dbl = null;
    for (dbPeriodeLalin element : Lalin)
    {
      if (element.Type == _Type) {
        dbl = element;
        break;
      }
    }

    if (dbl == null)
    {
      dbl = dbPeriodeLalin.CreateTypeEntry(_Connection, _Type);
      Lalin.add(dbl);
    }
    return dbl;
  }

//  private int CheckedType = 0;
//  private boolean CheckExistsType(dbPeriodeLalin LalinTable)
//  {
//    return LalinTable != null && LalinTable.Type == CheckedType;
//  }

  public final void LoadDataPeriode(int Slot)
  {
    // Load header
    Header.Select(Slot);

    // Load lalins
    int[] types = dbPeriodeLalin.GetLalinTypes(_Connection);
    for (int i = 0; i < types.length; i++)
    {
      dbPeriodeLalin lalin = new dbPeriodeLalin(_Connection);
      lalin.Select(types[i]);
      Lalin.add(lalin);
    }
  }
//  public static void LimitPeriodeList(SQLiteDatabase Connection, int Count)
//  {
//    dbPeriodeHeader[] headers = ListAll(Connection);
//
//    if (headers.length <= Count)
//    {
//      return;
//    }
//
//    int deleted = headers.length - Count;
//    for (int i = 0; i < deleted; i++)
//    {
//      headers[headers.length - 1 - i].DropSetPeriode();
//    }
//  }

//  public final boolean Drop()
//  {
//    for (int i = 0; i < Lalin.size(); i++)
//    {
//      Lalin.get(i).Drop();
//    }
//    Transaction.Drop();
//    Event.Drop();
//    Header.Drop();
//    return true;
//  }

  public final void CurrentIncrementLalin(int Type, int origin)
  {
    dbPeriodeLalin dbl = CreateLalinTypeEntry(Type);
    dbl.Origin[origin]++;
  }

  public final void CurrentDecrementLalin(int Type, int Origin)
  {
    dbPeriodeLalin dbl = CreateLalinTypeEntry(Type);
    dbl.Origin[Origin]--;
  }

  public static dbPeriodeHeader[] ListAll(SQLiteDatabase Connection)
  {
    long Cnt = 0;
    try
    {
      Cursor dr = Connection.rawQuery("select count(*) from tbl_periode", null);
      if (dr.moveToFirst())
      {
        Cnt = dr.getLong(0);
      }
      dr.close();
    }
    catch (Exception e)
    {
      Cnt = 0;
    }

    ArrayList<dbPeriodeHeader> _prds = new ArrayList<dbPeriodeHeader>((int)Cnt);
    for (int i = 0; i < Cnt; i++)
    {
      dbPeriodeHeader prd = new dbPeriodeHeader(Connection);
      prd.Select(i);
      _prds.add(prd);
    }

    return _prds.toArray(new dbPeriodeHeader[0]);
  }

  public final String GenerateID()
  {
    return getCurrentHeader().GenerateID();
  }
  public final void PrepaidLogEnqueue(LogPrepaid data)
  {
    Prepaid.LogEnqueue(data);
  }

  public final LogPrepaid PrepaidLogDequeue()
  {
    return Prepaid.LogDequeue();
  }

  public final LogPrepaid PrepaidLogPeek()
  {
    return Prepaid.LogPeek();
  }

  public final int PrepaidLogCount()
  {
    return Prepaid.LogCount();
  }

  public final void PrepaidLogClear()
  {
    Prepaid.ClearQueue();
  }

  public static void SetPeriodeCloseTime(SQLiteDatabase Connection, String pid, LocalDateTime wkt)
  {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String sql = "UPDATE tbl_periode SET CloseTimeStamp='" + wkt.format(formatter) + "',status=1 WHERE PeriodeId='" + pid + "'";
    Connection.execSQL(sql);
  }
}
