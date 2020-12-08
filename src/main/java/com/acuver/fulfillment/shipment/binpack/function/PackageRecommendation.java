package com.acuver.fulfillment.shipment.binpack.function;

import com.acuver.fulfillment.shipment.binpack.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PackageRecommendation implements
        RequestHandler<PackageRecommendationRequest, PackageRecommendationResponse> {

    @Override
    public PackageRecommendationResponse handleRequest(PackageRecommendationRequest request, Context context) {
        return findSuitableBinPack(request);
    }


    private PackageRecommendationResponse findSuitableBinPack(PackageRecommendationRequest request) {
        PackageRecommendationResponse response = new PackageRecommendationResponse();

        List<PackageDimension> sortedAvailablePackage= PackageRecommendationUtils.getSortedShippingPackageOptions(request);

        HashMap<String, Sku> mapSkuIdAndSku=new HashMap<>();
        request.getSkus().forEach(sku -> {
            mapSkuIdAndSku.put(sku.getId(),sku);
        });
        List<Shipment> responseShipment= new ArrayList<>();
        request.getShipments().forEach(shipment -> {
                PackageDimension requiredPackageDimension = PackageRecommendationUtils.getTotalWeightAndVolume(mapSkuIdAndSku,shipment);
                PackageDimension availablePackage = PackageRecommendationUtils.getSuitablePackageOption(requiredPackageDimension, mapSkuIdAndSku,sortedAvailablePackage);
                System.out.print(availablePackage);
                setRecommendedPackageInResponse(shipment, availablePackage);
                shipment.getRecommendedPackage().setTotalWeight( BigDecimal.valueOf(
                        Double.sum(requiredPackageDimension.getTotalWeight(), availablePackage.getTareWeight())).
                        setScale(2, RoundingMode.HALF_UP).doubleValue());
                System.out.print(shipment);
            responseShipment.add(shipment);

            });
        response.setShipments(responseShipment);






//        request.getShipments().stream().forEach(shipment -> {
//            ShipmentPackage shipmentPackage = new ShipmentPackage();
//            if("shipmentPackage1".equals(shipment.getId())) {
//                shipmentPackage.setId("package1");
//                shipmentPackage.setType("carton");
//            } else if("shipmentPackage2".equals(shipment.getId())) {
//                shipmentPackage.setId("package2");
//                shipmentPackage.setType("carton");
//            } else {
//                shipmentPackage.setId("other");
//                shipmentPackage.setType("carton");
//            }
//            shipment.setRecommendedPackage(shipmentPackage);
//        });
//        response.setShipments(request.getShipments());
        return response;
   }

    private static void setRecommendedPackageInResponse(Shipment shipment,
                                                        PackageDimension packageDimensions) {
        ShipmentPackage shipmentPackage1=new ShipmentPackage();
        shipmentPackage1.setLength(packageDimensions.getMaxLength());
        shipmentPackage1.setWidth(packageDimensions.getMaxWidth());
        shipmentPackage1.setHeight(packageDimensions.getTotalHeight());
        shipmentPackage1.setHeightUOM(packageDimensions.getHeightUOM());
        shipmentPackage1.setMaxWeight(packageDimensions.getTotalWeight() + packageDimensions.getTareWeight());
        shipmentPackage1.setId(packageDimensions.getPackageType());
        shipment.setRecommendedPackage(shipmentPackage1);
        //shipmentPackage.setPackageType(packageDimensions.getPackageType());
    }




}
