package com.wvega.services;

import com.wvega.models.ConsumeObject;
import com.wvega.models.enums.*;
import com.wvega.util.ResponseWS;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ConsumptionService {

    public ResponseWS addConsume(HashMap<String, Object> consumption) {
        if (null == consumption.get("type"))
            return new ResponseWS(400, false, "Type value not found.", null);
        if (null == consumption.get("emissionType"))
            return new ResponseWS(400, false, "EmissionType value not found.", null);
        if (null == consumption.get("categoryType"))
            return new ResponseWS(400, false, "CategoryType value not found.", null);
        if (null == consumption.get("UOM"))
            return new ResponseWS(400, false, "Unit Of Measure value not found.", null);
        if (null == consumption.get("quantity"))
            return new ResponseWS(400, false, "Quantity value not found.", null);
        if (null == consumption.get("month"))
            return new ResponseWS(400, false, "Month value not found.", null);

        String description = consumption.getOrDefault("description", "").toString();
        Type type = Type.valueOf(consumption.get("type").toString().toUpperCase());
        EmissionType emissionType = EmissionType.valueOf(consumption.get("emissionType").toString().toUpperCase());
        CategoryType categoryType = CategoryType.valueOf(consumption.get("categoryType").toString().toUpperCase());
        UnitOfMeasure uOM = UnitOfMeasure.valueOf(consumption.get("UOM").toString().toUpperCase());
        int quantity = Integer.parseInt(consumption.get("quantity").toString());
        Month month = Month.valueOf(consumption.get("month").toString().toUpperCase());
        SubType subType = null != consumption.get("subType") ? SubType.valueOf(consumption.get("subType").toString().toUpperCase()) : null;

        String id = month.getNumericValue() + "_" + type + "_" + categoryType;

        ConsumeObject consume = new ConsumeObject(id, description, type, emissionType, categoryType,
                uOM, quantity, month, subType);

        return new ResponseWS(200, true, "Success", consume);
    }
}
