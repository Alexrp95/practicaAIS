package buscaminas;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.Timer;

 
import javax.swing.*;
public class Buscaminas extends JFrame implements ActionListener, MouseListener{
    
    //Las variables que pone "static" al inicio es porque las uso dentro del metodo Main y necesitan ser static o sino el programa no funciona
    static String dificultadV; //se introduce en el constructor para establecer el tamaño y la dificultad del buscaminas
    JComboBox dificultadBox; //Son elementos del swing para crear el menu
    JDialog dificultadPanel; //Son elementos del swing para crear el menu
    JLabel dificultadLabel; //Son elementos del swing para crear el menu
    JLabel CargarLabel; //Son elementos del swing para crear el menu
    JLabel contador; //contador donde almacenaremos el numero de minas
    JButton Botonjugar; //Son elementos del swing para crear el menu
    JMenuBar Menu; //Son elementos del swing para crear el menu
    static JTextField nTextField;
    static JTextField mTextField;
    static JTextField minasTextField;
    String variableValorBoolean1; //Variables creadas para intentar guardar un Booleano dentro de un fichero de texto, de momento no hacen falta
    String variableValorBoolean2; //Variables creadas para intentar guardar un Booleano dentro de un fichero de texto, de momento no hacen falta
    int[] tiempos= {0,0,0,0,0,0,0,0,0,0}; //Arrays que almacenan los 10 mejores tiempos de cada dificultad
    int[] tiemposI = {0,0,0,0,0,0,0,0,0,0}; //Arrays que almacenan los 10 mejores tiempos de cada dificultad
    int[] tiemposE = {0,0,0,0,0,0,0,0,0,0}; //Arrays que almacenan los 10 mejores tiempos de cada dificultad
    String nombreDeJugador; //String que almacena el nombre de jugador que rellenas cuando ganas una partida(es opcional)
    int tiempoGanador; //Variable que almacena el tiempo cuando ganas una partida
    int nomines; //el numero de minas que tendra el buscaminas
    int perm[][];
    String tmp;
    boolean found = false; //booleano que muestra si algo ha sido encontrado o no
    int row; //variable que le da valor a las filas
    int column; //variable que le da valor a las columnas
    int guesses[][];
    JButton b[][]; //Boton donde se almacenaran datos en un array de 2 dimensiones que seran las teclas que pulsas en el buscaminas los "?"
    int[][] mines; //array de 2 dimensiones donde se almacenan minas
    boolean allmines; //un booleano que servira para detectar si se han acabado las minas del programa o han sido localizadas todas
    int n; //variable que se usa para generar la matriz del buscaminas y el layout
    int m; //variable que se usa para generar la matriz del buscaminas y el layout
    static int nPerson; //variable que uso para almacenar el valor del usuario al introducir cuantas filas quiere en la dificultad Personalizado
    static int mPerson; //variable que uso para almacenar el valor del usuario al introducir cuantas columnas quiere en la dificultad Personalizado
    static int minesPerson; //variable que uso para almacenar el valor del usuario al introducir cuantas minas quiere en la dificultad Personalizado
    int deltax[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    int deltay[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    double starttime; //Variable que se usa para calcular el tiempo que ha durado la partida
    double endtime; //Variable que se usa para calcular el tiempo que ha durado la partida
    int contadorMinas; //contador que muestra el numero de minas que quedan en partida
    int tiempoPartida; //contador que muestra el tiempo que transcurre en la partida
    static Timer timer; //variable que ayuda a que se muestre el tiempo de forma correcta y se actualize el jlabel
    //Constructor principal
    public Buscaminas(String dificultad){ 
        
        if("PRINCIPIANTE".equals(dificultad)){
            n = 10;
            m = 10;
            nomines = 10;
            contadorMinas= nomines;
        }
        else 
          if("INTERMEDIO".equals(dificultad)){
            n =16;
            m = 16;
            nomines = 40;
            contadorMinas= nomines;
        }
         else 
          if("EXPERTO".equals(dificultad)){
            n =32;
            m = 16;
            nomines = 99;
            contadorMinas= nomines;
        }
        else
              if("PERSONALIZADO".equals(dificultad)){
                  n= Integer.parseInt(nTextField.getText());
                  m = Integer.parseInt(mTextField.getText());
                  nomines = Integer.parseInt(minasTextField.getText());
              }
        
        //Elementos del Menu que hay en la cabecera de la ventana
        JMenuBar Menu = new JMenuBar(); //Barra del menu de la ventana del buscaminas
        JMenu Guardar = new JMenu("Guardar Partida"); //opciones del menu de JMenuBar
        JMenu Cargar = new JMenu("Cargar Partida");
        JMenu Reiniciar = new JMenu("Reiniciar Partida");
        contador = new JLabel(); //contador que mostrara el numero de minas
        contador.setText("Minas:"+contadorMinas+""); //añadimos la variable contador de minas que se ira sumando y restando al hacer click derecho
        JLabel tiempoPartidaBuscaminas= new JLabel(); //Etiqueta que almacena el tiempo de la partida
        tiempoPartidaBuscaminas.setText("Tiempo: "+tiempoPartida);
        
        TimerTask tempo; //Elemento de la clase TimerTask que junto con el Timer timer, creado en la seccion de variables hacen que el 
        //tiempo se pueda mostar adecuadamente en el label

        
        timer = new Timer(); //creamos el timer
        tempo= new TimerTask() { //creamos el timerTask
            @Override
            public void run() {
                tiempoPartida++;
                tiempoPartidaBuscaminas.setText(" Tiempo: "+tiempoPartida); //modificamos el valor de la etiqueta Label para que se actualize
            }
        };
        timer.schedule(tempo,0, 1000); //es una funcion que hace posible que el tiempo transcurra en segundos desde 0 hasta 999
        //Añadimos los elementos que deseamos que tengan las opciones de la ventana del buscaminas
        Menu.add(Guardar);
        Menu.add(Cargar);
        Menu.add(Reiniciar);
        Menu.add(contador); // contador de minas
        Menu.add(tiempoPartidaBuscaminas); //contador de tiempo
        JMenuItem guardar1 = new JMenuItem("Guardar");
        JMenuItem cargar1 = new JMenuItem("Cargar");
        JMenuItem reiniciar1 = new JMenuItem("Reiniciar");
        Guardar.add(guardar1);
        Cargar.add(cargar1);
        Reiniciar.add(reiniciar1);
        Menu.setVisible(true);
        
        
        //Accion del boton guardar, llama al metodo AccionGuardar que sera explicado mas adeltante
        guardar1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionGuardar();
                               
			}
		});
        //Accion del boton cargar, llama al metodo AccionCargar que sera explicado mas adeltante
        cargar1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionCargar();
                               
			}
		});  
        //Accion del boton reiniciar, llama al metodo ReiniciarPartida que sera explicado mas adeltante
        reiniciar1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               dispose();
                               ReiniciarPartida();
                               
			}
		}); 
        

        perm = new int[n][m]; //rellena el array PERM con los valores de n y de m
        boolean allmines = false; //establece el booleano de todas las minas descubiertas a 0
        guesses = new int [n+2][m+2]; //rellena el array de guesses con los valores de n+2 y de m+2, y los usara mas abajo en los bucles for para construir la pared de numeros 3s
        mines = new int[n+2][m+2]; //rellena el array de minas con los valores de n+2 y m+2 y los usara mas abajo en los bucles for para construir la pared de numetros 3s
        b = new JButton [n][m]; //crea el boton B que servira para el manejo del buscaminas, son los botones que pulsas para desvelar las casillas
        setLayout(new GridLayout(n,m)); //establece los valores del layout de la ventana del buscaminas
 
        for (int y = 0;y<m+2;y++){ //establece un borde de numeros:3 al rededor de la matriz del buscaminas de las filas, funciona como una especie de "pared"
            mines[0][y] = 3;       //se pueden ver estos 3s abajo en la salida "output" del programa
            mines[n+1][y] = 3;
            guesses[0][y] = 3;
            guesses[n+1][y] = 3;
        }
        for (int x = 0;x<n+2;x++){ //lo mismo que el for de arriba pero para las columnas
            mines[x][0] = 3;
            mines[x][m+1] = 3;
            guesses[x][0] = 3;
            guesses[x][m+1] = 3;
        }
        do {
            int check = 0; //crea una variable check y la inicializa a 0, la usara como contador para saber si el usuario ha descubierto minas o no
            for (int y = 1;y<m+1;y++){ //recorre 2 for y llena los array de minas y de "guesses" con 0, mas abajo rellenara el bsucaminas con bombas
                for (int x = 1;x<n+1;x++){
                    mines[x][y] = 0;
                    guesses[x][y] = 0;
                }
            }
            for (int x = 0;x<nomines;x++){ //establece la posicion aleatoria de las minas y rellena el array de minas
                mines [(int) (Math.random()*(n)+1)][(int) (Math.random()*(m)+1)] = 1;
            }
            for (int x = 0;x<n;x++){ //recorre los bucles for y si en las casillas del array mines hay un 1, es que hay una bomba y aumenta el valor de check
                for (int y = 0;y<m;y++){    //que inicializo arriba
                if (mines[x+1][y+1] == 1){
                        check++;
                    }
                }
            }
            if (check == nomines){ //si la variable check es igual al numero de minas establece el booleano de "allmines" a true y entra por elñ bucle del if
                allmines = true; //significando que ya no quedan minas
            }
        }while (allmines == false); //en while se utiliza para realizar un bucle, MIENTRAS no se hayan descubierto todas las minas ocurre los for de abajo
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                if ((mines[x+1][y+1] == 0) || (mines[x+1][y+1] == 1)){ //si en el array de minas hay un 0 o un 1 en la posicion x+1 o y+1
                    perm[x][y] = perimcheck(x,y); //almacena la variable perimcheck(x,y) en las posiciones del array PERM
                }
                b[x][y] = new JButton("?"); //crea las casillas que se van a utilizar para jugar
                b[x][y].addActionListener(this); //le añade la funcionalidad de la accion que tiene que realizar al ser clickeado el boton B
                b[x][y].addMouseListener(this); //le añade la funcionalidad del boton B al ser pulsado
                add(b[x][y]); //añade el boton B creado anteriormente
                b[x][y].setEnabled(true); //establece que el boton este disponible para ser clickado o pulsado
            }//end inner for
        }//end for
        pack();
        setVisible(true); //establece como visible la ventana del bsucaminas
        setJMenuBar(Menu); //establece el menu creado arriba en la ventana del buscaminas
        
        setLocationRelativeTo(null); //establece la ventana del buscaminas en el centro de la pantalla del pc
        
        for (int y = 0;y<m+2;y++){ //recorre mediante un for los arrays que contienen las variables n y m y muestra la posicion de las minas
            for (int x = 0;x<n+2;x++){
                System.out.print(mines[x][y]);
            }
        System.out.println("");} //Muestra un espacio para separar las variables
        starttime = System.nanoTime(); //inicializa la variable starttime con el metodo nanoTime(); que recoge un valor de tiempo
      
    }//end constructor Mine()
 
    
    //el mismo constructor que el anterior solo que el valor de las variables se las pasamos por parametros
    public Buscaminas(String dificultad, int n, int m, int[][] mines, int[][] guesses, JButton[][] b, boolean allmines, int contadorMinas, int nomines, int [][]perm){ 
        
        if("PRINCIPIANTE".equals(dificultad)){
            n = 10;
            m = 10;
            nomines = 10;
            contadorMinas= nomines;
        }
        else 
          if("INTERMEDIO".equals(dificultad)){
            n =16;
            m = 16;
            nomines = 40;
            contadorMinas= nomines;
        }
         else 
          if("EXPERTO".equals(dificultad)){
            n =32;
            m = 16;
            nomines = 99;
            contadorMinas= nomines;
        }
        else
              if("PERSONALIZADO".equals(dificultad)){
                  n= Integer.parseInt(nTextField.getText());
                  m = Integer.parseInt(mTextField.getText());
                  nomines = Integer.parseInt(minasTextField.getText());
              }
        
        //Elementos del Menu que hay en la cabecera de la ventana
        JMenuBar Menu = new JMenuBar();
        JMenu Guardar = new JMenu("Guardar Partida");
        JMenu Cargar = new JMenu("Cargar Partida");
        JMenu Reiniciar = new JMenu("Reiniciar Partida");
        JLabel contador = new JLabel();
        contador.setText("Minas"+ contadorMinas+"");
        JLabel tiempoPartidaBuscaminas= new JLabel(); 
        tiempoPartidaBuscaminas.setText("Tiempo: "+tiempoPartida);
        
        TimerTask tempo;

        
        timer = new Timer();
        tempo= new TimerTask() {
            @Override
            public void run() {
                tiempoPartida++;
                tiempoPartidaBuscaminas.setText(" Tiempo: "+tiempoPartida);
            }
        };
        timer.schedule(tempo,0, 1000);
       
        Menu.add(Guardar);
        Menu.add(Cargar);
        Menu.add(Reiniciar);
        Menu.add(contador);
        Menu.add(tiempoPartidaBuscaminas);
        JMenuItem guardar1 = new JMenuItem("Guardar");
        JMenuItem cargar1 = new JMenuItem("Cargar");
        JMenuItem reiniciar1 = new JMenuItem("Reiniciar");
        Guardar.add(guardar1);
        Cargar.add(cargar1);
        Reiniciar.add(reiniciar1);
        Menu.setVisible(true);
        
        
        //Accion del boton guardar, llama al metodo AccionGuardar
        guardar1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionGuardar();
                               
			}
		});
        //Accion del boton cargar, llama al metodo AccionCargar
        cargar1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionCargar();
                               
			}
		});  
        //Accion del boton reiniciar, llama al metodo ReiniciarPartida
        reiniciar1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               dispose();
                               ReiniciarPartida();
                               
			}
		}); 
        

        perm = new int[n][m];
 
        guesses = new int [n+2][m+2];
        mines = new int[n+2][m+2];
        b = new JButton [n][m];
        setLayout(new GridLayout(n,m));
 
        for (int y = 0;y<m+2;y++){
            mines[0][y] = 3;
            mines[n+1][y] = 3;
            guesses[0][y] = 3;
            guesses[n+1][y] = 3;
        }
        for (int x = 0;x<n+2;x++){
            mines[x][0] = 3;
            mines[x][m+1] = 3;
            guesses[x][0] = 3;
            guesses[x][m+1] = 3;
        }
        do {
            int check = 0;
            for (int y = 1;y<m+1;y++){
                for (int x = 1;x<n+1;x++){
                    mines[x][y] = 0;
                    guesses[x][y] = 0;
                }
            }
            for (int x = 0;x<nomines;x++){
                mines [(int) (Math.random()*(n)+1)][(int) (Math.random()*(m)+1)] = 1;
            }
            for (int x = 0;x<n;x++){
                for (int y = 0;y<m;y++){
                if (mines[x+1][y+1] == 1){
                        check++;
                    }
                }
            }
            if (check == nomines){
                allmines = true;
            }
        }while (allmines == false);
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                if ((mines[x+1][y+1] == 0) || (mines[x+1][y+1] == 1)){
                    perm[x][y] = perimcheck(x,y);
                }
                b[x][y] = new JButton("?");
                b[x][y].addActionListener(this);
                b[x][y].addMouseListener(this);
                add(b[x][y]);
                b[x][y].setEnabled(true);
            }//end inner for
        }//end for
        pack();
        setVisible(true);
        setJMenuBar(Menu);
        
        setLocationRelativeTo(null);
        
        for (int y = 0;y<m+2;y++){
            for (int x = 0;x<n+2;x++){
                System.out.print(mines[x][y]);
            }
        System.out.println("");}
        starttime = System.nanoTime();
      
    }//end constructor Mine()
    
    
    public void actionPerformed(ActionEvent e){ //unn metodo que proporciona un evento de acción 
        found =  false; //si el booleano de encontrado esta a false
        JButton current = (JButton)e.getSource();
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                JButton t = b[x][y]; //crea un boton T y le añade los valores del boton actual, depende de la posicion del for
                if(t == current){
                    row=x;column=y; found =true; //si la columna y la fila coinciden con las seleccionadas, pone la variable found a true, es decir
                                                    //ha encontrado la casilla que el usuario quiere pulas
                }
            }//end inner for
        }//end for
        if(!found) { //si no ha encontrado el boton que quiere pulsar el usuario salta un error y cierra la ventana
            System.out.println("didn't find the button, there was an error "); System.exit(-1);
        }
        Component temporaryLostComponent = null;
        if (b[row][column].getBackground() == Color.orange){ //establece de color naranja el boton que ha sido seleccionado
            return;
        }else if (mines[row+1][column+1] == 1){ //si pinchas en una casilla donde hay una mina, pierdes y el juego se cierra
            JOptionPane.showMessageDialog(temporaryLostComponent, "You set off a Mine!!!!.");
                     
                  System.exit(0);
                       
            
        } else {
            tmp = Integer.toString(perm[row][column]); //recoge los valores de row y column almacenados en el array PERM, los transforma en un String 
                                                        //los almacena en tmp
            if (perm[row][column] == 0){ //si en el array perm en la posicion de valores row y column hay un 0, tmp valdra un espacio " "
                    tmp = " ";
            }
            b[row][column].setText(tmp); //establece el texto almacenado en la variable tmp, sacado de arriba y lo establece en la casilla que tiene
            b[row][column].setEnabled(false); //como posicion row y column
            checkifend(); //llama al metodo checkifend
            if (perm[row][column] == 0){
                scan(row, column);
                checkifend();
            }
        }
    }
 
    public void checkifend(){ //metodo que se establece para ver si la partida ha acabado o no
        int check= 0;
        for (int y = 0; y<m;y++){ //recorre dos For con las variables n y m y establece el boton b como disponible y aumenta el contador check en 1.
            for (int x = 0;x<n;x++){
        if (b[x][y].isEnabled()){
            check++;
        }
            }}
        if (check == nomines){ //si el contador de check llega al numero de minas establece el endtime con el metodo nanotime();
            endtime = System.nanoTime();
            Component temporaryLostComponent = null;
            
            JTextField nombreJugador = new JTextField(10); //etiqueta que almacenara el valor del nombre de jugador
            
           //He puesto un nuevo mensaje cuando ganas la partida, en este nuevo mensaje se muestra un campo para rellenar el nombre del ganador y 
           //se guarda el tiempo, la linea comentada de abajo es el anitiguo metodo, como lo tenia el codigo inicial
           // JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!" + "Si quieres guardar tu tiempo: Añade un Nombre De Jugador");
            nombreDeJugador = JOptionPane.showInputDialog("Congratulations you won!!! It took you " +(int)((endtime-starttime)/1000000000)+" seconds!. Escribe tu nombre para guardar tu tiempo"); 
            if(nombreDeJugador != null){
                AccionGuardarTiempoJugador();
            }
               GuardarTiempo();
               System.exit(0);
             
        
        }
    }
    //Metodo que llama al JFileChooser, una ventana para elegir donde guardar un fichero dentro de tu pc
    
    public void AccionGuardarTiempoJugador(){
            JFileChooser selectorFichero = new JFileChooser();
            selectorFichero.setDialogTitle("Guardar Nombre y Tiempo");
            selectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = selectorFichero.showSaveDialog(this); //almacena el valor del selector de ficheros en la variable resultado
        if (resultado == JFileChooser.APPROVE_OPTION) { //si la ruta del fichero es posible y el fichero concuerda con el tipo entonces se guarda bien
            boolean resultadoOK = GuardarTiempoJugador(selectorFichero.getSelectedFile().getAbsolutePath());
            if (resultadoOK) {
                JOptionPane.showMessageDialog(this, "Fichero guardado correctamente", "Guardar ", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lo sentimos, fichero NO guardado", "Guardar ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //Metodo que se usa para guardar el tiempo del ganador, en un fichero de texto TXT, tambien se puede guardar en una variable como la lista de los 10
    //mejores tiempos
    public boolean GuardarTiempoJugador(String rutaFichero){
        try {
            
            FileWriter fw= new FileWriter(rutaFichero);
            BufferedWriter bw = new BufferedWriter(fw);
            
            tiempoGanador = (int)((endtime-starttime)/1000000000); //establezco una variable de tiempo en la que meto el tiempo de inicio de partida y el 
                                                                    //tiempo de finalizado de la partida
            bw.write("Nombre de Jugador:"+""+nombreDeJugador+" "+ "Tiempo Total:"+""+tiempoGanador+System.getProperty("line.separator"));
                                                                    //se guarda el nombre de jugador y el tiempo y se separa con el metodo de line separator

            bw.flush(); //se comprueba que el fichero ha sido usado y finalizado su uso de forma correcta, como cuando para desconectar un pen drive el pc te dice que lo desconectes de forma segura, el flush hace la misma funcion
            bw.close(); //se cierra el bufferedWriter y se acaba de usar el fichero
        } catch (Exception ex) { //captura todo tipo de excepciones y devuelve un false, porque si ha habido excepciones no se ha guardado bien el fichero
            return false;
        }
        return true; //se devuelve un true porque el fichero ha sido guardado de forma correcta
        
    }
    
    //Segun la dificultad que el jugador eliga su tiempo se guardara en un array u en otro, si cumple los requisitos: ser un mejor tiempo de los que
    //ya hay en el propio array
    public void GuardarTiempo(){


        int tiempo = (int)((endtime-starttime)/1000000000);
switch (dificultadV) {
      case "PRINCIPIANTE":
         
         
        for(int i=0; i<10; i++){
	 if(tiempos[i] == 0){
            tiempos[i]= tiempo;
            break; //la sentencia break salta para que se salga de forma forzada de un bucle
         }
         if(tiempos.length == 10 && tiempo < tiempos[i]){
            tiempos[i]= tiempo;
            break;
         }
          }
        try{
                File archivo; //se crea el archivo
                String ruta ="Mejores 10 Tiempos Buscaminas.txt"; //se establece ese nombre de fichero y la ruta
                archivo = new File (ruta); //se creara el archivo en la carpeta del proyecto de NeatBeans donde tengamos el buscaminas
                archivo.createNewFile();
             FileWriter fw= new FileWriter(archivo);
             BufferedWriter bw = new BufferedWriter(fw);
                  for(int i=0; i<tiempos.length;i++){
                      bw.write(" "+tiempos[i]+ System.getProperty("line.separator")); //recorre un bucle y guarda los tiempos 
                   }
              bw.flush();
              bw.close();
        }catch(IOException e){}
              
              
        System.out.println("Tu tiempo se ha guardado");
        System.out.println(Arrays.toString(tiempos));
           break;
      case "INTERMEDIO":
          for(int i=0; i<10; i++){
	 if(tiemposI[i] == 0){
            tiemposI[i]= tiempo;
            break;
         }
         if(tiempos.length == 10 && tiempo < tiempos[i]){
            tiemposI[i]= tiempo;
            break;
         }
          }
          try{
                File archivo;
                String ruta ="Mejores 10 Tiempos Buscaminas.txt";
                archivo = new File (ruta);
                archivo.createNewFile();
             FileWriter fw= new FileWriter(archivo);
             BufferedWriter bw = new BufferedWriter(fw);
                  for(int i=0; i<tiempos.length;i++){
                      bw.write(" "+tiemposI[i]+ System.getProperty("line.separator"));
                   }
              bw.flush();
              bw.close();
        }catch(IOException e){}
          
          
          System.out.println("Tu tiempo se ha guardado");
          System.out.println(Arrays.toString(tiemposI));
           break;
      case "EXPERTO":
          for(int i=0; i<10; i++){
	 if(tiemposE[i] == 0){
            tiemposE[i]= tiempo;
            break;
         }
         if(tiempos.length == 10 && tiempo < tiempos[i]){
            tiemposE[i]= tiempo;
            break;
         }
          }
          try{
                File archivo;
                String ruta ="Mejores 10 Tiempos Buscaminas.txt";
                archivo = new File (ruta);
                archivo.createNewFile();
             FileWriter fw= new FileWriter(archivo);
             BufferedWriter bw = new BufferedWriter(fw);
                  for(int i=0; i<tiempos.length;i++){
                      bw.write(" "+tiemposE[i]+ System.getProperty("line.separator"));
                   }
              bw.flush();
              bw.close();
        }catch(IOException e){}
          
          System.out.println("Tu tiempo se ha guardado");
          System.out.println(Arrays.toString(tiemposE));
           break;
           
           
      default:
           System.out.println("Tu tiempo no esta entre los 10 mejores. No se puede guardar");
           break;
      }
        
        
	}
    //Mismo que llama al JFileChooser para abrir el selector de ficheros y guardar un fichero
    public void AccionGuardar(){
         JFileChooser selectorFichero = new JFileChooser();
            selectorFichero.setDialogTitle("Guardar Partida");
            selectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = selectorFichero.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            boolean resultadoOK = GuardarPartida(selectorFichero.getSelectedFile().getAbsolutePath());
            if (resultadoOK) {
                JOptionPane.showMessageDialog(this, "Fichero guardado correctamente", "Guardar Partida", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lo sentimos, fichero NO guardado", "Guardar Partida", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //Mismo que llama al JFileChooser para abrir el selector de ficheros y cargar un fichero
    public void AccionCargar(){
        
        JFileChooser selectorFichero = new JFileChooser();
        selectorFichero.setDialogTitle("Restaurar Partida");
        selectorFichero.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = selectorFichero.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            boolean resultadoOK = CargarPartida(selectorFichero.getSelectedFile().getAbsolutePath());
            if (resultadoOK) {
                JOptionPane.showMessageDialog(this, "Fichero cargado correctamente", "Restaurar Partida", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Fichero NO cargado", "Restaurar Partida", JOptionPane.ERROR_MESSAGE);
            }
              new Buscaminas(dificultadV, n, m, mines,guesses,b,allmines,contadorMinas,nomines, perm);
        }
          new Buscaminas(dificultadV, n, m, mines,guesses,b,allmines,contadorMinas,nomines, perm);
    }
    //Metodo que reinicia la partida, llama de nuevo a un buscaminas el anterior buscaminas se cierra al pulsar el boton de reiniciar partida
    public void ReiniciarPartida(){
        
       Buscaminas buscaminas = new Buscaminas(dificultadV);
    }   
   

    public void scan(int x, int y){
        for (int a = 0;a<8;a++){
            if (mines[x+1+deltax[a]][y+1+deltay[a]] == 3){
 
            } else if ((perm[x+deltax[a]][y+deltay[a]] == 0) && (mines[x+1+deltax[a]][y+1+deltay[a]] == 0) && (guesses[x+deltax[a]+1][y+deltay[a]+1] == 0)){
                if (b[x+deltax[a]][y+deltay[a]].isEnabled()){
                    b[x+deltax[a]][y+deltay[a]].setText(" ");
                    b[x+deltax[a]][y+deltay[a]].setEnabled(false);
                    scan(x+deltax[a], y+deltay[a]);
                }
            } else if ((perm[x+deltax[a]][y+deltay[a]] != 0) && (mines[x+1+deltax[a]][y+1+deltay[a]] == 0)  && (guesses[x+deltax[a]+1][y+deltay[a]+1] == 0)){
                tmp = new Integer(perm[x+deltax[a]][y+deltay[a]]).toString();
                b[x+deltax[a]][y+deltay[a]].setText(Integer.toString(perm[x+deltax[a]][y+deltay[a]]));
                b[x+deltax[a]][y+deltay[a]].setEnabled(false);
            }
        }
    }
 
    public int perimcheck(int a, int y){
        int minecount = 0; //establece un contador de minas
        for (int x = 0;x<8;x++){
            if (mines[a+deltax[x]+1][y+deltay[x]+1] == 1){ //si el bucle encuentra una mina sube en 1 el contador de minas
                minecount++;
            }
        }
        return minecount; //devuelve el contador de minas
    }
 
    
   
    public void windowIconified(WindowEvent e){ //metodo que proporciona un evento de ventana
 
    }
 
    public static void main(String[] args){ //metodo Main que es el primero que se inicializa al darle a run a la aplicacion

        //Todos los componentes de la primera ventana que se abre nada mas iniciar el programa, dando paso a la elección de la dificultad
        
        
        JDialog dificultadPanel = new JDialog();
        JLabel dificultadLabel = new JLabel("Escoge la dificultad del nuevo buscaminas");
        
        JComboBox dificultadBox = new JComboBox();
		dificultadBox.addItem("PRINCIPIANTE");
		dificultadBox.addItem("INTERMEDIO");
		dificultadBox.addItem("EXPERTO");
                dificultadBox.addItem("PERSONALIZADO");
                
        JLabel personalizadoLabel = new JLabel("Buscaminas Personalizado");
        JLabel filaLabel = new JLabel("Filas"); 
        nTextField = new JTextField(10);
        JLabel columnaLabel = new JLabel("Columnas"); 
        mTextField = new JTextField(10);
        JLabel minasLabel = new JLabel("Minas");  
        minasTextField = new JTextField(10);
        
       // JFormattedTextField nTextField = new JFormattedTextField (new Integer());
       // JFormattedTextField mTextField = new JFormattedTextField (new Integer());
       // JFormattedTextField minasTextField = new JFormattedTextField (new Integer());
        
        JButton Botonjugar = new JButton("Jugar");
        
        //Accion del boton Jugar, basicamente recoge el valor de la dificultad seleccionada en el Box y lo mete al constructor e inicializa el juego
        Botonjugar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            
                            String dificultad = (String) dificultadBox.getSelectedItem();
                            dificultadV = dificultad;
				new Buscaminas(dificultadV);
                                 /*for(segundos =0;segundos<=999;segundos++){
                                       System.out.println(segundos);
                                       delaySegundo();
                                     }*/
        
			}
		});
        
        
        //Incluyo dentro del Panel/Ventana que se abre todos los componentes que quiero que esten
        dificultadPanel = new JDialog();
		dificultadPanel.getContentPane().setLayout(new FlowLayout());
                dificultadPanel.getContentPane().add(dificultadLabel);
		dificultadPanel.getContentPane().add(dificultadBox);
                dificultadPanel.getContentPane().add(Botonjugar); 
                dificultadPanel.getContentPane().add(personalizadoLabel);
                dificultadPanel.getContentPane().add(filaLabel);
                dificultadPanel.getContentPane().add(nTextField);
                dificultadPanel.getContentPane().add(columnaLabel);
                dificultadPanel.getContentPane().add(mTextField);
                dificultadPanel.getContentPane().add(minasLabel);
                dificultadPanel.getContentPane().add(minasTextField);
		dificultadPanel.pack();
		dificultadPanel.setVisible(true);
                dificultadPanel.setLocationRelativeTo(null);
        
    }
 
    public void mouseClicked(MouseEvent e) { //evento del raton al ser usado un click
 
    }
 
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void mousePressed(MouseEvent e) { //evento del boton cuando pulsas el click derecho
        if (e.getButton() == MouseEvent.BUTTON3) {
            found =  false; //establece la variable de encontrado a false
            Object current = e.getSource(); //crea un objeto y lo llama current, le establece el valor del objeto e
            for (int y = 0;y<m;y++){        //metodo que funciona igual que uno comentado anteriormente, si la casilla que el usuario quiere pulsar
                    for (int x = 0;x<n;x++){    //existe, encontonces devuelve un true
                            JButton t = b[x][y];
                            if(t == current){
                                    row=x;column=y; found =true;
                            }
                    }//end inner for
            }//end for
            if(!found) {    //si no encuentra la casilla se produce un error
                System.out.println("didn't find the button, there was an error "); System.exit(-1);
            }
            if ((guesses[row+1][column+1] == 0) && (b[row][column].isEnabled())){ //si la casilla esta disponible y en el array de guesses hay un 0
                b[row][column].setText("x");    //al usar el click derecho se marca la casilla con una x
                guesses[row+1][column+1] = 1;
                b[row][column].setBackground(Color.orange); //se pone la casilla en naranja
                 contadorMinas--; //y se descuenta el contador de minas en 1
                 contador.setText("Minas:"+ contadorMinas+""); //se actualiza el contador de las minas dentro del jeugo
                 
                } else if (guesses[row+1][column+1] == 1){ //metodo que hace lo mismo que el anterior pero si pulsas con el click derecho en una casilla
                b[row][column].setText("?");        //que tiene una x y no un ?
                guesses[row+1][column+1] = 0;
                b[row][column].setBackground(null);     //se sube en 1 el contador de minas y se actualiza dentro del juego
                 contadorMinas++;
                 contador.setText("Minas:"+ contadorMinas);
            }
        }
    }
 
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
    //Meter todas las variables en un fichero binario para "Guardar" la partida actual tal y como esta en el momento
   public boolean GuardarPartida(String rutaFichero) {
        try {
                FileOutputStream fos= new FileOutputStream(rutaFichero);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                
                oos.writeInt(n);
                oos.writeInt(m);
                oos.writeInt(nomines);
                oos.writeObject(mines);
                oos.writeBoolean(allmines);
                oos.writeObject(deltax);
                oos.writeObject(deltay);
                oos.writeInt((int)starttime);
                oos.writeInt((int)endtime);
                oos.writeObject(guesses);
                oos.writeInt(column);
                oos.writeInt(row);
                oos.writeObject(perm);
                oos.writeObject(tmp);
                oos.writeBoolean(found);
                oos.writeInt(contadorMinas);
                oos.writeObject(dificultadV);
                
                for(int i = 0;i < n;i++){
                    for (int j = 0; j < m; j++){
                        oos.writeObject(b[i][j].getText());                
                    }
                }
            
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    //Cargar todas las variables guardadas en el fichero en variables del programa para Cargar la partida guardada anteriormente
   public boolean CargarPartida(String rutaFichero) {
        try {
            FileInputStream fis = new FileInputStream(rutaFichero);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            
            this.n = (int) ois.readObject();
            this.m = (int)ois.readObject();
            this.nomines = (int)ois.readObject();
            this.mines = (int[][])ois.readObject();
            this.allmines = (boolean)ois.readObject();
            this.deltax = (int[]) ois.readObject();
            this.deltay = (int[])ois.readObject();
            this.starttime = (double) ois.readObject();
            this.endtime = (double) ois.readObject();
            this.guesses= (int[][])ois.readObject();
            this.column = (int)ois.readObject();
            this.row = (int)ois.readObject();
            this.perm = (int[][])ois.readObject();
            this.tmp = (String)ois.readObject();
            this.found = (boolean)ois.readObject();
            this.contadorMinas = (int)ois.readObject();
            this.dificultadV = (String)ois.readObject();
               
               
               for(int k = 0;k < n;k++){
                    for (int j = 0; j < m; j++){
                        this.b[k][j] = new JButton((String)ois.readObject());
                    }
                }
          

            ois.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
   
  
   
}//end class