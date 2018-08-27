/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

/*
 * moodle_name = omergerhard , ID - 204156384, name = OMER GERHARD
 * moodle_name = bergerbest, ID - 204540710, name = SAAR BERGERBEST
 */

public class WAVLTree 
{
	
	private IWAVLNode root;
	
	// creating an empty WAVL Tree
	public WAVLTree()
	{
		this.root = null;
	}
	
	//creating a Tree with specific root
	public WAVLTree(IWAVLNode root)
	{
		this.root = root;
	}
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty()
  { 
	  if(this.root==null)
	  {
		  return true;
	  }
	  return false;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {	
	  if(this.empty())
	  {
		  return null;
	  }
	  return searchKey(this.root,k);
  }
  
  public String searchKey(IWAVLNode node, int k)
  {
	  if(node.getKey()==(-1))
	  {
		  return null;
	  }
	  else if(node.getKey()==k)
	  {
		  return node.getValue();
	  }
	  else if(node.getKey()<k)
	  {
		  return searchKey(node.getRight(), k);
	  }
	  else
	  {
		  return searchKey(node.getLeft(),k);
	  }
  }
  
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) 
   {
	   if(this.search(k)!=null)
	   {
		   return -1;
	   }
	   IWAVLNode node = new WAVLNode(k,i);
	   IWAVLNode parent=naiveInsert(this,node);
	   int count=0;
	   int error=errorIdInsert(parent);
	   while(error!=0)
	   {
		   switch(error)
		   {
		   		case 1:
		   			promote(parent);
		   			count++;
		   			break;
		   		case 2:
		   			rotateLeftInsert(this,parent);
		   			count++;
		   			break;
		   		case 3:
		   			doubleRotateLeftInsert(this,parent);
		   			count=count+2;
		   			break;
		   		case 4:
		   			promote(parent);
		   			count++;
		   			break;
		   		case 5:
		   			rotateRightInsert(this,parent);
		   			count++;
		   			break;
		   		case 6:
		   			doubleRotateRightInsert(this,parent);
		   			count=count+2;
		   			break;
		   		default:
		   			break;
		   }
		   parent=parent.getParent();
		   error=errorIdInsert(parent);
	   }
	  return count;
   }
   
   private IWAVLNode naiveInsert(WAVLTree T, IWAVLNode x) 
   {
 	int k=x.getKey();
 	IWAVLNode parent=null;
 	if(T.empty())
 	{
 		T.setRoot(x);
 	}
 	else
 	{
 	 	IWAVLNode y=T.getRoot();
 		while(y.isRealNode())
 		{
 			parent=y;
 			y.incSize();
 			if(parent.getKey()>k)
 			{
 				y=parent.getLeft();
 			}
 			else
 			{
 				y=parent.getRight();
 			}
 		}
 		x.setParent(parent);
 		if(parent.getKey()>k)
 		{
 			parent.setLeft(x);
 		}
 		else
 		{
 			parent.setRight(x);
 		}
 	}
 	
 	return parent;
   }
   
   private int errorIdInsert(IWAVLNode x) 
   {
	   if(x==null)
	   {
		   return 0;
	   }
 	  int rank,rankl,rankr,rankDL,rankDR;
 	  rank=x.getRank();
 	  rankl=x.getLeft().getRank();
 	  rankr=x.getRight().getRank();
 	  rankDL=rank-rankl;
 	  rankDR=rank-rankr;
 	  if(rankDL==0)
 	  {
 		  if(rankDR==1)
 		  {
 			  return 1;
 		  }
 		  else
 		  {
 			  if(rankl-x.getLeft().getLeft().getRank()==1)
 			  {
 				  return 2;
 			  }
 			  else
 			  {
 				  return 3;
 			  }
 		  }
 	  }
 	  else if(rankDR==0)
 	  {
 		  if(rankDL==1)
 		  {
 			  return 4;
 		  }
 		  else
 		  {
 			  if(rankr-x.getRight().getRight().getRank()==1)
 			  {
 				  return 5;
 			  }
 			  else
 			  {
 				  return 6;
 			  }
 		  }
 	  }
 	  else
 	  {
 		  return 0;
 	  }
   }
   



private void rotateLeftInsert(WAVLTree T, IWAVLNode z) 
  {
	  IWAVLNode x=z.getLeft();
	  IWAVLNode tempParent=z.getParent();
	  IWAVLNode b=x.getRight();
	  b.setParent(z);
	  z.setLeft(b);
	  z.setSize(z.getSubtreeSize()+1);
	  z.setRank(z.getRank()-1);
	  z.setParent(x);
	  x.setRight(z);
	  x.setSize(x.getSubtreeSize()+1);
	  x.setParent(tempParent);
	  if(T.getRoot().getKey()==z.getKey())
	  {
		  T.setRoot(x);
	  }
	  else if(isGreater(x,tempParent))
	  {
		  tempParent.setLeft(x);
	  }
	  else
	  {
		  tempParent.setRight(x);
	  }
  }
  
