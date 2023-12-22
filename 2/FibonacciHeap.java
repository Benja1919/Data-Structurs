/*
 * Ben Cohen, 208685784, bc1
 * Ori Avrahami, 315416057, oriavrahami
 */

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */


public class FibonacciHeap
{

    public static int total_links;
    public static int total_cuts;
    public HeapNode min;
    public HeapNode first;
    public int number_of_trees;
    public int number_of_marked;
    public int n;
    

    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *   
     */
    
    //Complexity - O(1) 
    public boolean isEmpty()
    {
    	//if the heap is empty, there is no first
        return this.first == null;
    }

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.  
     * 
     * Returns the newly created node.
     */
    
    //Complexity - O(1) 
    public HeapNode insert(int key)
    {
        HeapNode inserted = new HeapNode(key);
        //promote number of trees and nodes
        this.number_of_trees++;
        this.n++;
        if (this.isEmpty()) {
        	//new node is the first in the heap
            this.first = inserted;
            this.min = inserted;
            this.min.next = inserted;
            this.min.prev = inserted;
        }
        else {
        	//concatenate the new node to the heap
            inserted.next = this.first;
            inserted.prev = this.first.prev;
            inserted.prev.next = inserted;
            this.first.prev = inserted;
            this.first = inserted;
            if (inserted.key < this.min.key) {
            	//check if new min
            	this.min = inserted;
            }
        }
        return inserted;
    }

    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    
  //Complexity - O(log(n)) amortized and O(n) Worst-Case
    
    public void deleteMin()
    {
        if (this.isEmpty() || this.n == 1) {
        	//delete from empty or a singal node
        	this.min = null;
        	this.first  = null;
        	this.n = 0;
        	this.number_of_trees = 0;
            return;
        }
        //initialize buckets array
        HeapNode[] array_of_buckets = new HeapNode[Integer.toBinaryString(n).length()+1];
        //reduce num of nodes
        this.n--;

        HeapNode curr = this.first;
        int edge = this.number_of_trees;
        int i = 0;
        int rank = this.min.rank;
        int j = 0;

        while (i < edge) {
            if (curr == this.min) {
            	//add min children to buckets
                HeapNode curr_son = this.min.child;
                while (j < rank) {
                	//subtract num of marks
                    if (curr_son.mark) {
                        curr_son.mark = false;
                        this.number_of_marked--;
                    }
                    HeapNode next = curr_son.next;
                    //move over to the next son
                    curr_son.parent = null;
                    curr_son.prev = curr_son;
                    curr_son.next = curr_son;
                    while (array_of_buckets[curr_son.rank] != null) {
                    	//join the two trees using "meld_array" method
                        curr_son = meldArray(array_of_buckets[curr_son.rank], curr_son);
                        array_of_buckets[curr_son.rank-1] = null;
                    }
                    array_of_buckets[curr_son.rank] = curr_son;
                    curr_son = next;
                    j++;
                }
                curr = curr.next;
                i++;
            }
            
            else {
            	//add other trees to buckets
                HeapNode temp = curr.next;
                curr.parent = null;
                curr.prev = curr;
                curr.next = curr;
                while (array_of_buckets[curr.rank] != null) {
                	//join the two trees using "meld_array" method
                    curr = meldArray(array_of_buckets[curr.rank], curr);
                    array_of_buckets[curr.rank-1] = null;
                }
                array_of_buckets[curr.rank] = curr;
                curr = temp;
                i++;
            }
        }
        
        //adjust first and num_of_trees
        this.first = null;
        this.number_of_trees = 0;
        retrieve_from_buckets(array_of_buckets);
    }

    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     */
    
