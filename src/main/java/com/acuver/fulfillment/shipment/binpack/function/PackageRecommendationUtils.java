package com.acuver.fulfillment.shipment.binpack.function;

import com.acuver.fulfillment.shipment.binpack.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class PackageRecommendationUtils {


    public static PackageDimension getTotalWeightAndVolume(Map<String, Sku> skuIdToSkuMap, Shipment shipment) {
        PackageDimension packageDimension = new PackageDimension();
        Double totalWeight = 0.0;
        Double totalVolume = 0.0;
        boolean setHazardousItemFlag = false;

        List<Double> lengthList = new ArrayList<>();
        List<Double> widthList = new ArrayList<>();
        List<Double> heightList = new ArrayList<>();

        List<Double> qtyWiseTotalLengthList = new ArrayList<>();
        List<Double> qtyWiseTotalWidthList = new ArrayList<>();
        List<Double> qtyWiseTotalHeightList = new ArrayList<>();



        for (Item item : shipment.getItems()) {
            Sku sku = skuIdToSkuMap.get(item.getSkuId());
            Double qty=item.getQty();
//            if(sku.isHazardousMaterial()){
//                setHazardousItemFlag = true;
//            }
            packageDimension.setHeightUOM(sku.getHeightUOM());
            packageDimension.setWeightUOM(sku.getWeightUOM());
            totalWeight += sku.getWeight() * qty;
            totalVolume += sku.getHeight() * sku.getWidth() * sku.getLength() * qty;
            lengthList.add(sku.getLength());
            widthList.add(sku.getWidth());
            heightList.add(sku.getHeight());

            qtyWiseTotalLengthList.add(sku.getLength() * qty);
            qtyWiseTotalWidthList.add(sku.getWidth() * qty);
            qtyWiseTotalHeightList.add(sku.getHeight() * qty);
        }

        //packageDimension.setSupportsHazardous(setHazardousItemFlag);
        packageDimension.setTotalWeight(totalWeight);
        packageDimension.setTotalVolume(totalVolume);
        packageDimension.setMaxLength(lengthList.stream().max(Comparator.comparing(Double::valueOf)).get());
        packageDimension.setMaxWidth(widthList.stream().max(Comparator.comparing(Double::valueOf)).get());
        packageDimension.setMaxHeight(heightList.stream().max(Comparator.comparing(Double::valueOf)).get());

        packageDimension.setTotalHeight(qtyWiseTotalHeightList.stream()
                .collect(Collectors.summingDouble(Double::valueOf)));
        packageDimension.setTotalLength(qtyWiseTotalLengthList.stream()
                .collect(Collectors.summingDouble(Double::valueOf)));
        packageDimension.setTotalWidth(qtyWiseTotalWidthList.stream()
                .collect(Collectors.summingDouble(Double::valueOf)));
        return packageDimension;
    }

    static List<PackageDimension> getSortedShippingPackageOptions(PackageRecommendationRequest request){
        List<PackageDimension> optionPackageDimensions = new ArrayList<>();
        request.getAvailablePackages().forEach(shipmentPackage->{
            PackageDimension optionPackageDimension =getPackageDimensionForPackageOption(shipmentPackage);
            optionPackageDimensions.add(optionPackageDimension);
        });
        optionPackageDimensions.sort(Comparator.comparing(PackageDimension::getTotalVolume).thenComparing(PackageDimension::getTotalWeight));
        return optionPackageDimensions;
    }

    private static PackageDimension getPackageDimensionForPackageOption(ShipmentPackage packageOptionDimensions) {
        PackageDimension packageDimension = new PackageDimension();
        packageDimension.setMaxLength(packageOptionDimensions.getLength());
        packageDimension.setMaxWidth(packageOptionDimensions.getWidth());
        packageDimension.setMaxHeight(packageOptionDimensions.getHeight());
        packageDimension.setTotalHeight(packageOptionDimensions.getHeight());
        packageDimension.setTotalLength(packageOptionDimensions.getLength());
        packageDimension.setTotalWidth(packageOptionDimensions.getWidth());
        packageDimension.setHeightUOM(packageOptionDimensions.getHeightUOM());
        packageDimension.setTotalWeight(packageOptionDimensions.getMaxWeight());
        packageDimension.setPackageType(packageOptionDimensions.getId());
      //  packageDimension.setSupportsHazardous((Boolean) packageOptionDimensions.get("hazardous"));
        if (packageOptionDimensions.getTareWeight() != null) {
            packageDimension.setTareWeight(packageOptionDimensions.getTareWeight());
        }
        packageDimension.setTotalVolume(packageOptionDimensions.getTotalVolume());
        return packageDimension;
    }


    static PackageDimension getSuitablePackageOption(PackageDimension requiredPackageDimension,
                                                     Map<String, Sku> skuIdToSkuMap, List<PackageDimension> sortedAvailablePackage) {

        for (PackageDimension packageDimension : sortedAvailablePackage) {
            if (packageSupportsItem()) {
                if (checkForDimension(requiredPackageDimension, packageDimension)) {
                    return packageDimension;
                }
            }
        }
        return null;
    }

    static Boolean packageSupportsItem(){
        return true;
    }

    private static boolean checkForDimension(PackageDimension requiredPackageDimension,
                                             PackageDimension packageOptionDimension) {

        return (requiredPackageDimension.getTotalWeight() <= packageOptionDimension.getTotalWeight() &&
                requiredPackageDimension.getTotalVolume() <= packageOptionDimension.getTotalVolume() &&
                (canStackOnLWH(requiredPackageDimension.getTotalLength(), requiredPackageDimension.getMaxWidth(),
                        requiredPackageDimension.getMaxHeight(), packageOptionDimension) ||
                        canStackOnLWH(requiredPackageDimension.getMaxLength(), requiredPackageDimension.getTotalWidth(),
                                requiredPackageDimension.getMaxHeight(), packageOptionDimension) ||
                        canStackOnLWH(requiredPackageDimension.getMaxLength(), requiredPackageDimension.getMaxWidth(),
                                requiredPackageDimension.getTotalHeight(), packageOptionDimension)));
    }


    private static  boolean canStackOnLWH (Double lengthDimension, Double widthDimension, Double heightDimension,
                                           PackageDimension packageOptionDimension ){

        List <Double> packageDimensions = new LinkedList<>();
        List <Double> itemDimensions = new LinkedList<>();

        itemDimensions.add(lengthDimension);
        itemDimensions.add(widthDimension);
        itemDimensions.add(heightDimension);

        Collections.sort(itemDimensions);

        packageDimensions.add(packageOptionDimension.getMaxLength());
        packageDimensions.add(packageOptionDimension.getMaxWidth());
        packageDimensions.add(packageOptionDimension.getMaxHeight());

        Collections.sort(packageDimensions);

        return (itemDimensions.get(0) <= packageDimensions.get(0) && itemDimensions.get(1) <= packageDimensions.get(1)
                && itemDimensions.get(2) <= packageDimensions.get(2));
    }


}
