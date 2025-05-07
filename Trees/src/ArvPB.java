public class ArvPB extends ArvBin {

    public ArvPB(int n) {
        super(n);
    }

    @Override
    public void insert(String valor) {
        if (this.qtd >= this.nodes.length) {
            System.out.println("Capacidade máxima atingida.");
            return;
        }

        if (contains(valor)) return;

        this.nodes[this.qtd++] = valor;
        reconstruirPerfeita();
    }

    @Override
    public boolean remove(String valor) {
        int index = indexOf(valor);
        if (index == -1) return false;

        // Substituir por um filho (preferencialmente o esquerdo)
        int posNaArvore = findInTree(valor);
        if (posNaArvore != -1) {
            int filho = -1;
            if (nodeLeft(posNaArvore) < getMaxTam() && !getNode(nodeLeft(posNaArvore)).equals("")) {
                filho = nodeLeft(posNaArvore);
            } else if (nodeRight(posNaArvore) < getMaxTam() && !getNode(nodeRight(posNaArvore)).equals("")) {
                filho = nodeRight(posNaArvore);
            }

            if (filho != -1) {
                String substituto = getNode(filho);
                this.nodes[index] = substituto;
                removeElemento(substituto);
                this.qtd--;
            } else {
                // Não tem filhos, apenas remove
                removeElemento(valor);
                this.qtd--;
            }
        } else {
            removeElemento(valor);
            this.qtd--;
        }
        reconstruirPerfeita();
        return true;
    }


    public void reconstruirPerfeita() {
        // Copia e ordena os elementos válidos
        String[] copia = new String[this.qtd];
        System.arraycopy(this.nodes, 0, copia, 0, this.qtd);
        java.util.Arrays.sort(copia);
    
        // Limpa a árvore
        for (int i = 0; i < getMaxTam(); i++) {
            setNode(i, "");
        }
        newLen(0);
    
        // Insere recursivamente para balancear
        reconstruirPerfeitaRec(copia, 0, this.qtd - 1, 0);
        newLen(this.qtd);
    }
    
    // Insere recursivamente o elemento do meio como raiz/sub-raiz
    private void reconstruirPerfeitaRec(String[] arr, int ini, int fim, int pos) {
        if (ini > fim || pos >= getMaxTam()) return;
        int meio = (ini + fim) / 2;
        setNode(pos, arr[meio]);
        reconstruirPerfeitaRec(arr, ini, meio - 1, nodeLeft(pos));
        reconstruirPerfeitaRec(arr, meio + 1, fim, nodeRight(pos));
    }

    private void removeElemento(String valor) {
        int idx = indexOf(valor);
        if (idx == -1) return;

        for (int i = idx; i < this.qtd - 1; i++) {
            this.nodes[i] = this.nodes[i + 1];
        }
        this.nodes[this.qtd - 1] = "";
    }

    private boolean contains(String valor) {
        for (int i = 0; i < this.nodes.length; i++) {
            if (this.nodes[i].equals(valor)) return true;
        }
        return false;
    }

    private int indexOf(String valor) {
        for (int i = 0; i < this.nodes.length; i++) {
            if (this.nodes[i].equals(valor)) return i;
        }
        return -1;
    }

    private int findInTree(String valor) {
        for (int i = 0; i < getMaxTam(); i++) {
            if (getNode(i).equals(valor)) return i;
        }
        return -1;
    }

    protected int getMaxTam(){
        return this.nodes.length;
    }

    //atualiza a quantidade de nós
    protected void newLen(int newlen){
        this.qtd = newlen;
    }
}
