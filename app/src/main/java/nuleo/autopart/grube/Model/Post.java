package nuleo.autopart.grube.Model;

public class Post {

    private String postid;
    private String postimage;
    private String modelomarcaaño;
    private String cilindraje;
    private String traccion;
    private String version;
    private String telefono;
    private String detalles;
    private String publisher;
    private String fullname;
    private String ficha_tecnica;
    private String commentid;
    private boolean expanded;



    public Post(String ficha_tecnica, String postid, String postimage, String modelomarcaaño, String cilindraje, String traccion, String version, String telefono, String detalles, String publisher, String commentid, String fullname) {
        this.ficha_tecnica = ficha_tecnica;

        this.postid = postid;
        this.postimage = postimage;
        this.modelomarcaaño = modelomarcaaño;
        this.cilindraje = cilindraje;
        this.traccion = traccion;
        this.version = version;
        this.telefono = telefono;
        this.detalles = detalles;
        this.publisher = publisher;
        this.commentid = commentid;
        this.fullname = fullname;
        this.expanded=false;

    }

    public Post (){

    }

    public String getFicha_tecnica() {
        return ficha_tecnica;
    }

    public void setFicha_tecnica(String ficha_tecnica) {
        this.ficha_tecnica = ficha_tecnica;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getModelomarcaaño() {
        return modelomarcaaño;
    }

    public void setModelomarcaaño(String modelomarcaaño) {
        this.modelomarcaaño = modelomarcaaño;
    }

    public String getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(String cilindraje) {
        this.cilindraje = cilindraje;
    }

    public String getTraccion() {
        return traccion;
    }

    public void setTraccion(String traccion) {
        this.traccion = traccion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;



    }



    @Override
    public String toString() {
        return "Post{" +
                ", modelomarcaaño='" + modelomarcaaño + '\'' +
                ", cilindraje='" + cilindraje + '\'' +
                ", traccion='" + traccion + '\'' +
                ", version='" + version + '\'' +
                ", telefono='" + telefono + '\'' +
                ", detalles='" + detalles + '\'' +
                '}';
    }
}
