# IS23-AM36

## Professors and Tutors
- Ing. Alessandro Margara   ( In Charge Professor  )
- Ing. Gian Enrico Conti    ( Practical Instructor )
- Ing. Barbara Schinaia     ( Laboratory Instructor)
- Stefano Fossati           ( Laboratory Tutor     )
- Ettore Zamponi            ( Laboratory Tutor     )

## Group Members          
- Carlo Arnone
- Davide Chiorlin
- Lorenzo Cattaneo
- Vincenzo Donatelli

## Description
In this repo there is the IngSoft2023 - Polimi Project 


### Development Tools
**IDE**: IntelliJ IDEA <br>
**PMS**: Maven <br>
**TES**: JUnit <br>

### Calendar
![image](https://user-images.githubusercontent.com/97902829/221122050-feeac1ae-38af-4795-b9bf-48aa239f9643.png)

### Status

| ProjectSubPart   | Assignee          | Completeness                             | Notes                                                                    |
|------------------|-------------------|------------------------------------------|--------------------------------------------------------------------------|
| BaseRules        | Vincenzo  Lorenzo | <span style="color:green">100%</span> 🟢 | Done                                                                     |
| CompleteRules    | Vincenzo  Lorenzo | <span style="color:green">100%</span> 🟢 | Done                                                                     |
| Controller       | Carlo             | <span style="color:green">100%</span> 🟢 | Done                                                                     |
| CLI              | Carlo             | <span style="color:green">95%</span>  🟢 | Testing corner case                                                      |
| GUI              | Davide            | <span style="color:green">95%</span>  🟢 | testing Corner Case                                                      |
| Persistence      | All               | <span style="color:green">95%</span>  🟢 | fixing last problem                                                      |
| ClientResilience | All               | <span style="color:green">95%</span>  🟢 | testing corner case                                                      |
| MultiGameServer  | All               | <span style="color:green">100%</span> 🟢 | Complete                                                                 |
| RMI              | Vincenzo          | <span style="color:red">95%</span>    🟢 | Strange inception with socket                                            |
| Socket           | Carlo Lorenzo     | <span style="color:red">100%</span>   🟢 | Done                                                                     |



### Pills of Software
The Application we are going to develop is **MyShelfie** (by CranioGames). <br>
The game states in one main accomplishment to score more points of the opponent, you can score points in different ways but basically is like Connect Four style playing Grid, where you have to insert some placeholders to build your own shelf and obtain some configurations. <br>
  

### Useful Links

MyShelfie Online Store: https://www.craniocreations.it/prodotto/my-shelfie



### How To Launch
1- Maven install <br>
2- cd $(main folder of the project - IS23-AM36)  <br>
3- **Server**: java -jar App.jar server {port}    ------  **Client**: java -jar App.jar {socket / RMI} {ip server} {port server} {GUI / CLI} .  <br>




