package com.example.mesutgungor.androidlistview;


 class Haber {
     String haberresmi;
     String haberbasligi;
     String habertarihi;
     String haberinkategorisi;
     String haberinicerigi;
     String haberUrl;

     public Haber(String haberresmi, String haberinkategorisi, String haberinicerigi, String haberUrl, String habertarihi, String haberbasligi) {
         this.haberresmi = haberresmi;
         this.haberinkategorisi = haberinkategorisi;
         this.haberinicerigi = haberinicerigi;
         this.haberUrl = haberUrl;
         this.habertarihi = habertarihi;
         this.haberbasligi = haberbasligi;
     }

     public String getHaberresmi() {
         return haberresmi;
     }

     public void setHaberresmi(String haberresmi) {
         this.haberresmi = haberresmi;
     }

     public String getHaberUrl() {
         return haberUrl;
     }

     public void setHaberUrl(String haberUrl) {
         this.haberUrl = haberUrl;
     }

     public String getHaberinicerigi() {
         return haberinicerigi;
     }

     public void setHaberinicerigi(String haberinicerigi) {
         this.haberinicerigi = haberinicerigi;
     }

     public String getHaberinkategorisi() {
         return haberinkategorisi;
     }

     public void setHaberinkategorisi(String haberinkategorisi) {
         this.haberinkategorisi = haberinkategorisi;
     }

     public String getHabertarihi() {
         return habertarihi;
     }

     public void setHabertarihi(String habertarihi) {
         this.habertarihi = habertarihi;
     }

     public String getHaberbasligi() {
         return haberbasligi;
     }

     public void setHaberbasligi(String haberbasligi) {
         this.haberbasligi = haberbasligi;
     }
 }
