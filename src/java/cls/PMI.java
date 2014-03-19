package cls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PMI {
    
    public class Pair<T, S> {
        public T first;
        public S second;
        public Pair(T f, S s) {
            first = f; second = s;
        }
    }
    public class PairComparator implements Comparator<Pair> {
        @Override
        public int compare(Pair a, Pair b) {
            return ((Double) a.first).compareTo((Double) b.first)*(-1);
        }
    }

    private int totalLength;
    private Map<Integer, Integer> docLengths;
    private Map<Integer, Map<String, Integer>> docs;
    private Map<String, Integer> frequencies;
    private Map<Integer, ArrayList<String>> entities;

    private static PMI instance = null;
    private PMI(){
        docLengths = new HashMap<Integer, Integer>();
        docs = new HashMap<Integer, Map<String, Integer>>();
        frequencies = new HashMap<String, Integer>();
        entities = new HashMap<Integer, ArrayList<String>>();
    }
    public static PMI getInstance() {
        if (instance == null) instance = new PMI();
        return instance;
    }
    
    public void clear() {
        docLengths.clear();
        docs.clear();
        frequencies.clear();
        totalLength = 0;
        entities.clear();
        System.gc();
    }

    public void add(int doc, String word) {
        if (null != entities.get(doc)
          && entities.get(doc).contains(word)) return;
        
        totalLength++;
        try {
            docLengths.put(doc, docLengths.get(doc) + 1);
        } catch (NullPointerException e) {
            docLengths.put(doc, 1);
        }
        Map<String, Integer> record = docs.get(doc);
        if (null == record){
            record = new HashMap<String, Integer>();
            docs.put(doc, record);
        }
        try {
            record.put(word, record.get(word) + 1);
        } catch (NullPointerException e) {
            record.put(word, 1);
        }
        try {
            frequencies.put(word, frequencies.get(word) + 1);
        } catch (NullPointerException e) {
            frequencies.put(word, 1);
        }
    }
    
    public void addEntity(int doc, String entity) {
        ArrayList<String> docEntities = entities.get(doc);
        if (null == docEntities) {
            docEntities = new ArrayList<String>();
            entities.put(doc, docEntities);
        }
        if (docEntities.contains(entity)) return;
        docEntities.add(entity);
    }

    public double pmi(Map<String, Integer> record, int docLen, String word) {
        int occInDoc, occTotal;
        try {
            occInDoc = record.get(word);
        } catch (NullPointerException e) {
            occInDoc = 0;
        }
        try {
            occTotal = frequencies.get(word);
        } catch (NullPointerException e) {
            occTotal = 0;
        }

        double pWordInDoc = (double) occInDoc / docLen;
        double pWordAnywhere = (double) occTotal / totalLength;
        return Math.log(pWordInDoc / pWordAnywhere);
    }

    public List<String> getKeywords(int doc, int limit) {
        
        /*System.out.println("Docs count:" + docs.size());
        System.out.println("Words count:" + totalLength);
        for (Entry<Integer, Map<String, Integer>> e: docs.entrySet()) {
            System.out.println("Doc_"+e.getKey()+"| wc: "+e.getValue().size());
            for(Entry<String, Integer> w: e.getValue().entrySet())
                System.out.println(" "+w.getKey() +": "+w.getValue());
        }*/
        Map<String, Integer> record;
        int docLength;
        try {
            record = docs.get(doc);
            docLength = docLengths.get(doc);
        } catch (NullPointerException e) {
            return null;
        }
        List<Pair<Double, String>> pmis = new ArrayList<Pair<Double, String>>();        
        for (Entry<String, Integer> word : record.entrySet()) {
            double pmi = pmi(record, docLength, word.getKey());
            pmis.add(new Pair<Double, String>(pmi, word.getKey()));
            //System.out.println(word.getKey() +" "+pmi);
        }
        Collections.sort(pmis, new PairComparator());
        List<String> output = new ArrayList<String>();
        for (Pair<Double, String> entry : pmis) {
            if (limit-- <= 0) {
                break;
            }
            output.add(entry.second);
        }
        return output;
    }
    
    public List<String> getEntities(int doc) {
        return entities.get(doc);
    }

}