  private void rotateRightInsert(WAVLTree T, IWAVLNode z) 
  {
	  IWAVLNode x=z.getRight();
	  IWAVLNode tempParent=z.getParent();
	  IWAVLNode b=x.getLeft();
	  b.setParent(z);
	  z.setRight(b);
	  z.setSize(z.getSubtreeSize()+1);
	  z.setRank(z.getRank()-1);
	  z.setParent(x);
	  x.setLeft(z);
	  x.setSize(x.getSubtreeSize()+1);
	  x.setParent(tempParent);
	  if(T.getRoot().getKey()==z.getKey())
	  {
		  T.setRoot(x);
	  }
	  else if(isGreater(x,tempParent))
	  {
		  tempParent.setLeft(x);
	  }
	  else
	  {
		  tempParent.setRight(x);
	  }
  }
  
  private void doubleRotateLeftInsert(WAVLTree T,IWAVLNode z) 
  {	
	  IWAVLNode x=z.getLeft();
	  IWAVLNode b=x.getRight();
	  IWAVLNode d=b.getRight();
	  IWAVLNode c=b.getLeft();
	  IWAVLNode tempParent=z.getParent();
	  c.setParent(x);
	  d.setParent(z);
	  x.setRight(c);
	  x.setSize(x.getSubtreeSize()+1);
	  x.setRank(x.getRank()-1);
	  x.setParent(b);
	  z.setLeft(d);
	  z.setSize(z.getSubtreeSize()+1);
	  z.setRank(z.getRank()-1);
	  z.setParent(b);
	  b.setLeft(x);
	  b.setRight(z);
	  b.setSize(b.getSubtreeSize()+1);
	  b.setRank(b.getRank()+1);
	  b.setParent(tempParent);
	  if(T.getRoot().getKey()==z.getKey())
	  {
		  T.setRoot(b);
	  }
	  else if(isGreater(b,tempParent))
	  {
		  tempParent.setLeft(b);
	  }
	  else
	  {
		  tempParent.setRight(b);
	  }
  }
  
