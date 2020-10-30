package ns.solr;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

public class Constants {
	/** solr home environment variable shared with solr */
	public static final String SOLR_HOME_PROPERTY = "SOLR_HOME";
	public static final String KLEVU_CS_URL = "KLEVU_CS_URL";
	public static final String KLEVU_INDEXER_URL = "KLEVU_INDEXER_URL";

	/**
	 * when user asked for same field to be indexed and store add this post fix
	 * value to field
	 **/
	public static final char INDEXED_SEARCHED_BOTH = '~';

	/** character for filter **/
	public static final char FILTER = '@';

	/** no of results to return **/
	public static final String NO_OF_RESULTS = "noOfResults";

	/** pagination start from **/
	public static final String PAGINATION_START_FROM = "paginationStartFrom";

	/** category field name **/
	public static final String CATEGORY_FIELD_NAME = "categoryField";

	/** category field value **/
	public static final String CATEGORY_FIELD_VALUE = "categoryValue";

	/** pricing field name **/
	public static final String PRICING_FIELD_NAME = "pricingField";

	/** pricing field value **/
	public static final String PRICING_FIELD_VALUE = "pricingFieldValue";

	/** country field name **/
	public static final String COUNTRY_FIELD_NAME = "countryFieldName";

	/** country field value **/
	public static final String COUNTRY_FIELD_VALUE = "countryFieldValue";

	/** total result found when query hits **/
	public static final String TOTAL_RESULTS_FOUND = "totalResultsFound";

	/** response headers **/
	public static final String RESPONSE_HEADERS = "responseHeaders";

	/** sort rating fields **/
	public static final String SORT_RATINGS = "sortRatings";

	/** sort uk related ratings **/
	public static final String SORT_UK_RATINGS = "field16";

	/** sort fi related ratings **/
	public static final String SORT_FI_RATINGS = "field17";

	/** sort fi related ratings **/
	public static final String SORT_US_RATINGS = "field18";

	/** sort fi related ratings **/
	public static final String SORT_IN_RATINGS = "field19";

	/** name of klevu pricing field **/
	public static final String KLEVU_PRICING = "klevu_price";

	/** name of klevu category field **/
	public static final String KLEVU_CATEGORY = "klevu_category";

	/** category with inflections **/
	public static final String KLEVU_CATEGORY_INFLECTED = "klevu_category_inflected";

	/** category with semantics **/
	public static final String KLEVU_CATEGORY_SEMANTICS = "klevu_category_semantics";

	// Code inserted by Agam Shah [08-Mar-2018]
	/** Used for core category boosting **/
	public static final String KLEVU_CATEGORY_CLASSIFICATION_SRC = "klevu_category_classification_src";

	/** name of klevu title field **/
	public static final String KLEVU_TITLE = "klevu_title";

	/** name of klevu title only field **/
	public static final String KLEVU_TITLE_ONLY = "klevu_title_only";

	/** title with semantics **/
	public static final String KLEVU_TITLE_SEMANTICS = "klevu_title_semantics";

	/** name of boosting field **/
	public static final String KLEVU_BOOSTING = "klevu_product_boosting";

	/** name of boosting field **/
	public static final String KLEVU_INDIVIDUAL_BOOSTING = "klevu_manual_boosting";

	/** name of boosting field **/
	public static final String KLEVU_BULK_BOOSTING = "klevu_bulk_boosting";

	/** name of boosting field **/
	public static final String KLEVU_SELFLEARN_BOOSTING = "klevu_selflearning_boosting";

	/** name of product popularity field **/
	public static final String KLEVU_POPULAR_KEYWORDS = "klevu_popular_keywords";

	/** name of boosting keyword field **/
	public static final String KLEVU_BOOSTING_KEYWORDS = "klevu_boosting_keywords";

	// Code inserted by Agam Shah [08-Feb-2018]
	/** name of exclusion keyword field **/
	public static final String KLEVU_EXCLUSION_KEYWORDS = "klevu_exclusion_keywords";

	/** name of boosting keyword field **/
	public static final String KLEVU_PRODUCT_NOUN = "klevu_product_noun";

	/** name of content field **/
	public static final String KLEVU_CONTENT = "contents";

	/** name of original contents field **/
	public static final String KLEVU_CONTENT_ORIGINAL = "contents_original";

	/** name of offset field **/
	public static final String KLEVU_OFFSETS = "offsets";

	/** name of score field **/
	public static final String KLEVU_SCORE = "score";

	/** name of id field **/
	public static final String KLEVU_DOC_ID = "id";

	/** enable boosting **/
	public static final String ENABLE_BOOSTING = "enableBoosting";

