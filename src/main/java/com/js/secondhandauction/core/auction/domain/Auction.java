package com.js.secondhandauction.core.auction.domain;

public class Auction {
    private Long auction_no;
    private Long item_no;

    private int price = 0;

    private Long reg_id;

    private String reg_date;

    public Long getAuction_no() {
        return auction_no;
    }

    public void setAuction_no(Long auction_no) {
        this.auction_no = auction_no;
    }

    public Long getItem_no() {
        return item_no;
    }

    public void setItem_no(Long item_no) {
        this.item_no = item_no;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Long getReg_id() {
        return reg_id;
    }

    public void setReg_id(Long reg_id) {
        this.reg_id = reg_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "auction_no=" + auction_no +
                ", item_no=" + item_no +
                ", price=" + price +
                ", reg_id=" + reg_id +
                ", reg_date='" + reg_date + '\'' +
                '}';
    }
}
