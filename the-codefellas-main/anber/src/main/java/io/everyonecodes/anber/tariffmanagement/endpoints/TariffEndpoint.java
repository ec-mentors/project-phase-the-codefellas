package io.everyonecodes.anber.tariffmanagement.endpoints;

import io.everyonecodes.anber.tariffmanagement.service.TariffService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class TariffEndpoint {

    private final TariffService tariffService;

    public TariffEndpoint(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PutMapping("/update/tariff/{id}")
    @Secured("ROLE_ADMIN")
    void updateTariff(@PathVariable Long id) {
        tariffService.tariffApplier(id);
    }
}
