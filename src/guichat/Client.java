package guichat;

import java.io.EOFException;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;

import java.awt.BorderLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.JFrame;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingUtilities;
import java.net.InetAddress;
import java.net.Socket;
 public class Client extends JFrame
 {
 private JTextField campodiscrittura; //l'utente puo scrivere..
 private JTextArea areavisuale; // fa vedere le informazioni all'utente
 private ObjectOutputStream output; // output stream al server
 private ObjectInputStream input; // input stream dal server
 private String messaggio = ""; // messaggio dal server
 private String chatServer; // 




private Socket client; // 
//
 public Client( String host )
 {
 super( "Client" );

 chatServer = host; // setto il server al quale i clients devono connettersi

 campodiscrittura = new JTextField(); // 
 campodiscrittura.setEditable( false );
 campodiscrittura.addActionListener(
 new ActionListener()
 {
 // invio messaggio al server
 public void actionPerformed( ActionEvent event )
 {
 spediscimessaggio( event.getActionCommand() );
 campodiscrittura.setText( "" );
 } // 
 } // 
 ); // 

 add( campodiscrittura, BorderLayout.NORTH );

 areavisuale = new JTextArea(); // 
 add( new JScrollPane( areavisuale ), BorderLayout.CENTER );

 setSize( 300, 150 ); // setto la dimensione  della finestra
 setVisible( true ); // rendo visibile la finestra
 } // 

 // mi connetto al server e processo i messaggi dal server
 public void runClient()
 {
 try // 
 {
 connectToServer(); // creo un Socket per effettuare la connessione
 getStreams(); // ottengo gli stream di input e output
 processodiConnessione(); //processo di connessione
 } // 
 catch ( EOFException eofException )
 {
 displayMessage( "\nil client ha terminato la connessione" );
 } // 
 catch ( IOException ioException )
 {
 ioException.printStackTrace();
 } // 
 finally
 {
 closeConnection(); // 
 } // 
 } // 
//
 private void connectToServer() throws IOException
 {
 displayMessage( "tentativo di connessione\n" );

 //creo il socket per connetttermi al server
 client = new Socket( InetAddress.getByName( chatServer ), 12345 );

 // visualizzo le informazioni relative alla connessione
 displayMessage( "Connesso al: " + client.getInetAddress().getHostName() );

 } //

 // 
 private void getStreams() throws IOException
 {
 // 

	 output = new ObjectOutputStream( client.getOutputStream() );
	 output.flush(); //

 // setto gli inputstream per gli oggetti
 input = new ObjectInputStream( client.getInputStream() );

 displayMessage( "\nottengo gli I/O streams\n" );
 } // 

 // process connection with server
 private void processodiConnessione() throws IOException
 {
 // abilito il campo di testo cosi che il client puo scrivere e inviare i messaggi
 setTextFieldEditable( true );

 do //
 {
 try // leggo il messaggio e lo visualizzo
 {
messaggio = ( String ) input.readObject(); // 
 displayMessage( "\n" + messaggio ); // 
 } // end try
 catch ( ClassNotFoundException classNotFoundException )
 {
 displayMessage( "\ntipocdell'oggetto ricevuto sconosciuto" );
 } // end catch

 } while ( !messaggio.equals( "SERVER>>> TERMINA" ) );
 } // 

 // chiudo streams e socket
 private void closeConnection()
 {
 displayMessage( "\nconnessione in chiusura" );
 setTextFieldEditable( false ); // 


try
 {

	output.close(); // 
	input.close(); // 
	client.close(); // 

 } // end try
 catch ( IOException ioException )
 {
 ioException.printStackTrace();
 } // 
 } // 

 // 
 private void spediscimessaggio( String messaggio )
 {
 try // send object to server
 {

	 output.writeObject( "CLIENT>>> " + messaggio );
	 output.flush(); // 
 displayMessage( "\nCLIENT>>> " + messaggio );
 } // end try
 catch ( IOException ioException )
 {
 areavisuale.append( "\nerrore nella scrittura dell'oggetto" );
 } // 
 } // 

 //
 private void displayMessage( final String messaggiodavisualizzare )
 {
 SwingUtilities.invokeLater(
 new Runnable()
 {
 public void run() // 
 {
 areavisuale.append( messaggiodavisualizzare );
 } // 
 } // 
 ); // 
 } // 

 // 
 private void setTextFieldEditable( final boolean modificabile )
 {
 SwingUtilities.invokeLater(
 new Runnable()
 {
 public void run() // 
 {
 campodiscrittura.setEditable( modificabile );
 } // 
 } // 

		 ); // 
  } // 
  }