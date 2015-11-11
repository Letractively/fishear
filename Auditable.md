# [Auditable](https://code.google.com/p/fishear/source/browse/fishear-data-audit/src/main/java/net/fishear/data/audit/annotations/Auditable.java) #

The annotation that - if used on class that is also annotated by "@Entity" annotation - causes all changes are audited if entity is manipulated by methods derived from [GenericService](GenericService.md) or its descendants.