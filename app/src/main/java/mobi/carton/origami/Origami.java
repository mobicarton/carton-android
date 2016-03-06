package mobi.carton.origami;


public class Origami {


    private String name;
    private String author;
    private int nbSteps;


    public String getName() {
        return name;
    }


    public String getAuthor() {
        return author;
    }


    public int getNbSteps() {
        return nbSteps;
    }


    public Origami(String name, String author, int nbSteps) {
        this.name = name;
        this.author = author;
        this.nbSteps = nbSteps;
    }
}
