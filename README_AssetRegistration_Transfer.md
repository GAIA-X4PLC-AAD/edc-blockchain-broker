# Introduction
This documentation shows howto register an asset in the EDC and howto transfer an asset from one EDC to another.

The samples are the minimum definition of data that is possible. They can be enhanced for specific requirements.

This documentation is based on the EDC version 0.3.1 and expects that the claim compliance provider (CCP) extension is running (see (here)[https://github.com/GAIA-X4PLC-AAD/EDC-Blockchain-Catalog/tree/main/extensions/claim-compliance-provider-integration].

# Infrastructure
## EDCs
There are two EDCs running in the msg infrastructure for testing purposes. The EDCs are running in the following URLs:
- https://edcdb-pr.gxfs.gx4fm.org/ (used as Producer)
- https://edcdb-co.gxfs.gx4fm.org/ (used as Consumer)

To access the EDCs, you need to have an API key (`x-api-key`). The API key is used as a header parameter in the requests. The API key is the same for both EDCs and can be requested from the msg / TUB team.

## Azure Storage
Those EDCs have associated azure storages both for the asset registration (on producer side) and for the data transfer (on consumer side).

### Storage and Containers
Name of the azure storage for asset registration: `msgedcstorage`
Available containers for asset registration: 
- `uc1-src`
- `uc2-src`
- `uc3-src`

Available containers for data transfer:
- `uc1-dest`
- `uc2-dest`
- `uc3-dest`

> NOTE:
Please use the correct container for the asset registration (`*-src`) and data transfer (`*-dest`) that fits to your use case.

To upload an asset to the azure storage, you need to have a **SAS token**. The SAS token can be requested from the msg team.
> NOTE: Please handle the SAS token with care! Do not share it with unauthorized persons.

# Asset registration
## Upload an asset to the azure storage
To programmatically upload an asset to the `msgedcstorage` you can use the provided **python** scripts. The scripts are located in the `scripts` directory next to this README file. 

There you can also find programming examples for **java** and **JavaScript**. 

Please refer to the readme files in the `scripts` directory for more information.

## Create asset in the provider EDC
### Base URL
`https://edcdb-pr.gxfs.gx4fm.org/management`
### Endpoint
`/v3/assets`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Content-Type: application/json`
`Accept: application/json`
### Method
`POST`
### Input payload
```json
{
	"@id": "{{assetNameFull}}",
	"properties": {
		"name": "{{assetNameFull}}",
		"version": "1.0",
		"contenttype": "text/json",
		"claimComplianceProviderResponse": "",
		"claimsList": "{{listOfClaimsAsBase64EncodedString}}",
		"gxParticipantCredentials": "{{listOfParticipantCredentialsAsBase64EncodedString}}"
	},
	"dataAddress": {
		"type": "AzureStorage",
		"name": "{{assetNameFull}}",
		"account": "msgedcstorage",
		"container": "{{AzureContainerID}}",
		"blobname": "{{Filename}}",
		"keyName": "msgedcstorage-key1"
	},
	"@context": {
		"edc": "https://w3id.org/edc/v0.0.1/ns/"
	}
}
```
### Example response
```json
{
    "@type": "edc:IdResponse",
    "@id": "bullwhip_result1",
    "edc:createdAt": 1727772367110,
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
- Note that you also can create an asset via the EDC UI (https://edcdb-pr.gxfs.gx4fm.org).
- Make sure to replace the variables in the payload with the correct values.

## Create policy in the provider EDC
### Base URL
`https://edcdb-pr.gxfs.gx4fm.org/management`
### Endpoint
`/v2/policydefinitions`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Content-Type: application/json`
`Accept: application/json`
### Method
`POST`
### Input payload
```json
{
	"policy": {
		"@type": "set",
		"@context": "http://www.w3.org/ns/odrl.jsonld"
	},
	"@id": "{{policyId}}",
	"@context": {
		"edc": "https://w3id.org/edc/v0.0.1/ns/"
	}
}
```
### Example response
```json
{
    "@type": "edc:IdResponse",
    "@id": "1",
    "edc:createdAt": 1727772413817,
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
- Note that you also can create an asset via the EDC UI (https://edcdb-pr.gxfs.gx4fm.org).
- Make sure to replace the variables in the payload with the correct values.
- Note that this is a very simple policy. You might want to extend it for your specific requirements.

## Create contract in the provider EDC
### Base URL
`https://edcdb-pr.gxfs.gx4fm.org/management`
### Endpoint
`v2/contractdefinitions`
### Header parameters
`x-api-key: {{API-KEY}}}`
`Content-Type: application/json`
`Accept: application/json`
### Method
`POST`
### Input payload
```json
{
	"@id": "{{contractDefinitionId}}",
	"assetsSelector": [
		{
			"operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
			"operator": "=",
			"operandRight": "{{assetNameFull}}"
		}
	],
	"accessPolicyId": "{{policyId}}",
	"contractPolicyId": "{{policyId}}",
	"@context": {
		"edc": "https://w3id.org/edc/v0.0.1/ns/"
	}
}
```
### Example response
```json
{
    "@type": "edc:IdResponse",
    "@id": "contract6",
    "edc:createdAt": 1727772440000,
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
- Note that you also can create an asset via the EDC UI (https://edcdb-pr.gxfs.gx4fm.org).
- Make sure to replace the variables in the payload with the correct values.

# Data transfer
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
- The value of `@id` is the value that must be used in the next steps (`{{contractDefinitionId}}`).

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