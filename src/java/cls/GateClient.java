package cls;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.InvalidOffsetException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * other entities extractors: http://www.pingar.com/getentities/
 */

public class GateClient {
    
    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;
    
    // whether the GATE is initialised
    private static boolean isGateInitilised = false;
    
    ArrayList<ProcessingResource> processingResources = null;
    ArrayList<Document> documents = new ArrayList<Document>();
    Corpus corpus = null;
    
    PMI pmi;
    
    private static GateClient instance;
    
    private GateClient () {
            
        try {
            
            initialiseGate();
            
            if (!isGateInitilised) {
                System.err.println("Gate failed to initialize");
                return;
            }
            
            processingResources = createProcessingResources();
            annotationPipeline = createPipeline();            
            corpus = Factory.newCorpus("");
            pmi = PMI.getInstance();
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static GateClient getInstance() {
        if (instance == null) instance = new GateClient();
        return instance;
    }
    
    public void clear() {
        try {
            corpus = Factory.newCorpus("");
            pmi.clear();
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ArrayList<ProcessingResource> createProcessingResources() throws ResourceInstantiationException {
        ArrayList<ProcessingResource> list = new ArrayList<ProcessingResource>();
        
        //register OpenNLP
        try {
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "OpenNLP").toURI().toURL());
            // locate the JAPE grammar file
            File japeOrigFile = new File("/home/jirka/DDW/hw1.jape");
            java.net.URI japeURI = japeOrigFile.toURI();
            
            // create feature map for the transducer
            FeatureMap transducerFeatureMap = Factory.newFeatureMap();
            // set the grammar location
            transducerFeatureMap.put("grammarURL", japeURI.toURL());
            // set the grammar encoding
            transducerFeatureMap.put("encoding", "UTF-8");           
            // create an instance of a JAPE Transducer processing resource
            ProcessingResource japeTransducerPR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerFeatureMap);
            
                
        // create an instance of a Document Reset processing resource
        list.add((ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR"));
        // create an instance of a English Tokeniser processing resource
        list.add((ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser"));
        // create an instance of a Sentence Splitter processing resource
        list.add((ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter"));
        //openNLP for NE
        list.add((ProcessingResource) Factory.createResource("gate.opennlp.OpenNLPNameFin"));
        list.add((ProcessingResource) Factory.createResource("gate.creole.POSTagger"));
        //create Lookup
        list.add((ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer"));
        list.add(japeTransducerPR);
        
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }
    
    private SerialAnalyserController createPipeline() throws ResourceInstantiationException {
        SerialAnalyserController pipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");
        for (ProcessingResource resource: processingResources) {
            pipeline.add(resource);
        }
        return pipeline;
    }
    
    public GateClient addDocument(String doc) {
        try {
            Document d = Factory.newDocument(doc);
            documents.add(d);
            corpus.add(d);
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    public void run() {
        try {
            annotationPipeline.setCorpus(corpus);
            annotationPipeline.execute();
            
            for(int i=0; i< corpus.size(); i++){

                Document doc = corpus.get(i);

                // get the default annotation set
                AnnotationSet as_default = doc.getAnnotations();
                
                FeatureMap futureMap = null;
                
                ArrayList annotations = new ArrayList();                
                annotations.add("Token");
                annotations.add("Organization");
                annotations.add("Person");
                annotations.add("Location");
                
                for (int an = 0; an < annotations.size(); an++) {
                    String annName = (String) annotations.get(an);
                    
                    // get all annotations
                    AnnotationSet annSetTokens = as_default.get(annName,futureMap);
                    //System.out.println("Number of Organization annotations: " + annSetTokens.size());
                    ArrayList tokenAnnotations = new ArrayList(annSetTokens);

                    // looop through annotations
                    for(int j = 0; j < tokenAnnotations.size(); ++j) {

                        // get a token annotation
                        Annotation token = (Annotation)tokenAnnotations.get(j);

                        // get the underlying string for the Token
                        Node isaStart = token.getStartNode();
                        Node isaEnd = token.getEndNode();
                        String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                        //System.out.println("Organization: " + underlyingString);

                        
                        //not token
                        if (an != 0) {
                            pmi.addEntity(i, underlyingString);
                        }
                        //tokens ..for keywords
                        if (an == 0 && i > 0) {
                            FeatureMap annFM = token.getFeatures();
                            String value = (String)annFM.get((Object)"category");
                            if (value != null && value.matches("NN[SP]*")) {
                                pmi.add(i, underlyingString);
                            }
                        }
                        // get the value of the "string" feature
                        //String value = (String)annFM.get((Object)"category");
                        //if (value != null && value.matches("NN[SP]*")) 
                    }
                
                }
            }
            
            
        } catch (ExecutionException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidOffsetException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PMI getPMI() {
        return pmi;
    }
    
    private void initialiseGate() {
        try {
            if (isGateInitilised) return;
            // set GATE home folder
            // Eg. /Applications/GATE_Developer_7.0
            File gateHomeFile = new File("/home/jirka/GATE_Developer_7.1");
            Gate.setGateHome(gateHomeFile);
            
            // set GATE plugins folder
            // Eg. /Applications/GATE_Developer_7.0/plugins            
            File pluginsHome = new File("/home/jirka/GATE_Developer_7.1/plugins");
            Gate.setPluginsHome(pluginsHome);            
            
            // set user config file (optional)
            // Eg. /Applications/GATE_Developer_7.0/user.xml
            Gate.setUserConfigFile(new File("/home/jirka/GATE_Developer_7.1", "user.xml"));            
            
            // initialise the GATE library
            Gate.init();
            
            // load ANNIE plugin
            CreoleRegister register = Gate.getCreoleRegister();
            URL annieHome = new File(pluginsHome, "ANNIE").toURL();
            register.registerDirectories(annieHome);
            
            // flag that GATE was successfuly initialised
            isGateInitilised = true;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
