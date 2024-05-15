package com.project.boatnavigation.model;

import java.io.Serializable;
import java.lang.String;

public class FishModel implements Serializable {
  private String name;

  private String lati;

  private String longi;

  private String type;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLati() {
    return this.lati;
  }

  public void setLati(String lati) {
    this.lati = lati;
  }

  public String getLongi() {
    return this.longi;
  }

  public void setLongi(String longi) {
    this.longi = longi;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
