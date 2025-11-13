package se.nackademin.devops24.pingurl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import se.nackademin.devops24.pingurl.model.PingedURL;
import se.nackademin.devops24.pingurl.repository.MemoryURLRepository;

class MemoryURLRepositoryTest {

    @Test
    void saveShouldAddUrl() {
        MemoryURLRepository repo = new MemoryURLRepository();

        repo.save("DN", "https://www.dn.se");

        // Ett enda objekt ska vara sparat
        assertEquals(1, repo.getUrls().size());
    }

    @Test
    void saveShouldNotAllowDuplicateNames() {
        MemoryURLRepository repo = new MemoryURLRepository();

        repo.save("DN", "https://www.dn.se");

        // spara samma namn igen → ska kasta IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            repo.save("DN", "https://example.com");
        });
    }

    @Test
    void deleteShouldRemoveUrl() {
        MemoryURLRepository repo = new MemoryURLRepository();  // <-- behövs
        repo.save("DN", "..."); 
        repo.delete("DN");
        assertEquals(0, repo.getUrls().size());
    }

    @Test
    void updateShouldModifyExistingUrl() {
        MemoryURLRepository repo = new MemoryURLRepository();  // <-- behövs
        repo.save("DN", "old");
        repo.update(new PingedURL().setName("DN").setUrl("new"));
        assertEquals("new", repo.getUrls().iterator().next().getUrl());
    }


}
