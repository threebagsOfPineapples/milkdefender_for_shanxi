package com.magispec.shield.domain;

/**
 * Created by guofe on 2016/9/7 0007.
 */
public class Midcategory  {
    private String photourl;
    private String modelobjectdesc;
    private String modelid;
    private String modeltype;

    public Midcategory(String photourl, String modelobjectdesc, String modelid,String modeltype) {
        this.photourl = photourl;
        this.modelobjectdesc = modelobjectdesc;
        this.modelid = modelid;
        this.modeltype=modeltype;
    }
    public String getModeltype() {
        return modeltype;
    }
    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
    public void setModelobjectdesc(String modelobjectdesc) {
        this.modelobjectdesc = modelobjectdesc;
    }
    public void setModleid(String modelid) {
        this.modelid = modelid;
    }

    public String getModleid() {
        return modelid;
    }

    public String getPhotourl() {
        return photourl;
    }

    public String getModelobjectdesc() {
        return modelobjectdesc;
    }
}
