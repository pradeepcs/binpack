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
    protected String heightUOM = "";
    protected String weightUOM = "";
    protected Double tareWeight;
    protected Double totalWeight;
    public Double getTotalVolume(){
        return this.height * this.length * this.width;
    }
}
