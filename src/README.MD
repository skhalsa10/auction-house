# Project: DISTRIBUTED AUCTION
## Students:  Siri Khalsa, Michelle Louie, and Alexandra Valdez

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

**MUST USE JRE 10.0.2**<br>
To use the program, you will need three .jar files: **Bank.jar**, **Agent.jar**, and **AuctionHouse.jar.***<br>
Machines running an Agent or Auction House will need the following arguments: <br><br>
**Auction House:** House Name, Bank Host Name, Bank Port, House Host Name, House Port<br>
**Agent:** Bank Host Name, Bank Port, Agent Name, Initial Account Balance<br>
**Bank:** Bank port<br><br>
Once connected to the Bank, simply use the provided GUI to place bids. 
<br>
*NOTE: The Bank.jar file MUST be launched first! the house and clients can be started in any order

## Project Assumptions
The bank expects clients to use integer amounts when creating accounts and placing bids.

## Docs
What folder is your documentation (diagram and class diagram) in?

## Status
### Implemented Features
State things that work.

### Known Issues
1. **BUG** When GUI terminates handle closing connection - **FIXED**
2. **BUG** Can place Bid on house after it closes down - **FIXED**
3. **BUG** Connect Exception: Connection refused: connect in GUI **Probably FIXED**
4. **BUG** House does not register a clients output stream until it sends its first message
possible to leave an output stream open without getting closed gracefully.
5. **BUG** Sometimes the house doesnt exit gracefully
6. **BUG** Resources cant be accessed from in jar **Fixed**
7. **BUG** Sometimes the GUI Window will shrink when it refreshes the data. even in full screen the window shrinks even when it thinks it is full screen.
