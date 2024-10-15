from azure.storage.blob import BlobServiceClient
from azure.core.exceptions import ResourceExistsError
import os
import sys


# Function to upload a file to the Blob storage
def upload_file_to_blob(file_path, sas_token, container_name):
    try:
        # Extract the file name from the file_path
        file_name = os.path.basename(file_path)

        # Create a Blob Service Client
        blob_service_client = BlobServiceClient(account_url="https://msgedcstorage.blob.core.windows.net",
                                                credential=sas_token)

        # Create a Container Client
        container_client = blob_service_client.get_container_client(container_name)

        # Create a Blob Client
        blob_client = container_client.get_blob_client(blob=file_name)

        # Upload the file
        print(f"Uploading file {file_name} to the container...")
        with open(file_path, "rb") as data:
            blob_client.upload_blob(data)

        print(f"File {file_path} uploaded successfully.")

    except ResourceExistsError:
        print("The file already exists in the container.")
    except Exception as e:
        print(f"Error uploading the file: {e}")


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python upload_asset.py <local_file_path_of_asset> <source_container_name>")
        sys.exit(100)

    # Path to the file to be uploaded
    file_path = sys.argv[1]

    # Container name from command line argument
    container_name = sys.argv[2]

    # Retrieve SAS token from environment variable
    sas_token = os.getenv('SAS_TOKEN')
    if not sas_token:
        print("SAS token not found in environment variable SAS_TOKEN.")
        sys.exit(101)

    # Check if the file_path exists
    if not os.path.exists(file_path):
        print(f"File path does not exist: {file_path}")
        sys.exit(102)

    # Upload the file to the container
    upload_file_to_blob(file_path, sas_token, container_name)
