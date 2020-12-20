package gameClient;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import javax.swing.*;
import org.json.JSONException;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 */
public class MyFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	/*private int _ind;*/
	private Arena _ar;
	private Image dbI;
	private Graphics dbG;

	private gameClient.util.Range2Range _w2f;
    Image pika = new ImageIcon(getClass().getResource("/images/pika.png")).getImage();
    Image bulbasaur = new ImageIcon(getClass().getResource("/images/bulbasaur.png")).getImage();
    Image ash = new ImageIcon(getClass().getResource("/images/ash.png")).getImage();
    Image field = new ImageIcon(getClass().getResource("/images/field.png")).getImage();
	Image pokedex = new ImageIcon(getClass().getResource("/images/pokedex.png")).getImage();
	
	/* Constructor */
    MyFrame(String a) {
		super(a);
		//super();
		/*int _ind = 0;*/
	}
	
    /**
	 * About update(Arena ar) method:
	 * The method init this._ar to the input Arena object
	 * and calls updateFrame() method
	 */
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
		//repaint();
	}
	
	/**
	 * About updateFrame() method:
	 * The method update the frame while it opens
	 */
	private void updateFrame() {
		// this two lines for open the jframe window in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		Range rx = new Range(20,this.getWidth()-10);
		Range ry = new Range(this.getHeight()-10,150); // this.getHeight()-10,150
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
		//Toolkit.getDefaultToolkit().sync();
	}
	
	/**
	 * About paint(Graphics g) method:
	 * The method paint the frame using double buffer
	 * @param g
	 */
	public void paint(Graphics g) {
		dbI = createImage(getWidth(),getHeight());
		dbG = dbI.getGraphics();
		paintG(dbG);
		g.drawImage(dbI,0,0,this);
	}
	
	/**
	 * About paintG(Graphics g) method:
	 * The method paint the frame by calling draw objects methods
	 * @param g
	 */
	public void paintG(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		// updateFrame();
		drawGraph(g);
		drawPokemons(g);
		drawAgants(g);
		drawInfo(g);
	}
	
	/**
	 * About drawInfo(Graphics g) method:
	 * The method adjusts the info string according to the resizing of the window
	 * @param g
	 */
	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}
	}
	
	/**
	 * About aspectRatio(double aspectratio) method:
	 * The method adjusts the aspect ratio according to the input Graphics
	 * @param aspectratio
	 */
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
	
	/**
	 * About drawGraph(Graphics g) method:
	 * The method draw the graph according to the input Graphics
	 * @param g
	 */
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
	
	/**
	 * About drawPokemons(Graphics g) method:
	 * The method draw the pokemons according to the input Graphics
	 * @param g
	 */
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> fs = _ar.getPokemons();
		if(fs!=null) {
		Iterator<CL_Pokemon> itr = fs.iterator();
		while(itr.hasNext()) {
			CL_Pokemon f = itr.next();
			Point3D c = f.getLocation();
			int r=25;
			g.setColor(Color.green);
			int w = getWidth();
	        int h = getHeight();
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				if(f.getType()<0) {
					g.drawImage(pika, ((int)fp.x()-r)*w/1010, ((int)fp.y()-r)*h/1010, (2*r)*w/1010, (2*r)*h/1010, this);
				} else {
					g.drawImage(bulbasaur, ((int)fp.x()-r)*w/1010, ((int)fp.y()-r)*h/1010, (2*r)*w/1010, (2*r)*h/1010, this);
					}
				}
			}
		}
	}
	
	/**
	 * About drawAgants(Graphics g) method:
	 * The method draw the agants according to the input Graphics
	 * @param g
	 */
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
			}
		}
	}
	
	/**
	 * About drawNode(node_data n, int r, Graphics g) method:
	 * The method draw the nodes according to the input Graphics
	 * @param n
	 * @param r
	 * @param g
	 */
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
	
	/**
	 * About drawEdge(edge_data e, Graphics g) method:
	 * The method draw the edges according to the input Graphics
	 * @param e
	 * @param g
	 */
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
	}
}