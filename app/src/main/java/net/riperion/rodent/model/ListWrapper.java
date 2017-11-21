package net.riperion.rodent.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class wraps a List of objects such that the deserializer can deserialize
 * the list-in-object form the API returns
 */

public class ListWrapper<T> {
    private final List<T> results;
    @SerializedName("count") private final int queryResultCount;

    /**
     * Creates a list wrapper instance, for use by the GSON deserializer
     * @param results the results returned by the API
     * @param queryResultCount the count of overall results that can be accessed through pagination
     */
    public ListWrapper(List<T> results, int queryResultCount) {
        this.results = results;
        this.queryResultCount = queryResultCount;
    }

    /**
     * Gets the wrapped list from the wrapper
     * @return the wrapped list
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Gets the number of results of the query that returned this wrapped list
     * @return number of results of query
     */
    public int getQueryResultCount() {
        return queryResultCount;
    }

}
