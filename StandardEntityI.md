# StandardEntityI #


This interface informs [GenericService](GenericService.md) that the entity to be saved contains special fields with user and date who creates and changes the entity.

[GenericService](GenericService.md) fills automatically these values depending on the oparation (insert, update).

[AbstractStandardEntity](AbstractStandardEntity.md) implements this interface, so if your entity extends it, these values are store automatically.