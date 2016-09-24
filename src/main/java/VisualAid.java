import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;
public class VisualAid {
	FileInputStream level;
	FileInputStream output;
	private int levelCounter = 0;
	private String[] levelnames = {"level1.txt","level2.txt","level3.txt"};
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel difficulty = new JLabel();
	JLabel matched = new JLabel("NOT MATCHED");
	JLabel diffchar = new JLabel("Amount of different character: ");
	
	//Sorry about the ugly try-catches. I had no choice
	 public VisualAid() {
		 try {
			level= new FileInputStream("C:\\Users\\bunu\\Desktop\\"+levelnames[levelCounter]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		try {
			output = new FileInputStream("C:\\Users\\bunu\\Desktop\\output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 difficulty = new JLabel("Level "+ (levelCounter+1));
		 int diff = 0;
		 try{
			 diff = difference(level,output);
		 } catch(IOException e){}
		 diffchar = new JLabel("Amount of different character: " + diff);
		 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 panel.add(matched);
	     panel.add(diffchar);
	     panel.add(difficulty);
	     frame.add(panel);
	     frame.pack();
	     frame.setVisible(true);
	 }
	 private int difference(FileInputStream file1, FileInputStream file2) throws IOException{
		 System.out.println("hit2");
		 FileInputStream smaller = file1;
		 FileInputStream bigger = file2;
		 int counter = 0;
		 if(file1.getChannel().size()>file2.getChannel().size()){
			 smaller = file2;
			 bigger = file1;
		 }
		
		 int c;
		 int d;
		 while ((c = smaller.read()) != -1 && (d=bigger.read()) != -1) {
             if(c != d){
			 	counter++;
             }
         }
		 
		 System.out.println(bigger.getChannel().size()-smaller.getChannel().size());
		 counter+=bigger.getChannel().size() - smaller.getChannel().size();
		 System.out.println("counter: "+counter);
		 
		 return counter;
	 }
	 
	 public void update(){
		 int difference=0;
		 try {
			level = new FileInputStream("C:\\Users\\bunu\\Desktop\\"+levelnames[levelCounter]);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 try {
			output = new FileInputStream("C:\\Users\\bunu\\Desktop\\output.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			difference = difference(level,output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 if(difference == 0){
			 matched.setText("Matched");
			 diffchar.setText("Amount of different character: 0");
			 if(levelCounter + 1 < levelnames.length){
				 levelCounter++;
				 matched.setText("Not Matched");
				 difficulty.setText("Level "+(levelCounter+1));
			 }
		 }
		 else{
			 diffchar.setText("Amount of different character: "+difference);
		 }
		 
	 }
}
