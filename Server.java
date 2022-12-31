import java.net.*;
import java.io.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Server extends JFrame {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out ;

    //Declaring Component
    private JLabel heading= new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();

    private Font font = new Font("Roboto", Font.PLAIN, 20);


   
    //Constructor
    public Server()
    {
       try {

        server = new ServerSocket(7777);
         System.out.println("Server is ready to accept connection");
         System.out.println("waiting ... ");
         socket = server.accept();

         br =  new BufferedReader( new InputStreamReader(socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());

         createGUI();
         handleEvents();

         startReading();
         startWriting();

       } catch (Exception e) {
          e.printStackTrace();
       }
     

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){
           
            
         

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("key Released " + e.getKeyCode());
                if(e.getKeyCode()==10){
                   // System.out.println("you have pressed Enter button");
                   String contentToSend= messageInput.getText();
                   messageArea.append("Me :" +contentToSend+"\n");
                   out.println(contentToSend);
                   out.flush();
                   messageInput.setText(" ");
                   messageInput.requestFocus();
                }
            }});
}


private void createGUI()
{
    //guiCode ..   (this. is our window screen)
    this.setTitle(" Server Messenger[Sparsh] ");
    this.setSize(600,700);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Coding for component
     heading.setFont(font);
     messageArea.setFont(font);
     messageInput.setFont(font);
     heading.setIcon(new ImageIcon("/Users/sparshshukla/Desktop/ComputerScience/JavaProject/ChatApplication/icons8-online-dating-chat-app-78.png"));
     
     heading.setHorizontalAlignment(SwingConstants.CENTER);
     heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
     messageArea.setEditable(false);
     heading.setBackground(Color.blue);
     heading.setOpaque(true);
     messageInput.setHorizontalAlignment(SwingConstants.CENTER);
     
   

     // Frame Layout Set Karenge

     this.setLayout(new BorderLayout());

     // adding the compoents to the frame
     this.add(heading, BorderLayout.NORTH);
     JScrollPane jScrollPane = new JScrollPane( messageArea);
     messageArea.setCaretPosition(messageArea.getDocument().getLength());
     
     this.add(jScrollPane,BorderLayout.CENTER);
     this.add(messageInput,BorderLayout.SOUTH);
    






    this.setVisible(true);
}



    
    public void startReading()
    {
       // multiThreading   (yeh Thread Read krta rahega)

       Runnable r1 = ()->{   // lambda Function
           
        System.out.println("Reader Started");

        try {

         while(true){
             
             String msg = br.readLine();
             if(msg.equals("exit"))
             {
                 System.out.println("Client Terminated the Chat");
                 JOptionPane.showMessageDialog(this,"Client Terminated the chat" );
             messageInput.setEnabled(false);
                 socket.close();
                 break;
             }
             
            messageArea.append("Client : " +msg+ "\n");
            
            }
           
           }
           
           catch(Exception e){
            //e.printStackTrace();
            System.out.println("Connection Closed");
           }

         };
           new Thread(r1).start();
    };

    
    public void startWriting()
    {
     // multithreading  (yeh Thread User se data lega and use send karega client ko)
           Runnable r2 = ()->{
               System.out.println("Writer Started");

            try {
               while(!socket.isClosed()){
               
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                   
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                {
                    socket.close();
                    break;

                }
                }

                System.out.println("Connection Closed");

           }catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Connection Closed");
         }
     };

           new Thread(r2).start();
    }


    







    public static void main(String[] args){
        System.out.println(" This is server .. going to start server")
        ;
        new Server();
    }
}