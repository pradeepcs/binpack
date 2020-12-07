package com.acuver.fulfillment.shipment.binpack.function;

import com.acuver.fulfillment.shipment.binpack.model.PackageRecommendationRequest;
import com.acuver.fulfillment.shipment.binpack.model.PackageRecommendationResponse;
import com.acuver.fulfillment.shipment.binpack.model.ShipmentPackage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PackageRecommendation implements
        RequestHandler<PackageRecommendationRequest, PackageRecommendationResponse> {

    @Override
    public PackageRecommendationResponse handleRequest(PackageRecommendationRequest request, Context context) {
        return findSuitableBinPack(request);
    }


    private PackageRecommendationResponse findSuitableBinPack(PackageRecommendationRequest request) {
        PackageRecommendationResponse response = new PackageRecommendationResponse();

        request.getShipments().stream().forEach(shipment -> {
            ShipmentPackage shipmentPackage = new ShipmentPackage();
            if("shipmentPackage1".equals(shipment.getId())) {
                shipmentPackage.setId("package1");
                shipmentPackage.setType("carton");
            } else if("shipmentPackage2".equals(shipment.getId())) {
                shipmentPackage.setId("package2");
                shipmentPackage.setType("carton");
            } else {
                shipmentPackage.setId("other");
                shipmentPackage.setType("carton");
            }
            shipment.setRecommendedPackage(shipmentPackage);
        });
        response.setShipments(request.getShipments());
        return response;
    }



}
