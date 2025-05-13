/*
NOMES:
HENRIQUE VIEIRA LIMA    NUSP: 15459372
GABRIEL PHILIPPE PRADO  NUSP: 15453730

*/

import java.util.Arrays;

public class ArvPB extends ArvBin {
    public ArvPB(int n) {
        super(n);
        this.kind = 1; //para o tipo "APB"
    }

    @Override
    public void insert(String value){
        super.insert(value);
        this.toBalance();
    }


    @Override
    public boolean remove(String target){
        if(super.remove(target)){
            this.toBalance();
            return true;
        }
        return false;
    }

    protected void toBalance(){
        if(!isBalance()){
            String[] arrAux = Arrays.stream(this.nodes).filter(item -> !item.equals("")).sorted().toArray(String[]::new);
            Arrays.fill(this.nodes, "");
            this.qtd = 0;
            this.rebuild(arrAux, 0, arrAux.length-1);
        }
    }

    private void rebuild(String[] arr, int inicio, int fim){
        if(inicio>fim) 
            return;

        int meio = (fim+inicio)/2;
        super.insert(arr[meio]);
        rebuild(arr, inicio, meio-1);
        rebuild(arr, meio+1, fim);
    }
}
