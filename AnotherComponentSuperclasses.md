## ComponentBase ##

  * Makes descendants more clear and better arranged by injecting commonly used services as variables that are available in descendants, so descandants may contain less code.
    * **crsc**: ComponentResources
    * **prsc**: PageLingRenderSource
    * **alerts**: AlertManager (to work, `<t:alerts />` component must be placed into the template)
  * provides set of methods that simplify daily work
    * **translate**(String key, Object... values): translates text the way ${message:key} does, replaces %s with values


## ExceptionHandlingComponentBase ##

in additin to ComponentBase, this superclass handles exceptions and shows alert that correspond to the exception.

Using this superclass, you need not take care about exceptions. Thea are transformed to alert error message (while still logged on log).