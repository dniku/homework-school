public class Patterns {
	public static void main(String[] args) {
		Counter cnt = Counter.getInstance();
		cnt.inc();
		System.out.println(cnt.getValue());
		
		Node tree = new BinOp(new Number(1), new Number(2), '+');
		
		Eval eval = new Eval();
		tree.accept(eval);
		System.out.println(eval.result);
		
		CountNumbers countnums = new CountNumbers();
		tree.accept(countnums);
		System.out.println(countnums.result);
		
		CountOps countops = new CountOps();
		tree.accept(countops);
		System.out.println(countops.result);
	}

}

class Counter { 
    private int cnt = 0; 

    private Counter() { 
        cnt = 0; 
    }

    private static class CH { 
        public static Counter instance = new Counter(); 
    } 

    public static Counter getInstance() { 
        return CH.instance; 
    } 

    public void inc() { 
        cnt++; 
    } 

    public int getValue() { 
        return cnt; 
    } 
}

interface Visitor {
    void visit(BinOp node);
    void visit(UnOp node);
    void visit(Number node);
}

interface Node {
    void accept (Visitor v);
}

class BinOp implements Node {
    Node l;
    Node r;
    char op;
    
    public BinOp(Node l, Node r, char op) {
    	this.l = l;
    	this.r = r;
    	this.op = op;
    }

    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

class UnOp implements Node {
    Node v;
    char op;
    
    public UnOp(Node v, char op) {
		this.v = v;
		this.op = op;
	}

	public void accept(Visitor vis) {
        vis.visit(this);
    }
}

class Number implements Node {
    int v;
    
    public Number(int v) {
		this.v = v;
	}

	public void accept(Visitor vis) {
        vis.visit(this);
    }
}

class Eval implements Visitor {
    public int result = 0;
    
    public Eval() {}
    
    public void visit(BinOp node) {
        node.l.accept(this);
        int left = result;
        node.r.accept(this);
        switch (node.op) {
        	case '+': result += left; break;
        	case '*': result *= left; break;
        }
    }
    
	public void visit(UnOp node) {
		node.v.accept(this);
    	switch (node.op) {
    		case '-': result = -result; 
    	}
	}

	public void visit(Number node) {
		result = node.v;
	}
}

class CountNumbers implements Visitor {
	public int result = 0;
	
	public CountNumbers () {}
	
	public void visit(BinOp node) {
		node.l.accept(this);
        int left = result;
        node.r.accept(this);
        result += left;
    }
    
	public void visit(UnOp node) {
		node.v.accept(this);
	}

	public void visit(Number node) {
		result = 1;
	}
}

class CountOps implements Visitor {
	public int result = 0;
	
	public CountOps () {}
	
	public void visit(BinOp node) {
		node.l.accept(this);
        int left = result;
        node.r.accept(this);
        result += left + 1;
    }
    
	public void visit(UnOp node) {
		node.v.accept(this);
		result += 1;
	}

	public void visit(Number node) {}
}