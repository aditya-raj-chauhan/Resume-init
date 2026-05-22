package com.devotee.resume.init.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "resume-collection")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Resume {
    @Id
    @JsonProperty("_id")

    private String id;
    private String userId;
    private String title;
    private String thumbnailLink;
    private Template template;
     private ProfileInfo profileInfo;
     private ContactInfo contactInfo;
     private List<WorkExperience>workExperiences;
     private List<Education>education;
     private List<Skill >skills;
     private List<Project >projects;
     private List<Certifications>certifications;
     private List<Language>languages;
     private List<String>intrest;
     @CreatedDate
     private LocalDateTime createdAt;
     @LastModifiedDate
     private LocalDateTime updatedAt;
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  Template{
        private String theme;
        private List<String>colorPalate;
    }
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  ProfileInfo{
        private String profilePreviewUrl;
        private String fullname;
        private String designation;
        private String summary;
    }
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  ContactInfo{
        private String email;
        private String phoneNumber;
        private String location;
        private String linkdIn;
        private String github;
        private String website;

    }

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  WorkExperience{
        private String company;
        private String role;
        private String startDate;
        private String endDate;
        private String description;

    }
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  Education{
        private String degree;
        private String institution;
        private String startDate;
        private String endDate;

    }
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  Skill{
        private String name;
        private Integer progress ;


    }
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  Project{
        private String title;
        private Integer description ;
        private Integer githubLink ;
        private Integer liveDemo ;



    }
    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  Certifications{
        private String title;
        private Integer issuer ;
        private Integer year ;



    }@Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static  class  Language{
        private String title;
        private Integer name ;
        private Integer progress ;



    }

}
