# Google Search Console java APIs CLI

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
