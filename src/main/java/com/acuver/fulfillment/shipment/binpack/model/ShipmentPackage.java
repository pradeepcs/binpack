package com.acuver.fulfillment.shipment.binpack.model;


import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ShipmentPackage {
    protected String id;
    protected String type;
    protected Double height;
    protected Double length;
    protected Double width;
    protected Double maxWeight;
    protected Double tareWeight;
    protected Double totalWeight;
}
