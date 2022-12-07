# EDC Blockchain Broker

## Description

### Introduction

**Please note: This repository does not contain production-grade code and is only intended for demonstration purposes.**

**This Repository was created to work for the specific demonstration of connecting the EDC to the tezos-edc-interface and the edc dashboard**

We use a fork of the [Eclipse Dataspace Connector (EDC)](https://github.com/eclipse-edc/Connector) and extended it with blockchain capabilities.

EDC Data Dashboard is a dev frontend application for [EDC Data Management API](https://github.com/eclipse-dataspaceconnector/DataSpaceConnector). We had to fix some issues, so use our forked version to make sure everything works.

The Tezos-EDC Interface is a programm we created, to enable the connection between EDC and the Tezos Blockchain.

### Goal

- Extend EDC functionality by managing and storing assets, policies and contract offerings as NFTs on Tezos blockchain
- Data of assets etc. should be included inside token’s metadata to ensure integrity during negotiation phase
- Implement a blockchain interface to realize minting and token querying functionality
- Optimize token request by adding blockchain indexer → lower response time
- Visualize process of asset, policy and contract offer creation in Data Dashboard → extension and modification is needed

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

- Run `npm run start-edc` to start provider part of the EDC (Java must installed locally). An EDC instance should now be running which implements an extension accessing the blockchain by using the Tezos client provided by EDC-Interface. A precompiled EDC provider .jar file is placed in this submodule to enable an easy start. The source code for this EDC version can be found in the EDC submodule.

- Run `npm run start` to host angular frontend

## Support

If there are any problems with installation or deployment, you can write me a mail:
<julian.legler@tu-berlin.de>

If you have any questions regarding the Tezos Client API implementation, feel free to contact me:
<hartmann@tu-berlin.de>

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
