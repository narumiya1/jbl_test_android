package com.example.jbl.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EMoneyData {
  public int GerbangId;
  public int GarduId;
  public int GarduType;
  public int GarduAsal;
  public LocalDateTime Tanggal;
  public LocalDateTime DateReport;
  public LocalDateTime WaktuEntrance;
  public String Id;
  public String CardNumber;
  public int CardType;
  public int TransType;
  public int TransNo;
  public int Origin;
  public String TerminalID;
  public long LastBalance;
  public String MAC;
  public int Shift;
  public int PeriodNo;
  public int Tarif;
  public int StrukNo;
  public int eTollNo;
  public String TarifId;
  public String IdBayar;

  public String toJson() {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
      @Override
      public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonContext) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
      }
    }).create();
    return gson.toJson(this);
  }
}