    //Complexity - O(1) 
    public HeapNode findMin()
    {
    	if (this.isEmpty()) {
    		//no min
    		return null;
    	}
        return this.min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    
    //Complexity - O(1) 
    public void meld (FibonacciHeap heap2)
    {
        if ((this.isEmpty() && heap2.isEmpty()) || heap2.isEmpty()) {
        	//check for edge case with empty heaps
            return;
        }
        else if (this.isEmpty()) {
        	//curr is empty, so values are from second heap
            this.first = heap2.first;
            this.min = heap2.findMin();
            this.number_of_trees = heap2.number_of_trees;
            this.number_of_marked = heap2.number_of_marked;
            this.n = heap2.size();
            return;
        }
        this.min.next.prev = heap2.min.prev;
        heap2.min.prev.next = this.min.next;
        this.min.next = heap2.min;
        heap2.min.prev = this.min;
        
        if (heap2.min.key < this.min.key) {
        	//check for min
        	this.min = heap2.min;
        }
        //adjust new values
        this.n += heap2.size();
        this.number_of_marked += heap2.number_of_marked;
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *   
     */
    
    //Complexity - O(1) 
    public int size()
    {
        return this.n;
    }

    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    
    //Complexity - O(n) 
    public int[] countersRep() {
    	if (this.isEmpty()) {
    		int[] emp_arr = new int[0];
    		return emp_arr;
    	}
        HeapNode temp = this.first;
        int max_rank = 0;
        do{
        	//check for max-rank
            if(temp.rank > max_rank){
                max_rank = temp.rank;
            }
            temp = temp.next;
        } while (temp != this.first);

        int[] arr = new int[max_rank+1];
        //intitalize array by max-rank
        HeapNode temp2 = this.first.next;
        arr[this.first.rank]++;

        while (temp2 != this.first) {
        	//going over the heap to add to arr
            arr[temp2.rank]++;
            temp2 = temp2.next;
        }

        return arr;
    }
    
    
    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
 	* It is assumed that x indeed belongs to the heap.
     *
     */
    
    //Complexity - O(log(n)) amortized , and O(n) Worst-Case 
    
    public void delete(HeapNode x)
    {
        x.key = this.min.key - 1;
        //set x to min and then decrease
        this.decreaseKey(x, 1);
        this.deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    
    //Complexity - O(1)     
    public void decreaseKey(HeapNode x, int delta)
    {
        x.key = x.key - delta;
        if (x.key < this.min.key) {
        	this.min = x;
        }
        if ((x.parent == null) || (x.parent.key < x.key)) {
        	//we break here, no need to proceed cutting
            return;
        }
        this.cascading_cut(x);
    }
    

    /**
     * public int potential() 
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * 
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap. 
     */
    
    //Complexity - O(1) 
    public int potential()
    {
        return this.number_of_trees + 2*this.number_of_marked;
    }

    /**
     * public static int totalLinks() 
     *
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    
    //Complexity - O(1) 
    public static int totalLinks()
    {
        return total_links;
    }

    /**
     * public static int totalCuts() 
     *
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods). 
     */
    
    //Complexity - O(1) 
    public static int totalCuts()
    {
        return total_cuts;
    }

    /**
   * public static int[] kMin(FibonacciHeap H, int k) 
   *
   * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
   * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
   *  
   * ###CRITICAL### : you are NOT allowed to change H. 
   */
    
   //Complexity - O(k*deg(H)) 
   public static int[] kMin(FibonacciHeap H, int k)
    {
        if (k <= 0) {
        	//return empty array
            return new int[0];
        }
        
        int[] k_smallest_arr = new int[k];

        if (k == 1) {
        	//only min
            k_smallest_arr[0] = H.min.key;
        }

        else {
            FibonacciHeap k_min_heap = new FibonacciHeap();
            //intialize new heap in order to not modify H
            k_min_heap.insert(H.min.key);
            k_min_heap.min.temp_pointer = H.min;
            int index = 0;
            while (index < k) {
                k_smallest_arr[index] = k_min_heap.min.key; // heap min
                HeapNode child = k_min_heap.min.temp_pointer.child;
                k_min_heap.deleteMin();
                if (child != null && child.key != k_smallest_arr[index]) {
                	//check if to continue
                    HeapNode lastChild = child;
                    while (child.next != lastChild) {
                    	//insert
                        k_min_heap.insert(child.key).temp_pointer = child;
                        child = child.next;
                    }
                    k_min_heap.insert(child.key).temp_pointer = child;
                }
                index++;
            }
        }
        return k_smallest_arr;
    }
   
	/**********************************************************************************
	 *
	 * 
	 * 						ADDED 				METHODS
	 * 
	 * 
	 * 
	 **********************************************************************************
	 */
   
   //Complexity - O(log(n) 
   //Going over each cell of the buckets and connecting them to a heap
   public void retrieve_from_buckets(HeapNode[] array_of_buckets) {

       HeapNode current = this.first;
       for (HeapNode cell : array_of_buckets) {
       	//going over each cell in the bucket
           if (cell != null) {
               this.number_of_trees++;
               if (this.first == null) {
            	   //initialize first and min
                   this.first = cell;
                   current = this.first;
                   this.min = first;
               }
               //insert to heap
               current.next = cell;
               cell.prev = current;
               current = current.next;
               if (current.key < this.min.key) {
               	//check for min
               	this.min = current;
               }
           }
       }
       //re-attach
       current.next = first;
       first.prev = current;
   }
   
   //Complexity - O(1) amortized, O(log(n)) Worst-Case
   //recursive method for cascading_cut until no parent marked
   public void cascading_cut(HeapNode node) {
       HeapNode temp_parent = node.parent;
       cut(node);
       if (temp_parent.parent == null) {
    	   //break
           return;
       }
       else if (!temp_parent.mark) {
    	   //mark father and promote num of marks
           temp_parent.mark = true;
           number_of_marked++;
           return;
       }
       cascading_cut(temp_parent);
   }
   
   //Complexity - O(1) 
   //Method for cut node from it's parent
   public void cut(HeapNode node) {
       total_cuts++;
       this.number_of_trees++;
       HeapNode temp_parent = node.parent;
       temp_parent.rank--;
       
       if (node.mark == true) {
       	//remove mark
           this.number_of_marked--;
           node.mark = false;
       }
       if (node.next == node) {
       	//no brother
           temp_parent.child = null;
       }
       else {
           if (temp_parent.child==node) {
               temp_parent.child=node.next;
           }
           node.prev.next = node.next;
           node.next.prev = node.prev;
       }
       //adjust first
       node.parent = null;
       node.next = this.first;
       first.prev.next = node;
       node.prev = this.first.prev;
       this.first.prev = node;
       this.first = node;
   }

   //Complexity - O(1) 
   //Concatenate between two binom trees
   public HeapNode meldArray(HeapNode node_1, HeapNode node_2) {
       total_links++;
       this.number_of_trees--;
       HeapNode higher;
       HeapNode lower;
       if (node_1.getKey() > node_2.getKey()) {
       	//check higher/lower between the two nodes
       	higher = node_2;
       	lower = node_1;
       }
       else {
       	higher = node_1;
       	lower = node_2;
       }
       if (higher.child == null) {
           higher.child = lower;
       }
       else {
       	//concatenate children
           HeapNode highChild = higher.child;
           higher.child = lower;
           lower.next = highChild;
           highChild.prev.next = lower;
           lower.prev = highChild.prev;
           highChild.prev = lower;
       }
       lower.parent = higher;
       higher.rank++;
       return higher;
   }
   

   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public class HeapNode{

        public int key;
        public int rank = 0;
        public boolean mark = false;
        public HeapNode child;
        public HeapNode next = this;
        public HeapNode prev = this;
        public HeapNode parent;
        
        //to be used in kMin_method
        public HeapNode temp_pointer = null;

        public HeapNode(int key) {
            this.key = key;
        }

        public int getKey() {
            return this.key;
        }

    }
}