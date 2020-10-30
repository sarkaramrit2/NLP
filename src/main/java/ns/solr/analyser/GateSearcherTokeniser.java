package ns.solr.analyser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeFactory;

/**
 * @author Niraj Aswani
 * @author Dhaivat Dave
 */
public final class GateSearcherTokeniser extends Tokenizer {
	/**
	 * field which holds the list of terms
	 */
	List<Token> terms = new ArrayList<Token>();

	/**
	 * object that defines term attribute
	 */
	private final CharTermAttribute charTermAtt = addAttribute(CharTermAttribute.class);

	/**
	 * Object that defines the offset attribute
	 */
	private final OffsetAttribute offsetAttribute = (OffsetAttribute) addAttribute(OffsetAttribute.class);

	/**
	 * Object that defines the position attribute
	 */
	private final PositionIncrementAttribute position = (PositionIncrementAttribute) addAttribute(PositionIncrementAttribute.class);

	/**
	 * final offset to save
	 */
	private int finalOffset = 0;

	/**
	 * token counter
	 */
	private int tokenCounter = -1;

	/**
	 * language parameter
	 */
	private String lang = "en";

	/**
	 * core name or folder name where index will be created
	 */
	private String coreName = null;
	
	/**
	 * To apply root inflections
	 */
	private boolean applyRootInflection = true;

	/**
	 * - Parameterized constructor
	 * 
	 * @param attributeFactory
	 */
	public GateSearcherTokeniser(AttributeFactory attributeFactory
			, String lang, String coreName, boolean applyRootInflection) {
		super(attributeFactory);
		this.lang = lang;
		this.coreName = coreName;
		this.applyRootInflection = applyRootInflection;
	}

	private List<Token> processText() throws IOException {
		try {

			// method which returns the string for given reader
			String string = readFully(input).trim();
			// making sure if the empty string is provided we should return
			if (string.trim().length() == 0)
				return null;
			// create a token stream
			// pass term to GateSearcherProcessor and get the annotaion list
			System.out.println("token: " + string + ", lang: " + lang + ", " +
					"coreName: " + coreName + ", applyRootInflection: " + applyRootInflection);
			List<Token> listOfTokens = GateSearcherProcessor.processQuery(
					string, lang, coreName, applyRootInflection);
			terms.addAll(listOfTokens);
			// adding all tokens to term list
			return listOfTokens;
		} finally {
			// nothing to return
		}
	}

	/**
	 * method returns
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public String readFully(Reader reader) throws IOException {
		char[] arr = new char[8 * 1024]; // 8K at a time
		StringBuffer buf = new StringBuffer();
		int numChars;
		try {
			while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
				buf.append(arr, 0, numChars);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	public final void end() throws IOException {
		super.end();
		offsetAttribute.setOffset(finalOffset, finalOffset);
	}

	/**
	 * method gets called when token increments
	 */
	public final boolean incrementToken() throws IOException {

		if (tokenCounter == -1) {
			processText();
		}
		clearAttributes();
		tokenCounter++;

		if (tokenCounter < terms.size()) {
			Token aToken = terms.get(tokenCounter);
			charTermAtt.append(aToken.toString());
			charTermAtt.setLength(aToken.length());
			finalOffset = aToken.endOffset();
			offsetAttribute.setOffset(aToken.startOffset(), aToken.endOffset());
			position.setPositionIncrement(aToken.getPositionIncrement());
			return true;
		}

		return false;

	}

	/**
	 * method resets all the variables
	 */
	public void reset() throws IOException {
		super.reset();
		tokenCounter = -1;
		terms = new ArrayList<>();
	}

}
