package com.manish.creditcardscanner.utils;

public class Transaction {
    private String txnDescriptionTxtVw;
    private String dateTxtVw;
    private String amountTxtVw;
    private String txnTypeTxtVw;

    public Transaction(String txnDescriptionTxtVw, String dateTxtVw, String amountTxtVw, String txnTypeTxtVw) {
        this.txnDescriptionTxtVw = txnDescriptionTxtVw;
        this.dateTxtVw = dateTxtVw;
        this.amountTxtVw = amountTxtVw;
        this.txnTypeTxtVw = txnTypeTxtVw;
    }

    public String getTxnDescriptionTxtVw() {
        return txnDescriptionTxtVw;
    }

    public void setTxnDescriptionTxtVw(String txnDescriptionTxtVw) {
        this.txnDescriptionTxtVw = txnDescriptionTxtVw;
    }

    public String getDateTxtVw() {
        return dateTxtVw;
    }

    public void setDateTxtVw(String dateTxtVw) {
        this.dateTxtVw = dateTxtVw;
    }

    public String getAmountTxtVw() {
        return amountTxtVw;
    }

    public void setAmountTxtVw(String amountTxtVw) {
        this.amountTxtVw = amountTxtVw;
    }

    public String getTxnTypeTxtVw() {
        return txnTypeTxtVw;
    }

    public void setTxnTypeTxtVw(String txnTypeTxtVw) {
        this.txnTypeTxtVw = txnTypeTxtVw;
    }
}
