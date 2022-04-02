package com.kau.dgscalender.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.kau.dgscalender.Notifications;
import com.kau.dgscalender.dao.EventDAO;
import com.kau.dgscalender.dao.SectorsDAO;
import com.kau.dgscalender.dao.UserDAO;
import com.kau.dgscalender.dto.EventDTO;
import com.kau.dgscalender.entity.Event;
import com.kau.dgscalender.entity.Sectors;
import com.kau.dgscalender.entity.User;
import com.kau.dgscalender.exception.BusinessException;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class EventService extends BaseService {
	@Autowired
    private SimpMessagingTemplate template;
	@Autowired
	private EventDAO eventDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SectorsDAO sectorsDAO;
	public String getAllEvents() {
		List<Event> events = eventDAO.findAll();
		return new Gson().toJson(mapToDTOList(events));
	}

	public EventDTO addEvent(EventDTO request,String username,Notifications notifications) throws BusinessException {
		try {
			log.info("Enter addEvent Function..with title= " + request.getTitle());
			User user = userDAO.findByUsername(username);
			if (!user.isAdmin()) {
				throw new BusinessException("You can't add Event");
			}
			if (request.getTitle() == null || request.getTitle().isEmpty()) {
				throw new BusinessException("Event title is required");
			}
			Sectors sector = sectorsDAO.findByName(request.getSector());
			if (sector == null) {
				throw new BusinessException("this sector does not found");
			}
			if (request.getStart() == null || request.getStart().isEmpty()) {
				throw new BusinessException("Event start date is required");
			}
			Event event = mapToEntity(request);
			event.setCreatedBy(user);
			event.setSector(sector);
			sendNotification(notifications);
			eventDAO.save(event);
			return request;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}
	
	public void sendNotification(Notifications notifications) {
		
	    // Increment Notification by one
        notifications.increment();
notifications.setCount(100);
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", notifications);
	}

	public EventDTO updateEvent(EventDTO request,String username) throws BusinessException {
		try {
			log.info("Enter updateEvent Function..with title= " + request.getTitle());
			User user = userDAO.findByUsername(username);
			if (!user.isAdmin()) {
				throw new BusinessException("You can't add Event");
			}
			if (request.getTitle() == null || request.getTitle().isEmpty()) {
				throw new BusinessException("Event title is required");
			}
			Sectors sector = sectorsDAO.findByName(request.getSector());
			if (sector == null) {
				throw new BusinessException("this sector does not found");
			}
			if (request.getStart() == null || request.getStart().isEmpty()) {
				throw new BusinessException("Event start date is required");
			}

			Event event = eventDAO.findById(request.getId()).get();
			event.setModifiedBy(user);
			event.setSector(sector);
			event.setTitle(request.getTitle());
			event.setStart(request.getStart() != null
					? LocalDateTime.parse(request.getStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
					: null);
			event.setEndDate(request.getEnd() != null
					? LocalDateTime.parse(request.getEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
					: null);
			event.setAllDay(request.isAllDay());
			Event savedEvent = eventDAO.save(event);
			return mapToDTO(savedEvent);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	public Event mapToEntity(EventDTO eventDTO) throws ParseException {
		Event event = new Event();
		event.setTitle(eventDTO.getTitle());
		event.setAllDay(eventDTO.isAllDay());
		event.setStart(eventDTO.getStart() != null
				? LocalDateTime.parse(eventDTO.getStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				: null);
		event.setEndDate(eventDTO.getEnd() != null
				? LocalDateTime.parse(eventDTO.getEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				: null);
		return event;
	}

	public EventDTO mapToDTO(Event event) throws ParseException {
		EventDTO eventDTO = new EventDTO();
		eventDTO.setId(event.getId());
		eventDTO.setTitle(event.getTitle());
		eventDTO.setAllDay(event.isAllDay());
		eventDTO.setSector(event.getSector().getName());
		eventDTO.setCreatedBy(event.getCreatedBy() != null ? event.getCreatedBy().getUsername():null);
		eventDTO.setModifiedBy(event.getModifiedBy() != null ? event.getModifiedBy().getUsername():null);
		eventDTO.setColor(event.getSector() != null ? event.getSector().getColor():null);
		eventDTO.setStart(
				event.getStart() != null ? event.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
						: null);
		eventDTO.setEnd(
				event.getEndDate() != null ? event.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
						: null);

		return eventDTO;
	}

	public List<EventDTO> mapToDTOList(List<Event> events) {
		List<EventDTO> eventDTOs = new ArrayList<>();
		for (Event event : events) {
			try {
				eventDTOs.add(mapToDTO(event));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return eventDTOs;
	}

}
