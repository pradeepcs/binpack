package com.acuver.fulfillment.shipment.binpack.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Sku {
    protected String id;
    protected String type;
    protected Double height;
    protected Double length;
    protected Double width;
    protected Double weight;
    protected String heightUOM;
    protected  String weightUOM;
    protected Boolean packAlone;
    protected Boolean fragile;
}
