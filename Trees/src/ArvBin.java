/*
NOMES:
HENRIQUE VIEIRA LIMA    NUSP: 15459372
GABRIEL PHILIPPE PRADO  NUSP: 15453730

*/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArvBin implements Arv{
    public String[] nodes;
    public int qtd;
    public int kind;
    public int lastNodeIndex;

    public ArvBin(int n){
        this.nodes = new String[n];
        for (int i = 0; i < n; i++) {
            this.nodes[i] = ""; // Inicializa cada posição com uma string vazia
        }

        this.qtd = 0;
        this.kind = 0; //para o tipo "ABB"
        this.lastNodeIndex = -1;
    }

    protected int countNodes(int sub) {
        if (sub < 0 || sub >= nodes.length || nodes[sub].equals("")) {
            return 0;
        }

        // Conta o nó atual e soma com os nós das subárvores esquerda e direita
        int leftCount = countNodes(nodeLeft(sub));
        int rightCount = countNodes(nodeRight(sub));

        return 1 + leftCount + rightCount;
    }

    @Override
    public boolean find(String target){
        if(findIndex(0, target) == -1) return false;
        else return true;
    }

    protected String getNode(int position){
        if (position < 0 || position >= this.nodes.length || nodes[position].equals("")) {
            return("");
        }

        return(this.nodes[position]);
    }

    @Override
    public void insert(String value) {
        if (this.nodes[0].equals("")) {
            // Insere na raiz se estiver vazia
            this.nodes[0] = value;
            this.qtd++;
        } else {
            // Chama a função recursiva para inserir
            insertRecursive(0, value);
        }
        findLastNodeIndex();
    }

    protected void findLastNodeIndex() {
        for (int cont = this.nodes.length-1; cont >= 0; cont--) { 
            if (!this.nodes[cont].equals("")){
                this.lastNodeIndex = cont;
                return;
            }
        } 
        this.lastNodeIndex = -1;
   }

    private void insertRecursive(int index, String value) {
        if (index >= this.nodes.length) return;

        if (this.nodes[index].equals("")) {
            this.nodes[index] = value;
            this.qtd++;
        } 
        else if (value.compareTo(this.nodes[index]) < 0) {
            insertRecursive(nodeLeft(index), value);
        } 
        else if (value.compareTo(this.nodes[index]) > 0) {
            insertRecursive(nodeRight(index), value);
        } 
    }

    protected boolean isBalance(){
        if(this.kind == 0) return(true);
        else return false;
    }

    @Override
    public int len(){
        return (this.qtd);
    }

    protected int nodeFather(int i){
        if (i < 0 || i >= this.nodes.length || this.nodes[i].equals("")) {
            return -1;
        }

        return ((int) ((i-1)/2));
    }

    protected int nodeLeft(int i){
        if (i < 0 || i >= this.nodes.length || this.nodes[i].equals("")) {
            return -1;
        }

        return (2*i+1);
    }

    protected int nodeRight(int i){
        if (i < 0 || i >= this.nodes.length || this.nodes[i].equals("")) {
            return -1;
        }

        return (2*i+2);
    }

    protected int max(int a, int b) {
        return (a > b) ? a : b;
    }

    @Override
    public boolean remove(String target) {
        int index = findIndex(0, target); // Encontra o índice do nó a ser removido

        if (index == -1) return false; // Nó não encontrado
        if (removeNode(index) == true){
            this.qtd--;
            return true;
        }
        return false;
    }

    private boolean removeNode(int index){
        
        // Caso 1: Nó é uma folha
        if (nodeLeft(index) >= nodes.length || nodes[nodeLeft(index)].equals("")) {
            if (nodeRight(index) >= nodes.length || nodes[nodeRight(index)].equals("")) {
                nodes[index] = ""; // Remove o nó simplesmente
                return true;
            }
        }

        // Caso 2: Nó tem apenas um filho
        if (nodes[nodeLeft(index)].equals("")) { // Apenas filho direito
            this.nodes[index] = this.nodes[nodeRight(index)];
            return(removeNode(nodeRight(index)));
        }
        else if (nodes[nodeRight(index)].equals("")) { // Apenas filho direito
            this.nodes[index] = this.nodes[nodeLeft(index)];
            return(removeNode(nodeLeft(index)));
        }


        // Caso 3: Nó tem dois filhos
        else {
            int maxesq = findMax(nodeLeft(index)); // Encontra o sucessor
            int mindir = findMin(nodeRight(index));
            

            if( maxesq < mindir){
                this.nodes[index] = this.nodes[maxesq];
                removeNode(maxesq);
                return true;
            }
            else{
                this.nodes[index] = this.nodes[mindir];
                removeNode(mindir);
                return true;
            }
            
        }
    }

    protected void removeNodes(List<String> list){
        for (int i = 0; i < this.nodes.length; i++) {
            String val =  this.nodes[i];
            if (list.contains(val)) this.nodes[i] = "";
      }
      this.qtd -= list.size();
   }

    // Encontra o índice do menor valor em uma subárvore
    protected int findMin(int index) {
        while (nodeLeft(index) < nodes.length && !nodes[nodeLeft(index)].equals("")) {
            index = nodeLeft(index);
        }
        return index;
    }

    protected int findMax(int index){
        while (nodeRight(index) < nodes.length && !nodes[nodeRight(index)].equals("")) {
            index = nodeRight(index);
        }
        return index;
    }

    protected void setNode(int i, String value){
        if (i < 0 || i >= nodes.length || this.nodes[i].equals("")) {
            return;
        }

        this.nodes[i] = value;
    }

    @Override
    public String toString() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph {\n");

        if(this.qtd==1) {
            dot.append("\"").append("0 ").append(this.nodes[0]).append("\"").append(" }\n");
        }
        else {
            for (int i = 0; i < this.nodes.length; i++) {
                if (!this.nodes[i].equals("")) {
                    int left = nodeLeft(i);
                    if (left < nodes.length && left != -1 && !this.nodes[left].equals("")) {
                        dot.append("\"").append(i+" ").append(this.nodes[i]).append("\"").append(" ->").append("\"").append(left+" ").append(this.nodes[left]).append("\"\n");
                    }
                    // Adiciona a aresta para o filho direito, se existir
                    int right = nodeRight(i);
                    if (right < nodes.length && right != -1 && !this.nodes[right].equals("")) {
                        dot.append("\"").append(i+" ").append(this.nodes[i]).append("\"").append(" ->").append("\"").append(right+" ").append(this.nodes[right]).append("\"\n");
                    }
                }
            }

            dot.append("}\n");
        }
        return dot.toString();
    }

    protected List<String> getSubTree(int position){ //Percorre a subarvore pelos niveis removendo os nós e guardando eles em ordem
        List <String> subt = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        int index;
        int left;
        int right;
        
        if(position >= 0 && position < this.nodes.length && !this.nodes[position].equals("")){
            queue.add(position);
        }

        while(!queue.isEmpty()){
            index = queue.poll();
            subt.add(this.nodes[index]);

            left = nodeLeft(index);
            right = nodeRight(index);

            if (left < this.nodes.length && !this.nodes[left].equals("")) {
                queue.add(left);
            }
    
            if (right < this.nodes.length && !this.nodes[right].equals("")) {
                queue.add(right);
            }
        }

        return subt;
    }

    protected int findIndex(int src, String target){
        if (src < 0 || src >= nodes.length || this.nodes[src].equals("")) {
            return -1;
        }

        String begin = this.nodes[src];

        if(begin.compareTo(target)==0) return src;
        else if(begin.compareTo(target)<0)
            return(findIndex(nodeRight(src), target));
        else
            return(findIndex(nodeLeft(src), target));
    }
}
