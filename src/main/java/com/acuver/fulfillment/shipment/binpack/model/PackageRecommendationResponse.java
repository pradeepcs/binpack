package com.acuver.fulfillment.shipment.binpack.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class PackageRecommendationResponse {
    protected String version;
    protected List<Shipment> shipments;

}
