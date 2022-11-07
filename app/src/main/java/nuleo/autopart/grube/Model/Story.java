package nuleo.autopart.grube.Model;

public class Story {

    private String imageurl;
    private long timestart;
    private long timmend;
    private String storyid;
    private String userid;

    public Story(String imageurl, long timestart, long timmend, String storyid, String userid) {
        this.imageurl = imageurl;
        this.timestart = timestart;
        this.timmend = timmend;
        this.storyid = storyid;
        this.userid = userid;
    }

    public Story(){

    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimmend() {
        return timmend;
    }

    public void setTimmend(long timmend) {
        this.timmend = timmend;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