	/** name of url field **/
	public static final String URL_FIELD_NAME = "url";

	/** name of product id **/
	public static final String KLEVU_PARENT_ID = "klevu_parent_id";

	/** name of klevu_rating field where the ratings are stored **/
	public static final String KLEVU_RATING = "klevu_rating";

	/** version field **/
	public static final String VERSION_FIELD = "_version_";

	/** creation date **/
	public static final String KLEVU_CREATION_DATE = "klevu_creation_date";

	/** for indexer task **/
	public static final String KLEVU_INDEXING_TASK = "klevu-indexer";

	/** for searching task **/
	public static final String KLEVU_SEARCHING_TASK = "klevu-searcher";

	/** for date sorting **/
	public static final String KLEVU_DATE_SORTING = "dateSorting";

	/** for holding url : used for grouping function **/
	public static final String KLEVU_URL = "klevu_url";

	/** type of store **/
	public static final String TYPE_OF_STORE = "store_type";

	public static final String KLEVU_STOCK_FIELD = "klevu_in_stock";
	public static final String KLEVU_GROUP_FIELD = "klevu_item_group";

	/** query filters to set **/
	public static final String KLEVU_QUERY_FILTER = "queryFilters";

	/** operatory to be used in query **/
	public static final String KLEVU_OPERATOR = "klevu_operator";

	/** whether to disable klevu popularity **/
	public static final String DISABLE_KLEVU_SORTING = "true";

	/** for storing currency **/
	public static final String KLEVU_CURRENCY = "klevu_currency";

	/** default field to search **/
	public static final String KLEVU_DEFAULT_FIELD_TO_SEARCH = "default";

	/** and query parameters **/
	public static final String KLEVU_AND_QUERY = "andQuery";

	/** OR query parameters **/
	public static final String KLEVU_OR_QUERY = "orQuery";

	/** enable wild card search or not **/
	public static final String KLEVU_ENABLE_WILD_CARD_SEARCH = "wildCardSearch";

	/** enable proximity search or not **/
	public static final String KLEVU_ENABLE_PROXIMITY_SEARCH = "proximitySearch";

	/** character for wild card **/
	public static final String KLEVU_WILD_CARD_CHAR = "*";

	/** character for proximity **/
	public static final String KLEVU_FUZZY_CHAR = "~";

	/** proximity values **/
	public static final String KLEVU_FUZZY_CHAR_VALUE = "0.70";

	/** group limit **/
	public static final String KLEVU_GROUP_LIMIT = "groupLimit";

	public static final String KLEVU_GROUP_QUERY = "groupQuery";

	public static final String KLEVU_FILTER_FIELDS = "filters";

	public static final String KLEVU_ENABLE_FILTERS = "enableFilters";

	public static final String KLEVU_FILTER_RESULTS = "filterQueries";

	public static final String KLEVU_LAST_SEARCHED_QUERY_TYPE = "lastSearchedQueryType";

	public static final String KLEVU_EXACT_MATCH = "exactMatch";

	public static final String KLEVU_TYPE_QUERY = "typeOfQuery";

	public static final String KLEVU_TOTAL_VARIANTS_TEXT = "totalVariants";

	public static final String KLEVU_ENABLE_SEARCH_ON_SKU = "searchOnSku";

	public static final String KLEVU_FIELDS_TO_RETURN = "fl";

	public static final String KLEVU_OTHER_FIELD = "other";

	public static final int ALLOWED_FILTER_FIELD_SIZE = 500;

	public static final int ALLOWED_FILTER_FIELD_VALUE = 1000;

	public static final String KLEVU_ENABLE_WILD_CARD_SEARCH_FOR_TERM = "wildCardSearchOnTerm";

	public static final String KLEVU_ENABLE_PARTIAL_SEARCH_FOR_TERM = "partialMatch";

	public static final String KLEVU_ENABLE_PARTIAL_SEARCH_FOR_ALL_TERMS = "partialMatchForAllTerms";

	public static final String KLEVU_GROUP_APPEND = "_klevu_grouping";

	public static final String KLEVU_ATTRIBUTE_FIELD = "klevu_attribute";

	public static final String KLEVU_CUSTOM_BOOSTING_FIELD = "boostingAttribute";

	public static final String KLEVU_PRICE_INTERVAL = "priceInterval";

	public static final String KLEVU_NAME_FOR_SORTING = "klevu_name_sorting";

	public static final String KLEVU_FACET_LIMIT = "klevu_facet_limit";

	public static final String KLEVU_FETCH_MIN_MAX_PRICE = "fetchMinMaxPrice";

	public static final String KLEVU_SHOW_PRICE_FILTER = "showPriceFilter";

