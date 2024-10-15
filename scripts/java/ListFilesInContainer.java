import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobItem;

public class ListFilesInContainer {

    public static void listFilesInContainer(String sasToken, String containerName) {
        try {
            // Create a Blob Service Client
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint("https://msgedcstorage.blob.core.windows.net")
                    .sasToken(sasToken)
                    .buildClient();

            // Create a Container Client
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // List all blobs in the container
            System.out.println("Files in container '" + containerName + "':");
            for (BlobItem blobItem : containerClient.listBlobs()) {
                System.out.println(blobItem.getName());
            }
        } catch (Exception e) {
            System.out.println("Error listing files in the container: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ListFilesInContainer <container_name>");
            System.exit(100);
        }

        // Container name from command line argument
        String containerName = args[0];

        // Retrieve SAS token from environment variable
        String sasToken = System.getenv("SAS_TOKEN");
        if (sasToken == null) {
            System.out.println("SAS token not found in environment variable SAS_TOKEN.");
            System.exit(101);
        }

        // List files in the container
        listFilesInContainer(sasToken, containerName);
    }
}