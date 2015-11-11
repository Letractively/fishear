## Data Objects Concept ##
This chapter describes concept of generic data objects (entities, DAO and services).

### Generic entities ###
The generic entities came from assumption that
  * **each object designed to be persistent has to have unique identifier** (= ID).
  * the ID has to be **object** (not a primitive)
  * The identifier has to be **serializable**
  * if entity has **null ID**, it is **new entity** (not persisted yet)
To satisfy those requirements, there exist **`EntityI`** interface. Every real entity has to implement this interface. Every fishear's system classes (services, DAO, components...) expect that entities implement this interface.

The best type of ID is integer numeric type (Long number), acceptable choice is character type (String), but in general, the ID may be any  object type (only there is lot more work with other types).

**EntityI** interface provides methods to manipulation with ID (getter, setter and methods to covert from / to String - because String is the most often used data type).
**GenericEntity** implements those methods (to convert from/to String, it uses concept of [Coercers](Coercers.md) similat to Tapestry5 Coercers).

In web forms, where we need pass the ID to client and get it back from the Client (especially in Tapestry), we should use those "String" methods to avoid problems with empty values.
```
EvtityI.getIdString()

EntityI,setIdString(String)
```
The method setIdString() treats empty string value as "no ID" (and set null as real ID in this case).

### Generic DAO ###
The DAO (<b>D</b>ata <b>A</b>ccess <b>O</b>bjects) is lowest data layer in data process (see [data schema](modules_Data_Schema.md)). The DAO provides basic data operations (create/update/delete records, simple query and reading objects). For data searching, filtering and result definitions the DAO uses generic independent [CriteriaAPI](CriteriaAPI.md).

In usual cases, the DAO is hidden for you, it is generated and used by system automatically. But its existence provides you possibility to change not only data source (this also allow various ORM tools like !Hibernate, EclipseLink etc...) but even data source type (for example from relational database to cloud, to Object Database ...). The "<i>only</i>" thing you need is proper DAO.

### Generic services ###
The services is higher layer built over the DAO. It provides basic (and generic) business data operations, including low level operations provided by DAO itself. Generic Services are designed to be extended by real services with aditional functionality.

Each service utilizes its ovn DAO instance (it is created automatically, but can be passed to the service). Such DAO is used internally for underlying data operations, but it is accessible from outside the service too (using mehod `getDao()` ).

It is possible to create service instance dynamically without need to create real class.
If you don't need any extra functionality in service (if you only neesd methods provided by GenericService).
The class `ServiceFactory` in method `createService` does it as shows next code snippet:
```

public class TestEntity<Long> {

   // fields, getters and setters

}

public class SampleClass {
    import net.fishear.data.generic.services.ServiceFactory;

    // from now, I can use myService for data operations
    ServiceI<TestEntity> myService = ServiceFactory.createService(TestEntity.class);
}
```