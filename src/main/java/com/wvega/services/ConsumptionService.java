package com.wvega.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.wvega.db.FireBaseInitializer;
import com.wvega.models.ConsumeObject;
import com.wvega.models.enums.*;
import com.wvega.util.ResponseWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {

    @Autowired
    private FireBaseInitializer fireBase;

    public List<ConsumeObject> list() {
        List<ConsumeObject> consumeList = new ArrayList<>();
        ConsumeObject consumeObject;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                consumeObject = doc.toObject(ConsumeObject.class);
                consumeList.add(consumeObject);
            }
            return consumeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseWS add(HashMap<String, Object> consumption) {
        if (null == consumption.get("type"))
            return new ResponseWS(400, false, "Type value not found.", null);
        if (null == consumption.get("emissionType"))
            return new ResponseWS(400, false, "EmissionType value not found.", null);
        if (null == consumption.get("categoryType"))
            return new ResponseWS(400, false, "CategoryType value not found.", null);
        if (null == consumption.get("unit"))
            return new ResponseWS(400, false, "Unit Of Measure value not found.", null);
        if (null == consumption.get("quantity"))
            return new ResponseWS(400, false, "Quantity value not found.", null);
        if (null == consumption.get("month"))
            return new ResponseWS(400, false, "Month value not found.", null);

        ConsumeObject consume = getConsume(consumption);

        CollectionReference posts = getCollection();
        ApiFuture<WriteResult> writeResultApiFuture = posts.document().create(consume);

        try {
            if (null != writeResultApiFuture.get())
                return new ResponseWS(200, true, "Success", consume);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseWS(500, false, "", null);
    }

    public ResponseWS edit(String typeS, String categoryTypeS, String monthS, int quantity) {
        Type type = Type.valueOf(typeS.toUpperCase());
        CategoryType categoryType = CategoryType.valueOf(categoryTypeS.toUpperCase());
        Month month = Month.valueOf(monthS.toUpperCase());

        String internalId = month.getNumericValue() + "_" + type + "_" + categoryType;
        String id;
        ConsumeObject consume;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                consume = doc.toObject(ConsumeObject.class);
                if (internalId.equals(consume.getInternalId())) {
                    id = doc.getId();
                    consume.setQuantity(quantity);
                    ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(consume);
                    if (null != writeResultApiFuture.get())
                        return new ResponseWS(200, true, "Success", consume);
                    break;
                }
            }
        } catch (Exception e) {
            return new ResponseWS(500, false, "Error", null);
        }
        return new ResponseWS(500, false, "Not found", null);
    }

    private CollectionReference getCollection() {
        return fireBase.getFireStore().collection("Consumo");
    }

    private ConsumeObject getConsume(HashMap<String, Object> consumption) {
        String description = consumption.getOrDefault("description", "").toString();
        Type type = Type.valueOf(consumption.get("type").toString().toUpperCase());
        EmissionType emissionType = EmissionType.valueOf(consumption.get("emissionType").toString().toUpperCase());
        CategoryType categoryType = CategoryType.valueOf(consumption.get("categoryType").toString().toUpperCase());
        UnitOfMeasure uOM = UnitOfMeasure.valueOf(consumption.get("unit").toString().toUpperCase());
        int quantity = Integer.parseInt(consumption.get("quantity").toString());
        Month month = Month.valueOf(consumption.get("month").toString().toUpperCase());
        SubType subType = null != consumption.get("subType") ? SubType.valueOf(consumption.get("subType").toString().toUpperCase()) : null;

        String internalId = month.getNumericValue() + "_" + type + "_" + categoryType;

        return new ConsumeObject(internalId, description, type, emissionType, categoryType,
                uOM, quantity, month, subType);
    }

    public ResponseWS annualFuelConsume() {
        List<ConsumeObject> fuelList = list().stream()
                .filter(c -> Type.FUEL == c.getType())
                .collect(Collectors.toList());

        double sumAdmin = fuelList.stream()
                .filter(c -> CategoryType.ADMIN == c.getCategoryType())
                .mapToDouble(ConsumeObject::getQuantity)
                .reduce(0, Double::sum);
        double sumLogistic = fuelList.stream()
                .filter(c -> CategoryType.LOGISTIC == c.getCategoryType())
                .mapToDouble(ConsumeObject::getQuantity)
                .reduce(0, Double::sum);
        double sumIndirect = fuelList.stream()
                .filter(c -> CategoryType.INDIRECT == c.getCategoryType())
                .mapToDouble(ConsumeObject::getQuantity)
                .reduce(0, Double::sum);

        double total = sumAdmin + sumLogistic + sumIndirect;

        HashMap<String, Double> percentage = new LinkedHashMap<>();
        percentage.put("admin", (sumAdmin / total));
        percentage.put("logistic", sumLogistic / total);
        percentage.put("indirect", sumIndirect / total);

        return new ResponseWS(200, true, "Percentage", percentage);
    }

    public ResponseWS monthlyFuelConsume() {
        List<ConsumeObject> fuelList = list().stream()
                .filter(c -> Type.FUEL == c.getType())
                .collect(Collectors.toList());
        HashMap<String, Double> monthGallons = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            List<ConsumeObject> monthList = fuelList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double divider = (monthList.size() / 3) > 0 ? monthList.size() / 3 : 1;
            double monthFuel = fuelList.stream()
                    .filter(c -> month == c.getMonth())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);
            monthGallons.put(month.getValue(), monthFuel / divider);
        }

        return new ResponseWS(200, true, "Averages", monthGallons);
    }

    public ResponseWS maxSegment() {
        List<ConsumeObject> segmentList = list();

        double direct = segmentList.stream()
                .filter(c -> EmissionType.DIRECT == c.getEmissionType())
                .mapToDouble(ConsumeObject::getQuantity)
                .reduce(0, Double::sum);
        double indirect = segmentList.stream()
                .filter(c -> EmissionType.INDIRECT == c.getEmissionType())
                .mapToDouble(ConsumeObject::getQuantity)
                .reduce(0, Double::sum);
        double other = segmentList.stream()
                .filter(c -> EmissionType.OTHER == c.getEmissionType())
                .mapToDouble(ConsumeObject::getQuantity)
                .reduce(0, Double::sum);

        double total = direct + indirect + other;

        direct = direct / total;
        indirect = indirect / total;
        other = other / total;

        double max1 = Math.max(direct, indirect);
        double max2 = Math.max(max1, other);

        HashMap<String, Double> segment = new LinkedHashMap<>();
        String name = max2 == direct ? "Direct" : max2 == indirect ? "Indirect" : "Other";
        segment.put(name, max2);
        return new ResponseWS(200, true, "More impact segment", segment);
    }

    public ResponseWS packagingPlant() {
        List<ConsumeObject> packagingList = list().stream()
                .filter(c -> Type.ELECTRICITY == c.getType())
                .filter(c -> CategoryType.LOGISTIC == c.getCategoryType())
                .collect(Collectors.toList());

        HashMap<String, Double> monthPackagingPlant = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            List<ConsumeObject> monthList = packagingList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double divider = (monthList.size() / 3) > 0 ? monthList.size() / 3 : 1;
            double monthFuel = packagingList.stream()
                    .filter(c -> month == c.getMonth())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);
            monthPackagingPlant.put(month.getValue(), monthFuel / divider);
        }
        return new ResponseWS(200, true, "Packaging Plant percentage", monthPackagingPlant);
    }

    public ResponseWS monthlyConsumption() {
        List<ConsumeObject> consumeList = list();

        List<ConsumeObject> electricityList = consumeList.stream()
                .filter(c -> Type.ELECTRICITY == c.getType())
                .collect(Collectors.toList());

        List<ConsumeObject> fuelList = consumeList.stream()
                .filter(c -> Type.FUEL == c.getType())
                .collect(Collectors.toList());

        HashMap<String, HashMap<String, Double>> monthConsumption = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            List<ConsumeObject> monthFuelList = fuelList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double dividerFuel = (monthFuelList.size() / 3) > 0 ? monthFuelList.size() / 3 : 1;
            double monthFuel = monthFuelList.stream()
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);

            List<ConsumeObject> monthElectricityList = electricityList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double dividerElectricity = (monthElectricityList.size() / 3) > 0 ? monthElectricityList.size() / 3 : 1;
            double monthElectricity = monthElectricityList.stream()
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);

            HashMap<String, Double> monthDetail = new LinkedHashMap<>();
            monthDetail.put("Fuel", monthFuel / dividerFuel);
            monthDetail.put("Electricity", monthElectricity / dividerElectricity);

            monthConsumption.put(month.getValue(), monthDetail);
        }

        return new ResponseWS(200, true, "Monthly Fuel and Electricity", monthConsumption);
    }

    public ResponseWS petroleumAverage() {
        List<ConsumeObject> List = list();

        HashMap<String, HashMap<String, Double>> monthConsumption = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            List<ConsumeObject> monthList = List.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());

            double monthFuel = monthList.stream()
                    .filter(c -> Type.FUEL == c.getType())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);

            double monthOil = monthList.stream()
                    .filter(c -> SubType.OIL == c.getSubType())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);

            double totalMonth = monthFuel + monthOil;

            HashMap<String, Double> salesDetail = new LinkedHashMap<>();
            salesDetail.put("Fuel", monthFuel / totalMonth);
            salesDetail.put("Oil", monthOil / totalMonth);

            monthConsumption.put(month.getValue(), salesDetail);
        }

        return new ResponseWS(200, true, "Travel monthly", monthConsumption);
    }

    public ResponseWS monthlyTravels() {
        List<ConsumeObject> travelList = list().stream()
                .filter(c -> Type.TRAVEL == c.getType())
                .collect(Collectors.toList());

        List<ConsumeObject> adminList = travelList.stream()
                .filter(c -> CategoryType.ADMIN == c.getCategoryType())
                .collect(Collectors.toList());

        List<ConsumeObject> salesList = travelList.stream()
                .filter(c -> CategoryType.SALES == c.getCategoryType())
                .collect(Collectors.toList());

        HashMap<String, HashMap<String, Double>> monthConsumption = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            List<ConsumeObject> travelAdminList = adminList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double divTravelAdmin = (travelAdminList.size() / 3) > 0 ? travelAdminList.size() / 3 : 1;
            double monthTravelAdmin = travelAdminList.stream()
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);

            List<ConsumeObject> travelSalesList = salesList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double divTravelSales = (travelSalesList.size() / 3) > 0 ? travelSalesList.size() / 3 : 1;
            double monthTravelSales = travelSalesList.stream()
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);

            HashMap<String, Double> salesDetail = new LinkedHashMap<>();
            salesDetail.put("Administration", monthTravelAdmin / divTravelAdmin);
            salesDetail.put("Sales", monthTravelSales / divTravelSales);

            monthConsumption.put(month.getValue(), salesDetail);
        }

        return new ResponseWS(200, true, "Travel monthly", monthConsumption);
    }

    public ResponseWS monthlyOil() {
        List<ConsumeObject> oilList = list().stream()
                .filter(c -> SubType.OIL == c.getSubType())
                .collect(Collectors.toList());

        HashMap<String, Double> monthOil = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            List<ConsumeObject> monthList = oilList.stream()
                    .filter(c -> month == c.getMonth())
                    .collect(Collectors.toList());
            double divider = (monthList.size() / 3) > 0 ? monthList.size() / 3 : 1;
            double monthFuel = oilList.stream()
                    .filter(c -> month == c.getMonth())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);
            monthOil.put(month.getValue(), monthFuel / divider);
        }
        return new ResponseWS(200, true, "Oil gallons", monthOil);
    }

    public ResponseWS coolantMonth() {
        List<ConsumeObject> coolantList = list().stream()
                .filter(c -> SubType.COOLANT == c.getSubType())
                .collect(Collectors.toList());
        String monthName = "January";
        double coolantMonthMax = 99999;
        for (Month month : Month.values()) {
            double coolantMonthAux = coolantList.stream()
                    .filter(c -> month == c.getMonth())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);
            if (coolantMonthMax > coolantMonthAux) {
                coolantMonthMax = coolantMonthAux;
                monthName = month.getValue();
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("Month", monthName);
        result.put("Gallons", coolantMonthMax);
        return new ResponseWS(200, true, "Best Coolant Month", result);
    }

    public ResponseWS getGallons() {
        List<ConsumeObject> fuelList = list().stream()
                .filter(c -> Type.FUEL == c.getType())
                .collect(Collectors.toList());
        HashMap<String, Double> monthConsumption = new HashMap<>();
        for (Month month : Month.values()) {
            double fuelResult = fuelList.stream()
                    .filter(c -> month == c.getMonth())
                    .mapToDouble(ConsumeObject::getQuantity)
                    .reduce(0, Double::sum);
            monthConsumption.put(month.getValue(), fuelResult);
        }

        Map.Entry<String, Double> minEntry = monthConsumption.entrySet()
                .stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .orElse(null);

        Map.Entry<String, Double> maxEntry = monthConsumption.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElse(null);

        String monthNameMin = minEntry.getKey();
        double gallonMonthMin = minEntry.getValue();

        String monthNameMax = maxEntry.getKey();
        double gallonMonthMax = maxEntry.getValue();

        HashMap<String, Object> maxMonth = new HashMap<>();
        maxMonth.put("Month", monthNameMax);
        maxMonth.put("Gallons", gallonMonthMax);

        HashMap<String, Object> minMonth = new HashMap<>();
        minMonth.put("Month", monthNameMin);
        minMonth.put("Gallons", gallonMonthMin);

        HashMap<String, Object> result = new HashMap<>();
        result.put("Max", maxMonth);
        result.put("Min", minMonth);
        return new ResponseWS(200, true, "Max and Min fuel consumption month", result);
    }
}
