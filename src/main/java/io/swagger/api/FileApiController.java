package io.swagger.api;

import io.swagger.model.DataFile;
import io.swagger.model.DataFileProperties;
import io.swagger.model.DataFilePropertiesResponse;

import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.DataFilePropertiesResponseAnswers;
import org.intermine.configurator.DataFileManager;
import org.intermine.configurator.MineConfigManager;
import org.intermine.configurator.ValidationResponse;
import org.intermine.configurator.source.project.AbstractSource;
import org.intermine.configurator.source.project.SourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-25T11:51:24.013Z[GMT]")
@Controller
public class FileApiController implements FileApi {

    private static final Logger log = LoggerFactory.getLogger(FileApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public FileApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<DataFileProperties> detectFileProperties(@ApiParam(value = "File that needs to be identified." ,required=true )  @Valid @RequestBody DataFile body,@NotNull @ApiParam(value = "ID of user who owns this mine", required = true) @Valid @RequestParam(value = "userId", required = true) UUID userId,@NotNull @ApiParam(value = "ID of mine", required = true) @Valid @RequestParam(value = "mineId", required = true) UUID mineId) {
        String accept = request.getHeader("Accept");
        if (!MineConfigManager.isValid(mineId, userId)) {
            return new ResponseEntity("Mine Config not found", HttpStatus.BAD_REQUEST);
        }
        DataFile dataFile = (DataFile) body;
        UUID fileId = dataFile.getFileId();

        String fileLocation = getFilePath(mineId.toString(), userId.toString(), fileId.toString(), System.getenv("IM_DATA_DIR"));

        ValidationResponse validationResponse = DataFileManager.processDataFile(mineId, userId, dataFile, fileLocation);
        if (validationResponse.isValid) {
            return new ResponseEntity(validationResponse.dataFileProperties, HttpStatus.OK);
        } else {
            return new ResponseEntity(validationResponse.errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Void> saveFileProperties(@ApiParam(value = "File that needs to be identified." ,required=true )  @Valid @RequestBody DataFilePropertiesResponse body,@NotNull @ApiParam(value = "ID of mine to fetch", required = true) @Valid @RequestParam(value = "mineId", required = true) UUID mineId,@NotNull @ApiParam(value = "ID of user who owns this mine", required = true) @Valid @RequestParam(value = "userId", required = true) UUID userId) {
        String accept = request.getHeader("Accept");

        if (!MineConfigManager.isValid(mineId, userId)) {
            return new ResponseEntity("Mine Config not found", HttpStatus.BAD_REQUEST);
        }

        DataFile dataFile = (DataFile) body.getDataFile();
        DataFile.FileFormatEnum fileFormatEnum = dataFile.getFileFormat();
        UUID fileId = dataFile.getFileId();

        String fileLocation = getFilePath(mineId.toString(), userId.toString(), fileId.toString(), System.getenv("IM_DATA_DIR"));

        // set the user config
        ValidationResponse validationResponse = DataFileManager.processDataFile(mineId, userId, dataFile, fileLocation);
        if (validationResponse.isValid) {

            DataFileProperties dataFileProperties = validationResponse.dataFileProperties;

            // add answers
            List<DataFilePropertiesResponseAnswers> answers = body.getAnswers();
            MineConfigManager.setSelectedAnswers(dataFileProperties, answers);

            MineConfigManager.addFileProperties(mineId, userId, fileId, dataFileProperties);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(validationResponse.errorMessage, HttpStatus.BAD_REQUEST);
    }

    private static String getFilePath(String userId, String mineId, String fileId, String baseDir) {
        return baseDir + "/" + userId + "/" + mineId + "/" + fileId + "/";
    }
}