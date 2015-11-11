# Queries in services and other places #

Queries are constructed the way similar to

There is QueryConstraints class that contains all information for the query and data proccessing:

  * [Restrictions](Restrictions.md) that limits returned data
  * [DataPaging](DataPaging.md) indformation that tells limits of returned records (from ... to)
  * [Projection](Projection.md) that tells which values are returned and sets additional operations (count, sum, max/min ... etc)
  * [OrderBy](OrderBy.md) section tells how result is sorted ..

All these functions are very similar to Hibernate Criteria API finctions, so everybody who is familiar with Hibernate Criteria API will be able to work with FishEar data module quickly.

Not all functions involved in QueryConstraints must be implemented in every DAO. In case some part of QueryConstaints is set but not implemented, IlelgalStateException should be thrown.


QueryFactory class exists for creating QueryConstraints various way.


QueryConstraints are passed to some [service](ServiceI.md) methods to limit and refine returned data.

See [Data Queries Example](DataQueriesExample.md) for more details.