
/*
 * Ben Cohen, 208685784, bc1
 * Ori Avrahami, 315416057, oriavrahami
 */

/**
*
* AVLTree
*
* An implementation of a AVL tree with a
* distinct integer keys and info.
*
*/


public class AVLTree {
	
	//creates private values for each tree
	//we want these values to be maintained so we can get them by O(1) actions
	 //these values are implemented in the prog
	
	private IAVLNode minimum; 
	private IAVLNode maximum; 
	private IAVLNode root; 

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 * O(1)
	 */
	public boolean empty() {
		//we want to check if the tree does not exist or if its root is not a real node
		return this.root == null || !this.root.isRealNode();
	}

	 /**
	   * public String search(int k)
	   *
	   * Returns the info of an item with key k if it exists in the tree.
	   * otherwise, returns null.
	   */
	
	/*Comlexity = O(log(n))*/
	
	
	public String search(int k)
	{
		boolean bool = this.empty();
		if(bool == true) {
			
			return null;
		}
		IAVLNode x = this.root;
		return search_recursive_for_val(k, x);
	}
	
	  /**
	   * public int insert(int k, String i)
	   *
	   * Inserts an item with key k and info i to the AVL tree.
	   * The tree must remain valid, i.e. keep its invariants.
	   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	   * Returns -1 if an item with key k already exists in the tree.
	   */
	
	/*Comlexity = O(log(n))*/
	
	public int insert(int k, String i) {
		//first we will check if the tree is empty
		if (this.empty()) { 
			IAVLNode node_1 = new AVLNode(k, i);
			this.root = node_1;
			if(node_1 !=null){
				node_1.setParent(null);
			}
			node_1.virtualize();
			node_1.adjustheight();
			node_1.adjust_size();
			node_1.adjust_balance();
			this.minimum = node_1;
			this.maximum = node_1;
			return 0;
		}
		else if (this.search(k) != null){ 
			return -1;
		}
		else{
			IAVLNode node_new = new AVLNode(k, i);
			node_new.virtualize(); 
			regular_insert(node_new); 
			IAVLNode node_1 = node_new.getParent();
			int couter = 0; 
			couter = balance2_for_insert_and_join(node_1);
			//we will return the counter of rebalance actions been made
			return couter;
		}
	}

	  /**
	   * public int delete(int k)
	   *
	   * Deletes an item with key k from the binary tree, if it is there.
	   * The tree must remain valid, i.e. keep its invariants.
	   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	   * Returns -1 if an item with key k was not found in the tree.
	   */
	
	/*Comlexity = O(log(n))*/
	
	public int delete(int k)
	{
		int counter = 0;
		if(search_this_node(k) == null) {
			return -1;
		}
		IAVLNode deleted = search_this_node(k);
		//creates new IAVLNodes
		IAVLNode current_parrent = delete_regular(deleted);
		if(this.empty() == true){
			//update
			this.root = null;
			this.maximum = null;
			this.minimum = null;
			return 0;
			//update
		}
		int to_insert = current_parrent.getLeft().getHeight() - current_parrent.getRight().getHeight();
		int new_balancing = to_insert;
		if(new_balancing != 1 && new_balancing != -1){
			counter = balance1_for_delete(current_parrent);
			//first rebalance
		}
		else {
			while (current_parrent != null){
				current_parrent.adjustheight();
				current_parrent.adjust_size();
				current_parrent.adjust_balance();
				current_parrent = current_parrent.getParent();
			}
		}
		if(k == this.minimum.getKey()){
			if(!this.root.isRealNode())
				this.minimum = this.root;
			IAVLNode node = this.root;
			while (node.getLeft().isRealNode()){
				node = node.getLeft();
			}	
		}
		if(k == this.maximum.getKey()){
			if(!this.root.isRealNode())
				this.maximum = this.root;
			IAVLNode node = this.root;
			while (node.getRight().isRealNode()){
				node = node.getRight();
			}
		}
		//returns counter
		return counter;
	}

