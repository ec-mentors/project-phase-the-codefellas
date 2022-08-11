package io.everyonecodes.anber.calculationmanagement;

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
    Double getAverageConsumption(@PathVariable String username, @PathVariable Long id){
        return calculationService.getAverageConsumption(username,id).orElse(null);
    }

    @GetMapping("/{username}/{homeID}/{tariffID}")
    @Secured("ROLE_USER")
    Double getAverageConsumptionForTariff (@PathVariable String username, @PathVariable Long homeID, @PathVariable Long tariffID){
        return calculationService.getAverageConsumptionForTariff(username,homeID,tariffID).orElse(null);
    }

    @GetMapping("/{username}/getAll")
    @Secured("ROLE_USER")
    List<Map<String, Double>> getAverageForAllHomes(@PathVariable String username) {
        return calculationService.getAverageConsumptionForAllHomes(username);
    }

    @PostMapping("/data/{username}/{id}")
    @Secured("ROLE_USER")
    Double getAverageConsumptionForGivenData(@PathVariable String username, @PathVariable Long id, @RequestBody String squareMeter) {
        return calculationService.getAverageConsumptionForData(username, id, squareMeter).orElse(null);
    }
}
