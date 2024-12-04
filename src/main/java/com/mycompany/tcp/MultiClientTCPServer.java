package com.mycompany.tcp;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Clase que representa un servidor de chat utilizando sockets TCP para
 * múltiples clientes.
 */
public class MultiClientTCPServer {

    private List<Socket> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket clientSocket;
   
   

    /**
     * Inicia el servidor en un puerto específico y acepta conexiones entrantes.
     *
     * @param port Puerto en el que el servidor escuchará las conexiones.
     */
    public void start(int port) {
        try {
            // Se crea un ServerSocket que escucha conexiones en el puerto especificado.
            serverSocket = new ServerSocket(port);
            System.out.println("Server running on port " + port);

            // El servidor se queda en un bucle infinito para aceptar conexiones entrantes.
            while (true) {
                // El servidor acepta una conexión entrante y crea un socket para comunicarse con el cliente.
                clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Se agrega el socket del cliente a una lista de clientes conectados.
                clients.add(clientSocket);

                // Se crea un nuevo hilo para manejar la comunicación con el cliente.
                // Cada cliente se gestiona en un hilo separado para permitir múltiples conexiones simultáneas.
                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Se imprime la traza de la excepción en caso de error al establecer el servidor.
        
        }
    }
    

    /**
     * Envía un mensaje a todos los clientes conectados, excepto al cliente que
     * lo envió.
     *
     * @param message Mensaje a ser transmitido a los clientes.
     * @param sender Socket del cliente que envió el mensaje, para evitar el
     * reenvío al mismo cliente.
     */
    public void broadcast(String message, Socket sender) {
        for (Socket client : clients) {
            // Itera sobre la lista de clientes conectados.
            if (!client.equals(sender)) { // Comprueba que el cliente no sea el remitente original del mensaje.
                try {
                    // Crea un PrintWriter para escribir en el flujo de salida del socket del cliente.
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                    // Envía el mensaje al cliente.
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace(); // Si hay un error al enviar el mensaje, imprime la traza de la excepción.
                }
            }
        }
    }

    /**
     * Método principal que inicia el servidor y escucha en un puerto
     * específico.
     *
     *
     */
    public static void main(String[] args) {
        MultiClientTCPServer server = new MultiClientTCPServer();
        server.start(8888);
    }

    /**
     * Clase interna que maneja la comunicación con un cliente.
     */
    class ClientHandler implements Runnable {

        private Socket clientSocket;
        private BufferedReader reader;

        /**
         * Constructor de la clase ClientHandler que maneja la comunicación con
         * un cliente.
         *
         * @param socket Socket del cliente con el que se establece la
         * comunicación.
         */
        public ClientHandler(Socket socket) {
            try {
                this.clientSocket = socket; // Asigna el socket del cliente a la variable de la clase.

                // Se crea un BufferedReader para leer del flujo de entrada del socket del cliente.
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace(); // Si hay un error al establecer el BufferedReader, se imprime la traza de la excepción.
            }
        }

        @Override
        public void run() {
            
            String message="";
            try {
                // El hilo se mantiene en un bucle para leer mensajes entrantes del cliente.
                while ((message = reader.readLine()) != null) {
                    // Se imprime el mensaje recibido en la consola, indicando que se ha recibido un mensaje.
                    System.out.println("Received: " + message);

                    // Se llama al método "broadcast" para enviar el mensaje a todos los clientes, excepto al que lo envió.
                    broadcast(message, clientSocket);
                }
            } catch (IOException e) {
                
                //JOptionPane.showMessageDialog(null,obtenerCliente(message)+" se ha desconetado"); // Si hay un error al leer los mensajes del cliente, se imprime la traza de la excepción.
                    JOptionPane.showMessageDialog(null, "NO HAY USUARIOS EN LINEA");
            
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private String obtenerCliente(String message){
        String nombre="";
        for (int i = 0; i < message.length(); i++) {
            if(!(message.substring(i,i+1).equals(" "))){
                nombre=nombre.concat(message.substring(i, i+1));
            }else{
                break;
            }
            
            
        }
        
        
        return nombre;
    }
}
