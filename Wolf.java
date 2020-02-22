/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package wolf;
import java.util.*;

// wolf class to implement FSM
public class Wolf {
    
    private static int Calories =20;  //calories that increase with successfull kill and decreases with moves
    private static int Predator_dis = 0;   
    private static int Prey_dis = 0;
    private static int Speed = 25;   //speed of wolf in miles per hour
    private static String Emotional_state = "Normal";  //emotional state of the wolf
    private static boolean time; //for timeout
    
    /**
     * Abstract base class of all states. Note that it has
     * been made static so we can use it in the static main
     * method. Do not change this! 
     */
    public static abstract class State {
      
        protected Scanner input = new Scanner(System.in);
        protected Random random = new Random();
        public State() {}
        public abstract void enter();
        public abstract void exit();
        public abstract int updateAndCheckTransitions();
        //timeout implemented for ruunning
        public Boolean timeOut(){
        int reply;
        System.out.println("Enter 1 to continue running "+"\n" );
        reply = input.nextInt();
        if(reply==1){
            time =true; 
            System.out.println("Wolf has ran to a safe place and away from predetor"+ "/n" );
        }
        else time = false;
        
        return time;
        }
    }
    
   
    
    /**
     * This normal state class that extends the state. This is the middle ground 
     * state that wolf goes to before transition between other states to show some 
     * intelligence and avoid state transition that might seem akward 
     
     */
    
    public static class NormalState extends State {
        public NormalState() {
            super();
        }
        
        
        public void enter() {
            
            //if emotional state is dead we make sure to let user know that the wolf is revived
            if(Emotional_state == "Dead"){
                System.out.println("We have revived your wolf character for you.");
                Calories = 20;
            }
            else
            Emotional_state = "Normal";
            System.out.println("The Wolf is in normal State");
        }
        public void exit() {            
            System.out.println("The wolf is changing state....."+ "\n");
        }
        public int updateAndCheckTransitions() {        
             //Change to Hungry State if calories is below 10
            if (Calories < 10){
                Calories--;
                System.out.println("The wolf is turning hungry.");    
                return 3;
            }
            
            //Change to Running State if prey is closer than 200ft
            //Also implements timeout if the player chooses to continue running after escape predator is not seen on another loop.
            if(time ==false){
                
                System.out.print("How far is a Predator ? (in ft) ");
                Predator_dis = input.nextInt();
                if (Predator_dis  < 200) {
                    System.out.println("There is a predator close by. Wolf has to escape.");
                    Calories--;
                    return 1;
                }
            }
            time =false; //setting the timeout back
            
           //change to Chasing State if distance is closer than 200ft
             System.out.print("How far is a Prey ? (in ft)");
            Prey_dis = input.nextInt();
           if (Prey_dis  < 200) {
                System.out.println("There is a prey nearby. Wolf will chase it.");
                Calories--;
                return 2;
           }   
          
            
           
           //checking to see if user wants the wolf to sleep
            System.out.println("Do you want the wolf to sleep (Y/N) ?");
            String sleep =input.next();
            if (sleep.equalsIgnoreCase("Y")){
                return 4;
            }
            
             return 0;   
            
            
        }
    }
    
    /**Chasing state that is initiated when chasing a prey. Extends class State.
    * Implements random to show some intelligence and not just repetative commands.
    */
     public static class Chasing extends State {
        public Chasing() {
            super();
        }
        public void enter() {
            System.out.println("The Wolf is chasing the prey.");
        }
        public void exit() {            
            System.out.println("The wolf is changing state....."+ "\n");
        }
        public int updateAndCheckTransitions() {        
            
            //considers the speed of prey to decide if they prey is caught or not. Also use of random for probability
            System.out.print("How fast is the prey? (in miles/hr)");
            int preySpeed= input.nextInt();
            if( Speed >= preySpeed) {
                if(random.nextInt(9) == 0){   //10% probability of prey escaping even if wolf is faster
                    Emotional_state= "Angry";
                    System.out.println ("The prey escaped. The wolf is "+ Emotional_state); 
                    return 0;
                }
                System.out.println("The wolf caught the prey.");
                return 5;       
                }
                
                
            else{
              if(random.nextInt(9) == 0){  //10% probability of prey egetting caught even if wolf is slower
                    System.out.println ("The wolf caught the prey."); 
                    Calories=-2;  //more calories lost when speed of prey is greater
                    return 5;
                }
                Emotional_state= "Angry";
                System.out.println("The prey escaped. The wolf is "+ Emotional_state);
                return 0; 
            }           
            
        }
    }
    
