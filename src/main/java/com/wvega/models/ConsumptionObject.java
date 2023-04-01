package com.wvega.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionObject {
    private String id;
    private String description;
    private Type type;
    private EmissionType emissionType;
    private CategoryType categoryType;
    private  UnitOfMeasure unitOfMeasure;
    private int quantity;
    private Month month;
    private SubType subType;
}
