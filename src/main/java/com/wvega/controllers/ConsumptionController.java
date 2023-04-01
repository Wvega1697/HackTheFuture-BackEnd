package com.wvega.controllers;

import com.wvega.models.ConsumeObject;
import com.wvega.models.enums.*;
import com.wvega.services.ConsumptionService;
import com.wvega.util.ResponseWS;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController("consume")
public class ConsumptionController {

    private ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @PostMapping("/add")
    public ResponseWS saveNewConsumption(@RequestBody HashMap<String, Object> newConsumption){
        return consumptionService.addConsume(newConsumption);
    }

}
