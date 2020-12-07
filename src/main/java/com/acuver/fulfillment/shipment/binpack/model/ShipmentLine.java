package com.acuver.fulfillment.shipment.binpack.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShipmentLine {
    protected String id;
    protected String skuId;
    protected Double qty;
}
