package api.apiAdminCommerce.converter;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class Base64ToMultipartFileConverter {

    public static MultipartFile convert(String base64, String originalFileName) throws IOException {
        String[] parts = base64.split(",");
        String base64Data = parts[1];
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

        String mimeType = parts[0].split(":")[1].split(";")[0];
        String extension = mimeType.split("/")[1];
        String fileName = originalFileName;

        if (!fileName.endsWith("." + extension)) {
            fileName += "." + extension;
        }

        return new MockMultipartFile(fileName, originalFileName, mimeType, decodedBytes);
    }

}
