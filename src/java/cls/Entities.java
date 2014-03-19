package cls;

import java.util.HashMap;

public class Entities {
        
    HashMap<Integer, HashMap<String, Integer>> map;
    
    public Entities() {
        map = new HashMap<Integer, HashMap<String, Integer>>();
    }
    
    public void add(String name) {
        add(name, 0);                    
    }
    
    public void add(String name, int d) {
        HashMap<String, Integer> doc = map.get(d);
        if (doc == null) {
            doc = new HashMap<String, Integer>();
            doc.put(name, 0);
            map.put(d, doc);
        }
        else {
            doc.put(name, doc.get(name)==null?1+doc.get(name):0);
        }
        
    }
    
    public HashMap<String, Integer> getEntries() {
        return getEntries(0);
    }
    
    public HashMap<String, Integer> getEntries(int d){
        return map.get(d);
    }
    
    public void clear() {
        map.clear();
        System.gc();
    }
}
