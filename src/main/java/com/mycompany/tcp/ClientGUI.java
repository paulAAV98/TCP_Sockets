/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tcp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private BufferedWriter writer;
    private int llave=0;
    private JButton sendButton1;
    private  JButton sendButton2;
    private JButton sendButton;
    private CryptoUtils crypto;

    public ClientGUI() {

        // Solicitar al usuario que ingrese su nombre mediante un cuadro de diálogo de entrada
        String input = JOptionPane.showInputDialog("Ingresar su nombre :");
        setTitle("Cliente " + input);
        // Configurar propiedades de la ventana JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Cerrar la aplicación al cerrar la ventana
        setSize(630, 300);
        setLocationRelativeTo(null);// Centrar la ventana en la pantalla
        setLayout(null); // Desactivar el diseño automático para colocar los componentes manualmente
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Cerrar la ventana al presionar el botón de cierre

        // Crear un área de texto (JTextArea) para mostrar los mensajes del chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);// Hacer que el área de texto sea de solo lectura
        JScrollPane scrollPane = new JScrollPane(chatArea);// Agregar un panel de desplazamiento al área de texto
        scrollPane.setBounds(10, 10, 400, 200);
        add(scrollPane);

        // Crear un campo de texto (JTextField) para que el usuario ingrese mensajes
        messageField = new JTextField();
        messageField.setBounds(10, 220, 280, 30);
        add(messageField);// Agregar el campo de texto a la ventana

         sendButton = new JButton("Enviar");
        sendButton.setBounds(300, 220, 90, 30);
         sendButton1 = new JButton("Desconectar");
        sendButton1.setBounds(400, 220, 100, 30);
         sendButton2 = new JButton("Conectar");
        sendButton2.setBounds(500, 220, 100, 30);
        add(sendButton);
        add(sendButton1);
        add(sendButton2);
        sendButton2.setEnabled(false);

        try {
            // Se establece una conexión con el servidor en localhost y puerto 8888.
            Socket socket = new Socket("localhost", 8888);

            // Se crea un BufferedWriter para escribir en el flujo de salida del socket.
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Se crea un BufferedReader para leer del flujo de entrada del socket.
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //System.out.println("COMO VASSSSSSSSSS "+socket.getLocalPort());

            // Se agrega un ActionListener al botón de enviar para manejar los eventos de clic.
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Cuando se hace clic en el botón de enviar:
                         crypto=new CryptoUtils();
                        String message = input + " : " + crypto.decrypt(crypto.encrypt(messageField.getText(), "1"), "1"); // Se obtiene el texto del campo de entrada.
                        if (!message.isEmpty()) { // Si el mensaje no está vacío:
                            try {
                                String menslocal="Yo : ".concat(messageField.getText());
                                //String menslocal=input.concat(": ".concat(messageField.getText()));
                                chatArea.append(menslocal+ "\n");
                               
                                // Se escribe el mensaje en el BufferedWriter y se envía al servidor.
                                String mensaje=crypto.encrypt(message,"2");
                                writer.write(mensaje+"\n");
                                writer.flush();
                                messageField.setText(""); // Se limpia el campo de entrada.
                            } catch (IOException ex) {
                                ex.printStackTrace(); // Se imprime la traza de la excepción si hay un error de E/S.
                            } catch (Exception ex) {
                                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            // Asociar un ActionListener al botón "Salir" (sendButton1)
            sendButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Este método se ejecutará cuando el botón "Salir" sea clicado

                    // Cerrar la ventana actual (dispose())
                    
                        llave=1;
                        JOptionPane.showMessageDialog(null, input + " se ha desconectado");
                        sendButton1.setEnabled(false);
                        sendButton2.setEnabled(true);
                        sendButton.setEnabled(false);
                        messageField.setEnabled(false);
                        
                   
                    
                    
                    //dispose();

                    // Mostrar un cuadro de diálogo informativo indicando que el usuario se ha desconectado
                    
                }
            });
            sendButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Este método se ejecutará cuando el botón "Salir" sea clicado

                    // Cerrar la ventana actual (dispose())
                    
                        llave=0;
                        JOptionPane.showMessageDialog(null, input + " se ha conectado");
                        sendButton2.setEnabled(false);
                        sendButton1.setEnabled(true);
                        sendButton.setEnabled(true);
                        messageField.setEnabled(true);
                        
                    
                    
                    
                    //dispose();

                    // Mostrar un cuadro de diálogo informativo indicando que el usuario se ha desconectado
                    
                }
            });

            // Se inicia un nuevo hilo para escuchar los mensajes entrantes del servidor.
            new Thread(() -> {
                String inputLine;
                
                try {
                    while ((inputLine = reader.readLine()) != null) {
                        // Mientras haya mensajes entrantes, se muestran en el área de chat.
                        if (llave==0){
                            crypto=new CryptoUtils();
                            String mensaje=crypto.decrypt(inputLine,"2");
                        chatArea.append(mensaje + "\n");
                        
                        }
                        
                        
                    }
                } catch (IOException ex) {
                    ex.printStackTrace(); // Se imprime la traza de la excepción si hay un error de E/S.
                } catch (Exception ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace(); // Se imprime la traza de la excepción si hay un error al establecer la conexión.
        }

        setVisible(true); // Se hace visible la interfaz gráfica del cliente.
    }

    public static void main(String[] args) {

        new ClientGUI();
    }
}
