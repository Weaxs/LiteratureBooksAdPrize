package org.weaxsey.book.domain;

public class BookMessage {

    //书名
    private String bookName = "";
    //著译者
    private String author = "";
    //出版社
    private String publisher = "";
    //出版日期
    private String publishDate = "";
    //大于/小于出版日期
    private String publishPrefix = "";
    //库存状态
    private String isHave = "";
    //获奖年
    private String winnerYear;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishPrefix() {
        return publishPrefix;
    }

    public void setPublishPrefix(String publishPrefix) {
        this.publishPrefix = publishPrefix;
    }

    public String getIsHave() {
        return isHave;
    }

    public void setIsHave(String isHave) {
        this.isHave = isHave;
    }

    public String getWinnerYear() {
        return winnerYear;
    }

    public void setWinnerYear(String winnerYear) {
        this.winnerYear = winnerYear;
    }
}
