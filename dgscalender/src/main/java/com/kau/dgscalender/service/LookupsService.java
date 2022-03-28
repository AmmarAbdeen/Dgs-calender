package com.kau.dgscalender.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.kau.dgscalender.dao.LookupsDAO;
import com.kau.dgscalender.dto.LookupsDTO;
import com.kau.dgscalender.entity.Lookups;
import com.kau.dgscalender.exception.BusinessException;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class LookupsService extends BaseService{
	
	@Autowired
	private LookupsDAO lookupsDAO;
	
	public String getAllLookupsByType(String type) throws BusinessException{
		try {
			return new Gson().toJson(mapToDTOList(lookupsDAO.findAllByType(type)));
		}catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	public List<LookupsDTO> mapToDTOList(List<Lookups> lookups){
		List<LookupsDTO> LookupsDTO = new ArrayList<>();
		for(Lookups lookup : lookups) {
			try {
				LookupsDTO.add(mapToDTO(lookup));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return LookupsDTO;
	}
	
	public LookupsDTO mapToDTO( Lookups lookup ) throws ParseException {
		LookupsDTO dto = new LookupsDTO();
		dto.setNameEN(lookup.getNameEN());
		dto.setNameAR(lookup.getNameAR());
		dto.setType(lookup.getType());
		dto.setCode(lookup.getCode());
		return dto;
	}

}
