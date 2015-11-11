Generic parent classes are suit for forms build oves single entity type. Each Generic superclass implements the entity cless.

There are 3 main parents:

  * detail
  * grid
  * grid with detail
  * Search

Two main superclasses types:

  * **Generic\*XxxxComponent
  ***Abstract\*XxxxComponent

Xxxx is aither "Detail", "Grid", "Search", "GridDetail" ...

**Abstract** superclasses
are designed to work with [AbstractEntity](AbstractEntity.md) (it has id of Long type).

**Generic** superclasses are more generic, they may operate over each implementation of [EntityI](EntityI.md) interface

Each superclass contains also set of event handler methods (depending on component type) that handle usual events for the component type (new form creation, record saved or deleted ...).

Each superclass also contains couple of methods, that have dummy implementation by default, but may be implemented in descendants and tweak / tune component behaviour.

Of cource, fully implemented methods may be alsoo reimplemented in descendants (either fully, or using originals in superclasses). It may be usefull mainly in case event handler methods.

## GridComponent ##
(AbstractGridComponent, GenericGridComponent)

  * abstract class, implementation is created for certain entity class
  * descandant must implement [getService](getService.md) method
  * variables that may be used in template
    * "row" (with getter/setter) - suit for grid row
    * "dataSource"
  * actions available in template that are proccesed automatically
    * delete: method onDelete() may be called ffrom eventLinky
  * pre-defined (dummy) methods
    * modifyConstraints: called before database query, conditions are pased. Descendan tmay modify these conditions.
    * modifyConditions

## DetailComponent ##
(AbstractDetailComponent, GenericDetailComponent)

  * abstract class, implementation is created for certain entity class
  * contains persisten entity of type that tha descendant implements
  * descandant must implement [beforeSave](beforeSave.md) method
  * variables that may be used in template
    * entity (via getter/setter): the main entity which is manipulated by the class. Its instance is handled automatically depending on events.It is session-persistent.
  * event haldlers (handled automatically, but may be overriden)
    * newEntityInstance(Entity): called immediately after new entity is created. New instance is created when getEntity() method is called, but internal instance is null
    * onNew
    * onDetail(id): id is proper type
    * onSuccess: called when form validation passed through

## GridDetailComponent ##
(AbstractGridDetailComponent, GenericGridDetailComponent)

  * combines both of above into single component superclass
  * in template you can use all of prevous variables
  * 
## Search ##
(AbstractSearch)

Superclass for component which acts as search form in collaboration
with grid. By placing of implementation of AbstractSearch intu "xxxGridComponent" or "xxxGridDetailComponent", they interconnects and provide automatic grid filtering.

Search conditions are constructed automatically, using one entity contained in or two entities instances. More on [Using Search Component](UsingSearchComponent.md) page.

To use searc, only extend the AbstractSearch superclass. No method needs to be implemented.

  * variables available and usable in template
    * **entity**: the main entity
    * **entity2**: the second entity to create intervals
  * Methods available
    * **newEntityInstance**(Entity): called immediately after new entity (both "entity" or "entity2") is created
    * **modifyConditions**(Conditions): may correct conditions
    * **beforeSearch**(entity): may be used to modify entity values before conditions are created