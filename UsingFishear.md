## FishEar Usage ##

<div>In a nutshell, you need to choose which <a href='Modules.md'>modules</a> want to use and add <b><a href='MavenArtefacts.md'>maven artefacts</a></b> to your project <font size='1'>(or <a href='DownloadJars.md'>download libraries</a> and add them to project's classpath)</font>. More you can tell on <a href='UsingFishear.md'>usage page</a>.</div>
The bunch of **[the sample applications](SampleProjects.md)** exists too, you can build them and run them to test FishEar's functionality.
Thise applications demonstrate most of "top level" functionality of FishEar's libraries.

[Using DAO and Services](DaoServicesUsage.md)

you can perform data access in you project very simply and quickly using predefined DAO and services

[Construct Queries Using Criteria Facade](ConstructQueries.md)

FishEar contains criteria facade (sometinng like slf4j facade for logging). It is similar to Hibernate criteria, but only facade so may be implemented for aby ORM tool.

[Database Auditing](DatabaseAuditing.md)

powerful, bud simple for usage database auditing audits all changes on entities that gou through the services (created, changed, deleted ...),

[Tapestry 5 Components and anndons](Tapestry5Components.md)

there is set of components, superclasses and bindings that helps speed up and simplify the code

[Tapestry 5 Data Components](Tapestry5DataComponents.md)

SET of components TO simplify routine tasks:

  * grid - form with serarch part
  * detail component woth predefined tasks
  * grid - detail dorm as combination of previous two (grid & detail form)
  * search form that interoperates with grids and creates queries automatically based on entity values

[Tools and Helpers classes](ToolsAndHelpers.md)

There are several classes for entity and texts manipulation, work with lists (filtering, grouping, sublists ...).