	  /**
	   * public String min()
	   *
	   * Returns the info of the item with the smallest key in the tree,
	   * or null if the tree is empty.
	   */
	
	/*Comlexity = O(1)*/
	
	public String min()
	{
		if (!this.empty() == true)
			// we will now find the minimum info to retrieve
			return this.minimum.getValue();
		//if we got here that means we return null
		return null;
	}

	  /**
	   * public String max()
	   *
	   * Returns the info of the item with the largest key in the tree,
	   * or null if the tree is empty.
	   */
	
	/*Comlexity = O(1)*/
	
	public String max()
	{
		if (!this.empty())
			// we will now find the maximum info to retrieve
			return this.maximum.getValue();
		//if we got here that means we return null
		return null;
	}

	 /**
	  * public int[] keysToArray()
	  *
	  * Returns a sorted array which contains all keys in the tree,
	  * or an empty array if the tree is empty.
	  */
	
	/*Comlexity = O(n)*/
	
	public int[] keysToArray()
	  {
		  if (!this.root.isRealNode()) {
			  //empty
			  int[] emp_arr = new int[0];
			  return emp_arr;
		  }
	      IAVLNode s;
	      int index = 1;
		  int to_size = this.root.get_size();
	      int[] arr = new int[to_size];
	      IAVLNode node = this.minimum;
	      arr[0] = node.getKey();
	      while (true) {
	    	   //n times successor
	           s = Successor(node);
	           if (s == null) {
	        	 //reaching the end
	               break;
	           }
	           arr[index] = s.getKey();
	           node = s;
	           index++;
	       }
		return arr;
	  }
	
	  /**
	   * public String[] infoToArray()
	   *
	   * Returns an array which contains all info in the tree,
	   * sorted by their respective keys,
	   * or an empty array if the tree is empty.
	   */
	
	/*Comlexity = O(n)*/
	
	public String[] infoToArray()
	  {
		  if (!this.root.isRealNode()) {
			  //empty
			  String[] emp_arr = new String[0];
			  return emp_arr;
		  }
	      IAVLNode s;
	      int index = 1;
	      String[] arr = new String[this.root.get_size()];
	      IAVLNode node = this.minimum;
	      arr[0] = node.getValue();
	      while (true) {
	    	 //n times successor
	           s = Successor(node);
	           if (s == null) {
	        	   //reaching the end
	               break;
	           }
	           arr[index] = s.getValue();
	           node = s;
	           index++;
	       }
		return arr;
	  }

	   /**
	    * public int size()
	    *
	    * Returns the number of nodes in the tree.
	    */
	
	/*Comlexity = O(1)*/
	
	public int size()
	{
		if (!this.empty() == true) {
			//it has a size we can retrieve
			return this.root.get_size();
		}
		else {
		return 0;
		}
	}

	   /**
	    * public int getRoot()
	    *
	    * Returns the root AVL node, or null if the tree is empty
	    */
	
	/*Comlexity = O(1)*/
	
	public IAVLNode getRoot()
	{
		//also taken care of null
		return this.root;
	}
	
  	   /**
	    * public AVLTree[] split(int x)
	    *
	    * splits the tree into 2 trees according to the key x. 
	    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	    * 
		* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	    * postcondition: none
	    */ 
	
	/*Comlexity = O(log(n))*/
	
