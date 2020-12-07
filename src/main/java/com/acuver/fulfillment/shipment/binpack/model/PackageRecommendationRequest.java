package com.acuver.fulfillment.shipment.binpack.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PackageRecommendationRequest {
    protected String version;
    protected List<Shipment> shipments;
    protected List<ShipmentPackage> availablePackages;
    protected List<Sku> skus;
}
