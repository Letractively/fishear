# Generic DAO #

The DAO (Data Access Object) is provided to decouple low level data access (save, read/list/search, and delete operations) and "higher level" logic.

As mentioned, tha DAO provides basic CRUD operation using few methods only (to make it as simple as possible), but those methods should cover moust of usual use cases. In case special needs, you can create and implement your own DAO.

The DAO implementation it is very simple for now, only creates instance of common superclass for given entity type. But the DAO machanism (used insice services) provides possibility to change underline data source as needed without change higher (= service level) logic.

[the DAO interface](http://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/dao/GenericDaoI.java)