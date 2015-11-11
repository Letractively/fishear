# Introduction #

`fishear-t5` module contains set of Tapestry 5 classes that may speed up development of usuall and commonly used componentd.

It is possible to create Component with search area, grid and detail form.

Also there exist components for Combobox created from service's data

All Tapestry components have prefix "**`fe`**" , so for example the "select" component from FishEar is used as **`<t:fe.select ...>`** in the template.

## Independent Components ##

[Select](Select.md)

[Combobox](Combobox.md)

[CheckboxOut](CheckboxOut.md)

[Label](Label.md)

## [Form Component superclasses](FormComponentSuperclasses.md) ##

set of superclasses speed up simúle forms creation. Make you component class successor of some of these parents, you can use predefined actons:

  * to have database based pading grid with search component, where search conditions are created automatically by the entity values
  * to have detail form where most actions is pre-devined, you need only set form layout

There is set od parents that may be used

  * the data grids with paging data source
  * for the detail forms (nandles entity manipulation)
  * for both (grid with detail form)
  * for the search form, that is directly linked into grid to filter grid content

More on [Form Component superclasses](FormComponentSuperclasses.md)
## [Another Component Superclasses](AnotherComponentSuperclasses.md) ##

[ComponentBase](ComponentBase.md)

[PageBase](PageBase.md)


## Mixins ##

[Confirm](Confirm.md)

[ConfirmDelete](ConfirmDelete.md)