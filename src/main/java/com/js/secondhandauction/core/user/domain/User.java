package com.js.secondhandauction.core.user.domain;



public class User {

    private Long id;

    private String name;

    private int amount = 10000000;

    private String reg_date;

    private String upt_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getUpt_date() {
        return upt_date;
    }

    public void setUpt_date(String upt_date) {
        this.upt_date = upt_date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", reg_date='" + reg_date + '\'' +
                ", upt_date='" + upt_date + '\'' +
                '}';
    }
}
