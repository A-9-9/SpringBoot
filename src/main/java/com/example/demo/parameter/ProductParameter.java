package com.example.demo.parameter;

public class ProductParameter {

    private String keyWord;
    private String orderBy;
    private String sortRule;

    public String getKeyWord() {

        return keyWord == null ? "" : keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
    }
}
