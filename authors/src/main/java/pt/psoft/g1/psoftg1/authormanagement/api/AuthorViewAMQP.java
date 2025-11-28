package pt.psoft.g1.psoftg1.authormanagement.api;

import lombok.Data;

@Data
public class AuthorViewAMQP {
    private Long authorNumber;
    private String name;
    private String bio;
    private String photoURI;
    private Long version;
} 