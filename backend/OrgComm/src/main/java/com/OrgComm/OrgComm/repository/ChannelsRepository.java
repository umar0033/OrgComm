package com.OrgComm.OrgComm.repository;

import com.OrgComm.OrgComm.entity.Channels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChannelsRepository extends JpaRepository<Channels,Long> {




    @Query(value="select * from channels where oid = ?1",nativeQuery = true)
    public List<Channels> getChannelsByOrganization(Long oid);

    @Query(value="select * from channels where admin_id = ?1",nativeQuery = true)
    public Channels getChannelByAdmin(Long admin_id);

    @Query(value="select * from channels where cid = ?1",nativeQuery = true)
    public Channels getChannelById(Long cid);

    // write insert query
    @Query(value = "insert into channels (cname,cdescription,ccreatedat,oid,admin_id) values (:cname,:cdescription,:ccreatedat,:oid,:admin_id)",nativeQuery = true)
    public int insertChannel(
            @Param("cname") String cname,
            @Param("cdescription") String cdescription,
            @Param("ccreatedat") String ccreatedat,
            @Param("oid") Long oid,
            @Param("admin_id") Long admin_id
    );



}
