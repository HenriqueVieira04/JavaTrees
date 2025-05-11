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
        if (!find(value)) {
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
        removeNode(index);
        this.qtd--;
        return true;
    }

    private boolean isPosicaoEfetivamenteVaziaOuForaDosLimites(int indicePosicao) {
        return indicePosicao < 0 || indicePosicao >= this.nodes.length || this.nodes[indicePosicao].equals("");
    }

    private boolean removeNode(int index) {
        // Verifica se o próprio nó a ser removido é válido e existe
        // O método público remove() deve garantir que 'index' seja válido inicialmente.
        // Esta verificação também é para chamadas recursivas.
        if (index < 0 || index >= this.nodes.length || this.nodes[index].equals("")) {
            return false; // Nó é inválido, fora dos limites ou já está vazio
        }

        int l_idx = nodeLeft(index);
        int r_idx = nodeRight(index);

        boolean filhoEsquerdoVazioOuOOB = isPosicaoEfetivamenteVaziaOuForaDosLimites(l_idx);
        boolean filhoDireitoVazioOuOOB = isPosicaoEfetivamenteVaziaOuForaDosLimites(r_idx);

        // Caso 1: Nó é uma folha (sem filhos válidos)
        if (filhoEsquerdoVazioOuOOB && filhoDireitoVazioOuOOB) {
            this.nodes[index] = ""; // Marca o nó atual como vazio
            return true;
        }
        // Caso 2: Nó tem apenas um filho
        else if (filhoEsquerdoVazioOuOOB && !filhoDireitoVazioOuOOB) { // Apenas o filho direito existe
            // Promove o sucessor (menor da subárvore direita)
            int indiceSucessor = findMin(r_idx); // r_idx é um índice de nó válido aqui
            this.nodes[index] = this.nodes[indiceSucessor];
            removeNode(indiceSucessor); // Remove recursivamente o nó sucessor original
            return true;
        }
        else if (!filhoEsquerdoVazioOuOOB && filhoDireitoVazioOuOOB) { // Apenas o filho esquerdo existe
            // Promove o predecessor (maior da subárvore esquerda)
            int indicePredecessor = findMax(l_idx); // l_idx é um índice de nó válido aqui
            this.nodes[index] = this.nodes[indicePredecessor];
            removeNode(indicePredecessor); // Remove recursivamente o nó predecessor original
            return true;
        }
        // Caso 3: Nó tem dois filhos
        else { // Ambos os filhos existem (!filhoEsquerdoVazioOuOOB && !filhoDireitoVazioOuOOB)
            // Promove o predecessor (maior da subárvore esquerda)
            int indicePredecessor = findMax(l_idx); // l_idx é um índice de nó válido
            this.nodes[index] = this.nodes[indicePredecessor];
            removeNode(indicePredecessor); // Remove recursivamente o nó predecessor original
            return true;
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
    protected int findMin(int indiceAtual) {
        // Assume-se que indiceAtual é um índice válido de um nó existente.
        int indiceFilhoEsquerdo = nodeLeft(indiceAtual);

        // Loop enquanto existir um filho esquerdo válido e não vazio
        while (indiceFilhoEsquerdo >= 0 && indiceFilhoEsquerdo < this.nodes.length && // Verifica os limites primeiro
               !this.nodes[indiceFilhoEsquerdo].equals("")) { // Depois verifica se está vazio
            indiceAtual = indiceFilhoEsquerdo;
            indiceFilhoEsquerdo = nodeLeft(indiceAtual); // Pega o próximo filho esquerdo para o novo atual
        }
        return indiceAtual; // Retorna o índice do menor nó na subárvore
    }

    protected int findMax(int indiceAtual) {
        // Assume-se que indiceAtual é um índice válido de um nó existente.
        int indiceFilhoDireito = nodeRight(indiceAtual);

        // Loop enquanto existir um filho direito válido e não vazio
        while (indiceFilhoDireito >= 0 && indiceFilhoDireito < this.nodes.length && // Verifica os limites primeiro
               !this.nodes[indiceFilhoDireito].equals("")) { // Depois verifica se está vazio
            indiceAtual = indiceFilhoDireito;
            indiceFilhoDireito = nodeRight(indiceAtual); // Pega o próximo filho direito para o novo atual
        }
        return indiceAtual; // Retorna o índice do maior nó na subárvore
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