  private void doubleRotateRightInsert(WAVLTree T,IWAVLNode z)
  {	
	  IWAVLNode x=z.getRight();
	  IWAVLNode b=x.getLeft();
	  IWAVLNode d=b.getLeft();
	  IWAVLNode c=b.getRight();
	  IWAVLNode tempParent=z.getParent();
	  c.setParent(x);
	  d.setParent(z);
	  x.setLeft(c);
	  x.setSize(x.getSubtreeSize()+1);
	  x.setRank(x.getRank()-1);
	  x.setParent(b);
	  z.setRight(d);
	  z.setSize(z.getSubtreeSize()+1);
	  z.setRank(z.getRank()-1);
	  z.setParent(b);
	  b.setRight(x);
	  b.setLeft(z);
	  b.setSize(b.getSubtreeSize()+1);
	  b.setRank(b.getRank()+1);
	  b.setParent(tempParent);
	  if(T.getRoot().getKey()==z.getKey())
	  {
		  T.setRoot(b);
	  }
	  else if(isGreater(b,tempParent))
	  {
		  tempParent.setLeft(b);
	  }
	  else
	  {
		  tempParent.setRight(b);
	  }
  }

private void promote(IWAVLNode parent) 
  {
	  parent.setRank(parent.getRank()+1);
  }

/**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if(this.search(k)==null)
	   {
		   return -1;
	   }
	   IWAVLNode parent = naiveDelete (this,this.root,k);
	   int error = errorIdDelete(parent);
	   int count = 0;
	   while(error!=0)
	   {
		   switch(error)
		   {
		   		case 1:
		   			demoteNode(parent);
		   			count++;
		   			break;
		   		case 2:
		   			doubleDemoteLeft(parent);
		   			count=count+2;
		   			break;
		   		case 3:
		   			rotateLeftDelete(this,parent);
		   			count++;
		   			break;
		   		case 4:
		   			doubleRotateLeftDelete(this,parent);
		   			count=count+2;
		   			break;
		   		case 5:
		   			doubleDemoteRight(parent);
		   			count=count+2;
		   			break;
		   		case 6:
		   			rotateRightDelete(this,parent);
		   			count++;
		   			break;
		   		case 7:
		   			doubleRotateRightDelete(this,parent);
		   			count=count+2;
		   			break;
		   		case 8:
		   			demoteLeaf(parent);
		   			count++;
		   			break;
		   		default:
		   			break;
		   }
		   parent=parent.getParent();
		   if(parent==null)
		   {
			   break;
		   }
		   error=errorIdDelete(parent);
	   }
	   return count;
   }
   
   private IWAVLNode naiveDelete(WAVLTree T, IWAVLNode node, int k) 
   {
	   IWAVLNode z = node;
	   while(z.getKey()!=k)
	   {
		   z.decSize();
		   if(z.getKey()<k)
		   {
			   z = z.getRight();
		   }
		   else
		   {
			   z = z.getLeft();
		   }
		   
	   }
	   IWAVLNode parent = z.getParent();
	   boolean isRoot = (parent==null);
	   if(isLeaf(z)) //if z is a leaf
	   {
		   if(isRoot)
		   {
			   T.root = null;
		   }
		   else if(isGreater(parent,z))
		   {
			   parent.setRight(z.getRight());
		   }
		   else
		   {
			   parent.setLeft(z.getLeft());
		   }
	   }
	   else //z is not a leaf
	   {
		   int childIndicator = hasOneChild(z);
		   if(childIndicator<2)
		   {
			   if(childIndicator==0) //z has only left child
			   {
				   if(isRoot)
				   {
					   T.setRoot(z.getLeft());
				   }
				   else if(isGreater(parent,z)) //z is the right child of parent
				   {
					   parent.setRight(z.getLeft());
					   z.getLeft().setParent(parent);
				   }
				   else //z is the left child of parent
				   {
					   parent.setLeft(z.getLeft());
					   z.getLeft().setParent(parent);
				   }
			   }
			   else //z has only right child
			   {
				   if(isRoot)
				   {
					   T.setRoot(z.getRight());
				   }
				   else if(isGreater(parent,z)) //z is the right child of parent
				   {
					   parent.setRight(z.getRight());
					   z.getRight().setParent(parent);
				   }
				   else //z is the left child of parent
				   {
					   parent.setLeft(z.getRight());
					   z.getRight().setParent(parent);
				   }
			   }
		   }
		   else //z has 2 children
		   {
			   IWAVLNode y = successor(z);
			   IWAVLNode b = y.getParent();
			   b.setLeft(y.getRight());
			   y.setLeft(z.getLeft());
			   z.getLeft().setParent(y);
			   y.setRight(z.getRight());
			   z.getRight().setParent(y);
			   y.setParent(parent);
			   y.setRank(z.getRank());
			   if(isRoot)
			   {
				   T.setRoot(y);
			   }
			   else if(isGreater(y,parent))
			   {
				   parent.setLeft(y);
			   }
			   else
			   {
				   parent.setRight(y);
			   }
			   updateSizeAfterDelete(b);
			   return b;
		   }
		   
	   }
	   return parent;
	   
   }

   private void updateSizeAfterDelete(IWAVLNode b) 
   {
	   while(b.getKey()==b.getParent().getLeft().getKey())
	   {
		   b.decSize();
		   b=b.getParent();
	   }
	   b=b.getParent();
	   b.decSize();
   }

private void demoteLeaf(IWAVLNode node) 
   {
	   node.setRank(0);
   }

private void doubleRotateRightDelete(WAVLTree T, IWAVLNode z) 
   {
	   IWAVLNode y = z.getLeft();
	   IWAVLNode a = y.getRight();
	   IWAVLNode c = a.getRight();
	   IWAVLNode d = a.getLeft();
	   IWAVLNode tempParent = z.getParent();
	   c.setParent(z);
	   d.setParent(y);
	   z.setLeft(c);
	   z.setSize(z.getSubtreeSize()+1);
	   z.setRank(z.getRank()-2);
	   z.setParent(a);
	   y.setRight(d);
	   y.setSize(y.getSubtreeSize()+1);
	   y.setRank(y.getRank()-1);
	   y.setParent(a);
	   a.setRight(z);
	   a.setLeft(y);
	   a.setSize(a.getSubtreeSize()+1);
	   a.setRank(a.getRank()+2);
	   a.setParent(tempParent);
	   if(T.getRoot().getKey()==z.getKey())
		{
			T.setRoot(a);
		}
	   else if(isGreater(a,tempParent))
	   {
		   tempParent.setLeft(a);
	   }
	   else
	   {
		   tempParent.setRight(a);
	   }
   }

   private void doubleRotateLeftDelete(WAVLTree T, IWAVLNode z) 
   {
	   IWAVLNode y = z.getRight();
	   IWAVLNode a = y.getLeft();
	   IWAVLNode c = a.getLeft();
	   IWAVLNode d = a.getRight();
	   IWAVLNode tempParent = z.getParent();
	   c.setParent(z);
	   d.setParent(y);
	   z.setRight(c);
	   z.setSize(z.getSubtreeSize()+1);
	   z.setRank(z.getRank()-2);
	   z.setParent(a);
	   y.setLeft(d);
	   y.setSize(y.getSubtreeSize()+1);
	   y.setRank(y.getRank()-1);
	   y.setParent(a);
	   a.setLeft(z);
	   a.setRight(y);
	   a.setSize(a.getSubtreeSize()+1);
	   a.setRank(a.getRank()+2);
	   a.setParent(tempParent);
	   if(T.getRoot().getKey()==z.getKey())
		{
			T.setRoot(a);
		}
	   else if(isGreater(a,tempParent))
	   {
		   tempParent.setLeft(a);
	   }
	   else
	   {
		   tempParent.setRight(a);
	   }
   }

	private void doubleDemoteRight(IWAVLNode parent) 
	{
		parent.setRank(parent.getRank()-1);	
		parent.getLeft().setRank(parent.getLeft().getRank()-1);
	}

	private void rotateLeftDelete(WAVLTree T, IWAVLNode z) 
	{
		IWAVLNode y = z.getRight();
		IWAVLNode a = y.getLeft();
		IWAVLNode tempParent=z.getParent();
		a.setParent(z);
		z.setRight(a);
		z.setSize(z.getSubtreeSize()+1);
		if(isLeaf(z))
		{
			z.setRank(0);
		}
		else
		{
			z.setRank(z.getRank()-1);
		}
		z.setParent(y);
		y.setLeft(z);
		y.setSize(y.getSubtreeSize()+1);
		y.setRank(y.getRank()+1);
		y.setParent(tempParent);
		if(T.getRoot().getKey()==z.getKey())
		{
			T.setRoot(y);
		}
		else if(isGreater(y,tempParent))
		{
			tempParent.setLeft(y);
		}
		else
		{
			tempParent.setRight(y);
		}
	}
	
	private void rotateRightDelete(WAVLTree T, IWAVLNode z) 
	{
		IWAVLNode y = z.getLeft();
		IWAVLNode a = y.getRight();
		IWAVLNode tempParent=z.getParent();
		a.setParent(z);
		z.setLeft(a);
		z.setSize(z.getSubtreeSize()+1);
		if(isLeaf(z))
		{
			z.setRank(0);
		}
		else
		{
			z.setRank(z.getRank()-1);
		}
		z.setParent(y);
		y.setRight(z);
		y.setSize(y.getSubtreeSize()+1);
		y.setRank(y.getRank()+1);
		y.setParent(tempParent);
		if(T.getRoot().getKey()==z.getKey())
		{
			T.setRoot(y);
		}
		else if(isGreater(y,tempParent))
		{
			tempParent.setLeft(y);
		}
		else
		{
			tempParent.setRight(y);
		}
	}

private void demoteNode(IWAVLNode parent)
   	{
	   	parent.setRank(parent.getRank()-1);	
   	}
   
   private void doubleDemoteLeft(IWAVLNode parent) 
	{
		parent.setRank(parent.getRank()-1);	
		parent.getRight().setRank(parent.getRight().getRank()-1);
	}

	private int hasOneChild(IWAVLNode z) 
   	{
   		if(z.getLeft().getKey()!=(-1) && z.getRight().getKey()==(-1)) //has only left child
		{
			return 0;
		}
		if(z.getLeft().getKey()==(-1) && z.getRight().getKey()!=(-1)) //has only right child
		{
			return 1;
		}
		return 2; //has 2 children
	}
   	
   	private boolean isGreater(IWAVLNode parent, IWAVLNode z)
   	{
   		if(parent.getKey()<z.getKey())
   		{
   			return true;   			
   		}
   		return false;
   	}
   	
    private int errorIdDelete(IWAVLNode node)
    {
 	   int rank, rankl, rankr, rankDL , rankDR;
	   if(node==null)
	   {
		   return 0;
	   }
 	   rank = node.getRank();
 	   rankl = node.getLeft().getRank();
 	   rankr = node.getRight().getRank();
 	   rankDL = rank-rankl;
 	   rankDR = rank-rankr;
 	   boolean leaf = isLeaf(node);
 	   if(leaf)
 	   {
 		   return 8;		   
 	   }
 	   if(rankDL==3)
 	   {
 		   if(rankDR==2)
 		   {
 			   return 1;
 		   }
 		   else
 		   {
 			   if(rankr-node.getRight().getRight().getRank()==2)
 			   {
 				   if(rankr-node.getRight().getLeft().getRank()==2)
 				   {
 					   return 2;
 				   }
 				   else
 				   {
 					   return 4;
 				   }
 			   }
 			   else
 			   {
 				   return 3;
 			   }
 		   }
 	   }
 	   else if(rankDR==3)
 	   {
 		   if(rankDL==2)
 		   {
 			   return 1;
 		   }
 		   else
 		   {
 			   if(rankl-node.getLeft().getLeft().getRank()==2)
 			   {
 				   if(rankl-node.getLeft().getRight().getRank()==2)
 				   {
 					   return 5;
 				   }
 				   else
 				   {
 					   return 7;
 				   }
 			   }
 			   else
 			   {
 				   return 6;
 			   }
 		   }
 	   }
 	   return 0;
    }

    private boolean isLeaf(IWAVLNode node) 
    {
    	if(node.getLeft().getKey() == (-1) && node.getRight().getKey()==(-1))
    	{
    		return true;    		
    	}
 	return false;
 }

/**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if(this.empty())
	   {
		   return null;
	   }
	   return findMin(this.root);
   }
   
   public String findMin(IWAVLNode node)
   {
	   while(node.getLeft().getKey()!=(-1))
	   {
		   node = node.getLeft();
	   }
	   return node.getValue();
   }
   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if(this.empty())
	   {
		   return null;
	   }
	   return findMax(this.root);
   }
   
   public String findMax(IWAVLNode node)
   {
	   while(node.getRight().getKey() != (-1))
	   {
		   node = node.getRight();
	   }
	   return node.getValue();
   }
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  int[] arr = new int[0]; // to be replaced by student code
	  if(this.empty())
	  {
		  return arr;
	  }
	  int length=1+this.root.getSubtreeSize();
	  arr=new int[length];
	  IWAVLNode node = minNode(this.root);
	  arr[0]=node.getKey();
	  for(int i=1;i<length;i++)
	  {
		  node=successor(node);
		  arr[i]=node.getKey();
	  }
      return arr;              // to be replaced by student code
  }
  
  private IWAVLNode minNode(IWAVLNode node) // return the node with the lowest key.
  {
	   while(node.getLeft().getKey()!=(-1))
	   {
		   node = node.getLeft();
	   }
	   return node;
  }

  private IWAVLNode successor(IWAVLNode node)
  {
	  if(node.getRight().isRealNode())
	  {
		  return minNode(node.getRight());
	  }
	  IWAVLNode y;
	  y=node.getParent();
	  while(y!=null && (node.getKey()==y.getRight().getKey())) //check if == works!!!!
	  {
		  node=y;
		  y=node.getParent();
	  }
	  return y;
  }
/**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  String[] arr = new String[0]; // to be replaced by student code
	  if(this.empty())
	  {
		  return arr;
	  }
	  int length=1+this.root.getSubtreeSize();
	  arr=new String[length];
	  IWAVLNode node = minNode(this.root);
	  arr[0]=node.getValue();
	  for(int i=1;i<length;i++)
	  {
		  node=successor(node);
		  arr[i]=node.getValue();
	  }
      return arr;
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   if(this.empty())
	   {
		   return 0;
	   }
	   return this.root.getSize(); // to be replaced by student code
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root WAVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IWAVLNode getRoot()
   {
	   return this.root;
   }
   
   public void setRoot(IWAVLNode root)
   {
	   this.root=root;
	   root.setParent(null);
   }
     /**
    * public int select(int i)
    *
    * Returns the value of the i'th smallest key (return null if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
    *
	* precondition: size() >= i > 0
    * postcondition: none
    */   
   public String select(int i)
   {
	   if(this.empty())
	   {
		   return null;
	   }
	   return recSelect(this.root,i);
   }
   
