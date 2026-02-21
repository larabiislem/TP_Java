import java.util.ArrayList;

public class Engine {

    public static boolean forward(Rule[] bdr, ArrayList<String> bdp, String goal) {

        boolean newFact = true;

        while (newFact) {
            newFact = false;

            for (int i = 0; i < bdr.length; i++) {
                if (!bdr[i].used) {

                    // vérifier toutes les conditions
                    boolean ok = true;
                    for (String cond : bdr[i].p) {
                        if (!bdp.contains(cond)) {
                            ok = false;
                            break;
                        }
                    }

                    // si toutes les conditions sont vraies
                    if (ok) {
                        bdr[i].used = true;

                        for (String concl : bdr[i].c) {
                            if (!bdp.contains(concl)) {
                                bdp.add(concl);
                                newFact = true;
                                System.out.println("R" + bdr[i].name + " -> nouveau fait : " + concl);
                            }
                        }

                        if (bdp.contains(goal)) {
                            System.out.println("Objectif " + goal + " atteint !");
                            return true;
                        }
                    }
                }
            }
        }

        System.out.println("Objectif " + goal + " non atteint");
        return false;
    }
}