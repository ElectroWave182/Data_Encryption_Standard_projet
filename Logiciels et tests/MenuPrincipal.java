import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class MenuPrincipal extends JFrame
{

// Type des éléments
public JLabel titreGauche, titreDroite;
private JTextField saisieGauche, saisieDroite;
private JButton validerGauche, validerDroite;
public JLabel resultatGauche, resultatDroite;
private JButton exporterGauche, importerDroite;


// Constructeur
public MenuPrincipal ()
{
	// Titre
	super ("Cryptage");

	// Réinitialisation des dossiers de stockage
	new File ("datas").mkdir ();
    new File ("temp").mkdir ();
	new File ("temp/export_key.txt").delete ();
    new File ("temp/import_key.txt").delete ();

	// Paramètres de la fenêtre
	this.setSize (600, 500);
	this.setResizable (false);

	this.setLayout (new BorderLayout ());

	// Côté encryptage
	JPanel gauche = new JPanel (new GridLayout (6, 1));

	this.titreGauche = new JLabel (" Entrez ci-dessous votre texte à encrypter... ");
	this.saisieGauche = new JTextField ();
	this.resultatGauche = new JLabel ("");

	this.validerGauche = new JButton ("Valider l'encryptage");
	this.validerGauche.addMouseListener (new EcouteurBouton ("encrypter", this, this.saisieGauche, this.resultatGauche));
	this.exporterGauche = new JButton ("Exporter la clef");
	this.exporterGauche.addMouseListener (new EcouteurBouton ("menuExporter", this));

	gauche.add (titreGauche);
	gauche.add (saisieGauche);
	gauche.add (validerGauche);
	gauche.add (resultatGauche);
	gauche.add (new JLabel ("")); // espace
	gauche.add (exporterGauche);

	// Côté décryptage
	JPanel droite = new JPanel (new GridLayout (6, 1));

	this.titreDroite = new JLabel (" Entrez ci-dessous votre code à décrypter... ");
	this.saisieDroite = new JTextField ();
	this.resultatDroite = new JLabel ("");

	this.validerDroite = new JButton ("Valider le décryptage");
	this.validerDroite.addMouseListener (new EcouteurBouton ("decrypter", this, this.saisieDroite, this.resultatDroite));
	this.importerDroite = new JButton ("Importer une clef");
	this.importerDroite.addMouseListener (new EcouteurBouton ("menuImporter", this));

	droite.add (titreDroite);
	droite.add (saisieDroite);
	droite.add (validerDroite);
	droite.add (resultatDroite);
	droite.add (new JLabel (""));
	droite.add (importerDroite);

	// Ajout des 2 colonnes à l'interface
	this.add (gauche, BorderLayout.WEST);
	this.add (droite, BorderLayout.EAST);

	this.setVisible (true);
}


public static void main (String[] args)
{
	MenuPrincipal fenetre = new MenuPrincipal ();
}

}
