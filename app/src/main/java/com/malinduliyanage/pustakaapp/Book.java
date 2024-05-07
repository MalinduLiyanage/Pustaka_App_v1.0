package com.malinduliyanage.pustakaapp;

public class Book {
    private int id;
    private String bookId;
    private String title;
    private String author;
    private String description;
    private String bookThumb;
    private String isbn;
    private String pages;
    private String publisher;
    private String copiesCount;

    public Book(String bookId, String title, String author, String description, String bookThumb, String isbn, String pages, String publisher, String copiesCount) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.bookThumb = bookThumb;
        this.isbn = isbn;
        this.pages = pages;
        this.publisher = publisher;
        this.copiesCount = copiesCount;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getPages() {
        return pages;
    }
    public void setPages(String pages) {
        this.pages = pages;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getCopiesCount() {
        return copiesCount;
    }
    public void setCopiesCount(String copiesCount) {
        this.copiesCount = copiesCount;
    }
    public String getbookThumb() {
        return bookThumb;
    }
    public void setbookThumb(String bookThumb) {
        this.bookThumb = bookThumb;
    }
}

