package com.acuver.fulfillment.shipment.binpack.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDimension {
    private String packageType;
    private Double totalWeight = 0.0;
    private Double totalVolume = 0.0;
    private Double maxLength = 0.0;
    private Double maxWidth = 0.0;
    private Double maxHeight = 0.0;
    private Double totalHeight = 0.0;
    private Double totalLength = 0.0;
    private Double totalWidth = 0.0;
    private String heightUOM = "";
    private String weightUOM = "";
    private Double tareWeight = 0.0;
}
