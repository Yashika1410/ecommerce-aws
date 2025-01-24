package com.example.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.common.enums.DocType;
import com.example.ecommerce.entities.DocumentMapping;
@Repository
public interface DocumentMappingRepository extends CrudRepository<DocumentMapping, Long> {
   @Query("SELECT MAX(d.position) FROM DocumentMapping d WHERE d.uuid = :uuid")
    Integer findMaxPositionByUuid(@Param("uuid") String uuid);

    boolean existsByUuid(String uuid);

    List<DocumentMapping> findAllByUuid(String uuid);

    List<DocumentMapping> findByUuidAndTypeOrderByPosition(String uuid, DocType type);

}
