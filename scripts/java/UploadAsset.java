import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobStorageException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class UploadAsset {

    public static void uploadFileToBlob(String filePath, String sasToken, String containerName) {
        try {
            // Extract the file name from the file path
            String fileName = Paths.get(filePath).getFileName().toString();

            // Create a Blob Service Client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint("https://msgedcstorage.blob.core.windows.net")
                    .sasToken(sasToken)
                    .buildClient();

            // Create a Blob Client
            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);

            // Upload the file
            System.out.println("Uploading file " + fileName + " to the container...");
            blobClient.upload(new FileInputStream(new File(filePath)), new File(filePath).length(), true);

            System.out.println("File " + filePath + " uploaded successfully.");
        } catch (BlobStorageException | IOException e) {
            System.out.println("Error uploading the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java UploadAsset <local_file_path_of_asset> <source_container_name>");
            System.exit(100);
        }

        // Path to the file to be uploaded
        String filePath = args[0];

        // Container name from command line argument
        String containerName = args[1];

        // Retrieve SAS token from environment variable
        String sasToken = System.getenv("SAS_TOKEN");
        if (sasToken == null) {
            System.out.println("SAS token not found in environment variable SAS_TOKEN.");
            System.exit(101);
        }

        // Check if the file path exists
        if (!new File(filePath).exists()) {
            System.out.println("File path does not exist: " + filePath);
            System.exit(102);
        }

        // Upload the file to the container
        uploadFileToBlob(filePath, sasToken, containerName);
    }
}