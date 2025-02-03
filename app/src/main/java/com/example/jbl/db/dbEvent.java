package com.example.jbl.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class dbEvent {
  private final SQLiteDatabase Connection;
  private final LinkedList<String> QueueLog = new LinkedList<String>();

  public dbEvent(SQLiteDatabase connection)
  {
    this.Connection = connection;
  }

  //private String _TableName = "";
  private int _Head = 0;
  private int _Tail = 0;

  public final boolean CreateTable()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLE IF NOT EXISTS EventLog (");
    sb.append("`stamp` datetime DEFAULT '2007-01-01 00:00:00',");
    sb.append("`periodeidx` int(10) DEFAULT '1',");
    sb.append("`type` int(10) NOT NULL,");
    sb.append("`category` int(10) NOT NULL,");
    sb.append("`data` varchar(512) NOT NULL,");
    sb.append("`sent` bit(1) DEFAULT 0,");
    sb.append("PRIMARY KEY (`stamp`)");
    sb.append(");");

    try
    {
      Connection.execSQL(sb.toString());
      Select();
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }

  public final void Select()
  {
    String sb = "select MIN(rowid), MAX(rowid) from EventLog where sent=0;";
    Cursor c = Connection.rawQuery(sb, null);
    if (c.moveToFirst()){
      _Tail = c.getInt(0) - 1;
      _Head = c.getInt(1) - 1;
    }
    else {
      _Tail = 0;
      _Head = 0;
    }
    c.close();
  }

  private void MarkSent(int Idx)
  {
    String sb = "update EventLog set sent=1 where rowid=" + String.valueOf(Idx);
    Connection.execSQL(sb);
  }

  private void InsertQueue(int PeriodeIdx, int Type, int Category, String Data)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("replace into EventLog");
    sb.append(" (stamp, periodeidx, type, category, data, sent) values ");
    sb.append(" (?, ?, ?, ?, ?, ?);");

    ContentValues values = new ContentValues();
    LocalDateTime localDateTime = LocalDateTime.now();
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //  localDateTime = LocalDateTime.now();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    values.put("stamp", localDateTime.format(dateFormatter));
    values.put("periodeidx", PeriodeIdx);
    values.put("type", Type);
    values.put("Category", Category);
    values.put("Data", Data);
    values.put("sent", 0);
    Connection.insert("users", null, values);
    //}
    //return true;
  }

  public final void Drop()
  {
    String cmdDrop = "drop table if exists EventLog";
    Connection.execSQL(cmdDrop);
  }

  public final void EventLogEnqueue(String data)
  {
    QueueLog.offer(data); // TCM Queue
  }

  /**
   Commit backup one event to MySql Database.
   */
  public final void EventLogEnqueueFlushOne()
  {
    if (QueueLog.isEmpty())
    {
      return;
    }
  }

  public final String EventLogDequeue()
  {
    if (QueueLog.isEmpty())
    {
      return null;
    }

    String dat = QueueLog.poll();
    MarkSent(_Tail);
    _Tail++;

    return dat;
  }

  public final String EventLogPeek()
  {
    if (QueueLog.isEmpty())
    {
      return null;
    }
    return QueueLog.peek();
  }

  public final int EventLogCount()
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
