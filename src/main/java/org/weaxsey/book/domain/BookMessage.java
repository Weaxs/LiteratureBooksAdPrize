package org.weaxsey.book.domain;

/**
 * book
 *
 * @author Weaxs
 */
public class BookMessage {

    /**
     * book name
     */
    private String bookName;

    /**
     * author name
     */
    private String author;

    /**
     * publisher
     */
    private String publisher;

    /**
     * publish date
     */
    private String publishDate;

    /**
     * publish prefix
     */
    private String publishPrefix;

    /**
     * is have
     */
    private String isHave;

    /**
     * winner year
     */
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
