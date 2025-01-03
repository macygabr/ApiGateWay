package com.example.demo.service;

import com.example.demo.models.s21.filter.FilterS21;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final KafkaService kafkaService;
    private final Logger logger = LoggerFactory.getLogger(SchoolService.class);

    public String peers(int page, int size, String campusId, FilterS21 filter) {
        logger.debug("Получение списка пиров: page={}, size={}, campusId={}, filter={}", page, size, campusId, filter);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", size);
        jsonObject.put("page", page);
        jsonObject.put("campusId", campusId);
        jsonObject.put("filter", filter);
        return kafkaService.sendRequest("peers", jsonObject.toString());
    }
}
