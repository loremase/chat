package guichat;

import java.io.EOFException;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.JFrame;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingUtilities;

 public class Server extends JFrame
 {
 private JTextField campodiscrittura; // il messaggio inserito dall'utente
 private JTextArea areavisuale; // visualizza il messaggio nella finestra dello stesso utente
 private ObjectOutputStream output; //  stream di output verso il client
 private ObjectInputStream input; // stream di input dal client
 private ServerSocket server; // server socket
 private Socket connessione; // connessione col client

 private int contatore = 1; // contatore del numero di connessioni

 //configuro la GUI
 public Server()
 {
 super( "Server" );

 campodiscrittura = new JTextField(); //creo campo di testo in cui inserire il messaggio
 campodiscrittura.setEditable( false );
 campodiscrittura.addActionListener(
 new ActionListener()
 {
 // invio messaggio al client
 public void actionPerformed( ActionEvent event )
 {spediscimessaggi( event.getActionCommand() );
 campodiscrittura.setText( "" );
 } //
 } // 
 ); // 

 add( campodiscrittura, BorderLayout.NORTH );

 areavisuale = new JTextArea(); // crea area dove visualizzare i messagggi
 add( new JScrollPane( areavisuale ), BorderLayout.CENTER );

 setSize( 300, 150 ); // setto la dimensione della finestra
 setVisible( true ); // rendo visibile la finestra
 } // fine del costruttore del server

 // configuro e lancio il server
 public void runServer()
 {
 try // configuro il server per ricevere richieste di connessioni;
 {
	 server = new ServerSocket( 12345, 100 ); // creo ServerSocket

 while ( true )
 {
 try
 {
 attendiperconnessione(); // attendo per una connessione
 getStreams(); // ottengo gli stream di input e output
 processodiConnessione(); // 
 } // end try
 catch ( EOFException eofException )
 {
 visualizzamessaggio( "\nil Server ha concluso la connessione" );
 } // end catch
 finally
 {
 chiudiConnessione(); // chiudo connessione
 ++contatore;
 } 
 }
 } 
 catch ( IOException ioException )
 {
 ioException.printStackTrace();
 } 
 } 

 // attendo la connessione, poi visualuzzo le informazioni della connessione
 private void attendiperconnessione() throws IOException
 {
 visualizzamessaggio( "attendi per la connessione\n" );
 connessione = server.accept(); 



visualizzamessaggio( "Connessione " + contatore + " ricevuta: " + connessione.getInetAddress().getHostName());
 } // 

 // ottengo gli streams per ricevere e inviare dati
 private void getStreams() throws IOException
 {
 // configuro output stream per oggetti
	 output = new ObjectOutputStream( connessione.getOutputStream() );
	 output.flush(); // 

 // configuro input stream per oggetti
	 input = new ObjectInputStream( connessione.getInputStream() );

 visualizzamessaggio( "\nGot I/O streams\n" );
 } // 

 // 
 private void processodiConnessione() throws IOException
 {
 String messaggio = "Connessione avvenuta con successo";
 spediscimessaggi( messaggio ); // send connection successful message

 // abilito il campo di testo cosi da permettere al server di scrivere i messaggi
 setTextFieldEditable( true );

 do // 
 {
 try // leggo il messaggio e lo visualizzo
 {
	 messaggio = ( String ) input.readObject(); // leggo un nuovo messaggio
 visualizzamessaggio( "\n" + messaggio ); // visualizzo il messaggio
 } // 
 catch ( ClassNotFoundException classNotFoundException )
 {
 visualizzamessaggio( "\tipo dell'oggetto ricevuto sconosciuto" );
 } // 

 } while ( !messaggio.equals( "CLIENT>>> TERMINA" ) );
 } // 

 // chiudo streams e socket
 private void chiudiConnessione()
 {
 visualizzamessaggio( "\nla connessione sta per terminare\n" );
 setTextFieldEditable( false ); // disabilito campo di testo

 try
 {

	 output.close(); // chiudo output stream
	 input.close(); // chiudo input stream
	 connessione.close(); // chiudo socket

 } //


catch ( IOException ioException )
 {
 ioException.printStackTrace();
 } // 
 } // 

 // invio messaggio al client
 private void spediscimessaggi( String messaggio )
 {
 try // spedisco oggetto al client
 {
	 output.writeObject( "SERVER>>> " + messaggio );
	 output.flush(); // 

 visualizzamessaggio( "\nSERVER>>> " + messaggio );
 } // 
 catch ( IOException ioException )
 {
 areavisuale.append( "\nerrore nella scrittura dell'oggetto" );
 } // 
 } // 

 // 
 private void visualizzamessaggio( final String messaggiodavisualizzare )
 {
 SwingUtilities.invokeLater(
 new Runnable()
 {
 public void run() // 
 {
 areavisuale.append( messaggiodavisualizzare ); // a
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
} //


