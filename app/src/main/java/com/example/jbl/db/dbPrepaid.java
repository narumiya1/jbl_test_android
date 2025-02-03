package com.example.jbl.db;

import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class dbPrepaid {
  private final LinkedList<LogPrepaid> QueueLog = new LinkedList<LogPrepaid>();
  //private String _TableName = "";
  private final SQLiteDatabase _Connection;

  public dbPrepaid(SQLiteDatabase Connection) {
    this._Connection = Connection;
  }

  public final boolean CreateTable() {
    String cmbCreate = "CREATE TABLE IF NOT EXISTS PrepaidLog (" +
        "`Id` VARCHAR(30) DEFAULT NULL," +
        "`Wkt` DATETIME DEFAULT NULL," +
        "`DateReport` DATE DEFAULT NULL," +
        "`WaktuEntrance` DATETIME DEFAULT NULL," +
        "`GarduType` INT(3) DEFAULT NULL," +
        "`GarduId` INT(3) DEFAULT NULL," +
        "`GarduAsal` INT(3) DEFAULT NULL," +
        "`CardNumber` VARCHAR(16) DEFAULT NULL," +
        "`CardType` INT(3) DEFAULT NULL," +
        "`TransType` int(10) DEFAULT NULL," +
        "`TransNo` int(10) DEFAULT NULL," +
        "`StrukNo` int(10) DEFAULT NULL," +
        "`eTollNo` int(10) DEFAULT NULL," +
        "`TarifID` VARCHAR(10) DEFAULT NULL," +
        "`IdBayar` VARCHAR(3) DEFAULT NULL," +
        "`Tarif` int(10) DEFAULT NULL," +
        "`Origin` int(10) DEFAULT NULL," +
        "`TerminalID` VARCHAR(8) DEFAULT NULL," +
        "`LastBalance` int(10) DEFAULT NULL," +
        "`Shift` int(3) DEFAULT 0," +
        "`PeriodNo` int(3) DEFAULT 0," +
        "`MAC` VARCHAR(255) DEFAULT NULL," +
        "`Sent` bit(1) DEFAULT 0," +
        " PRIMARY KEY (`Id`, `Wkt`, `CardNumber`)" +
        ");";
    _Connection.execSQL(cmbCreate);
    return true;
  }

  public final void InsertData(String id, LocalDateTime wkt, String cardNum, int cardType, int trstyp, int trsno, int origin, String tid, long bal, String mac, int Shift, int PrdNo, int strukno, int tarif, int etollno, String tarifId, LocalDateTime dtrpt, int gid, int gdtyp, String idByr, LocalDateTime wktEntr, int gdAsal) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss.fff");
    String cmbInsert = "insert into PrepaidLog" +
        " (Id, StrukNo,eTollNo,Tarif,GarduId,GarduType,GarduAsal,Wkt,DateReport,WaktuEntrance,CardNumber, CardType,TransType,TransNo,Origin,TerminalID,TarifID,IdBayar,LastBalance,Shift,PeriodNo,MAC) values (" +
        "'" + id + "'," +
        "'" + String.valueOf(strukno) + "'," +
        "'" + String.valueOf(etollno) + "'," +
        "'" + String.valueOf(tarif) + "'," +
        "'" + String.valueOf(gid) + "'," +
        "'" + String.valueOf(gdtyp) + "'," +
        "'" + String.valueOf(gdAsal) + "'," +
        "'" + wkt.format(formatter) + "'," +
        "'" + dtrpt.format(formatter) + "'," +
        "'" + wktEntr.format(formatter) + "'," +
        "'" + cardNum + "'," +
        "'" + cardType + "'," +
        "'" + String.valueOf(trstyp) + "'," +
        "'" + String.valueOf(trsno) + "'," +
        "'" + String.valueOf(origin) + "'," +
        "'" + tid + "'," +
        "'" + tarifId + "'," +
        "'" + idByr + "'," +
        "'" + String.valueOf(bal) + "'," +
        "'" + String.valueOf(Shift) + "'," +
        "'" + String.valueOf(PrdNo) + "'," +
        "'" + mac + "'" +
        ")";
    _Connection.execSQL(cmbInsert);
  }

  public final boolean Drop() {
    boolean result = true;
    try { _Connection.execSQL("drop table if exists PrepaidLog"); }
    catch(Exception ign) { result = false; }
    return result;
  }

  public final void LogEnqueue(LogPrepaid data) {
    InsertData(data.Id, data.Tanggal, data.CardNumber, data.CardType, data.TransType, data.TransNo, data.Origin, data.TerminalID, data.LastBalance, data.MAC, data.Shift, data.PeriodNo, data.StrukNo, data.Tarif, data.ETollNo, data.TarifID, data.DateReport, data.GarduId, data.GarduType, data.IdBayar, data.WaktuEntrance, data.GarduAsal);
  }

  public final LogPrepaid LogDequeue() {
    if (QueueLog.isEmpty()) {
      return null;
    }

    LogPrepaid data = QueueLog.poll();

    MarkSent(data.MAC);

    return data;
  }

  private void MarkSent(String mac)
  {
    _Connection.execSQL("update PrepaidLog set sent=1 where mac='"+mac.strip()+"'");
  }
  public final LogPrepaid LogPeek()
  {
    if (QueueLog.isEmpty())
    {
      return null;
    }
    return QueueLog.peek();
  }

  public final int LogCount()
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
