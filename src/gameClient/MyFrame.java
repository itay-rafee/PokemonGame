package gameClient;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import org.json.JSONException;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _ind;
	private Arena _ar;

	private Image dbI;
	private Graphics dbG;

	private gameClient.util.Range2Range _w2f;
    //Image pika = Toolkit.getDefaultToolkit().getImage("C:\\\\Users\\\\Almog\\\\Documents\\\\Ex2\\\\Ex2\\\\pika.png");
    Image pika = Toolkit.getDefaultToolkit().getImage("images\\pika.png");
    Image bulbasaur = Toolkit.getDefaultToolkit().getImage("images\\bulbasaur.png");
    Image ash = Toolkit.getDefaultToolkit().getImage("images\\ash.png");
    Image field = Toolkit.getDefaultToolkit().getImage("images\\field.png");
    Image pokedex = Toolkit.getDefaultToolkit().getImage("images\\pokedex.png");

    MyFrame(String a) {
		super(a);
		//super();
		int _ind = 0;
	}
	
	
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
		//repaint();
	}
	
	private void updateFrame() {
		// this two lines for open the jframe window in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		//
		Range rx = new Range(20,this.getWidth()-10);
		Range ry = new Range(this.getHeight()-10,150); // this.getHeight()-10,150
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
		//Toolkit.getDefaultToolkit().sync();
	}


	public void paint(Graphics g){
		dbI = createImage(getWidth(),getHeight());
		dbG = dbI.getGraphics();
		paintG(dbG);
		g.drawImage(dbI,0,0,this);
	}

	public void paintG(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		//	updateFrame();
		drawGraph(g);
		drawPokemons(g);
		drawAgants(g);
		drawInfo(g);
		
	}

	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}
		
	}
	
	private void aspectRatio(double aspectratio) {
		final Frame _frame = this;
		if (aspectratio>10/7.0) {
			SwingUtilities.invokeLater(new Runnable(){
			    public void run() {
			      _frame.setSize(_frame.getHeight()*10/7, _frame.getHeight());
			    }
			  });
		} else if (aspectratio<10/7.0) {
			SwingUtilities.invokeLater(new Runnable(){
			    public void run() {
			      _frame.setSize(_frame.getWidth(), _frame.getWidth()*7/10);
			    }
			  });
		}
	}
	
	private void drawGraph(Graphics g) {
		double aspectratio = this.getWidth()/this.getHeight();
		if (aspectratio!=10/7.0) {aspectRatio(aspectratio);}
		
		g.drawImage(field, 0, 0, this.getWidth(), this.getHeight(), this); 
		directed_weighted_graph gg = _ar.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.blue);
			drawNode(n,5,g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while(itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
        try {
        	int w = getWidth();
            int h = getHeight();
			g.drawImage(pokedex, (2*25)*w/1400, (2*25)*h/74, w/4, h/4+20, this);
        	g.setFont(new Font("default", Font.BOLD, h/50));
    	    g.setColor(Color.WHITE);
			g.drawString("Grade: "+_ar.getGrade(), (2*25)*w/845, (2*25)*h/64);
			g.drawString("Moves: "+_ar.getMoves(), (2*25)*w/845, (2*25)*h/62);
			g.drawString("Time: "+_ar.getTime(), (2*25)*w/845, (2*25)*h/60);
			g.setColor(Color.BLACK);
			List<CL_Agent> ags = _ar.getAgents();
			for (int i = 0; i < ags.size() && i < 3 ; i++) {
				g.drawString("A"+i+" Val: "+ags.get(i).getValue(), (2*25)*w/265, (2*25)*h/(64-2*i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> fs = _ar.getPokemons();
		if(fs!=null) {
		Iterator<CL_Pokemon> itr = fs.iterator();
		while(itr.hasNext()) {
			CL_Pokemon f = itr.next();
			Point3D c = f.getLocation();
			int r=25;
			g.setColor(Color.green);
			//if(f.getType()<0) {g.setColor(Color.orange);}
			int w = getWidth();
	        int h = getHeight();
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				if(f.getType()<0) {
					g.drawImage(pika, ((int)fp.x()-r)*w/1010, ((int)fp.y()-r)*h/1010, (2*r)*w/1010, (2*r)*h/1010, this);
				} else {
					g.drawImage(bulbasaur, ((int)fp.x()-r)*w/1010, ((int)fp.y()-r)*h/1010, (2*r)*w/1010, (2*r)*h/1010, this);
				}
				//g.drawImage(pika, ((int)fp.x()-r)*w/1000, ((int)fp.y()-r)*h/1000, (2*r)*w/1000, (2*r)*h/1000, this);
				//g.fillOval(((int)fp.x()-r)*w/1000, ((int)fp.y()-r)*h/1000, (2*r)*w/1000, (2*r)*h/1000);
			//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
				
			}
		}
		}
	}
	private void drawAgants(Graphics g) {
		List<CL_Agent> rs = _ar.getAgents();
	//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.red);
		int i=0;
		int w = getWidth();
        int h = getHeight();
		while(rs!=null && i<rs.size()) {
			geo_location c = rs.get(i).getLocation();
			int r=25;
			i++;
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				g.drawImage(ash, ((int)fp.x()-r)*w/1010, ((int)fp.y()-r)*h/1010, (2*r)*w/1010, (2*r)*h/1010, this);
				//g.fillOval(((int)fp.x()-r)*w/1015, ((int)fp.y()-r)*h/1015, (2*r)*w/1015, (2*r)*h/1015);
			}
		}
	}
	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this._w2f.world2frame(pos);
		int w = getWidth();
        int h = getHeight();
        g.setColor(Color.blue);
		g.fillOval(((int)fp.x()-r)*w/1010, ((int)fp.y()-r)*h/1010, (2*r)*w/1010, (2*r)*h/1010);
		g.setColor(Color.BLUE);
		g.drawString(""+n.getKey(), ((int)fp.x())*w/1010, ((int)fp.y()-4*r)*h/1010);
	}
	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
		int w = getWidth();
        int h = getHeight();
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawLine((int)s0.x()*w/1010, (int)s0.y()*h/1010, (int)d0.x()*w/1010, (int)d0.y()*h/1010);
        
		//g.drawLine((int)s0.x()*w/1000, (int)s0.y()*h/1000, (int)d0.x()*w/1000, (int)d0.y()*h/1000);
		//g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}
}
