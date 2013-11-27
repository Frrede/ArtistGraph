package fu.inf.artgraph.tagger;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Gibt eine Instanz von Stanford NER zurück, damit diese nur einmal initialisiert werden muss.
 */
public class NERFactory {
	
	private static String serializedClassifier = "dewac_175m_600.crf.ser.gz";
	private static AbstractSequenceClassifier<CoreLabel> classifier;
	
	public static synchronized AbstractSequenceClassifier<CoreLabel> getClassifier() {
		if(classifier == null) {
			classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		}
		return classifier;
	}
	
}
