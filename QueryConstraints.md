# [QueryConstraints](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/query/QueryFactory.java) #

Class that's instance is passed to certain [service](ServiceI.md) methods

  * query(QueryConstraints constraints)
  * list(QueryConstraints constraints)
  * read(QueryConstraints constraints)


It limits and refines returned data and tells [GenericService](GenericService.md) what kind of post-proccessing will be performed on the read data.

Instances QueryConstraints are created by QueryFactory.