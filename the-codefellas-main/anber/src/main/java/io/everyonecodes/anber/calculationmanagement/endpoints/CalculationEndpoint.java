package io.everyonecodes.anber.calculationmanagement.endpoints;

import io.everyonecodes.anber.calculationmanagement.service.CalculationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calculate")
public class CalculationEndpoint {
    private final CalculationService calculationService;

    public CalculationEndpoint(CalculationService calculationService) {
        this.calculationService = calculationService;
    }


    @GetMapping("/{username}/{id}")
    @Secured("ROLE_USER")
    String getAverageConsumption(@PathVariable String username, @PathVariable Long id){
        var calculationAverageForHomeID = calculationService.getAverageConsumption(username,id).orElse(null);
        return "The average Consumption for Home ID " + id + " in it's Country is " + calculationAverageForHomeID;
    }

    @GetMapping("/{username}/{homeID}/{tariffID}")
    @Secured("ROLE_USER")
    String getAverageConsumptionForTariff (@PathVariable String username, @PathVariable Long homeID, @PathVariable Long tariffID){
        var calculationForHomeAndTariff = calculationService.getAverageConsumptionForTariff(username,homeID,tariffID).orElse(null);
        return "The average Consumption for Home ID " + homeID + " with Tariff ID " + tariffID + " is " + calculationForHomeAndTariff;
    }

    @GetMapping("/{username}/getAll")
    @Secured("ROLE_USER")
    List<Map<String, Double>> getAverageForAllHomes(@PathVariable String username) {
        return calculationService.getAverageConsumptionForAllHomes(username);
    }

    @PostMapping("/data/{username}/{id}")
    @Secured("ROLE_USER")
    String getAverageConsumptionForGivenData(@PathVariable String username, @PathVariable Long id, @RequestBody String squareMeter) {
        var calculationForGivenData = calculationService.getAverageConsumptionForData(username, id, squareMeter).orElse(null);
        return "The average Consumption for the Tariff with ID " + id + " with your Input of " + squareMeter + " is " + calculationForGivenData;
    }
}
