package online.strongnation.unit.service;

import online.strongnation.repository.RegionRepository;
import online.strongnation.service.RegionService;
import online.strongnation.service.implementation.RegionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository repository;
    private RegionService service;
    private static final String id = "testID";

    @BeforeEach
    void setUp() {
//        service = new RegionServiceImpl(repository);
    }

    @Test
    void addNewRegion() {
        String name = "Rivne";
    }

    @Test
    void hardAddRegion() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByName() {
    }

    @Test
    void hardUpdateRegionById() {
    }

    @Test
    void hardUpdateRegionByName() {
    }

    @Test
    void hardAddNewCategoryById() {
    }

    @Test
    void hardAddNewCategoryName() {
    }

    @Test
    void deleteRegionById() {
    }

    @Test
    void deleteRegionByName() {
    }
}