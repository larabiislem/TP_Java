import java.util.ArrayList;

public class Rule {
    int name;              // numéro de la règle
    ArrayList<String> p;   // conditions (SI)
    ArrayList<String> c;   // conclusions (ALORS)
    boolean used;          // déjà utilisée ?

    public Rule(int name, ArrayList<String> p, ArrayList<String> c) {
        this.name = name;
        this.p = p;
        this.c = c;
        this.used = false;
    }
}