	public AVLTree[] split(int x)
	{
		IAVLNode connector = search_this_node(x);
		//initializing two new trees
		AVLTree big = new AVLTree();
		AVLTree small = new AVLTree();
		if (connector.getRight().isRealNode()){
			//adjusting big's root and connector right's parent
			big.root = connector.getRight();
			if(connector.getRight() !=null){
				connector.getRight().setParent(null);
			}
		}
		if (connector.getLeft().isRealNode()){
			//adjusting small's root and connector left's parent
			small.root = connector.getLeft();
			if(connector.getLeft() !=null){
				connector.getLeft().setParent(null);
			}
			
		}
		while (connector.getParent()!=null) { 
			if (connector.getParent().getKey() < connector.getKey()) {
				//s right son
				IAVLNode node = new AVLNode(connector.getParent().getKey(), connector.getParent().getValue());
				AVLTree tree = new AVLTree();
				if (connector.getParent().getLeft().isRealNode())
					tree.root = connector.getParent().getLeft();
				if(connector.getParent().getLeft() !=null){
					connector.getParent().getLeft().setParent(null);
				}
				//joins the trees
				small.join(node, tree);
				connector = connector.getParent();
			}
			else {
				//s left son
				IAVLNode node = new AVLNode(connector.getParent().getKey(), connector.getParent().getValue());
				AVLTree tree = new AVLTree();
				if (connector.getParent().getRight().isRealNode())
					tree.root = connector.getParent().getRight();
				if(connector.getParent().getRight() !=null){
					connector.getParent().getRight().setParent(null);
				}
				//joins the trees
				big.join(node, tree);
				connector = connector.getParent();
			}
		}
		//set min and max of new trees (each operation O(logn))
		small.adjust_min();
		small.adjust_max();
		big.adjust_min();
		big.adjust_max();
		AVLTree[] arr = {small, big};
		//returns the array with the trees//
		return arr;
	}

	   /**
	    * public int join(IAVLNode x, AVLTree t)
	    *
	    * joins t and x with the tree. 	
	    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
		*
		* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
	    * postcondition: none
	    */ 
	
	/*Comlexity = O(log(n))*/
	
