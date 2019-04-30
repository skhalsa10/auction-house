# Project: DISTRIBUTED AUCTION
## Students:  Siri Khalsa, Michelle Louie, and Alexandra Valdez

## Introduction
This project simulates multiple Auction Houses that sell items to Agents. 
The Agents place bids on items at the Auction Houses and a Bank processes 
the transfer of funds between Houses and Agents. The Houses, Agents, and Bank 
can communicate across multiple machines. 

## Contributions
**Siri's Contributions:**<br>
Write your contributions here!
<br><br>
**Michelle's Contributions:**<br>
Write your contributions here!
<br><br>
**Alexandra's Contributions:**<br>
Created bank entity, which manages client connections and accounts.   
It starts a separate thread to open a socket and listen for client connections.
The bank receives and processes requests from clients as Message objects in a blocking queue.
It handles all activity related to accounts, including: 
account creation, balance inquiry, hold/release of funds, and transfer of funds.
Agents may request a list of all existing Auction Houses from the bank.
The bank also keeps track of all clients and is responsible for notifying agents 
when a new Auction House is created.
<br><br>


## Usage
To use the program, you will need three .jar files: **Bank.jar**, **Agent.jar**, and **AuctionHouse.jar.***<br>
Machines running an Agent or Auction House will need the following arguments: <br><br>
**Auction House:** House Name, Bank Host Name, Bank Port, House Host Name, House Port<br>
**Agent:** Bank Host Name, Bank Port, Agent Name, Initial Account Balance<br><br>
Once connected to the Bank, simply use the provided GUI to place bids. 
<br>
*NOTE: The Bank.jar file MUST be launched first!

## Project Assumptions
The bank expects clients to use integer amounts when creating accounts and placing bids.
## Versions 
Where are the .jar files?
### V1
explain about how version 1 works
### V2
explain about how version 2 works etc...

## Docs
What folder is your documentation (diagram and class diagram) in?

## Status
### Implemented Features
State things that work.

### Known Issues
If there are things that don't work put them here. It will save the graders time and keep them in a good mood.

## Testing and Debugging
If you have tests, then explain how they work and how to use them.