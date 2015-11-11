# FishEar's Criteria API #

The CriteriaAPI is part of [fishear-data](modules_Data.md) module.
The API is similar to [Hibernate's criteria API](http://docs.jboss.org/hibernate/core/3.3/reference/en/html/querycriteria.html), but this API is independent to concrete source of data.

The criteria's main class [QueryConstraints.java](http://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/query/QueryConstraints.java) keeps all informations about the query: how to sort data, how to filter data, how to produce results, how many records should be returned, etc...

[fishear-t5](modules_T5.md) Tapestry5 module uses that QueryConstraints object internally to create sort and filter constraints from pages.

### Using QueryConstraints ###

To create instance of QueryConstraints class, the factory class [QueryFactory.java](http://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/query/QueryFactory.java) does exist.

TODO