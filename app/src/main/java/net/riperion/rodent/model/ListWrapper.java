package net.riperion.rodent.model;

import java.util.List;

/**
 * This class wraps a List of objects such that the deserializer can deserialize
 * the list-in-object form the API returns
 */

public class ListWrapper<T> {
    private List<T> results;

    /**
     * Creates a list wrapper instance, for use by the GSON deserializer
     * @param results
     */
    public ListWrapper(List<T> results) {
        this.results = results;
    }

    /**
     * Gets the wrapped list from the wrapper
     * @return the wrapped list
     */
    public List<T> getResults() {
        return results;
    }
}
