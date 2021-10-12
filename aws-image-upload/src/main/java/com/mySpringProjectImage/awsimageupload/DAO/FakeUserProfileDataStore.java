package com.mySpringProjectImage.awsimageupload.DAO;

import com.mySpringProjectImage.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("099f86bf-1602-4d32-8bfc-f86db100aff2"), "rafael", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("ce27934d-a9e3-4dd5-bad0-6e8bef84fc4e"), "junior", null));
    }

    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }

    public UserProfile getById(UUID userProfileId) {
        return USER_PROFILES.stream().filter(u -> u.getUserProfileId().equals(userProfileId)).findFirst().orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found!", userProfileId)));
    }
}
