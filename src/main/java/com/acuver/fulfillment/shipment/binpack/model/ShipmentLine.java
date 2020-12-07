package com.acuver.fulfillment.shipment.binpack.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ShipmentLine {
    protected String id;
    protected List<Item> items;
}
