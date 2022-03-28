package com.kau.dgscalender.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.kau.dgscalender.dao.SectorsDAO;
import com.kau.dgscalender.dao.UserDAO;
import com.kau.dgscalender.dto.EventDTO;
import com.kau.dgscalender.dto.SectorsDTO;
import com.kau.dgscalender.entity.Event;
import com.kau.dgscalender.entity.Sectors;
import com.kau.dgscalender.entity.User;
import com.kau.dgscalender.exception.BusinessException;

import lombok.extern.apachecommons.CommonsLog;


@CommonsLog
@Service
public class SectorsService  extends BaseService {
	
	@Autowired
	private SectorsDAO sectorsDAO; 
	@Autowired
	private UserDAO userDAO;
	public String getAllSectors() {
		List<Sectors> sectors = sectorsDAO.findAll();
		return new Gson().toJson(mapToDTOList(sectors));
	}
	
	public SectorsDTO addSector(SectorsDTO request) throws BusinessException {
		try {
			log.info("Enter addSector Function..with title= " + request.getName());
			User user = userDAO.findByUsername(request.getCreatedBy());
			if (!user.isAdmin()) {
				throw new BusinessException("You can't add Sector");
			}
			if (request.getName() == null || request.getName().isEmpty()) {
				throw new BusinessException("Sector name is required");
			}
			if (request.getColor() == null || request.getColor().isEmpty()) {
				throw new BusinessException("Sector color is required");
			}
			Sectors sector = mapToEntity(request);
			sector.setCreatedBy(user);
			sectorsDAO.save(sector);
			return request;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	public Sectors mapToEntity(SectorsDTO sectorsDTO) throws ParseException {
		Sectors sector = new Sectors();
		sector.setName(sectorsDTO.getName());
		sector.setColor(sectorsDTO.getColor());		
		return sector;
	}
	
	public List<SectorsDTO> mapToDTOList(List<Sectors> sectors) {
		List<SectorsDTO> sectorsDTOs = new ArrayList<>();
		for (Sectors sector : sectors) {
			try {
				sectorsDTOs.add(mapToDTO(sector));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return sectorsDTOs;
	}
	
	public SectorsDTO mapToDTO(Sectors sector) throws ParseException {
		SectorsDTO sectorDTO = new SectorsDTO();
		sectorDTO.setId(sector.getId());
		sectorDTO.setName(sector.getName());
		sectorDTO.setColor(sector.getColor());
		
		return sectorDTO;
	}


}