	public int join(IAVLNode x, AVLTree t)
	{
		IAVLNode connector = x;
		AVLTree tree = t;
		int value_to_return = 0;
		if(this.empty() && tree.empty()){
			//case when both trees are empty
			this.insert(connector.getKey(), connector.getValue());
			return 1;
		}
		else if(this.empty() && !tree.empty()){
			//case when current tree is empty and the second isn't
			tree.insert(connector.getKey(), connector.getValue());
			this.root = tree.getRoot();
			this.maximum = tree.maximum;
			this.minimum = tree.minimum;
			return tree.getRoot().getHeight() + 1;
		}
		else if(!this.empty() && tree.empty()){
			//case when the current tree isn't empty, and the given is empty
			this.insert(connector.getKey(), connector.getValue());
			int complexity = this.getRoot().getHeight() + 1;
			return complexity;
		}
		else{
			int current_prev = this.getRoot().getHeight();
			int other_prev = tree.getRoot().getHeight();
			//we will return the ranks difference
			value_to_return = Math.abs(current_prev - other_prev) + 1;
			if (tree.getRoot().getKey()< this.getRoot().getKey()){
				//checks for "higher_case"
				if(tree.getRoot().getHeight() < this.getRoot().getHeight()){
					this.case_higher(tree, connector);
				}
				else if(tree.getRoot().getHeight() > this.getRoot().getHeight()){
					//checks for "lower_case"
					this.case_shorter(tree,connector);
					this.root = tree.getRoot();
					if(tree.getRoot() !=null){
						tree.getRoot().setParent(null);
					}
				}
				else {
					this.case_equal(tree, this, connector);
				}
				this.minimum = tree.minimum;
			}
			else if (tree.getRoot().getKey()>this.getRoot().getKey()){
				if(tree.getRoot().getHeight() > this.getRoot().getHeight()){
					//checks for "higher_case"
					tree.case_higher(this, connector);
					this.root = tree.getRoot();
					if(tree.getRoot() !=null)
					{
						tree.getRoot().setParent(null);
					}
				}
				else if(tree.getRoot().getHeight() < this.getRoot().getHeight()){
					//checks for "lower_case"
					tree.case_shorter(this, connector);
				}
				else{
					this.case_equal(this, tree, connector);
				}
				this.maximum = tree.maximum;
			}
			balance2_for_insert_and_join(connector.getParent());
		}
		
		//return the rank-difference
		return value_to_return;
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
	
	/*method for adjusting tree's max
	 *Complexity = O(log(n))
	 */
	private void adjust_max(){
		if (this.empty())
			return;
		IAVLNode max = this.getRoot();
		if(!max.isRealNode())
			this.maximum = max;
		while(max.getRight().isRealNode()){
			max = max.getRight();
		}
		this.maximum = max;
	}

	
	/*method for adjusting tree's min
	 *Complexity = O(log(n))
	 */
	private void adjust_min(){
		if (this.empty())
			return;
		IAVLNode min = this.getRoot();
		if(!min.isRealNode())
			this.minimum = min;
		while (min.getLeft().isRealNode()){
			min = min.getLeft();
		}
		this.minimum = min;
	}
	
	/*supported search method for searching node
	 *Complexity = O(log(n))
	 */
	private IAVLNode search_this_node(int p)
	{
		//helper function for search method
		boolean bool = !this.empty();
		if(bool == false) {
			//returns null
			return null;
		}
		
		//checks it using the recursion
		IAVLNode x = this.root;
		return search_recursive_for_node(p, x);
	}
	
	/*method for recursive search
	 * in which we return the value of the node wer'e looking for
	 *Complexity = O(log(n))
	 */
	private String search_recursive_for_val(int p, IAVLNode node){
		
		int key = node.getKey();
		if(key == -1) {
			//no key found
			return null;
		}
		else if(key == p) {
			String value = node.getValue();
			return value;
		}
		else if (p > key) {
			IAVLNode right = node.getRight();
			// we need to search recursivly
			return search_recursive_for_val(p, right);
		}
		else {
			IAVLNode left = node.getLeft();
			// we need to search recursivly
			return search_recursive_for_val(p, left);
		}
	}

	/*method for recursive search
	 * in which we return the node wer'e looking for
	 *Complexity = O(log(n))
	 */
	
	private IAVLNode search_recursive_for_node(int p, IAVLNode node){
		int key = node.getKey();
		if(key == p) 
		{
			//returns the root
			return node;
		}
		if(key == -1)
		{
			//returns null
			return null;
		}
		if (p < node.getKey())
		{
			//we will take the left child of the root
			IAVLNode left = node.getLeft();
			return search_recursive_for_node(p, left); }
		
		else {
			
			//we will take the right child of the root
			IAVLNode Right = node.getRight();
			return search_recursive_for_node(p, Right);
		}

	}
	
	/*method for switching positions, supports the Delete method
	 * Complexity = O(1)
	 */
	private void switchthis(IAVLNode first, IAVLNode second){
		if(first.getParent() == null) {
			this.root = second;
		}
		else if(first.equals(first.getParent().getLeft())) {
			first.getParent().setLeft(second);
		}
		else
			first.getParent().setRight(second);
		if(second != null)
			second.setParent(first.getParent());

	}
	
	/*method for balancing the avl tree. it's the first method used to rebalance for delete
	 *Complexity = O(log(n))
	 */
	private int balance1_for_delete(IAVLNode n){
		int counter = 0;
		while (n != null && !n.check_my_balance()){
			if(n.getLeft().getHeight() - n.getRight().getHeight() == -2){
				if(n.getRight().getbalance() == 0){
					//left rotation needed
					n = rotate_to_the_left(n);
					counter+=3;
				}
				else if(n.getRight().getbalance() == -1){
					//left rotation needed
					n = rotate_to_the_left(n);
					counter+=3;
				}
				else{
					//double rotation, first right then left
					n = rotate_to_the_right(n.getRight());
					n = rotate_to_the_left(n.getParent());
					counter += 6;
				}
			}
			else if(n.getLeft().getHeight() - n.getRight().getHeight() == 2){
				if(n.getLeft().getbalance() == 0){
					// right rotation needed
					n = rotate_to_the_right(n);
					counter+=3;
				}
				else if(n.getLeft().getbalance() == 1){
					//right rotation needed
					n = rotate_to_the_right(n);
					counter+=3;
				}
				else{
					//double rotation, first left then right
					n = rotate_to_the_left(n.getLeft());
					n = rotate_to_the_right(n.getParent());
					counter += 6;
				}
			}
			else{
				//adjust values
				n.adjustheight();
				n.adjust_size();
				n.adjust_balance();
				counter++;
			}
			n = n.getParent();
		}
		//adjust values
		while (n != null){
			n.adjustheight();
			n.adjust_size();
			n.adjust_balance();
			n = n.getParent();
		}
		return counter;

	}
	
	/*method for balancing the avl tree. it's the second method used to rebalance for insert
	 *Complexity = O(log(n))
	 */
	private int balance2_for_insert_and_join(IAVLNode n){
		int counter =0;
		while (n != null){
			n.adjust_balance();
			if (Math.abs(n.getbalance())>1) { 
				if (n.getbalance() == 2 && n.getLeft().getbalance() == 1) {
					//right rotation needed
					n = rotate_to_the_right(n);
					counter += 2+balance2_for_insert_and_join(n.getParent());
				} else if (n.getbalance() == -2 && n.getRight().getbalance() == -1) {
					//left rotation needed
					n = rotate_to_the_left(n);
					counter += 2+balance2_for_insert_and_join(n.getParent());;
				}
				else if (n.getbalance() == 2 && n.getLeft().getbalance() == -1) {
					//double rotation, first to left then to the right
					n = rotate_to_the_left(n.getLeft());
					n = rotate_to_the_right(n.getParent());
					counter += 5+balance2_for_insert_and_join(n.getParent());;
				} else if (n.getbalance() == -2 && n.getRight().getbalance() == 1) {
					//double rotation, first to the right then to the left
					n = rotate_to_the_right(n.getRight());
					n = rotate_to_the_left(n.getParent());
					counter += 5+balance2_for_insert_and_join(n.getParent());;
				} else if (n.getbalance() == 2 && n.getLeft().getbalance() == 0) {
					//right rotation needed
					n = rotate_to_the_right(n);
					counter += 2+balance2_for_insert_and_join(n.getParent());;
				} else if (n.getbalance() == -2 && n.getRight().getbalance() == 0) {
					//left rotation needed
					n = rotate_to_the_left(n);
					counter += 2+balance2_for_insert_and_join(n.getParent());;
				}
				while (n != null){
					//adjust new values
					n.adjustheight();
					n.adjust_size();
					n.adjust_balance();
					n = n.getParent();
				}
				return counter; 
			}
			else if(n.getHeight() == n.getLeft().getHeight() || n.getHeight() == n.getRight().getHeight()){
				//promote is needed
				n.adjustheight();
				n.adjust_size();
				n.adjust_balance();
				counter +=1;
				n = n.getParent();
			}
			else{
				while (n != null){
					//adjust new values
					n.adjustheight();
					n.adjust_size();
					n.adjust_balance();
					n = n.getParent();
				}
				return counter;
			}
		}

		return counter;
	}

	/*method supports the delete action. it deletes the node as in binary tree
	 *Complexity = O(log(n))
	 */
	private IAVLNode delete_regular(IAVLNode to_delete){
		IAVLNode parent = null;
		if(!to_delete.getLeft().isRealNode()){
			switchthis(to_delete, to_delete.getRight());
			if(to_delete.getRight().getParent() == null)
			{
				//im root
				parent = this.root;
			}
			else {
				//im not the root, i have a parent
			parent =  to_delete.getRight().getParent();
			}
		}
		else if(!to_delete.getRight().isRealNode()){
			switchthis(to_delete, to_delete.getLeft());
			if(to_delete.getLeft().getParent() == null)
			{
				//im root
				parent = this.root;
			}
			else {
				//im not the root, i have a parent
			parent =  to_delete.getLeft().getParent();
			}
		}
		else{
			IAVLNode p = to_delete.getRight();
			while (p.getLeft().isRealNode()) {
				//he's in the tree and not virtual node
				//looking for successor
				p = to_delete.getLeft();
			if(p.getParent() == null)
			{
				//im root
				parent = this.root;
			}
			else {
				//im not the root, i have a parent
			parent =  p.getParent();
			}
			if(!p.getParent().equals(to_delete)){
				switchthis(p, p.getRight());
				p.setRight(to_delete.getRight());
				p.getRight().setParent(p);

			}
			else{
				parent = p;
			}
			switchthis(to_delete, p);
			p.setLeft(to_delete.getLeft());
			p.getLeft().setParent(p);
			p.adjustheight();
			p.adjust_size();
			p.adjust_balance();
		}
		}
		return parent;
	}

	
	/*method supports the insert action. it inserts the node as in binary tree
	 *Complexity = O(log(n))
	 */
	private void regular_insert(IAVLNode z){
		int key = z.getKey();
		IAVLNode x = this.root;
		IAVLNode y = null;
		while (x.isRealNode()){
			//regular insertion like binary search tree
			y = x;
			if (x.getKey() > key)
				x = x.getLeft();
			else
				x = x.getRight();
		}
		z.setParent(y);
		if (z.getKey()<y.getKey())
			y.setLeft(z);
		else
			y.setRight(z);
		//adjusting the min and max values
		if (this.maximum == null)
			this.adjust_max();
		if (this.minimum == null)
			this.adjust_min();
		if (z.getKey()<this.minimum.getKey())
			this.minimum = z;
		if (z.getKey()>this.maximum.getKey())
			this.maximum = z;
	}


	/*method for rotating the tree. it's the first method, which rotate to the left
	 *Complexity = O(1)
	 */
	private IAVLNode rotate_to_the_left(IAVLNode rotated){
		IAVLNode p_parent = rotated.getParent();
		IAVLNode y = rotated.getRight(); 
		rotated.setRight(y.getLeft());
		y.setLeft(rotated);
		//connect
		if (p_parent == null) {
			this.root = y;
			if(y !=null){
				y.setParent(null);
			}
		}
		else {
			
			y.setParent(p_parent);
			if (y.getKey() < p_parent.getKey()) {
				p_parent.setLeft(y);
			} else {
				p_parent.setRight(y);
			}
		}
		//done connecting
		rotated.getRight().setParent(rotated);
		y.getLeft().setParent(y);
		//adjusting new values
		rotated.adjustheight();
		rotated.adjust_size();
		rotated.adjust_balance();
		y.adjustheight();
		y.adjust_size();
		y.adjust_balance();
		return y; 
	}

	/*method for rotating the tree. it's the second method, which rotate to the right
	 *Complexity = O(1)
	 */
	private IAVLNode rotate_to_the_right(IAVLNode rotated){ 
		IAVLNode y = rotated.getLeft();
		IAVLNode p_parent = rotated.getParent();
		rotated.setLeft(y.getRight());
		y.setRight(rotated);
		//connect
		if (p_parent == null) {
			this.root = y;
			if(y !=null){
				y.setParent(null);
			}
		}
		else {
			
			y.setParent(p_parent);
			if (y.getKey() < p_parent.getKey()) {
				p_parent.setLeft(y);
			} else {
				p_parent.setRight(y);
			}
		}
		//done connecting
		rotated.getLeft().setParent(rotated);
		y.getRight().setParent(y);
		//adjusting new values
		rotated.adjustheight();
		rotated.adjust_size();
		rotated.adjust_balance();
		y.adjustheight();
		y.adjust_size();
		y.adjust_balance();
		return y;
	}


	/*method for finding and return the successor of the node
	 * Complexity = O(log(n))
	 */
   private static IAVLNode Successor(IAVLNode node) {
       IAVLNode successor = node;
       if (node.getRight().isRealNode()) {
           successor = node.getRight();
           while(successor.getLeft().isRealNode()) {
               successor = successor.getLeft();
           }
           return successor;
       }
       IAVLNode parent;
       while (successor.getParent() != null) {
           parent = successor.getParent();
           if (successor.getKey() == parent.getLeft().getKey()) {
               return parent;
           }
           successor = parent;
       }
       return null;
   }
   
	/*method supports join action and update it
	 * it's the first method, which deals when the current tree is at the same level as the second
	 * O(1)
	 */
	private void case_equal(AVLTree tree_first, AVLTree tree_second, IAVLNode connector){
		//setting children
		connector.setLeft(tree_first.getRoot());
		connector.getLeft().setParent(connector);
		connector.setRight(tree_second.getRoot());
		connector.getRight().setParent(connector);
		this.root = connector;
		if(connector !=null){
			connector.setParent(null);
		}
		//adjusting values
		connector.adjustheight();
		connector.adjust_size();
		connector.adjust_balance();
	}
	
	/*method supports join action and update it
	 * it's the second method, which deals when the current tree is lower than the second
	 * O(log(n))
	 */
	private void case_shorter(AVLTree tree, IAVLNode node_1){
		IAVLNode node = tree.getRoot();
		while (node.getHeight() > this.getRoot().getHeight()){
			//looking for the node
			node = node.getRight();
		}
		//setting children
		node_1.setRight(this.getRoot());
		node_1.setLeft(node);
		node.getParent().setRight(node_1);
		node_1.setParent(node.getParent());
		this.getRoot().setParent(node_1);
		node.setParent(node_1);
		//adjusting values
		node_1.adjustheight();
		node_1.adjust_size();
		node_1.adjust_balance();
		tree.balance2_for_insert_and_join(node_1.getParent());
	}
	
	/*method supports join action and update it
	 * it's the third method, which deals when the current tree is higher than the second
	 * O(log(n))
	 */
	private void case_higher(AVLTree tree, IAVLNode node_1){
		IAVLNode node = this.getRoot();
		while (node.getHeight() > tree.getRoot().getHeight()){
			//looking for node
			node = node.getLeft();
		}
		//setting the children
		node_1.setLeft(tree.getRoot());
		node_1.setRight(node);
		node.getParent().setLeft(node_1);
		node_1.setParent(node.getParent());
		tree.getRoot().setParent(node_1);
		node.setParent(node_1);
		//adjusting values
		node_1.adjustheight();
		node_1.adjust_size();
		node_1.adjust_balance();
		balance2_for_insert_and_join(node_1.getParent());
	}
	
	

	/** 
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNode node); // Sets left child.
		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNode node); // Sets right child.
		public IAVLNode getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNode node); // Sets parent.
		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
    	public void setHeight(int height); // Sets the height of the node.
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
    	
    	
    	/**********************************************************************************
   	 *
   	 * 
   	 * 						ADDED 				METHODS
   	 * 
   	 * 
   	 * 
   	 **********************************************************************************
   	 */
    	public void adjustheight();// Adjusts the height of the node
		public void adjust_size(); //sets size of subtree via children
		public int get_size(); //Returns the size of the subtree node is the root of
		public void adjust_balance(); //sets balance factor via children
		public int getbalance(); //return balance factor of node
		public void virtualize(); // virtual sons are added to the node (first, second - virtuals)
		public boolean check_my_balance(); //checks if the tree is balanced due to avl rules


	}

