package net.fishear.data.generic.entities;

/**
 * interface to store and get initial state of entity.
 * Initial state is saved after entity is loaded by fishear's service, queries by the service ebfore it is saved.
 * 
 * @author ffyxrr
 *
 */
public interface InitialStateI {

	EntityI<?> getInitialState();

	void saveInitialState();

}
