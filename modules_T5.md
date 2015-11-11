## Tapestry5 Module ##

The module is set of tools to simplify and speed-up of creating web applications using [Tapestry5](http://tapestry.apache.org/) framework.

Set of component superclasses to fulfill typical actions:
  * master/detail editation
  * grid with search form

### Search / Grid / Detail ###
Most common task is list of records with search form and another form for detail editing. Tapestry5 FishEar's library provides set of generic classes for this task:

  * AbstractGridComponent
  * AbstractFormComponent
  * AbstractSearchComponent

All those components are generic, to be used together, they must implement the same generic entity type.
The "roof" grid component PersonsList creates (in the template [PersonsList.tml](https://code.google.com/p/fishear/source/browse/evocus/src/main/java/net/fishear/sampleapps/evocus/components/persons/PersonsList.tml?repo=samples))
grid with result (it uses GTable to achieve it).
The class [PersonsList.java](https://code.google.com/p/fishear/source/browse/evocus/src/main/java/net/fishear/sampleapps/evocus/components/persons/PersonsList.java?repo=samples)
only extends AbstractGridComponent<?> with proper generic type (here Person) and provides its service (method `public AbstractService<Person> getService()`).

And, of course, it holds the search component ([DataSearch.java](https://code.google.com/p/fishear/source/browse/evocus/src/main/java/net/fishear/sampleapps/evocus/components/persons/DataSearch.java?repo=samples)) and the detail fofrm component (	[DetailForm.java](https://code.google.com/p/fishear/source/browse/evocus/src/main/java/net/fishear/sampleapps/evocus/components/persons/DetailForm.java?repo=samples)).

With the 	remaining two parts of the page - the search form and the datail form, the situation is similar. The search form's class extends [AbstractSearchComponent](AbstractSearchComponent.md) with generic type Person and returns the same service as grid's component,
similarly the detail form extends [AbstractFormComponent](AbstractFormComponent.md) with the same generic type (and returns service too).

Both those components have to have templates with forms. The form names are not importnant, but they must lay inside the `<t:form>...</t:form>` section.

The submit actions are handled by superclasses and processed in cooperation with the root grid.

That's it.

**AbstractGridComponent** is the "roof" component (the container for the remaining two components). The superclass provides GridDataSource, there

If you wont use search form, you have to create you own search component which extends AbstractSearchComponent and put it into the grid's component. That's all. System will find it and use it to filter grid.

**AbstractSearchComponent**
All three clesses are generic and have to implement the same entity (the same implementation of EntityI)

Tapestry 5 is very good web framework. It allows create large web application quickly, it leads developers to complay "best practices" ...

This project intents to provide set of tools (usually as superclasses) that simplify common task even more.

