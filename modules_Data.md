# Generic Data Module #

Module provides common data access functionality

So from the enduser (developer) perspective, there are 2 layers:

  * DAO layer
  * Business Service Layer

Both layers work with abstract business objects ( buiness **entities** ), that are expected do be implemented by needed. The module bringhts some abstract implementation for particular use cases.

## DAO Layer ##

DAO is designed to be hidden - it provides basic underlying CRUD operations. Also some low-level functions are available but should be used with care. DAO level is designed to be simply changed with another one. See [Generic DAO Interface](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/dao/GenericDaoI.java) for more details about methods available. Generic DAO is designed to be simple enough to be implemented for different databases or another data sources (web services ...). DAO stays the same for all entities of given type (the same for Hibernate, the same for EclipseLink - but different from Hibernate) ... etc.

DAO implementation currently exists for Hibernate (which also publishes some loew level hibernate - specific tasts; see [modules\_Hibernate](modules_Hibernate.md) for more details).

Each service has its own DAO instance, that provides low-level operations for that servce. [DaoSource](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/dao/DaoSourceI.java) generates the DAO for each service when service instance is created. Normally, it is done automatically by default and it is hidden for you. But developer may set its own DAO implementation, or create service with different DAO.

Usually only one database is connected to the application, but also more databases may be connected to single aplication.
Multiple DAO instances may exist in single system, [DaoSourceManager](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/dao/DaoSourceManager.java) manages these instances.

DaoSourceManager accepts [PersistenceContext](http://download.oracle.com/javaee/6/api/javax/persistence/PersistenceContext.html), in **unitName** it expects name of data s ource. First Data Source that is foung in system configuration is automatically set as default DAO Source. You may register additional DaoSources or change default one.

## Service layer ##

Service layer is upper layer, built on the DAO layer. It is designed to contain business logic and cen be modified as beeded (in contrast to DAO, whic is always the same). Deevelopers always work with service layer (not with the DAO).

In addition to CRUD operations (ported from DAO), there is couple of additional operation available in generic Service layer (see [Generic Service Interface](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/services/ServiceI.java)) which every service must implement. Developes mey add methods they need to implement required business logic.

# Data Module consists of #

  * [abstract business object superclasses](CreateEntity.md) (= entities) provide unified structure for business objects (aka entities)<br><font size='1'>This is useful in many tools, where those entities could be processed simply and automatically.</font></li></ul>

<ul><li><a href='GenericDao.md'>generic DAO</a> part (abstract layer for basic CRUD operation). It collects basic, most common data operation at one place. <br><font size='1'>Contains only necessary basic operations.  With those operation we should be able to do all with data we need (no further operations should be needed). It is relatively simple to implement it. If you don't have special requirements, the DAO is totally transparent - it is created and used automatically.</font></li></ul>

  * [generic services](GenericServices.md) provide unified superclasses for business objects with lot of predefined operation. <br><font size='1'>This also includes CRUD from DAO, verification and lot of next tasks</font></li></ul>

<ul><li><a href='CriteriaApi.md'>generic query criteria</a> General, database or ORM tool independent <a href='http://docs.jboss.org/hibernate/core/3.3/reference/en/html/querycriteria.html'>Hibernate-like criteria</a>. <br><font size='1'>Because the criteria are independent, they may be implemented for any persistance tool (ORM, cloud, key-value database...). Now thea are implemented for Hibernate, partially implemented for in-memory storage (currently only very few features, only for testing).</font></li></ul>

Generic modules contain set of interfaces and abstract classes.

Abstract Classes with "Generic" prefix (GenericEntity, GenericService ...) are really generic - they have ID of generic type.

Abstract classes wih prefix "Abstract" (AbstractEntity, AbstractService ...) are specific implementations for most commonly user type with ID of type Long.

### Data Module Schema <font size='2'>(<a href='modules_Data_Schema.md'>click here for full size schema</a>)</font> ###
<img src='https://sites.google.com/site/raterwork/fishear_schema.png' width='680' height='400' />