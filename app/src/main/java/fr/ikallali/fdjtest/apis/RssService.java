package fr.ikallali.fdjtest.apis;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom")
})

@Root
public class RssService {

    private static String inputDateFormat = "E, dd MMM yyyy HH:mm:ss Z"; //Wed, 14 Jun 2017 23:27:36 +0000

    @Attribute
    private String version;

    @Element
    private Channel channel;


    public List<Channel.Item> getItems() {
        return channel.itemList;
    }


    @Override
    public String toString() {
        return "RSS{" +
                "version='" + version + '\'' +
                ", channel=" + channel +
                '}';
    }




    @Root(name = "channel", strict = false)
    public static class Channel {


        @ElementList(name = "item", required = true, inline = true)
        public List<Item> itemList;


        @Element(name = "title", required = false)
        public String title;

        @Element(name = "language", required = false)
        public String language;

        @Element(name = "ttl", required = false)
        public int ttl;

        @Element(name = "pubDate", required = false)
        public String pubDate;

        @Override
        public String toString() {
            return "Channel{" +
                    ", itemList=" + itemList +
                    ", title='" + title + '\'' +
                    ", language='" + language + '\'' +
                    ", ttl=" + ttl +
                    ", pubDate='" + pubDate + '\'' +
                    '}';
        }


        @Root(name = "item", strict = false)
        public static class Item {

            @Element(name = "title", required = true)
            public String title;

            @Element(name = "link", required = true)
            public String link;

            @Element(name = "description", required = false)
            public String description;

            @Element(name = "author", required = false)
            public String author;

            @ElementList(entry = "enclosure", inline = true, required = false)
            public List<Enclosure> enclosures = new ArrayList<Enclosure>();
            public static class Enclosure {
                @Attribute(required = false)
                public String url;

                @Attribute(name = "type", required = false)
                public String contentType;

                @Attribute(name = "length", required = false)
                public String length;

                @Override
                public String toString() {
                    return url;
                }
            }


            @ElementList(name = "category", required = false, inline = true)
            public List<Category> categories = new ArrayList<Category>();

            public List<String> getCategoriesList(){
                List<String> categoriesList = new ArrayList<String>();
                if(categories != null) {
                    for (int i = 0; i < categories.size(); i++) {
                        if(categories.get(i).text != null)
                           categoriesList.add(categories.get(i).text);
                    }
                }
                return categoriesList;
            }

            @Root(name = "category", strict = false)
            public static class Category {
                @Text(required = false)
                public String text;

                @Override
                public String toString() {
                    return text;
                }
            }

            @Element(name = "guid", required = false)
            public String guid;

            @Element(name = "pubDate", required = false)
            public String pubDate;

            public Date getDate(){
                SimpleDateFormat formatterIn = new SimpleDateFormat(inputDateFormat, Locale.ENGLISH);
                try {
                    return formatterIn.parse(pubDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Element(name = "source", required = false)
            public String source;

            @Override
            public String toString() {
                return "Item{" +
                        "title='" + title + '\'' +
                        ", link='" + link + '\'' +
                        ", description='" + description + '\'' +
                        ", author='" + author + '\'' +
                        ", enclosures='" + enclosures + '\'' +
                        ", category='" + categories + '\'' +
                        ", guid='" + guid + '\'' +
                        ", pubDate='" + pubDate + '\'' +
                        ", source='" + source + '\'' +
                        '}';
            }
        }
    }
}
