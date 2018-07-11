package com.souq.hp.souq.task1.model;

public class ModelOne {
    private String id;
    private String titleEN;
    private String titleAR;
    private String photo;
    private String productCount;
    private String haveModel;

    public ModelOne(String id, String titleEN, String titleAR, String photo, String productCount, String haveModel) {
        this.id = id;
        this.titleEN = titleEN;
        this.titleAR = titleAR;
        this.photo = photo;
        this.productCount = productCount;
        this.haveModel = haveModel;
    }

    public ModelOne() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getTitleAR() {
        return titleAR;
    }

    public void setTitleAR(String titleAR) {
        this.titleAR = titleAR;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getHaveModel() {
        return haveModel;
    }

    public void setHaveModel(String haveModel) {
        this.haveModel = haveModel;
    }
}