   private String recSelect(IWAVLNode node, int i)
   {  
	   if(i>node.getSize())
	   {
		   return null;
	   }
	   int x = node.getLeft().getSize();
	   if(i==x+1)
	   {
		   return node.getValue();  
	   }
	   else if(i<x+1)
	   {
		   return recSelect(node.getLeft(), i);
	   }
	   else
	   {
		   return recSelect(node.getRight(), i-(x+1));
	   } 
   }
   
	/**
	   * public interface IWAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IWAVLNode{	
		public int getKey(); //returns node's key (for virtual node return -1)
		public IWAVLNode getParent(); //returns node's parent
		public void setParent(IWAVLNode node);//set a parent for the specific node.
		public String getValue(); //returns node's value (for virtual node return null)
		public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setLeft(IWAVLNode node); //set a node as the left child of another node
		public IWAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setRight(IWAVLNode node); //set a node as the right child of another node
		public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public int getSize(); //returns the size of the node's subtree
		public void setSize(int i); //updating the size of a node's subtree
		public void incSize();//increment size by 1.
		public void decSize();//decrement size by 1.
		public int getRank(); //returns the node's rank
		public void setRank(int i); //sets the rank for a node 
		public int getSubtreeSize(); //returns the number of real nodes in this node's subtree (Should be implemented in O(1))
	}

   /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IWAVLNode)
   */
  public class WAVLNode implements IWAVLNode
  {
	  
