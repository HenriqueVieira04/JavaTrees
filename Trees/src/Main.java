import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        ArvBin a = new ArvBin(100);
        AVL av = new AVL(100);

        av.insert("Sergio");
        av.insert("Abel");
        av.insert("Aaron");
        av.insert("Sergio");

        System.out.println(av.toString());

        Scanner teclado = new Scanner(System.in);

            String enter = teclado.nextLine();
            String[] conjunto = new String[2];
            conjunto = enter.split(" ");

            if(conjunto[0] == "i"){
                a.insert(conjunto[1]);
            }
            else{
                a.remove(conjunto[1]);
            }
        


        teclado.close();


    }
}
