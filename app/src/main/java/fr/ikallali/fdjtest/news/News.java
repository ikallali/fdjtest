package fr.ikallali.fdjtest.news;


import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Model class of a rss news
 */
public class News implements Serializable {

    private String title;
    private String description;
    private String image;
    private String link;
    private Date pubdate;
    private List<String> categories;

    public News() {

    }

    /**
     * Return the title string
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set a title
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return the description string
     * @return the descripion
     */
    public String getDescription() {
        return description;
    }


    /**
     * Set the description
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the date string
     * @return the date
     */
    public Date getPubDate() {
        return pubdate;
    }

    /**
     * Set the date
     * @param date new date
     */
    public void setPubDate(Date date) {
        this.pubdate = date;
    }


    /**
     * Return the date string
     * @return the date ago
     */
    public String getPubDateStr() {
        return getPubDateStr("dd'/'MM'/'yyyy  HH':'mm");
    }

    /**
     * Return the date string
     * @return the date ago
     */
    public String getPubDateStr(String dateFormat) {
        SimpleDateFormat formatterOut = new SimpleDateFormat(dateFormat);
        return formatterOut.format(pubdate);
    }



    public String getPubDateAgo() {
        return TimeAgo.using(pubdate.getTime());
    }

    /**
     * Return the image url
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * set the image url
     * @param image new image url
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Return the url of the news
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Set the url
     * @param link new url
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Return a categories list
     * @return the categories list
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * set a list of categories
     * @param categories new categories
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