	  private int key;
	  private String info;
	  private WAVLNode parent;
	  private WAVLNode left;
	  private WAVLNode right;
	  private int size;
	  private int rank;
	  
	  	
	  	public WAVLNode()
	  	{
		  this.key = -1;
		  this.info = null;
		  this.parent = null;
		  this.left = null;
		  this.right = null;
		  this.size = 0;
		  this.rank = -1;
	  	}
	  
	  	public WAVLNode(int i, String str)
	  	{
	  		this.key = i;
	  		this.info = str;
	  		this.parent=null;
	  		this.left = new WAVLNode();
	  		this.right = new WAVLNode();
	  		this.size = 1;
	  		this.rank = 0;
	  	}
	  
		public int getKey()
		{
			return this.key; //returns the node's key.
		}
		public String getValue()
		{
			return this.info; // returns the node's info.
		}
		public IWAVLNode getLeft()
		{
			return this.left; // returns the node's left child
		}
		
		public void setLeft(IWAVLNode node)
		{
			this.left = (WAVLNode) node;
		}
		
		public IWAVLNode getRight()
		{
			return this.right; // returns the node's right child
		}
		
		public void setRight(IWAVLNode node)
		{
			this.right = (WAVLNode) node;
		}
		
		// Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public boolean isRealNode()
		{
			return this.key!=(-1);
		}
		
		public int getSize()
		{
			return this.size;
		}
		
		public void setSize(int i)
		{
			this.size=i;
		}
		
		public int getSubtreeSize()
		{
			return this.left.size+this.right.size; // to be replaced by student code
		}
		
		public IWAVLNode getParent()
		{
			return this.parent;
		}
		
		public void setParent(IWAVLNode node)
		{
			this.parent = (WAVLNode) node;
		}
		
		public int getRank()
		{
			return this.rank;
		}
		
		public void setRank(int k)
		{
			this.rank = k;
		}
		
		public void incSize()//increment size by 1.
		{
			this.size++;
		}
		
		public void decSize()//decrement size by 1.
		{
			this.size--;
		}
		
  }
  
  
}


  

