package com.OrgComm.OrgComm.service;

import com.OrgComm.OrgComm.entity.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.OrgComm.OrgComm.repository.ChannelsRepository;

@Service
public class ChannelService {

    @Autowired
    private ChannelsRepository channelRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeeService employeeService;

    // create channel
    public boolean createChannel(Channels channel){
        if(channel.getOrganization()!=null){
            if(organizationService.findById(channel.getOrganization().getOid())==null){
                return false;
            }
        }
        if(channel.getAdmin()!=null) {
            if (employeeService.findById(channel.getAdmin().getEid()) == null) {
                return false;
            }
        }
        channelRepository.save(channel);
        return true;
    }

}
