# What FishEar brings, why to use it #

FishEar tries to provide set of functions that simplify daily work in (mainly - but not only) web application development. It is focused to web applications development, mainly to [Tapestry 5](http://tapestry.apache.org/) framework in collaboration with JPA tool (currently Hibernate).

The main goal is rapid and effective development of routine taska. So FishEar uses classes hierarchy instead of annotations `[1]`. Most of FishEar T5 components are base classes (often abstract), that are used as superclasses of application components.

It brings set Java Libraries that automatize some tasks, namely

  * Database CRUD and entities simplification
  * client forms with search part
  * database changes autitting
  * set of tools and utils

**All FishEar's parts fit together and all together may significantly speed up daily work.** But you may use each of them separately.

FishEar consists of these modules
  * [data module](modules_Data.md) - generic, commonly usable (and used) classes to simplify data operations make project structure more transparent.
    * [genetic entitity superclass set](AbstractEntity.md) that uniform common parts of entities and provide an option to work with entities at once
    * [generic DAO](GenericDao.md) independent of specific ORM tool, with [criteria API](CriteriaApi.md) similar to Hibernate's (but ORM tool independent too)
    * [business services](GenericService.md) superclasses and prototypes that creates simple and uniform way how to create business serviced that manipulate with [entitie's](AbstractEntity.md) data
    * [Hibernate DAO](HibernateDao.md) - the DAO implementation for Hibernate (including the criteria implementation), also [module that links the DAO](modules_T5Hibername.md) to the Tapestry 5 famework
  * [TREE Structures](modules_TreeData.md) data manipulation library
  * [Set of Tapestry 5 components](modules_T5.md) and superclassesfor typical use cases and repeated operatios. Mainly contains:
    * forms support: typical forms (search area, table with result, detail edit areea)
    * adds useful debug informations to T5 pages
    * commonly (and widely)used tasks, as messages, error handling etc...
  * [Sipmple Tapestry 5 access rights](modules_T5Rights.md) module - login form and process, role based (simple but useful) access control to pages or components, releated entities and services.
  * [Database Auditing Module](modules_Audit.md) is part of framework that logs all operations that go through [generic FishEar's service](AbstracService.md) (these, that extend any of FishEar's abstract services). Also there exists simple set of screens to search in audit hostory.

[Here is more detailed modules description](Modules.md)



&lt;hr /&gt;


`[1]` this approach - using superclasses instead of annotations - less flexible, but on the other hand it is more intuitive and simplier to implementation. Lots of things are checked at compile time and also what is needed to implement is obvious from the class hierarchy. This approach was proven as very effective during development of tens of applications.