# Project: DISTRIBUTED AUCTION
## Students:  Siri Khalsa, Michelle Louie, and Alexandra Valdez

###MAKE SURE YOU USE BRANCH final on csgit AS THIS HAS THE FINAL WORKING PROJECT


## Introduction
This project simulates multiple Auction Houses that sell items to Agents. 
The Agents place bids on items at the Auction Houses and a Bank processes 
the transfer of funds between Houses and Agents. The Houses, Agents, and Bank 
can communicate across multiple machines. 

## Contributions
**Siri's Contributions:**<br>
I primarily worked on the AuctionHouse part. this acts as a server to agents and a client  to the bank
When an Auction House starts it connects to the bank server and gets an account. It then starts its own server
and soawns a new thread to broker connections with agents. it randomly generates items and communicates with agents over sockets
to sell them. it uses the bank to confirm funds exist. after 30 seconds of no more bidding the item will get sold to the highest bid.
Made the GUI
<br><br>
**Michelle's Contributions:**<br>
Worked mainly on the Agent which is a client to both Auction Houses and the Bank.
The Agent first registers with the Bank to create an account. 
When Auction Houses are available, the Agent receives a list of houses from the Bank.
The Agent then chooses a house to connect to and receives a list of items 
from which it may place a bid. 
The Agent will receive messages regarding the bidding status of its bids and if a bid is won,
the agent will tell the Bank to transfer funds.
Worked on the underlying logic of the GUI with Siri

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

**MUST USE JRE 10.0.2**<br>
To use the program, you will need three .jar files: **Bank_skhalsa10_mllouie_alvaldez.jar**, **AuctionAgent_skhalsa10_mllouie_alvaldez.jar**, and **AuctionHouse_skhalsa10_mllouie_alvaldez.jar**<br>
Machines running an Agent or Auction House will need the following arguments: <br><br>
**Auction House:** House Name, Bank Host Name, Bank Port, House Host Name, House Port<br>
**Agent:** Bank Host Name, Bank Port, Agent Name, Initial Account Balance<br>
**Bank:** Bank port<br><br>
Once connected to the Bank, simply use the provided GUI to place bids. 
* A Loading screen indicates that no Auction Houses are yet available.
* A list of Auction Houses will appear once they are available. User can click on an Auction House to view its items.
* On the item page, user can click on the item they will like to place a bid on and enter the bid amount in the text field.
* Click the "Place Bid" button to place the bid.
* Bids will be rejected if item is already sold or no item is selected. 
* Multiple bids on different items in different Auction Houses may be placed by clicking on the back button on the bottom left.
* Agent info (Account #, Balance, Available Funds, Bid Status Messages) appear on the very bottom.
<br>
*NOTE: The Bank_skhalsa10_mllouie_alvaldez.jar file MUST be launched first! the house and clients can be started in any order

Examples of how to start all three applications successfully:
1. java -jar Bank_skhalsa10_mllouie_alvaldez.jar 7779 - this will start the bank server on port 7779 using the hostname of the computer it is running on
2. java -jar AuctionHouse_skhalsa10_mllouie_alvaldez.jar store1 b156-20 7779 b156-17 7778 - this will start a house to the bank above and start its own server on port 7778
3. java -jar AuctionAgent_skhalsa10_mllouie_alvaldez.jar b156-20 7779 bob 500000n - starts a new agent with the name bob and 500000 opening balance<br>

## Project Assumptions
- The bank expects clients to use integer amounts when creating accounts and placing bids. <br>
- Bank must be run first.<br>
- **Must use Java 10.0.2**<br>
- We assume that bank account is not requested to be open with a negative balance.
- The house and agent each have a parameter to start the programs that are their 
names. these names NEED to be unique.
- There is an assumption that people using this application understand networking.
for example if all running pieces are on the same LAN and DNS has hostnames registered you should be able to
use hostnames like "Bobs_Computer". but if DNS do not have these names registered you will need
to use IP addresses. Also you must pick a port that is open. the higher the port the better possibility it is not alread used
- this application will work accross the internet. using home routers. the ports need to be forwarded from
the  router tot he devices ip address running the bank and houses. public ip addresses are needed for connecting.

## Docs
What folder is your documentation (diagram and class diagram) in?

## Status
### Implemented Features

We implemented a GUI for our client.

### Known Issues
1. **BUG** When GUI terminates handle closing connection - **FIXED**
2. **BUG** Can place Bid on house after it closes down - **FIXED**
3. **BUG** Connect Exception: Connection refused: connect in GUI **Probably FIXED**
4. **BUG** Sometimes the house doesnt exit gracefully **FIXED**
5. **BUG** Resources cant be accessed from in jar **Fixed**
6. **BUG** Sometimes the GUI Window will shrink when it refreshes the data. even in full screen the window shrinks even when it thinks it is full screen. **FIXED**
7. **BUG** Bad bug sometimes when other clients flood the system with messages the other agents GUI crash and needs to be closed.