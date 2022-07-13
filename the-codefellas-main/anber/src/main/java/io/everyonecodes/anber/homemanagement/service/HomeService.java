package io.everyonecodes.anber.homemanagement.service;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.repository.HomeRepository;
import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final UserProfileRepository userProfileRepository;

    public HomeService(HomeRepository homeRepository, UserProfileRepository userProfileRepository) {
        this.homeRepository = homeRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public List<Home> getHomes() {
        return homeRepository.findAll();
    }

    public List<Home> addHome(Home home, String username) {
        var oProfile = userProfileRepository.findOneByUsername(username);
        List<Home> homes = new ArrayList<>();
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();
            homes = profile.getSavedHomes();
            if (!homes.contains(home)) {
                homes.add(home);
                homeRepository.save(home);
                profile.setSavedHomes(homes);
                userProfileRepository.save(profile);
            }
        }
        return homes;
    }

    public List<Home> removeHome(Home home, String username) {
        var oProfile = userProfileRepository.findOneByUsername(username);
        List<Home> homes = new ArrayList<>();
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();
            homes = profile.getSavedHomes();

            if (homes.contains(home)) {
                homes.remove(home);
                homeRepository.save(home);
                profile.setSavedHomes(homes);
                userProfileRepository.save(profile);
            }
        }
        return homes;
    }

    public void deleteHome(String username) {

        homeRepository.deleteAll();

        var oProfile = userProfileRepository.findOneByUsername(username);
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();

            profile.setSavedHomes(new ArrayList<>());
            userProfileRepository.save(profile);
        }
    }

}
