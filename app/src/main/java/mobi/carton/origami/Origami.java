package mobi.carton.origami;


/**
 * Model Origami use to populate/create adapter for a list of origami
 */
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
