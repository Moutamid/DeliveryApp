package nuleo.autopart.grube.Model;

public class Comment {

    private String comment;
    private String publisher;
    private String commentid;
    private String postid;


    public Comment(String comment, String publisher, String commentid, String postid) {
        this.comment = comment;
        this.publisher = publisher;
        this.commentid = commentid;
        this.postid = postid;
    }

    public Comment(){

    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }


}
