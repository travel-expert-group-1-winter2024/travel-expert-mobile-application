package com.example.travelexpertmobileapplication.Models;

import com.google.gson.annotations.SerializedName;

public class Customer {
    private int customerid;
    private String custfirstname;
    private String custlastname;
    private String custaddress;
    private String custcity;
    private String custprov;
    private String custpostal;
    private String custcountry;
    private String custhomephone;
    private String custbusphone;
    private String custemail;
    private int agentId;

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getCustfirstname() {
        return custfirstname;
    }

    public void setCustfirstname(String custfirstname) {
        this.custfirstname = custfirstname;
    }

    public String getCustlastname() {
        return custlastname;
    }

    public void setCustlastname(String custlastname) {
        this.custlastname = custlastname;
    }

    public String getCustaddress() {
        return custaddress;
    }

    public void setCustaddress(String custaddress) {
        this.custaddress = custaddress;
    }

    public String getCustcity() {
        return custcity;
    }

    public void setCustcity(String custcity) {
        this.custcity = custcity;
    }

    public String getCustprov() {
        return custprov;
    }

    public void setCustprov(String custprov) {
        this.custprov = custprov;
    }

    public String getCustpostal() {
        return custpostal;
    }

    public void setCustpostal(String custpostal) {
        this.custpostal = custpostal;
    }

    public String getCustcountry() {
        return custcountry;
    }

    public void setCustcountry(String custcountry) {
        this.custcountry = custcountry;
    }

    public String getCusthomephone() {
        return custhomephone;
    }

    public void setCusthomephone(String custhomephone) {
        this.custhomephone = custhomephone;
    }

    public String getCustbusphone() {
        return custbusphone;
    }

    public void setCustbusphone(String custbusphone) {
        this.custbusphone = custbusphone;
    }

    public String getCustemail() {
        return custemail;
    }

    public void setCustemail(String custemail) {
        this.custemail = custemail;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }
}
