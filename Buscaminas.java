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
    JLabel contador;
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
    int nomines;
    int perm[][];
    String tmp;
    boolean found = false;
    int row;
    int column;
    int guesses[][];
    JButton b[][];
    int[][] mines;
    boolean allmines;
    int n;
    int m;
    static int nPerson; //variable que uso para almacenar el valor del usuario al introducir cuantas filas quiere en la dificultad Personalizado
    static int mPerson; //variable que uso para almacenar el valor del usuario al introducir cuantas columnas quiere en la dificultad Personalizado
    static int minesPerson; //variable que uso para almacenar el valor del usuario al introducir cuantas minas quiere en la dificultad Personalizado
    int deltax[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    int deltay[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    double starttime; //Variable que se usa para calcular el tiempo que ha durado la partida
    double endtime; //Variable que se usa para calcular el tiempo que ha durado la partida
    int contadorMinas; //contador que muestra el numero de minas que quedan en partida
    int tiempoPartida;
    static Timer timer;
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
        JMenuBar Menu = new JMenuBar();
        JMenu Guardar = new JMenu("Guardar Partida");
        JMenu Cargar = new JMenu("Cargar Partida");
        JMenu Reiniciar = new JMenu("Reiniciar Partida");
        contador = new JLabel(); //contador que mostrara el numero de minas
        contador.setText("Minas:"+contadorMinas+""); //añadimos la variable contador de minas que se ira sumando y restando al hacer click derecho
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
        //Añadimos los elementos al menu
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
        boolean allmines = false;
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
    
    
    public void actionPerformed(ActionEvent e){
        found =  false;
        JButton current = (JButton)e.getSource();
        for (int y = 0;y<m;y++){
            for (int x = 0;x<n;x++){
                JButton t = b[x][y];
                if(t == current){
                    row=x;column=y; found =true;
                }
            }//end inner for
        }//end for
        if(!found) {
            System.out.println("didn't find the button, there was an error "); System.exit(-1);
        }
        Component temporaryLostComponent = null;
        if (b[row][column].getBackground() == Color.orange){
            return;
        }else if (mines[row+1][column+1] == 1){
            JOptionPane.showMessageDialog(temporaryLostComponent, "You set off a Mine!!!!.");
                     
                  System.exit(0);
                       
            
        } else {
            tmp = Integer.toString(perm[row][column]);
            if (perm[row][column] == 0){
                    tmp = " ";
            }
            b[row][column].setText(tmp);
            b[row][column].setEnabled(false);
            checkifend();
            if (perm[row][column] == 0){
                scan(row, column);
                checkifend();
            }
        }
    }
 
    public void checkifend(){
        int check= 0;
        for (int y = 0; y<m;y++){
            for (int x = 0;x<n;x++){
        if (b[x][y].isEnabled()){
            check++;
        }
            }}
        if (check == nomines){
            endtime = System.nanoTime();
            Component temporaryLostComponent = null;
            
            JTextField nombreJugador = new JTextField(10);
            
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
        
        int resultado = selectorFichero.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
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
            
            tiempoGanador = (int)((endtime-starttime)/1000000000);
            
            bw.write("Nombre de Jugador:"+""+nombreDeJugador+" "+ "Tiempo Total:"+""+tiempoGanador+System.getProperty("line.separator"));


            bw.flush();
            bw.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
        
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
            break;
         }
         if(tiempos.length == 10 && tiempo < tiempos[i]){
            tiempos[i]= tiempo;
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
                      bw.write(" "+tiempos[i]+ System.getProperty("line.separator"));
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
    //Metodo que reinicia la partida
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
        int minecount = 0;
        for (int x = 0;x<8;x++){
            if (mines[a+deltax[x]+1][y+deltay[x]+1] == 1){
                minecount++;
            }
        }
        return minecount;
    }
 
    
    private static void delaySegundo(){
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e){}
    }
    
    public void windowIconified(WindowEvent e){
 
    }
 
    public static void main(String[] args){

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
 
    public void mouseClicked(MouseEvent e) {
 
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
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            found =  false;
            Object current = e.getSource();
            for (int y = 0;y<m;y++){
                    for (int x = 0;x<n;x++){
                            JButton t = b[x][y];
                            if(t == current){
                                    row=x;column=y; found =true;
                            }
                    }//end inner for
            }//end for
            if(!found) {
                System.out.println("didn't find the button, there was an error "); System.exit(-1);
            }
            if ((guesses[row+1][column+1] == 0) && (b[row][column].isEnabled())){
                b[row][column].setText("x");
                guesses[row+1][column+1] = 1;
                b[row][column].setBackground(Color.orange);
                 contadorMinas--;
                 contador.setText("Minas:"+ contadorMinas+"");
                 
                } else if (guesses[row+1][column+1] == 1){
                b[row][column].setText("?");
                guesses[row+1][column+1] = 0;
                b[row][column].setBackground(null);
                 contadorMinas++;
                 contador.setText("Minas:"+ contadorMinas);
            }
        }
    }
 
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
    //Meter todas las variables en un fichero txt para "Guardar" la partida actual tal y como esta en el momento
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
   
  /* public boolean CargarPartida2(String rutaFichero) {
        try {
                FileInputStream fis = new FileInputStream(rutaFichero);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);
   
                Integer n = (Integer)ois.readInt();
                Integer m = (Integer)ois.readInt();
                Integer nomines = (Integer)ois.readInt();
                Integer contmines = (Integer)ois.readInt();
                String i = (String)ois.readObject();
                String tmp = (String)ois.readObject();
                Boolean found = (Boolean)ois.readBoolean();
                Integer row = (Integer)ois.readInt();
                Integer column = (Integer)ois.readInt();
                Integer tiempom = (Integer)ois.readInt();
                int[][] perm = (int[][])ois.readObject();
                int[][] guesses = (int[][])ois.readObject();
                int[][] mines = (int[][])ois.readObject();
                
               JButton[][] b = new JButton[n][m];
               
               for(int k = 0;k < n;k++){
                    for (int j = 0; j < m; j++){
                        b[k][j] = new JButton((String)ois.readObject());
                    }
                }
            new Buscaminas(n, m, nomines, contmines, i, tmp, found, row, column, tiempom,
                    perm, guesses, mines, b);
            
            ois.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }*/
   
}//end class