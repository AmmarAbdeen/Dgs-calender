package com.kau.dgscalender.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Service
public class FreeMakerService {
	
	private static Configuration cfg = null;

	public static Configuration getCfg() {
		return cfg;
	}

	public static void setCfg(Configuration cfg) {
		FreeMakerService.cfg = cfg;
	}

	public FreeMakerService() {
		freemarkerConfiguration();
	}

	public static Configuration freemarkerConfiguration() {
		String path = "D:\\myData\\Dgs calender\\dgscalender\\src\\main\\webapp";
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setOutputFormat(XMLOutputFormat.INSTANCE);
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cfg;
	}


	public String mappingMessageForSendPasswordMail( String param1, String param2,String fileName) {
		Map<String, String> map = new HashMap<>();
		String msg = null;
		map.put("param1", param1);
		map.put("param2", param2);
		try {
			Template template = cfg.getTemplate(fileName + ".ftl");
			StringWriter out = new StringWriter();
			try {
				template.process(map, out);
				msg = out.getBuffer().toString();
				out.flush();
			} catch (TemplateException e) {
				msg = null;
				e.printStackTrace();
			}
		} catch (IOException e) {
			msg = null;
			e.printStackTrace();
		}

		return msg;
	}

}
