# Google Search Console java APIs CLI

------------------------------------------------------------
## APIs
[Google APIs Explorer](https://developers.google.com/apis-explorer/#p/webmasters/v3/)

|name|description|
|:---|:----------|
|webmasters.searchanalytics.query|Query your data with filters and parameters that you define. Returns zero or more rows grouped by the row keys that you define. You must define a date range of one or more days. When date is one of the group by values, any days without data are omitted from the result list. If you need to know which days have data, issue a broad date range query grouped by date for any metric, and see which day rows are returned.|
|webmasters.sitemaps.delete|Deletes a sitemap from this site.|
|webmasters.sitemaps.get|Retrieves information about a specific sitemap.|
|webmasters.sitemaps.list|Lists the sitemaps-entries submitted for this site, or included in the sitemap index file (if sitemapIndex is specified in the request).|
|webmasters.sitemaps.submit|Submits a sitemap for a site.|
|webmasters.sites.add|Adds a site to the set of the user's sites in Search Console.|
|webmasters.sites.delete|Removes a site from the set of the user's Search Console sites.|
|webmasters.sites.get|Retrieves information about specific site.|
|webmasters.sites.list|Lists the user's Search Console sites.|
|webmasters.urlcrawlerrorscounts.query|Retrieves a time series of the number of URL crawl errors per error category and platform.
webmasters.urlcrawlerrorssamples.get|Retrieves details about crawl errors for a site's sample URL.|
|webmasters.urlcrawlerrorssamples.list|Lists a site's sample URLs for the specified crawl error category and platform.|
|webmasters.urlcrawlerrorssamples.markAsFixed|Marks the provided site's sample URL as fixed, and removes it from the samples list.|


------------------------------------------------------------
## Usage
* **sitemaps.list** 

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation={your_key_file} sitemaps.list -siteUrl {your_site_url} 
```

```console
{
  "sitemap" : [ {
    "contents" : [ {
      "indexed" : "6",
      "submitted" : "20",
      "type" : "web"
    } ],
    "errors" : "0",
    "isPending" : false,
    "isSitemapsIndex" : false,
    "lastDownloaded" : "2017-10-19T19:17:56.817Z",
    "lastSubmitted" : "2017-10-04T22:47:39.579Z",
    "path" : "your_site_url",
    "type" : "sitemap",
    "warnings" : "50"
  } ]
}
```

----------------------------------------------------------
## Reference

* [Reflections、Scannotation（とSpring）で、クラスパス上から特定のアノテーションが付与されたクラスを探し出す - CLOVER](http://d.hatena.ne.jp/Kazuhira/20150408/1428503151)

* [Java Code Example org.springframework.core.type.filter.AssignableTypeFilter](https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.core.type.filter.AssignableTypeFilter)   

