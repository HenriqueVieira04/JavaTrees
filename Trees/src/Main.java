import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        ArvBin a = new ArvBin(100);
        //AVL av = new AVL(100);
        /* 
        a.insert("Sergio");
        a.insert("Abel");
        a.insert("Aaron");
        a.insert("Sergio");

        */
        String[] conjunto = new String[2];
        Scanner teclado = new Scanner(System.in);

        String enter = teclado.nextLine();
        conjunto = enter.split(" ");

        while(!conjunto[0].equals("f")){
            if(conjunto[0].equals("i")){
                a.insert(conjunto[1]);
            }
            else if(conjunto[0].equals("d")){
                a.remove(conjunto[1]);
            }

            enter = teclado.nextLine();
            conjunto = enter.split(" ");
        }
        
        teclado.close();
        System.out.println(a.toString());
        
    }
}
