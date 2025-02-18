package com.OrgComm.OrgComm.repository;

import com.OrgComm.OrgComm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {


    // Add a method to find an organization by its id
    @Query(value = "SELECT * FROM organization WHERE oid = ?1", nativeQuery = true)
    Organization findByOid(Long id);

    @Query(value = "SELECT * FROM organization WHERE ousername = ?1", nativeQuery = true)
    Organization findByOusername(String ousername);




}
