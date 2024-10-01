# Introduction
This documentation shows howto register an asset in the EDC and howto transfer an asset from one EDC to another.

The samples are the minimum definition of data that is possible. They can be enhanced for specific requirements.

# Infrastructure
## EDCs
There are two EDCs running in the msg infrastructure for testing purposes. The EDCs are running in the following URLs:
- https://edcdb-pr.gxfs.gx4fm.org/ (used as Producer)
- https://edcdb-co.gxfs.gx4fm.org/ (used as Consumer)

To access the EDCs, you need to have an API key (`x-api-key`). The API key is used as a header parameter in the requests. The API key is the same for both EDCs and can be requested from the msg / TUB team.

## Azure Storages
Those EDCs have associated azure storages both for the asset registration (on producer side) and for the data transfer (on consumer side).

### Storage and Containers
Name of the azure storage for asset registration: `msgedcstorage`
Available containers: 
- `src-container`
- `dest-container`
- `uc1-src`
- `uc1-dest`
- `uc2-src`
- `uc2-dest`
- `uc3-src`
- `uc3-dest`

> NOTE:
Please use the correct container for the asset registration (`*-src`) and data transfer (`*-dest`) that fits to your use case.


# Asset registration
## Upload an asset to the azure storage
TODO
pip install azure-storage-blob


## Create asset

## Create policy

## Create contract

