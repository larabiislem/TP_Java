import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // créer les règles
        Rule[] bdr = new Rule[9];
        bdr[0] = new Rule(0, new ArrayList<>(Arrays.asList("A","B")),     new ArrayList<>(Arrays.asList("F")));
        bdr[1] = new Rule(1, new ArrayList<>(Arrays.asList("F","H")),     new ArrayList<>(Arrays.asList("I")));
        bdr[2] = new Rule(2, new ArrayList<>(Arrays.asList("D","H","G")), new ArrayList<>(Arrays.asList("A")));
        bdr[3] = new Rule(3, new ArrayList<>(Arrays.asList("O","G")),     new ArrayList<>(Arrays.asList("H")));
        bdr[4] = new Rule(4, new ArrayList<>(Arrays.asList("E","H")),     new ArrayList<>(Arrays.asList("B")));
        bdr[5] = new Rule(5, new ArrayList<>(Arrays.asList("G","A")),     new ArrayList<>(Arrays.asList("B")));
        bdr[6] = new Rule(6, new ArrayList<>(Arrays.asList("G","H")),     new ArrayList<>(Arrays.asList("P")));
        bdr[7] = new Rule(7, new ArrayList<>(Arrays.asList("G","H")),     new ArrayList<>(Arrays.asList("O")));
        bdr[8] = new Rule(8, new ArrayList<>(Arrays.asList("D","O","G")), new ArrayList<>(Arrays.asList("J")));

        // demander à l'utilisateur
        Scanner sc = new Scanner(System.in);
        System.out.print("Faits initiaux (ex: D,O,G) : ");
        String[] input = sc.nextLine().split(",");
        ArrayList<String> bdp = new ArrayList<>();
        for (String s : input) bdp.add(s.trim());

        System.out.print("Objectif (ex: I) : ");
        String goal = sc.nextLine().trim();

        // lancer
        System.out.println("---");
        Engine.forward(bdr, bdp, goal);
    }
}