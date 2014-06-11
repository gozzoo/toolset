package toolset;


public class TernaryTree {
    private Node root = null;
    int size = 0;
    
    public void replaceAll(TernaryTree t) {
        this.root = t.root;
    }

    public void add(String s, int start, int end) {
        if (s == null || s.equals("")) 
            throw new RuntimeException();

        AddContext ac = new AddContext();
        root = ac.add(s, start, end, root);
        size += ac.size;
    }
    
    public void add(String s) {
        add(s, 0, s.length() -1);
    }
    
    public boolean contains(String s) {
        if (s == null || s.equals(""))
            return false;

        int pos = 0;
        Node node = root;
        while (node != null) {
            char ch = s.charAt(pos);
            if (ch < node.ch) { 
                node = node.left; 
            } else if (ch > node.ch) { 
                node = node.right; 
            } else {
                if (++pos == s.length()) 
                    return node.count > 0;
                node = node.center;
            }
        }

        return false;
    }
    
    public String[] findAllMatches(String prefix, int count) {
        if (prefix == null || prefix.equals("")) 
            return null;

        SearchContext ctx = new SearchContext(prefix, root, count);
        return ctx.findAllMatches();
    }
}

class AddContext {
    String s;
    int end, size = 0;
    
    Node add(String s, int pos, int end, Node node) {
        this.s = s;
        this.end = end;
        
        return add(pos, node);
    }
    
    Node add(int pos, Node node) {
        char ch = Character.toLowerCase(s.charAt(pos));
        
        if (node == null) {
            node = new Node(ch);
            size++;
        }

        if (ch < node.ch) 
            node.left = add(pos, node.left); 
        else if (ch  > node.ch) 
            node.right = add(pos, node.right); 
        else if (pos == end)
            node.count++;
        else
            node.center = add(pos + 1, node.center); 

        return node;
    }    
}

class SearchContext {
    ResultNode[] rnodes;
    Node root;
    String prefix;
    StringBuilder sb;
    
    public SearchContext(String prefix, Node root, int count) {
        this.prefix = prefix;
        this.root = root;
        sb = new StringBuilder(prefix);
        rnodes = new ResultNode[count];
    }
    
    String[] findAllMatches() {
        findMatches();
        if (rnodes[0] == null)
            return null;
        
        int rsize = 0;
        while (rsize < rnodes.length && rnodes[rsize] != null)
            rsize++;
            
        String[] result = new String[rsize];
        for (int i = 0; i < rsize; i++)
            result[i] = rnodes[i].word;
        return result;
    }
    
    private void findMatches() {
        int pos = 0;
        Node node = root;
        while (node != null) {
            char ch = prefix.charAt(pos);
            if (ch < node.ch) { 
                node = node.left; 
            } else if (ch > node.ch) { 
                node = node.right; 
            } else {
                if (++pos == prefix.length()) { 
                    match(node);
                    return;
                }
                node = node.center;
            }
        }
    }
    
    private void match(Node n) {
        if (n.count > 0) 
            addResult(n);
        nextMatch(n.center);
    }
       
    private void nextMatch(Node n) {
        if (n == null)
            return;
        
        nextMatch(n.left);
        nextMatch(n.right);

        sb.append(n.ch);
        
        if (n.count > 0) 
            addResult(n);
        
        nextMatch(n.center);
        
        int len = sb.length();
        sb.setLength(len -1);
    }
    
    private void addResult(Node n) {    
        for (int i = 0; i < rnodes.length; i++) {
            ResultNode rn = rnodes[i];
            if (rn == null) {
                rnodes[i] =  new ResultNode(sb.toString(), n.count);
                break;
            } else {
                if (n.count > rn.count) {
                    System.arraycopy(rnodes, i, rnodes, i+1, rnodes.length - i - 1);
                    rnodes[i] =  new ResultNode(sb.toString(), n.count);
                    break;
                }
            }
        }
    }
}

class Node {
    char ch;
    Node left, center, right;
    int count;

    public Node(char ch) {
        this.ch = ch;
    }
}

class ResultNode {
    String word;
    int count;
    ResultNode(String word, int count) {
        this.word = word;
        this.count = count;
    }
}