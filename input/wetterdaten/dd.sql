CREATE TABLE Wetterstation (
  S_ID int NOT NULL,
  Standort varchar(255) NOT NULL,
  Geo_Breite double precision DEFAULT NULL,
  Geo_Laenge double precision DEFAULT NULL,
  Hoehe int DEFAULT NULL,
  Betreiber varchar(255) DEFAULT NULL,
  PRIMARY KEY (S_ID)
);

CREATE TABLE Wettermessung (
  Stations_ID int NOT NULL,
  Datum date NOT NULL,
  Qualitaet int DEFAULT NULL,
  Min_5cm double precision DEFAULT NULL,
  Min_2m double precision DEFAULT NULL,
  Mittel_2m double precision DEFAULT NULL,
  Max_2m double precision DEFAULT NULL,
  Relative_Feuchte double precision DEFAULT NULL,
  Mittel_Windstaerke double precision DEFAULT NULL,
  Max_Windgeschwindigkeit double precision DEFAULT NULL,
  Sonnenscheindauer double precision DEFAULT NULL,
  Mittel_Bedeckungsgrad double precision DEFAULT NULL,
  Niederschlagshoehe double precision DEFAULT NULL,
  Mittel_Luftdruck double precision DEFAULT NULL,
  PRIMARY KEY (Stations_ID,Datum),
  CONSTRAINT Wettermessung_ibfk_1 FOREIGN KEY (Stations_ID) REFERENCES Wetterstation (S_ID)
);