# Data Transfer
## Retrieve contract information
### Base URL
`https://edc-pr.gxfs.gx4fm.org/management`
### Endpoint
`/v2/contractdefinitions/:id`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Accept: application/json`
### Method
`GET`
### Input payload
No payload
### Example response
```json
{
  "@id": "contract6",
  "@type": "edc:ContractDefinition",
  "edc:accessPolicyId": "1",
  "edc:contractPolicyId": "1",
  "edc:assetsSelector": {
    "@type": "edc:Criterion",
    "edc:operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
    "edc:operator": "=",
    "edc:operandRight": "bullwhip_result1"
  },
  "@context": {
    "tuberlin": "https://ise.tu.berlin/edc/v0.0.1/ns/",
    "dct": "https://purl.org/dc/terms/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "dcat": "https://www.w3.org/ns/dcat/",
    "odrl": "http://www.w3.org/ns/odrl/2/",
    "dspace": "https://w3id.org/dspace/v0.8/"
  }
}
```
### Hints
- The value of `:id` must be replaced with the value of the desired contract-id (normally stored in the self-description).
- The value of `edc:assetsSelector` -> `"edc:operandRight"` of a `"edc:operandLeft": "https://w3id.org/edc/v0.0.1/ns/id"` is the value that must be used in the next steps (`{{assetNameFull}}`).

## Contract negotiation
### Base URL
`https://edc-co.gxfs.gx4fm.org/management`
### Endpoint
`/v2/contractnegotiations`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Content-Type: application/json`
`Accept: application/json`
### Method
`POST`
### Input payload
```json
{
  "@context": {
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  },
  "@type": "NegotiationInitiateRequestDto",
  "connectorId": "edc_pr",
  "connectorAddress": "https://edc-pr.gxfs.gx4fm.org/api/v1/dsp",
  "consumerId": "edc_co",
  "providerId": "edc_pr",
  "protocol": "dataspace-protocol-http",
  "offer": {
   "offerId": "{{contractDefinitionId}}:{{assetNameFull}}:{{$randomUUID}}",
   "assetId": "{{assetNameFull}}",
   "policy": {
     "@type": "Set",
     "odrl:permission": [],
     "odrl:prohibition": [],
     "odrl:obligation": [],
     "odrl:target": "{{assetNameFull}}"
   }
  }
}
```
### Example response
```
{
    "@type": "edc:IdResponse",
    "@id": "0ba52804-732f-4857-98b7-cd47f27d930f",
    "edc:createdAt": 1727708368814,
    "@context": {
        "tuberlin": "https://ise.tu.berlin/edc/v0.0.1/ns/",
        "dct": "https://purl.org/dc/terms/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "dcat": "https://www.w3.org/ns/dcat/",
        "odrl": "http://www.w3.org/ns/odrl/2/",
        "dspace": "https://w3id.org/dspace/v0.8/"
    }
}
```
### Hints
- Be aware of the variables (e.g. `{{assetNameFull}}`) in the payload. They must be replaced with the correct values.
- Take the value of `@id` from the response and use it in the next steps (`{{ContractNegotiationUID}}`)

## Retrieve agreement
### Base URL
`https://edc-co.gxfs.gx4fm.org/management`
### Endpoint
`/v2/contractnegotiations/:id`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Accept: application/json`
### Method
`GET`
### Input payload
No payload
### Example response
```json
{
  "@type": "edc:ContractNegotiation",
  "@id": "0ba52804-732f-4857-98b7-cd47f27d930f",
  "edc:type": "CONSUMER",
  "edc:protocol": "dataspace-protocol-http",
  "edc:state": "FINALIZED",
  "edc:counterPartyId": "edc_pr",
  "edc:counterPartyAddress": "https://edc-pr.gxfs.gx4fm.org/api/v1/dsp",
  "edc:callbackAddresses": [],
  "edc:createdAt": 1727708368814,
  "edc:contractAgreementId": "d375dc66-2704-498b-9e43-b77fefd85d1f",
  "@context": {
    "tuberlin": "https://ise.tu.berlin/edc/v0.0.1/ns/",
    "dct": "https://purl.org/dc/terms/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "dcat": "https://www.w3.org/ns/dcat/",
    "odrl": "http://www.w3.org/ns/odrl/2/",
    "dspace": "https://w3id.org/dspace/v0.8/"
  }
}
```
### Hints
- The value of `:id` must be replaced with the value of `@id` from the previous step (`{{ContractNegotiationUID}}`).
- The value of `edc:contractAgreementId` is the value that must be used in the next steps (`{{ContractAgreementUID}}`).
- The state should be `FINALIZED`.

## Initiate data transfer
### Base URL
`https://edc-co.gxfs.gx4fm.org/management`
### Endpoint
`/v2/transferprocesses`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Content-Type: application/json`
`Accept: application/json`
### Method
`POST`
### Input payload
```json
{
    "@context": {
        "edc": "https://w3id.org/edc/v0.0.1/ns/"
    },
    "assetId": "{{assetNameFull}}",
    "connectorId": "edc_co",
    "contractId": "{{ContractAgreementUID}}",
    "dataDestination": {
        "type": "AzureStorage",
        "properties": {
            "container": "dest-container",
            "account": "msgedcstorage",
            "keyName": "msgedcstorage-sas"
        }
    },
    "@type": "TransferRequestDto",
    "connectorAddress": "https://edc-pr.gxfs.gx4fm.org/api/v1/dsp",
    "managedResources": false,
    "protocol": "dataspace-protocol-http"
}
```
### Example response
```json
{
    "@type": "edc:IdResponse",
    "@id": "f02626b2-1424-44b7-8ed0-3d76273b6360",
    "edc:createdAt": 1727708594759,
    "@context": {
        "tuberlin": "https://ise.tu.berlin/edc/v0.0.1/ns/",
        "dct": "https://purl.org/dc/terms/",
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "dcat": "https://www.w3.org/ns/dcat/",
        "odrl": "http://www.w3.org/ns/odrl/2/",
        "dspace": "https://w3id.org/dspace/v0.8/"
    }
}
```
### Hints
- The value of `@id` must be used in the next step (`{{transferprocessId}}`).

## Check transfer status
### Base URL
`https://edc-co.gxfs.gx4fm.org/management`
### Endpoint
`/v2/transferprocesses/:id/state`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Accept: application/json`
### Method
`GET`
### Input payload
No payload
### Example response
```json
{
  "@type": "edc:TransferState",
  "edc:state": "STARTED",
  "@context": {
    "tuberlin": "https://ise.tu.berlin/edc/v0.0.1/ns/",
    "dct": "https://purl.org/dc/terms/",
    "edc": "https://w3id.org/edc/v0.0.1/ns/",
    "dcat": "https://www.w3.org/ns/dcat/",
    "odrl": "http://www.w3.org/ns/odrl/2/",
    "dspace": "https://w3id.org/dspace/v0.8/"
  }
}
```
### Hints
- The value of `:id` must be replaced with the value of `@id` from the previous step (`{{transferprocessId}}`).
> NOTE: the current EDC version has a bug in setting the correct transfer status. The status remains in `STARTED` even if the transfer is completed. This will be fixed with a deployment of a newer version of the EDC.
> 