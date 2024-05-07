package com.malinduliyanage.pustakaapp;

public class SearchBook {
    private int id;
    private String bookId;
    private String title;
    private String author;
    private String description;
    private String thumb;
    private String isbn;
    private String pages;
    private String publisher;
    private int copiesCount;

    public SearchBook(String bookId, String title, String author, String description, String thumb, String isbn, String pages, String publisher) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.thumb = thumb;
        this.isbn = isbn;
        this.pages = pages;
        this.publisher = publisher;
    }


    // Getters and setters
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
    public String getbookThumb() {
        return thumb;
    }
    public void setbookThumb(String thumb) {
        this.thumb = thumb;
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

    public int getCopiesCount() {
        return copiesCount;
    }

    public void setCopiesCount(int copiesCount) {
        this.copiesCount = copiesCount;
    }
}

