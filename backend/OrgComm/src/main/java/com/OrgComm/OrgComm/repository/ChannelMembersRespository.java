package com.OrgComm.OrgComm.repository;

import com.OrgComm.OrgComm.entity.ChannelMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelMembersRespository extends JpaRepository<ChannelMembers,Long> {



    @Query(value = "INSERT INTO channel_members (cid,eid) values (:cid,:eid)",nativeQuery = true)
    int insertChannelMembers(Long cid,Long eid);
}
