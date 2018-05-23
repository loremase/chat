package guichat;

import javax.swing.JFrame;

public class ClientTest {
 public static void main( String[] args )
 {
 Client applicazione;

 
 if ( args.length == 0 )
	 applicazione = new Client( "127.0.0.1" ); // mi connetto al localhost se non immetto nulla da linea di comando
else
	applicazione = new Client( args[ 0 ] ); // oppure uso args[0] per scegliere l'IP


 applicazione.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 applicazione.runClient(); // 
 } // 
} // 
