# Introduction
This directory shows how to access azure storage with java in a very basic way. 

# Getting Started
You may want to use this as a starting point for your own project by reusing the code provided in this directory.

> Note to replace the `System.out.println("...")` with proper logging.

## Usage
### Prerequisites
#### Obtain SAS-Token from the msg team and set it as environment variable
bash:
```bash
export SAS_TOKEN="<your SAS token>"
```
powershell:
```powers
$env:SAS_TOKEN="<your SAS token>"
```
### List of all files in a given container
Implemented in the `ListFilesInContainer` class. 
### Upload a file to a given container in azure storage
Implemented in the `UploadAsset` class.

# Dependency
You will need to import the azure-storage-blob dependency into your project:
```xml
<!-- Azure Storage Blob dependency -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-blob</artifactId>
    <version>12.14.2</version>
</dependency>
```

