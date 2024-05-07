package com.malinduliyanage.pustakaapp;

public class LendedBook {

    private String bookId;
    private String bookTitle;
    private String libId;
    private String timestamp;
    public LendedBook(String bookId, String bookTitle, String libId, String timestamp) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.libId = libId;
        this.timestamp = timestamp;
    }

    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getLibId() {
        return libId;
    }
    public void setLibId(String libId) {
        this.libId = libId;
    }
    public String getbookTitle() {
        return bookTitle;
    }
    public void setbookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}

