package io.everyonecodes.anber.homemanagement.service;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.repository.HomeRepository;
import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final UserProfileRepository userProfileRepository;

    public HomeService(HomeRepository homeRepository, UserProfileRepository userProfileRepository) {
        this.homeRepository = homeRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public List<Home> getHomes(String username) {

        Optional<UserProfile> oProfile = userProfileRepository.findOneByEmail(username);
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();
            return profile.getSavedHomes();
        }
        else {
            return List.of();
        }
    }

    public List<Home> addHome(Home home, String username) {
        var oProfile = userProfileRepository.findOneByEmail(username);
        List<Home> homes = new ArrayList<>();
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();
            homes = profile.getSavedHomes();
            if (!homes.contains(home)) {
                homes.add(home);
                profile.setSavedHomes(homes);
                homeRepository.save(home);
                userProfileRepository.save(profile);
            }
        }
        return homes;
    }


    public List<Home> removeHome(Long id, String username) {
        var oProfile = userProfileRepository.findOneByEmail(username);
        List<Home> homes = new ArrayList<>();
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();
            homes = profile.getSavedHomes();

            var oHome = homeRepository.findById(id);

            if (oHome.isPresent()) {
                Home home = oHome.get();
                if (homes.contains(home)) {
                    homes.remove(home);
                    homeRepository.delete(home);
                    profile.setSavedHomes(homes);
                    userProfileRepository.save(profile);
                }
            }
        }
        return homes;
    }

    public void deleteHomes(String username) {

        homeRepository.deleteAll();

        var oProfile = userProfileRepository.findOneByEmail(username);
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();

            profile.setSavedHomes(new ArrayList<>());
            userProfileRepository.save(profile);
        }
    }

}
