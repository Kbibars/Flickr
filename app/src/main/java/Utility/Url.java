package Utility;

/**
 * Created by KBibars on 2/5/2016.
 */
public class Url {

    String method="flickr.photos.search";
    String api_key="1bedf73110dd19f78411e9d142c59812";
    String format="rest";

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
