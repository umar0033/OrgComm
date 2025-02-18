package com.OrgComm.OrgComm.service;


import com.OrgComm.OrgComm.dto.OrganizationDTO;
import com.OrgComm.OrgComm.entity.Channels;
import com.OrgComm.OrgComm.entity.Department;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired ChannelService channelService;

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }



    public Organization createOrganization(Organization organization) {
        if(findByUsername(organization.getOusername()) != null){
            return null;
        }
        return organizationRepository.save(organization);
    }

    public Organization findById(Long id) {
        return organizationRepository.findByOid(id);
    }

    public Organization findByUsername(String ousername) {
        return organizationRepository.findByOusername(ousername);
    }

    public boolean login(OrganizationDTO organizationDTO) {
        Organization organization = findByUsername(organizationDTO.getOusername());
        if (organization == null) {
            return false;
        }
        return organization.getOpassword().equals(organizationDTO.getOpassword());
    }


    public void createAllChannelsForDepartments(Organization organization){
        organization=organizationRepository.findByOid(organization.getOid());
        List<Department> departments=organization.getDepartments();
        for(Department department:departments){
            Channels channel=new Channels();
            channel.setCname(department.getDname());
            channel.setOrganization(organization);
            channel.setAdmin(department.getManagerid());
            channelService.createChannel(channel);
        }
    }


    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }






}
