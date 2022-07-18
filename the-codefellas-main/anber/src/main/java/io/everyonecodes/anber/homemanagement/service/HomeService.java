package io.everyonecodes.anber.homemanagement.service;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.repository.HomeRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;

    public HomeService(HomeRepository homeRepository, UserRepository userRepository) {
        this.homeRepository = homeRepository;
        this.userRepository = userRepository;
    }

    public List<Home> getHomes(String username) {

        Optional<User> oProfile = userRepository.findOneByEmail(username);
        if (oProfile.isPresent()) {
            User profile = oProfile.get();
            return profile.getSavedHomes();
        }
        else {
            return List.of();
        }
    }

    public List<Home> addHome(Home home, String username) {
        var oProfile = userRepository.findOneByEmail(username);
        List<Home> homes = new ArrayList<>();
        if (oProfile.isPresent()) {
            User profile = oProfile.get();
            homes = profile.getSavedHomes();
            if (!homes.contains(home)) {
                homes.add(home);
                profile.setSavedHomes(homes);
                homeRepository.save(home);
                userRepository.save(profile);
            }
        }
        return homes;
    }


    public List<Home> removeHome(Long id, String username) {
        var oProfile = userRepository.findOneByEmail(username);
        List<Home> homes = new ArrayList<>();
        if (oProfile.isPresent()) {
            User profile = oProfile.get();
            homes = profile.getSavedHomes();

            var oHome = homeRepository.findById(id);

            if (oHome.isPresent()) {
                Home home = oHome.get();
                if (homes.contains(home)) {
                    homes.remove(home);
                    homeRepository.delete(home);
                    profile.setSavedHomes(homes);
                    userRepository.save(profile);
                }
            }
        }
        return homes;
    }

    public void deleteHomes(String username) {

        homeRepository.deleteAll();

        var oProfile = userRepository.findOneByEmail(username);
        if (oProfile.isPresent()) {
            User profile = oProfile.get();

            profile.setSavedHomes(new ArrayList<>());
            userRepository.save(profile);
        }
    }

}
