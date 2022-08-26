package io.everyonecodes.anber.calculationmanagement.endpoints;

import io.everyonecodes.anber.calculationmanagement.service.CalculationService;
import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calculate")
public class CalculationEndpoint {

    private final CalculationService calculationService;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;

    public CalculationEndpoint(CalculationService calculationService,
                               UserRepository userRepository,
                               TariffRepository tariffRepository) {
        this.calculationService = calculationService;
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
    }


    @GetMapping("/{username}/{id}")
    @Secured("ROLE_USER")
    String getAverageConsumption(@PathVariable String username, @PathVariable Long id){
        var calculationAverageForHomeID = calculationService.getAverageConsumption(username,id).orElse(null);

        var home = getHome(username, id);
        if (home != null && calculationAverageForHomeID != null) {
            return "The average energy consumption cost for Home '" + home.getHomeName() + "' (ID " + id + ") in its Country " + home.getCountry() + " is " + (Math.round(calculationAverageForHomeID * 100.00) / 100.00) + " €.";

        }
        return "The average Consumption for Home ID " + id + " in its Country is " + calculationAverageForHomeID + " €.";
    }

    @GetMapping("/{username}/{homeID}/{tariffID}")
    @Secured("ROLE_USER")
    String getAverageConsumptionForTariff (@PathVariable String username, @PathVariable Long homeID, @PathVariable Long tariffID){
        var calculationForHomeAndTariff = calculationService.getAverageConsumptionForTariff(username,homeID,tariffID).orElse(null);

        var home = getHome(username, homeID);
        var tariff = getTariff(tariffID);

        if (home != null && tariff != null && calculationForHomeAndTariff != null) {
            return "The average enegry consumption cost for Home '" + home.getHomeName() + "' (ID " + homeID + ") with Tariff '" + tariff.getTariffName() + "' (ID " + tariffID + ") is " + (Math.round(calculationForHomeAndTariff * 100.00) / 100.00) + " €.";
        }

        return "The average Consumption for Home ID " + homeID + " with Tariff ID " + tariffID + " is " + calculationForHomeAndTariff + " €.";
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

        var tariff = getTariff(id);

        if (calculationForGivenData != null) {
            return "The average energy consumption costs for the Tariff " + tariff.getTariffName() + " (ID " + id + ") with your Input of " + squareMeter + "m² is " + (Math.round(calculationForGivenData * 100.00) / 100.00) + " €.";
        }
        return "Operation failed due to insufficient calculation values.";
    }


    private Home getHome(String username, Long id) {
        var user = userRepository.findOneByEmail(username).orElse(null);
        if (user != null) {
            var oHome = user.getSavedHomes().stream()
                    .filter(home -> home.getId().equals(id))
                    .findFirst();

            if (oHome.isPresent()) {
                return oHome.get();
            }
        }
        return null;
    }

    private Tariff getTariff(Long id) {
        var oTariff = tariffRepository.findById(id);
        return oTariff.orElse(null);
    }

}
