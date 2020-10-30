package ns.solr.analyser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ns.klevu.nlp.applications.DefaultQueryApplication;
import ns.klevu.nlp.gate.Annotation;
import ns.klevu.nlp.gate.Document;
import ns.klevu.nlp.gate.ResourceInstantiationException;
import ns.solr.cache.NSFeatureCache;

import org.apache.lucene.analysis.Token;

/**
 * Class provide methods for processing term(query)
 * 
 * @author dhaivat dave
 */
public class GateSearcherProcessor {
	// map for term cache
	private static Map<String, List<Token>> termCache;

	// max size for cache
	private static int MAX_SIZE_FOR_CACHE = 3000;

	private static DefaultQueryApplication gateApp = null;

	static {
		System.out.println("static method in searcher processor called");
		// init term cache
		termCache = new LinkedHashMap<String, List<Token>>();

		try {
			gateApp = new DefaultQueryApplication();
			gateApp.init(new HashMap<String, Object>());
		} catch (ResourceInstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * method returns list of annotation for given term method holds internal
	 * cache
	 * 
	 * @param term
	 * @return
	 */
	public static List<Token> processQuery(String term, String lang,
			String coreName, boolean applyRootInflection) {
		// list of annotation to return
		List<Token> toReturn = new ArrayList<Token>();

		// lower casing term and trimming
		String termToSearch = term.toLowerCase().trim();

		// document processed by application
		Document processedDocument = null;

		long start = Calendar.getInstance().getTimeInMillis();

		try {
			// // if term is already processed then we will just return list of
			// // annotations
			// // no need to process them again
			// if (termCache.containsKey(termToSearch)) {
			// toReturn = termCache.get(termToSearch);
			// } else {
			// create document with existing term

			// as a part of split model sku logic, we are splitting the token
			// having a delimiter (e.g. '-','_')
			if (termToSearch != null
					&& (termToSearch.equals("klevu_product")
							|| termToSearch.equals("klevu_cms") || termToSearch
								.equals("klevu_category"))) {

				int annotSO = 0, annotEO = 13;

				if (termToSearch.equals("klevu_category")) {
					annotEO = 14;
				} else if (termToSearch.equals("klevu_cms")) {
					annotEO = 9;
				}

				Token aToken = new Token(termToSearch, annotSO, annotEO);
				aToken.setPositionIncrement(1);
				toReturn.add(aToken);
				return toReturn;
			}

			processedDocument = new Document(termToSearch);

			// set document language parameter
			processedDocument.getFeatures().put("lang", lang);
			processedDocument.getFeatures().put("coreName", coreName);
			
			if (coreName != null && coreName.trim().length() > 0) {

				Map<String, String> docFeature = NSFeatureCache
						.getFeature(coreName);

				if (docFeature != null && !docFeature.isEmpty()) {
					for (String feature : docFeature.keySet()) {
						processedDocument.getFeatures().put(feature.trim(),
								docFeature.get(feature).trim());
					}
				}
			}

			// execute the app on the document
			gateApp.execute(processedDocument, new HashMap<String, Object>());

			// get document annotation
				List<Annotation> sortedAnnotations = processedDocument
						.getAnnotations().get("Token").inDocumentOrder();
	
				String newTerm = null;
				for (Annotation annot : sortedAnnotations) {
					int annotSO = annot.getStart();
					int annotEO = annot.getEnd();
	
					newTerm = null; 
					if (applyRootInflection) {
						newTerm = (String) annot.getFeatures().get("root");
					}
					if (newTerm == null) {
						newTerm = processedDocument.string(annot);
					}
	
					newTerm = newTerm.toLowerCase().trim();
					Token aToken = new Token(newTerm, annotSO, annotEO);
					aToken.setPositionIncrement(1);
					toReturn.add(aToken);
				}

			// // we need to make sure that no of elements inside cache should
			// // not be greater then MAX_SIZE_CACHE
			// // in case if it exceed remove first one
			//
			// if (termCache.size() >= MAX_SIZE_FOR_CACHE) {
			// // following FIFO mechanism
			// for (String termToRemove : termCache.keySet()) {
			// termCache.remove(termToRemove);
			// break;
			// }
			// }
			//
			// // put the processed list of annotation to termcache
			// termCache.put(termToSearch, toReturn);

			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long end = Calendar.getInstance().getTimeInMillis();
			System.out.println("GateSearcherProcessor returned in :"
					+ (end - start) + "ms ");
		}

		return toReturn;
	}

}