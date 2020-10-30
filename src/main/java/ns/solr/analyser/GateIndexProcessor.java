package ns.solr.analyser;

import java.util.Calendar;
import java.util.HashMap;

import ns.klevu.nlp.applications.DefaultIndexApplication;
import ns.klevu.nlp.gate.Document;
import ns.klevu.nlp.gate.ResourceInstantiationException;

/**
 * Class provide methods for processing documents at indexing time
 * 
 * @author niraj aswani
 */
public class GateIndexProcessor {

	private static DefaultIndexApplication gateApp = null;

	static {
		System.out.println("static method in indexer processor called");
		try {
			gateApp = new DefaultIndexApplication();
			gateApp.init(new HashMap<String, Object>());
		} catch (ResourceInstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * method returns list of annotation for given term method holds internal
	 * cache
	 * 
	 * @return
	 */
	public static void processDocument(Document aDocument) {
		long start = Calendar.getInstance().getTimeInMillis();
		try {
			// execute the app on the document
			gateApp.execute(aDocument, new HashMap<String, Object>());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long end = Calendar.getInstance().getTimeInMillis();
			System.out.println("GateIndexProcessor returned in :"
					+ (end - start) + "ms ");
		}
	}
}