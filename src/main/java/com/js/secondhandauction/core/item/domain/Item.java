package com.js.secondhandauction.core.item.domain;

public class Item {
    private Long item_no;
    private String item;
    private String reg_date;
    private String upt_date;
    private int reg_price;
    private State state;
    private Long reg_id;

    private int bet;

    public Long getItem_no() {
        return item_no;
    }

    public void setItem_no(Long item_no) {
        this.item_no = item_no;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public int getReg_price() {
        return reg_price;
    }

    public void setReg_price(int reg_price) {
        this.reg_price = reg_price;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getReg_id() {
        return reg_id;
    }

    public void setReg_id(Long reg_id) {
        this.reg_id = reg_id;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_no=" + item_no +
                ", item='" + item + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", upt_date='" + upt_date + '\'' +
                ", reg_price=" + reg_price +
                ", state=" + state +
                ", reg_id=" + reg_id +
                ", bet=" + bet +
                '}';
    }
}
