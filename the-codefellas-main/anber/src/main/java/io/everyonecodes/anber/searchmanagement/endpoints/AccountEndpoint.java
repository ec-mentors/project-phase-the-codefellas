package io.everyonecodes.anber.searchmanagement.endpoints;

import io.everyonecodes.anber.searchmanagement.data.UnverifiedAccount;
import io.everyonecodes.anber.searchmanagement.data.VerifiedAccount;
import io.everyonecodes.anber.searchmanagement.service.AccountService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider")
public class AccountEndpoint {

    private final AccountService accountService;

    public AccountEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    List<VerifiedAccount> getAll() {
        return accountService.getAll();
    }

    @PostMapping("/{id}/create")
    @Secured("ROLE_ADMIN")
    UnverifiedAccount createAccount(@PathVariable Long id) {
        return accountService.createAccount(id).orElse(null);
    }


    @PutMapping("/{id}/verify")
    @Secured("ROLE_ADMIN")
    VerifiedAccount verifyAccount(@PathVariable Long id) {
        return accountService.verifyAccount(id).orElse(null);
    }

    @PutMapping("/{id}/unverify")
    @Secured("ROLE_ADMIN")
    UnverifiedAccount unverifyAccount(@PathVariable Long id) {
        return accountService.unverifyAccount(id).orElse(null);
    }

    @PutMapping("/v/{id}/edit/{property}")
    @Secured("ROLE_ADMIN")
    VerifiedAccount editAccountV(@PathVariable Long id, @PathVariable String property, @RequestBody String input) {
        return accountService.editAccountV(id, property, input).orElse(null);
    }

    @PutMapping("/u/{id}/edit/{property}")
    @Secured("ROLE_ADMIN")
    UnverifiedAccount editAccountU(@PathVariable Long id, @PathVariable String property, @RequestBody String input) {
        return accountService.editAccountU(id, property, input).orElse(null);
    }

    @DeleteMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}
