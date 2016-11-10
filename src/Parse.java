import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;


public class Parse {
	
	public static void main(String[] args) throws IOException{
		Properties props = new Properties();
        props.put("annotators", "tokenize,ssplit, pos,lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
		
		String inFile = "./brown_corpus.txt";
  	  	String outFile = "./brown_sen_rel.txt";
  	  	BufferedReader in = new BufferedReader(new FileReader(inFile));
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        String line = "";
        int count = 0;
        while((line = in.readLine())!=null){
        	String text = line;
        	String sentence = "Sen: " + text;
        	//System.out.println(sentence);
        	out.append(sentence);
        	out.newLine();
        	ArrayList<String> strList = getEnhancedDep(text,pipeline);
        	for(String rel : strList){
        		//System.out.println(rel);
        		out.append(rel);
            	out.newLine();
        	}
        	if((count++)%200 ==0){
        		System.out.println((count * 100)/57000);
        	}
        }
		in.close();
		out.close();
		
	}
	
	public static ArrayList<String> getEnhancedDep(String text, StanfordCoreNLP pipeline){
		Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        ArrayList<String> strList = new ArrayList<String>();
        for(CoreMap sentence: sentences) {
            SemanticGraph dependencies = sentence.get(EnhancedDependenciesAnnotation.class);
            for (SemanticGraphEdge e : dependencies.edgeIterable()) {
            	String str = "Rel: " + e.getGovernor().word() + " " + e.getRelation().toString() + " " + e.getDependent().word();
            	strList.add(str);
            }
        }
		return strList;
	}
}


