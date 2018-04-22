package buscaminas;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

 
import javax.swing.*;
public class Buscaminas extends JFrame implements ActionListener, MouseListener{
    static String dificultadV;
    JComboBox dificultadBox;
    JDialog dificultadPanel;
    JLabel dificultadLabel;
    JLabel CargarLabel;
    JButton Botonjugar;
    int[] tiempos = {0,0,0,0,0,0,0,0,0,0};
    int[] tiemposI = {0,0,0,0,0,0,0,0,0,0};
    int[] tiemposE = {0,0,0,0,0,0,0,0,0,0};
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
    int deltax[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    int deltay[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    double starttime;
    double endtime;
    public Buscaminas(String dificultad){
        
        if("PRINCIPIANTE".equals(dificultad)){
            n = 10;
            m = 10;
            nomines = 10;
        }
        else 
          if("INTERMEDIO".equals(dificultad)){
            n =16;
            m = 16;
            nomines = 40;
        }
         else 
          if("EXPERTO".equals(dificultad)){
            n =32;
            m = 16;
            nomines = 99;
        }
        
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
            GuardarTiempo();
            JDialog GuardarPanel = new JDialog();
             JLabel GuardarLabel = new JLabel("¿Quieres guardar la partida?");
             JButton GuardarBoton = new JButton("Guardar");
             JButton NoGuardarBoton = new JButton("No guardar");
             JLabel CargarLabel = new JLabel("¿Quieres cargar la partida?");
             JButton CargarBoton = new JButton("Cargar");
             
              GuardarBoton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionGuardar();
                               
			}
		});
               NoGuardarBoton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               System.exit(0);
			}
		});
        
                CargarBoton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionCargar();
                               
			}
		});
                
        GuardarPanel = new JDialog();
		GuardarPanel.getContentPane().setLayout(new FlowLayout());
                GuardarPanel.getContentPane().add(GuardarLabel); 
		GuardarPanel.getContentPane().add(GuardarBoton);
                GuardarPanel.getContentPane().add(NoGuardarBoton);
                GuardarPanel.getContentPane().add(CargarLabel);
                GuardarPanel.getContentPane().add(CargarBoton);
		GuardarPanel.pack();
		GuardarPanel.setVisible(true);
            
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
            
            JTextField nombreJugador = new JTextField();
            
            
            JOptionPane.showMessageDialog(temporaryLostComponent, "Congratulations you won!!! It took you "+(int)((endtime-starttime)/1000000000)+" seconds!" + "Nombre De Jugador");
            
            
             JDialog GuardarPanel = new JDialog();
             JLabel GuardarLabel = new JLabel("¿Quieres guardar la partida?");
             JButton GuardarBoton = new JButton("Guardar");
             JButton NoGuardarBoton = new JButton("No guardar");
             JLabel CargarLabel = new JLabel("¿Quieres cargar la partida?");
             JButton CargarBoton = new JButton("Cargar");
              GuardarBoton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionGuardar();
                               
			}
		});
               NoGuardarBoton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               System.exit(0);
			}
		});
                CargarBoton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                               AccionCargar();
                               
			}
		});
        
        GuardarPanel = new JDialog();
		GuardarPanel.getContentPane().setLayout(new FlowLayout());
                GuardarPanel.getContentPane().add(GuardarLabel); 
		GuardarPanel.getContentPane().add(GuardarBoton);
                GuardarPanel.getContentPane().add(NoGuardarBoton);
                GuardarPanel.getContentPane().add(CargarLabel);
                GuardarPanel.getContentPane().add(CargarBoton);
		GuardarPanel.pack();
		GuardarPanel.setVisible(true);
        
        }
    }

    public void GuardarTiempo(){
        
  
        int tiempo = (int)((endtime-starttime)/1000000000);
switch (dificultadV) {
      case "PRINCIPIANTE":
        for(int i=0; i<10; i++){
	 if(tiempos[i] == 0){
            tiempos[i]= tiempo;
         }
         if(tiempo < tiempos[i]){
            tiempos[i]= tiempo;

         }
          }
        System.out.println("Tu tiempo se ha guardado");
        System.out.println(Arrays.toString(tiempos));
           break;
      case "INTERMEDIO":
          for(int i=0; i<10; i++){
	 if(tiemposI[i] == 0){
            tiemposI[i]= tiempo;
         }
         if(tiempo < tiemposI[i]){
            tiemposI[i]= tiempo;
         }
          }
          System.out.println("Tu tiempo se ha guardado");
          System.out.println(Arrays.toString(tiemposI));
           break;
      case "EXPERTO":
          for(int i=0; i<10; i++){
	 if(tiemposE[i] == 0){
            tiemposE[i]= tiempo;

         }
         if(tiempo < tiemposE[i]){
            tiemposE[i]= tiempo;
         }
          }
          System.out.println("Tu tiempo se ha guardado");
          System.out.println(Arrays.toString(tiemposE));
           break;
      default:
           System.out.println("Tu tiempo no esta entre los 10 mejores. No se puede guardar");
           break;
      }
        
        
	}
    
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
        }
    }
    
    public void ReiniciarPartida(){
        new Buscaminas((String) dificultadBox.getSelectedItem());
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
 
    public void windowIconified(WindowEvent e){
 
    }
 
    public static void main(String[] args){
        
        JDialog dificultadPanel = new JDialog();
        JLabel dificultadLabel = new JLabel("Escoge la dificultad del nuevo buscaminas");
        JLabel CargarLabel = new JLabel("Cargar partida guardada");
        JComboBox dificultadBox = new JComboBox();
		dificultadBox.addItem("PRINCIPIANTE");
		dificultadBox.addItem("INTERMEDIO");
		dificultadBox.addItem("EXPERTO");
        
        JButton Botonjugar = new JButton("Jugar");
        JButton BotonCargar = new JButton("Cargar"); 
        Botonjugar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            String dificultad = (String) dificultadBox.getSelectedItem();
                            dificultadV = dificultad;
				new Buscaminas(dificultadV);
			}
		});
        
        BotonCargar.addActionListener(new ActionListener(){
                     @Override
                      public void actionPerformed(ActionEvent e) {
                         
			
			}
		});
        dificultadPanel = new JDialog();
		dificultadPanel.getContentPane().setLayout(new FlowLayout());
                dificultadPanel.getContentPane().add(dificultadLabel);
		dificultadPanel.getContentPane().add(dificultadBox);
                dificultadPanel.getContentPane().add(Botonjugar);
                dificultadPanel.getContentPane().add(CargarLabel);
                dificultadPanel.getContentPane().add(BotonCargar);
		dificultadPanel.pack();
		dificultadPanel.setVisible(true);

        
        
        
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
            } else if (guesses[row+1][column+1] == 1){
                b[row][column].setText("?");
                guesses[row+1][column+1] = 0;
                b[row][column].setBackground(null);
            }
        }
    }
 
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
 
   public boolean GuardarPartida(String rutaFichero) {
        try {
            FileOutputStream fos = new FileOutputStream(rutaFichero);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            oos.writeObject(this.n);
            oos.writeObject(this.m);
            oos.writeObject(this.nomines);
            oos.writeObject(this.mines);
            oos.writeObject(this.allmines);
            oos.writeObject(this.deltax);
            oos.writeObject(this.deltay);
            oos.writeObject(this.starttime);
            oos.writeObject(this.endtime);
            oos.writeObject(this.guesses);
            oos.writeObject(this.b);
            oos.writeObject(this.column);
            oos.writeObject(this.row);
            oos.writeObject(this.perm);
            oos.writeObject(this.tmp);
            oos.writeObject(this.found);

            
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
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
            this.b = (JButton[][])ois.readObject();
            this.column = (int)ois.readObject();
            this.row = (int)ois.readObject();
            this.perm = (int[][])ois.readObject();
            this.tmp = (String)ois.readObject();
            this.found = (boolean)ois.readObject();
            
            
            ois.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
   
}//end class
