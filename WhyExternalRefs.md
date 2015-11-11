## Why are not external frameworks linked firmly? ##
because we want to let you to use your favorite version of those frameworks, and as protection from version conflicts.

<div>You can add <font size='3'><b><a href='ExternalLibraries.md'>references to external libraries</a></b></font> to your POM to use preferred (and recommanded) version </div>of those libraries [as described here](ExternalLibraries.md).

<font color='#404040'>Of course, you can exclude artefact dependencies from your POM, but I think the better (and more transparent) way is to use your preferred versions directly. In case version conflicts occurs, you have to investigate why the software fails and this costs many time. Sometimes it happens at runtime long time after program starts, ant it can cause serious problems.</font>