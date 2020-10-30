package ns.solr.analyser;

import java.util.Map;

import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

public class GateTokeniserFactory extends TokenizerFactory {

	// core name
	private String coreName;

	// lang parameter
	private String lang = "en";

	public GateTokeniserFactory(Map<String, String> args) {
		super(args);
		//assureMatchVersion();
		for (String key : args.keySet()) {
			String value = args.get(key).toString();
			if (key.equals("collection")) {
				coreName = value;
			}
			if (coreName == null) {
				if (key.equals("coreName")) {
					coreName = value;
				}
			}
			if (key.equals("klevuLang")) {
				lang = value;
			}

		}
	}


	@Override
	public GateTokeniser create(AttributeFactory attribFactoryObj) {
		GateTokeniser gateTokeniser = null;

		try {
			gateTokeniser = new GateTokeniser(attribFactoryObj,
					coreName, lang);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gateTokeniser;
	}

}
