package com.acuver.fulfillment.shipment.binpack;

import com.acuver.fulfillment.shipment.binpack.function.PackageRecommendation;
import com.acuver.fulfillment.shipment.binpack.model.PackageRecommendationRequest;
import com.acuver.fulfillment.shipment.binpack.model.PackageRecommendationResponse;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;


public class BinPackApplicationTests {

	@Test
	public void testPackageRecommendationPackage1() throws Exception {
		PackageRecommendationRequest req = new GsonBuilder().create().fromJson(
				Files.readString(Paths.get("src/test/resources/Test1.json")),
				PackageRecommendationRequest.class);
		PackageRecommendationResponse response = new PackageRecommendation().handleRequest(req, null);
		Assertions.assertTrue("package1".equals(response.getShipments().get(0).getRecommendedPackage().getId()));
	}


	@Test
	public void testPackageRecommendationOtherPackage() throws Exception {
		PackageRecommendationRequest req = new GsonBuilder().create().fromJson(
				Files.readString(Paths.get("src/test/resources/Test2.json")),
				PackageRecommendationRequest.class);
		PackageRecommendationResponse response = new PackageRecommendation().handleRequest(req, null);
		Assertions.assertTrue("other".equals(response.getShipments().get(0).getRecommendedPackage().getId()));
	}
}

