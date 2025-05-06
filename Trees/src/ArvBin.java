public class ArvBin implements Arv{
    public String[] nodes;
    public int qtd;
    public int kind;

    public ArvBin(int n){
        this.nodes = new String[n];
        for (int i = 0; i < n; i++) {
            this.nodes[i] = ""; // Inicializa cada posição com uma string vazia
        }

        this.qtd = 0;
        this.kind = 0; //para o tipo "ABB"
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
        } 
        else if (value.compareTo(this.nodes[index]) < 0) {
            insertRecursive(nodeLeft(index), value);
        } 
        else if (value.compareTo(this.nodes[index]) > 0) {
            insertRecursive(nodeRight(index), value);
        } 
        else {
            // Valor já existe na árvore
            System.out.println("Valor já existe na árvore: " + value);
        }
    }

    protected boolean isBalance(){
        if(this.kind == 0) return(true);
        else return false; //codar o resto disso aqui 
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
        dot.append("digraph Arvore {\n");

        for (int i = 0; i < this.nodes.length; i++) {
            int left = nodeLeft(i);
            int right = nodeRight(i);

            if(left != -1 && right != -1){
                if (!this.nodes[nodeLeft(i)].equals("") && !this.nodes[nodeRight(i)].equals("")) {
                    
                    dot.append("\"").append(i+" ").append(this.nodes[i]).append("\"");
                    if (left < this.nodes.length && left != -1 && !this.nodes[left].equals("")) {
                        dot.append(" ->").append(left+" "+this.nodes[left]+"\"\n");
                    }

                    dot.append("\"").append(i+" ").append(this.nodes[i]).append("\"");
                    if (right < this.nodes.length && right != -1 && !this.nodes[right].equals("")) {
                        dot.append(" ->").append(right+" "+this.nodes[right]+"\"\n");
                    }
                }
            }

            else{ 
                if(left != -1){ 
                    if (!this.nodes[nodeLeft(i)].equals("")) {
                        // Adiciona o nó atual
                        dot.append("\"").append(i+" ").append(this.nodes[i]).append("\"");
                        // Adiciona a aresta para o filho esquerdo, se existir
                        if (!this.nodes[left].equals("")) {
                            dot.append(" ->").append(left+" "+this.nodes[left]+"\"\n");
                        }
                    }
                }

                else if(right != -1){
                    if (!this.nodes[nodeRight(i)].equals("")) {

                        dot.append("\"").append(i+" ").append(this.nodes[i]).append("\"");
                        if (!this.nodes[right].equals("")) {
                            dot.append(" ->").append(right+" "+this.nodes[right]+"\"\n");
                        }
                    }
                }   
            }
        }

        dot.append("}\n");
        return dot.toString();
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
