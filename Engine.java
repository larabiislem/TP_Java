import java.util.ArrayList;

public class Engine {

    public static ArrayList<String> forward(Rule[] bdr, ArrayList<String> bdp, String goal) {

        ArrayList<String> log = new ArrayList<>();
        boolean newFact = true;
        int tour = 1;

        // réinitialiser les règles
        for (Rule r : bdr) r.used = false;

        log.add("════════════════════════════════════════");
        log.add("  CHAÎNAGE AVANT");
        log.add("════════════════════════════════════════");
        log.add("  Faits initiaux : " + bdp);
        log.add("  Objectif       : " + goal);
        log.add("────────────────────────────────────────");

        while (newFact) {
            newFact = false;
            log.add("");
            log.add("🔄 Tour " + tour + " :");

            for (int i = 0; i < bdr.length; i++) {
                if (!bdr[i].used) {

                    boolean ok = true;
                    for (String cond : bdr[i].p) {
                        if (!bdp.contains(cond)) {
                            ok = false;
                            break;
                        }
                    }

                    if (ok) {
                        bdr[i].used = true;
                        log.add("  ✅ R" + bdr[i].name + " : " + bdr[i].p + " → " + bdr[i].c);

                        for (String concl : bdr[i].c) {
                            if (!bdp.contains(concl)) {
                                bdp.add(concl);
                                newFact = true;
                                log.add("     ➕ Nouveau fait : " + concl);
                            }
                        }

                        if (bdp.contains(goal)) {
                            log.add("");
                            log.add("────────────────────────────────────────");
                            log.add("🎯 OBJECTIF \"" + goal + "\" ATTEINT !");
                            log.add("  Faits finaux : " + bdp);
                            log.add("════════════════════════════════════════");
                            return log;
                        }
                    }
                }
            }
            tour++;
        }

        log.add("");
        log.add("────────────────────────────────────────");
        log.add("❌ OBJECTIF \"" + goal + "\" NON ATTEINT");
        log.add("  Faits finaux : " + bdp);
        log.add("════════════════════════════════════════");
        return log;
    }
}