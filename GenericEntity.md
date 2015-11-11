# [GenericEntity](https://code.google.com/p/fishear/source/browse/fishear-data/src/main/java/net/fishear/data/generic/entities/GenericEntity.java) #

This is the basic implementation of [EntityI](EntityI.md) interface. It contans generic ID manipulators (methods getId(), setId(...)) that may be implemented in successor classes.

It is suit for univesal usage regardless of type id IDs. Long ID implementation is [AbstractEntity](AbstractEntity.md), which acts as usuall superclas for all "normal" entities.