     //Eating states that updates the calories with each good hunt 
     public static class Eating extends State {
        public Eating() {
            super();
        }
        public void enter() {
            Emotional_state= "Happy";
            System.out.println("The Wolf is eating its hunt. The wolf is "+ Emotional_state);
        }
        public void exit() {            
            System.out.println("The wolf is changing state....."+ "\n");
        }
        public int updateAndCheckTransitions() {        
            Calories +=10;   //calories increased
            System.out.print("Now wolf is done eating. Its calories have increase to " + Calories +" ");
            return 0;
            
        }
    }
     
     //Running state that extends state and decides if the wolf escapes from a prey or not
     public static class Running extends State {
        
         public Running() {
            super();
        }
    
        public void enter() {
            Emotional_state= "Afraid";
            System.out.println("The Wolf is running. The wolf is "+ Emotional_state);
        }
        public void exit() {            
            System.out.println("The wolf is changing state....."+ "\n");
        }
     
       
        public int updateAndCheckTransitions() {        
             System.out.print("How fast is the pradator? (in miles/hr) ");
            int pradatorSpeed= input.nextInt();
            
            //using probability to determine if prey gest caught or not
            if( Speed >= pradatorSpeed) {
                if(random.nextInt(9) == 0){ //using random with 10% probability of getting caught when speed is faster than predator
                    Emotional_state= "Dead";
                    System.out.println ("The wolf is caught. The wolf is "+ Emotional_state); 
                    return 0;
                }
                System.out.println("The wolf escaped.");
                timeOut(); ;  //timeout to avoid the predetor for one loop
                
                return 0;       
                }
                
                
            else{
              if(random.nextInt(9) == 0){ //using random with 10% probability of escaping when speed is slower than predator
                    System.out.println ("The wolf barely escaped"); 
                     timeOut();  //timeout to avoid the predetor for one loop
                    Calories=-2;  //more calories lost when speed of predetor is greater
                    return 0;
                }
                Emotional_state= "Dead";
                System.out.println("The wolf is caught. The wolf is "+ Emotional_state);
                return 0; 
            }           
        }
    }
     
     
     //Hungry state that extends state and is initiated if the wolf is hungry and calories is below 10.
      public static class Hungry extends State {
        public Hungry() {
            super();
        }
        public void enter() {
            Emotional_state= "Frowning";
            System.out.println("The Wolf is hungry and low on calorie. The wolf is "+ Emotional_state);
        }
        public void exit() {            
           if(Emotional_state  == "Dead") return ;
            System.out.println("The wolf is changing state....."+ "\n");
        }
        public int updateAndCheckTransitions() { 
           
            //if calories fall below zero wolf is dead but revided again by calling normal state 
           if(Calories < 1){
               Emotional_state= "Dead";
               System.out.println ("The wolf is out of calories. The wolf is "+ Emotional_state); 
               return 0;
           }
            
           ////Change to Running State if prey is nearby
           System.out.print("How far is a Prey ? (in ft)");
            Prey_dis = input.nextInt();
           if (Prey_dis  < 200) {
                System.out.println("There is a prey nearby. Wolf will chase it.");
                Calories--;
                return 2;
           }   
           
           if(Calories < 10 ){
               System.out.println("Do you want to sleep to avoid losing calories while waiting for a prey? (Y/N)");
               String answer = input.next();
               if (answer.equalsIgnoreCase("Y")){
                   return 4;
               }  
           } 
           Calories--;
           return 3;  
        }
    }
     //Sleeping state which can be used to prevent loss of calories while wolf is hungry. Extends state class 
     public static class Sleeping extends State {
        public Sleeping() {
            super();
        }
        public void enter() {
            Emotional_state= "Calm";
            System.out.println("The Wolf is sleeping. The wolf is "+ Emotional_state);
        }
        public void exit() {            
            System.out.println("The wolf is changing state....."+ "\n");
        }
        public int updateAndCheckTransitions() {        
          System.out.println("Do you want to continue sleeping? (Y/N)");
               String answer = input.next();
               if (answer.equalsIgnoreCase("Y")){
                   return 4; 
               } 
                if (Calories < 10){
                System.out.println("The wolf is hungry.");    
                return 3;
            }
        return 0;
        }
               
    }
     

      
     

    /**
     * Main driver for FSM wolf. Controls all the states transition and keeps the loop to keep the game going
     */
    public static void main(String[] args) {
        int numberOfStates = 6;
        State[] states = new State[numberOfStates];
    
        states[0] = new NormalState();
        states[1] = new Running();
        states[2] = new Chasing();
        states[3] = new Hungry();
        states[4] = new Sleeping();
        states[5] = new Eating();

        int currentState = 0;
        int nextState;  

        states[0].enter();
        while(true) {
            nextState = states[currentState].updateAndCheckTransitions();
            if (nextState != currentState) {
                states[currentState].exit();
                currentState = nextState;
                states[currentState].enter();
            }
    }
}
}

    
    
   