	public static final String KLEVU_MULTISELECT_FACET = "multiSelectFacet";

	public static final String KLEVU_ENABLE_MANUAL_SYNONYNS = "enableManualSynonyms";

	public static final String KLEVU_FUZZY_SCORE = "fuzzyScore";

	public static final String KLEVU_CUSTOM_PRICE_RANGES = "customPriceRanges";

	public static final String KLEVU_DISABLE_PRICE_RANGES = "disablePriceRanges";

	public static final String KLEVU_STOCK_SORT_ORDER = "stockOrder";

	public static final String KLEVU_SORT_BY_USER_PREFERENCE = "sortByUserPreference";

	/** and query parameters **/
	public static final String KLEVU_AND_QUERY_TO_BOOST_LISTCATEGORY = "andQueryToBoostListCategory";

	public static final String KLEVU_SORT_ORDER = "sortOrder";

	public static final String KLEVU_ENABLE_TIE = "enableTie";

	public static final String KLEVU_SEARCH_BY_ID = "searchById";

	public static final String KLEVU_DISABLE_GROUPING = "disbleGrouping";

	public static final String KLEVU_MM_PARAM = "mm";

	/**
	 * inserted by Gunjan - [24-March-2018] for category specific promotion
	 * [category merchandising] ( for sorting parameter)
	 */
	public static final String KLEVU_BOOSTING_CATEGORY = "klevu_boosting_categories";

	// TODO inserted by Agam Shah [24-Apr-2018] for DebugQuery
	// used for debugQuery enable/disable
	public static final String KLEVU_DEBUG_PARAM = "debugQuery";
	// used to store synonyms is enable/disable
	public static final String KLEVU_DEBUG_SYNONYMS = "klevu_manual_synonyms";
	// used to store decompounding is enable/disable
	public static final String KLEVU_DEBUG_DECOMPOUNDING = "klevu_decompounding_status";
	// used to store solr query fired internally (Q param)
	public static final String KLEVU_DEBUG_SOLAR_QUERY = "solrQuery";
	// used to store debugFields
	public static final String KLEVU_DEBUG_FIELDS_PARAM = "debugFields";
	// used to store search in desc is enable/disable
	public static final String KLEVU_ENABLE_SEARCH_IN_DESC = "enable_search_in_description";
	// used to store partial match is enable/disable
	public static final String KLEVU_ENABLE_PARTIAL_MATCH = "enable_partial_match";
	// used to store fuzzy match is enable/disable
	public static final String KLEVU_ENABLE_FUZZY_MATCH = "enable_fuzzy_match";
	// used to store custom query filter is enable/disable
	public static final String KLEVU_ENABLE_CUSTOM_QUERY_FILTER = "enable_custom_query_filter";

	// code inserted by Agam Shah [28-Jun-2018] for Personalized Search
	// used to store fields that we want to boost in solr bq param
	public static final String KLEVU_OPTIONAL_FILTER_FIELDS = "optionalFilters";
	// used to store query that we want to parse in solr bq param
	public static final String KLEVU_OPTIONAL_FILTER_QUERY = "optionalFiltersQuery";

	// code inserted by Agam Shah [24-Jul-2018] for custom facet sorting
	public static final String KLEVU_FACETS_VALUES_SORT_CLASSES = "facetsValuesSortClasses";

	// used by multiple currency code
	public static final String KLEVU_SEARCH_CURRENCY = "priceFieldSuffix";

	// sale price for multiple currency
	public static final String KLEVU_SALEPRICE_CURRENCY = "salePrice_";
	public static final String KLEVU_SALE_PRICE = "salePrice";
	public static final String KLEVU_TO_PRICE = "toPrice";
	public static final String KLEVU_FROM_PRICE = "fromPrice";
	public static final String KLEVU_PRICE = "price";

	public static final String KLEVU_CUSTOM_BOOST = "customBoost";

	// visibility for product
	public static final String KLEVU_PRODUCT_VISIBILITY = "visibility";

	public static final String ENABLE_PRODUCT_VISIBILITY = "productVisibility";

	public static final String DEFAULT_PRODUCT_VISIBILITY = "catalog-search";

	// Code inserted by Agam Shah [04-Oct-2018] - To fetch boosting score from redis
	public static final String REDIS_BOOSTING = "klevusearch:_all:";
	public static final String REDIS_INDIVIDUAL_BOOSTING = "klevusearch:manual:_all:";
	public static final String REDIS_BULK_BOOSTING = "klevusearch:bulkboost:_all:";
	public static final String REDIS_SELFLEARN_BOOSTING = "klevusearch:selflearning:_all:";
}

