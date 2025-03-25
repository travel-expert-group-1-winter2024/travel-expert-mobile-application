package com.example.travelexpertmobileapplication.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Package implements Serializable {
    private int packageid;
    private String pkgname;
    private String pkgdesc;
    private BigDecimal pkgbaseprice;
    private BigDecimal pkgagencycommission;
    private String pkgstartdate;
    private String pkgenddate;

    private List<ProductSupplier> productsSuppliers;

    private List<Integer> productsupplierids;

    public Package(int packageid, String pkgname, String pkgdesc, BigDecimal pkgbaseprice, BigDecimal pkgagencycommission, String pkgstartdate, String pkgenddate) {
        this.packageid = packageid;
        this.pkgname = pkgname;
        this.pkgdesc = pkgdesc;
        this.pkgbaseprice = pkgbaseprice;
        this.pkgagencycommission = pkgagencycommission;
        this.pkgstartdate = pkgstartdate;
        this.pkgenddate = pkgenddate;
    }

    public Package(int id, String pkgname, String pkgdesc, BigDecimal pkgbaseprice, BigDecimal pkgagencycommission, String pkgstartdate, String pkgenddate, List<Integer> productsupplierids) {
        this.packageid = id;
        this.pkgname = pkgname;
        this.pkgdesc = pkgdesc;
        this.pkgbaseprice = pkgbaseprice;
        this.pkgagencycommission = pkgagencycommission;
        this.pkgstartdate = pkgstartdate;
        this.pkgenddate = pkgenddate;
        this.productsupplierids = productsupplierids;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid){
        this.packageid = packageid;
    }

    public String getPkgname() {
        return pkgname;
    }

    public String getPkgdesc() {
        return pkgdesc;
    }

    public BigDecimal getPkgbaseprice() {
        return pkgbaseprice;
    }

    public BigDecimal getPkgagencycommission() {
        return pkgagencycommission;
    }

    public String getPkgstartdate() {
        return pkgstartdate;
    }

    public String getPkgenddate() {
        return pkgenddate;
    }

    public List<ProductSupplier> getProductSupplier() {
        return productsSuppliers;
    }

    public void setProductSupplier(List<ProductSupplier> productSupplier) {
        this.productsSuppliers = productSupplier;
    }

    public List<Integer> getProductsupplierids() {
        return productsupplierids;
    }

    public void setProductsupplierids(List<Integer> productsupplierids) {
        this.productsupplierids = productsupplierids;
    }
    @Override
    public String toString() {
        return "Package{" +
                "id=" + packageid +
                ", pkgname='" + pkgname + '\'' +
                ", pkgdesc='" + pkgdesc + '\'' +
                ", pkgbaseprice=" + pkgbaseprice +
                ", pkgagencycommission=" + pkgagencycommission +
                ", pkgstartdate='" + pkgstartdate + '\'' +
                ", pkgenddate='" + pkgenddate + '\'' +
                ", productSupplier=" + productsSuppliers +
                ", productsupplierids=" + productsupplierids +
                '}';
    }

}