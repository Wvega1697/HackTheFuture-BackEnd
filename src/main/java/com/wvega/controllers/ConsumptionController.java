package com.wvega.controllers;

import com.wvega.services.ConsumptionService;
import com.wvega.util.ResponseWS;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("consume")
public class ConsumptionController {

    private ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @PostMapping("/add") //Desde este método POST se pueden agregar todos los consumos
    public ResponseWS saveNewConsumption(@RequestBody HashMap<String, Object> newConsumption){
        return consumptionService.add(newConsumption);
    }

    @PutMapping("/edit/{type}/{categoryType}/{month}/{quantity}")
    public ResponseWS editConsumption(@PathVariable String type, @PathVariable String categoryType, @PathVariable String month, @PathVariable int quantity){
        return consumptionService.edit(type, categoryType, month, quantity);
    }

    @GetMapping("/annualFuel")
    public ResponseWS getAnnualFuelConsume(){
        return consumptionService.annualFuelConsume();
    }

    @GetMapping("/monthlyFuel")
    public ResponseWS getMonthlyFuelConsume(){
        return consumptionService.monthlyFuelConsume();
    }

    @GetMapping("/segment")
    public ResponseWS getSegment(){
        return consumptionService.maxSegment();
    }/*

    @GetMapping("/")
    public ResponseWS getPackagingPlant(){
        return new ResponseWS();
    }

    @GetMapping("/")
    public ResponseWS getMonthlyConsumption(){
        return new ResponseWS();
    }

    @GetMapping("/")
    public ResponseWS getPetroleumAverage(){
        return new ResponseWS();
    }

    @GetMapping("/")
    public ResponseWS getMonthlyTravels(){
        return new ResponseWS();
    }

    @GetMapping("/")
    public ResponseWS getMonthlyOil(){
        return new ResponseWS();
    }

    @GetMapping("/")
    public ResponseWS getCoolantMonth(){
        return new ResponseWS();
    }

    @GetMapping("/")
    public ResponseWS getGallons(){
        return new ResponseWS();
    }//*/
}
