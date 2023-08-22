package com.finalprj.kess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalprj.kess.repository.IManagerRepository;

@Service
public class ManagerService implements IManagerService{
	@Autowired
	IManagerRepository managerRepository;
}