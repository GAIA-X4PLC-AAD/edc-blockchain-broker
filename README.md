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

Modify docker-compose.yml and add your [Pinata key](https://knowledge.pinata.cloud/en/articles/6191471-how-to-create-an-pinata-api-key). Than you can just run `docker-compose up` to get a quick setup running for simple demonstration purposes.

Optionally you can add environment varibales to the edc-interface container in order to specify your own smart contract addresses.
Following params can be modified:

- `ASSET_ADDRESS` - default value = `KT1N3iJne4jFnQz4tdHBz5q7Cd8Wmd6XtZSH`
- `POLICY_ADDRESS` - default value = `KT1J7FvNLo2yQSUm7jcm2wzNHDBhR19Y5dJ9`
- `CONTRACT_ADDRESS` - default value = `KT1QDheV2TkL3mitzYNKzunWYhSe6MmEPTh5`
- `TRANSFER_ADDRESS` - default value = `KT1N5oTfoLsKbXshfW5WrcnQJdB1kR5t21Vs`
- `AGREEMENT_ADDRESS` - default value = `KT19Jk6zvWfFjWMVSozPNm7VDMKSDVGrU6XD`

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
