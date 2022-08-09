package io.everyonecodes.anber.calculationmanagement;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
