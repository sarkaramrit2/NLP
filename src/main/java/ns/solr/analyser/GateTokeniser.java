package ns.solr.analyser;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import ns.klevu.nlp.gate.Annotation;
import ns.klevu.nlp.gate.AnnotationSet;
import ns.klevu.nlp.gate.Document;
import ns.klevu.nlp.gate.ResourceInstantiationException;
import org.apache.lucene.util.AttributeFactory;
import ns.solr.cache.NSFeatureCache;

/**
 * @author Niraj Aswani
 * @author Dhaivat Dave
 * <p>
 * <p>
 * Notes from the TokenSteram class
 * <p>
 * he workflow of the new TokenStream API is as follows:
 * <p>
 * - Instantiation of TokenStream/TokenFilters which add/get attributes
 * to/from the AttributeSource. </br>
 * - The consumer calls reset(). </br>
 * - The consumer retrieves attributes from the stream and stores local
 * references to all attributes it wants to access. </br>
 * - The consumer calls incrementToken() until it returns false
 * consuming the attributes after each call. </br>
 * - The consumer calls end() so that any end-of-stream operations can
 * be performed. </br>
 * - The consumer calls close() to release any resource when finished
 * using the TokenStream.
 */

public final class GateTokeniser extends Tokenizer {

    private static final String FEATURE_NORMALISED_ROOT = "normalisedRoot";
    private static final String FEATURE_ROOT = "root";
    private static final String FEATURE_UNITS = "units";
    private static final String FEATURE_NORMALISED_STRING = "normalisedString";
    private static Pattern patternForCategory = Pattern.compile("@kuCat@(.+?)@kuCat@");
    private static Pattern patternForDocId = Pattern.compile("@kudocid@(.+?)@kudocid@");
    private static final String FEATURE_STRING = "string";

    /**
     * field which holds the list of terms
     */
    List<Token> terms = new ArrayList<>();

    private final CharTermAttribute charTermAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute position = addAttribute(PositionIncrementAttribute.class);

    /**
     * core name or folder name where index will be created
     */
    private String coreName = null;

    /**
     * final offset
     */
    private int finalOffset = 0;

    /**
     * token counter
     */
    private int tokenCounter = -1;

    /**
     * language parameter
     */
    private String lang;

    /**
     * parameterized constructor
     *
     * @param coreName
     * @throws ResourceInstantiationException
     * @throws IOException
     */
    public GateTokeniser(AttributeFactory attribFactoryObj, String coreName, String lang) {
        super(attribFactoryObj);
        this.coreName = coreName;
        this.lang = lang;

        //System.out.println("Tokeniser created: coreName:" + coreName + " lang:" + lang);
    }

