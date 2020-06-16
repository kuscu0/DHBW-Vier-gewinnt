package bean;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Control c = new Control();
        c.newRound(true);

        boolean yourTurn = true;

        Scanner sc = new Scanner(System.in);


        System.out.println("New Game started.");
        while (c.getPlayerWon() == 0) {
            System.out.println();
            if (yourTurn) {
                String befehl = sc.nextLine();
                c.setChip(Integer.valueOf(befehl));
            }
            yourTurn = !yourTurn;

            int[][] field = c.getField();
            // Zu Test Zwecken
            // System.out.println("Field: " + Arrays.deepToString(c.getField()));

            for (int[] row: field) {
                for (int i: row) {
                    System.out.print(i + " ");
                }
                System.out.print("\n");
            }

            c.nextRound();
        }

        System.out.println(c.getPlayerWonToString());
        sc.close();
    }

}