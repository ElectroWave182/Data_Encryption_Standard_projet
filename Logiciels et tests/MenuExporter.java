import java.awt.*;
import java.io.File;
import javax.swing.*;


public class MenuExporter extends JFrame
{

// Type des éléments
public JLabel titre;
private JTextField saisie;
private JButton valider;


// Constructeur
public MenuExporter ()
{
	// Titre
	super ("Exporter");

	// Paramètres de la fenêtre
	this.setSize (300, 300);
	this.setResizable (false);

	this.setLayout (new BorderLayout ());

	JPanel colonne = new JPanel (new GridLayout (3, 1));

	this.titre = new JLabel (" Choisissez ci-dessous un nom de fichier... ");
	this.saisie = new JTextField ();

    this.valider = new JButton ("Enregistrer");
	this.valider.addMouseListener (new EcouteurBouton ("exporter", this, this.saisie));

	colonne.add (titre);
	colonne.add (saisie);
	colonne.add (valider);

	// Ajout des 2 colonnes à l'interface
	this.add (colonne, BorderLayout.CENTER);

	this.setVisible (true);
}


public static void main (String[] args)
{
	MenuExporter fenetre = new MenuExporter ();
}

}
