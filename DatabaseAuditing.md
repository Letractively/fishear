FishEar framework contains [Database Audit](modules_Audit.md) modules, that create simple and powerful database operations auditing subsystem.

Auditing is used with Entity/Service implementation, using implementations of [GenericService](GenericService.md).

If your service extends the GenericService any way, autiting subsystem is available and works seamlesly.

  * very simple to use
  * designed to be independent to concrete ORM (it depends only on internal FishEar libraries)

For full audit, all operation must go through FishEar [GenericService](GenericService.md) ir its descendants, so lists that are updated directly by ORM (aka lists in entities) are not audited.

# How to use #

  * import fishear-data-audit module (by Maven) to your project

  * Allow ORM tool to create database objects During first start application must be able to create database tables by ORM tool since audit tables must to be created. Optionally you may [create audit tables](CreateAuditTables.md) manually (TODO: will be described later)


  * annotate entity with "Auditable" annotation. The annotation instructs framework to audit all data opearationa.