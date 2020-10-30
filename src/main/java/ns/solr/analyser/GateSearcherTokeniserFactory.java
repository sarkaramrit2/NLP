package ns.solr.analyser;

import java.util.Map;

import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

public class GateSearcherTokeniserFactory extends TokenizerFactory {

	/**
	 * language parameter
	 */
	private String lang = "en";

	private String coreName = null;

	/**
	 * constructor
	 * 
	 * @param args
	 */
	public GateSearcherTokeniserFactory(Map<String, String> args) {
		super(args);
		System.out.println("factory constructor called");
		//assureMatchVersion();
		for (String key : args.keySet()) {
			String value = args.get(key).toString();
			if (key.equals("collection")) {
				coreName = value;
			}
			if (key.equals("klevuLang")) {
				lang = value;
			}
			if (coreName == null) {
				if (key.equals("coreName")) {
					coreName = value;
				}
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public GateSearcherTokeniser create(AttributeFactory attributeFactoryObj) {
		System.out.println("factory create called");
		GateSearcherTokeniser gateSearchTokeniser = null;
		try {
			gateSearchTokeniser = new GateSearcherTokeniser(
					attributeFactoryObj, lang, coreName, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gateSearchTokeniser;
	}

}
