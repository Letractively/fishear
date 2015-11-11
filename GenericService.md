# [GenericService](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/services/GenericService.java) #

The Service classes provide basic CRUD operations over entities. Each entity that is stored in database using FishEar must have its service.

Single service may manipulate with more entities using services for these entities, and alsi queries may be constructed to return diferrent entity type than "main" entity, but still, each service provides CRUD for only one entity type.

Non-FishEar entities may exist that manilulate with data over more entities, the may utilize anothe services.
This approach is usually used for complex business logic.

Service implementation is good place to place busines login relating to the entity values.

Each service contains its [DAO](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/dao/GenericDaoI.java), that directly access data in underlying storage (database, web service .. etc). The DAO is created automatically and it is available by "getDao()" method call. It contains some "low level" methods that should be used with care (or should not be used at all if it is possible).


See [Example for entity and service](EntityServiceExamples.md)