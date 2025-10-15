package pt.psoft.g1.psoftg1.bookmanagement.services;

import java.util.Optional;

public interface LibraryApi {

    Optional<String> findIsbnByTitle(String title);
    String getProviderName();
}
