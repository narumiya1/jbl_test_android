package com.example.jbl;

import android.annotation.SuppressLint;
import android.database.sqlite.*;
import android.os.Environment;

import com.example.jbl.db.PeriodStatus;
import com.example.jbl.db.dbDinas;
import com.example.jbl.db.dbPerioda;
import com.example.jbl.db.dbTarif;
import com.example.jbl.db.dbTarifEntry;
import com.example.jbl.db.dbUser;
import com.example.jbl.model.DinasData;
import com.example.jbl.model.TarifRequest;
import com.example.jbl.model.UserData;


import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Perioda {
  private final SQLiteDatabase connMaster;
  private SQLiteDatabase connTrx;
  private dbTarif dbTrf;
  private dbPerioda dbPrd = null;
  private Prepaid prepaid;
  private final Config cfg = Config.getInstance();
  private static volatile Perioda instance;

  public static Perioda getInstance() {
    if (instance == null) {
      synchronized (Perioda.class) {
        if (instance == null) {
          instance = new Perioda();
        }
      }
    }
    return instance;
  }

  public Perioda() {
    File sdcard = Environment.getExternalStorageDirectory();
    File file = new File(sdcard, "/tbn/tctmaster.db");
    String fn = file.getPath();
    if (file.exists())
      connMaster = SQLiteDatabase.openDatabase(fn, null, SQLiteDatabase.OPEN_READWRITE);
    else
      connMaster = SQLiteDatabase.openOrCreateDatabase(fn, null);

    dbTrf = new dbTarif(connMaster);
    dbTarif.CreateTable(connMaster);
    //dbTarifEntry dte06 = dbTrf.GetTarif(1, "P06");
    //OpenPerioda(1,1,1,false);
    //prepaid.getBalance();
  }

  public void setPrepaidHandler(Prepaid p) {
    prepaid = p;
  }
  public void close() {
    if (connTrx.isOpen()) connTrx.close();
    if (connMaster.isOpen()) connMaster.close();
  }
  @SuppressLint("DefaultLocale")
  public final void OpenPerioda(int pas, int pul, int shift, boolean IsMtn) {
    int[] active = dbTarif.GetTarifCodeList(connMaster, 1, 0);
    if (active.length == 0)
      create_dummy_tarif();
    else
      dbTrf = dbTarif.LoadTable(connMaster, active[0]);

    LocalDateTime _OpenTime = LocalDateTime.now();
    LocalDateTime _ReportTime;
    LocalDateTime TodayCycleTime = LocalDateTime.of(_OpenTime.getYear(), _OpenTime.getMonthValue(), _OpenTime.getDayOfMonth(), 5, 0, 0);

    if (cfg.getLastOpenTime().isBefore(TodayCycleTime) && !TodayCycleTime.isAfter(_OpenTime))
    {
      if (IsMtn)
        cfg.setAppState(50);
      else
        cfg.setAppNoPerioda(0);
    }

    if (IsMtn)
    {
      cfg.setAppState(cfg.getAppState()+1);
      if (cfg.getAppState() > 63)
        cfg.setAppState(51);
      if (cfg.getAppState() < 51)
        cfg.setAppState(51);
    }
    else
    {
      cfg.setAppNoPerioda(cfg.getAppNoPerioda()+1);
      if (cfg.getAppNoPerioda() > 40)
        cfg.setAppNoPerioda(1);
    }
    if (_OpenTime.isBefore(TodayCycleTime))
      _ReportTime = _OpenTime.plusDays(-1);
    else
      _ReportTime = _OpenTime.withHour(0).withMinute(0).withSecond(0).withNano(0);

    // Create Perioda database
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
    String filename = String.format("GB%02d_%02d_%d_%02d_%s.db", cfg.getGerbangNumber(), cfg.getGarduID(), shift, cfg.getAppNoPerioda(), _OpenTime.format(formatter));
    File sdcard = Environment.getExternalStorageDirectory();
    File file = new File(sdcard, "/tbn/"+filename);
    String fn = file.getPath();
    if (file.exists())
      connTrx = SQLiteDatabase.openDatabase(fn, null, SQLiteDatabase.OPEN_READWRITE);
    else
      connTrx = SQLiteDatabase.openOrCreateDatabase(fn, null);

    dbPrd = new dbPerioda(connTrx);
    dbPrd.getCurrentHeader().GerbangCode = cfg.getGerbangNumber();
    dbPrd.getCurrentHeader().GarduID = cfg.getGarduID();
    dbPrd.getCurrentHeader().GarduCode = cfg.getGarduNumber();
    dbPrd.getCurrentHeader().GarduType = cfg.getApplicationMode();
    dbPrd.getCurrentHeader().NoResiBcOlAwal = cfg.getBcTicketNumberOL();
    dbPrd.getCurrentHeader().NoResiBcSuAwal = cfg.getBcTicketNumberSU();
    dbPrd.getCurrentHeader().CloseTimeStamp = LocalDateTime.MIN;
    dbPrd.getCurrentHeader().OpenTimeStamp = _OpenTime;

    if (IsMtn)
      dbPrd.getCurrentHeader().PeriodeNumber = cfg.getAppState();
    else
      // TO do perioda id
      dbPrd.getCurrentHeader().PeriodeNumber = cfg.getAppNoPerioda();

    dbPrd.getCurrentHeader().ReportingDate = _ReportTime;
    dbPrd.getCurrentHeader().DinasCounterCount = 0;
    dbPrd.getCurrentHeader().OverloadCounterCount = 0;
    dbPrd.getCurrentHeader().PengawasId = pas;
    dbPrd.getCurrentHeader().PultolId = pul;
    dbPrd.getCurrentHeader().Shift = shift;
    dbPrd.getCurrentHeader().TarifId = dbTrf.TarifCode;
    dbPrd.getCurrentHeader().Status = PeriodStatus.PER_open.getValue();

    // save open perioda
    if (dbPrd.Create())
      dbPrd.getCurrentHeader().Update();
  }

  public final void ClosePerioda()
  {
    cfg.setLastOpenTime(dbPrd.getCurrentHeader().OpenTimeStamp);
    dbPrd.getCurrentHeader().CloseTimeStamp = LocalDateTime.now();
    dbPrd.getCurrentHeader().Status = PeriodStatus.PER_close.getValue();
    dbPrd.getCurrentHeader().Update();
    dbPrd = null;
    connTrx.close();
  }

  public void setTarif(TarifRequest tarif) {
    // Boleh setting tarif jika sedang tutup perioda
    if (dbPrd != null) return;

    dbTrf = dbTarif.setTarif(connMaster, tarif);
  }

  private void create_dummy_tarif() {
    String[] AId = new String[] {"P02", "P06", "P11", "P12", "P13", "P14", "P16", "P17", "P18", "P19", "P20", "P21", "P22", "P23", "P24"};
    dbTrf.TarifCode = 100;
    dbTrf.TarifName = "Dummy Tariff";
    dbTrf.ValidityDate = LocalDateTime.now();
    dbTrf.Status = 1;
    dbTrf.Discount = 0; // Non discount
    dbTrf.InsertTarif();
    dbTrf.TarifEntry.clear();
    for (int k = 0; k < AId.length; k++)
    {
      for (int i = 0; i < 5; i++)
      {
        dbTarifEntry entry = new dbTarifEntry(connMaster);
        entry.Type = i + 1;
        entry.TarifCode = dbTrf.TarifCode;
        for (int j = 0; j < cfg.MaximumGerbang; j++)
        {
          if (k == 0 || k == 13 || k == 14)
            entry.Origin[j] = 0;
          else
            entry.Origin[j] = i + i + i + 3;
        }
        entry.IdBayar = AId[k];
        entry.InsertEntry();

        dbTrf.TarifEntry.add(entry);
      }
    }
  }

  public void setUser(List<UserData> result) {
    dbUser.setUser(connMaster, result);
  }

  public void setDinas(List<DinasData> result) {
    dbDinas.setDinas(connMaster, result);
  }
}
