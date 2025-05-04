public class AVL extends ArvBin {
    int[] alturas;

    public AVL(int n){
        super(n);
        alturas = new int[n];
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

    private int max(int a, int b) {
        return (a > b) ? a : b;
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

        // Atualiza a altura do nó atual
        this.alturas[index] = 1 + max(altNo(nodeLeft(index)), altNo(nodeRight(index)));

        // Calcula o fator de balanceamento
        int balanceFactor = altNo(nodeLeft(index)) - altNo(nodeRight(index));

        // Verifica desbalanceamento e aplica rotações
        if (balanceFactor == -2) {
            // Desbalanceamento para a direita
            int rightChild = nodeRight(index);
            if (altNo(nodeLeft(rightChild)) <= altNo(nodeRight(rightChild))) {
                // Rotação simples à esquerda
                rotacaoEsquerda(index);
            } else {
                // Rotação dupla direita-esquerda
                rotacaoDireitaEsquerda(index);
            }
        } else if (balanceFactor == 2) {
            // Desbalanceamento para a esquerda
            int leftChild = nodeLeft(index);
            if (altNo(nodeLeft(leftChild)) >= altNo(nodeRight(leftChild))) {
                // Rotação simples à direita
                rotacaoDireita(index);
            } else {
                // Rotação dupla esquerda-direita
                rotacaoEsquerdaDireita(index);
            }
        }
    }

    private int altNo(int position){
        if (position < 0 || position >= this.nodes.length || nodes[position].equals("")) {
            return -1;
        }

        return(this.alturas[position]);
    }

    private void rotacaoEsquerda(int index) {
        int right = nodeRight(index);
        if (right == -1 || right >= this.nodes.length || this.nodes[right].equals("")) {
            return; // Não há filho direito para rotacionar
        }

        // Salva os valores temporariamente
        String tempValue = this.nodes[index];
        String rightValue = this.nodes[right];

        // Move o valor do filho direito para o nó atual
        this.nodes[index] = rightValue;

        // Ajusta os filhos
        int rightLeft = nodeLeft(right);
        if (rightLeft != -1 && rightLeft < this.nodes.length) {
            this.nodes[nodeRight(index)] = this.nodes[rightLeft];
            this.nodes[rightLeft] = "";
        } else {
            this.nodes[nodeRight(index)] = "";
        }

        this.nodes[nodeLeft(index)] = tempValue;

        // Atualiza as alturas
        this.alturas[right] = 1 + max(altNo(nodeLeft(right)), altNo(nodeRight(right)));
        this.alturas[index] = 1 + max(altNo(nodeLeft(index)), altNo(nodeRight(index)));
    }

    private void rotacaoDireita(int index) {
        int left = nodeLeft(index);
        if (left == -1 || left >= this.nodes.length || this.nodes[left].equals("")) {
            return; // Não há filho esquerdo para rotacionar
        }

        // Salva os valores temporariamente
        String tempValue = this.nodes[index];
        String leftValue = this.nodes[left];

        // Move o valor do filho esquerdo para o nó atual
        this.nodes[index] = leftValue;

        // Ajusta os filhos
        int leftRight = nodeRight(left);
        if (leftRight != -1 && leftRight < this.nodes.length) {
            this.nodes[nodeLeft(index)] = this.nodes[leftRight];
            this.nodes[leftRight] = "";
        } else {
            this.nodes[nodeLeft(index)] = "";
        }

        this.nodes[nodeRight(index)] = tempValue;

        // Atualiza as alturas
        this.alturas[left] = 1 + max(altNo(nodeLeft(left)), altNo(nodeRight(left)));
        this.alturas[index] = 1 + max(altNo(nodeLeft(index)), altNo(nodeRight(index)));
    }

    private void rotacaoEsquerdaDireita(int index) {
        int left = nodeLeft(index);
        rotacaoEsquerda(left);
        rotacaoDireita(index);
    }

    private void rotacaoDireitaEsquerda(int index) {
        int right = nodeRight(index);
        rotacaoDireita(right);
        rotacaoEsquerda(index);
    }

    @Override
    public boolean remove(String value) {
        int index = findIndex(0, value); // Encontra o índice do nó a ser removido
        if (index == -1) {
            return false; // Nó não encontrado
        }

        removeRecursive(index);
        this.qtd--;
        return true;
    }

    private void removeRecursive(int index) {
        if (index >= this.nodes.length || this.nodes[index].equals("")) {
            return; // Nó não existe
        }

        // Caso 1: Nó é uma folha
        if (nodeLeft(index) >= this.nodes.length || this.nodes[nodeLeft(index)].equals("")) {
            if (nodeRight(index) >= this.nodes.length || this.nodes[nodeRight(index)].equals("")) {
                this.nodes[index] = ""; // Remove o nó simplesmente
                this.alturas[index] = 0; // Atualiza a altura
                return;
            }
        }

        // Caso 2: Nó tem apenas um filho
        if (this.nodes[nodeLeft(index)].equals("")) { // Apenas filho direito
            this.nodes[index] = this.nodes[nodeRight(index)];
            removeRecursive(nodeRight(index));
        } else if (this.nodes[nodeRight(index)].equals("")) { // Apenas filho esquerdo
            this.nodes[index] = this.nodes[nodeLeft(index)];
            removeRecursive(nodeLeft(index));
        } else {
            // Caso 3: Nó tem dois filhos
            int successorIndex = findMin(nodeRight(index)); // Encontra o sucessor
            this.nodes[index] = this.nodes[successorIndex]; // Substitui pelo sucessor
            removeRecursive(successorIndex); // Remove o sucessor
        }

        // Atualiza a altura do nó atual
        this.alturas[index] = 1 + max(altNo(nodeLeft(index)), altNo(nodeRight(index)));

        // Calcula o fator de balanceamento
        int balanceFactor = altNo(nodeLeft(index)) - altNo(nodeRight(index));

        // Verifica desbalanceamento e aplica rotações
        if (balanceFactor == -2) {
            // Desbalanceamento para a direita
            int rightChild = nodeRight(index);
            if (altNo(nodeLeft(rightChild)) <= altNo(nodeRight(rightChild))) {
                // Rotação simples à esquerda
                rotacaoEsquerda(index);
            } else {
                // Rotação dupla direita-esquerda
                rotacaoDireitaEsquerda(index);
            }
        } else if (balanceFactor == 2) {
            // Desbalanceamento para a esquerda
            int leftChild = nodeLeft(index);
            if (altNo(nodeLeft(leftChild)) >= altNo(nodeRight(leftChild))) {
                // Rotação simples à direita
                rotacaoDireita(index);
            } else {
                // Rotação dupla esquerda-direita
                rotacaoEsquerdaDireita(index);
            }
        }
    }
}
