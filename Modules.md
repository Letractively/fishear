# FishEar's Modules #
FishEar is constructed as of set of modules. You may import only modules you want to use (more on [IncludingFishear](IncludingFishear.md) page)

Module dependencies are constructed to be dependent from more generic to more specialized. Underlying layers (especially [Data Module](modules_Data.md)) are usually independent on the rest. All modules depent on [Core Module](modules_Core.md), which contains general functions, tools etc...

Better view provides Module dependency graph:
<img src='https://sites.google.com/site/raterwork/fishear_module_dependencies.png?attredirects=0&d=1' width='600' height='400' />



### Module List ###
Every module described here is single Maven artefact. All  [artefact](MavenArtefacts.md) belongs to `net.fishear` group. Artefacts are described in more details on [MavenArtefacts page](MavenArtefacts.md).

  * `fishear-data`: [the generic data module](modules_Data.md)
  * `fishear-data-audit`: [Data part of Audit Module](modules_Audit.md)
  * `fishear-hibernate`: [hibernate DAO](modules_Hibernate.md)
  * `fishear-t5`: [Tapestry5 tools](modules_T5.md)
  * `fishear-t5-hibernate`: [Hibernate DAO for Tapestry5](modules_T5Hibername.md)
  * `fishear-t5-audit`: [User Interface Part of Audit Module](modules_Audit.md)
  * `fishear-t5-rights`: [access control for Tapestry5](modules_T5Rights.md)
  * `fishear-treedata`: [tree structures data manipulation functions](modules_TreeData.md)
  * `fishear-testsupport`: [support for unit tests](modules_TestSupport.md)
  * `fishear-core`: [the core module](modules_Core.md)

Note that all project modules depend on **[fishear-core](modules_Core.md)** module, adding any of project modules causes to add this (core) module to your project.
Dependencied among libraries are on this image.