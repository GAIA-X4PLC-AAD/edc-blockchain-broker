const { BlobServiceClient } = require('@azure/storage-blob');
const fs = require('fs');
const path = require('path');

async function uploadFileToBlob(filePath, sasToken, containerName) {
    try {
        const fileName = path.basename(filePath);

        const blobServiceClient = new BlobServiceClient(`https://msgedcstorage.blob.core.windows.net?${sasToken}`);
        const containerClient = blobServiceClient.getContainerClient(containerName);
        const blobClient = containerClient.getBlobClient(fileName);

        console.log(`Uploading file ${fileName} to the container...`);
        const data = fs.readFileSync(filePath);
        await blobClient.uploadData(data);

        console.log(`File ${filePath} uploaded successfully.`);
    } catch (error) {
        console.error(`Error uploading the file: ${error.message}`);
    }
}

if (process.argv.length !== 4) {
    console.log('Usage: node upload_asset.js <local_file_path_of_asset> <source_container_name>');
    process.exit(100);
}

const filePath = process.argv[2];
const containerName = process.argv[3];
const sasToken = process.env.SAS_TOKEN;

if (!sasToken) {
    console.error('SAS token not found in environment variable SAS_TOKEN.');
    process.exit(101);
}

if (!fs.existsSync(filePath)) {
    console.error(`File path does not exist: ${filePath}`);
    process.exit(102);
}

uploadFileToBlob(filePath, sasToken, containerName);