    /**
     * processed the indexed text with GATE application
     */
    private void processText() {
        try {

            Document processedDocument = null;

            try {

                // method which returns the string for given reader
                String string = readFully(input).trim();

                // making sure if the empty string is provided we should return
                if (string.trim().length() == 0)
                    return;

                Pattern p = Pattern.compile("@ku@(.+?)@ku@");
                Matcher m = p.matcher(string);

                // local declaration
                boolean titleContentFound = false;
                boolean categoryContentFound = false;
                boolean documentIdFound = true;
                boolean titleContentOnlyFound = false;
                boolean originalContentFound = false;
                boolean categoryInflectedFound = false;
                boolean categorySemanticsFound = false;
                boolean titleSemanticsFound = false;

                String docIdForProductName = null;
                String documentId = null;
                String categoryValue = null;

                // if we find a match, get the group
                if (m.find()) {
                    // get the matching group
                    String codeGroup = m.group(1);
                    documentId = codeGroup;
                    if (documentId.equalsIgnoreCase("kuCategory")) {
                        categoryContentFound = true;
                        documentIdFound = false;
                    } else if (documentId.equalsIgnoreCase("kuCategoryInflected")) {
                        categoryInflectedFound = true;
                        documentIdFound = false;
                    } else if (documentId.equalsIgnoreCase("kuCategorySemantics")) {
                        categorySemanticsFound = true;
                        documentIdFound = false;
                    } else if (documentId.equalsIgnoreCase("kuTitle")) {
                        titleContentFound = true;
                        documentIdFound = false;
                    } else if (documentId.equalsIgnoreCase("kuTitleOnly")) {
                        titleContentOnlyFound = true;
                        documentIdFound = false;
                    } else if (documentId.equalsIgnoreCase("kuTitleSemantics")) {
                        titleSemanticsFound = true;
                        documentIdFound = false;
                    } else if (documentId.equalsIgnoreCase("kuOriginalContent")) {
                        originalContentFound = true;
                        documentIdFound = false;
                    } else {
                        documentIdFound = true;
                    }
                }

                Matcher matcherForCategory = patternForCategory.matcher(string);

                // if we find a match
                if (matcherForCategory.find()) {
                    categoryValue = matcherForCategory.group(1);
                }

                // removing document id
                String newString = string;

                // making sure to remove all meta data information
                if (documentId != null) {

                    String idToReplace = "@ku@" + documentId + "@ku@";
                    newString = string.replace(idToReplace, "");
                }

                // if category is not null
                if (categoryValue != null) {

                    String categoryToReplace = "@kuCat@" + categoryValue + "@kuCat@";
                    newString = newString.replace(categoryToReplace, "");

                } else {

                    newString = newString.replace("@kuCat@@kuCat@", "");
                    categoryValue = "";
                }

                // pattern for document id for product name
                Matcher matcherForDocId = patternForDocId.matcher(string);

                // if we find a match
                if (matcherForDocId.find()) {
                    docIdForProductName = matcherForDocId.group(1);
                }

                if (docIdForProductName != null) {

                    String toReplace = "@kudocid@" + docIdForProductName + "@kudocid@";
                    newString = newString.replace(toReplace, "");

                }

                newString = removeHTMLTags(StringEscapeUtils.unescapeXml(newString));

                // if there is no data found after removing html tgas
                if (newString.trim().length() == 0) {
                    return;
                }

                // step 1: create a gate document from the provided string
                processedDocument = new Document(newString);

                // set document language parameter
                if (lang != null) {
                    processedDocument.getFeatures().put("lang", new String(lang));
                }

                // set the core name parameter
                if (coreName != null) {
                    processedDocument.getFeatures().put("core", new String(coreName));
                }

                // passing the category of the product
                if (categoryValue != null) {
                    processedDocument.getFeatures().put("category", new String(categoryValue));
                }

                // adding more document features related to which features
                // should be enabled
                if (coreName != null) {
                    Map<String, String> docFeature = NSFeatureCache.getFeature(new String(coreName));
                    if (docFeature != null) {
                        for (String feature : docFeature.keySet()) {
                            processedDocument.getFeatures().put(feature.trim(), docFeature.get(feature).trim());
                        }
                    }
                }

                // use this code to process it with http
                // step 2: process the corpus with the controller
                // use this code to process document locally
                GateIndexProcessor.processDocument(processedDocument);

                // step 3: access annotations from the default annotation set
                // and create a token stream
                // logic for contents fields only
                if (documentIdFound || categorySemanticsFound || titleSemanticsFound) {
                    // add klevu tokens only
                    addKlevuTokensOnly(processedDocument.getAnnotations().get("Word"));
                }

                // for original text
                if (categoryContentFound || titleContentOnlyFound || originalContentFound) {
                    addOriginalTokensOnly(processedDocument.getAnnotations().get("Token"));
                }

                // for inflected category
                if (categoryInflectedFound || titleContentFound) {
                    addInflectedTokensOnly(processedDocument.getAnnotations().get("Token"));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOriginalTokensOnly(AnnotationSet annotSet) {

        List<Annotation> sortedAnnotations = annotSet.inDocumentOrder();

        for (Annotation annot : sortedAnnotations) {

            int annotSO = annot.getStart();
            int annotEO = annot.getEnd();

            String originalString = annot.getFeatures().get(FEATURE_STRING).toLowerCase();

            if (originalString != null && originalString.trim().length() > 0) {

                Token aFirstToken = new Token(originalString, annotSO, annotEO);
                aFirstToken.setPositionIncrement(1);
                terms.add(aFirstToken);

                // adding the normalised strings to the solr index
                if (annot.getFeatures().get(FEATURE_NORMALISED_STRING) != null) {
                    String normalisedOriginalString = annot.getFeatures().get(FEATURE_NORMALISED_STRING).toLowerCase();

                    if (normalisedOriginalString.trim().length() > 0
                            && !normalisedOriginalString.equalsIgnoreCase(originalString)) {
                        Token normalisedToken = new Token(normalisedOriginalString, annotSO, annotEO);
                        normalisedToken.setPositionIncrement(0);
                        terms.add(normalisedToken);
                    }
                }

                String units = annot.getFeatures().get(FEATURE_UNITS);

                if (units != null && units.trim().length() > 0) {

                    String words[] = units.split("[ ]+");
                    for (String aWord : words) {
                        aFirstToken = new Token(aWord.toLowerCase(), annotSO, annotEO);
                        aFirstToken.setPositionIncrement(0);
                        terms.add(aFirstToken);
                    }
                }
            }
        }
    }

    private void addInflectedTokensOnly(AnnotationSet annotSet) {

        List<Annotation> sortedAnnotations = annotSet.inDocumentOrder();

        for (Annotation annot : sortedAnnotations) {

            int annotSO = annot.getStart();
            int annotEO = annot.getEnd();

            String rootFormOfString = null;
            String normalisedFormOfString = null;

            if (annot.getFeatures().get(FEATURE_ROOT) != null) {
                rootFormOfString = annot.getFeatures().get(FEATURE_ROOT).toLowerCase();
                if (annot.getFeatures().get(FEATURE_NORMALISED_ROOT) != null) {
                    normalisedFormOfString = annot.getFeatures().get(FEATURE_NORMALISED_ROOT).toLowerCase();
                }
            } else {
                rootFormOfString = annot.getFeatures().get(FEATURE_STRING).toLowerCase();
                if (annot.getFeatures().get(FEATURE_NORMALISED_STRING) != null) {
                    normalisedFormOfString = annot.getFeatures().get(FEATURE_NORMALISED_STRING).toLowerCase()
                            .toLowerCase();
                }
            }

            // if root form found and not equal to original string
            if (rootFormOfString != null) {
                Token aRootToken = new Token(rootFormOfString, annotSO, annotEO);
                aRootToken.setPositionIncrement(1);
                terms.add(aRootToken);

                // adding the normalised strings to the solr index
                if (normalisedFormOfString != null && normalisedFormOfString.trim().length() > 0
                        && !normalisedFormOfString.equalsIgnoreCase(rootFormOfString)) {
                    aRootToken = new Token(normalisedFormOfString, annotSO, annotEO);
                    aRootToken.setPositionIncrement(0);
                    terms.add(aRootToken);
                }
            }

        }
    }

    private void addKlevuTokensOnly(AnnotationSet annotationSet) {
        List<Annotation> sortedAnnotations = annotationSet.inDocumentOrder();
        long startOffset = -1;

        for (Annotation annot : sortedAnnotations) {
            int annotSO = annot.getStart();
            int annotEO = annot.getEnd();

            Set<Token> toAppend = new HashSet<>();

            // now index every feature key value
            for (Object key : annot.getFeatures().keySet()) {

                if (!key.equals(FEATURE_STRING))
                    continue;

                String stringToIndex = annot.getFeatures().get(key).toString().toLowerCase().trim();

                // if it is a multiterm synonym/semantic cat and so on.
                // i.e. x_y
                if (stringToIndex.contains("_")) {
                    String[] stringTerms = stringToIndex.split("[_ ]+");
                    if (stringTerms.length >= 1) {
                        for (String t : stringTerms) {
                            Token aToken = new Token(t.toLowerCase(), annotSO, annotEO);
                            toAppend.add(aToken);
                        }
                    }
                } else {
                    Token aToken = new Token(stringToIndex, annotSO, annotEO);
                    toAppend.add(aToken);
                }
            }

            // here will have one or more tokens in the toAppen set
            // now add all the appended terms here
            for (Token toIndex : toAppend) {
                if (startOffset == -1 || annotSO > startOffset) {
                    toIndex.setPositionIncrement(1);
                } else {
                    toIndex.setPositionIncrement(0);
                }
                terms.add(toIndex);
            }
            startOffset = annotSO;
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
        StringBuilder buf = new StringBuilder();
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

    /**
     * method replaces removes the html tags and returns the new string
     *
     * @param contentToReplace
     * @return
     */
    public String removeHTMLTags(String contentToReplace) {
        return contentToReplace.replaceAll("\\<.*?\\>", " ").replaceAll("[ ]+", " ").trim();
    }

    /**
     * method converts string to to lowercase
     *
     * @param str
     * @return
     */
    public static boolean isAllUpperCase(String str) {
        boolean upperFound = true;
        for (char c : str.toCharArray()) {
            if (Character.isLowerCase(c)) {
                upperFound = false;
                break;
            }
        }
        return upperFound;
    }

    /**
     * method reset the record
     *
     * @throws IOException
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        tokenCounter = -1;
        terms = new ArrayList<>();
        // counter++;
        //System.out.println("Same Tokeniser Being used for the time:" + counter);
    }

    /**
     * close object
     *
     * @throws IOException
     */

    /**
     * method increments token
     *
     * @return
     * @throws IOException
     */
    @Override
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
     * called when end method gets called
     *
     * @throws IOException
     */
    @Override
    public final void end() throws IOException {
        super.end();
        // setting final offset
        offsetAttribute.setOffset(finalOffset, finalOffset);

        // New addition
        terms.clear();

        //System.out.println("GateTokeniser end() called");
    }

    @Override
    public void close() throws IOException {
        super.close();
        terms = null;

        //System.out.println("GateTokeniser close() called");
    }
}