package com.mySpringProjectImage.awsimageupload.service;

import com.amazonaws.services.s3.model.Bucket;
import com.mySpringProjectImage.awsimageupload.DAO.UserProfileDAO;
import com.mySpringProjectImage.awsimageupload.buckets.BucketName;
import com.mySpringProjectImage.awsimageupload.fileStore.FileStore;
import com.mySpringProjectImage.awsimageupload.profile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {

    private final UserProfileDAO userProfileDAO;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDAO userProfileDAO, FileStore fileStore) {
        this.userProfileDAO = userProfileDAO;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileDAO.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) throws IOException {
        validateFile(file);

        UserProfile userProfile = getUserProfileOrThrow(userProfileId);

        Map<String, String> metadata = new HashMap<>();
        metadata.put(("Content-Type"), file.getContentType());
        metadata.put(("Content-Length"), String.valueOf(file.getSize()));

        String bucketPath = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfile.getUserProfileId());
        String fileName = String.format(("%s-%s"), file.getOriginalFilename(), UUID.randomUUID());

        fileStore.save(bucketPath, fileName, Optional.of(metadata), file.getInputStream());
        userProfile.setUserProfileImageLink(fileName);

    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return userProfileDAO.getByID(userProfileId);
    }

    private void validateFile(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalStateException("Cannot upload empty files!");
        }
        if(!Arrays.asList(
                IMAGE_PNG.getMimeType(),
                IMAGE_JPEG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("Cannot upload non image files!");
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile userProfile = getUserProfileOrThrow(userProfileId);

        String fullPath = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                userProfile.getUserProfileId());

        return userProfile.getUserProfileImageLink()
                .map(key -> fileStore.download(fullPath, key)).orElse(new byte[0]);
    }
}
