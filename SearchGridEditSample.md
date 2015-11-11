### The "search-grid-edit" sample application ###

<div>After you have built and start the application (how it is described on <a href='SampleProjects.md'>sample projects page</a> - the application subdirectory is</div>
**`fishear-samples/search-grid-edit-sample`**), it listen on URL http://localhost:8080/search-grid-edit-sample/

#### The Client Management page (http://localhost:8080/search-grid-edit-sample/clients) ####
the page manages two entities:
[Person](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/entities/Person.java?repo=samples)
and [Client](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/entities/Client.java?repo=samples).

The page's class [Clients.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/pages/Clients.java?repo=samples)
holds only main component [ClientList.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/components/client/ClientList.java?repo=samples) with template
[ClientList.tml](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/components/client/ClientList.tml?repo=samples).
It extends abstract superclass '`AbstractGrid<EntityI>`' and implements generic entity variable `<EntityI>` to the `Client`:
```
public class ClientList extends AbstractGrid<Client>
```

This component contains remaining controlled component - the detail form [ClientDetailForm.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/components/client/ClientDetailForm.java?repo=samples).
In addidtion it has the Messages component which displays error or success messages are displayed in.

**The detail form component class** (injected to the main component) manipulates with those two entities: entity Client is the "skeleton" entity, it implements generic variable in class header:
```
public class PersonsList extends AbstractGrid<Person>
```

There are two important methods: `prepareNewEntity(EntityI)` method which is called out in case new record is required from client form, and `beforeSave(EntityI)` method, which is called before data are saved permametly.
The firs method -`prepareNewEntity(Client)` - sets the instance of Person entity into Client entity (because entitie's internal ojects are by default null), and the second method 'beforeSave' saves the 'person' entity before return. Saving of `Client` will perform parent after verifications.

Each component's class inherits from its parent: list extends '`AbstractGrid<EntityI>`', detail form component class extends '`AbstractForm<EntityI>`'.

#### The Person Management page (http://localhost:8080/search-grid-edit-sample/extpersons) ####
This page manipulates only with one entity [Person](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/entities/Person.java?repo=samples) entity,
but - in contrast to the Client page - it contains search form.

Main class [ExtPersons.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/pages/ExtPersons.java?repo=samples)
contains only main component [PersonsList.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/components/persons/PersonsList.java?repo=samples).
It extends the same superclass as '`AbstractGrid<EntityI>`', but implements generic variable as '`Person`'
```
public class PersonsList extends AbstractGrid<Person>
```
Main component contains two other components: detail form [PersonDetail.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/components/persons/PersonDetail.java?repo=samples) form and
component with search form [PersonSearch.java](http://code.google.com/p/fishear/source/browse/search-grid-edit-sample/src/main/java/net/fishear/samples/t5/srchgridform/components/persons/PersonSearch.java?repo=samples)

Each nested component (PersonSearch, PersonDetail) serves its own events. They communicate with "Parent" component '`PersonDetail`' (parent is meaning from component hierarchy point of view, not as class hierarchy).

Each component class extends its specific superclass, implements generic parameter wit the same type '`Person`':
```
public class PersonSearch extends AbstractSearch<Person>
```
for search component, and
```
public class PersonDetail extends AbstractForm<Person>
```
for detail form component.

Each component has it's own - specific - templete, where the special variable named '**`entity`**' can be used. This variable references to instance of main class entity, with which the component manipulates.