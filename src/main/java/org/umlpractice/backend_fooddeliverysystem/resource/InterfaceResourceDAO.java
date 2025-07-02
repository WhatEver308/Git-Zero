package org.umlpractice.backend_fooddeliverysystem.resource;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfaceResourceDAO extends CrudRepository<Image,Integer> {
}
