package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.shared.api.UploadFileResponse;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("FileUtils Utility Class Tests")
class FileUtilsTest {

    private MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        // Setup mock HTTP request context before each test
        mockRequest = new MockHttpServletRequest();
        mockRequest.setScheme("http");
        mockRequest.setServerName("localhost");
        mockRequest.setServerPort(8080);
        mockRequest.setRequestURI("/api/photos");
        mockRequest.setContextPath("");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
    }

    @AfterEach
    void tearDown() {
        // Clean up request context after each test
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("When valid parameters are provided, then file upload succeeds")
    void whenValidParametersProvided_thenFileUploadSucceeds() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user123";
        String fileName = "test.jpg";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", "test content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertNotNull(response);
        assertEquals(fileName, response.getFileName());
        verify(mockFileStorageService).storeFile(id, mockFile);
    }

    @Test
    @DisplayName("When file storage service stores file, then correct file name is returned")
    void whenFileStorageServiceStoresFile_thenCorrectFileNameReturned() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user456";
        String expectedFileName = "photo123.png";
        MultipartFile mockFile = new MockMultipartFile("file", "original.png", "image/png", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(expectedFileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertEquals(expectedFileName, response.getFileName());
    }

    @Test
    @DisplayName("When null fileStorageService is provided, then throws Exception")
    void whenNullFileStorageServiceProvided_thenThrowsException() {
        // Arrange
        String expectedMessage = "Could not get reference of fileStorageService, id or file";
        String id = "user123";
        MultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());

        // Act + Assert
        Exception exception = assertThrows(Exception.class, () ->
                FileUtils.doUploadFile(null, id, mockFile)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When null id is provided, then throws Exception")
    void whenNullIdProvided_thenThrowsException() {
        // Arrange
        String expectedMessage = "Could not get reference of fileStorageService, id or file";
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        MultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());

        // Act + Assert
        Exception exception = assertThrows(Exception.class, () ->
                FileUtils.doUploadFile(mockFileStorageService, null, mockFile)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When null file is provided, then throws Exception")
    void whenNullFileProvided_thenThrowsException() {
        // Arrange
        String expectedMessage = "Could not get reference of fileStorageService, id or file";
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user123";

        // Act + Assert
        Exception exception = assertThrows(Exception.class, () ->
                FileUtils.doUploadFile(mockFileStorageService, id, null)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When all parameters are null, then throws Exception")
    void whenAllParametersAreNull_thenThrowsException() {
        // Arrange
        String expectedMessage = "Could not get reference of fileStorageService, id or file";

        // Act + Assert
        Exception exception = assertThrows(Exception.class, () ->
                FileUtils.doUploadFile(null, null, null)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When file with different content type is provided, then upload succeeds")
    void whenFileWithDifferentContentTypeProvided_thenUploadSucceeds() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user789";
        String fileName = "document.pdf";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "application/pdf", "pdf content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertNotNull(response);
        assertEquals("application/pdf", response.getFileType());
    }

    @Test
    @DisplayName("When file size is retrieved, then correct size is returned")
    void whenFileSizeRetrieved_thenCorrectSizeReturned() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user999";
        String fileName = "large.jpg";
        byte[] content = new byte[1024];
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", content);

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertEquals(1024L, response.getSize());
    }

    @Test
    @DisplayName("When download URI is generated, then contains correct filename")
    void whenDownloadURIGenerated_thenContainsCorrectFilename() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user555";
        String fileName = "avatar.jpg";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertTrue(response.getFileDownloadUri().contains(fileName));
    }

    @Test
    @DisplayName("When upload URI contains photos, then it is replaced with photo")
    void whenUploadURIContainsPhotos_thenReplacedWithPhoto() throws Exception {
        // Arrange
        mockRequest.setRequestURI("/api/photos"); // Set URI with "photos"
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user777";
        String fileName = "image.jpg";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertFalse(response.getFileDownloadUri().contains("/photos/"));
        assertTrue(response.getFileDownloadUri().contains("/photo/"));
    }

    @Test
    @DisplayName("When empty file is uploaded, then upload succeeds")
    void whenEmptyFileUploaded_thenUploadSucceeds() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user000";
        String fileName = "empty.txt";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "text/plain", new byte[0]);

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertNotNull(response);
        assertEquals(0L, response.getSize());
    }

    @Test
    @DisplayName("When file with long name is uploaded, then upload succeeds")
    void whenFileWithLongNameUploaded_thenUploadSucceeds() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user111";
        String longFileName = "a".repeat(100) + ".jpg";
        MultipartFile mockFile = new MockMultipartFile("file", longFileName, "image/jpeg", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(longFileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertEquals(longFileName, response.getFileName());
    }

    @Test
    @DisplayName("When file with special characters in name is uploaded, then upload succeeds")
    void whenFileWithSpecialCharactersUploaded_thenUploadSucceeds() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user222";
        String specialFileName = "my-file_2024(1).jpg";
        MultipartFile mockFile = new MockMultipartFile("file", specialFileName, "image/jpeg", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(specialFileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertEquals(specialFileName, response.getFileName());
    }

    @Test
    @DisplayName("When storeFile is called, then service method is invoked")
    void whenStoreFileCalled_thenServiceMethodInvoked() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user333";
        String fileName = "verify.jpg";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        verify(mockFileStorageService, times(1)).storeFile(id, mockFile);
    }

    @Test
    @DisplayName("When response contains correct content type, then file type matches")
    void whenResponseContainsCorrectContentType_thenFileTypeMatches() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user444";
        String fileName = "test.jpg";
        String contentType = "image/jpeg";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, contentType, "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertEquals(contentType, response.getFileType());
    }

    @Test
    @DisplayName("When response file download URI is generated, then contains server info")
    void whenResponseFileDownloadURIGenerated_thenContainsServerInfo() throws Exception {
        // Arrange
        FileStorageService mockFileStorageService = mock(FileStorageService.class);
        String id = "user555";
        String fileName = "image.jpg";
        MultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", "content".getBytes());

        when(mockFileStorageService.storeFile(id, mockFile)).thenReturn(fileName);

        // Act
        UploadFileResponse response = FileUtils.doUploadFile(mockFileStorageService, id, mockFile);

        // Assert
        assertTrue(response.getFileDownloadUri().startsWith("http://"));
        assertTrue(response.getFileDownloadUri().contains("localhost"));
    }
}