	   /** 
	    * public class AVLNode
	    *
	    * If you wish to implement classes other than AVLTree
	    * (for example AVLNode), do it in this file, not in another file. 
	    * 
	    * This class can and MUST be modified (It must implement IAVLNode).
	    */
	
	
	public class AVLNode implements IAVLNode{
		//constructor
		
		int key; //node's key
		String info; //node's value
		IAVLNode right; //node's right child
		IAVLNode left; //node's left child
		int height; //node's height
		int balance; //node's balance
		int size; //node's size
		IAVLNode parent; //node's parent


		public AVLNode(int key, String info){
			this.key = key;
			this.info = info;
			this.size = 1;
		}
		

		public AVLNode(int key, String info, int height, int size){
			//implement AVLNode
			this.key = key;
			this.info = info;
			this.height = height;
			this.size = size;
		}

		/*returns node's key
		 * Complexity = O(1)
		 */
		public int getKey()
		{
			return this.key;
		}
		
		/*returns node's value
		 * Complexity = O(1)
		 */
		public String getValue()
		{
			return this.info;
		}
		
		/*sets "node" as left node for current
		 * Complexity = O(1)
		 */
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		
		/*returns current left node
		 * Complexity = O(1)
		 */
		public IAVLNode getLeft()
		{
			return this.left;
		}
		
