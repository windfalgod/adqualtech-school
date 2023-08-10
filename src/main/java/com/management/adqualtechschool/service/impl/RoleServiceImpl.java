package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.dto.RoleDTO;
import com.management.adqualtechschool.entity.Role;
import com.management.adqualtechschool.repository.RoleRepository;
import com.management.adqualtechschool.service.RoleService;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDTO getRoleByName(String name) {
        Role role = roleRepository.findRoleByName(name);
        if (role == null) {
            throw new EntityNotFoundException(Message.NOT_FOUND_ROLE_NAME);
        }
        return modelMapper.map(role, RoleDTO.class);
    }
}
