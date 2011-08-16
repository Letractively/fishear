package net.fishear.data.generic.dao;

import java.util.List;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;



public interface 
	GenericDaoI<K extends EntityI<?>> 
{

   K read(Object id);

   Object save(K entity);

   void delete(K entity);

   boolean isLazyLoaded(Object entity, String propertyName);

   K newEntityInstance();

   Class<K> type();

   List<?> query(QueryConstraints queryConstraints);

   /**
    * synchronize internal state with cache, but bo not commit transaction.
    */
   void flush();

   /**
    * If transaction has been started, commits it.
    * Otherwise do nothing.
    */
   void commit();

   /**
    * If transaction has been started, rollbacks it.
    * Otherwise do nothing.
    */
   void rollback();

   /**
    * If dao supports transactions and transaction not started yet, begins int. 
    * Otherwise do nothing.
    */
   void transaction();
}