		/*sets "node" as right node for current
		 * Complexity = O(1)
		 */
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}
		
		/*returns current left node
		 * Complexity = O(1)
		 */
		public IAVLNode getRight()
		{
			return this.right;
		}
		
		/*sets "node" as current's parent
		 * Complexity = O(1)
		 */
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		
		/*returns current's parent
		 * Complexity = O(1)
		 */
		public IAVLNode getParent()
		{
			return this.parent;
		}

		/*boolean method for verifying node
		 * Complexity = O(1)
		 */
		public boolean isRealNode()
		{
			if(this.key != -1)
				return true;
			return false;
		}
		
		/*sets "height" as current's height
		 * Complexity = O(1)
		 */
		public void setHeight(int height)
		{
			this.height = height;
		}
		
		/*Returns current's height
		 * Complexity = O(1)
		 */
		public int getHeight()
		{
			return this.height;
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
		/*adjusts current's height by his children
		 * Complexity = O(1)
		 */
		public void adjustheight()
		{
			this.height = 1 + Math.max(this.left.getHeight(), this.right.getHeight());
		}

		/*Sets current size by his children
		 * Complexity = O(1)
		 */
		public void adjust_size()
		{
			this.size = this.left.get_size() + 1 + this.right.get_size();
		}

		/*Returns current size
		 * Complexity = O(1)
		 */
		public int get_size()
		{
			return this.size;
		}
		
		/*Checks current balance - if it's not balanced by AVL rules - returns false. otherwise - true.
		 * Complexity = O(1)
		 */
		public boolean check_my_balance(){
			if(this == null || !this.isRealNode()) {
				//balanced empty-case
				return true;
			}
			if(this.getHeight() - this.getLeft().getHeight() == 3 || this.getHeight() - this.getRight().getHeight() == 3)
			{
				//not balanced by avl
				return false;
			}
			if(this.getHeight() - this.getLeft().getHeight() == 2 && this.getHeight() - this.getRight().getHeight() == 2) 
			{
				//not balanced by avl
				return false;
			}
			if(this.getbalance() != 0 && this.getbalance() != 1 &&this.getbalance() != -1)
			{
				//not balanced by avl
				return false;
			}
			//yes, balanced
			return true;
		}

		/*set balance for current by his children
		 * Complexity = O(1)
		 */
		public void adjust_balance()
		{
			this.balance = this.left.getHeight()-this.right.getHeight();
		}

		/*Returns current's balance
		 * Complexity = O(1)
		 */
		public int getbalance()
		{
			return this.balance;
		}

		/*Create 2 virtual nodes and set as current's children
		 * Complexity = O(1)
		 */
		public void virtualize()
		{
			IAVLNode first = new AVLNode(-1, null, -1, 0);
			IAVLNode second = new AVLNode(-1, null, -1, 0);
			//assign virtuals to the node
			this.setRight(first);
			this.setLeft(second);
			this.right.setParent(this);
			this.left.setParent(this);
		}
		
	


	}
}
