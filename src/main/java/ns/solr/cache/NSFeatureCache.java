package ns.solr.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import ns.solr.Constants;

/**
 * Class caches the features list for particular core
 */
public class NSFeatureCache {

	private static Map<String, Map<String, String>> featureMap;
	private static String solrHome = null;
	public static String featuresFolderName = "features";
	static Thread featureHandlingThread = null;
	static boolean KEEP_RUNNING = true;

	static {

		featureMap = new LinkedHashMap<String, Map<String, String>>();

		// find out solr home but only once
		if (solrHome == null) {
			solrHome = System.getenv(Constants.SOLR_HOME_PROPERTY);
			System.out.println(solrHome);
			if (solrHome == null) {
				throw new RuntimeException("Please set the "
						+ Constants.SOLR_HOME_PROPERTY);
			}
		}

		// every 5 minutes, we run a thread to update settings
		// At the moment, we
		// are only checking the settings for "KLEVU_PRODUCT" (whether to
		// consider category=KLEVU_PRODUCT) If we know that the
		// settings is already set we will not check for update.
		featureHandlingThread = new Thread() {
			public void run() {
				while (KEEP_RUNNING) {
					if (featureMap != null) {
						for (String coreName : featureMap.keySet()) {
							synchronized (featureMap) {
								featureMap.put(coreName, updateFeatures(coreName));
							}
						}
					}

					// wait for 15 minutes
					try {
						Thread.sleep(900000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		// setting priority and starting
		featureHandlingThread.setPriority(Thread.MIN_PRIORITY);
		featureHandlingThread.start();

		// avoid killing of tomcat process
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				KEEP_RUNNING = false;
				featureHandlingThread.interrupt();
			}
		});
	}

	/**
	 * method returns the feature name for given corename
	 * 
	 * @param coreName
	 * @return
	 */
	public static Map<String, String> getFeature(String coreName) {

		Map<String, String> toReturn = null;
		toReturn = featureMap.get(coreName);
		if (toReturn == null) {
			toReturn = updateFeatures(coreName);
			synchronized (featureMap) {
				featureMap.put(coreName, toReturn);
			}
		}
		return toReturn;
	}

	/**
	 * update the features for given core name
	 * 
	 * @param coreName
	 * @return
	 */
	public static Map<String, String> updateFeatures(String coreName) {

		File coreFolder = new File(solrHome, coreName);

		// we are interested in feature folder
		File featureFolder = new File(coreFolder, featuresFolderName);
		if (!featureFolder.exists()) {
			return null;
		}

		// now reading list file inside feature folder
		File featureListFile = new File(featureFolder, "list");

		Map<String, String> featureList = new LinkedHashMap<String, String>();
		if (featureListFile.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(featureListFile));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.trim().length() == 0 || !line.contains(",")) {
						continue;
					}
					String splits[] = line.split(",");
					featureList.put(splits[0], splits[1]);
				}
				System.out.println("updated featureList: " + featureList);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return featureList;
	}

	/**
	 * method removes the
	 */
	public static void removeFeature(String coreName) {
		if (featureMap.containsKey(coreName)) {
			featureMap.remove(coreName);
			System.out.println("Removed feature core " + coreName);
		}
	}
}
