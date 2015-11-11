# Introduction #
What the Data module is is described on [DataModuleDescription](DataModuleDescription.md) page.

each entity have to implement interface EntityI, which provides getter/setter for generic ID.

For using datamodule, you have to

  * create your entities (business objects) as extensions of GenericEntity
    * the usual entity object has ID of type Long, and it can simply extend AbstracEntity class (which implements generic ID to Long).
  * creates your services, each for given entity type.
    * services out of this class hierarchy may exist too of course, but those could not be used via fishear's