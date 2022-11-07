package nuleo.autopart.grube.Model;

public class Com {


    private String comett;
    private String publii;
    private String comidd;
    private String postid;

    public Com(String comett, String publii, String comidd, String postid) {
        this.comett = comett;
        this.publii = publii;
        this.comidd = comidd;
        this.postid = postid;
    }

    public Com(){

    }

    public String getComett() {
        return comett;
    }

    public void setComett(String comett) {
        this.comett = comett;
    }

    public String getPublii() {
        return publii;
    }

    public void setPublii(String publii) {
        this.publii = publii;
    }

    public String getComidd() {
        return comidd;
    }

    public void setComidd(String comidd) {
        this.comidd = comidd;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
