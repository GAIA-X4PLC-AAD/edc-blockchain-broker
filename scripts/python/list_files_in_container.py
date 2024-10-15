from azure.storage.blob import BlobServiceClient
import os
import sys


# Function to list all files in the Blob storage container
def list_files_in_container(sas_token, container_name):
    try:
        # Create a Blob Service Client
        blob_service_client = BlobServiceClient(account_url="https://msgedcstorage.blob.core.windows.net",
                                                credential=sas_token)

        # Create a Container Client
        container_client = blob_service_client.get_container_client(container_name)

        # List all blobs in the container
        blobs_list = container_client.list_blobs()
        print(f"Files in container '{container_name}':")
        for blob in blobs_list:
            print(blob.name)

    except Exception as e:
        print(f"Error listing files in the container: {e}")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python list_files.py <container_name>")
        sys.exit(100)

    # Container name from command line argument
    container_name = sys.argv[1]

    # Retrieve SAS token from environment variable
    sas_token = os.getenv('SAS_TOKEN')
    if not sas_token:
        print("SAS token not found in environment variable SAS_TOKEN.")
        sys.exit(101)

    # List files in the container
    list_files_in_container(sas_token, container_name)
