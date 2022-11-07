package nuleo.autopart.grube.Model;

public class Comet {

    private String comet;
    private String publi;
    private String comid;
    private String postid;

    public Comet(String comet, String publi, String comid, String postid) {
        this.comet = comet;
        this.publi = publi;
        this.comid = comid;
        this.postid = postid;
    }

    public Comet(){

    }

    public String getComet() {
        return comet;
    }

    public void setComet(String comet) {
        this.comet = comet;
    }

    public String getPubli() {
        return publi;
    }

    public void setPubli(String publi) {
        this.publi = publi;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
