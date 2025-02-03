package com.example.jbl.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class dbTransaction {
  private SQLiteDatabase _Connection;
  private LinkedList<LogTransaction> QueueLog = new LinkedList<LogTransaction>();
  private String _TableName = "";
  private int _Head = 0;
  private int _Tail = 0;

  public dbTransaction(SQLiteDatabase Connection)
  {
    this._Connection = Connection;
  }

  public final boolean CreateTable()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLE IF NOT EXISTS TransLog (");
    sb.append("`stamp` datetime DEFAULT '2007-01-01 00:00:00',");
    sb.append("`periodeidx` int(10) DEFAULT '1',");
    sb.append("`transactionnumber` int(10) DEFAULT '0',");
    sb.append("`origincode` int(10) DEFAULT '0',");
    sb.append("`type` int(10) DEFAULT '1',");
    sb.append("`struknumber` int(10) DEFAULT '1',");
    sb.append("`ticketnumber` varchar(16) DEFAULT '0',");
    sb.append("`ticketasal` varchar(16) DEFAULT '0',");
    sb.append("`type_notran` int(2) DEFAULT '0',");
    sb.append("`validity` bit(1) DEFAULT 1,");
    sb.append("`sent` bit(1) DEFAULT 0,");
    sb.append("PRIMARY KEY (`stamp`)");
    sb.append(");");
    boolean result = true;

    try
    {
      _Connection.execSQL(sb.toString());
      Select();
    }
    catch (Exception e)
    {
      result = false;
    }

    return result;
  }

  public final void Select()
  {
    String sb = "select MIN(rowid), MAX(rowid) from TransLog where sent=0;";
    try
    {
      Cursor dr = _Connection.rawQuery(sb, null);
      if (dr.moveToFirst())
      {
        _Tail = dr.getInt(0) - 1;
        _Head = dr.getInt(1) - 1;
      }
      else
      {
        _Head = 0;
        _Tail = 0;
      }
    }
    catch (Exception ignored)
    {
    }
  }

  private void MarkSent(int Idx)
  {
    String sb = "update TransLog set sent=1 where rowid=" + String.valueOf(Idx);
    _Connection.execSQL(sb);
  }

  private void InsertQueue(int PeriodeIdx, int TransactionNumber, int Origin, int Type, int Struk, String Ticket, int type_notran, String TicketAsal, LocalDateTime stamp) {
    String sb = "insert into TransLog" +
        " (stamp, periodeidx, transactionnumber, origincode, type, struknumber, ticketnumber, ticketasal, validity, sent, type_notran) values " +
        " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    SQLiteStatement stmt = _Connection.compileStatement(sb);
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
      stmt.bindString(1, stamp.format(formatter));
      stmt.bindLong(2, PeriodeIdx);
      stmt.bindLong(3, TransactionNumber);
      stmt.bindLong(4, Origin);
      stmt.bindLong(5, Type);
      stmt.bindLong(6, Struk);
      stmt.bindString(7, Ticket);
      stmt.bindString(8, TicketAsal);
      stmt.bindLong(9, 1);
      stmt.bindLong(10, 0);
      stmt.bindLong(11, type_notran);
      stmt.executeInsert();
    } catch (Exception ignored) {
    }
  }

  public final void Drop()
  {
    _Connection.execSQL("drop table if exists TransLog");
  }

  public final void TransactionLogEnqueue(LogTransaction data)
  {
    InsertQueue(data.PeriodeIdx, data.TransactionNumber, data.Origin, data.Type, data.StrukNumber, data.TicketNumber, data.NotranType, data.TicketNumberAsal, data.Stamp);
    QueueLog.offer(data);
  }

  public final LogTransaction TransactionLogDequeue()
  {
    if (QueueLog.isEmpty())
    {
      return null;
    }

    LogTransaction data = QueueLog.poll();

    MarkSent(_Tail);
    _Tail++;

    return data;
  }

  public final LogTransaction TransactionLogPeek()
  {
    if (QueueLog.isEmpty())
    {
      return null;
    }
    return QueueLog.peek();
  }

  public final int TransactionLogCount()
  {
    return QueueLog.size();
  }

  public final void ClearQueue()
  {
    if (QueueLog.isEmpty())
    {
      return;
    }
    QueueLog.clear();
  }
}
