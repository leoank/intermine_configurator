package io.swagger.api;

import io.swagger.model.SupplementaryDataSource;
import io.swagger.model.SupplementaryDataSourcesResponse;
import java.util.UUID;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplementaryDataSourcesApiControllerIntegrationTest {

    @Autowired
    private SupplementaryDataSourcesApi api;

    @Test
    public void getSupplementaryDataSourcesTest() throws Exception {
        UUID mineId = java.util.UUID.randomUUID();
        UUID userId = java.util.UUID.randomUUID();
        ResponseEntity<List<SupplementaryDataSource>> responseEntity = api.getSupplementaryDataSources(mineId, userId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void setSupplementaryDataSourcesTest() throws Exception {
        UUID mineId = java.util.UUID.randomUUID();
        UUID userId = java.util.UUID.randomUUID();
        ResponseEntity<SupplementaryDataSourcesResponse> responseEntity = api.setSupplementaryDataSources(mineId, userId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}