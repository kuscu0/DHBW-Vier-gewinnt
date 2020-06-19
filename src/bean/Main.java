//package bean;
//
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
//        
//        Control c = new Control();
////        c.newRound(true);
//        
//        boolean yourTurn = true;
//
//        Scanner sc = new Scanner(System.in);
//
//
//        System.out.println("New Game started.");
//
//        int i = 2;
//        
//        while (c.getPlayerWon() == 0 && i > 0) 
//        {
////            System.out.println();
////            if (yourTurn) 
////            {
////                String befehl = sc.nextLine();
////                c.setChip(Integer.valueOf(befehl));
////            }
////            yourTurn = !yourTurn;
//            
//            int[][] field = c.getField();
//            // Zu Test Zwecken
//            // System.out.println("Field: " + Arrays.deepToString(c.getField()));
//
//            for (int[] row: field) 
//            {
//                for (int g: row) 
//                {
//                    System.out.print(g + " ");
//                }
//                System.out.print("\n");
//            }
//
////            c.nextRound();
//            c.setChip(1);
//            c.checkGewonnen();
//            i--;
//        }
//       
//        
//        System.out.println(c.getPlayerWonToString());
//        sc.close();
//    }
//
//}