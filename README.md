# EDC Blockchain Broker

## Description

### Introduction

**Please note: This repository does not contain production-grade code and is only intended for demonstration purposes.**

**This Repository was created to work for the specific demonstration of connecting the EDC to the tezos-edc-interface and the edc dashboard**

We provide an extension that uses the [Eclipse Dataspace Connector (EDC)](https://github.com/eclipse-edc/Connector) by extending it with blockchain capabilities for the brokering / contract discovery process.

EDC Data Dashboard is a dev frontend application for [EDC Data Management API](https://github.com/eclipse-dataspaceconnector/DataSpaceConnector). We had to fix some issues, so use our forked version to make sure everything works.

The Tezos-EDC Interface is a programm we created, to enable the connection between EDC and the Tezos Blockchain.

Currently we support the EDC version 0.2.0

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

In order to pin content to the IPFS you must include your Pinata API credentials in the form of environment parameters:

1. Create a file in root directory called ".env"
2. Add your [Pinata key](https://knowledge.pinata.cloud/en/articles/6191471-how-to-create-an-pinata-api-key) as a variable (the JWT key):
   ```
   PINATA_KEY="<JWT key>"
   ```

3. Than you can just run `docker-compose up` to get a quick setup running for simple demonstration purposes.

Optionally you can add environment varibales to the edc-interface container in order to specify your own smart contract addresses.
Following params can be modified:

- `ASSET_ADDRESS` - default value = `KT1S2BZRyg9MXgERzsCUjqjiKS95L7FCt8KM`
- `POLICY_ADDRESS` - default value = `KT1L7w74gXr1kuF2z4cEe59z7fhihdixSKWm`
- `CONTRACT_ADDRESS` - default value = `KT1VSymTD5oqBHqQqXPhc7hXhyoL38AK6xpC`
- `TRANSFER_ADDRESS` - default value = `KT1N5oTfoLsKbXshfW5WrcnQJdB1kR5t21Vs`
- `AGREEMENT_ADDRESS` - default value = `KT19Jk6zvWfFjWMVSozPNm7VDMKSDVGrU6XD`
- `VERIFIABLE_CREDENTIALS_ADDRESS` - default value = `KT1XgUq6rzN9q6YMh44TbLffEz3zb54HbY2H`

### Create your own tezos adress or asset, policy, contract contracts

In case you wish to create your own tezos address or asset, policy, contract contracts, we provide an option. It is delivered with our docker compose file and commented out by default. Comment in the octez-node lines at the bottom of the docker-compose.yml and run:
```
docker compose up
```
Folders are created outside of Docker where the blockchain data is stored. On Linux systems, it is unfortunately necessary to give these folders separate write permissions for the current user because otherwise tezos/tezos:octez-node-alpha docker service cannot continue and is caught in a loop.
```
sudo chmod 777 -c -R client_data/ container-data/ node_data/ 
```
Generate a new address tz1... and load tez on to it:
```
docker exec edc-interface-1 ./newAccount.sh 
```
Copy the hash and visit the tezos faucet page to fund your account at:
```
https://faucet.ghostnet.teztnets.xyz 
```
Generate new asset, policy, contract contracts with:
```
docker exec edc-interface-1 ./newContracts.sh 
```
These can then be replaced in the docker-compose.yml in the lines 47 to 49 and the lines 117 to 119.

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

Use this [postman workspace](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/collection/20564347-cf566691-f3bf-4e5c-aa66-84515bfd9b08?action=share&creator=20564347&active-environment=20564347-2ca28dbe-a227-4861-81ee-63c00544045f) to get access to the following requests. Also use this [Environment](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/environment/20564347-2ca28dbe-a227-4861-81ee-63c00544045f?action=share&creator=20564347&active-environment=20564347-2ca28dbe-a227-4861-81ee-63c00544045f) of the Postman Collection as there are many variables used to make the flow more easy. 

Navigate to the `EDC REST API 0.2.0` Collection. The workspace makes strong use of variables and environments which are manipulated via Pre-request Scripts and Tests. To change the used URLs, click on the `EDC REST API 0.2.0` and open `Variables`. The environment `Test` contains mostly variables that are changed during the execution of the requests. E.g. ids that are increased every request to mitigate duplicated keys and ids that are needed for later requests. 

For a full example the following requests have to be send in this order:

1. [`assets/create Asset`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-1c2d0dc5-6c83-4663-8251-083526687dad?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f)
2. [`policydefinitions/create Policy`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-c4f8a661-cc24-4b03-aba6-ecf91dcb6573?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f)
4. [`contractdefinitions/create Contract Definition`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-ec36f39d-1cfe-4dd1-92ab-a9f0804f5e5d?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f)
5. [`contractnegotiations/initiate Contract Negotiation`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-6d5cbc2e-30bd-4af1-8e27-b113ddd1a5dd?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f)
6. [`contractnegotiations/{id}/get Negotiation`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-6deadddf-bec9-4917-9018-c8b90822f5b5?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f) 
7. [`register dataplane`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-1b2fe7e9-3b8b-41b5-bb1d-f90e9dc5465e?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f)
8. [`transferprocess/initiate Transfer Process`](https://www.postman.com/payload-specialist-10840615/workspace/edc-api-playground-tu-berlin/request/20564347-ebb002e6-64bb-47a0-b669-eb7479e336f6?active-environment=2ca28dbe-a227-4861-81ee-63c00544045f)

Every step makes use of pre and post Requests scripts which collect values from request responses and save them for later requests. This should give an overview of how the requests works and how to change them for individual use cases.




## Support
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
