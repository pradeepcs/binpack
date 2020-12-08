package com.acuver.fulfillment.shipment.binpack.function;

import com.acuver.fulfillment.shipment.binpack.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackageRecommendation implements
        RequestHandler<PackageRecommendationRequest, PackageRecommendationResponse> {

    @Override
    public PackageRecommendationResponse handleRequest(PackageRecommendationRequest request, Context context) {
        return findSuitableBinPack(request);
    }


    private PackageRecommendationResponse findSuitableBinPack(PackageRecommendationRequest request) {
        PackageRecommendationResponse response = new PackageRecommendationResponse();

        List<PackageDimension> sortedAvailablePackage = PackageRecommendationUtils.getSortedShippingPackageOptions(request);

        HashMap<String, Sku> mapSkuIdAndSku = new HashMap<>();
        request.getSkus().forEach(sku -> {
            mapSkuIdAndSku.put(sku.getId(), sku);
        });
        List<Shipment> responseShipment = new ArrayList<>();
        request.getShipments().forEach(shipment -> {
            PackageDimension requiredPackageDimension = PackageRecommendationUtils.getTotalWeightAndVolume(mapSkuIdAndSku, shipment);
            PackageDimension availablePackage = PackageRecommendationUtils.getSuitablePackageOption(requiredPackageDimension, mapSkuIdAndSku, sortedAvailablePackage);
            PackageRecommendationUtils.setRecommendedPackageInResponse(shipment, availablePackage);
            shipment.getRecommendedPackage().setTotalWeight(BigDecimal.valueOf(
                    Double.sum(requiredPackageDimension.getTotalWeight(), availablePackage.getTareWeight())).
                    setScale(2, RoundingMode.HALF_UP).doubleValue());
            responseShipment.add(shipment);

        });
        response.setShipments(responseShipment);
        return response;
    }


}
