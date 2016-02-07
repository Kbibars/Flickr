package Utility;

import com.squareup.picasso.Picasso;

/**
 * Created by KBibars on 2/5/2016.
 */
public class SingleResponse {
    public String id;
    public String owner;
    public String secret;
    public String server;
    public String farm;
    public String title;
    public String isPublic;
    public String isFriend;
    public String isFamily;
    public String mPage;
    public String mPageCount;

    public String getmPage() {
        return mPage;
    }

    public void setmPage(String mPage) {
        this.mPage = mPage;
    }

    public String getmPageCount() {
        return mPageCount;
    }

    public void setmPageCount(String mPageCount) {
        this.mPageCount = mPageCount;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getIsFamily() {
        return isFamily;
    }

    public void setIsFamily(String isFamily) {
        this.isFamily = isFamily;
    }


}
