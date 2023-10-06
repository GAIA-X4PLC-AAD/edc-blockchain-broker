# EDC Blockchain Broker

## Description

### Introduction

**Please note: This repository does not contain production-grade code and is only intended for demonstration purposes.**

**This Repository was created to work for the specific demonstration of connecting the EDC to the tezos-edc-interface and the edc dashboard**

We provide an extension that uses the [Eclipse Dataspace Connector (EDC)](https://github.com/eclipse-edc/Connector) by extending it with blockchain capabilities for the brokering / contract discovery process.

EDC Data Dashboard is a dev frontend application for [EDC Data Management API](https://github.com/eclipse-dataspaceconnector/DataSpaceConnector). We had to fix some issues, so use our forked version to make sure everything works.

The Tezos-EDC Interface is a programm we created, to enable the connection between EDC and the Tezos Blockchain.

### Goal

- Extend EDC functionality by managing and storing assets, policies and contract offerings as NFTs on Tezos blockchain
- Data of assets etc. should be included inside token’s metadata to ensure integrity during negotiation phase
- Implement a blockchain interface to realize minting and token querying functionality
- Optimize token request by adding blockchain indexer → lower response time
- Visualize process of asset, policy and contract offer creation in Data Dashboard → extension and modification is needed

## Prerequisites

Install an extension to ignore CORS errors in the Browser, e.g. [CORS Unblock](https://chrome.google.com/webstore/detail/cors-unblock/lfhmikememgdcahcdlaciloancbhjino)
(We're still working on a fix for this)

## Quickstart with docker compose

Modify docker-compose.yml and add your [Pinata JWT key](https://knowledge.pinata.cloud/en/articles/6191471-how-to-create-an-pinata-api-key). Than you can just run `docker-compose up` to get a quick setup running for simple demonstration purposes.

Optionally you can add environment varibales to the edc-interface container in order to specify your own smart contract addresses.
Following params can be modified:

- `ASSET_ADDRESS` - default value = `KT1S2BZRyg9MXgERzsCUjqjiKS95L7FCt8KM`
- `POLICY_ADDRESS` - default value = `KT1L7w74gXr1kuF2z4cEe59z7fhihdixSKWm`
- `CONTRACT_ADDRESS` - default value = `KT1VSymTD5oqBHqQqXPhc7hXhyoL38AK6xpC`
- `TRANSFER_ADDRESS` - default value = `KT1N5oTfoLsKbXshfW5WrcnQJdB1kR5t21Vs`
- `AGREEMENT_ADDRESS` - default value = `KT19Jk6zvWfFjWMVSozPNm7VDMKSDVGrU6XD`

### Using Azurite (currently disabled)

---


Create a file in Azurite to test the later transfer process
```shell
conn_str="DefaultEndpointsProtocol=http;AccountName=company1assets;AccountKey=key1;BlobEndpoint=http://127.0.0.1:10000/company1assets;"

```

```bash
az storage container create --name src-container --connection-string $conn_str
```

```bash
az storage blob upload -f ./README.md --container-name src-container --name README.md --connection-string $conn_str
``` 


## Installation & Execution

In order to try the demonstration, you need to run three applications:

1. EDC Provider
2. Tezos-EDC Interface
3. Angular Frontend (EDC Data Dashboard)

All needed repositories are included in this repository as submodules.

### Steps:

- Clone this repository using `git clone --recursive [URL]` to make sure all submodules are also cloned correctly

- Enter the EDC Interface Submodule and follow the readme instructions of edc-interface. Don't forget to add the Pinata env variable. Finally run the API with `npm run serve`.

- Enter the Data Dashboard Submodule and run `npm install -g @angular/cli` to install Angular globally.

- Run `npm install` to install all dependencies

- Run `npm run start` to host angular frontend

To build and run the edc extension and the edc , run the next two commands in the root directory of the Samples-Blockchain submodule:

- `./gradlew BlockchainCatalog:blockchain-catalog-prosumer:build`

- `java -Dedc.fs.config=BlockchainCatalog/blockchain-catalog-prosumer/config.properties -jar BlockchainCatalog/blockchain-catalog-prosumer/build/libs/consumer.jar`

Use http://localhost:4200/ to explore the Data Dashboard in your browser.

## Example run via Postman requests

Deploy the two EDCs and the edc-interface.

Use this [postman workspace](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/overview) to get access to the following requests. Also use this [Environment](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/environment/20564347-2ca28dbe-a227-4861-81ee-63c00544045f) of the Postman Collection 

Navigate to the `EDC REST API (new)` Collection. The workspace makes strong use of variables and environments which are manipulated via Pre-request Scripts and Tests. To change the used URLs, click on the `EDC REST API (new)` and open `Variables`. The environment `Test` contains mostly variables that are changed during the execution of the requests. E.g. ids that are increased every request to mitigate duplicated keys and ids that are needed for later requests. 

For a full example the following requests have to be send in this order:

1. [`assets/create Asset`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-bbf66f1b-0187-4268-8ff4-c30fccd0b0c3)
2. [`policydefinitions/create Policy`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-6aabb435-a6bc-4de4-8102-419bf4480031)
3. [`policydefinitions/create Policy Client`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-1f23da91-b339-4068-9d71-b9cd0f751310)
4. [`contractdefinitions/create Contract Definition`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-c40ee970-2e1c-4837-a60d-9985bde97d39)
5. [`contractnegotiations/initiate Contract Negotiation`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-4832c0a1-de1e-4af8-a88e-800fa8624cf6)
6. [`contractnegotiations/{id}/get Agreement for Negotiation`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-dbf7af91-4431-4c1d-88a7-2412c7959605) 
7. [`dataplane/register dataplane at provider`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-fc79c794-f348-4ba7-9996-a5775de5ca17)
8. [`transferprocess/initiate Transfer http push`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-4d80d47b-9c55-428e-af12-449d99009436)

Every step makes use of pre and post Requests scripts which collect values from request responses and save them for later requests. This should give an overview of how the requests works and how to change them for individual use cases.




## Support

If you have any questions regarding the Tezos Client API implementation, feel free to contact me:
<hartmann@tu-berlin.de>

If there are any problems with installation or deployment, you can write me a mail:
<julian.legler@tu-berlin.de>

```

## Outlook

- reach higher level of decentralization and independency:
  - replace Pinata API with individual running IPFS nodes
  - replace TzKT API by running a full blockchain node and further implement lightweight blockchain indexer
- enrich blockchain interface functionality to comply with EDC processes:
  - delete/burn tokens (assets, policies and contract offers)
  - transfer tokens to map asset transfer after negotiation phase
  - link identity management to tokens
- analyze operation costs

## Legal

See legal comments of the three sub repositories.
```
