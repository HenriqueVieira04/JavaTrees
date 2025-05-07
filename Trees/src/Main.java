/*
NOMES:
HENRIQUE VIEIRA LIMA    NUSP: 15459372
GABRIEL PHILIPPE PRADO  NUSP: 15453730

*/

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        AVL avl = new AVL(100);
        ArvBin abb = new ArvBin(100);
        ArvPB apb = new ArvPB(100);


        String[] conjunto = new String[2];
        Scanner teclado = new Scanner(System.in);

        String enter = "";
        if (teclado.hasNextLine()) {
            enter = teclado.nextLine();
            conjunto = enter.split(" ");
        }

        while (true) {
            if (conjunto[0].equals("i")) {
                abb.insert(conjunto[1]);
                apb.insert(conjunto[1]);
                avl.insert(conjunto[1]);
            } else if (conjunto[0].equals("d")) {
                abb.remove(conjunto[1]);
                apb.remove(conjunto[1]);
                avl.remove(conjunto[1]);
            }

            if (teclado.hasNextLine()) {
                enter = teclado.nextLine();
                conjunto = enter.split(" ");
            } else {
                break; // Sai do loop se não houver mais linhas
            }
        }

        teclado.close();
        System.out.println(abb.toString());
        System.out.println(apb.toString());
        System.out.println(avl.toString());
    }
}
