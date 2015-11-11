# Data Audit Modules #

FishEar contains robust autitting subsystem that audits database operations that go through its services.

Auditting has two parts

  * database functions
  * user screens tha allow to view auditting data


# Database Audit #
`fishear-data-audit` module contains logic that evaluates changes in entities and logs them to extra audit tables.

## Audit Usage ##

Annotation [Auditable](Auditable.md) on the entity level tells that this entity falls into auditing.

Also, auditting module must be added (@Auditable annotation is not available in other case)


# Audit UI #
As mentioned, user screens to view audit data is in "`fishear-t5-audit`" module. By adding this module to your project, "baseUrl/fe/audit" screens become available.
You can see changes in adited entities (those annotated by [Auditable](Auditable.md) annotation).