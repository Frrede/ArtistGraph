import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import fu.inf.artgraph.tagger.NERFactory;

import java.util.List;
import java.io.IOException;

public class nerTest {

	public static void main(String[] args) throws IOException {

		//String serializedClassifier = "dewac_175m_600.crf.ser.gz";

		AbstractSequenceClassifier<CoreLabel> classifier = NERFactory.getClassifier();


		String t4 = "William Henry Gates ist ein US-amerikanischer Unternehmer, Programmierer und Mäzen. Bill Gates ist seit 2013 mit einem geschätzten Vermögen von 72,7 Mrd. US-Dollar wieder der reichste Mensch der Welt. Darüber hinaus hat er bisher 28 Milliarden Dollar der von ihm und seiner Frau gegründeten Bill & Melinda Gates Foundation gespendet. Gates gründete 1975 in den USA, gemeinsam mit Paul Allen, die Microsoft Corporation.";
		
		// System.out.println(classifier.classifyToString(s1));
		System.out.println(classifier.classifyWithInlineXML(t4));

		/*
		List<List<CoreLabel>> out = classifier.classify(t4);
		for (List<CoreLabel> sentence : out) {
			for (CoreLabel word : sentence) {

				String a = word.get(AnswerAnnotation.class);

				if(a.equals("I-PER")) {
					System.out.print(word.word() + " ");
				}
			}
			System.out.println();
			
		}
		
		
		AbstractSequenceClassifier<CoreLabel> classifier2 = NERFactory.getClassifier();
		List<List<CoreLabel>> out2 = classifier2.classify(t4);
		for (List<CoreLabel> sentence : out2) {
			for (CoreLabel word : sentence) {

				String a = word.get(AnswerAnnotation.class);

				if(a.equals("I-PER")) {
					System.out.print(word.word() + " ");
				}
			}
			System.out.println();
			
		}
		
		*/
	}

}
