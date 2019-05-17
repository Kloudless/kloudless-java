package com.kloudless.models;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import com.google.gson.JsonObject;
import com.kloudless.exceptions.ApiException;

/**
 * ResourceList Class is extends from ResponseBase, it contains List of Resource Object which is the
 * result of any API operation from Kloudless API server
 */
public class ResourceList extends ResponseBase {
    private List<Resource> resources;
    private String page;
    private String cursor;
    private String nextPageCursor;
    private JsonObject data;

    /**
     * Constructor of Resource
     * 
     * @param url            The url of this resource, could apply when user invoke refresh method.
     * @param defaultHeaders Store the original headers for further http operations.
     * @param resources      List of Resource is from the Kloudless API Server.
     * @param data           JsonObject from Kloudless API Server.
     */
    public ResourceList(String url, Map<String, Object> defaultHeaders, List<Resource> resources,
            JsonObject data) {
        this.url = this.storeAndRemoveQueryString(url);
        this.defaultHeaders = defaultHeaders;
        this.resources = resources;
        this.page = data.get("page") == null || data.get("page").isJsonNull() ? null
                : data.get("page").getAsString();
        this.cursor = data.get("cursor") == null || data.get("cursor").isJsonNull() ? null
                : data.get("cursor").getAsString();

        if (data.get("next_page") != null && !data.get("next_page").isJsonNull()) {
            this.nextPageCursor = data.get("next_page").getAsString();
        } else if (data.get("next_page") != null && data.get("next_page").isJsonNull()) {
            this.nextPageCursor = "";
        } else {
            // next_page does not existed
            if (this.cursor != null) {
                if (this.resources.size() == 0) {
                    this.nextPageCursor = "";
                } else {
                    this.nextPageCursor = this.cursor;
                }
            } else {
                this.nextPageCursor = null;
            }
        }
        this.data = data;
    }

    /**
     * Transfer the Sting to Integer
     * 
     * @param pageString String format of page number
     * @return int
     */
    private int toInt(String pageString) {
        int pageInt;
        try {
            pageInt = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            pageInt = -1;
        }
        return pageInt;
    }

    /**
     * This method is to actually get next page data.
     * 
     * @param cursor    check cursor is existed
     * @param nexCurosr nextCursor in previous query
     * @return ResourceList
     * @throws ApiException Error when get nextCursor failed.
     */
    private ResourceList tryNextPage(String cursor, String nextCursor) throws ApiException {
        String url = cursor != null ? "?cursor=" + nextCursor : "?page=" + nextCursor;
        return (ResourceList) super.get(url);
    }

    /**
     * Get resource data
     * 
     * @return List of Resource
     */
    public List<Resource> getResources() {
        return this.resources;
    }

    /**
     * Get page number
     * 
     * @return int of page number
     */
    public String getPage() {
        return this.page;
    }

    /**
     * cursor string if existed
     * 
     * @return String cursor string
     */
    public String getCursor() {
        return this.cursor;
    }

    /**
     * Get the next page cursor
     * 
     * Pagination information from API server has some cases.
     * 
     * 1. next_page is in JsonObject and it contains value, we could make sure there is next page.
     * 
     * 2. next_page is in JsonObject but its value is null, we could know there is no next page.
     * 
     * 3. next_page is not in JsonObject and cursor is, use cursor as value of next_page.
     * 
     * 4. next_page is not in JsonObject and page is string, hard to know next_page is actually
     * presented.
     * 
     * 5. next_page is not in JsonObject and page is Integer, we could probably try to get next page
     * 
     * @return String of next page cursor or null
     */
    public String getNextPageCursor() {

        if (this.nextPageCursor != null && !this.nextPageCursor.isEmpty()) {
            return this.nextPageCursor;
        } else if (this.nextPageCursor != null && this.nextPageCursor.isEmpty()) {
            return null;
        } else {
            if (this.page != null && toInt(this.page) > 0) {
                try {
                    ResourceList nextPage =
                            this.tryNextPage(this.cursor, String.valueOf(toInt(this.page) + 1));
                    if (nextPage.getResources().size() > 0) {
                        return String.valueOf(toInt(this.page) + 1);
                    } else {
                        return null;
                    }
                } catch (ApiException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * the raw data originally from API server
     * 
     * @return JsonObject raw data
     */
    public JsonObject getData() {
        return this.data;
    }


    /**
     * Status of if next page is existed
     * 
     * @return boolean type
     * @throws ApiException error when check next page is existed failed.
     */
    public boolean hasNextPage() throws ApiException {
        return this.getNextPageCursor() != null;
    }

    /**
     * Get a new ResourceList of next page.
     * 
     * @return ResourceList object contains data of next page.
     * @throws ApiException Error when any result other than 2xx is returned.
     */
    public ResourceList getNextPage() throws ApiException {
        try {
            if (!hasNextPage()) {
                throw new ApiException("There is no next page.", null);
            }
            return tryNextPage(this.cursor, this.getNextPageCursor());
        } catch (ApiException e) {
            throw new ApiException("Failed to retrieve next page.", e);
        }
    }

    /**
     * To refrsh this resource will trigger a new http get and update the data attribute in
     * resource.
     * 
     * @throws ApiException Error when any error from Kloudless API server.
     */
    public void refresh() throws ApiException {
        ResourceList newResourceList = (ResourceList) super.get("");
        this.data = newResourceList.getData();
        this.page = newResourceList.getPage();
        this.cursor = newResourceList.getCursor();
        this.nextPageCursor = newResourceList.getNextPageCursor();
    }

    /**
     * Create an Iterator of Resource
     * 
     * @return An Iterator of Resource objects
     */
    public Iterator<Resource> getPageIterator() {
        return getPageIterator(Long.MAX_VALUE);
    }

    /**
     * Create an Iterator of Resource with maximum limitaion.
     * 
     * @param maxGet specify the maximum number for this iterator
     * @return An Iterator of Resource objects
     */
    public Iterator<Resource> getPageIterator(Long maxGet) {
        return new Iterator<Resource>() {
            private int index = 0;
            private List<Resource> localResources = resources;
            private String curPage = page;
            private String localCursor = cursor;
            private String localNextPageCursor = getNextPageCursor();
            private String localUrl = url;
            private long maxResource = maxGet;
            private long curTotal = 0;

            @Override
            public boolean hasNext() {
                return (index < localResources.size() || !(localNextPageCursor == null))
                        && (curTotal < maxResource);
            }

            @Override
            public Resource next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                curTotal++;
                if (index < localResources.size()) {
                    Resource resource = localResources.get(index);
                    index++;
                    return resource;
                } else {
                    try {
                        ResourceList tempResourceList = getNextPageData();
                        curPage = tempResourceList.getPage();
                        localNextPageCursor = tempResourceList.getNextPageCursor();
                        localResources = tempResourceList.getResources();
                        localUrl = tempResourceList.getUrl();
                        localCursor = tempResourceList.getCursor();
                        index = 0;
                        Resource resource = localResources.get(index);
                        index++;
                        return resource;
                    } catch (ApiException | URISyntaxException | IndexOutOfBoundsException e) {
                        throw new NoSuchElementException(String.format(
                                "Get next failed, curTotal %s, page %s, index %s, url %s, reason %s",
                                curTotal, curPage, index, localUrl, e.getMessage()));
                    }
                }
            }

            private ResourceList getNextPageData() throws URISyntaxException, ApiException {
                if (!hasNext()) {
                    throw new ApiException("There is no next page.", null);
                }
                return tryNextPage(localCursor, localNextPageCursor);
            }

        };
    }
}
