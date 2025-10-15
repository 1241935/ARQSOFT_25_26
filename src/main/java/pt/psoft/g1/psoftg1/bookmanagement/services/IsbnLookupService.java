package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class IsbnLookupService implements LibraryApi {
    private final List<LibraryApi> providers;

    public IsbnLookupService(List<LibraryApi> providers) {
        this.providers = providers;
    }

    @Override
    public Optional<String> findIsbnByTitle(String title) {
        for (LibraryApi provider : providers) {
            try {
                Optional<String> isbn = provider.findIsbnByTitle(title);
                if (isbn.isPresent()) return isbn; // retorna ao primeiro sucesso
            } catch (Exception e) {
                // log e fallback para o próximo provider
            }
        }
        return Optional.empty();
    }

    @Override
    public String getProviderName() {
        return "";
    }
}
