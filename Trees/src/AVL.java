/*
NOMES:
HENRIQUE VIEIRA LIMA    NUSP: 15459372
GABRIEL PHILIPPE PRADO  NUSP: 15453730

*/

import java.util.List;

public class AVL extends ArvBin {

   public AVL(int len) {
      super(len);
      this.kind = 2; 
   }

   @Override
   public void insert(String value) {
      super.insert(value);
      super.isBalance(); 
      this.checkNodeListBalance(); 
   }

   @Override
   public boolean remove(String value) {
      super.remove(value);
      this.checkNodeListBalance();
      return true;
   }

   @Override
   public boolean isBalance(){
      return this.checkNodeListBalance();
   }


   private boolean nodeIsBalanced(int index) {
      int bf = this.getBalancing(index);
      if (Math.abs(bf) > 1) return false;
      else return true;
   }

   private int getBalancing(int index) {
      if (index > this.lastNodeIndex) {
         return 0;
      }
      int leftHeight = this.getHeightRecursive(nodeLeft(index));
      int rightHeight = this.getHeightRecursive(nodeRight(index));
      return leftHeight - rightHeight;
   }

   private int getHeightRecursive(int index) {
      if ((index > this.lastNodeIndex || index == -1)) return 0;
      if (this.nodes[index].equals("")) return 0;

      int leftHeight = 1 + this.getHeightRecursive(nodeLeft(index));
      int rightHeight = 1 + this.getHeightRecursive(nodeRight(index));

      if (leftHeight > rightHeight) return leftHeight;
      else return rightHeight;
   }

   private void makeRotation(int index) {
      if (index > this.lastNodeIndex) return;

      int rbf = this.getBalancing(index);

      if (rbf > 0) {
         int lb = this.getBalancing(nodeLeft(index));

         if (lb > 0) {
               this.rotationLogic(index, true);
         } 
         else {
            this.rotationLogic(nodeLeft(index), false);
            this.findLastNodeIndex(); 
            this.rotationLogic(index, true);
         }

      } 
      
      else {
         int rb = this.getBalancing(nodeRight(index));

         if (rb < 0) {
            this.rotationLogic(index, false);
         } 
         
         else {
            this.rotationLogic(nodeRight(index), true);
            this.findLastNodeIndex();
            this.rotationLogic(index, false);
         }
      }

      this.findLastNodeIndex();
   }

   private void rotationLogic(int rootIndex, boolean rightRotation) {
      List<String> UnbalancedSubTreeNodes = this.getSubTree(rootIndex);
      int leftChildIndex = this.nodeLeft(rootIndex);
      int rightChildIndex = this.nodeRight(rootIndex);
      String leftSon = "";
      String rightSon = "";

      if (leftChildIndex > -1 && leftChildIndex < this.nodes.length)
         leftSon = this.nodes[leftChildIndex];
      if (rightChildIndex > -1 && rightChildIndex < this.nodes.length)
         rightSon = this.nodes[rightChildIndex];

      this.removeNodes(UnbalancedSubTreeNodes);

      if (rightRotation && !leftSon.equals("")) {
         this.insertDefault(leftSon);
      } 
      
      else if (!rightSon.equals("")) {
         this.insertDefault(rightSon);
      }

      this.insertDefault(UnbalancedSubTreeNodes.get(0));
      this.insertListDefault(UnbalancedSubTreeNodes);
      this.qtd -= UnbalancedSubTreeNodes.size();
   }

   private void insertDefault(String value) {
      super.insert(value);
   }

   private void insertListDefault(List<String> list) {
      for (int i = 0; i < list.size(); i++)
         this.insertDefault(list.get(i));
   }

   private boolean checkNodeListBalance() {
      for (int i = this.lastNodeIndex; i >= 0; i--) {
         if (!this.nodeIsBalanced(i)) {
            this.makeRotation(i);
            return true;
         }
      }
      return false;
   }
}