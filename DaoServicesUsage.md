## FishEar Data Access ##

Data Acces in FishEar has 2 basic layers: **DAO layer** (the lower one) that directly access data, and **Business Service Layer** (built over the DAO layer).
Developers usually work only with **Business Service** layer (but underlying DAO functions are also available).
More details are available in [Data Module](modules_Data.md).

Usage of [Data Module](modules_Data.md) is straight - forward. Simplest way is to create [JPA annotated entity](CreateEntity.md) and corresponding service that does everything for you (basic CRUD, search, queries, paging data ... etc). In collaboration with [T5 Module](modules_T5.md) you can quickly and simply create screens with data grid, detail form and searching area.

# Create Entities #

To use data access functionality in FishEar, you need to create [Entity Class](CreateEntity.md) at correct place. The place is framework-depent, in case Tapestry 5 application, the place is "entities" package under application root package.

## Entity Classes ##

The entity class must implement [EntityI](EntityI.md) interface. The simplest way in case IDs represented by Long numbers is to extend one of the pre-implemented abstract classes [AbstractEntity](AbstractEntity.md) or [AbstractStandardEntity](AbstractStandardEntity.md). Another way is to extedn [GenericEntity](GenericEntity.md) class that provides generic ID. Of course you may implement the [EntityI](EntityI.md) interface by yourselves.

Depending of Entity superclass type, correct service implementation must be choosen:

  * [AbstractEntity](AbstractEntity.md) or [AbstractStandardEntity](AbstractStandardEntity.md) => [AbstractService](AbstractService.md)
  * [GenericEntity](GenericEntity.md) => [GenericService](GenericService.md)


Also you may implement your entity from the scratch, only by implementing all from [EntityI](EntityI.md) interface. Then you can also use [GenericService](GenericService.md) as superclass for the service.

## Register Entities to ORM ##

Entities must be also registered to ORM tool. In case Hibernate and Tapestry 5, everything is done automatically by placing entity classes to correct package (it is package "**entities**" in "Base T5 package", where "pages", "components" ... etc packages lie). Services are usually placed to the "services" package.

# Create Services #

Services are data manipulator, that manipulates with entity values. They are responsible for reading data drom underlying storage (database, web service ...) and storing it.

Beside services that communicate with underlying sorage (like database) using FishEar may exist another service - not directly use FishEar structures.

## Service Classes ##

service classes to be used in FishEar system must implement [ServiceI](ServiceI.md) interface. There is several implementation that may be used.

The simplest way how to create service that manipulate with entity data out of the box is to create empty class that extends one of abstrace service classes - depending on entity type.

[AbstractService](AbstractService.md) is designed to manipulate with "standard" entity that contains numeric IDs (Long values). [GenericService](GenericService.md) is superclass of previous one, it is suit for ID of any type.

All implement [ServiceI](ServiceI.md) interface, that is bridge to the FishEar system.

Instance of service classes may be managed manually, or may be managed by IOC tool (Spring, Tapestry, EJB3 ...). In case IOC, they must be registered to the system (as Tapestry services inAppModule class, as Spering beans in ApplicationContext.xml ... etc).

See [Example for entity and service](EntityServiceExamples.md)