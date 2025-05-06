import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AVL extends ArvBin {
    int[] alturas;
    List<String> toRemove;

    public AVL(int n){
        super(n);
        alturas = new int[n];
        toRemove = new ArrayList<>();
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
    }


    private List<String> getSubTree(int position){ //Percorre a subarvore pelos niveis removendo os nós e guardando eles em ordem
        List <String> toRem = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        int index;
        int left;
        int right;
        
        if(position >= 0 && position < this.nodes.length && !this.nodes[position].equals("")){
            queue.add(position);
        }

        while(!queue.isEmpty()){
            index = queue.poll();
            toRem.add(this.nodes[index]);

            left = nodeLeft(index);
            right = nodeRight(index);

            if (left < this.nodes.length && !this.nodes[left].equals("")) {
                queue.add(left);
            }
    
            if (right < this.nodes.length && !this.nodes[right].equals("")) {
                queue.add(right);
            }
        }

        return toRem;
    }

    private void reBalance(List <String> nodesToInsert, boolean right_rotation, int root){
        int left = nodeLeft(root);
        int right = nodeRight(root);
        String father = this.nodes[root];
        String leftValue = "";
        String rightValue = "";

        if(left >= 0 && left < this.nodes.length){
            leftValue = this.nodes[left];
        }
        if(right >= 0 && right < this.nodes.length){
            rightValue = this.nodes[right];
        }

        int a;
        for(int cont = 0; cont < nodesToInsert.size(); cont++){
            a = findIndex(0, nodesToInsert.get(cont));
            this.nodes[a] = ""; 
        }

        if(right_rotation == true && leftValue != ""){
            this.nodes[root] = leftValue;
            this.nodes[nodeRight(root)] = father;
        }

        else if(rightValue!=""){
            this.nodes[root] = rightValue;
            this.nodes[nodeLeft(root)] = father;
        }

        // O pai e filho que ja foram inseridos não são inseridos novamentos, visto que não há repetições nas inserções
        for(int cont = 0; cont < nodesToInsert.size(); cont++){
            insert(nodesToInsert.get(cont));
        }
    }

    private void insertRecursive(int index, String value) {
        if (index >= this.nodes.length) {
            System.out.println("Erro: Não é possível inserir, array cheio.");
            return;
        }

        if (this.nodes[index].equals("")) {
            // Insere o valor na posição correta
            this.nodes[index] = value;
            this.qtd++;
        } else if (value.compareTo(this.nodes[index]) < 0) {
            // Vai para o filho esquerdo
            int left = nodeLeft(index);
            insertRecursive(left, value);
        } else if (value.compareTo(this.nodes[index]) > 0) {
            // Vai para o filho direito
            int right = nodeRight(index);
            insertRecursive(right, value);
        } else {
            // Valor já existe na árvore
            System.out.println("Valor já existe na árvore: " + value);
            return;
        }

        // Atualiza a altura de todos os nós da arvore
        heightRecursive(0);

        // Calcula o fator de balanceamento
        int balanceFactor = heightNode(nodeLeft(index)) - heightNode(nodeRight(index));

        // Verifica desbalanceamento e aplica rotações
        int rightChild;
        int leftChild;

        if (balanceFactor == -2) {
            // Desbalanceamento para a direita
            rightChild = nodeRight(index);
            if (heightNode(nodeLeft(rightChild)) <= heightNode(nodeRight(rightChild))) {
                rotacaoEsquerda(index); // Rotação simples à esquerda
            } else {
                rotacaoDireitaEsquerda(index); // Rotação dupla direita-esquerda
            }
        } 
        else if (balanceFactor == 2) {
            // Desbalanceamento para a esquerda
            leftChild = nodeLeft(index);
            if (heightNode(nodeLeft(leftChild)) >= heightNode(nodeRight(leftChild))) {
                rotacaoDireita(index); // Rotação simples à direita
            } else {
                rotacaoEsquerdaDireita(index); // Rotação dupla esquerda-direita
            }
        }
    }


    private int heightRecursive(int index){
        if(this.nodes[index].equals("")){
            this.alturas[index] = 0;
            return 0;
        }

        int heightLeft = 1 + heightRecursive(nodeLeft(index));
        int heightRight = 1 + heightRecursive(nodeRight(index));

        int aux = max(heightLeft, heightRight);
        this.alturas[index] = aux;

        return(aux);
    }

    private int heightNode(int position){
        if (position < 0 || position >= this.nodes.length || nodes[position].equals("")) {
            return -1;
        }

        return(this.alturas[position]);
    }

    private void rotacaoEsquerda(int index) {
        List <String> sub = getSubTree(index);
        reBalance(sub, false, index);
    }

    private void rotacaoDireita(int index) {
        List <String> sub = getSubTree(index);
        reBalance(sub, true, index);
    }

    private void rotacaoEsquerdaDireita(int index) {
        List <String> sub = getSubTree(index);
        reBalance(sub, false, index);
        sub = getSubTree(index);
        reBalance(sub, true, index);
    }

    private void rotacaoDireitaEsquerda(int index) {
        List <String> sub = getSubTree(index);
        reBalance(sub, true, index);
        sub = getSubTree(index);
        reBalance(sub, false, index);
    }

   
}
