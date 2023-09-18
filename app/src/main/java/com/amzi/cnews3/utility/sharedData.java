package com.amzi.cnews3.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class sharedData {

    public static final String mySharedpref = "com.creative.rexwall.shared";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public sharedData() {

    }

    public sharedData(Context c) {
        pref = c.getSharedPreferences(mySharedpref, 0); // 0 - for private mode
        editor = pref.edit();
    }


    public void updateAutostart(String autostart) {
        editor.putString("Auto", autostart);
        editor.apply();

    }

    public String retrieveAutostart() {
        String id2 = pref.getString("Auto", null);
        if (id2 == null) {
            return "No";
        } else if (Objects.equals(id2, "Yes")) {
            return "Yes";

        } else if (Objects.equals(id2, "No")) {
            return "No";
        } else return "No";
    }

    public void updateLanguage(String Language) {
        editor.putString("Language", Language);
        editor.apply();

    }

    public void updateTheme(String Theme) {
        editor.putString("Theme", Theme);
        editor.apply();
    }

    public String retrieveTheme() {
        String id2 = pref.getString("Theme", null);
        if (id2 == null) {
            return "Day";
        } else if (Objects.equals(id2, "Night")) {
            return "Night";

        } else if (Objects.equals(id2, "Day")) {
            return "Day";

        } else return "Day";

    }

    public String retrieveLanguage() {
        String id2 = pref.getString("Language", null);
        if (id2 == null) {
            return "Eng";
        } else if (Objects.equals(id2, "Eng")) {
            return "Eng";

        } else if (Objects.equals(id2, "Hin")) {
            return "Hin";
        } else return "Eng";
    }


    public boolean getDownload(String docId) {
        docId = docId.concat("download");
        String tempdocId = pref.getString(docId, null);
        if (tempdocId == null) {
            return false;
        } else
            return true;
    }



    public void setDownload(String docId){
        docId = docId.concat("download");
        editor.putString(docId, docId);
        editor.commit();

    }

    public boolean getFavourites(String docId) {
        docId = docId.concat("favourites");
        String tempdocId = pref.getString(docId, null);
        if (tempdocId == null) {
            return false;
        } else
            return true;
    }



    public void setFavourites(String docId){
        docId = docId.concat("favourites");
        editor.putString(docId, docId);
        editor.commit();

    }

    public void removeFavourites(String docId){
        docId = docId.concat("favourites");
        editor.remove(docId);
        editor.commit();
    }

    public void removeDownloads(String docId){
        docId = docId.concat("download");
        editor.remove(docId);
        editor.commit();

    }

    public int increasecounter(){
        int mcounter = pref.getInt("counter",0);
        editor.putInt("counter",++mcounter);
        editor.commit();
        return mcounter;
    }

    public void resetcounter(){
        editor.putInt("counter",0);
        editor.commit();

    }


   //manager
   public String getLink() {
       String tempdocId = pref.getString("link", null);
       if (tempdocId == null) {
           return null;
       } else
           return tempdocId;
   }



    public void setLink(String docId){
        editor.putString("link", docId);
        editor.commit();

    }
    public int getVersion() {
        int version = pref.getInt("version", 0);
            return version;
    }



    public void setVersion(int version){
        editor.putInt("version", version);
        editor.commit();

    }
    public boolean getshowads() {
        String tempdocId = pref.getString("showads", null);
        if (tempdocId == null|| tempdocId.equals("no")) {
            return false;
        } else
            return true;
    }



    public void setshowads(String docId){
        editor.putString("showads", docId);
        editor.commit();

    }
    public boolean getavailable() {
        String tempdocId = pref.getString("available", null);
        if (tempdocId == null||tempdocId.equalsIgnoreCase("no")) {
            return false;
        } else
            return true;
    }



    public void setavailable(String docId){
        editor.putString("available", docId);
        editor.commit();

    }

}
